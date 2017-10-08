package v1.listener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import v1.Notepad;

/**
 * 对JTextField添加监听
 * @author Administrator
 */
public class MyDocumentListener implements DocumentListener{
	private Object sourceObj;
	
	public <T> MyDocumentListener(T sourceObj) {
		this.sourceObj=sourceObj;
	}
	
	public Object getSourceObj() {
		return sourceObj;
	}

    private void textChange(DocumentEvent e){
    	if(this.sourceObj==Notepad.notepad.ui.filterField){
    		//
    		JTextField filterField=(JTextField) sourceObj;
    		String pattern=filterField.getText().toString();
    		String textContent=Notepad.notepad.ui.textArea.getText().toString().trim();
    		if(pattern.equals("")||textContent.equals("")){
    			return;
    		}
    		ArrayList<String> list=findAll(pattern, textContent);
    		if(list!=null){
//    			String[] key=new String[list.size()];
//    			for(int i=0;i<list.size();i++){
//    				key[i]=list.get(i);
//    			}
//    			System.out.println(Arrays.toString(key));
//    			MyTextPane._keys=key;
//    			String content=Notepad.notepad.ui.textArea.getText().toString();
//    			Notepad.notepad.ui.textArea.init();
//    			Notepad.notepad.ui.textArea.setText(content);
    		}
    		
    	}else if(this.sourceObj==Notepad.notepad.ui.textArea){
    		Notepad.notepad.myListener.saved=false;
    	}
    }
    
    public ArrayList<String> findAll(String pattern,String str){
		ArrayList<String> list=new ArrayList<String>();
		// 创建 Pattern 对象
		Pattern p = null;
		try {
			p = Pattern.compile(pattern);
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}
		// 现在创建 matcher 对象
		Matcher m = p.matcher(str);
		while(m.find()){
			list.add(m.group());
		}
		return list;
	}
    
    
	@Override
	public void insertUpdate(DocumentEvent e) {
		textChange(e);
	}
	@Override
	public void removeUpdate(DocumentEvent e) {
		textChange(e);
	}
	@Override
	public void changedUpdate(DocumentEvent e) {
		textChange(e);
	}

}
