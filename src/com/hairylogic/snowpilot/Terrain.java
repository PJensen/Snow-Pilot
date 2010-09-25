package com.hairylogic.snowpilot;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;

/**
 * Terrain
 * A terrain goes from one side of the screen to the other; starting
 * at a specified y location.  Other sizing and measurable things come from
 * a passed Screen object.
 * @see Screen
 * @author dasm80x86
 *
 */
public class Terrain extends Drawable {
	
	/**
	 * Create a new terrain with the appropriate width.
	 * @param aStartingHeight - The height the terrain starts at.
	 * @param aScreen - The Screen; affects terrain layout.
	 */
	public Terrain(int aStartingHeight, Screen aScreen) {
		_startingHeight = aStartingHeight;
		_terrainWidth = aScreen.getWidth();
		_screenHeight = aScreen.getHeight();
		_surface = new int[_terrainWidth];
		_radSurface = new int[_terrainWidth];
		
		_terrainPaint = new Paint();
		_terrainPaint.setStrokeCap(Cap.ROUND);
		_terrainPaint.setColor(Color.WHITE);
		_terrainPaint.setStyle(Style.FILL);
		_terrainPaint.setAntiAlias(true);
	}
		
	/**
	 * Generate the terrain.
	 * Should really only be called once; unless of course the screen is resized.
	 */
	public void generate(TerrainStyle aTerrainStyle) {		
		isGenerated = true; int tmpRandomWalk = 0;
		
		initRadSurface(RAD_SURFACE_INIT);
		
		switch (aTerrainStyle) {
		
		case FLAT_TERRAIN:
			for (int index = 0; index < _terrainWidth; ++index) {
				_surface[index] = _startingHeight + 100;
			} return;
		case RANDOM_TERRAIN:
			for (int index = 0; index < _terrainWidth; ++index) {
				tmpRandomWalk += SnowPilot.mRandom.nextInt(2);
				tmpRandomWalk -= SnowPilot.mRandom.nextInt(2);
				_surface[index] = tmpRandomWalk + 100;
			} return;
		case JAGGED_TERRAIN:
			boolean upDown = true;
			int tmpPeakReset = SnowPilot.mRandom.nextInt(MAX_PEAK_RESET) + 1;
			for (int index = 0; index < _terrainWidth; ++index) {
				if (index % tmpPeakReset == 0) {
					tmpPeakReset = SnowPilot.mRandom.nextInt(MAX_PEAK_RESET) + 1;
					upDown = !upDown;
				} else {
					if (upDown)
						tmpRandomWalk += 1;
					else tmpRandomWalk -= 1;
				}
				_surface[index] = _startingHeight + tmpRandomWalk;
			} return;
		}
	}
	
	/**
	 * Determine if a particular snow flake impacted the surface.
	 * @param aFlake - The flake to check.
	 * @return True if the flake has gone is at or gone past the surface.
	 */
	public boolean chkImpact(SnowFlake aFlake) {
		try { return (aFlake.y > _startingHeight + _surface[aFlake.x]); }
		catch(Exception ex) { return false; } 
	}
	
	
	/**
	 * When a snow-flake impacts the surface perform the following operation.
	 * @param aFlake - The flake that definately impacted the surface
	 * Note: Does not compute if-impact; that is done elsewhere.
	 * Note: Subtracting values from surface array makes value go upward.
	 */
	public void snowImpact(SnowFlake aFlake) {
		 
		_surface[aFlake.x] -= 2;
		_radSurface[aFlake.x] = aFlake.s;
	}

	/**
	 * Initialize each element of the radial surface.
	 */
	private void initRadSurface(int aVal) {
		for(int index =0; index < _terrainWidth; ++index) {
			_radSurface[index] = aVal;
		}
	}
	
	/**
	 * Simple terrain smoothing algorithm.
	 */
	public void smoothTerrain() {
		
	}
	
	/**
	 * Has this terrain been generated?
	 * Warning: Setting this to false has a side affect in MainThread.
	 */
	public static boolean isGenerated = false;
	
	/**
	 * Draw the terrain
	 * @param canvas - The canvas on to which to draw this terrain.
	 */
	@Override
	public void draw(Canvas canvas) {
		for (int index = 0; index < _terrainWidth; ++index) {
			canvas.drawCircle(index, _startingHeight + _surface[index], _radSurface[index], _terrainPaint);
			canvas.drawLine(index, _startingHeight + _surface[index], index, _screenHeight, _terrainPaint);
		}
	}
	
	/**
	 * The common paint object for this terrain.
	 */
	private final Paint _terrainPaint;
	
		
	/**
	 * The  starting height for the terrain
	 */
	private final int _startingHeight;
	
	/**
	 * The maximum height of the screen.
	 */
	private final int _screenHeight;
	
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
	 * Array containing all radius of each snow particle on the surface.
	 * _radSurface[a] = 0 implies a circle with radius zero.
	 * _radSurfce[a] = 20 implies a circle with radius 20.
	 * Note: Snow actually hits _surface[x] + _radSurface[x]
	 */
	private int[] _radSurface = null;
	
	/**
	 * The maximum distance (across) a single peek can rise or fall. 
	 * Affects terrain generation when type is JAGGED_TERRAIN (ONLY).
	 */
	static final int MAX_PEAK_RESET = 50;
	
	/**
	 * Radii start out at this value.
	 */
	static final int RAD_SURFACE_INIT = 5;
	
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
