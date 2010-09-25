package com.hairylogic.snowpilot;

import java.util.Iterator;

import com.hairylogic.snowpilot.Terrain.TerrainStyle;

/**
 * The thread responsible for performing all logical game operations; 
 * like detecting collisions, calling move methods for various actors.
 * Note: Nothing in this thread should call draw. 
 * @author dasm80x86
 */
public final class MainThread implements Runnable {

	private final int ONE_HUNDRED = 100;
	private final int SNOW_THRESHOLD = 10;
	private static final long THREAD_DELAY = 10L;
	
	/**
	 * Implements Runnable
	 */
	public synchronized void run() {
		
		
		// This thread should be considered always running unless
		// stopped; 
		while (true) {
			while (SnowPilot.mScreen == null)
				;
			
			// If a random number between one and one-hundres is less than
			// the snow threshold make snow this time around.
			 if (SnowPilot.mRandom.nextInt(ONE_HUNDRED) < SNOW_THRESHOLD) {
				 int tmpRandomX = SnowPilot.mRandom.nextInt(SnowPilot.mScreen.getWidth());
				 int tmpRandomS = SnowPilot.mRandom.nextInt(5);
				 SnowPilot.mSnow.add(new SnowFlake(tmpRandomX, 1, tmpRandomS));	
			}
			
			Iterator<SnowFlake> iSnow = SnowPilot.mSnow.iterator();
			while (iSnow.hasNext()) {
				SnowFlake tmpFlake = iSnow.next();
				tmpFlake.doMove();
				if (tmpFlake.y > SnowPilot.mScreen.getHeight())
					tmpFlake.invalidate();
			}

			try { Thread.sleep(THREAD_DELAY); } 
			catch (InterruptedException e) {  }
		}
	}
}