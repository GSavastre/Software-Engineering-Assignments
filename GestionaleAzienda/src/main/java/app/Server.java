//Savastre Cosmin Gabriele 283110
package app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
	
	private static final int PORT = 5555;
	
	private static final int CORETHREADS = 5;
	private static final int MAXTHREADS = 50;
	private static final int IDLETIME = 5000;
	
	private ServerSocket socket;
	private ThreadPoolExecutor pool;

	public Server() throws IOException{
		this.socket = new ServerSocket(PORT);
	}
	
	private void Run() {
		this.pool = new ThreadPoolExecutor(CORETHREADS, MAXTHREADS, IDLETIME,
					TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		
		while(true) {
			try {
				Socket s = this.socket.accept();
				
				this.pool.execute(new ServerThread(this, s));
			}catch(Exception e) {
				break;
			}
		}
		
		this.pool.shutdown();
	}
	public ThreadPoolExecutor GetPool() {
		return this.pool;
	}
	
	public void Close() {
		try {
			this.socket.close();
		}catch(Exception e) {
			e.getMessage();
		}
	}
	
	public static void main(String[] args) throws IOException{
		new Server().Run();
	}

}
