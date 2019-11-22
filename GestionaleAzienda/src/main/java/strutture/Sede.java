package strutture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import filemanager.FileManager;

public class Sede {
	public String nome;
	public String indirizzo;
	private static FileManager files = new FileManager();
	
	public Sede(String nome, String indirizzo){
		this.nome = nome;
		this.indirizzo = indirizzo;
	}
	
	private Sede(String[] parametri) {
		try {
			this.nome = parametri[0];
			this.indirizzo = parametri[1];
		}catch(Exception e) {
			e.getMessage();
		}
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
		
		ArrayList<String> elementi = new ArrayList<String>();
		
		try(BufferedReader fin = new BufferedReader(new FileReader(files.FILESEDI))) {
			while((riga = fin.readLine()) != null) {
				if(!riga.startsWith("#")) {
					try {
						elemento = riga.split(",");
						String nome = elemento[0];
						
						if(nome.contentEquals(this.nome.toLowerCase())) {
							elementi.add(nuovaSede);
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
			
			if(elementi.size() == 0) {
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
					fnuovo.write(s);
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
			System.out.println("Impossibile trovare il file per il caricamento degli impiegati!");
		}catch(IOException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Errore caricamento dati dell'impiegato!");
		}
		
		return null;
	}
	
	/*
	 * Description : Controlla l'esistenza di un nome di una sede all'interno del file delle sedi
	 * Parameters : String nome -> nome della sede da controllare
	 * Returns : boolean -> risultato ricerca, true -> il nome è duplicato | false -> il nome non è duplicato
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
								this.indirizzo+System.lineSeparator()
							).toLowerCase();
	}
	
	public void Print() {
		System.out.println("Nome : "+this.nome);
		System.out.println("Indirizzo : "+ this.indirizzo);
	}
}
