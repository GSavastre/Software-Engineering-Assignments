package app;

import java.io.Serializable;
import java.util.ArrayList;

import personale.Impiegato;

public class Response implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private boolean esito;
	private ArrayList<Impiegato> risultato;
	
	public Response(final boolean esito, final ArrayList<Impiegato> risultato) {
		this.esito = esito;
		this.risultato = risultato;
	}
	
	public boolean GetEsito() {
		return esito;
	}
	
	public ArrayList<Impiegato> GetRisultato(){
		return risultato;
	}
	
	/*private final int value;
	
	public Response(final int v) {
		this.value = v;
	}
	
	public int GetValue() {
		return this.value;
	}*/
}
