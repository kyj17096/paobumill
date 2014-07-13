package com.weihan.treatmill;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * 
 * <p>
 * Title: MyTextView.java
 * </p>
 * <p>
 * E-Mail: 176291935@qq.com
 * </p>
 * <p>
 * QQ: 176291935
 * </p>
 * <p>
 * Http: iaiai.iteye.com
 * </p>
 * <p>
 * Create time: 2011-9-28
 * </p>
 * 
 * @author 丸子
 * @version 0.0.1
 */
public class MyFloatFrameLayout extends FrameLayout {
	private final String TAG = MyFloatFrameLayout.class.getSimpleName();
	
	public static int TOOL_BAR_HIGH = 0;
	public static final int UPDATE_REAL_FIT_DATE = 1;
	public static final int SHOW = 2;
	public static final int HIDE = 3;
	public static final int RETRUN_AJDUST_PARA = 4;
	public static final int SELECT_ADJUST_INCLINE = 5;
	public static final int SELECT_ADJUST_SPEED = 6;
	public static final int CLOSE_ADJUST_DIALOG = 7;
	public static final int CLEAN_DATA = 8;
	public static WindowManager.LayoutParams params = new WindowManager.LayoutParams(); 
	private float startX;
	private float startY;
	private float x,xr;
	private float y;
    private TextView floatIconSpeed;
    private TextView floatIconIncline;
    private TextView floatIconTime;
    private TextView floatIconDistance;
    private TextView floatIconHeartbeat;
    private TextView floatIconCalorie;
    FloatFitnessDate ffd;
    public  static Handler handler;	
    private float xClickDown;
    private float xClickUp;
    private float yClickDown;
    private float yClickUp;
    MyFloatDialog fvFloatDialog = null; 
    private int select;
    private float currentValue;
    
    public Context ctx;
	WindowManager wm = (WindowManager)getContext().getApplicationContext().getSystemService(getContext().WINDOW_SERVICE);
 
