//Savastre Cosmin Gabriele 283110 Assegnamento 1

package app;

import java.util.Scanner;

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
  
  public Evento SetAttributes() {
	  Scanner input = new Scanner(System.in);
	  
	  System.out.print("Nome:"); this.nome = input.nextLine();
	  
	  input.close();
	  
	  return this;
  }
  
  /*
   * Aggiunge una persona all'interno della lista di iscritti dell'evento
   * Nel caso la persona fosse già iscritta non verrà aggiunta
   * Nel caso l'array di iscritti sia vuoto verrà creato un array
   * di un elemento con quella persona al suo interno.
   */
  public Persona[] AggiungiPersona(Persona persona) {
	  
	  if(iscritti == null) {
		  iscritti = new Persona[0];
	  }
	  
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
  
  /*
   * Rimuove una persona dalla lista di iscritti dell'evento
   * Nel caso l'evento non abbia iscritti oppure non esista tale persona nella lista di iscritti verrà ritornata la
   * lista corrente di iscritti all'evento.
   */
  public Persona[] RimuoviPersona(Persona persona) {
	  
	  if(iscritti != null) {
		  //Creo nuovo array ridotto per l'esclusione della persona rimossa
		  Persona[] nuovaLista = new Persona[iscritti.length - 1];
		  
		  //Controllo che la persona sia già iscritta all'evento
		  int indicePresenza = this.PresenzaIscritto(persona);
		  
		  if(indicePresenza == 0) {
			  System.arraycopy(iscritti, 1, nuovaLista, 0, iscritti.length-1);
			  iscritti = nuovaLista;
		  }else if(indicePresenza > 0) {
			  //Ricopio la prima parte dell'array
			  System.arraycopy(iscritti, 0, nuovaLista, 0, indicePresenza);
			  
			  //Ricopio la seconda parte escludendo la persona rimossa
			  System.arraycopy(iscritti, indicePresenza + 1, nuovaLista	, indicePresenza, iscritti.length - indicePresenza - 1);
			  
			  iscritti = nuovaLista;
		  }
	  }
	  
	  return iscritti;
  }
  
  /*Cerca l'indice in cui si trova una persona all'interno dell'array di iscritti
   * Nel caso non sia trovata ritorna -1
   */
  public int PresenzaIscritto(Persona persona) {
	  int indiceTrovato = -1;
	  
	  for(int i = 0; i < this.iscritti.length; i++) {
		  //Overload della funzione equals() per classe di tipo Persona
		  if(persona.equals(iscritti[i])) {
			  indiceTrovato = i;
		  }
	  }
	  
	  return indiceTrovato;
  }
  
  public boolean equals(Object obj) {
	  return obj instanceof Evento && this.nome.equals(((Evento)obj).nome);
  }
}