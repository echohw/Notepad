package v1;

import v1.listener.MyListener;

public class Notepad {
	public MyListener myListener;
	public UI ui;
	public static Notepad notepad;
	
	private void init(){
		this.myListener=new MyListener(this);
		this.ui=new UI(this);
	}
	
	public static void main(String[] args) {
		notepad=new Notepad();
		notepad.init();
		
		notepad.ui.setVisible(true);
	}
	
}
