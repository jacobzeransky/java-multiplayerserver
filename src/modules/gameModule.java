package modules;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import connection.connectionPool;
import exceptions.DatabaseError;

public class gameModule extends databaseModule{

	public int startGame(String p1, String p2) throws DatabaseError{
		sql = "insert into mpdb.game (player1, player2, start_time) values ('"+p1+"', '"+p2+"', '"+(new Timestamp(jdate.getTime())).toString()+"')";
		
		int[] ret = connection.executeGameCreation(sql);
		if (ret[0] == 0){
			throw new DatabaseError("Start game, no table entry");
		}
		if (ret[1] == -1){
			throw new DatabaseError("Start game, invalid id");
		}
		
		sql = "update user_state set status=2, status_id="+ret[1]+" where u_name = '"+p1+"'";
		if (connection.executeStatement(sql) != 1){
			throw new DatabaseError("Start game, player 1 not updated");
		}
		sql = "update user_state set status=2, status_id="+ret[1]+" where u_name = '"+p2+"'";
		if (connection.executeStatement(sql) != 1){
			throw new DatabaseError("Start game, player 2 not updated");
		}
		
		return ret[1];
	}
	
	public boolean endGame(int id, int outc) throws DatabaseError{
		sql = "select player1, player2 from game where gameid="+id;
		ResultSet rs = connection.executeSelect(sql);
		
		try {
			if (rs.next()){
				sql = "update game set status="+outc+", end_time='"+(new Timestamp(jdate.getTime())).toString()+"' where gameid="+id;
				if (connection.executeStatement(sql) != 1){
					throw new DatabaseError("End game, entry not updated id: "+id);
				}
				
				sql = "update user_state set status=0, status_id=null where u_name = '"+rs.getString(1)+"'";
				if (connection.executeStatement(sql) != 1){
					throw new DatabaseError("End game, player 1 not updated");
				}
				
				sql = "update user_state set status=0, status_id=null where u_name = '"+rs.getString(2)+"'";
				if (connection.executeStatement(sql) != 1){
					throw new DatabaseError("End game, player 2 not updated");
				}
				connection.closeStmt();
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			connection.closeStmt();
			throw new DatabaseError("End game: "+e.getMessage());
		}
		
		return false;
	}
	
	public static void main(String[] args){
		gameModule gmod = new gameModule();
		
		
		try {
			int g;
			g = gmod.startGame("detra", "joebob");
			System.out.println("g: "+g);
			System.out.println(gmod.endGame(g, 3));
		} catch (DatabaseError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gmod.close();
		connectionPool.getInstance().shutDown();
	}
}
