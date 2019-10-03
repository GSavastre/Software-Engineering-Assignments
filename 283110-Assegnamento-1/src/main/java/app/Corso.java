package app;

public class Corso extends Evento{
  
  Corso(String nome, Persona[] partecipanti){
    super(nome, partecipanti);
  }

  public Corso(String nome) {
	  super(nome);
  }
}