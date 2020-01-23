package com.example.icthackathon.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.icthackathon.Data.MenuCommentModel;
import com.example.icthackathon.R;

import java.util.ArrayList;

public class MenuCommentAdapter extends BaseAdapter {

    private ArrayList<MenuCommentModel> menuCommentModelArrayList = new ArrayList<>();
    private LayoutInflater myInflater;

    public MenuCommentAdapter(Activity context, ArrayList<MenuCommentModel> array) {
        this.menuCommentModelArrayList = array;
        myInflater  = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return menuCommentModelArrayList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        ViewHolder v;
        if (convertView == null) {
            convertView = myInflater.inflate(R.layout.menu_comment_list_item, null);
            v = new ViewHolder();
            v.tv_menu_comment_name = (TextView)convertView.findViewById(R.id.tv_menu_comment_name);
            v.tv_menu_comment = (TextView)convertView.findViewById(R.id.tv_menu_comment);

            convertView.setTag(v);
        }else{
            v = (ViewHolder) convertView.getTag();
        }

        MenuCommentModel menuCommentModel = menuCommentModelArrayList.get(position);

        v.tv_menu_comment_name.setText(menuCommentModel.getNick_name());
        v.tv_menu_comment.setText(menuCommentModel.getComment());


        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return menuCommentModelArrayList.get(position);
    }


    static class ViewHolder
    {
        public TextView tv_menu_comment_name;
        public TextView tv_menu_comment;
//        public TextView coupon_code;
//        public TextView coupon_period;
    }

}
