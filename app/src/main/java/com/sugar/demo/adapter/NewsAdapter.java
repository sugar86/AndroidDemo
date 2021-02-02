package com.sugar.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.sugar.demo.R;
import com.sugar.demo.entity.News;

import java.util.List;

public class NewsAdapter extends BaseAdapter {

    private List<News> mData;
    private Context mContext;

    public NewsAdapter(List<News> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_layout, parent, false);
//        ImageView img_icon = (ImageView) convertView.findViewById(R.id.img_icon);
//        TextView title = (TextView) convertView.findViewById(R.id.tv_title);
//        TextView content = (TextView) convertView.findViewById(R.id.tv_content);
//        img_icon.setBackgroundResource(mData.get(position).getaIcon());
//        title.setText(mData.get(position).getTitle());
//        content.setText(mData.get(position).getContent());
//        return convertView;
//    }


    //BaseAdapter 优化
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) { // 只需实例化一次
            holder = new ViewHolder();
            // 获取模板布局
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_layout
                    , parent
                    , false);
            holder.img_icon = (ImageView) convertView.findViewById(R.id.img_icon);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder); // 把 holder 缓存到 convertView
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 设置视图对象的值
        holder.img_icon.setBackgroundResource(mData.get(position).getaIcon());
        holder.title.setText(mData.get(position).getTitle());
        holder.content.setText(mData.get(position).getContent());
        return convertView;
    }

    static class ViewHolder {
        ImageView img_icon;
        TextView title;
        TextView content;
    }
}