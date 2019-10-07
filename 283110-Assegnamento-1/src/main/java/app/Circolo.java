package app;

public class Circolo {
	
	public String nome = "";
	public Persona[] partecipanti = null;
	public Evento[] eventi = null;
	
	Circolo(String nome){
		this.nome = nome;
	}
	
	Circolo(String nome, Persona[] partecipanti){
		this.nome = nome;
		this.partecipanti = partecipanti;
	}
	
	Circolo(String nome, Evento[] eventi){
		this.nome = nome;
		this.eventi = eventi;
	}
	
	Circolo(String nome, Persona[] partecipanti, Evento[] eventi){
		this.nome = nome;
		this.partecipanti = partecipanti;
		this.eventi = eventi;
	}
	
	public Evento[] AggiungiEvento(Evento evento) {
		  if(eventi == null) {
			  eventi = new Evento[0];
		  }
		  
		  //Creo nuovo array maggiorato per il nuovo evento
		  Evento[] nuovaLista = new Evento[eventi.length + 1];
		  
		  //Controllo che l'evento non sia già registrato nel circolo
		  if(this.PresenzaEvento(evento) == -1) {
			  System.arraycopy(eventi, 0, nuovaLista, 0, eventi.length);
			  nuovaLista[eventi.length] = evento;
			  
			  eventi = nuovaLista;
		  }
		  
		  return eventi;
	}
	
	public Evento[] RimuoviEvento(Evento evento) {
		if(eventi != null) {
			  //Creo nuovo array ridotto per l'esclusione dell'evento rimosso
			  Evento[] nuovaLista = new Evento[eventi.length - 1];
			  
			  //Controllo che l'evento sia già registrato nel circolo
			  int indicePresenza = this.PresenzaEvento(evento);
			  
			  if(indicePresenza == 0) {
				  System.arraycopy(eventi, 1, nuovaLista, 0, eventi.length-1);
				  eventi = nuovaLista;
			  }else if(indicePresenza > 0) {
				  //Ricopio la prima parte dell'array
				  System.arraycopy(eventi, 0, nuovaLista, 0, indicePresenza);
				  
				  //Ricopio la seconda parte escludendo l'evento rimosso
				  System.arraycopy(eventi, indicePresenza + 1, nuovaLista	, indicePresenza, eventi.length - indicePresenza - 1);
				  
				  eventi = nuovaLista;
			  }
		  }
		  
		  return eventi;
	}
	
	/*Cerca l'indice in cui si trova un evento all'interno dell'array di eventi
	   * Nel caso non sia trovata ritorna -1
	   */
	  public int PresenzaEvento(Evento evento) {
		  int indiceTrovato = -1;
		  
		  for(int i = 0; i < this.eventi.length; i++) {
			  //Overload della funzione equals() per classe di tipo Evento
			  if(evento.equals(eventi[i])) {
				  indiceTrovato = i;
			  }
		  }
		  
		  return indiceTrovato;
	  }
	
}
