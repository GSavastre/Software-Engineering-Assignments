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
  
  public Persona[] AggiungiPersona(Persona persona) {
	  
	  //Creo nuovo array maggiorato per il nuovo iscritto
	  Persona[] nuovaLista = new Persona[iscritti.length + 1];
	  
	  //Controllo che la persona non sia già iscritta all'evento
	  if(this.PresenzaIscritto(persona) == -1) {
		  System.arraycopy(iscritti, 0, nuovaLista, 0, iscritti.length);
		  nuovaLista[iscritti.length] = persona;
		  
		  iscritti = nuovaLista;
	  }
	  
	  return iscritti;
  }
  
  public Persona[] RimuoviPersona() {
	  
	  return iscritti;
  }
  
  /*Cerca l'indice in cui si trova una persona all'interno dell'array di iscritti
   * Nel caso non sia trovata ritorna -1
   */
  public int PresenzaIscritto(Persona persona) {
	  int indiceTrovato = -1;
	  
	  for(int i = 0; i < this.iscritti.length; i++) {
		  if(persona.equals(iscritti[i])) {
			  indiceTrovato = i;
		  }
	  }
	  
	  return indiceTrovato;
  }
}