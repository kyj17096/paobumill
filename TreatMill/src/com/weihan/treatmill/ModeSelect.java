package com.weihan.treatmill;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.weihan.treatmill.RunningNow.MyTimerTask;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Message;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
public class ModeSelect extends Activity implements OnClickListener,OnTouchListener,OnGestureListener,OnItemClickListener
{
    private MovableTabView moveImageView;  
    private FrameLayout.LayoutParams moveFL;

    int currentMode;
    Button myFitness,relaxation,expertMode,arcadeMode;
    GestureDetector mGestureDetector;
    Button userBtn;
    Button helpBtn;
    Button deleteRecordBtn;
    LayoutParams deleteRecordBtnFL;
    int deleteItemIndex = 0;
    private int intervalForStaticPixel = 60;

    Button calorieMode,timeMode,heartRateMode,distanceMode;
    Button localMoive;
    Button grassLandMode,mountainMode,plainMode,seaMode,cityMode;
    private MyFrameLayout myfitnessPanel,relaxModePanel,arcadeModePanel,expertModePanel;
    String currentLanguage;
    private int lastPosition = 0;
    private FrameLayout userPanel;
    Button editUserPanel;
    Button doneUserPanel;
    ListView userListView;
    private View currentView;
    private View lastView;
    public static Handler mHandler;
    private TextView userNameInTitle;
    private SoundPool mSoundPool;  
    private HashMap ringHashMap ;
    private HashMap ringHashMapen ;
    private HashMap ringHashMapzh ;
    Button selectModeStatisticBtn,selectDurationForStatisticBtn;
    TextView goalText,adviceText;
    MovableImgFortitnessStatistic modeStatisticImg;
    ImageView modeStatisticScaleImg;
    private int HEIGHT_OF_STATISTIC_IMG = 135;
    private int SECOND_TICK = 5;
    Canvas canvas;
    private boolean errorHanped = false;
   
    ImageView goalCupImag;
    FrameLayout myfitnessFL;
    ListView fitnessListView;
    float touch_move_last_position;
    ListView btnModeListViewInMyfitness, btnDurationListViewInMyfitness;
    FrameLayout btnStatisticModeButtonListFL,btnDurationButtonListFL;
    FrameLayout.LayoutParams relaxlParams;
    MovableTabView backgroundForClick;
    DBForTreatmill db;
    String[] userNameList;
    int[] userIdList;
    TextView displayFitnessInfo;
    ImageView hintForSelectFitnessData;
    FrameLayout statictisBackgroud,fitnessRrecordInfoistviewFL,goalForClick,adviceForClick;
    TextView goalForMessage;
    ImageView goalForImage;
    static String currentUserName;
    static String currentUserId;
    final int SET_INVISUAL = 1;
    PowerManager powerManager = null;
    PowerManager.WakeLock wakeLock = null;
    public static int serialFd;
    public static boolean securityLockMissForSendStatus = false;
    public static int  errrorHappendForSendStatus = 0;
    private ReceiveThread receiveThread;
    
    public static float maxIncline=15;
    public static float maxSpeed=18;
    public static float minSpeed=(float) 1.0;
    AudioManager mAudioManager;
    
    static Timer mTimer;
    static MyTimerTask mTimerTask;
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState)  
    {  
        super.onCreate(savedInstanceState);      
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        wm = (WindowManager)getApplicationContext().getSystemService(WINDOW_SERVICE);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE); 
    	currentUserName = storeDataInLocal.getConfig(this,storeDataInLocal.CURRENT_USER_NAME);
    	
    	currentUserId = storeDataInLocal.getConfig(this,storeDataInLocal.CURRENT_USER_ID);
        VideoList.initSceneVideoList(this);
        setContentView(R.layout.mode_select);
        powerManager = (PowerManager)ModeSelect.this.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock( PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "My Lock");

        mSoundPool=new SoundPool(2,AudioManager.STREAM_MUSIC,0); 
         
        
        initRingMap();
        
        Log.v("current language","current language  "+Locale.getDefault().getLanguage());
        currentLanguage = Locale.getDefault().getLanguage();
        if(currentLanguage.contains("zh"))
        	ringHashMap = ringHashMapzh;
        else
        	ringHashMap = ringHashMapen;
        
        serialFd = SerialDataTransmission.openSerial();
        receiveThread = new ReceiveThread(serialFd);
        receiveThread.start();
        initTimer();
        db = new DBForTreatmill(this);
        displayFitnessInfo = (TextView)findViewById(R.id.display_fitnees_data);
        hintForSelectFitnessData = (ImageView)findViewById(R.id.select_fitnees_data_hint);
        backgroundForClick = (MovableTabView)findViewById(R.id.bg_choice);
        backgroundForClick.setOnClickListener(this);
        statictisBackgroud =  (FrameLayout)findViewById(R.id.statictis_bg);
        statictisBackgroud.setOnClickListener(this);
        fitnessRrecordInfoistviewFL =  (FrameLayout)findViewById(R.id.fitness_record_info_listview_for_click);
        fitnessRrecordInfoistviewFL.setOnClickListener(this);
        goalForClick =  (FrameLayout)findViewById(R.id.goal_for_click);
        goalForMessage = (TextView)findViewById(R.id.goal_message_text);
        goalForImage = (ImageView)findViewById(R.id.cup_img);
        goalForClick.setOnClickListener(this);
        adviceForClick = (FrameLayout)findViewById(R.id.advice_for_click);
        goalForClick.setOnClickListener(this);
        selectModeStatisticBtn = (Button)findViewById(R.id.select_statistic_mode_btn);
        selectModeStatisticBtn.setOnClickListener(this);
        btnModeListViewInMyfitness = (ListView)findViewById(R.id.statistic_button_list);
        btnStatisticModeButtonListFL = (FrameLayout)findViewById(R.id.statistic_mode_button_list_fl);
       String[] buttonListName1 = new String[]{this.getResources().getString(R.string.distance),
    		   this.getResources().getString(R.string.calorie),this.getResources().getString(R.string.time)};
        ArrayList<HashMap<String, Object>> buttonList1 = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<buttonListName1.length;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("buttonListName",buttonListName1[i]);
            buttonList1.add(map);
        }
        SimpleAdapter listItemAdapter1 = new SimpleAdapter(this,buttonList1,
                R.layout.fitness_button_list_item,
                new String[] {"buttonListName"},
                new int[] {R.id.fitness_statistic_button});

        btnModeListViewInMyfitness.setAdapter(listItemAdapter1);
        
        btnModeListViewInMyfitness.setOnItemClickListener(this); 

        deleteRecordBtn = (Button)findViewById(R.id.delete_statistic_record);
        deleteRecordBtn.setOnClickListener(this);
        deleteRecordBtnFL = new LayoutParams(100,50);
        selectDurationForStatisticBtn = (Button)findViewById(R.id.select_duration_btn);
        selectDurationForStatisticBtn.setOnClickListener(this);
        btnDurationListViewInMyfitness = (ListView)findViewById(R.id.duration_button_list);
        btnDurationButtonListFL = (FrameLayout)findViewById(R.id.duration_button_list_fl);
        String[] buttonListName2 = new String[]{this.getResources().getString(R.string.all_statistic),
        		this.getResources().getString(R.string.this_month_statistic),
        		this.getResources().getString(R.string.this_week_statistic)};
        ArrayList<HashMap<String, Object>> buttonList2 = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<buttonListName2.length;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("buttonListName",buttonListName2[i]);
            buttonList2.add(map);
        }
        SimpleAdapter listItemAdapter2 = new SimpleAdapter(this,buttonList2,
                R.layout.fitness_button_list_item,
                new String[] {"buttonListName"},
                new int[] {R.id.fitness_statistic_button});

        btnDurationListViewInMyfitness.setAdapter(listItemAdapter2);
        btnDurationListViewInMyfitness.setOnItemClickListener(this); 

        fitnessListView = (ListView) findViewById(R.id.fitness_record_info_listview);
        fitnessListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteRecordBtnFL.topMargin = view.getTop() + 93;
                deleteRecordBtnFL.leftMargin = 450;
                deleteRecordBtn.setLayoutParams(new FrameLayout.LayoutParams(deleteRecordBtnFL));
                deleteRecordBtn.setVisibility(View.VISIBLE);

                deleteItemIndex = i;


                return false;
            }
        });

        goalText = (TextView)findViewById(R.id.goal_message_text);
        adviceText = (TextView)findViewById(R.id.advice_text);
        goalCupImag = (ImageView)findViewById(R.id.cup_img);
        myfitnessFL = (FrameLayout)findViewById(R.id.my_fitness_movable);


        modeStatisticScaleImg = (ImageView)findViewById(R.id.mode_statistic_map_scale_img);
        modeStatisticImg = (MovableImgFortitnessStatistic)findViewById(R.id.mode_statistic_map_img);
        modeStatisticImg.setOnTouchListener(this);
        
        moveFL = new FrameLayout.LayoutParams(800, 1024);  
        moveFL.leftMargin = -10 ;   
        currentMode = ConstantValue.MY_FITNESS_MODE; 
        currentView = myFitness;
        lastView = myFitness;
    
        lastPosition = 60; 
        
        
        
        moveImageView = (MovableTabView)findViewById(R.id.bg_choice);
        moveImageView.setCallBack(new CallBack());
        
        userNameInTitle = (TextView)findViewById(R.id.user_name_in_title);
        userBtn = (Button)findViewById(R.id.user_button);
        userBtn.setOnClickListener(this);
        helpBtn = (Button)findViewById(R.id.help);
        helpBtn.setOnClickListener(this);
        
        myFitness = (Button)findViewById(R.id.my_fitness);
        myFitness.setBackgroundResource(R.drawable.my_fitness_selected);
        myFitness.setOnClickListener(this);
        relaxation = (Button)findViewById(R.id.relaxation);
        relaxation.setOnClickListener(this);
        arcadeMode = (Button)findViewById(R.id.arcade_mode);
        arcadeMode.setOnClickListener(this);
        expertMode = (Button)findViewById(R.id.expert_mode);
        expertMode.setOnClickListener(this);
        
        myfitnessPanel = (MyFrameLayout)findViewById(R.id.my_fitness_movable);
        myfitnessPanel.setOnClickListener(this);
        myfitnessPanel.setCallBack(new callBackForMyFrameLayout());
        
        arcadeModePanel = (MyFrameLayout)findViewById(R.id.arcade_mode_btn_panel);
        arcadeModePanel.setCallBack(new callBackForMyFrameLayout());
        localMoive = (Button)findViewById(R.id.local_moive_mode);
        localMoive.setOnClickListener(this);
        
        expertModePanel = (MyFrameLayout)findViewById(R.id.expert_mode_btn_panel);
        expertModePanel.setCallBack(new callBackForMyFrameLayout());
        calorieMode= (Button)findViewById(R.id.calorie_mode);
        calorieMode.setOnClickListener(this);
        timeMode= (Button)findViewById(R.id.time_mode);
        timeMode.setOnClickListener(this);
        heartRateMode= (Button)findViewById(R.id.heart_rate_mode);
        heartRateMode.setOnClickListener(this);
        distanceMode= (Button)findViewById(R.id.distance_mode);
        distanceMode.setOnClickListener(this);
        
        relaxModePanel = (MyFrameLayout)findViewById(R.id.relax_mode_btn_panel);
        relaxModePanel.setCallBack(new callBackForMyFrameLayout());
        relaxModePanel.setOnTouchListener(this);
        relaxModePanel.setOnClickListener(this);
        
        relaxlParams = new FrameLayout.LayoutParams(750,800);
        relaxlParams.leftMargin = 0;
        relaxlParams.topMargin = 0;
        //mGestureDetector = new GestureDetector(this,this);  
        
        //relaxModePanel.onInterceptHoverEvent (MotionEvent event)
        //mGestureDetector.setIsLongpressEnabled(false);
        
        grassLandMode= (Button)findViewById(R.id.grassland_mode);
        grassLandMode.setOnClickListener(this);
        grassLandMode.setOnTouchListener(this);
        mountainMode= (Button)findViewById(R.id.mountain_mode);
        mountainMode.setOnClickListener(this);
        mountainMode.setOnTouchListener(this);
        plainMode= (Button)findViewById(R.id.plain_mode);
        plainMode.setOnClickListener(this);
        plainMode.setOnTouchListener(this);
        seaMode= (Button)findViewById(R.id.sea_mode);
        seaMode.setOnClickListener(this);
        seaMode.setOnTouchListener(this);
        cityMode= (Button)findViewById(R.id.city_mode);
        cityMode.setOnClickListener(this);
        cityMode.setOnTouchListener(this);

        
        userNameInTitle.setText(currentUserName);
        userPanel = (FrameLayout)findViewById(R.id.user_panel_id);

        editUserPanel = (Button)findViewById(R.id.edit_user_panel);
        editUserPanel.setOnClickListener(this);
        doneUserPanel = (Button)findViewById(R.id.done_user_panel);
        doneUserPanel.setOnClickListener(this);
        userListView = (ListView)findViewById(R.id.user_list);
        upDateUserInforList();
        getuserListView();
        userListView.setOnItemClickListener(this); 
        
        
        mHandler = new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case  SET_INVISUAL:
                        displayFitnessInfo.setVisibility(View.GONE);
                        hintForSelectFitnessData.setVisibility(View.GONE);
                        break;
                                           	
	       			case ConstantValue.VOL_PLUS:
	       			 mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,    
	                            AudioManager.FX_FOCUS_NAVIGATION_UP);    
	       				break;
	       			case  ConstantValue.VOL_MINS:
	       				mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,    
	                            AudioManager.FX_FOCUS_NAVIGATION_UP);  
	       				break;
	       			case  ConstantValue.VOL_MUTE:
	       				mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
	       				break;	
	       			case ConstantValue.MODE_SELECT_UI_UPDATE_ALL:
	       				updateStaticAll();
	       				Log.v("pop message","pop message");
	       				break;
