package com.weihan.treatmill;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class SelectTaskMode extends Activity implements SeekBar.OnSeekBarChangeListener{
	TextView choose_task_mode_tip,seekbar_start_tip,seekbar_end_tip, value, scale;
	Button confirm;
	SeekBar seekbar; 
	int currentMode;
	int currentTask;
	int currentPlace;
	int taskValue;
    private final int CALORIE_MODE = 8;
    private final int TIME_MODE = 9;
    private final int HEART_RATE_MODE = 10;
    private final int DISTANCE_MODE = 11;
    String mediaPath;
    SharedPreferences sp;
    String currentUserID;
    int heartBeatMin,heartBeatMax;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_task_mode);  
        currentTask = this.getIntent().getExtras().getInt("task");
        currentMode = this.getIntent().getExtras().getInt("mode");
        currentPlace = this.getIntent().getExtras().getInt("where",0);
        Log.v("task select "+ currentMode + " "+currentTask,"task select");
        mediaPath = this.getIntent().getExtras().getString("mediaPath");
        //choose_task_mode_tip = (TextView)findViewById(R.id.select_task_tip);
        choose_task_mode_tip = (TextView)findViewById(R.id.hint_for_task_mode);
        value = (TextView)findViewById(R.id.select_value);
        scale = (TextView)findViewById(R.id.scale);
        seekbar = (SeekBar)findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(this);
        confirm = (Button)findViewById(R.id.select_task_mode);
        
        currentUserID = storeDataInLocal.getConfig(this,storeDataInLocal.CURRENT_USER_ID );
        Log.v("user register currentUserID "+currentUserID,"user register currentUserID "+currentUserID);
        if(currentUserID == null)
        {
            return;
        }
        if(currentTask == CALORIE_MODE)
        {
        	//seekbar_start_tip.setText("0 carlorie");
        	//seekbar_end_tip.setText("10000 calorie");
        	choose_task_mode_tip.setText(R.string.please_select_calorie);
        	scale.setText(R.string.select_task_cal);
            seekbar.setMax(999);
            
        	
        }
        else if(currentTask == TIME_MODE)
        {
        	//seekbar_start_tip.setText("0 min");
        	//seekbar_end_tip.setText("100 min");
        	choose_task_mode_tip.setText(R.string.please_select_time);
        	scale.setText(R.string.select_task_minute);
            seekbar.setMax(99);
        }
        else if(currentTask == HEART_RATE_MODE)
        {
        	//seekbar_start_tip.setText("70 beat");
        	//seekbar_end_tip.setText("100 beat");
        	choose_task_mode_tip.setText(R.string.please_select_heart_beat);
        	scale.setText(R.string.select_task_beat);
            DBForTreatmill db= new DBForTreatmill(this);
            SQLiteDatabase rdb = db.getReadableDatabase();
            Cursor cr = db.select(DBForTreatmill.TABLE_NAME_USER,new String[]{DBForTreatmill.USER_AGE},DBForTreatmill.ID+"=?",new String[]{currentUserID},rdb);
            int age = 20;
            if(!cr.moveToFirst())
            {
            	age = 20;
            }
            else
            {
            	age = cr.getInt(cr.getColumnIndex(DBForTreatmill.USER_AGE));
            }
            int hb = (int)((220-age)*0.6);
            heartBeatMin = (int)(hb*0.9);
            heartBeatMax = (int)(hb*1.1);
            seekbar.setMax(heartBeatMax-heartBeatMin);
        }
        else if(currentTask == DISTANCE_MODE)
        {
        	//seekbar_start_tip.setText("0 Km");
        	//seekbar_end_tip.setText("100 Km");
        	choose_task_mode_tip.setText(R.string.please_select_distance);
        	scale.setText(R.string.select_task_km);
            seekbar.setMax(99);
        }
        	
        
        confirm.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(taskValue == 0&&currentTask != HEART_RATE_MODE)
				{
	        		  new AlertDialog.Builder(SelectTaskMode.this).setTitle(R.string.error).
	        		  setMessage(R.string.please_select_value_larger_than_0).setPositiveButton(R.string.ok, null).show(); 

					return;
				}
				SelectTaskMode.this.finish();
				Intent intent = null;
				if(currentMode == ConstantValue.EXPERT_MODE)
				{

					intent = new Intent(SelectTaskMode.this, SelectSceneInExpertMode.class);
				}
				else 
				{
					intent = new Intent(SelectTaskMode.this, RunningNow.class);
				}
	    		intent.putExtra("mode", currentMode);
	    		intent.putExtra("task", currentTask);
	    	    intent.putExtra("task_value", taskValue);
	    	    intent.putExtra("where", currentPlace);
	    	    intent.putExtra("mediaPath", mediaPath);
	    		Log.v("click calorie mode","click calorie mode");
	    		startActivity(intent);
			}
        	
        });
    }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		Log.v("test seekbar","test seekbar");
		taskValue = progress;
		if(currentTask == HEART_RATE_MODE)
			taskValue = heartBeatMin +progress;
	    else
            taskValue = progress;
		value.setText(String.valueOf(taskValue));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
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