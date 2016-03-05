package ChatClient;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import AddFriend.djChatAddFriend;
import MakeRoom.djChatMakeTalkRoom;
import Network.djChatNetwork;
import MakeRoom.*;
import RoomList.djChatRoomList;
public class djChatWindow extends JFrame implements ActionListener{
	djChatSingleton obj_Singleton = djChatSingleton.getInstance();
	djChatLogin obj_Login;
	djChatNetwork obj_Network;
	djChatFriendTree obj_FriendTree;
	djChatRoomList obj_RoomList;
	djChatAddFriend addFriend;
	
	JSONObject dataJSON;

	private static boolean checkOnline = false;
	
	
	JTabbedPane jTab = new JTabbedPane();
	
	JPanel jp1 = new JPanel();
	JPanel jp2 = new JPanel();
	JPanel jp3 = new JPanel();
	JPanel jp4 = new JPanel();  // 프로필 관련부분
	JPanel jp5 = new JPanel();
	
	JButton jbtn1 = new JButton();
	JButton jbtn2 = new JButton();
	JButton jbtn3 = new JButton();
	
//	djChatFriendTree treePanel; // 새롭게 만드는 클래스
	JLabel myId; //내 아이디를 써주는 라벨부분
	JLabel myStatus; //내 아이디를 써주는 라벨부분
	///개인 프로필
	ImageIcon profilePic = new ImageIcon();
	JButton jbtn4 = new JButton(profilePic);
	
	
	List talklist = new List();
	
	//임시대화창
	JPanel tempJp1 = new JPanel();
	JPanel tempJp2 = new JPanel();
	JButton tempJBtn1 = new JButton();
	TextArea tempTextA = new TextArea();
	TextField tempTextF = new TextField();

	// 유져 ID
	String My_ID;
	
