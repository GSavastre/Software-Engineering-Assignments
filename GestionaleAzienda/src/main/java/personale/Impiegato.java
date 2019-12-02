package personale;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import org.apache.commons.text.RandomStringGenerator;

import strutture.Sede;
import filemanager.FileManager;

public class Impiegato implements Serializable{
	public String nome;
	public String cognome;
	public String codiceFiscale;
	public Sede sedeLavorativa;
	public LocalDate inizioAttivita;
	public LocalDate fineAttivita;
	
	protected static FileManager files = new FileManager();
	
	private static final int LUNGHEZZACF = 16;
	
	
	public Impiegato(String nome, String cognome, String codiceFiscale, Sede sedeLavorativa, LocalDate inizioAttivita, LocalDate fineAttivita) {
		this.nome = nome;
		this.cognome = cognome;
		this.codiceFiscale = codiceFiscale;
		this.sedeLavorativa = sedeLavorativa;
		this.inizioAttivita = inizioAttivita;
		this.fineAttivita = fineAttivita;
	}
	
	public Impiegato(String nome, String cognome, Sede sedeLavorativa, LocalDate inizioAttivita) {
		this.nome = nome;
		this.cognome = cognome;
		this.codiceFiscale = Impiegato.GeneraCodiceFiscale();
		this.sedeLavorativa = sedeLavorativa;
		this.inizioAttivita = inizioAttivita;
		this.fineAttivita = null;
	}
	
	public Impiegato(String[] parametri) {
		try {
			this.nome = parametri[0];
			this.cognome = parametri[1];
			this.codiceFiscale = parametri[2];
			this.sedeLavorativa = Sede.CaricaDaFile(parametri[3]);
			this.inizioAttivita = LocalDate.parse(parametri[4]);
			if(parametri[5].contentEquals("null")) {
				this.fineAttivita = null;
			}else {
				try {
					this.fineAttivita = LocalDate.parse(parametri[5]);
				}catch(Exception e){
					e.printStackTrace();
					e.getMessage();
					this.fineAttivita = LocalDate.now().plusYears(2);
				}
			}
			
		}catch(Exception e) {
			e.getMessage();
		}
	}
	/*
	 * Description: Genera un codice fiscale casuale univoco
	 * Parameters : nessun parametro necessario
	 * Returns : String -> Codice univoco generato
	 * 
	 * Notes : Chiamato dal costruttore in cui non è necessario passare il codice fiscale
	 */
	public static String GeneraCodiceFiscale() {
		RandomStringGenerator generator = new RandomStringGenerator.Builder()
										        .withinRange('0', 'z')
										        .filteredBy(Character::isLetterOrDigit)
										        .build();
		String codice = generator.generate(LUNGHEZZACF);
		while(CodiceFiscaleEsiste(codice)) {
			codice = generator.generate(LUNGHEZZACF);
		}
		
		return codice.toLowerCase();
	}
	
