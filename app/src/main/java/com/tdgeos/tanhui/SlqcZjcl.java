package com.tdgeos.tanhui;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.dlg.base.MyListDialog;
import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.dlg.yddc.ZjfcDialog;
import com.tdgeos.dlg.yddc.ZjxcDialog;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.yangdi.Cljl;
import com.tdgeos.yangdi.Qianqimgr;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SlqcZjcl extends Activity implements View.OnClickListener {
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

    private int type = 0;//0表示复测，1表示新测
    private RadioGroup rgType = null;
    private EditText etJdbhc = null;
    private EditText etXdbhc = null;
    private EditText etZcwc = null;
    private LinearLayout layXdbhc = null;
    private LinearLayout layZcwc = null;

    private LinearLayout layQq = null;
    private LinearLayout layQqList = null;
    private LinearLayout layQqWu = null;
    private CheckBox cbAll = null;
    private Button btnCopy = null;
    private boolean bVisible = false;
    private List<Cljl> lstQq = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.slqc_zjcl);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = this.getIntent();
        ydh = intent.getIntExtra("ydh", 0);
        //type = intent.getIntExtra("type", 0);
        int[] ii = YangdiMgr.GetDczt(ydh);
        iStatus = ii[4];

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

        layXdbhc = (LinearLayout) findViewById(R.id.lay_xdbhc);
        layZcwc = (LinearLayout) findViewById(R.id.lay_xdzcwc);
        etJdbhc = (EditText) findViewById(R.id.et_juedui);
        etXdbhc = (EditText) findViewById(R.id.et_xiangdui);
        etZcwc = (EditText) findViewById(R.id.et_zhouchang);
        if (type == 0) {
            layXdbhc.setVisibility(8);
        } else {
            layZcwc.setVisibility(8);
        }

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

        rgType = (RadioGroup) findViewById(R.id.rg_type);
        rgType.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup v, int id) {
                if (id == R.id.rb_fuce) {
                    layXdbhc.setVisibility(8);
                    layZcwc.setVisibility(1);
                    type = 0;
                } else {
                    layXdbhc.setVisibility(1);
                    layZcwc.setVisibility(8);
                    type = 1;
                }
                YangdiMgr.SetZjclType(ydh, type);
            }
        });

        int type = YangdiMgr.GetZjclType(ydh);
        rgType.check(type == 1 ? R.id.rb_ceshe : R.id.rb_fuce);

        initData();
        initQqData();

        String sql = "select jdbhc, xdbhc, zcwc from qt where ydh = '" + ydh + "'";
        String[][] sss = YangdiMgr.SelectData(ydh, sql);
        if (sss != null) {
            etJdbhc.setText(sss[0][0]);
            etXdbhc.setText(sss[0][1]);
            etZcwc.setText(sss[0][2]);
        }

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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("是否需要保存数据？");
            builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    boolean b = save();
                    if (iStatus != 2) {
                        iStatus = 1;
                    } else if (!b) {
                        iStatus = 1;
                    }
                    String sql = "update dczt set zjcl = '" + iStatus + "' where ydh = '" + ydh + "'";
                    YangdiMgr.ExecSQL(ydh, sql);
                    SlqcZjcl.this.finish();
                }
            });
            builder.setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    SlqcZjcl.this.finish();
                }
            });
            builder.show();
            return true;
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
                        Cljl item = lstQq.get(i);
                        boolean b = YangdiMgr.IsHaveZjcl(ydh, item);
                        if (b) continue;
                        YangdiMgr.AddZjcl(ydh, item);
                    }
                }
                initData();
                resetLj();
                cbAll.setChecked(false);
                break;
            }
            case R.id.btn_save: {
                boolean b = save();
                if (iStatus != 2) {
                    iStatus = 1;
                } else if (!b) {
                    iStatus = 1;
                }
                String sql = "update dczt set zjcl = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                break;
            }
            case R.id.btn_finish: {
                boolean b = save();
                if (b) {
                    iStatus = 2;
                } else {
                    iStatus = 1;
                }
                String sql = "update dczt set zjcl = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                if (b) finish();
                break;
            }
            case R.id.btn_add: {
                Cljl cljl = null;
                if (type == 0) {
                    ZjfcDialog dlg = new ZjfcDialog(this, null, ydh);
                    cljl = dlg.showDialog();
                } else {
                    ZjxcDialog dlg = new ZjxcDialog(this, null, ydh);
                    cljl = dlg.showDialog();
                }
                if (cljl != null) {
                    YangdiMgr.AddZjcl(ydh, cljl);
                    initData();
                    save();
                }
                break;
            }
            case R.id.btn_del: {
                MyMakeSureDialog dlg = new MyMakeSureDialog(this, "删除", "删除后将不可恢复，是否继续删除？", "删除", "取消");
                if (dlg.showDialog()) {
                    delRow();
                    save();
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
                    Cljl cljl = YangdiMgr.GetZjcl(ydh, xh);
                    Cljl item = null;
                    if (type == 0) {
                        ZjfcDialog dlg = new ZjfcDialog(this, cljl, ydh);
                        item = dlg.showDialog();
                    } else {
                        ZjxcDialog dlg = new ZjxcDialog(this, cljl, ydh);
                        item = dlg.showDialog();
                    }
                    if (item != null) {
                        YangdiMgr.UpdateZjcl(ydh, item);
                        TextView tvCz = (TextView) layRow.findViewById(R.id.tv_cz);
                        TextView tvFwj = (TextView) layRow.findViewById(R.id.tv_fwj);
                        TextView tvQxj = (TextView) layRow.findViewById(R.id.tv_qxj);
                        TextView tvXj = (TextView) layRow.findViewById(R.id.tv_xj);
                        TextView tvSpj = (TextView) layRow.findViewById(R.id.tv_spj);
                        tvCz.setText(item.cz);
                        tvFwj.setText(String.valueOf(item.fwj));
                        tvQxj.setText(String.valueOf(item.qxj));
                        tvXj.setText(String.valueOf(item.xj));
                        tvSpj.setText(String.valueOf(item.spj));
                        save();
                    }
                }
                break;
            }
        }
    }

    private void initData() {
        layList.removeAllViews();
        List<Cljl> lst = YangdiMgr.GetZjclList(ydh);
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                addRow(lst.get(i));
            }
        }
    }

    private void addRow(Cljl item) {
        if (item == null) return;
        LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.slqc_yxcl_item, null);
        layRow.setTag(item.xh);
        layRow.setOnClickListener(this);
        CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
        cb.setTag(item.xh);
        TextView tvCz = (TextView) layRow.findViewById(R.id.tv_cz);
        TextView tvFwj = (TextView) layRow.findViewById(R.id.tv_fwj);
        TextView tvQxj = (TextView) layRow.findViewById(R.id.tv_qxj);
        TextView tvXj = (TextView) layRow.findViewById(R.id.tv_xj);
        TextView tvSpj = (TextView) layRow.findViewById(R.id.tv_spj);
        TextView tvLj = (TextView) layRow.findViewById(R.id.tv_lj);
        tvCz.setText(item.cz);
        tvFwj.setText(String.valueOf(item.fwj));
        tvQxj.setText(String.valueOf(item.qxj));
        tvXj.setText(String.valueOf(item.xj));
        tvSpj.setText(String.valueOf(item.spj));
        tvLj.setText(String.valueOf(item.lj));
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
                YangdiMgr.DelZjcl(ydh, xh);
            }
        }
        resetLj();
        n = layList.getChildCount();
        if (n == 0) {
            etZcwc.setText("");
            etXdbhc.setText("");
            etJdbhc.setText("");
        }
    }

    private void resetLj() {
        List<Cljl> lst = YangdiMgr.GetZjclList(ydh);
        int n = lst.size();
        if (n == 0) return;

        float lj = lst.get(0).spj;
        lst.get(0).lj = lj;
        YangdiMgr.UpdateZjcl(ydh, lst.get(0));
        LinearLayout lay = (LinearLayout) layList.getChildAt(0);
        TextView tvLj = (TextView) lay.findViewById(R.id.tv_lj);
        tvLj.setText(String.valueOf(lj));

        for (int i = 1; i < n; i++) {
            lj += lst.get(i).spj;
            lj = MyFuns.MyDecimal(lj, 2);
            lst.get(i).lj = lj;
            YangdiMgr.UpdateZjcl(ydh, lst.get(i));
            lay = (LinearLayout) layList.getChildAt(i);
            tvLj = (TextView) lay.findViewById(R.id.tv_lj);
            tvLj.setText(String.valueOf(lj));
        }

        String str = etZcwc.getText().toString();
        int z = 0;
        try {
            z = Integer.parseInt(str);
        } catch (Exception e) {
        }
        if (type == 0) {
            int i_zcwc = (int) ((lj - YangdiMgr.YD_SIZE * 4) * 100);
            i_zcwc = i_zcwc < 0 ? -i_zcwc : i_zcwc;
            etZcwc.setText(String.valueOf(i_zcwc));
            if (i_zcwc > YangdiMgr.MAX_ZJCL_ZCWC) etZcwc.setTextColor(Color.RED);
            else etZcwc.setTextColor(Color.BLACK);
        }
    }

    private boolean save() {
        int n = layList.getChildCount();
        int dl = YangdiMgr.GetBqdl(ydh);
        if (n == 0) {
            if (dl > 200 && dl < 255) {
                return true;
            } else {
                Toast.makeText(this, "记录数量错误，应至少4条记录！", 1).show();
                return false;
            }
        }
        if (n < 4) {
            Toast.makeText(this, "记录数量错误，应至少4条记录！", 1).show();
            return false;
        }

        resetLj();

        String jdbhc = etJdbhc.getText().toString();
        String xdbhc = etXdbhc.getText().toString();
        String zcwc = etZcwc.getText().toString();
        float f_jdbhc = 0;
        try {
            f_jdbhc = Float.parseFloat(jdbhc);
        } catch (NumberFormatException nfe) {
        }
        float f_xdbhc = 0;
        try {
            f_xdbhc = Float.parseFloat(xdbhc);
        } catch (NumberFormatException nfe) {
        }
        float f_zcwc = 0;
        try {
            f_zcwc = Float.parseFloat(zcwc);
        } catch (NumberFormatException nfe) {
        }

        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (YangdiMgr.QueryExists(ydh, sql)) {
            sql = "update qt set jdbhc = '" + f_jdbhc + "', xdbhc = '" + f_xdbhc + "', zcwc = '" + f_zcwc + "'  where ydh = '" + ydh + "'";
        } else {
            sql = "insert into qt(ydh, jdbhc, xdbhc, zcwc) values('" + ydh + "', '" + f_jdbhc + "', '" + f_xdbhc + "', '" + f_zcwc + "')";
        }
        YangdiMgr.ExecSQL(ydh, sql);

        if (type == 0) {
            if (f_zcwc > YangdiMgr.MAX_ZJCL_ZCWC || f_zcwc < YangdiMgr.MIN_ZJCL_ZCWC) {
                Toast.makeText(this, "周长误差超限！", 1).show();
                return false;
            }
        } else {
            if (f_xdbhc > YangdiMgr.MAX_ZJCL_BHC || f_xdbhc < YangdiMgr.MIN_ZJCL_BHC) {
                Toast.makeText(this, "闭合差超限！", 1).show();
                return false;
            }
        }

        return true;
    }

    private void initQqData() {
        layQqList.removeAllViews();
        lstQq = Qianqimgr.GetQqZjclList(ydh);
        if (lstQq != null && lstQq.size() > 0) {
            for (int i = 0; i < lstQq.size(); i++) {
                addQqRow(lstQq.get(i));
            }
        } else {
            layQqWu.setVisibility(1);
        }
    }

    private void addQqRow(Cljl item) {
        if (item == null) return;
        LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.slqc_yxcl_item, null);
        TextView tvCz = (TextView) layRow.findViewById(R.id.tv_cz);
        TextView tvFwj = (TextView) layRow.findViewById(R.id.tv_fwj);
        TextView tvQxj = (TextView) layRow.findViewById(R.id.tv_qxj);
        TextView tvXj = (TextView) layRow.findViewById(R.id.tv_xj);
        TextView tvSpj = (TextView) layRow.findViewById(R.id.tv_spj);
        TextView tvLj = (TextView) layRow.findViewById(R.id.tv_lj);
        tvCz.setText(item.cz);
        tvFwj.setText(String.valueOf(item.fwj));
        tvQxj.setText(String.valueOf(item.qxj));
        tvXj.setText(String.valueOf(item.xj));
        tvSpj.setText(String.valueOf(item.spj));
        tvLj.setText(String.valueOf(item.lj));
        layQqList.addView(layRow);
    }
}
