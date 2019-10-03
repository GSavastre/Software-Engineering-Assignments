package app;

public class Admin extends Persona{

  Admin(String nome, String cognome, String mail, String password){
    super(nome, cognome, mail, password);
  }
  
  public Admin() {
	// TODO Auto-generated constructor stub
	  super();
  }

public void AggiungiUtente(Evento evento,Persona utente) {
    //TODO: Implementa aggiunta di un partecipante
  }
  
  public void RimuoviUtente(Evento evento,String mail) {
    //TODO: Rimuovi utenti in base alla loro mail
  }
  
  public void ModificaUtente(Evento evento, Persona utente) {
    //TODO: Modifica un utente
  }
  
  public void AggiungiEvento(String nome, String type) {
    //TODO
  }
  
  public void RimuoviEvento(String nome) {
     //TODO
  }
  
  public void ModificaEvento(String nome) {
    //TODO
  }
}