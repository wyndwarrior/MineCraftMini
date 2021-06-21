package minecraftmini;

public class MCFPSTimer extends MCTimer {

	private int fps;
	private int lastFps;

	public int getFPS() {
		return lastFps;
	}

	public void frame() {
		if (getTime() - curTime() > 1000) {
			lastFps = fps;
			fps = 0;
			time += 1000;
		}
		fps++;
	}

}
