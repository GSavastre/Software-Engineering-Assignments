package personale;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import app.Server;
import strutture.Sede;
import filemanager.FileManager;

public class Impiegato {
	public String nome;
	public String cognome;
	public String codiceFiscale;
	public Sede sedeLavorativa;
	public LocalDate inizioAttivita;
	public LocalDate fineAttivita;
	
	protected static FileManager files = new FileManager();
	
	
	public Impiegato(String nome, String cognome, String codiceFiscale, Sede sedeLavorativa, LocalDate inizioAttivita, LocalDate fineAttivita) {
		this.nome = nome;
		this.cognome = cognome;
		this.codiceFiscale = codiceFiscale;
		this.sedeLavorativa = sedeLavorativa;
		this.inizioAttivita = inizioAttivita;
		
		if(fineAttivita == null) {
			fineAttivita = LocalDate.now();
		}
		
		this.fineAttivita = fineAttivita;
		if(fineAttivita.compareTo(inizioAttivita) < 0) {
			this.fineAttivita.plusYears(1);
		}
	}
	
	public Impiegato(String[] parametri) {
		try {
			this.nome = parametri[0];
			this.cognome = parametri[1];
			this.codiceFiscale = parametri[2];
			this.sedeLavorativa = Sede.CaricaDaFile(parametri[3]);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/d");
			this.inizioAttivita = LocalDate.parse(parametri[4]);
			this.fineAttivita = LocalDate.parse(parametri[5]);
		}catch(Exception e) {
			e.getMessage();
		}
	}
	
