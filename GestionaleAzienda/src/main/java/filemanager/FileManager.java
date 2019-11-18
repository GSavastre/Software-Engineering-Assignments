//Savastre Cosmin Gabriele 283110
package filemanager;

import java.io.File;
import java.util.ArrayList;
/*
 * Classe "statica" usata per trovare i percorsi dei file contenenti tutte le informazioni
 */
public class FileManager {
	
	public final String FILEIMPIEGATI = "./src/main/resources/impiegati.csv";
	public final String FILESEDI = "./src/main/resources/sedi.csv";
	
	/*
	 * Cerca i file necessari per il funzionamento del programma
	 * nel caso non esistano li genera nuovi
	 */
	
	public FileManager(){
		ArrayList<File> listaFiles = new ArrayList<File>() {
			{
				add(new File(FILEIMPIEGATI));
				add(new File(FILESEDI));
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
