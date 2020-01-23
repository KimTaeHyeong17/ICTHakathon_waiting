package com.example.icthackathon.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.icthackathon.R;

public class CancelWaitingDialog extends Dialog {

    private final String TAG = "MessageDialog";


    Activity mactivity;
    private TextView text;
    Button dialogButton1, dialogButton2;

    public CancelWaitingDialog(Activity context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mactivity = context;
        setContentView(R.layout.dialog_cancel_waiting);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //확인버튼
        //취소버튼
        dialogButton2 = (Button) findViewById(R.id.btn2);

        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


    }
    public void setMessage(String str){text.setText(str);}
    public void setBtnMsg(String str){dialogButton1.setText(str);}
    public void setClickListener(View.OnClickListener listener){
        dialogButton1.setOnClickListener(listener);

    }


}
