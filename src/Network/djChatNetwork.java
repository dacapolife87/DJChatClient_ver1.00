package Network;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ChatClient.djChatSingleton;

public class djChatNetwork {
	djChatSingleton obj_Singleton = djChatSingleton.getInstance();
	djChatReceiver obj_Receiver;
//	djChatWindow obj_Window;
	
	//Client에서 받는 메시지
    private static final String SEPARATOR = "|"; //메시지 구분자
    private static final String DELEMETER = " '"; //소메시지 구분자
    private static final int REQ_USER_LOGIN = 1011;
    private static final int REQ_USER_LOGOUT = 1012;
    private static final int REQ_FRIEND_SHOWLIST = 1021;
    private static final int REQ_TALK_MAKEROOM = 1031;
    private static final int REQ_TALK_BROADCAST = 1032;
    private static final int REQ_TALK_EXITROOM = 1033;
    private static final int REQ_TALK_SENDMSG = 1034; //대화방 내에서 보낼 메시지
    private static final int REQ_TALK_INVITE = 1036; // 대화방 내에서 다른 사용자 초대요청 패킷
    private static final int REQ_USER_JOIN = 1013;
    private static final int REQ_USER_IDEXIST = 1014;
    private static final int REQ_FRIEND_ADDFRIEND = 1022;
    private static final int REQ_FRIEND_REQUEST = 1023;
    private static final int REQ_FRIEND_ACCEPT = 1024;
    private static final int REQ_FRIEND_REJECT = 1025;
    private static final int REQ_FRIEND_DELETE = 1026;

	Thread receiverThread;
	
	private int port = 2345;
	private String ip = "localhost";
	private Socket soc;
	
	///////////sender
	DataOutputStream dos;
	StringBuffer sb;
	////////////////////
	public djChatNetwork() {
		obj_Singleton.setObjNetwork(this);
		// TODO Auto-generated constructor stub
		try {
			soc = new Socket(ip,port);
			makeOutputStream(soc);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	///////////////sender
	public void makeOutputStream(Socket soc){
		try {
			dos = new DataOutputStream(new BufferedOutputStream(soc.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void closeOutputStream(){
		try {
			dos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void makePacket(int res,String data){
		StringBuffer sb = new StringBuffer();
		sb.setLength(0);
		sb.append(res);
		sb.append(SEPARATOR);
		sb.append(data);
		//sendMsg(sb.toString());
	}
	
	public String reformJSON(JSONObject json){
		return json.toJSONString();
	}
	public void makeJSONPacket(String seperator,JSONObject dataJSON,String sender,JSONArray receiver){
		JSONObject packetJSON = new JSONObject();
		packetJSON.put("SEPARATOR", seperator);
		packetJSON.put("DATA", dataJSON);
		packetJSON.put("SENDER", sender);
		packetJSON.put("RECEIVER", receiver);
		
		sendPacket(reformJSON(packetJSON));
	}
	public void sendPacket(String msg){
		try {
			System.out.println("client send packet : ");
			System.out.println("-> "+msg);
			dos.writeUTF(msg);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//////////////////////////////
	public Socket getSocket(){
		return soc;
	}
	
	public void callObjReceiver(){
		obj_Receiver = obj_Singleton.getObjReceiver();
	}
	
	public void startThread(){
		callObjReceiver();
		
		receiverThread = new Thread(obj_Receiver);
		receiverThread.start();

	}
	
//	// 로그인 테스트용 코드
////	public void checkLogin(String id){
////		makePacket(REQ_USER_LOGIN,id);
////	}
	public void talkMsg(String msg){
		makePacket(REQ_TALK_BROADCAST, msg);
	}
//	// 대화방 만들기 요청 패킷을 만드는 함수
//	public void makeTalkRoomPacket(String data){
//		System.out.println("djChatNetwork makeTalkRoomPacket data : " +data);
//		makePacket(REQ_TALK_MAKEROOM, data);
//	}
//	
//	// 대화방 내에서의 채팅메시지를 전달하는 패킷을 만드는 함수
//	public void talkMsgIntoRoom(String data){
//		System.out.println("djChatNetwork talkMsgIntoRoom data : " +data);
//		makePacket(REQ_TALK_SENDMSG, data);
//	}
//	
//	// 대화방 내에서 나가기 버튼을 클릭시ㅣ 패킷을 만드는 함수
//	public void exitTalkRoom(String data){
//		System.out.println("djChatNetworkd exitTalkRoom data : " +data);
//		makePacket(REQ_TALK_EXITROOM, data);
//	}
//	
//	// 대화방 내에서 추가로 초대하기 버튼을 클릭시 패킷을 만드는 함수
//	public void inviteToTalk(String data){
//		System.out.println("djChatNetwork inviteToTalk data : " +data);
//		makePacket(REQ_TALK_INVITE, data);
//	}
//		
	public void stopNetwork(){
		stopReceiverThread();
		obj_Receiver.closeInputStream();
		stopSocket();
	}
	public void stopReceiverThread(){
		if(receiverThread != null){
			if (receiverThread != Thread.currentThread())
				receiverThread.interrupt();
			receiverThread=null;
		}
	}
	
	public void stopSocket(){
		try {
			soc.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
//	// make login data _ start
//	public void sendLoginData(String id,String password){
//		StringBuffer sb = new StringBuffer();
//		sb.setLength(0);
//		sb.append(id);
//		sb.append(DELEMETER);
//		sb.append(password);
//		
//		makePacket(REQ_USER_LOGIN , sb.toString());
//	}
//	// make login data _ end
	
	// make Join data _ start
	public void sendJoinData(String userId,String userPassword,String userName,String userPhoneNum){
		StringBuffer sb = new StringBuffer();
		sb.setLength(0);
		sb.append(userId);
		sb.append(DELEMETER);
		sb.append(userPassword);
		sb.append(DELEMETER);
		sb.append(userName);
		sb.append(DELEMETER);
		sb.append(userPhoneNum);
		System.out.println("sendMsg : "+sb.toString());
		makePacket(REQ_USER_JOIN, sb.toString());
	}
	// make Join data _ end
	
	// exist Id check data _ start
	public void sendExistIdCheckData(String userId){
		StringBuffer sb = new StringBuffer();
		sb.setLength(0);
		sb.append(userId);
		System.out.println("sendMsg : "+sb.toString());
		makePacket(REQ_USER_IDEXIST, sb.toString());
	}
	// exist Id check data _ end
//	// request add friend  data _ start
//	public void sendRequestAddFriendData(String targetUserId){
//		System.out.println("network req add func : "+targetUserId);
//		StringBuffer sb = new StringBuffer();
//		String requestUserId;
//		requestUserId = obj_Window.getUserId();
//		sb.setLength(0);
//		sb.append(requestUserId);
//		sb.append(DELEMETER);
//		sb.append(targetUserId);
//		System.out.println("sendMsg : "+sb.toString());
//		makePacket(REQ_FRIEND_ADDFRIEND, sb.toString());
//	}
//	// accept add friend data _ end
//	public void sendAcceptAddFriendData(String requestUserId){
//		StringBuffer sb = new StringBuffer();
//		String targetUserId;
//		targetUserId = obj_Window.getUserId();
//		sb.setLength(0);
//		sb.append(targetUserId);
//		sb.append(DELEMETER);
//		sb.append(requestUserId);
//		System.out.println("sendMsg : "+sb.toString());
//		makePacket(REQ_FRIEND_ACCEPT, sb.toString());
//	}
//	// accept add friend data _ end
}
