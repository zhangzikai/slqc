package com.tdgeos.dlg.yddc;

import java.util.ArrayList;
import java.util.List;

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
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tdgeos.lib.MyFuns;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Cljl;
import com.tdgeos.yangdi.YangdiMgr;

public class ZjxcDialog extends Dialog {
    private Handler mHandler = null;
    private MyListener myListener = null;
    private Context context;

    private boolean lock = true;

    private EditText etCz = null;
    private Spinner spFwj = null;
    private EditText etQxj = null;
    private EditText etXj = null;
    private EditText etSpj = null;
    private EditText etLj = null;

    private Button btnOk = null;
    private Button btnCancel = null;

    private Cljl zjcljl = null;
    private Cljl result = null;
    private int ydh = 0;

    public ZjxcDialog(Context context, Cljl cljl, int ydh) {
        super(context);
        this.context = context;
        myListener = new MyListener();
        zjcljl = cljl;
        this.ydh = ydh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_zjcl_xc);
        this.setTitle("样地周界测量记录");
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

        etCz = (EditText) findViewById(R.id.et_cz);
        spFwj = (Spinner) findViewById(R.id.sp_fwj);
        etQxj = (EditText) findViewById(R.id.et_qxj);
        etXj = (EditText) findViewById(R.id.et_xj);
        etSpj = (EditText) findViewById(R.id.et_spj);
        etLj = (EditText) findViewById(R.id.et_lj);
        //etXj.setEnabled(false);
        //etSpj.setEnabled(false);
        etLj.setEnabled(false);

