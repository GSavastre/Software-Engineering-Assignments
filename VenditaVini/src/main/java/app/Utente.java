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
	
	public static ArrayList<Vino> RicercaVino(Vino vino) {
		
		String riga = null;
		String[] dettagliVino;
		ArrayList<Vino> vini = null;
		//Apro il file dei vini
		try(BufferedReader fin = new BufferedReader(new FileReader(files.fileVini))){
			
			vini = new ArrayList<Vino>();
			
			while((riga = fin.readLine())!= null) {
				if(!riga.startsWith("#")) {
					try {
						dettagliVino = riga.split(",");
						
						if(vino.nome.isBlank() && vino.anno == 0) {
							vini.add(new Vino(dettagliVino));
						}else {
							if(dettagliVino[0].contains(vino.nome) || Integer.parseInt(dettagliVino[1]) == vino.anno){
								vini.add(new Vino(dettagliVino));
							}
						}
						
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}catch(Exception e) {
			e.getMessage();
		}
		
		return vini;
	}
	
	//TODO: Implementa acquisto del vino con ritorno di un ordine
	public Ordine AcquistaVino(Vino vino, int quantita) {
		//Parametri di vendita -> this, vino
		//return new Ordine();
		Ordine ordine = null;
		
		if(quantita < 0 || vino.nome.isBlank() || vino.nome.isBlank()) {
			return null;
		}
		
		vino = RicercaVino(vino).get(0);
		
		if(vino.numeroBottiglie < quantita) {
			//TODO: Inizializza ordine non completo
		}else {
			//TODO: Inizia un ordine completo
		}
		
		return ordine;
	}
}
