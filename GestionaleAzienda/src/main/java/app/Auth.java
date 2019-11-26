package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import filemanager.FileManager;
import personale.Impiegato;

public class Auth {
	
	private static FileManager files = new FileManager();
	
	public static Impiegato Login(String nome, String cognome, String password) {
		Impiegato impiegato = Impiegato.CaricaDaFile(nome, cognome);
		
		if(impiegato.equals(null)) {
			return null;
		}
		
		try(BufferedReader fin = new BufferedReader(new FileReader(files.FILEAUTH))){
			String riga = null;
			String[] parametri;
			
			while((riga = fin.readLine()) != null) {
				if(!riga.startsWith("#")) {
					parametri = riga.split(",");
					
					if(impiegato.codiceFiscale.contentEquals(parametri[0]) && password.contentEquals(parametri[1])) {
						return impiegato;
					}
				}
			}
			
			
		}catch(IOException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Impossibile procedere con l'accesso dell'impiegato!");
		}catch(Exception e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Errore nel processo di accesso dell'impiegato!");
		}
		
		return null;
	}
	
	public static boolean Register(Impiegato nuovoImpiegato, String password) {
		ArrayList<String> elementi = new ArrayList<String>();
		
		try(BufferedReader fin = new BufferedReader(new FileReader(files.FILEAUTH))) {
			String riga = null;
			
			while((riga = fin.readLine()) != null) {
				if(!riga.startsWith("#")) {
					if(nuovoImpiegato.codiceFiscale.contentEquals(riga.split(",")[0])) {
						return false;
					}else {
						elementi.add(riga);
					}
				}
			}
			
			elementi.add(String.join(",", nuovoImpiegato.codiceFiscale, password));
			//Sovrascrittura su file
			File fvecchio = new File(files.FILEAUTH);
			fvecchio.delete();
			
			File fout = new File(files.FILEAUTH);
			try {
				FileWriter fnuovo = new FileWriter(fout, false);
				//TODO: In questo caso l'uso di dictionary faciliterebbe la scrittura di commenti in caso di cambiamento della struttura della classe (si possono ciclare le chiavi)
				fnuovo.write("#codiceFiscale,password"+System.lineSeparator());
				
				for(String s : elementi) {
					fnuovo.write(s.toLowerCase() + System.lineSeparator());
				}
				
				fnuovo.close();
				return true;
				
			}catch(IOException e) {
				e.getMessage();
				System.out.println("Errore nella sovrascrittura del file impiegati!");
			}
		}catch(IOException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Impossibile procedere con la registrazione del nuovo impiegato!");
		}catch(Exception e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("Errore nel processo di registrazione del nuovo impiegato!");
		}
		
		return false;
	}
}
