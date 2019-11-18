package strutture;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import filemanager.FileManager;

public class Sede {
	public String nome;
	public String indirizzo;
	private static FileManager files = new FileManager();
	
	public Sede(String nome, String indirizzo) {
		this.nome = nome;
		this.indirizzo = indirizzo;
	}
	
	public Sede(String[] parametri) {
		try {
			this.nome = parametri[0];
			this.indirizzo = parametri[1];
		}catch(Exception e) {
			e.getMessage();
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
					
					if(nome.contentEquals(sedeFile.nome)) {
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
}
