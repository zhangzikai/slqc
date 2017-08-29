package com.tdgeos.dlg.yddc;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WclzldDialog extends Dialog implements View.OnClickListener {
    private Handler mHandler = null;
    private Context context;

    private EditText etSz = null;
    private EditText etBl = null;

    private Button btnSz = null;
    private Button btnOk = null;
    private Button btnCancel = null;

    private String sz = "";
    private int bl = -1;
    private String[] result = null;

    public WclzldDialog(Context context, String sz, int bl) {
        super(context);
        this.context = context;
        this.sz = sz;
        this.bl = bl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_wclzld);
        this.setTitle("树种组成");
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        this.setCanceledOnTouchOutside(false);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        etSz = (EditText) findViewById(R.id.et_sz);
        etBl = (EditText) findViewById(R.id.et_bl);

        etBl.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                float f = -1;
                try {
                    f = Float.parseFloat(str);
                } catch (Exception e) {
                }
                if (f > 0 && f <= 10) {
                    etBl.setTextColor(Color.BLACK);
                } else {
                    etBl.setTextColor(Color.RED);
                }
            }
        });

        if (sz != null) etSz.setText(sz);
        if (bl > 0) etBl.setText(String.valueOf(bl));

        btnSz = (Button) findViewById(R.id.btn_sz);
        btnSz.setOnClickListener(this);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    public String[] showDialog() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sz: {
                SzxzDialog dlg = new SzxzDialog(context);
                String str = dlg.showDialog();
                if (str != null) {
                    etSz.setText(str);
                }
                break;
            }
            case R.id.btn_ok: {
                String s_sz = etSz.getText().toString();
                String s_bl = etBl.getText().toString();
                int i_bl = -1;

                if (s_sz.equals("")) {
                    Toast.makeText(context, "树种不能为空！", 1).show();
                    break;
                }
                if (s_bl.equals("")) {
                    Toast.makeText(context, "比例不能为空！", 1).show();
                    break;
                }

                try {
                    i_bl = Integer.parseInt(s_bl);
                } catch (java.lang.NumberFormatException nfe) {
                }

                if (i_bl > 10) {
                    Toast.makeText(context, "比例不能超过10，请重新填写！", 1).show();
                    break;
                }

                result = new String[2];
                result[0] = s_sz;
                result[1] = String.valueOf(i_bl);

                WclzldDialog.this.cancel();
                break;
            }
            case R.id.btn_cancel: {
                WclzldDialog.this.cancel();
                break;
            }
        }
    }
}
