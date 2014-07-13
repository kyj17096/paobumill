package com.weihan.treatmill;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KYJ on 13-6-22.
 */
public class UserListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<String> userList;

    public UserListAdapter (Context context,ArrayList<String> ul){
        this.mInflater = LayoutInflater.from(context);
        userList = ul;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return userList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {

            holder=new ViewHolder();

            convertView = mInflater.inflate(R.layout.user_list_item, null);
            holder.name = (TextView)convertView.findViewById(R.id.user_name);
            convertView.setTag(holder);

        }else {

            holder = (ViewHolder)convertView.getTag();
        }

        holder.name.setText((String)userList.get(position));
        if(position == getCount()-1)
        {
            holder.name.setTextColor(Color.GREEN);
        }
        return convertView;
    }

    public final class ViewHolder{
        public TextView name;
    }
}
