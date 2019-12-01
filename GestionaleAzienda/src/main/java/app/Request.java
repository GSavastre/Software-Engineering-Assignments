package app;

import java.io.Serializable;

import personale.Impiegato;

public class Request implements Serializable{
	private static final long serialVersiounUID = 1L;
	
	//private final int value;
	private final Impiegato richiedente;
	private final String azione;
	private final String parametri;
	
	public Request(Impiegato impiegato, String azione, String parametri) {
		this.richiedente = impiegato;
		this.azione = azione;
		this.parametri = parametri;
	}
	
	public Request(String azione, String parametri) {
		this.richiedente = null;
		this.azione = azione;
		this.parametri = parametri;
	}
	
	public Impiegato GetRichiedente() {
		return richiedente;
	}
	
	public String GetAzione() {
		return azione;
	}
	
	public String GetParametri() {
		return parametri;
	}
	
	/*public Request(final int v) {
		this.value = v;
	}
	
	public int GetValue() {
		return this.value;
	}*/
}
