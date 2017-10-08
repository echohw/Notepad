package v1.listener;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JLabel;

import v1.Notepad;
import v1.UI;
import v1.components.MyListTextField;
import v1.utils.MyUtils;


public class MyMouseListener extends MouseAdapter{
	private static MyListTextField preTextField;
	
	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		UI ui=Notepad.notepad.ui;
		//控件为JTextField
		if(e.getSource() instanceof MyListTextField){
			MyListTextField myTextField=(MyListTextField) e.getSource();
			
			if(e.getClickCount()==1){
				//单击事件
				if(preTextField!=null){
					preTextField.setBackground(Color.getColor("#F0F0F0")); //恢复背景颜色
				}
				myTextField.setBackground(new Color(102, 204, 255)); //设置背景颜色
				preTextField=myTextField;
				
				ui.hintLabel.setText(myTextField.getFilePath());
			}else if(e.getClickCount()==2){
				//双击事件
				ui.textArea.setText(MyUtils.readFile(myTextField.getFilePath()));
			}
		}
		
		//控件为JLabel
		if(e.getSource() instanceof JLabel){
			JLabel label=(JLabel) e.getSource();
			if(label==ui.hintLabel&&e.getClickCount()==2){ //双击时执行
				String filePath=label.getText().toString();
				//将内容添加到剪切板
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(filePath), null); 
				if(!new File(filePath).exists()){
					return;
				}
				try {
					Runtime.getRuntime().exec("explorer /e,/select,"+filePath);//调用explorer打开资源管理器视图并选中文件
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}
