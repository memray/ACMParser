package org.whuims.commons.util;

import java.util.List;

public class CollectionWorker {
	public static int findString(List<String> list, String str) {
		int count = 0;
		for (String temp : list) {
			if (temp.equals(str)) {
				count++;
			}
		}
		return count;
	}

}
