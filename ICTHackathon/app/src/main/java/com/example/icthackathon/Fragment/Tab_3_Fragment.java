package com.example.icthackathon.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.icthackathon.R;

import java.util.Locale;


public class Tab_3_Fragment extends Fragment {

    private View view;
    private String language;

    public Tab_3_Fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_3_, container, false);

        Locale systemLocale = getContext().getResources().getConfiguration().locale;
        language = systemLocale.getCountry(); // KR

        Log.e("tab3","asdojiawoiejfoiewajiof");
        Log.e("language",language);
        ImageView img_map = (ImageView) view.findViewById(R.id.img_map);

        switch (language){
            case "EN":
                img_map.setImageResource(R.drawable.map_en);
                break;

            case "CN":
                img_map.setImageResource(R.drawable.map_cn);
                break;
        }

        return view;
    }

}
