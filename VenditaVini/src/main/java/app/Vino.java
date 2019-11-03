package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Vino {
	
	public String nome;
	public int anno;
	public String note;
	//public Vitigno vitigno;
	public String vitigno;
	public int numeroBottiglie;
	public static FileManager files = new FileManager();
	
	public Vino(String nome, int anno, String note,String vitigno, int numeroBottiglie){
		this.nome = nome;
		this.anno = anno;
		this.note = note;
		this.vitigno = vitigno;
		this.numeroBottiglie = numeroBottiglie;
		
		SalvaSuFile();
	}
	
	/*
	 * Costruttore per la ricerca di un vino eseguita da un utente
	 */
	public Vino(String nome, int anno) {
		this.nome = nome;
		this.anno = anno;
	}
	
	public Vino(String[] dettagli) {
		try {
			this.nome = dettagli[0];
			this.anno = Integer.parseInt(dettagli[1]);
			this.note = dettagli[2];
			this.vitigno = dettagli[3];
			this.numeroBottiglie = Integer.parseInt(dettagli[4]);
		}catch(Exception e){
			e.getMessage();
		}
	}
	
	/*
	 * Rimuove una certa quantita di vini dal numero di vini disponibili salvato su file
	 * return : true -> Il numero di bottiglie disponibile è maggiore di quello da rimuovere
	 * return : false -> Non esistono abbastanza bottiglie da rimuovere per completare l'ordine
	 */
	public boolean Rimuovi(int quantita) {
		if(numeroBottiglie < quantita) {
			return false;
		}
		
		numeroBottiglie -= quantita;
		
		SalvaSuFile();
		return true;
	}
	
	/*
	 * Aumenta di una certa quantita il numero di bottiglie disponibili
	 * return : int -> Nuovo numero di bottiglie disponibile.
	 */
	public int Rifornisci(int quantita) {
		numeroBottiglie += quantita;
		
		SalvaSuFile();
		return numeroBottiglie;
	}
	
	/*
	 * Salva i cambiamenti di un vino su file, nel caso il vino non fosse ancora memorizzato sul file allora verrà appeso alla fine del file altrimenti
	 * verrà riscritto tutto il file sovvrascrivendo solo il vino interessato
	 */
	//nome,anno,note,vitigno,numeroBottiglie
	public void SalvaSuFile() {
		//Controllo esistenza di questo vino sul file
		boolean exists = false;
		
		//Lista contenente tutti i vini già segnati su file
		ArrayList<Vino> contenuti = new ArrayList<Vino>();
		
		//Stringa ricavata dallo Split() della stringa dal file
		String[] contenuto;
		
		//Singola riga del file
		String riga = null;
		
		//Popolazione della lista dei contenuti dei vari vini
		try(BufferedReader fin = new BufferedReader(new FileReader(files.fileVini))){
			
			//Legge tutte le righe del file
			while((riga = fin.readLine()) != null) {
				//Ignora i commenti
				if(!riga.startsWith("#")) {
					try {
						contenuto = riga.split(",");
						String nomeVino = contenuto[0];
						int anno = Integer.parseInt(contenuto[1]);
						
						//Un vino viene considerato duplicato solo se il suo nome e anno coincide con un altro vino con stesso nome e anno
						if(nomeVino.contentEquals(nome) && anno == this.anno) {
							
							exists = true;
							
							//Non aggiorno nome e anno siccome sono uguali alla stringa su file
							
							//Note
							contenuto[2] = this.note;
							
							//Vitigno
							contenuto[3] = this.vitigno;
							
							//Numero bottiglie
							contenuto[4] = String.valueOf(this.numeroBottiglie);
						}
						
						//Questo sarà il nuovo vino sovrascritto nel caso sia duplicato altrimenti verrà aggiunto un vino normalmente
						contenuti.add(new Vino(contenuto));
						
					}catch(Exception e) {
						e.printStackTrace();
						System.out.println("Errore lettura file vini");
					}
				}
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Impossibile trovare il file per il salvataggio dei vini!");
		}catch(IOException e) {
			e.printStackTrace();
			System.out.println("Errore salvataggio vini!");
		}
		
		//Se non esiste il vino appendilo alla fine del file
		if(!exists) {
			contenuti.add(this);
		}
		
		//File vecchio da eliminare
		File fvecchio = new File(files.fileVini);
		fvecchio.delete();
		
		File fout = new File(files.fileVini);
		try {
			FileWriter fnuovo = new FileWriter(fout,false);
			//Scrivo tutti i vini su file
			for(Vino v : contenuti) {
				fnuovo.write(v.ToFileString());
			}
			fnuovo.close();
		}catch(IOException e) {
			e.printStackTrace();
			System.out.println("Errore nella sovrascrittura dei vini");
		}
		
	}
	
	/*
	 * Carica una lista di vini dal file di vini
	 * return : ArrayList<Vino> : Lista contenete vini
	 */
	public ArrayList<Vino> CaricaDaFile(){
		ArrayList<Vino> vini = new ArrayList<Vino>();
		
		//Singola riga del file
		String riga = null;
		
		try(BufferedReader fin = new BufferedReader(new FileReader(files.fileVini))){
			
			while((riga = fin.readLine()) != null) {
				if(riga.startsWith("#")) {
					try {
						vini.add(new Vino(riga.split(",")));
					}catch(Exception e) {
						e.printStackTrace();
						System.out.println("Errore nella lettura dell'archivio dei vini");
					}
				}
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Errore, impossibile trovare l'archivio di vini");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Errore nel caricamento dell'archivio di vini");
		}
		
		
		return vini;
	}
	
	/*
	 * Ritorna una stringa formattata contenete i dettagli del vino da salvare su un file
	 * return : String -> Stringa formattata per il salvataggio
	 */
	public String ToFileString() {
		return this.nome+","+this.anno+","+this.note+","+this.vitigno+","+this.numeroBottiglie+System.lineSeparator();
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
	
	/*
	 * Stampa su console i dettagli del vino
	 */
	public void Print() {
		System.out.println("Nome :"+ nome);
		System.out.println("Anno :"+ anno);
		System.out.println("Note :"+ note);
		System.out.println("Vitigno :"+ vitigno);
		System.out.println("Numero disponibile :"+ numeroBottiglie +"\n");
		
	}

}
