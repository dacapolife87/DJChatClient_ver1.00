package TalkRoom;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ChatClient.djChatFriendTree;
import RoomList.djChatRoomList;
import ChatClient.djChatSingleton;
import MakeRoom.djChatInviteRoom;
import MakeRoom.djChatMakeTalkRoom;
import Network.djChatNetwork;

public class djChatTalkRoom extends JFrame implements ActionListener ,MouseListener{
	private static final Color GREY_COLOR = new Color(200, 248, 248);
	djChatSingleton obj_Singleton = djChatSingleton.getInstance();
	djChatNetwork obj_Network;
	djChatRoomHandler obj_RoomHandler;
	djChatFriendTree obj_FriendTree;
	djChatRoomList obj_RoomList;
	//djChatProfilePicListener obj_piclistener;
	
	//// 추가
	//CellButtonsMouseListener cbml;
	MouseListener ml1;
	JList<Msg> listMsg;
	DefaultListModel<Msg> model;
	JPanel jp5;
	/////
    private static final String DELEMETER = " '"; //소메시지 구분자

	
	private String index; //대하방 번호
	private String My_Id; //내 아이디 
	private String Join_UserId; //참여한 사람의 모든 아이디
	private boolean activeTalkRoom;
	
	JPanel jp1; //전체 화면
	JPanel jp2; //아래에 TextField 담을 판넬
	JPanel jp3; //전송버튼 나가기버튼 담을 판넬
	JPanel jp4; //MyId, 참여자 ID 를 담을 판넬
	
	JLabel label1; //참여자 id 보여주는 곳  id1, id2, id3 .... 
	JLabel label2; //My_id
	
	JTextArea textArea; //대화내용출력
	
	JTextField textField; //채팅 입력
	JButton jbtn1; //전송버튼
	JButton jbtn2; //초대 버튼
	JButton jbtn3; //나가기 버튼
	
	JSONArray jsonArray;
	
