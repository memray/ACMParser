package org.whuims.easynlp.dict.seedom;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Class SeedomDict.
 */
public class SeedomDict implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The domains. */
	private List<Domain> domains = new ArrayList<Domain>();

	/**
	 * The domain map. domainID到Domain的对应。 根据domainID可以知道domain的上下位类。
	 * 例如，某个domain的id为9.1，则上位类的id为9，所有下位类的id都已“9.1.”开头。
	 * 
	 */
	private Map<String, Domain> domainMap = new TreeMap<String, Domain>();

	/** The word dict. */
	private Map<String, List<Type>> wordDict = new HashMap<String, List<Type>>();

	/**
	 * Word count.
	 *
	 * @return the int
	 */
	public int wordCount() {
		return wordDict.size();
	}

	/**
	 * Word type.
	 *
	 * @param s
	 *            the s
	 * @return the list
	 */
	public List<Type> wordType(String s) {
		return this.wordDict.get(s);
	}

	/**
	 * Major type.
	 *
	 * @param s
	 *            the s
	 * @return the type
	 */
	public Type majorType(String s) {
		if (!this.wordDict.containsKey(s)) {
			return null;
		}
		return this.wordDict.get(s).get(0);
	}

	/**
	 * Adds the domain.
	 *
	 * @param domain
	 *            the domain
	 */
	public void addDomain(Domain domain) {
		domains.add(domain);
		domainMap.put(domain.getDomainID(), domain);
		for (Type type : domain.getTypes()) {
			for (String word : type.getWords()) {
				if (!wordDict.containsKey(word)) {
					wordDict.put(word, new ArrayList<Type>());
				}
				if (domain.domainID.trim().startsWith("3.2")
						|| domain.domainID.trim().startsWith("3.5.7")) {
					System.out.println(word + "\t\t[" + type.typeDesc + "]");
				}
				wordDict.get(word).add(type);
			}
		}
	}

	/**
	 * Reserial.
	 *
	 * @param filePath
	 *            the file path
	 * @return the seedom dict
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public static SeedomDict reserial(String filePath) throws IOException,
			ClassNotFoundException {
		SeedomDict dict = null;
		FileInputStream in = new FileInputStream(filePath);
		ObjectInputStream s = new ObjectInputStream(in);
		dict = (SeedomDict) s.readObject(); // 恢复对象;
		s.close();
		return dict;
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		SeedomDict dict = null;
		try {
			dict = SeedomDict.reserial("resource/dict/seedomDict.ser");
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		String word = "show";
		System.out.println(word + "\t\t[" + dict.majorType(word).getTypeDesc()
				+ "]");
		System.out.println("present" + "\t\t["
				+ dict.majorType("present").getTypeDesc() + "]");
		System.out.println("cover" + "\t\t["
				+ dict.majorType("cover").getTypeDesc() + "]");
	}
}
