package com.weihan.treatmill;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import com.weihan.treatmill.LocalMoiveList.VideoInfo;
import com.weihan.treatmill.MyFloatFrameLayout.FloatFitnessDate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RunningNow extends Activity implements SurfaceHolder.Callback {

	SharedPreferences sp; 
	String currentUserID;
	private FrameLayout.LayoutParams moveFLForSpeedIcon;
	public MovableSpeedSelectIcon speedSelectIcon;
    private FrameLayout.LayoutParams moveFLForSpeedSelectPanel;
    private MovableSpeedSelectPanel speedSelectPanel;
	MyFloatFrameLayout fvFloat = null; 
	ImageView speedScrollSetting;
    //ImageView speedScaleInPanel,speedScaleValueInPanel;
    private FrameLayout.LayoutParams moveFLForSpeedScalescroll;//moveFLForSpeedScaleValuescroll;
    private FrameLayout.LayoutParams moveFLForspeedcrollTexture;
    ImageView speedScrollTextureImage;
    private Button turnOffSpeedSelectPanel;
    TextView speedValueInSpeedIcon,speedScaleInSpeedIcon;
    TextView speedValueTextInPanel,textSpeedScaleInPanel;
    private float currentSpeedValue;
    private float oldSpeedValue;
    static int currentInclineValue;
    private int oldInclineValue;
    byte[] programModeSpeedData;
    byte[] programModeInclineData;
    int heartbeatValue;
    private boolean errorHanped= false;
    private FrameLayout.LayoutParams moveFLForHeightIcon;
	public MovableHeightSelectIcon heightSelectIcon;
    private FrameLayout.LayoutParams moveFLForHeightSelectPanel;
    private MovableHeightSelectPanel heightSelectPanel;
    ImageView heightScrollSetting;
   //ImageView heightScaleInPanel,heightScaleValueInPanel;
    private FrameLayout.LayoutParams moveFLForHeightScalescroll;
    private FrameLayout.LayoutParams moveFLForHeightScaleValuescroll;
    private FrameLayout.LayoutParams moveFLForHeightscrollTexture;
    ImageView heightScrollTextureImage;
    private Button turnOffHeightSelectPanel;
    TextView heightValueInHeightIcon,heightSacleInHeightIcon,textHeightScaleInPanel,heightValueTextInPanel;
    private int maxVolume = 50;
    private int currVolume;
    public static Handler runningNowHandle;
	CountDownTimer cdt;
	float finalDistance,finalTime,finalCalorie;
	public MovableRealTimeRunningStatus realTimeRunningStatus;
    private FrameLayout.LayoutParams moveFLForRealTimeStatusPanel;
    float scroll_speed_last_position;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer vedioMediaPlayer; 
    boolean pauseOperationFlag = false;;
    static boolean isEndStatus = false;
    private MediaPlayer musicMmediaPlayer; 
    private MediaPlayer mdeiaPlayerForCotrollBar;
    Button playMedia,stopMedia,selectMedia,preMedia,nextMedia,volumeDec,volumeAdd,speakerMute,speakerOn;
    TextView currentPlayMediaName;
    ListView mediaListView;
    ArrayList<MediaInfo> medialist;
    int curentMediaPosition = 0;
    Button hideDisplayRTSPanel;
    int ANIMATION_DURATION = 300;
    int countDownStartRunningFirst = 0;

    int currenMdeiatMode;
    boolean realPanelAnimationIng = false;
    int currentTask;
    float currentTaskValue;
    private float currentCarorieInRealPanel;
    private float currentDistanceInRealPanel;
    private int currentTimeInRealPanel;
    TextView speedValueInRealPanel,inclineValueInRealPanel;
    private TextView distanceValueInRealPanel,timeValueInRealPanel,calorieValueInRealPanel;
    ImageView chartInrealPanel,heartBeatChart;
    TextView heartBeatCount;
    ImageView heartBeatSpeedHint;
    
    private Timer mTimer;
    private MyTimerTask mTimerTask;

    private FrameLayout videoListPanel;
    private String currentMediaPath;
    Button playStop;
    ImageView bluetoothConnectionStatus;
    TextView bluetoothConnectionStatusText;
    private SurfaceView surfaceView;
    private final int SECOND_TICK = 0;
    private final int CONNECT_STATION_CHANGE = 1;
    private final int HEART_BEAT_DATA = 2;
    public static final int FINISH_THIS_ACTIVITY = 711;
    public static final int PAUSE_WHEN_ERROR_HAPPEN = 712;
    private int currentMode;
    int currentSpeedScale;
    int currentInclineScale;
    Bitmap speedInclineBt;
    Canvas canvas;
    private int speedXdelta;
    private int speedYdelta;
    private int inclineXdelta;
    private int inclineYdelta;
     boolean firstCountDown;
    FrameLayout controlBarForMediaPlayer;
    Button frMovieBtn,ffMovieBtn,movieListButton;
    boolean pauseOrStop = false;
    PowerManager powerManager = null;
    PowerManager.WakeLock wakeLock = null;
    AudioManager mAudioManager;   
    private int testCount = 1;
    static final int  SPEED_VALUE_SET_IN_FLOAT_DIALOG = 700;
	static final int INCLINE_VALUE_SET_IN_FLOAT_DIALOG = 701;
	public static final int SECURITY_DISMISS_AND_STOP = 710;
	static final int SECURITY_LOCK = 702;
    private int maxSpeedPosInPanel;
    private int minSpeedPosInPanel;
    private int maxInclinePosInPanel;
    private boolean fiveSecondCountIsFinish = false;
    int currentVolume;
    boolean cantPlayLocalMediaFile = false;
    boolean cantPlayRelaxMediaFile = false;
    int currentPlace;
    WindowManager wm;
    public final static int GO_ON_PLAY = 432;
    //static boolean securityLockMissForSendStatus = false;
    //static int  errrorHappendForSendStatus = 0;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
        setContentView(R.layout.running_now);
        Log.v("runningnow oncreat","runningnow oncreat");
        firstCountDown = true;
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);    
         wm = (WindowManager)getApplicationContext().getSystemService(WINDOW_SERVICE);
         

         show();  
	    maxSpeedPosInPanel  = (int) (36-(ModeSelect.maxSpeed-1)*17.5);
	    minSpeedPosInPanel  = (int) (36-(ModeSelect.minSpeed-1)*17.5);
	   
	    maxInclinePosInPanel = (int) (28-(ModeSelect.maxIncline+0.5)*17.5);
	    
        powerManager = (PowerManager)RunningNow.this.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock( PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "My Lock2");
        
        currentMediaPath = this.getIntent().getExtras().getString("mediaPath");       
        currentMode = this.getIntent().getExtras().getInt("mode");
        
        Log.v("current = "+ currentMode,"current = "+ currentMode);

        if(currentMediaPath == null)
        	cantPlayRelaxMediaFile = true;
        
        medialist = new ArrayList<MediaInfo>();
      // audioList = new ArrayList<MediaInfo>();
       // videoList = new ArrayList<MediaInfo>();
        initMediaInfoList(currentMode);       
        initMediaListView();
        if(medialist.size()==0)
        {
        	cantPlayLocalMediaFile = true;
        }
        Log.v("medialist leng "+medialist.size(),"medialist leng "+medialist.size());
        canvas = new Canvas();
        speedInclineBt = Bitmap.createBitmap(550, 160, Config.ARGB_8888);
        canvas.setBitmap(speedInclineBt);
        canvas.clipRect(0,0,550,160);
        
        sp = getSharedPreferences("user_name_id", MODE_PRIVATE);
        currentUserID = sp.getString(storeDataInLocal.CURRENT_USER_ID, null);
        Log.v("running currentUserID "+currentUserID,"running currentUserID "+currentUserID);
    	Message msm = Message.obtain();
        msm.what = ConstantValue.CONNECT_TO_ROMOTE_BLUETOOTH_DEVICE;
       // BlueToothMsgService.mHandler.sendMessage(msm);
        runningNowHandle = new Handler(){
        	
            @Override
            public void handleMessage(Message msg) {
            		switch (msg.what) 
	                {           		
	                case  SECOND_TICK:
	                	if(pauseOrStop == false&&fiveSecondCountIsFinish == true)
	                	{
	                		secondTickUpdate();
	                		sendRunStatus();
	                	}
	                	
	                	break;
                        case HEART_BEAT_DATA:
                            //upDateHeartBeatStatus(msg.arg1);
                            break;
                        case SPEED_VALUE_SET_IN_FLOAT_DIALOG:
                        	currentSpeedValue = (Float)msg.obj;
                        	speedValueInSpeedIcon.setText(String.format("%.1f",currentSpeedValue));
                        	break;
                        case INCLINE_VALUE_SET_IN_FLOAT_DIALOG:
                        	currentInclineValue = (Integer)msg.obj;
                        	heightValueInHeightIcon.setText(""+currentInclineValue);
                        	break;
//                        case SPEED_VALUE_SET_IN_UART:
//                        	currentSpeedValue = (Float)msg.obj;
//                        	break;
//                        case INCLINE_VALUE_SET_IN_UART:
//                        	currentInclineValue = (Float)msg.obj;
//                        	break;
                        case  ConstantValue.SECURITY_LOCK :	 
                        	
//                        	if(msg.arg1 == 1)
//                        	{	//new AlertDialog.Builder(RunningNow.this).setTitle("´íÎó").setMessage("°²È«ËøÍÑÂä").setPositiveButton(null, null).show(); 
//                        		
//                        		ModeSelect.securityLockMissForSendStatus = true;
//                        		cleanRunStatus();
                        		
//                        		SendDataToMCU.senddataFunc((byte)0x40,(byte)ConstantValue.NOSAFE_STATUS,(byte)0);
//                        		if(SecurityErrorLockDisconnectDialog.mHandler !=null && errorHanped == false)
//                            		return;
//                        		if(SecurityErrorLockDisconnectDialog.mHandler !=null && errorHanped == true)
//                        		{
//                        			errorHanped = false;
//                        			Message msg1 = Message.obtain();
//                        			msg1.what = SecurityErrorLockDisconnectDialog.SECURITY_LOCK_AFTER_ERROR;
//                        			msg1.obj = "°²È«ËøÍÑÂä";                   			
//                        			SecurityErrorLockDisconnectDialog.mHandler.sendMessage(msg1);
//                        			return;
//                        		}
//                        		
//                        		pauseOrStop = true;
//                        		
//                        		mediaPlayersPause();
//                        		Intent intent2 = new Intent(RunningNow.this, SecurityErrorLockDisconnectDialog.class);
//                        		intent2.putExtra("security_error_info", "°²È«ËøÍÑÂä");
//                        		intent2.putExtra("current_app_status", 1);
//                    			startActivity(intent2);
//                    			RunningNow.this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
//                    			getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                		        		WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                        	}
//                        	else
 //                       	{
//                        		ModeSelect.securityLockMissForSendStatus = false;
//                        		
//                        		ModeSelect.errrorHappendForSendStatus = 0;
//                        	    Log.v("send to finish secrutiy lock","send to finish secrutiy lock");
//                        		if(SecurityErrorLockDisconnectDialog.mHandler !=null)
//                        		SecurityErrorLockDisconnectDialog.mHandler.sendEmptyMessage(SecurityErrorLockDisconnectDialog.FINISH_THIS_ACTIVITY);
//                        		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                        	}
                        	break; 
        	       				
                        case ConstantValue.CURRENT_SPEED_AND_INCLINE:
                        	currentInclineValue = msg.arg2;//ËÙ¶È
               				break;
                        case ConstantValue.SPEED_SET:
                        	Log.v("running Uart speed set command "+(Float)msg.obj,"Uart command ");
                        	currentSpeedValue = (Float)msg.obj;
                        	speedSet(0);
                        	
                        	
                        	break;
                        case ConstantValue.INCLINE_SET:
                        	currentInclineValue = (int)((Float)msg.obj).floatValue();
                        	inclineSet(0);
                        	break;
                        	
    	       			case  ConstantValue.INCLINE_ADD:
    	       				currentInclineValue++;
    	       				if(currentInclineValue > ModeSelect.maxIncline)
    	       					currentInclineValue =  (int) ModeSelect.maxIncline;
                        	//heightValueInHeightIcon.setText(""+currentInclineValue);
    	       				inclineSet(0);
    	       				break;			
    	       			case  ConstantValue.INCLINE_MINS:
    	       				currentInclineValue--;
    	       				if(currentInclineValue <=0)
    	       					currentInclineValue = 0;
                        	//heightValueInHeightIcon.setText(""+currentInclineValue);
    	       				inclineSet(0);
    	       				break;	
    	       			case  ConstantValue.SPEED_ADD:
    	       				currentSpeedValue =  (currentSpeedValue+(float)0.1);
    	       				if(currentSpeedValue > ModeSelect.maxSpeed)
    	       					currentSpeedValue = ModeSelect.maxSpeed;
                        	//speedValueInSpeedIcon.setText(String.format("%.1f",currentSpeedValue));
    	       				speedSet(0);
    	       				break;		
    	       			case  ConstantValue.SPEED_MINS:
    	       				currentSpeedValue =  (currentSpeedValue-(float)0.1);
    	       				if(currentSpeedValue <= ModeSelect.minSpeed)
    	       					currentSpeedValue = ModeSelect.minSpeed;
                        	speedValueInSpeedIcon.setText(String.format("%.1f",currentSpeedValue));
                        	speedSet(0);
    	       				break;	
    	       				
    	       			case  ConstantValue.START_RUN://¿ªÊ¼µ¹¼ÆÊ±
    	       				if(pauseOrStop == false)
    	       					return;
    	       				ModeSelect.mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 2, -1).sendToTarget();
//    	       				if(pauseOperationFlag == true)
//    	       				{
	    	       				Intent intent = new Intent(RunningNow.this, CountDownForPlayer.class);
	    	                    Log.v("click calorie mode","click calorie mode");
	    	                    startActivity(intent);
	    						pauseOrStop = true;
	    						pauseOperationFlag = false;
//    	       				}
	    					if(StopPlay.mHandler!=null)
	    						StopPlay.mHandler.sendEmptyMessage(StopPlay.FINISH_THIS_ACTIVITY_AND_GO_ON);
    	       				break;
    	       			case  ConstantValue.STOP_RUN:
    	       				if(pauseOrStop == true)
    	       					return;
    	       				pauseOrStop = true;
//    	       				if(pauseOperationFlag == false)
//    	       				{
//    	       					pauseOperationFlag = true;
//    	       					pauseOperation();
//    	       					
 //   	       				}
    	       				isEndStatus = true;
    	    				pauseOperation();
    	    				ModeSelect.mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 3, -1).sendToTarget();
    	       				break;
    	       			case ConstantValue.HEART_BEAT_AND_ERROR:
        	       			if(msg.arg1 > 0)
        	       			{
//        	       				ModeSelect.securityLockMissForSendStatus = true;
        	       	    		errorHanped = true;
        	       	    		cleanRunStatus();
//        	       	    		ModeSelect.errrorHappendForSendStatus = msg.arg1;
//                        	    SendDataToMCU.senddataFunc((byte)0x40,(byte)ModeSelect.errrorHappendForSendStatus,(byte)0);
//                          		
//                        		Intent intent2 = new Intent(RunningNow.this, SecurityErrorLockDisconnectDialog.class);
//                        		intent2.putExtra("security_error_info", "´íÎó"+msg.arg1);
//                        		intent2.putExtra("current_app_status", 1);
//                    			startActivity(intent2);
//                    			RunningNow.this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
        	       				mediaPlayersPause();
                    			getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                		        		WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
  
        	       			}
    	       				//new AlertDialog.Builder(RunningNow.this).setTitle("´íÎó").setMessage("ÅÜ²½»ú×´Ì¬´íÎó"+msg.arg1).setPositiveButton("È·¶¨", null).show();   	       				
	       					heartbeatValue = msg.arg2;
	       					heartBeatCount.setText(""+heartbeatValue);
	       					Log.v("heart beat "+heartbeatValue ,"heart beat "+heartbeatValue );
    	       				break;
    	       			case SECURITY_DISMISS_AND_STOP:
    	       					closeActivity();
    	       				break;
    	       			case FINISH_THIS_ACTIVITY:
    	       				closeActivity();
    	       				break;
    	       			case PAUSE_WHEN_ERROR_HAPPEN:
    	       				mediaPlayersPause();
    	       				break;
    	       			case GO_ON_PLAY://¿ªÊ¼ÅÜ
    						isEndStatus = false;
    						Log.v("stop play3 ","stop play");
    						//mediaPlayersGoOn();
    						pauseOperationFlag = false;
//    	                    Intent intent1 = new Intent(RunningNow.this, CountDownForPlayer.class);
//    	                    intent1.putExtra("count_time",3);
//    	                    Log.v("click calorie mode","click calorie mode");
//    	                    startActivityForResult(intent1,5);
//    						pauseOrStop = true;
    						
    						fiveSecondCountIsFinish = true;
    						countDownStartRunningFirst = 0;
    						pauseOrStop = false;
    						mediaPlayersGoOn();
    						break;
    	       			case ConstantValue.MEDIA_FORMER:
    	       				frontMedia(mdeiaPlayerForCotrollBar);
    	       				break;
    	       			case ConstantValue.MEDIA_NEXT:
    	       				nextMedia(mdeiaPlayerForCotrollBar);
    	       				break;
    	       				
    	       			case ConstantValue.FINISH_RUNNINGNOW_ACTIVITY:
    						closeMediaPlayer();
    						closeActivity();
    						break;
	                	default:
	                		break;
	                	
	                }
            	
            }
        
            
        };
        currVolume = 25;
       
        playStop = (Button)findViewById(R.id.stop_play);
        playStop.setOnClickListener(new OnClickListener(){
		@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			runningNowHandle.obtainMessage(ConstantValue.STOP_RUN).sendToTarget();
//			isEndStatus = true;
//				pauseOperation();
//				ModeSelect.mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 3, -1).sendToTarget();
//			if(testCount%2 == 0)
//			{
//				Message msg = Message.obtain();
//				msg.what = ConstantValue.SECURITY_LOCK;
//				msg.arg1 = 0;
//				runningNowHandle.sendMessage(msg);
//			}
//			else
//			{
//
//				Message msg = Message.obtain();
//				msg.what = ConstantValue.SECURITY_LOCK;
//				msg.arg1 = 1;
//				runningNowHandle.sendMessage(msg);
//			}
//				
//			testCount++;
			}
        });
        
        currentSpeedValue = ModeSelect.minSpeed; 
        oldSpeedValue = currentSpeedValue;
        moveFLForSpeedIcon = new FrameLayout.LayoutParams(70, 80); 
        moveFLForSpeedIcon.topMargin = 130;
        moveFLForSpeedIcon.gravity = Gravity.LEFT ; 
        speedSelectIcon = (MovableSpeedSelectIcon)findViewById(R.id.speed_select_icon);
        speedSelectIcon.setCallBack(new CallBackForSpeedIcon());       
        speedValueInSpeedIcon= (TextView)findViewById(R.id.speed_value_in_speed_icon);
        speedValueInSpeedIcon.setText(String.format("%.1f", currentSpeedValue));
        speedScaleInSpeedIcon = (TextView)findViewById(R.id.speed_scale_in_speed_icon);
        speedScaleInSpeedIcon.setText("km/h");
        speedSelectIcon.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				moveFLForSpeedIcon.leftMargin = -80;  
				speedSelectIcon.setTP(moveFLForSpeedIcon);  
		        TranslateAnimation ani = new TranslateAnimation(0, -80, 0, 0);  
		        ani.setDuration(ANIMATION_DURATION);
		        speedSelectIcon.startAnimation(ani);
			}
           });

        moveFLForSpeedSelectPanel = new FrameLayout.LayoutParams(150, 239); 
        moveFLForSpeedSelectPanel.topMargin = 70;
        moveFLForSpeedSelectPanel.gravity = Gravity.LEFT ; 
        speedSelectPanel = (MovableSpeedSelectPanel)findViewById(R.id.speed_select_panel);
        speedSelectPanel.setCallBack(new CallBackForSpeedSelectPanel());

        moveFLForSpeedScalescroll = new FrameLayout.LayoutParams(30, 546);
        moveFLForSpeedScalescroll.leftMargin = 25;
        moveFLForSpeedScalescroll.topMargin = 36- (int)((currentSpeedValue-1.0)*17.5);
        
