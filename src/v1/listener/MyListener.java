package v1.listener;

import javax.swing.*;

import v1.NoteConfig;
import v1.Notepad;
import v1.SearchFile;
import v1.UI;
import v1.utils.MyUtils;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/7/9 0009.
 */
public class MyListener extends WindowAdapter implements ActionListener{
    public boolean saved=true;
    private Notepad notepad;
    private File curFile,chooserFile;

    public MyListener(Notepad notepad){
        this.notepad=notepad;
    }
    
    /**
     * 修改Document并调用方法写入文件
     */
    public void savaToXml(){
    	UI ui=notepad.ui;
    	//保存窗体的宽高
    	NoteConfig.setElementContent("width", ui.getWidth()+"");
    	NoteConfig.setElementContent("height", ui.getHeight()+"");
    	//保存窗体的位置信息
    	Point p=ui.getLocation();
    	NoteConfig.setAttributeValue("location", "x", ((int)p.getX())+"");
    	NoteConfig.setAttributeValue("location", "y", ((int)p.getY())+"");
    	//保存面板的可见状态
		NoteConfig.setElementContent("visible", ui.centerTopPanel.isVisible()?"true":"false");
    	//保存CheckBox的勾选状态
		NoteConfig.setElementContent("matchCaseCheck", ui.matchCaseCheck.isSelected()?"true":"false");
		NoteConfig.setElementContent("fileNameCheck", ui.fileNameCheck.isSelected()?"true":"false");
		NoteConfig.setElementContent("fileContentCheck", ui.fileContentCheck.isSelected()?"true":"false");
    	//保存JTextField的内容
		if(!ui.filterField.getText().toString().equals(ui.filterField.getHintStr())){
			NoteConfig.setElementContent("filterField", ui.filterField.getText().toString());
		}
		if(!ui.filePathField.getText().toString().equals(ui.filePathField.getHintStr())){
			NoteConfig.setElementContent("filePathField", ui.filePathField.getText().toString());
		}
		//保存分割条位置
		if(ui.leftPanelBase.isVisible()){
			NoteConfig.setElementContent("dividerLocation", ui.splitPane.getDividerLocation()+"");
		}
    	
    	NoteConfig.saveDocument(); //保存Document对象到xml文档
    }
    
    
    public void myClose(){
    	try {
    		savaToXml();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
        //super.windowClosing(e);
        if(saved){
            System.exit(0);
        }else if(curFile!=null||!Notepad.notepad.ui.textArea.getText().equals("")){
            String message="是否将更改保存到文件?";
            if(curFile!=null){ //curFile对象存在说明已经保存过或是curFile是打开的文件
                message="是否将更改保存到\n"+curFile.getAbsolutePath()+"?";
            }
            int num = JOptionPane.showConfirmDialog(notepad.ui, message); //确认对话框(是:0;否:1;取消:2;关闭窗体:-1)
            if(num==0){
                if(curFile!=null){
                    //保存
                    save(curFile,notepad.ui.textArea.getText().toString());
                    System.exit(0);
                }else{
                    //另存
                    saveAs();
                    if(chooserFile!=null){
                        System.exit(0);
                    }
                }
            }else if(num==1){
                System.exit(0);
            }
        }else{
        	System.exit(0);
        }
    }


    /**
     * 重写windowClosing方法,改变窗体关闭事件
     */
    @Override
    public void windowClosing(WindowEvent e) { 
    	myClose();
    }


    /**
     * 按钮监听事件
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        UI ui=notepad.ui;
        if(e.getSource() instanceof JMenuItem){
	        JMenuItem menuItem= (JMenuItem) e.getSource();//获取事件源
	        if(menuItem==ui.newMenuItem){
	            //新建
	            newFile();
	        }else if(menuItem==ui.openMenuItem){
	            //打开
	        	if(curFile!=null&&!saved||!ui.textArea.getText().equals("")){
	           	 String message="是否将更改保存到文件?";
	                if(curFile!=null){ //file对象存在说明已经保存过或是file是打开的文件
	                    message="是否将更改保存到\n"+curFile.getAbsolutePath()+"?";
	                }
	                int num = JOptionPane.showConfirmDialog(notepad.ui, message); //确认对话框(是:0;否:1;取消:2;关闭窗体:-1)
	                if(num==0){
	                    if(curFile!=null){
	                        //保存
	                        save(curFile,notepad.ui.textArea.getText().toString());
	                    }else{
	                        //另存
	                        saveAs();
	                        if(chooserFile==null){
	                        	return;
	                        }
	                    }
	                }else if(num==1){
	                }else{
	               	 return;
	                }
	           }
	            JFileChooser fileChooser = new JFileChooser();
	            fileChooser.showOpenDialog(ui);
	            //String dir=fileDialog.getCurrentDirectory().toString(); //获取文件所在目录
	            //String fileName=fileDialog.getSelectedFile().getName(); //获取文件名
            	chooserFile=fileChooser.getSelectedFile();
	            if(chooserFile!=null){
	            	ui.setTitle(chooserFile.getName());
	            	ui.textArea.setText(MyUtils.readFile(chooserFile));
	                curFile=chooserFile;
	                chooserFile=null;
	            }
	        }else if(menuItem==ui.saveMenuItem){
	            //保存
	            if(curFile!=null&&!saved){
	                //保存
	                save(curFile,ui.textArea.getText().toString());
	            }else if(curFile==null){
	                //调用另存方法
	                saveAs();
	            }
	
	        }else if(menuItem==ui.saveAsMenuItem){
	            //另存为
	            saveAs();
	        }else if(menuItem==ui.exitMenuItem){
	        	//调用退出方法
	            myClose();
	        }else if(menuItem==ui.searchMenuItem){
	        	//调用查找方法
	        	if(ui.centerTopPanel.isVisible()){
	        		ui.centerTopPanel.setVisible(false);
	        	}else{
	        		ui.centerTopPanel.setVisible(true);
	        	}
	        }else{
	            if(ui.textArea.getText().toString()!=""){
	
	            }
	            System.out.print(menuItem.getText());
	        }
	    }else if(e.getSource() instanceof JButton){
	    	JButton btn=(JButton) e.getSource();
	    	if(btn==ui.searchBtn){
	    		//查找按钮
	    		if(!ui.fileNameCheck.isSelected()&&!ui.fileContentCheck.isSelected()){
	    			//如果复选框未被选中,停止执行
	    			ui.searchHintLabel.setText("未勾选复选框");
	    			return;
	    		}
	    		if(ui.filterField.getText().toString().equals(ui.filterField.getHintStr())){
	    			ui.searchHintLabel.setText("未填写正则式");
	    			return;
	    		}
	    		try {
	    			Pattern.compile(ui.filterField.getText().toString());
	    			if(ui.fileNameCheck.isSelected()&&ui.fileContentCheck.isSelected()){
						//文件名和文件内容过滤
						String[] patterns=ui.filterField.getText().toString().split("\\|\\|"); //根据"||"分割正则表达式,左边匹配文件名,右边匹配文件内容
						String s=patterns[1];
					}
				} catch (Exception e2) {
					e2.printStackTrace();
					ui.searchHintLabel.setText("正则式有误");
	    			return;
				}
	    		new Thread(){
	    			@Override
					public void run() {
	    				SearchFile.getSearchFile().searchFile(ui.filePathField.getText().toString());
	    			};
	    		}.start();
	    	}
	    }
    }
    
    
    private void newFile(){
    	UI ui=Notepad.notepad.ui;
    	//新建
        if(curFile!=null&&!saved||!ui.textArea.getText().equals("")){
        	 String message="是否将更改保存到文件?";
             if(curFile!=null){ //file对象存在说明已经保存过或是file是打开的文件
                 message="是否将更改保存到\n"+curFile.getAbsolutePath()+"?";
             }
             int num = JOptionPane.showConfirmDialog(notepad.ui, message); //确认对话框(是:0;否:1;取消:2;关闭窗体:-1)
             if(num==0){
                 if(curFile!=null){
                     //保存
                     save(curFile,notepad.ui.textArea.getText().toString());
                     if(curFile.canWrite()){ //保存成功,令curFile指向null
                    	 curFile=null;
                    	 ui.textArea.setText("");
                     }
                 }else{
                     //另存
                     saveAs();
                     if(chooserFile!=null){
                    	 ui.textArea.setText("");
                    	 chooserFile=null;
                     }
                 }
             }else if(num==1){
            	 curFile=null;
            	 ui.textArea.setText("");
             }else{
            	 return;
             }
        }else{
        	ui.textArea.setText("");
        	curFile=null;
        }
    }

    //保存操作
    private void save(File file,String content){
        if(file==null){
            return;
        }
        if(file.exists()&&!file.canWrite()){
        	//文件为只读状态,只能另存
        	saveAs();
        	return;
        }
        BufferedWriter bw=null;
        try {
            bw=new BufferedWriter(new FileWriter(file)); 
            bw.write(content);
            bw.flush();
            saved=true;
        } catch (IOException e1) {
            e1.printStackTrace();
        }finally {
        	try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }

    //另存为操作
    private void saveAs(){
        UI ui=notepad.ui;
        JFileChooser fileDialog = new JFileChooser();
        fileDialog.setDialogTitle("另存为");
        fileDialog.showSaveDialog(ui);
        //String dir=fileDialog.getCurrentDirectory().toString();
        //String fileName=fileDialog.getSelectedFile().getName();
    	chooserFile=fileDialog.getSelectedFile();
        save(chooserFile,ui.textArea.getText().toString());
    }

}
