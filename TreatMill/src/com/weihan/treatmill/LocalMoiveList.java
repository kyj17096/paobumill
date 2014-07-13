package com.weihan.treatmill;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 锟斤拷实锟斤拷锟斤拷锟角斤拷通锟斤拷锟斤拷锟絊DCard锟较碉拷Video锟斤拷息
 * 锟斤拷MediaStore锟叫ｏ拷MediaStore.Video.Media锟叫撅拷锟斤拷Video锟斤拷锟斤拷锟较拷锟�
 * 同时MediaStore.Video.Thumbnails锟叫猴拷锟叫革拷锟斤拷video锟斤拷应锟斤拷锟斤拷锟斤拷图锟斤拷息
 * 
 * @author Administrator
 *
 */
public class LocalMoiveList extends Activity {
	
	private Cursor cursor;
	ArrayList<VideoInfo> videoList;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.local_video_list);

	    GridView gridview = (GridView) findViewById(R.id.gridview);
	    init();
	    gridview.setAdapter(new ImageAdapter(this));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	    		Intent intent = new Intent(LocalMoiveList.this, RunningNow.class);
	    		intent.putExtra("mode", ConstantValue.ARCADE_MODE);
	    		intent.putExtra("mediaPath", videoList.get(position).filePath);
	    		startActivity(intent);
	    		
	    		LocalMoiveList.this.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
	            Toast.makeText(LocalMoiveList.this, "" + position, Toast.LENGTH_SHORT).show();
	            }
	        });
	    }  
	
	
	private void init(){
		String[] thumbColumns = new String[]{	
				MediaStore.Video.Thumbnails.DATA,
				MediaStore.Video.Thumbnails.VIDEO_ID			
		};
		
		String[] mediaColumns = new String[]{
				MediaStore.Video.Media.DATA,
				MediaStore.Video.Media._ID,
				MediaStore.Video.Media.TITLE,
				MediaStore.Video.Media.MIME_TYPE
		};
		
		//锟斤拷锟饺硷拷锟斤拷SDcard锟斤拷锟斤拷锟叫碉拷video
		cursor = this.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);
		
		videoList = new ArrayList<VideoInfo>();
		
		if(cursor.moveToFirst()){
			do{
				VideoInfo info = new VideoInfo();
				
				info.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
				info.mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
				info.title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
			
				//锟斤拷取锟斤拷前Video锟斤拷应锟斤拷Id锟斤拷然锟斤拷锟捷革拷ID锟斤拷取锟斤拷Thumb
				int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
				
				Log.v("test " + id,"test "+ id);  
				String selection = MediaStore.Video.Thumbnails.VIDEO_ID +"=?";
				String[] selectionArgs = new String[]{id+""};
				Cursor thumbCursor = this.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, selection, selectionArgs, null);
				Log.v("test " + id +" "+thumbCursor.getCount(),"test "+ id); 
				if(thumbCursor.moveToFirst()){
					   Log.v("Thumbnails","Thumbnails"); 
					info.thumbPath = thumbCursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
				}
				else
				{
					BitmapFactory.Options options = new BitmapFactory.Options();  
	                options.inDither = false;  
					options.inPreferredConfig = Bitmap.Config.ARGB_8888;  
					info.b = MediaStore.Video.Thumbnails.getThumbnail(getContentResolver(), id,  Images.Thumbnails.MICRO_KIND, options);  
					Log.v("b "+ info.b.describeContents() ,"b "+ info.b.describeContents()); 
				}
				
				//然锟斤拷锟斤拷锟斤拷氲絭ideoList
				videoList.add(info);
				
			}while(cursor.moveToNext());
		}

	}
	
	static class VideoInfo{
		String filePath;
		String mimeType;
		String thumbPath;
		String title;
		Bitmap b;
	}
	

	   private class ImageAdapter extends BaseAdapter{  
		   private Context mContext;  
		
		        public ImageAdapter(Context context) {  
		            this.mContext=context;  
		        }  
		  
		        @Override  
		        public int getCount() {  
		           return videoList.size();  
		        }  
		  
		        @Override  
		        public Object getItem(int position) {  
		            return null;  
		        }  
	  
		        @Override  
		        public long getItemId(int position) {  
		            // TODO Auto-generated method stub  
		            return 0;  
		        }  
		  
		        @Override  
		        public View getView(int position, View convertView, ViewGroup parent) {  
	            //锟斤拷锟斤拷一锟斤拷ImageView,锟斤拷示锟斤拷GridView锟斤拷  
		        	ViewHolder holder = null;  
		        	if(convertView == null){  
		        	holder = new ViewHolder();  
		        	convertView = LayoutInflater.from(mContext).inflate(R.layout.media_list_item, null);  
		        	holder.thumbImage = (ImageView)convertView.findViewById(R.id.image);  
		        	holder.titleText = (TextView)convertView.findViewById(R.id.media_name);  
		        	convertView.setTag(holder);  
		        	}else{  
		        		holder = (ViewHolder)convertView.getTag();  
		        	}  
		               //锟斤拷示锟斤拷息  
		        	holder.titleText.setText(videoList.get(position).title);  
		        	if(videoList.get(position).thumbPath != null){  
		        	   holder.thumbImage.setImageURI(Uri.parse(videoList.get(position).thumbPath));
		        	   Log.v("viewhold","viewhold");
		        	}  
		        	else
		        	{
		        		holder.thumbImage.setImageBitmap(videoList.get(position).b);
		        	}
		        	             
		        	return convertView;  
		        }  
		        	   
		        	 class ViewHolder{  
		        	         ImageView thumbImage;  
		        	        TextView titleText;  
		            }

		          
		  
		          
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
