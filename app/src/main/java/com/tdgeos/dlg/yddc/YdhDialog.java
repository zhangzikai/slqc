package com.tdgeos.dlg.yddc;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Setmgr;

public class YdhDialog extends Dialog implements OnClickListener {
    private RadioGroup rgType = null;
    private LinearLayout lay1 = null;
    private LinearLayout lay2 = null;
    private EditText etYdh = null;
    private Spinner spYdh = null;

    private boolean bType = false;

    private Button btnOk;
    private Button btnCancel;

    private Context ctt;
    private Handler mHandler;
    private int ydh;

    public YdhDialog(Context context, int ydh) {
        super(context);
        ctt = context;
        this.ydh = ydh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_ydh);
        setTitle("切换样地");
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        //this.getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        lay1 = (LinearLayout) findViewById(R.id.lay_input);
        lay2 = (LinearLayout) findViewById(R.id.lay_list);
        etYdh = (EditText) findViewById(R.id.et_ydh);
        spYdh = (Spinner) findViewById(R.id.sp_ydh);
        rgType = (RadioGroup) findViewById(R.id.rg_ydh);
        rgType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup v, int id) {
                if (id == R.id.rb_input) {
                    lay1.setVisibility(1);
                    lay2.setVisibility(8);
                    bType = false;
                } else {
                    lay1.setVisibility(8);
                    lay2.setVisibility(1);
                    bType = true;
                }
            }
        });

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        List<String> lstYdhs = Setmgr.GetYdhList();
        ArrayAdapter<String> ap = new ArrayAdapter<String>(ctt, android.R.layout.simple_spinner_item, lstYdhs);
        ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYdh.setAdapter(ap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok: {
                String sydh = null;
                if (bType) {
                    sydh = spYdh.getSelectedItem().toString();
                } else {
                    sydh = etYdh.getText().toString();
                }
                try {
                    ydh = Integer.parseInt(sydh);
                } catch (Exception e) {
                }
                if (Setmgr.GetTask(ydh) == null) {
                    ydh = 0;
                    Toast.makeText(ctt, "样地号设置错误，该样地号不在任务列表中！", 1).show();
                    break;
                }
                this.cancel();
                break;
            }
            case R.id.btn_cancel: {
                this.cancel();
                break;
            }
        }
    }

    public int showDialog() {
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
        return ydh;
    }
}