//	       			case  ConstantValue.INCLINE_ADD:
//	       				ModeSelect.mHandler.sendEmptyMessage(ConstantValue.INCLINE_ADD);
//	       				break;			
//	       			case  ConstantValue.INCLINE_MINS:
//	       				ModeSelect.mHandler.sendEmptyMessage(ConstantValue.INCLINE_MINS);
//	       				break;	
//	       			case  ConstantValue.SPEED_ADD:
//	       				ModeSelect.mHandler.sendEmptyMessage(ConstantValue.SPEED_ADD);
//	       				break;		
//	       			case  ConstantValue.SPEED_MINS:
//	       				ModeSelect.mHandler.sendEmptyMessage(ConstantValue.SPEED_MINS);
//	       				break;	       			   	       			    				
//	       			case ConstantValue.SPEED_SET:       				
//	       				break;	
//	       			case ConstantValue.INCLINE_SET:    	       				
//    	       				break;	
    	       				  	       				
	       			case  ConstantValue.START_RUN:   	       				
    	       				break;
    	       		case  ConstantValue.STOP_RUN :    	       				
    	       				break;
    	       		case  ConstantValue.SECURITY_LOCK :	 
    	       			Log.v("current security lock is "+ msg.arg1,"dfjdl");
                    	if(msg.arg1 == 1)
                    	{	//new AlertDialog.Builder(RunningNow.this).setTitle("错误").setMessage("安全锁脱落").setPositiveButton(null, null).show(); 
                    		   securityLockMissForSendStatus = true;
                    		mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 30, -1).sendToTarget();
                    		SendDataToMCU.senddataFunc((byte)0x40,(byte)ConstantValue.NOSAFE_STATUS,(byte)0);
                    		securityDialog();
                    		Message msg1 = Message.obtain();
                			msg1.what = MyFloatSecurityDialog.ERROR_MESSAGE;
                			msg1.obj = ModeSelect.this.getResources().getString(R.string.safe_lock_is_off);                   			
                			MyFloatSecurityDialog.mHandler.sendMessage(msg1);
                			
                			if(RunningNow.runningNowHandle!=null)
                			{
                				RunningNow.runningNowHandle.sendEmptyMessage(RunningNow.FINISH_THIS_ACTIVITY);
                			}
                			
                			if(CountDownForPlayer.mHandler!=null)
                			{
                				CountDownForPlayer.mHandler.sendEmptyMessage(CountDownForPlayer.FINISH_THIS_ACTIVITY_WHEN_SECURITY);
                			}
                		
                    		
//                    		if(SecurityErrorLockDisconnectDialog.mHandler !=null && errorHanped == false)
//                        		return;
//                    		if(SecurityErrorLockDisconnectDialog.mHandler !=null && errorHanped == true)
//                    		{
//                    			errorHanped = false;
//                    			Message msg1 = Message.obtain();
//                    			msg1.what = SecurityErrorLockDisconnectDialog.SECURITY_LOCK_AFTER_ERROR;
//                    			msg1.obj = "安全锁脱落";                   			
//                    			SecurityErrorLockDisconnectDialog.mHandler.sendMessage(msg1);
//                    			return;
//                    		}
                    		
//                    		Intent intent2 = new Intent(ModeSelect.this, SecurityErrorLockDisconnectDialog.class);
//                    		intent2.putExtra("security_error_info", "安全锁脱落");
//                    		intent2.putExtra("current_app_status",0);
//                			startActivity(intent2);
//                			ModeSelect.this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
                			 getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                		        		WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                			
                    	}
                    	else
                    	{
                    		securityLockMissForSendStatus = false;
                    		
                    	    errrorHappendForSendStatus = 0;
                    	    
                    		Log.v("current security lock cancle "+ msg.arg1,"dfjdl "+SecurityErrorLockDisconnectDialog.mHandler);
                    		//if(SecurityErrorLockDisconnectDialog.mHandler !=null)
                    		//SecurityErrorLockDisconnectDialog.mHandler.sendEmptyMessage(SecurityErrorLockDisconnectDialog.FINISH_THIS_ACTIVITY);
                    		cleanFloatSecurityDialog();
                    		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    	}
                    	break; 
    	       					       		
           	
    	       		case ConstantValue.HEART_BEAT_AND_ERROR:
    	       			if(msg.arg1 > 0)
    	       			{
    	       				errorHanped = true;
    	       				mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, msg.arg1+30, -1).sendToTarget();	       				
                    	    errrorHappendForSendStatus = msg.arg1;
                    	    SendDataToMCU.senddataFunc((byte)0x40,(byte)errrorHappendForSendStatus,(byte)0);
                    	    securityDialog();
                    		Message msg1 = Message.obtain();
                			msg1.what = MyFloatSecurityDialog.ERROR_MESSAGE;
                			msg1.obj = ModeSelect.this.getResources().getString(R.string.error) + msg.arg1;                   			
                			MyFloatSecurityDialog.mHandler.sendMessage(msg1);
                			
                			
                			if(RunningNow.runningNowHandle!=null)
                			{
                				RunningNow.runningNowHandle.sendEmptyMessage(RunningNow.FINISH_THIS_ACTIVITY);
//                				Message m = Message.obtain();
//                				msg.what = RunningNow.FINISH_THIS_ACTIVITY;
//                				//msg.arg1 = msg.arg1;
//                				RunningNow.runningNowHandle.sendMessage(m);
                			}
                			
                			if(CountDownForPlayer.mHandler!=null)
                			{
                				CountDownForPlayer.mHandler.sendEmptyMessage(CountDownForPlayer.FINISH_THIS_ACTIVITY_WHEN_ERROR);
                			}
