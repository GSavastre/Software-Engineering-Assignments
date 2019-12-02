package app;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import personale.*;
import strutture.Sede;

public class Client {
	
	private static final int PORT = 5555;
	private static final String HOST = "localhost";
	
	//Credenziali testing
	private static final String CREDFUNZIONARIO = "Marco,Rossi,123";
	private static final String CREDAMMINISTRATORE = "Luca,Bianchi,123";
	private static final String CREDDIRIGENTE = "Carlo,Verdi,123";
	
	private static final int MAX = 100;

	public void Run() {
		try {
			Socket client = new Socket(HOST, PORT);
			
			ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
			ObjectInputStream is = null;
			
			/*ArrayList<Request> registerRqs = new ArrayList<Request>() {
				{
					add(new Request("register", CREDFUNZIONARIO));
					add(new Request("register", CREDAMMINISTRATORE));
					add(new Request("register", CREDDIRIGENTE));
				}
			};
			
			
			ArrayList<Request> loginRqs = new ArrayList<Request>() {
				{
					add(new Request("login", CREDFUNZIONARIO));
					add(new Request("login", CREDAMMINISTRATORE));
					add(new Request("login", CREDDIRIGENTE));
				}
			};*/
			
			Sede testSede = new Sede("SedeA","indirizzoa");
			Sede testSeconda = new Sede("SedeB","IndirizzoB");
			testSede.SalvaSuFile();
			testSeconda.SalvaSuFile();
			
			//Funzionario funzTest = new Funzionario("Marco", "Rossi", testSede, LocalDate.now());
			
			Request rq = new Request("login","Marco,Rossi,123");
			System.out.format("Client sends: %s to server", rq.GetAzione());
			
			os.writeObject(rq);
			os.flush();
			
			if(is == null) {
				is = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
			}
			
			Object o = is.readObject();
			
			if(o instanceof Response) {
				Response rs = (Response) o;
				
				System.out.format(" and received: %s from Server%n", rs.GetEsito());
				
				if(rs.GetEsito() == false || rs.GetRisultato().size() == 0) {
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
			
			client.close();
			
		}catch(Exception e){
			e.getMessage();
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		PrintTestingValues();
		new Client().Run();
		//Impiegato logged = Auth.Login("marco", "rossi", "123");
		
		/*ArrayList<Class<?>> mansioni = new ArrayList<Class<?>>() {
			{
				add(Amministratore.class);
				add(Funzionario.class);
			}
		};
		Impiegato.Ricerca(4, mansioni);
		Sede testSede = new Sede("SedeA", "indirizzoa");
		testSede.SalvaSuFile();
		Dirigente dirTest = new Dirigente("Giulio", "Azzurri", testSede, LocalDate.now());
		dirTest.SalvaSuFile(dirTest);
		dirTest.SalvaSuFile(new Dirigente("Giuliano","Azzurri",dirTest.codiceFiscale, testSede, dirTest.inizioAttivita, dirTest.fineAttivita));*/
	}
	
	//Stampa le credenziali di testing di vari impiegati
	public static void PrintTestingValues() {
		System.out.println("Credenziali per testing(nome,cognome,password):");
		System.out.println("Amministratore:"+CREDAMMINISTRATORE);
		System.out.println("Dirigente:"+CREDDIRIGENTE);
		System.out.println("Funzionario:"+CREDFUNZIONARIO+"\n");
	}

	/*public static void main(String[] args) throws IOException{
		Sede testSede = new Sede("SedeA","indirizzoa");
		Sede testSeconda = new Sede("SedeB","IndirizzoB");
		testSede.SalvaSuFile();
		testSeconda.SalvaSuFile();
		
		Funzionario funzTest = new Funzionario("Marco", "Rossi", testSede, LocalDate.now());
		Amministratore ammTest = new Amministratore("Giulio", "Cesare", testSede, LocalDate.now().plusDays(20));
		funzTest.SalvaSuFile();
		ammTest.SalvaSuFile();
		System.out.println("Info sede");
		Sede.CaricaDaFile(testSede.nome).Print();
		System.out.println("\nInfo funzionario");
		Funzionario.CaricaDaFile(funzTest.nome, funzTest.cognome).Print();
		System.out.println("Info amministratore");
		ammTest.Print();
		
		if(Auth.Register(funzTest, "123")) {
			System.out.println("Funzionario registrato correttamente");
		}else {
			System.out.println("Funzionario non registrato");
		}
		
		
		if(Auth.Register(funzTest, "345")) {
			System.out.println("Funzionario registrato correttamente");
		}else {
			System.out.println("Funzionario non registrato");
		}
		
		if(Auth.Register(ammTest, "123")) {
			System.out.println("Amministratore registrato correttamente");
		}else {
			System.out.println("Amministratore non registrato");
		}
		
		Impiegato accesso = Auth.Login(funzTest.nome, funzTest.cognome, "123");
		
		if(accesso != null) {
			System.out.println("Accesso effettuato come : "+ accesso.nome);
		}else {
			System.out.println("Accesso fallito");
		}
	}*/

}
