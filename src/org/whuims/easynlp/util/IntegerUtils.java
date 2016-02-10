package org.whuims.easynlp.util;

public class IntegerUtils {
	public static int max(int... args){
		int maxValue=args[0];
		for(int value:args){
			if(maxValue<value){
				maxValue=value;
			}
		}
		return maxValue;
	}
	
	public static void main(String[] args) {
		System.out.println(IntegerUtils.max(2, 4,6,3));
	}

}
