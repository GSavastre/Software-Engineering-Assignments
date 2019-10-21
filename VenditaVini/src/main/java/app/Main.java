package app;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		ArrayList<String> primaScelta = new ArrayList<String>() {
			{
				add("Login");
				add("Registrazione");
			}
		};
		
		stampaMenu(primaScelta);
	}
	
	
	private static void stampaMenu(ArrayList<String> scelte) {
		int counterIndex = 1;
		for(String scelta : scelte) {
			System.out.println("["+counterIndex+"] "+ scelta);
			counterIndex++;
		}
		
		System.out.println("[0] Esci");
	}
	
	private int ottieniScelta(int limite) {
		Scanner input = new Scanner(System.in);
		
		//Non 0 perché è il valore di default per l'uscita
		int scelta = -1;
		
		do {
			System.out.println("Inserisci la tua scelta : ");
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

}
