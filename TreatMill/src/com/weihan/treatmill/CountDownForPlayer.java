package com.weihan.treatmill;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import com.weihan.treatmill.ModeSelect.MyTimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

public class CountDownForPlayer extends Activity {

	CountDownTimer cdt;
	ImageView iv;
	boolean firstSendRing = true;
    int countTime;
    Timer mTimer ;
    MyTimerTask mTimerTask;
    public static  Handler mHandler;
    public static final int FINISH_THIS_ACTIVITY_WHEN_SECURITY = 1;
    public static final int FINISH_THIS_ACTIVITY_WHEN_ERROR = 2;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.countdown_for_player);
        iv = (ImageView)findViewById(R.id.countdown_image);
        iv.setBackgroundResource(R.drawable.countdown_for_player_5);
        //countTime = getIntent().getExtras().getInt("count_time");
        countTime = 7;
        //ModeSelect.mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 2, -1).sendToTarget();
        mHandler = new Handler(){
        	@Override
        	public void handleMessage(Message msg) {
        		// TODO Auto-generated method stub
        		switch(msg.what)
        		{
        		case  FINISH_THIS_ACTIVITY_WHEN_SECURITY:
        	        stopTimer();
        	        CountDownForPlayer.this.finish();
        	        break;
        		case FINISH_THIS_ACTIVITY_WHEN_ERROR:
        			stopTimer();
        	        CountDownForPlayer.this.finish();
//        			stopTimer();
//        			if(RunningNow.runningNowHandle!=null)
//        				RunningNow.runningNowHandle.sendEmptyMessage(RunningNow.PAUSE_MEDIA);
//        	        CountDownForPlayer.this.finish();
        	
        		}
        		super.handleMessage(msg);
        	}
        };
        initTimer();
//        cdt = new CountDownTimer((countTime)*1000, 1000) {
//        
//            public void onTick(long millisUntilFinished) {
//
//                
//                
//        	   // SendDataToMCU.senddataFunc((byte)0x40,(byte)ConstantValue.FIVE_SECOND_STATUS,(byte)0);
//		    	
//            }
//
//            public void onFinish() {
//            	//CountDownForPlayer.this.finish();
//            }
//         }.start();
 

    }
    
	public void initTimer()
	{
       if(mTimer!=null)
           mTimer.cancel();
		 mTimer = new Timer(true);
       if(mTimerTask!=null)
           mTimerTask.cancel();
		 mTimerTask = new MyTimerTask();  // �½�һ������
		 mTimer.schedule(mTimerTask, 0, 1000);
		
		
	}
	public  void stopTimer()
	{
		if(mTimer!=null)
               mTimer.cancel();
			 mTimer = new Timer(true);
           if(mTimerTask!=null)
               mTimerTask.cancel();
	}
	class MyTimerTask extends TimerTask{
		  @Override
		  public void run() {
			 // Message msg = Message.obtain();
			 // msg.what = SECOND_TICK;
			runOnUiThread(new Runnable()
			{

				@Override
				public void run() {
					// TODO Auto-generated method stub
		          	if(countTime == 7)
		          		iv.setBackgroundResource(R.drawable.countdown_for_player_5);
		          	else if(countTime == 6)
		          		iv.setBackgroundResource(R.drawable.countdown_for_player_4);
		          	else if(countTime == 5)
		          		iv.setBackgroundResource(R.drawable.countdown_for_player_3);
		          	else if(countTime == 4)
		          		iv.setBackgroundResource(R.drawable.countdown_for_player_2);
		          	else if(countTime == 3)
		          		iv.setBackgroundResource(R.drawable.countdown_for_player_1);
		          	else if(countTime ==2)
		          		
		          	{
		          		//iv.setBackgroundResource(R.drawable.countdown_for_player_1);
		          		//Intent intent = new Intent();
		              	//setResult(RESULT_OK,intent); //������2������(int resultCode, Intent intent)
		              	if(RunningNow.runningNowHandle!=null)
		              		RunningNow.runningNowHandle.obtainMessage(RunningNow.GO_ON_PLAY).sendToTarget();
		          		stopTimer();
		              	CountDownForPlayer.this.finish();
		          	}
		          	
		          	SendDataToMCU.senddataFunc((byte)0x40,(byte)ConstantValue.FIVE_SECOND_STATUS,(byte)0);
		          	
		          	SendDataToMCU.senddataFunc((byte)0x20, (byte)  RunningNow.currentInclineValue, (byte) 0);

			    	if(ModeSelect.securityLockMissForSendStatus)
			  		{
			  			SendDataToMCU.senddataFunc((byte)0x40,(byte)ConstantValue.NOSAFE_STATUS,(byte)0);
			  		}
			  		else if(ModeSelect.errrorHappendForSendStatus>0)
			  		{
			  			SendDataToMCU.senddataFunc((byte)0x40,(byte)ModeSelect.errrorHappendForSendStatus,(byte)0);
			  		}
		          	countTime--;
				}
	              
			  });
			  	
		  }
		     
	}
    
    
	protected void onResume() 
	{
		super.onResume();
		//cdt.start();
		//initTimer();
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
		
	}

    @Override
    protected void onPause() {
        super.onPause();
       // cdt.cancel();
       // stopTimer();
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        		WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        //wakeLock.release();
       // RunningNow.runningNowHandle.sendEmptyMessage(RunningNow.SECURITY_DISMISS_AND_STOP);
       // CountDownForPlayer.this.finish();
        
    }
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
    	mHandler = null;
    }
}
