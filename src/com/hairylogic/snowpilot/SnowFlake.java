package com.hairylogic.snowpilot;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

/**
 * A snow-flake. 
 * @author dasm80x86

 */
public class SnowFlake extends Drawable {
	
	/**
	 * Create a new snow flake
	 * @param aX - The x-coordinate
	 * @param aY - The y-coordinate.
	 * @param aS - The size of the snow flake.
	 */
	public SnowFlake(int aX, int aY, int aS) {
		this.x = aX;	// Set X
		this.y = aY;	// Set Y
		this.s = aS;	// Set S
	}
	
	public SnowFlake(Point a, Point b, int aS) {
		this.x = a.x;
		this.y = a.y;
	}
	
	/**
	 * Move the snow flake to T+1 location.
	 */
	public void doMove() {

		x += SnowPilot.mRandom.nextInt(2);
		x -= SnowPilot.mRandom.nextInt(2);
		y += SnowPilot.mRandom.nextInt(2);
		y+=(s/3);
	}
	
	/**
	 * Draw this SnowFlake.
	 */
	@Override
	public void draw(Canvas canvas) {
		if (invalidated)
			return;
		mPaint.setStrokeWidth(s);
		canvas.drawPoint(x, y, mPaint);
	}

	/**
	 * Get the opacity for this draw-able shape.
	 */
	@Override
	public int getOpacity() {
		return 0;
	}

	/**
	 * Set the transparency color.
	 */
	@Override
	public void setAlpha(int alpha) {
	}

	/**
	 * Set color filter.
	 */
	@Override
	public void setColorFilter(ColorFilter cf) {
	}
	
	/**
	 * The x-coordinate for this snow flake
	 */
	protected int x;
	
	/**
	 * The y-coordinate for this snow flake.
	 */
	protected int y;
	
	/**
	 * The relative size of this snow flake
	 */
	protected int s;
	
	public void invalidate() {
		invalidated = true;
	}
	
	/**
	 * Settings invalidated to true flags this SnowFlake for
	 * thread safe removal.
	 */
	protected boolean invalidated = false;
	
	/**
	 * Set the paint style for *all* snow-flakes in a static manner. Most values
	 * remain the same while few change between snow flakes.  Color and Size doesn't 
	 * really warrant a separate Paint object for each.
	 * @param aPaint - The paint style for most of the snow-flakes.
	 * @return void
	 */
	public static void setPaint(Paint aPaint) {
		// Set the bulk of the paint style.
		mPaint = aPaint;
	}
	
	// Fields
	private static Paint mPaint = null;
}
