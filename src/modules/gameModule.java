package modules;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import connection.connectionPool;

public class gameModule extends databaseModule{

	public int startGame(String p1, String p2){
		sql = "insert into mpdb.game (player1, player2, start_time) values ('"+p1+"', '"+p2+"', '"+(new Timestamp(jdate.getTime())).toString()+"')";
		
		int[] ret = connection.executeGameCreation(sql);
		if (ret[0] == 0){
			System.out.println("error starting game");
			return -1;
		}
		if (ret[1] == -1){
			System.out.println("error generating game id");
			return -1;
		}
		
		sql = "update user_state set status=2, status_id="+ret[1]+" where u_name = '"+p1+"'";
		if (connection.executeStatement(sql) != 1){
			System.out.println("error updating player 1");
			return -1;
		}
		sql = "update user_state set status=2, status_id="+ret[1]+" where u_name = '"+p2+"'";
		if (connection.executeStatement(sql) != 1){
			System.out.println("error updating player2");
			return -1;
		}
		
		return ret[1];
	}
	
	public boolean endGame(int id, int outc){
		sql = "select player1, player2 from game where gameid="+id;
		ResultSet rs = connection.executeSelect(sql);
		
		try {
			if (rs.next()){
				sql = "update game set status="+outc+", end_time='"+(new Timestamp(jdate.getTime())).toString()+"' where gameid="+id;
				if (connection.executeStatement(sql) != 1){
					System.out.println("error updating game "+id);
					return false;
				}
				
				sql = "update user_state set status=0, status_id=null where u_name = '"+rs.getString(1)+"'";
				if (connection.executeStatement(sql) != 1){
					System.out.println("error updating player 1");
					return false;
				}
				
				sql = "update user_state set status=0, status_id=null where u_name = '"+rs.getString(2)+"'";
				if (connection.executeStatement(sql) != 1){
					System.out.println("error updating player 2");
					return false;
				}
				connection.closeStmt();
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			connection.closeStmt();
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
	
	public static void main(String[] args){
		gameModule gmod = new gameModule();
		
		int g = gmod.startGame("detra", "joebob");
		System.out.println("g: "+g);
		System.out.println(gmod.endGame(g, 3));
		gmod.close();
		connectionPool.getInstance().shutDown();
	}
}
