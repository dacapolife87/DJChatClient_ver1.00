package ChatClient;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.util.Enumeration;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;


public class djChatFriendTree extends JPanel implements TreeSelectionListener {
	djChatSingleton obj_Singleton = djChatSingleton.getInstance();
	
	TreePath selectTreepath;
	
	public DefaultMutableTreeNode rootNode; //模备格废
	protected DefaultTreeModel treemodel; //TreeModel
	public static boolean checkOnline = false; //Online咯何 犬牢 函荐
	
	protected JTree tree;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	public djChatFriendTree(){
		super(new BorderLayout());
		
		rootNode = new DefaultMutableTreeNode("模备格废");
		
		treemodel = new DefaultTreeModel(rootNode);

		tree = new JTree(treemodel);
		tree.setEditable(true);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.addTreeSelectionListener(this);
		
		JScrollPane scrollPane = new JScrollPane(tree);
		add(scrollPane);

		obj_Singleton.setObjFriendTree(this);

	}
	public String dellObject(){
		String friendId;
		treemodel.removeNodeFromParent((DefaultMutableTreeNode)selectTreepath.getPathComponent(1));
		friendId = selectTreepath.getPathComponent(1).toString();
		return friendId;
	}
	public DefaultMutableTreeNode addObject(Object child){
		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = null;
		
		if(parentPath == null){
			parentNode = rootNode;
		}else{
			
		}
		
		return addObject(parentNode, child, true);
	}
	
	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child){
		return addObject(parent, child, false);
	}
	
	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child, boolean shoudBeVisible){
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
		
		if(parent == null){
			parent = rootNode;
		}
		
		treemodel.insertNodeInto(childNode, parent, parent.getChildCount());
		if(shoudBeVisible){
			tree.scrollPathToVisible(new TreePath(childNode.getPath()));
		}
		return childNode;
	}
	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		// TODO Auto-generated method stub
		selectTreepath = arg0.getPath();
	}
}
