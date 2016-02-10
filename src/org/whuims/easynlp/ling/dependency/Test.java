package org.whuims.easynlp.ling.dependency;

public class Test {
	public static void main(String[] args) {
		String number=6789+"";
		if(number.length()>5){
			number=number.substring(number.length()-5);
		}
		System.out.println(number);
	}

}
