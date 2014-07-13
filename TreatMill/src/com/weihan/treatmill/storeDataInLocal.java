package com.weihan.treatmill;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class storeDataInLocal {
	public static String CURRENT_USER_ID = "current_user_id";
	public static String CURRENT_USER_NAME = "current_user_name";
    static void saveConfig(Context ctx,String key, String value)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("user_name_id",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
    static String getConfig(Context ctx,String key)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("user_name_id",Context.MODE_PRIVATE);
        Log.v("sharepreference = "+ sharedPreferences.getString(key,""),"sharepreference = "+ sharedPreferences.getString(key,""));
        return sharedPreferences.getString(key,"");
    }
}
