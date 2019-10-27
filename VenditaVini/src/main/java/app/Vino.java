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
	
	public Vino(String[] dettagli) {
		try {
			this.nome = dettagli[0];
			this.anno = Integer.parseInt(dettagli[1]);
			this.note = dettagli[2];
			this.vitigno = dettagli[3];
			this.numeroBottiglie = Integer.parseInt(dettagli[4]);
		}catch(Exception e){
			e.getMessage();
		}
	}
	
	public void Print() {
		System.out.println("Nome :"+ nome);
		System.out.println("Anno :"+ anno);
		System.out.println("Note :"+ note);
		System.out.println("Vitigno :"+ vitigno);
		System.out.println("Numero disponibile :"+ numeroBottiglie +"\n");
		
	}

}
