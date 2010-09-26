package com.hairylogic.snowpilot;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

public class SnowPilot extends Activity {
	
    /**
     * Called when activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load up the snowman Bitmap.
        SnowMan.setBitmap(BitmapFactory.decodeResource(getResources(), 
        		R.drawable.snow_man));
        
        // Hug a tree save the earth.
        mTreeBitmap = BitmapFactory.decodeResource(getResources(), 
        		R.drawable.tree);
        
        mMoonBitmap = BitmapFactory.decodeResource(getResources(), 
        		R.drawable.lunar);
        
        mCloudA = BitmapFactory.decodeResource(getResources(), 
        		R.drawable.cloud_a);
        
        // Generate what ends up being the only copy of paint; 
        // additionally call setPaint for *all* snow-flakes.
        Paint tmpSnowPaint = new Paint();
        tmpSnowPaint.setStrokeCap(Cap.ROUND);
        tmpSnowPaint.setColor(Color.WHITE);
        tmpSnowPaint.setStyle(Style.FILL);
        tmpSnowPaint.setAntiAlias(true);
        SnowFlake.setPaint(tmpSnowPaint);
        
        // Create the the screen; attached to content view.
        setContentView(mScreen = new Screen(this));
        
        // Set the random seed based upon the number of milliseconds
        // since UNIX epoch.
        randomizeTimer();
                        
        // Fire the main thread.
        if (!mThread.isAlive())
        	mThread.start();
    }
    
    /**
     * Occurs when the view field is touched.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
    	if (event.getRawX() > mScreen.getWidth())
    		return false;
    	else if (event.getRawX() < 0)
    		return false;
    	
    	// Make a quick determination if the touch is above the surface; we 
    	// just pass the whole event.
    	if (!SnowPilot.mTerrain.chkTouchPastSurface(event)) {
    		    		
    		// Save the current pressure
    		int tmpPressure = (int)(event.getPressure() * 30);

    		// Based upon pressure, generate another flake.
    		tmpPressure += mRandom.nextInt(tmpPressure);
    		tmpPressure -= mRandom.nextInt(tmpPressure);
    		mSnow.add(new SnowFlake((int)event.getRawX(), 
    				(int)event.getRawY(), tmpPressure));

    		// Based upon pressure, generate another flake.
    		tmpPressure += mRandom.nextInt(tmpPressure);
    		tmpPressure -= mRandom.nextInt(tmpPressure);
    		mSnow.add(new SnowFlake((int)event.getRawX(), 
    				(int)event.getRawY(), tmpPressure));

    		// Based upon pressure, generate another flake.
    		tmpPressure += mRandom.nextInt(tmpPressure);
    		tmpPressure -= mRandom.nextInt(tmpPressure);
    		mSnow.add(new SnowFlake((int)event.getRawX(), 
    				(int)event.getRawY(), tmpPressure));
    		
    	} else { SnowPilot.mTerrain.dropSurface(event); }
    	
    	// The motion even call chain stops here.
    	return false;  // super.onTouchEvent(event); 
    }
    
    /**
     * When the back button is pressed a variety of things need to happen.
     * In my case I use the back button to exit applications; this method
     * allows the subsequent regeneration of the terrain by setting it's 
     * static boolean to false.
     */
    @Override
    public void onBackPressed() {
    	// 
    	Terrain.isGenerated = false;
    	super.onBackPressed();
    }
    
    @Override
    public boolean onSearchRequested() {
    	Terrain.isGenerated = false;
    	return false;
    	// return super.onSearchRequested();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	
    	case R.id.item01:
    		Terrain.isGenerated = false;
    		return true;
    	case R.id.item02:
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    
    
    /**
     * Return the slope between two points.
     * @param a - The first point
     * @param b - The second point
     * @return - The slope between two points as a float.
     */
    public static float getSlope(Point a, Point b) {
    	return (b.y - a.y) / (b.x - a.x); 
    }
    
    public static void randomizeTimer() {
    	
    	// Grab a time object, set it to the current time.
		Time tmpTime = new Time();
		tmpTime.setToNow();
		
        // Set the random seed based upon the number of milliseconds
        // since UNIX epoch.
        SnowPilot.mRandom.setSeed(tmpTime.toMillis(false));
    }
    
    /**
     * Set to true if the player is about to throw something.
     */
    // public static boolean mThrowing = false;
    
    // public static SnowMan mSnowMan; 
    
    /**
     * The terrain onto which the snow falls and accumulates.
     */
    public static Terrain mTerrain;
    
    public static Bitmap mTreeBitmap;
    
    public static Bitmap mMoonBitmap;
    
    public static Bitmap mCloudA;
    
    /**
     * The screen that *everything* is drawn on.
     */
    public static Screen mScreen;
    
    // public static float throwSlope = 0.0f;
    
    /**
     * The main-game thread; does all game logic etc.
     */
    public static Thread mThread = new Thread(new MainThread());
    
    /**
     * Not a fan of creating a new Random() object every time we need one.
     */
    public static Random mRandom = new Random();
    
    /**
     * Snow flake blocking queue
     */
    public static BlockingQueue<SnowFlake> mSnow = new LinkedBlockingQueue<SnowFlake>();
    
    /**
     * Determines if debugging is enabled or not.
     */
    public static boolean IsDebug = true;
    
}