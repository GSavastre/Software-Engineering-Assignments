package app;

public class Gara extends Evento{
  
	Gara(String nome, Persona[] partecipanti) {
		super(nome, partecipanti);
	}

	public Gara(String nome) {
		super(nome);
	}
}