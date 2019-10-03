package app;

public class Circolo {
	
	public String nome = "";
	public Persona[] partecipanti = null;
	public Evento[] eventi = null;
	
	Circolo(String nome){
		this.nome = nome;
	}
	
	Circolo(String nome, Persona[] partecipanti){
		this.nome = nome;
		this.partecipanti = partecipanti;
	}
	
	Circolo(String nome, Evento[] eventi){
		this.nome = nome;
		this.eventi = eventi;
	}
	
	Circolo(String nome, Persona[] partecipanti, Evento[] eventi){
		this.nome = nome;
		this.partecipanti = partecipanti;
		this.eventi = eventi;
	}
	
}
