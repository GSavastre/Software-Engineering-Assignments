package app;

public class Vino {
	
	public String nome;
	public int anno;
	public String note;
	//public Vitigno vitigno;
	public String vitigno;
	public int numeroBottiglie;
	
	public Vino() {}
	
	public Vino(String nome, int anno, String note,String vitigno, int numeroBottiglie){
		this.nome = nome;
		this.anno = anno;
		this.note = note;
		this.vitigno = vitigno;
		this.numeroBottiglie = numeroBottiglie;
	}
	
	/*
	 * Costruttore per la ricerca di un vino eseguita da un utente
	 */
	public Vino(String nome, int anno) {
		this.nome = nome;
		this.anno = anno;
	}
	
	public void Print() {
		System.out.println("Nome :"+ nome);
		System.out.println("Anno :"+ anno);
		System.out.println("Note :"+ note);
		System.out.println("Vitigno :"+ vitigno);
		System.out.println("Numero disponibile :"+ numeroBottiglie);
	}

}
