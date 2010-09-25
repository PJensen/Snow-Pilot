package com.hairylogic.snowpilot;

import java.util.Iterator;

import android.content.Context;
import android.graphics.Canvas;
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
		
		super.draw(canvas);
	}

}
