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
			
			if (!Terrain.isGenerated) {
				if (SnowPilot.mScreen.getWidth() > 0) {
					SnowPilot.mTerrain = new Terrain((SnowPilot.mScreen.getHeight()/4), 
							SnowPilot.mScreen);
					TerrainStyle tmpNewStyle = null;
					switch(SnowPilot.mRandom.nextInt(3) + 1) {
					case 1:
						tmpNewStyle = TerrainStyle.FLAT_TERRAIN;
						break;
					case 2:
						tmpNewStyle = TerrainStyle.JAGGED_TERRAIN;
						break;
					case 3:
						tmpNewStyle = TerrainStyle.RANDOM_TERRAIN;
						break;
					case 4:
						tmpNewStyle = TerrainStyle.FLAT_TERRAIN;
						break;
					}
					SnowPilot.mTerrain.generate(tmpNewStyle);
					Terrain.isGenerated = true;
				}
			}
			
			/*
			if (mTerrtain == null)
				mTerrtain = new Terrain(400, SnowPilot.mScreen.getWidth(), SnowPilot.mScreen.getHeight());
			else if (!mTerrtain.isGenerated) {
				mTerrtain = new Terrain(SnowPilot.mScreen.getWidth() + SnowPilot.mScreen.getWidth()/4, SnowPilot.mScreen.getWidth(), SnowPilot.mScreen.getHeight());
				mTerrtain.generate(TerrainStyle.JAGGED_TERRAIN);
			}*/
			
			// If a random number between one and one-hundred is less than
			// the snow threshold make snow this time around.
			 if (SnowPilot.mRandom.nextInt(ONE_HUNDRED) < SNOW_THRESHOLD) {
				 int tmpRandomX = SnowPilot.mRandom.nextInt(SnowPilot.mScreen.getWidth());
				 int tmpRandomS = SnowPilot.mRandom.nextInt(10);
				 SnowPilot.mSnow.add(new SnowFlake(tmpRandomX, 1, tmpRandomS));	
			}
			
			Iterator<SnowFlake> iSnow = SnowPilot.mSnow.iterator();
			while (iSnow.hasNext()) {
				SnowFlake tmpFlake = iSnow.next();
				tmpFlake.doMove();
				
				if (SnowPilot.mTerrain.chkImpact(tmpFlake)) {
					SnowPilot.mTerrain.snowImpact(tmpFlake);
				// if (tmpFlake.y > SnowPilot.mScreen.getHeight())
					tmpFlake.invalidate();
				}
			}
			
			iSnow = SnowPilot.mSnow.iterator();
			while (iSnow.hasNext())	{
				SnowFlake tmpFlake = iSnow.next();
				if (tmpFlake.invalidated)
					SnowPilot.mSnow.remove(tmpFlake);
			}

			try { Thread.sleep(THREAD_DELAY); } 
			catch (InterruptedException e) {  }
		}
	}
}