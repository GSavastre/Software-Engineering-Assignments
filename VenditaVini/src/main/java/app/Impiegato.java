package app;

import java.io.BufferedReader;
import java.io.FileReader;

public class Impiegato extends Persona{

	public Impiegato() {
		super();
	}
	
	public Impiegato(String nome, String cognome, String email, String password) {
		super(nome, cognome, email, password);
	}
	
	
	//TODO: Aggiungere throws
	public Impiegato(String[] credenziali) {
		super(credenziali[0], credenziali[1], credenziali[2], credenziali[3]);
	}
	
	/*
	 * Spedisce un certo tipo di vino ad un utente, queste informazioni sono memorizzate all'interno di un ordine
	 * return : true -> Il vino è stato spedito correttamente
	 * return : false -> Non si è riusciti a spedire il vino
	 * TODO: Exception handling
	 */
	public boolean SpedisciVino(Ordine ordine, int quantita) {
		
		if(ordine.venditore == null) {
			ordine.SetImpiegato(this);
		}
		
		
		if(ordine.spediti < ordine.richiesti) {
			int daSpedire = ordine.richiesti - ordine.spediti;
			if(daSpedire >= quantita) {
				return ordine.SpedisciVini(quantita);
			}else {
				return ordine.SpedisciVini(daSpedire);
			}
		}
		
		return false;
	}
	
	/*
	 * Rifornisce la quantità disponibile di un certo vino
	 * return : true -> Il vino è stato aggiunto correttamente
	 * return : false -> Non è stato possibile aggiungere il vino al deposito
	 */
	public boolean RifornisciVino(Vino vino, int quantita) {
		if(vino.Rifornisci(quantita) > 0) {
			return true;
		}
		
		return false;
	}
	
	public static Impiegato RicercaImpiegato(String mail) {
		
		String riga = null;
		String[] dettagliPersona;
		//Apro il file dei vini
		try(BufferedReader fin = new BufferedReader(new FileReader(files.fileUtenti))){
			
			while((riga = fin.readLine())!= null) {
				if(!riga.startsWith("#") && !riga.isBlank()) {
					try {
						dettagliPersona = riga.split(",");
						
						if(dettagliPersona[2].contentEquals(mail)) {
							return new Impiegato(dettagliPersona);
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
