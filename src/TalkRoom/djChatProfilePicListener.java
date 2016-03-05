//package TalkRoom;
//
//import java.awt.Component;
//import java.awt.Point;
//import java.awt.Rectangle;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//
//import javax.swing.JButton;
//import javax.swing.JList;
//import javax.swing.SwingUtilities;
//
//public class djChatProfilePicListener extends MouseAdapter {
//    private int prevIndex = -1;
//    private JButton prevButton;
//    private final JList<Msg> listMsg;
//    
//    public djChatProfilePicListener(JList<Msg> listMsg) {
//        super();
//        this.listMsg = listMsg;
//    }
//
//    @Override
//    public void mousePressed(MouseEvent e) {
//        //JList list = (JList) e.getComponent();
//        Point pt = e.getPoint();
//        int index = listMsg.locationToIndex(pt);
//        if (index >= 0) {
//            JButton button = getButton(listMsg, pt, index);
//            if (button!=null) {
//            	djChatMsgRenderer renderer = (djChatMsgRenderer) listMsg.getCellRenderer();
//                renderer.pressedIndex = index;
//                renderer.button = button;
//                listRepaint(listMsg, listMsg.getCellBounds(index, index));
//            }
//        }
//    }
//    @Override public void mouseReleased(MouseEvent e) {
//        //JList list = (JList) e.getComponent();
//        Point pt = e.getPoint();
//        int index = listMsg.locationToIndex(pt);
//        if (index >= 0) {
//            JButton button = getButton(listMsg, pt, index);
//            if (button!=null) {
//            	djChatMsgRenderer renderer = (djChatMsgRenderer) listMsg.getCellRenderer();
//                renderer.pressedIndex = -1;
//                renderer.button = null;
//                button.doClick();
//                listRepaint(listMsg, listMsg.getCellBounds(index, index));
//            }
//        }
//    }
//    private static void listRepaint(JList list, Rectangle rect) {
//        if (rect!=null) {
//        	System.out.println("list repaint");
//            list.repaint(rect);
//        }
//    }
//    
//    private static JButton getButton(JList<Msg> list, Point pt, int index) {
//        Component c = list.getCellRenderer().getListCellRendererComponent(list, ((Msg)value).getName(), index, false, false);
//        Rectangle r = list.getCellBounds(index, index);
//        c.setBounds(r);
//        //c.doLayout(); //may be needed for mone LayoutManager
//        pt.translate(-r.x, -r.y);
//        Component b = SwingUtilities.getDeepestComponentAt(c, pt.x, pt.y);
//        if (b instanceof JButton) {
//            return (JButton) b;
//        }else {
//        	//System.out.println("test123456");
//            return null;
//        }
//    }
//}
//
//
