package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Persona {
	public String nome;
	public String cognome;
	public String email;
	public String password;
	protected static FileManager files = new FileManager();

	public Persona() { }
	
	public Persona(String nome, String cognome, String email, String password) {
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.password = password;
		files = new FileManager();
	}
	
	public Persona(String[] parametri) {
		this.nome = parametri[0];
		this.cognome = parametri[1];
		this.email = parametri[2];
		this.password = parametri[3];
	}
	
	public static Persona RicercaPersona(String mail) {
		
		String riga = null;
		String[] dettagliPersona;
		//Apro il file dei vini
		try(BufferedReader fin = new BufferedReader(new FileReader(files.fileUtenti))){
			
			while((riga = fin.readLine())!= null) {
				if(!riga.startsWith("#")) {
					try {
						dettagliPersona = riga.split(",");
						
						if(dettagliPersona[2].contentEquals(mail)) {
							return new Persona(dettagliPersona);
						}
						
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}catch(Exception e) {
			e.getMessage();
		}
		
		return null;
	}
}
