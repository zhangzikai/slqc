package com.tdgeos.dlg.yddc;

import java.util.List;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.tanhui.TukuList;
import com.tdgeos.tanhui.TukuPic;
import com.tdgeos.yangdi.Resmgr;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class SzxzDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private Handler mHandler = null;

    private EditText etSz = null;
    private Button btnReset = null;

    private RadioGroup rgQglx = null;
    private RadioGroup rgLzlx = null;
    private RadioGroup rgZklx = null;
    private RadioGroup rgMclx = null;

    private ListView lvSz = null;

    private Button btnTuku = null;
    private Button btnOk = null;
    private Button btnCancel = null;

    private String result = null;

    public SzxzDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_szxz);
        this.setTitle("树种");
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        this.getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.setCanceledOnTouchOutside(false);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        lvSz = (ListView) findViewById(R.id.lv_sz);
        lvSz.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String str = lvSz.getItemAtPosition(arg2).toString();
                str = Resmgr.GetSzCode(str) + " " + str;
                etSz.setText(str);
            }
        });

        etSz = (EditText) findViewById(R.id.et_sz);
        etSz.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                resetList();
            }
        });

        rgQglx = (RadioGroup) findViewById(R.id.rg_qglx);
        rgQglx.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                resetList();
            }
        });

        rgLzlx = (RadioGroup) findViewById(R.id.rg_lzlx);
        rgLzlx.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                resetList();
            }
        });

        rgZklx = (RadioGroup) findViewById(R.id.rg_zklx);
        rgZklx.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                resetList();
            }
        });

        rgMclx = (RadioGroup) findViewById(R.id.rg_mclx);
        rgMclx.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                resetList();
            }
        });

        btnReset = (Button) findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(this);
        btnTuku = (Button) findViewById(R.id.btn_tuku);
        btnTuku.setOnClickListener(this);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        resetList();
    }

    public String showDialog() {
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
            case R.id.btn_tuku: {
                String str = etSz.getText().toString().trim();
                String[] ss = str.split(" ");
                int dm = -1;
                String mc = null;
                if (ss.length == 1) {
                    try {
                        dm = Integer.parseInt(str);
                    } catch (Exception e) {
                    }
                    if (dm > 0) {
                        mc = Resmgr.GetSzName(dm);
                        if (mc.equals("")) {
                            dm = -1;
                            mc = null;
                        }
                    } else {
                        mc = str;
                        dm = Resmgr.GetSzCode(mc);
                        if (dm <= 0) {
                            dm = -1;
                            mc = null;
                        }
                    }
                } else if (ss.length == 2) {
                    try {
                        dm = Integer.parseInt(ss[0]);
                    } catch (Exception e) {
                    }
                    if (dm <= 0) {
                        dm = -1;
                        mc = null;
                    }
                    mc = ss[1];
                    if (dm != Resmgr.GetSzCode(mc)) {
                        dm = -1;
                        mc = null;
                    }
                }
                if (dm == -1) {
                    if (!str.equals("")) Toast.makeText(context, "图库中无该树种的信息.", 1).show();
                    Intent intent = new Intent();
                    intent.setClass(context, TukuList.class);
                    context.startActivity(intent);
                } else {
                    if (Resmgr.IsHasTuku(dm, mc)) {
                        String en = Resmgr.GetTukuEname(mc);
                        Intent intent = new Intent();
                        intent.putExtra("szmc", mc);
                        intent.putExtra("szdm", dm);
                        intent.putExtra("enmc", en);
                        intent.setClass(context, TukuPic.class);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "图库中无该树种的信息.", 1).show();
                        Intent intent = new Intent();
                        intent.setClass(context, TukuList.class);
                        context.startActivity(intent);
                    }
                }
                break;
            }
            case R.id.btn_reset: {
                etSz.setText("");
                resetList();
                break;
            }
            case R.id.btn_ok: {
                String str = etSz.getText().toString().trim();
                String[] ss = str.split(" ");
                int dm = -1;
                String mc = null;
                if (ss.length == 1) {
                    try {
                        dm = Integer.parseInt(str);
                    } catch (Exception e) {
                    }
                    if (dm > 0) {
                        mc = Resmgr.GetSzName(dm);
                        if (mc.equals("")) {
                            Toast.makeText(context, "树种填写错误，无法匹配该树种的代码！", 1).show();
                            break;
                        }
                    } else {
                        mc = str;
                        dm = Resmgr.GetSzCode(mc);
                        if (dm <= 0) {
                            Toast.makeText(context, "树种填写错误，无法匹配该树种的代码！", 1).show();
                            break;
                        }
                    }
                } else if (ss.length == 2) {
                    try {
                        dm = Integer.parseInt(ss[0]);
                    } catch (Exception e) {
                    }
                    if (dm <= 0) {
                        Toast.makeText(context, "树种填写错误！", 1).show();
                        break;
                    }
                    mc = ss[1];
                    if (dm != Resmgr.GetSzCode(mc)) {
                        Toast.makeText(context, "树种填写错误，代码和名称不一致！", 1).show();
                        break;
                    }
                } else {
                    Toast.makeText(context, "树种填写错误，无法识别的格式！", 1).show();
                    break;
                }

                result = dm + " " + mc;
                if (result.equals("")) result = null;
                this.cancel();
                break;
            }
            case R.id.btn_cancel: {
                this.cancel();
                break;
            }
        }
    }

    private void resetList() {
        String str = etSz.getText().toString().trim();
        int dm = 0;
        try {
            dm = Integer.parseInt(str);
        } catch (Exception e) {
        }

        int qglx = rgQglx.getCheckedRadioButtonId();
        if (qglx == R.id.rb_qglx_all) qglx = 0;
        if (qglx == R.id.rb_qglx_qm) qglx = 1;
        if (qglx == R.id.rb_qglx_gm) qglx = 2;

        int lzlx = rgLzlx.getCheckedRadioButtonId();
        if (lzlx == R.id.rb_lzlx_all) lzlx = 0;
        if (lzlx == R.id.rb_lzlx_fh) lzlx = 1;
        if (lzlx == R.id.rb_lzlx_jj) lzlx = 2;

        int zklx = rgZklx.getCheckedRadioButtonId();
        if (zklx == R.id.rb_zklx_all) zklx = 0;
        if (zklx == R.id.rb_zklx_zy) zklx = 1;
        if (zklx == R.id.rb_zklx_ky) zklx = 2;

        int mclx = rgMclx.getCheckedRadioButtonId();
        if (mclx == R.id.rb_mclx_all) mclx = 0;
        if (mclx == R.id.rb_mclx_mb) mclx = 1;
        if (mclx == R.id.rb_mclx_cb) mclx = 2;

        String sql = "select name from sz where 1=1 ";

        if (dm > 0) {
            sql += " and code like '" + dm + "%' ";
        } else if (!str.equals("")) {
            sql += " and name like '%" + str + "%' ";
        }

        if (qglx == 1) {
            sql += " and qglx in('1','3') ";
        }
        if (qglx == 2) {
            sql += " and qglx in('2','3') ";
        }

        if (lzlx == 1) {
            sql += " and lzlx in('1','3') ";
        }
        if (lzlx == 2) {
            sql += " and lzlx in('2','3') ";
        }

        if (zklx == 1) {
            sql += " and zklx = '1' ";
        }
        if (zklx == 2) {
            sql += " and zklx = '2' ";
        }

        if (mclx == 1) {
            sql += " and cmlx in('1','3') ";
        }
        if (mclx == 2) {
            sql += " and cmlx in('2','3') ";
        }

        List<String> lst = Resmgr.GetSzList(sql);
        lvSz.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, lst));
    }
}
