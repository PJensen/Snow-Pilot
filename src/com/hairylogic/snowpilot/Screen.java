package com.hairylogic.snowpilot;

import java.util.Iterator;

import com.hairylogic.snowpilot.Terrain.TerrainStyle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Screen extends View {

	public Screen(Context context) {
		super(context);
	}

	@Override
	public void draw(Canvas canvas) {
		
		Iterator<SnowFlake> iSnow = SnowPilot.mSnow.iterator();
		while (iSnow.hasNext()) {
			iSnow.next().draw(canvas);
		}
		
		try { SnowPilot.mTerrain.draw(canvas); }
		catch (Exception ex) { }
		
		if (SnowPilot.IsDebug) {
			Paint tmpPaint = new Paint();
			tmpPaint.setColor(Color.WHITE);
			tmpPaint.setTextSize(20);
			canvas.drawText("Snow Count: " + Integer.toString(SnowPilot.mSnow.size()), 
					10, 50, tmpPaint);
			canvas.drawText("Throw:" + Float.toString(SnowPilot.throwSlope), 10, 70, tmpPaint);
		}
		
		
		invalidate();
		
		super.draw(canvas);
	}
}
