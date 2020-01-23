package com.example.icthackathon.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.icthackathon.Data.NearSpotModel;
import com.example.icthackathon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ItemClick itemClick;
    public interface ItemClick {
        public void onClick(View view,int position);
    }
    //아이템 클릭시 실행 함수 등록 함수
    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    private LayoutInflater inflater;
    private ArrayList<NearSpotModel> nearSpotModelArrayList;

    public RecyclerAdapter(Context ctx, ArrayList<NearSpotModel> imageModelArrayList) {

        inflater = LayoutInflater.from(ctx);
        this.nearSpotModelArrayList = imageModelArrayList;
    }

    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.MyViewHolder holder, final int position) {

//        이미지 설정
        String baseurl = "https://www.taky.co.kr/images/hack/";
        String fullurl = baseurl + nearSpotModelArrayList.get(position).getImage1();
        Log.e("image Full url",fullurl);
        Picasso.get().get()
                .load(fullurl)
                .fit()
                .into((ImageView) holder.iv);

        holder.time.setText(nearSpotModelArrayList.get(position).getName());


        //카테고리 나누기 1: 맛집, 2:카페, 3:쇼핑,4:관광지,5:숙박
        switch (nearSpotModelArrayList.get(position).getCategory()){
            case "1":
                holder.tv_place_category.setText("Treanding Resturant");
                break;
            case "2":
                holder.tv_place_category.setText("Cafe");
                break;
            case "3":
                holder.tv_place_category.setText("Shopping");
                break;
            case "4":
                holder.tv_place_category.setText("Tour Spot");
                break;
            case "5":
                holder.tv_place_category.setText("Accomodation");
                break;

        }




        //1. 마지막 3글자 자르기 ABCDEFG
        String str = nearSpotModelArrayList.get(position).getDistance();
        String result = str.substring(str.length()-3, str.length());
        System.out.println(result);
        //결과값EFG

        String ETA = String.valueOf(Integer.parseInt(result));
        holder.tv_spot_eta.setText(ETA);
        holder.tv_place_rating.setText(nearSpotModelArrayList.get(position).getScore());
        holder.tv_comment_number.setText(nearSpotModelArrayList.get(position).getComment());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick != null){
                    itemClick.onClick(v, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return nearSpotModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView time, tv_place_category,tv_spot_eta,tv_place_rating,tv_comment_number;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);

            time = (TextView) itemView.findViewById(R.id.tv);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            tv_place_category = itemView.findViewById(R.id.tv_place_category);
            tv_spot_eta = itemView.findViewById(R.id.tv_spot_eta);
            tv_place_rating = itemView.findViewById(R.id.tv_place_rating);
            tv_comment_number = itemView.findViewById(R.id.tv_comment_number);
        }

    }

}