package DB;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class djChatMakeDB {
	Statement statement;
	Connection connection = null;
	
	public void connDB(String userId) throws ClassNotFoundException{
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:src/"+userId+"/"+userId+".db");
			statement = connection.createStatement();
			System.out.println("conn db");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createTable(){
		String query1 = "create table friendList (num integer,id string,name string,statusMsg string,photo string)";
		String query2 = "create table chatRoomList(num integer,chatRoomNum integer,id string,talkRoomName string,lastMsgTime string,photo string)";
		try {
			System.out.println("stmt : "+statement);
			statement.executeUpdate(query1);
			statement.executeUpdate(query2);
			System.out.println("make table db");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("error : "+e);
			e.printStackTrace();
		}
	}
	public djChatMakeDB(String userId) throws ClassNotFoundException{
		//String folder = "src/"+userId;
		String folder = "src/"+userId;
		File desti = new File(folder);
		
		if(!desti.exists()){
			desti.mkdirs();
			connDB(userId);
			createTable();
			System.out.println("test db");
		}
		else{
			connDB(userId);
			try {
				statement.executeUpdate("insert into friendList values(1, 'leo', 'leo', 'leo', 'leo')");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
