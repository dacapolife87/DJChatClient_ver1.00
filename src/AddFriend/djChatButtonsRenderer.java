package AddFriend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

import org.json.simple.JSONObject;

import ChatClient.djChatDlgMsg;
import Network.djChatNetwork;

public class djChatButtonsRenderer<E> extends JPanel implements ListCellRenderer<E>{
    private static final Color EVEN_COLOR = new Color(230, 255, 230);
    //public JTextArea label = new JTextArea();
    
    
    djChatNetwork obj_Network;
    String my_Id;
    private final JTextArea label = new JTextArea();
   
   
    private final JButton addButton = new JButton(new AbstractAction("추가") {
        @Override
        public void actionPerformed(ActionEvent e) {
        	// 아이디값 친구 추가
        	JSONObject dataJSON = new JSONObject();
			dataJSON.put("DATA", model.getElementAt(index));
			obj_Network.makeJSONPacket("REQ_FRIEND_RECOMMANDADD", dataJSON, my_Id,null);
            if (model.getSize() > 0) {
                model.remove(index);
            }
        }
    });
    private final DefaultListModel<E> model;
    private int index;
    public int pressedIndex  = -1;
    public int rolloverIndex = -1;
    public JButton button;
    
    public djChatButtonsRenderer(DefaultListModel<E> model,djChatNetwork obj_Network,String my_Id) {
        super(new BorderLayout());
        this.model = model;
        this.obj_Network = obj_Network;
        this.my_Id = my_Id;
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        setOpaque(true);
        label.setLineWrap(true);
        label.setOpaque(false);
        //label.addMouseListener(this);
        
        add(label);

        
        
        Box box = Box.createHorizontalBox();
        JButton b = null;
        b = addButton;
        b.setFocusable(false);
        b.setRolloverEnabled(false);
        box.add(b);
        box.add(Box.createHorizontalStrut(5));
        
        add(box, BorderLayout.EAST);
    }
    @Override public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean hasFocus) {
        label.setText(Objects.toString(value, ""));
//        System.out.println("test : "+value);
//        djChatDlgMsg test=new djChatDlgMsg();
//        test.printMsg(Objects.toString(value, ""));
        this.index = index;
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            label.setForeground(list.getSelectionForeground());
        } else {
            setBackground(index % 2 == 0 ? EVEN_COLOR : list.getBackground());
            label.setForeground(list.getForeground());
        }
        resetButtonStatus();
        if (button!=null) {
            if (index == pressedIndex) {
                button.getModel().setSelected(true);
                button.getModel().setArmed(true);
                button.getModel().setPressed(true);
            } else if (index == rolloverIndex) {
                button.getModel().setRollover(true);
            }
        }
        return this;
    }
    private void resetButtonStatus() {
    	JButton b;
    	b = addButton;
        ButtonModel m = b.getModel();
        m.setRollover(false);
        m.setArmed(false);
        m.setPressed(false);
        m.setSelected(false);
    }
}
