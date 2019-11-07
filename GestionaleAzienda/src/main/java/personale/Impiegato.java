package personale;

import java.util.Date;

import strutture.Sede;

public class Impiegato {
	public String nome;
	public String cognome;
	public String codiceFiscale;
	public Sede sedeLavorativa;
	public Date inizioAttivita;
	public Date fineAttivita;
	
	public Impiegato(String nome, String cognome, String codiceFiscale, Sede sedeLavorativa, Date inizioAttivita, Date fineAttivita) {
		this.nome = nome;
		this.cognome = cognome;
		this.codiceFiscale = codiceFiscale;
		this.sedeLavorativa = sedeLavorativa;
		this.inizioAttivita = inizioAttivita;
		
		if(fineAttivita.compareTo(inizioAttivita) > 0) {
			this.fineAttivita = fineAttivita;
		}else {
			this.fineAttivita = null;
		}
	}
}
