package com.tdgeos.tanhui;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.dlg.base.MyListDialog;
import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.dlg.yddc.WclzldDialog;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.yangdi.Qianqimgr;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.YangdiMgr;
import com.tdgeos.yangdi.Yddww;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SlqcWclzld extends Activity implements View.OnClickListener {
    private int ydh = 0;
    private int iStatus = 0;

    private Button btnClose = null;
    private Button btnQianqi = null;
    private Button btnSave = null;
    private Button btnFinish = null;

    private LayoutInflater layInflater = null;
    private LinearLayout layList = null;

    private Spinner spWclzldqk = null;
    private EditText etZlnd = null;
    private EditText etMl = null;
    private EditText etCzmd = null;
    private EditText etMmchl = null;

    private CheckBox cbGg = null;
    private CheckBox cbBz = null;
    private CheckBox cbSf = null;
    private CheckBox cbFy = null;
    private CheckBox cbGh = null;

    private Button btnAdd = null;
    private Button btnDel = null;

    private LinearLayout layQq = null;
    private LinearLayout layQqWu = null;
    private CheckBox cbAll = null;
    private Button btnCopy = null;
    private boolean bVisible = false;
    private EditText etQqZldqk = null;
    private EditText etQqZlnd = null;
    private EditText etQqMl = null;
    private EditText etQqCzmd = null;
    private EditText etQqChl = null;
    private EditText etQqGg = null;
    private EditText etQqBz = null;
    private EditText etQqSf = null;
    private EditText etQqFy = null;
    private EditText etQqGh = null;
    private EditText etQqSzzc = null;
    private EditText etQqSzbl = null;
    private CheckBox cbQqZldqk = null;
    private CheckBox cbQqZlnd = null;
    private CheckBox cbQqMl = null;
    private CheckBox cbQqCzmd = null;
    private CheckBox cbQqChl = null;
    private CheckBox cbQqGg = null;
    private CheckBox cbQqBz = null;
    private CheckBox cbQqSf = null;
    private CheckBox cbQqFy = null;
    private CheckBox cbQqGh = null;
    private CheckBox cbQqSzzc = null;
    private CheckBox cbQqSzbl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.slqc_wclzld);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = this.getIntent();
        ydh = intent.getIntExtra("ydh", 0);
        int[] ii = YangdiMgr.GetDczt(ydh);
        iStatus = ii[15];

        btnClose = (Button) findViewById(R.id.btn_close);
        btnQianqi = (Button) findViewById(R.id.btn_qq);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        btnClose.setOnClickListener(this);
        btnQianqi.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        spWclzldqk = (Spinner) findViewById(R.id.sp_wclzldqk);
        List<String> lst = Resmgr.GetValueList("wclzldqk");
        ArrayAdapter<String> ap = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lst);
        ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWclzldqk.setAdapter(ap);

        etZlnd = (EditText) findViewById(R.id.et_zlnd);
        etMl = (EditText) findViewById(R.id.et_ml);
        etCzmd = (EditText) findViewById(R.id.et_czmd);
        etMmchl = (EditText) findViewById(R.id.et_mmchl);

        etZlnd.setInputType(InputType.TYPE_CLASS_NUMBER);
        etMl.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etCzmd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etMmchl.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        cbGg = (CheckBox) findViewById(R.id.cb_gg);
        cbBz = (CheckBox) findViewById(R.id.cb_bz);
        cbSf = (CheckBox) findViewById(R.id.cb_sf);
        cbFy = (CheckBox) findViewById(R.id.cb_fy);
        cbGh = (CheckBox) findViewById(R.id.cb_gh);

        layInflater = LayoutInflater.from(this);
        layList = (LinearLayout) findViewById(R.id.lay_list);

        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDel = (Button) findViewById(R.id.btn_del);
        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);

        etZlnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                int f = -1;
                try {
                    f = Integer.parseInt(str);
                } catch (Exception e) {
                }
                if (f >= YangdiMgr.MIN_ZLND && f <= YangdiMgr.MAX_ZLND) {
                    etZlnd.setTextColor(Color.BLACK);
                } else {
                    etZlnd.setTextColor(Color.RED);
                }
            }
        });

        etMl.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                float f = -1;
                try {
                    f = Float.parseFloat(str);
                } catch (Exception e) {
                }
                if (f >= YangdiMgr.MIN_ML && f <= YangdiMgr.MAX_ML) {
                    etMl.setTextColor(Color.BLACK);
                } else {
                    etMl.setTextColor(Color.RED);
                }
            }
        });

        etCzmd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                float f = -1;
                try {
                    f = Float.parseFloat(str);
                } catch (Exception e) {
                }
                if (f >= YangdiMgr.MIN_CZMD && f <= YangdiMgr.MAX_CZMD) {
                    etCzmd.setTextColor(Color.BLACK);
                } else {
                    etCzmd.setTextColor(Color.RED);
                }
            }
        });

        etMmchl.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                float f = -1;
                try {
                    f = Float.parseFloat(str);
                } catch (Exception e) {
                }
                if (f >= 41 && f <= YangdiMgr.MAX_BFB) {
                    etMmchl.setTextColor(Color.BLACK);
                } else {
                    etMmchl.setTextColor(Color.RED);
                }
            }
        });

        layQq = (LinearLayout) findViewById(R.id.lay_qq);
        layQqWu = (LinearLayout) findViewById(R.id.lay_qq_wu);
        cbAll = (CheckBox) findViewById(R.id.cb_all);
        btnCopy = (Button) findViewById(R.id.btn_copy);
        btnCopy.setOnClickListener(this);

        etQqZldqk = (EditText) findViewById(R.id.et_qq_zldqk);
        etQqZlnd = (EditText) findViewById(R.id.et_qq_zlnd);
        etQqMl = (EditText) findViewById(R.id.et_qq_ml);
        etQqCzmd = (EditText) findViewById(R.id.et_qq_czmd);
        etQqChl = (EditText) findViewById(R.id.et_qq_mmchl);
        etQqGg = (EditText) findViewById(R.id.et_qq_gg);
        etQqBz = (EditText) findViewById(R.id.et_qq_bz);
        etQqSf = (EditText) findViewById(R.id.et_qq_sf);
        etQqFy = (EditText) findViewById(R.id.et_qq_fy);
        etQqGh = (EditText) findViewById(R.id.et_qq_gh);
        etQqSzzc = (EditText) findViewById(R.id.et_qq_szzc);
        etQqSzbl = (EditText) findViewById(R.id.et_qq_szbl);

        cbQqZldqk = (CheckBox) findViewById(R.id.cb_zldqk);
        cbQqZlnd = (CheckBox) findViewById(R.id.cb_zlnd);
        cbQqMl = (CheckBox) findViewById(R.id.cb_ml);
        cbQqCzmd = (CheckBox) findViewById(R.id.cb_czmd);
        cbQqChl = (CheckBox) findViewById(R.id.cb_mmchl);
        cbQqGg = (CheckBox) findViewById(R.id.cb_qq_gg);
        cbQqBz = (CheckBox) findViewById(R.id.cb_qq_bz);
        cbQqSf = (CheckBox) findViewById(R.id.cb_qq_sf);
        cbQqFy = (CheckBox) findViewById(R.id.cb_qq_fy);
        cbQqGh = (CheckBox) findViewById(R.id.cb_qq_gh);
        cbQqSzzc = (CheckBox) findViewById(R.id.cb_szzc);
        cbQqSzbl = (CheckBox) findViewById(R.id.cb_szbl);

        cbAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                cbQqZldqk.setChecked(arg1);
                cbQqZlnd.setChecked(arg1);
                cbQqMl.setChecked(arg1);
                cbQqCzmd.setChecked(arg1);
                cbQqChl.setChecked(arg1);
                cbQqGg.setChecked(arg1);
                cbQqBz.setChecked(arg1);
                cbQqSf.setChecked(arg1);
                cbQqFy.setChecked(arg1);
                cbQqGh.setChecked(arg1);
                cbQqSzzc.setChecked(arg1);
                cbQqSzbl.setChecked(arg1);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("是否需要保存数据？");
            builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (save()) {
                        if (iStatus != 2) iStatus = 1;
                    } else {
                        iStatus = 1;
                    }
                    String sql = "update dczt set wclzld = '" + iStatus + "' where ydh = '" + ydh + "'";
                    YangdiMgr.ExecSQL(ydh, sql);
                    SlqcWclzld.this.finish();
                }
            });
            builder.setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    SlqcWclzld.this.finish();
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

                if (cbQqZldqk.isChecked()) {
                    if (r == 0) {
                        spWclzldqk.setSelection(Resmgr.GetPosByValue("wclzldqk", etQqZldqk.getText().toString()));
                    } else if (r == 1 && spWclzldqk.getSelectedItemPosition() <= 0) {
                        spWclzldqk.setSelection(Resmgr.GetPosByValue("wclzldqk", etQqZldqk.getText().toString()));
                    }
                }
                if (cbQqZlnd.isChecked()) {
                    if (r == 0) {
                        etZlnd.setText(etQqZlnd.getText());
                    } else if (r == 1 && etZlnd.getText().toString().equals("")) {
                        etZlnd.setText(etQqZlnd.getText());
                    }
                }
                if (cbQqCzmd.isChecked()) {
                    if (r == 0) {
                        etCzmd.setText(etQqCzmd.getText());
                    } else if (r == 1 && etCzmd.getText().toString().equals("")) {
                        etCzmd.setText(etQqCzmd.getText());
                    }
                }
                if (cbQqMl.isChecked()) {
                    int ml = -1;
                    String sml = etQqMl.getText().toString();
                    try {
                        ml = Integer.parseInt(sml);
                    } catch (Exception e) {
                    }
                    if (ml > 0) ml += 5;
                    if (r == 0) {
                        if (ml > 0) etMl.setText(String.valueOf(ml));
                    } else if (r == 1 && etMl.getText().toString().equals("")) {
                        if (ml > 0) etMl.setText(String.valueOf(ml));
                    }
                }
                if (cbQqChl.isChecked()) {
                    if (r == 0) {
                        etMmchl.setText(etQqChl.getText());
                    } else if (r == 1 && etMmchl.getText().toString().equals("")) {
                        etMmchl.setText(etQqChl.getText());
                    }
                }
                if (cbQqGg.isChecked()) {
                    if (r == 0) {
                        cbGg.setChecked(etQqGg.getText().toString().equals("有"));
                    } else if (r == 1 && !cbGg.isChecked()) {
                        cbGg.setChecked(etQqGg.getText().toString().equals("有"));
                    }
                }
                if (cbQqBz.isChecked()) {
                    if (r == 0) {
                        cbBz.setChecked(etQqBz.getText().toString().equals("有"));
                    } else if (r == 1 && !cbBz.isChecked()) {
                        cbBz.setChecked(etQqBz.getText().toString().equals("有"));
                    }
                }
                if (cbQqSf.isChecked()) {
                    if (r == 0) {
                        cbSf.setChecked(etQqSf.getText().toString().equals("有"));
                    } else if (r == 1 && !cbSf.isChecked()) {
                        cbSf.setChecked(etQqSf.getText().toString().equals("有"));
                    }
                }
                if (cbQqFy.isChecked()) {
                    if (r == 0) {
                        cbFy.setChecked(etQqFy.getText().toString().equals("有"));
                    } else if (r == 1 && !cbFy.isChecked()) {
                        cbFy.setChecked(etQqFy.getText().toString().equals("有"));
                    }
                }
                if (cbQqGh.isChecked()) {
                    if (r == 0) {
                        cbGh.setChecked(etQqGh.getText().toString().equals("有"));
                    } else if (r == 1 && !cbGh.isChecked()) {
                        cbGh.setChecked(etQqGh.getText().toString().equals("有"));
                    }
                }
                if (cbQqSzzc.isChecked() || cbQqSzbl.isChecked()) {
                    if (r == 0) {
                        layList.removeAllViews();
                        String szzc = etQqSzzc.getText().toString();
                        String szbl = etQqSzbl.getText().toString();
                        String[] szs = MyFuns.Split(szzc, ',');
                        String[] bls = MyFuns.Split(szbl, ',');
                        for (int i = 0; i < szs.length; i++) {
                            int i_bl = -1;
                            try {
                                i_bl = Integer.parseInt(bls[i]);
                            } catch (Exception e) {
                            }
                            addRow(szs[i], i_bl);
                        }
                    } else if (r == 1) {
                        String szzc = etQqSzzc.getText().toString();
                        String szbl = etQqSzbl.getText().toString();
                        String[] szs = MyFuns.Split(szzc, ',');
                        String[] bls = MyFuns.Split(szbl, ',');
                        for (int i = 0; i < szs.length; i++) {
                            int i_bl = -1;
                            try {
                                i_bl = Integer.parseInt(bls[i]);
                            } catch (Exception e) {
                            }
                            addRow(szs[i], i_bl);
                        }
                    }
                }
                cbAll.setChecked(false);
                cbQqZldqk.setChecked(false);
                cbQqZlnd.setChecked(false);
                cbQqCzmd.setChecked(false);
                cbQqMl.setChecked(false);
                cbQqChl.setChecked(false);
                cbQqGg.setChecked(false);
                cbQqBz.setChecked(false);
                cbQqSf.setChecked(false);
                cbQqFy.setChecked(false);
                cbQqGh.setChecked(false);
                cbQqSzzc.setChecked(false);
                cbQqSzbl.setChecked(false);
                save();
                break;
            }
            case R.id.btn_save: {
                if (save()) {
                    if (iStatus != 2) iStatus = 1;
                } else {
                    iStatus = 1;
                }
                String sql = "update dczt set wclzld = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                break;
            }
            case R.id.btn_finish: {
                if (save()) {
                    String sql = "update dczt set wclzld = '2' where ydh = '" + ydh + "'";
                    YangdiMgr.ExecSQL(ydh, sql);
                    this.finish();
                } else {
                    String sql = "update dczt set wclzld = '1' where ydh = '" + ydh + "'";
                    YangdiMgr.ExecSQL(ydh, sql);
                }
                break;
            }
            case R.id.btn_add: {
                WclzldDialog dlg = new WclzldDialog(this, "", -1);
                String[] ss = dlg.showDialog();
                if (ss != null) {
                    int i_bl = -1;
                    try {
                        i_bl = Integer.parseInt(ss[1]);
                    } catch (Exception e) {
                    }

                    addRow(ss[0], i_bl);
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
                    TextView tvSz = (TextView) layRow.findViewById(R.id.tv_sz);
                    TextView tvBl = (TextView) layRow.findViewById(R.id.tv_bl);
                    String sz = tvSz.getText().toString();
                    String bl = tvBl.getText().toString();
                    int i_bl = -1;
                    try {
                        i_bl = Integer.parseInt(bl);
                    } catch (Exception e) {
                    }
                    WclzldDialog dlg = new WclzldDialog(this, sz, i_bl);
                    String[] ss = dlg.showDialog();
                    if (ss != null) {
                        tvSz.setText(ss[0]);
                        tvBl.setText(ss[1]);
                    }
                }
                break;
            }
        }
    }

    private void initData() {
        spWclzldqk.setSelection(0);
        etZlnd.setText("");
        etMl.setText("");
        etCzmd.setText("");
        etMmchl.setText("");
        cbGg.setChecked(false);
        cbBz.setChecked(false);
        cbSf.setChecked(false);
        cbFy.setChecked(false);
        cbGh.setChecked(false);

        String sql = "select * from wclzld where ydh = '" + ydh + "'";
        String[][] sss = YangdiMgr.SelectData(ydh, sql);
        if (sss != null) {
            try {
                int zldqk = -1;
                zldqk = Integer.parseInt(sss[0][1]);
                spWclzldqk.setSelection(Resmgr.GetPosByCode("wclzldqk", zldqk));
            } catch (Exception e) {
            }
            try {
                int zlnd = -1;
                zlnd = Integer.parseInt(sss[0][2]);
                if (zlnd > 0) etZlnd.setText(String.valueOf(zlnd));
            } catch (Exception e) {
            }
            try {
                int ml = -1;
                ml = Integer.parseInt(sss[0][3]);
                if (ml > 0) etMl.setText(String.valueOf(ml));
            } catch (Exception e) {
            }
            try {
                int czmd = -1;
                czmd = Integer.parseInt(sss[0][4]);
                if (czmd > 0) etCzmd.setText(String.valueOf(czmd));
            } catch (Exception e) {
            }
            try {
                int chl = -1;
                chl = Integer.parseInt(sss[0][5]);
                if (chl > 0) etMmchl.setText(String.valueOf(chl));
            } catch (Exception e) {
            }

            try {
                int gg = -1;
                gg = Integer.parseInt(sss[0][6]);
                cbGg.setChecked(gg == 1);
            } catch (Exception e) {
            }
            try {
                int bz = -1;
                bz = Integer.parseInt(sss[0][7]);
                cbBz.setChecked(bz == 1);
            } catch (Exception e) {
            }
            try {
                int sf = -1;
                sf = Integer.parseInt(sss[0][8]);
                cbSf.setChecked(sf == 1);
            } catch (Exception e) {
            }
            try {
                int fy = -1;
                fy = Integer.parseInt(sss[0][9]);
                cbFy.setChecked(fy == 1);
            } catch (Exception e) {
            }
            try {
                int gh = -1;
                gh = Integer.parseInt(sss[0][10]);
                cbGh.setChecked(gh == 1);
            } catch (Exception e) {
            }

            layList.removeAllViews();
            String[] sz = MyFuns.Split(sss[0][11], ',');
            String[] bl = MyFuns.Split(sss[0][12], ',');
            int n = sz.length < bl.length ? sz.length : bl.length;
            for (int i = 0; i < n; i++) {
                LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.slqc_wclzld_item, null);
                layRow.setOnClickListener(this);
                TextView tvSz = (TextView) layRow.findViewById(R.id.tv_sz);
                TextView tvBl = (TextView) layRow.findViewById(R.id.tv_bl);
                tvSz.setText(sz[i]);
                tvBl.setText(bl[i]);
                layList.addView(layRow);
            }
        }
    }

    private void addRow(String sz, int bl) {
        LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.slqc_wclzld_item, null);
        layRow.setOnClickListener(this);
        TextView tvSz = (TextView) layRow.findViewById(R.id.tv_sz);
        TextView tvBl = (TextView) layRow.findViewById(R.id.tv_bl);
        tvSz.setText(sz);
        tvBl.setText(String.valueOf(bl));
        layList.addView(layRow);
    }

    private void delRow() {
        int n = layList.getChildCount();
        for (int i = n - 1; i >= 0; i--) {
            LinearLayout layRow = (LinearLayout) layList.getChildAt(i);
            CheckBox cb = (CheckBox) layRow.getChildAt(0);
            if (cb.isChecked()) {
                layList.removeViewAt(i);
            }
        }
    }

    private void initQqData() {
        String[][] sss = Qianqimgr.GetQqWclzld(ydh);
        if (sss != null) {
            //etQqZldqk.setText(Resmgr.GetValueByCode("wclzldqk", code));
            etQqZlnd.setText(sss[0][1]);
            etQqMl.setText(sss[0][2]);
            etQqCzmd.setText(sss[0][3]);
            etQqChl.setText(sss[0][4]);
            etQqGg.setText(sss[0][5].equals("1") ? "有" : "无");
            etQqBz.setText(sss[0][6].equals("1") ? "有" : "无");
            etQqSf.setText(sss[0][7].equals("1") ? "有" : "无");
            etQqFy.setText(sss[0][8].equals("1") ? "有" : "无");
            etQqGh.setText(sss[0][9].equals("1") ? "有" : "无");
            String szzc = sss[0][10];
            String szbl = sss[0][11];
            if (sss.length > 1) {
                for (int i = 1; i < sss.length; i++) {
                    szzc += "," + sss[i][10];
                    szbl += "," + sss[i][11];
                }
            }
            etQqSzzc.setText(szzc);
            etQqSzbl.setText(szbl);
        } else {
            layQqWu.setVisibility(1);
        }
    }

    private boolean save() {
        String wclzldqk = spWclzldqk.getSelectedItem().toString();
        String zlnd = etZlnd.getText().toString();
        String ml = etMl.getText().toString();
        String czmd = etCzmd.getText().toString();
        String mmchl = etMmchl.getText().toString();

        int i_wclzldqk = 0;
        try {
            i_wclzldqk = Resmgr.GetCodeByValue("wclzldqk", wclzldqk);
        } catch (NumberFormatException nfe) {
        }
        int i_zlnd = 0;
        try {
            i_zlnd = Integer.parseInt(zlnd);
        } catch (NumberFormatException nfe) {
        }
        int i_ml = 0;
        try {
            i_ml = Integer.parseInt(ml);
        } catch (NumberFormatException nfe) {
        }
        int i_czmd = 0;
        try {
            i_czmd = Integer.parseInt(czmd);
        } catch (NumberFormatException nfe) {
        }
        int i_mmchl = 0;
        try {
            i_mmchl = Integer.parseInt(mmchl);
        } catch (NumberFormatException nfe) {
        }

        int gg = cbGg.isChecked() ? 1 : 2;
        int bz = cbBz.isChecked() ? 1 : 2;
        int sf = cbSf.isChecked() ? 1 : 2;
        int fy = cbFy.isChecked() ? 1 : 2;
        int gh = cbGh.isChecked() ? 1 : 2;

        if (i_wclzldqk == 0) {
            Toast.makeText(this, "未成林造林地情况不能为空！", 1).show();
            return false;
        }
        if (zlnd.equals("")) {
            Toast.makeText(this, "造林年度不能为空！", 1).show();
            return false;
        }
        if (i_zlnd < YangdiMgr.MIN_ZLND || i_zlnd > YangdiMgr.MAX_ZLND) {
            Toast.makeText(this, "造林年度超限！", 1).show();
            return false;
        }
        if (ml.equals("")) {
            Toast.makeText(this, "苗龄不能为空！", 1).show();
            return false;
        }
        if (i_ml < YangdiMgr.MIN_ML || i_ml > YangdiMgr.MAX_ML) {
            Toast.makeText(this, "苗龄超限！", 1).show();
            return false;
        }
        if (czmd.equals("")) {
            Toast.makeText(this, "初植密度不能为空！", 1).show();
            return false;
        }
        if (i_czmd < YangdiMgr.MIN_CZMD || i_czmd > YangdiMgr.MAX_CZMD) {
            Toast.makeText(this, "初植密度超限！", 1).show();
            return false;
        }
        if (mmchl.equals("")) {
            Toast.makeText(this, "苗木成活率不能为空！", 1).show();
            return false;
        }
        if (i_mmchl < 41 || i_mmchl > YangdiMgr.MAX_BFB) {
            Toast.makeText(this, "苗木成活率超限！", 1).show();
            return false;
        }
        if (i_mmchl < 70 && i_wclzldqk == 1) {
            Toast.makeText(this, "苗木成活率与未成林造林地类型不一致！", 1).show();
            return false;
        }
        if (i_mmchl < 41 || i_mmchl >= 85 && i_wclzldqk == 2) {
            Toast.makeText(this, "苗木成活率与未成林造林地类型不一致！", 1).show();
            return false;
        }


        int n = layList.getChildCount();
        if (n == 0) {
            Toast.makeText(this, "树种组成不能为空！", 1).show();
            return false;
        }

        String szs = "";
        String bls = "";
        float z_bl = 0;
        for (int i = 0; i < n; i++) {
            LinearLayout layRow = (LinearLayout) layList.getChildAt(i);
            TextView tvSz = (TextView) layRow.findViewById(R.id.tv_sz);
            TextView tvBl = (TextView) layRow.findViewById(R.id.tv_bl);
            String sz = tvSz.getText().toString();
            String bl = tvBl.getText().toString();
            int i_bl = -1;
            try {
                i_bl = Integer.parseInt(bl);
            } catch (NumberFormatException nfe) {
            }

            if (i_bl > 0) {
                z_bl += i_bl;
            }

            if (i < n - 1) {
                szs += sz + ",";
                bls += i_bl + ",";
            } else {
                szs += sz;
                bls += i_bl;
            }
        }

        if (z_bl > 10.001f || z_bl < 9.999f) {
            Toast.makeText(this, "树种比例总和应为10，请修正后再次保存！", 1).show();
            return false;
        }

        String sql = "select * from wclzld where ydh = '" + ydh + "'";
        if (YangdiMgr.QueryExists(ydh, sql)) {
            sql = "update wclzld set "
                    + "wclzldqk = '" + i_wclzldqk + "', "
                    + "zlnd = '" + i_zlnd + "', "
                    + "ml = '" + i_ml + "', "
                    + "czmd = '" + i_czmd + "', "
                    + "mmchl = '" + i_mmchl + "', "
                    + "gg = '" + gg + "', "
                    + "bz = '" + bz + "', "
                    + "sf = '" + sf + "', "
                    + "fy = '" + fy + "', "
                    + "gh = '" + gh + "', "
                    + "sz = '" + szs + "', "
                    + "szbl = '" + bls + "' "
                    + " where ydh = '" + ydh + "'";
            YangdiMgr.ExecSQL(ydh, sql);
        } else {
            sql = "insert into wclzld(ydh, wclzldqk, zlnd, ml, czmd, mmchl, gg, bz, sf, fy, gh, sz, szbl) values("
                    + "'" + ydh + "', "
                    + "'" + i_wclzldqk + "', "
                    + "'" + i_zlnd + "', "
                    + "'" + i_ml + "', "
                    + "'" + i_czmd + "', "
                    + "'" + i_mmchl + "', "
                    + "'" + gg + "', "
                    + "'" + bz + "', "
                    + "'" + sf + "', "
                    + "'" + fy + "', "
                    + "'" + gh + "', "
                    + "'" + szs + "', "
                    + "'" + bls + "')";
            YangdiMgr.ExecSQL(ydh, sql);
        }
        return true;
    }
}
