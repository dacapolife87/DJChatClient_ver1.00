package AddFriend;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ChatClient.djChatSingleton;
import Network.djChatNetwork;

public final class djChatAddFriend extends JPanel implements ActionListener{
	djChatSingleton obj_Singleton = djChatSingleton.getInstance();
	djChatNetwork obj_Network;
	
	DefaultListModel<String> model;
	
	String my_Id;
	private JPanel jp1;
	private JPanel jp2;
	private JPanel jp3;
	private JPanel jp4;
	
	private JTextField friendIdInput;
	private JTextArea searchResult;
	private JList friendList;
	private JButton serachBtn;
	private JButton addBtn;
	
    public djChatAddFriend(String my_Id) {
        super(new BorderLayout());
        System.out.println("add friend");
        
        this.my_Id = my_Id;
        obj_Singleton.setObjAddFriend(this);
        callObjNetwork();
        
        JFrame frame = new JFrame("친구추가");
        
        model = new DefaultListModel<>();

        jp1 = new JPanel(new BorderLayout());
		jp3 = new JPanel(new BorderLayout());
		jp2 = new JPanel(new GridLayout(1,2));
		serachBtn = new JButton("검색");
		serachBtn.addActionListener(this);
		addBtn = new JButton("추가");
		addBtn.addActionListener(this);
		addBtn.setEnabled(false);
		searchResult = new JTextArea();
		searchResult.setEditable(false);
		friendIdInput = new JTextField("검색할 ID를 입력하세요");
		
		jp2.add(serachBtn); jp2.add(addBtn);
		jp4 = new JPanel(new GridLayout(2, 1));
		jp4.add(jp2);
		jp4.add(searchResult);
		
		jp3.add(friendIdInput, BorderLayout.NORTH);
		
		jp3.add(jp4, BorderLayout.SOUTH);
		
		jp1.add(jp3, BorderLayout.NORTH);
		/////////// 
        JList<String> list = new JList<>(model);
        list.setFixedCellHeight(-1);
        djChatCellButtonsMouseListener cbml = new djChatCellButtonsMouseListener(list);
        list.addMouseListener(cbml);
        list.addMouseMotionListener(cbml);
        list.setCellRenderer(new djChatButtonsRenderer<String>(model,obj_Network,my_Id));
        
        
        setPreferredSize(new Dimension(320, 240));
        jp1.add(new JScrollPane(list),BorderLayout.CENTER);
        frame.getContentPane().add(jp1);
        frame.pack();
        frame.setSize(200, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public void addElement(JSONObject friendInfo){
        int arraySize=0;
        JSONObject dataJSON = new JSONObject();
	    JSONArray friendListJSON = new JSONArray();

	    dataJSON =(JSONObject) friendInfo.get("DATA");
	    friendListJSON = (JSONArray) dataJSON.get("RECOMMANDFRIENDLIST");
	    
	    arraySize = friendListJSON.size();

	    for(int i=0;i<arraySize;i++)
	    {
	    	model.addElement((String) friendListJSON.get(i));
	    }
    }
    public void callObjNetwork(){
		obj_Network = obj_Singleton.getObjNetwork();
	}
    public void searchResult(JSONObject data){
		addBtn.setEnabled(true);
		JSONObject dataJSON = new JSONObject();
		String searchId;
		dataJSON = (JSONObject) data.get("DATA");
		
		searchId = (String) dataJSON.get("DATA");
		searchResult.setText(searchId);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("검색")){
			JSONObject dataJSON = new JSONObject();
			dataJSON.put("DATA", friendIdInput.getText());

			obj_Network.makeJSONPacket("REQ_FRIEND_EXIST", dataJSON, my_Id,null);
		}
		if(e.getActionCommand().equals("추가")){
			addBtn.setEnabled(false);
			JSONObject dataJSON = new JSONObject();
			dataJSON.put("DATA", searchResult.getText());
			obj_Network.makeJSONPacket("REQ_FRIEND_ADD", dataJSON, my_Id,null);
			searchResult.setText("");
		}
	}
}

