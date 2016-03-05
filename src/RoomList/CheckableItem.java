package RoomList;

import org.json.simple.JSONArray;

class CheckableItem{
	private String My_Id;
	private String index;
	private String roomName;
	private boolean isSelected;
	private JSONArray users;
	private String Msg;
	public CheckableItem(String index, String My_Id, String roomName){
		this.My_Id = My_Id;
		this.index = index;
		this.users = users;
		this.roomName = roomName;
		
		isSelected = false;

	}
	public void setMsg(String Msg){
        this.Msg = Msg;
    }
    public String getMsg(){
        return Msg;
    }
	public String getMy_Id(){
		return My_Id;
	}
	public void setSelected(boolean check){
		isSelected = check;
	}
	public boolean isSelected(){
		return isSelected;
	}
	public void setName(String roomName){
		this.roomName = roomName;
	}
	public String getName(){
		return roomName;
	}
	public String getIndex(){
		return index;
	}
}