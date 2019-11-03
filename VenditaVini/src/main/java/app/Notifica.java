package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Notifica {
	
	/*
	 * Crea una notifica
	 * isSpedizione viene usato per distinguere i tipi di notifiche che possono essere due, spedizione oppure rifornimento
	 * se isSpedizione è true allora la notifica sarà una spedizione altrimenti sarà un rifornimento (sempre riguardanti i vini)
	 * return : true -> La notifica è stata salvata correttamente sul file
	 * return : false -> Ci sono stati problemi e la notifica non è stata salvata sul file
	 */
	public static boolean CreaNotifica(String emailUtente, String emailImpiegato, Vino vino, boolean isSpedizione) {
		
		FileManager files = new FileManager();
		
		String notifica = "";
		
		if(isSpedizione) {
			notifica += "spedizione,";
		}else {
			notifica += "rifornimento,";
		}
		
		notifica += emailUtente+","+emailImpiegato+","+vino.nome+","+String.valueOf(vino.anno)+System.lineSeparator();
		
		try(BufferedWriter fout = new BufferedWriter(new FileWriter(files.fileNotifiche))){
			fout.append(notifica);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Errore nel processo di creazione di una notifica");
		}
		
		return false;
	}
	
	/*
	 * Stampa le notifiche che sono indirizzate all'utente che abbia l'indirizzo email passato come parametro alla funzione
	 */
	public static void StampaNotifiche(String mailUtente) {
		
		//Percorso del file contenente le notifiche
		FileManager files = new FileManager();
		
		//Riga del file
		String riga = null;
		
		//Tutte le notifiche all'interno del file, dobbiamo leggerle tutte perché non è possibile sovrascrivere solo una riga all'interno di un file
		ArrayList<String[]> notifiche = new ArrayList<String[]>();
		
		//Apre il file per la lettura
		try(BufferedReader fin = new BufferedReader(new FileReader(files.fileNotifiche))){
			//Legge tutte le righe del file una per una
			while((riga = fin.readLine()) != null) {
				//Ignora i commenti
				if(!riga.startsWith("#") && !riga.isBlank()) {
					//Aggiungo alla lista di notifiche (split ritorna una String[])
					notifiche.add(riga.split(","));
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Errore nel processo di lettura delle notifiche");
		}
		
		//Se non esistono notifiche esci
		if(notifiche.size() == 0) {
			System.out.println("Al momento non hai nessuna nuova notifica");
			return;
		}
		
		//Salvataggio del file escludendo le notifiche lette
		
		File fvecchio = new File(files.fileNotifiche);
		fvecchio.delete();
		
		File fout = new File(files.fileNotifiche);
		try {
			try(FileWriter fnuovo = new FileWriter(fout,false)){
				//Le notifiche che sono indirizzate verso questo utente le stampo, il resto le scrivo sul file
				//In questo modo man mano che vengono mostrate le notifiche il file viene ripulito
				for(String[] notifica : notifiche) {
					if(notifica[1].contentEquals(mailUtente)) {
						if(notifica[0].contentEquals("spedizione")) {
							System.out.println("-Il vino "+notifica[3]+" dell'anno "+notifica[4]+" e' stato spedito!\n Per maggiori informazioni contatta "+notifica[2]);
						}else {
							System.out.println("-Il vino "+notifica[3]+" dell'anno "+notifica[4]+" è nuovamente disponibile per l'acquisto!");
						}
					}else {
						fnuovo.write(String.join(",", notifica)+System.lineSeparator());
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("Errore nel salvataggio delle notifiche");
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Errore nell'aggiornamento del file delle notifiche");
		}
	}
}
