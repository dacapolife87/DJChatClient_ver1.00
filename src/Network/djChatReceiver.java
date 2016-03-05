package Network;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.StringTokenizer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import TalkRoom.djChatRoomHandler;
import AddFriend.djChatAddFriend;
import ChatClient.djChatDlgMsg;
import ChatClient.djChatJoin;
import ChatClient.djChatLogin;
import ChatClient.djChatSingleton;
import ChatClient.djChatWindow;
import DB.djChatMakeDB;

public class djChatReceiver implements Runnable{
	djChatSingleton obj_Singleton = djChatSingleton.getInstance();
	djChatLogin obj_Login;
	djChatWindow obj_Window;
	djChatNetwork obj_Network;
	djChatDlgMsg obj_DlgMsg;
	djChatJoin obj_Join;
	djChatRoomHandler obj_RoomHandler;
	djChatAddFriend obj_AddFriend;
	
//	private static final String SEPARATOR = "|"; //메시지 구분자
//    private static final String DELEMETER = " '"; //소메시지 구분자
	//Client에 보내는 메시지
//    private static final int RES_USER_YLOGIN = 2011;
//    private static final int RES_USER_NLOGIN = 2012;
//    private static final int RES_USER_DISCONNECT = 2013;
//    private static final int RES_FRIEND_LIST = 2021;
//    private static final int RES_TALK_MSG = 2031;
//    private static final int RES_TALK_EXITROOM = 2032;
//    private static final int RES_TALK_MAKEROOM = 2033;
//    private static final int RES_TALKROOM_MSG = 2034; //대화방 내에 메시지
//    private static final int RES_TALKROOM_UPDATE = 2035; //대화방내에 변화를 업데이트하는 패킷
//    private static final int RES_TALK_ROOMUPDATE = 2037; //대화방 내용을 업데이트시키다.
//    private static final int RES_TALK_INVITE = 2038; //기존 대화방에서 초대된 사용자들에게 받은 패킷
//
//    
//    private static final int RES_USER_YJOIN = 2014;
//    private static final int RES_USER_IDEXIST = 2015;
//    private static final int RES_USER_IDNOTEXIST = 2016;
//  //추가
//    private static final int RES_USER_NICKEXIST = 2017;
//    private static final int RES_USER_NICKNOTEXIST = 2018;
//    
//    private static final int RES_FRIEND_IDEXIST = 2022;
//    private static final int RES_FRIEND_IDNOTEXIST = 2023;
//    private static final int RES_FRIEND_REQUEST = 2024;
//    private static final int RES_FRIEND_ACCEPT = 2025;
//    private static final int RES_FRIEND_REJECT = 2026;
//    private static final int RES_FRIEND_DELETE = 2027;
//    private static final int RES_FRIEND_CONNECT = 2028; //새로 추가한 상수
//    private static final int RES_FRIEND_RECOMMANDLIST = 2029;
//    
	Socket soc;
	DataInputStream dis;
	
	public djChatReceiver(){
		obj_Singleton.setObjReceiver(this);
		callObjLogin();
		callObjNetwork();
		soc = obj_Network.getSocket();
		makeInputStream(soc);
		
		callObjTalkRoom();
	}
	
	public void callObjNetwork(){
		obj_Network = obj_Singleton.getObjNetwork();
	}
	public void callObjWindow(){
		obj_Window = obj_Singleton.getObjWindow();
	}
	public void callObjTalkRoom(){
		obj_RoomHandler = obj_Singleton.getObjTalkRoom();
	}
	public void callObjLogin(){
		obj_Login = obj_Singleton.getObjLogin();
	}
	public void callObjDlgMsg(){
		obj_DlgMsg = obj_Singleton.getObjDlgMsg();
	}
	public void callObjJoin(){
		obj_Join = obj_Singleton.getObjJoin();
	}
	public void callObjAddFriend(){
		obj_AddFriend=obj_Singleton.getObjAddFreind();
	}
	
