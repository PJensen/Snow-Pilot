package com.hairylogic.snowpilot;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.hairylogic.snowpilot.Terrain.TerrainStyle;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.view.MotionEvent;

public class SnowPilot extends Activity {
	
    /**
     * Called when activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paint tmpSnowFlakePaint = new Paint();
        tmpSnowFlakePaint.setStrokeCap(Cap.ROUND);
        tmpSnowFlakePaint.setStyle(Style.FILL);
        tmpSnowFlakePaint.setAntiAlias(true);
        SnowFlake.setPaint(tmpSnowFlakePaint);
        
        setContentView(mScreen = new Screen(this));
        
        mTerrain = new Terrain(50, mScreen.getWidth());
        mTerrain.generate(TerrainStyle.FLAT_TERRAIN);
        
        // Fire the main thread.
        if (!mThread.isAlive())
        	mThread.start();
    }
    
    /**
     * Occurs when the view field is touched.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	Interaction.setLast(event);
    	return false;
    	// return super.onTouchEvent(event);
    }
    
    /**
     * The main-game thread; does all game logic etc.
     */
    public static Thread mThread = new Thread(new MainThread());
    
    /**
     * The screen that *everything* is drawn on.
     */
    public static Screen mScreen;
    
    /**
     * Not a fan of creating a new Random() object every time we need one.
     */
    public static Random mRandom = new Random();
    
    public static Terrain mTerrain;
    
    /**
     * Snow flake blocking queue
     */
    public static BlockingQueue<SnowFlake> mSnow = new LinkedBlockingQueue<SnowFlake>();
}