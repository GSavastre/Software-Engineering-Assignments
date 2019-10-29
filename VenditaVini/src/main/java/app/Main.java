package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

//IMPORTANT TODO: Ciclare il menu, adesso una volta uscito dal menu oppure dopo un'eccezzione si usa System.exit(0) che forza l'utente a ripetere tutto il processo da capo

public class Main {
	
	public static final String fileUtenti = "./src/main/resources/utenti.csv";
	public static final String fileOrdini = "./src/main/resources/ordini.csv";
	public static final String fileVini   = "./src/main/resources/vini.csv";
	public static final String fileVitigni = "./src/main/resources/vitigni.csv";
	
	//Utente o impiegato che hanno eseguito l'accesso al sistema, una sorta di sessione (fatta male)
	public static Utente utente = null;
	public static Impiegato impiegato = null;
	
	public static Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		
		CaricaFiles();
		
		ArrayList<String> listaPrimaScelta = new ArrayList<String>() {
			{
				add("Login");
				add("Registrazione");
			}
		};

		int scelta = -1;
		
		do {
			StampaMenu(listaPrimaScelta);
			
			scelta = OttieniScelta(listaPrimaScelta.size());
			
			switch(scelta) {
			
				//Uscita dall'applicazione
				case 0:
					System.out.println("Logout avvenuto con successo");
					System.exit(0);
					break;
				
				//Login
				case 1:
					int ruolo = Login();
					if(ruolo == 0) {
						ScelteImpiegato();
					}else if(ruolo == 1) {
						ScelteUtente();
					}else {
						System.out.println("Accesso non autorizzato!");
					}
					break;
				
				//Registrazione
				case 2:
					Utente nuovoUtente = RegistraUtente();
					if(nuovoUtente != null) {
						System.out.println("Registrazione avvenuta con successo!");
						ScelteUtente();
					}else {
						System.out.println("Registrazione fallita!");
						System.exit(0);
					}
					break;
					
				default:
					System.out.println("Errore inaspettato!");
			}
		}while(scelta != 0);
		
