package strutture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import filemanager.FileManager;
import personale.Amministratore;
import personale.*;
public class Sede implements Serializable{
	public String nome;
	public String indirizzo;
	public ArrayList<Impiegato> impiegati;
	private static FileManager files = new FileManager();
	
	public Sede(String nome, String indirizzo){
		this.nome = nome;
		this.indirizzo = indirizzo;
		CaricaImpiegati();
	}
	
	private Sede(String[] parametri) {
		try {
			this.nome = parametri[0];
			this.indirizzo = parametri[1];
		}catch(Exception e) {
			e.getMessage();
		}
		CaricaImpiegati();
	}
	
	private void CaricaImpiegati() {
		ArrayList<Class<?>> mansioni = new ArrayList<Class<?>>() {
			{
				add(Operaio.class);
				add(Amministratore.class);
				add(Dirigente.class);
				add(Funzionario.class);
			}
		};
		this.impiegati = this.Ricerca(-1, mansioni);
	}
	
	/*
	 * Description : Genera una lista di impiegati che appartengano alla sede in base al ruolo e quantita' da visualizzare richiesti
	 * Paramaters: int risultati -> Il numero di risultati che si vogliono visualizzare
	 * 				ArrayList<Class<?>> mansione -> Lista di tipi di impiegato che si vuole mostrare (Funzionario, Amministratore, Dirigente, Operaio)
	 * Returns: ArrayList<Impiegato> risRicerca -> Risultato della ricerca degli impiegati
	 * Notes: La ricerca pu� ritornare una lista vuota in caso non ci siano impiegati
	 */
	public ArrayList<Impiegato> Ricerca(int risultati, ArrayList<Class<?>> mansione) {
		ArrayList<Impiegato> impiegati = Impiegato.CaricaDaFile();
		ArrayList<Impiegato> risRicerca = new ArrayList<Impiegato>();
		//Impiegato impiegato;
		
		/*for(int i = 0; (i < risultati) && (i < impiegati.size()); i++) {
			impiegato = impiegati.get(i);
			
			if(mansione.contains(impiegato.getClass()) && impiegato.sedeLavorativa.equals(this)) {
				risRicerca.add(impiegato);
			}
		}*/
		
		for(Impiegato i : impiegati) {
			if(mansione.contains(i.getClass()) && i.sedeLavorativa.contentEquals(this.nome)) {
				if(risultati > 0) {
					if(risRicerca.size() < risultati) {
						risRicerca.add(i);
					}else {
						break;
					}
				}else {
					risRicerca.add(i);
				}
			}
		}
		
		return risRicerca;
	}
	
