package v1;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class NoteConfig {
	
	private static String noteConfig="noteConfig.xml";
	private static String filePath=System.getProperty("user.dir")+"\\"+noteConfig;
	private static HashMap<String, String> hashMap=new HashMap<String, String>();
	private static Document doc;
	
	static{
		initMap(); //类加载时执行
	}
	
	/**
	 * 设置标签与xpath的对应关系
	 */
	private static void initMap(){
		hashMap.put("location", "//location");
		hashMap.put("width", "//width");
		hashMap.put("height", "//height");
		hashMap.put("visible", "//visible");
		hashMap.put("fileNameCheck", "//fileNameCheck");
		hashMap.put("fileContentCheck", "//fileContentCheck");
		hashMap.put("matchCaseCheck", "//matchCaseCheck");
		hashMap.put("filterField", "//filterField");
		hashMap.put("filePathField", "//filePathField");
		hashMap.put("dividerLocation", "//dividerLocation");
		
	}
	
	/**
	 * @param elemName 标签名
	 * @return 返回标签内容
	 */
	public static String getElementContent(String elemName){
		String xpath=hashMap.get(elemName);
		return ((Element)getDocument().selectSingleNode(xpath)).getText();
	}
	
	/**
	 * 设置标签内容
	 * @param elemName 标签名
	 * @param content 内容
	 */
	public static void setElementContent(String elemName,String content){
		String xpath=hashMap.get(elemName);
		getDocument().selectSingleNode(xpath).setText(content);
	}
	
	/**
	 * @param elemName 标签名
	 * @param attrName 属性名
	 * @return 返回属性值
	 */
	public static String getAttributeValue(String elemName,String attrName){
		String xpath=hashMap.get(elemName);
		return ((Element)getDocument().selectSingleNode(xpath)).attributeValue(attrName);
	}
	
	/**
	 * 设置属性内容
	 * @param elemName 标签名
	 * @param attrName 属性名
	 * @param attrValue 属性值
	 */
	public static void setAttributeValue(String elemName,String attrName,String attrValue){
		String xpath=hashMap.get(elemName);
		((Element)getDocument().selectSingleNode(xpath)).addAttribute(attrName, attrValue);
	}
	
	/**
	 * @return 返回Document对象
	 */
	public static Document getDocument(){
		if(doc==null){
			if(new File(filePath).exists()){
				try {
					doc=new SAXReader().read(new FileReader(filePath));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				try {
					doc=new SAXReader().read(NoteConfig.class.getResourceAsStream("/config/"+noteConfig));
				} catch (DocumentException e) {
					e.printStackTrace();
				}
			}
		}
		return doc;
	}
	
	/**
	 * 保存Document对象到xml文件
	 */
	public static void saveDocument(){
		OutputFormat format=OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		
		XMLWriter writer=null;
		try {
			writer=new XMLWriter(new FileWriter(filePath),format);
			writer.write(getDocument());
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	
}
