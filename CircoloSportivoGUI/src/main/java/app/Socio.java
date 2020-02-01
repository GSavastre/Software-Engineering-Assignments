//Savastre Cosmin Gabriele 283110 Assegnamento 1

package app;

public class Socio extends Persona {
  
  public Socio(String nome, String cognome, String mail, String password){
    super(nome, cognome, mail, password);
  }
  
  public Socio() {
	super();
}

public Persona[] IscriviAdEvento(Evento evento) {
	  return evento.AggiungiPersona(this);
  }
  
  public Persona[] CancellaIscrizioneAdEvento(Evento evento) {
	  return evento.RimuoviPersona(this);
  }
}