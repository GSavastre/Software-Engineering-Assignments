package app.auth;

import java.sql.*;

import app.database.DB;

public class Auth {

	
	
	//Effettua il login dell'utente e ritorna la stringa ruolo oppure ritorna stringa vuota se il login fallisce
	public static String Login(String email, String password) {
		
		try(Connection conn = DriverManager.getConnection(DB.URL + DB.ARGS, DB.USER, DB.PASSWORD);
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
		
		try(Connection conn = DriverManager.getConnection(DB.URL + DB.ARGS, DB.USER, DB.PASSWORD);
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
