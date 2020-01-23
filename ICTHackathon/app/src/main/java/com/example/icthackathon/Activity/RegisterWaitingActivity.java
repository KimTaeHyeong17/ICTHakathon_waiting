package com.example.icthackathon.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.icthackathon.Data.StoreStatus;
import com.example.icthackathon.Network.Network;
import com.example.icthackathon.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class RegisterWaitingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_start_lining;
    private TextView tv_adult_minus, tv_adult_plus, tv_adult_number, tv_child_minus, tv_child_plus, tv_child_number, tv_waiting_team, tv_estimated_time;

    private String url;
    private int store_serial = 1;
    private String token;
    private String language;
    private int num_adult = 2;
    private int num_child = 0;

    private StoreStatus storeStatus;
    private int accept_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_waiting);

        Log.d("Firebase", "token " + FirebaseInstanceId.getInstance().getToken());
        saveTokenLanguage();
        url = getIntent().getStringExtra("EXTRA_SESSION_ID");
        bindUI();
        getStoreState(store_serial);


        btn_start_lining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInWaitingLine(String.valueOf(store_serial), token);
//                startActivity();
            }
        });

    }

    private void saveTokenLanguage() {
        SharedPreferences pref = getSharedPreferences("info", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", FirebaseInstanceId.getInstance().getToken());
        editor.commit();
        token = FirebaseInstanceId.getInstance().getToken();


        Locale systemLocale = getApplicationContext().getResources().getConfiguration().locale;
        String strCountry = systemLocale.getCountry(); // KR

        SharedPreferences pref2 = getSharedPreferences("info", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = pref2.edit();
        editor2.putString("language", strCountry);
        editor2.commit();
        language = strCountry;
        Log.e("language", language);

    }

    private void bindUI() {
        btn_start_lining = findViewById(R.id.btn_start_lining);
        tv_adult_minus = findViewById(R.id.tv_adult_minus);
        tv_adult_plus = findViewById(R.id.tv_adult_plus);
        tv_adult_number = findViewById(R.id.tv_adult_number);
        tv_child_minus = findViewById(R.id.tv_child_minus);
        tv_child_plus = findViewById(R.id.tv_child_plus);
        tv_child_number = findViewById(R.id.tv_child_number);
        tv_waiting_team = findViewById(R.id.tv_waiting_team);
        tv_estimated_time = findViewById(R.id.tv_estimated_time);

        tv_adult_plus.setOnClickListener(this);
        tv_adult_minus.setOnClickListener(this);
        tv_child_plus.setOnClickListener(this);
        tv_child_minus.setOnClickListener(this);


        tv_adult_number.setText(String.valueOf(num_adult));
        tv_child_number.setText(String.valueOf(num_child));

    }

    private void startActivity() {
        Intent intent = new Intent(RegisterWaitingActivity.this, MainActivity.class);
        intent.putExtra("qrcode_url", url);
        startActivity(intent);
        finish();
    }


    private void getStoreState(int store_serial) {
        RequestParams params = new RequestParams();
        params.put("store_serial", String.valueOf(store_serial));
        params.put("language", language);
        Network.post("/get_store_state", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getString("code").equals("S01")) {
                        Gson gson = new Gson();
                        JSONObject data = response.getJSONObject("data");
                        String jsonstr = data.toString();
                        Log.e("jsonstr", jsonstr);

                        storeStatus = gson.fromJson(jsonstr, StoreStatus.class);
                        tv_waiting_team.setText(storeStatus.getWaiting_count());
                        tv_estimated_time.setText(storeStatus.getWaiting_time());
//                        Log.e("waitingTime",storeStatus.getWaiting_time());


                    } else {
                        String message = response.getString("message");
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//onsucess
        });//network

    }//


    private void getInWaitingLine(String store_serial, String device_token) {
        RequestParams params = new RequestParams();
        params.put("store_serial", store_serial);
        params.put("device_token", device_token);
        params.put("language", language);
        params.put("num_adult", num_adult);
        params.put("num_child", num_child);

        Network.post("/set_waiting_state", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getString("code").equals("S01")) {

                        accept_number = response.getInt("data");
                        Log.e("print data int ",String.valueOf(response.getInt("data")));
                        Log.e("print data string",response.getString("data"));
                        SharedPreferences pref2 = getSharedPreferences("info", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = pref2.edit();
                        editor2.putString("accept_number", String.valueOf(accept_number));
                        Log.e("save_accept_number",String.valueOf(accept_number));
                        editor2.commit();


                        //mainactivity로 넘어간 다음 fragment1 실행
                        startActivity();
                    } else {
                        String message = response.getString("message");

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        startActivity();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//onsucess
        });//network

    }//getWaitingState

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_adult_plus:
                num_adult++;
                tv_adult_number.setText(String.valueOf(num_adult));

                break;
            case R.id.tv_adult_minus:
                if (num_adult == 0) {
                    Toast.makeText(getApplicationContext(), "0", Toast.LENGTH_SHORT).show();

                } else {
                    num_adult--;
                    tv_adult_number.setText(String.valueOf(num_adult));
                }
                break;
            case R.id.tv_child_plus:
                num_child++;
                tv_child_number.setText(String.valueOf(num_child));

                break;
            case R.id.tv_child_minus:
                if (num_child == 0) {
                    Toast.makeText(getApplicationContext(), "0", Toast.LENGTH_SHORT).show();
                } else {
                    num_child--;
                    tv_child_number.setText(String.valueOf(num_child));
                }
                break;

        }
    }
}
