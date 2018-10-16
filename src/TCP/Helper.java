package TCP;

import java.text.DecimalFormat;

/**
 * Helper class to calculate times for sent packets
 *
 */
public class Helper {

	DecimalFormat df = new DecimalFormat("#.##");

	public Helper() {

	}

	// total time required for message to complete the A->B->A cycle
	private long totalCycleTime = 0;
	// time required for message to complete the A->B->A cycle

	private long currentCycleTime = 0;
	private long currentABTime = 0;
	private long currentBATime = 0;

	/**
	 * Resets the current times for sent packets
	 */
	public void reset() {
		currentCycleTime = 0;
		currentABTime = 0;
		currentBATime = 0;
	}

	/**
	 * Method is used to calculate the necessary times
	 * 
	 * @param timestampA
	 *            - time stamp when the packet is sent from pitcher
	 * @param timestampB
	 *            - time stamp when the catcher received the sent package from
	 *            pitcher
	 * @param currentA
	 *            - time when the packet got back from catcher to pitcher
	 */
	public void updateStatistics(long timestampA, long timestampB, long currentA) {

		long cycleTime = currentA - timestampA;
		totalCycleTime += cycleTime;

		currentCycleTime += cycleTime;

		currentABTime += Math.abs(timestampB - timestampA);
		currentBATime += Math.abs(currentA - timestampB);

	}

	/**
	 * Getters for specific times
	 * 
	 * @param rate
	 *            - the speed of message sending expressed as „messages per
	 *            second“
	 * @return returns double in format #.##
	 */
	public String getCurrentCycleTime(int rate) {

		return df.format((double) currentCycleTime / rate);
	}

	public String getCurrentABTime(int rate) {

		return df.format((double) currentABTime / rate);
	}

	public String getCurrentBATime(int rate) {

		return df.format((double) currentBATime / rate);
	}

	public String getTotalCycleTime() {
		return df.format((double) totalCycleTime);
	}

}