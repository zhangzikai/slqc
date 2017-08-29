package com.tdgeos.dlg.yddc;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Trgx;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class TrgxDialog extends Dialog implements View.OnClickListener {
    private Handler mHandler = null;
    private Context context;
    private int ydh = 0;

    private EditText etSz = null;
    private EditText etZs1 = null;
    private EditText etZs2 = null;
    private EditText etZs3 = null;
    private Spinner spJkzk = null;
    private Spinner spPhqk = null;

    private Button btnSz = null;
    private Button btnOk = null;
    private Button btnCancel = null;

    private Trgx trgx = null;
    private Trgx result = null;

    public TrgxDialog(Context context, Trgx trgx, int ydh) {
        super(context);
        this.context = context;
        this.trgx = trgx;
        this.ydh = ydh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_trgx);
        this.setTitle("天然更新情况");
        this.getWindow().getAttributes().width = MyConfig.GetBigWidth();
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
        etZs1 = (EditText) findViewById(R.id.et_zs1);
        etZs2 = (EditText) findViewById(R.id.et_zs2);
        etZs3 = (EditText) findViewById(R.id.et_zs3);
        spJkzk = (Spinner) findViewById(R.id.sp_jkzk);
        spPhqk = (Spinner) findViewById(R.id.sp_phqk);

        List<String> lstJkzk = new ArrayList<String>();
        lstJkzk.add("");
        lstJkzk.add("健壮");
        lstJkzk.add("一般");
        lstJkzk.add("较弱");
        ArrayAdapter<String> apJkzk = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstJkzk);
        apJkzk.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spJkzk.setAdapter(apJkzk);

        List<String> lstPhqk = new ArrayList<String>();
        lstPhqk.add("");
        lstPhqk.add("严重");
        lstPhqk.add("较轻");
        lstPhqk.add("无");
        ArrayAdapter<String> apPhqk = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstPhqk);
        apPhqk.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPhqk.setAdapter(apPhqk);

        if (trgx != null) {
            etSz.setText(trgx.sz);
            etZs1.setText(String.valueOf(trgx.zs1));
            etZs2.setText(String.valueOf(trgx.zs2));
            etZs3.setText(String.valueOf(trgx.zs3));
            int pos = 0;
            if (trgx.jkzk.equals("健壮")) pos = 1;
            if (trgx.jkzk.equals("一般")) pos = 2;
            if (trgx.jkzk.equals("较弱")) pos = 3;
            spJkzk.setSelection(pos);
            pos = 0;
            if (trgx.phqk.equals("严重")) pos = 1;
            if (trgx.phqk.equals("较轻")) pos = 2;
            if (trgx.phqk.equals("无")) pos = 3;
            spPhqk.setSelection(pos);
        }

        btnSz = (Button) findViewById(R.id.btn_sz);
        btnSz.setOnClickListener(this);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    public Trgx showDialog() {
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
                String s_zs1 = etZs1.getText().toString();
                String s_zs2 = etZs2.getText().toString();
                String s_zs3 = etZs3.getText().toString();
                String s_jkzk = spJkzk.getSelectedItem().toString();
                String s_phqk = spPhqk.getSelectedItem().toString();
                int i_zs1 = 0;
                int i_zs2 = 0;
                int i_zs3 = 0;

                if (s_sz.equals("")) {
                    Toast.makeText(context, "树种不能为空！", 1).show();
                    break;
                }
                if (trgx == null) {
                    List<Trgx> lst = YangdiMgr.GetTrgxList(ydh);
                    boolean b = false;
                    for (int i = 0; i < lst.size(); i++) {
                        if (s_sz.equalsIgnoreCase(lst.get(i).sz)) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        Toast.makeText(context, "该树种的天然更新记录已经存在！", 1).show();
                        break;
                    }
                } else if (!trgx.sz.equals(s_sz)) {
                    List<Trgx> lst = YangdiMgr.GetTrgxList(ydh);
                    boolean b = false;
                    for (int i = 0; i < lst.size(); i++) {
                        if (s_sz.equalsIgnoreCase(lst.get(i).sz)) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        Toast.makeText(context, "该树种的天然更新记录已经存在！", 1).show();
                        break;
                    }
                }
                if (s_jkzk.equals("")) {
                    Toast.makeText(context, "健康状况不能为空！", 1).show();
                    break;
                }
                if (s_phqk.equals("")) {
                    Toast.makeText(context, "破坏情况不能为空！", 1).show();
                    break;
                }

                try {
                    i_zs1 = Integer.parseInt(s_zs1);
                } catch (java.lang.NumberFormatException nfe) {
                }
                try {
                    i_zs2 = Integer.parseInt(s_zs2);
                } catch (java.lang.NumberFormatException nfe) {
                }
                try {
                    i_zs3 = Integer.parseInt(s_zs3);
                } catch (java.lang.NumberFormatException nfe) {
                }

                result = new Trgx();
                result.sz = s_sz;
                result.zs1 = i_zs1;
                result.zs2 = i_zs2;
                result.zs3 = i_zs3;
                result.jkzk = s_jkzk;
                result.phqk = s_phqk;
                if (trgx != null) result.xh = trgx.xh;

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
