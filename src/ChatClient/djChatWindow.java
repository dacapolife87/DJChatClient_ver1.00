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
	JPanel jp4 = new JPanel();  // ������ ���úκ�
	JPanel jp5 = new JPanel();
	
	JButton jbtn1 = new JButton();
	JButton jbtn2 = new JButton();
	JButton jbtn3 = new JButton();
	
//	djChatFriendTree treePanel; // ���Ӱ� ����� Ŭ����
	JLabel myId; //�� ���̵� ���ִ� �󺧺κ�
	JLabel myStatus; //�� ���̵� ���ִ� �󺧺κ�
	///���� ������
	ImageIcon profilePic = new ImageIcon();
	JButton jbtn4 = new JButton(profilePic);
	
	
	List talklist = new List();
	
	//�ӽô�ȭâ
	JPanel tempJp1 = new JPanel();
	JPanel tempJp2 = new JPanel();
	JButton tempJBtn1 = new JButton();
	TextArea tempTextA = new TextArea();
	TextField tempTextF = new TextField();

	// ���� ID
	String My_ID;
	
	public djChatWindow(){
		obj_Singleton.setObjWindow(this);
		callObjFriendTree();
		callObjRoomList();
		
		myId = new JLabel(); //�� ID�� �����ϴ� ��
		myStatus = new JLabel(); // ��ȭ�� ��
		// ���� ���� �г�
		jp4.setLayout(new GridLayout(2, 1));
		jp4.add(myId);
		jp4.add(myStatus);
		jp5.setLayout(new BorderLayout());
		jp5.add(jbtn4,BorderLayout.WEST);
		jp5.add(jp4,BorderLayout.CENTER);
		//
		jbtn1.setText("ģ���߰�");
		jbtn1.addActionListener(this); // ========
		jbtn2.setText("ģ������");
		jbtn2.addActionListener(this); // ========
		jbtn3.setText("��ȭ�游���");
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
		
		jTab.addTab("ģ�����", jp1);
		jTab.addTab("��ȭ��", jp2);
		
		// �ӽô�ȭâ/////////////
		tempJBtn1.setText("����");
		tempJp1.setLayout(new BorderLayout());
		tempJp2.setLayout(new BorderLayout());
		tempJp1.add(tempTextA, BorderLayout.CENTER);
		tempJp2.add(tempTextF,BorderLayout.CENTER);
		tempJp2.add(tempJBtn1,BorderLayout.EAST);
		tempJp1.add(tempJp2,BorderLayout.SOUTH);
		jTab.addTab("�ӽô�ȭâ", tempJp1);
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
		myId.setText(userId); //�� ���̵�� Label�� ����Ѵ�.
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
	
	//ó�� ������������ ��� �����ڵ��� �ҷ����� �Լ�
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
	//������ ����ڸ��� ID�� �޾Ƽ� TREE�� �߰��ϴ� �Լ�
	public void addFriendList(JSONObject receiveData){
		JSONObject dataJSON= new JSONObject();
		String id=null;
		dataJSON=(JSONObject) receiveData.get("DATA");
		id = (String) dataJSON.get("DATA");
		obj_FriendTree.addObject(id);
	}
	//��õģ������� �޾Ƽ� TREE�� ����
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
		if(event.getActionCommand().equals("����")){
			obj_Network.talkMsg("[" +My_ID+ "]" + readText());

			tempTextF.setText("");
		}else if(event.getActionCommand().equals("��ȭ�游���")){
			System.out.println("��ȭ�游��� : "+My_ID);
			new djChatMakeTalkRoom(obj_FriendTree.rootNode, My_ID);
		}else if(event.getActionCommand().equals("ģ���߰�")){
			System.out.println("window _ ģ���߰�  Ŭ��");
			obj_Network.makeJSONPacket("REQ_FRIEND_RECOMMANDLIST",null, My_ID,null);
			//new djChatRecommFriendTable(dataJSON,My_ID);
			addFriend = new djChatAddFriend(My_ID);
			addRecommandFriendJSON();
		}
		else if(event.getActionCommand().equals("ģ������")){
			System.out.println("window _ ģ����ü  Ŭ��");
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

