//Savastre Cosmin Gabriele 283110
package app;

import java.io.BufferedReader;
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
	
	
	/*
	 * Inizializza un ordine di acquisto di un vino, se il vino non è disponibile verrà passata la scelta di notifica
	 * Se il vino è disponibile la scelta di default è di non inviare una notifica 
	 * TODO: Da fixare, un utente può ordinare una quantità di vino che è disponibile ma mentre aspetta viene esaurita
	 * nel caso il vino fosse esaurito prima che fosse spedito all'utente non verrà inviata la notifica di rifornimento
	 * perché la scelta di default è no ('n')
	 */
	public Ordine AcquistaVino(Vino vino, int quantita, char sceltaNotifica) {
		Ordine ordine = null;
		
		try {
			ordine =  new Ordine(vino, this, quantita, sceltaNotifica);
			ordine.SalvaSuFile();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Impossibile creare un ordine");
		}
		
		return ordine;
	}
	
	
	//Guarda RicercaImpiegato
	public static Utente RicercaUtente(String mail) {
		
		String riga = null;
		String[] dettagliPersona;
		//Apro il file dei vini
		try(BufferedReader fin = new BufferedReader(new FileReader(files.fileUtenti))){
			
			while((riga = fin.readLine())!= null) {
				//Ignora commenti e righe vuote
				if(!riga.startsWith("#") && !riga.isBlank()) {
					try {
						dettagliPersona = riga.split(",");
						
						if(dettagliPersona[2].contentEquals(mail)) {
							return new Utente(dettagliPersona);
						}
						
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}catch(Exception e) {
			e.getMessage();
		}
		
		return null;
	}
}
