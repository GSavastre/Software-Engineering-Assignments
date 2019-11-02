package app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
		
		if(quantita <= 0 || vino.nome.isBlank()) {
			return null;
		}
		
		vino = Vino.RicercaVino(vino).get(0);
		
		try {
			ordine =  new Ordine(vino, this, quantita);
			if() {
				
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Impossibile creare un ordine");
		}finally {
			return ordine;
		}
	}
	
	public void MostraNotifiche() {
		//TODO
	}
}
