package com.tdgeos.tanhui;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.dlg.yddc.KjlDialog;
import com.tdgeos.yangdi.Kjl;
import com.tdgeos.yangdi.Qianqimgr;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SlqcKjl extends Activity implements View.OnClickListener {
    private int ydh = 0;
    private int iStatus = 0;

    private Button btnClose = null;
    private Button btnQianqi = null;
    private Button btnAdd = null;
    private Button btnDel = null;
    private Button btnSave = null;
    private Button btnFinish = null;

    private LayoutInflater layInflater = null;
    private LinearLayout layList = null;

    private LinearLayout layQq = null;
    private LinearLayout layQqList = null;
    private LinearLayout layQqWu = null;
    private CheckBox cbAll = null;
    private Button btnCopy = null;
    private boolean bVisible = false;
    private List<Kjl> lstQq = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.slqc_kjl);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = this.getIntent();
        ydh = intent.getIntExtra("ydh", 0);
        int[] ii = YangdiMgr.GetDczt(ydh);
        iStatus = ii[6];

        btnClose = (Button) findViewById(R.id.btn_close);
        btnQianqi = (Button) findViewById(R.id.btn_qianqi);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDel = (Button) findViewById(R.id.btn_del);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        btnClose.setOnClickListener(this);
        btnQianqi.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        layInflater = LayoutInflater.from(this);
        layList = (LinearLayout) findViewById(R.id.lay_list);

        layQq = (LinearLayout) findViewById(R.id.lay_qq);
        layQqList = (LinearLayout) findViewById(R.id.lay_qq_list);
        layQqWu = (LinearLayout) findViewById(R.id.lay_qq_wu);
        cbAll = (CheckBox) findViewById(R.id.cb_all);
        btnCopy = (Button) findViewById(R.id.btn_copy);
        btnCopy.setOnClickListener(this);

        cbAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                int n = layQqList.getChildCount();
                for (int i = 0; i < n; i++) {
                    LinearLayout layRow = (LinearLayout) layQqList.getChildAt(i);
                    CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
                    cb.setChecked(arg1);
                }
            }
        });

        initData();
        initQqData();

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
                case 0: {
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
            case R.id.btn_qianqi: {
                if (bVisible) {
                    TranslateAnimation ta = new TranslateAnimation(0, 0, 0, layQq.getHeight());
                    ta.setDuration(200);
                    ta.setFillAfter(true);
                    ta.setAnimationListener(new AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation arg0) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            layQq.setVisibility(8);
                        }
                    });
                    layQq.startAnimation(ta);
                    bVisible = false;
                } else {
                    TranslateAnimation ta = new TranslateAnimation(0, 0, layQq.getHeight(), 0);
                    ta.setDuration(200);
                    ta.setFillAfter(true);
                    ta.setAnimationListener(new AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation arg0) {
                            layQq.setVisibility(1);
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                        }
                    });
                    layQq.startAnimation(ta);
                    bVisible = true;
                }
                break;
            }
            case R.id.btn_copy: {
                int n = layQqList.getChildCount();
                for (int i = 0; i < n; i++) {
                    LinearLayout layRow = (LinearLayout) layQqList.getChildAt(i);
                    CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
                    if (cb.isChecked()) {
                        Kjl item = lstQq.get(i);
                        item.yssz = Resmgr.GetSzNewCodeByOldCode(item.yssz);
                        YangdiMgr.AddKjl(ydh, item);
                        initData();
                    }
                }
                cbAll.setChecked(false);
                break;
            }
            case R.id.btn_save: {
                List<String> lstError = new ArrayList<String>();
                List<String> lstWarn = new ArrayList<String>();
                YangdiMgr.CheckKjl(ydh, lstError, lstWarn);
                if (lstError.size() > 0) {
                    iStatus = 1;
                }
                if (iStatus != 2) {
                    iStatus = 1;
                }
                String sql = "update dczt set kjl = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                break;
            }
            case R.id.btn_finish: {
                List<String> lstError = new ArrayList<String>();
                List<String> lstWarn = new ArrayList<String>();
                YangdiMgr.CheckKjl(ydh, lstError, lstWarn);
                if (lstError.size() > 0) {
                    iStatus = 1;
                } else {
                    iStatus = 2;
                }
                String sql = "update dczt set kjl = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                finish();
                break;
            }
            case R.id.btn_add: {
                if (layList.getChildCount() == 3) {
                    Toast.makeText(this, "最多只能添加三个跨角林记录！", 1).show();
                    break;
                }
                KjlDialog dlg = new KjlDialog(this, null, ydh);
                Kjl kjl = dlg.showDialog();
                if (kjl != null) {
                    YangdiMgr.AddKjl(ydh, kjl);
                    initData();
                }
                if (!YangdiMgr.IsHasKjlxh(ydh, 1)) {
                    YangdiMgr.SetKjlYssz(ydh, 1, 0, "");
                }
                if (!YangdiMgr.IsHasKjlxh(ydh, 2)) {
                    YangdiMgr.SetKjlYssz(ydh, 2, 0, "");
                }
                if (!YangdiMgr.IsHasKjlxh(ydh, 3)) {
                    YangdiMgr.SetKjlYssz(ydh, 3, 0, "");
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
                    Kjl kjl = YangdiMgr.GetKjl(ydh, xh);
                    KjlDialog dlg = new KjlDialog(this, kjl, ydh);
                    Kjl item = dlg.showDialog();
                    if (item != null) {
                        YangdiMgr.UpdateKjl(ydh, item);
                        layRow.setTag(item.xh);
                        TextView tvXh = (TextView) layRow.findViewById(R.id.tv_xh);
                        TextView tvMjbl = (TextView) layRow.findViewById(R.id.tv_mjbl);
                        TextView tvDl = (TextView) layRow.findViewById(R.id.tv_dl);
                        TextView tvTdqs = (TextView) layRow.findViewById(R.id.tv_tdqs);
                        TextView tvLmqs = (TextView) layRow.findViewById(R.id.tv_lmqs);
                        TextView tvLinzh = (TextView) layRow.findViewById(R.id.tv_linzh);
                        TextView tvQy = (TextView) layRow.findViewById(R.id.tv_qy);
                        TextView tvYssz = (TextView) layRow.findViewById(R.id.tv_yssz);
                        tvXh.setText(String.valueOf(item.xh));
                        if (item.mjbl > 0) tvMjbl.setText(String.valueOf(item.mjbl));
                        tvDl.setText(Resmgr.GetValueByCode("dl", item.dl));
                        tvTdqs.setText(Resmgr.GetValueByCode("tdqs", item.tdqs));
                        tvLmqs.setText(Resmgr.GetValueByCode("lmqs", item.lmqs));
                        tvLinzh.setText(Resmgr.GetValueByCode("linzh", item.linzh));
                        tvQy.setText(Resmgr.GetValueByCode("qy", item.qy));
                        tvYssz.setText(Resmgr.GetSzName(item.yssz));
                    }
                    if (!YangdiMgr.IsHasKjlxh(ydh, 1)) {
                        YangdiMgr.SetKjlYssz(ydh, 1, 0, "");
                    }
                    if (!YangdiMgr.IsHasKjlxh(ydh, 2)) {
                        YangdiMgr.SetKjlYssz(ydh, 2, 0, "");
                    }
                    if (!YangdiMgr.IsHasKjlxh(ydh, 3)) {
                        YangdiMgr.SetKjlYssz(ydh, 3, 0, "");
                    }
                }
                break;
            }
        }
    }

    private void initData() {
        layList.removeAllViews();
        List<Kjl> lst = YangdiMgr.GetKjlList(ydh);
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                addRow(lst.get(i));
            }
        }
    }

    private void addRow(Kjl item) {
        if (item == null) return;
        LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.slqc_kjl_item, null);
        layRow.setTag(item.xh);
        layRow.setOnClickListener(this);
        CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
        cb.setTag(item.xh);
        TextView tvXh = (TextView) layRow.findViewById(R.id.tv_xh);
        TextView tvMjbl = (TextView) layRow.findViewById(R.id.tv_mjbl);
        TextView tvDl = (TextView) layRow.findViewById(R.id.tv_dl);
        TextView tvTdqs = (TextView) layRow.findViewById(R.id.tv_tdqs);
        TextView tvLmqs = (TextView) layRow.findViewById(R.id.tv_lmqs);
        TextView tvLinzh = (TextView) layRow.findViewById(R.id.tv_linzh);
        TextView tvQy = (TextView) layRow.findViewById(R.id.tv_qy);
        TextView tvYssz = (TextView) layRow.findViewById(R.id.tv_yssz);
        tvXh.setText(String.valueOf(item.xh));
        if (item.mjbl > 0) tvMjbl.setText(String.valueOf(item.mjbl));
        tvDl.setText(Resmgr.GetValueByCode("dl", item.dl));
        tvTdqs.setText(Resmgr.GetValueByCode("tdqs", item.tdqs));
        tvLmqs.setText(Resmgr.GetValueByCode("lmqs", item.lmqs));
        tvLinzh.setText(Resmgr.GetValueByCode("linzh", item.linzh));
        tvQy.setText(Resmgr.GetValueByCode("qy", item.qy));
        tvYssz.setText(Resmgr.GetSzName(item.yssz));
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
                YangdiMgr.DelKjl(ydh, xh);
            }
        }
    }

    private void initQqData() {
        layQqList.removeAllViews();
        lstQq = Qianqimgr.GetQqKjlList(ydh);
        if (lstQq != null && lstQq.size() > 0) {
            for (int i = 0; i < lstQq.size(); i++) {
                addQqRow(lstQq.get(i));
            }
        } else {
            layQqWu.setVisibility(1);
        }
    }

    private void addQqRow(Kjl item) {
        if (item == null) return;
        LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.slqc_kjl_item, null);
        TextView tvXh = (TextView) layRow.findViewById(R.id.tv_xh);
        TextView tvMjbl = (TextView) layRow.findViewById(R.id.tv_mjbl);
        TextView tvDl = (TextView) layRow.findViewById(R.id.tv_dl);
        TextView tvTdqs = (TextView) layRow.findViewById(R.id.tv_tdqs);
        TextView tvLmqs = (TextView) layRow.findViewById(R.id.tv_lmqs);
        TextView tvLinzh = (TextView) layRow.findViewById(R.id.tv_linzh);
        TextView tvQy = (TextView) layRow.findViewById(R.id.tv_qy);
        TextView tvYssz = (TextView) layRow.findViewById(R.id.tv_yssz);
        tvXh.setText(String.valueOf(item.xh));
        tvMjbl.setText(String.valueOf(item.mjbl));
        tvDl.setText(Resmgr.GetValueByCode("dl", item.dl));
        tvTdqs.setText(Resmgr.GetValueByCode("tdqs", item.tdqs));
        tvLmqs.setText(Resmgr.GetValueByCode("lmqs", item.lmqs));
        tvLinzh.setText(Resmgr.GetValueByCode("linzh", item.linzh));
        tvQy.setText(Resmgr.GetValueByCode("qy", item.qy));
        int code = item.yssz;
        String name = Resmgr.GetSzName(code);
        String yssz = "";
        if (code > 0 && !name.equals("")) {
            yssz = code + " " + name;
        } else {
            yssz = "";
        }
        tvYssz.setText(yssz);
        layQqList.addView(layRow);
    }
}
