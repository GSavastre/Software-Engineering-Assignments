package app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

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
		
		Random r = new Random();
		
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
		}
	}
}
