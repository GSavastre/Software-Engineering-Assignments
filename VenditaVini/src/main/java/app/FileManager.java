package app;

import java.io.File;
import java.util.ArrayList;

public class FileManager {
	public final String fileUtenti = "./src/main/resources/utenti.csv";
	public final String fileOrdini = "./src/main/resources/ordini.csv";
	public final String fileNotifiche = "./src/main/resources/notifiche.csv";
	public final String fileVini   = "./src/main/resources/vini.csv";
	
	/*
	 * Cerca i file necessari per il funzionamento del programma
	 * nel caso non esistano li genera nuovi
	 */
	
	public FileManager(){
		ArrayList<File> listaFiles = new ArrayList<File>() {
			{
				add(new File(fileUtenti));
				add(new File(fileOrdini));
				add(new File(fileVini));
				add(new File(fileNotifiche));
			}
		};
		
		for(File f : listaFiles) {
			if(!f.isFile()) {
				try {
					f.createNewFile();
				}catch(Exception e) {
					e.getMessage();
				}
			}
		}
	}
}
