import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//Group5(LuYu & XuezhiLi)-The product p1 is deleted from Product and Stock. 

public class Group5 {
	public static void main(String args[]) throws SQLException, IOException, ClassNotFoundException {
	
		Class.forName("com.mysql.jdbc.Driver");
				
		String myUrl = "jdbc:mysql://localhost:3306/FinalProject";

		Connection conn = DriverManager.getConnection(myUrl, "root", "lxz910825"); 

		//For atomicity
		conn.setAutoCommit(false);

		// For isolation 
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE); 
		
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate("DROP TABLE IF EXISTS Stock CASCADE");
			stmt.executeUpdate("DROP TABLE IF EXISTS Product CASCADE");
			stmt.executeUpdate("DROP TABLE IF EXISTS Depot CASCADE");
			
			//Create Table
			stmt.executeUpdate("Create table IF NOT EXISTS Product("
					+ "prod_id CHAR(10),"
					+ "pname VARCHAR(30),"
					+ "price DECIMAL,"
					+ "PRIMARY KEY (prod_id),"
					+ "CHECK (price > 0)"
					+ ")");
			stmt.executeUpdate("Create table IF NOT EXISTS Depot("
					+ "dep_id CHAR(10),"
					+ "addr VARCHAR(30),"
					+ "volume INTEGER,"
					+ "PRIMARY KEY (dep_id),"
					+ "CHECK (volume > 0)"
					+ ")");
			stmt.executeUpdate("Create table IF NOT EXISTS Stock("
					+ "prod_id CHAR(10),"
					+ "dep_id CHAR(10),"
					+ "quantity INTEGER,"
					+ "PRIMARY KEY (prod_id, dep_id),"
					+ "FOREIGN KEY (prod_id) REFERENCES Product (prod_id) ON UPDATE CASCADE," 
					+ "FOREIGN KEY (dep_id) REFERENCES Depot (dep_id) ON UPDATE CASCADE"
					+ ")");
			
			stmt.executeUpdate("INSERT INTO Product (prod_id, pname, price) Values" 
					+ "('p1', 'tape', 2.5)," 
					+ "('p2', 'tv', 250), "
					+ "('p3', 'vcr', 80);");
			stmt.executeUpdate("INSERT INTO Depot (dep_id, addr, volume) Values" 
					+ "('d1', 'New Yrok', 9000)," 
					+ "('d2', 'Syracuse', 6000), "
					+ "('d4', 'New Yrok', 2000);");
			stmt.executeUpdate("INSERT INTO Stock (prod_id, dep_id, quantity) Values" 
					+ "('p1', 'd2', -100)," 
					+ "('p1', 'd4', 1200)," 
					+ "('p3', 'd1', 3000)," 
					+ "('p3', 'd4', 2000)," 
					+ "('p2', 'd4', 1500)," 
					+ "('p2', 'd1', -400)," 
					+ "('p2', 'd2', 2000);");

			//output before update
			System.out.println("Before Update");
			ResultSet a = stmt.executeQuery("select * from Product");
			System.out.println("Product");
			System.out.println("prod_id " + "pname " + "price");
			while(a.next()) {
				System.out.println( a.getString("prod_id") 
						 + "\t " + a.getString("pname")
						 + "\t " + a.getInt("price"));
			} 
			
			ResultSet b = stmt.executeQuery("select * from Stock");
			System.out.println("\nStock");
			System.out.println("prod_id " + "dep_id " + "quantity");
			while(b.next()) {
				System.out.println(b.getString("prod_id") 
						+ "\t " + b.getString("dep_id") 
						+ "\t " + b.getInt("quantity"));
			} 
			
			
			//output after update
			System.out.println("After Update");
			stmt.executeUpdate("DELETE FROM Stock WHERE prod_id = 'p1'");
			stmt.executeUpdate("DELETE FROM Product WHERE prod_id = 'p1'");
			
			ResultSet c = stmt.executeQuery("select * from Product");
			System.out.println("Product");
			System.out.println("prod_id " + "pname " + "price");
			while(c.next()) {
				System.out.println( c.getString("prod_id") 
						+ "\t " + c.getString("pname") 
						+ "\t " + c.getInt("price"));
			} 
			
			ResultSet d = stmt.executeQuery("select * from Stock");
			System.out.println("\nStock");
			System.out.println("prod_id " + "dep_id " + "quantity");
			while(d.next()) {
				System.out.println(d.getString("prod_id") 
						+ "\t " + d.getString("dep_id") 
						+ "\t " + d.getInt("quantity"));
			} 
			
		} catch (Exception e) {
			System.out.println( e);
			conn.rollback();
			stmt.close();
			conn.close();
			return;
		} 
		conn.commit();
		stmt.close();
		conn.close();
	}
}