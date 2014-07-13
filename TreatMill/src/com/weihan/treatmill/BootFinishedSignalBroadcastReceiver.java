package com.weihan.treatmill;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


//�˹㲥����
public class BootFinishedSignalBroadcastReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		// ��ʾ�㲥��Ϣ
		
		//Toast.makeText(context,"BOOT_COMPLETED~~~~~~~~~~~~~~~~", Toast.LENGTH_LONG).show();
		Log.i("my_tag", "BOOT_COMPLETED~~~~~~~~~~~~~~~~");
		
		//Intent intent1 = new Intent();
		//intent1.setAction("com.amaker.ch08.app.MY_SERVER");
		
		Intent intent1 = new Intent(context, StartActivity.class);
		intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent1);
	}
}