	public void makeInputStream(Socket soc){
		try {
			dis = new DataInputStream(new BufferedInputStream(soc.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void closeInputStream(){
		try {
			dis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void checkJSONPacket(String data){
		String separator=null;
		JSONObject receiveData =null;
		
		JSONParser jsonParser = new JSONParser();
		try {
			receiveData = (JSONObject) jsonParser.parse(data);
			separator = (String) receiveData.get("SEPARATOR");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(obj_DlgMsg==null){
			callObjDlgMsg();
		}
		System.out.println("client receive");
		System.out.println("-> "+separator);
		System.out.println("-> "+data);
		switch(separator){
		case "RES_USER_YLOGIN":
			obj_Login.closeWindow();
			obj_Window.loadWindow(receiveData);
			try {
				new djChatMakeDB("test1");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "RES_USER_NLOGIN":
			obj_Login.setUserID(null);
			obj_Login.setUserPassword(null);

			obj_DlgMsg.printMsg("ID 또는  Password 가 잘못되었습니다.");
			break;
		case "RES_USER_YJOIN":
			obj_DlgMsg.printMsg("회원가입 완료 ");
			if(obj_Join==null){
				callObjJoin();
			}
			obj_Join.closeWindow();
			break;
		case "RES_USER_NJOIN":
			obj_DlgMsg.printMsg("회원가입 실패 ");
			break;
		case "RES_USER_IDNOTEXIST":
			obj_DlgMsg.printMsg("사용가능한 ID입니다.");
			break;
		case "RES_USER_IDEXIST":
			obj_DlgMsg.printMsg("존재하는 ID입니다.");
			break;
		case "RES_USER_NICKNOTEXIST":
			break;
		case "RES_USER_NICKEXIST":
			break;
		case "RES_FRIEND_LOGIN":
			break;
		case "RES_FRIEND_IDEXIST":
			if(obj_AddFriend==null){
				callObjAddFriend();
			}
			obj_AddFriend.searchResult(receiveData);
			break;
		case "RES_FRIEND_IDNOTEXIST":
			break;
		case "RES_FRIEND_ADD":
			obj_Window.addFriendList(receiveData);
			break;
		case "RES_FRIEND_ADDFAIL":
			obj_DlgMsg.printMsg("이미 등록된 친구 입니다.");
			break;
		case "RES_FRIEND_DEL":
			
			break;
		case "RES_FRIEND_RECOMMANDLIST":
			obj_Window.showRecommandFriendList(receiveData);
			obj_Window.addRecommandFriendJSON();
			break;
		//////////////talk
		case "RES_TALK_MSG":
			obj_Window.printMsg(data);
			break;
		case "RES_TALK_MAKEROOM":
			//System.out.println("djChatReciever RES_TALK_MKEROOM : " +data);
			// data = index , My_Id , Join_UserId
			makeTalkRoom(receiveData);
			break;
		case "RES_TALKROOM_MSG":
			//data = index '대화내용..
			//System.out.println("djChatReciever RES_TALKROOM_MSG : " +data);
			obj_RoomHandler.takenRoomMsg(receiveData);
			break;
		case "RES_TALKROOM_UPDATE":
			//System.out.println("djChatReciever RES_TALKROOM_UPDATE : " +data);
			obj_RoomHandler.updateUserID(receiveData);
			break;
		case "RES_FRIEND_CONNECT":
			//System.out.println("djChatReceiver " + data);
			obj_Window.addFriendList(receiveData);
			break;
		case "RES_FRIEND_LIST":
			//System.out.println("data 수신 : "+data);
			obj_Window.showFriendList(receiveData);
			//obj_Window.showRecommandFriendList(data);
			break;
		case "RES_TALK_ROOMLIST":
			obj_RoomHandler.remakeTalkRoom(receiveData);
			break;
		case "RES_TALK_NOTREADMSG":
			System.out.println("client msg : "+receiveData);
			String timeStr = "2015-08-26 02:47:41.0";
			System.out.println("timeStr : "+timeStr);
			System.out.println("timeStr type : "+timeStr.getClass());
			
			Timestamp time;
			time = Timestamp.valueOf(timeStr);
			
			System.out.println("time : "+time);
			System.out.println("time type : "+time.getClass());
			obj_RoomHandler.notReadMsg(receiveData);
			break;
		}
	}
//	public void checkPacket(String msg){
//		StringTokenizer st = new StringTokenizer(msg,SEPARATOR);
//		int command = 0;
//		String data=null;
//		if(msg!=null){
//			command = Integer.parseInt(st.nextToken());
//			if(st.hasMoreTokens()){
//				data = st.nextToken();
//			}
//		}
//		if(obj_DlgMsg==null){
//			callObjDlgMsg();
//		}
//		switch(command){
//		case RES_USER_YLOGIN:
//			obj_Login.closeWindow();
//			obj_Window.loadWindow();
//			System.out.println("login");
//			break;
//		case RES_USER_NLOGIN:
//			obj_Login.setUserID(null);
//			obj_Login.setUserPassword(null);
//
//			obj_DlgMsg.printMsg("ID 또는  Password 가 잘못되었습니다.");
//			break;
//		case RES_USER_YJOIN:
//			obj_DlgMsg.printMsg("회원가입 완료 ");
//			if(obj_Join==null){
//				callObjJoin();
//			}
//			obj_Join.closeWindow();
//			break;
//		case RES_USER_IDEXIST:
//			obj_DlgMsg.printMsg("존재하는 ID입니다.");
//			break;
//		case RES_USER_IDNOTEXIST:
//			obj_DlgMsg.printMsg("사용가능한 ID입니다.");
//			break;
//		case RES_USER_NICKEXIST:
//			obj_DlgMsg.printMsg("존재하는 별명입니다.");
//			break;
//		case RES_USER_NICKNOTEXIST:
//			obj_DlgMsg.printMsg("사용가능한 별명입니다.");
//			break;
//		case RES_TALK_MSG:
//			obj_Window.printMsg(data);
//			break;
//		case RES_TALK_MAKEROOM:
//			System.out.println("djChatReciever RES_TALK_MKEROOM : " +data);
//			// data = index , My_Id , Join_UserId
//			//makeTalkRoom(data);
//			break;
//		case RES_TALKROOM_MSG:
//			//data = index '대화내용..
//			System.out.println("djChatReciever RES_TALKROOM_MSG : " +data);
//			//obj_RoomHandler.takenRoomMsg(data);
//			break;
//		case RES_TALKROOM_UPDATE:
//			System.out.println("djChatReciever RES_TALKROOM_UPDATE : " +data);
//			//obj_RoomHandler.updateUserID(data);
//			break;
//		case RES_TALK_INVITE:
//			//초대된 사용자들의 채팅방을 띄어주는 함수 호출
//			break;
//		case RES_FRIEND_LIST:
//			System.out.println("djChatReceiver checkReceivePacket() " + data);
//			//obj_Window.showFriendList(data);
//			break;
//		case RES_FRIEND_CONNECT:
//			System.out.println("djChatReceiver " + data);
//			//obj_Window.addFriendList(data);
//			break;
//		case RES_FRIEND_RECOMMANDLIST:
//			System.out.println("recommandlist : "+data);
//			//obj_Window.showRecommandFriendList(data);
//			break;
//		case RES_FRIEND_REQUEST:
//			if(obj_DlgReqFriend==null){
//				obj_DlgReqFriend = obj_Singleton.getObjDlgReqFriend();
//			}
//			System.out.println("req msg : "+data);
//			obj_DlgReqFriend.printMsg(data);
//			break;
//		case RES_FRIEND_ACCEPT:
//			obj_DlgMsg.printMsg(data+"님께서 친구요청을 수락하셨습니다.");
//			break;
//		case RES_FRIEND_IDNOTEXIST:
//			obj_DlgMsg.printMsg("존재 하지않는 사용자 입니다.");
//			break;
//		}
//	}
//	
	//처음 대화방 개설시 
	public void makeTalkRoom(JSONObject json){
		if(obj_RoomHandler == null){
			callObjTalkRoom();
		}
		obj_RoomHandler.makeTalkRoom(json);
	}
	
	//기존의 대화방에 사용자가 초대될때 호출되는 함수
	public void makeInviteTalkRoom(String data){
		if(obj_RoomHandler == null){
			callObjTalkRoom();
		}
		
	}
	
//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//        readMsg();
//	}
//	
//	public void readMsg(){
//		
//		if(obj_Window==null){
//			callObjWindow();
//		}
//		String msg = null;
//		try {
//			while (!Thread.interrupted()) {
//			msg = dis.readUTF();
//			checkJSONPacket(msg);
//			//checkPacket(msg);
//			}
//		} catch (IOException e){
//			// TODO Auto-generated catch block
//			obj_Network.stopNetwork();			
//			
//			System.out.println("exception");
//			e.printStackTrace();
//			
//		}
//	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
        readMsg();
		
	}
	
	public void readMsg(){
		
		if(obj_Window==null){
			callObjWindow();
		}
		String msg = null;
		try {
			while (!Thread.interrupted()) {
			msg = dis.readUTF();
			checkJSONPacket(msg);
			//checkPacket(msg);
			}
			
		} catch (IOException e){
			// TODO Auto-generated catch block
			obj_Network.stopNetwork();			
			
			System.out.println("exception : "+e);
			e.printStackTrace();
			
		}
	}
}