package ChatClient;

import Network.djChatNetwork;
import Network.djChatReceiver;
import TalkRoom.djChatRoomHandler;
import RoomList.djChatRoomList;

public class djChatMain {
	public static void main(String[] args) {
		djChatSingleton obj_Singleton = djChatSingleton.getInstance();
		
		djChatLogin obj_Login = new djChatLogin();
		djChatFriendTree obj_FriendTree = new djChatFriendTree();
		djChatRoomHandler obj_RoomHandler = new djChatRoomHandler();
		djChatRoomList obj_RoomList = new djChatRoomList();
		djChatWindow obj_Window = new djChatWindow();
		djChatNetwork obj_Network = new djChatNetwork();
		djChatReceiver obj_Receiver = new djChatReceiver();
		djChatJoin obj_Join = new djChatJoin();
		djChatDlgMsg obj_DlgMsg = new djChatDlgMsg();
		
		
		obj_Login.loadWindow();
	}
}
