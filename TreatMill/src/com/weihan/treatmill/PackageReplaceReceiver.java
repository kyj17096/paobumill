package com.weihan.treatmill;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Message;
import android.widget.Toast;

public class PackageReplaceReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
    //	if(intent.getExtras().getString("android.intent.extra.UID")== "com.bluetootapp.bluetoothmsg")
    	{
	
        		Toast.makeText(context,"PACK REPLACE", Toast.LENGTH_LONG).show();

               	context.getPackageManager().setComponentEnabledSetting(
                new ComponentName("com.bluetootapp.bluetoothmsg", "com.bluetootapp.bluetoothmsg.StartActivity"),
            	PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
            	PackageManager.DONT_KILL_APP);
    	}
    }
}
