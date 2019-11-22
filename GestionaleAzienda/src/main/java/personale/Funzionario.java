package personale;

import java.time.LocalDate;

import strutture.Sede;

public class Funzionario extends Impiegato{
	public Funzionario(String nome, String cognome, String codiceFiscale, Sede sedeLavorativa, LocalDate inizioAttivita, LocalDate fineAttivita) {
		super(nome, cognome, codiceFiscale, sedeLavorativa, inizioAttivita, fineAttivita);
	}
	
	public Funzionario(String nome, String cognome, Sede sedeLavorativa, LocalDate inizioAttivita) {
		super(nome, cognome, sedeLavorativa, inizioAttivita);
	}
	
	public Funzionario(String[] parametri) {
		super(parametri);
	}
}