//        moveFLForSpeedScaleValuescroll = new MyFrameLayout.LayoutParams(15,546);
//        moveFLForSpeedScaleValuescroll.leftMargin = 25;
//        moveFLForSpeedScaleValuescroll.topMargin =  35- (int)((currentSpeedValue)*180/24);
       
        speedScrollSetting = (ImageView)findViewById(R.id.select_speed_scroll_sv);
        //speedScaleInPanel = (ImageView)findViewById(R.id.select_speed_scroll_scale);
       // speedScaleValueInPanel = (ImageView)findViewById(R.id.speed_scroll_scale_value);
        //speedScaleValueInPanel.setImageBitmap( drawScaleValueOnSpeedScale());
        
        
        moveFLForspeedcrollTexture = new FrameLayout.LayoutParams(14, 800);       
        moveFLForspeedcrollTexture.leftMargin = 0;
        moveFLForspeedcrollTexture.topMargin = -(int)(1.5*(currentSpeedValue)*35) ;
        speedScrollTextureImage = (ImageView)findViewById(R.id.speed_texture_move);
       
        speedValueTextInPanel = (TextView)findViewById(R.id.selectSpeedValue);
        speedValueTextInPanel.setText(String.format("%.1f", currentSpeedValue));
        textSpeedScaleInPanel = (TextView)findViewById(R.id.text_speed_scale_in_panel);

        speedScrollSetting.setLayoutParams(moveFLForSpeedScalescroll);
        //speedScaleInPanel.setLayoutParams(moveFLForSpeedScalescroll);
        //speedScaleValueInPanel.setLayoutParams(moveFLForSpeedScaleValuescroll);
        speedScrollTextureImage.setLayoutParams(moveFLForspeedcrollTexture);

        //updateSpeedAndHeightState(); 	
        speedSelectPanel.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				
				final int X = (int) arg1.getRawX();
			    final int Y = (int) arg1.getRawY();
			    switch (arg1.getAction() & MotionEvent.ACTION_MASK) {
			        case MotionEvent.ACTION_DOWN:
			        	moveFLForSpeedScalescroll = (FrameLayout.LayoutParams) speedScrollSetting.getLayoutParams();
			        	//speedXdelta = X - moveFLForSpeedScalescroll.leftMargin;
			        	speedYdelta = Y - moveFLForSpeedScalescroll.topMargin;
			            break;
			        case MotionEvent.ACTION_UP:
			        	if(oldSpeedValue > currentSpeedValue)
				    		ModeSelect.mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 8, -1).sendToTarget();	
				    	else if (oldSpeedValue < currentSpeedValue)
				    		ModeSelect.mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING,7, -1).sendToTarget();	
				    	oldSpeedValue = currentSpeedValue;
			            break;
			        case MotionEvent.ACTION_POINTER_DOWN:
			            break;
			        case MotionEvent.ACTION_POINTER_UP:
			            break;
			        case MotionEvent.ACTION_MOVE:
			        	moveFLForSpeedScalescroll = (FrameLayout.LayoutParams) speedScrollSetting.getLayoutParams();
			          //  layoutParams.leftMargin = X - _xDelta;
			        	moveFLForSpeedScalescroll.topMargin = Y - speedYdelta;
			        	if(moveFLForSpeedScalescroll.topMargin > minSpeedPosInPanel /*32*/)
			        		moveFLForSpeedScalescroll.topMargin = minSpeedPosInPanel;
			        	if(moveFLForSpeedScalescroll.topMargin < maxSpeedPosInPanel/*-405*/)
			        		moveFLForSpeedScalescroll.topMargin = maxSpeedPosInPanel;
			        	currentSpeedValue = (36 - (float)moveFLForSpeedScalescroll.topMargin )/(float)17.5+1 ;
		        	
			        	//moveFLForSpeedScaleValuescroll.topMargin  = moveFLForSpeedScalescroll.topMargin ;
			        	moveFLForspeedcrollTexture.topMargin = (int) ((moveFLForSpeedScalescroll.topMargin-30)*1.5);
			        	speedValueInSpeedIcon.setText(String.format("%.1f",currentSpeedValue));
						speedValueTextInPanel.setText(String.format("%.1f",currentSpeedValue));
						speedScrollSetting.setLayoutParams(moveFLForSpeedScalescroll);
						//speedScaleValueInPanel.setLayoutParams(moveFLForSpeedScaleValuescroll);
						speedScrollTextureImage.setLayoutParams(moveFLForspeedcrollTexture);
			        						
			        	break;
			    }

				return true;
			}
        	
        });
        
        turnOffSpeedSelectPanel = (Button)findViewById(R.id.turn_off_speed_select_panel);
        turnOffSpeedSelectPanel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v("speed select","speed select");
				
				moveFLForSpeedSelectPanel.leftMargin = -160;  
				speedSelectPanel.setTP(moveFLForSpeedSelectPanel);  
		        TranslateAnimation ni = new TranslateAnimation(0, -160, 0, 0);  
		        ni.setDuration(ANIMATION_DURATION);
		        speedSelectPanel.startAnimation(ni);
			}      	
        });
        
        currentInclineValue = 0;  
        oldInclineValue = 0;
        moveFLForHeightIcon = new FrameLayout.LayoutParams(70, 80); 
        moveFLForHeightIcon.topMargin = 130;
        moveFLForHeightIcon.leftMargin = 730;
        moveFLForHeightIcon.gravity = Gravity.RIGHT ; 
        heightSelectIcon = (MovableHeightSelectIcon)findViewById(R.id.height_select_icon);
        heightSelectIcon.setCallBack(new CallBackForHeightIcon());
        heightSelectIcon.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				moveFLForHeightIcon.leftMargin = 800;  
				heightSelectIcon.setTP(moveFLForHeightIcon);  
		        TranslateAnimation ani = new TranslateAnimation(0, 70, 0, 0);  
		        ani.setDuration(ANIMATION_DURATION);
		        heightSelectIcon.startAnimation(ani);
			}
        });
        
        moveFLForHeightSelectPanel = new FrameLayout.LayoutParams(150, 239); 
        moveFLForHeightSelectPanel.topMargin = 70;
        moveFLForHeightSelectPanel.leftMargin = 800;
        moveFLForHeightSelectPanel.gravity = Gravity.RIGHT ; 
        heightSelectPanel = (MovableHeightSelectPanel)findViewById(R.id.height_select_panel);
        heightSelectPanel.setCallBack(new CallBackForHeightSelectPanel());



        moveFLForHeightScalescroll = new FrameLayout.LayoutParams(30, 546);
        moveFLForHeightScalescroll.leftMargin = 30;
        moveFLForHeightScalescroll.topMargin=28- (int)((currentInclineValue)*17.5);
