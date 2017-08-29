package com.tdgeos.dlg.yddc;

import java.util.List;

import com.tdgeos.lib.MyFuns;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Xm;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class XmDialog extends Dialog implements View.OnClickListener {
    private Handler mHandler = null;
    private Context context;
    private int ydh = 0;

    private EditText etMc = null;
    private EditText etGd = null;
    private EditText etXj = null;

    private Button btnSz = null;
    private Button btnOk = null;
    private Button btnCancel = null;

    private Xm xm = null;
    private Xm result = null;

    public XmDialog(Context context, Xm xm, int ydh) {
        super(context);
        this.context = context;
        this.xm = xm;
        this.ydh = ydh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_xm);
        this.setTitle("下木");
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

        etMc = (EditText) findViewById(R.id.et_mc);
        etGd = (EditText) findViewById(R.id.et_sg);
        etXj = (EditText) findViewById(R.id.et_xj);

        etGd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etXj.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

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
                if (f > YangdiMgr.MIN_XM_SG && f <= YangdiMgr.MAX_XM_SG) {
                    etGd.setTextColor(Color.BLACK);
                } else {
                    etGd.setTextColor(Color.RED);
                }
            }
        });

        etXj.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_XM_XJ && f < YangdiMgr.MAX_XM_XJ) {
                    etXj.setTextColor(Color.BLACK);
                } else {
                    etXj.setTextColor(Color.RED);
                }
            }
        });

        if (xm != null) {
            etMc.setText(xm.mc);
            etGd.setText(String.valueOf(xm.gd));
            etXj.setText(String.valueOf(xm.xj));
        }

        btnSz = (Button) findViewById(R.id.btn_mc);
        btnSz.setOnClickListener(this);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    public Xm showDialog() {
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
                    String[] ss = MyFuns.Split(str, ' ');
                    if (ss.length == 2) etMc.setText(ss[1]);
                    else etMc.setText(str);
                }
                break;
            }
            case R.id.btn_ok: {
                String s_sz = etMc.getText().toString();
                String s_gd = etGd.getText().toString();
                String s_xj = etXj.getText().toString();
                float f_gd = 0;
                float f_xj = 0;

                if (s_sz.equals("")) {
                    Toast.makeText(context, "名称不能为空！", 1).show();
                    break;
                }
                if (xm == null) {
                    List<Xm> lst = YangdiMgr.GetXmList(ydh);
                    boolean b = false;
                    for (int i = 0; i < lst.size(); i++) {
                        if (s_sz.equalsIgnoreCase(lst.get(i).mc)) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        Toast.makeText(context, "该下木名称已经存在！", 1).show();
                        break;
                    }
                } else if (!xm.mc.equals(s_sz)) {
                    List<Xm> lst = YangdiMgr.GetXmList(ydh);
                    boolean b = false;
                    for (int i = 0; i < lst.size(); i++) {
                        if (s_sz.equalsIgnoreCase(lst.get(i).mc)) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        Toast.makeText(context, "该下木名称已经存在！", 1).show();
                        break;
                    }
                }
                if (s_gd.equals("")) {
                    Toast.makeText(context, "高度不能为空！", 1).show();
                    break;
                }
                if (s_xj.equals("")) {
                    Toast.makeText(context, "胸径不能为空！", 1).show();
                    break;
                }

                try {
                    f_gd = Float.parseFloat(s_gd);
                } catch (java.lang.NumberFormatException nfe) {
                }
                try {
                    f_xj = Float.parseFloat(s_xj);
                } catch (java.lang.NumberFormatException nfe) {
                }
                if (f_gd <= YangdiMgr.MIN_XM_SG || f_gd > YangdiMgr.MAX_XM_SG) {
                    Toast.makeText(context, "高度超限！", 1).show();
                    break;
                }
                if (f_xj < YangdiMgr.MIN_XM_XJ || f_xj >= YangdiMgr.MAX_XM_XJ) {
                    Toast.makeText(context, "胸径超限！", 1).show();
                    break;
                }

                result = new Xm();
                result.mc = s_sz;
                result.gd = f_gd;
                result.xj = f_xj;
                if (xm != null) result.xh = xm.xh;

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
