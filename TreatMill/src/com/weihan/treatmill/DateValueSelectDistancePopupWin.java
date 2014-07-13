package com.weihan.treatmill;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class DateValueSelectDistancePopupWin extends PopupWindow implements OnClickListener{


	private Button btnBack, btnSave;
	private TextView tvDisplaySelect;
	private View mMenuView;
	private Context ctx;
	private int hValue = 3;;
	private int lValue = 3;
	private int returnValue;
	 private boolean wheelScrolled = false;
	private FrameLayout.LayoutParams layoutPara;
	
	public DateValueSelectDistancePopupWin (Context context, View v,int select) {
		super(context);
		ctx = context;
		LayoutInflater inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.task_value_select_for_distance_goal, null);
		btnBack = (Button) mMenuView.findViewById(R.id.goal_value_select_distance_back);
		btnSave = (Button) mMenuView.findViewById(R.id.goal_value_select_distance_save);
		tvDisplaySelect = (TextView) mMenuView.findViewById(R.id.goal_value_select_distance_display);
		btnBack.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		
		
		 initWheelH(ctx,R.id.distance_select_value_in_wheel_h);
		 initWheelL(ctx,R.id.distance_select_value_in_wheel_l);
	//	 initWheelScale(ctx,R.id.calorie_select_value_in_wheel_scale);
//		btnSelectCalorie = (Button) mMenuView.findViewById(R.id.goal_calorie_select);
//		btnSelectHistory = (Button) mMenuView.findViewById(R.id.goal_history);
//		btnCncel = (Button) mMenuView.findViewById(R.id.goal_cancle);
	//	initGoalSelectListView();
		//layoutPara = (FrameLayout.LayoutParams) mMenuView.getLayoutParams();
	//	mMenuView.setLayoutParams(layoutPara);
	//	datePicker = (DatePicker) findViewById(R.id.dpPicker);
		//设置按钮监听
	//	btnSelectDistance.setOnClickListener(this);
	//	btnSelectCalorie.setOnClickListener(this);
	//	btnSelectHistory.setOnClickListener(this);
//		btnCncel.setOnClickListener(this);
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		//this.setAnimationStyle();
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	     int[] location = new int[2];  
	     v.getLocationOnScreen(location);  
		//this.showAtLocation(context.findViewById(), showAtLocation, x, y);
		this.showAtLocation(v, Gravity.NO_GRAVITY, location[0]-v.getWidth(), location[1]);  
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {  
		case R.id.goal_value_select_distance_back:  
			dismiss();
			break;
		case R.id.goal_value_select_distance_save:
			if(hValue == 0&& lValue == 0)
			{	
				new AlertDialog.Builder(ctx).setTitle(R.string.not_allowed_data).setMessage(R.string.distance_can_not_be_0).setPositiveButton(R.string.ok, null).show(); 
	    		return;
			}
			else
			{
				returnValue = (hValue*10+lValue);
				storeGoalInfoInDB(returnValue);
				dismiss();
			}
			break;
//            break;  
//        case R.id.goal_calorie_select:                 
//            break;  
//        case R.id.goal_history:  
//            break;  
//        case R.id.goal_cancle: 
//        	dismiss();
//            break;  
        default:  
            break;  
        }  
          
              
    }  
	   private void initWheelH(Context ctx,int id) {
	        WheelView wheel = (WheelView) mMenuView.findViewById(id);
	        wheel.setViewAdapter(new NumericWheelAdapter(ctx, 0, 9));
	        wheel.setCurrentItem(3);
	        
	        wheel.addChangingListener(changedListener);
	        wheel.addScrollingListener( new OnWheelScrollListener() {
		        public void onScrollingStarted(WheelView wheel) {
			            wheelScrolled = true;
			        }
			        public void onScrollingFinished(WheelView wheel) {
			            wheelScrolled = false;
			        	hValue = wheel.getCurrentItem();
			        	updateStatus();
			        }
			    });
	        wheel.setCyclic(true);
	        wheel.setInterpolator(new AnticipateOvershootInterpolator());
	    }

	   private void initWheelL(Context ctx,int id) {
	        WheelView wheel = (WheelView) mMenuView.findViewById(id);
	        wheel.setViewAdapter(new NumericWheelAdapter(ctx, 0, 9));
	        wheel.setCurrentItem(3);
	        
	        wheel.addChangingListener(changedListener);
	        wheel.addScrollingListener( new OnWheelScrollListener() {
		        public void onScrollingStarted(WheelView wheel) {
			           wheelScrolled = true;
			        }
			        public void onScrollingFinished(WheelView wheel) {
			            wheelScrolled = false;
			        	
			        	lValue = wheel.getCurrentItem();
			        	updateStatus();
			        }
			    });
	        wheel.setCyclic(true);
	        wheel.setInterpolator(new AnticipateOvershootInterpolator());
	    }

	    // Wheel scrolled listener
	   // OnWheelScrollListener scrolledListener =;
	    
	    
	    
	    // Wheel changed listener
	    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
	        public void onChanged(WheelView wheel, int oldValue, int newValue) {
	            
	                updateStatus();
	            
	        }
	    };
	   
	   public void updateStatus()
	   {
		   tvDisplaySelect.setText(""+(hValue*10+lValue));
	   }
	   
		public void storeGoalInfoInDB(int value)
		{
          Log.v("store fitness data", "store fitness data");
			DBForTreatmill dbScene = new DBForTreatmill(ctx);	
	   		 ContentValues cv = new ContentValues();
	   		 
	   	  String currentUserId = storeDataInLocal.getConfig(ctx,storeDataInLocal.CURRENT_USER_ID);
          cv.put(DBForTreatmill.USER_ID, currentUserId);
          cv.put(DBForTreatmill.HISTORY_CALORIE_OR_DISTANCE,DBForTreatmill.HISTORY_DISTANCE);
          cv.put(DBForTreatmill.HISTORY_GOAL_VALUE, value);
          cv.put(DBForTreatmill.HISTORY_GOAL_FINISH_PROCESS, 0);

          Calendar c = Calendar.getInstance();
          cv.put(DBForTreatmill.HISTORY_YEAR, c.get(Calendar.YEAR));     
          cv.put(DBForTreatmill.HISTORY_MONTH, c.get(Calendar.MONTH));   
          cv.put(DBForTreatmill.HISTORY_DAY, c.get(Calendar.DAY_OF_MONTH));        
	   	  dbScene.insert(DBForTreatmill.TABLE_HISTORY_GOAL, cv);
	   	  ModeSelect.mHandler.sendEmptyMessage(ConstantValue.MODE_SELECT_UI_UPDATE_ALL);
	   		dbScene.close();
		}
		
}
