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
		
		try { MainThread.mTerrtain.draw(canvas); } 
		catch(Exception ex) { }

		
		invalidate();
		
		super.draw(canvas);
	}
}
