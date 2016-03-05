package RoomList;

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
import ChatClient.djChatSingleton;

public class RoomName extends JFrame implements ActionListener{
	private JLabel label;
	private JTextField textField;
	private JPanel panel;
	private JButton btn1;
	private JButton btn2;
	private CheckableItem item;
	
	djChatNetwork obj_Network;
	djChatSingleton obj_Singleton;
	djChatRoomList obj_RoomList;
	
	public RoomName(CheckableItem item){
		obj_Singleton = djChatSingleton.getInstance();
		callObjRoomList();
		this.item = item;
		setLayout(new GridLayout(3,1));
		label = new JLabel("변경할 제목을 입력하세요");
		textField = new JTextField(30);
		
		panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		btn1 = new JButton("확인");
		btn1.addActionListener(this);
		panel.add(btn1);
		
		btn2 = new JButton("취소");
		btn2.addActionListener(this);
		panel.add(btn2);
		
		add(label);
		add(textField);
		add(panel);
		setSize(300,200);
		setVisible(true);
	}
	public void callObjRoomList(){
		this.obj_RoomList = obj_Singleton.getObjRoomList();
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if(event.getActionCommand().equals("확인")){
			obj_Network = obj_Singleton.getObjNetwork();
			item.setName(textField.getText());
			obj_RoomList.listRefresh();
			JSONObject jsonData = new JSONObject();
			jsonData.put("INDEX", item.getIndex());
			jsonData.put("ROOMNAME", item.getName());
			obj_Network.makeJSONPacket("REQ_TALK_RENAME", jsonData, item.getMy_Id(), null);
			dispose();
		}else if(event.getActionCommand().equals("취소")){
			dispose();
		}
	}
}
