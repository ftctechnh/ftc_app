package detectors;

public abstract class StartStoppable {
	volatile boolean activated = false;

	Thread loop = new Thread(()->{
		while (activated){
			loop();
		}
	});

	public final void start() {
		activated = true;
		loop.start();
	}

	public final void stop() {
		//yes stop() is deprecated but there is no big penalty for premature death
		//and suspend is useless as well because each cycle is independent
		activated = false;
		loop.stop();
	}

	public abstract void loop();
	public abstract void begin();
	public abstract void end();

}
