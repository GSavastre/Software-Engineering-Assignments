package app;

public class Impiegato extends Persona{

	public Impiegato(String nome, String cognome, String email, String password) {
		super(nome, cognome, email, password);
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