	public djChatWindow(){
		obj_Singleton.setObjWindow(this);
		callObjFriendTree();
		callObjRoomList();
		
		myId = new JLabel(); //내 ID를 저장하는 곳
		myStatus = new JLabel(); // 대화명 라벨
		// 개인 정보 패널
		jp4.setLayout(new GridLayout(2, 1));
		jp4.add(myId);
		jp4.add(myStatus);
		jp5.setLayout(new BorderLayout());
		jp5.add(jbtn4,BorderLayout.WEST);
		jp5.add(jp4,BorderLayout.CENTER);
		//
		jbtn1.setText("친구추가");
		jbtn1.addActionListener(this); // ========
		jbtn2.setText("친구삭제");
		jbtn2.addActionListener(this); // ========
		jbtn3.setText("대화방만들기");
		jbtn3.addActionListener(this);
		
		jp1.setLayout(new BorderLayout());
		jp2.setLayout(new BorderLayout());
		//jp1.add(myId, BorderLayout.NORTH); //myid
		jp1.add(jp5, BorderLayout.NORTH); //myid
		jp1.add(obj_FriendTree,BorderLayout.CENTER);
//		jp2.add(talklist,BorderLayout.CENTER);
//		jp2.add(roomList,BorderLayout.CENTER);
		jp2.add(obj_RoomList,BorderLayout.CENTER);
		jp2.add(jbtn3,BorderLayout.SOUTH);
		jp3.add(jbtn1,BorderLayout.WEST);
		jp3.add(jbtn2,BorderLayout.EAST);
		
		jp1.add(jp3,BorderLayout.SOUTH);
		
		jTab.addTab("친구목록", jp1);
		jTab.addTab("대화방", jp2);
		
		// 임시대화창/////////////
		tempJBtn1.setText("전송");
		tempJp1.setLayout(new BorderLayout());
		tempJp2.setLayout(new BorderLayout());
		tempJp1.add(tempTextA, BorderLayout.CENTER);
		tempJp2.add(tempTextF,BorderLayout.CENTER);
		tempJp2.add(tempJBtn1,BorderLayout.EAST);
		tempJp1.add(tempJp2,BorderLayout.SOUTH);
		jTab.addTab("임시대화창", tempJp1);
		tempJBtn1.addActionListener(this);
		/////////////////////////////
		add(jTab);
		
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
		
		setTitle("DoubleJ Chat");
		setSize(300, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(false);
	}

	public void loadWindow(JSONObject data){
		JSONObject replydataJSON = new JSONObject();
		replydataJSON = (JSONObject) data.get("DATA");
		//callObjLogin();
		callObjNetwork();
		//My_ID = obj_Login.getUserId();
		String userId;
		String statusMsg;
		userId = (String) data.get("SENDER");
		statusMsg = (String) replydataJSON.get("DATA");
		My_ID = userId;
		myId.setText(userId); //내 아이디는 Label에 등록한다.
		myStatus.setText(statusMsg);
		setVisible(true);
	}
	
	public void callObjNetwork(){
		obj_Network = obj_Singleton.getObjNetwork();
	}
	public void callObjLogin(){
		obj_Login = obj_Singleton.getObjLogin();
	}
	public void callObjFriendTree(){
		obj_FriendTree = obj_Singleton.getObjFreindTree();
	}
	public void callObjRoomList(){
		obj_RoomList = obj_Singleton.getObjRoomList();
	}
	public void printMsg(String msg){
		tempTextA.append(msg + "\n");
	}
	
	public String readText(){
		String txt=null;
		txt = tempTextF.getText();
		System.out.println("readtxt : "+txt);

		return txt;
	}
	
	//처음 접속했을때에 모든 접속자들을 불러오는 함수
	public void showFriendList(JSONObject receiveData){
		JSONArray friendList = new JSONArray();
		JSONObject dataJSON = new JSONObject();
		dataJSON = (JSONObject) receiveData.get("DATA");
		friendList = (JSONArray) dataJSON.get("FRIENDLIST");
		System.out.println("receiveData : "+receiveData);
		System.out.println("friendList : "+friendList);
		System.out.println("friendListsize : "+friendList!=null);
		if(friendList!=null){
			for(int i=0;i<friendList.size();i++){
				if(!friendList.get(i).equals(My_ID)){
					System.out.println("tget");
					obj_FriendTree.addObject(friendList.get(i));
				}
			}
		}
		
	}
	public void delFriendList(){
		JSONObject dataJSON= new JSONObject();
		String friendId;
		friendId = obj_FriendTree.dellObject();
		dataJSON.put("FRIENDID", friendId);
		obj_Network.makeJSONPacket("REQ_FRIEND_DELETE",dataJSON, My_ID,null);
	}
	//접속한 사용자만의 ID를 받아서 TREE에 추가하는 함수
	public void addFriendList(JSONObject receiveData){
		JSONObject dataJSON= new JSONObject();
		String id=null;
		dataJSON=(JSONObject) receiveData.get("DATA");
		id = (String) dataJSON.get("DATA");
		obj_FriendTree.addObject(id);
	}
	//추천친구목록을 받아서 TREE에 저장
	public void showRecommandFriendList(JSONObject dataJSON){
		this.dataJSON = dataJSON;
	}
	public void addRecommandFriendJSON(){
		if(addFriend!=null){
			addFriend.addElement(dataJSON);
		}
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if(event.getActionCommand().equals("전송")){
			obj_Network.talkMsg("[" +My_ID+ "]" + readText());

			tempTextF.setText("");
		}else if(event.getActionCommand().equals("대화방만들기")){
			System.out.println("대화방만들기 : "+My_ID);
			new djChatMakeTalkRoom(obj_FriendTree.rootNode, My_ID);
		}else if(event.getActionCommand().equals("친구추가")){
			System.out.println("window _ 친구추가  클릭");
			obj_Network.makeJSONPacket("REQ_FRIEND_RECOMMANDLIST",null, My_ID,null);
			//new djChatRecommFriendTable(dataJSON,My_ID);
			addFriend = new djChatAddFriend(My_ID);
			addRecommandFriendJSON();
		}
		else if(event.getActionCommand().equals("친구삭제")){
			System.out.println("window _ 친구삭체  클릭");
			delFriendList();
			//obj_Network.makeJSONPacket("REQ_FRIEND_RECOMMANDLIST",null, My_ID,null);
			
		}
	}
	
	/*
	public void addList(String id){
		friendlist.add(id);
	}
	*/
	
}

