package v1;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import v1.components.MyListTextField;
import v1.listener.MyMouseListener;
import v1.utils.MyUtils;


public class SearchFile{
	ArrayList<File> allList=new ArrayList<File>();
	ArrayList<File> showList=new ArrayList<File>();
	private  UI ui=Notepad.notepad.ui;
	private static SearchFile searchFile;
	
	private SearchFile(){
	}
	
	public static SearchFile getSearchFile(){
		if(searchFile==null){
			searchFile=new SearchFile();
		}
		return searchFile;
	}
	
	public void  searchFile(String dir) {
		File fileDir=new File(dir);
		if(!fileDir.exists()){
			ui.searchHintLabel.setText("文件路径错误");
			return  ;
		}
		ui.searchHintLabel.setText("");
		allList.clear();
		showList.clear();
		ui.searchBtn.setEnabled(false);
		
		try {
			scanFolder(fileDir); //递归遍历目录
			ui.searchHintLabel.setText(allList.size()+"");
		} catch (Exception e) {
			ui.searchHintLabel.setText("程序异常");
			e.printStackTrace();
		}finally {
			ui.searchBtn.setEnabled(true);
		}
		
		ui.leftPanel.removeAll();
		//ui.leftPanel.updateUI();
		ui.leftPanel.repaint(); //重新绘制组件
		showList.addAll(allList); //将遍历的文件对象添加到显示列表
		
		//递归遍历完成
		for(File file:showList){
			MyListTextField textField=new MyListTextField();
			textField.setFileName(file.getName());
			textField.setFilePath(file.getAbsolutePath());
			
			textField.setText(file.getName());
			textField.setEditable(false);
			
			textField.addMouseListener(new MyMouseListener());
			ui.leftPanel.add(textField);
		}
		

		if(showList.size()!=0){
			//显示分割板
			ui.leftPanelBase.setVisible(true);
			ui.splitPane.setDividerLocation(Integer.parseInt(NoteConfig.getElementContent("dividerLocation")));//设置分割条的位置
		}
	}

	
	private void scanFolder(File srcFolder) {
		File[] files=srcFolder.listFiles(); //srcFolder为空的话下面执行会报空指针
		if(files==null){
			return ;
		}
		for(File file:files){
			if(file.isFile()&&file.exists()){
					
				if(ui.fileNameCheck.isSelected()&&!ui.fileContentCheck.isSelected()){
					//文件名过滤
					if(fileNameFilter(file,ui.filterField.getText().toString())){
						allList.add(file);
						ui.searchHintLabel.setText(allList.size()+"");
					}
				}else if(!ui.fileNameCheck.isSelected()&&ui.fileContentCheck.isSelected()){
					//文件内容过滤
					if(fileContentFilter(file,ui.filterField.getText().toString())){
						allList.add(file);
						ui.searchHintLabel.setText(allList.size()+"");
					}
				}else if(ui.fileNameCheck.isSelected()&&ui.fileContentCheck.isSelected()){
					//文件名和文件内容过滤
					String[] patterns=ui.filterField.getText().toString().split("\\|\\|"); //根据"||"分割正则表达式,左边匹配文件名,右边匹配文件内容
					try {
						String s=patterns[1];
					} catch (Exception e) {
						ui.searchHintLabel.setText("正则式有误");
						ui.searchBtn.setEnabled(true);
						e.printStackTrace();
					}
					if(fileNameFilter(file, patterns[0])){
						if(fileContentFilter(file, patterns[1])){
							allList.add(file);
							ui.searchHintLabel.setText(allList.size()+"");
						}
					}
				}
			}else if(file.isDirectory()){
				scanFolder(file);
			}
		}
	}
	
	//对文件名进行过滤
	private boolean fileNameFilter(File file,String pattern){
		return match(pattern,file.getName())?true:false;
	}
	
	//对文件内容进行过滤
	private boolean fileContentFilter(File file,String pattern){
		return match(pattern,MyUtils.readFile(file))?true:false;
	}
	
	private boolean match(String pattern,String str){
		// 创建 Pattern 对象
		Pattern r = Pattern.compile(pattern);
		// 现在创建 matcher 对象
		Matcher m = r.matcher(str);
		return m.find()?true:false; //判断是否有匹配的内容
	}
	
}
