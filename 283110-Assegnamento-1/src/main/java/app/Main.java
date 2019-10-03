package app;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		Persona[] persone = {
			new Socio("Nome A","Cognome A", "Mail a", "Password A"),
			new Socio("Nome B","Cognome B", "Mail B", "Password B"),
			new Socio("Nome C","Cognome C", "Mail C", "Password C"),
			
			new Admin("Nome D","Cognome D", "Mail D", "Password D"),
			new Admin("Nome E","Cognome E", "Mail E", "Password E"),
		};
		
		Evento[] eventi = {
			new Corso("Evento A", new Persona[] {persone[0],persone[1]}),
			new Gara("Evento B", persone)
		};
		
		Circolo circolo = new Circolo("Circolo A", persone, eventi);
	/*
	 * 	Admin nuovoAdmin = new Admin();
		
		Scanner input = new Scanner(System.in);
		nuovoAdmin.SetAttributes();
		nuovoAdmin.GetAttributes();*/
		
		/*
		int numUtenti = 0;
		
		do {
			System.out.println("Quanti utenti vuoi inserire?");
			numUtenti = input.nextInt();
		}while(numUtenti <= 0);
		
		Persona[] utenti = new Persona[numUtenti];
		
		int numAttivita = 0;
		
		do {
			System.out.println("Quante attivita vuoi inserire?");
			numAttivita = input.nextInt();
		}while(numAttivita <= 0);
		
		Evento[] attivita = new Evento[numAttivita];
		
		for(int i = 0; i < numUtenti; i++) {
			System.out.println("Inserisci i dati dell'utente n."+ i+1);
			
		}
		
		for(int i = 0; i < numAttivita; i++) {
			System.out.println("Inserisci i dati dell'attivita n."+ i+1);
			
		}*/
	}
}