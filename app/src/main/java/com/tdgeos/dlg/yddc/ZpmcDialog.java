package com.tdgeos.dlg.yddc;

import com.tdgeos.lib.MyFile;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Qianqimgr;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class ZpmcDialog extends Dialog implements OnClickListener {
    private RadioGroup rgType;
    private EditText etName;
    private EditText etInfo;
    private EditText etYmh;
    private LinearLayout layYmh;
    private Button btnOk;
    private Button btnCancel;
    private Handler mHandler;
    private Context ctt;
    private String exname = "jpg";
    private int ydh;
    private int ymh;
    private int type = 1;
    private String[] result;

    public ZpmcDialog(Context context, int ydh, int ymh) {
        super(context);
        ctt = context;
        this.ydh = ydh;
        this.ymh = ymh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_yddc_zpmc);
        setTitle("照片名称");
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        String name = Qianqimgr.GetXianJuNameByYdh(ydh) + "-" + ydh + "-X-";
        int pos = 1;
        for (int i = 0; i < 100; i++) {
            if (YangdiMgr.IsValidZpmc(ydh, name + (i + 1))) {
                pos = i + 1;
                break;
            }
        }
        name += pos;

        layYmh = (LinearLayout) findViewById(R.id.lay_ymh);
        etYmh = (EditText) findViewById(R.id.et_ymh);

        rgType = (RadioGroup) findViewById(R.id.rg_type);
        etName = (EditText) findViewById(R.id.et_name);
        etName.setText(name);
        etInfo = (EditText) findViewById(R.id.et_info);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        rgType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                if (arg1 == R.id.rb_yd) {
                    type = 1;
                    layYmh.setVisibility(8);
                }
                if (arg1 == R.id.rb_ym) {
                    type = 2;
                    layYmh.setVisibility(1);
                }
                if (arg1 == R.id.rb_qt) {
                    type = 3;
                    layYmh.setVisibility(8);
                }
            }
        });

        if (ymh > 0) {
            etYmh.setText(String.valueOf(ymh));
            layYmh.setVisibility(1);
            rgType.check(R.id.rb_ym);
        } else {
            layYmh.setVisibility(8);
        }
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
            case R.id.btn_ok: {
                String newname = etName.getText().toString();
                if (newname.equals("")) {
                    Toast.makeText(ctt, "名称不能为空！", 1).show();
                    etName.setFocusable(true);
                    break;
                }
                if (exname != null && MyFile.GetFileExtension(newname) != null && !MyFile.GetFileExtension(newname).equalsIgnoreCase(exname)) {
                    newname += "." + exname;
                }
                String str = MyFile.GetFileNameNoEx(newname);
                if (!YangdiMgr.IsValidZpmc(ydh, str)) {
                    Toast.makeText(ctt, "名称已经存在，请重新输入！", 1).show();
                    etName.setFocusable(true);
                    break;
                }
                result = new String[4];
                result[0] = String.valueOf(type);
                result[1] = etYmh.getText().toString();
                result[2] = etInfo.getText().toString();
                result[3] = newname;
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
