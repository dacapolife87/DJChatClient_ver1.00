package ChatClient;

import AddFriend.djChatAddFriend;
import Network.djChatNetwork;
import Network.djChatReceiver;
import TalkRoom.djChatRoomHandler;
import RoomList.djChatRoomList;

public class djChatSingleton {
	private volatile static djChatSingleton obj_Singleton;
	
	/// 변수선언
	private djChatLogin obj_Login;
	private djChatWindow obj_Window;
	private djChatNetwork obj_Network;
	private djChatReceiver obj_Receiver;
	private djChatJoin obj_Join;
	private djChatRoomHandler obj_RoomHandler;
	private djChatRoomList obj_RoomList;
	
	private djChatDlgMsg obj_DlgMsg;
	private djChatFriendTree obj_FrinedTree;
	
	private djChatAddFriend obj_AddFriend;
//	private djChatDlgAddFriend obj_DlgAddFriend;
//	private djChatDlgReqFriendMsg obj_DlgReqFriendMsg;
//	private djChatDlgAcceptFriend obj_DlgAcceptFriend;

	
	private djChatSingleton(){}
	
	public synchronized static djChatSingleton getInstance(){
		if(obj_Singleton == null){
			synchronized (djChatSingleton.class) {
				if(obj_Singleton == null){
					obj_Singleton = new djChatSingleton();
				}
			}
		}
		return obj_Singleton;
	}
	
	// 변수 저장
	public void setObjLogin(djChatLogin obj_Login){
		this.obj_Login = obj_Login;
	}
	public void setObjWindow(djChatWindow obj_Window){
		this.obj_Window = obj_Window;
	}
	public void setObjNetwork(djChatNetwork obj_Network){
		this.obj_Network = obj_Network;
	}
	public void setObjReceiver(djChatReceiver obj_Receiver){
		this.obj_Receiver = obj_Receiver;
	}
	public void setObjJoin(djChatJoin obj_Join){
		this.obj_Join = obj_Join;
	}
	public void setObjDlgMsg(djChatDlgMsg obj_DlgMsg){
		this.obj_DlgMsg = obj_DlgMsg;
	}
	public void setObjTalkRoom(djChatRoomHandler obj_RoomHandler){
		this.obj_RoomHandler = obj_RoomHandler;
	}
	public void setObjFriendTree(djChatFriendTree obj_FrinedTree){
		this.obj_FrinedTree = obj_FrinedTree;
	}
	public void setObjAddFriend(djChatAddFriend obj_AddFriend){
		this.obj_AddFriend = obj_AddFriend;
	}
	public void setObjRoomList(djChatRoomList djChatRoomList){
		this.obj_RoomList = djChatRoomList;
	}
//	public void setObjDlgAddFriend(djChatDlgAddFriend obj_DlgAddFriend){
//		this.obj_DlgAddFriend = obj_DlgAddFriend;
//	}
//	public void setObjDlgReqFriendMsg(djChatDlgReqFriendMsg obj_DlgReqFriendMsg){
//		this.obj_DlgReqFriendMsg = obj_DlgReqFriendMsg;
//	}
//	public void setObjDlgAcceptFriend(djChatDlgAcceptFriend obj_DlgAcceptFriend){
//		this.obj_DlgAcceptFriend = obj_DlgAcceptFriend;
//	}

	// 변수호출
	public djChatLogin getObjLogin(){
		return obj_Login;
	}
	public djChatWindow getObjWindow(){
		return obj_Window;
	}
	public djChatNetwork getObjNetwork(){
		return obj_Network;
	}
	public djChatReceiver getObjReceiver(){
		return obj_Receiver;
	}
	public djChatJoin getObjJoin(){
		return obj_Join;
	}
	public djChatDlgMsg getObjDlgMsg(){
		return obj_DlgMsg;
	}
	public djChatRoomHandler getObjTalkRoom(){
		return obj_RoomHandler;
	}
	public djChatFriendTree getObjFreindTree(){
		return obj_FrinedTree;
	}
	public djChatAddFriend getObjAddFreind(){
		return obj_AddFriend;
	}
	public djChatRoomList getObjRoomList(){
		return obj_RoomList;
	}
	
//	public djChatDlgAddFriend getObjDlgAddFriend(){
//		return obj_DlgAddFriend;
//	}
//	public djChatDlgReqFriendMsg getObjDlgReqFriend(){
//		return obj_DlgReqFriendMsg;
//	}
//	public djChatDlgAcceptFriend getObjDlgAcceptFriend(){
//		return obj_DlgAcceptFriend;
//	}
//	
}
