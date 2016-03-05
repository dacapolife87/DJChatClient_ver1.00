package RoomList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.json.simple.JSONArray;

import ChatClient.djChatSingleton;
import TalkRoom.djChatRoomHandler;
import TalkRoom.djChatTalkRoom;
import ChatClient.djChatSingleton;

public class djChatRoomList extends JPanel implements MouseListener{
	djChatSingleton obj_Singleton = djChatSingleton.getInstance();
	djChatRoomHandler obj_RoomHandler = obj_Singleton.getObjTalkRoom();
	
	JList talkRoomList;
	private DefaultListModel model;
	
	public djChatRoomList(){
		super(new BorderLayout());
		obj_Singleton.setObjRoomList(this);
		model = new DefaultListModel();
		
		talkRoomList = new JList(model);
		talkRoomList.setCellRenderer(new talkRoomRenderer());
		talkRoomList.addMouseListener(this);
		add(talkRoomList);
	}

public void setRoomMsg(String index, String SenderId, String Msg){
        CheckableItem[] items = new CheckableItem[model.size()];
        for(int i=0; i<model.size(); i++){
            System.out.println("NUM : " + model.get(i));
            items[i] = (CheckableItem)model.get(i);
        }
        System.out.println("Receive Index : " +index);
        System.out.println("====================");
        for(int i=0; i<items.length; i++){
            System.out.println(" Second For ");
            System.out.println(" items[i] getIndex : " +items[i].getIndex());
            String index2 = items[i].getIndex();
            if(index.equals(index2)){
                System.out.println("NUM INdex : " +index);
                CheckableItem item = (CheckableItem)talkRoomList.getModel().getElementAt(i);
                item.setMsg(Msg);
                listRefresh();
                break;
            }
        }
    }



	public void addTalkRoomList(String index , String My_Id , String roomName){
		CheckableItem item = new CheckableItem(index, My_Id, roomName);
		model.addElement(item);
	}

	public void addTalkRoomList(String index, String My_Id, JSONArray people){
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<people.size(); i++){
			sb.append(people.get(i));
			sb.append(" ");
		}
		CheckableItem item = new CheckableItem(index, My_Id, sb.toString());
		model.addElement(item);
		
	}
	
	public void delTalkRoomList(String index){
		System.out.println("delTalkRoomList Test");
		CheckableItem[] items = new CheckableItem[model.size()];
		for(int i=0; i<model.size(); i++){
			System.out.println("NUM : " + model.get(i));
			items[i] = (CheckableItem)model.get(i);
		}
		
		for(int i=0; i<items.length; i++){
			if(index == items[i].getIndex()){
				model.removeElementAt(i);
				break;
			}
		}		
	}
	public void listRefresh(){
		talkRoomList.updateUI();
	}
		

class talkRoomRenderer extends JPanel implements ListCellRenderer{


        private JLabel label;

        private JLabel label2;

        private Font f1;

        public talkRoomRenderer(){

            super(new GridLayout(2,1));

            label = new JLabel();

            label.setSize(200, 50);

            

            label2 = new JLabel();

            label2.setSize(200, 50);

            f1 = new Font("Serif", Font.ITALIC, 20);

            add(label);

            add(label2);

            setOpaque(true);

        }
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
               int index, boolean isSelected, boolean cellHasFocus) {
            // TODO Auto-generated method stub
            String str = ((CheckableItem)value).getName(); //참여자들 
            String msg = ((CheckableItem)value).getMsg();
        //    String msg = "TestMsg";
            label.setText(str);
            label2.setText(msg);
            if(index % 2 == 0){
            setBackground(Color.yellow);
            }else{
                setBackground(Color.gray);
            }
            //일단 구분자
            label.setFont(f1);
            label.setSize(100,100);
        //    setSize(200,200);
            label2.setSize(100, 100);
            label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
            label2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
            return this;
        }
    }

	@Override
	public void mouseClicked(MouseEvent event) {
		// TODO Auto-generated method stub
		if(event.getModifiers() == event.BUTTON1_MASK){
			try{
				int index = talkRoomList.locationToIndex(event.getPoint());
				CheckableItem item = (CheckableItem)talkRoomList.getModel().getElementAt(index);
				obj_RoomHandler.callTalkRoom(item.getIndex());
			}catch(Exception ie){
				System.out.println("Null");
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent event) {
		// TODO Auto-generated method stub
		if(event.getModifiers() == event.BUTTON3_MASK){
			int index = talkRoomList.locationToIndex(event.getPoint());
			CheckableItem item = (CheckableItem)talkRoomList.getModel().getElementAt(index);
			new djChatRoomInfo(item);
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
}