//                    		Intent intent2 = new Intent(ModeSelect.this, SecurityErrorLockDisconnectDialog.class);
//                    		intent2.putExtra("security_error_info", "错误"+msg.arg1);
//                    		intent2.putExtra("current_app_status", 0);
//                			startActivity(intent2);
//                			getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//            		        		WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                			
//                			ModeSelect.this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
        	           }      				
           				break;
           	
           
           			case ConstantValue.SEET_TREATMILL_MAX_VALUE:
           				maxIncline = (0x0ff&(Integer)msg.obj);
           				minSpeed = ((float)(0x0ff&msg.arg1))/10;
           				maxSpeed = ((float)(0x0ff&msg.arg2))/10;
           				Log.v("maxIncline "+maxIncline+ " minSpeed "+minSpeed+" maxspeed"+ maxSpeed, "logs");
           				break;
           			case ConstantValue.PLAY_WAV_RING:
           				
           		        mSoundPool.play((Integer) ringHashMap.get(msg.arg1), 1, 1, 0, 0, 1); 
           				break;
           			case ConstantValue.UPDATE_MY_FITNESS:
           		    	currentUserName = storeDataInLocal.getConfig(ModeSelect.this,storeDataInLocal.CURRENT_USER_NAME);
          		    	
           		    	currentUserId = storeDataInLocal.getConfig(ModeSelect.this,storeDataInLocal.CURRENT_USER_ID);
           				Log.v("currentUserName "+currentUserName + "currentUserId "+ currentUserId,
           						"currentUserName "+currentUserName + "currentUserId "+ currentUserId);
           		    	updateStaticAll();
           				
           				break;
                    default:
                        break;
                }
            }
        };
        initDrawForStatisticImg();
        updatelistview(3);
        drawStatisticImg(0);
        int soundId = mSoundPool.load(this, R.raw.a1, 1);
        mSoundPool.play(soundId, 1, 1, 0, 0, 1);
        StartActivity.mHandler.obtainMessage(1).sendToTarget();
        //mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 1, -1);
        //int soundId = mSoundPool.load(this, R.raw.a1, 1);
        //mSoundPool.play(soundId, 1, 1, 0, 0, 1); 
       // new AlertDialog.Builder(ModeSelect.this).setTitle("错误").setMessage("没有请求的视频文件").setPositiveButton("确定", null).show(); 
    	
        //testDB();
    }  
   
   void imageViewMove(View v)
   {
		v = currentView;
		Log.v("mode select","mode select");
		currentMode = v.getId();
       final int newPosition = v.getTop();   
       int delta =  newPosition - lastPosition;
       moveFL.topMargin = newPosition-435;  
       moveImageView.setTP(moveFL);  
       TranslateAnimation ani = new TranslateAnimation(0, 0, 0, delta);  
       ani.setDuration(300);  
       updateBackGroundBeforeMove();
       moveImageView.startAnimation(ani);
       lastPosition =newPosition;
       lastView = v;
   }

 

    public void updateBackGroundBeforeMove()
    {
       	switch(currentMode)
    	{
    	case R.id.my_fitness:
    		myFitness.setBackgroundResource(R.drawable.my_fitness_not_selected);
    		relaxation.setBackgroundResource(R.drawable.relax_mode_not_selected);
    		arcadeMode.setBackgroundResource(R.drawable.arcade_mode_not_selected);
    		expertMode.setBackgroundResource(R.drawable.expert_mode_not_selected);   
    		break;
    	case R.id.relaxation:
    		myFitness.setBackgroundResource(R.drawable.my_fitness_not_selected);
    		relaxation.setBackgroundResource(R.drawable.relax_mode_not_selected);
    		arcadeMode.setBackgroundResource(R.drawable.arcade_mode_not_selected);
    		expertMode.setBackgroundResource(R.drawable.expert_mode_not_selected);
    		
    		break;
    	case R.id.arcade_mode:
    		myFitness.setBackgroundResource(R.drawable.my_fitness_not_selected);
    		relaxation.setBackgroundResource(R.drawable.relax_mode_not_selected);
    		arcadeMode.setBackgroundResource(R.drawable.arcade_mode_not_selected);
    		expertMode.setBackgroundResource(R.drawable.expert_mode_not_selected);
    		
    		break;
    	case R.id.expert_mode:
    		myFitness.setBackgroundResource(R.drawable.my_fitness_not_selected);
    		relaxation.setBackgroundResource(R.drawable.relax_mode_not_selected);
    		arcadeMode.setBackgroundResource(R.drawable.arcade_mode_not_selected);
    		expertMode.setBackgroundResource(R.drawable.expert_mode_not_selected);
    	
    		break;
    	default:
    		break;
    	}

    }
    public void updateBackGroundAfterMove()
    {
       	switch(currentMode)
    	{
    	case R.id.my_fitness:
    		myFitness.setBackgroundResource(R.drawable.my_fitness_selected);    
    		break;
    	case R.id.relaxation:
    		relaxation.setBackgroundResource(R.drawable.relax_mode_selected);
    		
    		break;
    	case R.id.arcade_mode:
    		arcadeMode.setBackgroundResource(R.drawable.arcade_mode_selected);
    		
    		break;
    	case R.id.expert_mode:
    		expertMode.setBackgroundResource(R.drawable.expert_mode_selected);    	
    		break;
    		
    	default:
    		break;
    	}

    }
    

	public class CallBack 
	{
		public void func()
		{
	        updateBackGroundAfterMove();
	        arcadeModeButtonUpdate(currentView);
		}
	}
	
	public class callBackForMyFrameLayout
	{
		public void func()
		{
			View v = currentView;
			Log.v("mode select","mode select");
	    	currentMode = v.getId();
	        final int newPosition = v.getTop();   
	        int delta =  newPosition - lastPosition;
	        moveFL.topMargin = newPosition-390;  
	        moveImageView.setTP(moveFL);  
	        TranslateAnimation ani = new TranslateAnimation(0, 0, 0, delta);  
	        ani.setDuration(300);  
	        updateBackGroundBeforeMove();
	        moveImageView.startAnimation(ani);
	        lastPosition =newPosition;
	        lastView = v;
	       
		}
	}

	
	void arcadeModeButtonUpdate(View v)
	{
		if(v.getId() == R.id.expert_mode)
		{
			expertModePanel.setVisibility(View.VISIBLE);
	   		AlphaAnimation ani= new AlphaAnimation(0,1); 
    		ani.setDuration(300);
    		expertModePanel.startAnimation(ani); 
			
			myfitnessPanel.setVisibility(View.GONE);
			arcadeModePanel.setVisibility(View.GONE);
			relaxModePanel.setVisibility(View.GONE);
		}
		else if(v.getId() == R.id.my_fitness)
		{
			expertModePanel.setVisibility(View.GONE);
			myfitnessPanel.setVisibility(View.VISIBLE);
			arcadeModePanel.setVisibility(View.GONE);
			relaxModePanel.setVisibility(View.GONE);
	   		AlphaAnimation ani= new AlphaAnimation(0,1); 
    		ani.setDuration(300);
    		myfitnessPanel.startAnimation(ani); 
		}
		else if(v.getId() == R.id.relaxation)
		{
			expertModePanel.setVisibility(View.GONE);
			myfitnessPanel.setVisibility(View.GONE);
			arcadeModePanel.setVisibility(View.GONE);
			relaxModePanel.setVisibility(View.VISIBLE);
			
	   		AlphaAnimation ani= new AlphaAnimation(0,1); 
    		ani.setDuration(300);
    		relaxModePanel.startAnimation(ani); 
		}
		else if(v.getId() == R.id.arcade_mode)
		{
			expertModePanel.setVisibility(View.GONE);
			myfitnessPanel.setVisibility(View.GONE);
			arcadeModePanel.setVisibility(View.VISIBLE);
			relaxModePanel.setVisibility(View.GONE);
			
	   		AlphaAnimation ani= new AlphaAnimation(0,1); 
    		ani.setDuration(300);
    		arcadeModePanel.startAnimation(ani); 
		}	
	}
	
	protected void onResume() 
	{
		super.onResume();
		updateStaticAll();
        //wakeLock.acquire();
//	    calorieMode.setBackgroundResource(R.drawable.calorie_mode_not_selected);
//	    timeMode.setBackgroundResource(R.drawable.time_mode_not_selected);
//	    heartRateMode.setBackgroundResource(R.drawable.heart_rate_mode_not_selected);
//	    distanceMode.setBackgroundResource(R.drawable.distance_mode_not_selected);
//	    localMoive.setBackgroundResource(R.drawable.local_movie_not_selected);
//	    grassLandMode.setBackgroundResource(R.drawable.grassland_mode_not_selected);
//	    mountainMode.setBackgroundResource(R.drawable.mountain_land_mode_not_selected);
//	    plainMode.setBackgroundResource(R.drawable.plain_mode_not_selected);
//	    seaMode.setBackgroundResource(R.drawable.sea_mode_not_selected);
//	    cityMode.setBackgroundResource(R.drawable.city_mode_not_selected);
	}

    @Override
    protected void onPause() {
        super.onPause();
     
        //wakeLock.release();
    }

    // 閺囧瓨鏌妉istview


    public void testDB()
    {
        for(int i = 0;i<12;i++)
        {
            ContentValues cv = new ContentValues();
            cv.put(DBForTreatmill.USER_ID, "hello");
            cv.put(DBForTreatmill.DISTANCE, i*10+"m");
            cv.put(DBForTreatmill.CALORIE, i*100+"Ka");
            cv.put(DBForTreatmill.TIME,i*1000+"Min");
            cv.put(DBForTreatmill.HEART_BEAT,"85");
            cv.put(DBForTreatmill.USER_FITNESS_MODE,"Calorie");
            cv.put(DBForTreatmill.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            cv.put(DBForTreatmill.MONTH, Calendar.getInstance().get(Calendar.MONTH));
            cv.put(DBForTreatmill.YEAR, Calendar.getInstance().get(Calendar.YEAR));

            db.insert(DBForTreatmill.TABLE_NAME_USER_FITNESS, cv);
        }
    }


//    private String[] monthName = new String[]{"January","February","March","April","May","June","July","August","September",
//            "October","November","December"};

    void deleteItemInDb(int i)
    {
        Calendar c = Calendar.getInstance();
        Cursor cr = null;
        SQLiteDatabase rdb = db.getReadableDatabase();
        if(currentDuration == 0)//all
        {
            cr = db.select(DBForTreatmill.TABLE_NAME_USER_FITNESS,null,null,null,rdb);
        }
       /* else if(currentDuration == 1)//month
        {
            cr = db.select(DBForTreatmill.TABLE_NAME_USER_FITNESS,null,DBForTreatmill.MONTH+"=?",new String[]{""+c.get(Calendar.MONTH)},rdb);
        }
        else if(currentDuration == 2)//week
        {
            cr = db.select(DBForTreatmill.TABLE_NAME_USER_FITNESS,null,DBForTreatmill.WEEK+"=?",new String[]{""+c.get(Calendar.WEEK_OF_YEAR)},rdb);
        }
        else if(currentDuration == 3)//week//defalut
        {
            cr = db.select(DBForTreatmill.TABLE_NAME_USER_FITNESS,null,null,null,rdb);
        }*/
        cr.moveToPosition(i);
        String id = cr.getString(cr.getColumnIndex(DBForTreatmill.ID));
        db.delete(DBForTreatmill.TABLE_NAME_USER_FITNESS,DBForTreatmill.ID+"=?",new String[]{id});

        updateStaticAll();
        rdb.close();
    }
    private int currentDuration = 0 ;
    public void updatelistview(int duration) 
    {
        Calendar c = Calendar.getInstance();
        SQLiteDatabase rdb = db.getReadableDatabase();
        Cursor cr=null;
        Log.v("duration = "+duration,"duration = "+duration);
       // if(duration == 0)//all
        {
            cr = db.select(DBForTreatmill.TABLE_NAME_USER_FITNESS,null,DBForTreatmill.USER_ID+"=? ",new String[]{currentUserId},rdb);
        }
       /* else if(duration == 1)//month
        {
           cr = db.select(DBForTreatmill.TABLE_NAME_USER_FITNESS,null,DBForTreatmill.MONTH+"=? "+"AND " +DBForTreatmill.USER_ID+"=?",new String[]{""+c.get(Calendar.MONTH),currentUserId},rdb);
        }
        else if(duration == 2)//week
        {
            cr = db.select(DBForTreatmill.TABLE_NAME_USER_FITNESS,null,DBForTreatmill.WEEK+"=? "+"AND " +DBForTreatmill.USER_ID+"=?",new String[]{""+c.get(Calendar.WEEK_OF_YEAR),currentUserId},rdb);
        }
        else if(duration == 3)//week//defalut
        {
            cr = db.select(DBForTreatmill.TABLE_NAME_USER_FITNESS,null,DBForTreatmill.USER_ID+"=? ",new String[]{currentUserId},rdb);
        }
        */
        currentDuration = duration;

        Log.v("cursor size is = "+ cr.getCount(),"cursor size is = "+ cr.getCount());
       
        
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        if(cr.getCount()>0)
        {
	        int defalutQuality = 0;
	        cr.moveToFirst();
	        do{
	            Log.v("id in deb is = "+cr.getString(cr.getColumnIndex(DBForTreatmill.ID)),"id in deb is = "+cr.getString(cr.getColumnIndex(DBForTreatmill.ID)));
	            HashMap<String, Object> map = new HashMap<String, Object>();
	            String tempModeName = null;
	            int tempMode = cr.getInt(cr.getColumnIndex(DBForTreatmill.USER_FITNESS_MODE));
	            if( tempMode== ConstantValue.CALORIE_MODE)
	               tempModeName = this.getResources().getString(R.string.expert_mode)
	               +"/"+this.getResources().getString(R.string.calorie);
	            else if(tempMode== ConstantValue.DISTANCE_MODE)
	            	 tempModeName = this.getResources().getString(R.string.expert_mode)
	                 +"/"+this.getResources().getString(R.string.distance);
	            	
	            else if(tempMode== ConstantValue.TIME_MODE)
	            	 tempModeName = this.getResources().getString(R.string.expert_mode)
	                 +"/"+this.getResources().getString(R.string.time);
	            	
	            else if(tempMode== ConstantValue.HEART_RATE_MODE)
	            	 tempModeName = this.getResources().getString(R.string.expert_mode)
	                 +"/"+this.getResources().getString(R.string.heart_beat);
	               
	            else if(tempMode== ConstantValue.RELAXATION_MODE)
	            	 tempModeName = this.getResources().getString(R.string.relaxed_mode)+"/";
	            	//tempModeName = "Relaxation/";
	            else if(tempMode== ConstantValue.ARCADE_MODE)
	            	 tempModeName = this.getResources().getString(R.string.arcade_mode)+"/";
	                //tempModeName = "Arcade/";
	             map.put("mode",tempModeName);
	            Log.v("db mode is "+ tempMode,"db mode is "+ tempMode);
	             float d =  cr.getFloat(cr.getColumnIndex(DBForTreatmill.DISTANCE));
	
	                map.put("distance",String.format("%.2f",d)+"KM");
	            Log.v("distance get from db is "+cr.getFloat(cr.getColumnIndex(DBForTreatmill.DISTANCE)) ,
	                    "distance get from db is "+cr.getFloat(cr.getColumnIndex(DBForTreatmill.DISTANCE)));
	            float cc = cr.getFloat(cr.getColumnIndex(DBForTreatmill.CALORIE));
	
	            map.put("calorie",String.format("%.2f",cc/1000)+"KJ");
	
	            int t =  cr.getInt(cr.getColumnIndex(DBForTreatmill.TIME));
	            if(t<0)
	            	t = 0;
	            map.put("timeduration",String.format("%02d",(int)(t/3600))+":"+String.format("%02d",(int)(t/60))+":"+String.format("%02d",(t%3600)%60));
	
	            map.put("time",""+cr.getInt(cr.getColumnIndex(DBForTreatmill.ID))/*(cr.getInt(cr.getColumnIndex(DBForTreatmill.DAY))+1)+""+monthName[cr.getInt(cr.getColumnIndex(DBForTreatmill.MONTH))]+
	                    ""+cr.getInt(cr.getColumnIndex(DBForTreatmill.YEAR))*/);
	            list.add(map);
	            defalutQuality++;
	            if(duration == 3&&defalutQuality>6)
	                break;
	        }while(cr.moveToNext());

        }
        SimpleAdapter listItemAdapter2 = new SimpleAdapter(this,list,
                R.layout.fitness_record_list_view_item,
                new String[] {"mode","time","distance","calorie","timeduration"},
                new int[] { R.id.treat_mode,  R.id.treat_date,
                        R.id.treat_distance, R.id.treat_calorie, R.id.treat_time });

        fitnessListView.setAdapter(listItemAdapter2);


//       String[] ColumnNames = { DBForTreatmill.USER_FITNESS_MODE ,DBForTreatmill.TREAT_DATE, db.DISTANCE, db.CALORIE,db.TIME };
//       ListAdapter adapter = new MySimpleCursorAdapter(this,
//       R.layout.fitness_record_list_view_item, cr, ColumnNames, new int[] { R.id.treat_mode,  R.id.treat_date,
//    		   R.id.treat_distance, R.id.treat_calorie, R.id.treat_time });
//
//       fitnessListView.setAdapter(adapter);
        cr.close();
       rdb.close();
    }




    class ClsForStatisticDrawData
    {
        float value;
        String time;
        float realValue;
    }
    private Cursor crForStatisticDraw = null;
    private int currentModeWhenDraw=-1;
    private ClsForStatisticDrawData[] mClsForStatisticDrawData;
    private  FrameLayout.LayoutParams staticImagDisplayPositionFL;
    private Bitmap statisticBitMapC0;
    private Bitmap statisticBitMapC1;
    private Bitmap statisticBitMapC2;
    private int currentDataCount;
    private int currentImageLen;
    private int currentStatisticImgMovePosition;


    void initDrawForStatisticImg()
    {
        currentModeWhenDraw=-1;
        staticImagDisplayPositionFL = new FrameLayout.LayoutParams(580,HEIGHT_OF_STATISTIC_IMG);
        mClsForStatisticDrawData = null;

        statisticBitMapC0 = Bitmap.createBitmap(20,20,Config.ARGB_8888);
        statisticBitMapC0.eraseColor(Color.argb(0,0,0,0));
        Canvas c = new Canvas();
        c.setBitmap(statisticBitMapC0);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor( Color.rgb(0,0,128));
        c.drawCircle(7,7,7,p);
        statisticBitMapC1 = Bitmap.createBitmap(20,20,Config.ARGB_8888);
        c.setBitmap(statisticBitMapC1);
        p.setColor( Color.rgb(200,5,0));
        c.drawCircle(7,7,7,p);
        statisticBitMapC2 = Bitmap.createBitmap(20,20,Config.ARGB_8888);
        c.setBitmap(statisticBitMapC2);
        p.setColor( Color.rgb(23,112,16));
        c.drawCircle(7,7,7,p);


    }


    private boolean ifDisplayFitnessDataInText = false;
    public void displayFitnessDataInText(float x,float y,boolean donwOrUp)
    {
        int mode= (int)x%intervalForStaticPixel;
        int clickIndex;
        if(donwOrUp == true)//down
        {
            Log.v("click x = "+x,"click x = "+x);
            if(mode<=(intervalForStaticPixel/2))
            {
                clickIndex = (int)(x)/intervalForStaticPixel-2;//+2 is for spare zero 2
            }
            else
            {
                clickIndex = (int)(x)/intervalForStaticPixel+1-2;
            }
            if(clickIndex<0|| clickIndex>=currentDataCount)
                return;
            String displayValue = "";
            if((mClsForStatisticDrawData.length <= clickIndex) || (mClsForStatisticDrawData[clickIndex] == null))
            	return ;
            float f = mClsForStatisticDrawData[clickIndex].realValue;
            if(currentModeWhenDraw == 0)
            {
                displayValue =  String.format("%.2f",f);
            }
            else if(currentModeWhenDraw == 1)//calorie
            {
                displayValue =  String.format("%.1f",f);
            }
            else
            {
                int t = (int)f;
                displayValue =  String.format("%02d",t/3600)+":"+String.format("%02d",t/60)+":"+String.format("%02d",(t%3600)%60);
            }
            displayFitnessInfo.setText(displayValue +"\n"+ mClsForStatisticDrawData[clickIndex].time);
            Log.v("clickIndex = "+clickIndex+" currentDataCount = "+currentDataCount,"clickIndex = "+clickIndex);
            LayoutParams lpl = new LayoutParams(100,50);
            LayoutParams lpp = new LayoutParams(20,20);
            float lm = x+staticImagDisplayPositionFL.leftMargin;
            Log.v("position in map is ="+lm+" x value is = "+ x,"position in map is ="+lm+" x value is = "+ x);
            if(mode <=(intervalForStaticPixel/2))
            {
                lpl.leftMargin = ((int)lm/intervalForStaticPixel)*intervalForStaticPixel-intervalForStaticPixel - (int)Math.abs(staticImagDisplayPositionFL.leftMargin)%intervalForStaticPixel;
                lpp.leftMargin = ((int)lm/intervalForStaticPixel)*intervalForStaticPixel-8 -(int)Math.abs(staticImagDisplayPositionFL.leftMargin)%intervalForStaticPixel;
                //lpp.leftMargin = ((int)(x+staticImagDisplayPositionFL.leftMargin)/100)*100-10;
            }
            else
            {
                lpl.leftMargin = ((int)lm/intervalForStaticPixel+1)*intervalForStaticPixel-intervalForStaticPixel - (int)Math.abs(staticImagDisplayPositionFL.leftMargin)%intervalForStaticPixel;
                lpp.leftMargin = ((int)lm/intervalForStaticPixel+1)*intervalForStaticPixel-8 - (int)Math.abs(staticImagDisplayPositionFL.leftMargin)%intervalForStaticPixel;
                //lpl.leftMargin =((int)(x+staticImagDisplayPositionFL.leftMargin)/100+1)*100 - 50;
                //lpp.leftMargin = ((int)(x+staticImagDisplayPositionFL.leftMargin)/100+1)*100-10;
            }

            Log.v("lpl.leftMargin = "+lpl.leftMargin,"lpl.leftMargin = "+lpl.leftMargin);
            if(mClsForStatisticDrawData[clickIndex].value<intervalForStaticPixel)
            {
                lpl.topMargin = (int) mClsForStatisticDrawData[clickIndex].value+ 30;
            }
            else
            {
                lpl.topMargin = (int) mClsForStatisticDrawData[clickIndex].value-50;
            }

            displayFitnessInfo.setLayoutParams(new FrameLayout.LayoutParams(lpl));

            lpp.topMargin = (int) mClsForStatisticDrawData[clickIndex].value-7;
            hintForSelectFitnessData.setLayoutParams(new FrameLayout.LayoutParams(lpp));
            displayFitnessInfo.setVisibility(View.VISIBLE);
            hintForSelectFitnessData.setVisibility(View.VISIBLE);
           // if(ifDisplayFitnessDataInText == true)
               //  mHandler.sendEmptyMessageDelayed(SET_INVISUAL,2000);
        }
        else//up
        {
           // displayFitnessInfo.setVisibility(View.GONE);
            //hintForSelectFitnessData.setVisibility(View.GONE);

        }

    }


    public void drawStatisticImg(int currentMode)
    {
    Log.v("draw static map ","draw static map");
        SQLiteDatabase rdb = db.getReadableDatabase();
        Calendar c = Calendar.getInstance();
        String colName = null;
        String scale = null;
        float maxValue = 0;
        int dataCount = 0;
        int colorForDraw = 0;
        Path pathForClip = new Path();
        int  currentDrawPosition = 0;
        Bitmap statisticBitMap;
       Canvas statisticBitMapCanvas;
        Paint statisticBitmapPaint;
        statisticBitMapCanvas = new Canvas();
        statisticBitmapPaint = new Paint();
        


            if(currentMode == 0)
            {
                scale = "km";
                colName = DBForTreatmill.DISTANCE;
                maxValue = 99;
                colorForDraw = Color.rgb(0,0,128);
                hintForSelectFitnessData.setImageBitmap(statisticBitMapC0);
            }
            else if(currentMode == 1)
            {
                colName =DBForTreatmill.CALORIE;
                scale = "cal";
                maxValue = 999;
                colorForDraw = Color.rgb(200,5,0);
                hintForSelectFitnessData.setImageBitmap(statisticBitMapC1);
            }
            else if(currentMode == 2)
            {
                colName =DBForTreatmill.TIME;
                scale = "min";
                maxValue = 199;
                colorForDraw = Color.rgb(23,112,16);
                hintForSelectFitnessData.setImageBitmap(statisticBitMapC2);
            }
            else
            {
                return;
            }
            currentModeWhenDraw=currentMode;
            crForStatisticDraw = db.selectInAsce(DBForTreatmill.TABLE_NAME_USER_FITNESS, null, DBForTreatmill.USER_ID+"=?", new String[]{currentUserId}, rdb);
           dataCount = crForStatisticDraw.getCount();
           if(dataCount<=0)
           {
        	   Bitmap bForScale = Bitmap.createBitmap(60,HEIGHT_OF_STATISTIC_IMG,Config.ARGB_8888);
               bForScale.eraseColor(Color.argb(0,0,0,0));
               modeStatisticScaleImg.setImageBitmap(bForScale);
               statisticBitMap =Bitmap.createBitmap(800,HEIGHT_OF_STATISTIC_IMG, Config.ARGB_8888);
                  statisticBitMap.eraseColor(Color.argb(0,0,0,0));
               modeStatisticImg.setImageBitmap(statisticBitMap);
               return;
           }   
           Log.v("data count "+dataCount,"data count "+dataCount);
            mClsForStatisticDrawData = new ClsForStatisticDrawData[dataCount];

           int index = 0;
            crForStatisticDraw.moveToFirst();


            do
            {
                Log.v("cr count "+index,"cr count"+index);
               ClsForStatisticDrawData temp = new ClsForStatisticDrawData();
                temp.realValue =  crForStatisticDraw.getFloat(crForStatisticDraw.getColumnIndex(colName));
                if(currentMode == 0)
                {
                  temp.value = HEIGHT_OF_STATISTIC_IMG -15/*above 0*/- temp.realValue/40*27;//计算真实值对应的纵坐标像素值
                }
                else if(currentMode == 1)
                {
                    temp.value = HEIGHT_OF_STATISTIC_IMG -15 - temp.realValue/1000*27;
                }
                else if(currentMode == 2)
                {
                    temp.value = HEIGHT_OF_STATISTIC_IMG -15 - temp.realValue/60/100*27;
                }
                temp.time = ""+crForStatisticDraw.getInt(crForStatisticDraw.getColumnIndex(DBForTreatmill.ID));/*String.format("%02d",crForStatisticDraw.getInt(crForStatisticDraw.getColumnIndex(DBForTreatmill.MONTH)+1))+"-"+
                String.format("%02d",crForStatisticDraw.getInt(crForStatisticDraw.getColumnIndex(DBForTreatmill.DAY)+1));
                Log.v("fitness date "+temp.time,"fitness date "+temp.time);
                if(formerStr.compareTo(temp.time)!=0)
                {
                    formerStr = new String(temp.time);
                    charIndex = 65;//同一天内，不同的锻炼时间段用ABCDE来区分
                    temp.time = temp.time+(char)charIndex;

                }
                else
                {
                    charIndex++;
                     temp.time = temp.time+(char)charIndex;
                }*/
                mClsForStatisticDrawData[index++] = temp;

            }while (crForStatisticDraw.moveToNext());
            
            Log.v("fitness date index "+ index,"fitness date index"+index);
        rdb.close();
        currentDataCount = dataCount;
		 int imageLen = dataCount*intervalForStaticPixel+4*intervalForStaticPixel;
         /*int imageLen = 10*intervalForStaticPixel;//先默认一个图像大小，当数据太多时，再从新定义图像
            if(dataCount>5)
            {
             imageLen = dataCount*intervalForStaticPixel+4*intervalForStaticPixel;
            }*/
            statisticBitMap =Bitmap.createBitmap(imageLen,HEIGHT_OF_STATISTIC_IMG, Config.ARGB_8888);
        currentImageLen = imageLen;
        staticImagDisplayPositionFL.width = imageLen;
        staticImagDisplayPositionFL.leftMargin = 580-imageLen;
		 if(staticImagDisplayPositionFL.leftMargin >0)
        	staticImagDisplayPositionFL.leftMargin = 0;
            statisticBitMapCanvas.setBitmap(statisticBitMap);
           statisticBitMap.eraseColor(Color.argb(0,0,0,0));


            int strokeWidth = 3;
            Path path = new Path();
            statisticBitmapPaint.setStyle(Paint.Style.STROKE);
            statisticBitmapPaint.setStrokeWidth(strokeWidth);
            statisticBitmapPaint .setAntiAlias(true);
             pathForClip.moveTo(0,HEIGHT_OF_STATISTIC_IMG);
            int i = 0;
           int indexInBitmap = 0;
//画起始2个虚线
            path.moveTo(0,mClsForStatisticDrawData[0].value);
            path.lineTo(intervalForStaticPixel,mClsForStatisticDrawData[0].value);
            path.lineTo(2*intervalForStaticPixel,mClsForStatisticDrawData[0].value);
            pathForClip.lineTo(0, mClsForStatisticDrawData[0].value);
            pathForClip.lineTo(intervalForStaticPixel,mClsForStatisticDrawData[0].value);
            pathForClip.lineTo(2*intervalForStaticPixel,mClsForStatisticDrawData[0].value);
            //path.addRect(-10, 0, 5, strokeWidth, Path.Direction.CCW);
            PathEffect pe = new DashPathEffect(new float[] {10, 5,10, 5}, 0);
            //Path pathEffectSharp = new Path();
            //PathEffect pe =new PathDashPathEffect(path, 10, 0, PathDashPathEffect.Style.ROTATE);//虚线间隔
            statisticBitmapPaint.setPathEffect(pe);
            statisticBitmapPaint.setColor(colorForDraw);
            statisticBitMapCanvas.drawPath(path,statisticBitmapPaint);
            Log.v("now currentDrawPosition when =0 "+ currentDrawPosition,"now currentDrawPosition "+ currentDrawPosition);

            //画中间的线
           indexInBitmap = 2;
            path = new Path();
            path.moveTo(indexInBitmap*intervalForStaticPixel,mClsForStatisticDrawData[0].value);
            for( i =0;i<dataCount;i++)
            {
                Log.v("currentDrawPosition "+currentDrawPosition+i,"currentDrawPosition "+currentDrawPosition+i);

                path.lineTo(indexInBitmap*intervalForStaticPixel ,mClsForStatisticDrawData[i].value);
                pathForClip.lineTo(indexInBitmap*intervalForStaticPixel ,mClsForStatisticDrawData[i].value);
                indexInBitmap++;
            }

             pe =new CornerPathEffect(10);
            statisticBitmapPaint.setPathEffect(pe);
            statisticBitMapCanvas.drawPath(path,statisticBitmapPaint);

             path = new Path();
            path.moveTo((indexInBitmap-1)*intervalForStaticPixel ,mClsForStatisticDrawData[dataCount-1].value);
             Log.v("indexinbitmap "+indexInBitmap,"indexinbitmap "+indexInBitmap);
//            int spareRight = 2;//留给右侧的空间
 //            if(dataCount<6)
//             {
//                 spareRight = 9-dataCount;
//             }

//画右侧的2个虚线
              for(i =0;i<2;i++)
             {
                path.lineTo(indexInBitmap*intervalForStaticPixel ,mClsForStatisticDrawData[dataCount-1].value);
                pathForClip.lineTo(indexInBitmap*intervalForStaticPixel,mClsForStatisticDrawData[dataCount-1].value);
                 indexInBitmap++;
            }
          //  path.moveTo((indexInBitmap-1)*100,0);
            pathForClip.lineTo((indexInBitmap-1)*intervalForStaticPixel ,HEIGHT_OF_STATISTIC_IMG);
            //path.addRect(-10, 0, 0,strokeWidth, Path.Direction.CCW);
            
            // pe =new PathDashPathEffect(path, 10, 0, PathDashPathEffect.Style.ROTATE);
            pe = new DashPathEffect(new float[] {10, 5, 10, 5}, 0);
             statisticBitmapPaint.setPathEffect(pe);
            statisticBitMapCanvas.drawPath(path,statisticBitmapPaint);

            
           //画折线下面的阴影区域
           statisticBitMapCanvas.clipPath(pathForClip);

            statisticBitMapCanvas.drawARGB(80,Color.red(colorForDraw),Color.green(colorForDraw),Color.blue(colorForDraw));

            statisticBitMapCanvas.clipRect(new Rect(0,0,imageLen,HEIGHT_OF_STATISTIC_IMG), Region.Op.REPLACE);
            statisticBitmapPaint.setPathEffect(null);
          //画圆圈
            indexInBitmap = 2;
            for( i =0;i<dataCount;i++)
            {
                statisticBitMapCanvas.drawCircle((indexInBitmap)*intervalForStaticPixel,mClsForStatisticDrawData[i].value,4,statisticBitmapPaint);
                indexInBitmap++;
            }
            indexInBitmap = 2;
            statisticBitmapPaint.setColor(Color.WHITE);
             statisticBitmapPaint.setStyle(Paint.Style.FILL);
            for( i =0;i<dataCount;i++)
            {
                statisticBitMapCanvas.drawCircle((indexInBitmap)*intervalForStaticPixel,mClsForStatisticDrawData[i].value,3,statisticBitmapPaint);
                indexInBitmap++;
            }

//画底侧的时间轴值
            statisticBitmapPaint.setStrokeWidth(0);
            statisticBitmapPaint.setColor(Color.rgb(100,100,100));
             statisticBitmapPaint.setTextSize(10);
            statisticBitmapPaint.setPathEffect(null);
//画底下左侧修饰边的两个刻度
            statisticBitMapCanvas.drawLine(intervalForStaticPixel,HEIGHT_OF_STATISTIC_IMG,intervalForStaticPixel,130,statisticBitmapPaint);
            statisticBitMapCanvas.drawLine(2*intervalForStaticPixel,HEIGHT_OF_STATISTIC_IMG,2*intervalForStaticPixel,130,statisticBitmapPaint);
            indexInBitmap = 2;
            for( i =0;i<dataCount;i++)
            {
                statisticBitMapCanvas.drawLine((indexInBitmap)*intervalForStaticPixel,HEIGHT_OF_STATISTIC_IMG,(indexInBitmap)*intervalForStaticPixel,130,statisticBitmapPaint);
                statisticBitMapCanvas.drawText(mClsForStatisticDrawData[i].time,(indexInBitmap)*intervalForStaticPixel-5,130 ,statisticBitmapPaint);
                indexInBitmap++;
            }
//画右侧边的修饰刻度
            for(i=0;i<2;i++)
            {
            statisticBitMapCanvas.drawLine(indexInBitmap*intervalForStaticPixel,HEIGHT_OF_STATISTIC_IMG,indexInBitmap*intervalForStaticPixel,130,statisticBitmapPaint);
            indexInBitmap++;
            }

//画左侧刻度
        Paint pForScale = new Paint();
        pForScale.setColor(Color.rgb(100,100,100));
        pForScale.setTextSize(13);
        pForScale.setAntiAlias(true);
        Bitmap bForScale = Bitmap.createBitmap(60,HEIGHT_OF_STATISTIC_IMG,Config.ARGB_8888);
        bForScale.eraseColor(Color.argb(0,0,0,0));
        Canvas cForScale = new Canvas();
        cForScale.setBitmap(bForScale);
        int scaleIntervalValue = 25;
            for(i =0;i<5;i++)
            {
                cForScale.drawLine(0,i*scaleIntervalValue+20,6,i*scaleIntervalValue+20,pForScale);
                if(currentMode == 0)//distance
                {
                    cForScale.drawText((4-i)*10+scale,10,i*scaleIntervalValue+20,pForScale);
                }
                else if(currentMode == 1)//calorie
                {
                    cForScale.drawText((4-i)*250+scale,10,i*scaleIntervalValue+20,pForScale);
                }
                else//time
                {
                    cForScale.drawText((4-i)*25+scale,10,i*scaleIntervalValue+20,pForScale);
                }
            }
            modeStatisticScaleImg.setImageBitmap(bForScale);
            modeStatisticImg.setImageBitmap(statisticBitMap);
            modeStatisticImg.setLayoutParams(staticImagDisplayPositionFL);
        }

       /* for(int i =0;i<10;i++)
        {

            canvas.drawText(monthName[i],20+i*75,230,paint);
            divide = 5;
            if(divide!=0)
            {
                valuePicBmp = Bitmap.createScaledBitmap(valuePicBmp, 33, divide, true);
                valuePicCanvas.setBitmap(valuePicBmp);
                //bmp.eraseColor(Color.argb(i*10,0,0,0));
                valuePicPaint.setAlpha(divide/4);
                valuePicCanvas.drawPaint(valuePicPaint);
                canvas.drawBitmap(valuePicBmp,20+i*75,200-divide,paint);
            }
        }*/





    void archToRightUp(Path p, float x1, float y1,float x2, float y2)
    {
        float midx = (x2-x1)/2;

        float midy = 0;
        if(y2-y1<0)
        {
            midy = (y2-y1)/2;
            RectF r = new RectF(x1,midy,midx+midx-x1,y1+(y1-midy));
            p.arcTo(r,180,270);
            r = new RectF(midx,y2,x2+x2-midx,midy + (midy-y2));
            p.arcTo(r,180,270);
        }
        else
        {
            midy = (y2-y1)/2;
            RectF r = new RectF(x1-(midx-x1),y1,midx,midy+(midy-y1));
            p.arcTo(r,270,360);
            r = new RectF(midx-(x2-midx),midy,x2,y2 + (y2 - midy));
            p.arcTo(r,270,360);
        }
    }

    void upDateUserInforList()
    {
        SQLiteDatabase rdb = db.getReadableDatabase();
        Cursor cr = db.select(DBForTreatmill.TABLE_NAME_USER, new String[]{DBForTreatmill.USER_NAME,DBForTreatmill.ID},null,null,rdb);
        Log.v("treatmill","cr.count="+cr.getCount());
        userNameList = new String[cr.getCount()];
        userIdList = new int[cr.getCount()];
        int index = 0;
        if(cr.moveToFirst()){
        	userNameList[index]=cr.getString(cr.getColumnIndex(DBForTreatmill.USER_NAME));
        	userIdList[index] = cr.getInt(cr.getColumnIndex(DBForTreatmill.ID));
        	index++;
        }
        while(cr.moveToNext()){
        	userNameList[index]=cr.getString(cr.getColumnIndex(DBForTreatmill.USER_NAME));
        	userIdList[index] = cr.getInt(cr.getColumnIndex(DBForTreatmill.ID));
        	index++;
        }
        cr.close();
        rdb.close();
    }
    void getuserListView()
    {
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<userNameList.length;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("userName",userNameList[i]);
            listItem.add(map);
            Log.v("treat mill","user name "+i+" ="+userNameList[i]);
        }
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,
                R.layout.user_list_item,
                new String[] {"userName"},
                new int[] {R.id.user_name});

        userListView.setAdapter(listItemAdapter);
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.v("treatmill return ok","treatmill return ok "+resultCode);
//        switch(requestCode)
//        {
//            case 0:
//                switch (resultCode) { //resultCode为锟截达拷锟侥憋拷牵锟斤拷锟斤拷锟紹锟叫回达拷锟斤拷锟斤拷RESULT_OK
//
//                    case RESULT_OK:
//                        Log.v("treatmill return ok","treatmill return ok");
//                        Bundle b=data.getExtras();  //data为B锟叫回达拷锟斤拷Intent
//                        currentUser = b.getString("new_user_name");
//                        Log.v("mode select currentUser "+currentUser,"mode select currentUser "+currentUser);
//                        saveConfig("current_user_name",currentUser);
//                        userNameInTitle.setText(currentUser);
//                        updateStaticAll();
//
//                    default:
//                        break;
//                }
//                break;
//            default:
//                break;
//        }
//    }



    @Override
    protected void onDestroy() {
//        Message msg = Message.obtain();
//        msg.what = BlueToothMsgService.TURN_OFF_ALL;
//        BlueToothMsgService.mHandler.sendMessage(msg);
    	SerialDataTransmission.closeSerial(serialFd);
    	cleanFloatSecurityDialog();
    	receiveThread.setrun(false);
    	stopTimer();
        super.onDestroy();
    }
    
    void updateGoalPrize()
    {
		SQLiteDatabase rdb = db.getReadableDatabase();
   		Cursor cr = db.select(DBForTreatmill.TABLE_HISTORY_GOAL,null,DBForTreatmill.USER_ID+"=? ",new String[]{currentUserId},rdb);
  		if (!cr.moveToFirst())
  		{
  			goalForMessage.setText(R.string.have_not_set_goal);
  			return;
  		}
   		int calorOrDis = cr.getInt(cr.getColumnIndex(DBForTreatmill.HISTORY_CALORIE_OR_DISTANCE));

   		
   		float finishProcess = 0;

		int Aleary = cr.getInt(cr.getColumnIndex(DBForTreatmill.HISTORY_GOAL_VALUE));
		int goal =  cr.getInt(cr.getColumnIndex(DBForTreatmill.HISTORY_GOAL_VALUE));
		finishProcess = (float)Aleary/(float)goal;
   			
//		int year = cr.getInt(cr.getColumnIndex(DBForTreatmill.HISTORY_YEAR));
//        int month = cr.getInt(cr.getColumnIndex(DBForTreatmill.HISTORY_MONTH));
//        int day = cr.getInt(cr.getColumnIndex(DBForTreatmill.HISTORY_DAY));
   		
   		if(finishProcess<0.5)
   		{
   			goalForImage.setImageDrawable(this.getResources().getDrawable(R.drawable.cup_b));
   			
   		}
   		else if(finishProcess>0.5&& finishProcess<1)
   		{
   			goalForImage.setImageDrawable(this.getResources().getDrawable(R.drawable.cup_c));
   		}
   		else if(finishProcess>=1.0)
   		{
   			goalForImage.setImageDrawable(this.getResources().getDrawable(R.drawable.cup_a));
   		}
   		Log.v("mode calOrDis "+calorOrDis,"calOrDis "+calorOrDis);
    	if(calorOrDis == DBForTreatmill.HISTORY_CALORIE)
    	{
    		Log.v("dis goal for message","dis goal for message");
    		goalForMessage.setText(this.getResources().getString(R.string.aim)+":"+
    				this.getResources().getString(R.string.calorie)+" "+goal+"KJ.");
    	}
    	else if(calorOrDis == DBForTreatmill.HISTORY_DISTANCE)
    	{
    		Log.v("dis goal for message","dis goal for message");
    		goalForMessage.setText(this.getResources().getString(R.string.aim)
    				+":"+this.getResources().getString(R.string.distance)+" "+goal+"KM");
    	}
   		cr.close();
   		rdb.close();
    }
    void updateStaticAll()
    {
        if(currentLanguage.contains("zh"))
        	ringHashMap = ringHashMapzh;
        else
        	ringHashMap = ringHashMapen;
    	currentUserName = storeDataInLocal.getConfig(this,storeDataInLocal.CURRENT_USER_NAME);
    	currentUserId = storeDataInLocal.getConfig(this,storeDataInLocal.CURRENT_USER_ID);
        if(currentUserName == null || currentUserName == "")
        {
            currentUserName = this.getResources().getString(R.string.default_user);
            storeDataInLocal.saveConfig(ModeSelect.this,storeDataInLocal.CURRENT_USER_NAME,currentUserName);
            
        }
        if(currentUserId== null || currentUserId == "")
        {
            currentUserId = ""+0;
            storeDataInLocal.saveConfig(ModeSelect.this,storeDataInLocal.CURRENT_USER_ID,currentUserName);
            
        }
        userNameInTitle.setText(currentUserName);
        
        updatelistview(currentDuration);
        drawStatisticImg(currentModeWhenDraw);
        updateGoalPrize();
        displayFitnessInfo.setVisibility(View.GONE);
        hintForSelectFitnessData.setVisibility(View.GONE);
    }

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
			
			//case R.id.relax_mode_btn_panel:
			{
//				if(arg0.getY()>arg1.getY())
//				{
//                    TranslateAnimation ani = new TranslateAnimation(0, 0, 0, delta);  
//                    ani.setDuration(300);  
//                    updateBackGroundBeforeMove();
//                    moveImageView.startAnimation(ani);
//
//				}
			}
			
			Log.v("relax gesture panel touch","relax gesture panel touch");
			float delta = arg1.getY()-arg0.getY();
			
		
	        TranslateAnimation ani = new TranslateAnimation(0, 0, 0, delta);  
	        ani.setDuration(300);  
	        updateBackGroundBeforeMove();
	        moveImageView.startAnimation(ani);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		
		Log.v("relax gesture panel touch","relax gesture panel touch");
		float delta = arg1.getY()-arg0.getY();
		
	
        TranslateAnimation ani = new TranslateAnimation(0, 0, 0, delta);  
        ani.setDuration(300);  
        updateBackGroundBeforeMove();
        moveImageView.startAnimation(ani);

			
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		// TODO Auto-generated method stub
		{
		switch(adapterView.getId())
		{
			case R.id.statistic_button_list:
			{
				if(i ==0 )
		        {
		            selectModeStatisticBtn.setBackgroundResource(R.drawable.distance_for_static_not_selected);
		
		        }
		        else if(i == 1)
		        {
		            selectModeStatisticBtn.setBackgroundResource(R.drawable.calories_for_static_not_selected);
		        }
		        else if(i==2)
		        {
		            selectModeStatisticBtn.setBackgroundResource(R.drawable.time_for_static_not_selected);
		        }
				storeDataInLocal.saveConfig(ModeSelect.this,"selectModeStatisticBtn",String.valueOf(i));
		         btnStatisticModeButtonListFL.setVisibility(View.GONE);
		         drawStatisticImg(i);
			}
			break;
			/*case R.id.duration_button_list:
			{
				 if(i ==0 )
	             {
	                 selectDurationForStatisticBtn.setBackgroundResource(R.drawable.all);
	             }
	             else if(i == 1)
	             {
	                 selectDurationForStatisticBtn.setBackgroundResource(R.drawable.this_month);
	             }
	             else if(i==2)
	             {
	                 selectDurationForStatisticBtn.setBackgroundResource(R.drawable.thisweek);
	             }
				 storeDataInLocal.saveConfig(ModeSelect.this,"selectDurationForStatisticBtn",String.valueOf(i));
	             btnDurationButtonListFL.setVisibility(View.GONE);
	             updatelistview(i);
			}*/
			//break;
			case R.id.user_list:
			{
				final String thisName = userNameList[i];
				final int thisId = userIdList[i];
                PopupMenu popup = new PopupMenu(ModeSelect.this, view);
                popup.getMenuInflater().inflate(R.layout.popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.edit_user_info) {
                            Intent intent = new Intent(ModeSelect.this, UserRegisterPanel.class);
                            intent.putExtra("is_new_user", false);
                            intent.putExtra("user_name", thisName);
                            intent.putExtra("user_id", thisId);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.delete_user_info) {
                           // db.delete(DBForTreatmill.TABLE_NAME_USER, DBForTreatmill.USER_NAME + "=?", new String[]{thisName});
                            db.delete(DBForTreatmill.TABLE_NAME_USER,DBForTreatmill.ID+"=?",new String[]{""+thisId});
                            currentUserName = ModeSelect.this.getResources().getString(R.string.default_user);
                            currentUserId = ""+0;
                           
                            storeDataInLocal.saveConfig(ModeSelect.this,storeDataInLocal.CURRENT_USER_NAME, currentUserName);
                            storeDataInLocal.saveConfig(ModeSelect.this,storeDataInLocal.CURRENT_USER_ID, currentUserId);
                            updateStaticAll();

                        } else {
                            currentUserName = thisName;
                            currentUserId = ""+thisId;
                            storeDataInLocal.saveConfig(ModeSelect.this,storeDataInLocal.CURRENT_USER_NAME, currentUserName);
                            storeDataInLocal.saveConfig(ModeSelect.this,storeDataInLocal.CURRENT_USER_ID, currentUserId);
                            updateStaticAll();
                        }
//                        Toast.makeText(ModeSelect.this, "Clicked popup menu item " + item.getTitle(),
//                                Toast.LENGTH_SHORT).show();
                        userPanel.setVisibility(View.GONE);
                        //userBtn.setBackgroundResource(R.drawable.user_btn_not_selected);
                        return true;
                    }
                });

                popup.show();
			}
			break;
			default:
				break;
		}
		}
	}

	int yDelta = 0;
	int xDelta = 0;
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		int x = (int)arg1.getX();
		Log.v("on touch check ","on touch check");
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		
			case R.id.mode_statistic_map_img:
			{
				//Log.v("on touch check statistic ","on touch check");
				if(arg1.getAction() == MotionEvent.ACTION_DOWN)
				{
					//touch_move_last_position = arg1.getX();
					xDelta = x - staticImagDisplayPositionFL.leftMargin;

                    displayFitnessDataInText(arg1.getX(),arg1.getY(),true);
				}
				else if(arg1.getAction( )== MotionEvent.ACTION_MOVE)
				{
					//Log.v("on touch check statistic move ","on touch check");
					staticImagDisplayPositionFL.leftMargin = x - xDelta;
					if(staticImagDisplayPositionFL.leftMargin >=0)
	                {
	                    staticImagDisplayPositionFL.leftMargin = 0;
	                }
					if(staticImagDisplayPositionFL.leftMargin <= 580- currentImageLen
						&&(580>=currentImageLen))
					{
						staticImagDisplayPositionFL.leftMargin = 0;
					}
					else if(staticImagDisplayPositionFL.leftMargin <= (580- currentImageLen))
	                {
	                    staticImagDisplayPositionFL.leftMargin = 580 - currentImageLen;
	                }
					 modeStatisticImg.setLayoutParams(staticImagDisplayPositionFL);		           		            
                  
				}
                else if(arg1.getAction() == MotionEvent.ACTION_UP)
                {
                    displayFitnessDataInText(arg1.getX(),arg1.getY(),false);
                	Log.v("on touch check statistic up ","on touch check");
                }
				
			}
			break;
			case R.id.relax_mode_btn_panel:
            case R.id.grassland_mode:
            case R.id.mountain_mode:
            case R.id.plain_mode:
            case R.id.sea_mode:
            case  R.id.city_mode:
			{
				//Log.v("relax panel touch","relax panel touch "+arg1.getAction());
				  final int Y = (int) arg1.getRawY();
				  switch (arg1.getAction() & MotionEvent.ACTION_MASK) 
				  {
				        case MotionEvent.ACTION_DOWN:				            
				            yDelta = Y - relaxlParams.topMargin;
				            Log.v("relax first panel touch","relax panel touch "+yDelta);
				            break;
				        case MotionEvent.ACTION_UP:
				            break;
				        case MotionEvent.ACTION_POINTER_DOWN:
				            break;
				        case MotionEvent.ACTION_POINTER_UP:
				            break;
				        case MotionEvent.ACTION_MOVE:				           
				        	
				        	relaxlParams.topMargin = Y - yDelta;
				        	Log.v("relax last panel touch","relax panel touch "+relaxlParams.topMargin);
				        	if(relaxlParams.topMargin < -150)
				        		relaxlParams.topMargin = -150;
				        	else if(relaxlParams.topMargin > 0)
				        		relaxlParams.topMargin = 0;
				        	relaxModePanel.setLayoutParams(relaxlParams);
				            break;
				            default:
				            	break;
				//
				//return mGestureDetector.onTouchEvent(arg1);
				  }
				//relaxlParams.topMargin =  - _yDelta;
			//	relaxModePanel.setLayoutParams(relaxlParams);
			}
			return false;
			default:
				break;
			
		}	
		return true;
		
	}
	   public void onClick(View v)
	    {
	        Intent intent;
	        String media_path;
	        int configVal;
	    	currentView = v;
	        String configStr = null;
	        switch (v.getId())
	        {
	            case R.id.my_fitness:
	                updateStaticAll();
	                imageViewMove(v);
	                mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 102, -1).sendToTarget();
	                break;
	            case R.id.arcade_mode:
	            	imageViewMove(v);
	            	mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 104, -1).sendToTarget();
	            	break;
	            case R.id.relaxation:
	            	imageViewMove(v);
	            	mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 101, -1).sendToTarget();
	            	break;
	            case R.id.expert_mode:
	            	mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 105, -1).sendToTarget();
	                Log.v("myfitness","myfitness");
	                imageViewMove(v);
	                break;
	            case R.id.edit_user_panel:
	                intent = new Intent(ModeSelect.this, UserRegisterPanel.class);