	/*
	 * Description : SalvaSuFile() -> Salva un oggetto Sede su un file csv
	 * Parameters : nessun parametro richiesto
	 * Returns : void
	 * 
	 * Notes : TODO: Implementare il salvataggio in caso di modifiche dei parametri
	 *  (ad esempio passando un oggetto Sede come parametro e usarlo per sovrascrivere quello vecchio)
	 */
	public void SalvaSuFile() {
		String nuovaSede = this.toString();
		
		String riga = null;
		
		String[] elemento;
		
		boolean aggiunto = false;
		
		ArrayList<String> elementi = new ArrayList<String>();
		
		try(BufferedReader fin = new BufferedReader(new FileReader(files.FILESEDI))) {
			while((riga = fin.readLine()) != null) {
				if(!riga.startsWith("#")) {
					try {
						elemento = riga.split(",");
						String nome = elemento[0];
						
						if(nome.contentEquals(this.nome.toLowerCase())) {
							elementi.add(nuovaSede);
							aggiunto = true;
						}else {
							elementi.add(riga);
						}
					}catch(Exception e) {
						e.printStackTrace();
						e.getMessage();
						System.out.println("Errore nella lettura dei dati!");
					}
				
				}
			}
			
			if(elementi.size() == 0 || !aggiunto) {
				elementi.add(nuovaSede);
			}
			
			//Sovrascrittura
			File fvecchio = new File(files.FILESEDI);
			fvecchio.delete();
			
			File fout = new File(files.FILESEDI);
			try {
				FileWriter fnuovo = new FileWriter(fout, false);
				
				fnuovo.write("#nome,indirizzo"+System.lineSeparator());
				for(String s : elementi) {
					fnuovo.write(s + System.lineSeparator());
				}
				
				fnuovo.close();
			}catch(Exception e) {
				e.getMessage();
				System.out.println("Errore nella sovrascrittura del file sedi!");
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Impossibile trovare il file per il salvataggio della sede");
		}catch(IOException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Errore salvataggio della sede!");
		}
	}
	
	/*
	 * Description : Carica una sede dal file
	 * Parameters : String nome -> nome della sede da cercare e caricare
	 * Returns : Sede -> Sede caricata o null se non si riesce a trovare
	 * 
	 * Notes : Puo' ritornare null se non riesce a trovare la sede in base al nome
	 */
	public static Sede CaricaDaFile(String nome) {
		String riga = null;
		
		try(BufferedReader fin = new BufferedReader(new FileReader(files.FILESEDI))) {
			while((riga = fin.readLine())!= null) {
				if(!riga.startsWith("#")) {
					Sede sedeFile = new Sede(riga.split(","));
					
					if(nome.toLowerCase().contentEquals(sedeFile.nome)) {
						return sedeFile;
					}
				}
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Impossibile trovare il file per il caricamento della sede!");
		}catch(IOException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Errore caricamento dati della sede!");
		}
		
		return null;
	}
	
	/*
	 * Description: Caricamento di sedi da un file csv
	 * Parameters: Nessun parametro necessario
	 * Returns: ArrayList<Sede> lista contenente tutte le sedi salvate su file
	 * 
	 * Notes: Tutte le sedi salvate su file saranno caricate oppure ritorner� una lista vuota
	 */
	public static ArrayList<Sede> CaricaDaFile(){
		String riga = null;
		
		ArrayList<Sede> sedi = new ArrayList<Sede>();
		try(BufferedReader fin = new BufferedReader(new FileReader(files.FILESEDI))) {
			while((riga = fin.readLine())!= null) {
				if(!riga.startsWith("#")) {
					sedi.add(new Sede(riga.split(",")));
				}
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Impossibile trovare il file per il caricamento delle sedi!");
		}catch(IOException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Errore caricamento dati dell'impiegato!");
		}
		
		return sedi;
	}
	
	/*
	 * Description : Controlla l'esistenza di un nome di una sede all'interno del file delle sedi
	 * Parameters : String nome -> nome della sede da controllare
	 * Returns : boolean -> risultato ricerca, true -> il nome � duplicato | false -> il nome non � duplicato
	 * 
	 */
	public static boolean NomeDuplicato(String nome) {
		
		try(BufferedReader fin = new BufferedReader(new FileReader(files.FILESEDI))){
			String riga = null;
			
			while((riga = fin.readLine()) != null) {
				if(!riga.startsWith("#")) {
					if(nome.contentEquals(riga.split(",")[0])) return true;
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Errore apertura file sedi!");
		}catch(Exception e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Errore nel controllo del nome della sede");
		}
		
		return false;
	}
	
	/*
	 * Description : toString() -> Cambia un oggetto Sede in String da salvare su file
	 * Parameters : nessun parametro necessario
	 * Returns : String -> String formattata per il salvataggio della sede su un file
	 * 
	 * Notes: Valutare l'utilizzo di un dictionary (TODO)
	 */
	public String toString() {
		return String.join(",",
								this.nome,
								this.indirizzo
							).toLowerCase();
	}
	
	public void Print() {
		System.out.println("Nome : "+this.nome);
		System.out.println("Indirizzo : "+ this.indirizzo);
	}
}
