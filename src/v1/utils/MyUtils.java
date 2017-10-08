package v1.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

public class MyUtils {

	public static String readFile(String filePath) {
		return readFile(new File(filePath));
	}

	public static String readFile(File file) {
		String text = "";
		if (file.getName().endsWith(".docx")) {
			// 读取word文档
			text = readWordFile2007(file.getAbsolutePath());
		} else {
			StringBuffer sb = new StringBuffer();
			String line = null;
			BufferedReader br = null;
			String encode = CPDetector.getFileEncode(file.getAbsolutePath());
			try {
				br = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(file)),encode != null ? encode : "utf-8"));
				while ((line = br.readLine()) != null) {
					sb.append(line+"\n");
				}
			} catch (Exception e) {
				System.out.println(file.getAbsolutePath());
				e.printStackTrace();
			}finally {
				if(br!=null){
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			text=sb.toString();
		}
		return text;
	}

	/**
	 * 读取word 2007以上版本文件 .docx格式 使用到的jar包 poi*.jar poi-ooxml*.jar
	 * poi-ooxml-schemas*.jar poi-scraatchpad*.jar xmlbeans*.jar
	 * @param filePath 文件路径
	 * @return
	 */
	public static String readWordFile2007(String filePath) {
		String text = "";
		try {
			OPCPackage opcPackage = POIXMLDocument.openPackage(filePath);
			POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
			text = extractor.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}

}