//        moveFLForHeightScaleValuescroll = new FrameLayout.LayoutParams(15,546);
//        moveFLForHeightScaleValuescroll.leftMargin =45;
//        moveFLForHeightScaleValuescroll.topMargin = 27- (int)((currentInclineValue)*168/12);
        
        heightScrollSetting = (ImageView)findViewById(R.id.select_incline_scroll_sv);
        //heightScaleInPanel = (ImageView)findViewById(R.id.select_height_scroll_scale);
        //heightScaleValueInPanel = (ImageView)findViewById(R.id.height_scroll_scale_value);
        //heightScaleValueInPanel.setImageBitmap(drawScaleValueOnInclineScale());
        moveFLForHeightscrollTexture = new FrameLayout.LayoutParams(14, 800);
        moveFLForHeightscrollTexture.leftMargin = 0;
        moveFLForHeightscrollTexture.topMargin =-(int)(1.5*(currentInclineValue+8)*35) ;
        heightScrollTextureImage = (ImageView)findViewById(R.id.height_texture_move);

        heightScrollSetting.setLayoutParams(moveFLForHeightScalescroll);
       // heightScaleInPanel.setLayoutParams(moveFLForHeightScalescroll);
      //  heightScaleValueInPanel.setLayoutParams(moveFLForHeightScaleValuescroll);
        heightScrollTextureImage.setLayoutParams(moveFLForHeightscrollTexture);

        heightValueTextInPanel = (TextView)findViewById(R.id.select_height_value);
        heightValueTextInPanel.setText(""+(int)currentInclineValue);
        textHeightScaleInPanel = (TextView)findViewById(R.id.text_height_scale_in_panel);
        heightValueInHeightIcon= (TextView)findViewById(R.id.height_value_in_height_icon);
        heightValueInHeightIcon.setText(""+(int)currentInclineValue);
        heightSacleInHeightIcon = (TextView)findViewById(R.id.height_scale_in_height_icon);
        heightSacleInHeightIcon.setText("unit");
        heightSelectPanel.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				
				
			//	final int X = (int) arg1.getRawX();
			    final int Y = (int) arg1.getRawY();
			    switch (arg1.getAction() & MotionEvent.ACTION_MASK) {
			        case MotionEvent.ACTION_DOWN:
			        	moveFLForHeightScalescroll = (FrameLayout.LayoutParams) heightScrollSetting.getLayoutParams();
			        	//speedXdelta = X - moveFLForSpeedScalescroll.leftMargin;
			        	inclineYdelta = Y - moveFLForHeightScalescroll.topMargin;
			            break;
			        case MotionEvent.ACTION_UP:
			        	if(oldInclineValue > currentInclineValue)
				    		ModeSelect.mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 50, -1).sendToTarget();	
				    	else if(oldInclineValue < currentInclineValue)
				    		ModeSelect.mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING,51, -1).sendToTarget();	
				    	
				    	oldInclineValue = currentInclineValue;
			            break;
			        case MotionEvent.ACTION_POINTER_DOWN:
			            break;
			        case MotionEvent.ACTION_POINTER_UP:
			        	
			            break;
			        case MotionEvent.ACTION_MOVE:
			        	moveFLForHeightScalescroll = (FrameLayout.LayoutParams) heightScrollSetting.getLayoutParams();
			          //  layoutParams.leftMargin = X - _xDelta;
			        	moveFLForHeightScalescroll.topMargin = Y - inclineYdelta;
			        	if(moveFLForHeightScalescroll.topMargin > 28)
			        		moveFLForHeightScalescroll.topMargin = 28;
			        	if(moveFLForHeightScalescroll.topMargin < maxInclinePosInPanel/*-410*/)
			        		moveFLForHeightScalescroll.topMargin = maxInclinePosInPanel;
			        	currentInclineValue = (int) ((28 - moveFLForHeightScalescroll.topMargin )/17.5) ;
			        	
			        	
			        	//moveFLForHeightScaleValuescroll.topMargin  = moveFLForHeightScalescroll.topMargin ;
			        	moveFLForHeightscrollTexture.topMargin = (int) ((moveFLForHeightScalescroll.topMargin-10)*1.5);
						heightValueTextInPanel.setText(""+(int)currentInclineValue);
						heightValueInHeightIcon.setText(""+(int)currentInclineValue);
						heightScrollSetting.setLayoutParams(moveFLForHeightScalescroll);
				        //heightScaleValueInPanel.setLayoutParams(moveFLForHeightScaleValuescroll);
				        heightScrollTextureImage.setLayoutParams(moveFLForHeightscrollTexture);
								        	
			        	break;
			    }

				return true;
			}
        	
        });
        
        turnOffHeightSelectPanel = (Button)findViewById(R.id.turn_off_height_select_panel);
        turnOffHeightSelectPanel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v("speed select","speed select");
				
				moveFLForHeightSelectPanel.leftMargin = 800;  
				heightSelectPanel.setTP(moveFLForHeightSelectPanel);  
		        TranslateAnimation ni = new TranslateAnimation(0, 150, 0, 0);  
		        ni.setDuration(ANIMATION_DURATION);
		        heightSelectPanel.startAnimation(ni);
			}      	
        });



        heartBeatCount = (TextView)findViewById(R.id.heart_beat_value_text);
        heartBeatSpeedHint = (ImageView )findViewById(R.id.heart_beat_speed_hint_img);
        chartInrealPanel = (ImageView)findViewById(R.id.speed_incline_chart);       
        speedValueInRealPanel = (TextView)findViewById(R.id.speed_value_in_real_panel);
        inclineValueInRealPanel = (TextView)findViewById(R.id.incline_value_in_real_panel);;
        distanceValueInRealPanel = (TextView)findViewById(R.id.distance_value_in_real_panel);
        timeValueInRealPanel = (TextView)findViewById(R.id.time_value_in_real_panel);
        calorieValueInRealPanel = (TextView)findViewById(R.id.calorie_value_in_real_panel);
        heartBeatChart = (ImageView)findViewById(R.id.heart_beat_image_in_real_panel);       
        moveFLForRealTimeStatusPanel = new FrameLayout.LayoutParams(800, 221);  
        moveFLForRealTimeStatusPanel.gravity = Gravity.LEFT ; 
        realTimeRunningStatus = (MovableRealTimeRunningStatus)findViewById(R.id.real_time_running_status_panel);
       // hideDisplayRTSPanel = (Button)findViewById(R.id.hide_display_real_time_status_panel);
        realTimeRunningStatus.setCallBack(new CallBackForRealPanel());
        realTimeRunningStatus.setOnClickListener(new OnClickListener(){       	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int delta;
				if(realTimeRunningStatus.getTop()<390)
				{
					delta = 230;
					moveFLForRealTimeStatusPanel.topMargin = 410; 
					
				}
				else
				{
					delta = -230;
					moveFLForRealTimeStatusPanel.topMargin = 180; 
				}

				realTimeRunningStatus.setTP(moveFLForRealTimeStatusPanel);  
		        TranslateAnimation ni = new TranslateAnimation(0, 0, 0, delta);  
		        ni.setDuration(ANIMATION_DURATION);
		        realPanelAnimationIng = true;
		        realTimeRunningStatus.startAnimation(ni);
			}
        });


        
        videoListPanel = (FrameLayout)findViewById(R.id.media_list_panel);
        
        
        frMovieBtn= (Button)findViewById(R.id.fr_movie_btn);
        frMovieBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				frontMedia(mdeiaPlayerForCotrollBar);
				//vedioMediaPlayer.start();
			}
        	
        });
        ffMovieBtn = (Button)findViewById(R.id.ff_moive_btn);
        ffMovieBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//mdeiaPlayerForCotrollBar.start();
				nextMedia(mdeiaPlayerForCotrollBar);
			}
        	
        });
        movieListButton= (Button)findViewById(R.id.movie_list_btn);
        movieListButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                frMovieBtn.setVisibility(View.VISIBLE);
                ffMovieBtn.setVisibility(View.VISIBLE);
                stopMedia.setVisibility(View.VISIBLE);
                if(videoListPanel.getVisibility() == View.GONE)
                {
                    videoListPanel.setVisibility(View.VISIBLE);
                }
                else if(videoListPanel.getVisibility() == View.VISIBLE)
                {
                    videoListPanel.setVisibility(View.GONE);
			    }
            }
        	
        });
        playMedia = (Button)findViewById(R.id.play_media_btn);
        playMedia.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				playMedia.setVisibility(View.GONE);
				stopMedia.setVisibility(View.VISIBLE);
				if(!cantPlayLocalMediaFile)
					mdeiaPlayerForCotrollBar.start();
			}
        	
        });
        stopMedia = (Button)findViewById(R.id.stop_play_media_btn);
        stopMedia.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopMedia.setVisibility(View.GONE);
				playMedia.setVisibility(View.VISIBLE);
				if(!cantPlayLocalMediaFile)					
					mdeiaPlayerForCotrollBar.pause();
			}
        	
        });
        selectMedia = (Button)findViewById(R.id.music_list_btn);
        selectMedia.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
