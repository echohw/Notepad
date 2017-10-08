package v1.components;

import javax.swing.JTextField;
import javax.swing.text.Document;

public class MyListTextField extends JTextField{
	public String fileName;
	public String filePath;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	

	public MyListTextField() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MyListTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
		// TODO Auto-generated constructor stub
	}

	public MyListTextField(int columns) {
		super(columns);
		// TODO Auto-generated constructor stub
	}

	public MyListTextField(String text, int columns) {
		super(text, columns);
		// TODO Auto-generated constructor stub
	}

	public MyListTextField(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}
	
}
