package app;

public class Evento {
	
	public String nome = "";
	public Persona[] partecipanti = null;
	
	Evento(String nome, Persona[] partecipanti){
		this.nome = nome;
		this.partecipanti = partecipanti;
	}
}
