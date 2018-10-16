
package TCP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sends messages to Catcher and displays the netowrk statisics.
 */
public class Pitcher extends Thread {
	private int port;
	private int rate;
	private int messageSize;
	private String hostName;

	private Map<Long, Package> sentIds;
	private static String newLine = System.getProperty("line.separator");
	private int messageCounter = 0;
	private Helper calculate;
	private Socket socket;

	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;

	/**
	 * 
	 * @param params
	 *            - parameters send from @link PitcherParameters
	 */
	public Pitcher(PitcherParameters params) {
		this.port = params.port;
		this.rate = params.rate;
		this.messageSize = params.size;
		this.hostName = params.getHostName();
		this.sentIds = new ConcurrentHashMap<>();
		this.calculate = new Helper();
		try {
			this.socket = new Socket(InetAddress.getByName(hostName), port);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Pitcher thread start. Schedules the times when a message should be send
	 * and when the statistics of a network are to be displayed
	 */
	public void run() {

		System.out.println("*Pitcher started*");

		Timer senderTimer = new Timer();

		MessageSender sender = new MessageSender();
		senderTimer.scheduleAtFixedRate(sender, 1000, 1000 / rate);

		Timer displayTimer = new Timer();

		MessageDisplay displayer = new MessageDisplay();
		displayTimer.scheduleAtFixedRate(displayer, 1000, 1000);
		receiverTask();
	}

	private class MessageSender extends TimerTask {

		/**
		 * Binds the socket to an adress and port given by the Pitcher
		 * parameters.
		 */
		@Override
		public void run() {

			try {

				synchronized (socket) {

					long id = new Random().nextLong();

					long timeStamp = System.currentTimeMillis();

					Package p = new Package(id, timeStamp);

					sentIds.put(id, p);

					messageCounter++;
					
					outputStream.writeObject(p);
					outputStream.flush();
					outputStream.reset();
					socket.notify();

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private synchronized void process(Package received) throws IOException {

		StringBuffer sb = new StringBuffer();

		if (sentIds.containsKey(received.getId())) {
			sentIds.remove(received.getId());
		}

		if (sentIds.size() > 0) {
			for (Long value : sentIds.keySet()) {
				System.out.println(sb.append("Lost pakets").append(value.toString()));
			}
		}

		calculate.updateStatistics(received.getTimeStampPitcher(), received.getTimeStampCatcher(),

				System.currentTimeMillis());

	}

	private class MessageDisplay extends TimerTask {

		@Override
		public void run() {

			StringBuffer sb = new StringBuffer();

			synchronized (calculate) {
				sb.append("______ new packet _______").append(newLine);

				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
				String localTime = LocalTime.now().format(dtf);

				sb.append(localTime).append(newLine);
				sb.append("Number of messages sent: ").append(Integer.toString(messageCounter)).append(newLine);
				sb.append("Rate of sending: ").append(rate).append(newLine);
				sb.append("Current Cycle Time: ").append(calculate.getCurrentCycleTime(rate)).append(newLine);
				sb.append("Total Cycle Time: ").append(calculate.getTotalCycleTime()).append(newLine);
				sb.append("Time form Pitcher to Catcher: ").append(calculate.getCurrentABTime(rate)).append(newLine);
				sb.append("Time from Catcher to Pitcher").append(calculate.getCurrentBATime(rate)).append(newLine);
				System.out.println(sb.toString());

				calculate.reset();
				calculate.notify();
			}
		}
	}

	private void receiverTask() {

		while (true) {

			try {
				Package received = (Package) inputStream.readObject();

				process(received);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}