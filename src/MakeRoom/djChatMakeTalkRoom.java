package MakeRoom;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import org.json.simple.JSONArray;

import ChatClient.*;
import Network.djChatNetwork;

public class djChatMakeTalkRoom extends JFrame implements ActionListener , MouseListener{
	private static final String DELEMETER = " '"; //소메시지 구분자
	private static final int REQ_TALK_MAKEROOM = 1031;
	
	private String My_ID;
	
	private JPanel jp1;
	private JPanel jp2;
	
	private JList friendList;
	private JButton inviteBtn;
	private JButton cancelBtn;
//	protected DefaultListModel onlineList;
	
	djChatSingleton obj_Singleton = djChatSingleton.getInstance();
	djChatNetwork obj_Network = obj_Singleton.getObjNetwork();
	
	
	
	public djChatMakeTalkRoom(DefaultMutableTreeNode online , String My_ID) {
		// TODO Auto-generated constructor stub
		super("Invite TalkRoom");
		this.My_ID = My_ID; //초대하는 사람의 ID를 받아온다.
		
		CheckableItem[] item = createData(online);
		
		jp1 = new JPanel(new BorderLayout());
		friendList = new JList(item);
		friendList.setCellRenderer(new CheckBoxListCellRenderer());
		friendList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		friendList.setBorder(new EmptyBorder(0,4,0,0));
		friendList.addMouseListener(this);
		
		jp1.add(friendList, BorderLayout.CENTER);
		
		jp2 = new JPanel(new GridLayout(1,2));
		inviteBtn = new JButton("초대");
		inviteBtn.addActionListener(this);
		
		cancelBtn = new JButton("취소");
		cancelBtn.addActionListener(this);
		
		jp2.add(inviteBtn); jp2.add(cancelBtn);
		
		jp1.add(jp2, BorderLayout.SOUTH);
		
		add(jp1);
		
		setSize(300,600);
		setVisible(true);
	}
	
	private CheckableItem[] createData(DefaultMutableTreeNode online) {
		Enumeration en = online.depthFirstEnumeration();
		String user;
		int size = online.getChildCount();
		CheckableItem[] items = new CheckableItem[size];
		for (int i=0;i<size;i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)en.nextElement();
			user = String.valueOf(node.getUserObject());
			items[i] = new CheckableItem(user);
		}
		return items;
	}

	class CheckBoxListCellRenderer extends djChatCheckRenderer implements ListCellRenderer{

		Icon commonIcon;
		
		public CheckBoxListCellRenderer(){
			check.setBackground(UIManager.getColor("List.textBackground"));
			label.setForeground(UIManager.getColor("List.textForeground"));
			commonIcon = UIManager.getIcon("Tree.leafIcon");
		}
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean hasFocus) {
			// TODO Auto-generated method stub
			setEnabled(list.isEnabled());
			check.setSelected(((CheckableItem)value).isSelected());
			label.setFont(list.getFont());
			label.setText(value.toString());
			label.setSelected(isSelected);
			label.setFocus(hasFocus);
			Icon icon = ((CheckableItem)value).getIcon();
			if(icon == null){
				icon = commonIcon;
			}
			label.setIcon(icon);
			return this;
		}
		
	}
	
	class CheckableItem{
		private String str;
		private boolean isSelected;
		private Icon icon;
		
		public CheckableItem(String str){
			this.str = str;
			isSelected = false;
		}
		public void setSelected(boolean check){
			isSelected = check;
		}
		public boolean isSelected(){
			return isSelected;
		}
		public String toString(){
			return str;
		}
		public void setIcon(Icon icon){
			this.icon = icon;
		}
		public Icon getIcon(){
			return icon;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if(event.getActionCommand().equals("초대")){
			JSONArray jsonReceiver = new JSONArray();
			ListModel modeltest = friendList.getModel();
			int m = modeltest.getSize();
			for(int i=0; i<m; i++){
				CheckableItem testitem = (CheckableItem)modeltest.getElementAt(i);
				if(testitem.isSelected){
					jsonReceiver.add(testitem.toString());
				}
			}
			obj_Network.makeJSONPacket("REQ_TALK_MAKEROOM", null, My_ID, jsonReceiver);
			dispose();
		}else if(event.getActionCommand().equals("취소")){
			dispose();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int index = friendList.locationToIndex(e.getPoint());
		CheckableItem item = (CheckableItem)friendList.getModel().getElementAt(index);
		item.setSelected(!item.isSelected());
		Rectangle rect = friendList.getCellBounds(index, index);
		friendList.repaint(rect);
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
