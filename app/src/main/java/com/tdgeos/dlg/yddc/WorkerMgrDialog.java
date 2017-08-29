package com.tdgeos.dlg.yddc;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Setmgr;
import com.tdgeos.yangdi.WorkerInfo;

public class WorkerMgrDialog extends Dialog implements OnClickListener {
    private LinearLayout layList = null;
    private LayoutInflater layInflater = null;

    private Button btnAdd = null;
    private Button btnDel = null;
    private Button btnOk;
    private Button btnCancel;

    private Context ctt;

    public WorkerMgrDialog(Context context) {
        super(context);
        ctt = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_yddc_workermgr);
        setTitle("人员管理");
        this.getWindow().getAttributes().width = MyConfig.GetBigWidth();
        this.getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        layInflater = LayoutInflater.from(ctt);
        layList = (LinearLayout) findViewById(R.id.lay_list);

        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDel = (Button) findViewById(R.id.btn_del);
        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        loadList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add: {
                WorkerAddDialog dlg = new WorkerAddDialog(ctt, null);
                dlg.showDialog();
                loadList();
                break;
            }
            case R.id.btn_del: {
                MyMakeSureDialog dlg = new MyMakeSureDialog(ctt, "删除", "删除后将不可恢复，是否继续删除？", "删除", "取消");
                if (dlg.showDialog()) {
                    delRow();
                }
                break;
            }
            default: {
                int id = (Integer) v.getTag();
                WorkerInfo worker = Setmgr.GetWorker(id);
                WorkerAddDialog dlg = new WorkerAddDialog(ctt, worker);
                dlg.showDialog();
                loadList();
                break;
            }
            case R.id.btn_ok: {
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
        List<WorkerInfo> lst = Setmgr.GetWorkerList(-1);
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
        layRow.setTag(item.id);
        layRow.setOnClickListener(this);
        layList.addView(layRow);
    }

    private void delRow() {
        int n = layList.getChildCount();
        for (int i = n - 1; i >= 0; i--) {
            LinearLayout layRow = (LinearLayout) layList.getChildAt(i);
            CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_check);
            if (cb.isChecked()) {
                int id = (Integer) cb.getTag();
                layList.removeViewAt(i);
                Setmgr.RemoveWorker(id);
            }
        }
    }
}
