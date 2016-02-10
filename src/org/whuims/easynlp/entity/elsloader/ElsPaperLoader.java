package org.whuims.easynlp.entity.elsloader;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.whuims.easynlp.entity.commonentity.Paper;
import org.whuims.easynlp.entity.commonentity.PaperLoader;
import org.whuims.easynlp.entity.commonentity.PaperUtils;
import org.whuims.easynlp.entity.commonentity.Paragraph;
import org.whuims.easynlp.entity.commonentity.Section;
import org.whuims.easynlp.entity.commonentity.StructuredPaper;

public class ElsPaperLoader implements PaperLoader {
    String filePath;

    public ElsPaperLoader(String filePath) {
        super();
        this.filePath = filePath;
    }

    /* (non-Javadoc)
     * @see org.whuims.easynlp.entity.elsloader.PaperLoader#loadPaper()
     */
    @Override
    public Paper loadPaper() throws DocumentException {
        return load();
    }

    /* (non-Javadoc)
     * @see org.whuims.easynlp.entity.elsloader.PaperLoader#load()
     */
    @Override
    public StructuredPaper load() throws DocumentException {
        StructuredPaper paper = new StructuredPaper();
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(this.filePath));
        Element root = document.getRootElement();
        for (Iterator<Element> i = root.elementIterator(); i.hasNext();) {
            Element element = (Element) i.next();
            if (element.getName().trim().equals("metadata")) {
                scanMetaData(element, paper, document);
            }
            if (element.getName().trim().equals("content")) {
                scanContentData(element, paper, document);
            }
        }
        return paper;
    }

    private void scanContentData(Element root, StructuredPaper paper,
            Document document) {
        // 提取关键词
        List<? extends Node> list = document.selectNodes("//keyword");
        for (Node node : list) {
            Element e = (Element) node;
            paper.getKeywords().add(e.getTextTrim());
        }
        List<? extends Node> sumList = document.selectNodes("//abstract/p");
        StringBuilder sb = new StringBuilder();
        Section abSection = new Section();
        for (Node node : sumList) {
            Element e = (Element) node;
            String text = e.getTextTrim();
            sb.append(text).append("\n");
            abSection.getParagraphs().add(PaperUtils.paraProductOf(text));
        }
        paper.setAbstractRawText(sb.toString().trim());
        paper.setAbstract(abSection);

        // 遍历
        for (Iterator<Element> i = root.elementIterator(); i.hasNext();) {
            Element element = (Element) i.next();
            String nodeName = element.getName().trim();
            String nodeText = element.getText().trim();
            if (nodeName.equals("content_end")) {
                break;
            }
            if (nodeName.equals("title")) {
                paper.setTitle(PaperUtils.sentProductOf(nodeText));
            }
            if (nodeName.equals("h2")) {
                if (element.attributeValue("id").startsWith("bib")) {
                    break;
                }
                Section section = new Section();
                section.setId(element.attributeValue("id"));
                section.setTitle(PaperUtils.sentProductOf(nodeText));
                section.setType(nodeName);
                paper.getSections().add(section);
            } else if (nodeName.equals("p") || nodeName.matches("h\\d+")) {
//                System.out.println();
//                System.out.println("--------------a------------");
//                System.out.println(nodeText);
//                System.out.println("--------------b------------");
//                System.out.println();
                String rawText = nodeText.replaceAll("<a href=\"#.+?\">", "");
                rawText = rawText.replace("</a>", "");
                rawText = rawText.replaceAll(
                        "\\{math_begin\\}.+?\\{math_end\\}", " FORMULAR ");
                System.out.println(rawText);
                Paragraph para = PaperUtils.paraProductOf(rawText);
                para.setId(element.attributeValue("id"));
                para.setTheClass(element.attributeValue("class"));
                if (paper.getSections().isEmpty()) {
                    paper.getSections().add(new Section());
                }
                paper.getSections().get(paper.getSections().size() - 1)
                        .getParagraphs().add(para);
            }
        }

    }

    private void scanMetaData(Element root, StructuredPaper paper,
            Document document) {
        // 提取affiliation数据
        Map<String, String> affMap = new HashMap<String, String>();
        List<? extends Node> list = document.selectNodes("//affiliation");
        for (Node node : list) {
            Element e = (Element) node;
            affMap.put(e.attributeValue("id"), e.getTextTrim());
        }
        // 提取作者列表
        List<? extends Node> authorLines = document
                .selectNodes("//authors/author");
        for (Node node : authorLines) {
            Element e = (Element) node;
            Author author = new Author();
            author.setAuthorID(e.attributeValue("authorid"));
            author.setBn(e.attributeValue("bn"));
            author.setFn(e.attributeValue("fn"));
            author.setEmail(e.attributeValue("mail"));
            author.setAffiliation(affMap.get(e.attributeValue("affhref")));
            author.setAuthorName(e.getTextTrim());
            paper.getAuthors().add(author.getAuthorName());
        }

        for (Iterator<Element> i = root.elementIterator(); i.hasNext();) {
            Element element = (Element) i.next();
            String nodeName = element.getName().trim();
            String nodeText = element.getTextTrim();
            if (nodeName.equals("doi")) {
                paper.setDoi(nodeText);
            } else if (nodeName.equals("pageTitle")) {
                //                paper.setPageTitle(nodeText);
            } else if (nodeName.equals("journal")) {
                paper.setJournalTitle(nodeText);
            } else if (nodeName.equals("volume")) {
                paper.setVolume(nodeText);
            } else if (nodeName.equals("date")) {
                paper.setDateStr(nodeText);
            }
        }
    }

    public static void main(String[] args) {
        String filePath = "g:\\930.xml";
        System.out.println(filePath);
        PaperLoader loader = new ElsPaperLoader(filePath);
        try {
            StructuredPaper paper = loader.load();
            System.out.println(paper.getText());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
