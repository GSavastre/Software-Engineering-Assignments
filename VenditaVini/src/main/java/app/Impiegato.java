package app;

public class Impiegato extends Persona{

	public Impiegato() {
		super();
	}
	
	public Impiegato(String nome, String cognome, String email, String password) {
		super(nome, cognome, email, password);
	}
	
	
	//TODO: Aggiungere throws
	public Impiegato(String[] credenziali) {
		super(credenziali[0], credenziali[1], credenziali[2], credenziali[3]);
	}
	
	/*
	 * Spedisce un certo tipo di vino ad un utente
	 * TODO: Exception handling
	 */
	public void SpedisciVino(Vino vino, Utente utente) {
		//TODO:Spedizione
	}
	
	/*
	 * Rifornisce la quantità disponibile di un certo vino
	 */
	public void RifornisciVino(Vino vino, int quantita) {
		//TODO: Accesso al file di vini per l'incremento della quantità
	}

}
