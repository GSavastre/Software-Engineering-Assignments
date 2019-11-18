package personale;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import strutture.Sede;
import filemanager.FileManager;

public class Impiegato {
	public String nome;
	public String cognome;
	public String codiceFiscale;
	public Sede sedeLavorativa;
	public Date inizioAttivita;
	public Date fineAttivita;
	protected FileManager files;
	
	public Impiegato(String nome, String cognome, String codiceFiscale, Sede sedeLavorativa, Date inizioAttivita, Date fineAttivita) {
		
		try {
			files = new FileManager();
		}catch(Exception e) {
			e.getMessage();
		}
		
		this.nome = nome;
		this.cognome = cognome;
		this.codiceFiscale = codiceFiscale;
		this.sedeLavorativa = sedeLavorativa;
		this.inizioAttivita = inizioAttivita;
		
		if(fineAttivita.compareTo(inizioAttivita) > 0) {
			this.fineAttivita = fineAttivita;
		}else {
			this.fineAttivita = null;
		}
	}
	
	public void SalvaSuFile(final Impiegato persona) {
		
		String nuovaPersona = persona.toString();
		
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
						if(codiceFiscale.contentEquals(persona.codiceFiscale) || (nome.contentEquals(persona.nome) && cognome.contentEquals(persona.cognome))) {
							elementi.add(nuovaPersona);
						}else {
							elementi.add(riga);
						}
					}catch(Exception e) {
						e.getMessage();
						System.out.println("Formattazione dati impiegati errata!");
					}
					
					////Sovrascrittura su file
					File fvecchio = new File(files.FILEIMPIEGATI);
					fvecchio.delete();
					
					File fout = new File(files.FILEIMPIEGATI);
					try {
						FileWriter fnuovo = new FileWriter(fout, false);
						//TODO: In questo caso l'uso di dictionary faciliterebbe la scrittura di commenti in caso di cambiamento della struttura della classe (si possono ciclare le chiavi)
						fnuovo.write("#nome,cognome,codiceFiscale,nomeSedeLavorativa,inizioAttivita,fineAttivita");
						
						for(String s : elementi) {
							fnuovo.write(s);
						}
						
						fnuovo.close();
						
					}catch(IOException e) {
						e.getMessage();
						System.out.println("Errore nella sovrascrittura del file impiegati!");
					}
				}
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
	
	public Impiegato CaricaDaFile() {
		//TODO
		return null;
	}
	
	/*
	 * description : toString() ->Cambia un oggetto Impiegato in una String per il salvataggio su file
	 * parametri : Impiegato persona -> persona da cui recuperare i parametri
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
									this.fineAttivita.toString()
								);
	}
}
