package minecraftmini;

import org.lwjgl.*;

public class MCTimer {

	protected long time;

	public MCTimer() {
		time = getTime();
	}

	public int getDelta() {
		return (int) (getTime() - time);
	}

	public void update() {
		time = getTime();
	}

	public long curTime() {
		return time;
	}

	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

}