	public MyFloatFrameLayout(Context context) {
		super(context);
		ctx = context;
		//this.setBackgroundColor(Color.argb(90, 87, 176, 163));
		View v =View.inflate( context, R.layout.floatlayout,null);	
		this.addView(v);
        floatIconSpeed = (TextView)findViewById(R.id.float_speed_value);
        floatIconIncline= (TextView)findViewById(R.id.float_height_value);
        floatIconTime= (TextView)findViewById(R.id.float_time_value);
        floatIconDistance= (TextView)findViewById(R.id.float_distance_value);
        floatIconHeartbeat= (TextView)findViewById(R.id.float_heart_beat_value);
        floatIconCalorie= (TextView)findViewById(R.id.float_carlilu_value);
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what)
				{
				case UPDATE_REAL_FIT_DATE:
					ffd = (FloatFitnessDate)msg.obj;
					floatIconSpeed.setText(String.format("%.1f",ffd.floatIconSpeedValue));
				    floatIconIncline.setText(""+(int)ffd.floatIconInclineValue);;
				    floatIconTime.setText((String.format("%02d",ffd.floatIconTimeValue/60))+":"+String.format("%02d",ffd.floatIconTimeValue%60));;
				    floatIconDistance.setText(String.format("%.2f",ffd.floatIconDistanceValue));
				    floatIconHeartbeat.setText(""+ffd.floatIconHeartbeatValue);
				    if(ffd.floatIconCalorieValue<1000)
				    	floatIconCalorie.setText(String.format("%.1f",ffd.floatIconCalorieValue));
				    else
				    	floatIconCalorie.setText(String.format("%d",(int)ffd.floatIconCalorieValue));
				    break;
				case SHOW:
					MyFloatFrameLayout.this.setVisibility(View.VISIBLE);
					break;
				case HIDE:
					MyFloatFrameLayout.this.setVisibility(View.GONE);
					break;
				case RETRUN_AJDUST_PARA:
					if(msg.arg1 == SELECT_ADJUST_INCLINE)
					{
						floatIconIncline.setText(String.format("%.0f",(Float)(msg.obj)));
						Message m = Message.obtain();
						m.what = ConstantValue.INCLINE_SET;
						m.obj = msg.obj;
						RunningNow.runningNowHandle.sendMessage(m);
					}
					else if (msg.arg1 == SELECT_ADJUST_SPEED)
					{
						
						floatIconSpeed.setText(String.format("%.1f",(Float)msg.obj));
						Message m = Message.obtain();
						m.what = ConstantValue.SPEED_SET;
						m.obj = msg.obj;
						RunningNow.runningNowHandle.sendMessage(m);
					}
									
					break;
				case CLEAN_DATA:
					floatIconSpeed.setText(String.format("%.1f",0.0));
				    floatIconIncline.setText(""+0);;
				    floatIconTime.setText((String.format("%02d",0))+":"+String.format("%02d",0));;
				    floatIconDistance.setText(String.format("%.2f",0.0));
				    floatIconHeartbeat.setText(""+0);
				    floatIconCalorie.setText(String.format("%.1f",0.0));
					break;
				case CLOSE_ADJUST_DIALOG:
					cleanFloatIcon();
					break;
					
				}
				
			}
		};
		
		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.v("click topframe","click topframe");
			}
		});
		
		//
		//speed.setText("1000");
		//handler.post(update);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//触摸点相对于屏幕左上角坐标
		x = event.getRawX();   
	    y = event.getRawY() - TOOL_BAR_HIGH;
	    Log.d(TAG, "------X: "+ x +"------Y:" + y);
		xr = event.getX();   
	    Log.d(TAG, "------X: ");
	    switch(event.getAction()) {
	    	case MotionEvent.ACTION_DOWN:
	    		xClickDown = event.getRawX();
	    		yClickDown  = event.getRawY();
	    	    
	    		startX = event.getX();
	    		startY = event.getY();
	    		break;
	    	case MotionEvent.ACTION_MOVE:
	    		updatePosition();
	    		break;
	    	case MotionEvent.ACTION_UP:
	    		
	    		startX = startY = 0;
	    		xClickUp = event.getRawX();
	    	    yClickUp = event.getRawY();
	    	    
	    	    float len = Math.abs(xClickUp-xClickDown)+ Math.abs(yClickUp-yClickDown);
	    	    Log.v("move len is "+len,"move len is "+ len);
	    	    if(len<16)
	    	    {
	    			WindowManager.LayoutParams params = fvFloatDialog.params;
	    			params.format=PixelFormat.RGBA_8888;
	    			params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
	    			params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
	    					| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE 
	    					|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
	    			
	    			params.width = 500;
	    			params.height = 500;//WindowManager.LayoutParams.WRAP_CONTENT;
	    			//params.alpha = 80;
	    			
	    			params.gravity=Gravity.LEFT|Gravity.TOP;
	    		    //以屏幕左上角为原点，设置x、y初始值
	    			params.x = 250;
	    			params.y = 80;
	    			//MyFloatFrameLayout tv = null;   
	    			
	    			if(fvFloatDialog != null)	
	    			{
	    					wm.removeView(fvFloatDialog); 	    					
	    					fvFloatDialog =null;
	    			}
	    			
	    			if(xr<110)
	    			{
	    				select = SELECT_ADJUST_INCLINE;
	    				currentValue = ffd.floatIconInclineValue;
	    			}
	    			else if(xr>550)
	    			{
	    				select = SELECT_ADJUST_SPEED;
	    				currentValue = ffd.floatIconSpeedValue;
	    			}
	    			else if(xr>=110&&xr<=550)
	    			{
	    				return true;
	    			}
	    			fvFloatDialog = new MyFloatDialog(ctx, select,currentValue);		
	    			wm.addView(fvFloatDialog, params);	    		
	    	
	    	    }
	    	    else
	    	    {
	    	    	//updatePosition();
	    	    }
	    	    //	ShowDialog(xr);
//	    		SeekBarDialog sd = new SeekBarDialog(ctx);
//	    		sd.setMessage("tiaojie");
//	    		sd.setTitle("错误");
//	    		sd.show();
	    		//new AlertDialog.Builder(ctx).setTitle("错误").setMessage("没有请求的视频文件").setPositiveButton("确定", null).show(); 
	    		break;
	    }
	    
		return true;
	}
	
	
	public void cleanFloatIcon()
	{
		if(fvFloatDialog != null)	
		{
			wm.removeView(fvFloatDialog); 
			fvFloatDialog =null;
			//wm.addView(fvFloat, params);
//			final ViewGroup parent=(ViewGroup)fvFloat.getParent();
//			if(parent!=null)
//			{
//				parent.removeView(fvFloat);
//			}
//			fvFloat = null;
		}
	}
//	@Override
//	protected void onDraw(Canvas canvas) {
//		// TODO Auto-generated method stub
//		/*super.onDraw(canvas);
//		float1 += 0.001f;
//		float2 += 0.001f;	
//		
//		if(float2 > 1.0){
//			float1 = 0.0f;
//			float2 = 0.01f;
//		}
//		//this.setText("");
//		//float len = this.getTextSize() * text.length();
//		Shader shader = new LinearGradient(0, 0, len, 0, 
//				new int[] { Color.YELLOW, Color.RED },  new float[]{float1, float2},
//				TileMode.CLAMP);
//		Paint p = new Paint();
//		p.setShader(shader);
//		p.setTypeface(Typeface.DEFAULT_BOLD);
//		canvas.drawText(text, 0, 10, p);*/
//		
//
//		
//	}
	
