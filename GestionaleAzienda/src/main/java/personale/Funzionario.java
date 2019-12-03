package personale;

import java.time.LocalDate;

import strutture.Sede;

public class Funzionario extends Impiegato{
	public Funzionario(String nome, String cognome, String codiceFiscale, /*Sede*/String sedeLavorativa, LocalDate inizioAttivita, LocalDate fineAttivita) {
		super(nome, cognome, codiceFiscale, sedeLavorativa, inizioAttivita, fineAttivita);
	}
	
	public Funzionario(String nome, String cognome, /*Sede*/String sedeLavorativa, LocalDate inizioAttivita) {
		super(nome, cognome, sedeLavorativa, inizioAttivita);
	}
	
	public Funzionario(String[] parametri) {
		super(parametri);
	}
	
	public void SalvaSuFile() {
		super.SalvaSuFile(this);
	}
}
