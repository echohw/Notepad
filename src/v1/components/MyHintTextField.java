package v1.components;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.text.Document;

public class MyHintTextField extends JTextField implements FocusListener{
	
	private String hintStr;
	
	public String getHintStr() {
		return hintStr;
	}

	public void setHintStr(String hintStr) {
		this.hintStr = hintStr;
		
		this.addFocusListener(this); //添加焦点监听,显示提示文字
	}
	

	@Override
	public void focusGained(FocusEvent e) {//获得焦点时,清空提示文字
		if(e.getSource() instanceof JTextField){
			JTextField field=((JTextField) e.getSource());
			if(field.getText().toString().equals(hintStr)){
				field.setText("");
			}
		}
		
	}
	@Override
	public void focusLost(FocusEvent e) { //失去焦点时,如果为空,就显示提示
		if(e.getSource() instanceof JTextField){
			JTextField field=((JTextField) e.getSource());
			if(field.getText().toString().equals("")){
				field.setText(hintStr);
			}
		}
	}

	public MyHintTextField() {
		super();
	}

	public MyHintTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
	}

	public MyHintTextField(int columns) {
		super(columns);
	}

	public MyHintTextField(String text, int columns) {
		super(text, columns);
	}

	public MyHintTextField(String text) {
		super(text);
	}

}
