package app;

import java.io.Serializable;
import java.util.ArrayList;

import personale.Impiegato;

public class Response implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private boolean esito;
	private ArrayList<Impiegato> risultato;
	
	public Response() {
		this.esito = false;
		this.risultato = null;
	}
	
	public Response(final boolean esito, final ArrayList<Impiegato> risultato) {
		this.esito = esito;
		this.risultato = risultato;
	}
	
	public void SetEsito(boolean val) {
		this.esito = val;
	}
	
	public boolean GetEsito() {
		return esito;
	}
	
	public void SetRisultato(ArrayList<Impiegato> ris) {
		this.risultato = ris;
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
