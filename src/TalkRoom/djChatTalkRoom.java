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
	
	//// �߰�
	//CellButtonsMouseListener cbml;
	MouseListener ml1;
	JList<Msg> listMsg;
	DefaultListModel<Msg> model;
	JPanel jp5;
	/////
    private static final String DELEMETER = " '"; //�Ҹ޽��� ������

	
	private String index; //���Ϲ� ��ȣ
	private String My_Id; //�� ���̵� 
	private String Join_UserId; //������ ����� ��� ���̵�
	private boolean activeTalkRoom;
	
	JPanel jp1; //��ü ȭ��
	JPanel jp2; //�Ʒ��� TextField ���� �ǳ�
	JPanel jp3; //���۹�ư �������ư ���� �ǳ�
	JPanel jp4; //MyId, ������ ID �� ���� �ǳ�
	
	JLabel label1; //������ id �����ִ� ��  id1, id2, id3 .... 
	JLabel label2; //My_id
	
	JTextArea textArea; //��ȭ�������
	
	JTextField textField; //ä�� �Է�
	JButton jbtn1; //���۹�ư
	JButton jbtn2; //�ʴ� ��ư
	JButton jbtn3; //������ ��ư
	
	JSONArray jsonArray;
	
	public djChatTalkRoom(String myId){
		obj_Network = obj_Singleton.getObjNetwork();
		obj_RoomHandler = obj_Singleton.getObjTalkRoom();
		obj_FriendTree = obj_Singleton.getObjFreindTree(); //������Ʈ��
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
		
		jbtn1 = new JButton("����");
		jbtn1.addActionListener(this);
		jbtn2 = new JButton("�ʴ�");
		jbtn2.addActionListener(this);
		jbtn3 = new JButton("������");
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
	
	//�ʱ⿡ ��ȭ���� ȣ���Ҷ� ��ȭ�� ��ȣ�� �����Ѵ�.
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
	
	//��ȭ�� ������ �� ���̵� ����
	public void set_Myid(String My_Id){
		//this.My_Id = My_Id;
		label2.setText("MyId : " + My_Id);
	}
	//��ȭ�� ������ ������ ���̵� ����
	public void printJoinUser(JSONArray jsonArray){
		this.jsonArray = jsonArray; //��ȭ�濡 ������ ������� ���̵���� �迭
		StringBuffer sb = new StringBuffer();
		sb.setLength(0);
		for(int i=0; i<jsonArray.size(); i++){
			if(!((String)jsonArray.get(i)).equals(My_Id)){
			sb.append((String)jsonArray.get(i));
			sb.append(", ");
			}
		}
		label1.setText("������ : " + sb.toString());
	//	this.jsonArray = jsonArray;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if(event.getActionCommand().equals("����")){

			JSONObject jsonData = new JSONObject();
			jsonData.put("INDEX", index);
			jsonData.put("MESSAGE", readText());
			
			obj_Network.makeJSONPacket("REQ_TALK_SENDMSG", jsonData, My_Id, null);
			textField.setText("");
			
			//=======================================
		}else if(event.getActionCommand().equals("�ʴ�")){
			//��ȭ�濡�� �ʴ��� ����
			//new djChatMakeTalkRoom(obj_FriendTree.rootNode, My_Id);
			//��ȭ�濡 �̹� �ִ� ������� üũ���� ���� �Ұ��� ����� ���ο� ��ü ȣ��
			//new djChatInviteRoom(obj_FriendTree.rootNode, alreadyUSer)
			//�ٲ�� �� �κ�
			new djChatInviteRoom(obj_FriendTree.rootNode, index , jsonArray);
		}
		else if(event.getActionCommand().equals("������")){
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
