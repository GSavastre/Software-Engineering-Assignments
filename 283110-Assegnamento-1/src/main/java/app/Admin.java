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
  
  public void AggiungiEvento(Circolo circolo, String nomeEvento, String tipo) {
    if(tipo.equals("Corso") || tipo.equals("corso")) {
    	//TODO
    }else if(tipo.equals("Gara") || tipo.equals("gara")){
    	//TODO
    }
  }
  
  public void RimuoviEvento(Circolo circolo,Evento evento) {
     //TODO
  }
  
  public void ModificaEvento(Evento evento) {
    //TODO
  }
}