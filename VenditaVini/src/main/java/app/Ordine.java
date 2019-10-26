package app;

import java.time.LocalDateTime;

public class Ordine {
	public Vino vino;
	public Utente acquirente;
	public Impiegato venditore;
	public boolean completato;
	public LocalDateTime data;
	
	public Ordine() {
		
	}
	
	public Ordine(Vino vino, Utente acquirente, Impiegato venditore, boolean completato) {
		this.vino = vino;
		this.acquirente = acquirente; 
		this.venditore = venditore;
		this.completato = completato;
		this.data = LocalDateTime.now();
	}
	
	public void CompletaOrdine() {
		this.completato = true;
	}
}