        etQxj.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etXj.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etSpj.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        List<String> lstFwj = new ArrayList<String>();
        lstFwj.add("0");
        lstFwj.add("90");
        lstFwj.add("180");
        lstFwj.add("270");
        ArrayAdapter<String> apFwj = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstFwj);
        apFwj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFwj.setAdapter(apFwj);

        if (zjcljl != null) {
            etCz.setText(zjcljl.cz);
            int i_fwj = (int) zjcljl.fwj;
            if (i_fwj == 0) spFwj.setSelection(0);
            if (i_fwj == 90) spFwj.setSelection(1);
            if (i_fwj == 180) spFwj.setSelection(2);
            if (i_fwj == 270) spFwj.setSelection(3);
            etQxj.setText(String.valueOf(zjcljl.qxj));
            etXj.setText(String.valueOf(zjcljl.xj));
            etSpj.setText(String.valueOf(zjcljl.spj));
            etLj.setText(String.valueOf(zjcljl.lj));
        }

        spFwj.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int i_fwj = 0;
                if (arg2 == 0) i_fwj = 0;
                if (arg2 == 1) i_fwj = 90;
                if (arg2 == 2) i_fwj = 180;
                if (arg2 == 3) i_fwj = 270;
                int n = 0;
                List<Cljl> lst = YangdiMgr.GetZjclList(ydh);
                if (lst != null) {
                    for (int j = 0; j < lst.size(); j++) {
                        int i_fwj_t = (int) lst.get(j).fwj;
                        if (i_fwj_t == i_fwj) {
                            n++;
                        }
                    }
                }
                if (zjcljl == null || zjcljl.cz == null || zjcljl.cz.equals("")) {
                    if (n == 0) {
                        if (arg2 == 0) etCz.setText("西南");
                        if (arg2 == 1) etCz.setText("西北");
                        if (arg2 == 2) etCz.setText("东北");
                        if (arg2 == 3) etCz.setText("东南");
                    } else {
                        if (arg2 == 0) etCz.setText("西南-" + n);
                        if (arg2 == 1) etCz.setText("西北-" + n);
                        if (arg2 == 2) etCz.setText("东北-" + n);
                        if (arg2 == 3) etCz.setText("东南-" + n);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        etQxj.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().trim();
                if (!text.equals("")) {
                    float f_qxj = -100;
                    try {
                        f_qxj = Float.parseFloat(text);
                    } catch (Exception e) {
                    }
                    if (f_qxj < YangdiMgr.MIN_QXJ || f_qxj > YangdiMgr.MAX_QXJ) {
                        etQxj.setTextColor(Color.RED);
                        etXj.setEnabled(false);
                        etSpj.setEnabled(false);
                    } else {
                        if (f_qxj < 0) f_qxj = -f_qxj;
                        etQxj.setTextColor(Color.BLACK);
                        etXj.setEnabled(true);
                        etSpj.setEnabled(true);
                        String s_xj = s.toString().trim();
                        float f_xj = -1;
                        try {
                            f_xj = Float.parseFloat(s_xj);
                        } catch (Exception e) {
                        }
                        if (f_xj > 0 && f_qxj >= 0 && f_qxj <= 90) {
                            float f_spj = (float) Math.cos(f_qxj * Math.PI / 180) * f_xj;
                            f_spj = MyFuns.MyDecimal(f_spj, 2);
                            if (lock) {
                                etSpj.setText(String.valueOf(f_spj));
                            }
                        }
                    }
                } else {
                    etQxj.setTextColor(Color.BLACK);
                    etXj.setEnabled(false);
                    etSpj.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String s_fwj = spFwj.getSelectedItem().toString();
                String s_qxj = etQxj.getText().toString();
                int i_fwj = 0;
                float f_qxj = 0;
                try {
                    i_fwj = (int) Float.parseFloat(s_fwj);
                } catch (Exception e) {
                }
                try {
                    f_qxj = Float.parseFloat(s_qxj);
                    if (f_qxj < 0) f_qxj = -f_qxj;
                } catch (Exception e) {
                }

                float spj = 0;
                List<Cljl> lst = YangdiMgr.GetZjclList(ydh);
                if (lst != null) {
                    for (int j = 0; j < lst.size(); j++) {
                        int i_fwj_t = (int) lst.get(j).fwj;
                        if (i_fwj_t == i_fwj) {
                            spj += lst.get(j).spj;
                        }
                    }
                }
                spj = YangdiMgr.YD_SIZE - spj;
                float xj = spj / (float) Math.cos(f_qxj * Math.PI / 180);
                if (xj < 0) xj = -xj;
                xj = MyFuns.MyDecimal(xj, 2);
                etXj.setText(String.valueOf(xj));
                etSpj.setText(String.valueOf(spj));
            }
        });

        etXj.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s_xj = s.toString().trim();
                String s_qxj = etQxj.getText().toString().trim();
                float f_xj = -1;
                float f_qxj = -100;
                try {
                    f_xj = Float.parseFloat(s_xj);
                } catch (Exception e) {
                }
                try {
                    f_qxj = Float.parseFloat(s_qxj);
                } catch (Exception e) {
                }
                if (f_xj > 0 && f_qxj >= YangdiMgr.MIN_QXJ && f_qxj <= YangdiMgr.MAX_QXJ) {
                    if (f_qxj < 0) f_qxj = -f_qxj;
                    float f_spj = (float) Math.cos(f_qxj * Math.PI / 180) * f_xj;
                    f_spj = MyFuns.MyDecimal(f_spj, 2);
                    if (lock) {
                        etSpj.setText(String.valueOf(f_spj));
                    }
                } else {
                    if (lock) {
                        etSpj.setText("");
                    }
                }
                if (f_xj < 0) {
                    etXj.setTextColor(Color.RED);
                } else {
                    etXj.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etXj.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lock = hasFocus;
            }
        });

        etSpj.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s_spj = s.toString().trim();
                String s_qxj = etQxj.getText().toString().trim();
                float f_spj = -1;
                float f_qxj = -100;
                try {
                    f_spj = Float.parseFloat(s_spj);
                } catch (Exception e) {
                }
                try {
                    f_qxj = Float.parseFloat(s_qxj);
                } catch (Exception e) {
                }
                if (f_spj > 0 && f_qxj >= YangdiMgr.MIN_QXJ && f_qxj <= YangdiMgr.MAX_QXJ) {
                    if (f_qxj < 0) f_qxj = -f_qxj;
                    float f_xj = f_spj / (float) Math.cos(f_qxj * Math.PI / 180);
                    f_xj = MyFuns.MyDecimal(f_xj, 2);
                    if (!lock) {
                        etXj.setText(String.valueOf(f_xj));
                    }
                } else {
                    if (!lock) {
                        etXj.setText("");
                    }
                }
                if (f_spj < 0 || f_spj > YangdiMgr.YD_SIZE) {
                    etSpj.setTextColor(Color.RED);
                } else {
                    etSpj.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etSpj.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lock = !hasFocus;
            }
        });

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(myListener);
        btnCancel.setOnClickListener(myListener);
    }

    public Cljl showDialog() {
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

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_ok:
                    String cz = etCz.getText().toString();
                    String s_fwj = spFwj.getSelectedItem().toString();
                    String s_qxj = etQxj.getText().toString();
                    String s_xj = etXj.getText().toString();
                    String s_spj = etSpj.getText().toString();
                    float f_fwj = -1;
                    float f_qxj = -100;
                    float f_xj = -1;
                    float f_spj = -1;

                    if (cz.equals("")) {
                        Toast.makeText(context, "测站不能为空！", 1).show();
                        break;
                    }
                    if (s_fwj.equals("")) {
                        Toast.makeText(context, "方位角不能为空！", 1).show();
                        break;
                    }
                    if (s_qxj.equals("")) {
                        Toast.makeText(context, "倾斜角不能为空！", 1).show();
                        break;
                    }
                    if (s_xj.equals("")) {
                        Toast.makeText(context, "斜距不能为空！", 1).show();
                        break;
                    }
                    if (s_spj.equals("")) {
                        Toast.makeText(context, "水平距不能为空！", 1).show();
                        break;
                    }

                    try {
                        f_fwj = Float.parseFloat(s_fwj);
                        f_fwj = MyFuns.MyDecimal(f_fwj, 1);
                    } catch (java.lang.NumberFormatException nfe) {
                    }

                    try {
                        f_qxj = Float.parseFloat(s_qxj);
                        f_qxj = MyFuns.MyDecimal(f_qxj, 1);
                    } catch (java.lang.NumberFormatException nfe) {
                    }

                    if (f_qxj < YangdiMgr.MIN_QXJ || f_qxj > YangdiMgr.MAX_QXJ) {
                        Toast.makeText(context, "倾斜角超限！", 1).show();
                        break;
                    }

                    try {
                        f_xj = Float.parseFloat(s_xj);
                        f_xj = MyFuns.MyDecimal(f_xj, 2);
                    } catch (java.lang.NumberFormatException nfe) {
                    }

                    try {
                        f_spj = Float.parseFloat(s_spj);
                        f_spj = MyFuns.MyDecimal(f_spj, 2);
                    } catch (java.lang.NumberFormatException nfe) {
                    }

                    float s = 0;
                    int i_fwj = (int) f_fwj;
                    if (zjcljl == null) {
                        List<Cljl> lst = YangdiMgr.GetZjclList(ydh);
                        if (lst != null) {
                            for (int i = 0; i < lst.size(); i++) {
                                int tmp = (int) lst.get(i).fwj;
                                if (i_fwj == tmp) s += lst.get(i).spj;
                            }
                        }
                        s += f_spj;
                        if (s > YangdiMgr.YD_SIZE) {
                            Toast.makeText(context, "单边长度超过样地边长！", 1).show();
                            break;
                        }
                    } else {

                    }

                    result = new Cljl();
                    result.cz = cz;
                    result.fwj = f_fwj;
                    result.qxj = f_qxj;
                    result.xj = f_xj;
                    result.spj = f_spj;
                    result.lj = 0;
                    if (zjcljl != null) result.xh = zjcljl.xh;

                    ZjxcDialog.this.cancel();
                    break;
                case R.id.btn_cancel:
                    ZjxcDialog.this.cancel();
                    break;
            }
        }
    }
}
