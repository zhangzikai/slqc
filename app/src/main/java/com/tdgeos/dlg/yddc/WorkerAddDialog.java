package com.tdgeos.dlg.yddc;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Setmgr;
import com.tdgeos.yangdi.WorkerInfo;

public class WorkerAddDialog extends Dialog implements OnClickListener {
    private EditText etName;
    private Spinner spType;
    private EditText etPhone;
    private EditText etCompany;
    private EditText etAddress;
    private EditText etNotes;

    private Button btnOk;
    private Button btnCancel;

    private Context ctt;
    private Handler mHandler;
    private WorkerInfo info;

    public WorkerAddDialog(Context context, WorkerInfo info) {
        super(context);
        ctt = context;
        this.info = info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_yddc_workeradd);
        setTitle("添加人员");
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.et_name);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etCompany = (EditText) findViewById(R.id.et_company);
        etAddress = (EditText) findViewById(R.id.et_address);
        etNotes = (EditText) findViewById(R.id.et_notes);

        spType = (Spinner) findViewById(R.id.sp_type);
        List<String> lst = new ArrayList<String>();
        lst.add("调查员");
        lst.add("检查员");
        lst.add("向导");
        ArrayAdapter<String> ap = new ArrayAdapter<String>(ctt, android.R.layout.simple_spinner_item, lst);
        ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(ap);

        if (info != null) {
            etName.setText(info.name);
            etPhone.setText(info.phone);
            etCompany.setText(info.company);
            etAddress.setText(info.address);
            etNotes.setText(info.notes);

            spType.setSelection(info.type);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok: {
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                String company = etCompany.getText().toString();
                String address = etAddress.getText().toString();
                String notes = etNotes.getText().toString();
                int type = spType.getSelectedItemPosition();
                if (name.equals("")) {
                    Toast.makeText(ctt, "姓名不能为空！", 1).show();
                    return;
                }
                if (phone.equals("")) {
                    Toast.makeText(ctt, "电话不能为空！", 1).show();
                    return;
                }
                if (company.equals("")) {
                    Toast.makeText(ctt, "单位不能为空！", 1).show();
                    return;
                }
                WorkerInfo worker = new WorkerInfo();
                worker.name = name;
                worker.type = type;
                worker.phone = phone;
                worker.company = company;
                worker.address = address;
                worker.notes = notes;
                if (info == null) {
                    Setmgr.AddWorker(worker);
                } else {
                    worker.id = info.id;
                    Setmgr.UpdateWorker(worker);
                }

                this.cancel();
            }
            break;
            case R.id.btn_cancel:
                this.cancel();
                break;
        }
    }

    public void showDialog() {
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
        return;
    }
}