//				// TODO Auto-generated method stub
//                if(currentMode == ConstantValue.ARCADE_MODE)
//                {
//
//                }
//                else
//                {
                     preMedia.setVisibility(View.VISIBLE);
                     nextMedia.setVisibility(View.VISIBLE);
                    stopMedia.setVisibility(View.VISIBLE);
              //  }
				if(videoListPanel.getVisibility() == View.GONE)
				{
					videoListPanel.setVisibility(View.VISIBLE);
				}
				else if(videoListPanel.getVisibility() == View.VISIBLE)
				{
					videoListPanel.setVisibility(View.GONE);
				}
			}
        	
        });
        preMedia = (Button)findViewById(R.id.pre_music_btn);
        preMedia.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				frontMedia(mdeiaPlayerForCotrollBar);
			}
        	
        });
        nextMedia = (Button)findViewById(R.id.next_music_btn);
        nextMedia.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nextMedia(mdeiaPlayerForCotrollBar);
				
			}
			
        });
        volumeDec = (Button)findViewById(R.id.volume_dec);
        volumeDec.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,    
					                            AudioManager.FX_FOCUS_NAVIGATION_UP);    

				/*currentVolume--;
			  
				if(currentVolume>0)
				{
					currentVolume--;
				}
				else if(currentVolume<=0)
				{
					currentVolume = 0;
					speakerMute.setVisibility(View.VISIbBLE);
					speakerOn.setVisibility(View.GONE);
				}
				//float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
				//mdeiaPlayerForCotrollBar.setVolume(1-log1,1-log1);
				 mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0); 
				*/
			}
        	
        });
        volumeAdd = (Button)findViewById(R.id.volume_add_btn);
        volumeAdd.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*if(currVolume<50)
				{
					currVolume++;
					speakerMute.setVisibility(View.GONE);
					speakerOn.setVisibility(View.VISIBLE);
                    float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
                    mdeiaPlayerForCotrollBar.setVolume(1-log1,1-log1);
					
				}
				else if(currVolume>=50)
				{
					currVolume = 50;
 
				}
				
				float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
				mdeiaPlayerForCotrollBar.setVolume(1-log1,1-log1);*/
				 mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,    
				                            AudioManager.FX_FOCUS_NAVIGATION_UP);    

			}
        	
        });
        speakerMute = (Button)findViewById(R.id.music_mute);
        speakerMute.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
               /* float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
                mdeiaPlayerForCotrollBar.setVolume(1-log1,1-log1);*/
				speakerMute.setVisibility(View.VISIBLE);
				speakerOn.setVisibility(View.GONE);
				mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
			}
        	
        });
        speakerOn = (Button)findViewById(R.id.music_on);
        speakerOn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v("speak on","speak on");
               /* mdeiaPlayerForCotrollBar.setVolume(0, 0);*/

				speakerMute.setVisibility(View.GONE);
				speakerOn.setVisibility(View.VISIBLE);
				mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
			}
        	
        });
        controlBarForMediaPlayer = (FrameLayout)findViewById(R.id.control_bar_for_media);
        controlBarForMediaPlayer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            	int delta;
				if(realTimeRunningStatus.getTop()<390)
				{
					delta = 230;
					moveFLForRealTimeStatusPanel.topMargin = 410; 
					
				}
				else
				{
					delta = -230;
					moveFLForRealTimeStatusPanel.topMargin = 180; 
				}

				realTimeRunningStatus.setTP(moveFLForRealTimeStatusPanel);  
		        TranslateAnimation ni = new TranslateAnimation(0, 0, 0, delta);  
		        ni.setDuration(ANIMATION_DURATION);
		        realPanelAnimationIng = true;
		        realTimeRunningStatus.startAnimation(ni);
               // gklmediaListView.setVisibility(View.VISIBLE);
            }
        });
        
       
        initMedia();
        initParameterInRealPanel();

//        if(BlueToothMsgService.connectStatus == true)
//        {
//            Log.v("buletooth status update  true","buletooth status update");
//            upDateConnectStation( ConstantValue.BLUETOOTH_STATE_CONNECTION);
//        }
//        else
//        {
//            Log.v("buletooth status update  false","buletooth status update");
//            upDateConnectStation(ConstantValue.BLUETOOTH_STATE_DISCONNECTION);
//        }
       // mediaPlayersPause();
        ModeSelect.stopTimer();
        initTimer();
        pauseOrStop = true;
        runningNowHandle.obtainMessage(ConstantValue.START_RUN).sendToTarget();
//		 Intent intent = new Intent(RunningNow.this, CountDownForPlayer.class);
//	     intent.putExtra("count_time",5);
//	     Log.v("click calorie mode","click calorie mode");
//		 startActivityForResult(intent,5);
		 
		  
    }
    

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	private boolean surfaceFirstTimeCreat = true;
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.v("surface creat before "+ cantPlayLocalMediaFile+" "+ cantPlayRelaxMediaFile,"surface creat");
//		if( currentMode ==  ConstantValue.ARCADE_MODE && cantPlayLocalMediaFile)
//			return;
//		else if(currentMode !=  ConstantValue.ARCADE_MODE  && cantPlayRelaxMediaFile)
//			return;
//		   {
//			    
//			   Log.v("surface creat","surface creat");
		vedioMediaPlayer.setDisplay(surfaceHolder);// ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ä»
		
		if(!surfaceFirstTimeCreat&&!pauseOrStop)
		{
			Log.v("direct play in surface creat ","direct play in surface creat ");
			   mediaPlayersGoOn();
		}
		
		surfaceFirstTimeCreat = false;
//			   
//			   {
//			        vedioMediaPlayer.reset();// ï¿½Ö¸ï¿½ï¿½ï¿½Î´ï¿½ï¿½Ê¼ï¿½ï¿½ï¿½ï¿½×´Ì¬
//		   			   
//		        	//String path = Envronment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)+"/007.mp4";
//	
//		        	vedioMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
//		        	    @Override
//		        	    public void onPrepared(MediaPlayer mp) {
//		        	        // Do something. For example: playButton.setEnabled(true);
//		        	    	
//		        	    	//vedioMediaPlayer.start();
//		        	    }
//		        	});        
//		        	try {
//						vedioMediaPlayer.setDataSource(currentMediaPath);
//				      	//mediaPlayer = MediaPlayer.create(RunningNow.this, R.raw.test);// ï¿½ï¿½È¡ï¿½ï¿½Æµ
//						vedioMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//			           	vedioMediaPlayer.setDisplay(surfaceHolder);// ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ä»
//			           	
//			           	vedioMediaPlayer.prepare();
//			           	vedioMediaPlayer.setLooping(true);
//						} catch (IllegalArgumentException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (SecurityException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (IllegalStateException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//		        	
//		        	//playMedia(vedioMediaPlayer);
//			 	}
			   
//			   else
//			   {
//				   vedioMediaPlayer.setDisplay(surfaceHolder);// ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ä»
//				   mediaPlayersGoOn();
//			   }
//		   }
		   
		   
		 //  BlueToothMsgService.setCallBack(new callback());
		   
//		   
//		   String musicPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)+"/Vincent.mp3";
//
//		   final SoundPool mSoundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,0);  
//			//ï¿½ï¿½È¡ï¿½ï¿½Ð§  
//			final int mSound_0 = mSoundPool.load(musicPath,1); 
//			mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener(){
//	            public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
//	               mSoundPool.play(mSound_0, 1, 1, 0, 0, 1); 
//	            }});s
//	        
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
	public class CallBackForRealPanel 
	{
		public void func()
		{
			Log.v("call back for real panel","call back for real panel");
			realPanelAnimationIng = false;
		}
	}
	public class CallBackForSpeedIcon 
	{
		public void func()
		{
			if(moveFLForSpeedIcon.leftMargin == -80)
			{
				moveFLForSpeedSelectPanel.leftMargin = 0;  
				speedSelectPanel.setTP(moveFLForSpeedSelectPanel);  
		        TranslateAnimation ni = new TranslateAnimation(-160, 0, 0, 0);  
		        ni.setDuration(ANIMATION_DURATION);
		        speedSelectPanel.startAnimation(ni);
			}
		}
	}
	
	public class CallBackForHeightIcon 
	{
		public void func()
		{
			if(moveFLForHeightIcon.leftMargin == 800)
			{
				heightSelectIcon.setVisibility(View.GONE);
				heightSelectPanel.setVisibility(View.VISIBLE);
				moveFLForHeightSelectPanel.leftMargin = 650;  
				heightSelectPanel.setTP(moveFLForHeightSelectPanel);  
		        TranslateAnimation ni = new TranslateAnimation(150,0, 0, 0);  
		        ni.setDuration(ANIMATION_DURATION);
		        heightSelectPanel.startAnimation(ni);
			}
		}
	}
	
	public class CallBackForSpeedSelectPanel
	{
		public void func()
		{     		
			if(moveFLForSpeedSelectPanel.leftMargin == -160)
			{
				moveFLForSpeedIcon.leftMargin = 0;  
				speedSelectIcon.setTP(moveFLForSpeedIcon);  
		        TranslateAnimation ani = new TranslateAnimation(-80,0, 0, 0);  
		        ani.setDuration(ANIMATION_DURATION);
		        speedSelectIcon.startAnimation(ani);
			}
		}
	}
	public class CallBackForHeightSelectPanel
	{
		public void func()
		{     		
			if(moveFLForHeightSelectPanel.leftMargin == 800)
			{
				heightSelectIcon.setVisibility(View.VISIBLE);
				heightSelectPanel.setVisibility(View.GONE);
				moveFLForHeightIcon.leftMargin = 730;  
				heightSelectIcon.setTP(moveFLForHeightIcon);  
		        TranslateAnimation ani = new TranslateAnimation(70,0, 0, 0);  
		        ani.setDuration(ANIMATION_DURATION);
		        heightSelectIcon.startAnimation(ani);
			}
		}
	}
	@Override
	public void onBackPressed()  {
		//super.onBackPressed();
		pauseOperation();
		
        //closeActivity();
	}

