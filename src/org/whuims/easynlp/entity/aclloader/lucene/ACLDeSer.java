package org.whuims.easynlp.entity.aclloader.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.whuims.easynlp.entity.commonentity.Paragraph;
import org.whuims.easynlp.entity.commonentity.Section;
import org.whuims.easynlp.entity.commonentity.Sentence;
import org.whuims.easynlp.entity.commonentity.StructuredPaper;
import org.whuims.easynlp.phrase.PhraseTokenizer;

public class ACLDeSer {
	private String filePath;

	public ACLDeSer(String filePath) {
		super();
		this.filePath = filePath;
	}

	public StructuredPaper deSer() throws IOException {
		StructuredPaper paper = SerializationUtils
				.deserialize(new FileInputStream(this.filePath));
		return paper;
	}
	public static void main(String[] args) {
		String indexPath = "f:/Expriment/data/acl/AclSentPorterIndexabc/";
		Directory indexDir;
		IndexWriter writer = null;
		try {
			indexDir = FSDirectory.open(new File(indexPath));
			Analyzer analyzer = new PorterStandardAnalyzer(
					Version.LUCENE_4_10_0);
			IndexWriterConfig iwc = new IndexWriterConfig(
					Version.LUCENE_4_10_0, analyzer);
			writer = new IndexWriter(indexDir, iwc);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String dirPath = "f:/Experiment/Data/ACL/ACLXmlSer";
		File dir = new File(dirPath);
		int size = 0;
		for (File file : dir.listFiles()) {
			if (size++ % 100 == 0) {
				System.out.println(size);
			}
			String filePath = file.getAbsolutePath();
			ACLDeSer de = new ACLDeSer(filePath);
			StructuredPaper paper = null;
			try {
				paper = de.deSer();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (paper.getAbstract() != null)
				paper.getSections().add(0, paper.getAbstract());
			int count = 0;
			StringBuilder sb = new StringBuilder();
			for (Section section : paper.getSections()) {
				for (Paragraph para : section.getParagraphs()) {
					for (Sentence sent : para.getSentences()) {
						Document doc = new Document();
						Field oriTextField = new TextField("oriText", sent
								.getText().replace("-LRB-", "(")
								.replace("-RRB-", ")"), Field.Store.YES);
						Field oriPostagsField = new TextField("oriPostags",
								sent.getPostag(), Field.Store.YES);
						doc.add(oriTextField);
						doc.add(oriPostagsField);

						Sentence phrasedSent = PhraseTokenizer.createInstance()
								.tokenize(sent);
						Field pathField = new StringField("path",
								file.getAbsolutePath(), Field.Store.YES);
						Field textField = new TextField("text", phrasedSent
								.getText().replace("-LRB-", "(")
								.replace("-RRB-", ")").replace("-LSB-", "[").replace("-RSB-", "]"), Field.Store.YES);
						Field postagField = new TextField("postags",
								phrasedSent.getPostag(), Field.Store.YES);
						Field offsetField = new LongField("offset", count,
								Field.Store.YES);
						doc.add(pathField);
						doc.add(offsetField);
						doc.add(textField);
						doc.add(postagField);
						sb.append(sent.getText());
						try {
							writer.addDocument(doc);
						} catch (IOException e) {
							e.printStackTrace();
						}
						count++;
					}
				}
			}
			sb.append("\r\n");
			try {
				FileUtils.write(new File("d://sent.txt"), sb.toString()
						.toLowerCase(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
