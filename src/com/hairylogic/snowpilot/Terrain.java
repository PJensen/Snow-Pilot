package com.hairylogic.snowpilot;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class Terrain extends Drawable {
	
	/**
	 * Create a new terrain with the appropriate width.
	 * @param aWidth - The width the terrain should go to.
	 * @param aStartingHeight - The height the terrain starts at.
	 */
	public Terrain(int aStartingHeight, int aWidth) {
		_startingHeight = aStartingHeight;
		_terrainWidth = aWidth;
		_surface = new int[aWidth];
	}
	
	/**
	 * Generate the terrain.
	 * Should really only be called once; unless of course the screen is resized.
	 */
	public void generate(TerrainStyle aTerrainStyle) {
		int tmpRandomWalk = 0;
		switch (aTerrainStyle) {
		
		case FLAT_TERRAIN:
			for (int index = 0; index < _terrainWidth; ++index) {
				_surface[index] = _startingHeight;
			} return;
		case RANDOM_TERRAIN:
			for (int index = 0; index < _terrainWidth; ++index) {
				tmpRandomWalk += SnowPilot.mRandom.nextInt(1);
				tmpRandomWalk -= SnowPilot.mRandom.nextInt(1);
				_surface[index] = tmpRandomWalk;
			} return;
		case JAGGED_TERRAIN:
			boolean upDown = true; 		// true is up, false is down.
			int tmpPeakReset = SnowPilot.mRandom.nextInt(MAX_PEAK_RESET);
			for (int index = 0; index < _terrainWidth; ++index) {
				if (index % tmpPeakReset == 0) {
					tmpPeakReset = 0;	// Start a new peak or valley.
					upDown = !upDown;
				}
				else {
					if (upDown)
						tmpRandomWalk += 1;
					else
						tmpRandomWalk -= 1;
					_surface[index] = tmpRandomWalk;
				}
			} return;
		}
	}
	
	/**
	 * Simple terrain smoothing algorithm.
	 */
	public void smoothTerrain() {
		
	}
	
	/**
	 * Draw the terrain
	 */
	@Override
	public void draw(Canvas canvas) {
		Paint tmpPaint = new Paint();
		tmpPaint.setColor(Color.WHITE);
		for (int index = 0; index < _terrainWidth; ++index)
			canvas.drawPoint(index, 40, tmpPaint);		
	}
	
	/**
	 * The  starting height for the terrain
	 */
	private final int _startingHeight;
	
	/**
	 * The width of the terrain
	 */
	private final int _terrainWidth;
	
	/**
	 * The terrain surface.
	 * Note: Undefined until constructor.
	 */
	private int[] _surface = null;
	
	/**
	 * The maximum distance (across) a single peek can rise or fall. 
	 * Affects terrain generation when type is JAGGED_TERRAIN (ONLY).
	 */
	static final int MAX_PEAK_RESET = 50;
	
	/**
	 * Enumerated type that defines a particular terrain style.
	 * @author dasm80x86
	 */
	public enum TerrainStyle { RANDOM_TERRAIN, FLAT_TERRAIN, JAGGED_TERRAIN }

	@Override
	public int getOpacity() {
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
	};
}
