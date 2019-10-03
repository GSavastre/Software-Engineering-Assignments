package app;

public class Evento {
  
  public String nome = "";
  public Persona[] iscritti = null;
  
  public Evento(String nome, Persona[] iscritti){
    this.nome = nome;
    this.iscritti = iscritti;
  }

  public Evento(String nome) {
	this.nome = nome;
	this.iscritti = null;
  }
}