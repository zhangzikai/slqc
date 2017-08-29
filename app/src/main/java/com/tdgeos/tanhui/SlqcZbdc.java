package com.tdgeos.tanhui;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.dlg.base.MyListDialog;
import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.dlg.yddc.ZbDialog;
import com.tdgeos.yangdi.Qianqimgr;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.YangdiMgr;
import com.tdgeos.yangdi.Zb;

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
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TabHost.OnTabChangeListener;

public class SlqcZbdc extends Activity implements View.OnClickListener {
    private int ydh = 0;
    private int iStatus = 0;

    private Button btnClose = null;
    private Button btnQianqi = null;
    private Button btnAdd = null;
    private Button btnDel = null;
    private Button btnSave = null;
    private Button btnFinish = null;

    private LayoutInflater layInflater = null;
    private LinearLayout layListGm = null;
    private LinearLayout layListCb = null;
    private LinearLayout layListDbw = null;
    private TabHost tabHost = null;
    private int type = 1;

    private LinearLayout layQq = null;
    private LinearLayout layQqList = null;
    private LinearLayout layQqWu = null;
    private CheckBox cbAll = null;
    private Button btnCopy = null;
    private boolean bVisible = false;
    private List<Zb> lstQq = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.slqc_zbdc);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = this.getIntent();
        ydh = intent.getIntExtra("ydh", 0);
        int[] ii = YangdiMgr.GetDczt(ydh);
        iStatus = ii[11];

        btnClose = (Button) findViewById(R.id.btn_close);
        btnQianqi = (Button) findViewById(R.id.btn_qq);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        btnClose.setOnClickListener(this);
        btnQianqi.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        layInflater = LayoutInflater.from(this);

        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("gm").setIndicator("灌木", null).setContent(R.id.tab_gm));
        tabHost.addTab(tabHost.newTabSpec("cb").setIndicator("草本", null).setContent(R.id.tab_cb));
        //tabHost.addTab(tabHost.newTabSpec("dbw").setIndicator("地被物", null).setContent(R.id.tab_dbw)); 
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("gm")) type = 1;
                if (tabId.equals("cb")) type = 2;
                //if(tabId.equals("dbw")) type = 3;
            }
        });
        TabWidget tabWidget = tabHost.getTabWidget();
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(18);
        }

        layListGm = (LinearLayout) findViewById(R.id.lay_gm);
        layListCb = (LinearLayout) findViewById(R.id.lay_cb);
        layListDbw = (LinearLayout) findViewById(R.id.lay_dbw);

        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDel = (Button) findViewById(R.id.btn_del);
        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);

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
                        Zb item = lstQq.get(i);
                        boolean b = YangdiMgr.IsHaveZbdc(ydh, item);
                        if (b) {
                            if (r == 0) {
                                String sql = "update zbdc set "
                                        + "pjgd = '" + item.pjgd + "', "
                                        + "fgd = '" + item.fgd + "', "
                                        + "zs = '" + item.zs + "', "
                                        + "pjdj = '" + item.pjdj + "' "
                                        + " where ydh = '" + ydh + "' and mc = '" + item.mc + "'";
                                YangdiMgr.ExecSQL(ydh, sql);
                            } else {
                                continue;
                            }
                        } else {
                            YangdiMgr.AddZb(ydh, item);
                        }
                    }
                }
                initData();
                cbAll.setChecked(false);
                break;
            }
            case R.id.btn_save: {
                String sql = "select count(mc) from zbdc where ydh = '" + ydh + "' group by mc having count(mc) > 1";
                if (YangdiMgr.QueryExists(ydh, sql)) {
                    Toast.makeText(this, "植被名称有重复！", 1).show();
                    iStatus = 1;
                }
                if (iStatus != 2) {
                    iStatus = 1;
                }
                sql = "update dczt set zbdc = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                break;
            }
            case R.id.btn_finish: {
                String sql = "select count(mc) from zbdc where ydh = '" + ydh + "' group by mc having count(mc) > 1";
                if (YangdiMgr.QueryExists(ydh, sql)) {
                    Toast.makeText(this, "植被名称有重复！", 1).show();
                    break;
                }
                sql = "update dczt set zbdc = '2' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                finish();
                break;
            }
            case R.id.btn_add: {
                ZbDialog dlg = new ZbDialog(this, null, type, ydh);
                Zb item = dlg.showDialog();
                if (item != null) {
                    YangdiMgr.AddZb(ydh, item);
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
                    Zb zb = YangdiMgr.GetZb(ydh, xh);
                    ZbDialog dlg = new ZbDialog(this, zb, type, ydh);
                    Zb item = dlg.showDialog();
                    if (item != null) {
                        YangdiMgr.UpdateZb(ydh, item);
                        TextView tvMc = (TextView) layRow.findViewById(R.id.tv_mc);
                        TextView tvPjgd = (TextView) layRow.findViewById(R.id.tv_pjg);
                        TextView tvFgd = (TextView) layRow.findViewById(R.id.tv_gd);
                        tvMc.setText(String.valueOf(item.mc));
                        tvPjgd.setText(String.valueOf(item.pjgd));
                        tvFgd.setText(String.valueOf(item.fgd));
                        if (type == 1) {
                            TextView tvZs = (TextView) layRow.findViewById(R.id.tv_zs);
                            TextView tvPjdj = (TextView) layRow.findViewById(R.id.tv_dj);
                            tvZs.setText(String.valueOf(item.zs));
                            tvPjdj.setText(String.valueOf(item.pjdj));
                        }
                    }
                }
                break;
            }
        }
    }

    private void initData() {
        loadGm();
        loadCb();
        loadDbw();
    }

    private void loadGm() {
        layListGm.removeAllViews();
        List<Zb> lst = YangdiMgr.GetZbList(ydh, 1);
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                addGm(lst.get(i));
            }
        }
    }

    private void loadCb() {
        layListCb.removeAllViews();
        List<Zb> lst = YangdiMgr.GetZbList(ydh, 2);
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                addCb(lst.get(i));
            }
        }
    }

    private void loadDbw() {
        layListDbw.removeAllViews();
        List<Zb> lst = YangdiMgr.GetZbList(ydh, 3);
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                addDbw(lst.get(i));
            }
        }
    }

    private void addGm(Zb item) {
        if (item == null) return;
        LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.slqc_zb_item_gm, null);
        layRow.setTag(item.xh);
        layRow.setOnClickListener(this);
        CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
        cb.setTag(item.xh);
        TextView tvMc = (TextView) layRow.findViewById(R.id.tv_mc);
        TextView tvPjgd = (TextView) layRow.findViewById(R.id.tv_pjg);
        TextView tvFgd = (TextView) layRow.findViewById(R.id.tv_gd);
        TextView tvZs = (TextView) layRow.findViewById(R.id.tv_zs);
        TextView tvPjdj = (TextView) layRow.findViewById(R.id.tv_dj);
        tvMc.setText(String.valueOf(item.mc));
        tvPjgd.setText(String.valueOf(item.pjgd));
        tvFgd.setText(String.valueOf(item.fgd));
        if (item.zs >= 0) tvZs.setText(String.valueOf(item.zs));
        if (item.pjdj >= 0) tvPjdj.setText(String.valueOf(item.pjdj));
        layListGm.addView(layRow);
    }

    private void addCb(Zb item) {
        if (item == null) return;
        LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.slqc_zb_item, null);
        layRow.setTag(item.xh);
        layRow.setOnClickListener(this);
        CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
        cb.setTag(item.xh);
        TextView tvMc = (TextView) layRow.findViewById(R.id.tv_mc);
        TextView tvPjgd = (TextView) layRow.findViewById(R.id.tv_pjg);
        TextView tvFgd = (TextView) layRow.findViewById(R.id.tv_gd);
        tvMc.setText(String.valueOf(item.mc));
        tvPjgd.setText(String.valueOf(item.pjgd));
        tvFgd.setText(String.valueOf(item.fgd));
        layListCb.addView(layRow);
    }

    private void addDbw(Zb item) {
        if (item == null) return;
        LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.slqc_zb_item, null);
        layRow.setTag(item.xh);
        layRow.setOnClickListener(this);
        CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
        cb.setTag(item.xh);
        TextView tvMc = (TextView) layRow.findViewById(R.id.tv_mc);
        TextView tvPjgd = (TextView) layRow.findViewById(R.id.tv_pjg);
        TextView tvFgd = (TextView) layRow.findViewById(R.id.tv_gd);
        tvMc.setText(String.valueOf(item.mc));
        tvPjgd.setText(String.valueOf(item.pjgd));
        tvFgd.setText(String.valueOf(item.fgd));
        layListDbw.addView(layRow);
    }

    private void delRow() {
        if (type == 1) {
            int n = layListGm.getChildCount();
            for (int i = n - 1; i >= 0; i--) {
                LinearLayout layRow = (LinearLayout) layListGm.getChildAt(i);
                CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
                if (cb.isChecked()) {
                    int xh = (Integer) cb.getTag();
                    layListGm.removeViewAt(i);
                    YangdiMgr.DelZb(ydh, xh);
                }
            }
        }
        if (type == 2) {
            int n = layListCb.getChildCount();
            for (int i = n - 1; i >= 0; i--) {
                LinearLayout layRow = (LinearLayout) layListCb.getChildAt(i);
                CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
                if (cb.isChecked()) {
                    int xh = (Integer) cb.getTag();
                    layListCb.removeViewAt(i);
                    YangdiMgr.DelZb(ydh, xh);
                }
            }
        }
        if (type == 3) {
            int n = layListDbw.getChildCount();
            for (int i = n - 1; i >= 0; i--) {
                LinearLayout layRow = (LinearLayout) layListDbw.getChildAt(i);
                CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
                if (cb.isChecked()) {
                    int xh = (Integer) cb.getTag();
                    layListDbw.removeViewAt(i);
                    YangdiMgr.DelZb(ydh, xh);
                }
            }
        }
    }

    private void initQqData() {
        layQqList.removeAllViews();
        lstQq = Qianqimgr.GetQqZbList(ydh);
        if (lstQq != null && lstQq.size() > 0) {
            for (int i = 0; i < lstQq.size(); i++) {
                addQqRow(lstQq.get(i));
            }
        } else {
            layQqWu.setVisibility(1);
        }
    }

    private void addQqRow(Zb item) {
        if (item == null) return;
        LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.slqc_zb_item2, null);
        TextView tvZblx = (TextView) layRow.findViewById(R.id.tv_zblx);
        TextView tvMc = (TextView) layRow.findViewById(R.id.tv_mc);
        TextView tvPjgd = (TextView) layRow.findViewById(R.id.tv_pjg);
        TextView tvFgd = (TextView) layRow.findViewById(R.id.tv_gd);
        TextView tvZs = (TextView) layRow.findViewById(R.id.tv_zs);
        TextView tvDj = (TextView) layRow.findViewById(R.id.tv_dj);
        tvZblx.setText(Resmgr.GetValueByCode("yfzblx", item.zblx));
        tvMc.setText(String.valueOf(item.mc));
        tvPjgd.setText(String.valueOf(item.pjgd));
        tvFgd.setText(String.valueOf(item.fgd));
        if (item.zs >= 0) tvZs.setText(String.valueOf(item.zs));
        if (item.pjdj >= 0) tvDj.setText(String.valueOf(item.pjdj));
        layQqList.addView(layRow);
    }
}
