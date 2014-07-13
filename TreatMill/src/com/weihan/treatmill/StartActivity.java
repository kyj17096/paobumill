package com.weihan.treatmill;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;


public class StartActivity extends Activity {

	CountDownTimer cdt;
	private final String TAG = MyFrameLayout.class.getSimpleName();
	
	public static int TOOL_BAR_HIGH = 0;
	public static WindowManager.LayoutParams params = new WindowManager.LayoutParams(); 
	private float startX;
	private float startY;
	private float x;
	private float y;
	private TextView tv1;
	private TextView speed;
	private SoundPool mSoundPool;  
	boolean startModeSelect = false;;
    public static Handler mHandler;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.main);
        //speed = (TextView)findViewById(R.id.float_speed_value);
        //speed.setText("1000");
        mSoundPool=new SoundPool(2,AudioManager.STREAM_MUSIC,0); 
      
        mHandler = new Handler(){
        	 
            @Override
            public void handleMessage(Message msg) {
            	switch(msg.what)
            	{
            	case 1:
            		 
                    Log.v("play sound","play sound");
                    break;
            	case 2:
            		//StartActivity.this.finish();
            		break;
                    
            	}
            }
        }; 
        cdt = new CountDownTimer(3000, 1000) {
        
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
            	Intent intent2 = new Intent(StartActivity.this, ModeSelect.class);
        		startActivity(intent2);
        		StartActivity.this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
        		StartActivity.this.finish();
            }
         }.start();
 

 
		//Intent intent1 = new Intent(this, BlueToothMsgService.class);
		//startService(intent1);
		
//		File file = new File(getExternalFilesDir(null), "DemoFile.jpg");
//        try {
//            // Very simple code to copy a picture from the application's
//            // resource into the external file.  Note that this code does
//            // no error checking, and assumes the picture is small (does not
//            // try to copy it in chunks).  Note that if external storage is
//            // not currently mounted this will silently fail.
//            InputStream is = getResources().openRawResource(R.drawable.volume_add);
//            OutputStream os = new FileOutputStream(file);
//            byte[] data = new byte[is.available()];
//            is.read(data);
//            os.write(data);
//            is.close();
//            os.close();
//        } catch (IOException e) {
//            // Unable to create file, likely because external storage is
//            // not currently mounted.
//            Log.w("ExternalStorage", "Error writing " + file, e);
//        }
//		
//		Intent intent2 = new Intent(this, ModeSelect.class);
//		startActivity(intent2);
//		this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
        
   

    }

	protected void onResume() 
	{

		super.onResume();
		
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
	}

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        		WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        //wakeLock.release();
    }

}