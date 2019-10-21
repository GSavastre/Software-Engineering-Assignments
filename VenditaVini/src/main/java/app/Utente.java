package app;

public class Utente extends Persona{

	public Utente(String nome, String cognome, String email, String password) {
		super(nome, cognome, email, password);
	}

	/*
	 * Effettua il processo di registrazione per un nuovo utente
	 * solo se la password � stata ripetuta correttamente
	 * 
	 * return: oggetto Utente oppure null se registrazione � fallita
	 */
	public Utente Registrazione(String nome, String cognome, String email, String password, String passwordRepeat) {
		
		if(password.contentEquals(passwordRepeat)) {
			return new Utente(nome, cognome, email, password);
		}
		
		return null;
	}
	
	//TODO: Implementa acquisto del vino con ritorno di una vendita
	public Vendita AcquistaVino(Vino vino) {
		//Parametri di vendita -> this, vino
		return new Vendita();
		
		//Se il vino non � pi� disponibile ritorna null
	}
}
