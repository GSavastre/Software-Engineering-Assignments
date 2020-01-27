package app.auth;

import java.sql.*;

public class Auth {

	private static String URL = "jdbc:mysql://mysql-souldp.alwaysdata.net:3306/souldp_softw_ing_assegnamento_4?";
	private static String ARGS = "serverTimezone=UTC";
	private static String USER = "souldp";
	private static String PASSWORD = "NonGuardarmiLaPassword";
	
	public static void main(String args[]) {
		//System.out.println(Login("savastrecosmingabriele@gmail.com", "test"));
		/*if(Register("test","test","test","pwd","pwd","socio")) {
			System.out.println("Register ok");
		}else {
			System.out.println("Register error");
		}*/
	}
	
	//Effettua il login dell'utente e ritorna la stringa ruolo oppure ritorna stringa vuota se il login fallisce
	public static String Login(String email, String password) {
		
		try(Connection conn = DriverManager.getConnection(URL + ARGS, USER, PASSWORD);
				Statement stmt = conn.createStatement();){
			String strSelect = "SELECT * FROM utenti WHERE email='"+email+"';";
			
			ResultSet rset = stmt.executeQuery(strSelect);
			
			while(rset.next()) {
				String rsEmail = rset.getString("email");
				String rsPassword = rset.getString("password");
				String rsRuolo = rset.getString("ruolo");
				
				if(email.contentEquals(rsEmail) && password.contentEquals(rsPassword)) {
					return rsRuolo;
				}
				
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return "";
	}
	
	//Effettua la registrazione di un nuovo utente sul db, ritorna true se l'account è stato registrato
	//correttamente altrimenti ritorna false
	public static boolean Register(String nome, String cognome, String email, String password, String passwordRpt, String ruolo) {
		
		//Se le password non combaciano non procedere con la registrazione
		if(!password.contentEquals(passwordRpt)) {
			return false;
		}
		nome = String.format("\'%s\'", nome);
		cognome = String.format("\'%s\'", cognome);
		email = String.format("\'%s\'", email);
		password = String.format("\'%s\'", password);
		ruolo = String.format("\'%s\'", ruolo);
		
		try(Connection conn = DriverManager.getConnection(URL + ARGS, USER, PASSWORD);
				Statement stmt = conn.createStatement();){
			String values = String.join(",", nome, cognome, email, password, ruolo);
			String insertString = "INSERT INTO utenti (nome, cognome, email, password, ruolo) VALUES ("+ values+")";
			
			
			int countUpdated = stmt.executeUpdate(insertString);
			
			if(countUpdated != 0) {
				return true;
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return false;
	}
}
