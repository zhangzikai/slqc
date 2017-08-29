package com.tdgeos.tanhui;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.dlg.base.MyListDialog;
import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.dlg.yddc.CheckYdyzDialog;
import com.tdgeos.dlg.yddc.PdbzDialog;
import com.tdgeos.dlg.yddc.YsszDialog;
import com.tdgeos.lib.CoordTransform;
import com.tdgeos.lib.MyCoord;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.yangdi.Qianqimgr;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Setmgr;
import com.tdgeos.yangdi.Slzh;
import com.tdgeos.yangdi.Trgx;
import com.tdgeos.yangdi.YDInfo;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SlqcYdyz extends Activity implements View.OnClickListener {
    private int ydh = 0;
    private int iStatus = 0;

    private Button btnClose = null;

    private Button btnYdloc = null;
    private Button btnResetloc = null;
    private Button btnLingz = null;
    private Button btnSlqljg = null;
    private Button btnYssz = null;
    private Button btnCopy = null;
    private Button btnCheck = null;
    private Button btnSave = null;
    private Button btnFinish = null;
    private Button btnYsszReset = null;
    private Button btnPjxjReset = null;

    private EditText etYdh = null;
    private Spinner spYdlb = null;
    private EditText etZzb = null;
    private EditText etHzb = null;
    private EditText etGpszzb = null;
    private EditText etGpshzb = null;
    private EditText etXian = null;
    private Spinner spDm = null;
    private EditText etHb = null;
    private Spinner spPx = null;
    private Spinner spPw = null;
    private EditText etPd = null;
    private Spinner spDbxt = null;
    private EditText etSqgd = null;
    private EditText etFshd = null;
    private EditText etQsgmjbl = null;
    private EditText etJyll = null;
    private Spinner spTrmc = null;
    private Spinner spTrzd = null;
    private EditText etTrlshl = null;
    private EditText etTchd = null;
    private EditText etFzchd = null;
    private EditText etKzlyhd = null;
    private Spinner spZblx = null;
    private EditText etGmfgd = null;
    private EditText etGmgd = null;
    private EditText etCbfgd = null;
    private EditText etCbgd = null;
    private EditText etZbzfgd = null;
    private Spinner spDl = null;
    private Spinner spTdqs = null;
    private Spinner spLmqs = null;
    private Spinner spSllb = null;
    private Spinner spGylsqdj = null;
    private Spinner spGylbhdj = null;
    private Spinner spSpljydj = null;
    private Spinner spFycs = null;
    private Spinner spLinzh = null;
    private Spinner spQy = null;
    private EditText etYssz = null;
    private EditText etPjnl = null;
    private Spinner spLingz = null;
    private Spinner spCq = null;
    private EditText etPjxj = null;
    private EditText etPjsg = null;
    private EditText etYbd = null;
    private Spinner spSlqljg = null;
    private Spinner spLcjg = null;
    private Spinner spSzjg = null;
    private Spinner spZrd = null;
    private Spinner spKjd = null;
    private Spinner spSlzhlx = null;
    private Spinner spSlzhdj = null;
    private Spinner spSljkdj = null;
    private EditText etSpszs = null;
    private EditText etZzzs = null;
    private Spinner spTrgxdj = null;
    private Spinner spDlmjdj = null;
    private Spinner spDlbhyy = null;
    private Spinner spYwtsdd = null;
    private EditText etDcrq = null;
    //private Spinner spF1 = null;
    //private Spinner spF2 = null;

    private TextView tvDm = null;
    private TextView tvHb = null;
    private TextView tvPx = null;
    private TextView tvPw = null;
    private TextView tvPd = null;
    private TextView tvDbxt = null;
    private TextView tvSqgd = null;
    private TextView tvFshd = null;
    private TextView tvQsgmjbl = null;
    private TextView tvJyll = null;
    private TextView tvTrmc = null;
    private TextView tvTrzd = null;
    private TextView tvTrlshl = null;
    private TextView tvTchd = null;
    private TextView tvFzchd = null;
    private TextView tvKzlyhd = null;
    private TextView tvZblx = null;
    private TextView tvGmfgd = null;
    private TextView tvGmgd = null;
    private TextView tvCbfgd = null;
    private TextView tvCbgd = null;
    private TextView tvZbzfgd = null;
    private TextView tvDl = null;
    private TextView tvTdqs = null;
    private TextView tvLmqs = null;
    private TextView tvSllb = null;
    private TextView tvGylsqdj = null;
    private TextView tvGylbhdj = null;
    private TextView tvSpljydj = null;
    private TextView tvFycs = null;
    private TextView tvLinzh = null;
    private TextView tvQy = null;
    private TextView tvYssz = null;
    private TextView tvPjnl = null;
    private TextView tvLingz = null;
    private TextView tvCq = null;
    private TextView tvPjxj = null;
    private TextView tvPjsg = null;
    private TextView tvYbd = null;
    private TextView tvSlqljg = null;
    private TextView tvLcjg = null;
    private TextView tvSzjg = null;
    private TextView tvZrd = null;
    private TextView tvKjd = null;
    private TextView tvSlzhlx = null;
    private TextView tvSlzhdj = null;
    private TextView tvSljkdj = null;
    private TextView tvSpszs = null;
    private TextView tvZzzs = null;
    private TextView tvTrgxdj = null;
    private TextView tvDlmjdj = null;
    private TextView tvDlbhyy = null;
    private TextView tvYwtsdd = null;
    private TextView tvDcrq = null;
    //private TextView tvF1 = null;
    //private TextView tvF2 = null;

    private TextView qqYdh = null;
    private TextView qqYdlb = null;
    private TextView qqZzb = null;
    private TextView qqHzb = null;
    private TextView qqGpszzb = null;
    private TextView qqGpshzb = null;
    private TextView qqXian = null;
    private TextView qqDm = null;
    private TextView qqHb = null;
    private TextView qqPx = null;
    private TextView qqPw = null;
    private TextView qqPd = null;
    private TextView qqDbxt = null;
    private TextView qqSqgd = null;
    private TextView qqFshd = null;
    private TextView qqQsgmjbl = null;
    private TextView qqJyll = null;
    private TextView qqTrmc = null;
    private TextView qqTrzd = null;
    private TextView qqTrlshl = null;
    private TextView qqTchd = null;
    private TextView qqFzchd = null;
    private TextView qqKzlyhd = null;
    private TextView qqZblx = null;
    private TextView qqGmfgd = null;
    private TextView qqGmgd = null;
    private TextView qqCbfgd = null;
    private TextView qqCbgd = null;
    private TextView qqZbzfgd = null;
    private TextView qqDl = null;
    private TextView qqTdqs = null;
    private TextView qqLmqs = null;
    private TextView qqSllb = null;
    private TextView qqGylsqdj = null;
    private TextView qqGylbhdj = null;
    private TextView qqSpljydj = null;
    private TextView qqFycs = null;
    private TextView qqLinzh = null;
    private TextView qqQy = null;
    private TextView qqYssz = null;
    private TextView qqPjnl = null;
    private TextView qqLingz = null;
    private TextView qqCq = null;
    private TextView qqPjxj = null;
    private TextView qqPjsg = null;
    private TextView qqYbd = null;
    private TextView qqSlqljg = null;
    private TextView qqLcjg = null;
    private TextView qqSzjg = null;
    private TextView qqZrd = null;
    private TextView qqKjd = null;
    private TextView qqSlzhlx = null;
    private TextView qqSlzhdj = null;
    private TextView qqSljkdj = null;
    private TextView qqSpszs = null;
    private TextView qqZzzs = null;
    private TextView qqTrgxdj = null;
    private TextView qqDlmjdj = null;
    private TextView qqDlbhyy = null;
    private TextView qqYwtsdd = null;
    private TextView qqDcrq = null;

    private CheckBox cbYdh = null;
    private CheckBox cbYdlb = null;
    private CheckBox cbZzb = null;
    private CheckBox cbHzb = null;
    private CheckBox cbGpszzb = null;
    private CheckBox cbGpshzb = null;
    private CheckBox cbXian = null;
    private CheckBox cbDm = null;
    private CheckBox cbHb = null;
    private CheckBox cbPx = null;
    private CheckBox cbPw = null;
    private CheckBox cbPd = null;
    private CheckBox cbDbxt = null;
    private CheckBox cbSqgd = null;
    private CheckBox cbFshd = null;
    private CheckBox cbQsgmjbl = null;
    private CheckBox cbJyll = null;
    private CheckBox cbTrmc = null;
    private CheckBox cbTrzd = null;
    private CheckBox cbTrlshl = null;
    private CheckBox cbTchd = null;
    private CheckBox cbFzchd = null;
    private CheckBox cbKzlyhd = null;
    private CheckBox cbZblx = null;
    private CheckBox cbGmfgd = null;
    private CheckBox cbGmgd = null;
    private CheckBox cbCbfgd = null;
    private CheckBox cbCbgd = null;
    private CheckBox cbZbzfgd = null;
    private CheckBox cbDl = null;
    private CheckBox cbTdqs = null;
    private CheckBox cbLmqs = null;
    private CheckBox cbSllb = null;
    private CheckBox cbGylsqdj = null;
    private CheckBox cbGylbhdj = null;
    private CheckBox cbSpljydj = null;
    private CheckBox cbFycs = null;
    private CheckBox cbLinzh = null;
    private CheckBox cbQy = null;
    private CheckBox cbYssz = null;
    private CheckBox cbPjnl = null;
    private CheckBox cbLingz = null;
    private CheckBox cbCq = null;
    private CheckBox cbPjxj = null;
    private CheckBox cbPjsg = null;
    private CheckBox cbYbd = null;
    private CheckBox cbSlqljg = null;
    private CheckBox cbLcjg = null;
    private CheckBox cbSzjg = null;
    private CheckBox cbZrd = null;
    private CheckBox cbKjd = null;
    private CheckBox cbSlzhlx = null;
    private CheckBox cbSlzhdj = null;
    private CheckBox cbSljkdj = null;
    private CheckBox cbSpszs = null;
    private CheckBox cbZzzs = null;
    private CheckBox cbTrgxdj = null;
    private CheckBox cbDlmjdj = null;
    private CheckBox cbDlbhyy = null;
    private CheckBox cbYwtsdd = null;
    private CheckBox cbDcrq = null;

    private CheckBox cbAll = null;

    private MyTouchListener myTouchListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.slqc_ydyz);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = this.getIntent();
        ydh = intent.getIntExtra("ydh", 0);
        int[] ii = YangdiMgr.GetDczt(ydh);
        iStatus = ii[5];

        myTouchListener = new MyTouchListener();

        btnClose = (Button) findViewById(R.id.btn_close);
        btnYdloc = (Button) findViewById(R.id.btn_coord);
        btnResetloc = (Button) findViewById(R.id.btn_reset);
        btnLingz = (Button) findViewById(R.id.btn_lingz);
        btnSlqljg = (Button) findViewById(R.id.btn_slqljg);
        btnYssz = (Button) findViewById(R.id.btn_yssz);
        btnCopy = (Button) findViewById(R.id.btn_copy);
        btnCheck = (Button) findViewById(R.id.btn_check);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        btnClose.setOnClickListener(this);
        btnYdloc.setOnClickListener(this);
        btnResetloc.setOnClickListener(this);
        btnLingz.setOnClickListener(this);
        btnSlqljg.setOnClickListener(this);
        btnYssz.setOnClickListener(this);
        btnCopy.setOnClickListener(this);
        btnCheck.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        btnYsszReset = (Button) findViewById(R.id.btn_yssz_reset);
        btnYsszReset.setOnClickListener(this);
        btnPjxjReset = (Button) findViewById(R.id.btn_pjxj_reset);
        btnPjxjReset.setOnClickListener(this);

        tvDm = (TextView) findViewById(R.id.tv_dm);
        tvHb = (TextView) findViewById(R.id.tv_hb);
        tvPx = (TextView) findViewById(R.id.tv_px);
        tvPw = (TextView) findViewById(R.id.tv_pw);
        tvPd = (TextView) findViewById(R.id.tv_pd);
        tvDbxt = (TextView) findViewById(R.id.tv_dbxt);
        tvSqgd = (TextView) findViewById(R.id.tv_sqgd);
        tvFshd = (TextView) findViewById(R.id.tv_fshd);
        tvQsgmjbl = (TextView) findViewById(R.id.tv_qsgmjbl);
        tvJyll = (TextView) findViewById(R.id.tv_jyll);
        tvTrmc = (TextView) findViewById(R.id.tv_trmc);
        tvTrzd = (TextView) findViewById(R.id.tv_trzd);
        tvTrlshl = (TextView) findViewById(R.id.tv_trlshl);
        tvTchd = (TextView) findViewById(R.id.tv_trhd);
        tvFzchd = (TextView) findViewById(R.id.tv_fzchd);
        tvKzlyhd = (TextView) findViewById(R.id.tv_kzlyhd);
        tvZblx = (TextView) findViewById(R.id.tv_zblx);
        tvGmfgd = (TextView) findViewById(R.id.tv_gmfgd);
        tvGmgd = (TextView) findViewById(R.id.tv_gmgd);
        tvCbfgd = (TextView) findViewById(R.id.tv_cbfgd);
        tvCbgd = (TextView) findViewById(R.id.tv_cbgd);
        tvZbzfgd = (TextView) findViewById(R.id.tv_zbzfgd);
        tvDl = (TextView) findViewById(R.id.tv_dl);
        tvTdqs = (TextView) findViewById(R.id.tv_tdqs);
        tvLmqs = (TextView) findViewById(R.id.tv_lmqs);
        tvSllb = (TextView) findViewById(R.id.tv_sllb);
        tvGylsqdj = (TextView) findViewById(R.id.tv_gylsqdj);
        tvGylbhdj = (TextView) findViewById(R.id.tv_gylbhdj);
        tvSpljydj = (TextView) findViewById(R.id.tv_spljydj);
        tvFycs = (TextView) findViewById(R.id.tv_fycs);
        tvLinzh = (TextView) findViewById(R.id.tv_linzh);
        tvQy = (TextView) findViewById(R.id.tv_qy);
        tvYssz = (TextView) findViewById(R.id.tv_yssz);
        tvPjnl = (TextView) findViewById(R.id.tv_pjnl);
        tvLingz = (TextView) findViewById(R.id.tv_lingz);
        tvCq = (TextView) findViewById(R.id.tv_cq);
        tvPjxj = (TextView) findViewById(R.id.tv_pjxj);
        tvPjsg = (TextView) findViewById(R.id.tv_pjsg);
        tvYbd = (TextView) findViewById(R.id.tv_ybd);
        tvSlqljg = (TextView) findViewById(R.id.tv_slqljg);
        tvLcjg = (TextView) findViewById(R.id.tv_lcjg);
        tvSzjg = (TextView) findViewById(R.id.tv_szjg);
        tvZrd = (TextView) findViewById(R.id.tv_zrd);
        tvKjd = (TextView) findViewById(R.id.tv_kjd);
        tvSlzhlx = (TextView) findViewById(R.id.tv_slzhlx);
        tvSlzhdj = (TextView) findViewById(R.id.tv_slzhdj);
        tvSljkdj = (TextView) findViewById(R.id.tv_sljkdj);
        tvSpszs = (TextView) findViewById(R.id.tv_spszs);
        tvZzzs = (TextView) findViewById(R.id.tv_zzzs);
        tvTrgxdj = (TextView) findViewById(R.id.tv_trgxdj);
        tvDlmjdj = (TextView) findViewById(R.id.tv_dlmjdj);
        tvDlbhyy = (TextView) findViewById(R.id.tv_dlbhyy);
        tvYwtsdd = (TextView) findViewById(R.id.tv_ywtsdd);
        tvDcrq = (TextView) findViewById(R.id.tv_dcrq);
        //tvF1 = (TextView)findViewById(R.id.tv_f1);
        //tvF2 = (TextView)findViewById(R.id.tv_f2);

        tvDl.setTextColor(Color.RED);

        etYdh = (EditText) findViewById(R.id.et_ydh);
        etZzb = (EditText) findViewById(R.id.et_zzb);
        etHzb = (EditText) findViewById(R.id.et_hzb);
        etGpszzb = (EditText) findViewById(R.id.et_gpszzb);
        etGpshzb = (EditText) findViewById(R.id.et_gpshzb);
        etXian = (EditText) findViewById(R.id.et_xian);
        etHb = (EditText) findViewById(R.id.et_hb);
        etPd = (EditText) findViewById(R.id.et_pd);
        etSqgd = (EditText) findViewById(R.id.et_sqgd);
        etFshd = (EditText) findViewById(R.id.et_fshd);
        etQsgmjbl = (EditText) findViewById(R.id.et_qsgmjbl);
        etJyll = (EditText) findViewById(R.id.et_jyll);
        etTrlshl = (EditText) findViewById(R.id.et_trlshl);
        etTchd = (EditText) findViewById(R.id.et_trhd);
        etFzchd = (EditText) findViewById(R.id.et_fzchd);
        etKzlyhd = (EditText) findViewById(R.id.et_kzlyhd);
        etGmfgd = (EditText) findViewById(R.id.et_gmfgd);
        etGmgd = (EditText) findViewById(R.id.et_gmgd);
        etCbfgd = (EditText) findViewById(R.id.et_cbfgd);
        etCbgd = (EditText) findViewById(R.id.et_cbgd);
        etZbzfgd = (EditText) findViewById(R.id.et_zbzfgd);
        etYssz = (EditText) findViewById(R.id.et_yssz);
        etPjnl = (EditText) findViewById(R.id.et_pjnl);
        etPjxj = (EditText) findViewById(R.id.et_pjxj);
        etPjsg = (EditText) findViewById(R.id.et_pjsg);
        etYbd = (EditText) findViewById(R.id.et_ybd);
        etSpszs = (EditText) findViewById(R.id.et_spszs);
        etZzzs = (EditText) findViewById(R.id.et_zzzs);
        etDcrq = (EditText) findViewById(R.id.et_dcrq);

        etYdh.setEnabled(false);
        etZzb.setEnabled(false);
        etHzb.setEnabled(false);
        etGpszzb.setEnabled(false);
        etGpshzb.setEnabled(false);
        etXian.setEnabled(false);
        etPjxj.setEnabled(false);
        etYssz.setEnabled(false);
        etDcrq.setEnabled(false);

        etPd.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_QXJ && f <= YangdiMgr.MAX_QXJ) {
                    etPd.setTextColor(Color.BLACK);
                } else {
                    etPd.setTextColor(Color.RED);
                }
            }
        });
        etSqgd.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_SQGD && f <= YangdiMgr.MAX_SQGD) {
                    etSqgd.setTextColor(Color.BLACK);
                } else {
                    etSqgd.setTextColor(Color.RED);
                }
            }
        });
        etFshd.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_FSHD && f <= YangdiMgr.MAX_FSHD) {
                    etFshd.setTextColor(Color.BLACK);
                } else {
                    etFshd.setTextColor(Color.RED);
                }
            }
        });
        etQsgmjbl.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_BFB && f <= YangdiMgr.MAX_BFB) {
                    etQsgmjbl.setTextColor(Color.BLACK);
                } else {
                    etQsgmjbl.setTextColor(Color.RED);
                }
            }
        });
        etJyll.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_BFB && f <= YangdiMgr.MAX_BFB) {
                    etJyll.setTextColor(Color.BLACK);
                } else {
                    etJyll.setTextColor(Color.RED);
                }
            }
        });
        etTrlshl.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_BFB && f <= YangdiMgr.MAX_BFB) {
                    etTrlshl.setTextColor(Color.BLACK);
                } else {
                    etTrlshl.setTextColor(Color.RED);
                }
            }
        });
        etTchd.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_TRHD && f <= YangdiMgr.MAX_TRHD) {
                    etTchd.setTextColor(Color.BLACK);
                } else {
                    etTchd.setTextColor(Color.RED);
                }
            }
        });
        etFzchd.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_FZCHD && f <= YangdiMgr.MAX_FZCHD) {
                    etFzchd.setTextColor(Color.BLACK);
                } else {
                    etFzchd.setTextColor(Color.RED);
                }
            }
        });
        etKzlyhd.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_KZLYHD && f <= YangdiMgr.MAX_KZLYHD) {
                    etKzlyhd.setTextColor(Color.BLACK);
                } else {
                    etKzlyhd.setTextColor(Color.RED);
                }
            }
        });
        etGmfgd.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_BFB && f <= YangdiMgr.MAX_BFB) {
                    etGmfgd.setTextColor(Color.BLACK);
                } else {
                    etGmfgd.setTextColor(Color.RED);
                }
            }
        });
        etGmgd.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_GMGD && f <= YangdiMgr.MAX_GMGD) {
                    etGmgd.setTextColor(Color.BLACK);
                } else {
                    etGmgd.setTextColor(Color.RED);
                }
            }
        });
        etCbfgd.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_BFB && f <= YangdiMgr.MAX_BFB) {
                    etCbfgd.setTextColor(Color.BLACK);
                } else {
                    etCbfgd.setTextColor(Color.RED);
                }
            }
        });
        etCbgd.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_CBGD && f <= YangdiMgr.MAX_CBGD) {
                    etCbgd.setTextColor(Color.BLACK);
                } else {
                    etCbgd.setTextColor(Color.RED);
                }
            }
        });
        etZbzfgd.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_BFB && f <= YangdiMgr.MAX_BFB) {
                    etZbzfgd.setTextColor(Color.BLACK);
                } else {
                    etZbzfgd.setTextColor(Color.RED);
                }
            }
        });
        etPjnl.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_PJNL && f <= YangdiMgr.MAX_PJNL) {
                    etPjnl.setTextColor(Color.BLACK);
                } else {
                    etPjnl.setTextColor(Color.RED);
                }
            }
        });
        etPjxj.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_PJXJ && f <= YangdiMgr.MAX_PJXJ) {
                    etPjxj.setTextColor(Color.BLACK);
                } else {
                    etPjxj.setTextColor(Color.RED);
                }
            }
        });
        etPjsg.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_SG && f <= YangdiMgr.MAX_SG) {
                    etPjsg.setTextColor(Color.BLACK);
                } else {
                    etPjsg.setTextColor(Color.RED);
                }
            }
        });
        etYbd.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_YBD && f <= YangdiMgr.MAX_YBD) {
                    etYbd.setTextColor(Color.BLACK);
                } else {
                    etYbd.setTextColor(Color.RED);
                }
            }
        });
        etDcrq.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                if (YangdiMgr.IsValidDate(str)) {
                    etDcrq.setTextColor(Color.BLACK);
                } else {
                    etDcrq.setTextColor(Color.RED);
                }
            }
        });

        spYdlb = (Spinner) findViewById(R.id.sp_ydlb);
        spDm = (Spinner) findViewById(R.id.sp_dm);
        spPx = (Spinner) findViewById(R.id.sp_px);
        spPw = (Spinner) findViewById(R.id.sp_pw);
        spDbxt = (Spinner) findViewById(R.id.sp_dbxt);
        spTrmc = (Spinner) findViewById(R.id.sp_trmc);
        spTrzd = (Spinner) findViewById(R.id.sp_trzd);
        spZblx = (Spinner) findViewById(R.id.sp_zblx);
        spDl = (Spinner) findViewById(R.id.sp_dl);
        spTdqs = (Spinner) findViewById(R.id.sp_tdqs);
        spLmqs = (Spinner) findViewById(R.id.sp_lmqs);
        spSllb = (Spinner) findViewById(R.id.sp_sllb);
        spGylsqdj = (Spinner) findViewById(R.id.sp_gylsqdj);
        spGylbhdj = (Spinner) findViewById(R.id.sp_gylbhdj);
        spSpljydj = (Spinner) findViewById(R.id.sp_spljydj);
        spFycs = (Spinner) findViewById(R.id.sp_fycs);
        spLinzh = (Spinner) findViewById(R.id.sp_linzh);
        spQy = (Spinner) findViewById(R.id.sp_qy);
        spCq = (Spinner) findViewById(R.id.sp_cq);
        spLingz = (Spinner) findViewById(R.id.sp_lingz);
        spSlqljg = (Spinner) findViewById(R.id.sp_slqljg);
        spLcjg = (Spinner) findViewById(R.id.sp_lcjg);
        spSzjg = (Spinner) findViewById(R.id.sp_szjg);
        spZrd = (Spinner) findViewById(R.id.sp_zrd);
        spKjd = (Spinner) findViewById(R.id.sp_kjd);
        spSlzhlx = (Spinner) findViewById(R.id.sp_slzhlx);
        spSlzhdj = (Spinner) findViewById(R.id.sp_slzhdj);
        spSljkdj = (Spinner) findViewById(R.id.sp_sljkdj);
        spTrgxdj = (Spinner) findViewById(R.id.sp_trgxdj);
        spDlmjdj = (Spinner) findViewById(R.id.sp_dlmjdj);
        spDlbhyy = (Spinner) findViewById(R.id.sp_dlbhyy);
        spYwtsdd = (Spinner) findViewById(R.id.sp_ywtsdd);
        //spF1 = (Spinner)findViewById(R.id.sp_f1);
        //spF2 = (Spinner)findViewById(R.id.sp_f2);

        spYdlb.setOnTouchListener(myTouchListener);
        spDm.setOnTouchListener(myTouchListener);
        spPx.setOnTouchListener(myTouchListener);
        spPw.setOnTouchListener(myTouchListener);
        spDbxt.setOnTouchListener(myTouchListener);
        spTrmc.setOnTouchListener(myTouchListener);
        spTrzd.setOnTouchListener(myTouchListener);
        spZblx.setOnTouchListener(myTouchListener);
        spDl.setOnTouchListener(myTouchListener);
        spTdqs.setOnTouchListener(myTouchListener);
        spLmqs.setOnTouchListener(myTouchListener);
        spSllb.setOnTouchListener(myTouchListener);
        spGylsqdj.setOnTouchListener(myTouchListener);
        spGylbhdj.setOnTouchListener(myTouchListener);
        spSpljydj.setOnTouchListener(myTouchListener);
        spFycs.setOnTouchListener(myTouchListener);
        spLinzh.setOnTouchListener(myTouchListener);
        spQy.setOnTouchListener(myTouchListener);
        spCq.setOnTouchListener(myTouchListener);
        spLingz.setOnTouchListener(myTouchListener);
        spSlqljg.setOnTouchListener(myTouchListener);
        spLcjg.setOnTouchListener(myTouchListener);
        spSzjg.setOnTouchListener(myTouchListener);
        spZrd.setOnTouchListener(myTouchListener);
        spKjd.setOnTouchListener(myTouchListener);
        spSlzhlx.setOnTouchListener(myTouchListener);
        spSlzhdj.setOnTouchListener(myTouchListener);
        spSljkdj.setOnTouchListener(myTouchListener);
        spTrgxdj.setOnTouchListener(myTouchListener);
        spDlmjdj.setOnTouchListener(myTouchListener);
        spDlbhyy.setOnTouchListener(myTouchListener);
        spYwtsdd.setOnTouchListener(myTouchListener);
        //spF1.setOnTouchListener(myTouchListener);
        //spF2.setOnTouchListener(myTouchListener);

        List<String> lstYdlb = Resmgr.GetValueList("ydlb");
        ArrayAdapter<String> apYdlb = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstYdlb);
        apYdlb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYdlb.setAdapter(apYdlb);

        List<String> lstDm = Resmgr.GetValueList("dm");
        ArrayAdapter<String> apDm = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstDm);
        apDm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDm.setAdapter(apDm);

        List<String> lstPx = Resmgr.GetValueList("px");
        ArrayAdapter<String> apPx = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstPx);
        apPx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPx.setAdapter(apPx);

        List<String> lstPw = Resmgr.GetValueList("pw");
        ArrayAdapter<String> apPw = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstPw);
        apPw.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPw.setAdapter(apPw);

        List<String> lstDbxt = Resmgr.GetValueList("dbxt");
        ArrayAdapter<String> apDbxt = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstDbxt);
        apDbxt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDbxt.setAdapter(apDbxt);

        List<String> lstTrmc = Resmgr.GetValueList("trmc");
        ArrayAdapter<String> apTrmc = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstTrmc);
        apTrmc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTrmc.setAdapter(apTrmc);

        List<String> lstTrzd = Resmgr.GetValueList("trzd");
        ArrayAdapter<String> apTrzd = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstTrzd);
        apTrzd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTrzd.setAdapter(apTrzd);

        List<String> lstZblx = Resmgr.GetValueList("zblx");
        ArrayAdapter<String> apZblx = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstZblx);
        apZblx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spZblx.setAdapter(apZblx);

        List<String> lstDl = Resmgr.GetValueList("dl");
        ArrayAdapter<String> apDl = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstDl);
        apDl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDl.setAdapter(apDl);

        List<String> lstTdqs = Resmgr.GetValueList("tdqs");
        ArrayAdapter<String> apTdqs = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstTdqs);
        apTdqs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTdqs.setAdapter(apTdqs);

        List<String> lstLmqs = Resmgr.GetValueList("lmqs");
        ArrayAdapter<String> apLmqs = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstLmqs);
        apLmqs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLmqs.setAdapter(apLmqs);

        List<String> lstSllb = Resmgr.GetValueList("sllb");
        ArrayAdapter<String> apSllb = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstSllb);
        apSllb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSllb.setAdapter(apSllb);

        List<String> lstGylsqdj = Resmgr.GetValueList("gylsqdj");
        ArrayAdapter<String> apGylsqdj = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstGylsqdj);
        apGylsqdj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGylsqdj.setAdapter(apGylsqdj);

        List<String> lstGylbhdj = Resmgr.GetValueList("gylbhdj");
        ArrayAdapter<String> apGylbhdj = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstGylbhdj);
        apGylbhdj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGylbhdj.setAdapter(apGylbhdj);

        List<String> lstSpljydj = Resmgr.GetValueList("spljydj");
        ArrayAdapter<String> apSpljydj = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstSpljydj);
        apSpljydj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSpljydj.setAdapter(apSpljydj);

        List<String> lstFycs = Resmgr.GetValueList("fycs");
        ArrayAdapter<String> apFycs = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstFycs);
        apFycs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFycs.setAdapter(apFycs);

        List<String> lstLinzh = Resmgr.GetValueList("linzh");
        ArrayAdapter<String> apLinzh = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstLinzh);
        apLinzh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLinzh.setAdapter(apLinzh);

        List<String> lstQy = Resmgr.GetValueList("qy");
        ArrayAdapter<String> apQy = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstQy);
        apQy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spQy.setAdapter(apQy);

        List<String> lstLingz = Resmgr.GetValueList("lingz");
        ArrayAdapter<String> apLingz = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstLingz);
        apLingz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLingz.setAdapter(apLingz);

        List<String> lstCq = Resmgr.GetValueList("cq");
        ArrayAdapter<String> apCq = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstCq);
        apCq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCq.setAdapter(apCq);

        List<String> lstSlqljg = Resmgr.GetValueList("slqljg");
        ArrayAdapter<String> apSlqljg = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstSlqljg);
        apSlqljg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSlqljg.setAdapter(apSlqljg);

        List<String> lstLcjg = Resmgr.GetValueList("ydlcjg");
        ArrayAdapter<String> apLcjg = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstLcjg);
        apLcjg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLcjg.setAdapter(apLcjg);

        List<String> lstSzjg = Resmgr.GetValueList("szjg");
        ArrayAdapter<String> apSzjg = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstSzjg);
        apSzjg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSzjg.setAdapter(apSzjg);

        List<String> lstZrd = Resmgr.GetValueList("zrd");
        ArrayAdapter<String> apZrd = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstZrd);
        apZrd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spZrd.setAdapter(apZrd);

        List<String> lstKjd = Resmgr.GetValueList("kjd");
        ArrayAdapter<String> apKjd = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstKjd);
        apKjd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKjd.setAdapter(apKjd);

        List<String> lstSlzhlx = Resmgr.GetValueList("slzhlx");
        ArrayAdapter<String> apSlzhlx = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstSlzhlx);
        apSlzhlx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSlzhlx.setAdapter(apSlzhlx);

        List<String> lstSlzhdj = Resmgr.GetValueList("slzhdj");
        ArrayAdapter<String> apSlzhdj = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstSlzhdj);
        apSlzhdj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSlzhdj.setAdapter(apSlzhdj);

        List<String> lstSljkdj = Resmgr.GetValueList("sljkdj");
        ArrayAdapter<String> apSljkdj = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstSljkdj);
        apSljkdj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSljkdj.setAdapter(apSljkdj);

        List<String> lstTrgxdj = Resmgr.GetValueList("trgxdj");
        ArrayAdapter<String> apTrgxdj = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstTrgxdj);
        apTrgxdj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTrgxdj.setAdapter(apTrgxdj);

        List<String> lstDlmjdj = Resmgr.GetValueList("dlmjdj");
        ArrayAdapter<String> apDlmjdj = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstDlmjdj);
        apDlmjdj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDlmjdj.setAdapter(apDlmjdj);

        List<String> lstDlbhyy = Resmgr.GetValueList("dlbhyy");
        ArrayAdapter<String> apDlbhyy = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstDlbhyy);
        apDlbhyy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDlbhyy.setAdapter(apDlbhyy);

        List<String> lstYwtsdd = Resmgr.GetValueList("ywtsdd");
        ArrayAdapter<String> apYwtsdd = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstYwtsdd);
        apYwtsdd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYwtsdd.setAdapter(apYwtsdd);
        /*
        List<String> lstF1 = Resmgr.GetValueList("sfntjjl");
    	ArrayAdapter<String> apF1 = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstF1);
    	apF1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spF1.setAdapter(apF1);

    	List<String> lstF2 = Resmgr.GetValueList("sffldsl");
    	ArrayAdapter<String> apF2 = new ArrayAdapter<String>(SlqcYdyz.this, android.R.layout.simple_spinner_item, lstF2);
    	apF2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spF2.setAdapter(apF2);
        */
        spYdlb.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == 3) {
                    etPjxj.setEnabled(true);
                } else {
                    etPjxj.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spDl.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String s_dl = spDl.getSelectedItem().toString();
                int i_dl = Resmgr.GetCodeByValue("dl", s_dl);
                int c0 = Color.BLACK;
                int c1 = Color.RED;
                int c2 = Color.BLUE;
                int c3 = Color.BLACK;
                switch (i_dl) {
                    case 111:
                    case 119: {
                        tvDm.setTextColor(c1);
                        tvHb.setTextColor(c1);
                        tvPx.setTextColor(c1);
                        tvPw.setTextColor(c1);
                        tvPd.setTextColor(c1);
                        tvDbxt.setTextColor(c1);
                        tvSqgd.setTextColor(c2);
                        tvFshd.setTextColor(c2);
                        tvQsgmjbl.setTextColor(c2);
                        tvJyll.setTextColor(c2);
                        tvTrmc.setTextColor(c1);
                        tvTrzd.setTextColor(c1);
                        tvTrlshl.setTextColor(c1);
                        tvTchd.setTextColor(c1);
                        tvFzchd.setTextColor(c2);
                        tvKzlyhd.setTextColor(c2);
                        tvZblx.setTextColor(c1);
                        tvGmfgd.setTextColor(c2);
                        tvGmgd.setTextColor(c2);
                        tvCbfgd.setTextColor(c2);
                        tvCbgd.setTextColor(c2);
                        tvZbzfgd.setTextColor(c1);
                        //tvDl.setTextColor(c1);
                        tvTdqs.setTextColor(c1);
                        tvLmqs.setTextColor(c1);
                        tvSllb.setTextColor(c1);
                        tvGylsqdj.setTextColor(c2);
                        tvGylbhdj.setTextColor(c2);
                        tvSpljydj.setTextColor(c2);
                        tvFycs.setTextColor(c1);
                        tvLinzh.setTextColor(c1);
                        tvQy.setTextColor(c1);
                        tvYssz.setTextColor(c1);
                        tvPjnl.setTextColor(c1);
                        tvLingz.setTextColor(c2);
                        tvCq.setTextColor(c2);
                        tvPjxj.setTextColor(c2);
                        tvPjsg.setTextColor(c2);
                        tvYbd.setTextColor(c1);
                        tvSlqljg.setTextColor(c1);
                        //tvLcjg.setTextColor(c0);
                        tvSzjg.setTextColor(c1);
                        tvZrd.setTextColor(c1);
                        tvKjd.setTextColor(c2);
                        tvSlzhlx.setTextColor(c1);
                        tvSlzhdj.setTextColor(c1);
                        tvSljkdj.setTextColor(c1);
                        tvSpszs.setTextColor(c2);
                        tvZzzs.setTextColor(c2);
                        tvTrgxdj.setTextColor(c3);
                        tvDlmjdj.setTextColor(c1);
                        tvDlbhyy.setTextColor(c2);
                        tvYwtsdd.setTextColor(c1);
                        //tvDcrq.setTextColor(c0);
                        //tvF1.setTextColor(c1);
                        //tvF2.setTextColor(c1);
                        break;
                    }
                    case 131:
                    case 1311:
                    case 1312:
                    case 1315: {
                        tvDm.setTextColor(c1);
                        tvHb.setTextColor(c1);
                        tvPx.setTextColor(c1);
                        tvPw.setTextColor(c1);
                        tvPd.setTextColor(c1);
                        tvDbxt.setTextColor(c1);
                        tvSqgd.setTextColor(c2);
                        tvFshd.setTextColor(c2);
                        tvQsgmjbl.setTextColor(c2);
                        tvJyll.setTextColor(c2);
                        tvTrmc.setTextColor(c1);
                        tvTrzd.setTextColor(c1);
                        tvTrlshl.setTextColor(c1);
                        tvTchd.setTextColor(c1);
                        tvFzchd.setTextColor(c2);
                        tvKzlyhd.setTextColor(c2);
                        tvZblx.setTextColor(c1);
                        tvGmfgd.setTextColor(c1);
                        tvGmgd.setTextColor(c1);
                        tvCbfgd.setTextColor(c2);
                        tvCbgd.setTextColor(c2);
                        tvZbzfgd.setTextColor(c1);
                        //tvDl.setTextColor(c1);
                        tvTdqs.setTextColor(c1);
                        tvLmqs.setTextColor(c1);
                        tvSllb.setTextColor(c1);
                        tvGylsqdj.setTextColor(c2);
                        tvGylbhdj.setTextColor(c2);
                        tvSpljydj.setTextColor(c2);
                        tvFycs.setTextColor(c1);
                        tvLinzh.setTextColor(c1);
                        tvQy.setTextColor(c1);
                        tvYssz.setTextColor(c1);
                        tvPjnl.setTextColor(c2);
                        tvLingz.setTextColor(c3);
                        tvCq.setTextColor(c2);
                        tvPjxj.setTextColor(c3);
                        tvPjsg.setTextColor(c3);
                        tvYbd.setTextColor(c3);
                        tvSlqljg.setTextColor(c1);
                        //tvLcjg.setTextColor(c0);
                        tvSzjg.setTextColor(c1);
                        tvZrd.setTextColor(c1);
                        tvKjd.setTextColor(c3);
                        tvSlzhlx.setTextColor(c1);
                        tvSlzhdj.setTextColor(c1);
                        tvSljkdj.setTextColor(c1);
                        tvSpszs.setTextColor(c2);
                        tvZzzs.setTextColor(c2);
                        tvTrgxdj.setTextColor(c3);
                        tvDlmjdj.setTextColor(c1);
                        tvDlbhyy.setTextColor(c2);
                        tvYwtsdd.setTextColor(c1);
                        //tvDcrq.setTextColor(c0);
                        //tvF1.setTextColor(c1);
                        //tvF2.setTextColor(c1);
                        break;
                    }
                    case 132: {
                        tvDm.setTextColor(c1);
                        tvHb.setTextColor(c1);
                        tvPx.setTextColor(c1);
                        tvPw.setTextColor(c1);
                        tvPd.setTextColor(c1);
                        tvDbxt.setTextColor(c1);
                        tvSqgd.setTextColor(c2);
                        tvFshd.setTextColor(c2);
                        tvQsgmjbl.setTextColor(c2);
                        tvJyll.setTextColor(c2);
                        tvTrmc.setTextColor(c1);
                        tvTrzd.setTextColor(c1);
                        tvTrlshl.setTextColor(c1);
                        tvTchd.setTextColor(c1);
                        tvFzchd.setTextColor(c2);
                        tvKzlyhd.setTextColor(c2);
                        tvZblx.setTextColor(c1);
                        tvGmfgd.setTextColor(c1);
                        tvGmgd.setTextColor(c1);
                        tvCbfgd.setTextColor(c2);
                        tvCbgd.setTextColor(c2);
                        tvZbzfgd.setTextColor(c1);
                        //tvDl.setTextColor(c1);
                        tvTdqs.setTextColor(c1);
                        tvLmqs.setTextColor(c1);
                        tvSllb.setTextColor(c1);
                        tvGylsqdj.setTextColor(c2);
                        tvGylbhdj.setTextColor(c2);
                        tvSpljydj.setTextColor(c2);
                        tvFycs.setTextColor(c1);
                        tvLinzh.setTextColor(c1);
                        tvQy.setTextColor(c1);
                        tvYssz.setTextColor(c1);
                        tvPjnl.setTextColor(c2);
                        tvLingz.setTextColor(c3);
                        tvCq.setTextColor(c3);
                        tvPjxj.setTextColor(c3);
                        tvPjsg.setTextColor(c3);
                        tvYbd.setTextColor(c3);
                        tvSlqljg.setTextColor(c3);
                        //tvLcjg.setTextColor(c0);
                        tvSzjg.setTextColor(c3);
                        tvZrd.setTextColor(c3);
                        tvKjd.setTextColor(c3);
                        tvSlzhlx.setTextColor(c3);
                        tvSlzhdj.setTextColor(c3);
                        tvSljkdj.setTextColor(c3);
                        tvSpszs.setTextColor(c2);
                        tvZzzs.setTextColor(c2);
                        tvTrgxdj.setTextColor(c1);
                        tvDlmjdj.setTextColor(c1);
                        tvDlbhyy.setTextColor(c2);
                        tvYwtsdd.setTextColor(c1);
                        //tvDcrq.setTextColor(c0);
                        //tvF1.setTextColor(c1);
                        //tvF2.setTextColor(c1);
                        break;
                    }
                    case 113: {
                        tvDm.setTextColor(c1);
                        tvHb.setTextColor(c1);
                        tvPx.setTextColor(c1);
                        tvPw.setTextColor(c1);
                        tvPd.setTextColor(c1);
                        tvDbxt.setTextColor(c1);
                        tvSqgd.setTextColor(c2);
                        tvFshd.setTextColor(c2);
                        tvQsgmjbl.setTextColor(c2);
                        tvJyll.setTextColor(c2);
                        tvTrmc.setTextColor(c1);
                        tvTrzd.setTextColor(c1);
                        tvTrlshl.setTextColor(c1);
                        tvTchd.setTextColor(c1);
                        tvFzchd.setTextColor(c2);
                        tvKzlyhd.setTextColor(c2);
                        tvZblx.setTextColor(c1);
                        tvGmfgd.setTextColor(c2);
                        tvGmgd.setTextColor(c2);
                        tvCbfgd.setTextColor(c2);
                        tvCbgd.setTextColor(c2);
                        tvZbzfgd.setTextColor(c1);
                        //tvDl.setTextColor(c1);
                        tvTdqs.setTextColor(c1);
                        tvLmqs.setTextColor(c1);
                        tvSllb.setTextColor(c1);
                        tvGylsqdj.setTextColor(c2);
                        tvGylbhdj.setTextColor(c2);
                        tvSpljydj.setTextColor(c2);
                        tvFycs.setTextColor(c1);
                        tvLinzh.setTextColor(c1);
                        tvQy.setTextColor(c1);
                        tvYssz.setTextColor(c1);
                        tvPjnl.setTextColor(c1);
                        tvLingz.setTextColor(c3);
                        tvCq.setTextColor(c3);
                        tvPjxj.setTextColor(c3);
                        tvPjsg.setTextColor(c3);
                        tvYbd.setTextColor(c1);
                        tvSlqljg.setTextColor(c1);
                        //tvLcjg.setTextColor(c0);
                        tvSzjg.setTextColor(c1);
                        tvZrd.setTextColor(c1);
                        tvKjd.setTextColor(c3);
                        tvSlzhlx.setTextColor(c1);
                        tvSlzhdj.setTextColor(c1);
                        tvSljkdj.setTextColor(c1);
                        tvSpszs.setTextColor(c2);
                        tvZzzs.setTextColor(c2);
                        tvTrgxdj.setTextColor(c3);
                        tvDlmjdj.setTextColor(c1);
                        tvDlbhyy.setTextColor(c2);
                        tvYwtsdd.setTextColor(c1);
                        //tvDcrq.setTextColor(c0);
                        //tvF1.setTextColor(c1);
                        //tvF2.setTextColor(c1);
                        break;
                    }
                    case 120: {
                        tvDm.setTextColor(c1);
                        tvHb.setTextColor(c1);
                        tvPx.setTextColor(c1);
                        tvPw.setTextColor(c1);
                        tvPd.setTextColor(c1);
                        tvDbxt.setTextColor(c1);
                        tvSqgd.setTextColor(c2);
                        tvFshd.setTextColor(c2);
                        tvQsgmjbl.setTextColor(c2);
                        tvJyll.setTextColor(c2);
                        tvTrmc.setTextColor(c1);
                        tvTrzd.setTextColor(c1);
                        tvTrlshl.setTextColor(c1);
                        tvTchd.setTextColor(c1);
                        tvFzchd.setTextColor(c2);
                        tvKzlyhd.setTextColor(c2);
                        tvZblx.setTextColor(c1);
                        tvGmfgd.setTextColor(c2);
                        tvGmgd.setTextColor(c2);
                        tvCbfgd.setTextColor(c2);
                        tvCbgd.setTextColor(c2);
                        tvZbzfgd.setTextColor(c1);
                        //tvDl.setTextColor(c1);
                        tvTdqs.setTextColor(c1);
                        tvLmqs.setTextColor(c1);
                        tvSllb.setTextColor(c1);
                        tvGylsqdj.setTextColor(c2);
                        tvGylbhdj.setTextColor(c2);
                        tvSpljydj.setTextColor(c2);
                        tvFycs.setTextColor(c1);
                        tvLinzh.setTextColor(c1);
                        tvQy.setTextColor(c1);
                        tvYssz.setTextColor(c1);
                        tvPjnl.setTextColor(c1);
                        tvLingz.setTextColor(c3);
                        tvCq.setTextColor(c3);
                        tvPjxj.setTextColor(c3);
                        tvPjsg.setTextColor(c3);
                        tvYbd.setTextColor(c1);
                        tvSlqljg.setTextColor(c3);
                        //tvLcjg.setTextColor(c0);
                        tvSzjg.setTextColor(c3);
                        tvZrd.setTextColor(c3);
                        tvKjd.setTextColor(c3);
                        tvSlzhlx.setTextColor(c3);
                        tvSlzhdj.setTextColor(c3);
                        tvSljkdj.setTextColor(c3);
                        tvSpszs.setTextColor(c2);
                        tvZzzs.setTextColor(c2);
                        tvTrgxdj.setTextColor(c1);
                        tvDlmjdj.setTextColor(c1);
                        tvDlbhyy.setTextColor(c2);
                        tvYwtsdd.setTextColor(c1);
                        //tvDcrq.setTextColor(c0);
                        //tvF1.setTextColor(c1);
                        //tvF2.setTextColor(c1);
                        break;
                    }
                    case 141: {
                        tvDm.setTextColor(c1);
                        tvHb.setTextColor(c1);
                        tvPx.setTextColor(c1);
                        tvPw.setTextColor(c1);
                        tvPd.setTextColor(c1);
                        tvDbxt.setTextColor(c1);
                        tvSqgd.setTextColor(c2);
                        tvFshd.setTextColor(c2);
                        tvQsgmjbl.setTextColor(c2);
                        tvJyll.setTextColor(c2);
                        tvTrmc.setTextColor(c1);
                        tvTrzd.setTextColor(c1);
                        tvTrlshl.setTextColor(c1);
                        tvTchd.setTextColor(c1);
                        tvFzchd.setTextColor(c2);
                        tvKzlyhd.setTextColor(c2);
                        tvZblx.setTextColor(c1);
                        tvGmfgd.setTextColor(c2);
                        tvGmgd.setTextColor(c2);
                        tvCbfgd.setTextColor(c2);
                        tvCbgd.setTextColor(c2);
                        tvZbzfgd.setTextColor(c1);
                        //tvDl.setTextColor(c1);
                        tvTdqs.setTextColor(c1);
                        tvLmqs.setTextColor(c1);
                        tvSllb.setTextColor(c1);
                        tvGylsqdj.setTextColor(c2);
                        tvGylbhdj.setTextColor(c2);
                        tvSpljydj.setTextColor(c2);
                        tvFycs.setTextColor(c2);
                        tvLinzh.setTextColor(c1);
                        tvQy.setTextColor(c1);
                        tvYssz.setTextColor(c1);
                        tvPjnl.setTextColor(c1);
                        tvLingz.setTextColor(c3);
                        tvCq.setTextColor(c3);
                        tvPjxj.setTextColor(c3);
                        tvPjsg.setTextColor(c3);
                        tvYbd.setTextColor(c3);
                        tvSlqljg.setTextColor(c3);
                        //tvLcjg.setTextColor(c0);
                        tvSzjg.setTextColor(c3);
                        tvZrd.setTextColor(c3);
                        tvKjd.setTextColor(c3);
                        tvSlzhlx.setTextColor(c3);
                        tvSlzhdj.setTextColor(c3);
                        tvSljkdj.setTextColor(c3);
                        tvSpszs.setTextColor(c2);
                        tvZzzs.setTextColor(c2);
                        tvTrgxdj.setTextColor(c3);
                        tvDlmjdj.setTextColor(c1);
                        tvDlbhyy.setTextColor(c2);
                        tvYwtsdd.setTextColor(c1);
                        //tvDcrq.setTextColor(c0);
                        //tvF1.setTextColor(c1);
                        //tvF2.setTextColor(c1);
                        break;
                    }
                    case 150: {
                        tvDm.setTextColor(c1);
                        tvHb.setTextColor(c1);
                        tvPx.setTextColor(c1);
                        tvPw.setTextColor(c1);
                        tvPd.setTextColor(c1);
                        tvDbxt.setTextColor(c1);
                        tvSqgd.setTextColor(c2);
                        tvFshd.setTextColor(c2);
                        tvQsgmjbl.setTextColor(c2);
                        tvJyll.setTextColor(c2);
                        tvTrmc.setTextColor(c1);
                        tvTrzd.setTextColor(c1);
                        tvTrlshl.setTextColor(c1);
                        tvTchd.setTextColor(c1);
                        tvFzchd.setTextColor(c2);
                        tvKzlyhd.setTextColor(c2);
                        tvZblx.setTextColor(c2);
                        tvGmfgd.setTextColor(c2);
                        tvGmgd.setTextColor(c2);
                        tvCbfgd.setTextColor(c2);
                        tvCbgd.setTextColor(c2);
                        tvZbzfgd.setTextColor(c2);
                        //tvDl.setTextColor(c1);
                        tvTdqs.setTextColor(c1);
                        tvLmqs.setTextColor(c1);
                        tvSllb.setTextColor(c1);
                        tvGylsqdj.setTextColor(c2);
                        tvGylbhdj.setTextColor(c2);
                        tvSpljydj.setTextColor(c2);
                        tvFycs.setTextColor(c3);
                        tvLinzh.setTextColor(c3);
                        tvQy.setTextColor(c3);
                        tvYssz.setTextColor(c3);
                        tvPjnl.setTextColor(c3);
                        tvLingz.setTextColor(c3);
                        tvCq.setTextColor(c3);
                        tvPjxj.setTextColor(c3);
                        tvPjsg.setTextColor(c3);
                        tvYbd.setTextColor(c3);
                        tvSlqljg.setTextColor(c3);
                        //tvLcjg.setTextColor(c0);
                        tvSzjg.setTextColor(c3);
                        tvZrd.setTextColor(c3);
                        tvKjd.setTextColor(c3);
                        tvSlzhlx.setTextColor(c3);
                        tvSlzhdj.setTextColor(c3);
                        tvSljkdj.setTextColor(c3);
                        tvSpszs.setTextColor(c2);
                        tvZzzs.setTextColor(c2);
                        tvTrgxdj.setTextColor(c3);
                        tvDlmjdj.setTextColor(c1);
                        tvDlbhyy.setTextColor(c2);
                        tvYwtsdd.setTextColor(c1);
                        //tvDcrq.setTextColor(c0);
                        //tvF1.setTextColor(c1);
                        //tvF2.setTextColor(c1);
                        break;
                    }
                    case 161:
                    case 162:
                    case 163: {
                        tvDm.setTextColor(c1);
                        tvHb.setTextColor(c1);
                        tvPx.setTextColor(c1);
                        tvPw.setTextColor(c1);
                        tvPd.setTextColor(c1);
                        tvDbxt.setTextColor(c1);
                        tvSqgd.setTextColor(c2);
                        tvFshd.setTextColor(c2);
                        tvQsgmjbl.setTextColor(c2);
                        tvJyll.setTextColor(c2);
                        tvTrmc.setTextColor(c1);
                        tvTrzd.setTextColor(c1);
                        tvTrlshl.setTextColor(c1);
                        tvTchd.setTextColor(c1);
                        tvFzchd.setTextColor(c2);
                        tvKzlyhd.setTextColor(c2);
                        tvZblx.setTextColor(c2);
                        tvGmfgd.setTextColor(c2);
                        tvGmgd.setTextColor(c2);
                        tvCbfgd.setTextColor(c2);
                        tvCbgd.setTextColor(c2);
                        tvZbzfgd.setTextColor(c2);
                        //tvDl.setTextColor(c1);
                        tvTdqs.setTextColor(c1);
                        tvLmqs.setTextColor(c2);
                        tvSllb.setTextColor(c1);
                        tvGylsqdj.setTextColor(c2);
                        tvGylbhdj.setTextColor(c2);
                        tvSpljydj.setTextColor(c2);
                        tvFycs.setTextColor(c3);
                        tvLinzh.setTextColor(c3);
                        tvQy.setTextColor(c3);
                        tvYssz.setTextColor(c3);
                        tvPjnl.setTextColor(c3);
                        tvLingz.setTextColor(c3);
                        tvCq.setTextColor(c3);
                        tvPjxj.setTextColor(c3);
                        tvPjsg.setTextColor(c3);
                        tvYbd.setTextColor(c3);
                        tvSlqljg.setTextColor(c3);
                        //tvLcjg.setTextColor(c0);
                        tvSzjg.setTextColor(c3);
                        tvZrd.setTextColor(c3);
                        tvKjd.setTextColor(c3);
                        tvSlzhlx.setTextColor(c3);
                        tvSlzhdj.setTextColor(c3);
                        tvSljkdj.setTextColor(c3);
                        tvSpszs.setTextColor(c2);
                        tvZzzs.setTextColor(c2);
                        tvTrgxdj.setTextColor(c1);
                        tvDlmjdj.setTextColor(c1);
                        tvDlbhyy.setTextColor(c2);
                        tvYwtsdd.setTextColor(c1);
                        //tvDcrq.setTextColor(c0);
                        //tvF1.setTextColor(c1);
                        //tvF2.setTextColor(c1);
                        break;
                    }
                    case 171:
                    case 172: {
                        tvDm.setTextColor(c1);
                        tvHb.setTextColor(c1);
                        tvPx.setTextColor(c1);
                        tvPw.setTextColor(c1);
                        tvPd.setTextColor(c1);
                        tvDbxt.setTextColor(c1);
                        tvSqgd.setTextColor(c2);
                        tvFshd.setTextColor(c2);
                        tvQsgmjbl.setTextColor(c2);
                        tvJyll.setTextColor(c2);
                        tvTrmc.setTextColor(c1);
                        tvTrzd.setTextColor(c1);
                        tvTrlshl.setTextColor(c1);
                        tvTchd.setTextColor(c1);
                        tvFzchd.setTextColor(c2);
                        tvKzlyhd.setTextColor(c2);
                        tvZblx.setTextColor(c2);
                        tvGmfgd.setTextColor(c2);
                        tvGmgd.setTextColor(c2);
                        tvCbfgd.setTextColor(c2);
                        tvCbgd.setTextColor(c2);
                        tvZbzfgd.setTextColor(c2);
                        //tvDl.setTextColor(c1);
                        tvTdqs.setTextColor(c1);
                        tvLmqs.setTextColor(c2);
                        tvSllb.setTextColor(c1);
                        tvGylsqdj.setTextColor(c2);
                        tvGylbhdj.setTextColor(c2);
                        tvSpljydj.setTextColor(c2);
                        tvFycs.setTextColor(c3);
                        tvLinzh.setTextColor(c3);
                        tvQy.setTextColor(c3);
                        tvYssz.setTextColor(c3);
                        tvPjnl.setTextColor(c3);
                        tvLingz.setTextColor(c3);
                        tvCq.setTextColor(c3);
                        tvPjxj.setTextColor(c3);
                        tvPjsg.setTextColor(c3);
                        tvYbd.setTextColor(c3);
                        tvSlqljg.setTextColor(c3);
                        //tvLcjg.setTextColor(c0);
                        tvSzjg.setTextColor(c3);
                        tvZrd.setTextColor(c3);
                        tvKjd.setTextColor(c3);
                        tvSlzhlx.setTextColor(c3);
                        tvSlzhdj.setTextColor(c3);
                        tvSljkdj.setTextColor(c3);
                        tvSpszs.setTextColor(c2);
                        tvZzzs.setTextColor(c2);
                        tvTrgxdj.setTextColor(c1);
                        tvDlmjdj.setTextColor(c1);
                        tvDlbhyy.setTextColor(c2);
                        tvYwtsdd.setTextColor(c1);
                        //tvDcrq.setTextColor(c0);
                        //tvF1.setTextColor(c1);
                        //tvF2.setTextColor(c1);
                        break;
                    }
                    case 173: {
                        tvDm.setTextColor(c1);
                        tvHb.setTextColor(c1);
                        tvPx.setTextColor(c1);
                        tvPw.setTextColor(c1);
                        tvPd.setTextColor(c1);
                        tvDbxt.setTextColor(c3);
                        tvSqgd.setTextColor(c3);
                        tvFshd.setTextColor(c3);
                        tvQsgmjbl.setTextColor(c3);
                        tvJyll.setTextColor(c3);
                        tvTrmc.setTextColor(c3);
                        tvTrzd.setTextColor(c3);
                        tvTrlshl.setTextColor(c3);
                        tvTchd.setTextColor(c3);
                        tvFzchd.setTextColor(c3);
                        tvKzlyhd.setTextColor(c3);
                        tvZblx.setTextColor(c3);
                        tvGmfgd.setTextColor(c3);
                        tvGmgd.setTextColor(c3);
                        tvCbfgd.setTextColor(c3);
                        tvCbgd.setTextColor(c3);
                        tvZbzfgd.setTextColor(c3);
                        //tvDl.setTextColor(c0);
                        tvTdqs.setTextColor(c0);
                        tvLmqs.setTextColor(c2);
                        tvSllb.setTextColor(c1);
                        tvGylsqdj.setTextColor(c2);
                        tvGylbhdj.setTextColor(c2);
                        tvSpljydj.setTextColor(c2);
                        tvFycs.setTextColor(c3);
                        tvLinzh.setTextColor(c3);
                        tvQy.setTextColor(c3);
                        tvYssz.setTextColor(c3);
                        tvPjnl.setTextColor(c3);
                        tvLingz.setTextColor(c3);
                        tvCq.setTextColor(c3);
                        tvPjxj.setTextColor(c3);
                        tvPjsg.setTextColor(c3);
                        tvYbd.setTextColor(c3);
                        tvSlqljg.setTextColor(c3);
                        //tvLcjg.setTextColor(c0);
                        tvSzjg.setTextColor(c3);
                        tvZrd.setTextColor(c3);
                        tvKjd.setTextColor(c3);
                        tvSlzhlx.setTextColor(c3);
                        tvSlzhdj.setTextColor(c3);
                        tvSljkdj.setTextColor(c3);
                        tvSpszs.setTextColor(c2);
                        tvZzzs.setTextColor(c2);
                        tvTrgxdj.setTextColor(c3);
                        tvDlmjdj.setTextColor(c1);
                        tvDlbhyy.setTextColor(c2);
                        tvYwtsdd.setTextColor(c1);
                        //tvDcrq.setTextColor(c0);
                        //tvF1.setTextColor(c1);
                        //tvF2.setTextColor(c1);
                        break;
                    }
                    case 210:
                    case 220: {
                        tvDm.setTextColor(c1);
                        tvHb.setTextColor(c1);
                        tvPx.setTextColor(c1);
                        tvPw.setTextColor(c1);
                        tvPd.setTextColor(c1);
                        tvDbxt.setTextColor(c3);
                        tvSqgd.setTextColor(c3);
                        tvFshd.setTextColor(c3);
                        tvQsgmjbl.setTextColor(c3);
                        tvJyll.setTextColor(c3);
                        tvTrmc.setTextColor(c3);
                        tvTrzd.setTextColor(c3);
                        tvTrlshl.setTextColor(c3);
                        tvTchd.setTextColor(c3);
                        tvFzchd.setTextColor(c3);
                        tvKzlyhd.setTextColor(c3);
                        tvZblx.setTextColor(c2);
                        tvGmfgd.setTextColor(c3);
                        tvGmgd.setTextColor(c3);
                        tvCbfgd.setTextColor(c3);
                        tvCbgd.setTextColor(c3);
                        tvZbzfgd.setTextColor(c3);
                        //tvDl.setTextColor(c0);
                        tvTdqs.setTextColor(c1);
                        tvLmqs.setTextColor(c2);
                        tvSllb.setTextColor(c3);
                        tvGylsqdj.setTextColor(c3);
                        tvGylbhdj.setTextColor(c3);
                        tvSpljydj.setTextColor(c3);
                        tvFycs.setTextColor(c3);
                        tvLinzh.setTextColor(c3);
                        tvQy.setTextColor(c3);
                        tvYssz.setTextColor(c3);
                        tvPjnl.setTextColor(c3);
                        tvLingz.setTextColor(c3);
                        tvCq.setTextColor(c3);
                        tvPjxj.setTextColor(c3);
                        tvPjsg.setTextColor(c3);
                        tvYbd.setTextColor(c3);
                        tvSlqljg.setTextColor(c3);
                        //tvLcjg.setTextColor(c0);
                        tvSzjg.setTextColor(c3);
                        tvZrd.setTextColor(c3);
                        tvKjd.setTextColor(c3);
                        tvSlzhlx.setTextColor(c3);
                        tvSlzhdj.setTextColor(c3);
                        tvSljkdj.setTextColor(c3);
                        tvSpszs.setTextColor(c2);
                        tvZzzs.setTextColor(c2);
                        tvTrgxdj.setTextColor(c3);
                        tvDlmjdj.setTextColor(c1);
                        tvDlbhyy.setTextColor(c2);
                        tvYwtsdd.setTextColor(c1);
                        //tvDcrq.setTextColor(c0);
                        //tvF1.setTextColor(c3);
                        //tvF2.setTextColor(c3);
                        break;
                    }
                    case 230:
                    case 240:
                    case 251:
                    case 252:
                    case 253:
                    case 254: {
                        tvDm.setTextColor(c1);
                        tvHb.setTextColor(c1);
                        tvPx.setTextColor(c1);
                        tvPw.setTextColor(c1);
                        tvPd.setTextColor(c1);
                        tvDbxt.setTextColor(c3);
                        tvSqgd.setTextColor(c3);
                        tvFshd.setTextColor(c3);
                        tvQsgmjbl.setTextColor(c3);
                        tvJyll.setTextColor(c3);
                        tvTrmc.setTextColor(c3);
                        tvTrzd.setTextColor(c3);
                        tvTrlshl.setTextColor(c3);
                        tvTchd.setTextColor(c3);
                        tvFzchd.setTextColor(c3);
                        tvKzlyhd.setTextColor(c3);
                        tvZblx.setTextColor(c3);
                        tvGmfgd.setTextColor(c3);
                        tvGmgd.setTextColor(c3);
                        tvCbfgd.setTextColor(c3);
                        tvCbgd.setTextColor(c3);
                        tvZbzfgd.setTextColor(c3);
                        //tvDl.setTextColor(c0);
                        tvTdqs.setTextColor(c1);
                        tvLmqs.setTextColor(c2);
                        tvSllb.setTextColor(c3);
                        tvGylsqdj.setTextColor(c3);
                        tvGylbhdj.setTextColor(c3);
                        tvSpljydj.setTextColor(c3);
                        tvFycs.setTextColor(c3);
                        tvLinzh.setTextColor(c3);
                        tvQy.setTextColor(c3);
                        tvYssz.setTextColor(c3);
                        tvPjnl.setTextColor(c3);
                        tvLingz.setTextColor(c3);
                        tvCq.setTextColor(c3);
                        tvPjxj.setTextColor(c3);
                        tvPjsg.setTextColor(c3);
                        tvYbd.setTextColor(c3);
                        tvSlqljg.setTextColor(c3);
                        //tvLcjg.setTextColor(c0);
                        tvSzjg.setTextColor(c3);
                        tvZrd.setTextColor(c3);
                        tvKjd.setTextColor(c3);
                        tvSlzhlx.setTextColor(c3);
                        tvSlzhdj.setTextColor(c3);
                        tvSljkdj.setTextColor(c3);
                        tvSpszs.setTextColor(c2);
                        tvZzzs.setTextColor(c2);
                        tvTrgxdj.setTextColor(c3);
                        tvDlmjdj.setTextColor(c1);
                        tvDlbhyy.setTextColor(c2);
                        tvYwtsdd.setTextColor(c1);
                        //tvDcrq.setTextColor(c0);
                        //tvF1.setTextColor(c3);
                        //tvF2.setTextColor(c3);
                        break;
                    }
                    default: {
                        tvDm.setTextColor(c0);
                        tvHb.setTextColor(c0);
                        tvPx.setTextColor(c0);
                        tvPw.setTextColor(c0);
                        tvPd.setTextColor(c0);
                        tvDbxt.setTextColor(c0);
                        tvSqgd.setTextColor(c0);
                        tvFshd.setTextColor(c0);
                        tvQsgmjbl.setTextColor(c0);
                        tvJyll.setTextColor(c0);
                        tvTrmc.setTextColor(c0);
                        tvTrzd.setTextColor(c0);
                        tvTrlshl.setTextColor(c0);
                        tvTchd.setTextColor(c0);
                        tvFzchd.setTextColor(c0);
                        tvKzlyhd.setTextColor(c0);
                        tvZblx.setTextColor(c0);
                        tvGmfgd.setTextColor(c0);
                        tvGmgd.setTextColor(c0);
                        tvCbfgd.setTextColor(c0);
                        tvCbgd.setTextColor(c0);
                        tvZbzfgd.setTextColor(c0);
                        //tvDl.setTextColor(c0);
                        tvTdqs.setTextColor(c0);
                        tvLmqs.setTextColor(c0);
                        tvSllb.setTextColor(c0);
                        tvGylsqdj.setTextColor(c0);
                        tvGylbhdj.setTextColor(c0);
                        tvSpljydj.setTextColor(c0);
                        tvFycs.setTextColor(c0);
                        tvLinzh.setTextColor(c0);
                        tvQy.setTextColor(c0);
                        tvYssz.setTextColor(c0);
                        tvPjnl.setTextColor(c0);
                        tvLingz.setTextColor(c0);
                        tvCq.setTextColor(c0);
                        tvPjxj.setTextColor(c0);
                        tvPjsg.setTextColor(c0);
                        tvYbd.setTextColor(c0);
                        tvSlqljg.setTextColor(c0);
                        //tvLcjg.setTextColor(c0);
                        tvSzjg.setTextColor(c0);
                        tvZrd.setTextColor(c0);
                        tvKjd.setTextColor(c0);
                        tvSlzhlx.setTextColor(c0);
                        tvSlzhdj.setTextColor(c0);
                        tvSljkdj.setTextColor(c0);
                        tvSpszs.setTextColor(c0);
                        tvZzzs.setTextColor(c0);
                        tvTrgxdj.setTextColor(c0);
                        tvDlmjdj.setTextColor(c0);
                        tvDlbhyy.setTextColor(c0);
                        tvYwtsdd.setTextColor(c0);
                        //tvDcrq.setTextColor(c0);
                        //tvF1.setTextColor(c0);
                        //tvF2.setTextColor(c0);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spLinzh.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 > 0) {
                    String s = spLinzh.getSelectedItem().toString();
                    int code = Resmgr.GetCodeByValue("linzh", s);
                    code /= 10;
                    spSllb.setSelection(Resmgr.GetPosByCode("sllb", code));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        qqYdh = (TextView) findViewById(R.id.qq_ydh);
        qqYdlb = (TextView) findViewById(R.id.qq_ydlb);
        qqZzb = (TextView) findViewById(R.id.qq_zzb);
        qqHzb = (TextView) findViewById(R.id.qq_hzb);
        qqGpszzb = (TextView) findViewById(R.id.qq_gpszzb);
        qqGpshzb = (TextView) findViewById(R.id.qq_gpshzb);
        qqXian = (TextView) findViewById(R.id.qq_xian);
        qqDm = (TextView) findViewById(R.id.qq_dm);
        qqHb = (TextView) findViewById(R.id.qq_hb);
        qqPx = (TextView) findViewById(R.id.qq_px);
        qqPw = (TextView) findViewById(R.id.qq_pw);
        qqPd = (TextView) findViewById(R.id.qq_pd);
        qqDbxt = (TextView) findViewById(R.id.qq_dbxt);
        qqSqgd = (TextView) findViewById(R.id.qq_sqgd);
        qqFshd = (TextView) findViewById(R.id.qq_fshd);
        qqQsgmjbl = (TextView) findViewById(R.id.qq_qsgmjbl);
        qqJyll = (TextView) findViewById(R.id.qq_jyll);
        qqTrmc = (TextView) findViewById(R.id.qq_trmc);
        qqTrzd = (TextView) findViewById(R.id.qq_trzd);
        qqTrlshl = (TextView) findViewById(R.id.qq_trlshl);
        qqTchd = (TextView) findViewById(R.id.qq_trhd);
        qqFzchd = (TextView) findViewById(R.id.qq_fzchd);
        qqKzlyhd = (TextView) findViewById(R.id.qq_kzlyhd);
        qqZblx = (TextView) findViewById(R.id.qq_zblx);
        qqGmfgd = (TextView) findViewById(R.id.qq_gmfgd);
        qqGmgd = (TextView) findViewById(R.id.qq_gmgd);
        qqCbfgd = (TextView) findViewById(R.id.qq_cbfgd);
        qqCbgd = (TextView) findViewById(R.id.qq_cbgd);
        qqZbzfgd = (TextView) findViewById(R.id.qq_zbzfgd);
        qqDl = (TextView) findViewById(R.id.qq_dl);
        qqTdqs = (TextView) findViewById(R.id.qq_tdqs);
        qqLmqs = (TextView) findViewById(R.id.qq_lmqs);
        qqSllb = (TextView) findViewById(R.id.qq_sllb);
        qqGylsqdj = (TextView) findViewById(R.id.qq_gylsqdj);
        qqGylbhdj = (TextView) findViewById(R.id.qq_gylbhdj);
        qqSpljydj = (TextView) findViewById(R.id.qq_spljydj);
        qqFycs = (TextView) findViewById(R.id.qq_fycs);
        qqLinzh = (TextView) findViewById(R.id.qq_linzh);
        qqQy = (TextView) findViewById(R.id.qq_qy);
        qqYssz = (TextView) findViewById(R.id.qq_yssz);
        qqPjnl = (TextView) findViewById(R.id.qq_pjnl);
        qqLingz = (TextView) findViewById(R.id.qq_lingz);
        qqCq = (TextView) findViewById(R.id.qq_cq);
        qqPjxj = (TextView) findViewById(R.id.qq_pjxj);
        qqPjsg = (TextView) findViewById(R.id.qq_pjsg);
        qqYbd = (TextView) findViewById(R.id.qq_ybd);
        qqSlqljg = (TextView) findViewById(R.id.qq_slqljg);
        qqLcjg = (TextView) findViewById(R.id.qq_lcjg);
        qqSzjg = (TextView) findViewById(R.id.qq_szjg);
        qqZrd = (TextView) findViewById(R.id.qq_zrd);
        qqKjd = (TextView) findViewById(R.id.qq_kjd);
        qqSlzhlx = (TextView) findViewById(R.id.qq_slzhlx);
        qqSlzhdj = (TextView) findViewById(R.id.qq_slzhdj);
        qqSljkdj = (TextView) findViewById(R.id.qq_sljkdj);
        qqSpszs = (TextView) findViewById(R.id.qq_spszs);
        qqZzzs = (TextView) findViewById(R.id.qq_zzzs);
        qqTrgxdj = (TextView) findViewById(R.id.qq_trgxdj);
        qqDlmjdj = (TextView) findViewById(R.id.qq_dlmjdj);
        qqDlbhyy = (TextView) findViewById(R.id.qq_dlbhyy);
        qqYwtsdd = (TextView) findViewById(R.id.qq_ywtsdd);
        qqDcrq = (TextView) findViewById(R.id.qq_dcrq);

        cbYdh = (CheckBox) findViewById(R.id.cb_ydh);
        cbYdlb = (CheckBox) findViewById(R.id.cb_ydlb);
        cbZzb = (CheckBox) findViewById(R.id.cb_zzb);
        cbHzb = (CheckBox) findViewById(R.id.cb_hzb);
        cbGpszzb = (CheckBox) findViewById(R.id.cb_gpszzb);
        cbGpshzb = (CheckBox) findViewById(R.id.cb_gpshzb);
        cbXian = (CheckBox) findViewById(R.id.cb_xian);
        cbDm = (CheckBox) findViewById(R.id.cb_dm);
        cbHb = (CheckBox) findViewById(R.id.cb_hb);
        cbPx = (CheckBox) findViewById(R.id.cb_px);
        cbPw = (CheckBox) findViewById(R.id.cb_pw);
        cbPd = (CheckBox) findViewById(R.id.cb_pd);
        cbDbxt = (CheckBox) findViewById(R.id.cb_dbxt);
        cbSqgd = (CheckBox) findViewById(R.id.cb_sqgd);
        cbFshd = (CheckBox) findViewById(R.id.cb_fshd);
        cbQsgmjbl = (CheckBox) findViewById(R.id.cb_qsgmjbl);
        cbJyll = (CheckBox) findViewById(R.id.cb_jyll);
        cbTrmc = (CheckBox) findViewById(R.id.cb_trmc);
        cbTrzd = (CheckBox) findViewById(R.id.cb_trzd);
        cbTrlshl = (CheckBox) findViewById(R.id.cb_trlshl);
        cbTchd = (CheckBox) findViewById(R.id.cb_trhd);
        cbFzchd = (CheckBox) findViewById(R.id.cb_fzchd);
        cbKzlyhd = (CheckBox) findViewById(R.id.cb_kzlyhd);
        cbZblx = (CheckBox) findViewById(R.id.cb_zblx);
        cbGmfgd = (CheckBox) findViewById(R.id.cb_gmfgd);
        cbGmgd = (CheckBox) findViewById(R.id.cb_gmgd);
        cbCbfgd = (CheckBox) findViewById(R.id.cb_cbfgd);
        cbCbgd = (CheckBox) findViewById(R.id.cb_cbgd);
        cbZbzfgd = (CheckBox) findViewById(R.id.cb_zbzfgd);
        cbDl = (CheckBox) findViewById(R.id.cb_dl);
        cbTdqs = (CheckBox) findViewById(R.id.cb_tdqs);
        cbLmqs = (CheckBox) findViewById(R.id.cb_lmqs);
        cbSllb = (CheckBox) findViewById(R.id.cb_sllb);
        cbGylsqdj = (CheckBox) findViewById(R.id.cb_gylsqdj);
        cbGylbhdj = (CheckBox) findViewById(R.id.cb_gylbhdj);
        cbSpljydj = (CheckBox) findViewById(R.id.cb_spljydj);
        cbFycs = (CheckBox) findViewById(R.id.cb_fycs);
        cbLinzh = (CheckBox) findViewById(R.id.cb_linzh);
        cbQy = (CheckBox) findViewById(R.id.cb_qy);
        cbYssz = (CheckBox) findViewById(R.id.cb_yssz);
        cbPjnl = (CheckBox) findViewById(R.id.cb_pjnl);
        cbLingz = (CheckBox) findViewById(R.id.cb_lingz);
        cbCq = (CheckBox) findViewById(R.id.cb_cq);
        cbPjxj = (CheckBox) findViewById(R.id.cb_pjxj);
        cbPjsg = (CheckBox) findViewById(R.id.cb_pjsg);
        cbYbd = (CheckBox) findViewById(R.id.cb_ybd);
        cbSlqljg = (CheckBox) findViewById(R.id.cb_slqljg);
        cbLcjg = (CheckBox) findViewById(R.id.cb_lcjg);
        cbSzjg = (CheckBox) findViewById(R.id.cb_szjg);
        cbZrd = (CheckBox) findViewById(R.id.cb_zrd);
        cbKjd = (CheckBox) findViewById(R.id.cb_kjd);
        cbSlzhlx = (CheckBox) findViewById(R.id.cb_slzhlx);
        cbSlzhdj = (CheckBox) findViewById(R.id.cb_slzhdj);
        cbSljkdj = (CheckBox) findViewById(R.id.cb_sljkdj);
        cbSpszs = (CheckBox) findViewById(R.id.cb_spszs);
        cbZzzs = (CheckBox) findViewById(R.id.cb_zzzs);
        cbTrgxdj = (CheckBox) findViewById(R.id.cb_trgxdj);
        cbDlmjdj = (CheckBox) findViewById(R.id.cb_dlmjdj);
        cbDlbhyy = (CheckBox) findViewById(R.id.cb_dlbhyy);
        cbYwtsdd = (CheckBox) findViewById(R.id.cb_ywtsdd);
        cbDcrq = (CheckBox) findViewById(R.id.cb_dcrq);

        cbAll = (CheckBox) findViewById(R.id.cb_all);
        cbAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                //cbYdh.setChecked(arg1);
                //cbYdlb.setChecked(arg1);
                //cbZzb.setChecked(arg1);
                //cbHzb.setChecked(arg1);
                //cbGpszzb.setChecked(arg1);
                //cbGpshzb.setChecked(arg1);
                //cbXian.setChecked(arg1);
                //cbDm.setChecked(arg1);
                //cbHb.setChecked(arg1);
                //cbPx.setChecked(arg1);
                //cbPw.setChecked(arg1);
                //cbPd.setChecked(arg1);
                cbDbxt.setChecked(arg1);
                cbSqgd.setChecked(arg1);
                cbFshd.setChecked(arg1);
                cbQsgmjbl.setChecked(arg1);
                cbJyll.setChecked(arg1);
                cbTrmc.setChecked(arg1);
                cbTrzd.setChecked(arg1);
                cbTrlshl.setChecked(arg1);
                cbTchd.setChecked(arg1);
                cbFzchd.setChecked(arg1);
                cbKzlyhd.setChecked(arg1);
                cbZblx.setChecked(arg1);
                //cbGmfgd.setChecked(arg1);
                //cbGmgd.setChecked(arg1);
                //cbCbfgd.setChecked(arg1);
                //cbCbgd.setChecked(arg1);
                //cbZbzfgd.setChecked(arg1);
                cbDl.setChecked(arg1);
                cbTdqs.setChecked(arg1);
                cbLmqs.setChecked(arg1);
                cbSllb.setChecked(arg1);
                cbGylsqdj.setChecked(arg1);
                cbGylbhdj.setChecked(arg1);
                cbSpljydj.setChecked(arg1);
                cbFycs.setChecked(arg1);
                cbLinzh.setChecked(arg1);
                cbQy.setChecked(arg1);
                //cbYssz.setChecked(arg1);
                cbPjnl.setChecked(arg1);
                cbLingz.setChecked(arg1);
                cbCq.setChecked(arg1);
                //cbPjxj.setChecked(arg1);
                //cbPjsg.setChecked(arg1);
                //cbYbd.setChecked(arg1);
                cbSlqljg.setChecked(arg1);
                //cbLcjg.setChecked(arg1);
                cbSzjg.setChecked(arg1);
                cbZrd.setChecked(arg1);
                cbKjd.setChecked(arg1);
                cbSlzhlx.setChecked(arg1);
                cbSlzhdj.setChecked(arg1);
                cbSljkdj.setChecked(arg1);
                cbSpszs.setChecked(arg1);
                cbZzzs.setChecked(arg1);
                cbTrgxdj.setChecked(arg1);
                cbDlmjdj.setChecked(arg1);
                //cbDlbhyy.setChecked(arg1);
                cbYwtsdd.setChecked(arg1);
                //cbDcrq.setChecked(arg1);
            }
        });

        setQqData();
        setData();
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
                    save();
                    boolean b = check();
                    if (iStatus != 2) {
                        iStatus = 1;
                    } else if (!b) {
                        iStatus = 1;
                    }
                    String sql = "update dczt set ydyz = '" + iStatus + "' where ydh = '" + ydh + "'";
                    YangdiMgr.ExecSQL(ydh, sql);
                    SlqcYdyz.this.finish();
                }
            });
            builder.setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    SlqcYdyz.this.finish();
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
            case R.id.btn_copy: {
                List<String> items = new ArrayList<String>();
                items.add("覆盖已录入数据");
                items.add("跳过已录入数据");
                MyListDialog dlg = new MyListDialog(this, "选项", items);
                int r = dlg.showDialog();
                if (r == -1) break;
                if (cbDbxt.isChecked()) {
                    if (r == 0) {
                        spDbxt.setSelection(Resmgr.GetPosByValue("dbxt", qqDbxt.getText().toString()));
                    } else if (r == 1 && spDbxt.getSelectedItemPosition() == 0) {
                        spDbxt.setSelection(Resmgr.GetPosByValue("dbxt", qqDbxt.getText().toString()));
                    }
                }
                if (cbSqgd.isChecked()) {
                    if (r == 0) {
                        etSqgd.setText(qqSqgd.getText().toString());
                    } else if (r == 1 && etSqgd.getText().toString().equals("")) {
                        etSqgd.setText(qqSqgd.getText().toString());
                    }
                }
                if (cbFshd.isChecked()) {
                    if (r == 0) {
                        etFshd.setText(qqFshd.getText().toString());
                    } else if (r == 1 && etFshd.getText().toString().equals("")) {
                        etFshd.setText(qqFshd.getText().toString());
                    }
                }
                if (cbQsgmjbl.isChecked()) {
                    if (r == 0) {
                        etQsgmjbl.setText(qqQsgmjbl.getText().toString());
                    } else if (r == 1 && etQsgmjbl.getText().toString().equals("")) {
                        etQsgmjbl.setText(qqQsgmjbl.getText().toString());
                    }
                }
                if (cbJyll.isChecked()) {
                    if (r == 0) {
                        etJyll.setText(qqJyll.getText().toString());
                    } else if (r == 1 && etJyll.getText().toString().equals("")) {
                        etJyll.setText(qqJyll.getText().toString());
                    }
                }
                if (cbTrmc.isChecked()) {
                    if (r == 0) {
                        spTrmc.setSelection(Resmgr.GetPosByValue("trmc", qqTrmc.getText().toString()));
                    } else if (r == 1 && spTrmc.getSelectedItemPosition() == 0) {
                        spTrmc.setSelection(Resmgr.GetPosByValue("trmc", qqTrmc.getText().toString()));
                    }
                }
                if (cbTrzd.isChecked()) {
                    if (r == 0) {
                        spTrzd.setSelection(Resmgr.GetPosByValue("trzd", qqTrzd.getText().toString()));
                    } else if (r == 1 && spTrzd.getSelectedItemPosition() == 0) {
                        spTrzd.setSelection(Resmgr.GetPosByValue("trzd", qqTrzd.getText().toString()));
                    }
                }
                if (cbTrlshl.isChecked()) {
                    if (r == 0) {
                        etTrlshl.setText(qqTrlshl.getText().toString());
                    } else if (r == 1 && etTrlshl.getText().toString().equals("")) {
                        etTrlshl.setText(qqTrlshl.getText().toString());
                    }
                }
                if (cbTchd.isChecked()) {
                    if (r == 0) {
                        etTchd.setText(qqTchd.getText().toString());
                    } else if (r == 1 && etTchd.getText().toString().equals("")) {
                        etTchd.setText(qqTchd.getText().toString());
                    }
                }
                if (cbFzchd.isChecked()) {
                    if (r == 0) {
                        etFzchd.setText(qqFzchd.getText().toString());
                    } else if (r == 1 && etFzchd.getText().toString().equals("")) {
                        etFzchd.setText(qqFzchd.getText().toString());
                    }
                }
                if (cbKzlyhd.isChecked()) {
                    if (r == 0) {
                        etKzlyhd.setText(qqKzlyhd.getText().toString());
                    } else if (r == 1 && etKzlyhd.getText().toString().equals("")) {
                        etKzlyhd.setText(qqKzlyhd.getText().toString());
                    }
                }
                if (cbZblx.isChecked()) {
                    if (r == 0) {
                        spZblx.setSelection(Resmgr.GetPosByValue("zblx", qqZblx.getText().toString()));
                    } else if (r == 1 && spZblx.getSelectedItemPosition() == 0) {
                        spZblx.setSelection(Resmgr.GetPosByValue("zblx", qqZblx.getText().toString()));
                    }
                }
            /*
	        if(cbGmfgd.isChecked())
			{
				if(r == 0)
				{
					etGmfgd.setText(qqGmfgd.getText().toString());
				}
				else if(r == 1 && etGmfgd.getText().toString().equals(""))
				{
					etGmfgd.setText(qqGmfgd.getText().toString());
				}
			}
	        if(cbGmgd.isChecked())
			{
				if(r == 0)
				{
					etGmgd.setText(qqGmgd.getText().toString());
				}
				else if(r == 1 && etGmgd.getText().toString().equals(""))
				{
					etGmgd.setText(qqGmgd.getText().toString());
				}
			}
	        if(cbCbfgd.isChecked())
			{
				if(r == 0)
				{
					etCbfgd.setText(qqCbfgd.getText().toString());
				}
				else if(r == 1 && etCbfgd.getText().toString().equals(""))
				{
					etCbfgd.setText(qqCbfgd.getText().toString());
				}
			}
	        if(cbCbgd.isChecked())
			{
				if(r == 0)
				{
					etCbgd.setText(qqCbgd.getText().toString());
				}
				else if(r == 1 && etCbgd.getText().toString().equals(""))
				{
					etCbgd.setText(qqCbgd.getText().toString());
				}
			}
	        if(cbZbzfgd.isChecked())
			{
				if(r == 0)
				{
					etZbzfgd.setText(qqZbzfgd.getText().toString());
				}
				else if(r == 1 && etZbzfgd.getText().toString().equals(""))
				{
					etZbzfgd.setText(qqZbzfgd.getText().toString());
				}
			}
			*/
                if (cbDl.isChecked()) {
                    if (r == 0) {
                        spDl.setSelection(Resmgr.GetPosByValue("dl", qqDl.getText().toString()));
                    } else if (r == 1 && spDl.getSelectedItemPosition() == 0) {
                        spDl.setSelection(Resmgr.GetPosByValue("dl", qqDl.getText().toString()));
                    }
                }
                if (cbTdqs.isChecked()) {
                    if (r == 0) {
                        spTdqs.setSelection(Resmgr.GetPosByValue("tdqs", qqTdqs.getText().toString()));
                    } else if (r == 1 && spTdqs.getSelectedItemPosition() == 0) {
                        spTdqs.setSelection(Resmgr.GetPosByValue("tdqs", qqTdqs.getText().toString()));
                    }
                }
                if (cbLmqs.isChecked()) {
                    if (r == 0) {
                        spLmqs.setSelection(Resmgr.GetPosByValue("lmqs", qqLmqs.getText().toString()));
                    } else if (r == 1 && spLmqs.getSelectedItemPosition() == 0) {
                        spLmqs.setSelection(Resmgr.GetPosByValue("lmqs", qqLmqs.getText().toString()));
                    }
                }
                if (cbSllb.isChecked()) {
                    if (r == 0) {
                        spSllb.setSelection(Resmgr.GetPosByValue("sllb", qqSllb.getText().toString()));
                    } else if (r == 1 && spSllb.getSelectedItemPosition() == 0) {
                        spSllb.setSelection(Resmgr.GetPosByValue("sllb", qqSllb.getText().toString()));
                    }
                }
                if (cbGylsqdj.isChecked()) {
                    if (r == 0) {
                        spGylsqdj.setSelection(Resmgr.GetPosByValue("gylsqdj", qqGylsqdj.getText().toString()));
                    } else if (r == 1 && spGylsqdj.getSelectedItemPosition() == 0) {
                        spGylsqdj.setSelection(Resmgr.GetPosByValue("gylsqdj", qqGylsqdj.getText().toString()));
                    }
                }
                if (cbGylbhdj.isChecked()) {
                    if (r == 0) {
                        spGylbhdj.setSelection(Resmgr.GetPosByValue("gylbhdj", qqGylbhdj.getText().toString()));
                    } else if (r == 1 && spGylbhdj.getSelectedItemPosition() == 0) {
                        spGylbhdj.setSelection(Resmgr.GetPosByValue("gylbhdj", qqGylbhdj.getText().toString()));
                    }
                }
                if (cbSpljydj.isChecked()) {
                    if (r == 0) {
                        spSpljydj.setSelection(Resmgr.GetPosByValue("spljydj", qqSpljydj.getText().toString()));
                    } else if (r == 1 && spSpljydj.getSelectedItemPosition() == 0) {
                        spSpljydj.setSelection(Resmgr.GetPosByValue("spljydj", qqSpljydj.getText().toString()));
                    }
                }
                if (cbFycs.isChecked()) {
                    if (r == 0) {
                        spFycs.setSelection(Resmgr.GetPosByValue("fycs", qqFycs.getText().toString()));
                    } else if (r == 1 && spFycs.getSelectedItemPosition() == 0) {
                        spFycs.setSelection(Resmgr.GetPosByValue("fycs", qqFycs.getText().toString()));
                    }
                }
                if (cbLinzh.isChecked()) {
                    if (r == 0) {
                        spLinzh.setSelection(Resmgr.GetPosByValue("linzh", qqLinzh.getText().toString()));
                    } else if (r == 1 && spLinzh.getSelectedItemPosition() == 0) {
                        spLinzh.setSelection(Resmgr.GetPosByValue("linzh", qqLinzh.getText().toString()));
                    }
                }
                if (cbQy.isChecked()) {
                    if (r == 0) {
                        spQy.setSelection(Resmgr.GetPosByValue("qy", qqQy.getText().toString()));
                    } else if (r == 1 && spQy.getSelectedItemPosition() == 0) {
                        spQy.setSelection(Resmgr.GetPosByValue("qy", qqQy.getText().toString()));
                    }
                }
	        /*
	        if(cbYssz.isChecked())
			{
				if(r == 0)
				{
					etYssz.setText(qqYssz.getText().toString());
				}
				else if(r == 1 && etYssz.getText().toString().equals(""))
				{
					etYssz.setText(qqYssz.getText().toString());
				}
			}
			*/
                if (cbPjnl.isChecked()) {
                    if (r == 0) {
                        int nl = -1;
                        try {
                            nl = Integer.parseInt(qqPjnl.getText().toString());
                        } catch (Exception e) {
                        }
                        if (nl >= 0) etPjnl.setText(String.valueOf(nl + 5));
                    } else if (r == 1 && etPjnl.getText().toString().equals("")) {
                        int nl = -1;
                        try {
                            nl = Integer.parseInt(qqPjnl.getText().toString());
                        } catch (Exception e) {
                        }
                        if (nl >= 0) etPjnl.setText(String.valueOf(nl + 5));
                    }
                }
                if (cbLingz.isChecked()) {
                    if (r == 0) {
                        spLingz.setSelection(Resmgr.GetPosByValue("lingz", qqLingz.getText().toString()));
                    } else if (r == 1 && spLingz.getSelectedItemPosition() == 0) {
                        spLingz.setSelection(Resmgr.GetPosByValue("lingz", qqLingz.getText().toString()));
                    }
                }
                if (cbCq.isChecked()) {
                    if (r == 0) {
                        spCq.setSelection(Resmgr.GetPosByValue("cq", qqCq.getText().toString()));
                    } else if (r == 1 && spCq.getSelectedItemPosition() == 0) {
                        spCq.setSelection(Resmgr.GetPosByValue("cq", qqCq.getText().toString()));
                    }
                }
	        /*
	        if(cbPjxj.isChecked())
			{
				if(r == 0)
				{
					etPjxj.setText(qqPjxj.getText().toString());
				}
				else if(r == 1 && etPjxj.getText().toString().equals(""))
				{
					etPjxj.setText(qqPjxj.getText().toString());
				}
			}
	        if(cbPjsg.isChecked())
			{
				if(r == 0)
				{
					etPjsg.setText(qqPjsg.getText().toString());
				}
				else if(r == 1 && etPjsg.getText().toString().equals(""))
				{
					etPjsg.setText(qqPjsg.getText().toString());
				}
			}
	        if(cbYbd.isChecked())
			{
				if(r == 0)
				{
					etYbd.setText(qqYbd.getText().toString());
				}
				else if(r == 1 && etYbd.getText().toString().equals(""))
				{
					etYbd.setText(qqYbd.getText().toString());
				}
			}
			*/
                if (cbSlqljg.isChecked()) {
                    if (r == 0) {
                        spSlqljg.setSelection(Resmgr.GetPosByValue("slqljg", qqSlqljg.getText().toString()));
                    } else if (r == 1 && spSlqljg.getSelectedItemPosition() == 0) {
                        spSlqljg.setSelection(Resmgr.GetPosByValue("slqljg", qqSlqljg.getText().toString()));
                    }
                }
                if (cbSzjg.isChecked()) {
                    if (r == 0) {
                        spSzjg.setSelection(Resmgr.GetPosByValue("szjg", qqSzjg.getText().toString()));
                    } else if (r == 1 && spSzjg.getSelectedItemPosition() == 0) {
                        spSzjg.setSelection(Resmgr.GetPosByValue("szjg", qqSzjg.getText().toString()));
                    }
                }
                if (cbZrd.isChecked()) {
                    if (r == 0) {
                        spZrd.setSelection(Resmgr.GetPosByValue("zrd", qqZrd.getText().toString()));
                    } else if (r == 1 && spZrd.getSelectedItemPosition() == 0) {
                        spZrd.setSelection(Resmgr.GetPosByValue("zrd", qqZrd.getText().toString()));
                    }
                }
                if (cbKjd.isChecked()) {
                    if (r == 0) {
                        spKjd.setSelection(Resmgr.GetPosByValue("kjd", qqKjd.getText().toString()));
                    } else if (r == 1 && spKjd.getSelectedItemPosition() == 0) {
                        spKjd.setSelection(Resmgr.GetPosByValue("kjd", qqKjd.getText().toString()));
                    }
                }
                if (cbSlzhlx.isChecked()) {
                    if (r == 0) {
                        spSlzhlx.setSelection(Resmgr.GetPosByValue("slzhlx", qqSlzhlx.getText().toString()));
                    } else if (r == 1 && spSlzhlx.getSelectedItemPosition() == 0) {
                        spSlzhlx.setSelection(Resmgr.GetPosByValue("slzhlx", qqSlzhlx.getText().toString()));
                    }
                }
                if (cbSlzhdj.isChecked()) {
                    if (r == 0) {
                        spSlzhdj.setSelection(Resmgr.GetPosByValue("slzhdj", qqSlzhdj.getText().toString()));
                    } else if (r == 1 && spSlzhdj.getSelectedItemPosition() == 0) {
                        spSlzhdj.setSelection(Resmgr.GetPosByValue("slzhdj", qqSlzhdj.getText().toString()));
                    }
                }
                if (cbSljkdj.isChecked()) {
                    if (r == 0) {
                        spSljkdj.setSelection(Resmgr.GetPosByValue("sljkdj", qqSljkdj.getText().toString()));
                    } else if (r == 1 && spSljkdj.getSelectedItemPosition() == 0) {
                        spSljkdj.setSelection(Resmgr.GetPosByValue("sljkdj", qqSljkdj.getText().toString()));
                    }
                }
                if (cbSpszs.isChecked()) {
                    if (r == 0) {
                        etSpszs.setText(qqSpszs.getText().toString());
                    } else if (r == 1 && etSpszs.getText().toString().equals("")) {
                        etSpszs.setText(qqSpszs.getText().toString());
                    }
                }
                if (cbZzzs.isChecked()) {
                    if (r == 0) {
                        etZzzs.setText(qqZzzs.getText().toString());
                    } else if (r == 1 && etZzzs.getText().toString().equals("")) {
                        etZzzs.setText(qqZzzs.getText().toString());
                    }
                }
                if (cbTrgxdj.isChecked()) {
                    if (r == 0) {
                        spTrgxdj.setSelection(Resmgr.GetPosByValue("trgxdj", qqTrgxdj.getText().toString()));
                    } else if (r == 1 && spTrgxdj.getSelectedItemPosition() == 0) {
                        spTrgxdj.setSelection(Resmgr.GetPosByValue("trgxdj", qqTrgxdj.getText().toString()));
                    }
                }
                if (cbDlmjdj.isChecked()) {
                    if (r == 0) {
                        spDlmjdj.setSelection(Resmgr.GetPosByValue("dlmjdj", qqDlmjdj.getText().toString()));
                    } else if (r == 1 && spDlmjdj.getSelectedItemPosition() == 0) {
                        spDlmjdj.setSelection(Resmgr.GetPosByValue("dlmjdj", qqDlmjdj.getText().toString()));
                    }
                }
	        /*
	        if(cbDlbhyy.isChecked())
			{
				if(r == 0)
				{
					spDlbhyy.setSelection(Resmgr.GetPosByValue("dlbhyy", qqDlbhyy.getText().toString()));
				}
				else if(r == 1 && spDlbhyy.getSelectedItemPosition() == 0)
				{
					spDlbhyy.setSelection(Resmgr.GetPosByValue("dlbhyy", qqDlbhyy.getText().toString()));
				}
			}
			*/
                if (cbYwtsdd.isChecked()) {
                    if (r == 0) {
                        spYwtsdd.setSelection(Resmgr.GetPosByValue("ywtsdd", qqYwtsdd.getText().toString()));
                    } else if (r == 1 && spYwtsdd.getSelectedItemPosition() == 0) {
                        spYwtsdd.setSelection(Resmgr.GetPosByValue("ywtsdd", qqYwtsdd.getText().toString()));
                    }
                }
                cbAll.setChecked(false);
                cbDbxt.setChecked(false);
                cbSqgd.setChecked(false);
                cbFshd.setChecked(false);
                cbQsgmjbl.setChecked(false);
                cbJyll.setChecked(false);
                cbTrmc.setChecked(false);
                cbTrzd.setChecked(false);
                cbTrlshl.setChecked(false);
                cbTchd.setChecked(false);
                cbFzchd.setChecked(false);
                cbKzlyhd.setChecked(false);
                cbZblx.setChecked(false);
                //cbGmfgd.setChecked(false);
                //cbGmgd.setChecked(false);
                //cbCbfgd.setChecked(false);
                //cbCbgd.setChecked(false);
                //cbZbzfgd.setChecked(false);
                cbDl.setChecked(false);
                cbTdqs.setChecked(false);
                cbLmqs.setChecked(false);
                cbSllb.setChecked(false);
                cbGylsqdj.setChecked(false);
                cbGylbhdj.setChecked(false);
                cbSpljydj.setChecked(false);
                cbFycs.setChecked(false);
                cbLinzh.setChecked(false);
                cbQy.setChecked(false);
                //cbYssz.setChecked(false);
                cbPjnl.setChecked(false);
                cbLingz.setChecked(false);
                cbCq.setChecked(false);
                //cbPjxj.setChecked(false);
                //cbPjsg.setChecked(false);
                //cbYbd.setChecked(false);
                cbSlqljg.setChecked(false);
                cbSzjg.setChecked(false);
                cbZrd.setChecked(false);
                cbKjd.setChecked(false);
                cbSlzhlx.setChecked(false);
                cbSlzhdj.setChecked(false);
                cbSljkdj.setChecked(false);
                cbSpszs.setChecked(false);
                cbZzzs.setChecked(false);
                cbTrgxdj.setChecked(false);
                cbDlmjdj.setChecked(false);
                //cbDlbhyy.setChecked(false);
                cbYwtsdd.setChecked(false);
                save();
                break;
            }
            case R.id.btn_coord: {
                MyCoord c = YangdiMgr.GetYdloc(ydh);
                if (c == null) {
                    Toast.makeText(this, "尚未测定样地坐标！", 1).show();
                    break;
                }
                int zone = MyConfig.GetParamsZone();
                int lon0 = MyConfig.GetParamsLon0();
                double dx = MyConfig.GetParamsDx();
                double dy = MyConfig.GetParamsDy();
                double dz = MyConfig.GetParamsDz();
                double rx = MyConfig.GetParamsRx();
                double ry = MyConfig.GetParamsRy();
                double rz = MyConfig.GetParamsRz();
                double k = MyConfig.GetParamsK();
                MyCoord c1 = CoordTransform.GeoToKjzj_wgs84(c);
                MyCoord c2 = CoordTransform.Transform(c1, -dx, -dy, -dz, rx, ry, rz, k);
                MyCoord c3 = CoordTransform.KjzjToGeo_bj54(c2);
                MyCoord c4 = CoordTransform.GeoToGauss_bj54(c3, lon0);
                if (zone == 6) {
                    c.x = c4.x + MyMap.Get6Daihao(lon0) * 1000000;
                } else if (zone == 3) {
                    c.x = c4.x + MyMap.Get3Daihao(lon0) * 1000000;
                }
                c.y = c4.y;
                int x = 0;
                int y = 0;
                try {
                    //x = Integer.parseInt(etGpshzb.getText().toString());
                    //y = Integer.parseInt(etGpszzb.getText().toString());
                    x = Integer.parseInt(qqGpshzb.getText().toString());
                    y = Integer.parseInt(qqGpszzb.getText().toString());
                } catch (Exception e) {
                }
                if (x <= 0 || y <= 0) {
                    etGpshzb.setText(String.valueOf((int) c.x));
                    etGpszzb.setText(String.valueOf((int) c.y));
                    String sql = "update qt set qq_hzb = '" + x + "', qq_zzb = '" + y + "' where ydh = '" + ydh + "'";
                    YangdiMgr.ExecSQL(ydh, sql);
                } else {
                    double dis = Math.sqrt((x - c.x) * (x - c.x) + (y - c.y) * (y - c.y));
                    dis = MyFuns.MyDecimal(dis, 2);
                    MyMakeSureDialog dlg = new MyMakeSureDialog(this, "提示", "本次测定坐标与前期GPS坐标相差 " + dis + " 米，是否继续替换原坐标？\n相差15米以内建议使用原坐标。", "替换", "取消");
                    if (dlg.showDialog()) {
                        etGpshzb.setText(String.valueOf((int) c.x));
                        etGpszzb.setText(String.valueOf((int) c.y));
                        String sql = "update qt set qq_hzb = '" + x + "', qq_zzb = '" + y + "' where ydh = '" + ydh + "'";
                        YangdiMgr.ExecSQL(ydh, sql);
                    }
                }
                break;
            }
            case R.id.btn_reset: {
                MyMakeSureDialog dlg = new MyMakeSureDialog(this, "提示", "确定要重置为前期GPS坐标吗？", "确定", "取消");
                if (dlg.showDialog()) {
                    etGpshzb.setText(qqGpshzb.getText());
                    etGpszzb.setText(qqGpszzb.getText());
                    String sql = "update qt set qq_hzb = '', qq_zzb = '' where ydh = '" + ydh + "'";
                    YangdiMgr.ExecSQL(ydh, sql);
                }
                break;
            }
            case R.id.btn_lingz: {
                PdbzDialog dlg = new PdbzDialog(this, PdbzDialog.TYPE_LINGZ);
                dlg.show();
                break;
            }
            case R.id.btn_slqljg: {
                PdbzDialog dlg = new PdbzDialog(this, PdbzDialog.TYPE_SLQLJG);
                dlg.show();
                break;
            }
            case R.id.btn_yssz_reset: {
                MyMakeSureDialog dlg = new MyMakeSureDialog(this, "提示", "确定要删除当前‘优势树种’数据吗?", "删除", "保留");
                if (dlg.showDialog()) {
                    YangdiMgr.SetYssz(ydh, "");
                    etYssz.setText("");
                    MyMakeSureDialog dlg2 = new MyMakeSureDialog(this, "提示", "是否同时删除‘平均胸径’和‘平均树高’数据?", "删除", "保留");
                    if (dlg2.showDialog()) {
                        etPjxj.setText("");
                        etPjsg.setText("");
                        save();
                    }
                }
                break;
            }
            case R.id.btn_pjxj_reset: {
                MyMakeSureDialog dlg = new MyMakeSureDialog(this, "提示", "确定要删除当前‘平均胸径’数据吗?", "删除", "保留");
                if (dlg.showDialog()) {
                    etPjxj.setText("");
                    save();
                }
                break;
            }
            case R.id.btn_yssz: {
                save();
                String sdl = spDl.getSelectedItem().toString();
                int dl = Resmgr.GetCodeByValue("dl", sdl);
                if (dl <= 0) {
                    Toast.makeText(this, "请先填写样地地类！", 1).show();
                    break;
                }
                if (!YangdiMgr.IsNeedYssz(dl)) {
                    Toast.makeText(this, "此地类不需要填写优势树种！", 1).show();
                    break;
                }
                int all = YangdiMgr.GetYangmuCount(ydh);
                int pass = YangdiMgr.GetPassYangmuCount(ydh);
                if (pass < all) {
                    MyMakeSureDialog dlg = new MyMakeSureDialog(this, "警告", "每木检尺调查尚未完成，是否继续设置优势树种?", "确定", "取消");
                    if (!dlg.showDialog()) {
                        break;
                    }
                }
                YsszDialog szDlg = new YsszDialog(this, ydh);
                String str = szDlg.showDialog();
                if (str != null) {
                    if (str.equals("")) {
                        MyMakeSureDialog dlg = new MyMakeSureDialog(this, "提示", "优势树种被清除，是否清除‘平均胸径’和‘平均树高’?", "清除", "保留");
                        if (dlg.showDialog()) {
                            etYssz.setText("");
                            etPjxj.setText("");
                            etPjsg.setText("");
                        } else {
                            etYssz.setText("");
                        }
                    } else {
                        etYssz.setText(str);
                        float xj = YangdiMgr.GetPjxj(ydh);
                        if (xj > 0) etPjxj.setText(String.valueOf(xj));
                        else etPjxj.setText("");
                    }
                }
                break;
            }
            case R.id.btn_check: {
                save();
                boolean b = check();
                if (iStatus != 2) {
                    iStatus = 1;
                } else if (!b) {
                    iStatus = 1;
                }
                String sql = "update dczt set ydyz = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);

                CheckYdyzDialog dlg = new CheckYdyzDialog(this, ydh);
                dlg.show();
                break;
            }
            case R.id.btn_save: {
                save();
                boolean b = check();
                if (iStatus != 2) {
                    iStatus = 1;
                } else if (!b) {
                    iStatus = 1;
                }
                String sql = "update dczt set ydyz = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                break;
            }
            case R.id.btn_finish: {
                save();
                boolean b = check();
                if (b) {
                    iStatus = 2;
                } else {
                    iStatus = 1;
                }
                String sql = "update dczt set ydyz = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                finish();
                break;
            }
        }
    }

    private void setQqData() {
        qqYdh.setText(String.valueOf(ydh));

        String[] ss = YangdiMgr.GetQqYdyz(ydh);
        if (ss != null) {
            try {
                int ydlb = Integer.parseInt(ss[1]);
                qqYdlb.setText(Resmgr.GetValueByCode("ydlb", ydlb));
            } catch (Exception e) {
            }
            qqZzb.setText(ss[2]);
            qqHzb.setText(ss[3]);
            qqGpszzb.setText(ss[4]);
            qqGpshzb.setText(ss[5]);
            qqXian.setText(ss[6]);
            try {
                int dm = Integer.parseInt(ss[7]);
                qqDm.setText(Resmgr.GetValueByCode("dm", dm));
            } catch (Exception e) {
            }
            qqHb.setText(ss[8]);
            try {
                int px = Integer.parseInt(ss[9]);
                qqPx.setText(Resmgr.GetValueByCode("px", px));
            } catch (Exception e) {
            }
            try {
                int pw = Integer.parseInt(ss[10]);
                qqPw.setText(Resmgr.GetValueByCode("pw", pw));
            } catch (Exception e) {
            }
            qqPd.setText(ss[11]);
            try {
                int dbxt = Integer.parseInt(ss[12]);
                qqDbxt.setText(Resmgr.GetValueByCode("dbxt", dbxt));
            } catch (Exception e) {
            }
            try {
                if (!ss[13].equals("")) {
                    float sqgd = Float.parseFloat(ss[13]);
                    sqgd /= 10;
                    qqSqgd.setText(String.valueOf(sqgd));
                }
            } catch (Exception e) {
            }
            qqFshd.setText(ss[14]);
            qqQsgmjbl.setText(ss[15]);
            qqJyll.setText(ss[16]);
            try {
                int trmc = Integer.parseInt(ss[17]);
                qqTrmc.setText(Resmgr.GetValueByCode("trmc", trmc));
            } catch (Exception e) {
            }
            try {
                int trzd = Integer.parseInt(ss[18]);
                qqTrzd.setText(Resmgr.GetValueByCode("trzd", trzd));
            } catch (Exception e) {
            }
            qqTrlshl.setText(ss[19]);
            qqTchd.setText(ss[20]);
            qqFzchd.setText(ss[21]);
            qqKzlyhd.setText(ss[22]);
            try {
                int zblx = Integer.parseInt(ss[23]);
                qqZblx.setText(Resmgr.GetValueByCode("zblx", zblx));
            } catch (Exception e) {
            }
            qqGmfgd.setText(ss[24]);
            try {
                if (!ss[25].equals("")) {
                    float gmgd = Float.parseFloat(ss[25]);
                    gmgd /= 10;
                    qqGmgd.setText(String.valueOf(gmgd));
                }
            } catch (Exception e) {
            }
            qqCbfgd.setText(ss[26]);
            try {
                if (!ss[27].equals("")) {
                    float cbgd = Float.parseFloat(ss[27]);
                    cbgd /= 10;
                    qqCbgd.setText(String.valueOf(cbgd));
                }
            } catch (Exception e) {
            }
            qqZbzfgd.setText(ss[28]);
            try {
                int dl = Integer.parseInt(ss[29]);
                //int dl = YangdiMgr.GetQqdl(ydh);
                qqDl.setText(Resmgr.GetValueByCode("dl", dl));
            } catch (Exception e) {
            }
            try {
                int tdqs = Integer.parseInt(ss[30]);
                qqTdqs.setText(Resmgr.GetValueByCode("tdqs", tdqs));
            } catch (Exception e) {
            }
            try {
                int lmqs = Integer.parseInt(ss[31]);
                qqLmqs.setText(Resmgr.GetValueByCode("lmqs", lmqs));
            } catch (Exception e) {
            }
            try {
                int sllb = Integer.parseInt(ss[32]);
                qqSllb.setText(Resmgr.GetValueByCode("sllb", sllb));
            } catch (Exception e) {
            }
            try {
                int gylsqdj = Integer.parseInt(ss[33]);
                qqGylsqdj.setText(Resmgr.GetValueByCode("gylsqdj", gylsqdj));
            } catch (Exception e) {
            }
            try {
                int gylbhdj = Integer.parseInt(ss[34]);
                qqGylbhdj.setText(Resmgr.GetValueByCode("gylbhdj", gylbhdj));
            } catch (Exception e) {
            }
            try {
                int spljydj = Integer.parseInt(ss[35]);
                qqSpljydj.setText(Resmgr.GetValueByCode("spljydj", spljydj));
            } catch (Exception e) {
            }
            try {
                int fycs = Integer.parseInt(ss[36]);
                qqFycs.setText(Resmgr.GetValueByCode("fycs", fycs));
            } catch (Exception e) {
            }
            try {
                int linzh = Integer.parseInt(ss[37]);
                qqLinzh.setText(Resmgr.GetValueByCode("linzh", linzh));
            } catch (Exception e) {
            }
            try {
                int qy = Integer.parseInt(ss[38]);
                qqQy.setText(Resmgr.GetValueByCode("qy", qy));
            } catch (Exception e) {
            }
            try {
                int code = Integer.parseInt(ss[39]);
                String name = Resmgr.GetSzName(code);
                String yssz = "";
                if (code > 0 && !name.equals("")) {
                    yssz = code + " " + name;
                } else {
                    yssz = "";
                }
                qqYssz.setText(yssz);
            } catch (Exception e) {
            }
            qqPjnl.setText(ss[40]);
            try {
                int lingz = Integer.parseInt(ss[41]);
                qqLingz.setText(Resmgr.GetValueByCode("lingz", lingz));
            } catch (Exception e) {
            }
            try {
                int cq = Integer.parseInt(ss[42]);
                qqCq.setText(Resmgr.GetValueByCode("cq", cq));
            } catch (Exception e) {
            }
            try {
                if (!ss[43].equals("")) {
                    float pjxj = Float.parseFloat(ss[43]);
                    pjxj /= 10;
                    qqPjxj.setText(String.valueOf(pjxj));
                }
            } catch (Exception e) {
            }
            try {
                if (!ss[44].equals("")) {
                    float pjsg = Float.parseFloat(ss[44]);
                    pjsg /= 10;
                    qqPjsg.setText(String.valueOf(pjsg));
                }
            } catch (Exception e) {
            }
            try {
                if (!ss[45].equals("")) {
                    float ybd = Float.parseFloat(ss[45]);
                    ybd /= 100;
                    qqYbd.setText(String.valueOf(ybd));
                }
            } catch (Exception e) {
            }
            try {
                int slqljg = Integer.parseInt(ss[46]);
                qqSlqljg.setText(Resmgr.GetValueByCode("slqljg", slqljg));
            } catch (Exception e) {
            }
			/*
			try{
				int lcjg = Integer.parseInt(ss[47]);
				qqLcjg.setText(Resmgr.GetValueByCode("ydlcjg", lcjg));
			}catch(Exception e){}
			*/
            try {
                int szjg = Integer.parseInt(ss[48]);
                qqSzjg.setText(Resmgr.GetValueByCode("szjg", szjg));
            } catch (Exception e) {
            }
            try {
                int zrd = Integer.parseInt(ss[49]);
                qqZrd.setText(Resmgr.GetValueByCode("zrd", zrd));
            } catch (Exception e) {
            }
            try {
                int kjd = Integer.parseInt(ss[50]);
                qqKjd.setText(Resmgr.GetValueByCode("kjd", kjd));
            } catch (Exception e) {
            }
            try {
                int slzhlx = Integer.parseInt(ss[51]);
                qqSlzhlx.setText(Resmgr.GetValueByCode("slzhlx", slzhlx));
            } catch (Exception e) {
            }
            try {
                int slzhdj = Integer.parseInt(ss[52]);
                qqSlzhdj.setText(Resmgr.GetValueByCode("slzhdj", slzhdj));
            } catch (Exception e) {
            }
            try {
                int sljkdj = Integer.parseInt(ss[53]);
                qqSljkdj.setText(Resmgr.GetValueByCode("sljkdj", sljkdj));
            } catch (Exception e) {
            }
            qqSpszs.setText(ss[54]);
            qqZzzs.setText(ss[55]);
            try {
                int trgxdj = Integer.parseInt(ss[56]);
                qqTrgxdj.setText(Resmgr.GetValueByCode("trgxdj", trgxdj));
            } catch (Exception e) {
            }
            try {
                int dlmjdj = Integer.parseInt(ss[57]);
                qqDlmjdj.setText(Resmgr.GetValueByCode("dlmjdj", dlmjdj));
            } catch (Exception e) {
            }
            try {
                int dlbhyy = Integer.parseInt(ss[58]);
                qqDlbhyy.setText(Resmgr.GetValueByCode("dlbhyy", dlbhyy));
            } catch (Exception e) {
            }
            try {
                int ywtsdd = Integer.parseInt(ss[59]);
                qqYwtsdd.setText(Resmgr.GetValueByCode("ywtsdd", ywtsdd));
            } catch (Exception e) {
            }
            //qqDcrq.setText(ss[60]);
        }
    }

    private void setData() {
        etYdh.setText("");
        etXian.setText("");
        etZzb.setText("");
        etHzb.setText("");
        etGpszzb.setText("");
        etGpshzb.setText("");
        etHb.setText("");
        etPd.setText("");
        etSqgd.setText("");
        etFshd.setText("");
        etQsgmjbl.setText("");
        etJyll.setText("");
        etTrlshl.setText("");
        etTchd.setText("");
        etFzchd.setText("");
        etKzlyhd.setText("");
        etGmfgd.setText("");
        etGmgd.setText("");
        etCbfgd.setText("");
        etCbgd.setText("");
        etZbzfgd.setText("");
        etPjnl.setText("");
        etPjxj.setText("");
        etPjsg.setText("");
        etYbd.setText("");
        etSpszs.setText("");
        etZzzs.setText("");
        etDcrq.setText("");
        etYssz.setText("");
        spYdlb.setSelection(0);
        spDm.setSelection(0);
        spPx.setSelection(0);
        spPw.setSelection(0);
        spDbxt.setSelection(0);
        spTrmc.setSelection(0);
        spTrzd.setSelection(0);
        spZblx.setSelection(0);
        spDl.setSelection(0);
        spTdqs.setSelection(0);
        spLmqs.setSelection(0);
        spSllb.setSelection(0);
        spGylsqdj.setSelection(0);
        spGylbhdj.setSelection(0);
        spSpljydj.setSelection(0);
        spFycs.setSelection(0);
        spLinzh.setSelection(0);
        spQy.setSelection(0);
        spLingz.setSelection(0);
        spCq.setSelection(0);
        spSlqljg.setSelection(0);
        spLcjg.setSelection(1);
        spSzjg.setSelection(0);
        spZrd.setSelection(0);
        spKjd.setSelection(0);
        spSlzhlx.setSelection(0);
        spSlzhdj.setSelection(0);
        spSljkdj.setSelection(0);
        spTrgxdj.setSelection(0);
        spDlmjdj.setSelection(0);
        spDlbhyy.setSelection(0);
        spYwtsdd.setSelection(0);
        //spF1.setSelection(0);
        //spF2.setSelection(0);

        int dl = -1;

        etYdh.setText(String.valueOf(ydh));

        String sql = "select * from ydyz where ydh = '" + ydh + "'";
        String[][] sss = YangdiMgr.SelectData(ydh, sql);
        if (sss != null) {
            String[] ss = sss[0];
            try {
                int ydlb = Integer.parseInt(ss[1]);
                spYdlb.setSelection(Resmgr.GetPosByCode("ydlb", ydlb));
            } catch (Exception e) {
            }
            etZzb.setText(ss[2]);
            etHzb.setText(ss[3]);
            etGpszzb.setText(ss[4]);
            etGpshzb.setText(ss[5]);
            etXian.setText(ss[6]);
            try {
                int dm = Integer.parseInt(ss[7]);
                spDm.setSelection(Resmgr.GetPosByCode("dm", dm));
            } catch (Exception e) {
            }
            if (ss[8].equals("-1")) etHb.setText("");
            else etHb.setText(ss[8]);
            try {
                int px = Integer.parseInt(ss[9]);
                spPx.setSelection(Resmgr.GetPosByCode("px", px));
            } catch (Exception e) {
            }
            try {
                int pw = Integer.parseInt(ss[10]);
                spPw.setSelection(Resmgr.GetPosByCode("pw", pw));
            } catch (Exception e) {
            }
            if (ss[11].equals("-1")) etPd.setText("");
            else etPd.setText(ss[11]);
            try {
                int dbxt = Integer.parseInt(ss[12]);
                spDbxt.setSelection(Resmgr.GetPosByCode("dbxt", dbxt));
            } catch (Exception e) {
            }
            if (ss[13].equals("-1")) etSqgd.setText("");
            else etSqgd.setText(ss[13]);
            if (ss[14].equals("-1")) etFshd.setText("");
            else etFshd.setText(ss[14]);
            if (ss[15].equals("-1")) etQsgmjbl.setText("");
            else etQsgmjbl.setText(ss[15]);
            if (ss[16].equals("-1")) etJyll.setText("");
            else etJyll.setText(ss[16]);
            try {
                int trmc = Integer.parseInt(ss[17]);
                spTrmc.setSelection(Resmgr.GetPosByCode("trmc", trmc));
            } catch (Exception e) {
            }
            try {
                int trzd = Integer.parseInt(ss[18]);
                spTrzd.setSelection(Resmgr.GetPosByCode("trzd", trzd));
            } catch (Exception e) {
            }
            if (ss[19].equals("-1")) etTrlshl.setText("");
            else etTrlshl.setText(ss[19]);
            if (ss[20].equals("-1")) etTchd.setText("");
            else etTchd.setText(ss[20]);
            if (ss[21].equals("-1")) etFzchd.setText("");
            else etFzchd.setText(ss[21]);
            if (ss[22].equals("-1")) etKzlyhd.setText("");
            else etKzlyhd.setText(ss[22]);
            try {
                int zblx = Integer.parseInt(ss[23]);
                spZblx.setSelection(Resmgr.GetPosByCode("zblx", zblx));
            } catch (Exception e) {
            }
            if (ss[24].equals("-1")) etGmfgd.setText("");
            else etGmfgd.setText(ss[24]);
            if (ss[25].equals("-1")) etGmgd.setText("");
            else etGmgd.setText(ss[25]);
            if (ss[26].equals("-1")) etCbfgd.setText("");
            else etCbfgd.setText(ss[26]);
            if (ss[27].equals("-1")) etCbgd.setText("");
            else etCbgd.setText(ss[27]);
            if (ss[28].equals("-1")) etZbzfgd.setText("");
            else etZbzfgd.setText(ss[28]);
            try {
                dl = Integer.parseInt(ss[29]);
                spDl.setSelection(Resmgr.GetPosByCode("dl", dl));
            } catch (Exception e) {
            }
            try {
                int tdqs = Integer.parseInt(ss[30]);
                spTdqs.setSelection(Resmgr.GetPosByCode("tdqs", tdqs));
            } catch (Exception e) {
            }
            try {
                int lmqs = Integer.parseInt(ss[31]);
                spLmqs.setSelection(Resmgr.GetPosByCode("lmqs", lmqs));
            } catch (Exception e) {
            }
            try {
                int sllb = Integer.parseInt(ss[32]);
                spSllb.setSelection(Resmgr.GetPosByCode("sllb", sllb));
            } catch (Exception e) {
            }
            try {
                int gylsqdj = Integer.parseInt(ss[33]);
                spGylsqdj.setSelection(Resmgr.GetPosByCode("gylsqdj", gylsqdj));
            } catch (Exception e) {
            }
            try {
                int gylbhdj = Integer.parseInt(ss[34]);
                spGylbhdj.setSelection(Resmgr.GetPosByCode("gylbhdj", gylbhdj));
            } catch (Exception e) {
            }
            try {
                int spljydj = Integer.parseInt(ss[35]);
                spSpljydj.setSelection(Resmgr.GetPosByCode("spljydj", spljydj));
            } catch (Exception e) {
            }
            try {
                int fycs = Integer.parseInt(ss[36]);
                spFycs.setSelection(Resmgr.GetPosByCode("fycs", fycs));
            } catch (Exception e) {
            }
            try {
                int linzh = Integer.parseInt(ss[37]);
                spLinzh.setSelection(Resmgr.GetPosByCode("linzh", linzh));
            } catch (Exception e) {
            }
            try {
                int qy = Integer.parseInt(ss[38]);
                spQy.setSelection(Resmgr.GetPosByCode("qy", qy));
            } catch (Exception e) {
            }
            try {
                int yssz = Integer.parseInt(ss[39]);
                if (yssz > 0) etYssz.setText(yssz + " " + Resmgr.GetSzName(yssz));
            } catch (Exception e) {
            }
            if (ss[40].equals("-1")) etPjnl.setText("");
            else etPjnl.setText(ss[40]);
            try {
                int lingz = Integer.parseInt(ss[41]);
                spLingz.setSelection(Resmgr.GetPosByCode("lingz", lingz));
            } catch (Exception e) {
            }
            try {
                int cq = Integer.parseInt(ss[42]);
                spCq.setSelection(Resmgr.GetPosByCode("cq", cq));
            } catch (Exception e) {
            }
            if (ss[43].equals("-1")) etPjxj.setText("");
            else etPjxj.setText(ss[43]);
            if (ss[44].equals("-1")) etPjsg.setText("");
            else etPjsg.setText(ss[44]);
            if (ss[45].equals("-1")) etYbd.setText("");
            else etYbd.setText(ss[45]);
            try {
                int slqljg = Integer.parseInt(ss[46]);
                spSlqljg.setSelection(Resmgr.GetPosByCode("slqljg", slqljg));
            } catch (Exception e) {
            }
            try {
                int lcjg = Integer.parseInt(ss[47]);
                spLcjg.setSelection(Resmgr.GetPosByCode("ydlcjg", lcjg));
            } catch (Exception e) {
            }
            try {
                int szjg = Integer.parseInt(ss[48]);
                spSzjg.setSelection(Resmgr.GetPosByCode("szjg", szjg));
            } catch (Exception e) {
            }
            try {
                int zrd = Integer.parseInt(ss[49]);
                spZrd.setSelection(Resmgr.GetPosByCode("zrd", zrd));
            } catch (Exception e) {
            }
            try {
                int kjd = Integer.parseInt(ss[50]);
                spKjd.setSelection(Resmgr.GetPosByCode("kjd", kjd));
            } catch (Exception e) {
            }
            try {
                int slzhlx = Integer.parseInt(ss[51]);
                spSlzhlx.setSelection(Resmgr.GetPosByCode("slzhlx", slzhlx));
            } catch (Exception e) {
            }
            try {
                int slzhdj = Integer.parseInt(ss[52]);
                spSlzhdj.setSelection(Resmgr.GetPosByCode("slzhdj", slzhdj));
            } catch (Exception e) {
            }
            try {
                int sljkdj = Integer.parseInt(ss[53]);
                spSljkdj.setSelection(Resmgr.GetPosByCode("sljkdj", sljkdj));
            } catch (Exception e) {
            }
            if (ss[54].equals("-1")) etSpszs.setText("");
            else etSpszs.setText(ss[54]);
            if (ss[55].equals("-1")) etZzzs.setText("");
            else etZzzs.setText(ss[55]);
            try {
                int trgxdj = Integer.parseInt(ss[56]);
                spTrgxdj.setSelection(Resmgr.GetPosByCode("trgxdj", trgxdj));
            } catch (Exception e) {
            }
            try {
                int dlmjdj = Integer.parseInt(ss[57]);
                spDlmjdj.setSelection(Resmgr.GetPosByCode("dlmjdj", dlmjdj));
            } catch (Exception e) {
            }
            try {
                int dlbhyy = Integer.parseInt(ss[58]);
                spDlbhyy.setSelection(Resmgr.GetPosByCode("dlbhyy", dlbhyy));
            } catch (Exception e) {
            }
            try {
                int ywtsdd = Integer.parseInt(ss[59]);
                spYwtsdd.setSelection(Resmgr.GetPosByCode("ywtsdd", ywtsdd));
            } catch (Exception e) {
            }
            etDcrq.setText(ss[60]);
			/*
			try{
				int f1 = Integer.parseInt(ss[61]);
				spF1.setSelection(Resmgr.GetPosByCode("sfntjjl", f1));
			}catch(Exception e){}
			try{
				int f2 = Integer.parseInt(ss[62]);
				spF2.setSelection(Resmgr.GetPosByCode("sffldsl", f2));
			}catch(Exception e){}
			*/
        }

        int all = YangdiMgr.GetYangmuCount(ydh);
        int pass = YangdiMgr.GetPassYangmuCount(ydh);
        if (YangdiMgr.IsNeedYssz(dl) && etYssz.getText().toString().trim().equals("") && all == pass) {
            int yssz = YangdiMgr.GetDefaultYssz(ydh);
            if (yssz > 0) {
                etYssz.setText(yssz + " " + Resmgr.GetSzName(yssz));
                YangdiMgr.SetYssz(ydh, String.valueOf(yssz));
            }
            float xj = YangdiMgr.GetPjxj(ydh);
            if (xj > 0) etPjxj.setText(String.valueOf(xj));
        }
		/*
		if(etZzzs.getText().toString().trim().equals(""))
		{
			int n = YangdiMgr.GetZzCount(ydh);
			etZzzs.setText(String.valueOf(n));
		}

		if(etSpszs.getText().toString().trim().equals(""))
		{
			int n = YangdiMgr.GetSpsCount(ydh);
			etSpszs.setText(String.valueOf(n));
		}
		*/
        if (spSlzhlx.getSelectedItemPosition() == 0) {
            List<Slzh> lst = YangdiMgr.GetSlzhList(ydh);
            if (lst.size() > 0) {
                int dj = 0;
                int lx = 0;
                for (int i = 0; i < lst.size(); i++) {
                    if (lst.get(i).szdj > dj) {
                        dj = lst.get(i).szdj;
                        lx = lst.get(i).zhlx;
                    }
                }
                spSlzhlx.setSelection(Resmgr.GetPosByCode("slzhlx", lx));
                spSlzhdj.setSelection(Resmgr.GetPosByCode("slzhdj", dj));
                spSljkdj.setSelection(Resmgr.GetPosByCode("sljkdj", dj + 1));
            }
        }

        if (spTrgxdj.getSelectedItemPosition() == 0) {
            List<Trgx> lst = YangdiMgr.GetTrgxList(ydh);
            if (lst.size() > 0) {
                int dj = 0;
                for (int i = 0; i < lst.size(); i++) {
                    int tmp = 0;
                    if (lst.get(i).zs1 >= 5000 || lst.get(i).zs2 >= 3000 || lst.get(i).zs3 >= 2500) {
                        tmp = 1;
                    } else if ((lst.get(i).zs1 >= 3000 && lst.get(i).zs1 <= 4999)
                            || (lst.get(i).zs2 >= 1000 && lst.get(i).zs2 <= 2999)
                            || (lst.get(i).zs3 >= 500 && lst.get(i).zs3 <= 2499)) {
                        tmp = 2;
                    } else {
                        tmp = 3;
                    }
                    if (tmp > dj) {
                        dj = tmp;
                    }
                }
                spTrgxdj.setSelection(Resmgr.GetPosByCode("trgxdj", dj));
            }
        }

        if (spSzjg.getSelectedItemPosition() == 0 && YangdiMgr.IsNeedSzjg(dl) && all == pass) {
            int code = YangdiMgr.GetDefaultSzjg(ydh);
            spSzjg.setSelection(Resmgr.GetPosByCode("szjg", code));
        }

        sql = "select dcrq from kpfm where ydh = '" + ydh + "'";
        String dcrq = YangdiMgr.QueryString(ydh, sql);
        if (dcrq != null) {
            etDcrq.setText(dcrq);
        }

        YDInfo yd = Setmgr.GetTask(ydh);
        if (etZzb.getText().toString().trim().equals("")) etZzb.setText(String.valueOf(yd.zzb));
        if (etHzb.getText().toString().trim().equals("")) etHzb.setText(String.valueOf(yd.hzb));
        if (etGpszzb.getText().toString().trim().equals(""))
            etGpszzb.setText(String.valueOf(yd.gpszzb));
        if (etGpshzb.getText().toString().trim().equals(""))
            etGpshzb.setText(String.valueOf(yd.gpshzb));
        if (etXian.getText().toString().trim().equals("")) etXian.setText(String.valueOf(yd.xian));
        if (etHb.getText().toString().trim().equals("")) etHb.setText(String.valueOf(yd.hb));
        if (etPd.getText().toString().trim().equals("")) etPd.setText(String.valueOf(yd.pd));
        if (spYdlb.getSelectedItemPosition() == 0)
            spYdlb.setSelection(Resmgr.GetPosByCode("ydlb", yd.ydlb));
        if (spDm.getSelectedItemPosition() == 0)
            spDm.setSelection(Resmgr.GetPosByCode("dm", yd.dm));
        if (spPx.getSelectedItemPosition() == 0)
            spPx.setSelection(Resmgr.GetPosByCode("px", yd.px));
        if (spPw.getSelectedItemPosition() == 0)
            spPw.setSelection(Resmgr.GetPosByCode("pw", yd.pw));
    }

    private void save() {
        String ydlb = spYdlb.getSelectedItem().toString();
        String y = etZzb.getText().toString();
        String x = etHzb.getText().toString();
        String gps_y = etGpszzb.getText().toString();
        String gps_x = etGpshzb.getText().toString();
        String xian = etXian.getText().toString();
        String dm = spDm.getSelectedItem().toString();
        String hb = etHb.getText().toString();
        String px = spPx.getSelectedItem().toString();
        String pw = spPw.getSelectedItem().toString();
        String pd = etPd.getText().toString();
        String dbxt = spDbxt.getSelectedItem().toString();
        String sqgd = etSqgd.getText().toString();
        String fshd = etFshd.getText().toString();
        String qsgmjbl = etQsgmjbl.getText().toString();
        String jyll = etJyll.getText().toString();
        String trmc = spTrmc.getSelectedItem().toString();
        String trzd = spTrzd.getSelectedItem().toString();
        String trlshl = etTrlshl.getText().toString();
        String tchd = etTchd.getText().toString();
        String fzchd = etFzchd.getText().toString();
        String kzlyhd = etKzlyhd.getText().toString();
        String zblx = spZblx.getSelectedItem().toString();
        String gmfgd = etGmfgd.getText().toString();
        String gmgd = etGmgd.getText().toString();
        String cbfgd = etCbfgd.getText().toString();
        String cbgd = etCbgd.getText().toString();
        String zbzfgd = etZbzfgd.getText().toString();
        String dl = spDl.getSelectedItem().toString();
        String tdqs = spTdqs.getSelectedItem().toString();
        String lmqs = spLmqs.getSelectedItem().toString();
        String sllb = spSllb.getSelectedItem().toString();
        String gylsqdj = spGylsqdj.getSelectedItem().toString();
        String gylbhdj = spGylbhdj.getSelectedItem().toString();
        String spljydj = spSpljydj.getSelectedItem().toString();
        String fycs = spFycs.getSelectedItem().toString();
        String linzh = spLinzh.getSelectedItem().toString();
        String qy = spQy.getSelectedItem().toString();
        String yssz = etYssz.getText().toString();
        String pjnl = etPjnl.getText().toString();
        String lingz = spLingz.getSelectedItem().toString();
        String cq = spCq.getSelectedItem().toString();
        String pjxj = etPjxj.getText().toString();
        String pjsg = etPjsg.getText().toString();
        String ybd = etYbd.getText().toString();
        String slqljg = spSlqljg.getSelectedItem().toString();
        String lcjg = spLcjg.getSelectedItem().toString();
        String szjg = spSzjg.getSelectedItem().toString();
        String zrd = spZrd.getSelectedItem().toString();
        String kjd = spKjd.getSelectedItem().toString();
        String slzhlx = spSlzhlx.getSelectedItem().toString();
        String slzhdj = spSlzhdj.getSelectedItem().toString();
        String sljkdj = spSljkdj.getSelectedItem().toString();
        String spszs = etSpszs.getText().toString();
        String zzzs = etZzzs.getText().toString();
        String trgxdj = spTrgxdj.getSelectedItem().toString();
        String dlmjdj = spDlmjdj.getSelectedItem().toString();
        String dlbhyy = spDlbhyy.getSelectedItem().toString();
        String ywtsdd = spYwtsdd.getSelectedItem().toString();
        String dcrq = etDcrq.getText().toString();
        //String f1 = spF1.getSelectedItem().toString();
        //String f2 = spF2.getSelectedItem().toString();

        int i_y = -1;
        try {
            i_y = Integer.parseInt(y);
        } catch (NumberFormatException nfe) {
        }
        int i_x = -1;
        try {
            i_x = Integer.parseInt(x);
        } catch (NumberFormatException nfe) {
        }
        int i_gps_y = -1;
        try {
            i_gps_y = (int) Double.parseDouble(gps_y);
        } catch (NumberFormatException nfe) {
        }
        int i_gps_x = -1;
        try {
            i_gps_x = (int) Double.parseDouble(gps_x);
        } catch (NumberFormatException nfe) {
        }
        int i_xian = -1;
        try {
            i_xian = Integer.parseInt(xian);
        } catch (NumberFormatException nfe) {
        }
        int i_hb = -1;
        try {
            i_hb = Integer.parseInt(hb);
        } catch (NumberFormatException nfe) {
        }
        int i_pd = -1;
        try {
            i_pd = Integer.parseInt(pd);
        } catch (NumberFormatException nfe) {
        }
        float f_sqgd = -1;
        try {
            f_sqgd = Float.parseFloat(sqgd);
        } catch (NumberFormatException nfe) {
        }
        int i_fshd = -1;
        try {
            i_fshd = Integer.parseInt(fshd);
        } catch (NumberFormatException nfe) {
        }
        int i_qsgmjbl = -1;
        try {
            i_qsgmjbl = Integer.parseInt(qsgmjbl);
        } catch (NumberFormatException nfe) {
        }
        int i_jyll = -1;
        try {
            i_jyll = Integer.parseInt(jyll);
        } catch (NumberFormatException nfe) {
        }
        int i_trlshl = -1;
        try {
            i_trlshl = Integer.parseInt(trlshl);
        } catch (NumberFormatException nfe) {
        }
        int i_tchd = -1;
        try {
            i_tchd = Integer.parseInt(tchd);
        } catch (NumberFormatException nfe) {
        }
        int i_fzchd = -1;
        try {
            i_fzchd = Integer.parseInt(fzchd);
        } catch (NumberFormatException nfe) {
        }
        int i_kzlyhd = -1;
        try {
            i_kzlyhd = Integer.parseInt(kzlyhd);
        } catch (NumberFormatException nfe) {
        }
        int i_gmfgd = -1;
        try {
            i_gmfgd = Integer.parseInt(gmfgd);
        } catch (NumberFormatException nfe) {
        }
        float f_gmgd = -1;
        try {
            f_gmgd = Float.parseFloat(gmgd);
        } catch (NumberFormatException nfe) {
        }
        int i_cbfgd = -1;
        try {
            i_cbfgd = Integer.parseInt(cbfgd);
        } catch (NumberFormatException nfe) {
        }
        float f_cbgd = -1;
        try {
            f_cbgd = Float.parseFloat(cbgd);
        } catch (NumberFormatException nfe) {
        }
        int i_zbzfgd = -1;
        try {
            i_zbzfgd = Integer.parseInt(zbzfgd);
        } catch (NumberFormatException nfe) {
        }
        int i_pjnl = -1;
        try {
            i_pjnl = Integer.parseInt(pjnl);
        } catch (NumberFormatException nfe) {
        }
        float f_pjxj = -1;
        try {
            f_pjxj = Float.parseFloat(pjxj);
        } catch (NumberFormatException nfe) {
        }
        float f_pjsg = -1;
        try {
            f_pjsg = Float.parseFloat(pjsg);
        } catch (NumberFormatException nfe) {
        }
        float f_ybd = -1;
        try {
            f_ybd = Float.parseFloat(ybd);
        } catch (NumberFormatException nfe) {
        }
        int i_spszs = -1;
        try {
            i_spszs = Integer.parseInt(spszs);
        } catch (NumberFormatException nfe) {
        }
        int i_zzzs = -1;
        try {
            i_zzzs = Integer.parseInt(zzzs);
        } catch (NumberFormatException nfe) {
        }

        int szdm = -1;
        if (!yssz.equals("")) {
            String[] ss = yssz.split(" ");
            String szmc = null;
            if (ss.length == 1) {
                try {
                    szdm = Integer.parseInt(yssz);
                } catch (Exception e) {
                }
                if (szdm > 0) {
                    szmc = Resmgr.GetSzName(szdm);
                    if (szmc.equals("")) {
                        szdm = -1;
                    }
                } else {
                    szmc = yssz;
                    szdm = Resmgr.GetSzCode(szmc);
                }
            } else if (ss.length == 2) {
                try {
                    szdm = Integer.parseInt(ss[0]);
                } catch (Exception e) {
                }
                szmc = ss[1];
                if (szdm != Resmgr.GetSzCode(szmc)) {
                    szdm = -1;
                }
            } else {
                szdm = -1;
            }
        }

        int i_ydlb = Resmgr.GetCodeByValue("ydlb", ydlb);
        int i_dm = Resmgr.GetCodeByValue("dm", dm);
        int i_px = Resmgr.GetCodeByValue("px", px);
        int i_pw = Resmgr.GetCodeByValue("pw", pw);
        int i_dbxt = Resmgr.GetCodeByValue("dbxt", dbxt);
        int i_trmc = Resmgr.GetCodeByValue("trmc", trmc);
        int i_trzd = Resmgr.GetCodeByValue("trzd", trzd);
        int i_zblx = Resmgr.GetCodeByValue("zblx", zblx);
        int i_dl = Resmgr.GetCodeByValue("dl", dl);
        int i_tdqs = Resmgr.GetCodeByValue("tdqs", tdqs);
        int i_lmqs = Resmgr.GetCodeByValue("lmqs", lmqs);
        int i_sllb = Resmgr.GetCodeByValue("sllb", sllb);
        int i_gylsqdj = Resmgr.GetCodeByValue("gylsqdj", gylsqdj);
        int i_gylbhdj = Resmgr.GetCodeByValue("gylbhdj", gylbhdj);
        int i_spljydj = Resmgr.GetCodeByValue("spljydj", spljydj);
        int i_fycs = Resmgr.GetCodeByValue("fycs", fycs);
        int i_linzh = Resmgr.GetCodeByValue("linzh", linzh);
        int i_qy = Resmgr.GetCodeByValue("qy", qy);
        int i_lingz = Resmgr.GetCodeByValue("lingz", lingz);
        int i_cq = Resmgr.GetCodeByValue("cq", cq);
        int i_slqljg = Resmgr.GetCodeByValue("slqljg", slqljg);
        int i_lcjg = Resmgr.GetCodeByValue("ydlcjg", lcjg);
        int i_szjg = Resmgr.GetCodeByValue("szjg", szjg);
        int i_zrd = Resmgr.GetCodeByValue("zrd", zrd);
        int i_kjd = Resmgr.GetCodeByValue("kjd", kjd);
        int i_slzhlx = Resmgr.GetCodeByValue("slzhlx", slzhlx);
        int i_slzhdj = Resmgr.GetCodeByValue("slzhdj", slzhdj);
        int i_sljkdj = Resmgr.GetCodeByValue("sljkdj", sljkdj);
        int i_trgxdj = Resmgr.GetCodeByValue("trgxdj", trgxdj);
        int i_dlmjdj = Resmgr.GetCodeByValue("dlmjdj", dlmjdj);
        int i_dlbhyy = Resmgr.GetCodeByValue("dlbhyy", dlbhyy);
        int i_ywtsdd = Resmgr.GetCodeByValue("ywtsdd", ywtsdd);
        //int i_f1 = Resmgr.GetCodeByValue("sfntjjl", f1);
        //int i_f2 = Resmgr.GetCodeByValue("sffldsl", f2);

        String s_ydlb = String.valueOf(i_ydlb);
        String s_zzb = String.valueOf(i_y);
        String s_hzb = String.valueOf(i_x);
        String s_gpszzb = String.valueOf(i_gps_y);
        String s_gpshzb = String.valueOf(i_gps_x);
        String s_xian = String.valueOf(i_xian);
        String s_dm = String.valueOf(i_dm);
        String s_hb = String.valueOf(i_hb);
        String s_px = String.valueOf(i_px);
        String s_pw = String.valueOf(i_pw);
        String s_pd = String.valueOf(i_pd);
        String s_dbxt = String.valueOf(i_dbxt);
        String s_sqgd = String.valueOf(f_sqgd);
        String s_fshd = String.valueOf(i_fshd);
        String s_qsgmjbl = String.valueOf(i_qsgmjbl);
        String s_jyll = String.valueOf(i_jyll);
        String s_trmc = String.valueOf(i_trmc);
        String s_trzd = String.valueOf(i_trzd);
        String s_trlshl = String.valueOf(i_trlshl);
        String s_tchd = String.valueOf(i_tchd);
        String s_fzchd = String.valueOf(i_fzchd);
        String s_kzlyhd = String.valueOf(i_kzlyhd);
        String s_zblx = String.valueOf(i_zblx);
        String s_gmfgd = String.valueOf(i_gmfgd);
        String s_gmgd = String.valueOf(f_gmgd);
        String s_cbfgd = String.valueOf(i_cbfgd);
        String s_cbgd = String.valueOf(f_cbgd);
        String s_zbzfgd = String.valueOf(i_zbzfgd);
        String s_dl = String.valueOf(i_dl);
        String s_tdqs = String.valueOf(i_tdqs);
        String s_lmqs = String.valueOf(i_lmqs);
        String s_sllb = String.valueOf(i_sllb);
        String s_gylsqdj = String.valueOf(i_gylsqdj);
        String s_gylbhdj = String.valueOf(i_gylbhdj);
        String s_spljydj = String.valueOf(i_spljydj);
        String s_fycs = String.valueOf(i_fycs);
        String s_linzh = String.valueOf(i_linzh);
        String s_qy = String.valueOf(i_qy);
        String s_yssz = String.valueOf(szdm);
        String s_pjnl = String.valueOf(i_pjnl);
        String s_lingz = String.valueOf(i_lingz);
        String s_cq = String.valueOf(i_cq);
        String s_pjxj = String.valueOf(f_pjxj);
        String s_pjsg = String.valueOf(f_pjsg);
        String s_ybd = String.valueOf(f_ybd);
        String s_slqljg = String.valueOf(i_slqljg);
        String s_lcjg = String.valueOf(i_lcjg);
        String s_szjg = String.valueOf(i_szjg);
        String s_zrd = String.valueOf(i_zrd);
        String s_kjd = String.valueOf(i_kjd);
        String s_slzhlx = String.valueOf(i_slzhlx);
        String s_slzhdj = String.valueOf(i_slzhdj);
        String s_sljkdj = String.valueOf(i_sljkdj);
        String s_spszs = String.valueOf(i_spszs);
        String s_zzzs = String.valueOf(i_zzzs);
        String s_trgxdj = String.valueOf(i_trgxdj);
        String s_dlmjdj = String.valueOf(i_dlmjdj);
        String s_dlbhyy = String.valueOf(i_dlbhyy);
        String s_ywtsdd = String.valueOf(i_ywtsdd);
        String s_dcrq = dcrq;
        //String s_f1 = String.valueOf(i_f1);
        //String s_f2 = String.valueOf(i_f2);

        String sql = "select * from ydyz where ydh = '" + ydh + "'";
        if (!YangdiMgr.QueryExists(ydh, sql)) {
            sql = "insert into ydyz values("
                    + "'" + ydh + "', "
                    + "'" + s_ydlb + "', "
                    + "'" + s_zzb + "', "
                    + "'" + s_hzb + "', "
                    + "'" + s_gpszzb + "', "
                    + "'" + s_gpshzb + "', "
                    + "'" + s_xian + "', "
                    + "'" + s_dm + "', "
                    + "'" + s_hb + "', "
                    + "'" + s_px + "', "
                    + "'" + s_pw + "', "
                    + "'" + s_pd + "', "
                    + "'" + s_dbxt + "', "
                    + "'" + s_sqgd + "', "
                    + "'" + s_fshd + "', "
                    + "'" + s_qsgmjbl + "', "
                    + "'" + s_jyll + "', "
                    + "'" + s_trmc + "', "
                    + "'" + s_trzd + "', "
                    + "'" + s_trlshl + "', "
                    + "'" + s_tchd + "', "
                    + "'" + s_fzchd + "', "
                    + "'" + s_kzlyhd + "', "
                    + "'" + s_zblx + "', "
                    + "'" + s_gmfgd + "', "
                    + "'" + s_gmgd + "', "
                    + "'" + s_cbfgd + "', "
                    + "'" + s_cbgd + "', "
                    + "'" + s_zbzfgd + "', "
                    + "'" + s_dl + "', "
                    + "'" + s_tdqs + "', "
                    + "'" + s_lmqs + "', "
                    + "'" + s_sllb + "', "
                    + "'" + s_gylsqdj + "', "
                    + "'" + s_gylbhdj + "', "
                    + "'" + s_spljydj + "', "
                    + "'" + s_fycs + "', "
                    + "'" + s_linzh + "', "
                    + "'" + s_qy + "', "
                    + "'" + s_yssz + "', "
                    + "'" + s_pjnl + "', "
                    + "'" + s_lingz + "', "
                    + "'" + s_cq + "', "
                    + "'" + s_pjxj + "', "
                    + "'" + s_pjsg + "', "
                    + "'" + s_ybd + "', "
                    + "'" + s_slqljg + "', "
                    + "'" + s_lcjg + "', "
                    + "'" + s_szjg + "', "
                    + "'" + s_zrd + "', "
                    + "'" + s_kjd + "', "
                    + "'" + s_slzhlx + "', "
                    + "'" + s_slzhdj + "', "
                    + "'" + s_sljkdj + "', "
                    + "'" + s_spszs + "', "
                    + "'" + s_zzzs + "', "
                    + "'" + s_trgxdj + "', "
                    + "'" + s_dlmjdj + "', "
                    + "'" + s_dlbhyy + "', "
                    + "'" + s_ywtsdd + "', "
                    + "'" + s_dcrq + "', "
                    + "'', "
                    + "'', "
                    + "'', "
                    + "'', "
                    + "'' "
                    + ")";
            YangdiMgr.ExecSQL(ydh, sql);
        } else {
            sql = "update ydyz set "
                    + "ydh = '" + ydh + "', "
                    + "ydlb = '" + s_ydlb + "', "
                    + "y = '" + s_zzb + "', "
                    + "x = '" + s_hzb + "', "
                    + "gps_y = '" + s_gpszzb + "', "
                    + "gps_x = '" + s_gpshzb + "', "
                    + "xian = '" + s_xian + "', "
                    + "dm = '" + s_dm + "', "
                    + "hb = '" + s_hb + "', "
                    + "px = '" + s_px + "', "
                    + "pw = '" + s_pw + "', "
                    + "pd = '" + s_pd + "', "
                    + "dbxt = '" + s_dbxt + "', "
                    + "sqgd = '" + s_sqgd + "', "
                    + "fshd = '" + s_fshd + "', "
                    + "qsgmjbl = '" + s_qsgmjbl + "', "
                    + "jyll = '" + s_jyll + "', "
                    + "trmc = '" + s_trmc + "', "
                    + "trzd = '" + s_trzd + "', "
                    + "trlshl = '" + s_trlshl + "', "
                    + "tchd = '" + s_tchd + "', "
                    + "fzchd = '" + s_fzchd + "', "
                    + "kzlyhd = '" + s_kzlyhd + "', "
                    + "zblx = '" + s_zblx + "', "
                    + "gmfgd = '" + s_gmfgd + "', "
                    + "gmgd = '" + s_gmgd + "', "
                    + "cbfgd = '" + s_cbfgd + "', "
                    + "cbgd = '" + s_cbgd + "', "
                    + "zbzfgd = '" + s_zbzfgd + "', "
                    + "dl = '" + s_dl + "', "
                    + "tdqs = '" + s_tdqs + "', "
                    + "lmqs = '" + s_lmqs + "', "
                    + "sllb = '" + s_sllb + "', "
                    + "gylsqdj = '" + s_gylsqdj + "', "
                    + "gylbhdj = '" + s_gylbhdj + "', "
                    + "spljydj = '" + s_spljydj + "', "
                    + "fycs = '" + s_fycs + "', "
                    + "linzh = '" + s_linzh + "', "
                    + "qy = '" + s_qy + "', "
                    + "yssz = '" + s_yssz + "', "
                    + "pjnl = '" + s_pjnl + "', "
                    + "lingz = '" + s_lingz + "', "
                    + "cq = '" + s_cq + "', "
                    + "pjxj = '" + s_pjxj + "', "
                    + "pjsg = '" + s_pjsg + "', "
                    + "ybd = '" + s_ybd + "', "
                    + "slqljg = '" + s_slqljg + "', "
                    + "lcjg = '" + s_lcjg + "', "
                    + "szjg = '" + s_szjg + "', "
                    + "zrd = '" + s_zrd + "', "
                    + "kjd = '" + s_kjd + "', "
                    + "slzhlx = '" + s_slzhlx + "', "
                    + "slzhdj = '" + s_slzhdj + "', "
                    + "sljkdj = '" + s_sljkdj + "', "
                    + "spszs = '" + s_spszs + "', "
                    + "zzzs = '" + s_zzzs + "', "
                    + "trgxdj = '" + s_trgxdj + "', "
                    + "dlmjdj = '" + s_dlmjdj + "', "
                    + "dlbhyy = '" + s_dlbhyy + "', "
                    + "ywtsdd = '" + s_ywtsdd + "', "
                    + "dcrq = '" + s_dcrq + "' "
                    + " where ydh = '" + ydh + "'";
            YangdiMgr.ExecSQL(ydh, sql);
        }

        String sdl = i_dl < 0 ? "无" : dl;
        String sqy = i_qy < 0 ? "无" : qy;
        String stdqs = i_tdqs < 0 ? "无" : tdqs;
        String slinzh = i_linzh < 0 ? "无" : linzh;
        String szblx = i_zblx < 0 ? "无" : zblx;
        String syssz = szdm < 0 ? "无" : yssz;
        sql = "update task set "
                + "dl_b = '" + sdl + "', "
                + "qy_b = '" + sqy + "', "
                + "tdqs_b = '" + stdqs + "', "
                + "linzh_b = '" + slinzh + "', "
                + "zblx_b = '" + szblx + "', "
                + "yssz_b = '" + syssz + "' "
                + "where ydh = '" + ydh + "'";
        Setmgr.ExecSQL(sql);

        //List<String> lstError = new ArrayList<String>();
        //List<String> lstWarn = new ArrayList<String>();
        //YangdiMgr.CheckYdyz(ydh, lstError, lstWarn);
        //return lstError.size() == 0;
    }

    private boolean check() {
        List<String> lstError = new ArrayList<String>();
        List<String> lstWarn = new ArrayList<String>();
        YangdiMgr.CheckYdyz(ydh, lstError, lstWarn);
        return lstError.size() == 0;
    }

    class MyTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                etYdh.clearFocus();
                etZzb.clearFocus();
                etHzb.clearFocus();
                etGpszzb.clearFocus();
                etGpshzb.clearFocus();
                etXian.clearFocus();
                etHb.clearFocus();
                etPd.clearFocus();
                etSqgd.clearFocus();
                etFshd.clearFocus();
                etQsgmjbl.clearFocus();
                etJyll.clearFocus();
                etTrlshl.clearFocus();
                etTchd.clearFocus();
                etFzchd.clearFocus();
                etKzlyhd.clearFocus();
                etGmfgd.clearFocus();
                etGmgd.clearFocus();
                etCbfgd.clearFocus();
                etCbgd.clearFocus();
                etZbzfgd.clearFocus();
                etYssz.clearFocus();
                etPjnl.clearFocus();
                etPjxj.clearFocus();
                etPjsg.clearFocus();
                etYbd.clearFocus();
                etSpszs.clearFocus();
                etZzzs.clearFocus();
                etDcrq.clearFocus();

                spYdlb.clearFocus();
                spDm.clearFocus();
                spPx.clearFocus();
                spPw.clearFocus();
                spDbxt.clearFocus();
                spTrmc.clearFocus();
                spTrzd.clearFocus();
                spZblx.clearFocus();
                spDl.clearFocus();
                spTdqs.clearFocus();
                spLmqs.clearFocus();
                spSllb.clearFocus();
                spGylsqdj.clearFocus();
                spGylbhdj.clearFocus();
                spSpljydj.clearFocus();
                spFycs.clearFocus();
                spLinzh.clearFocus();
                spQy.clearFocus();
                spCq.clearFocus();
                spLingz.clearFocus();
                spSlqljg.clearFocus();
                spLcjg.clearFocus();
                spSzjg.clearFocus();
                spZrd.clearFocus();
                spKjd.clearFocus();
                spSlzhlx.clearFocus();
                spSlzhdj.clearFocus();
                spSljkdj.clearFocus();
                spTrgxdj.clearFocus();
                spDlmjdj.clearFocus();
                spDlbhyy.clearFocus();
                spYwtsdd.clearFocus();
            }
            return false;
        }
    }
}
