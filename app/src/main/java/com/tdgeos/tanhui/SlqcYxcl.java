package com.tdgeos.tanhui;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.dlg.base.MyListDialog;
import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.dlg.yddc.YxclDialog;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.yangdi.Cljl;
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
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlqcYxcl extends Activity implements View.OnClickListener {
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
    private List<Cljl> lstQq = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.slqc_yxcl);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = this.getIntent();
        ydh = intent.getIntExtra("ydh", 0);
        int[] ii = YangdiMgr.GetDczt(ydh);
        iStatus = ii[3];

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
                        Cljl item = lstQq.get(i);
                        boolean b = YangdiMgr.IsHaveYxcl(ydh, item);
                        if (b) continue;
                        YangdiMgr.AddYxcl(ydh, item);
                    }
                }
                initData();
                resetLj();
                cbAll.setChecked(false);
                break;
            }
            case R.id.btn_save: {
                if (iStatus != 2) {
                    iStatus = 1;
                }
                String sql = "update dczt set yxcl = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
            /*
            float fwj = YangdiMgr.GetYxclFwj(ydh);
			List<Cljl> lst = YangdiMgr.GetYxclList(ydh);
			boolean b = false;
			for(int i=0;i<lst.size();i++)
			{
				if((int)fwj != (int)lst.get(i).fwj)
				{
					b = true;
					break;
				}
			}
			if(b)
			{
				Toast.makeText(this, "引线测量方位角不一致！", 1).show();
			}
			*/
                break;
            }
            case R.id.btn_finish: {
            /*
			float fwj = YangdiMgr.GetYxclFwj(ydh);
			List<Cljl> lst = YangdiMgr.GetYxclList(ydh);
			boolean b = false;
			for(int i=0;i<lst.size();i++)
			{
				if((int)fwj != (int)lst.get(i).fwj)
				{
					b = true;
					break;
				}
			}
			if(b)
			{
				Toast.makeText(this, "引线测量方位角不一致！", 1).show();
				break;
			}
			*/
                String sql = "update dczt set yxcl = '2' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                finish();
                break;
            }
            case R.id.btn_add: {
                YxclDialog dlg = new YxclDialog(this, null, ydh);
                Cljl cljl = dlg.showDialog();
                if (cljl != null) {
                    YangdiMgr.AddYxcl(ydh, cljl);
                    initData();
                    resetLj();
                }
                break;
            }
            case R.id.btn_del: {
                MyMakeSureDialog dlg = new MyMakeSureDialog(this, "删除", "删除后将不可恢复，是否继续删除？", "删除", "取消");
                if (dlg.showDialog()) {
                    delRow();
                    resetLj();
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
                    Cljl cljl = YangdiMgr.GetYxcl(ydh, xh);
                    YxclDialog dlg = new YxclDialog(this, cljl, ydh);
                    Cljl item = dlg.showDialog();
                    if (item != null) {
                        YangdiMgr.UpdateYxcl(ydh, item);
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
                        resetLj();
                    }
                }
                break;
            }
        }
    }

    private void initData() {
        layList.removeAllViews();
        List<Cljl> lst = YangdiMgr.GetYxclList(ydh);
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
                YangdiMgr.DelYxcl(ydh, xh);
            }
        }
    }

    private void resetLj() {
        List<Cljl> lst = YangdiMgr.GetYxclList(ydh);
        int n = lst.size();
        if (n == 0) return;

        float lj = lst.get(0).spj;
        lst.get(0).lj = lj;
        YangdiMgr.UpdateYxcl(ydh, lst.get(0));
        LinearLayout lay = (LinearLayout) layList.getChildAt(0);
        TextView tvLj = (TextView) lay.findViewById(R.id.tv_lj);
        tvLj.setText(String.valueOf(lj));

        for (int i = 1; i < n; i++) {
            lj += lst.get(i).spj;
            lj = MyFuns.MyDecimal(lj, 2);
            lst.get(i).lj = lj;
            YangdiMgr.UpdateYxcl(ydh, lst.get(i));
            lay = (LinearLayout) layList.getChildAt(i);
            tvLj = (TextView) lay.findViewById(R.id.tv_lj);
            tvLj.setText(String.valueOf(lj));
        }
    }

    private void initQqData() {
        layQqList.removeAllViews();
        lstQq = Qianqimgr.GetQqYxclList(ydh);
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
