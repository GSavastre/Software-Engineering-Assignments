package app.db;

import java.sql.*;

public class DBManager {
	private static String URL = "jdbc:mysql://mysql-souldp.alwaysdata.net:3306/souldp_softw_ing_assegnamento_4?";
	private static String ARGS = "serverTimezone=UTC";
	private static String USER = "souldp";
	private static String PASSWORD = "NonGuardarmiLaPassword";
	
	public static void main(String[] args) {
		
		System.out.println("Loading driver...");

		/*try {
		    Class.forName("com.mysql.cj.jdbc.Driver");
		    System.out.println("Driver loaded!");
		} catch (ClassNotFoundException e) {
		    throw new IllegalStateException("Cannot find the driver in the classpath!", e);
		}*/
		
		try(Connection conn = DriverManager.getConnection(URL + ARGS, USER, PASSWORD);
				Statement stmt = conn.createStatement();){
			String strSelect = "SELECT * FROM utenti";
			System.out.println("Querying : "+strSelect);
			
			ResultSet rset = stmt.executeQuery(strSelect);
			
			int rowCount = 0;
			
			while(rset.next()) {
				String nome = rset.getString("nome");
				String cognome = rset.getString("cognome");
				String email = rset.getString("email");
				String password = rset.getString("password");
				String ruolo = rset.getString("ruolo");
				
				System.out.format("%s %s %s %s %s", nome,cognome,email,password,ruolo);
				rowCount++;
			}
			
			System.out.format("\nThere are %d users", rowCount);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}
