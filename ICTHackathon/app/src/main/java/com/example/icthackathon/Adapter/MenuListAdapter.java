package com.example.icthackathon.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.icthackathon.Data.StoreMenuModel;
import com.example.icthackathon.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MenuListAdapter extends BaseAdapter {

    private ArrayList<StoreMenuModel> storeMenuModelArrayList = new ArrayList<>();

    public MenuListAdapter(ArrayList<StoreMenuModel> array) {
        this.storeMenuModelArrayList = array;
    }


    @Override
    public int getCount() {
        return storeMenuModelArrayList.size();
    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menu_list_item, parent, false);
        }
        //메뉴이름
        TextView menu_list_item_name = (TextView) convertView.findViewById(R.id.menu_list_item_name);
        //메뉴 가격 (원)
        TextView menu_list_item_KW = (TextView) convertView.findViewById(R.id.menu_list_item_KW);
        //외화단위
        TextView menu_list_item_currency = (TextView) convertView.findViewById(R.id.menu_list_item_currency);
        //외화 가격
        TextView menu_list_item_money = (TextView) convertView.findViewById(R.id.menu_list_item_money);
        TextView menu_list_item_star_rating = (TextView) convertView.findViewById(R.id.menu_list_item_star_rating);
        TextView menu_list_item_comment_number = (TextView) convertView.findViewById(R.id.menu_list_item_comment_number);
        ImageView menu_image = (ImageView) convertView.findViewById(R.id.imageview_menu);


        StoreMenuModel item = storeMenuModelArrayList.get(pos);

        menu_list_item_name.setText(item.getName());
        menu_list_item_KW.setText(item.getPrice());
        //language 별로 다르게
        menu_list_item_currency.setText("USD");
        menu_list_item_money.setText(String.valueOf(Integer.parseInt(item.getPrice()) / 1200));

        menu_list_item_star_rating.setText(item.getMenu_score());
        menu_list_item_comment_number.setText(item.getMenu_count());

        String baseurl = "https://www.taky.co.kr/images/hack/";
        String fullurl = baseurl + item.getImage();
        Log.e("image Full url",fullurl);
        Picasso.get().get()
                .load(fullurl)
                .fit()
                .into((ImageView) menu_image);


        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return storeMenuModelArrayList.get(position);
    }

    public static void pricedigittextView(final TextView textView) {
        final DecimalFormat decimalFormat = new DecimalFormat("###,###");
        final String[] result = {""};
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!result[0].equals(s.toString()) && !s.toString().equals("")) {

                    result[0] = decimalFormat.format(Integer.parseInt(s.toString().replaceAll(",", "")));   // 에딧텍스트의 값을 변환하여, result에 저장.
                    textView.setText(result[0]);

                } else if (s.equals("")) {
                    result[0] = "";
                    textView.setText(result[0]);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}



