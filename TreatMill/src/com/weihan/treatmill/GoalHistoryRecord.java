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
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class GoalHistoryRecord extends PopupWindow implements OnClickListener{
	private View mMenuView;
	private Context ctx;
	private ListView lv;
	DBForTreatmill db;
	public GoalHistoryRecord (Context context, View v,int select) {
		super(context);
		ctx = context;
		LayoutInflater inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.goal_history_record, null);
		lv = (ListView)mMenuView.findViewById(R.id.goal_history_listview);
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

		updatelistview();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {  
		
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
	
	  public void updatelistview() 
	  {
		  DBForTreatmill db = new DBForTreatmill(ctx);	
		  	String currentUserId = storeDataInLocal.getConfig(ctx,storeDataInLocal.CURRENT_USER_ID);
	        SQLiteDatabase rdb = db.getReadableDatabase();
	        Cursor cr = db.select(DBForTreatmill.TABLE_HISTORY_GOAL,null,DBForTreatmill.USER_ID+"=? ",new String[]{currentUserId},rdb);
	        
	        Log.v("history cursor size is = "+ cr.getCount(),"cursor size is = "+ cr.getCount());
	        if(cr.getCount()==0)
	            return;	        
	        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	        cr.moveToFirst();
	        do{
	        	int calOrDis = cr.getInt(cr.getColumnIndex(DBForTreatmill.HISTORY_CALORIE_OR_DISTANCE));
	        	Log.v("calOrDis "+calOrDis,"calOrDis "+calOrDis);
	        	String strCalOrDis = "";
	        	String scale = "";
	        	if(calOrDis == DBForTreatmill.HISTORY_CALORIE)
	        	{
	        		strCalOrDis = (String) ctx.getResources().getText(R.string.calorie);
	        		scale = "KJ";
	        	}
	        	else if(calOrDis == DBForTreatmill.HISTORY_DISTANCE)
	        	{
	        		strCalOrDis = (String) ctx.getResources().getText(R.string.distance);
	        		scale = "KM";
	        	}
	            Log.v("id in deb is = "+cr.getString(cr.getColumnIndex(DBForTreatmill.ID)),"id in deb is = "+cr.getString(cr.getColumnIndex(DBForTreatmill.ID)));
	            HashMap<String, Object> map = new HashMap<String, Object>();
	            int goal_value = cr.getInt(cr.getColumnIndex(DBForTreatmill.HISTORY_GOAL_VALUE));
	            
	             map.put("goal_value",""+strCalOrDis+"    "+goal_value+scale);	        
	          
	            
//	             String[] monthName = new String[]{"January","February","March","April","May","June","July","August","September",
//	                     "October","November","December"};
//	            int year = cr.getInt(cr.getColumnIndex(DBForTreatmill.HISTORY_YEAR));
//	            int month = cr.getInt(cr.getColumnIndex(DBForTreatmill.HISTORY_MONTH));
//	            int day = cr.getInt(cr.getColumnIndex(DBForTreatmill.HISTORY_DAY));
//	            
//	             map.put("goal_date",""+year+"/"+(month+1)+"/"+day);
	            
	             
	
	     		int Aleary = cr.getInt(cr.getColumnIndex(DBForTreatmill.HISTORY_GOAL_FINISH_PROCESS));
	    		int goal =  cr.getInt(cr.getColumnIndex(DBForTreatmill.HISTORY_GOAL_VALUE));
	    		float finishProcess = (float)Aleary/(float)goal; 
	            
	            map.put("goal_finish_process",""+(int)(finishProcess*100)+"%");
	             list.add(map);
	        }while(cr.moveToNext());


	        SimpleAdapter listItemAdapter2 = new SimpleAdapter(ctx,list,
	                R.layout.goal_history_list_item,
	                new String[] {"goal_value",/*"goal_date",*/"goal_finish_process"},
	                new int[] { R.id.goal_select_value_in_history, /* R.id.goal_select_date_in_history,*/
	                        R.id.goal_finish_process_in_history });

	        lv.setAdapter(listItemAdapter2);
	  }
}
