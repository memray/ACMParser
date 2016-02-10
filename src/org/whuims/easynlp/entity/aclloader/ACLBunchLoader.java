package org.whuims.easynlp.entity.aclloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.lang3.SerializationUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.whuims.easynlp.entity.commonentity.PaperBunch;
import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Section;
import org.whuims.easynlp.entity.commonentity.SimplePaper;
import org.whuims.easynlp.entity.commonentity.StructuredPaper;

public class ACLBunchLoader extends DirectoryWalker {
	int count = 0;
	int fileCount = 0;
	PaperBunch bunch = new PaperBunch("ACL_ANN", 20000);

	public PaperBunch load() {
		return bunch;
	}

	@Override
	protected void handleFile(File file, int depth, Collection results)
			throws IOException {
		if (!file.getAbsolutePath().endsWith(".xml")) {
			return;
		}
		System.out.println((this.fileCount++) + "\t" + file.getName());
		// loadFile(file);
		ACLPaperLoader loader = new ACLPaperLoader(file.getAbsolutePath());
		try {
			StructuredPaper paper = loader.load();
			String paperID=file.getName().replace(".xml", "");
			entityProcess(paper,paperID);
//			SerializationUtils.serialize(paper,
//					new FileOutputStream("H:\\Data\\ACL\\ACLXmlSer\\"
//							+ file.getName().replace(".xml", ".ser")));
			// this.bunch.addPaper(paper);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	private void entityProcess(StructuredPaper paper, String paperID) {
		
	}

	private void loadFile(File file) throws DocumentException {
		SimplePaper paper = new SimplePaper();
		SAXReader reader = new SAXReader();
		Document document = reader.read(file);
		Element root = document.getRootElement();
		String id = root.attribute("id").getValue();
		paper.setDoi(id);
		int count = 0;
		for (Iterator<Element> i = root.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			String nodeName = element.getName().trim();
			if (nodeName.trim().equals("Title")) {
				paper.setTitleRawText(element.getTextTrim());
				paper.setTitle(PaperUtils.sentProductOf(element.getTextTrim()
						.trim()));
			} else if (nodeName.equals("Section")) {
				if (count == 0)
					processSection(element, paper);
				count++;
			}
		}
		this.bunch.addPaper(paper);
	}

	private void processSection(Element element, SimplePaper paper) {
		StringBuilder sb = new StringBuilder();
		String sectionTitle = "";
		Section abSection = new Section();
		for (Iterator<Element> si = element.elementIterator(); si.hasNext();) {
			Element e = si.next();
			String nodeName = e.getName().trim();
			String nodeValue = e.getTextTrim();
			if (nodeName.equals("SectionTitle")) {
				sectionTitle = e.getTextTrim().trim().toLowerCase();
			} else if (nodeName.equals("Paragraph")) {
				if (sectionTitle.toLowerCase().trim().equals("abstract")) {
					abSection.getParagraphs().add(
							PaperUtils.paraProductOf(nodeValue));
					sb.append(nodeValue).append(" ");
					break;
				} else {
				}
			}
		}
		paper.setAbstractRawText(sb.toString().trim());
		paper.setAbstract(abSection);
	}

	public static void main(String[] args) {
		ACLBunchLoader loader = new ACLBunchLoader();
		File dir = new File("F:\\Experiment\\Data\\ACL\\ACLXml\\");
		try {
			loader.walk(dir, null);
			PaperBunch bunch = loader.load();
			// SerializationUtils.serialize(bunch, new FileOutputStream(
			// "H:\\Data\\ACL\\aclxml.ser"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
