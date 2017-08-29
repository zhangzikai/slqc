package com.tdgeos.dlg.base;

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
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//仅仅返回一个文件夹名称
public class MyNewFolderDialog extends Dialog implements OnClickListener {
    private EditText etName;
    private Button btnOk;
    private Button btnCancel;
    private Handler mHandler;
    private Context ctt;
    private String result;

    public MyNewFolderDialog(Context context) {
        super(context);
        ctt = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_base_newfolder);
        setTitle("新建文件夹");
        this.getWindow().getAttributes().width = MyConfig.GetBigWidth();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        etName = (EditText) findViewById(R.id.et_name);
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
                String foldername = etName.getText().toString();
                if (foldername.equals("")) {
                    Toast.makeText(ctt, "请输入文件夹名.", 1).show();
                    etName.setFocusable(true);
                    break;
                }
                result = foldername;
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
