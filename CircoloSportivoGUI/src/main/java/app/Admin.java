//Savastre Cosmin Gabriele 283110 Assegnamento 1

package app;

public class Admin extends Persona{

  public Admin(String nome, String cognome, String mail, String password){
    super(nome, cognome, mail, password);
  }
  
  public Admin() {
	  super();
  }

  public Persona[] AggiungiUtente(Evento evento,Persona utente) {
    return evento.AggiungiPersona(utente);
  }
  
  public Persona[] AggiungiUtente(Circolo circolo, Persona utente) {
	  return circolo.AggiungiPersona(utente);
  }
  
  public Persona[] RimuoviUtente(Evento evento,Persona utente) {
    return evento.RimuoviPersona(utente);
  }
  
  public Persona[] RimuoviUtente(Circolo circolo, Persona utente) {
	  return circolo.RimuoviPersona(utente);
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