	public djChatTalkRoom(String myId){
		obj_Network = obj_Singleton.getObjNetwork();
		obj_RoomHandler = obj_Singleton.getObjTalkRoom();
		obj_FriendTree = obj_Singleton.getObjFreindTree(); //프렌드트리
		obj_RoomList = obj_Singleton.getObjRoomList();
		EventHandler handler = new EventHandler();
		this.My_Id = myId;
		jp1 = new JPanel(new BorderLayout());
		jp2 = new JPanel(new GridLayout(2,1));
		jp3 = new JPanel(new GridLayout(1,3));
		jp4 = new JPanel(new GridLayout(2,1));
		jp5 = new JPanel(new BorderLayout());
		label1 = new JLabel();
		label2 = new JLabel("MyId : " + My_Id);
		jp4.add(label2);
		jp4.add(label1);

		model = new DefaultListModel<>();

		listMsg = new JList<Msg>(model);
		
		//obj_piclistener = new djChatProfilePicListener(listMsg);
		listMsg.setBackground(GREY_COLOR);

		jp1.add(new JScrollPane(listMsg),BorderLayout.CENTER);

		listMsg.setCellRenderer(new djChatMsgRenderer<Msg>(model,My_Id));
		
		textArea = new JTextArea();
		textArea.addFocusListener(handler);
		jp1.add(jp4, BorderLayout.NORTH);
		
		textField = new JTextField(20);
		textField.addFocusListener(handler);
		textField.addActionListener(handler);
		
		jbtn1 = new JButton("전송");
		jbtn1.addActionListener(this);
		jbtn2 = new JButton("초대");
		jbtn2.addActionListener(this);
		jbtn3 = new JButton("나가기");
		jbtn3.addActionListener(this);
		jp2.add(textField);
		jp3.add(jbtn1);
		jp3.add(jbtn2);
		jp3.add(jbtn3);
		jp2.add(jp3);
		jp1.add(jp2, BorderLayout.SOUTH);
		
		add(jp1);
		setSize(300,500);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				activeTalkRoom = false;
				dispose();
			}
		});
	//	setDefaultCloseOperation(HIDE_ON_CLOSE);
		activeTalkRoom = false;
		setVisible(false);
	}
	
	public void inputData(String userId,String msgData) {

    	model.addElement(new Msg(userId, msgData));
    }
	public void printMsg(String userId, String msg){
		
		inputData(userId, msg);
		//textArea.append(msg + "\n");
		chatRoomReadMsg();
		
	}
	public void chatRoomReadMsg(){
		JSONObject dataJSON = new JSONObject();
		dataJSON.put("DATA", index);
		if(activeTalkRoom == true){
			obj_Network.makeJSONPacket("REQ_USER_LASTREAD", dataJSON, My_Id, null);
		}
	}
	public String readText(){
		String txt = null;
		txt = textField.getText();
		return txt;
	}
	
	//초기에 대화방을 호출할때 대화방 번호를 저장한다.
	public void callTalkRoomWindow(String index){
		setVisible(true);
		activeTalkRoom = true;
		this.index = index;
		
		StringBuffer sb = new StringBuffer();
		for(int i=0; i < jsonArray.size(); i++){
			if(!(My_Id.equals(jsonArray.get(i)))){
				sb.append(jsonArray.get(i));
				sb.append(" ");
			}
			
		}
		
		obj_RoomList.addTalkRoomList(index, My_Id, sb.toString());
	}
	
	public void recallTalkRoom(){
		chatRoomReadMsg();
		setVisible(true);
		activeTalkRoom = true;
	}
	
	//대화방 개설시 내 아이디를 저장
	public void set_Myid(String My_Id){
		//this.My_Id = My_Id;
		label2.setText("MyId : " + My_Id);
	}
	//대화방 개설시 참여자 아이디를 저장
	public void printJoinUser(JSONArray jsonArray){
		this.jsonArray = jsonArray; //대화방에 참여한 사람들의 아이디들의 배열
		StringBuffer sb = new StringBuffer();
		sb.setLength(0);
		for(int i=0; i<jsonArray.size(); i++){
			if(!((String)jsonArray.get(i)).equals(My_Id)){
			sb.append((String)jsonArray.get(i));
			sb.append(", ");
			}
		}
		label1.setText("참여자 : " + sb.toString());
	//	this.jsonArray = jsonArray;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if(event.getActionCommand().equals("전송")){

			JSONObject jsonData = new JSONObject();
			jsonData.put("INDEX", index);
			jsonData.put("MESSAGE", readText());
			
			obj_Network.makeJSONPacket("REQ_TALK_SENDMSG", jsonData, My_Id, null);
			textField.setText("");
			
			//=======================================
		}else if(event.getActionCommand().equals("초대")){
			//대화방에서 초대기능 구현
			//new djChatMakeTalkRoom(obj_FriendTree.rootNode, My_Id);
			//대화방에 이미 있는 사람들은 체크상태 변경 불가로 만드는 새로운 객체 호출
			//new djChatInviteRoom(obj_FriendTree.rootNode, alreadyUSer)
			//바꿔야 할 부분
			new djChatInviteRoom(obj_FriendTree.rootNode, index , jsonArray);
		}
		else if(event.getActionCommand().equals("나가기")){
			JSONObject jsonData = new JSONObject();

			jsonData.put("INDEX", index);
			obj_Network.makeJSONPacket("REQ_TALK_EXITROOM", jsonData, My_Id, null);
			obj_RoomHandler.exitTalkRoom(index);
			dispose();

		}
	}
	public void reJoinTalkRoom(String index){
		this.index = index;
	}

	class EventHandler extends FocusAdapter implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			JSONObject jsonData = new JSONObject();
			jsonData.put("INDEX", index);
			jsonData.put("MESSAGE", readText());
			
			obj_Network.makeJSONPacket("REQ_TALK_SENDMSG", jsonData, My_Id, null);
			textField.setText("");
			
		}
		public void focusGained(FocusEvent e){
			textField.requestFocus();
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
