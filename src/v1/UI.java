package v1;

import javax.swing.*;

import v1.controls.MyHintTextField;
import v1.listener.MyDocumentListener;
import v1.listener.MyListener;
import v1.listener.MyMouseListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by Administrator on 2017/7/9 0009.
 */
public class UI extends JFrame{
    public JMenuItem newMenuItem,openMenuItem,saveMenuItem,saveAsMenuItem,exitMenuItem; //文件菜单项
    public JMenuItem searchMenuItem; //编辑菜单项
    public JTextArea textArea;
    public JScrollPane scrollPane,leftScrollPane;
    public JSplitPane splitPane;
    public JPanel leftPanel,leftPanelBase,centerTopPanel,bottemPanel;
    public Notepad notepad;
    public JLabel hintLabel;
    
    public JTextField leftFilterField;
    
    //-------------search面板控件------------
    public JTextField replaceToField;
    public MyHintTextField filterField,filePathField;
    public JButton preBtn,nextBtn,replaceBtn,replaceAllBtn,searchBtn;
    public JCheckBox matchCaseCheck,fileNameCheck,fileContentCheck;
    public JLabel searchHintLabel;

    
    public UI(Notepad notepad){
        this.notepad=notepad;
        //设置窗体风格
        if(UIManager.getLookAndFeel().isSupportedLookAndFeel()){ 
            final String platform = UIManager.getSystemLookAndFeelClassName();
            if (!UIManager.getLookAndFeel().getName().equals(platform)) {
                try {
                    UIManager.setLookAndFeel(platform);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        
        
        initUI();
    }
    
    /**
     * 根据noteConfig设置窗体大小
     */
    public void setSize(){
    	int width=Integer.parseInt(NoteConfig.getElementContent("width"));
    	int height=Integer.parseInt(NoteConfig.getElementContent("height"));
    	this.setSize(width, height);
    }

    /**
     * 根据noteConfig设置窗体位置
     */
    public void setLocation(){
    	int x=Integer.parseInt(NoteConfig.getAttributeValue("location", "x"));
    	int y=Integer.parseInt(NoteConfig.getAttributeValue("location", "y"));
    	this.setLocation(x, y);
    }
    
    /**
     * 初始化窗体
     */
    public void initUI(){
    	
        MyListener myListener=notepad.myListener;
        this.setSize();
        this.setLocation();
        this.setTitle("仿记事本");//设置窗口标题
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);//设置窗体默认关闭事件
        this.addWindowListener(myListener);
        this.setIconImage(new ImageIcon(this.getClass().getResource("/resource/hj.png")).getImage());//设置图标

        JMenuBar menuBar=new JMenuBar(); //创建菜单栏
        this.add(menuBar, BorderLayout.NORTH); //窗体添加菜单栏
        
        bottemPanel=new JPanel(); //创建底部面板作为状态栏
        bottemPanel.setLayout(new BoxLayout(bottemPanel, BoxLayout.X_AXIS));
        hintLabel=new JLabel();
        hintLabel.addMouseListener(new MyMouseListener()); //添加鼠标监听
        bottemPanel.add(hintLabel);
        this.add(bottemPanel,BorderLayout.SOUTH);

        /*---------------创建菜单定义快捷键并添加至菜单栏------------*/
        JMenu fileMenu=new JMenu("文件(F)"); //创建菜单
        fileMenu.setMnemonic(KeyEvent.VK_F); //定义快捷键(alt+f)
        
        JMenu editMenu=new JMenu("编辑(E)");
        editMenu.setMnemonic(KeyEvent.VK_E);

        JMenu formatMent=new JMenu("格式(O)");
        formatMent.setMnemonic(KeyEvent.VK_O);

        JMenu checkMenu=new JMenu("检查(V)");
        checkMenu.setMnemonic(KeyEvent.VK_V);

        JMenu helpMenu=new JMenu("帮助(H)");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        menuBar.add(fileMenu); //菜单栏添加菜单
        menuBar.add(editMenu);
        menuBar.add(formatMent);
        menuBar.add(checkMenu);
        menuBar.add(helpMenu);

        /*---------------文件菜单添加菜单项------------*/
        newMenuItem=new JMenuItem("新建(N)"); //创建菜单项
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));//设置组合键(ctrl+n)
        newMenuItem.addActionListener(myListener);

        openMenuItem=new JMenuItem("打开(O)");
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK)); //设置快捷键
        openMenuItem.addActionListener(myListener);

        saveMenuItem=new JMenuItem("保存(S)");
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK)); //设置组合键
        saveMenuItem.addActionListener(myListener);

        saveAsMenuItem=new JMenuItem("另存为(A)");
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,ActionEvent.CTRL_MASK)); //设置组合键
        saveAsMenuItem.addActionListener(myListener);

        exitMenuItem=new JMenuItem("退出(X)");
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK)); //设置组合键
        exitMenuItem.addActionListener(myListener);

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(exitMenuItem);
        
        /*---------------编辑菜单添加菜单项------------*/
        searchMenuItem=new JMenuItem("查找(F)");
        searchMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,ActionEvent.CTRL_MASK)); //设置组合键
        searchMenuItem.addActionListener(myListener);
        
        editMenu.add(searchMenuItem);

        /*--------------*------------*/
        
        //创建一个指定方向的分割板,为左右布局
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); 
        splitPane.setContinuousLayout(true); //设置是否连续重新显示组件,false:调整过程中显示黑线(默认)
        splitPane.setDividerSize(4); //设置分割条大小
        splitPane.setDividerLocation(100);//设置分割条的位置
        
        leftPanel=new JPanel(); //创建左边的面板
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS)); //左边的面板设置布局为盒布局
        leftScrollPane=new JScrollPane(leftPanel); //创建左边的滚动面板
        leftScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        leftScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        leftScrollPane.getVerticalScrollBar().setUnitIncrement(20); //设置滚动速度
        leftFilterField=new JTextField(); //创建左边的过滤文本行
        leftPanelBase=new JPanel(new BorderLayout()); //创建左边的基础面板
        
        leftPanelBase.add(leftFilterField,BorderLayout.NORTH); //基础面板添加过滤文本行
        leftPanelBase.add(leftScrollPane); //基础面板添加滚动面板
        leftPanelBase.setVisible(false);
        
        JPanel centerPanel=new JPanel(new BorderLayout()); //创建中间的面板
        
        splitPane.setLeftComponent(leftPanelBase); //分割面板添加左边的基础面板
        splitPane.setRightComponent(centerPanel); //添加中间的面板
        
        centerTopPanel=new JPanel(); //创建中间的上面的面板
        centerTopPanel.setLayout(new BoxLayout(centerTopPanel, BoxLayout.Y_AXIS)); //设置为boxlayout竖直方向
        if(NoteConfig.getElementContent("visible").equals("false")){
        	centerTopPanel.setVisible(false);
        }
        
        centerPanel.add(centerTopPanel,BorderLayout.NORTH); //中间面板添加中上的面板
        
        textArea=new JTextArea(15,15);
        textArea.getDocument().addDocumentListener(new MyDocumentListener(textArea));
        scrollPane=new JScrollPane(textArea);
        //分别设置水平和垂直滚动条自动出现
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        centerPanel.add(scrollPane); //中间面板添加滚动文本域
        
        /*--------------中上面板添加控件------------*/
        JPanel centerTopPanelTop=new JPanel();//上
        centerTopPanelTop.setLayout(new BoxLayout(centerTopPanelTop, BoxLayout.X_AXIS));
        centerTopPanelTop.setBorder(BorderFactory.createEmptyBorder(5, 6, 2, 6)); //创建空边框,调整控件四周距离
        
        JPanel centerTopPanelBottom=new JPanel();//下
        centerTopPanelBottom.setLayout(new BoxLayout(centerTopPanelBottom, BoxLayout.X_AXIS));
        centerTopPanelBottom.setBorder(BorderFactory.createTitledBorder("查找"));
        
        
        JPanel topLeftPanel=new JPanel(); //创建field的组合面板
        filterField=new MyHintTextField(NoteConfig.getElementContent("filterField").equals("")?"正则式..":NoteConfig.getElementContent("filterField"),20);
        filterField.setHintStr("正则式..");
        filterField.getDocument().addDocumentListener(new MyDocumentListener(filterField)); //正则式文本框添加监听
        replaceToField=new JTextField(20);
        
        topLeftPanel.setLayout(new BoxLayout(topLeftPanel, BoxLayout.Y_AXIS));
        topLeftPanel.add(filterField); //组合面板添加文本框
        topLeftPanel.add(Box.createVerticalStrut(5));
        topLeftPanel.add(replaceToField);
        
        centerTopPanelTop.add(topLeftPanel); //添加左上面板
        centerTopPanelTop.add(Box.createHorizontalStrut(10)); //创建不可见 Strut,对右边留出一定的空间
        
        JPanel topRightPanel=new JPanel(); //创建右上面板
        topRightPanel.setLayout(new BoxLayout(topRightPanel, BoxLayout.Y_AXIS));
        
        JPanel topRigthTopPanel =new JPanel(); //创建上层按钮面板
        topRigthTopPanel.setLayout(new BoxLayout(topRigthTopPanel, BoxLayout.X_AXIS));
        
        topRightPanel.add(topRigthTopPanel); //右上面板添加上层按钮面板
        
        topRightPanel.add(Box.createVerticalStrut(2));
        
        JPanel topRigthBottomPanel =new JPanel(); //创建下层按钮面板
        topRigthBottomPanel.setLayout(new BoxLayout(topRigthBottomPanel, BoxLayout.X_AXIS));
        topRightPanel.add(topRigthBottomPanel); //右上面板添加下层按钮面板
        
        centerTopPanelTop.add(topRightPanel); //添加右上面板
        
        preBtn=new JButton("↑");
        nextBtn=new JButton("↓");
        matchCaseCheck=new JCheckBox("Match Case");
        if(NoteConfig.getElementContent("matchCaseCheck").equals("true")){
        	matchCaseCheck.setSelected(true);
        }
        topRigthTopPanel.add(preBtn); //上层按钮面板添加按钮
        topRigthTopPanel.add(Box.createHorizontalStrut(5)); //添加不可见strut,拉伸距离
        topRigthTopPanel.add(nextBtn);
        topRigthTopPanel.add(Box.createHorizontalStrut(5));
        topRigthTopPanel.add(matchCaseCheck);
        topRigthTopPanel.add(Box.createHorizontalGlue());
        
        
        replaceBtn=new JButton("Replace");
        replaceAllBtn=new JButton("ReplaceAll");
        topRigthBottomPanel.add(replaceBtn); //下层按钮面板添加按钮
        topRigthBottomPanel.add(Box.createHorizontalStrut(5));
        topRigthBottomPanel.add(replaceAllBtn);
        topRigthBottomPanel.add(Box.createHorizontalGlue()); //占据控件之间多余空间
        
        filePathField=new MyHintTextField(NoteConfig.getElementContent("filePathField").equals("")?"文件路径..":NoteConfig.getElementContent("filePathField"), 20);
        filePathField.setHintStr("文件路径..");
        fileNameCheck=new JCheckBox("文件名");
        if(NoteConfig.getElementContent("fileNameCheck").equals("true")){
        	fileNameCheck.setSelected(true);
        }
        fileContentCheck=new JCheckBox("文件内容");
        if(NoteConfig.getElementContent("fileContentCheck").equals("true")){
        	fileContentCheck.setSelected(true);
        }
        searchHintLabel=new JLabel(" ");
        searchBtn=new JButton("查找");
        
        centerTopPanelBottom.add(filePathField);
        centerTopPanelBottom.add(Box.createHorizontalStrut(15));
        centerTopPanelBottom.add(fileNameCheck);
        centerTopPanelBottom.add(Box.createHorizontalStrut(5));
        centerTopPanelBottom.add(fileContentCheck);
        centerTopPanelBottom.add(Box.createHorizontalStrut(20));
        centerTopPanelBottom.add(searchHintLabel);
        centerTopPanelBottom.add(Box.createHorizontalStrut(20));
        centerTopPanelBottom.add(searchBtn);
        
        centerTopPanel.add(centerTopPanelTop);
        centerTopPanel.add(centerTopPanelBottom);
        
        /*---------------*------------*/
        searchBtn.addActionListener(myListener); //查找按钮添加事件
        
        
        this.add(splitPane);
    }
}
