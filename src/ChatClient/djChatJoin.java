package ChatClient;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.simple.JSONObject;

import Network.djChatNetwork;


public class djChatJoin extends JFrame implements ActionListener{
	djChatSingleton obj_Singleton = djChatSingleton.getInstance();
	djChatNetwork obj_Network;
	djChatDlgMsg obj_DlgMsg;
	
	private static final String DELEMETER = " '"; //소메시지 구분자
	
	JPanel jp1 = new JPanel();
	JPanel jp2 = new JPanel();
	JPanel jp3 = new JPanel();
	JPanel jp4 = new JPanel();
	JPanel jp5 = new JPanel();
	JPanel jp6 = new JPanel();
	
	JLabel jlabel1 = new JLabel();
	JLabel jlabel2 = new JLabel();
	JLabel jlabel3 = new JLabel();
	JLabel jlabel4 = new JLabel();
	JLabel jlabel5 = new JLabel();
	JLabel jlabel6 = new JLabel();

	
	
	JTextField jTextF1 = new JTextField();
	JTextField jTextF2 = new JTextField();
	JTextField jTextF3 = new JTextField();
	JTextField jTextF4 = new JTextField();
	JTextField jTextF5 = new JTextField();
	
	JButton jbtn1 = new JButton();
	JButton jbtn2 = new JButton();
	JButton jbtn3 = new JButton();
	
	public djChatJoin(){
		obj_Singleton.setObjJoin(this);

		jlabel1.setText("회원가입");
		jlabel2.setText("ID");
		jlabel3.setText("비밀번호");
		jlabel4.setText("비밀번호확인");
		jlabel5.setText("이름");
		jlabel6.setText("전화번호");

		jbtn1.setText("중복확인");
		jbtn2.setText("회원가입");
		jbtn3.setText("취소");
		

		jTextF1.setColumns(10);
		jTextF2.setColumns(10);
		jTextF3.setColumns(10);
		jTextF4.setColumns(10);
		jTextF5.setColumns(10);
		
		jp1.setLayout(new BorderLayout());
		jp1.add(jTextF1,BorderLayout.CENTER);
		jp1.add(jbtn1,BorderLayout.EAST);
		
		jp2.setLayout(new GridLayout(5,1));
		jp2.add(jlabel2);
		jp2.add(jlabel3);
		jp2.add(jlabel4);
		jp2.add(jlabel5);
		jp2.add(jlabel6);
		
		jp3.setLayout(new GridLayout(5,1));
		jp3.add(jp1);
		jp3.add(jTextF2);
		jp3.add(jTextF3);
		jp3.add(jTextF4);
		jp3.add(jTextF5);
		
		jp4.setLayout(new GridLayout(1,2));
		jp4.add(jp2);
		jp4.add(jp3);
		
		jp5.setLayout(new FlowLayout());
		jbtn2.setText("회원가입");
		jbtn3.setText("취소");
		jp5.add(jbtn2);
		jp5.add(jbtn3);
		
		jp6.setLayout(new FlowLayout());
		jp6.add(jlabel1);
		jp6.add(jp4);
		jp6.add(jp5);
		
		add(jp6);
		
		jbtn1.addActionListener((ActionListener) this);
		jbtn2.addActionListener((ActionListener) this);
		jbtn3.addActionListener((ActionListener) this);
		
		setTitle("DJ Chat 회원가입");
		setSize(450, 250);
		setResizable(false);
		setVisible(false);
	}
	public void loadWindow(){
		setVisible(true);
		callObjNetwork();
		callObjMsgDlg();
	}
	public void callObjNetwork(){
		obj_Network = obj_Singleton.getObjNetwork();
	}
	public void callObjMsgDlg(){
		obj_DlgMsg = obj_Singleton.getObjDlgMsg();
	}
	public void closeWindow(){
		dispose();
	}
	public void joinUser(String userId,String userPassword,String userName,String userPhoneNum){
		JSONObject dataJSON = new JSONObject();
		dataJSON.put("USERID", userId);
		dataJSON.put("USERPW", userPassword);
		dataJSON.put("USERPHONENUM", userPhoneNum);
		dataJSON.put("USERNAME", userName);
		dataJSON.put("USERNICKNAME", userId);
		
		obj_Network.makeJSONPacket("REQ_USER_JOIN", dataJSON, userId, null);
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if (event.getActionCommand().equals("중복확인")) {
			String userId;
			userId = jTextF1.getText();
			obj_Network.makeJSONPacket("REQ_USER_IDEXIST", null, userId, null);
		//	obj_Network.sendExistIdCheckData(userId);
		}
		if (event.getActionCommand().equals("회원가입")) {
			String userId;
			String userPassword;
			String userPasswordCheck;
			String userName;
			String userPhoneNum;
			userId = jTextF1.getText();
			userPassword = jTextF2.getText();
			userPasswordCheck = jTextF3.getText();
			userName = jTextF4.getText();
			userPhoneNum = jTextF5.getText();
			if(!userPassword.equals(userPasswordCheck)){
				
				obj_DlgMsg.printMsg("비밀번호를 다시 확인해 주십시오.");
				userPassword=null;
				userPasswordCheck=null;
			}else{
				joinUser(userId, userPasswordCheck, userName, userPhoneNum);
				//obj_Network.sendJoinData(userId, userPassword, userName, userPhoneNum);
				jTextF1.setText("");
				jTextF2.setText("");
				jTextF3.setText("");
				jTextF4.setText("");
				jTextF5.setText("");
			}
		}
		if (event.getActionCommand().equals("취소")) {
			closeWindow();
		}
	}
}
