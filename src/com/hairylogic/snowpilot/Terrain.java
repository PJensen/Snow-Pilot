package com.hairylogic.snowpilot;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.text.format.Time;
import android.view.MotionEvent;

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
		SnowPilot.randomizeTimer();		
		switch (aTerrainStyle) {
		
		case FLAT_TERRAIN:
			for (int index = 0; index < _terrainWidth; ++index) {
				_surface[index] = _startingHeight;
			} return;
		case RANDOM_TERRAIN:
			for (int index = 0; index < _terrainWidth; ++index) {
				tmpRandomWalk += SnowPilot.mRandom.nextInt(2);
				tmpRandomWalk -= SnowPilot.mRandom.nextInt(2);
				_surface[index] = tmpRandomWalk + _startingHeight;
			} return;
		case JAGGED_TERRAIN:
			boolean upDown = true;
			int tmpPeakReset = SnowPilot.mRandom.nextInt(MAX_PEAK_RESET) + 1;
			for (int index = 0; index < _terrainWidth; ++index) {
				if (index % tmpPeakReset == 0) {
					tmpPeakReset = SnowPilot.mRandom.nextInt(MAX_PEAK_RESET) + 1;
					upDown = !upDown;
				} else {
					if (upDown) tmpRandomWalk += 1;
					else tmpRandomWalk -= 1;
				}
				_surface[index] = tmpRandomWalk + _startingHeight;
			} return;
		}
	}
	
	/**
	 * Given an 2-tuple, (x, y) a coordinate determine if it's 
	 * beyond the extent of the top of the surface.
	 * @param aX - The x-coordinate.
	 * @param aY - The y-coordinate.
	 * @return True is the coordinate is beyond the surface.
	 * <b>Precondition</b>: aX does not exceed getWidth()
	 */
	/* public boolean chkTouchPastSurface(MotionEvent event) {
		return (event.getRawY() > _surface[(int)event.getRawX()]); 
	}*/
	
	public boolean chkTouchPastSurface(MotionEvent event) {
		return ((int)event.getRawY() > 
			_surface[(int)event.getRawX()]);
	}
	
	/**
	 * Determine if a particular snow flake impacted the surface.
	 * @param aFlake - The flake to check.
	 * @return True if the flake has gone is at or gone past the surface.
	 */
	public boolean chkImpact(SnowFlake aFlake) {
		if (aFlake.x > _terrainWidth) return false;
		try { return (aFlake.y > _surface[aFlake.x]); }
		catch(Exception ex) { return false; } 
	}
	
	/**
	 * 
	 * @param event
	 */
	public void dropSurface(MotionEvent event) {
		if (event.getRawX() < _terrainWidth  && event.getRawX() > 0)
		if (_surface[(int)event.getRawX()] + 10 < _screenHeight) {
			_surface[(int)event.getRawX()] += 10; 
		}
	}
	
	
	/**
	 * When a snow-flake impacts the surface perform the following operation.
	 * @param aFlake - The flake that definately impacted the surface
	 * Note: Does not compute if-impact; that is done elsewhere.
	 * Note: Subtracting values from surface array makes value go upward.
	 */
	public void snowImpact(SnowFlake aFlake) {
		 
		_surface[aFlake.x] -= aFlake.s;
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
	 * The height at a particular x-coordinate
	 * @param x - The x-coordinate to check up on.
	 * @return - The height there.
	 */
	public int getHeightAt(int x) {
		return _surface[x];
	}
	
	/**
	 * Simple terrain smoothing algorithm.
	 */
	public void smoothTerrain() {
		
		int dX;
		for (int index = 1; index < _terrainWidth; ++index) {	
			dX = _surface[index - 1] - _surface[index];
			if (Math.abs(dX) > 1) {
				int tmpLR = SnowPilot.mRandom.nextInt(100);
				if (dX < 0)
					_surface[index - 1] += 1;
				else 
					_surface[index - 1] -= 1;
			}
		}
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
			
			
			// _terrainPaint.setColor(Color.WHITE);
			// canvas.drawCircle(index, _startingHeight + _surface[index], _radSurface[index], _terrainPaint);
						
			// _terrainPaint.setStrokeCap(Cap.ROUND);
			// _terrainPaint.setStrokeWidth(_radSurface[index]);
			canvas.drawLine(index, _surface[index], index, _screenHeight, _terrainPaint);
			
			canvas.drawBitmap(SnowPilot.mTreeBitmap, (_terrainWidth  / 3) - (SnowPilot.mTreeBitmap.getWidth()/2), 
					(_surface[_terrainWidth  / 3] - SnowPilot.mTreeBitmap.getHeight()) + 10,_terrainPaint);  
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