/*	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v("stop play enter "+requestCode+" "+resultCode,"stop play");
		switch(requestCode)
		{
		case 0:
		Log.v("stop play1 "+requestCode+" "+resultCode,"stop play");
			switch (resultCode) 
			{ //resultCodeÎªï¿½Ø´ï¿½ï¿½Ä±ï¿½Ç£ï¿½ï¿½ï¿½ï¿½ï¿½Bï¿½Ð»Ø´ï¿½ï¿½ï¿½ï¿½ï¿½RESULT_OK
			
			case RESULT_OK:
				Bundle b=data.getExtras();  //dataÎªBï¿½Ð»Ø´ï¿½ï¿½ï¿½Intent
				int choice = b.getInt("stop_play_choice");
				Log.v("stop play2","stop play");
				if(choice == ConstantValue.GO_ON_PLAY)
				{
					isEndStatus = false;
//					Log.v("stop play3 ","stop play");
//					mediaPlayersGoOn();
//					pauseOperationFlag = false;
//                    Intent intent = new Intent(RunningNow.this, CountDownForPlayer.class);
//                   // intent.putExtra("count_time",3);
//                    Log.v("click calorie mode","click calorie mode");
//                    startActivityForResult(intent,5);
					pauseOrStop = true;
				}
				else if(choice == ConstantValue.BACK_TO_MAIN)
				{
					closeMediaPlayer();
					closeActivity();
										
				}
				break;
				default:
					break; 
			}
			break;
		case 5:
//			fiveSecondCountIsFinish = true;
//			countDownStartRunningFirst = 0;
//			pauseOrStop = false;
//			mediaPlayersGoOn();
//			
			Log.v("back running","back running");
					
			break;
			default:
				break;
		}
	}
*/	
	public void closeMediaPlayer()
	{
		if(currentMode == ConstantValue.ARCADE_MODE)
		{
			//mdeiaPlayerForCotrollBar.stop();
			if(mdeiaPlayerForCotrollBar !=null)
			{
				mdeiaPlayerForCotrollBar.release();
				mdeiaPlayerForCotrollBar = null;
			}
		}
		else
		{
			//vedioMediaPlayer.stop();
			if(vedioMediaPlayer!=null)
			{
				vedioMediaPlayer.release();
				vedioMediaPlayer  = null;
			}
			//mdeiaPlayerForCotrollBar.stop();
			if(mdeiaPlayerForCotrollBar !=null)
			{
				mdeiaPlayerForCotrollBar.release();
				mdeiaPlayerForCotrollBar = null;
			}
		}
	}
	class MediaInfo{
		String name;
		String path;
	}
	
	void initMediaInfoList(int mode)
	{
		String[] videoColumns = new String[]{
				MediaStore.Video.Media.DATA,
				MediaStore.Video.Media._ID,
				MediaStore.Video.Media.TITLE,
		};
		
		String[] musicColumns = new String[]{
	               MediaStore.Audio.Media.TITLE,
	                 MediaStore.Audio.Media.DATA,
	                 MediaStore.Audio.Media.DISPLAY_NAME,
		};
		Cursor cursor = null;
		if(mode == ConstantValue.RELAXATION_MODE || mode == ConstantValue.EXPERT_MODE)
		{
			cursor = this.getContentResolver().query(MediaStore.Audio.Media. EXTERNAL_CONTENT_URI, videoColumns, null, null, null);
			
			if(cursor.moveToFirst()){
				do{
					MediaInfo info = new MediaInfo();
					
					info.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
					info.name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
					medialist.add(info);
					
				}while(cursor.moveToNext());
			}
		

		}
		else if(mode == ConstantValue.ARCADE_MODE)
		{
			cursor = this.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, musicColumns, null, null, null);
			if(cursor.moveToFirst()){
				do{
					MediaInfo info = new MediaInfo();
					
						info.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
						
						info.name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
						
						Log.v("arcade mode movie list path "+ info.path,"arcade mode movie list path "+ info.path);
						if(!info.path.contains("987654321"))
						{	
							medialist.add(info);
						}
					
				}while(cursor.moveToNext());
			}
			
		}
        cursor.close();
		
	}
	
	void initMediaListView()
	{
//		String[] data = new String[medialist.size()];
//		for(int i = 0;i< medialist.size();i++)
//			data[i] = medialist.get(i).name;
//       	
//		mediaListView.setAdapter(new ArrayAdapter<String>(this, 
//			                android.R.layout.simple_list_item_1, data)); 
		
	  mediaListView = (ListView)findViewById(R.id.media_list);	
      ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
      for(int i=0;i<medialist.size();i++)  
      {  
          HashMap<String, Object> map = new HashMap<String, Object>();  
          map.put("mediaName", medialist.get(i).name);
          map.put("actionIcon", null);//Í¼ï¿½ï¿½ï¿½ï¿½Ô´ï¿½ï¿½ID   
          listItem.add(map);  
      }  
      SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,
          R.layout.media_list_in_running_panel,     
         new String[] {"mediaName","actionIcon"},   
          new int[] {R.id.media_name_in_running_now,R.id.image_in_running_now});           
      
      mediaListView.setAdapter(listItemAdapter);    
      //ï¿½ï¿½Óµï¿½ï¿½  
      mediaListView.setOnItemClickListener(new OnItemClickListener() {  
          @Override  
          public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                  long arg3) {  
        	  curentMediaPosition = arg2;
        	  videoListPanel.setVisibility(View.GONE);
        	  playMedia(mdeiaPlayerForCotrollBar);
        	  
          }  
      });   
	}
	private int currentPosWhenPlay = 0;
	
	private void playSceneMedia(final MediaPlayer mMediaPlayer)  
    {  
		if(cantPlayRelaxMediaFile && currentMode !=  ConstantValue.ARCADE_MODE)
			return;
		 try  
		 {  
	     /* ï¿½ï¿½ï¿½ï¿½MediaPlayer */  
	         mMediaPlayer.reset();  
	      /* ï¿½ï¿½ï¿½ï¿½Òªï¿½ï¿½ï¿½Åµï¿½ï¿½Ä¼ï¿½ï¿½ï¿½Â·ï¿½ï¿½ */  
	         
	        mMediaPlayer.setDataSource(currentMediaPath);  
	       
	         /* ×¼ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ */  
	        mMediaPlayer.prepare();  
	        mMediaPlayer.setLooping(true);
	     /* ï¿½ï¿½Ê¼ï¿½ï¿½ï¿½ï¿½ */  
	        Log.v("currentPosWhenPlay "+currentPosWhenPlay,"currentPosWhenPlay "+currentPosWhenPlay);
	        mMediaPlayer.seekTo(currentPosWhenPlay);
	        mMediaPlayer.start();  
	      	mMediaPlayer.setOnCompletionListener(new OnCompletionListener()   
	      	{  
	      		public void onCompletion(MediaPlayer arg0)  
	      		{  
	      			currentPosWhenPlay = 0;
	               //ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ò»ï¿½ï¿½Ö®ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ò»ï¿½ï¿½  
	      			playSceneMedia(mMediaPlayer);  
	              }  
	            });  
	        }catch (IOException e){}  
	   }  
	 private void playMedia(final MediaPlayer mMediaPlayer)  
    {  
		 try  
		 {  
	     /* ï¿½ï¿½ï¿½ï¿½MediaPlayer */  
			 if(cantPlayLocalMediaFile)
			 {
				 new AlertDialog.Builder(RunningNow.this).setTitle("´íÎó").setMessage("Ã»ÓÐ±¾µØÊÓÆµÎÄ¼þ").setPositiveButton("È·¶¨", null).show(); 
				 return;
			 }
	         mMediaPlayer.reset();  
	      /* ï¿½ï¿½ï¿½ï¿½Òªï¿½ï¿½ï¿½Åµï¿½ï¿½Ä¼ï¿½ï¿½ï¿½Â·ï¿½ï¿½ */  
	         currentPlayMediaName.setText(medialist.get(curentMediaPosition).name);
	        mMediaPlayer.setDataSource(medialist.get(curentMediaPosition).path);  
	       
	         /* ×¼ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ */  
	        mMediaPlayer.prepare();  
	        mMediaPlayer.setLooping(true);
	     /* ï¿½ï¿½Ê¼ï¿½ï¿½ï¿½ï¿½ */  
	        Log.v("currentPosWhenPlay "+currentPosWhenPlay,"currentPosWhenPlay "+currentPosWhenPlay);
	        mMediaPlayer.seekTo(currentPosWhenPlay);
	        mMediaPlayer.start();  
	      	mMediaPlayer.setOnCompletionListener(new OnCompletionListener()   
	      	{  
	      		public void onCompletion(MediaPlayer arg0)  
	      		{  
	               //ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ò»ï¿½ï¿½Ö®ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ò»ï¿½ï¿½  
	      			currentPosWhenPlay = 0;
	      			nextMedia(mMediaPlayer);  
	              }  
	            });  
	        }catch (IOException e){}  
	   }  
	 
	    /* ï¿½ï¿½Ò»ï¿½ï¿½ */  
	  private void nextMedia(MediaPlayer mMediaPlayer)  
	   {  
		  if (++curentMediaPosition >= medialist.size())  
		  {  
			  curentMediaPosition = 0;  
		  }  
 
		  playMedia(mMediaPlayer);   
	    }  
	      
	     /* ï¿½ï¿½Ò»ï¿½ï¿½ */  
	    private void frontMedia(MediaPlayer mMediaPlayer )  
	    {  
	       if (--curentMediaPosition < 0)  
	        {  
	    	   curentMediaPosition = medialist.size()-1;  
	        }  
	 
	       playMedia(mMediaPlayer);    
	    }


	  void closeActivity()
	  {
		  if(ModeSelect.securityLockMissForSendStatus = false)
			  ModeSelect.mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 57, -1).sendToTarget();
          Log.v("close Activity","close Activity");
	    	Message msm1 = Message.obtain();
	    	msm1.what = ConstantValue.SEND_DATA_TO_MCU;

	    	isEndStatus = true;
	    	
    	    SendDataToMCU.senddataFunc((byte)0x40,(byte)ConstantValue.END_STATUS,(byte)0);
		    storefitnessInfoInDB();
		  closeMediaPlayer();
		  runningNowHandle = null;
		  cleanFloatIcon();
		  if(mTimerTask!=null)
			  mTimerTask.cancel();
		  if(mTimer!=null)
			  mTimer.cancel();
		  this.finish();
      }

	 protected void onResume()
	 {
		 super.onResume();
		 Log.v("runningnow onreume","runningnow onresume");
//		// Log.v("homeKeyPress "+ homeKeyPress,"homeKeyPress "+ homeKeyPress);
//		 if(!coverByOtherActivity)
//		 {	
//			 mediaPlayersGoOn();
//			// initTimer();
//			// mediaPlayersPause();
//			// Intent intent = new Intent(RunningNow.this, StopPlay.class);
//			/// startActivityForResult(intent,0);				
//		 }
//			 
		 //pauseOperation();
		 //MyFloatFrameLayout.handler.sendEmptyMessage(MyFloatFrameLayout.HIDE);
		/* wakeLock.acquire();
		 if(!firstTimePlay)
         {
            // mediaPlayersGoOn();
             firstTimePlay = false;
         }*/
	 }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("runningnow onpause","runningnow onpause");
       // activityPause = true;
       	mediaPlayersPause();
        
		
        //MyFloatFrameLayout.handler.sendEmptyMessage(MyFloatFrameLayout.SHOW);
       // wakeLock.release();
    }
    
    	void mediaPlayersPause()
		{
    		
			if(!cantPlayLocalMediaFile && currentMode == ConstantValue.ARCADE_MODE)
			{
                if(mdeiaPlayerForCotrollBar !=null&&mdeiaPlayerForCotrollBar.isPlaying())
                {
                	currentPosWhenPlay = mdeiaPlayerForCotrollBar.getCurrentPosition();
                	  Log.v("pause currentPosWhenPlay "+currentPosWhenPlay,"currentPosWhenPlay "+currentPosWhenPlay);
                	  
                	mdeiaPlayerForCotrollBar.pause();
                }
			}
            else
			{
                if(!cantPlayRelaxMediaFile&& vedioMediaPlayer !=null&&vedioMediaPlayer.isPlaying())
                {
                	currentPosWhenPlay = vedioMediaPlayer.getCurrentPosition();
                	vedioMediaPlayer.pause();
                }
//                if(!cantPlayLocalMediaFile && mdeiaPlayerForCotrollBar !=null && mdeiaPlayerForCotrollBar.isPlaying())
//				mdeiaPlayerForCotrollBar.pause();
			}
		}
		void mediaPlayersGoOn()
		{
			
			
			if(!cantPlayLocalMediaFile && currentMode == ConstantValue.ARCADE_MODE)
			{
				if((mdeiaPlayerForCotrollBar!= null) && (!mdeiaPlayerForCotrollBar.isPlaying()))
				{
					playMedia(mdeiaPlayerForCotrollBar);
                //playMedia(vedioMediaPlayer);
					Log.v("play media","play media");
				}
			}
			else 
            {
				if(!cantPlayRelaxMediaFile && vedioMediaPlayer!=null && (!vedioMediaPlayer.isPlaying()))
				{
					playSceneMedia(vedioMediaPlayer);
					Log.v("play scene media","play scene media");
				}
					//vedioMediaPlayer.start();
//				if(!cantPlayLocalMediaFile && mdeiaPlayerForCotrollBar!=null &&(!mdeiaPlayerForCotrollBar.isPlaying()))
//				{
//					playMedia(musicMmediaPlayer);
//					Log.v("play music media","play music media");
//				}
					//mdeiaPlayerForCotrollBar.start();
                
			}
		}
		
		private  long timeSegmentInRelaxMode;
		private int programModeIndex = 0;

		
		private float[] mountainProgramSpeed = new float[]{30,70,90,40,70,100,40,70,110,50,70,120,40,70,120,60};
		private byte[] mountainProgramIncline = new byte[]{0,2,3,4,5,5,7,7,4,4,8,4,6,3,2,0};
		
		private float[] seaProgramSpeed = new float[]{20,30,50,50,70,70,80,50,50,50,30,30,80,80,40,30};
		private byte[] seaProgramIncline = new byte[]{0,2,3,4,5,5,7,7,4,4,4,4,6,3,2,0};
		
		private float[] plainProgramSpeed = new float[]{20,30,50,50,70,120,80,50,90,50,30,30,80,60,40,30};
		private byte[] plainProgramIncline = new byte[]{0,2,3,4,5,5,7,7,4,4,8,4,6,3,2,0};
		
		private float[] cityProgramSpeed = new float[]{10,30,30,70,70,50,50,50,70,70,50,50,50,50,30,20};
		private byte[] cityProgramIncline = new byte[]{0,2,6,6,8,8,6,6,4,4,4,6,6,2,2,0};
		
		private float[] grassProgramSpeed = new float[]{20,30,30,60,50,50,80,50,50,60,80,80,80,50,40,30};
		private byte[] grassProgramIncline = new byte[]{0,3,9,9,9,2,2,8,8,4,1,1,4,4,4,0};
		private int programseg = 0;
		private int segIndex = 0;
		private int segSum = 0;
		public void initParameterInRealPanel()
		{
			if(currentMode == ConstantValue.RELAXATION_MODE)
			{
			    currentTaskValue= this.getIntent().getExtras().getInt("task_value");
		        currentPlace =  this.getIntent().getExtras().getInt("where",0);
			    currentTimeInRealPanel = (int)currentTaskValue*60;
			    timeSegmentInRelaxMode = currentTimeInRealPanel/16;
			    finalTime = currentTimeInRealPanel;
			    currentDistanceInRealPanel = 0;
			    currentCarorieInRealPanel = 0;	
			    
			    programseg = (int) (currentTaskValue*60/16);
			    
		 
			}
			else if(currentMode == ConstantValue.ARCADE_MODE)
	        {
        	    currentCarorieInRealPanel = 0;
        	    currentDistanceInRealPanel = 0;
        	    currentTimeInRealPanel = 0;
	        }
			else if(currentMode == ConstantValue.EXPERT_MODE)
			{
	        	currentTask = this.getIntent().getExtras().getInt("task");
	        	currentTaskValue = this.getIntent().getExtras().getInt("task_value");
	        	if(currentTask == ConstantValue.DISTANCE_MODE)
	        	{
	        		
	        	    currentCarorieInRealPanel = 0;
	        	    currentDistanceInRealPanel = currentTaskValue;
	        	    finalDistance = (float) currentDistanceInRealPanel;
	        	    currentTimeInRealPanel = 0;
	        	}
	        	else if(currentTask == ConstantValue.CALORIE_MODE)
	        	{
	        	    currentCarorieInRealPanel = currentTaskValue;
	        	    currentDistanceInRealPanel = 0;
	        	    currentTimeInRealPanel = 0;
	        	    finalCalorie = (float) currentCarorieInRealPanel;
	        	}
	        	else if(currentTask == ConstantValue.TIME_MODE)
	        	{
	        	    currentCarorieInRealPanel = 0;
	        	    currentDistanceInRealPanel = 0;
	        	    currentTimeInRealPanel = (int)currentTaskValue*60;
	        	    finalTime = currentTimeInRealPanel;
	        	}
	        	else if(currentTask == ConstantValue.HEART_RATE_MODE)
	        	{
	        	    currentCarorieInRealPanel = 0;
	        	    currentDistanceInRealPanel = 0;
	        	    currentTimeInRealPanel = 0;
	        	}
	        }
			timeForChartDraw = 0;
		    speedValueInRealPanel.setText(String.format("%.1f",currentSpeedValue));
		    inclineValueInRealPanel.setText(String.format(""+(int)currentInclineValue));
		    distanceValueInRealPanel.setText(String.format("%.2f",currentDistanceInRealPanel));
		    timeValueInRealPanel.setText((String.format("%02d",currentTimeInRealPanel/60))+":"+String.format("%02d",currentTimeInRealPanel%60));
		    if(currentCarorieInRealPanel <1000)
		    	calorieValueInRealPanel.setText(String.format("%.1f",currentCarorieInRealPanel));
		    else
		    	calorieValueInRealPanel.setText(String.format("%d",(int)currentCarorieInRealPanel));
		}


		public void initTimer()
		{
            if(mTimer!=null)
                mTimer.cancel();
			 mTimer = new Timer(true);
            if(mTimerTask!=null)
                mTimerTask.cancel();
			 mTimerTask = new MyTimerTask();  // ï¿½Â½ï¿½Ò»ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
			 mTimer.schedule(mTimerTask, 0, 1000);

			
		}

		class MyTimerTask extends TimerTask{
			  @Override
			  public void run() {
				  Message msg = Message.obtain();
				  msg.what = SECOND_TICK;
				  if(runningNowHandle!=null)
					  runningNowHandle.sendMessage(msg);
			  }
			     
		}
    public void pauseOperation()
    { 
    		pauseOrStop = true;
    		ModeSelect.mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 3, -1).sendToTarget();
			mediaPlayersPause();
	    	Intent intent = new Intent(RunningNow.this, StopPlay.class);
			startActivity(intent);

    }

    
    FloatFitnessDate floatfd = new FloatFitnessDate();
    byte[] serialSendData = new byte[6];
    private int intervalSendFlag = 0;
    private int maxArcadeModeTime = 60*100;
    public void secondTickUpdate()
		{
				timeForChartDraw++;
			   // TODO Auto-generated method stub
				  Log.v("time task","time task");
					if(currentMode == ConstantValue.RELAXATION_MODE)
					{						
						if(segSum==0)
						{							
							if(currentPlace == ConstantValue.GRASS_PLACE)
							{
								currentSpeedValue = grassProgramSpeed[segIndex]/10;
								currentInclineValue = grassProgramIncline[segIndex];
								
							}
							else if(currentPlace == ConstantValue.MOUNTAIN_PLACE)
							{
								currentSpeedValue = mountainProgramSpeed[segIndex]/10;
								currentInclineValue = mountainProgramIncline[segIndex];
								
							}
							else if(currentPlace == ConstantValue.PLAIN_PALCE)
							{
								currentSpeedValue = plainProgramSpeed[segIndex]/10;
								currentInclineValue = plainProgramIncline[segIndex];
								
							}
							else if(currentPlace == ConstantValue.SEA_PLACE)
							{
								currentSpeedValue = seaProgramSpeed[segIndex]/10;
								currentInclineValue = seaProgramIncline[segIndex];
								
							}
							else if(currentPlace == ConstantValue.CITY_PLACE)
							{
								currentSpeedValue = cityProgramSpeed[segIndex]/10;
								currentInclineValue = cityProgramIncline[segIndex];
								
							}
							
							if(currentSpeedValue==oldSpeedValue)
							{
								inclineSet(0);
							}
							else
							{
								inclineSet(2000);
							}
							speedSet(0);//put behind incline set
						}						
						segSum++;
						if(segSum>=programseg)
						{
							segIndex++;
						
							if(segIndex>=16)
								segIndex = 15;
							segSum = 0;
						}
						
						
					    currentTimeInRealPanel--;
					    currentDistanceInRealPanel = currentDistanceInRealPanel + currentSpeedValue/3600;
					    currentCarorieInRealPanel = (float) (currentCarorieInRealPanel + (currentSpeedValue/3.6)/((1+0.01*currentInclineValue)*33));
			    	    if(currentTimeInRealPanel <= 0)
			    	    	closeActivity();
					}
					else if(currentMode == ConstantValue.ARCADE_MODE)
			        {
					    currentTimeInRealPanel++;
					    currentDistanceInRealPanel = currentDistanceInRealPanel + currentSpeedValue/3600;
					    currentCarorieInRealPanel = (float) (currentCarorieInRealPanel + (currentSpeedValue/3.6)/((1+0.01*currentInclineValue)*33));
					    if(currentTimeInRealPanel>=maxArcadeModeTime)
					    	closeActivity();
			        }
					else if(currentMode == ConstantValue.EXPERT_MODE)
					{
				    	if(currentTask == ConstantValue.DISTANCE_MODE)
				    	{
				    		
						    currentTimeInRealPanel++;
						    currentDistanceInRealPanel = currentDistanceInRealPanel - currentSpeedValue/(float)3600;
						    currentCarorieInRealPanel = (float) (currentCarorieInRealPanel + (currentSpeedValue/(float)3.6)/((1+0.01*(float)currentInclineValue)*33));
				    	    if(currentDistanceInRealPanel <= 0)
				    	    	closeActivity();
				    	}
				    	else if(currentTask == ConstantValue.CALORIE_MODE)
				    	{
						    currentTimeInRealPanel++;
						    currentDistanceInRealPanel = currentDistanceInRealPanel + currentSpeedValue/(float)3600;
						    currentCarorieInRealPanel = (float) (currentCarorieInRealPanel - (currentSpeedValue/(float)3.6)/((1+0.01*(float)currentInclineValue)*33));

				    	    if(currentCarorieInRealPanel <= 0)
				    	    	closeActivity();
				    	}
				    	else if(currentTask == ConstantValue.TIME_MODE)
				    	{
						    currentTimeInRealPanel--;
						    currentDistanceInRealPanel = currentDistanceInRealPanel + currentSpeedValue/(float)3600;
						    currentCarorieInRealPanel = (float) (currentCarorieInRealPanel + (currentSpeedValue/(float)3.6)/((1+0.01*(float)currentInclineValue)*33));

				    	    if(currentTimeInRealPanel <= 0)
				    	    	closeActivity();
				    	}
				    	else if(currentTask == ConstantValue.HEART_RATE_MODE)
				    	{
						    currentTimeInRealPanel++;
						    currentDistanceInRealPanel = currentDistanceInRealPanel + currentSpeedValue/(float)3600;
						    currentCarorieInRealPanel =  (float) (currentCarorieInRealPanel + (currentSpeedValue/(float)3.6)/((1+0.01*(float)currentInclineValue)*33));
				    	}
					}
					
					floatfd.floatIconCalorieValue = currentCarorieInRealPanel;
					floatfd.floatIconDistanceValue = currentDistanceInRealPanel;
					floatfd.floatIconHeartbeatValue = heartbeatValue;
					floatfd.floatIconInclineValue = currentInclineValue;
					floatfd.floatIconSpeedValue = currentSpeedValue;
					floatfd.floatIconTimeValue = currentTimeInRealPanel;
					
					Message msgForFloat = Message.obtain();
					msgForFloat.what = MyFloatFrameLayout.UPDATE_REAL_FIT_DATE;
					msgForFloat.obj = floatfd;
					MyFloatFrameLayout.handler.sendMessage(msgForFloat);
					
					if(realPanelAnimationIng == false)
					{	
						Log.v("bluetooth send1","bluetooth send1");
						drawBitMapForSpeedInclineChart();
					    speedValueInRealPanel.setText(String.format("%.1f",currentSpeedValue));
					    inclineValueInRealPanel.setText(""+(int)currentInclineValue);
					    distanceValueInRealPanel.setText(String.format("%.2f",currentDistanceInRealPanel));
                        timeValueInRealPanel.setText((String.format("%02d",currentTimeInRealPanel/60))+":"+String.format("%02d",currentTimeInRealPanel%60));
                        if(currentCarorieInRealPanel<1000)
                        	calorieValueInRealPanel.setText(String.format("%.1f",currentCarorieInRealPanel));
                        else
                        	calorieValueInRealPanel.setText(String.format("%d",(int)currentCarorieInRealPanel));
				    	Message msm = Message.obtain();				    	
				    	msm.what = ConstantValue.SEND_DATA_TO_MCU;
				    	
				    	
					}
			  
		}
		
    	public void sendRunStatus()
    	{
    		SendDataToMCU.senddataFunc((byte)0x20, (byte) currentInclineValue, (byte) (currentSpeedValue*10));
	    	if((!ModeSelect.securityLockMissForSendStatus)&&(ModeSelect.errrorHappendForSendStatus==0)&&(!isEndStatus))
	    	{
	    		
	    		SendDataToMCU.senddataFunc((byte)0x40, (byte) ConstantValue.NORMAL_RUN_STATUS, (byte) 0);			            

	    	}
	    	if(isEndStatus)
	    	{
	    		SendDataToMCU.senddataFunc((byte)0x40, (byte) ConstantValue.END_STATUS, (byte) 0);			            				    		
	    	}
	    	
	    	if(ModeSelect.securityLockMissForSendStatus)
	  		{
	  			SendDataToMCU.senddataFunc((byte)0x40,(byte)ConstantValue.NOSAFE_STATUS,(byte)0);
	  		}
	  		else if(ModeSelect.errrorHappendForSendStatus>0)
	  		{
	  			SendDataToMCU.senddataFunc((byte)0x40,(byte)ModeSelect.errrorHappendForSendStatus,(byte)0);
	  		}
    	}
		


		private int timeForChartDraw =0;
		private float[] speedDataInChart = new float[630];
		private float[] InclineDataInChart  = new float[630];
		private Paint paintForChart = new Paint();
		private float intervalPixel= ((float)550)/300;
        private int newAim = 300;
		private int intervalTime = 1;
        private int indexTime = 0;
        private int indexInBufferForSpeed = 0;
         private int indexInBufferForIncline = 0;
        private float xAxieForChart = 0;
		public void drawBitMapForSpeedInclineChart()
		{

			if( timeForChartDraw == newAim)
            {
                xAxieForChart = xAxieForChart/2;
                canvas.drawColor(Color.TRANSPARENT);//white
                Log.v("time egdearriver ","a time edge");
                newAim = newAim*2;
                intervalTime = intervalTime*2;
                int t = speedDataInChart.length/2;

                for(int i = 2;i<t;i=i+2)
                {
                    speedDataInChart[i] = speedDataInChart[i*2]/2;
                    speedDataInChart[i+1] = speedDataInChart[i*2+1];
                    InclineDataInChart[i] = InclineDataInChart[i*2]/2;
                    InclineDataInChart[i+1] = InclineDataInChart[i*2+1];
                }
                indexInBufferForSpeed = indexInBufferForSpeed/2;
                if(indexInBufferForSpeed%2 !=0)
                    indexInBufferForSpeed = indexInBufferForSpeed - 1;
                indexInBufferForIncline = indexInBufferForIncline/2;
                if(indexInBufferForIncline%2 !=0)
                    indexInBufferForIncline = indexInBufferForIncline - 1;
            }
            if(++indexTime >= intervalTime)
            {
                Log.v("timeForChartDraw = "+timeForChartDraw,"timeForChartDraw = "+timeForChartDraw);
                indexTime = 0;

                xAxieForChart = xAxieForChart + intervalPixel;

                speedDataInChart[indexInBufferForSpeed++] = xAxieForChart;
                speedDataInChart[indexInBufferForSpeed++]=100-currentSpeedValue*3;
                Log.v("indexInBufferForSpeed = "+indexInBufferForSpeed,"indexInBufferForSpeed = "+indexInBufferForSpeed);
                paintForChart.setColor(Color.rgb(133,226,39));
                paintForChart.setStrokeWidth(1);
                paintForChart.setAntiAlias(true);
                for(int i = 0;i<indexInBufferForSpeed-3;i=i+2)
                {
                    canvas.drawLine(speedDataInChart[i],speedDataInChart[i+1],speedDataInChart[i+2],speedDataInChart[i+3],paintForChart);
                }

                InclineDataInChart[indexInBufferForIncline++] = xAxieForChart;
                InclineDataInChart[indexInBufferForIncline++]=100-currentInclineValue*3;

                paintForChart.setColor(Color.rgb(202,143,23));
                paintForChart.setStrokeWidth(1);
                for(int i = 0;i<indexInBufferForIncline-3;i=i+2)
                {
                    canvas.drawLine(InclineDataInChart[i],InclineDataInChart[i+1],InclineDataInChart[i+2],InclineDataInChart[i+3],paintForChart);
                }
                //canvas.drawLines(InclineDataInChart,0,indexInBufferForIncline,paintForChart);

                chartInrealPanel.setImageBitmap(speedInclineBt);
            }

		}
		
		public void initMedia()
		{
	        currentPlayMediaName = (TextView)findViewById(R.id.media_name_in_control_bar);
	        vedioMediaPlayer = new MediaPlayer();
		       
		        if(currentMode==ConstantValue.ARCADE_MODE )
		        {
		   		 if(cantPlayLocalMediaFile)
		   		 {
					 new AlertDialog.Builder(RunningNow.this).setTitle("´íÎó").setMessage("Ã»ÓÐ±¾µØÒôÆµÎÄ¼þ").setPositiveButton("È·¶¨", null).show(); 

					 return;
		   		 }
		        	mdeiaPlayerForCotrollBar = vedioMediaPlayer;
	        		curentMediaPosition = 0;
	        		
	        		currentMediaPath = medialist.get(curentMediaPosition).path;
	        		//if(currentMediaPath == null)
	        			//new AlertDialog.Builder(this).setTitle("´íÎó").setMessage("±¾»úÃ»ÓÐÊÓÆµÎÄ¼þ").setPositiveButton("È·¶¨", null).show(); 
	        		currentPlayMediaName.setText(medialist.get(curentMediaPosition).name);
	        		Log.v("movies path",currentMediaPath);
	        	    frMovieBtn.setVisibility(View.GONE);
	        	    ffMovieBtn.setVisibility(View.GONE);
                    movieListButton.setVisibility(View.VISIBLE);                   
	        	    selectMedia.setVisibility(View.GONE);
	        	    preMedia.setVisibility(View.GONE);
	        	    nextMedia.setVisibility(View.GONE);
	        	   
	      
		        }
		        else 
		        {
		      	    frMovieBtn.setVisibility(View.GONE);
	        	    ffMovieBtn.setVisibility(View.GONE);
                    movieListButton.setVisibility(View.GONE);
	        	    selectMedia.setVisibility(View.VISIBLE);
	        	    playMedia.setVisibility(View.GONE);
	        	    preMedia.setVisibility(View.GONE);
	        	    nextMedia.setVisibility(View.GONE);
		        	vedioMediaPlayer.setVolume(0, 0);
		 			   musicMmediaPlayer = new MediaPlayer();
		 			   mdeiaPlayerForCotrollBar = musicMmediaPlayer;
		 			   {
		 				  if(!cantPlayLocalMediaFile)
		 				{		 
			 				   musicMmediaPlayer.reset();// ï¿½Ö¸ï¿½ï¿½ï¿½Î´ï¿½ï¿½Ê¼ï¿½ï¿½ï¿½ï¿½×´Ì¬
			 		        	String path = medialist.get(curentMediaPosition).path;
			 		        	Log.v("movies path",path);
			 		        	musicMmediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			 		        	    @Override
			 		        	    public void onPrepared(MediaPlayer mp) {
			 		        	        // Do something. For example: playButton.setEnabled(true);
			 		        	    	//musicMmediaPlayer.pause();
			 		        	    }
			 		        	});
			 		            try {
			 		            	musicMmediaPlayer.setDataSource(path);
			 		            	//mediaPlayer = MediaPlayer.create(RunningNow.this, R.raw.test);// ï¿½ï¿½È¡ï¿½ï¿½Æµ
			 		            	musicMmediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			 		            	musicMmediaPlayer.prepare();
			 		            } catch (IllegalArgumentException e) {
			 		                // TODO Auto-generated catch block
			 		                e.printStackTrace();
			 		            } catch (IllegalStateException e) {
			 		                // TODO Auto-generated catch block
			 		                e.printStackTrace();
			 		            } catch (IOException e) {
			 		                // TODO Auto-generated catch block
			 		                e.printStackTrace();
			 		            }
							}
		 			   }
		 		   }
                 speakerMute.setVisibility(View.GONE);
                 speakerOn.setVisibility(View.VISIBLE);
                 playMedia.setVisibility(View.GONE);
                 stopMedia.setVisibility(View.GONE);

				//float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
				//mdeiaPlayerForCotrollBar.setVolume(1-log1,1-log1);
		        surfaceView = (SurfaceView) findViewById(R.id.play_video_view);
                surfaceView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        TranslateAnimation ni;
                        videoListPanel.setVisibility(View.GONE);
                        if(moveFLForSpeedSelectPanel.leftMargin !=-160)
                        {
                         moveFLForSpeedSelectPanel.leftMargin = -160;
                        speedSelectPanel.setTP(moveFLForSpeedSelectPanel);
                        ni = new TranslateAnimation(0, -160, 0, 0);
                        ni.setDuration(ANIMATION_DURATION);
                        speedSelectPanel.startAnimation(ni);
                        }
                        if(moveFLForHeightSelectPanel.leftMargin!=800)
                        {
                        moveFLForHeightSelectPanel.leftMargin = 800;
                        heightSelectPanel.setTP(moveFLForHeightSelectPanel);
                        ni = new TranslateAnimation(0, 150, 0, 0);
                        ni.setDuration(ANIMATION_DURATION);
                        heightSelectPanel.startAnimation(ni);
                        }

                        if(moveFLForRealTimeStatusPanel.topMargin == 298)
                        {
                            int  delta = 240;
                            moveFLForRealTimeStatusPanel.topMargin = 540;
                            realTimeRunningStatus.setTP(moveFLForRealTimeStatusPanel);
                            ni = new TranslateAnimation(0, 0, 0, delta);
                            ni.setDuration(ANIMATION_DURATION);
                            realPanelAnimationIng = true;
                            realTimeRunningStatus.startAnimation(ni);
                        }

                    }
                });
		        surfaceHolder = surfaceView.getHolder();
		        surfaceHolder.addCallback(this);
		        
		       
		}
		
		public Bitmap drawScaleValueOnSpeedScale()
		{
			//Bitmap bt=BitmapFactory.decodeResource(this.getResources(), R.drawable.speed_scale);
		    ///Bitmap b = Bitmap.createBitmap(bmp);
		    Bitmap bt = Bitmap.createBitmap(15, 546, Config.ARGB_8888);
            bt.eraseColor(Color.argb(0,0,0,0));
			Canvas cs = new Canvas();
            cs.setBitmap(bt);
            Paint p = new Paint();
            p.setAntiAlias(true);
            p.setTextSize(15);
			p.setColor(Color.WHITE);
			int y = 60;
			int i;
			for(i = 0;i < 12;i++)
			{
                if(i<5){
                    cs.drawText(String.valueOf(i*2+1), 5, y, p);
                }else{
                    cs.drawText(String.valueOf(i*2+1), 0, y, p);
                }
				y = y+37;
			}
			return bt;
				
		}
		public Bitmap drawScaleValueOnInclineScale()
		{
			Bitmap bt = Bitmap.createBitmap(15, 546, Config.ARGB_8888);
            bt.eraseColor(Color.argb(0,0,0,0));
			Canvas cs = new Canvas();
			cs.setBitmap(bt);
			Paint p = new Paint();
			p.setTextSize(15);
            p.setAntiAlias(true);
			p.setColor(Color.WHITE);
			int y = 60;
			int i;
			for(i = 0;i < 12;i++)
			{
				cs.drawText(String.valueOf(i), 0, y, p);
				y = y+37;
			}
			return bt;
				
		}
        void upDateHeartBeatStatus(int count)
        {
            if(count > 90)
            {
                Message msg = Message.obtain();
                msg.what = HEART_BEAT_DATA;
                msg.arg1 = count;
                runningNowHandle.sendMessageDelayed(msg,300);
               // heartBeatSpeedHint.setBackground(R.drawable);
            }
            else
            {

            }

            heartBeatCount.setText(""+count);

        }
		public void storefitnessInfoInDB()
		{
            Log.v("store fitness data", "store fitness data");
			DBForTreatmill dbScene = new DBForTreatmill(this);	
	   		 ContentValues cv = new ContentValues();
	   		 

	   		if(currentMode == ConstantValue.RELAXATION_MODE)
			{
                finalTime = currentTaskValue - currentTimeInRealPanel;
			    finalDistance = currentDistanceInRealPanel;
			    finalCalorie = (float) currentCarorieInRealPanel;
	    	
			}
			else if(currentMode == ConstantValue.ARCADE_MODE)
	        {
				   finalTime = currentTimeInRealPanel;
				   finalDistance = (float) currentDistanceInRealPanel;
				   finalCalorie = (float) currentCarorieInRealPanel;

	        }
			else if(currentMode == ConstantValue.EXPERT_MODE)
			{
		    	if(currentTask == ConstantValue.DISTANCE_MODE)
		    	{
		    		finalDistance = currentTaskValue - currentDistanceInRealPanel;
		    		finalTime = currentTimeInRealPanel;
					finalCalorie = currentCarorieInRealPanel;
		    	}
		    	else if(currentTask == ConstantValue.CALORIE_MODE)
		    	{
		    		   finalTime = currentTimeInRealPanel;
					    finalDistance = currentDistanceInRealPanel;
                        finalCalorie = currentTaskValue - currentCarorieInRealPanel;
		    	}
		    	else if(currentTask == ConstantValue.TIME_MODE)
		    	{
					    finalDistance =  currentDistanceInRealPanel;
					    finalCalorie =  currentCarorieInRealPanel;
                        finalTime = currentTaskValue*60 - currentTimeInRealPanel;
		    	}
		    	else if(currentTask == ConstantValue.HEART_RATE_MODE)
		    	{
		    		   finalTime = currentTimeInRealPanel;
					    finalDistance =  currentDistanceInRealPanel;
					    finalCalorie =  currentCarorieInRealPanel;
		    	}
			}
            Log.v("finalTime = "+finalTime+" finalDistance = "+finalDistance+"finalCalorie "+finalCalorie,
                    "finalTime = "+finalTime+" finalDistance = "+finalDistance+"finalCalorie "+finalCalorie);
//            finalTime = currentTimeInRealPanel;
//            finalDistance = (float) currentDistanceInRealPanel;
//            finalCalorie = (float) currentCarorieInRealPanel;
            cv.put(DBForTreatmill.USER_ID, currentUserID);
            if(currentMode == ConstantValue.EXPERT_MODE)
            {
                cv.put(DBForTreatmill.USER_FITNESS_MODE, currentTask);
            }
            else
            {
            	cv.put(DBForTreatmill.USER_FITNESS_MODE, currentMode);
            }
            cv.put(DBForTreatmill.DISTANCE, finalDistance);
	   		cv.put(DBForTreatmill.CALORIE, finalCalorie);
	   		cv.put(DBForTreatmill.TIME,finalTime);
	   		cv.put(DBForTreatmill.HEART_BEAT,85);

//            Calendar c = Calendar.getInstance();
//            cv.put(DBForTreatmill.YEAR,c.get(Calendar.YEAR)); // èŽ·å–å½“å‰å¹´ä»½
//            cv.put(DBForTreatmill.MONTH,c.get(Calendar.MONTH)); // èŽ·å–å½“å‰å¹´ä»½
//            cv.put(DBForTreatmill.WEEK,c.get(Calendar.WEEK_OF_YEAR)); // èŽ·å–å½“å‰å¹´ä»½
//            cv.put(DBForTreatmill.DAY,c.get(Calendar.DAY_OF_MONTH)); // èŽ·å–å½“å‰å¹´ä»½
//            cv.put(DBForTreatmill.HOUR,c.get(Calendar.HOUR_OF_DAY)); // èŽ·å–å½“å‰å¹´ä»½
//            cv.put(DBForTreatmill.MINUTES,c.get(Calendar.MINUTE)); // èŽ·å–å½“å‰å¹´ä»½
	   		dbScene.insert(DBForTreatmill.TABLE_NAME_USER_FITNESS, cv);
	   		
	   		
	   		SQLiteDatabase rdb = dbScene.getReadableDatabase();
	   		Cursor cr = dbScene.select(DBForTreatmill.TABLE_HISTORY_GOAL,null,DBForTreatmill.USER_ID+"=? ",new String[]{currentUserID},rdb);
	   		if(!cr.moveToLast())
	   			return;
	   		
	   		int historyId = cr.getInt(cr.getColumnIndex(DBForTreatmill.ID));
	   		int calorOrDis = cr.getInt(cr.getColumnIndex(DBForTreatmill.HISTORY_CALORIE_OR_DISTANCE));
	   		ContentValues cv2 = new ContentValues();
	   		if(calorOrDis == DBForTreatmill.HISTORY_CALORIE)
	   		{
	   			int calorieAleary = cr.getInt(cr.getColumnIndex(DBForTreatmill.HISTORY_GOAL_VALUE));
	   			int calorieGoal =  cr.getInt(cr.getColumnIndex(DBForTreatmill.HISTORY_GOAL_VALUE));
	   			calorieAleary = (int) (calorieAleary + finalCalorie);
	   			
	   			cv2.put(DBForTreatmill.HISTORY_GOAL_VALUE,calorieAleary);
	   		}
	   		else
	   		{
	   			int distanceAleary = cr.getInt(cr.getColumnIndex(DBForTreatmill.HISTORY_GOAL_VALUE));
	   			int distanceGoal =  cr.getInt(cr.getColumnIndex(DBForTreatmill.HISTORY_GOAL_VALUE));
	   			distanceAleary = (int) (distanceAleary + finalDistance);
	   			cv2.put(DBForTreatmill.HISTORY_GOAL_VALUE,distanceAleary);
	   		}
	   		
	   		
	   		
   			dbScene.update(DBForTreatmill.TABLE_HISTORY_GOAL, cv2, DBForTreatmill.ID+"=? ",new String[]{""+historyId});
	   		
		}
		
		
		private void show(){
			Rect frame = new Rect();  
			getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
			MyFloatFrameLayout.TOOL_BAR_HIGH = frame.top;  
			
			
			
			WindowManager.LayoutParams params = fvFloat.params;
			params.format=PixelFormat.RGBA_8888;
			params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
			params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE |LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
			
			params.width = 680;
			params.height = 70;//WindowManager.LayoutParams.WRAP_CONTENT;
			params.alpha = 80;
			
			params.gravity=Gravity.LEFT|Gravity.TOP;
		    //ÒÔÆÁÄ»×óÉÏ½ÇÎªÔ­µã£¬ÉèÖÃx¡¢y³õÊ¼Öµ
			params.x = 70;
			params.y = 300;
			//MyFloatFrameLayout tv = null;   
			
			if(fvFloat != null)	
			{
					wm.removeView(fvFloat); 
					fvFloat =null;
			}
			
			fvFloat = new MyFloatFrameLayout(RunningNow.this);		
			wm.addView(fvFloat, params);
		}
		
		public void cleanFloatIcon()
		{
			if(fvFloat != null)	
			{
				wm.removeView(fvFloat); 
				fvFloat =null;
				//wm.addView(fvFloat, params);
//				final ViewGroup parent=(ViewGroup)fvFloat.getParent();
//				if(parent!=null)
//				{
//					parent.removeView(fvFloat);
//				}
//				fvFloat = null;
			}
		}
		
	    @Override
	    protected void onDestroy() {
//	        Message msg = Message.obtain();
//	        msg.what = BlueToothMsgService.TURN_OFF_ALL;
//	        BlueToothMsgService.mHandler.sendMessage(msg);
	    	runningNowHandle = null;
	    	ModeSelect.initTimer();
	        super.onDestroy();
	    }
	    public void speedSet(int playWavDelay)
	    {
	    	int t;
	    	if(oldSpeedValue > currentSpeedValue)
	    	{
	    		Message s = Message.obtain();
	    		s.what = ConstantValue.PLAY_WAV_RING;
	    		s.arg1 = 8;
	    		ModeSelect.mHandler.sendMessageDelayed(s, playWavDelay);
		    	oldSpeedValue = currentSpeedValue;
	    	}	    		
	    	else if(oldSpeedValue < currentSpeedValue)
	    	{
	    		Message s = Message.obtain();
	    		s.what = ConstantValue.PLAY_WAV_RING;
	    		s.arg1 = 7;
	    		ModeSelect.mHandler.sendMessageDelayed(s, playWavDelay);
		    	oldSpeedValue = currentSpeedValue;
	    	}
	    	
	    	
	    	speedValueInSpeedIcon.setText(String.format("%.1f",currentSpeedValue));
		    moveFLForSpeedScalescroll.topMargin = (int) (36-(currentSpeedValue-1)*17.5);
	    	if(moveFLForSpeedScalescroll.topMargin > minSpeedPosInPanel /*32*/)
	    		moveFLForSpeedScalescroll.topMargin = minSpeedPosInPanel;
	    	if(moveFLForSpeedScalescroll.topMargin < maxSpeedPosInPanel/*-405*/)
	    		moveFLForSpeedScalescroll.topMargin = maxSpeedPosInPanel;
	    	moveFLForspeedcrollTexture.topMargin = (int) ((moveFLForSpeedScalescroll.topMargin-30)*1.5);
	    	speedValueInSpeedIcon.setText(String.format("%.1f",currentSpeedValue));
			speedValueTextInPanel.setText(String.format("%.1f",currentSpeedValue));
			speedScrollSetting.setLayoutParams(moveFLForSpeedScalescroll);
			//speedScaleValueInPanel.setLayoutParams(moveFLForSpeedScaleValuescroll);
			speedScrollTextureImage.setLayoutParams(moveFLForspeedcrollTexture);
			
	    }
	    public void inclineSet(int playWavDelay)
	    {
	    	
	    	if(oldInclineValue > currentInclineValue)
	    	{
	    		
	    		Message s = Message.obtain();
	    		s.what = ConstantValue.PLAY_WAV_RING;
	    		s.arg1 = 50;
	    		ModeSelect.mHandler.sendMessageDelayed(s, playWavDelay);
	    	}	    		
	    	else if(oldInclineValue < currentInclineValue)
	    	{
	    	
	    		Message s = Message.obtain();
	    		s.what = ConstantValue.PLAY_WAV_RING;
	    		s.arg1 = 51;
	    		ModeSelect.mHandler.sendMessageDelayed(s, playWavDelay);
	    	}
	    	
	    	
	    	oldInclineValue = currentInclineValue;
	    	
	    	heightValueInHeightIcon.setText(""+currentInclineValue);
        	
        	moveFLForHeightScalescroll.topMargin = (int) (28-currentInclineValue*17.5);
        	if(moveFLForHeightScalescroll.topMargin > 28)
        		moveFLForHeightScalescroll.topMargin = 28;
        	if(moveFLForHeightScalescroll.topMargin < maxInclinePosInPanel/*-410*/)
        		moveFLForHeightScalescroll.topMargin = maxInclinePosInPanel;
        	moveFLForHeightscrollTexture.topMargin = (int) ((moveFLForHeightScalescroll.topMargin-10)*1.5);
			heightValueTextInPanel.setText(""+(int)currentInclineValue);
			heightValueInHeightIcon.setText(""+(int)currentInclineValue);
			heightScrollSetting.setLayoutParams(moveFLForHeightScalescroll);
	        //heightScaleValueInPanel.setLayoutParams(moveFLForHeightScaleValuescroll);
	        heightScrollTextureImage.setLayoutParams(moveFLForHeightscrollTexture);
	    }
	    
	    public void cleanRunStatus()
	    {
     		currentCarorieInRealPanel = 0;
    		currentDistanceInRealPanel = 0;
    		currentTimeInRealPanel = 0;
    		currentSpeedValue = 0;
    		oldSpeedValue = 0;
    		currentInclineValue = 0;
    		oldInclineValue = 0;
    		heartbeatValue = 0;
    		MyFloatFrameLayout.handler.sendEmptyMessage(MyFloatFrameLayout.CLEAN_DATA);
    		speedSet(0);
    		inclineSet(2000);
		    speedValueInRealPanel.setText(String.format("%.1f",0.0));
		    inclineValueInRealPanel.setText(String.format(""+0));
		    distanceValueInRealPanel.setText(String.format("%.2f",0.0));
		    timeValueInRealPanel.setText((String.format("%02d",0))+":"+String.format("%02d",0));
		    calorieValueInRealPanel.setText(String.format("%.1f",0.0));
	    }
	    
}