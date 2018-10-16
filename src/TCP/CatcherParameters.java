package TCP;

import com.beust.jcommander.Parameter;

/**
 * Catcher parameters to be read in command line by Jcommander
 */
public class CatcherParameters {

	CatcherParameters() {
	}

	@Parameter(names = "-port", description = "TCP socket port")
	public Integer port;

	@Parameter(names = "-bind", description = "TCP socket bind address")
	public String ipAddress;
}
