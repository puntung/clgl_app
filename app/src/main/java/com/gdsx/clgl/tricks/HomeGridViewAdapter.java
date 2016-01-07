package com.gdsx.clgl.tricks;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdsx.clgl.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by mglory on 2015/12/21.
 */
public class HomeGridViewAdapter extends BaseAdapter {
    private Context context;
    private String[] str;
    public HomeGridViewAdapter(Context context,String[] str) {
        this.context = context;
        this.str = str;
    }

    @Override
    public int getCount() {
        return str.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.home_grid_view,null);
             holder.icon_img = (ImageView)convertView.findViewById(R.id.homegridview_icon);
            holder.desc_txt = (TextView)convertView.findViewById(R.id.homegridview_txt);
            convertView.setTag(holder);
        }else {
            holder = (Holder)convertView.getTag();
        }
        holder.icon_img.setImageDrawable(context.getResources().getDrawable(R.drawable.kog));
        holder.desc_txt.setText(str[position]);
        return convertView;
    }

    class Holder {
        ImageView icon_img;
        TextView  desc_txt;
    }
}
