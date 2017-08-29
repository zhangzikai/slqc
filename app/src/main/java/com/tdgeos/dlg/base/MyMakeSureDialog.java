package com.tdgeos.dlg.base;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MyMakeSureDialog extends Dialog implements OnClickListener {
    private TextView tvInfo;
    private Button btnYes;
    private Button btnNo;
    private Handler mHandler;
    private String title;
    private String info;
    private String okText;
    private String cancelText;
    private boolean result;

    public MyMakeSureDialog(Context context, String title, String info, String ok, String cancel) {
        super(context);
        this.title = title;
        this.info = info;
        okText = ok;
        cancelText = cancel;
        result = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_base_makesure);
        setTitle(title);
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        tvInfo = (TextView) findViewById(R.id.tv_info);
        tvInfo.setText(info);

        btnYes = (Button) findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(this);
        if (okText != null) btnYes.setText(okText);

        btnNo = (Button) findViewById(R.id.btn_no);
        btnNo.setOnClickListener(this);
        if (cancelText != null) btnNo.setText(cancelText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes: {
                result = true;
                this.cancel();
            }
            break;
            case R.id.btn_no: {
                this.cancel();
            }
            break;
        }
    }

    public boolean showDialog() {
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
        return result;
    }
}
