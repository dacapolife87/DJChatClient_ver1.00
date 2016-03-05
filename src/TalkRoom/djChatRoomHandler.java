package TalkRoom;

import java.util.HashMap;
import java.util.StringTokenizer;

import org.json.simple.*;
import org.json.simple.parser.*;

import RoomList.djChatRoomList;
import ChatClient.djChatSingleton;

public class djChatRoomHandler {
	djChatSingleton obj_Singleton = djChatSingleton.getInstance();
	djChatRoomList obj_RoomList;
	//��ȭ�� ��ü�� index�� �����Ͽ� HashMap�� ���� �� �����Ѵ�.
	String My_Id;
	private HashMap<String, djChatTalkRoom> talkRoom = new HashMap();
	
	public djChatRoomHandler(){
		obj_Singleton.setObjTalkRoom(this); //�̱��Ͽ� ���
	}
	public void callObjRoomList(){
		if(obj_RoomList == null){
		obj_RoomList = obj_Singleton.getObjRoomList();
		}
	}
	
	public void callTalkRoom(String index){
		djChatTalkRoom roomNum = talkRoom.get(index);
		roomNum.recallTalkRoom();
	}
	
	public HashMap getTalkRoom(){
		return talkRoom;
	}
	public void makeTalkRoom(JSONObject json){
		JSONObject jsonData = (JSONObject)json.get("DATA");
		String index = (String)jsonData.get("INDEX");
		
		My_Id = (String)json.get("SENDER");
		
		talkRoom.put(index, new djChatTalkRoom(My_Id));
		djChatTalkRoom roomNum = talkRoom.get(index);
		
		//roomNum.set_Myid(My_Id);
		
		JSONArray jsonArray = (JSONArray)json.get("RECEIVER");
//		JSONObject temp = (JSONObject)json.get("DATA");
//		JSONArray jsonArray = (JSONArray)json.get(key)
		roomNum.printJoinUser(jsonArray); //��ȭ�濡 ���� ����� ����� �����ֱ�
		roomNum.callTalkRoomWindow(index); //��ȭ�� ��Ͽ� ����ϱ�
		
	}
	public void takenRoomMsg(JSONObject json){
		JSONObject jsonData = (JSONObject)json.get("DATA");
		String index = (String)jsonData.get("INDEX");
		String sender_Id = (String)json.get("SENDER");
		String msg = (String)jsonData.get("MESSAGE");
		djChatTalkRoom roomNum = talkRoom.get(index);
		roomNum.printMsg(sender_Id,msg);
		//roomNum.printMsg("[ " + sender_Id + " ] " + msg);	

		if(obj_RoomList == null){
            callObjRoomList();
        }
        obj_RoomList.setRoomMsg(index, sender_Id, msg);
	}
	public void notReadMsg(JSONObject json){
		JSONObject chatRoomMsg = (JSONObject)json.get("DATA");
		JSONArray chatRooMMsgARRAY = new JSONArray();
		JSONObject chatMsg = new JSONObject();
		chatRooMMsgARRAY = (JSONArray) chatRoomMsg.get("CHATROOMMSG");
		for(int i =0; i<chatRooMMsgARRAY.size();i++){
			chatMsg = (JSONObject) chatRooMMsgARRAY.get(i);
			takenRoomMsg(chatMsg);
		}
	}
	public void exitTalkRoom(String index){
		talkRoom.remove(index); //���� ��ȭ���� HashMap���� �����Ѵ�.
		callObjRoomList();
		obj_RoomList.delTalkRoomList(index); //��ȭ�� ��� ����
		
	}
	public void updateUserID(JSONObject json){
		JSONObject jsonData = (JSONObject)json.get("DATA");
		String index = (String)jsonData.get("INDEX");
		JSONArray jsonArray = (JSONArray)json.get("RECEIVER");
		djChatTalkRoom roomNum = talkRoom.get(index);
		roomNum.printJoinUser(jsonArray);
	}


	public void remakeTalkRoom(JSONObject json){
        String My_Id = (String)json.get("SENDER");
        JSONObject jsonData = (JSONObject)json.get("DATA");
        JSONArray roomList = (JSONArray)jsonData.get("ROOMINFO");
        String index;
        String roomName;
        long num;
        JSONObject roomInfo;
        JSONArray peopleArray;
        System.out.println("roomList : "+roomList);
        
        

        for(int i=0; i<roomList.size(); i++){
            roomInfo = (JSONObject)roomList.get(i);
            System.out.println("roomInfo : "+roomInfo);
            
            roomName = (String)roomInfo.get("ROOMNAME");
            num = (long) roomInfo.get("ROOMINDEX");
            System.out.println(" num : "+roomInfo.get("ROOMINDEX"));
            index = Integer.toString((int)num); //�̰� �Ƚᵵ �ǳ�????
            peopleArray = (JSONArray)roomInfo.get("PEOPLEARRAY");
            talkRoom.put(index, new djChatTalkRoom(My_Id));
            djChatTalkRoom roomNum = talkRoom.get(index);
            roomNum.printJoinUser(peopleArray);
            roomNum.reJoinTalkRoom(index);
            if(obj_RoomList == null){
                callObjRoomList();
            }
            if(roomName != null){
                obj_RoomList.addTalkRoomList(index, My_Id, roomName);
            }else if(roomName == null && peopleArray.size() != 0){
                obj_RoomList.addTalkRoomList(index, My_Id, peopleArray);
            }else if(peopleArray.size() == 0 && roomName == null){
                obj_RoomList.addTalkRoomList(index, My_Id, "�˼� ����");
            }
        }
    }


}
