package TalkRoom;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

import org.json.simple.JSONObject;

public class djChatMsgRenderer<E> extends JPanel implements ListCellRenderer<Msg> {
    private static final Color EVEN_COLOR = new Color(230, 255, 230);
    private static final Color YELLOW_COLOR = new Color(255, 255, 0);
    private static final Color LIGHTYELLOW_COLOR = new Color(248, 248, 152);
    private static final Color LIME_COLOR = new Color(200, 248, 48);
    private static final Color GREY_COLOR = new Color(200, 248, 248);
    private static final Color PLUM_COLOR = new Color(200, 152, 248);
    private static final Color SMOKEY_COLOR = new Color(192, 192, 248);
    
    String myId;

    private JTextArea msgName = new JTextArea();
    private JTextArea msgText = new JTextArea();
    private ImageIcon iconImg = new ImageIcon();
    
    JPanel panelText;
    
    public JButton button = new JButton();
    public int pressedIndex  = -1;
    public int rolloverIndex = -1;
    
    private final JButton addButton = new JButton(new AbstractAction("") {
        @Override
        public void actionPerformed(ActionEvent e) {
        	// 아이디값 친구 추가
        	System.out.println("test point");

        }
    });
    
    public djChatMsgRenderer(DefaultListModel<Msg> model,String myId) {
        super(new BorderLayout());
        this.myId = myId;

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        setOpaque(true);
        //panelText = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelText = new JPanel(new GridLayout(0, 1));
        JPanel jp1 = new JPanel(new BorderLayout());
        panelText.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        panelText.add(msgName);
        //panelText.add(msgName,FlowLayout.LEFT);
        panelText.add(msgText);
        
        
        add(button,BorderLayout.WEST);
        add(panelText, BorderLayout.CENTER);
    }

	@Override
	public Component getListCellRendererComponent(JList<? extends Msg> list,
			Msg value, int index, boolean isSelected, boolean cellHasFocus) {
		// TODO Auto-generated method stub
		String name = null;
		String text = null;


		//button.setIcon(new ImageIcon(getClass().getResource("/nguyenvanquan7826/JList/" + ((Msg)value).getIcon() + ".jpg")));

		
		name = ((Msg)value).getName();
		text = ((Msg)value).getText();
		//iconImg = ((Msg)value).getIcon();
		
		msgName.setText(name);
    	msgText.setText(text);

		msgText.setBackground(name.equals(myId) ? SMOKEY_COLOR : LIGHTYELLOW_COLOR);
		msgName.setBackground(name.equals(myId) ? SMOKEY_COLOR : LIGHTYELLOW_COLOR);
		panelText.setBackground(list.getBackground());
		setBackground(GREY_COLOR);
		setForeground(list.getForeground());

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
