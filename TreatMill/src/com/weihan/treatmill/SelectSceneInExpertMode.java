package com.weihan.treatmill;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class SelectSceneInExpertMode  extends Activity {
	 ListView lv;
	 private int currentMode;
	 private int currentTask;
	 private int currentTaskValue;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_scene_in_expert_mode);  
        
   	 currentMode = getIntent().getExtras().getInt("mode");
   	 currentTask = getIntent().getExtras().getInt("task");
   	 currentTaskValue = getIntent().getExtras().getInt("task_value");
        lv =(ListView)findViewById(R.id.select_scene);
        
      ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
      int[] id = new int[]{R.drawable.grassland_mode_not_selected,R.drawable.mountain_land_mode_not_selected,
    		  R.drawable.plain_mode_not_selected,R.drawable.sea_mode_not_selected,R.drawable.city_mode_not_selected};
      for(int i=0;i<5;i++)  
      {  
          HashMap<String, Object> map = new HashMap<String, Object>();  
          map.put("picName", "");
          map.put("Icon", id[i]);//ͼ����Դ��ID   
          listItem.add(map);  
      }  
      SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,
          R.layout.select_scene_item,     
         new String[] {"picName","Icon"},   
          new int[] {R.id.scene_name,R.id.scene_icon});           
      
      lv.setAdapter(listItemAdapter);    
      //��ӵ��  
      lv.setOnItemClickListener(new OnItemClickListener() {  
          @Override  
          public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                  long arg3) {
        	  String path = null;
        	  if(arg2 == 0)
        	  {
        		  path = VideoList.getPath("987654321grass");
        	  }
        	  else if(arg2 == 1)
        	  {
        		  path =  VideoList.getPath("987654321mountain");
        	  }
        	  else if(arg2 == 2)
        	  {
        		  path =  VideoList.getPath("987654321park");
        	  }
        	  else if(arg2 == 3)
        	  {
        		  path =  VideoList.getPath("987654321sea");
        	  }
        	  else if(arg2 == 4)
        	  {
        		  path =  VideoList.getPath("987654321street");
        	  }
        	  
        	  if(path == null)
        	  {
        		  new AlertDialog.Builder(SelectSceneInExpertMode.this).setTitle(R.string.error).
        		  setMessage(R.string.no_exist_video_file).setPositiveButton(R.string.ok, null).show(); 
        		  return;
        	  }
        	  SelectSceneInExpertMode.this.finish();
    		Intent intent = new Intent(SelectSceneInExpertMode.this, RunningNow.class);
    		intent.putExtra("mode", currentMode);
    		intent.putExtra("task", currentTask);
    	    intent.putExtra("task_value", currentTaskValue);
    	    intent.putExtra("mediaPath", path);
    		Log.v("click calorie mode","click calorie mode");
    		startActivity(intent);
          }  
      });   
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
