package com.example.icthackathon.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.icthackathon.Activity.MenuDetailActivity;
import com.example.icthackathon.Adapter.MenuListAdapter;
import com.example.icthackathon.Data.StoreMenuModel;
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



public class Tab_2_Fragment extends Fragment {

    private ListView menu_listview;
    private View view;
    private MenuListAdapter menuListAdapter;
    private String language;
    private String store_serial = "1";
    private ArrayList<StoreMenuModel> storeMenuModelArrayList = new ArrayList<>();

    public Tab_2_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tab_2_,container,false);
        Locale systemLocale = getContext().getResources().getConfiguration().locale;
        language = systemLocale.getCountry(); // KR

        bindUI();
        getMenu(store_serial,language);
        return view;
    }

    private void bindUI(){
        menu_listview = view.findViewById(R.id.listview);
        menuListAdapter = new MenuListAdapter(storeMenuModelArrayList);
        menu_listview.setAdapter(menuListAdapter);

        menu_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), MenuDetailActivity.class);
                intent.putExtra("url",storeMenuModelArrayList.get(position).getImage());
                intent.putExtra("tv_menu_description",storeMenuModelArrayList.get(position).getComment());
                Log.e("storecomment",storeMenuModelArrayList.get(position).getComment());
                startActivity(intent);
            }
        });
    }


    private void getMenu(String store_serial, String language){
        RequestParams params = new RequestParams();
        Log.e("getMenu","gettingMenu");
        params.put("store_serial", String.valueOf(store_serial));
        params.put("language", language);
        Network.post("/get_store_menu", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getString("code").equals("S01")) {
                        storeMenuModelArrayList.clear();
                        Gson gson = new Gson();
                        JSONArray data = response.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            String jsonstr = data.get(i).toString();
                            StoreMenuModel storeMenuModel = gson.fromJson(jsonstr, StoreMenuModel.class);
                            storeMenuModelArrayList.add(storeMenuModel);
                        }
                        menuListAdapter.notifyDataSetChanged();

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

    }//getWaitingState


}
