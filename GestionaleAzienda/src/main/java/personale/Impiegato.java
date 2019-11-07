package personale;

import java.util.Date;

import strutture.Sede;
import filemanager.FileManager;

public class Impiegato {
	public String nome;
	public String cognome;
	public String codiceFiscale;
	public Sede sedeLavorativa;
	public Date inizioAttivita;
	public Date fineAttivita;
	public FileManager files;
	
	public Impiegato(String nome, String cognome, String codiceFiscale, Sede sedeLavorativa, Date inizioAttivita, Date fineAttivita) {
		
		try {
			files = new FileManager();
		}catch(Exception e) {
			e.getMessage();
		}
		
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
