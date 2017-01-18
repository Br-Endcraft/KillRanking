package me.jonasxpx.killranking.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import me.jonasxpx.killranking.None;

public class DBManager {
	
	private Connection conn;
	
	public DBManager(String host, String username, String db, String password) throws SQLException {
			conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + db, username, password);
			conn.prepareStatement("CREATE TABLE IF NOT EXISTS killranking(username VARCHAR(32), kills INT(16), PRIMARY KEY(username))").execute();
	}
	
	public int update(String username, int kills){
		try{
			PreparedStatement pre = conn.prepareStatement("UPDATE killranking SET kills = ? WHERE username = ?");
			pre.setInt(1, kills);
			pre.setString(2, username);
			return pre.executeUpdate();
		}catch(SQLException e){e.printStackTrace();}
		return -1;
	}
	
	public void register(String username){
		try{
			PreparedStatement pre = conn.prepareStatement("INSERT INTO killranking VALUES(?, ?)");
			pre.setString(1, username);
			pre.setInt(2, 0);
			pre.executeUpdate();
		}catch(SQLException e){e.printStackTrace();}
	}
	public void register(String username, int preKills){
		try{
			PreparedStatement pre = conn.prepareStatement("INSERT INTO killranking VALUES(?, ?)");
			pre.setString(1, username);
			pre.setInt(2, preKills);
			pre.executeUpdate();
		}catch(SQLException e){e.printStackTrace();}
	}
	
	public boolean contains(String username){
		try{
			PreparedStatement pre = conn.prepareStatement("SELECT * FROM killranking WHERE username = ?");
			pre.setString(1, username);
			ResultSet r = pre.executeQuery();
			if(r.next())
				return true;
			else
				return false;
		}catch(SQLException e){e.printStackTrace();}
		return false;
	}
	
	public int get(String username){
		try{
			PreparedStatement pre = conn.prepareStatement("SELECT kills FROM killranking WHERE username = ?");
			pre.setString(1, username);
			ResultSet r = pre.executeQuery();
			return r.next() ? r.getInt(1) : -1;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return -1;
	}
	
	public static void main(String[] args) {
		HashMap<String, None> cache = new HashMap<>();
		cache.put("JonasXPX", new None(1));
		cache.values().forEach(p -> System.out.println(p.getV()));
		None n = cache.get("JonasXPX");
		n.setV(6246);
		cache.values().forEach(p -> System.out.println(p.getV()));
	}
	
}
