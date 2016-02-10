package org.whuims.easynlp.entity.aclloader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.whuims.easynlp.entity.commonentity.Paper;
import org.whuims.easynlp.entity.commonentity.PaperLoader;
import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Paragraph;
import org.whuims.easynlp.entity.commonentity.Section;
import org.whuims.easynlp.entity.commonentity.Sentence;
import org.whuims.easynlp.entity.commonentity.StructuredPaper;
import org.whuims.easynlp.phrase.PhraseDictionary;
import org.whuims.easynlp.phrase.PhraseTokenizer;

/**
 * 用来加载ACL论文的xml格式文档。
 * 
 * @author Qikai
 *
 */
public class ACLPaperLoader implements PaperLoader {
	private String filePath;

	public ACLPaperLoader(String filePath) {
		super();
		this.filePath = filePath;
	}

	@Override
	public Paper loadPaper() throws DocumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StructuredPaper load() throws DocumentException {
		StructuredPaper paper = new StructuredPaper();
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(this.filePath));
		Element root = document.getRootElement();
		int deepth = 1;
		for (Iterator<Element> i = root.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			if (element.getName().trim().equals("Title")) {
				paper.setTitle(PaperUtils.sentProductOf(element.getTextTrim()));
				paper.setTitleRawText(element.getTextTrim());
			}
			if (element.getName().trim().equals("Section")) {
				scanContentData(element, paper, document, deepth);
			}
		}
		for (int i = 0; i < paper.getSections().size(); i++) {
			Section section = paper.getSections().get(i);
			StringBuilder sb = new StringBuilder();
			if (section.getTitleStr() != null
					&& section.getTitleStr().trim().toLowerCase()
							.matches("\\d*[\\.*\\d*]*\\s*abstract")) {
				Section abSection = new Section();
				abSection.getParagraphs().add(section.getParagraphs().get(0));
				section.getParagraphs().remove(0);
				paper.setAbstract(abSection);
				paper.setAbstractRawText(section.getRawText());
			}
		}
		return paper;
	}

	private void scanContentData(Element root, StructuredPaper paper,
			Document document, int deepth) {
		Section section = new Section();
		for (Iterator<Element> i = root.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			String name = element.getName();
			if (name.equals("Section")) {
				if (section.getParagraphs().size() > 0) {
					paper.addSection(section);
				}
				section = new Section();
				scanContentData(element, paper, document, deepth + 1);
			} else {
				if (name.equals("SectionTitle")) {
					section.setTitleStr(element.getTextTrim());
					section.setTitle(PaperUtils.sentProductOf(section
							.getTitleStr()));
				} else if (name.equals("Paragraph")) {
					section.getParagraphs().add(
							PaperUtils.paraProductOf(element.getTextTrim()));
				}
			}
		}
		if (section.getParagraphs().size() > 0) {
			paper.addSection(section);
		}
	}

	public static void main(String[] args) {
		TecTermCal tc = new TecTermCal();
		PaperTermCal pc = new PaperTermCal();
		try {
			tc.load();
			pc.load();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PhraseDictionary dict = new PhraseDictionary();
		// for (String termID : pc.getTerms("A00-1034")) {
		// String term = tc.getTermString(termID).toLowerCase();
		// dict.addPhrase(term.split("\\s+"), "NNP", 1);
		// }
		for (String term : tc.getTermList()) {
			term = term.toLowerCase();
			dict.addPhrase(term.split("\\s+"), "NNP", 1);
		}

		String filePath = "F:\\Experiment\\Data\\ACL\\ACLXml\\00\\A00-1034_cln.xml";

		ACLPaperLoader loader = new ACLPaperLoader(filePath);
		try {
			StructuredPaper paper = loader.load();
			System.out.println(paper.getTitleText());
			paper.getSections().add(0, paper.getAbstract());
			for (Section section : paper.getSections()) {
				for (Paragraph para : section.getParagraphs()) {
					for (Sentence sent : para.getSentences()) {
						// System.out.println(sent.getText());
						PhraseTokenizer tokenizer = new PhraseTokenizer(dict);
						sent = tokenizer.tokenize(sent);
						System.out.println(sent.getRawText()
								.replaceAll("<.+?>", "").replace("\\ [", "[")
								.replace("\\ ]", "]").replaceAll("\\s+", " "));
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

}
