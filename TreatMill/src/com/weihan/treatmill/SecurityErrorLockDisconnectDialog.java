package com.weihan.treatmill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

public class SecurityErrorLockDisconnectDialog extends Activity {
    /** Called when the activity is first created. */
	private TextView hintForSecuAndError;
	public static Handler mHandler;
	public final static int FINISH_THIS_ACTIVITY = 1;
	private int currentStatusOfAPP ;
	public final static int SECURITY_LOCK_AFTER_ERROR = 2;
	private boolean firstCreatStopPlayActivity = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.security_lock);
        
        hintForSecuAndError = (TextView)findViewById(R.id.security_error_info);
        hintForSecuAndError.setText(getIntent().getStringExtra("security_error_info"));
        currentStatusOfAPP = getIntent().getIntExtra("current_app_status", 0);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        		WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        
        mHandler = new Handler(){
        	@Override
        public void handleMessage(Message msg) {
        	// TODO Auto-generated method stub
        	switch (msg.what)
        	{
        	case FINISH_THIS_ACTIVITY:
        		if(currentStatusOfAPP == 1&&firstCreatStopPlayActivity == true)
        		{
        			firstCreatStopPlayActivity = false;
        			if(RunningNow.runningNowHandle!=null)
        				RunningNow.runningNowHandle.sendEmptyMessage(RunningNow.SECURITY_DISMISS_AND_STOP);
        			if(CountDownForPlayer.mHandler!=null)
        				CountDownForPlayer.mHandler.sendEmptyMessage(CountDownForPlayer.FINISH_THIS_ACTIVITY_WHEN_SECURITY);
//        			Intent intent = new Intent(SecurityErrorLockDisconnectDialog.this, StopPlay.class);
//        			startActivity(intent);
//        			SecurityErrorLockDisconnectDialog.this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
//        			
        		}
        		Log.v("finish security lock activity","finish security lock activity");
        		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        		SecurityErrorLockDisconnectDialog.this.finish();
        		break;
        	case SECURITY_LOCK_AFTER_ERROR:
        		hintForSecuAndError.setText((String)msg.obj);
        		break;
        	}
        		
        }};
    }
    @Override
    public void onDestroy()
    {
    	mHandler = null;
    	super.onDestroy();
    }
    
    public void onBackPressed()
    {
    	
    }
   
}

