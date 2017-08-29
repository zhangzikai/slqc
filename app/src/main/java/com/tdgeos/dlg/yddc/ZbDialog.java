package com.tdgeos.dlg.yddc;

import java.util.List;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.YangdiMgr;
import com.tdgeos.yangdi.Zb;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ZbDialog extends Dialog implements View.OnClickListener {
    private Handler mHandler = null;
    private Context context;

    private EditText etMc = null;
    private EditText etPjg = null;
    private EditText etGd = null;
    private EditText etZs = null;
    private EditText etPjdj = null;

    private LinearLayout layZs = null;
    private LinearLayout layPjdj = null;

    private Button btnSz = null;
    private Button btnOk = null;
    private Button btnCancel = null;

    private int type = 1;
    private int ydh = 0;

    private Zb zb = null;
    private Zb result = null;

    public ZbDialog(Context context, Zb zb, int type, int ydh) {
        super(context);
        this.context = context;
        this.zb = zb;
        this.type = type;
        this.ydh = ydh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_zb);
        this.setTitle("植被调查");
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

        btnSz = (Button) findViewById(R.id.btn_mc);
        btnSz.setOnClickListener(this);
        layZs = (LinearLayout) findViewById(R.id.lay_zs);
        layPjdj = (LinearLayout) findViewById(R.id.lay_pjdj);
        if (type == 1) {
            btnSz.setVisibility(1);
            layZs.setVisibility(1);
            layPjdj.setVisibility(1);
        }

        etMc = (EditText) findViewById(R.id.et_mc);
        etPjg = (EditText) findViewById(R.id.et_pjg);
        etGd = (EditText) findViewById(R.id.et_gd);
        etZs = (EditText) findViewById(R.id.et_zs);
        etPjdj = (EditText) findViewById(R.id.et_pjdj);

        etPjg.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etGd.setInputType(InputType.TYPE_CLASS_NUMBER);
        etZs.setInputType(InputType.TYPE_CLASS_NUMBER);
        etPjdj.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        etGd.addTextChangedListener(new TextWatcher() {
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
                    etGd.setTextColor(Color.BLACK);
                } else {
                    etGd.setTextColor(Color.RED);
                }
            }
        });

        etPjg.addTextChangedListener(new TextWatcher() {
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
                if (type == 1) {
                    if (f >= YangdiMgr.MIN_GMGD && f <= YangdiMgr.MAX_GMGD) {
                        etPjg.setTextColor(Color.BLACK);
                    } else {
                        etPjg.setTextColor(Color.RED);
                    }
                }
                if (type == 2) {
                    if (f >= YangdiMgr.MIN_CBGD && f <= YangdiMgr.MAX_CBGD) {
                        etPjg.setTextColor(Color.BLACK);
                    } else {
                        etPjg.setTextColor(Color.RED);
                    }
                }
                if (type == 3) {
                    if (f >= YangdiMgr.MIN_DBWGD && f <= YangdiMgr.MAX_DBWGD) {
                        etPjg.setTextColor(Color.BLACK);
                    } else {
                        etPjg.setTextColor(Color.RED);
                    }
                }
            }
        });

        if (zb != null) {
            etMc.setText(zb.mc);
            if (zb.pjgd > 0) etPjg.setText(String.valueOf(zb.pjgd));
            if (zb.fgd > 0) etGd.setText(String.valueOf(zb.fgd));
            if (zb.zs > 0) etZs.setText(String.valueOf(zb.zs));
            if (zb.pjdj > 0) etPjdj.setText(String.valueOf(zb.pjdj));
        }

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    public Zb showDialog() {
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
            case R.id.btn_mc: {
                SzxzDialog dlg = new SzxzDialog(context);
                String str = dlg.showDialog();
                if (str != null) {
                    etMc.setText(str);
                }
                break;
            }
            case R.id.btn_ok: {
                String s_mc = etMc.getText().toString();
                String s_pjg = etPjg.getText().toString();
                String s_gd = etGd.getText().toString();
                float f_pjg = -1;
                int i_gd = -1;

                if (s_mc.equals("")) {
                    Toast.makeText(context, "名称不能为空！", 1).show();
                    break;
                }
                if (zb == null) {
                    List<Zb> lst = YangdiMgr.GetZbList(ydh, type);
                    boolean b = false;
                    for (int i = 0; i < lst.size(); i++) {
                        if (s_mc.equalsIgnoreCase(lst.get(i).mc)) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        Toast.makeText(context, "该植被名称已经存在！", 1).show();
                        break;
                    }
                } else if (!zb.mc.equals(s_mc)) {
                    List<Zb> lst = YangdiMgr.GetZbList(ydh, type);
                    boolean b = false;
                    for (int i = 0; i < lst.size(); i++) {
                        if (s_mc.equalsIgnoreCase(lst.get(i).mc)) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        Toast.makeText(context, "该植被名称已经存在！", 1).show();
                        break;
                    }
                }
                if (s_pjg.equals("")) {
                    Toast.makeText(context, "平均高不能为空！", 1).show();
                    break;
                }
                if (s_gd.equals("")) {
                    Toast.makeText(context, "盖度不能为空！", 1).show();
                    break;
                }

                try {
                    f_pjg = Float.parseFloat(s_pjg);
                } catch (java.lang.NumberFormatException nfe) {
                }
                if (type == 1) {
                    if (f_pjg < YangdiMgr.MIN_GMGD || f_pjg > YangdiMgr.MAX_GMGD) {
                        Toast.makeText(context, "平均高度超限！", 1).show();
                        break;
                    }
                }
                if (type == 2) {
                    if (f_pjg < YangdiMgr.MIN_CBGD || f_pjg > YangdiMgr.MAX_CBGD) {
                        Toast.makeText(context, "平均高度超限！", 1).show();
                        break;
                    }
                }
                if (type == 3) {
                    if (f_pjg < YangdiMgr.MIN_DBWGD || f_pjg > YangdiMgr.MAX_DBWGD) {
                        Toast.makeText(context, "平均高度超限！", 1).show();
                        break;
                    }
                }
                try {
                    i_gd = Integer.parseInt(s_gd);
                } catch (java.lang.NumberFormatException nfe) {
                }
                if (i_gd < YangdiMgr.MIN_BFB || i_gd > YangdiMgr.MAX_BFB) {
                    Toast.makeText(context, "盖度超限！", 1).show();
                    break;
                }

                int i_zs = -1;
                float f_dj = -1;
                if (type == 1) {
                    String s_zs = etZs.getText().toString();
                    String s_dj = etPjdj.getText().toString();
                    if (s_zs.equals("")) {
                        Toast.makeText(context, "灌木株数不能为空！", 1).show();
                        break;
                    }
                    if (s_dj.equals("")) {
                        Toast.makeText(context, "平均地径不能为空！", 1).show();
                        break;
                    }
                    try {
                        f_dj = Float.parseFloat(s_dj);
                    } catch (java.lang.NumberFormatException nfe) {
                    }
                    if (f_dj < YangdiMgr.MIN_PJDJ || f_dj > YangdiMgr.MAX_PJDJ) {
                        Toast.makeText(context, "平均地径超限！", 1).show();
                        break;
                    }
                    try {
                        i_zs = Integer.parseInt(s_zs);
                    } catch (java.lang.NumberFormatException nfe) {
                    }
                }

                result = new Zb();
                result.zblx = type;
                result.mc = s_mc;
                result.pjgd = f_pjg;
                result.fgd = i_gd;
                result.zs = i_zs;
                result.pjdj = f_dj;
                if (zb != null) result.xh = zb.xh;

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
