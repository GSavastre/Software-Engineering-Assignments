package app;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import com.github.javafaker.Faker;

import personale.*;
import strutture.Sede;

public class Client {
	
	private static final int PORT = 5555;
	private static final String HOST = "localhost";
	
	//Credenziali testing
	private static final String CREDFUNZIONARIO = "Marco,Rossi,123";
	private static final String CREDAMMINISTRATORE = "Luca,Bianchi,123";
	private static final String CREDDIRIGENTE = "Carlo,Verdi,123";
	
	public static Scanner input = new Scanner(System.in);
	
	private static final int MAX = 100;

	public void Run() {
		try {
			Socket client = new Socket(HOST, PORT);
			
			ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
			ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
			
			Sede testSede = new Sede("SedeA","indirizzoa");
			Sede testSeconda = new Sede("SedeB","IndirizzoB");
			testSede.SalvaSuFile();
			testSeconda.SalvaSuFile();
			
			Request rq = new Request("login","Marco,Rossi,123");
			//System.out.format("Client sends: %s to server", rq.GetAzione());
			
			os.writeObject(rq);
			os.flush();
			
			Object o = is.readObject();
			
			if(o instanceof Response) {
				Response rs = (Response) o;
				
				System.out.format(" and received: %s from Server%n", rs.GetEsito());
				
				if(rs.GetEsito() == false || rs.GetRisultato().size() == 0) {
					client.close();
					return;
				}else {
					Impiegato impiegatoClient = rs.GetRisultato().get(0);
				}
			}
			
			/*while(true) {
				Request rq = new Request(r.nextInt(MAX));
				
				System.out.format("Client sends: %s to server", rq.GetValue());
				
				os.writeObject(rq);
				os.flush();
				
				if(is == null) {
					is = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
				}
				
				Object o = is.readObject();
				
				if(o instanceof Response) {
					Response rs = (Response) o;
					
					System.out.format(" and received: %s from Server%n", rs.GetValue());
					
					if(rs.GetValue() == 0) {
						break;
					}
				}
			}*/
			
			input.close();
			client.close();
			
		}catch(Exception e){
			e.getMessage();
			e.printStackTrace();
		}
	}
	/*
	 * Operazioni eseguibili da Dirigenti e Amministratori
	 */
	public void operazioniRicerca(ObjectOutputStream os, ObjectInputStream is) {
	}
	
	/*
	 * Operazioni eseguibili da Funzionari
	 */
	public void operazioniFunzionario() {
		
	}
	
	public static void main(String[] args) {
		
		/*PrintTestingValues();
		new Client().Run();*/
		//Init();
		Sede testSede = new Sede("sedea","indirizzoa");
	}
	
	//Stampa le credenziali di testing di vari impiegati
	public static void PrintTestingValues() {
		System.out.println("Credenziali per testing(nome,cognome,password):");
		System.out.println("Amministratore:"+CREDAMMINISTRATORE);
		System.out.println("Dirigente:"+CREDDIRIGENTE);
		System.out.println("Funzionario:"+CREDFUNZIONARIO+"\n");
	}
	
	public static void Init() {
		ArrayList<Sede> sedi = Sede.CaricaDaFile();
		Faker nameGenerator = new Faker();
		Sede nuovaSede;
		String companyName;
		String companyAddress;
		
		for(int i = 0; i < 10; i++) {
			companyName = nameGenerator.company().name();
			companyAddress = nameGenerator.address().streetAddress(false);
			if(!companyName.contains(",") && !Sede.NomeDuplicato(companyName)) {
				nuovaSede = new Sede(companyName, companyAddress);
				nuovaSede.SalvaSuFile();
				sedi.add(nuovaSede);
			}
		}
	}
}
