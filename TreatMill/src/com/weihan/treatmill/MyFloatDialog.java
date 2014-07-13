package com.weihan.treatmill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.weihan.treatmill.MyFloatFrameLayout.FloatFitnessDate;

public class MyFloatDialog extends LinearLayout{

	private final String TAG = MyFloatFrameLayout.class.getSimpleName();
	

	public static WindowManager.LayoutParams params = new WindowManager.LayoutParams(); 
	
    private TextView hintForAdjustPara;
    private TextView valueForAdjustPara;
    private SeekBar  seekBarForAdjustPara;
    private Button  okForAdjustPara;
    private float setValue;
    
    public Context ctx;
    private int select;
    private float currentValue;

	public MyFloatDialog(Context context, int sel,float curvalue) {
		super(context);
		ctx = context;
		select = sel;
		setValue = currentValue = curvalue;
		View v =View.inflate( context, R.layout.float_ajust_etting_dialog,null);	
		this.addView(v);
		hintForAdjustPara = (TextView)findViewById(R.id.hine_for_adjust_para);
		valueForAdjustPara= (TextView)findViewById(R.id.value_for_adjust_para);
        seekBarForAdjustPara= (SeekBar)findViewById(R.id.seekbar_for_adjust_para);
        okForAdjustPara= (Button)findViewById(R.id.ok_for_setting_para);
        
        if(select == MyFloatFrameLayout.SELECT_ADJUST_INCLINE)
		{
        	hintForAdjustPara.setText(R.string.please_select_incline);
        	seekBarForAdjustPara.setProgress((int) (currentValue*100/ModeSelect.maxIncline));
			valueForAdjustPara.setText(String.format("%.0f",currentValue));
		}
		else if (select == MyFloatFrameLayout.SELECT_ADJUST_SPEED)
		{ 	
			hintForAdjustPara.setText(R.string.please_select_speed);
			seekBarForAdjustPara.setProgress((int) (currentValue*100/(ModeSelect.maxSpeed-ModeSelect.minSpeed)));
			valueForAdjustPara.setText(String.format("%.1f",currentValue));
			
		}	    
        	
		
        okForAdjustPara.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub\
				
	    		Message m = Message.obtain();
				m.what = MyFloatFrameLayout.RETRUN_AJDUST_PARA;
				m.arg1 = select;
				m.obj = setValue;
				MyFloatFrameLayout.handler.sendMessage(m);
				
				Message msg = Message.obtain();
				msg.what = MyFloatFrameLayout.CLOSE_ADJUST_DIALOG;
				msg.arg1 = select;
				msg.obj = setValue;
				MyFloatFrameLayout.handler.sendMessage(msg);
				Log.v("click topframe","click topframe");
				
				
			}
		});
		
        seekBarForAdjustPara.setMax(100);
        
        seekBarForAdjustPara.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
		        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
		            //Do something here with new value
		    		if(select == MyFloatFrameLayout.SELECT_ADJUST_INCLINE)
		    		{
		    			
		    			setValue = ((float)ModeSelect.maxIncline)/100*progress;
		    			if(setValue <0)
		    				setValue = 0;
		    			valueForAdjustPara.setText(String.format("%.0f",setValue));
		    		}
		    		else if (select == MyFloatFrameLayout.SELECT_ADJUST_SPEED)
		    		{ 		    			
		    			setValue = ((float)ModeSelect.maxSpeed - ModeSelect.minSpeed)/100*progress+ModeSelect.minSpeed;
		    			if(setValue <ModeSelect.minSpeed)
		    				setValue = ModeSelect.minSpeed;
		    			valueForAdjustPara.setText(String.format("%.1f",setValue));
		    			
		    		}	    
		        	
		        }

				public void onStartTrackingTouch(SeekBar arg0) {
					// TODO Auto-generated method stub
					
				}

				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
		    });
		//
		//speed.setText("1000");
		//handler.post(update);
	}
	 
}

