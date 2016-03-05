package RoomList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.AncestorListener;

public class djChatRoomInfo extends JFrame {
	
	JPanel jp1;
	JPanel jp2;
	JPanel jp3;
	JPanel jp4;
	JLabel label1;
	JLabel label2;
	JLabel label3;
	CheckableItem item;
	public djChatRoomInfo(CheckableItem item){
		
		this.item = item;
		
		setLayout(new GridLayout(3,1));
		label1 = new JLabel("Name");
		label1.setBounds(10, 10, 200, 50);
		label1.setBackground(Color.yellow);
		
		label2 = new JLabel("Change");
		label2.setBounds(10, 60, 200, 50);
		label2.addMouseListener(new MouseHandler());
		
		label3 = new JLabel("Exit");
		label3.setBounds(10, 110, 200, 50);
		label3.addMouseListener(new MouseHandler2());
		
		add(label1);
		add(label2);
		add(label3);
		
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
		
		setSize(200,200);
		setVisible(true);
		
	}

	class MouseHandler extends MouseAdapter{
		public void mouseClicked(MouseEvent event){
			System.out.println("Change");
			System.out.println("index : " + item.getIndex());
			new RoomName(item);
			dispose();
		}
	}
	class MouseHandler2 extends MouseAdapter{
		public void mouseClicked(MouseEvent event){
			System.out.println("Exit");
			dispose();
		}
	}
	
}