		input.close();
	}
	
	/*
	 * Stampa in modo formattato un menu
	 * param : ArrayList<String> scelte - lista di scelte
	 * return : void
	 * note : Le scelte partono dall'indice 1 alla fine del menu
	 * e finisconon con il numero di scelte possibili
	 * dopo l'ultimo indice viene inserito automaticamente la scelta
	 * zero [0] che viene usata per uscire
	 */
	private static void StampaMenu(ArrayList<String> scelte) {
		int counterIndex = 1;
		for(String scelta : scelte) {
			System.out.println("["+counterIndex+"] "+ scelta);
			counterIndex++;
		}
		
		System.out.println("[0] Esci");
	}

	/*
	 * Funzione per la scelta dell'opzione da un menu
	 * param : int limite - numero massimo inseribile corrispondente al numero di elementi del menu
	 * return : int scelta - numero della scelta eseguita dall'utenteù
	 * note : la funzione cicla finché non viene eseguita una scelta valida
	 */
	private static int OttieniScelta(int limite) {
		//Scanner input = new Scanner(System.in);
		
		//Non 0 perché è il valore di default per l'uscita
		int scelta = -1;
		
		do {
			System.out.print("Inserisci la tua scelta : ");
			try {
				scelta = Integer.parseInt(input.nextLine());
			}catch(NumberFormatException e){
				System.out.println("Selezione non valida");
			}
			
			if(scelta < 0 || scelta > limite) {
				System.out.println("Selezione non valida");
			}
		}while(scelta < 0 || scelta > limite);
		
		//input.close();
		
		return scelta;
	}
	
	/*
	 * Menu con le azioni che un utente/cliente può eseguire
	 */
	private static void ScelteUtente() {
		ArrayList<String> listaScelteUtente = new ArrayList<String>() {
			{
				add("Ricerca vini");
				add("Acquista vini");
			}
		};
		String nomeVino;
		int scelta = -1;
		int annoVino;
		int quantita;
		Vino vino;
		
		do {
			StampaMenu(listaScelteUtente);
			scelta = OttieniScelta(listaScelteUtente.size());
			
			switch(scelta) {
			
				case 0:
					break;
				
				case 1:
					System.out.print("Inserisci nome vino : ");
					nomeVino = input.nextLine();
					
					System.out.print("Inserisci anno vino :");
					
					try {
						annoVino = Integer.parseInt(input.nextLine());
					}catch(Exception e){
						annoVino = 0;
					}
					
					vino = new Vino(nomeVino, annoVino);
					ArrayList<Vino> vini = utente.RicercaVino(fileVini, vino);
					for(Vino v : vini) {
						v.Print();
					}
					break;
					
				case 2:
					System.out.print("Inserisci nome vino : ");
					nomeVino = input.nextLine();
					
					System.out.print("Inserisci anno vino :");
					
					try {
						annoVino = Integer.parseInt(input.nextLine());
					}catch(Exception e){
						annoVino = 0;
					}
					
					System.out.print("Inserisci quantità da acquistare :");
					
					try {
						quantita = Integer.parseInt(input.nextLine());
					}catch(Exception e){
						quantita = 0;
					}
					
					vino = new Vino(nomeVino, annoVino);
					utente.AcquistaVino(fileVini, vino, quantita);
					
					break;
			}
			
		}while(scelta != 0);
	}
	
	/*
	 * Menu con le azioni che un impiegato può eseguire
	 */
	private static void ScelteImpiegato() {
		ArrayList<String> listaScelteImpiegato = new ArrayList<String>() {
			{
				add("Spedisci vini");
				add("Rimpiazza vini");
			}
		};
		
		StampaMenu(listaScelteImpiegato);
	}
	
	/*
	 * Effettua il processo di login per un utente che sia cliente o impiegato
	 * ritorna intero che identifica il ruolo dell'utente, inoltre istanzia un oggetto
	 * utente o impiegato all'interno del main come specie di sessione a seconda del ruolo
	 * che si ha dopo aver eseguito un login con successo
	 * return 0 -> impiegato
	 * return 1 -> cliente
	 * return -1 -> errore
	 */
	private static int Login() {
		
		//Ruolo dell'utente individuato
		int ruolo = -1;
		//Riga di testo letta dal file
		String riga = null;
		//Credenziali ricavate dalla variabile stringa riga usando split()
		String[] credenziali;
		
		System.out.print("Inserisci la email : ");
		String email = input.nextLine();
		
		System.out.print("Inserisci la password : ");
		String password = input.nextLine();
		
		//Apre il file per l'accesso dell'utente
		try(BufferedReader fin = new BufferedReader(new FileReader(fileUtenti))) {
			//Legge le righe del file fino alla fine
			while((riga = fin.readLine()) != null) {
				//Se il primo carattere è # significa che è un commento, in quel caso non lo consideriamo
				if(!riga.startsWith("#")) {
					try {
						credenziali = riga.split(",");
						
						//Controllo che le credenziali inserite siano valide
						if(credenziali[2].contains(email) && credenziali[3].contains(password)) {
							//Verifico il ruolo
							
							if(credenziali[4].contains("0")) {
								System.out.println("\nAccesso eseguito correttamente come impiegato!\n");
								impiegato = new Impiegato(credenziali);
							}else if(credenziali[4].contains("1")) {
								System.out.println("\nAccesso eseguito correttamente come cliente!\n");
								utente = new Utente(credenziali); 
							}else {
								return ruolo;
							}
							return Integer.parseInt(credenziali[4]);
						}
						
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}catch(FileNotFoundException e){
			System.out.println("Impossibile trovare il file per l'accesso utenti.");
			e.printStackTrace();
		}
		catch(IOException e){
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
		
		//input.close();
		return ruolo;
		
	}
	
	/*
	 * Inizia il processo di registrazione per un nuovo utente
	 * return : Object Utente oppure null in caso di registrazione fallita (la ripetizione della password non è corretta)
	 */
	private static Utente RegistraUtente() {
		//Scanner input = new Scanner(System.in);
		
		//Inserimento credenziali registrazione
		System.out.print("Inserisci il nome : ");
		String nome = input.nextLine();
		
		System.out.print("Inserisci il cognome : ");
		String cognome = input.nextLine();
		
		System.out.print("Inserisci l'email : ");
		String email = input.nextLine();
		
		System.out.print("Inserisci la password : ");
		String password = input.nextLine();
		
		System.out.print("Ripeti la password : ");
		String pwdRipeti = input.nextLine();
		
		//Fine inserimento credenziali
		
		//Riga di testo ricavata dal file di utenti per controllare che tale utente non sia già registrato
		//l'unicità si basa sull'email
		String riga = null;
		
		//input.close();
		
		Utente nuovoUtente = new Utente().Registrazione(nome, cognome, email, password, pwdRipeti);
		boolean userExists = false;
		
		//Ricerca email già esistente
		try(BufferedReader fin = new BufferedReader(new FileReader(fileUtenti))){
			//Legge tutte le righe del file
			while((riga = fin.readLine())!= null) {
				if(!riga.startsWith("#")) {
					try {
						String fileEmail = riga.split(",")[2];
						if(email.equals(fileEmail)){
							System.out.println("Questa email è già in uso, scegliere un altro indirizzo e riprovare");
							userExists = true;
						}
					}catch(Exception e) {
						e.printStackTrace();
						return null;
					}
				}
			}
		}catch(FileNotFoundException e){
			System.out.println("Impossibile trovare il file per l'accesso utenti.");
			e.printStackTrace();
		}
		catch(IOException e){
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
		
		if(!userExists) {
			try(BufferedWriter fout = new BufferedWriter(new FileWriter(fileUtenti, true))){
				
				fout.append(System.lineSeparator()+nome+","+cognome+","+email+","+password+",1");
				return nuovoUtente;
				
			}catch(FileNotFoundException e){
				System.out.println("Impossibile registrare il nuovo utente");
				e.printStackTrace();
			}
			catch(IOException e){
				System.out.println(e.getMessage());
				//e.printStackTrace();
			}
		}
		
		return null;
	}

	
	/*
	 * Cerca i file necessari per il funzionamento del programma
	 * nel caso non esistano li genera nuovi
	 */
	private static void CaricaFiles() {
		ArrayList<File> listaFiles = new ArrayList<File>() {
			{
				add(new File(fileUtenti));
				add(new File(fileOrdini));
				add(new File(fileVini));
				add(new File(fileVitigni));
			}
		};
		
		for(File f : listaFiles) {
			if(!f.isFile()) {
				try {
					f.createNewFile();
				}catch(Exception e) {
					e.getMessage();
				}
			}
		}
	}
}
