package com.tdgeos.dlg.yddc;

import java.util.List;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Slzh;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SlzhDialog extends Dialog implements View.OnClickListener {
    private Handler mHandler = null;
    private Context context;

    private EditText etXh = null;
    private Spinner spZhlx = null;
    private EditText etWhbw = null;
    private EditText etSzzs = null;
    private Spinner spSzdj = null;

    private Button btnOk = null;
    private Button btnCancel = null;

    private int ydh = 0;
    private Slzh slzh = null;
    private Slzh result = null;

    public SlzhDialog(Context context, Slzh slzh, int ydh) {
        super(context);
        this.context = context;
        this.slzh = slzh;
        this.ydh = ydh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_slzh);
        this.setTitle("森林灾害");
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

        etXh = (EditText) findViewById(R.id.et_xh);
        spZhlx = (Spinner) findViewById(R.id.sp_zhlx);
        etWhbw = (EditText) findViewById(R.id.et_whbw);
        etSzzs = (EditText) findViewById(R.id.et_szzs);
        spSzdj = (Spinner) findViewById(R.id.sp_szdj);

        etXh.setEnabled(false);

        etSzzs.setInputType(InputType.TYPE_CLASS_NUMBER);

        List<String> lstZhlx = Resmgr.GetValueList("slzhlx");
        ArrayAdapter<String> apZhlx = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstZhlx);
        apZhlx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spZhlx.setAdapter(apZhlx);

        List<String> lstZhdj = Resmgr.GetValueList("slzhdj");
        ArrayAdapter<String> apZhdj = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstZhdj);
        apZhdj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSzdj.setAdapter(apZhdj);

        spZhlx.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == 9) {
                    etWhbw.setText("无");
                    etSzzs.setText("0");
                    spSzdj.setSelection(1);
                }
                if (arg2 == 0) {
                    etWhbw.setText("");
                    etSzzs.setText("");
                    spSzdj.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        etSzzs.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_BFB && f <= YangdiMgr.MAX_BFB) {
                    etSzzs.setTextColor(Color.BLACK);
                } else {
                    etSzzs.setTextColor(Color.RED);
                }
                int lx = spZhlx.getSelectedItemPosition();
                if (lx == 1 || lx == 2)//病害或虫害
                {
                    if (f < 10) {
                        spSzdj.setSelection(1);
                    } else if (f <= 29) {
                        spSzdj.setSelection(2);
                    } else if (f <= 59) {
                        spSzdj.setSelection(3);
                    } else {
                        spSzdj.setSelection(4);
                    }
                } else if (lx == 3)//火灾
                {
                    if (f < 10) {
                        spSzdj.setSelection(1);
                    } else if (f <= 19) {
                        spSzdj.setSelection(2);
                    } else if (f <= 49) {
                        spSzdj.setSelection(3);
                    } else {
                        spSzdj.setSelection(4);
                    }
                } else {
                    if (f < 10) {
                        spSzdj.setSelection(1);
                    } else if (f <= 19) {
                        spSzdj.setSelection(2);
                    } else if (f <= 59) {
                        spSzdj.setSelection(3);
                    } else {
                        spSzdj.setSelection(4);
                    }
                }
            }
        });

        if (slzh != null) {
            etXh.setText(String.valueOf(slzh.xh));
            spZhlx.setSelection(Resmgr.GetPosByCode("slzhlx", slzh.zhlx));
            etWhbw.setText(slzh.whbw);
            etSzzs.setText(String.valueOf(slzh.szymzs));
            spSzdj.setSelection(Resmgr.GetPosByCode("slzhdj", slzh.szdj));
        } else {
            int xh = YangdiMgr.GetMaxSlzhXh(ydh);
            xh++;
            etXh.setText(String.valueOf(xh));
        }

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    public Slzh showDialog() {
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
            case R.id.btn_ok: {
                String s_xh = etXh.getText().toString();
                String s_zhlx = spZhlx.getSelectedItem().toString();
                String s_whbw = etWhbw.getText().toString();
                String s_szzs = etSzzs.getText().toString();
                String s_szdj = spSzdj.getSelectedItem().toString();
                int i_xh = -1;
                int i_zhlx = -1;
                int i_szzs = -1;
                int i_szdj = -1;

                if (s_zhlx.equals("")) {
                    Toast.makeText(context, "灾害类型不能为空！", 1).show();
                    break;
                }
                if (s_whbw.equals("")) {
                    Toast.makeText(context, "危害部位不能为空！", 1).show();
                    break;
                }
                if (s_szzs.equals("")) {
                    Toast.makeText(context, "受灾样木株数不能为空！", 1).show();
                    break;
                }
                if (s_szdj.equals("")) {
                    Toast.makeText(context, "受灾等级不能为空！", 1).show();
                    break;
                }

                try {
                    i_xh = Integer.parseInt(s_xh);
                } catch (java.lang.NumberFormatException nfe) {
                }
                i_zhlx = Resmgr.GetCodeByValue("slzhlx", s_zhlx);
                if (slzh == null) {
                    if (YangdiMgr.IsHaveSlzh(ydh, i_zhlx)) {
                        Toast.makeText(context, "该灾害类型的记录已经存在！", 1).show();
                        break;
                    }
                } else if (slzh.zhlx != i_zhlx && YangdiMgr.IsHaveSlzh(ydh, i_zhlx)) {
                    Toast.makeText(context, "该灾害类型的记录已经存在！", 1).show();
                    break;
                }
                try {
                    i_szzs = Integer.parseInt(s_szzs);
                } catch (java.lang.NumberFormatException nfe) {
                }
                try {
                    i_szdj = Resmgr.GetCodeByValue("slzhdj", s_szdj);
                } catch (java.lang.NumberFormatException nfe) {
                }

                if (i_szzs > 100) {
                    Toast.makeText(context, "受灾样木株数填写错误！", 1).show();
                    break;
                }

                result = new Slzh();
                result.xh = i_xh;
                result.zhlx = i_zhlx;
                result.whbw = s_whbw;
                result.szymzs = i_szzs;
                result.szdj = i_szdj;

                this.cancel();
                break;
            }
            case R.id.btn_cancel: {
                this.cancel();
                break;
            }
        }
    }
}
