package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Ordine {
	public Vino vino;
	public Utente acquirente;
	public Impiegato venditore;
	public int richiesti;
	public int spediti;
	public boolean completato;
	public LocalDateTime data;
	public FileManager files;
	
	public Ordine() {
		
	}
	
	public Ordine(Vino vino, Utente acquirente, int richiesti) {
		this.vino = vino;
		this.acquirente = acquirente;
		this.venditore = new Impiegato();
		this.completato = false;
		this.richiesti = richiesti;
		this.spediti = 0;
		this.data = LocalDateTime.now();
		this.files = new FileManager();
	}
	
	
	/*
	 * Segna l'impiegato che ha preso carico dell'ordine
	 */
	public void SetImpiegato(Impiegato impiegato) {
		venditore = impiegato;
	}
	
	/*
	 * Completa un ordine solo se la quantità di vini richiesti è uguale a quella di vini spediti
	 * return : true -> tutti i vini sono stati forniti
	 * return : false -> mancano vini da spedire
	 */
	public boolean CompletaOrdine() {
		if(richiesti == spediti) {
			return this.completato = true;
		}
		
		return false;
	}
	
	/*
	 * Spedisci una certa quantità di vini appartenente ad un ordine
	 */
	public boolean SpedisciVini(int quantita) {
		if(!vino.Rimuovi(quantita)) {
			return false;
		}
		
		spediti += quantita;
		//TODO:Inizializza una nuova notifica per l'utente
		
		SalvaSuFile();
		return true;
	}
	
	/*
	 * Salva l'ordine corrente su file
	 * #dataOrdine,nomevino,mailCliente,mailImpiegato,viniRichiesti,viniSpediti
	 */
	
	//IMPORTANT TODO: Aggiungere controllo per impiegato null
		public void SalvaSuFile() {
			//Controllo esistenza di questo ordine sul file
			boolean exists = false;
			
			//Lista contenente tutti i vini già segnati su file
			ArrayList<String[]> contenuti = new ArrayList<String[]>();
			
			//Stringa ricavata dallo Split() della stringa dal file
			String[] contenuto;
			
			//Singola riga del file
			String riga = null;
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			
			//Popolazione della lista dei contenuti dei vari ordini
			try(BufferedReader fin = new BufferedReader(new FileReader(files.fileOrdini))){
				
				//Legge tutte le righe del file
				while((riga = fin.readLine()) != null) {
					//Ignora i commenti
					if(!riga.startsWith("#")) {
						try {
							contenuto = riga.split(",");
							
							LocalDateTime dataFile = LocalDateTime.parse(contenuto[0], formatter);
							String nomeVino = contenuto[1];
							String mailCliente = contenuto[2];
							
							//Un ordine viene considerato duplicato in base alla mail del cliente, il nome del vino ordinato e la data precisa in cui viene effettuata l'ordine
							if(nomeVino.contentEquals(this.vino.nome) && mailCliente == this.acquirente.email && dataFile.isEqual(this.data)) {
								
								exists = true;
								
								//Non aggiorno nomeVino, mailCliente e data perché sono uguali
								
								//Email venditore
								if(this.venditore == null) {
									contenuto[3] = "null";
								}else {
									contenuto[3] = this.venditore.email;
								}
								
								//Numero bottiglie richieste
								contenuto[4] = String.valueOf(this.richiesti);
								
								//Numero bottiglie spediti
								contenuto[5] = String.valueOf(this.spediti);
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
				//Scrivo tutti i vini su file
				for(String[] v : contenuti) {
					fnuovo.write(String.join(",", v)+System.lineSeparator());
				}
				fnuovo.close();
			}catch(IOException e) {
				e.printStackTrace();
				System.out.println("Errore nella sovrascrittura dei vini");
			}
			
		}
		
		//#dataOrdine,nomevino,mailCliente,mailImpiegato,viniRichiesti,viniSpediti
		
		public String[] ToFileString() {
			return new String[] {this.data.toString(), this.vino.nome, this.acquirente.email, this.venditore.email, String.valueOf(this.richiesti), String.valueOf(this.spediti)};
		}
	
	//TODO: Overload della funzione Equals();
}
