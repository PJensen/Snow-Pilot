package com.hairylogic.snowpilot;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class SnowMan extends Drawable {
	
	public static boolean snowManExists = false;
	
	public SnowMan() {
		x = 1;
		y = SnowPilot.mTerrain.getHeightAt(x);
		snowManExists = true;
	}
	
	public static void setBitmap(Bitmap aBitmap) {
		mBitmap = aBitmap;
	}
	
	static Bitmap mBitmap;
	protected int x;
	protected int y;
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, x, y - mBitmap.getHeight(), new Paint());
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
		
	}
}
