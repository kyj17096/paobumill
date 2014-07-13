package com.weihan.treatmill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StopPlay extends Activity {

	Button goOnPlay;
	Button backToMain;
	RunningNow former;
	int GO_ON_PLAY = 1;
	int BACK_TO_MAIN = 2;
	CountDownTimer mCountDownTimer;
	public static final int FINISH_THIS_ACTIVITY_AND_GO_ON = 3;
	public static Handler mHandler;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.stop_play);
        
        mCountDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
               
            	SendDataToMCU.senddataFunc((byte)0x20, (byte) RunningNow.currentInclineValue, (byte) 0);
 
		    	if(ModeSelect.securityLockMissForSendStatus)
		  		{
		  			SendDataToMCU.senddataFunc((byte)0x40,(byte)ConstantValue.NOSAFE_STATUS,(byte)0);
		  		}
		  		else if(ModeSelect.errrorHappendForSendStatus>0)
		  		{
		  			SendDataToMCU.senddataFunc((byte)0x40,(byte)ModeSelect.errrorHappendForSendStatus,(byte)0);
		  		}
            	
            	
            }
            public void onFinish() {
            	finish();
            	
            	//ModeSelect.mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 2, -1).sendToTarget();
				//Intent intent = new Intent(StopPlay.this, RunningNow.class);
				//intent.putExtra("stop_play_choice", GO_ON_PLAY);
				//setResult(RESULT_OK,intent); //������2������(int resultCode, Intent intent)
				if(RunningNow.runningNowHandle!=null)
					RunningNow.runningNowHandle.obtainMessage(ConstantValue.START_RUN).sendToTarget();
            	mCountDownTimer.cancel();
				finish();
            	//if(RunningNow.runningNowHandle !=null)
            	//RunningNow.runningNowHandle.obtainMessage(RunningNow.GO_ON_PLAY).sendToTarget();
            }
         }.start();

        goOnPlay = (Button)findViewById(R.id.go_on_play);
        goOnPlay.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//ModeSelect.mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 2, -1).sendToTarget();
//				Intent intent = new Intent(StopPlay.this, RunningNow.class);
//				intent.putExtra("stop_play_choice", GO_ON_PLAY);
//				setResult(RESULT_OK,intent); //������2������(int resultCode, Intent intent)
				if(RunningNow.runningNowHandle!=null)
              		RunningNow.runningNowHandle.obtainMessage(ConstantValue.START_RUN).sendToTarget();
				mCountDownTimer.cancel();
				finish();
			}
        	
        });
        backToMain = (Button)findViewById(R.id.back_to_main);
        backToMain.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(RunningNow.runningNowHandle!=null)
              		RunningNow.runningNowHandle.obtainMessage(ConstantValue.FINISH_RUNNINGNOW_ACTIVITY).sendToTarget();
				
				// TODO Auto-generated method stub
//				Intent intent = new Intent(StopPlay.this, RunningNow.class);
//				intent.putExtra("stop_play_choice", BACK_TO_MAIN);
//				setResult(RESULT_OK,intent); //������2������(int resultCode, Intent intent)
				mCountDownTimer.cancel();
				finish();
			}
        	
        });
		
        mHandler = new Handler(){
        	@Override
        	public void handleMessage(Message msg) {
        		
        		switch(msg.what)
        		{
        		case FINISH_THIS_ACTIVITY_AND_GO_ON:
        			Intent intent = new Intent(StopPlay.this, RunningNow.class);
    				intent.putExtra("stop_play_choice", GO_ON_PLAY);
    				setResult(RESULT_OK,intent); //������2������(int resultCode, Intent intent)
    				finish();
    				mCountDownTimer.cancel();
    				break;
        		}
        		// TODO Auto-generated method stub
        		super.handleMessage(msg); 		
        	}
        };
//		
//		Intent intent2 = new Intent(this, ModeSelect.class);
//		startActivity(intent2);
//		this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
    }
    @Override
   	public void onBackPressed(){
//   		super.onBackPressed();
//    	Intent intent = new Intent(StopPlay.this, RunningNow.class);
//		intent.putExtra("stop_play_choice", GO_ON_PLAY);
//		setResult(RESULT_OK,intent); //������2������(int resultCode, Intent intent)
//		finish();
//		mCountDownTimer.cancel();
    	return;
		
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
    @Override 
    protected void onDestroy()
    {
    	super.onDestroy();
    	mHandler = null;
    }
    
   
}