package app;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

import personale.*;
import strutture.Sede;

public class Client {
	
	private static final int PORT = 5555;
	private static final String HOST = "localhost";
	
	private static final int MAX = 100;

	public void Run() {
		try {
			Socket client = new Socket(HOST, PORT);
			
			ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
			ObjectInputStream is = null;
			
			Random r = new Random();
			
			while(true) {
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
			}
			
			client.close();
			
		}catch(Exception e){
			e.getMessage();
			e.printStackTrace();
		}
	}
	
	/*public static void main(String[] args) {
		new Client().Run();
	}*/
	

	public static void main(String[] args) throws IOException{
		Sede testSede = new Sede("SedeA","indirizzoa");
		testSede.SalvaSuFile();
		
		Funzionario funzTest = new Funzionario("nome", "cognome", testSede, LocalDate.now());
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
	}

}
