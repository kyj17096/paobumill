package com.weihan.treatmill;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class VideoList {

	private static Cursor cursor;
	public static ArrayList<VideoInfo> videoList;
		
    static String[] mediaColumns = new String[]{
				MediaStore.Video.Media.DATA,
				MediaStore.Video.Media._ID,
				MediaStore.Video.Media.TITLE,
				MediaStore.Video.Media.MIME_TYPE
		};
		
		//���ȼ���SDcard�����е�video
    public static void initSceneVideoList(Context ctx)
    {
    	cursor = ctx.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);
		
    	videoList = new ArrayList<VideoInfo>();
    		if (cursor == null)
    			return;
    		if(cursor.moveToFirst()){
    			do{
    				VideoInfo info = new VideoInfo();
    				
    				info.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
    				info.mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
    				info.title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
    				videoList.add(info);
    				
    			}while(cursor.moveToNext());
    		}
    	Log.v("video list size "+videoList.size(),"video list size ");
    }

    public static String getPath(int i)
    {
    	return videoList.get(i).filePath;
    }
	
    public static String getPath(String name)
    {
    	for(VideoInfo a: videoList)
    	{
    		Log.v("video name "+a.title,"video name  "+name);
    		if(a.title.compareTo(name)==0)
    			return a.filePath;
    	}
    	Log.v("get file path failed","get file path failed");
		return null;
    }
    
	static class VideoInfo{
		String filePath;
		String mimeType;
		String thumbPath;
		String title;
	}
	
	
}
