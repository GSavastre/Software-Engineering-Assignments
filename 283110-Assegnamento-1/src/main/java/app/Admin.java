package app;

public class Admin extends Persona{

  Admin(String nome, String cognome, String mail, String password){
    super(nome, cognome, mail, password);
  }
  
  public Admin() {
	  super();
  }

  public void AggiungiUtente(Evento evento,Persona utente) {
    evento.AggiungiPersona(utente);
  }
  
  public void RimuoviUtente(Evento evento,Persona utente) {
    evento.RimuoviPersona(utente);
  }
  
  public void ModificaUtente(Persona utente) {
    utente.SetAttributes();
  }
  
  public Evento[] AggiungiEvento(Circolo circolo, Evento evento) {
    return circolo.AggiungiEvento(evento);
  }
  
  public Evento[] RimuoviEvento(Circolo circolo,Evento evento) {
     return circolo.RimuoviEvento(evento);
  }
  
  public Evento ModificaEvento(Evento evento) {
	  return evento.SetAttributes();
  }
}