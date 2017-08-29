package com.tdgeos.tanhui;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.dlg.base.MyListDialog;
import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.dlg.yddc.XmDialog;
import com.tdgeos.yangdi.Qianqimgr;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Trgx;
import com.tdgeos.yangdi.Xm;
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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class SlqcXmdc extends Activity implements View.OnClickListener {
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
    private List<Xm> lstQq = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.slqc_xmdc);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = this.getIntent();
        ydh = intent.getIntExtra("ydh", 0);
        int[] ii = YangdiMgr.GetDczt(ydh);
        iStatus = ii[12];

        btnClose = (Button) findViewById(R.id.btn_close);
        btnQianqi = (Button) findViewById(R.id.btn_qq);
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

        if (!Qianqimgr.IsYangfang(ydh)) {
            btnQianqi.setVisibility(8);
        }

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
            case R.id.btn_qq: {
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
                List<String> items = new ArrayList<String>();
                items.add("覆盖已录入数据");
                items.add("跳过已录入数据");
                MyListDialog dlg = new MyListDialog(this, "选项", items);
                int r = dlg.showDialog();
                if (r == -1) return;

                int n = layQqList.getChildCount();
                for (int i = 0; i < n; i++) {
                    LinearLayout layRow = (LinearLayout) layQqList.getChildAt(i);
                    CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
                    if (cb.isChecked()) {
                        Xm item = lstQq.get(i);
                        boolean b = YangdiMgr.IsHaveXmdc(ydh, item);
                        if (b) {
                            if (r == 0) {
                                String sql = "update xmdc set "
                                        + "gd = '" + item.gd + "', "
                                        + "xj = '" + item.xj + "' "
                                        + " where ydh = '" + ydh + "' and mc = '" + item.mc + "'";
                                YangdiMgr.ExecSQL(ydh, sql);
                            } else {
                                continue;
                            }
                        } else {
                            YangdiMgr.AddXm(ydh, item);
                        }
                    }
                }
                initData();
                cbAll.setChecked(false);
                break;
            }
            case R.id.btn_save: {
                String sql = "select count(mc) from xmdc where ydh = '" + ydh + "' group by mc having count(mc) > 1";
                if (YangdiMgr.QueryExists(ydh, sql)) {
                    Toast.makeText(this, "下木名称有重复！", 1).show();
                    iStatus = 1;
                }
                if (iStatus != 2) {
                    iStatus = 1;
                }
                sql = "update dczt set xmdc = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                break;
            }
            case R.id.btn_finish: {
                String sql = "select count(mc) from xmdc where ydh = '" + ydh + "' group by mc having count(mc) > 1";
                if (YangdiMgr.QueryExists(ydh, sql)) {
                    Toast.makeText(this, "下木名称有重复！", 1).show();
                    break;
                }
                sql = "update dczt set xmdc = '2' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                finish();
                break;
            }
            case R.id.btn_add: {
                XmDialog dlg = new XmDialog(this, null, ydh);
                Xm item = dlg.showDialog();
                if (item != null) {
                    YangdiMgr.AddXm(ydh, item);
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
                    Xm xm = YangdiMgr.GetXm(ydh, xh);
                    XmDialog dlg = new XmDialog(this, xm, ydh);
                    Xm item = dlg.showDialog();
                    if (item != null) {
                        YangdiMgr.UpdateXm(ydh, item);
                        TextView tvMc = (TextView) layRow.findViewById(R.id.tv_mc);
                        TextView tvGd = (TextView) layRow.findViewById(R.id.tv_sg);
                        TextView tvXj = (TextView) layRow.findViewById(R.id.tv_xj);
                        tvMc.setText(item.mc);
                        tvGd.setText(String.valueOf(item.gd));
                        tvXj.setText(String.valueOf(item.xj));
                    }
                }
                break;
            }
        }
    }

    private void initData() {
        layList.removeAllViews();
        List<Xm> lst = YangdiMgr.GetXmList(ydh);
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                addRow(lst.get(i));
            }
        }
    }

    private void addRow(Xm item) {
        if (item == null) return;
        LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.slqc_xm_item, null);
        layRow.setTag(item.xh);
        layRow.setOnClickListener(this);
        CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
        cb.setTag(item.xh);
        TextView tvMc = (TextView) layRow.findViewById(R.id.tv_mc);
        TextView tvGd = (TextView) layRow.findViewById(R.id.tv_sg);
        TextView tvXj = (TextView) layRow.findViewById(R.id.tv_xj);
        tvMc.setText(item.mc);
        tvGd.setText(String.valueOf(item.gd));
        tvXj.setText(String.valueOf(item.xj));
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
                YangdiMgr.DelXm(ydh, xh);
            }
        }
    }

    private void initQqData() {
        layQqList.removeAllViews();
        lstQq = Qianqimgr.GetQqXmList(ydh);
        if (lstQq != null && lstQq.size() > 0) {
            for (int i = 0; i < lstQq.size(); i++) {
                addQqRow(lstQq.get(i));
            }
        } else {
            layQqWu.setVisibility(1);
        }
    }

    private void addQqRow(Xm item) {
        if (item == null) return;
        LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.slqc_xm_item, null);
        TextView tvMc = (TextView) layRow.findViewById(R.id.tv_mc);
        TextView tvGd = (TextView) layRow.findViewById(R.id.tv_sg);
        TextView tvXj = (TextView) layRow.findViewById(R.id.tv_xj);
        tvMc.setText(item.mc);
        tvGd.setText(String.valueOf(item.gd));
        tvXj.setText(String.valueOf(item.xj));
        layQqList.addView(layRow);
    }
}
