package com.weihan.treatmill;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MyFloatSecurityDialog extends LinearLayout{

	public static Handler mHandler;
	private Context ctx;
	private TextView tv;
	public static final int ERROR_MESSAGE = 1;
	public static WindowManager.LayoutParams params = new WindowManager.LayoutParams(); 

	public MyFloatSecurityDialog(Context context) {
		super(context);
		
		ctx = context;
		View v =View.inflate( context, R.layout.security_lock,null);	
		this.addView(v);
		
		tv = (TextView)findViewById(R.id.security_error_info);
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what)
				{
				case ERROR_MESSAGE:
					tv.setText((String)msg.obj);
					break;
				}
				
				super.handleMessage(msg);
			}
		};
		
	

	}
	 
}


