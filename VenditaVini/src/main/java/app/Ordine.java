package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Ordine {
	public Vino vino;
	public Utente acquirente;
	public Impiegato venditore;
	public int richiesti;
	public int spediti;
	public boolean completato;
	public boolean notifica;
	public LocalDateTime data;
	public static FileManager files = new FileManager();
	
	public Ordine() {
	}
	
	public Ordine(Vino vino, Utente acquirente, int richiesti, char notifica) {
		this.vino = vino;
		this.acquirente = acquirente;
		this.venditore = new Impiegato();
		this.completato = false;
		this.richiesti = richiesti;
		this.spediti = 0;
		if(notifica == 'y') {
			this.notifica = true;
		}else {
			this.notifica = false;
		}
		
		this.data = LocalDateTime.now();
		//SalvaSuFile();
	}
	
	
	/*
	 * Segna l'impiegato che ha preso carico dell'ordine
	 */
	public void SetImpiegato(Impiegato impiegato) {
		venditore = impiegato;
	}
	
	/*
	 * Completa un ordine solo se la quantità di ordini richiesti è uguale a quella di ordini spediti
	 * return : true -> tutti i ordini sono stati forniti
	 * return : false -> mancano ordini da spedire
	 */
	public boolean CompletaOrdine() {
		if(richiesti == spediti) {
			System.out.println("L'ordine di "+vino.nome+" per "+acquirente.nome+" "+acquirente.cognome+" è stato completato!");
			return this.completato = true;
		}
		
		System.out.println("Mancano ancora "+(richiesti - spediti)+" vini da spedire al cliente "+acquirente.email);
		return false;
	}
	
	/*
	 * Spedisci una certa quantità di ordini appartenente ad un ordine
	 */
	public boolean SpedisciVini(int quantita) {
		if(!vino.Rimuovi(quantita)) {
			return false;
		}
		
		spediti += quantita;
		CompletaOrdine();
		Notifica.CreaNotifica(acquirente.email, venditore.email, vino, true);
		
		SalvaSuFile();
		return true;
	}
	/*
	 * Salva l'ordine corrente su file
	 * #dataOrdine,nomevino,annoVino,mailCliente,mailImpiegato,viniRichiesti,viniSpediti,vuoleNotifica
	 */
	public void SalvaSuFile() {
			//Controllo esistenza di questo ordine sul file
			boolean exists = false;
			
			//Lista contenente tutti i ordini già segnati su file
			ArrayList<String[]> contenuti = new ArrayList<String[]>();
			
			//Stringa ricavata dallo Split() della stringa dal file
			String[] contenuto;
			
			//Singola riga del file
			String riga = null;
			
			//Popolazione della lista dei contenuti dei vari ordini
			try(BufferedReader fin = new BufferedReader(new FileReader(files.fileOrdini))){
				
				//Legge tutte le righe del file
				while((riga = fin.readLine()) != null) {
					//Ignora i commenti
					if(!riga.startsWith("#")) {
						try {
							contenuto = riga.split(",");
							
							//LocalDateTime dataFile = LocalDateTime.parse(contenuto[0], formatter);
							LocalDateTime dataFile = LocalDateTime.parse(contenuto[0]);
							String nomeVino = contenuto[1];
							String mailCliente = contenuto[3];
							
							//Un ordine viene considerato duplicato in base alla mail del cliente, il nome del vino ordinato e la data precisa in cui viene effettuata l'ordine
							if(nomeVino.contentEquals(this.vino.nome) && mailCliente == this.acquirente.email && dataFile.isEqual(this.data)) {
								
								exists = true;
								
								//Non aggiorno nomeVino, mailCliente e data perché sono uguali
								
								//Email venditore
								if(this.venditore == null) {
									contenuto[4] = "null";
								}else {
									contenuto[4] = this.venditore.email;
								}
								
								//Numero bottiglie richieste
								contenuto[5] = String.valueOf(this.richiesti);
								
								//Numero bottiglie spediti
								contenuto[6] = String.valueOf(this.spediti);
							}
							
							//Questo sarà il nuovo ordine sovrascritto nel caso sia duplicato altrimenti verrà aggiunto un ordine normalmente
							contenuti.add(contenuto);
							
						}catch(Exception e) {
							e.printStackTrace();
							System.out.println("Errore lettura file ordini");
						}
					}
				}
			}catch(FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("Impossibile trovare il file per il salvataggio degli ordini!");
			}catch(IOException e) {
				e.printStackTrace();
				System.out.println("Errore salvataggio ordini!");
			}
			
			//Se non esiste il vino appendilo alla fine del file
			if(!exists) {
				contenuti.add(this.ToFileString());
			}
			
			//File vecchio da eliminare
			File fvecchio = new File(files.fileOrdini);
			fvecchio.delete();
			
			File fout = new File(files.fileOrdini);
			try {
				FileWriter fnuovo = new FileWriter(fout,false);
				//Scrivo tutti i ordini su file
				for(String[] v : contenuti) {
					fnuovo.write(String.join(",", v)+System.lineSeparator());
				}
				fnuovo.close();
			}catch(IOException e) {
				e.printStackTrace();
				System.out.println("Errore nella sovrascrittura dei ordini");
			}
			
		}
		
	//#dataOrdine,nomevino,annoVino,mailCliente,mailImpiegato,viniRichiesti,viniSpediti,vuoleNotifica
	
	public static ArrayList<Ordine> CaricaDaFile(){
		ArrayList<Ordine> ordini = new ArrayList<Ordine>();
		
		//Singola riga del file
		String riga = null;
		
		try(BufferedReader fin = new BufferedReader(new FileReader(files.fileOrdini))){
			
			while((riga = fin.readLine()) != null) {
				if(!riga.startsWith("#") && !riga.isBlank()) {
					try {
						Ordine ordine = CaricaOrdine(LocalDateTime.parse(riga.split(",")[0]));
						if(ordine != null) {
							ordini.add(ordine);
						}
					}catch(Exception e) {
						e.printStackTrace();
						System.out.println("Errore nella lettura dell'archivio dei ordini");
					}
				}
			}
		}catch(FileNotFoundException e) {
			e.getMessage();
			e.printStackTrace();
			System.out.println("Errore, impossibile trovare l'archivio di ordini");
		}catch(Exception e) {
			e.getMessage();
			e.printStackTrace();
			System.out.println("Errore nel caricamento dell'archivio di ordini");
		}
		
		
		return ordini;
	}
	
	/*
	 * Genera un oggetto di tipo Ordine da info ottenute da un ordine su file
	 */
	private static Ordine CaricaOrdine(LocalDateTime timestampOrdine) {
		Ordine ordine = null;
		String riga = null;
		String[] contenuto;
		LocalDateTime tempoOrdine;
		
		try(BufferedReader fin = new BufferedReader(new FileReader(files.fileOrdini))){
			
			while((riga = fin.readLine()) != null) {
				if(!riga.startsWith("#") && !riga.isBlank()) {
					try {
						contenuto = riga.split(",");
						
						tempoOrdine = LocalDateTime.parse(contenuto[0]);
						
						if(timestampOrdine.isEqual(tempoOrdine)) {
							Vino ricercaVino = Vino.RicercaVino(new Vino(contenuto[1],Integer.parseInt(contenuto[2]))).get(0);
							Utente cliente = Utente.RicercaUtente(contenuto[3]);
							ordine = new Ordine(ricercaVino,cliente,Integer.parseInt(contenuto[5]),contenuto[6].toCharArray()[0]);
						
							if(contenuto[4].contentEquals("null")) {
								ordine.SetImpiegato(null);
							}else {
								ordine.SetImpiegato(Impiegato.RicercaImpiegato(contenuto[4]));
							}
						}
					}catch(Exception e) {
						e.printStackTrace();
						System.out.println("Errore nella lettura dell'archivio dei ordini");
					}
				}
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Errore, impossibile trovare l'archivio di ordini");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Errore nel caricamento dell'archivio di ordini");
		}
		
		return ordine;
	}
		
	public String[] ToFileString() {
		
		String notifica = "n";
		
		if(this.notifica) {
			notifica = "y";
		}
		return new String[] {this.data.toString(), this.vino.nome, String.valueOf(this.vino.anno), this.acquirente.email, this.venditore.email, String.valueOf(this.richiesti), String.valueOf(this.spediti), notifica};
	}
	
	//TODO: Overload della funzione Equals();
}
