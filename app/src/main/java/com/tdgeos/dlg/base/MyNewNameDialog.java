package com.tdgeos.dlg.base;

import com.tdgeos.lib.MyFile;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;

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
import android.widget.Toast;

public class MyNewNameDialog extends Dialog implements OnClickListener {
    private EditText etName;
    private Button btnOk;
    private Button btnCancel;
    private Handler mHandler;
    private Context ctt;
    private String dir;
    private String exname;
    private String name;
    private String result;

    public MyNewNameDialog(Context context, String name, String dir, String exname) {
        super(context);
        ctt = context;
        this.name = name;
        this.dir = dir;
        this.exname = exname;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_base_newname);
        setTitle("重命名");
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        etName = (EditText) findViewById(R.id.et_name);
        etName.setText(name);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
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
                result = newname;
                if (dir != null && MyFile.IsDirectory(dir)) {
                    String path = dir + "/" + newname;
                    if (MyFile.Exists(path)) {
                        Toast.makeText(ctt, "名称已经存在，请重新输入！", 1).show();
                        etName.setFocusable(true);
                        result = null;
                        break;
                    }
                }
                this.cancel();
            }
            break;
            case R.id.btn_cancel: {
                this.cancel();
            }
            break;
        }
    }
}
