package personale;

import java.time.LocalDate;

import strutture.Sede;

public class Operaio extends Impiegato{

	public Operaio(String nome, String cognome, String codiceFiscale, Sede sedeLavorativa, LocalDate inizioAttivita, LocalDate fineAttivita) {
		super(nome, cognome, codiceFiscale, sedeLavorativa, inizioAttivita, fineAttivita);
	}
	
	public Operaio(String nome, String cognome, Sede sedeLavorativa, LocalDate inizioAttivita) {
		super(nome, cognome, sedeLavorativa, inizioAttivita);
	}
	
	public Operaio(String[] parametri) {
		super(parametri);
	}
	
}
