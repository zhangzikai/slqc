package com.tdgeos.dlg.yddc;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;

public class ZjclTypeDialog extends Dialog implements OnClickListener {
    private RadioButton rbFc = null;
    private RadioButton rbXc = null;

    private int iType = 0;

    private Button btnOk;
    private Button btnCancel;

    private Context ctt;
    private Handler mHandler;

    public ZjclTypeDialog(Context context, int type) {
        super(context);
        ctt = context;
        iType = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_zjcltype);
        setTitle("样地周界测量");
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        //this.getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        rbFc = (RadioButton) findViewById(R.id.rb_fc);
        rbXc = (RadioButton) findViewById(R.id.rb_xc);

        if (iType == 0) rbFc.setChecked(true);
        else rbXc.setChecked(true);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok: {
                if (rbFc.isChecked()) iType = 0;
                else iType = 1;
                this.cancel();
                break;
            }
            case R.id.btn_cancel: {
                iType = -1;
                this.cancel();
                break;
            }
        }
    }

    public int showDialog() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };
        super.show();
        try {
            Looper.getMainLooper();
            Looper.loop();
        } catch (RuntimeException e2) {
        }
        return iType;
    }
}
