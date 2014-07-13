package com.weihan.treatmill;
  
import android.content.Context;  
import android.database.Cursor;  
import android.graphics.Color;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.SimpleCursorAdapter;  
  
public class MySimpleCursorAdapter extends SimpleCursorAdapter {  
  
    public MySimpleCursorAdapter(Context context, int layout, Cursor c,  
            String[] from, int[] to) {  
        super(context, layout, c, from, to);  
        // TODO Auto-generated constructor stub  
  
    }  
  
    @Override  
    public View getView(final int position, View convertView, ViewGroup parent) {  
        // TODO Auto-generated method stub  
        View view = null;  
        if (convertView != null) {  
            view = convertView;   
  
        } else {  
            view = super.getView(position, convertView, parent);  
  
        }  
  
        int[] colors = { Color.WHITE, Color.rgb(219, 238, 244) };//RGB茅垄艙猫鈥奥� 
  
        view.setBackgroundColor(colors[position % 2]);// 忙炉锟矫┡♀�item盲鹿鈥姑┾�麓茅垄艙猫鈥奥裁ぢ革拷氓锟脚� 
  
        return super.getView(position, view, parent);  
    }  
  
}  