//	            		Log.v("click calorie mode","click calorie mode");
	                intent.putExtra("is_new_user",true);
	                userPanel.setVisibility(View.GONE);

	                //userBtn.setBackgroundResource(R.drawable.user_btn_not_selected);
	                startActivity(intent);
	                break;
	            case R.id.user_button:
	                //userBtn.setBackgroundResource(R.drawable.user_btn_selected);
	            	userNameInTitle.setText(currentUserName);
	                upDateUserInforList();
	                getuserListView();
	                userPanel.setVisibility(View.VISIBLE);
	                break;
	            case  R.id.done_user_panel:
	                userPanel.setVisibility(View.GONE);
	                //userBtn.setBackgroundResource(R.drawable.user_btn_not_selected);
	                break;
	            case R.id.calorie_mode:
	            	mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 12, -1).sendToTarget();
	                //v.setBackgroundResource(R.drawable.calorie_mode_selected);
	                Intent intent1 = new Intent(this, SelectTaskMode.class);
	                intent1.putExtra("task", ConstantValue.CALORIE_MODE);
	                intent1.putExtra("mode", ConstantValue.EXPERT_MODE);
	                Log.v("click calorie mode","click calorie mode");
	                startActivity(intent1);
	                ModeSelect.this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
	                break;
	            case R.id.time_mode:
	            	mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 10, -1).sendToTarget();
	               // v.setBackgroundResource(R.drawable.time_mode_selected);
	                intent = new Intent(this, SelectTaskMode.class);
	                intent.putExtra("task", ConstantValue.TIME_MODE);
	                intent.putExtra("mode", ConstantValue.EXPERT_MODE);
	                startActivity(intent);

	                ModeSelect.this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
	                break;
	            case R.id.heart_rate_mode:
	            	mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 103, -1).sendToTarget();
	               // v.setBackgroundResource(R.drawable.heart_rate_mode_selected);
	                intent = new Intent(this, SelectTaskMode.class);
	                intent.putExtra("task", ConstantValue.HEART_RATE_MODE);
	                intent.putExtra("mode", ConstantValue.EXPERT_MODE);
	                startActivity(intent);
	                ModeSelect.this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
	                break;
	            case R.id.distance_mode:
	            	mHandler.obtainMessage(ConstantValue.PLAY_WAV_RING, 11, -1).sendToTarget();
	                //v.setBackgroundResource(R.drawable.distance_mode_selected);
	                intent = new Intent(this, SelectTaskMode.class);
	                intent.putExtra("task", ConstantValue.DISTANCE_MODE);
	                intent.putExtra("mode", ConstantValue.EXPERT_MODE);
	                startActivity(intent);
	                ModeSelect.this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
	                break;
	            case R.id.local_moive_mode:
	            	
	                //v.setBackgroundResource(R.drawable.local_movie_selected);
	                intent = new Intent(this, RunningNow.class);
	                intent.putExtra("mode", ConstantValue.ARCADE_MODE);
	                startActivity(intent);
	                ModeSelect.this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
	                break;
	            case R.id.grassland_mode:
	                //v.setBackgroundResource(R.drawable.grassland_mode_selected);
	                intent = new Intent(this, SelectTaskMode.class);
	                intent.putExtra("mode", ConstantValue.RELAXATION_MODE);
	                intent.putExtra("task", ConstantValue.TIME_MODE);
	                intent.putExtra("where", ConstantValue.GRASS_PLACE);
	                String path = VideoList.getPath("987654321grass");
	                Log.v("video path "+ path,"video path");
	                if(path == null)
	                {
	                	 v.setBackgroundResource(R.drawable.grassland_mode_not_selected);
	                	 new AlertDialog.Builder(this).setTitle(R.string.error).
	                	 setMessage(R.string.no_exist_video_file).setPositiveButton(R.string.ok, null).show(); 
	                	break;
	                }
	               
	                intent.putExtra("mediaPath", path);
	                startActivity(intent);

	                ModeSelect.this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
	                break;
	            case  R.id.mountain_mode:
	                //v.setBackgroundResource(R.drawable.mountain_land_mode_selected);
	                intent = new Intent(this, SelectTaskMode.class);
	                intent.putExtra("mode", ConstantValue.RELAXATION_MODE);
	                intent.putExtra("task", ConstantValue.TIME_MODE);
	                intent.putExtra("where", ConstantValue.MOUNTAIN_PLACE);
	                media_path = VideoList.getPath("987654321mountain");
	                
	                if(media_path == null)
	                {
	                	v.setBackgroundResource(R.drawable.mountain_land_mode_not_selected);
	                	new AlertDialog.Builder(this).setTitle(R.string.error).
	                	setMessage(R.string.no_exist_video_file).setPositiveButton(R.string.ok, null).show();  
	                	break;  
	                	 
	                }
	                intent.putExtra("mediaPath", media_path);
	                startActivity(intent);
	                ModeSelect.this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
	                break;
	            case  R.id.plain_mode:
	                //v.setBackgroundResource(R.drawable.plain_mode_selected);
	                intent = new Intent(this, SelectTaskMode.class);
	                intent.putExtra("mode", ConstantValue.RELAXATION_MODE);
	                intent.putExtra("task", ConstantValue.TIME_MODE);
	                intent.putExtra("where", ConstantValue.PLAIN_PALCE);
	                media_path = VideoList.getPath("987654321park");
	                if(media_path == null)
	                {
	                	 v.setBackgroundResource(R.drawable.plain_mode_not_selected);
	                	 new AlertDialog.Builder(this).setTitle(R.string.error).
	                	 setMessage(R.string.no_exist_video_file).setPositiveButton(R.string.ok, null).show(); 
	                	break;
	                }
	                intent.putExtra("mediaPath", media_path);
	                startActivity(intent);
	                ModeSelect.this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
	                break;
	            case R.id.sea_mode:
	                //v.setBackgroundResource(R.drawable.sea_mode_selected);
	                intent = new Intent(this, SelectTaskMode.class);
	                intent.putExtra("mode", ConstantValue.RELAXATION_MODE);
	                intent.putExtra("task", ConstantValue.TIME_MODE);
	                intent.putExtra("where", ConstantValue.SEA_PLACE);
	                media_path = VideoList.getPath("987654321sea");
	                if(media_path == null)
	                {
	                	 v.setBackgroundResource(R.drawable.sea_mode_not_selected);
	                	 new AlertDialog.Builder(this).setTitle(R.string.error).
	                	 setMessage(R.string.no_exist_video_file).setPositiveButton(R.string.ok, null).show(); 
	                	break;
	                }
	                intent.putExtra("mediaPath", media_path);
	                startActivity(intent);
	                ModeSelect.this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
	                break;
	            case  R.id.city_mode:
	                //v.setBackgroundResource(R.drawable.city_mode_selected);
	                intent = new Intent(this, SelectTaskMode.class);
	                intent.putExtra("mode", ConstantValue.RELAXATION_MODE);
	                intent.putExtra("task", ConstantValue.TIME_MODE);
	                intent.putExtra("where", ConstantValue.CITY_PLACE);
	                media_path = VideoList.getPath("987654321street");
	                if(media_path == null)
	                {
	                	 v.setBackgroundResource(R.drawable.city_mode_not_selected);
	                	 new AlertDialog.Builder(this).setTitle(R.string.error).
	                	 setMessage(R.string.no_exist_video_file).setPositiveButton(R.string.ok, null).show(); 
	                	break;
	                }
	                intent.putExtra("mediaPath", media_path);
	                startActivity(intent);
	                ModeSelect.this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
	                break;
	            case R.id.help:
	            	PopupMenu popup = new PopupMenu(this, v);
	                MenuInflater inflater = popup.getMenuInflater();
	                popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
						
						@Override
						public boolean onMenuItemClick(MenuItem item) {
							// TODO Auto-generated method stub
							switch (item.getItemId()) {
					        case R.id.help_instruction:
					            //archive(item);
					            return true;
					        case R.id.help_about_us:
					           	Intent itent = new Intent(ModeSelect.this,HelpAboutActivity.class);
					           	startActivity(itent);
					            return true;
					        default:
					            return false;
							}
							
						}
					});
	                inflater.inflate(R.layout.help_menu, popup.getMenu());
	                popup.show();
	            	
	                break;
	            case R.id.select_statistic_mode_btn:
	                configStr= storeDataInLocal.getConfig(this,"selectModeStatisticBtn");
	                Log.v("configStr "+configStr,"configStr "+configStr);
	                 if(configStr ==null||configStr == "")
	                     configVal = 0;
	                else
	                    configVal = Integer.parseInt(configStr);

	                if(configVal ==0 )
	                {
	                    selectModeStatisticBtn.setBackgroundResource(R.drawable.distance_for_static_selected);

	                }
	                else if(configVal == 1)
	                {
	                    selectModeStatisticBtn.setBackgroundResource(R.drawable.calories_for_static_selected);
	                }
	                else if(configVal==2)
	                {
	                    selectModeStatisticBtn.setBackgroundResource(R.drawable.time_for_static_selected);
	                }

	                btnStatisticModeButtonListFL.setVisibility(View.VISIBLE);

	                break;
