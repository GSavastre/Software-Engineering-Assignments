package personale;

import java.time.LocalDate;
import java.util.Date;

import strutture.Sede;

public class Funzionario extends Impiegato{
	public Funzionario(String nome, String cognome, String codiceFiscale, Sede sedeLavorativa, LocalDate inizioAttivita, LocalDate fineAttivita) {
		super(nome, cognome, codiceFiscale, sedeLavorativa, inizioAttivita, fineAttivita);
	}
	
	public Funzionario(String[] parametri) {
		super(parametri);
	}
}
