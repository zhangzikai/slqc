package com.tdgeos.dlg.yddc;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


public class ParamsDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private Handler mHandler = null;
    private boolean result = false;

    private EditText etLon0 = null;
    private EditText etDx = null;
    private EditText etDy = null;
    private EditText etDz = null;
    private EditText etRx = null;
    private EditText etRy = null;
    private EditText etRz = null;
    private EditText etK = null;

    private Button btnOk = null;
    private Button btnCancel = null;

    public ParamsDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_yddc_params);
        setTitle("GPS≤Œ ˝…Ë÷√");
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        //this.getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        etLon0 = (EditText) findViewById(R.id.et_lon);
        etDx = (EditText) findViewById(R.id.et_dx);
        etDy = (EditText) findViewById(R.id.et_dy);
        etDz = (EditText) findViewById(R.id.et_dz);
        etRx = (EditText) findViewById(R.id.et_rx);
        etRy = (EditText) findViewById(R.id.et_ry);
        etRz = (EditText) findViewById(R.id.et_rz);
        etK = (EditText) findViewById(R.id.et_k);
        etLon0.setText(String.valueOf(MyConfig.GetParamsLon0()));
        etDx.setText(String.valueOf(MyConfig.GetParamsDx()));
        etDy.setText(String.valueOf(MyConfig.GetParamsDy()));
        etDz.setText(String.valueOf(MyConfig.GetParamsDz()));
        etRx.setText(String.valueOf(MyConfig.GetParamsRx()));
        etRy.setText(String.valueOf(MyConfig.GetParamsRy()));
        etRz.setText(String.valueOf(MyConfig.GetParamsRz()));
        etK.setText(String.valueOf(MyConfig.GetParamsK()));
        etLon0.setInputType(InputType.TYPE_CLASS_NUMBER);
        etDx.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etDy.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etDz.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etRx.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etRy.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etRz.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etK.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok: {
                String s_lon0 = etLon0.getText().toString();
                String s_dx = etDx.getText().toString();
                String s_dy = etDy.getText().toString();
                String s_dz = etDz.getText().toString();
                String s_rx = etRx.getText().toString();
                String s_ry = etRy.getText().toString();
                String s_rz = etRz.getText().toString();
                String s_k = etK.getText().toString();
                int lon0 = 0;
                double dx = 0;
                double dy = 0;
                double dz = 0;
                double rx = 0;
                double ry = 0;
                double rz = 0;
                double k = 1;
                try {
                    lon0 = Integer.parseInt(s_lon0);
                } catch (Exception e) {
                }
                try {
                    dx = Double.parseDouble(s_dx);
                } catch (Exception e) {
                }
                try {
                    dy = Double.parseDouble(s_dy);
                } catch (Exception e) {
                }
                try {
                    dz = Double.parseDouble(s_dz);
                } catch (Exception e) {
                }
                try {
                    rx = Double.parseDouble(s_rx);
                } catch (Exception e) {
                }
                try {
                    ry = Double.parseDouble(s_ry);
                } catch (Exception e) {
                }
                try {
                    rz = Double.parseDouble(s_rz);
                } catch (Exception e) {
                }
                try {
                    k = Double.parseDouble(s_k);
                } catch (Exception e) {
                }
                MyConfig.SetParamsLon0(lon0);
                MyConfig.SetParamsDx(dx);
                MyConfig.SetParamsDy(dy);
                MyConfig.SetParamsDz(dz);
                MyConfig.SetParamsRx(rx);
                MyConfig.SetParamsRy(ry);
                MyConfig.SetParamsRz(rz);
                MyConfig.SetParamsK(k);

                result = true;
                this.cancel();
                break;
            }
            case R.id.btn_cancel: {
                this.cancel();
                break;
            }
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