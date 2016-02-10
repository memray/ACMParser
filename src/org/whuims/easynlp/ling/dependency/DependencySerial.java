package org.whuims.easynlp.ling.dependency;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.SerializationUtils;
import org.whuims.easynlp.parserwrapper.SentDetector;
import org.whuims.easynlp.parserwrapper.StanfordParser;

import edu.stanford.nlp.trees.Tree;

public class DependencySerial {
	String filePath="";
	String savePath="";
	Set<Integer> set=new HashSet<Integer>();
	
	public DependencySerial(String filePath, String savePath) {
		super();
		this.filePath = filePath;
		this.savePath = savePath;
	}

	public void work() throws IOException{
		BufferedReader reader=new BufferedReader(new FileReader(this.filePath));
		String line=reader.readLine();
		int count=0;
		while(line!=null){
			String[] array=SentDetector.createInstance().detect(line);
			for(String str:array){
				count++;
				process(str);
			}
			if(count%1000==0){
				System.out.println(count);
			}
			line=reader.readLine();
		}
		reader.close();
	}
	
	private void process(String line){
		Tree tree=StanfordParser.createInstance().parse(line);
		try {
			byte[] bytes=SerializationUtils.serialize(tree);
			//生成写入压缩文件
			int hashCode=line.hashCode();
			if(set.contains(hashCode)){
				return;
			}else{
				set.add(hashCode);
			}
			String number=hashCode+"";
			if(number.length()>4){
				number=number.substring(number.length()-4);
			}
			String dirPath=this.savePath+"/"+number+"/";
			File dir=new File(dirPath);
			if(!dir.exists()){
				dir.mkdirs();
			}
			ObjectOutputStream out=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(dirPath+hashCode), 2048));
			out.write(bytes);
			out.close();
//			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipPath), 2048));
//			out.putNextEntry(new ZipEntry(hashCode+""));
//			System.out.println(hashCode);
//			out.write(bytes);
//			out.close() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		String filePath=args[0];
		String savePath=args[1];
		DependencySerial ser=new DependencySerial(filePath,savePath);
		try {
			ser.work();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
