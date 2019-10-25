package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

//IMPORTANT TODO: Ciclare il menu, adesso una volta uscito dal menu oppure dopo un'eccezzione si usa System.exit(0) che forza l'utente a ripetere tutto il processo da capo
//TODO: File loader
public class Main {
	
	public static final String fileUtenti = "./src/main/resources/utenti.csv";
	
	//Utente o impiegato che hanno eseguito l'accesso al sistema, una sorta di sessione (fatta male)
	public static Utente utente = null;
	public static Impiegato impiegato = null;
	
	public static Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		
		ArrayList<String> listaPrimaScelta = new ArrayList<String>() {
			{
				add("Login");
				add("Registrazione");
			}
		};
		
		StampaMenu(listaPrimaScelta);
		
		switch(OttieniScelta(listaPrimaScelta.size())) {
		
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
	 * return : int scelta - numero della scelta eseguita dall'utente�
	 * note : la funzione cicla finch� non viene eseguita una scelta valida
	 */
	private static int OttieniScelta(int limite) {
		//Scanner input = new Scanner(System.in);
		
		//Non 0 perch� � il valore di default per l'uscita
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
	 * Menu con le azioni che un utente/cliente pu� eseguire
	 */
	private static void ScelteUtente() {
		ArrayList<String> listaScelteUtente = new ArrayList<String>() {
			{
				add("Ricerca vini");
				add("Acquista vini");
			}
		};
		
		StampaMenu(listaScelteUtente);
	}
	
	/*
	 * Menu con le azioni che un impiegato pu� eseguire
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
				//Se il primo carattere � # significa che � un commento, in quel caso non lo consideriamo
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
	 * return : Object Utente oppure null in caso di registrazione fallita (la ripetizione della password non � corretta)
	 */
	private static Utente RegistraUtente() {
}
