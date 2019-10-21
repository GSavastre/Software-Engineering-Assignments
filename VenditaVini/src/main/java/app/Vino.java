package app;

public class Vino {
	
	public String nome;
	public int anno;
	public String note;
	public Vitigno vitigno;
	public int numeroBottiglie;
	
	public Vino() {}
	
	public Vino(String nome, int anno, String note,Vitigno vitigno, int numeroBottiglie){
		this.nome = nome;
		this.anno = anno;
		this.note = note;
		this.vitigno = vitigno;
		this.numeroBottiglie = numeroBottiglie;
	}

}