	/*
	 * Description : Salva un impiegato su file
	 * Parameters : nessun parametro necessario
	 * Returns : void
	 * 
	 * Notes : Non sarebbe male restituire un booleano che indichi il successo o fallimento del salvataggio 
	 */
	public void SalvaSuFile(Impiegato persona) {
		
		//Se il codice fiscale della nuova persona è già esistente e non appartiene alla persona da modificare annulla operazione
		if(CodiceFiscaleEsiste(persona.codiceFiscale) && !this.codiceFiscale.contentEquals(persona.codiceFiscale)) {
			return;
		}
		
		
		String nuovaPersona = persona.toString();
		
		if(persona instanceof Amministratore) {
			nuovaPersona += ",1";
		}else if(persona instanceof Dirigente) {
			nuovaPersona += ",2";
		}else if(persona instanceof Funzionario) {
			nuovaPersona += ",3";
		}else {
			nuovaPersona += ",0";
		}
		
		//Riga del file
		String riga = null;
		
		//Tutti gli elementi che sono già scritti su file per la sovrascrittura
		ArrayList<String> elementi = new ArrayList<String>();
		
		//Singolo elemento già scritto sul file
		String[] elemento;
		
		boolean aggiunto = false;
		
		try(BufferedReader fin = new BufferedReader(new FileReader(files.FILEIMPIEGATI))){
			while((riga = fin.readLine())!= null) {
				//Ignoro i commenti
				if(!riga.startsWith("#")) {
					//Lettura da file
					try {
						//TODO: Implementa uso di dictionary invece che split
						elemento = riga.split(",");
						String nome = elemento[0];
						String cognome = elemento[1];
						String codiceFiscale = elemento[2];
						
						//Se tale impiegato è già presente sul file
						//Se si modifica il codice fiscale si farà un controllo in base al nome
						/*if(codiceFiscale.contentEquals(persona.codiceFiscale.toLowerCase()) || (nome.contentEquals(persona.nome.toLowerCase()) && cognome.contentEquals(persona.cognome.toLowerCase()))) {
							elementi.add(nuovaPersona);
							aggiunto = true;
						}else {
							elementi.add(riga);
						}*/
						if(codiceFiscale.contentEquals(this.codiceFiscale.toLowerCase()) || (nome.contentEquals(this.nome.toLowerCase()) && cognome.contentEquals(this.cognome.toLowerCase()))) {
							elementi.add(nuovaPersona);
							aggiunto = true;
						}else {
							elementi.add(riga);
						}
					}catch(Exception e) {
						e.getMessage();
						System.out.println("Formattazione dati impiegati errata!");
					}
					
				}
			}
			
			if(elementi.size() == 0 || !aggiunto) {
				elementi.add(nuovaPersona);
				this.nome = persona.nome;
				this.cognome = persona.cognome;
				this.codiceFiscale = persona.codiceFiscale;
				this.sedeLavorativa = persona.sedeLavorativa;
				this.inizioAttivita = persona.inizioAttivita;
				this.fineAttivita = persona.fineAttivita;
			}

			//Sovrascrittura su file
			File fvecchio = new File(files.FILEIMPIEGATI);
			fvecchio.delete();
			
			File fout = new File(files.FILEIMPIEGATI);
			try {
				FileWriter fnuovo = new FileWriter(fout, false);
				//TODO: In questo caso l'uso di dictionary faciliterebbe la scrittura di commenti in caso di cambiamento della struttura della classe (si possono ciclare le chiavi)
				fnuovo.write("#nome,cognome,codiceFiscale,nomeSedeLavorativa,inizioAttivita,fineAttivita,ruolo"+System.lineSeparator());
				
				for(String s : elementi) {
					fnuovo.write(s + System.lineSeparator());
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
		String[] elemento;
		int ruolo;
		try(BufferedReader fin = new BufferedReader(new FileReader(files.FILEIMPIEGATI))) {
			while((riga = fin.readLine())!= null) {
				if(!riga.startsWith("#")) {
					elemento = riga.split(",");
					try {
						ruolo = Integer.parseInt(elemento[elemento.length - 1]);
					}catch(Exception e) {
						e.printStackTrace();
						e.getMessage();
						System.out.println("Errore nella lettura dei ruoli");
						break;
					}
					
					switch(ruolo) {
							case 0: impiegati.add(new Operaio(elemento));
									break;
							case 1: impiegati.add(new Amministratore(elemento));
									break;
							case 2: impiegati.add(new Dirigente(elemento));
									break;
							case 3: impiegati.add(new Funzionario(elemento));
									break;
									
							default: impiegati.add(new Operaio(elemento));
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
		String[] elemento;
		int ruolo;
		
		try(BufferedReader fin = new BufferedReader(new FileReader(files.FILEIMPIEGATI))) {
			while((riga = fin.readLine())!= null) {
				if(!riga.startsWith("#")) {
					elemento = riga.split(",");
					try {
						ruolo = Integer.parseInt(elemento[elemento.length - 1]);
					}catch(Exception e) {
						e.printStackTrace();
						e.getMessage();
						System.out.println("Errore nella lettura dei ruoli");
						break;
					}
					
					if(codiceFiscale.contentEquals(elemento[2].toLowerCase())) {
						switch(ruolo) {
							case 0: return new Operaio(elemento);
							case 1: return new Amministratore(elemento);
							case 2: return new Dirigente(elemento);
							case 3: return new Funzionario(elemento);
							default: return new Operaio(elemento);
						}
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
		String[] elemento;
		int ruolo;
		
		try(BufferedReader fin = new BufferedReader(new FileReader(files.FILEIMPIEGATI))) {
			while((riga = fin.readLine())!= null) {
				if(!riga.startsWith("#")) {
					elemento = riga.split(",");
					try {
						ruolo = Integer.parseInt(elemento[elemento.length - 1]);
					}catch(Exception e) {
						e.printStackTrace();
						e.getMessage();
						System.out.println("Errore nella lettura dei ruoli");
						break;
					}
					
					if(nome.toLowerCase().contentEquals(elemento[0].toLowerCase()) && cognome.toLowerCase().contentEquals(elemento[1].toLowerCase())) {
						switch(ruolo) {
							case 0: return new Operaio(elemento);
							case 1: return new Amministratore(elemento);
							case 2: return new Dirigente(elemento);
							case 3: return new Funzionario(elemento);
							default: return new Operaio(elemento);
						}
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
	 * Description : Stampa una lista di impiegati che combacino con i parametri della ricerca
	 * Paramaters: int risultati -> Il numero di risultati che si vogliono visualizzare
	 * 				ArrayList<Class<?>> mansione -> Lista di tipi di impiegato che si vuole mostrare (Funzionario, Amministratore, Dirigente, Operaio)
	 * Returns: void
	 * Notes : Si basa sul metodo Print() per la stampa dell'anagrafica degli impiegati
	 */
	public static ArrayList<Impiegato> Ricerca(int risultati, ArrayList<Class<?>> mansione) {
		ArrayList<Impiegato> impiegati = Impiegato.CaricaDaFile();
		ArrayList<Impiegato> risRicerca = new ArrayList<Impiegato>();
		Impiegato impiegato;
		
		if(impiegati.size() > 0) {
			String mansioni = "";
			for(Class<?> c : mansione) {
				mansioni += c.getSimpleName()+", ";
			}
			System.out.format("Risultato sulla ricerca di impiegati di tipo : %s%n",mansioni);
		}
		
		for(int i = 0; (i < risultati) && (i < impiegati.size()); i++) {
			impiegato = impiegati.get(i);
			/*if(impiegato.getClass() == mansione) {
				impiegato.Print();
			}*/
			
			if(mansione.contains(impiegato.getClass())) {
				//impiegato.Print();
				risRicerca.add(impiegato);
			}
		}
		
		return risRicerca;
	}
	
	/*
	 * Description : Stampa una lista di impiegati che combacino con i parametri della ricerca
	 * Paramaters: int risultati -> Il numero di risultati che si vogliono visualizzare
	 * 				Class<?> mansione -> Il tipo di impiegato che si vuole mostrare (Funzionario, Amministratore, Dirigente, Operaio)
	 * Returns: void
	 * Notes : Si basa sul metodo Print() per la stampa dell'anagrafica degli impiegati
	 */
	/*public static ArrayList<Impiegato> Ricerca(int risultati, Class<?> mansione) {
		ArrayList<Impiegato> impiegati = Impiegato.CaricaDaFile();
		ArrayList<Impiegato> risRicerca = new ArrayList<Impiegato>();
		Impiegato impiegato;
		
		if(impiegati.size() > 0) {
			System.out.format("Risultato sulla ricerca di impiegati di tipo : %s%n",mansione);
		}
		
		for(int i = 0; (i < risultati) && (i < impiegati.size()); i++) {
			impiegato = impiegati.get(i);
			if(impiegato.getClass() == mansione) {
				//impiegato.Print();
				risRicerca.add(impiegato);
			}
		}
		
		return risRicerca;
	}*/
	
	
	public static boolean CodiceFiscaleEsiste(String codice) {
		
		ArrayList<String> codici = new ArrayList<String>();
		
		for(Impiegato impiegato : Impiegato.CaricaDaFile()) {
			codici.add(impiegato.codiceFiscale);
		}
		
		return codici.contains(codice);
	}

	/*
	 * description : toString() ->Cambia un oggetto Impiegato in una String per il salvataggio su file
	 * parametri : nessun parametro necessario
	 * return : String -> String contenete i parametri di persona
	 * 
	 * notes: Valutare l'utilizzo di un dictionary al posto di String
	 */
	public String toString() {
		
		String dataFineAttivita;
		
		try {
			dataFineAttivita = this.fineAttivita.toString();
		}catch(Exception e){
			dataFineAttivita = "null";
		}
		
		
		return String.join(",", 
								this.nome,
								this.cognome,
								this.codiceFiscale,
								this.sedeLavorativa.nome,
								this.inizioAttivita.toString(),
								dataFineAttivita
							).toLowerCase();
	}
	
	public void Print() {
		
		String dataFineAttivita;
		
		try {
			dataFineAttivita = this.fineAttivita.toString();
		}catch(Exception e) {
			dataFineAttivita = "non e' stata ancora impostata alcuna data di fine attivita'";
		}
		
		
		System.out.println("Nome : "+this.nome);
		System.out.println("Cognome : "+this.cognome);
		System.out.println("Codice fiscale : "+this.codiceFiscale);
		System.out.println("Sede lavorativa : "+this.sedeLavorativa.nome);
		System.out.println("Inizio attivita : "+this.inizioAttivita.toString());
		System.out.println("Fine attivita : "+dataFineAttivita);
	}
	
}
