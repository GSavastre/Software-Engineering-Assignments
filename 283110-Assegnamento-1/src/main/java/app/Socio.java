package app;

public class Socio extends Persona {
  
  Socio(String nome, String cognome, String mail, String password){
    super(nome, cognome, mail, password);
  }
  
  public Persona[] IscriviAdEvento(Evento evento) {
	  return evento.AggiungiPersona(this);
  }
  
  public Persona[] CancellaIscrizioneAdEvento(Evento evento) {
	  return evento.RimuoviPersona(this);
  }
}