package com.weihan.treatmill;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpAboutActivity extends Activity  {
	 
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState); 
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       setContentView(R.layout.help_about_me);  

   
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