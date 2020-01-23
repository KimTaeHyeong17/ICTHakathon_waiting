package com.example.icthackathon.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.icthackathon.Adapter.MenuCommentAdapter;
import com.example.icthackathon.Data.MenuCommentModel;
import com.example.icthackathon.Data.StoreStatus;
import com.example.icthackathon.Network.Network;
import com.example.icthackathon.R;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MenuDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout tab_1, tab_2,tab_1_container,tab_2_container;
    private TextView tv_tab_1, tv_tab_2,tv_menu_description;
    private ImageView img_menu;

    private ListView menuCommentListview;
    private MenuCommentAdapter adapter;

    private ArrayList<MenuCommentModel> menuCommentModelArrayList = new ArrayList<>();
    private String language;
    String imgURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);
        Intent intent = getIntent();
        imgURL = intent.getStringExtra("url");
        String desc = intent.getStringExtra("tv_menu_description");
        Log.e("desc",desc);

        bindUI();
        tv_menu_description.setText(desc);
        tab_2_container.setVisibility(View.GONE);
        Locale systemLocale = getApplicationContext().getResources().getConfiguration().locale;
        language = systemLocale.getCountry(); // KR

        tab_1.setOnClickListener(this);
        tab_2.setOnClickListener(this);

        getMenuComment("4",language);
    }

    private void bindUI(){
        tab_1 = findViewById(R.id.tab_1);
        tab_2 = findViewById(R.id.tab_2);
        tab_1_container = findViewById(R.id.tab_1_container);
        tab_2_container = findViewById(R.id.tab_2_container);
        tv_tab_1 = findViewById(R.id.tv_tab_1);
        tv_tab_2 = findViewById(R.id.tv_tab_2);
        img_menu = findViewById(R.id.img_menu);
        tv_menu_description = findViewById(R.id.tv_menu_description);

        Picasso.get().get()
                .load("https://www.taky.co.kr/images/hack/"+imgURL)
                .fit()
                .into((ImageView) img_menu);

        menuCommentListview = findViewById(R.id.listview_menu_comment);
        adapter = new MenuCommentAdapter(MenuDetailActivity.this , menuCommentModelArrayList);
        menuCommentListview.setAdapter(adapter);

    }

    private void getMenuComment(String menu_serial, String language) {
        RequestParams params = new RequestParams();
        params.put("menu_serial", menu_serial);
        params.put("language", language);
        Network.post("/get_menu_review", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getString("code").equals("S01")) {
                        menuCommentModelArrayList.clear();
                        Gson gson = new Gson();
                        JSONArray data = response.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            String jsonstr = data.get(i).toString();
                            MenuCommentModel menuCommentModel = gson.fromJson(jsonstr, MenuCommentModel.class);
                            menuCommentModelArrayList.add(menuCommentModel);
                        }
                        Log.e("commet",String.valueOf(menuCommentModelArrayList.size()));
                        adapter.notifyDataSetChanged();
                    } else {
                        String message = response.getString("message");
                        Toast.makeText(MenuDetailActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//onsucess

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("onFailure", "onFailure : " + statusCode);
            }
        });//network

    }//getWaitingState


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab_1:
                tab_1_container.setVisibility(View.VISIBLE);
                tab_2_container.setVisibility(View.GONE);
                tv_tab_1.setTextColor(Color.BLACK);
                tv_tab_2.setTextColor(Color.GRAY);
                tv_tab_1.setBackground(ContextCompat.getDrawable(this, R.drawable.border_underline));
                tv_tab_2.setBackground(null);

                break;
            case R.id.tab_2:
                tab_1_container.setVisibility(View.GONE);
                tab_2_container.setVisibility(View.VISIBLE);
                tv_tab_1.setTextColor(Color.GRAY);
                tv_tab_2.setTextColor(Color.BLACK);
                tv_tab_1.setBackground(null);
                tv_tab_2.setBackground(ContextCompat.getDrawable(this, R.drawable.border_underline));
                break;
        }
    }
}
