package app;

public class Persona {
	
	//TODO: Adattare lo scope degli attributi
	
	public String nome = "";
	public String cognome = "";
	public String mail = "";
	public String password = "";
	
	Persona(String nome, String cognome, String mail, String password){
		this.nome = nome;
		this.cognome = cognome;
		this.mail = mail;
		this.password = password;
	}
}
