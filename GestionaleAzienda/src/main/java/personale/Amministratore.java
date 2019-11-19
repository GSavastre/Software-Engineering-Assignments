package personale;

import java.time.LocalDate;

import strutture.Sede;

public class Amministratore extends Impiegato{

	public Amministratore(String nome, String cognome, String codiceFiscale, Sede sedeLavorativa,
			LocalDate inizioAttivita, LocalDate fineAttivita) {
		super(nome, cognome, codiceFiscale, sedeLavorativa, inizioAttivita, fineAttivita);
	}
	
	public Amministratore(String[] parametri) {
		super(parametri);
	}
}
