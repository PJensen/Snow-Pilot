package com.hairylogic.snowpilot;

/**
 * The thread responsible for performing all logical game operations; 
 * like detecting collisions, calling move methods for various actors.
 * Note: Nothing in this thread should call draw. 
 * @author dasm80x86
 */
public final class MainThread implements Runnable {

	/**
	 * Implements Runnable
	 */
	public synchronized void run() {
		// This thread should be considered always running unless
		// stopped; 
		while (true) {
			// If a random number between one and one-hundres is less than
			// the snow threshold make snow this time around.
			if (SnowPilot.mRandom.nextInt(ONE_HUNDRED) < SNOW_THRESHOLD) {
				SnowPilot.mSnow.add(new SnowFlake(Interaction.x, Interaction.y, Interaction.s));
			}
		}
	}
	
	
	private final int ONE_HUNDRED = 100;
	private final int SNOW_THRESHOLD = 5;
}