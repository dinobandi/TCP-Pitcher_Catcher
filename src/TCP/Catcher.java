package TCP;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Catcher {

	private ServerSocket ss;
	private Socket s;

	Catcher(CatcherParameters catcherParameters) {
		try {
			ss = new ServerSocket(catcherParameters.port, 50, InetAddress.getByName(catcherParameters.ipAddress));
			System.out.println("Catcher started");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
/**
 * Accepts the incoming connections and starts a thread that will service the request from the pitcher
 */
	public void runServer() {
		while (true)
		 {
			try {
				s=ss.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			new CatcherConnection(s).start();
		 }
		
	}
}
