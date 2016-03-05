package ChatClient;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class djChatDlgMsg extends JFrame implements ActionListener{
	
	djChatSingleton obj_Singleton = djChatSingleton.getInstance();
	
	Dialog msgBox;
	JLabel fLabel; 
	JPanel jp1;
	JPanel jp2;
	JButton ok;
	
	public djChatDlgMsg() {
		// TODO Auto-generated constructor stub
		obj_Singleton.setObjDlgMsg(this);
		
		jp1 = new JPanel();
		jp2 = new JPanel();
		msgBox = new Dialog(msgBox);
		msgBox.setSize(300, 150);
		msgBox.setLocation(50,50);
		msgBox.setLayout(new BorderLayout());
		fLabel = new JLabel();
		ok = new JButton("확인");
		ok.addActionListener(this);
		
		jp1.setLayout(new FlowLayout());
		jp1.add(fLabel);
		jp2.setLayout(new FlowLayout());
		jp2.add(ok);
		
		msgBox.add(jp1,BorderLayout.CENTER);
		msgBox.add(jp2,BorderLayout.SOUTH);
		
		msgBox.setVisible(false);	
	}
	

	public void loadWindow(){
		msgBox.setVisible(true);
	}
	public void printMsg(String msg){
		fLabel.setText(msg);
		loadWindow();
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if(event.getActionCommand().equals("확인"))
		{
			//msgBox.setVisible(false);
			msgBox.dispose();
			//요청 버튼 클릭시 이벤트 처리
		}
	}
	
}
