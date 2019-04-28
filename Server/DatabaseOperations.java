
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOperations {

	public Connection getConnection() throws InstantiationException, IllegalAccessException {
		try {
			//Class.forName("org.postgresql.Driver");
			Class.forName ("com.mysql.cj.jdbc.Driver").newInstance ();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;

		}

		Connection connection = null;

		try {

			connection = DriverManager.getConnection("jdbc:mysql://database:3306/idp", "root", "root");

		} catch (SQLException e) {
			e.printStackTrace();
			return null;

		}

		if (connection != null) {
			return connection;
		} else {
			return null;
		}
	}

	public List<Records> selectByName(String name) throws SQLException, InstantiationException, IllegalAccessException {

		Connection c = getConnection();
		Statement stmt = c.createStatement();
		Records r = null;
		List<Records> resultList = new ArrayList<Records>();

		String sql = "SELECT * FROM records r " + "WHERE r.phone = '" + name + "' " + "ORDER BY r.task_date ASC;";

		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			r = new Records(rs.getInt("id"), rs.getString("phone"), rs.getString("task_date"), rs.getString("task"));
			resultList.add(r);
		}

		System.out.println(resultList);
		return resultList;
	}

	public boolean insertRecords(Records r) throws SQLException, InstantiationException, IllegalAccessException {
		Connection c = getConnection();
		Statement stmt = c.createStatement();
		System.out.println("Adding :" + r);

		String sql = "INSERT INTO records( " + "phone, task_date, task ) " + "VALUES ('" + r.phone
				+ "', STR_TO_DATE('" + r.date + "','%d/%m/%Y'), " + "'" + r.task + "');";
		System.out.println("Sql query: " + sql);
		stmt.executeUpdate(sql);
		System.out.println("Succesfully added: " + r);
		return true;
	}
	
	public boolean deleteRecords(String id) throws SQLException, InstantiationException, IllegalAccessException {
		Connection c = getConnection();
		Statement stmt = c.createStatement();

		String sql = "DELETE FROM records WHERE id=" + id +";";
		System.out.println("Sql query: " + sql);
		stmt.executeUpdate(sql);
		System.out.println("Succesfully removed id=: " + id);
		return true;
	}
	
	public boolean intDB() throws SQLException, InstantiationException, IllegalAccessException {
		Connection c = getConnection();
		Statement stmt = c.createStatement();
		
		String sql = "CREATE TABLE IF NOT EXISTS records (id INTEGER PRIMARY KEY AUTO_INCREMENT, phone VARCHAR(50), task_date date, task VARCHAR(50))";
		System.out.println("Sql query: " + sql);
		stmt.executeUpdate(sql);
		System.out.println("Succesfully created: ");
		return true;
	}

}
