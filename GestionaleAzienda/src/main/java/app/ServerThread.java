package app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import personale.Amministratore;
import personale.Dirigente;
import personale.Funzionario;
import personale.Impiegato;
import personale.Operaio;

public class ServerThread implements Runnable{
	private static final int MAX = 100;
	private static final long SLEEPTIME = 200;
	
	private Server server;
	private Socket socket;
	
	public ServerThread(final Server server, final Socket socket) {
		this.server = server;
		this.socket = socket;
	}
	
	public void run() {
		ObjectInputStream is = null;
		ObjectOutputStream os = null;
		
		try {
			is = new ObjectInputStream(new BufferedInputStream(this.socket.getInputStream()));
		}catch(Exception e) {
			e.getMessage();
			e.printStackTrace();
			
			return;
		}
		
		String id = String.valueOf(this.hashCode());
		
		try {
			Object message = is.readObject();
			
			if(message instanceof Request) {
				Request rq = (Request) message;
				String azione = rq.GetAzione();
				//Non conviene usare un array di string
				String[] parametri = rq.GetParametri().split(",");
				System.out.format("Thread %s receives %s request from client%n",id, azione);
				
				switch(azione) {
					case "login": {
						Auth.Login(parametri);
					}break;
					
					//uso i parametri (string) del messaggio per generare un oggetto impiegato invece di dover usare un oggetto da se nel messaggio che verrà usato solo per la registrazione
					case "register":{
						String password = parametri[parametri.length -1];
						Auth.Register(new Impiegato(parametri), password);
					}break;
						
					case "search":{
						int numRisultati = Integer.parseInt(parametri[0]);
						
						
						//Se passo solo due parametri ovvero numero di risultati da mostrare e solo un tipo di classe da mostrare
						if(parametri.length < 3) {
							Class<?> mansione;
							try {
								mansione = Class.forName(parametri[2]);
							}catch(Exception e){
								System.out.println("Impossibile riconoscere mansione richiesta :"+parametri[2]);
								mansione = Operaio.class;
							}
							
							Impiegato.Ricerca(numRisultati, mansione);
						}else {
							ArrayList<Class<?>> mansioni = new ArrayList<Class<?>>();
							for(int i = 1; i < parametri.length; i++) {
								try {
									mansioni.add(Class.forName(parametri[i]));
								}catch(Exception e) {
									e.getMessage();
									System.out.println("Errore nell riconoscimento della classe "+parametri[i]);
								}
							}
							
							if(mansioni.size() == 0) {
								mansioni.add(Operaio.class);
							}
							
							Impiegato.Ricerca(2, mansioni);
						}
					}break;
						
					case "update":{
						//Codice fiscale dell'impiegato da modificare
						String cf = parametri[0];
						String[] paramNuovi = Arrays.copyOfRange(parametri, 1, parametri.length);
						
						Impiegato impiegato = Impiegato.CaricaDaFile(cf);
						if(impiegato != null) {
							switch(impiegato.getClass().getSimpleName()) {
							case "Operaio": impiegato.SalvaSuFile(new Operaio(paramNuovi));
								break;
							case "Dirigente": impiegato.SalvaSuFile(new Dirigente(paramNuovi));
								break;
							case "Funzionario": impiegato.SalvaSuFile(new Funzionario(paramNuovi));
								break;
							case "Amministratore": impiegato.SalvaSuFile(new Amministratore(paramNuovi));
								break;
							}
						}
					}break;
						
					default: System.out.format("Request %s is not recognized by thread %s%n", azione, id);
						break;
				}
				
				
				if(os == null) {
					os = new ObjectOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			e.getMessage();
			e.printStackTrace();
			
			System.exit(0);
		}
		
		
		/*Random r = new Random();
		
		while(true) {
			try {
				Object i = is.readObject();
				
				if(i instanceof Request) {
					Request rq = (Request) i;
					
					System.out.format("Thread %s receives: %s form its client%n", id, rq.GetValue());
					
					Thread.sleep(SLEEPTIME);
					
					if(os == null) {
						os = new ObjectOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
					}
					
					Response rs = new Response(r.nextInt(MAX));
					
					System.out.format("Thread %s sends: %s to its client%n", id, rs.GetValue());
					
					os.writeObject(rs);
					os.flush();
					
					if(rs.GetValue() == 0) {
						if(this.server.GetPool().getActiveCount() == 1) {
							this.server.Close();
						}
						
						this.socket.close();
						return;
					}
				}
			}catch(Exception e) {
				e.getMessage();
				e.printStackTrace();
				
				System.exit(0);
			}
		}*/
	}
}
