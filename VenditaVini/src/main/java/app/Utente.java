package app;

import java.util.ArrayList;

public class Utente extends Persona{
	
	ArrayList<Ordine> ordini;

	public Utente(String nome, String cognome, String email, String password) {
		super(nome, cognome, email, password);
	}
	
	public Utente(String[] credenziali) {
		super(credenziali[0], credenziali[1], credenziali[2], credenziali[3]);
	}

	public Utente() {
		super();
	}

	/*
	 * Effettua il processo di registrazione per un nuovo utente
	 * solo se la password è stata ripetuta correttamente
	 * 
	 * return: oggetto Utente oppure null se registrazione è fallita
	 */
	public Utente Registrazione(String nome, String cognome, String email, String password, String passwordRepeat) {
		
		if(password.contentEquals(passwordRepeat)) {
			return new Utente(nome, cognome, email, password);
		}
		
		return null;
	}
	
	public Ordine AcquistaVino(Vino vino, int quantita, char sceltaNotifica) {
		//Parametri di vendita -> this, vino
		//return new Ordine();
		Ordine ordine = null;
		
		try {
			ordine =  new Ordine(vino, this, quantita, sceltaNotifica);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Impossibile creare un ordine");
		}
		
		return ordine;
	}
}
