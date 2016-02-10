package org.whuims.easynlp.entity.acmloader.lucene;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.whuims.acm.db.Mysql;
import org.whuims.easynlp.entity.aclloader.lucene.PorterStandardAnalyzer;
import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Sentence;
import org.whuims.easynlp.parserwrapper.ShallowParser;

public class ACMLuceneIndexBuilder {
	String filePath = "";
	IndexWriter sentWriter = null;
	Analyzer analyzer = new PorterStandardAnalyzer(CharArraySet.EMPTY_SET);
//	Analyzer analyzer = new WhitespaceAnalyzer();

	public ACMLuceneIndexBuilder(String filePath) {
		super();
		this.filePath = filePath;
		init();
	}

	public void init() {
		initIndexWriter(this.filePath);
		// initIndexWriter(docWriter, "f:/Expriment/data/acm/docPorterIndex/");
	}

	public void delete() throws SQLException, IOException {
		Connection conn = Mysql.getConn("semsearch");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select id from acm_paper where duplicate=2");
		while (rs.next()) {
			Long id = rs.getLong("id");
			System.out.println(id);
			Term term = new Term("id", id + "");
			this.sentWriter.deleteDocuments(term);
			this.sentWriter.commit();
		}
		this.sentWriter.close();
	}

	public void index() {
		try {
			Connection conn = Mysql.getConn("semsearch");
			Statement stmt;
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from acm_paper where duplicate is null");
			int count = 1;
			while (rs.next()) {
				System.out.println(count++);
				long id = rs.getLong("id");
				String title = rs.getString("title");
				if (title == null)
					title = "";
				String summary = rs.getString("summary");
				if (summary == null)
					summary = "";
				List<Sentence> sents = new ArrayList<Sentence>();
				sents.add(PaperUtils.sentProductOf(title));
				sents.addAll(PaperUtils.paraProductOf(summary).getSentences());
				for (Sentence sent : sents) {
					// sent = PhraseTokenizer.createInstance().tokenize(sent);
					sent.setLattices(ShallowParser.createInstance().generateLattices(sent.getTokenArray(),
							sent.getPostagArray()));
				}
				indexSents(id, sents);
			}
			analyzer.close();
			sentWriter.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void indexSents(long id, List<Sentence> sents) throws IOException {
		for (int i = 0; i < sents.size(); i++) {
			Sentence sent = sents.get(i);
			Document doc = new Document();
			Field idField = new LongField("id", id, Field.Store.YES);
			Field offsetField = new IntField("offset", i, Field.Store.YES);
			Field textField = new TextField("text", sent.getText(), Field.Store.YES);
			Field posField = new TextField("postag", sent.getPostag(), Field.Store.YES);
			Field latticeField = new TextField("lattice", sent.getLattices(), Field.Store.YES);
			doc.add(idField);
			doc.add(offsetField);
			doc.add(textField);
			doc.add(posField);
			doc.add(latticeField);
			sentWriter.addDocument(doc);
		}

	}

	public void initIndexWriter(String indexPath) {
		if (!new File(indexPath).exists()) {
			new File(indexPath).mkdirs();
		}
		Directory indexDir;
		try {
			indexDir = FSDirectory.open(new File(indexPath));
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);
			this.sentWriter = new IndexWriter(indexDir, iwc);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private int printDocSize() {
		System.out.println(this.sentWriter.maxDoc());
		System.out.println(this.sentWriter.numDocs());
		return this.sentWriter.maxDoc();
	}

	public static void main(String args[]) {
		ACMLuceneIndexBuilder builder = new ACMLuceneIndexBuilder(args[0]);
		builder.index();
	}

}
