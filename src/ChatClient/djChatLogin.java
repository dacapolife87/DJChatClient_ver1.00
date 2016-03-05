package ChatClient;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.simple.JSONObject;

import Network.djChatNetwork;

public class djChatLogin extends JFrame implements ActionListener{
	djChatSingleton obj_Singleton = djChatSingleton.getInstance();
	djChatWindow obj_Window;
	djChatNetwork obj_Network;
	djChatJoin obj_Join;
	djChatDlgMsg obj_MsgDlg;
	
	private String userId;
	private String userPassword;
	
	
	
	JPanel jp1 = new JPanel();
	JPanel jp2 = new JPanel();
	JPanel jp3 = new JPanel();
	JPanel jp4 = new JPanel();
	JPanel jp5 = new JPanel();
	JPanel jp6 = new JPanel();
	
	JButton jbtn1 = new JButton();
	JButton jbtn2 = new JButton();
	JButton jbtn3 = new JButton();

	JLabel jlabel1 = new JLabel();
	JLabel jlabel2 = new JLabel();
	JLabel jlabel3 = new JLabel();
	
	JTextField jTextF1 = new JTextField();
	JTextField jTextF2 = new JTextField();
	
	public djChatLogin(){	
		obj_Singleton.setObjLogin(this);

		
		jlabel1.setText("ID");		
		jlabel2.setText("PASSWORD");
		jlabel3.setText("Double J Chat");
		jTextF1.setColumns(20);
		jTextF2.setColumns(20);
		
		
		jbtn1.setText("접속");
		jbtn2.setText("회원가입");
		jbtn3.setText("ID/PW찾기");
		
		jp1.setLayout(new GridLayout(2,1));
		jp1.add(jlabel1);
		jp1.add(jlabel2);
		jp2.setLayout(new GridLayout(2,1));
		jp2.add(jTextF1);
		jp2.add(jTextF2);

		jp3.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		jp3.add(jp1);	
		jp3.add(jp2);
		jp3.add(jbtn1);
		
		jp4.setLayout(new GridLayout(1,2));
		jp4.add(jbtn2);
		jp4.add(jbtn3);
		jp5.setLayout(new FlowLayout());
		jp5.add(jp3);
		jp5.add(jp4);
		
		jp6.setLayout(new GridLayout(2,1));
		jp6.add(jlabel3);
		jp6.add(jp5);
		add(jp6);
		
		jbtn1.addActionListener((ActionListener) this);
		jbtn2.addActionListener((ActionListener) this);
	
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	//dcn.stopSocket();
            	//obj_Receiver.stopReceiver();
                //obj_Sender.stopSender();
                // 프레임의 자원을 반납하고 시스템을 종료한다.
            	//closeProgram();
            	
                //dispose();
                //System.exit(0);
            }
        });
		
		setTitle("Welcome to DoubleJ Chat");
		setSize(400, 250);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(false);
	}

	public void loadWindow(){
		setVisible(true);
		callObjNetwork();
		callObjWindow();
		callObjJoin();
		callObjMsgDlg();
		obj_Network.startThread();
	}
	public void callObjMsgDlg(){
		obj_MsgDlg = obj_Singleton.getObjDlgMsg();
	}
	public void callObjWindow(){
		obj_Window = obj_Singleton.getObjWindow();
	}
	public void callObjNetwork(){
		obj_Network = obj_Singleton.getObjNetwork();
	}
	public void callObjJoin(){
		obj_Join = obj_Singleton.getObjJoin();
	}
	
	public String getUserId(){
		return userId;
	}
	public void setUserID(String userId){
		this.userId = userId;
	}
	public void setUserPassword(String userPassword){
		this.userPassword = userPassword;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if (event.getActionCommand().equals("접속")) {
			setUserID(jTextF1.getText());
			setUserPassword(jTextF2.getText());

			//obj_Network.checkLogin(userId);
			JSONObject dataJSON = new JSONObject();
			dataJSON.put("USERPW", userPassword);
			
			obj_Network.makeJSONPacket("REQ_USER_LOGIN", dataJSON, userId, null);
			//obj_Network.sendLoginData(userId, userPassword);
		}
		if (event.getActionCommand().equals("회원가입")) {
			obj_Join.loadWindow();
		}
	}
	
	public void closeWindow(){
		obj_Network.makeJSONPacket("REQ_FRIEND_LIST",null, userId,null);
		dispose();
	}
	
}
