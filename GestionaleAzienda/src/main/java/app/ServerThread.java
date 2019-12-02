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
				Response rs = new Response();
				Impiegato richiedente = rq.GetRichiedente();
				String azione = rq.GetAzione();
				
				//Non conviene usare un array di string
				String[] parametri = rq.GetParametri().split(",");
				System.out.format("Thread %s receives %s request from client%n",id, azione);
				
				ArrayList<Impiegato> ris = new ArrayList<Impiegato>();
				
				switch(azione) {
					case "login": {
						Impiegato accesso = Auth.Login(parametri);
						
						if(accesso != null) {
							ris.add(accesso);
							rs.SetEsito(true);
							rs.SetRisultato(ris);
						}else {
							rs.SetEsito(false);
						}
					}break;
					
					//uso i parametri (string) del messaggio per generare un oggetto impiegato invece di dover usare un oggetto da se nel messaggio che verrà usato solo per la registrazione
					case "register":{
						String password = parametri[parametri.length -1];
						if(Auth.Register(new Impiegato(parametri), password)) {
							rs.SetEsito(true);
						}
					}break;
						
					case "search":{
						int numRisultati;
						try {
							numRisultati = Integer.parseInt(parametri[0]);
						}catch(Exception e) {
							numRisultati = 10;
						}
						
						ArrayList<Class<?>> mansioni = new ArrayList<Class<?>>();
						for(int i = 1; i < parametri.length; i++) {
							Class<?> mansione = Class.forName(parametri[i]);
							try {
								//Se il richiedente è un dirigente
								if(richiedente instanceof Dirigente) {
									//Non aggiungere gli amministratori alla ricerca
									if(!mansione.isInstance(Amministratore.class)) {
										mansioni.add(mansione);
									}
								}else {
									mansioni.add(mansione);
								}
							}catch(Exception e) {
								e.getMessage();
								System.out.println("Errore nell riconoscimento della classe "+parametri[i]);
							}
						}
						
						//Nel caso non siano riconosciute le mansioni cerca solo gli operai
						if(mansioni.size() == 0) {
							mansioni.add(Operaio.class);
						}
						
						ris = Impiegato.Ricerca(numRisultati, mansioni);
						
						if(ris.size() > 0) {
							rs.SetEsito(true);
							rs.SetRisultato(ris);
						}else{
							rs.SetEsito(false);
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
						
						ris.add(impiegato);
						rs.SetEsito(true);
						rs.SetRisultato(ris);
					}break;
						
					default: System.out.format("Request %s is not recognized by thread %s%n", azione, id);
						break;
				}
				
				
				if(os == null) {
					os = new ObjectOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
				}
				
				System.out.format("Thread %s sends: %s result to its client%n", id, rs.GetEsito());
				os.writeObject(rs);
				os.flush();
				
				if(this.server.GetPool().getActiveCount() == 1) {
					this.server.Close();
				}
				
				this.socket.close();
				return;
				
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
