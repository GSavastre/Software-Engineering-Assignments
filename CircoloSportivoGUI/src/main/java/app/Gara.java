//Savastre Cosmin Gabriele 283110 Assegnamento 1

package app;

public class Gara extends Evento{
  
	Gara(String nome, Persona[] partecipanti) {
		super(nome, partecipanti);
	}

	public Gara(String nome) {
		super(nome);
	}
	
	public boolean equals(Object obj) {
		  return obj instanceof Gara && this.nome.equals(((Gara)obj).nome);
	  }
}