//Savastre Cosmin Gabriele 283110
package app;

import java.io.BufferedReader;
import java.io.FileReader;

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
}
