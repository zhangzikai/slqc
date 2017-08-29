package com.tdgeos.dlg.yddc;

import java.util.List;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Klw;
import com.tdgeos.yangdi.Resmgr;
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
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class KlwDialog extends Dialog implements View.OnClickListener {
    private Handler mHandler = null;
    private Context context;
    private int ydh = 0;

    private Spinner spYfh = null;
    private EditText etHd = null;
    private EditText etYfxz = null;
    private EditText etYfgz = null;
    private EditText etYpxz = null;
    private EditText etYpgz = null;

    private Button btnOk = null;
    private Button btnCancel = null;

    private Klw klw = null;
    private Klw result = null;

    public KlwDialog(Context context, Klw klw, int ydh) {
        super(context);
        this.context = context;
        this.klw = klw;
        this.ydh = ydh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_klw);
        this.setTitle("枯落物");
        this.getWindow().getAttributes().width = MyConfig.GetSmallWidth();
        this.setCanceledOnTouchOutside(false);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        spYfh = (Spinner) findViewById(R.id.sp_yfh);
        etHd = (EditText) findViewById(R.id.et_hd);
        etYfxz = (EditText) findViewById(R.id.et_yfxz);
        etYfgz = (EditText) findViewById(R.id.et_yfgz);
        etYpxz = (EditText) findViewById(R.id.et_ypxz);
        etYpgz = (EditText) findViewById(R.id.et_ypgz);

        List<String> lstYfh = Resmgr.GetValueList("yfwz");
        ArrayAdapter<String> apYfh = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstYfh);
        apYfh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYfh.setAdapter(apYfh);

        etHd.addTextChangedListener(new TextWatcher() {
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
                if (f >= 0 && f <= 40) {
                    etHd.setTextColor(Color.BLACK);
                } else {
                    etHd.setTextColor(Color.RED);
                }
            }
        });

        etYfxz.addTextChangedListener(new TextWatcher() {
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
                if (f >= 0 && f <= 5000) {
                    etYfxz.setTextColor(Color.BLACK);
                } else {
                    etYfxz.setTextColor(Color.RED);
                }
            }
        });

        etYfgz.addTextChangedListener(new TextWatcher() {
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
                if (f >= 0 && f <= 5000) {
                    etYfgz.setTextColor(Color.BLACK);
                } else {
                    etYfgz.setTextColor(Color.RED);
                }
            }
        });

        etYpxz.addTextChangedListener(new TextWatcher() {
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
                if (f >= 0 && f <= 1000) {
                    etYpxz.setTextColor(Color.BLACK);
                } else {
                    etYpxz.setTextColor(Color.RED);
                }
            }
        });

        etYpgz.addTextChangedListener(new TextWatcher() {
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
                if (f >= 0 && f <= 1000) {
                    etYpgz.setTextColor(Color.BLACK);
                } else {
                    etYpgz.setTextColor(Color.RED);
                }
            }
        });

        if (klw != null) {
            spYfh.setSelection(Resmgr.GetPosByCode("yfwz", klw.yfh));
            etHd.setText(String.valueOf(klw.hd));
            etYfxz.setText(String.valueOf(klw.yfxz));
            etYfgz.setText(String.valueOf(klw.yfgz));
            etYpxz.setText(String.valueOf(klw.ypxz));
            etYpgz.setText(String.valueOf(klw.ypgz));
        }

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    public Klw showDialog() {
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
                String s_yfh = spYfh.getSelectedItem().toString();
                String s_hd = etHd.getText().toString();
                String s_yfxz = etYfxz.getText().toString();
                String s_yfgz = etYfgz.getText().toString();
                String s_ypxz = etYpxz.getText().toString();
                String s_ypgz = etYpgz.getText().toString();
                int i_yfh = -1;
                float f_hd = 0;
                float f_yfxz = 0;
                float f_yfgz = 0;
                float f_ypxz = 0;
                float f_ypgz = 0;

                if (s_yfh.equals("")) {
                    Toast.makeText(context, "样方号不能为空！", 1).show();
                    break;
                }
                try {
                    i_yfh = Integer.parseInt(s_yfh);
                } catch (java.lang.NumberFormatException nfe) {
                }
                if (klw == null) {
                    List<Klw> lst = YangdiMgr.GetKlwList(ydh);
                    boolean b = false;
                    for (int i = 0; i < lst.size(); i++) {
                        if (i_yfh == lst.get(i).yfh) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        Toast.makeText(context, "该样方号已经存在！", 1).show();
                        break;
                    }
                } else if (klw.yfh != i_yfh) {
                    List<Klw> lst = YangdiMgr.GetKlwList(ydh);
                    boolean b = false;
                    for (int i = 0; i < lst.size(); i++) {
                        if (i_yfh == lst.get(i).yfh) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        Toast.makeText(context, "该样方号已经存在！", 1).show();
                        break;
                    }
                }
                if (s_yfxz.equals("")) {
                    Toast.makeText(context, "样方鲜重不能为空！", 1).show();
                    break;
                }
                if (s_yfgz.equals("")) {
                    Toast.makeText(context, "样方干重不能为空！", 1).show();
                    break;
                }
                if (s_ypxz.equals("")) {
                    Toast.makeText(context, "样品鲜重不能为空！", 1).show();
                    break;
                }
                if (s_ypgz.equals("")) {
                    Toast.makeText(context, "样品干重不能为空！", 1).show();
                    break;
                }

                try {
                    f_hd = Float.parseFloat(s_hd);
                } catch (java.lang.NumberFormatException nfe) {
                }
                try {
                    f_yfxz = Float.parseFloat(s_yfxz);
                } catch (java.lang.NumberFormatException nfe) {
                }
                try {
                    f_yfgz = Float.parseFloat(s_yfgz);
                } catch (java.lang.NumberFormatException nfe) {
                }
                try {
                    f_ypxz = Float.parseFloat(s_ypxz);
                } catch (java.lang.NumberFormatException nfe) {
                }
                try {
                    f_ypgz = Float.parseFloat(s_ypgz);
                } catch (java.lang.NumberFormatException nfe) {
                }

                if (!s_hd.equals("") && (f_hd <= 0 || f_hd > 40)) {
                    Toast.makeText(context, "厚度超限！", 1).show();
                    break;
                }
                if (f_yfxz <= 0 || f_yfxz > 5000) {
                    Toast.makeText(context, "样方鲜重超限！", 1).show();
                    break;
                }
                if (f_yfgz <= 0 || f_yfgz > 5000) {
                    Toast.makeText(context, "样方干重超限！", 1).show();
                    break;
                }
                if (f_ypxz <= 0 || f_ypxz > 1000) {
                    Toast.makeText(context, "样品鲜重超限！", 1).show();
                    break;
                }
                if (f_ypgz <= 0 || f_ypgz > 1000) {
                    Toast.makeText(context, "样品干重超限！", 1).show();
                    break;
                }

                if (f_yfgz > f_yfxz) {
                    Toast.makeText(context, "样方干重不能超过样方鲜重！", 1).show();
                    break;
                }
                if (f_ypgz > f_ypxz) {
                    Toast.makeText(context, "样品干重不能超过样品鲜重！", 1).show();
                    break;
                }

                result = new Klw();
                result.yfh = i_yfh;
                result.hd = f_hd;
                result.yfxz = f_yfxz;
                result.yfgz = f_yfgz;
                result.ypxz = f_ypxz;
                result.ypgz = f_ypgz;
                if (klw != null) result.xh = klw.xh;

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