//	            case R.id.select_duration_btn:
//	                configStr= getConfig("selectDurationForStatisticBtn");
//	                Log.v("configStr "+configStr,"configStr "+configStr);
//	                if(configStr ==null||configStr == "")
//	                    configVal = 0;
//	                else
//	                    configVal = Integer.parseInt(configStr);

/*	                if(currentDuration ==0 )
	                {
	                    selectDurationForStatisticBtn.setBackgroundResource(R.drawable.all01);

	                }
	                else if(currentDuration == 1)
	                {
	                    selectDurationForStatisticBtn.setBackgroundResource(R.drawable.this_month01);
	                }
	                else if(currentDuration==2)
	                {
	                    selectDurationForStatisticBtn.setBackgroundResource(R.drawable.thisweek01);
	                }

	                btnDurationButtonListFL.setVisibility(View.VISIBLE);
	                break;
*/
	            case R.id.delete_statistic_record:
	                //db.delete();
	                Log.v("ipcamera click","ipcamera click");
	                deleteItemInDb(deleteItemIndex);
	                deleteRecordBtn.setVisibility(View.GONE);
	                break;
	            case R.id.fitness_record_info_listview_for_click:
	            	break;
	            case R.id.goal_for_click:
	            	 new GoalForSelectPopupWin(ModeSelect.this,v); 
	            	
	            	break;
	            case R.id.advice_for_click:
	            case R.id.bg_choice:
	                userPanel.setVisibility(View.GONE);
	                //userBtn.setBackgroundResource(R.drawable.user_btn_not_selected);
	                btnDurationButtonListFL.setVisibility(View.GONE);
