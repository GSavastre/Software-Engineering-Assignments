package app;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		ArrayList<String> listaPrimaScelta = new ArrayList<String>() {
			{
				add("Login");
				add("Registrazione");
			}
		};
		
		stampaMenu(listaPrimaScelta);
		
		switch(ottieniScelta(listaPrimaScelta.size())) {
		
			//Uscita dall'applicazione
			case 0:
				System.out.println("Logout avvenuto con successo");
				System.exit(0);
				break;
			
			//Login
			case 1:
				break;
			
			//Registrazione
			case 2:
				
				break;
				
			default:
				System.out.println("Errore inaspettato!");
		}
	}
	
	
	private static void stampaMenu(ArrayList<String> scelte) {
		int counterIndex = 1;
		for(String scelta : scelte) {
			System.out.println("["+counterIndex+"] "+ scelta);
			counterIndex++;
		}
		
		System.out.println("[0] Esci");
	}
	
	private static int ottieniScelta(int limite) {
		Scanner input = new Scanner(System.in);
		
		//Non 0 perché è il valore di default per l'uscita
		int scelta = -1;
		
		do {
			System.out.print("Inserisci la tua scelta : ");
			try {
				scelta = Integer.parseInt(input.nextLine());
			}catch(NumberFormatException e){
				System.out.println("Selezione non valida");
			}
			
			if(scelta < 0 || scelta > limite) {
				System.out.println("Selezione non valida");
			}
		}while(scelta < 0 || scelta > limite);
		
		input.close();
		
		return scelta;
	}
	
	private void sceltaUtente() {
		
	}
	
	private void sceltaImpiegato() {
		
	}
	
	//TODO: Finish user registration
	
	private void registraUtente() {
		Scanner input = new Scanner(System.in);
		
		System.out.print("Inserisci il nome : ");
		String nome = input.nextLine();
		
		System.out.print("Inserisci il cognome : ");
		String cognome = input.nextLine();
		
		System.out.print("Inserisci l'email : ");
		String mail = input.nextLine();
		
		System.out.print("Inserisci la password : ");
		String pwd = input.nextLine();
		
		System.out.print("Ripeti la password : ");
		String pwdRipeti = input.nextLine();
		
		input.close();
	}

}
