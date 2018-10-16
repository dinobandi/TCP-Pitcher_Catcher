package TCP;

import java.util.Arrays;
import org.apache.commons.validator.routines.InetAddressValidator;
import com.beust.jcommander.JCommander;

public class TCPPing {
	private static final int MIN_MESSAGE_SIZE = 50;
	private static final int MAX_MESSAGE_SIZE = 3000;

	/**
	 * Start of the program. Checks is all of the arguments are in order
	 * 
	 * @param args
	 *            - arguments from the command line
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			throw new IllegalArgumentException("At least one parameter expected");
		}

		String option = args[0];
		args = Arrays.copyOfRange(args, 1, args.length);

		if (option.equals("-p")) {
			PitcherParameters params = new PitcherParameters();
			new JCommander(params, args);

			if ((params.port > 0 && params.port < 65535)
					&& (params.size >= MIN_MESSAGE_SIZE && params.size <= MAX_MESSAGE_SIZE)) {
				Pitcher pitcher = new Pitcher(params);
				pitcher.start();

			} else {
				throw new IllegalArgumentException("invalid port number or Message size");
			}
		} else if (option.equals("-c")) {
			CatcherParameters params = new CatcherParameters();
			new JCommander(params, args);

			InetAddressValidator validator = new InetAddressValidator();
			boolean isIpv4valid = validator.isValidInet4Address(params.ipAddress);

			if (isIpv4valid && (params.port > 0 && params.port < 65535)) {
				Catcher catcher1 = new Catcher(params);
				catcher1.runServer();

			} else {
				throw new IllegalArgumentException("Ip address is invalid or the port number is invalid");
			}
		} else {
			throw new IllegalArgumentException("This option isn't supported");
		}
	}
}
