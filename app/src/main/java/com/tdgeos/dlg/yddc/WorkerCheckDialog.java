package com.tdgeos.dlg.yddc;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Setmgr;
import com.tdgeos.yangdi.WorkerInfo;

public class WorkerCheckDialog extends Dialog implements OnClickListener {
    private LinearLayout layList = null;
    private LayoutInflater layInflater = null;

    private CheckBox cbAll = null;
    private Button btnOk;
    private Button btnCancel;
    private Button btnMgr;

    private Context ctt;
    private Handler mHandler;
    private int type = 0;
    private String[] result;

    public WorkerCheckDialog(Context context, int type) {
        super(context);
        ctt = context;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_yddc_workercheck);
        setTitle("选择人员");
        this.getWindow().getAttributes().width = MyConfig.GetBigWidth();
        this.getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        layInflater = LayoutInflater.from(ctt);
        layList = (LinearLayout) findViewById(R.id.lay_list);

        cbAll = (CheckBox) findViewById(R.id.cb_all);
        cbAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                int n = layList.getChildCount();
                for (int i = 0; i < n; i++) {
                    LinearLayout layRow = (LinearLayout) layList.getChildAt(i);
                    CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_check);
                    cb.setChecked(arg1);
                }
            }
        });

        btnMgr = (Button) findViewById(R.id.btn_mgr);
        btnMgr.setOnClickListener(this);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        loadList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_mgr: {
                WorkerAddDialog dlg = new WorkerAddDialog(ctt, null);
                dlg.showDialog();
                loadList();
                break;
            }
            case R.id.btn_ok: {
                int n = layList.getChildCount();
                String names = "";
                String dws = "";
                String ids = "";
                int count = 0;
                for (int i = 0; i < n; i++) {
                    LinearLayout layRow = (LinearLayout) layList.getChildAt(i);
                    CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_check);
                    TextView tvName = (TextView) layRow.findViewById(R.id.tv_name);
                    TextView tvCompany = (TextView) layRow.findViewById(R.id.tv_company);
                    if (cb.isChecked()) {
                        count++;
                        int id = (Integer) cb.getTag();
                        String name = tvName.getText().toString();
                        String dw = tvCompany.getText().toString();
                        if (names.equals("")) {
                            names = name;
                            dws = dw;
                            ids += id;
                        } else {
                            names += "," + name;
                            dws += "," + dw;
                            ids += "," + id;
                        }
                    }
                }
                if (count > 0) {
                    result = new String[3];
                    result[0] = names;
                    result[1] = dws;
                    result[2] = ids;
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

    private void loadList() {
        layList.removeAllViews();
        List<WorkerInfo> lst = Setmgr.GetWorkerList(type);
        if (lst == null) return;
        for (int i = 0; i < lst.size(); i++) {
            addRow(lst.get(i));
        }
    }

    private void addRow(WorkerInfo item) {
        if (item == null) return;
        LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.dlg_yddc_worker_item, null);
        CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_check);
        TextView tvName = (TextView) layRow.findViewById(R.id.tv_name);
        TextView tvType = (TextView) layRow.findViewById(R.id.tv_type);
        TextView tvPhone = (TextView) layRow.findViewById(R.id.tv_phone);
        TextView tvCompany = (TextView) layRow.findViewById(R.id.tv_company);
        TextView tvAddress = (TextView) layRow.findViewById(R.id.tv_address);
        TextView tvNotes = (TextView) layRow.findViewById(R.id.tv_notes);
        cb.setTag(item.id);
        tvName.setText(item.name);
        if (item.type == 0) tvType.setText("调查员");
        if (item.type == 1) tvType.setText("检查员");
        if (item.type == 2) tvType.setText("向导");
        tvPhone.setText(item.phone);
        tvCompany.setText(item.company);
        tvAddress.setText(item.address);
        tvNotes.setText(item.notes);
        layList.addView(layRow);
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
}
