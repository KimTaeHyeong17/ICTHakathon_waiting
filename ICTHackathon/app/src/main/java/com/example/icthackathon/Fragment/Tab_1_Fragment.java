package com.example.icthackathon.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.icthackathon.Activity.PlaceDetailActivity;
import com.example.icthackathon.Activity.RegisterWaitingActivity;
import com.example.icthackathon.Adapter.RecyclerAdapter;
import com.example.icthackathon.Data.NearSpotModel;
import com.example.icthackathon.Data.StoreStatus;
import com.example.icthackathon.Dialog.CancelWaitingDialog;
import com.example.icthackathon.Network.Network;
import com.example.icthackathon.R;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;


public class Tab_1_Fragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private TextView tv_current_waiting_team, tv_current_waiting_time,tv_accepted_number,tv_my_turn_number;
    private Button btn_cancel_waiting;


    private RecyclerAdapter adapter;
    private int store_serial = 1;
    private String language;
    private String accept_number;

    private StoreStatus storeStatus;
    private ArrayList<NearSpotModel> nearSpotModelArrayList = new ArrayList<>();
    private CancelWaitingDialog cancelWaitingDialog;


    public Tab_1_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_1_, container, false);
        Locale systemLocale = getContext().getResources().getConfiguration().locale;
        language = systemLocale.getCountry(); // KR
        //접수번호 받기
        SharedPreferences prefs = getActivity().getSharedPreferences("info", MODE_PRIVATE);
        accept_number = prefs.getString("accept_number", "-1"); //키값, 디폴트값
        Log.e("frag1 accept_number",accept_number);
        bindUI();
        getStoreState(store_serial);
        getNearSpot(String.valueOf(store_serial),language);
        return view;
    }

    private void bindUI(){
        tv_current_waiting_team = view.findViewById(R.id.tv_current_waiting_team);
        tv_current_waiting_time = view.findViewById(R.id.tv_current_waiting_time);
        tv_accepted_number = view.findViewById(R.id.tv_accepted_number);
        tv_my_turn_number = view.findViewById(R.id.tv_my_turn_number);
        btn_cancel_waiting = view.findViewById(R.id.btn_cancel_waiting);

        cancelWaitingDialog = new CancelWaitingDialog(getActivity());
        cancelWaitingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                cancelWaiting(store_serial, Integer.parseInt(accept_number));

            }
        });

        btn_cancel_waiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelWaitingDialog.show();
            }
        });

        //Horizontal RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        adapter = new RecyclerAdapter(getActivity().getApplicationContext(), nearSpotModelArrayList);
        adapter.setItemClick(new RecyclerAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                //클릭시 실행될 함수 작성
                Intent intent = new Intent(getContext(), PlaceDetailActivity.class);
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

    }



    private void getStoreState(int store_serial) {
        Log.e("getStoreState","");
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
                        Log.e("jsonstr",jsonstr);

                        storeStatus = gson.fromJson(jsonstr, StoreStatus.class);
                        tv_current_waiting_team.setText(storeStatus.getWaiting_count());
                        tv_current_waiting_time.setText(storeStatus.getWaiting_time());
                        tv_accepted_number.setText(accept_number);
                        tv_my_turn_number.setText(storeStatus.getWaiting_count());


                    } else {
                        String message = response.getString("message");
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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

    }//

    private void cancelWaiting(int store_serial, int accept_number){
        Log.e("cancelWaiting","");
        Log.e("store_serial", String.valueOf(store_serial));
        Log.e("accept_number", String.valueOf(accept_number));
        RequestParams params = new RequestParams();
        params.put("store_serial", String.valueOf(store_serial));
        params.put("accept_number", accept_number);
        Network.post("/set_cancel_state", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getString("code").equals("S01")) {
                        Gson gson = new Gson();
                        Toast.makeText(getContext(),"취소되었습니다",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getContext(), RegisterWaitingActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                    } else {
                        String message = response.getString("message");
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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

    }//

    private void getNearSpot(String store_serial, String language){
        RequestParams params = new RequestParams();
        params.put("store_serial", store_serial);
        params.put("language", language);
        Network.post("/get_near_spot", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getString("code").equals("S01")) {
                        nearSpotModelArrayList.clear();
                        Gson gson = new Gson();
                        JSONArray data = response.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            String jsonstr = data.get(i).toString();
                            NearSpotModel nearSpotModel = gson.fromJson(jsonstr, NearSpotModel.class);
                            nearSpotModelArrayList.add(nearSpotModel);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        String message = response.getString("message");
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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

    }
}
