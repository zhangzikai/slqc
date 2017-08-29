package com.tdgeos.tanhui;

import java.util.List;

import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.dlg.yddc.KlwDialog;
import com.tdgeos.yangdi.Klw;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SlqcKlwdc extends Activity implements View.OnClickListener {
    private int ydh = 0;
    private int iStatus = 0;

    private Button btnClose = null;
    private Button btnAdd = null;
    private Button btnDel = null;
    private Button btnSave = null;
    private Button btnFinish = null;

    private LayoutInflater layInflater = null;
    private LinearLayout layList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.slqc_klwdc);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = this.getIntent();
        ydh = intent.getIntExtra("ydh", 0);
        iStatus = YangdiMgr.GetKlwdcStatu(ydh);

        btnClose = (Button) findViewById(R.id.btn_close);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDel = (Button) findViewById(R.id.btn_del);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        btnClose.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        layInflater = LayoutInflater.from(this);
        layList = (LinearLayout) findViewById(R.id.lay_list);

        initData();

    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 101: {
                    break;
                }
                default:
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close: {
                this.finish();
                break;
            }
            case R.id.btn_save: {
                int n = YangdiMgr.GetKlwCount(ydh);
                if (n != 3) {
                    iStatus = 1;
                }
                if (iStatus != 2) {
                    iStatus = 1;
                }
                String sql = "update tag set f11 = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                break;
            }
            case R.id.btn_finish: {
                int n = YangdiMgr.GetKlwCount(ydh);
                if (n <= 0) {
                    Toast.makeText(this, "尚未进行枯落物调查！", 1).show();
                    break;
                } else if (n < 3) {
                    Toast.makeText(this, "枯落物调查记录过少！", 1).show();
                    break;
                } else if (n > 3) {
                    Toast.makeText(this, "枯落物调查记录过多！", 1).show();
                    break;
                }
                String sql = "update tag set f11 = '2' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                finish();
                break;
            }
            case R.id.btn_add: {
                KlwDialog dlg = new KlwDialog(this, null, ydh);
                Klw item = dlg.showDialog();
                if (item != null) {
                    YangdiMgr.AddKlw(ydh, item);
                    initData();
                }
                break;
            }
            case R.id.btn_del: {
                MyMakeSureDialog dlg = new MyMakeSureDialog(this, "删除", "删除后将不可恢复，是否继续删除？", "删除", "取消");
                if (dlg.showDialog()) {
                    delRow();
                }
                break;
            }
            default: {
                LinearLayout layRow = null;
                try {
                    layRow = (LinearLayout) v;
                } catch (Exception e) {
                }
                if (layRow != null) {
                    int xh = (Integer) layRow.getTag();
                    Klw klw = YangdiMgr.GetKlw(ydh, xh);
                    KlwDialog dlg = new KlwDialog(this, klw, ydh);
                    Klw item = dlg.showDialog();
                    if (item != null) {
                        YangdiMgr.UpdateKlw(ydh, item);
                        TextView tvYfh = (TextView) layRow.findViewById(R.id.tv_yfh);
                        TextView tvHd = (TextView) layRow.findViewById(R.id.tv_hd);
                        TextView tvYfxz = (TextView) layRow.findViewById(R.id.tv_yfxz);
                        TextView tvYfgz = (TextView) layRow.findViewById(R.id.tv_yfgz);
                        TextView tvYpxz = (TextView) layRow.findViewById(R.id.tv_ypxz);
                        TextView tvYpgz = (TextView) layRow.findViewById(R.id.tv_ypgz);
                        tvYfh.setText(item.yfh);
                        tvHd.setText(String.valueOf(item.hd));
                        tvYfxz.setText(String.valueOf(item.yfxz));
                        tvYfgz.setText(String.valueOf(item.yfgz));
                        tvYpxz.setText(String.valueOf(item.ypxz));
                        tvYpgz.setText(String.valueOf(item.ypgz));
                    }
                }
                break;
            }
        }
    }

    private void initData() {
        layList.removeAllViews();
        List<Klw> lst = YangdiMgr.GetKlwList(ydh);
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                addRow(lst.get(i));
            }
        }
    }

    private void addRow(Klw item) {
        if (item == null) return;
        LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.slqc_klw_item, null);
        layRow.setTag(item.xh);
        layRow.setOnClickListener(this);
        CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
        cb.setTag(item.xh);
        TextView tvYfh = (TextView) layRow.findViewById(R.id.tv_yfh);
        TextView tvHd = (TextView) layRow.findViewById(R.id.tv_hd);
        TextView tvYfxz = (TextView) layRow.findViewById(R.id.tv_yfxz);
        TextView tvYfgz = (TextView) layRow.findViewById(R.id.tv_yfgz);
        TextView tvYpxz = (TextView) layRow.findViewById(R.id.tv_ypxz);
        TextView tvYpgz = (TextView) layRow.findViewById(R.id.tv_ypgz);
        tvYfh.setText(item.yfh);
        tvHd.setText(String.valueOf(item.hd));
        tvYfxz.setText(String.valueOf(item.yfxz));
        tvYfgz.setText(String.valueOf(item.yfgz));
        tvYpxz.setText(String.valueOf(item.ypxz));
        tvYpgz.setText(String.valueOf(item.ypgz));
        layList.addView(layRow);
    }

    private void delRow() {
        int n = layList.getChildCount();
        for (int i = n - 1; i >= 0; i--) {
            LinearLayout layRow = (LinearLayout) layList.getChildAt(i);
            CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
            if (cb.isChecked()) {
                int xh = (Integer) cb.getTag();
                layList.removeViewAt(i);
                YangdiMgr.DelKlw(ydh, xh);
            }
        }
    }

}
