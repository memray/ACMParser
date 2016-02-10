package org.whuims.graph;

import java.util.HashMap;
import java.util.Map;

public class TokenIDMap {
	private static Map<String, String> instanceTokenIDMap = new HashMap<String, String>();
	private static Map<String, String> instanceIDTokenMap = new HashMap<String, String>();
	private static Map<String, String> catTokenIDMap = new HashMap<String, String>();
	private static Map<String, String> catIDTokenMap = new HashMap<String, String>();

	public static String addInstanceToken(String token) {
		return addToken(token, instanceTokenIDMap, instanceIDTokenMap, "i");
	}

	public static String addCatToken(String token) {
		return addToken(token, catTokenIDMap, catIDTokenMap, "c");
	}

	public static String getInstanceID(String token) {
		if(!instanceTokenIDMap.containsKey(token)){
			return addInstanceToken(token);
		}
		return instanceTokenIDMap.get(token);
	}

	public static String getCatID(String token) {
		if(!catTokenIDMap.containsKey(token)){
			return addCatToken(token);
		}
		return catTokenIDMap.get(token);
	}

	public static String getInstanceTokenByID(String id) {
		return instanceIDTokenMap.get(id);
	}

	public static String getCatTokenByID(String id) {
		return catIDTokenMap.get(id);
	}

	private static String addToken(String token, Map<String, String> usingTokenIDMap,
			Map<String, String> usingIDTokenMap, String label) {
		String id = usingTokenIDMap.get(token);
		String size = label + usingTokenIDMap.size();
		if (id == null) {
			usingTokenIDMap.put(token, size);
			usingIDTokenMap.put(size, token);
			return size;
		}
		return id;
	}

	public static void main(String[] args) {

	}

}
