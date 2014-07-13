package com.weihan.treatmill;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class GoalForSelectPopupWin extends PopupWindow implements OnClickListener{


	private Button btnSelectDistance, btnSelectCalorie, btnSelectHistory;
	private View mMenuView;
	private View viewLocation;
	private Context ctx;
	private ListView lv;
	public GoalForSelectPopupWin (Context context, View v) {
		super(context);
		ctx = context;
		viewLocation = v;
		LayoutInflater inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.goal_select, null);
		
//		btnSelectDistance = (Button) mMenuView.findViewById(R.id.goal_distance_select);
//		btnSelectCalorie = (Button) mMenuView.findViewById(R.id.goal_calorie_select);
//		btnSelectHistory = (Button) mMenuView.findViewById(R.id.goal_history);
//		btnCncel = (Button) mMenuView.findViewById(R.id.goal_cancle);
		initGoalSelectListView();
		//���ð�ť����
//		btnSelectDistance.setOnClickListener(this);
//		btnSelectCalorie.setOnClickListener(this);
//		btnSelectHistory.setOnClickListener(this);
//		btnCncel.setOnClickListener(this);
		//����SelectPicPopupWindow��View
		this.setContentView(mMenuView);
		//����SelectPicPopupWindow��������Ŀ�
		this.setWidth(LayoutParams.WRAP_CONTENT);
		//����SelectPicPopupWindow��������ĸ�
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//����SelectPicPopupWindow��������ɵ��
		this.setFocusable(true);
		this.setBackgroundDrawable(new ColorDrawable(0x00000000));
		//����SelectPicPopupWindow�������嶯��Ч��
		//this.setAnimationStyle();
		//ʵ����һ��ColorDrawable��ɫΪ��͸��
		//ColorDrawable dw = new ColorDrawable(0xb0000000);
		//����SelectPicPopupWindow��������ı���
		//this.setBackgroundDrawable(dw);
	     int[] location = new int[2];  
	     v.getLocationOnScreen(location);  
		//this.showAtLocation(context.findViewById(), showAtLocation, x, y);
		this.showAtLocation(v, Gravity.NO_GRAVITY, location[0]-v.getWidth(), location[1]);  
		//mMenuView���OnTouchListener�����жϻ�ȡ����λ�������ѡ������������ٵ�����
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
//        case R.id.goal_distance_select:  
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
	
	void initGoalSelectListView()
	{
		
	  lv = (ListView)mMenuView.findViewById(R.id.goal_select_listview);	
      ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
   
          HashMap<String, Object> map = new HashMap<String, Object>();  
          map.put("action_name", ctx.getResources().getString(R.string.aim_distance));
          map.put("actionIcon",R.drawable.arrow_right);//ͼ����Դ��ID   
          listItem.add(map);  
          HashMap<String, Object> map2 = new HashMap<String, Object>();  
          map2.put("action_name", ctx.getResources().getString(R.string.aim_calorie));
          map2.put("actionIcon",R.drawable.arrow_right);//ͼ����Դ��ID   
          listItem.add(map2); 
          HashMap<String, Object> map3 = new HashMap<String, Object>();  
          map3.put("action_name", ctx.getResources().getString(R.string.history_aim));
          map3.put("actionIcon",R.drawable.arrow_right);//ͼ����Դ��ID   
          listItem.add(map3); 
      SimpleAdapter listItemAdapter = new SimpleAdapter(ctx,listItem,
          R.layout.goal_select_list_item,     
         new String[] {"action_name","actionIcon"},   
          new int[] {R.id.goal_select_text_listview,R.id.goal_select_image_listview});           
      
      lv.setAdapter(listItemAdapter);    
      //��ӵ��  
      lv.setOnItemClickListener(new OnItemClickListener() {  
          @Override  
          public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                  long arg3) {  
        	  int select = arg2;
        	  if(select == 0)
        	  {
        		  new DateValueSelectDistancePopupWin(ctx,viewLocation,0);
        		  
        	  }
        	  else if (select == 1)
        	  {
        		  new DateValueSelectCaloriePopupWin(ctx,viewLocation,0);
        	  }
        	  else if (select == 2)
        	  {
        		  new GoalHistoryRecord(ctx,viewLocation,0);
        	  }
        	 
          }  
      });   
	}

}
