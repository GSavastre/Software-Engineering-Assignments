package app;

public class Persona {
	public String nome;
	public String cognome;
	public String email;
	public String password;

	public Persona() { }
	
	public Persona(String nome, String cognome, String email, String password) {
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.password = password;
	}
}
