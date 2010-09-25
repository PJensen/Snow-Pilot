package com.hairylogic.snowpilot;

import android.view.MotionEvent;

/**
 * A humans interaction
 * @author dasm80x86
 */
public class Interaction {
	
	/**
	 * Given a motion event set the last known values for
	 * pressure, x and y.
	 * @param aEvent - The event to derive pressure, x and y from.
	 */
	public static void setLast(MotionEvent aEvent) {
		x = (int) aEvent.getRawX();
		y = (int) aEvent.getRawY();
		s = (int) aEvent.getPressure() * PRESSURE_SCALAR;
	}
	
	/**
	 * Last known x coordinate.
	 */
	public static int x;
	
	/**
	 * Last known y coordinate.
	 */
	public static int y;
	
	/**
	 * Last known pressure value.
	 */
	public static int s;
	
	/**
	 * Scalar value multiplied to pressure.
	 */
	public static final int PRESSURE_SCALAR = 4;
}
