package personale;

import java.time.LocalDate;

import strutture.Sede;

public class Dirigente extends Impiegato{

	public Dirigente(String nome, String cognome, String codiceFiscale, Sede sedeLavorativa, LocalDate inizioAttivita,
			LocalDate fineAttivita) {
		super(nome, cognome, codiceFiscale, sedeLavorativa, inizioAttivita, fineAttivita);
	}

	public Dirigente(String[] parametri) {
		super(parametri);
	}
}
