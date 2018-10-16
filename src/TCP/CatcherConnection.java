package TCP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CatcherConnection extends Thread {

	Socket socket;
	ObjectInputStream input;
	ObjectOutputStream output;
	Package packet;

	public CatcherConnection(Socket s) {
		this.socket = s;
	}

	/**
	 * Catcher thread start. Receives the send packets through sockets, writes
	 * the time that it has received it and sends it back to the original
	 * sender.
	 */
	@Override
	public void run() {
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());

			synchronized (socket) {
				while ((packet = (Package) input.readObject()) != null) {

					packet.setTimeStampCatcher(System.currentTimeMillis());
					output.writeObject(packet);
					output.flush();
					output.reset();
					socket.notify();
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
