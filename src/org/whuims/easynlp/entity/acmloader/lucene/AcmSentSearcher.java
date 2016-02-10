package org.whuims.easynlp.entity.acmloader.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.whuims.easynlp.entity.aclloader.lucene.PorterStandardAnalyzer;
import org.whuims.easynlp.entity.commonentity.Sentence;

public class AcmSentSearcher {
	private static final String WHITESPACE_INDEX_PATH = "g:\\data\\ACM\\ACMSentPorterIndex";
	public static final String PORTER_INDEX_PATH = "g:\\data\\ACM\\ACMSentWhiteIndex";
	private static AcmSentSearcher instance = new AcmSentSearcher();
	private IndexSearcher porterSearcher = null;
	private IndexSearcher whiteSearcher = null;
	private Analyzer whiteSpaceAnalyzer = null;
	private Analyzer porterStandardAnalyzer = null;

	private AcmSentSearcher() {
		super();
		init();
	}

	private void init() {
		try {
			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(AcmSentSearcher.PORTER_INDEX_PATH)));
			System.out.println(reader.maxDoc());
			porterSearcher = new IndexSearcher(reader);
			IndexReader whiteReader = DirectoryReader
					.open(FSDirectory.open(new File(AcmSentSearcher.WHITESPACE_INDEX_PATH)));
			this.whiteSearcher = new IndexSearcher(whiteReader);
			// init Analyzer
			this.porterStandardAnalyzer = new PorterStandardAnalyzer();
			this.whiteSpaceAnalyzer = new WhitespaceAnalyzer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static AcmSentSearcher createInstance() {
		return instance;
	}

	public List<AcmSentenceDto> search(String queryLine, String field, boolean porterStemming) {
		List<AcmSentenceDto> results = new ArrayList<AcmSentenceDto>();
		List<Document> docs = null;
		try {
			docs = indexSearch(queryLine, field, porterStemming);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		if (docs == null)
			return results;
		for (Document doc : docs) {
			String idString = doc.get("id");
			String offsetString = doc.get("offset");
			int id = Integer.parseInt(idString);
			int offset = Integer.parseInt(offsetString);
			String text = doc.get("text");
			String postag = doc.get("postag");
			String lattice = doc.get("lattice");
			AcmSentenceDto dto = new AcmSentenceDto(id, offset, text, postag, lattice);
			results.add(dto);
		}
		return results;
	}

	private List<Document> indexSearch(String queryLine, String field, boolean porterStemming)
			throws ParseException, IOException {
		Analyzer analyzer = this.porterStandardAnalyzer;
		IndexSearcher searcher = this.porterSearcher;
		if (!porterStemming) {
			analyzer = this.whiteSpaceAnalyzer;
			searcher = this.whiteSearcher;
		}
		QueryParser parser = new QueryParser(field, analyzer);
		Query query = parser.parse(queryLine);

		TopDocs results = searcher.search(query, Integer.MAX_VALUE);
		ScoreDoc[] hits = results.scoreDocs;
		List<Document> docs = new ArrayList<Document>();
		for (ScoreDoc hit : hits) {
			docs.add(searcher.doc(hit.doc));
		}
		return docs;
	}

	public static void main(String[] args) {
		List<AcmSentenceDto> dtos = AcmSentSearcher.createInstance().search("is to the", "text", true);
		for(AcmSentenceDto dto:dtos){
			System.out.println(dto.getId()+"\t"+dto.getOffset()+"\t"+dto.getText());
		}
	}

}