//	                configStr= getConfig("selectDurationForStatisticBtn");
//	                Log.v("configStr "+configStr,"configStr "+configStr);
//	                if(configStr ==null||configStr == "")
//	                    configVal = 0;
//	                else
//	                    configVal = Integer.parseInt(configStr);
	                if(currentDuration ==0 )
	                {
	                    selectDurationForStatisticBtn.setBackgroundResource(R.drawable.all);

	                }
	                else if(currentDuration == 1)
	                {
	                    selectDurationForStatisticBtn.setBackgroundResource(R.drawable.this_month);
	                }
	                else if(currentDuration==2)
	                {
	                    selectDurationForStatisticBtn.setBackgroundResource(R.drawable.thisweek);
	                }
	                btnStatisticModeButtonListFL.setVisibility(View.GONE);

	                configStr= storeDataInLocal.getConfig(this,"selectModeStatisticBtn");
	                Log.v("configStr "+configStr,"configStr "+configStr);
	                if(configStr ==null||configStr == "")
	                    configVal = 0;
	                else
	                    configVal = Integer.parseInt(configStr);

	                if(configVal ==0 )
	                {
	                    selectModeStatisticBtn.setBackgroundResource(R.drawable.distance_for_static_not_selected);

	                }
	                else if(configVal == 1)
	                {
	                    selectModeStatisticBtn.setBackgroundResource(R.drawable.calories_for_static_not_selected);
	                }
	                else if(configVal==2)
	                {
	                    selectModeStatisticBtn.setBackgroundResource(R.drawable.time_for_static_not_selected);
	                }
	                deleteRecordBtn.setVisibility(View.GONE);
	                break;
	            default:
	                break;
	        }

	    }
	   

		public static void initTimer()
		{
           if(mTimer!=null)
               mTimer.cancel();
			 mTimer = new Timer(true);
           if(mTimerTask!=null)
               mTimerTask.cancel();
			 mTimerTask = new MyTimerTask();  // 锟铰斤拷一锟斤拷锟斤拷锟斤拷
			 mTimer.schedule(mTimerTask, 0, 1000);

			
		}
		
		public static void stopTimer()
		{
			if(mTimer!=null)
	               mTimer.cancel();
				 mTimer = new Timer(true);
	           if(mTimerTask!=null)
	               mTimerTask.cancel();
		}
		static byte[] serialSendData= new byte[6];;
		static class MyTimerTask extends TimerTask{
			  @Override
			  public void run() {
				 // Message msg = Message.obtain();
				 // msg.what = SECOND_TICK;
				  
			 SendDataToMCU.senddataFunc((byte)0x20, (byte)0, (byte) 0);
			  if((!securityLockMissForSendStatus)&&(errrorHappendForSendStatus==0))
			  {
		    		
		    		SendDataToMCU.senddataFunc((byte)0x40, (byte)0, (byte) 0);			            

		      }  
			    			    
		  		if(securityLockMissForSendStatus)
		  		{
		  			SendDataToMCU.senddataFunc((byte)0x40,(byte)ConstantValue.NOSAFE_STATUS,(byte)0);
		  		}
		  		 if(errrorHappendForSendStatus>0)
		  		{
		  			SendDataToMCU.senddataFunc((byte)0x40,(byte)errrorHappendForSendStatus,(byte)0);
		  		}
				  	
			  }
			     
		}
		
		
		private MyFloatSecurityDialog fvSecurityFloatDialog = null; 
		WindowManager wm ;
		private void securityDialog()
		{
			
			WindowManager.LayoutParams params = fvSecurityFloatDialog.params;
			params.format=PixelFormat.RGBA_8888;
			params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
			params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
					| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE 
					|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
			
			params.width = 400;
			params.height = 200;//WindowManager.LayoutParams.WRAP_CONTENT;
			//params.alpha = 80;
			
			params.gravity=Gravity.LEFT|Gravity.TOP;
		    //以屏幕左上角为原点，设置x、y初始值
			params.x = 200;
			params.y = 80;
			//MyFloatFrameLayout tv = null;   
			
			if(fvSecurityFloatDialog != null)	
			{
					wm.removeView(fvSecurityFloatDialog); 
					fvSecurityFloatDialog =null;
			}
			

			
			fvSecurityFloatDialog = new MyFloatSecurityDialog(ModeSelect.this);		
			wm.addView(fvSecurityFloatDialog, params);	    
		}
		
		public void cleanFloatSecurityDialog()
		{
			if(fvSecurityFloatDialog != null)	
			{
				wm.removeView(fvSecurityFloatDialog); 
				fvSecurityFloatDialog =null;
				//wm.addView(fvFloat, params);
//				final ViewGroup parent=(ViewGroup)fvFloat.getParent();
//				if(parent!=null)
//				{
//					parent.removeView(fvFloat);
//				}
//				fvFloat = null;
			}
		}
		
		 void initRingMap()
		 {
	        ringHashMapzh = new HashMap<Integer, Integer>();
	        
	        
	        ringHashMapzh.put(1, mSoundPool.load(this, R.raw.a1, 1));  //欢迎
	        ringHashMapzh.put(2, mSoundPool.load(this, R.raw.a2, 1));  //r
	        ringHashMapzh.put(3, mSoundPool.load(this, R.raw.a3, 1));  
	        ringHashMapzh.put(5, mSoundPool.load(this, R.raw.a5, 1)); 
	        ringHashMapzh.put(7, mSoundPool.load(this, R.raw.a7, 1));  
	        ringHashMapzh.put(8, mSoundPool.load(this, R.raw.a8, 1)); 
	        ringHashMapzh.put(9, mSoundPool.load(this, R.raw.a9, 1)); 
	        ringHashMapzh.put(10, mSoundPool.load(this, R.raw.a10, 1)); 
	        ringHashMapzh.put(11, mSoundPool.load(this, R.raw.a11, 1)); 
	        ringHashMapzh.put(12, mSoundPool.load(this, R.raw.a12, 1)); 
	        ringHashMapzh.put(30, mSoundPool.load(this, R.raw.a30, 1)); 
	        
	        ringHashMapzh.put(31, mSoundPool.load(this, R.raw.a31, 1));  
	        ringHashMapzh.put(32, mSoundPool.load(this, R.raw.a32, 1)); 
	        ringHashMapzh.put(33, mSoundPool.load(this, R.raw.a33, 1)); 
	        ringHashMapzh.put(34, mSoundPool.load(this, R.raw.a34, 1)); 
	        ringHashMapzh.put(35, mSoundPool.load(this, R.raw.a35, 1)); 
	        ringHashMapzh.put(36, mSoundPool.load(this, R.raw.a36, 1)); 
	        ringHashMapzh.put(37, mSoundPool.load(this, R.raw.a36, 1)); 
	        ringHashMapzh.put(38, mSoundPool.load(this, R.raw.a36, 1)); 
	        ringHashMapzh.put(39, mSoundPool.load(this, R.raw.a36, 1)); 
	        ringHashMapzh.put(57, mSoundPool.load(this, R.raw.a57, 1)); 
	        
	        
	        ringHashMapzh.put(50, mSoundPool.load(this, R.raw.a50, 1)); 
	        ringHashMapzh.put(51, mSoundPool.load(this, R.raw.a51, 1));   
	        
	        ringHashMapzh.put(102, mSoundPool.load(this, R.raw.a102, 1));   //我的健身
	        ringHashMapzh.put(101, mSoundPool.load(this, R.raw.a101, 1)); //场景模式
	        
	        ringHashMapzh.put(103, mSoundPool.load(this, R.raw.a103, 1)); //心率模式
	        ringHashMapzh.put(104, mSoundPool.load(this, R.raw.a104, 1));   //娱乐模式
	        ringHashMapzh.put(105, mSoundPool.load(this, R.raw.a105, 1));   //专业模式
	        
	        
	        ringHashMapen = new HashMap<Integer, Integer>();
	        
	        
	        //ringHashMapen.put(1, mSoundPool.load(this, R.raw.a1, 1));  
	        ringHashMapen.put(2, mSoundPool.load(this, R.raw.a2e, 1)); 
	        ringHashMapen.put(3, mSoundPool.load(this, R.raw.a3e, 1));  
	        ringHashMapen.put(5, mSoundPool.load(this, R.raw.a5e, 1)); 
	        ringHashMapen.put(7, mSoundPool.load(this, R.raw.a7e, 1));  
	        ringHashMapen.put(8, mSoundPool.load(this, R.raw.a8e, 1)); 
	        ringHashMapen.put(9, mSoundPool.load(this, R.raw.a9e, 1)); 
	        ringHashMapen.put(10, mSoundPool.load(this, R.raw.a10e, 1)); 
	        ringHashMapen.put(11, mSoundPool.load(this, R.raw.a11e, 1)); 
	        ringHashMapen.put(12, mSoundPool.load(this, R.raw.a12e, 1)); 
	        ringHashMapen.put(30, mSoundPool.load(this, R.raw.a30e, 1)); 
	        
	        ringHashMapen.put(31, mSoundPool.load(this, R.raw.a31e, 1));  
	        ringHashMapen.put(32, mSoundPool.load(this, R.raw.a32e, 1)); 
	        ringHashMapen.put(33, mSoundPool.load(this, R.raw.a33e, 1)); 
	        ringHashMapen.put(34, mSoundPool.load(this, R.raw.a34e, 1)); 
	        ringHashMapen.put(35, mSoundPool.load(this, R.raw.a35e, 1)); 
	        ringHashMapen.put(36, mSoundPool.load(this, R.raw.a36e, 1)); 
	        ringHashMapen.put(37, mSoundPool.load(this, R.raw.a36e, 1)); 
	        ringHashMapen.put(38, mSoundPool.load(this, R.raw.a36e, 1)); 
	        ringHashMapen.put(39, mSoundPool.load(this, R.raw.a36e, 1)); 
	        ringHashMapen.put(57, mSoundPool.load(this, R.raw.a57e, 1)); 
	        
	        
	        ringHashMapen.put(50, mSoundPool.load(this, R.raw.a50e, 1)); 
	        ringHashMapen.put(51, mSoundPool.load(this, R.raw.a51e, 1));   
	        
	        ringHashMapen.put(102, mSoundPool.load(this, R.raw.a102e, 1));   //我的健身
	        ringHashMapen.put(101, mSoundPool.load(this, R.raw.a101e, 1)); //场景模式
	        
	        ringHashMapen.put(103, mSoundPool.load(this, R.raw.a103e, 1)); //心率模式
	        ringHashMapen.put(104, mSoundPool.load(this, R.raw.a104e, 1));   //娱乐模式
	        ringHashMapen.put(105, mSoundPool.load(this, R.raw.a105e, 1));   //专业模式
		 }
}