	/*
	 * Description : Salva un impiegato su file
	 * Parameters : nessun parametro necessario
	 * Returns : void
	 * 
	 * Notes : Non sarebbe male restituire un booleano che indichi il successo o fallimento del salvataggio 
	 */
	public void SalvaSuFile() {
		
		String nuovaPersona = this.toString();
		
		//Riga del file
		String riga = null;
		
		//Tutti gli elementi che sono già scritti su file per la sovrascrittura
		ArrayList<String> elementi = new ArrayList<String>();
		
		//Singolo elemento già scritto sul file
		String[] elemento;
		
		
		try(BufferedReader fin = new BufferedReader(new FileReader(files.FILEIMPIEGATI))){
			while((riga = fin.readLine())!= null) {
				//Ignoro i commenti
				if(!riga.startsWith("#")) {
					//Lettura da file
					try {
						//TODO: Implementa uso di dictionary invece ceh split
						elemento = riga.split(",");
						String nome = elemento[0];
						String cognome = elemento[1];
						String codiceFiscale = elemento[2];
						
						//Se tale impiegato è già presente sul file
						//Se si modifica il codice fiscale si farà un controllo in base al nome
						if(codiceFiscale.contentEquals(this.codiceFiscale) || (nome.contentEquals(this.nome) && cognome.contentEquals(this.cognome))) {
							elementi.add(nuovaPersona);
						}else {
							elementi.add(riga);
						}
					}catch(Exception e) {
						e.getMessage();
						System.out.println("Formattazione dati impiegati errata!");
					}
					
				}
			}
			
			if(elementi.size() == 0) {
				elementi.add(nuovaPersona);
			}

			////Sovrascrittura su file
			File fvecchio = new File(files.FILEIMPIEGATI);
			fvecchio.delete();
			
			File fout = new File(files.FILEIMPIEGATI);
			try {
				FileWriter fnuovo = new FileWriter(fout, false);
				//TODO: In questo caso l'uso di dictionary faciliterebbe la scrittura di commenti in caso di cambiamento della struttura della classe (si possono ciclare le chiavi)
				fnuovo.write("#nome,cognome,codiceFiscale,nomeSedeLavorativa,inizioAttivita,fineAttivita"+System.lineSeparator());
				
				for(String s : elementi) {
					fnuovo.write(s);
				}
				
				fnuovo.close();
				
			}catch(IOException e) {
				e.getMessage();
				System.out.println("Errore nella sovrascrittura del file impiegati!");
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Impossibile trovare il file per il salvataggio degli impiegati!");
		}catch(IOException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Errore salvataggio dell'impiegato!");
		}
	}
	
	/*
	 * Description: Caricamento di impiegati da un file csv
	 * Parameters: Nessun parametro necessario
	 * Returns: ArrayList<Impiegato> lista contenente tutti gli impiegati salvati su file
	 * 
	 * Notes: Tutti gli impiegati salvati su file saranno caricati oppure ritornerà una lista vuota
	 */
	public static ArrayList<Impiegato> CaricaDaFile(){
		String riga = null;
		
		ArrayList<Impiegato> impiegati = new ArrayList<Impiegato>();
		
		try(BufferedReader fin = new BufferedReader(new FileReader(files.FILEIMPIEGATI))) {
			while((riga = fin.readLine())!= null) {
				if(!riga.startsWith("#")) {
					 impiegati.add(new Impiegato(riga.split(",")));
				}
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Impossibile trovare il file per il caricamento degli impiegati!");
		}catch(IOException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Errore caricamento dati dell'impiegato!");
		}
		
		return impiegati;
	}
	
	/*
	 * Description: Carica un singolo impiegato in base al codice fiscale
	 * Parameters: String codiceFiscale -> codice fiscale univoco ad ogni impiegato
	 * Returns: Impiegato -> impiegato caricato da file oppure null
	 * 
	 * Notes: Ritorna null se non viene trovato l'impiegato corretto
	 */
	public static Impiegato CaricaDaFile(String codiceFiscale) {
		
		String riga = null;
		
		try(BufferedReader fin = new BufferedReader(new FileReader(files.FILEIMPIEGATI))) {
			while((riga = fin.readLine())!= null) {
				if(!riga.startsWith("#")) {
					Impiegato personaFile = new Impiegato(riga.split(","));
					
					if(codiceFiscale.contentEquals(personaFile.codiceFiscale)) {
						return personaFile;
					}
				}
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Impossibile trovare il file per il caricamento degli impiegati!");
		}catch(IOException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Errore caricamento dati dell'impiegato!");
		}
		
		return null;
	}
	
	/*
	 * Description: Carica un singolo impiegato in base al nome e al cognome
	 * Parameters String nome -> nome dell'impiegato
	 * 			  String cognome -> cognome dell'impiegato
	 * Returns: Impiegato -> impiegato caricato da file oppure null
	 * 
	 * Notes: Ritorna null se non viene trovato l'impiegato corretto
	 */
	public static Impiegato CaricaDaFile(String nome, String cognome) {
		String riga = null;
		
		try(BufferedReader fin = new BufferedReader(new FileReader(files.FILEIMPIEGATI))) {
			while((riga = fin.readLine())!= null) {
				if(!riga.startsWith("#")) {
					Impiegato personaFile = new Impiegato(riga.split(","));
					
					if(nome.contentEquals(personaFile.nome) && cognome.contentEquals(personaFile.cognome)) {
						return personaFile;
					}
				}
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Impossibile trovare il file per il caricamento degli impiegati!");
		}catch(IOException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Errore caricamento dati dell'impiegato!");
		}
		
		return null;
	}
	
	/*
	 * description : toString() ->Cambia un oggetto Impiegato in una String per il salvataggio su file
	 * parametri : nessun parametro necessario
	 * return : String -> String contenete i parametri di persona
	 * 
	 * notes: Valutare l'utilizzo di un dictionary al posto di String
	 */
	
	public String toString() {
			return String.join(",", 
									this.nome,
									this.cognome,
									this.codiceFiscale,
									this.sedeLavorativa.nome,
									this.inizioAttivita.toString(),
									this.fineAttivita.toString()+System.lineSeparator()
								);
	}
	
}