//	private Runnable update = new Runnable() {
//        public void run() {
//        	//MyTextView.this.update();
//        	handler.postDelayed(update, 5);
//        }
//    };
//	
//	private void update(){
//		postInvalidate();	//更新界面
//	}
//	
	
	

	//更新浮动窗口位置参数
	 private void updatePosition(){
		 // View的当前位置
		
		 params.x = (int)( x - startX);
		 params.y = (int) (y - startY);
		 wm.updateViewLayout(this, params);
		 
		 Log.v("updatePosition "+ params.x + "  "+ params.y,"updatePosition");
		 
		 //floatIconSpeed.setText(""+ xr);
	 }
	 
	 public static class FloatFitnessDate
	 {
		    public float floatIconSpeedValue;
		    public float floatIconInclineValue;
		    public int floatIconTimeValue;
		    public float floatIconDistanceValue;
		    public int floatIconHeartbeatValue;
		    public float floatIconCalorieValue;
		    
	 }
	 
//
// 	public void ShowDialog(final float xcordi)
// 	{
// 		if(xcordi >90 && xcordi<450)
// 			return;
// 			
// 		final AlertDialog.Builder popDialog = new AlertDialog.Builder(ctx);
// 		final SeekBar seek = new SeekBar(ctx);
// 		final int setValue = 0;
// 		seek.setMax(100);
// 		seek.setPadding(5, 20, 5, 5);
// 		popDialog.setIcon(android.R.drawable.btn_star_big_on);
// 				
// 		LinearLayout linear=new LinearLayout(ctx); 
// 		//LayoutParams paras = new LayoutParams();
// 		
// 		//linear.setLayoutParams(params)
// 		linear.setOrientation(1); 
// 		final TextView text=new TextView(ctx); 
// 		text.setPadding(5, 10, 5, 5);
// 	    text.setText("Hello Android"); 
//
// 		if(xcordi < 90)
// 		{
// 			popDialog.setTitle("Please Select incline 0-"+ModeSelect.maxIncline);
// 	
// 		}
// 		else if (xcordi > 450)
// 		{
// 			popDialog.setTitle("Please Select speed "+ModeSelect.minSpeed+"-"+ModeSelect.maxIncline);
// 	
// 		}
// 		
// 		
// 	   linear.addView(seek); 
// 	    linear.addView(text); 
// 	   popDialog.setView(linear);
// 		seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
// 		        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
// 		            //Do something here with new value
// 		    		if(xcordi < 90)
// 		    		{
// 		    			float setValue = ((float)ModeSelect.maxIncline)/100*progress;
// 		    			text.setText("incline is " + setValue);
// 		    		}
// 		    		else if (xcordi > 450)
// 		    		{ 		    			
// 		    			float setValue = ((float)ModeSelect.maxSpeed - ModeSelect.minSpeed)/100*progress+ModeSelect.minSpeed;
// 		    			text.setText("speed is " + setValue);
// 		    			
// 		    		}	        	
// 		        	
// 		        }
//
// 				public void onStartTrackingTouch(SeekBar arg0) {
// 					// TODO Auto-generated method stub
// 					
// 				}
//
// 				public void onStopTrackingTouch(SeekBar seekBar) {
// 					// TODO Auto-generated method stub
// 					
// 				}
// 		    });
// 		 
//
// 		// Button OK
// 		popDialog.setPositiveButton("OK",
// 				new DialogInterface.OnClickListener() {
// 					public void onClick(DialogInterface dialog, int which) {
// 						
// 						if(RunningNow.runningNowHandle != null)
// 						{
// 							Message msg = Message.obtain();
// 		 		    		if(xcordi < 90)
// 		 		    		{
// 		 		    			msg.what = RunningNow.INCLINE_VALUE_SET_IN_FLOAT_DIALOG;
// 	 							msg.obj = setValue;
// 	 							RunningNow.runningNowHandle.sendMessage(msg);
// 		 		    		}
// 		 		    		else if (xcordi > 450)
// 		 		    		{ 		    			
// 		 		    			msg.what = RunningNow.SPEED_VALUE_SET_IN_FLOAT_DIALOG;
// 	 							msg.obj = setValue;
// 	 							RunningNow.runningNowHandle.sendMessage(msg);
// 		 		    		}	
// 							
// 						}
// 						dialog.dismiss();
// 					}
//
// 				});
//
//
// 		popDialog.create();
// 		popDialog.show();
//         
// 	}
// 	

	
	 
}
