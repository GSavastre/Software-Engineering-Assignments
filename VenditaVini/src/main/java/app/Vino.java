package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
	private FileManager files;
	
	public Vino() {}
	
	public Vino(String nome, int anno, String note,String vitigno, int numeroBottiglie){
		this.nome = nome;
		this.anno = anno;
		this.note = note;
		this.vitigno = vitigno;
		this.numeroBottiglie = numeroBottiglie;
		this.files = new FileManager();
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
	
	public boolean Rimuovi(int quantita) {
		if(numeroBottiglie < quantita) {
			return false;
		}
		
		SalvaSuFile();
		return true;
	}
	
	//nome,anno,note,vitigno,numeroBottiglie
	public void SalvaSuFile() {
		//Controllo esistenza di questo vino sul file
		boolean exists = false;
		
		//Lista contenente tutti i vini gi� segnati su file
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
						
						//Questo sar� il nuovo vino sovrascritto nel caso sia duplicato altrimenti verr� aggiunto un vino normalmente
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
	
	public String ToFileString() {
		return this.nome+","+this.anno+","+this.note+","+this.vitigno+","+this.numeroBottiglie+System.lineSeparator();
	}
	
	public void Print() {
		System.out.println("Nome :"+ nome);
		System.out.println("Anno :"+ anno);
		System.out.println("Note :"+ note);
		System.out.println("Vitigno :"+ vitigno);
		System.out.println("Numero disponibile :"+ numeroBottiglie +"\n");
		
	}

}
