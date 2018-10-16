package TCP;

import java.io.Serializable;

public class Package implements Serializable {
private long id,timeStampPitcher,timeStampCatcher;
	
	Package(long id, long timeStampPitcher) {
		setId(id);
		setTimeStampPitcher(timeStampPitcher);
		
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getTimeStampPitcher() {
		return timeStampPitcher;
	}
	public void setTimeStampPitcher(long timeStampPitcher) {
		this.timeStampPitcher = timeStampPitcher;
	}
	public long getTimeStampCatcher() {
		return timeStampCatcher;
	}
	public void setTimeStampCatcher(long timeStampCatcher) {
		this.timeStampCatcher = timeStampCatcher;
	}

}
