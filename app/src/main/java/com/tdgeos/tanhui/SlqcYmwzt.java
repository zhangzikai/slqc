package com.tdgeos.tanhui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.dlg.yddc.SzxzDialog;
import com.tdgeos.dlg.yddc.ZpmcDialog;
import com.tdgeos.lib.MyFile;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.lib.MyPoint;
import com.tdgeos.lib.MyRect;
import com.tdgeos.yangdi.Qianqimgr;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Setmgr;
import com.tdgeos.yangdi.YangdiMgr;
import com.tdgeos.yangdi.Yangmu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Paint.Style;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;

public class SlqcYmwzt extends Activity implements View.OnClickListener {
    private int ydh = 0;

    private LinearLayout lay = null;
    private MyView myView = null;

    private RadioGroup rgDrawType = null;
    private Button btnRevoke = null;
    private Button btnRestore = null;
    private Button btnDelpic = null;

    private EditText etInfo = null;

    private RadioButton rbCheck = null;
    private RadioButton rbAdd = null;
    private Button btnDelete = null;
    private Button btnMmjcjl = null;
    private Button btnTuku = null;
    private Button btnFinish;

    private Button btnZoomin;
    private Button btnZoomout;
    private Button btnZoomall;

    private Button btnUp;
    private Button btnDown;
    private Button btnLeft;
    private Button btnRight;

    private TextView tvTj = null;

    private EditText etSearch = null;
    private Button btnSearch = null;

    private Button btnClose = null;

    private int iChecked = -1;
    private int iYmType = 0;//1表示添加，0表示修改.
    private int iCkd = 1;
    private Yangmu tmpYangmu = null;//添加是的临时点。
    private List<LabelInfo> lstLabels = new ArrayList<LabelInfo>();
    private List<Yangmu> lstYangmus = null;

    private TextView tvBqxj = null;

    private Spinner spCkdYm = null;
    private Spinner spYmlx = null;
    private EditText etYmh = null;
    private Spinner spLmlx = null;
    private Spinner spCjlx = null;
    private EditText etSzmc = null;
    private EditText etQqxj = null;
    private EditText etBqxj = null;
    private Spinner spCfgllx = null;
    private Spinner spLc = null;
    private Spinner spKjdlxh = null;
    private EditText etFwj = null;
    private EditText etSpj = null;
    private EditText etBeizhu = null;
    private EditText etQxj = null;
    private EditText etXj = null;

    private Button btnSzmc = null;
    private Button btnTake = null;
    private Button btnOk = null;

    private ListView lvYmh;
    private MyItemSelectedListener myItemSelectedListener = null;

    private int picType = 2;
    private int picYmh = 0;
    private String picInfo = null;
    private String picPath = null;

    private ListView listView;
    private PopupWindow pop;

    private boolean lock = true;

    private float dpiScale = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.slqc_ymwzt);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = this.getIntent();
        ydh = intent.getIntExtra("ydh", 0);
        lstYangmus = YangdiMgr.GetYangmus(ydh);
        if (lstYangmus == null) lstYangmus = new ArrayList<Yangmu>();

        dpiScale = getResources().getDisplayMetrics().densityDpi * 1.0f / 160;

        myView = new MyView(this);
        lay = (LinearLayout) findViewById(R.id.yangmutu_lay_view);
        lay.addView(myView);

        rgDrawType = (RadioGroup) findViewById(R.id.yangmutu_rg);
        rgDrawType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio0) {
                    myView.iDrawType = 0;
                }
                if (checkedId == R.id.radio1) {
                    myView.iDrawType = 1;
                }
                if (checkedId == R.id.radio2) {
                    myView.iDrawType = 2;
                }
            }
        });
        btnRevoke = (Button) findViewById(R.id.yangmutu_btn_revoke);
        btnRevoke.setOnClickListener(this);
        btnRestore = (Button) findViewById(R.id.yangmutu_btn_restore);
        btnRestore.setOnClickListener(this);

        btnDelpic = (Button) findViewById(R.id.yangmutu_btn_delpic);
        btnDelpic.setOnClickListener(this);

        tvTj = (TextView) findViewById(R.id.tv_tj);

        etInfo = (EditText) findViewById(R.id.yangmutu_et_info);
        String sql = "select guding from qt where ydh = '" + ydh + "'";
        String[][] sss = YangdiMgr.SelectData(ydh, sql);
        if (sss != null) {
            etInfo.setText(sss[0][0]);
        }
        if (etInfo.getText().toString().equals("")) {
            sql = "select guding from slqc_qt where ydh = '" + ydh + "'";
            sss = Qianqimgr.SelectData(sql);
            if (sss != null) {
                etInfo.setText(sss[0][0]);
            }
        }

        etSearch = (EditText) findViewById(R.id.et_search);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);

        btnClose = (Button) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        rbCheck = (RadioButton) findViewById(R.id.rb_check);
        rbAdd = (RadioButton) findViewById(R.id.rb_add);
        rbCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (!arg1) {
                    iYmType = 1;
                    iChecked = -1;
                    initYangmu(null);
                    myView.postInvalidate();
                } else {
                    iYmType = 0;
                    iChecked = -1;
                    tmpYangmu = null;
                    initYangmu(null);
                    myView.postInvalidate();
                }
            }
        });

        btnMmjcjl = (Button) findViewById(R.id.yangmutu_btn_mmjcjl);
        btnDelete = (Button) findViewById(R.id.yangmutu_btn_delete);
        btnMmjcjl.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnTuku = (Button) findViewById(R.id.yangmutu_btn_tuku);
        btnTuku.setOnClickListener(this);
        btnFinish = (Button) findViewById(R.id.yangmutu_btn_finish);
        btnFinish.setOnClickListener(this);

        spCkdYm = (Spinner) findViewById(R.id.ymgl_sp_ckd);
        List<String> lstCkd = Resmgr.GetValueList("ckd");
        ArrayAdapter<String> apCkd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lstCkd);
        apCkd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCkdYm.setAdapter(apCkd);
        spCkdYm.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                iCkd = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spCkdYm.setSelection(iCkd);

        spYmlx = (Spinner) findViewById(R.id.ymgl_sp_ymlx);
        List<String> lstYmlx = Resmgr.GetValueList("szlx");
        ArrayAdapter<String> apYmlx = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lstYmlx);
        apYmlx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYmlx.setAdapter(apYmlx);

        btnZoomin = (Button) findViewById(R.id.btn_zoomin);
        btnZoomin.setOnClickListener(this);
        btnZoomout = (Button) findViewById(R.id.btn_zoomout);
        btnZoomout.setOnClickListener(this);
        btnZoomall = (Button) findViewById(R.id.btn_zoomall);
        btnZoomall.setOnClickListener(this);

        btnUp = (Button) findViewById(R.id.yangmutu_btn_move_up);
        btnUp.setOnClickListener(this);
        btnDown = (Button) findViewById(R.id.yangmutu_btn_move_down);
        btnDown.setOnClickListener(this);
        btnLeft = (Button) findViewById(R.id.yangmutu_btn_move_left);
        btnLeft.setOnClickListener(this);
        btnRight = (Button) findViewById(R.id.yangmutu_btn_move_right);
        btnRight.setOnClickListener(this);

        tj();

        listView = new ListView(this);

        tvBqxj = (TextView) findViewById(R.id.ymgl_tv_bqxj);

        etYmh = (EditText) findViewById(R.id.ymgl_et_ymh);
        spLmlx = (Spinner) findViewById(R.id.ymgl_sp_lmlx);
        spCjlx = (Spinner) findViewById(R.id.ymgl_sp_cjlx);
        etSzmc = (EditText) findViewById(R.id.ymgl_et_szmc);
        etQqxj = (EditText) findViewById(R.id.ymgl_et_qqxj);
        etBqxj = (EditText) findViewById(R.id.ymgl_et_bqxj);
        spCfgllx = (Spinner) findViewById(R.id.ymgl_sp_cfgllx);
        spLc = (Spinner) findViewById(R.id.ymgl_sp_lc);
        spKjdlxh = (Spinner) findViewById(R.id.ymgl_sp_kjdlxh);
        etFwj = (EditText) findViewById(R.id.ymgl_et_fwj);
        etSpj = (EditText) findViewById(R.id.ymgl_et_spj);
        etBeizhu = (EditText) findViewById(R.id.ymgl_et_beizhu);
        etQxj = (EditText) findViewById(R.id.ymgl_et_qxj);
        etXj = (EditText) findViewById(R.id.ymgl_et_xj);

        etQqxj.setEnabled(false);

        myItemSelectedListener = new MyItemSelectedListener();
        lvYmh = (ListView) findViewById(R.id.lv_ymh);
        lvYmh.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String symh = lvYmh.getItemAtPosition(arg2).toString();
                int i_ymh = 0;
                try {
                    i_ymh = Integer.parseInt(symh);
                } catch (Exception e) {
                }
                if (i_ymh > 0) {
                    int pos = -1;
                    for (int i = 0; i < lstYangmus.size(); i++) {
                        if (i_ymh == lstYangmus.get(i).ymh) {
                            pos = i;
                            break;
                        }
                    }
                    if (pos != -1) {
                        iChecked = pos;
                        myView.moveTo(i_ymh);
                        rbCheck.setChecked(true);
                        iYmType = 0;
                        initYangmu(lstYangmus.get(pos));
                    } else {
                        iChecked = -1;
                        initYangmu(null);
                    }
                }
            }
        });

        List<String> lstXh = Resmgr.GetValueList("kjdlxh");
        ArrayAdapter<String> apXh = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lstXh);
        apXh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKjdlxh.setAdapter(apXh);

        List<String> lstLmlx = Resmgr.GetValueList("lmlx");
        ArrayAdapter<String> apLmlx = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lstLmlx);
        apLmlx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLmlx.setAdapter(apLmlx);

        List<String> lstCjlx = Resmgr.GetValueList("jclx");
        ArrayAdapter<String> apCjlx = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lstCjlx);
        apCjlx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCjlx.setAdapter(apCjlx);

        List<String> lstCfgllx = Resmgr.GetValueList("cfgllx");
        ArrayAdapter<String> apCfgllx = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lstCfgllx);
        apCfgllx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCfgllx.setAdapter(apCfgllx);

        List<String> lstLc = Resmgr.GetValueList("ymlcjg");
        ArrayAdapter<String> apLc = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lstLc);
        apLc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLc.setAdapter(apLc);
        spLc.setSelection(1);

        spCjlx.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String s_ymh = etYmh.getText().toString();
                if (s_ymh.equals("")) return;
                int i_ymh = -1;
                try {
                    i_ymh = Integer.parseInt(s_ymh);
                } catch (Exception e) {
                }
                if (i_ymh < 0) return;
                String s_qqxj = etQqxj.getText().toString();
                float f_qqxj = 0;
                try {
                    f_qqxj = Float.parseFloat(s_qqxj);
                } catch (Exception e) {
                }
                String s_jclx = spCjlx.getSelectedItem().toString();
                int jclx = Resmgr.GetCodeByValue("jclx", s_jclx);
                if (jclx == 13 || jclx == 14 || jclx == 15 || jclx == 17 || jclx == 24) {
                    if (f_qqxj > 0) {
                        etBqxj.setText(String.valueOf(f_qqxj));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        setSZList();

        etSzmc.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (pop == null) {
                        pop = new PopupWindow(listView, etSzmc.getWidth(), LayoutParams.WRAP_CONTENT);
                        pop.setBackgroundDrawable(new ColorDrawable(0xffffffff));
                        pop.setOutsideTouchable(true);
                    }
                    pop.showAsDropDown(etSzmc);
                } else {
                    if (pop != null) pop.dismiss();
                }
            }
        });

        etSzmc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop == null) {
                    pop = new PopupWindow(listView, etSzmc.getWidth(), LayoutParams.WRAP_CONTENT);
                    pop.setBackgroundDrawable(new ColorDrawable(0xffffffff));
                    pop.setOutsideTouchable(true);
                }
                pop.showAsDropDown(etSzmc);
            }
        });

        btnSearch = (Button) findViewById(R.id.btn_ymsearch);
        btnSearch.setOnClickListener(this);
        btnSzmc = (Button) findViewById(R.id.ymgl_btn_szmc);
        btnSzmc.setOnClickListener(this);
        btnTake = (Button) findViewById(R.id.ymgl_btn_take);
        btnTake.setOnClickListener(this);
        btnOk = (Button) findViewById(R.id.ymgl_btn_ok);
        btnOk.setOnClickListener(this);

        etYmh.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                int i_ymh = -1;
                try {
                    i_ymh = Integer.parseInt(str);
                } catch (Exception e) {
                }
                if (i_ymh > 0) {
                    int pos = -1;
                    for (int i = 0; i < lstYangmus.size(); i++) {
                        if (i_ymh == lstYangmus.get(i).ymh) {
                            pos = i;
                            break;
                        }
                    }
                    if (pos != -1) {
                        iChecked = pos;
                        myView.moveTo(i_ymh);
                        rbCheck.setChecked(true);
                        iYmType = 0;
                        initYangmuNoYmh(lstYangmus.get(pos));
                    } else {
                        iChecked = -1;
                        initYangmuNoYmh(null);
                    }
                } else {
                    initYangmuNoYmh(null);
                }
            }
        });
        etYmh.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //lock = hasFocus;
            }
        });

        etBqxj.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_YM_XJ && f <= YangdiMgr.MAX_YM_XJ) {
                    etBqxj.setTextColor(Color.BLACK);
                } else {
                    etBqxj.setTextColor(Color.RED);
                }
                //if(!lock && f >= YangdiMgr.MIN_YM_XJ && !init)
                //{
                //saveYangmu2();
                //}
            }
        });
        etBqxj.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //lock = !hasFocus;
            }
        });

        etFwj.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_FWJ && f <= YangdiMgr.MAX_FWJ) {
                    etFwj.setTextColor(Color.BLACK);
                } else {
                    etFwj.setTextColor(Color.RED);
                }
            }
        });

        etSpj.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_YM_ZB && f <= YangdiMgr.MAX_YM_ZB) {
                    etSpj.setTextColor(Color.BLACK);
                } else {
                    etSpj.setTextColor(Color.RED);
                }
            }
        });
        etSpj.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lock = !hasFocus;
            }
        });

        etXj.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s_xj = s.toString().trim();
                String s_qxj = etQxj.getText().toString().trim();
                float f_xj = -1;
                float f_qxj = -100;
                try {
                    f_xj = Float.parseFloat(s_xj);
                } catch (Exception e) {
                }
                try {
                    f_qxj = Float.parseFloat(s_qxj);
                } catch (Exception e) {
                }
                if (f_xj > 0 && f_qxj >= YangdiMgr.MIN_QXJ && f_qxj <= YangdiMgr.MAX_QXJ) {
                    if (f_qxj < 0) f_qxj = -f_qxj;
                    float f_spj = (float) Math.cos(f_qxj * Math.PI / 180) * f_xj;
                    f_spj = MyFuns.MyDecimal(f_spj, 2);
                    if (lock) {
                        etSpj.setText(String.valueOf(f_spj));
                    }
                }
                if (f_xj < 0) {
                    etXj.setTextColor(Color.RED);
                } else {
                    etXj.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etXj.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lock = hasFocus;
            }
        });

        etQxj.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().trim();
                if (!text.equals("")) {
                    float f_qxj = -100;
                    try {
                        f_qxj = Float.parseFloat(text);
                    } catch (Exception e) {
                    }
                    if (f_qxj < YangdiMgr.MIN_QXJ || f_qxj > YangdiMgr.MAX_QXJ) {
                        etQxj.setTextColor(Color.RED);
                    } else {
                        if (f_qxj < 0) f_qxj = -f_qxj;
                        etQxj.setTextColor(Color.BLACK);
                        String s_xj = etXj.getText().toString().trim();
                        float f_xj = -1;
                        try {
                            f_xj = Float.parseFloat(s_xj);
                        } catch (Exception e) {
                        }
                        if (f_xj > 0 && f_qxj >= 0 && f_qxj <= 90) {
                            float f_spj = (float) Math.cos(f_qxj * Math.PI / 180) * f_xj;
                            f_spj = MyFuns.MyDecimal(f_spj, 2);
                            etSpj.setText(String.valueOf(f_spj));
                        }
                    }
                } else {
                    etQxj.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        initYmhList();

    }

    @Override
    public void onRestart() {
        super.onRestart();
        lstYangmus = YangdiMgr.GetYangmus(ydh);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0: {
                    if (picType == 1 || picType == 3)
                        YangdiMgr.InsertZp(ydh, picType, 0, picInfo, picPath);
                    else YangdiMgr.InsertZp(ydh, picType, picYmh, picInfo, picPath);
                    YangdiMgr.ResetZpCount(ydh);
                    MyFile.DeleteFile(picPath);
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
            case R.id.yangmutu_btn_revoke: {
                myView.Revoke();
                break;
            }
            case R.id.yangmutu_btn_restore: {
                myView.Restore();
                break;
            }
            case R.id.yangmutu_btn_delpic: {
                MyMakeSureDialog dlg = new MyMakeSureDialog(this, "删除图片", "删除后将不可恢复，是否继续删除？", "删除", "取消");
                if (dlg.showDialog()) {
                    YangdiMgr.DelYangmutuDitu(ydh);
                    myView.ResetImage();
                }
                break;
            }
            case R.id.yangmutu_btn_mmjcjl: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, SlqcMmjc.class);
                startActivity(intent);
                break;
            }
            case R.id.yangmutu_btn_tuku: {
                Intent intent = new Intent();
                intent.setClass(this, TukuList.class);
                startActivity(intent);
                break;
            }
            case R.id.yangmutu_btn_move_up: {
                if (iChecked >= 0 && iChecked < lstYangmus.size()) {
                    Yangmu ym = lstYangmus.get(iChecked);
                    ym.Move(0, 0.2f);
                    YangdiMgr.UpdateYangmu(ydh, ym);
                    initYangmu(ym);
                    save();
                    myView.postInvalidate();
                }
                break;
            }
            case R.id.yangmutu_btn_move_down: {
                if (iChecked >= 0 && iChecked < lstYangmus.size()) {
                    Yangmu ym = lstYangmus.get(iChecked);
                    ym.Move(0, -0.2f);
                    YangdiMgr.UpdateYangmu(ydh, ym);
                    initYangmu(ym);
                    save();
                    myView.postInvalidate();
                }
                break;
            }
            case R.id.yangmutu_btn_move_left: {
                if (iChecked >= 0 && iChecked < lstYangmus.size()) {
                    Yangmu ym = lstYangmus.get(iChecked);
                    ym.Move(-0.2f, 0);
                    YangdiMgr.UpdateYangmu(ydh, ym);
                    initYangmu(ym);
                    save();
                    myView.postInvalidate();
                }
                break;
            }
            case R.id.yangmutu_btn_move_right: {
                if (iChecked >= 0 && iChecked < lstYangmus.size()) {
                    Yangmu ym = lstYangmus.get(iChecked);
                    ym.Move(0.2f, 0);
                    YangdiMgr.UpdateYangmu(ydh, ym);
                    initYangmu(ym);
                    save();
                    myView.postInvalidate();
                }
                break;
            }
            case R.id.yangmutu_btn_delete: {
                if (iChecked >= 0 && iChecked < lstYangmus.size()) {
                    MyMakeSureDialog dlg = new MyMakeSureDialog(this, "删除", "删除后将不可恢复，是否继续删除？", "删除", "取消");
                    if (dlg.showDialog()) {
                        initYangmu(null);
                        YangdiMgr.DelYangmu(ydh, lstYangmus.get(iChecked).ymh);
                        lstYangmus = YangdiMgr.GetYangmus(ydh);
                        iChecked = -1;
                        save();
                        tj();
                        myView.postInvalidate();
                        initYmhList();
                    }
                }
                break;
            }
            case R.id.yangmutu_btn_finish: {
                myView.SaveDitu();
                myView.SaveYmwzt();
                save();
                List<String> lstError = new ArrayList<String>();
                List<String> lstWarn = new ArrayList<String>();
                YangdiMgr.CheckMmjc(ydh, lstError, lstWarn);
                if (lstError.size() == 0) {
                    String sql = "update dczt set ymwzt = '2' where ydh = '" + ydh + "'";
                    YangdiMgr.ExecSQL(ydh, sql);
                } else {
                    String sql = "update dczt set ymwzt = '1' where ydh = '" + ydh + "'";
                    YangdiMgr.ExecSQL(ydh, sql);
                }
                int all = YangdiMgr.GetYangmuCount(ydh);
                int pass = YangdiMgr.GetPassYangmuCount(ydh);
                if (pass < all) {
                    String str = YangdiMgr.GetResidueYangmu(ydh);
                    str += "，是否仍然离开该界面？";
                    MyMakeSureDialog dlg = new MyMakeSureDialog(this, "提示", str, "是", "否");
                    if (dlg.showDialog()) {
                        this.finish();
                    }
                } else {
                    this.finish();
                }
                break;
            }
            case R.id.btn_zoomin: {
                myView.ZoomIn();
                break;
            }
            case R.id.btn_zoomout: {
                myView.ZoomOut();
                break;
            }
            case R.id.btn_zoomall: {
                myView.ZoomToAll();
                break;
            }
            case R.id.btn_search: {
                String s_ymh = etSearch.getText().toString();
                int i_ymh = 0;
                try {
                    i_ymh = Integer.parseInt(s_ymh);
                } catch (Exception e) {
                }
                if (i_ymh > 0) {
                    int pos = -1;
                    for (int i = 0; i < lstYangmus.size(); i++) {
                        if (i_ymh == lstYangmus.get(i).ymh) {
                            pos = i;
                            break;
                        }
                    }
                    if (pos != -1) {
                        iChecked = pos;
                        myView.moveTo(i_ymh);
                        rbCheck.setChecked(true);
                        iYmType = 0;
                        initYangmu(lstYangmus.get(pos));
                    } else {
                        Toast.makeText(this, "无效的样木号！", 1).show();
                    }
                } else {
                    Toast.makeText(this, "无效的样木号！", 1).show();
                }
                break;
            }
            case R.id.ymgl_btn_szmc: {
                SzxzDialog dlg = new SzxzDialog(this);
                String str = dlg.showDialog();
                if (str != null) {
                    etSzmc.setText(str);
                }
                break;
            }
            case R.id.ymgl_btn_ok: {
                saveYangmu();
                if (iYmType == 1) setSZList();
                break;
            }
            case R.id.ymgl_btn_take: {
                String s_ymh = etYmh.getText().toString();
                int i_ymh = 0;
                try {
                    i_ymh = Integer.parseInt(s_ymh);
                } catch (Exception e) {
                }

                ZpmcDialog dlg = new ZpmcDialog(this, ydh, i_ymh);
                String[] ss = dlg.showDialog();
                if (ss != null) {
                    picType = 1;
                    try {
                        picType = Integer.parseInt(ss[0]);
                    } catch (Exception e) {
                    }
                    picYmh = 0;
                    try {
                        picYmh = Integer.parseInt(ss[1]);
                    } catch (Exception e) {
                    }
                    picInfo = ss[2];
                    picPath = MyConfig.GetWorkdir() + "/temp/" + ss[3];
                    MyFile.DeleteFile(picPath);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(picPath)));
                    startActivityForResult(intent, 0);
                }
                break;
            }
        }
    }

    private void tj() {
        int all = YangdiMgr.GetYangmuCount(ydh);
        int pass = YangdiMgr.GetPassYangmuCount(ydh);
        tvTj.setText("样木总数：" + all + "，已检尺：" + pass + "，剩余：" + (all - pass));
    }

    private void initYmhList() {
        List<String> lst = new ArrayList<String>();
        String sql = "select ymh from mmjc where ydh = '" + ydh + "' order by ymh";
        String[][] sss = YangdiMgr.SelectData(ydh, sql);
        if (sss != null) {
            for (int i = 0; i < sss.length; i++) {
                lst.add(sss[i][0]);
            }
        }
        lvYmh.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lst));
        //lvYmh.setOnItemSelectedListener(myItemSelectedListener);
    }

    private void initYangmu(Yangmu ym) {
        spCkdYm.setSelection(1);
        spYmlx.setSelection(0);
        etYmh.setText("");
        spLmlx.setSelection(1);
        spCjlx.setSelection(2);
        etSzmc.setText("");
        etQqxj.setText("");
        etBqxj.setText("");
        spCfgllx.setSelection(1);
        spLc.setSelection(1);
        spKjdlxh.setSelection(0);
        etFwj.setText("");
        etSpj.setText("");
        etBeizhu.setText("");
        etQxj.setText("");
        etXj.setText("");

        if (ym != null) {
            spCkdYm.setSelection(ym.ckd);
            spYmlx.setSelection(ym.szlx);
            if (ym.ymh > 0) etYmh.setText(String.valueOf(ym.ymh));
            spLmlx.setSelection(Resmgr.GetPosByCode("lmlx", ym.lmlx));
            spCjlx.setSelection(Resmgr.GetPosByCode("jclx", ym.jclx));
            if (ym.qqxj > 0) etQqxj.setText(String.valueOf(ym.qqxj));
            if (ym.bqxj > 0) etBqxj.setText(String.valueOf(ym.bqxj));
            spCfgllx.setSelection(Resmgr.GetPosByCode("cfgllx", ym.cfgllx));
            spLc.setSelection(Resmgr.GetPosByCode("ymlcjg", ym.lc));
            spKjdlxh.setSelection(ym.kjdlxh);
            if (ym.fwj >= YangdiMgr.MIN_YM_ZB) etFwj.setText(String.valueOf(ym.fwj));
            if (ym.spj >= YangdiMgr.MIN_YM_ZB) etSpj.setText(String.valueOf(ym.spj));
            etBeizhu.setText(ym.bz);

            if (ym.qxj >= 0) etQxj.setText(String.valueOf(ym.qxj));
            if (ym.xj >= 0) etXj.setText(String.valueOf(ym.xj));

            if (ym.szdm <= 0) {
                String qqym = Qianqimgr.GetQqYmsz(ydh, ym.ymh);
                if (qqym != null) {
                    etSzmc.setText(qqym);
                    etSzmc.setTextColor(Color.RED);
                } else {
                    etSzmc.setTextColor(Color.BLACK);
                }
            } else {
                etSzmc.setText(ym.szdm + " " + ym.szmc);
                etSzmc.setTextColor(Color.BLACK);
            }

            if (ym.bqxj < 5) {
                tvBqxj.setTextColor(Color.RED);
            } else {
                tvBqxj.setTextColor(Color.BLACK);
            }

            if (ym.lmlx == -1) spLmlx.setSelection(1);
            if (ym.jclx == -1) spCjlx.setSelection(2);
            if (ym.lc == -1) spLc.setSelection(1);
            if (ym.cfgllx == -1) spCfgllx.setSelection(1);
        }
    }

    private void initYangmuNoYmh(Yangmu ym) {
        /*
        Yangmu curYm = getCurYangmu();
		if(curYm != null && curYm.ymh > 0 && curYm.bqxj >= 5)
		{
			Yangmu ym0 = YangdiMgr.GetYangmu(ydh, curYm.ymh);
			if(ym0 != null)
			{
				if(!ym0.Equals(curYm))
				{
					if(!saveYangmu())
					{
						return;
					}
				}
			}
			else
			{
				if(!saveYangmu())
				{
					return;
				}
			}
		}
		*/

        spCkdYm.setSelection(1);
        spYmlx.setSelection(0);
        //etYmh.setText("");
        spLmlx.setSelection(1);
        spCjlx.setSelection(2);
        etSzmc.setText("");
        etQqxj.setText("");
        etBqxj.setText("");
        spCfgllx.setSelection(1);
        spLc.setSelection(1);
        spKjdlxh.setSelection(0);
        etFwj.setText("");
        etSpj.setText("");
        etBeizhu.setText("");
        etQxj.setText("");
        etXj.setText("");

        if (ym != null) {
            spCkdYm.setSelection(ym.ckd);
            spYmlx.setSelection(ym.szlx);
            //if(ym.ymh > 0) etYmh.setText(String.valueOf(ym.ymh));
            spLmlx.setSelection(Resmgr.GetPosByCode("lmlx", ym.lmlx));
            spCjlx.setSelection(Resmgr.GetPosByCode("jclx", ym.jclx));
            if (ym.qqxj > 0) etQqxj.setText(String.valueOf(ym.qqxj));
            if (ym.bqxj > 0) etBqxj.setText(String.valueOf(ym.bqxj));
            spCfgllx.setSelection(Resmgr.GetPosByCode("cfgllx", ym.cfgllx));
            spLc.setSelection(Resmgr.GetPosByCode("ymlcjg", ym.lc));
            spKjdlxh.setSelection(ym.kjdlxh);
            if (ym.fwj >= YangdiMgr.MIN_YM_ZB) etFwj.setText(String.valueOf(ym.fwj));
            if (ym.spj >= YangdiMgr.MIN_YM_ZB) etSpj.setText(String.valueOf(ym.spj));
            etBeizhu.setText(ym.bz);

            if (ym.qxj >= 0) etQxj.setText(String.valueOf(ym.qxj));
            if (ym.xj >= 0) etXj.setText(String.valueOf(ym.xj));

            if (ym.szdm <= 0) {
                String qqym = Qianqimgr.GetQqYmsz(ydh, ym.ymh);
                if (qqym != null) {
                    etSzmc.setText(qqym);
                    etSzmc.setTextColor(Color.RED);
                } else {
                    etSzmc.setTextColor(Color.BLACK);
                }
            } else {
                etSzmc.setText(ym.szdm + " " + ym.szmc);
                etSzmc.setTextColor(Color.BLACK);
            }

            if (ym.bqxj < 5) {
                tvBqxj.setTextColor(Color.RED);
            } else {
                tvBqxj.setTextColor(Color.BLACK);
            }

            if (ym.lmlx == -1) spLmlx.setSelection(1);
            if (ym.jclx == -1) spCjlx.setSelection(2);
            if (ym.lc == -1) spLc.setSelection(1);
            if (ym.cfgllx == -1) spCfgllx.setSelection(1);
        }
    }

    private void setSZList() {
        List<String> lstSz = Setmgr.GetCyszList();
        DropdownAdapter adapter = new DropdownAdapter(this, lstSz);
        listView.setAdapter(adapter);
    }

    private int getNextYmh() {
        int bqYmh = YangdiMgr.GetMaxYmhQq(ydh);
        int qqYmh = YangdiMgr.GetMaxYmh(ydh);
        bqYmh++;
        qqYmh++;
        return bqYmh > qqYmh ? bqYmh : qqYmh;
    }

    private void save() {
        String str = etInfo.getText().toString();

        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (YangdiMgr.QueryExists(ydh, sql)) {
            sql = "update qt set guding = '" + str + "' where ydh = '" + ydh + "'";
        } else {
            sql = "insert into qt(ydh, guding) values('" + ydh + "', '" + str + "')";
        }
        YangdiMgr.ExecSQL(ydh, sql);
    }

    private boolean saveYangmu() {
        int count = lstYangmus.size();
        int zt = 1;

        String szmc = null;
        int szdm = -1;
        int ymh = 0;
        String symh = etYmh.getText().toString();
        if (symh.equals("")) {
            zt = 0;
            Toast.makeText(this, "样木号不能为空！", 1).show();
            return false;
        }
        try {
            ymh = Integer.parseInt(symh);
        } catch (java.lang.NumberFormatException nfe) {
        }
        if (ymh == 0) {
            zt = 0;
            Toast.makeText(this, "样木号填写错误！", 1).show();
            return false;
        }


        Yangmu qqym = YangdiMgr.GetQqYangmu(ydh, ymh);

        int lmlx = Resmgr.GetCodeByValue("lmlx", spLmlx.getSelectedItem().toString());
        if (lmlx == -1) {
            zt = 0;
            Toast.makeText(this, "立木类型不能为空！", 1).show();
            //return false;
        }
        int jclx = Resmgr.GetCodeByValue("jclx", spCjlx.getSelectedItem().toString());
        if (jclx == -1) {
            zt = 0;
            Toast.makeText(this, "检尺类型不能为空！", 1).show();
            //return false;
        }
        if (qqym != null) {
            if (jclx == 12 || jclx == 16) {
                zt = 0;
                Toast.makeText(this, "检尺类型与前期数据冲突！", 1).show();
                //return false;
            }
        }
        if (qqym == null) {
            if (jclx != 12 && jclx != 16 && jclx != 1 && jclx != 10) {
                MyMakeSureDialog dlg = new MyMakeSureDialog(this, "警告", "前期数据中无此样木，是否继续按此捡尺类型保存?", "保存", "修改");
                if (!dlg.showDialog()) {
                    return false;
                }
            }
        }
        String str = etSzmc.getText().toString().trim();
        String[] ss = str.split(" ");
        szdm = -1;
        szmc = null;
        if (ss.length == 1) {
            try {
                szdm = Integer.parseInt(str);
            } catch (Exception e) {
            }
            if (szdm > 0) {
                szmc = Resmgr.GetSzName(szdm);
                if (szmc.equals("")) {
                    zt = 0;
                    Toast.makeText(this, "树种填写错误，无法匹配该树种的代码！", 1).show();
                    //return false;
                }
            } else {
                szmc = str;
                szdm = Resmgr.GetSzCode(szmc);
                if (szdm <= 0) {
                    zt = 0;
                    Toast.makeText(this, "树种填写错误，无法匹配该树种的代码！", 1).show();
                    //return false;
                }
            }
        } else if (ss.length == 2) {
            try {
                szdm = Integer.parseInt(ss[0]);
            } catch (Exception e) {
            }
            if (szdm <= 0) {
                zt = 0;
                Toast.makeText(this, "树种填写错误！", 1).show();
                //return false;
            }
            szmc = ss[1];
            if (szdm != Resmgr.GetSzCode(szmc)) {
                zt = 0;
                Toast.makeText(this, "树种填写错误，代码和名称不一致！", 1).show();
                //return false;
            }
        } else {
            zt = 0;
            Toast.makeText(this, "树种名称填写错误！", 1).show();
            //return false;
        }
        etSzmc.setTextColor(Color.BLACK);

        if (Resmgr.IsHunjiaoSz(szdm)) {
            zt = 0;
            Toast.makeText(this, "检尺样木树种名称不能填写填写混交树种！", 1).show();
            //return false;
        }

        if (qqym != null) {
            if (qqym.szdm != szdm) {
                if (jclx != 19) {
                    MyMakeSureDialog dlg = new MyMakeSureDialog(this, "警告", "该样木的本期树种与前期树种不一致，也不是'树种错测木'，是否继续保存?", "保存", "修改");
                    if (!dlg.showDialog()) {
                        return false;
                    }
                }
            }
        }
        String sqqxj = etQqxj.getText().toString();
        float qqxj = -1;
        if (!sqqxj.equals("")) {
            try {
                qqxj = Float.parseFloat(sqqxj);
            } catch (java.lang.NumberFormatException nfe) {
            }
        }

        float bqxj = -1;
        try {
            bqxj = Float.parseFloat(etBqxj.getText().toString());
            bqxj = MyFuns.MyDecimal(bqxj, 1);
        } catch (java.lang.NumberFormatException nfe) {
        }
        if (bqxj < YangdiMgr.MIN_YM_XJ || bqxj > YangdiMgr.MAX_YM_XJ) {
            zt = 0;
            Toast.makeText(this, "本期胸径超限！", 1).show();
            //return false;
        }
        if (qqym != null) {
            if (qqxj > bqxj && bqxj > 0) {
                if (jclx != 18) {
                    MyMakeSureDialog dlg = new MyMakeSureDialog(this, "警告", "该样木的本期胸径小于前期胸径，也不是'胸径错测木'，是否继续保存?", "保存", "修改");
                    if (!dlg.showDialog()) {
                        return false;
                    }
                }
            }
            if (bqxj - qqxj > 40) {
                MyMakeSureDialog dlg = new MyMakeSureDialog(this, "警告", "该样木的本期胸径增长过大，是否继续保存?", "保存", "修改");
                if (!dlg.showDialog()) {
                    return false;
                }
            }
            if (jclx == 13 || jclx == 14 || jclx == 15 || jclx == 17 || jclx == 24) {
                if (qqym.bqxj != bqxj && bqxj > 0) {
                    zt = 0;
                    Toast.makeText(this, "本期胸径填写错误，应与前期胸径保持一致！", 1).show();
                    //return false;
                }
            }
        }
        if (jclx == 12 && bqxj > 30) {
            MyMakeSureDialog dlg = new MyMakeSureDialog(this, "警告", "该进界木的本期胸径过大，是否继续保存?", "保存", "修改");
            if (!dlg.showDialog()) {
                return false;
            }
        }

        int cfgllx = Resmgr.GetCodeByValue("cfgllx", spCfgllx.getSelectedItem().toString());
        if (cfgllx == -1) {
            zt = 0;
            Toast.makeText(this, "采伐管理类型不能为空！", 1).show();
            //return false;
        }
        int lc = Resmgr.GetCodeByValue("ymlcjg", spLc.getSelectedItem().toString());
        if (lmlx == 11 && lc == -1) {
            zt = 0;
            Toast.makeText(this, "林层不能为空！", 1).show();
            //return false;
        }
        int kjdlxh = spKjdlxh.getSelectedItemPosition();

        String sfwj = etFwj.getText().toString();
        String sspj = etSpj.getText().toString();
        if (jclx != 13 && jclx != 14 && jclx != 15 && jclx != 17 && jclx != 24 && szdm != 8001) {
            if (sfwj.equals("") || sspj.equals("")) {
                zt = 0;
                Toast.makeText(this, "样木坐标不能为空！", 1).show();
                //return false;
            }
        }
        float fwj = 0;
        try {
            fwj = Float.parseFloat(etFwj.getText().toString());
            fwj = MyFuns.MyDecimal(fwj, 1);
        } catch (java.lang.NumberFormatException nfe) {
        }
        float spj = 0;
        try {
            spj = Float.parseFloat(etSpj.getText().toString());
            spj = MyFuns.MyDecimal(spj, 1);
        } catch (java.lang.NumberFormatException nfe) {
        }

        String bz = etBeizhu.getText().toString();
        int tag = spYmlx.getSelectedItemPosition();

        String s_qxj = etQxj.getText().toString();
        String s_xj = etXj.getText().toString();
        float f_qxj = -1;
        float f_xj = -1;
        try {
            f_qxj = Float.parseFloat(s_qxj);
        } catch (java.lang.NumberFormatException nfe) {
        }
        try {
            f_xj = Float.parseFloat(s_xj);
        } catch (java.lang.NumberFormatException nfe) {
        }
        if (f_qxj < YangdiMgr.MIN_QXJ || f_qxj > YangdiMgr.MAX_QXJ) {
            zt = 0;
            Toast.makeText(this, "倾斜角超限！", 1).show();
        }

        Yangmu ym = new Yangmu();
        ym.ymh = ymh;
        ym.lmlx = lmlx;
        ym.jclx = jclx;
        ym.szmc = szmc;
        ym.szdm = szdm;
        ym.qqxj = qqxj;
        ym.bqxj = bqxj;
        ym.cfgllx = cfgllx;
        ym.lc = lc;
        ym.kjdlxh = kjdlxh;
        ym.fwj = fwj;
        ym.spj = spj;
        ym.bz = bz;
        ym.ckd = iCkd;
        ym.szlx = tag;
        ym.jczt = zt;
        ym.bqxj = MyFuns.MyDecimal(ym.bqxj, 1);
        ym.fwj = MyFuns.MyDecimal(ym.fwj, 1);
        ym.spj = MyFuns.MyDecimal(ym.spj, 1);

        ym.qxj = f_qxj;
        ym.xj = f_xj;

        float dis = ym.GetErrorDis();
        if (dis > 3) {
            Toast.makeText(this, "样木坐标误差过大！", 1).show();
            return false;
        }
        if (dis > 0.1f && dis < 3) {
            MyMakeSureDialog dlg = new MyMakeSureDialog(this, "提示", "此样木位于标准样地范围外" + dis + "米处，是否仍然保存？", "保存", "修改");
            if (!dlg.showDialog()) {
                ym = null;
                return false;
            }
        }

        boolean b = YangdiMgr.IsHasYangmu(ydh, ymh);
        if (b) {
            if (iChecked >= 0 && iChecked < count && lstYangmus.get(iChecked).ymh != ymh) {
                Toast.makeText(this, "样木号已经存在，请重新输入！", 1).show();
                return false;
            }
            if (iChecked < 0 || iChecked >= count) {
                Toast.makeText(this, "样木号已经存在，请重新输入！", 1).show();
                return false;
            }
        }

        if (iChecked >= 0 && iChecked < count) {
            YangdiMgr.UpdateYangmu(ydh, ym);
            lstYangmus = YangdiMgr.GetYangmus(ydh);
        } else {
            YangdiMgr.AddYangmu(ydh, ym);
            lstYangmus = YangdiMgr.GetYangmus(ydh);
            initYmhList();
            MyConfig.SetLastSzdm(szdm);
            MyConfig.SetLastKjxh(kjdlxh);
        }
        Setmgr.AddCysz(szdm, szmc);

        tj();

        iChecked = -1;
        tmpYangmu = null;
        initYangmu(null);
        myView.postInvalidate();

        return true;
    }

    class MyItemSelectedListener implements OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            String symh = lvYmh.getSelectedItem().toString();
            System.out.println("symh = " + symh);
            int i_ymh = 0;
            try {
                i_ymh = Integer.parseInt(symh);
            } catch (Exception e) {
            }
            if (i_ymh > 0) {
                int pos = -1;
                for (int i = 0; i < lstYangmus.size(); i++) {
                    if (i_ymh == lstYangmus.get(i).ymh) {
                        pos = i;
                        break;
                    }
                }
                if (pos != -1) {
                    iChecked = pos;
                    myView.moveTo(i_ymh);
                    rbCheck.setChecked(true);
                    iYmType = 0;
                    initYangmu(lstYangmus.get(pos));
                } else {
                    iChecked = -1;
                    initYangmu(null);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    class DropdownAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<String> list;
        private TextView content;
        private ImageButton close;

        public DropdownAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        public int getCount() {
            return list.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.slqc_cysz_item, null);
            close = (ImageButton) convertView.findViewById(R.id.close_row);
            content = (TextView) convertView.findViewById(R.id.text_row);
            final String editContent = list.get(position);
            content.setText(list.get(position).toString());
            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etSzmc.setText(editContent);
                    pop.dismiss();
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String name = list.get(position);
                    int code = Resmgr.GetSzCode(name);
                    Setmgr.DelCysz(code);
                    list.remove(position);
                    DropdownAdapter.this.notifyDataSetChanged();
                    pop.setHeight(LayoutParams.WRAP_CONTENT);
                }
            });
            return convertView;
        }
    }


    class MyCommand {
        public int color;
        public int width;
        public List<MyPoint> lstPts;
    }

    class MyView extends View implements View.OnTouchListener {
        private GestureDetector mGestureDetector = null;
        private Paint paint = new Paint();
        private int view_size = 0;
        private int view_width = 0;
        private int view_height = 0;
        private float xBegin = 0;
        private float yBegin = 0;
        private float xEnd = 0;
        private float yEnd = 0;
        private float interval = 2;

        private float minScale;
        private float maxScale;
        private float initScale;
        private float curScale;
        private float mapOriginX;
        private float mapOriginY;

        public int iDrawType = 0;
        private boolean isFirst = true;
        private boolean isRefresh = false;
        private boolean isMoveYm = false;

        //private Canvas tmpCanvas = null;
        //private Bitmap bmpTempCanvas = null;

        private int width = (int) (3 * dpiScale);
        private int color = 0x88808080;
        private int earserWidth = (int) (3 * dpiScale);
        private int textSize = (int) (14 * dpiScale);
        private int r1 = (int) (7 * dpiScale);
        private int r2 = (int) (3 * dpiScale);
        private int r3 = (int) (2 * dpiScale);
        private int intv = (int) (100 * dpiScale);

        private List<MyCommand> lstCmds = new ArrayList<MyCommand>();
        private List<MyCommand> lstDels = new ArrayList<MyCommand>();
        private List<MyPoint> tmpLine = new ArrayList<MyPoint>();
        private List<MyPoint> cmdLine = null;
        private Bitmap bmpBackground = null;

        public MyView(Context context) {
            super(context);
            mGestureDetector = new GestureDetector(context, new LearnGestureListener());
            this.setOnTouchListener(this);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (isFirst) {
                isFirst = false;

                view_width = this.getWidth();
                view_height = this.getHeight();
                view_size = view_width < view_height ? view_width : view_height;
                int initSize = view_size - intv;
                minScale = initSize / YangdiMgr.YD_SIZE / 2;
                maxScale = initSize / interval;
                initScale = initSize / YangdiMgr.YD_SIZE;
                curScale = initScale;
                mapOriginX = view_size / 2 - YangdiMgr.YD_SIZE / 2 * initScale;
                mapOriginY = view_size + (YangdiMgr.YD_SIZE / 2 * initScale - view_size / 2);

                int size = initSize + intv * 2;//
                bmpBackground = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
                Canvas cv = new Canvas(bmpBackground);
                cv.drawColor(0xffffffff);

                Bitmap bmp = YangdiMgr.GetYangmutuDitu(ydh);
                if (bmp != null) {
                    int s = (size - bmp.getHeight()) / 2;
                    cv.drawBitmap(bmp, s, s, paint);
                    bmp.recycle();
                    bmp = null;
                }

                //bmpTempCanvas = Bitmap.createBitmap(view_width, view_height, Bitmap.Config.ARGB_4444);
                //tmpCanvas = new Canvas(bmpTempCanvas);
                //tmpCanvas.drawColor(Color.WHITE);
            }

            canvas.drawColor(0xffffffff);

            MyPoint ptLt = null;
            MyPoint ptRb = null;

            ptLt = new MyPoint(0, YangdiMgr.YD_SIZE);
            ptRb = new MyPoint(YangdiMgr.YD_SIZE, 0);
            mapToScreen(ptLt);
            mapToScreen(ptRb);
            float s = (ptRb.x - ptLt.x) / (bmpBackground.getHeight() - intv * 2);//
            Matrix m = new Matrix();
            m.setScale(s, s);
            m.postTranslate(ptLt.x - intv * s, ptLt.y - intv * s);//
            canvas.drawBitmap(bmpBackground, m, paint);

            float left = ptLt.x - intv * s;
            float top = ptLt.y - intv * s;
            float right = left + bmpBackground.getHeight() * s;
            float bottom = top + bmpBackground.getHeight() * s;
            paint.setColor(0xfff0d0d0);
            paint.setStyle(Style.STROKE);
            canvas.drawRect(left, top, right, bottom, paint);

            paint.reset();

            for (int i = 0; i < lstCmds.size(); i++) {
                List<MyPoint> pts = lstCmds.get(i).lstPts;
                if (pts.size() == 1) {
                    paint.setStyle(Style.FILL);
                    paint.setStrokeWidth(lstCmds.get(i).width);
                    paint.setColor(lstCmds.get(i).color);
                    MyPoint pt = new MyPoint(pts.get(0));
                    mapToScreen(pt);
                    canvas.drawCircle(pt.x, pt.y, width, paint);
                } else if (pts.size() > 1) {
                    paint.setStyle(Style.STROKE);
                    paint.setStrokeWidth(lstCmds.get(i).width);
                    paint.setColor(lstCmds.get(i).color);
                    Path path = new Path();
                    MyPoint pt = new MyPoint(pts.get(0));
                    mapToScreen(pt);
                    path.moveTo(pt.x, pt.y);
                    for (int j = 1; j < pts.size(); j++) {
                        pt = new MyPoint(pts.get(j));
                        mapToScreen(pt);
                        path.lineTo(pt.x, pt.y);
                    }
                    canvas.drawPath(path, paint);
                }
            }

            paint.setStyle(Style.STROKE);
            if (iDrawType == 1) {
                paint.setStrokeWidth(width);
                paint.setColor(color);
            }
            if (iDrawType == 2) {
                paint.setStrokeWidth(earserWidth);
                paint.setColor(0xfff0f0ff);
            }
            if (tmpLine.size() > 0) {
                Path path = new Path();
                MyPoint pt = new MyPoint(tmpLine.get(0));
                mapToScreen(pt);
                path.moveTo(pt.x, pt.y);
                for (int j = 1; j < tmpLine.size(); j++) {
                    pt = new MyPoint(tmpLine.get(j));
                    mapToScreen(pt);
                    path.lineTo(pt.x, pt.y);
                }
                canvas.drawPath(path, paint);
            }

            if (YangdiMgr.YMT_WG_TYPE == 0) {
                paint.reset();
                paint.setStyle(Style.STROKE);
                paint.setColor(0xffd0d0d0);
                paint.setTextSize(textSize);
                ptLt = new MyPoint(0, 0);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, 0);
                mapToScreen(ptLt);
                mapToScreen(ptRb);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(0, 0);
                ptRb = new MyPoint(0, YangdiMgr.YD_SIZE);
                mapToScreen(ptLt);
                mapToScreen(ptRb);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(YangdiMgr.YD_SIZE, 0);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, YangdiMgr.YD_SIZE);
                mapToScreen(ptLt);
                mapToScreen(ptRb);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(0, YangdiMgr.YD_SIZE);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, YangdiMgr.YD_SIZE);
                mapToScreen(ptLt);
                mapToScreen(ptRb);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);

                paint.setColor(0xffd0d0d0);
                ptLt = new MyPoint(0, 0);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, YangdiMgr.YD_SIZE);
                mapToScreen(ptLt);
                mapToScreen(ptRb);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(0, YangdiMgr.YD_SIZE);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, 0);
                mapToScreen(ptLt);
                mapToScreen(ptRb);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(0, YangdiMgr.YD_SIZE / 2);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, YangdiMgr.YD_SIZE / 2);
                mapToScreen(ptLt);
                mapToScreen(ptRb);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(YangdiMgr.YD_SIZE / 2, YangdiMgr.YD_SIZE);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE / 2, 0);
                mapToScreen(ptLt);
                mapToScreen(ptRb);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);

                ptLt = new MyPoint(YangdiMgr.YD_SIZE / 2, YangdiMgr.YD_SIZE / 2);
                mapToScreen(ptLt);
                float r11 = 2 * curScale;
                canvas.drawCircle(ptLt.x, ptLt.y, r11, paint);
                canvas.drawText("2", ptLt.x + r11, ptLt.y - 3, paint);
                float r12 = 4 * curScale;
                canvas.drawCircle(ptLt.x, ptLt.y, r12, paint);
                canvas.drawText("4", ptLt.x + r12, ptLt.y - 3, paint);
                float r13 = 6 * curScale;
                canvas.drawCircle(ptLt.x, ptLt.y, r13, paint);
                canvas.drawText("6", ptLt.x + r13, ptLt.y - 3, paint);
                float r14 = 8 * curScale;
                canvas.drawCircle(ptLt.x, ptLt.y, r14, paint);
                canvas.drawText("8", ptLt.x + r14, ptLt.y - 3, paint);
                float r15 = 10 * curScale;
                canvas.drawCircle(ptLt.x, ptLt.y, r15, paint);
                canvas.drawText("10", ptLt.x + r15, ptLt.y - 3, paint);
                float r16 = 12 * curScale;
                canvas.drawCircle(ptLt.x, ptLt.y, r16, paint);
                canvas.drawText("12", ptLt.x + r16, ptLt.y - 3, paint);
                float r17 = 14 * curScale;
                RectF box = new RectF(ptLt.x - r17, ptLt.y - r17, ptLt.x + r17, ptLt.y + r17);
                canvas.drawArc(box, 23, 44, false, paint);
                canvas.drawArc(box, 113, 44, false, paint);
                canvas.drawArc(box, 203, 44, false, paint);
                canvas.drawArc(box, 293, 44, false, paint);
                float r18 = 16 * curScale;
                box = new RectF(ptLt.x - r18, ptLt.y - r18, ptLt.x + r18, ptLt.y + r18);
                canvas.drawArc(box, 36, 18, false, paint);
                canvas.drawArc(box, 126, 18, false, paint);
                canvas.drawArc(box, 216, 18, false, paint);
                canvas.drawArc(box, 306, 18, false, paint);
            } else {
                paint.reset();
                paint.setStyle(Style.STROKE);
                paint.setColor(0xffd0d0d0);
                paint.setTextSize(textSize);
                ptLt = null;
                ptRb = null;

                ptLt = new MyPoint(0, 0);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, 0);
                mapToScreen(ptLt);
                mapToScreen(ptRb);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(0, 0);
                ptRb = new MyPoint(0, YangdiMgr.YD_SIZE);
                mapToScreen(ptLt);
                mapToScreen(ptRb);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(YangdiMgr.YD_SIZE, 0);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, YangdiMgr.YD_SIZE);
                mapToScreen(ptLt);
                mapToScreen(ptRb);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(0, YangdiMgr.YD_SIZE);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, YangdiMgr.YD_SIZE);
                mapToScreen(ptLt);
                mapToScreen(ptRb);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);

                float a = interval;
                while (a < YangdiMgr.YD_SIZE) {
                    ptLt = new MyPoint(0, a);
                    ptRb = new MyPoint(YangdiMgr.YD_SIZE, a);
                    mapToScreen(ptLt);
                    mapToScreen(ptRb);
                    canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                    String text = String.valueOf((int) a);
                    canvas.drawText(text, ptLt.x - textSize - 5, ptLt.y, paint);
                    if (ptLt.x < 0) canvas.drawText(text, 2, ptLt.y, paint);

                    ptLt = new MyPoint(a, 0);
                    ptRb = new MyPoint(a, YangdiMgr.YD_SIZE);
                    mapToScreen(ptLt);
                    mapToScreen(ptRb);
                    canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                    canvas.drawText(text, ptLt.x, ptLt.y + textSize + 3, paint);
                    if (ptLt.y > view_height) canvas.drawText(text, ptLt.x, view_height - 2, paint);

                    a += interval;
                }

                ptLt = new MyPoint(0, 0);
                mapToScreen(ptLt);
                canvas.drawText("0", ptLt.x - textSize - 5, ptLt.y, paint);
                if (ptLt.x < 0) canvas.drawText("0", 2, ptLt.y, paint);
            }

            int yf = YangdiMgr.GetYfwz(ydh);
            if (Qianqimgr.IsYangfang(ydh)) {
                paint.setStyle(Style.FILL);
                paint.setColor(0xff80d080);
                if (yf == 1) {
                    ptLt = new MyPoint(-3, 2);
                    ptRb = new MyPoint(-1, 0);
                    mapToScreen(ptLt);
                    mapToScreen(ptRb);
                    canvas.drawRect(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                }
                if (yf == 2) {
                    ptLt = new MyPoint(0, YangdiMgr.YD_SIZE + 3);
                    ptRb = new MyPoint(2, YangdiMgr.YD_SIZE + 1);
                    mapToScreen(ptLt);
                    mapToScreen(ptRb);
                    canvas.drawRect(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                }
                if (yf == 3) {
                    ptLt = new MyPoint(YangdiMgr.YD_SIZE + 1, YangdiMgr.YD_SIZE);
                    ptRb = new MyPoint(YangdiMgr.YD_SIZE + 3, YangdiMgr.YD_SIZE - 2);
                    mapToScreen(ptLt);
                    mapToScreen(ptRb);
                    canvas.drawRect(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                }
                if (yf == 4) {
                    ptLt = new MyPoint(YangdiMgr.YD_SIZE - 2, -1);
                    ptRb = new MyPoint(YangdiMgr.YD_SIZE, -3);
                    mapToScreen(ptLt);
                    mapToScreen(ptRb);
                    canvas.drawRect(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                }

                paint.setStyle(Style.STROKE);
                paint.setColor(0xff8080d0);
                ptLt = new MyPoint(-3, 2);
                ptRb = new MyPoint(-1, 0);
                mapToScreen(ptLt);
                mapToScreen(ptRb);
                canvas.drawRect(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(0, YangdiMgr.YD_SIZE + 3);
                ptRb = new MyPoint(2, YangdiMgr.YD_SIZE + 1);
                mapToScreen(ptLt);
                mapToScreen(ptRb);
                canvas.drawRect(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(YangdiMgr.YD_SIZE + 1, YangdiMgr.YD_SIZE);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE + 3, YangdiMgr.YD_SIZE - 2);
                mapToScreen(ptLt);
                mapToScreen(ptRb);
                canvas.drawRect(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(YangdiMgr.YD_SIZE - 2, -1);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, -3);
                mapToScreen(ptLt);
                mapToScreen(ptRb);
                canvas.drawRect(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
            }

            int count = lstYangmus.size();


            lstLabels.clear();
            paint.setStyle(Style.FILL);
            paint.setTextSize(textSize);
            for (int i = 0; i < count; i++) {
                Yangmu ym = lstYangmus.get(i);
                float x = ym.GetX();
                float y = ym.GetY();
                MyPoint pt = new MyPoint(x, y);
                mapToScreen(pt);
                x = pt.x;
                y = pt.y;

                LabelInfo label = new LabelInfo();
                label.label = String.valueOf(ym.ymh);
                float height = textSize;
                float width = paint.measureText(label.label);
                label.x = x;
                label.y = y;
                label.box = new MyRect();
                label.box.left = x + r1 + r2;
                label.box.top = y + r1 - r3;
                label.box.bottom = label.box.top - height;
                label.box.right = label.box.left + width;
                label.box.adjust();
                if (iChecked == i) {
                    label.color = 0xff0000f0;
                } else if (ym.jczt == 0) {
                    label.color = 0xffff0000;
                } else if (ym.jclx == 13 || ym.jclx == 14 || ym.jclx == 15 || ym.jclx == 17 || ym.jclx == 24) {
                    label.color = 0xff000000;
                } else {
                    label.color = 0xff00ff00;
                }
                lstLabels.add(label);

                if (iChecked == i) {
                    paint.setColor(0x8800d0f0);
                    canvas.drawCircle(x, y, r1, paint);
                    paint.setColor(0xff0000f0);
                    canvas.drawCircle(x, y, r2, paint);

                    if (dpiScale > 1.9 && dpiScale < 2.1) {
                        paint.setColor(0xff0000ff);
                        Path p = new Path();
                        p.reset();
                        p.moveTo(x, y + 28);
                        p.lineTo(x - 10.6f, y + 17.4f);
                        p.lineTo(x + 10.6f, y + 17.4f);
                        p.close();
                        canvas.drawPath(p, paint);

                        p.reset();
                        p.moveTo(x, y - 28);
                        p.lineTo(x - 10.6f, y - 17.4f);
                        p.lineTo(x + 10.6f, y - 17.4f);
                        p.close();
                        canvas.drawPath(p, paint);

                        p.reset();
                        p.moveTo(x + 28, y);
                        p.lineTo(x + 17.4f, y + 10.6f);
                        p.lineTo(x + 17.4f, y - 10.6f);
                        p.close();
                        canvas.drawPath(p, paint);

                        p.reset();
                        p.moveTo(x - 28, y);
                        p.lineTo(x - 17.4f, y + 10.6f);
                        p.lineTo(x - 17.4f, y - 10.6f);
                        p.close();
                        canvas.drawPath(p, paint);
                    } else {
                        paint.setColor(0xff0000ff);
                        Path p = new Path();
                        p.reset();
                        p.moveTo(x, y + 20);
                        p.lineTo(x - 7.07f, y + 12.92f);
                        p.lineTo(x + 7.07f, y + 12.92f);
                        p.close();
                        canvas.drawPath(p, paint);

                        p.reset();
                        p.moveTo(x, y - 20);
                        p.lineTo(x - 7.07f, y - 12.92f);
                        p.lineTo(x + 7.07f, y - 12.92f);
                        p.close();
                        canvas.drawPath(p, paint);

                        p.reset();
                        p.moveTo(x + 20, y);
                        p.lineTo(x + 12.92f, y + 7.07f);
                        p.lineTo(x + 12.92f, y - 7.07f);
                        p.close();
                        canvas.drawPath(p, paint);

                        p.reset();
                        p.moveTo(x - 20, y);
                        p.lineTo(x - 12.92f, y + 7.07f);
                        p.lineTo(x - 12.92f, y - 7.07f);
                        p.close();
                        canvas.drawPath(p, paint);
                    }
                } else if (ym.jczt == 0)//未检尺
                {
                    if (Resmgr.IsZhenye(ym.szdm)) {
                        paint.setColor(0x88d08080);
                        Path p = new Path();
                        p.moveTo(x, y - r1);
                        p.lineTo(x - r1, y + r1 - r3);
                        p.lineTo(x + r1, y + r1 - r3);
                        p.close();
                        canvas.drawPath(p, paint);
                        paint.setColor(0xffff0000);
                        canvas.drawCircle(x, y, r3, paint);
                    } else {
                        paint.setColor(0x88d08080);
                        canvas.drawCircle(x, y, r1, paint);
                        paint.setColor(0xffff0000);
                        canvas.drawCircle(x, y, r2, paint);
                    }
                } else if (ym.jclx == 13 || ym.jclx == 14 || ym.jclx == 15 || ym.jclx == 17 || ym.jclx == 24)//枯倒采
                {
                    if (Resmgr.IsZhenye(ym.szdm)) {
                        paint.setColor(0x88a0a0a0);
                        Path p = new Path();
                        p.moveTo(x, y - r1);
                        p.lineTo(x - r1, y + r1 - r3);
                        p.lineTo(x + r1, y + r1 - r3);
                        p.close();
                        canvas.drawPath(p, paint);
                        paint.setColor(0xff000000);
                        canvas.drawCircle(x, y, r3, paint);
                    } else {
                        paint.setColor(0x88a0a0a0);
                        canvas.drawCircle(x, y, r1, paint);
                        paint.setColor(0xff000000);
                        canvas.drawCircle(x, y, r2, paint);
                    }
                } else//正常
                {
                    if (Resmgr.IsZhenye(ym.szdm)) {
                        paint.setColor(0xffc0f0c0);
                        Path p = new Path();
                        p.moveTo(x, y - r1);
                        p.lineTo(x - r1, y + r1 - r3);
                        p.lineTo(x + r1, y + r1 - r3);
                        p.close();
                        canvas.drawPath(p, paint);
                        paint.setColor(0xff40ff40);
                        canvas.drawCircle(x, y, r3, paint);
                    } else {
                        paint.setColor(0xffc0f0c0);
                        canvas.drawCircle(x, y, r1, paint);
                        paint.setColor(0xff40ff40);
                        canvas.drawCircle(x, y, r2, paint);
                    }
                }
            }

            adjustLabel(textSize, r1);
            paint.setStyle(Style.FILL);
            paint.setTextSize(textSize);
            for (int i = 0; i < lstLabels.size(); i++) {
                LabelInfo label = lstLabels.get(i);
                paint.setColor(label.color);
                canvas.drawText(label.label, label.box.left, label.box.top, paint);
            }

            if (tmpYangmu != null) {
                float x = tmpYangmu.GetX();
                float y = tmpYangmu.GetY();
                MyPoint pt = new MyPoint(x, y);
                mapToScreen(pt);
                x = pt.x;
                y = pt.y;
                paint.setStyle(Style.STROKE);
                paint.setStrokeWidth(3);
                paint.setColor(0xffc0c0f0);
                canvas.drawCircle(x, y, r1, paint);
                paint.setColor(0xff4040ff);
                canvas.drawCircle(x, y, r2, paint);
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int count = YangdiMgr.GetYangmuCount(ydh);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    xBegin = event.getX();
                    yBegin = event.getY();
                    if (iDrawType == 1 || iDrawType == 2) {
                        cmdLine = new ArrayList<MyPoint>();
                    } else if (iChecked >= 0 && iChecked < count) {
                        //MyPoint pt = new MyPoint(xBegin, yBegin);
                        //mapToScreen(pt);
                        float x0 = xBegin;
                        float y0 = yBegin;
                        Yangmu ym = lstYangmus.get(iChecked);
                        float x = ym.GetX();
                        float y = ym.GetY();
                        MyPoint pt2 = new MyPoint(x, y);
                        mapToScreen(pt2);
                        x = pt2.x;
                        y = pt2.y;
                        float dx = x0 - x;
                        float dy = y0 - y;
                        dx = dx < 0 ? -dx : dx;
                        dy = dy < 0 ? -dy : dy;
                        int r = (int) (25 * dpiScale);
                        if (dx < r && dy < r) {
                            isMoveYm = true;
                        }
                    }
                }
                break;
                case MotionEvent.ACTION_UP: {
                    if (iDrawType == 1 || iDrawType == 2) {
                        float x = event.getX();
                        float y = event.getY();
                        MyPoint pt = new MyPoint(x, y);
                        screenToMap(pt);
                        cmdLine.add(pt);
                        MyCommand myCommand = new MyCommand();
                        if (iDrawType == 1) {
                            myCommand.color = color;
                            myCommand.width = width;
                        }
                        if (iDrawType == 2) {
                            myCommand.color = 0xfff0f0ff;
                            myCommand.width = earserWidth;
                        }
                        myCommand.lstPts = cmdLine;
                        lstCmds.add(myCommand);
                        tmpLine.clear();
                        postInvalidate();
                    } else if (isMoveYm) {
                        Yangmu ym = lstYangmus.get(iChecked);
                        YangdiMgr.UpdateYangmu(ydh, ym);
                        initYangmu(ym);
                        save();
                        isMoveYm = false;
                    }
                }
                break;
                case MotionEvent.ACTION_MOVE: {
                    xEnd = event.getX();
                    yEnd = event.getY();
                    float distanceX = xEnd > xBegin ? xEnd - xBegin : xBegin - xEnd;
                    float distanceY = yEnd > yBegin ? yEnd - yBegin : yBegin - yEnd;

                    if (iDrawType == 1 || iDrawType == 2) {
                        MyPoint pt = new MyPoint(xEnd, yEnd);
                        screenToMap(pt);
                        cmdLine.add(pt);
                        tmpLine.add(pt);
                        postInvalidate();
                    } else if (isMoveYm) {
                        Yangmu ym = lstYangmus.get(iChecked);
                        float dx = xEnd - xBegin;
                        float dy = yEnd - yBegin;
                        xBegin = xEnd;
                        yBegin = yEnd;
                        dx /= curScale;
                        dy /= curScale;
                        ym.Move(dx, -dy);
                        myView.postInvalidate();
                    } else if (distanceX > 10 && distanceY > 10) {
                        move(xEnd - xBegin, yEnd - yBegin);
                        xBegin = xEnd;
                        yBegin = yEnd;
                        postInvalidate();
                    }
                }
                break;
            }
            return mGestureDetector.onTouchEvent(event);
        }

        public void RefreshMap() {
            isRefresh = true;
            postInvalidate();
        }

        public void RefreshOverlay() {
            isRefresh = false;
            postInvalidate();
        }

        private void drawMap(Canvas canvas) {

        }

        private void drawDitu(Canvas canvas) {
            MyPoint ptLt = null;
            MyPoint ptRb = null;

            ptLt = new MyPoint(0, YangdiMgr.YD_SIZE);
            ptRb = new MyPoint(YangdiMgr.YD_SIZE, 0);
            mapToScreen(ptLt);
            mapToScreen(ptRb);
            float s = (ptRb.x - ptLt.x) / (bmpBackground.getHeight() - intv * 2);//
            Matrix m = new Matrix();
            m.setScale(s, s);
            m.postTranslate(ptLt.x - intv * s, ptLt.y - intv * s);//
            canvas.drawBitmap(bmpBackground, m, paint);
        }

        private void drawLines(Canvas canvas) {

        }

        private void drawWangge(Canvas canvas) {

        }

        private void drawYangmu(Canvas canvas) {

        }

        private void drawSelect(Canvas canvas) {
            Yangmu ym = lstYangmus.get(iChecked);
            float x = ym.GetX();
            float y = ym.GetY();
            MyPoint pt = new MyPoint(x, y);
            mapToScreen(pt);
            x = pt.x;
            y = pt.y;

            paint.setColor(0x8800d0f0);
            canvas.drawCircle(x, y, r1, paint);
            paint.setColor(0xff0000f0);
            canvas.drawCircle(x, y, r2, paint);

            if (dpiScale > 1.9 && dpiScale < 2.1) {
                paint.setColor(0xff0000ff);
                Path p = new Path();
                p.reset();
                p.moveTo(x, y + 28);
                p.lineTo(x - 10.6f, y + 17.4f);
                p.lineTo(x + 10.6f, y + 17.4f);
                p.close();
                canvas.drawPath(p, paint);

                p.reset();
                p.moveTo(x, y - 28);
                p.lineTo(x - 10.6f, y - 17.4f);
                p.lineTo(x + 10.6f, y - 17.4f);
                p.close();
                canvas.drawPath(p, paint);

                p.reset();
                p.moveTo(x + 28, y);
                p.lineTo(x + 17.4f, y + 10.6f);
                p.lineTo(x + 17.4f, y - 10.6f);
                p.close();
                canvas.drawPath(p, paint);

                p.reset();
                p.moveTo(x - 28, y);
                p.lineTo(x - 17.4f, y + 10.6f);
                p.lineTo(x - 17.4f, y - 10.6f);
                p.close();
                canvas.drawPath(p, paint);
            } else {
                paint.setColor(0xff0000ff);
                Path p = new Path();
                p.reset();
                p.moveTo(x, y + 20);
                p.lineTo(x - 7.07f, y + 12.92f);
                p.lineTo(x + 7.07f, y + 12.92f);
                p.close();
                canvas.drawPath(p, paint);

                p.reset();
                p.moveTo(x, y - 20);
                p.lineTo(x - 7.07f, y - 12.92f);
                p.lineTo(x + 7.07f, y - 12.92f);
                p.close();
                canvas.drawPath(p, paint);

                p.reset();
                p.moveTo(x + 20, y);
                p.lineTo(x + 12.92f, y + 7.07f);
                p.lineTo(x + 12.92f, y - 7.07f);
                p.close();
                canvas.drawPath(p, paint);

                p.reset();
                p.moveTo(x - 20, y);
                p.lineTo(x - 12.92f, y + 7.07f);
                p.lineTo(x - 12.92f, y - 7.07f);
                p.close();
                canvas.drawPath(p, paint);
            }
        }

        public void moveTo(int ymh) {
            Yangmu ym = YangdiMgr.GetYangmu(ydh, ymh);
            float x = ym.GetX();
            float y = ym.GetY();
            MyPoint pt = new MyPoint(x, y);
            mapToScreen(pt);
            x = pt.x;
            y = pt.y;

            if (x > 10 && x < view_width - 10 && y > 10 && y < view_height - 10) {
                this.postInvalidate();
                return;
            }

            move(view_size / 2 - x, view_size / 2 - y);

            this.postInvalidate();
        }

        private void zoomTo(float scale) {
            if (scale < minScale) scale = minScale;
            if (scale > maxScale) scale = maxScale;
            float s = scale / curScale;
            MyPoint pt = new MyPoint(view_size / 2, view_size / 2);
            screenToMap(pt);
            float dx = pt.x * curScale * (1 - s);
            float dy = pt.y * curScale * (s - 1);
            move(dx, dy);
            curScale = scale;
        }

        public void ZoomIn() {
            zoomTo(curScale * 1.2f);
            postInvalidate();
        }

        public void ZoomOut() {
            zoomTo(curScale * 0.8f);
            postInvalidate();
        }

        //全图显示，vw,vh为显示区像素大小
        public void ZoomToAll() {
            curScale = initScale;
            mapOriginX = view_size / 2 - YangdiMgr.YD_SIZE / 2 * initScale;
            mapOriginY = view_size + (YangdiMgr.YD_SIZE / 2 * initScale - view_size / 2);
            postInvalidate();
        }

        private void move(float dx, float dy) {
            mapOriginX += dx;
            mapOriginY += dy;
        }

        public void Revoke() {
            int n = lstCmds.size();
            if (n > 0) {
                MyCommand tmp = lstCmds.get(n - 1);
                lstDels.add(tmp);
                lstCmds.remove(n - 1);
                postInvalidate();
            }
        }

        public void Restore() {
            int n = lstDels.size();
            if (n > 0) {
                MyCommand tmp = lstDels.get(n - 1);
                lstCmds.add(tmp);
                lstDels.remove(n - 1);
                postInvalidate();
            }
        }

        public void screenToMap(MyPoint pt) {
            pt.x = (pt.x - mapOriginX) / curScale;
            pt.y = (mapOriginY - pt.y) / curScale;
        }

        public void mapToScreen(MyPoint pt) {
            pt.x = pt.x * curScale + mapOriginX;
            pt.y = mapOriginY - pt.y * curScale;
        }

        //保存底图
        public void SaveDitu() {
            Canvas canvas = new Canvas(bmpBackground);
            int size = bmpBackground.getHeight() - intv * 2;
            float scale = size / YangdiMgr.YD_SIZE;
            float ox = size / 2 - YangdiMgr.YD_SIZE / 2 * scale;
            float oy = size + (YangdiMgr.YD_SIZE / 2 * scale - size / 2);
            ox += intv;
            oy += intv;

            paint.reset();

            for (int i = 0; i < lstCmds.size(); i++) {
                List<MyPoint> pts = lstCmds.get(i).lstPts;
                if (pts.size() == 1) {
                    paint.setStyle(Style.FILL);
                    paint.setStrokeWidth(lstCmds.get(i).width);
                    paint.setColor(lstCmds.get(i).color);
                    MyPoint pt = new MyPoint(pts.get(0));
                    pt.x = pt.x * scale + ox;
                    pt.y = oy - pt.y * scale;
                    canvas.drawCircle(pt.x, pt.y, width, paint);
                } else if (pts.size() > 1) {
                    paint.setStyle(Style.STROKE);
                    paint.setStrokeWidth(lstCmds.get(i).width);
                    paint.setColor(lstCmds.get(i).color);
                    Path path = new Path();
                    MyPoint pt = new MyPoint(pts.get(0));
                    pt.x = pt.x * scale + ox;
                    pt.y = oy - pt.y * scale;
                    path.moveTo(pt.x, pt.y);
                    for (int j = 1; j < pts.size(); j++) {
                        pt = new MyPoint(pts.get(j));
                        pt.x = pt.x * scale + ox;
                        pt.y = oy - pt.y * scale;
                        path.lineTo(pt.x, pt.y);
                    }
                    canvas.drawPath(path, paint);
                }
            }

            lstCmds.clear();
            lstDels.clear();

            String pic = MyConfig.GetWorkdir() + "/temp/ymdt.jpg";
            try {
                FileOutputStream outStream = new FileOutputStream(pic);
                bmpBackground.compress(CompressFormat.PNG, 100, outStream);
                outStream.close();
                YangdiMgr.SetYangmutuDitu(ydh, pic);
            } catch (IOException e) {
                e.printStackTrace();
            }

            postInvalidate();
        }

        private void mapToScreen(MyPoint pt, float ox, float oy, float scale) {
            pt.x = pt.x * scale + ox;
            pt.y = oy - pt.y * scale;
        }

        //保存样木位置图
        public void SaveYmwzt() {
            int initSize = view_size - intv;
            int aa = initSize + intv * 2;
            Bitmap bmp = Bitmap.createBitmap(aa, aa, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            paint.reset();
            canvas.drawBitmap(bmpBackground, 0, 0, paint);
            int size = bmp.getHeight() - intv * 2;
            float scale = size / YangdiMgr.YD_SIZE;
            float ox = size / 2 - YangdiMgr.YD_SIZE / 2 * scale;
            float oy = size + (YangdiMgr.YD_SIZE / 2 * scale - size / 2);
            ox += intv;
            oy += intv;

            MyPoint ptLt = null;
            MyPoint ptRb = null;

            if (YangdiMgr.YMT_WG_TYPE == 0) {
                paint.reset();
                paint.setStyle(Style.STROKE);
                paint.setColor(0xffd0d0d0);
                paint.setTextSize(textSize);
                ptLt = new MyPoint(0, 0);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, 0);
                mapToScreen(ptLt, ox, oy, scale);
                mapToScreen(ptRb, ox, oy, scale);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(0, 0);
                ptRb = new MyPoint(0, YangdiMgr.YD_SIZE);
                mapToScreen(ptLt, ox, oy, scale);
                mapToScreen(ptRb, ox, oy, scale);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(YangdiMgr.YD_SIZE, 0);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, YangdiMgr.YD_SIZE);
                mapToScreen(ptLt, ox, oy, scale);
                mapToScreen(ptRb, ox, oy, scale);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(0, YangdiMgr.YD_SIZE);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, YangdiMgr.YD_SIZE);
                mapToScreen(ptLt, ox, oy, scale);
                mapToScreen(ptRb, ox, oy, scale);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);

                paint.setColor(0xffd0d0d0);
                ptLt = new MyPoint(0, 0);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, YangdiMgr.YD_SIZE);
                mapToScreen(ptLt, ox, oy, scale);
                mapToScreen(ptRb, ox, oy, scale);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(0, YangdiMgr.YD_SIZE);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, 0);
                mapToScreen(ptLt, ox, oy, scale);
                mapToScreen(ptRb, ox, oy, scale);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(0, YangdiMgr.YD_SIZE / 2);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, YangdiMgr.YD_SIZE / 2);
                mapToScreen(ptLt, ox, oy, scale);
                mapToScreen(ptRb, ox, oy, scale);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(YangdiMgr.YD_SIZE / 2, YangdiMgr.YD_SIZE);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE / 2, 0);
                mapToScreen(ptLt, ox, oy, scale);
                mapToScreen(ptRb, ox, oy, scale);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);

                ptLt = new MyPoint(YangdiMgr.YD_SIZE / 2, YangdiMgr.YD_SIZE / 2);
                mapToScreen(ptLt, ox, oy, scale);
                float r11 = 2 * curScale;
                canvas.drawCircle(ptLt.x, ptLt.y, r11, paint);
                canvas.drawText("2", ptLt.x + r11, ptLt.y - 3, paint);
                float r12 = 4 * curScale;
                canvas.drawCircle(ptLt.x, ptLt.y, r12, paint);
                canvas.drawText("4", ptLt.x + r12, ptLt.y - 3, paint);
                float r13 = 6 * curScale;
                canvas.drawCircle(ptLt.x, ptLt.y, r13, paint);
                canvas.drawText("6", ptLt.x + r13, ptLt.y - 3, paint);
                float r14 = 8 * curScale;
                canvas.drawCircle(ptLt.x, ptLt.y, r14, paint);
                canvas.drawText("8", ptLt.x + r14, ptLt.y - 3, paint);
                float r15 = 10 * curScale;
                canvas.drawCircle(ptLt.x, ptLt.y, r15, paint);
                canvas.drawText("10", ptLt.x + r15, ptLt.y - 3, paint);
                float r16 = 12 * curScale;
                canvas.drawCircle(ptLt.x, ptLt.y, r16, paint);
                canvas.drawText("12", ptLt.x + r16, ptLt.y - 3, paint);
                float r17 = 14 * curScale;
                RectF box = new RectF(ptLt.x - r17, ptLt.y - r17, ptLt.x + r17, ptLt.y + r17);
                canvas.drawArc(box, 23, 44, false, paint);
                canvas.drawArc(box, 113, 44, false, paint);
                canvas.drawArc(box, 203, 44, false, paint);
                canvas.drawArc(box, 293, 44, false, paint);
                float r18 = 16 * curScale;
                box = new RectF(ptLt.x - r18, ptLt.y - r18, ptLt.x + r18, ptLt.y + r18);
                canvas.drawArc(box, 36, 18, false, paint);
                canvas.drawArc(box, 126, 18, false, paint);
                canvas.drawArc(box, 216, 18, false, paint);
                canvas.drawArc(box, 306, 18, false, paint);
            } else {
                paint.reset();
                paint.setStyle(Style.STROKE);
                paint.setColor(0xffd0d0d0);
                paint.setTextSize(textSize);
                ptLt = null;
                ptRb = null;

                ptLt = new MyPoint(0, 0);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, 0);
                mapToScreen(ptLt, ox, oy, scale);
                mapToScreen(ptRb, ox, oy, scale);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(0, 0);
                ptRb = new MyPoint(0, YangdiMgr.YD_SIZE);
                mapToScreen(ptLt, ox, oy, scale);
                mapToScreen(ptRb, ox, oy, scale);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(YangdiMgr.YD_SIZE, 0);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, YangdiMgr.YD_SIZE);
                mapToScreen(ptLt, ox, oy, scale);
                mapToScreen(ptRb, ox, oy, scale);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                ptLt = new MyPoint(0, YangdiMgr.YD_SIZE);
                ptRb = new MyPoint(YangdiMgr.YD_SIZE, YangdiMgr.YD_SIZE);
                mapToScreen(ptLt, ox, oy, scale);
                mapToScreen(ptRb, ox, oy, scale);
                canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);

                float a = interval;
                while (a < YangdiMgr.YD_SIZE) {
                    ptLt = new MyPoint(0, a);
                    ptRb = new MyPoint(YangdiMgr.YD_SIZE, a);
                    mapToScreen(ptLt, ox, oy, scale);
                    mapToScreen(ptRb, ox, oy, scale);
                    canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                    String text = String.valueOf((int) a);
                    canvas.drawText(text, ptLt.x - textSize - 5, ptLt.y, paint);
                    if (ptLt.x < 0) canvas.drawText(text, 2, ptLt.y, paint);

                    ptLt = new MyPoint(a, 0);
                    ptRb = new MyPoint(a, YangdiMgr.YD_SIZE);
                    mapToScreen(ptLt, ox, oy, scale);
                    mapToScreen(ptRb, ox, oy, scale);
                    canvas.drawLine(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                    canvas.drawText(text, ptLt.x, ptLt.y + textSize + 3, paint);
                    if (ptLt.y > view_height) canvas.drawText(text, ptLt.x, view_height - 2, paint);

                    a += interval;
                }

                ptLt = new MyPoint(0, 0);
                mapToScreen(ptLt, ox, oy, scale);
                canvas.drawText("0", ptLt.x - textSize - 5, ptLt.y, paint);
                if (ptLt.x < 0) canvas.drawText("0", 2, ptLt.y, paint);
            }

            int yf = YangdiMgr.GetYfwz(ydh);
            if (Qianqimgr.IsYangfang(ydh)) {
                paint.setStyle(Style.FILL);
                paint.setColor(0xff000000);
                if (yf == 1) {
                    ptLt = new MyPoint(-3, 2);
                    ptRb = new MyPoint(-1, 0);
                    mapToScreen(ptLt, ox, oy, scale);
                    mapToScreen(ptRb, ox, oy, scale);
                    canvas.drawRect(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                }
                if (yf == 2) {
                    ptLt = new MyPoint(0, YangdiMgr.YD_SIZE + 3);
                    ptRb = new MyPoint(2, YangdiMgr.YD_SIZE + 1);
                    mapToScreen(ptLt, ox, oy, scale);
                    mapToScreen(ptRb, ox, oy, scale);
                    canvas.drawRect(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                }
                if (yf == 3) {
                    ptLt = new MyPoint(YangdiMgr.YD_SIZE + 1, YangdiMgr.YD_SIZE);
                    ptRb = new MyPoint(YangdiMgr.YD_SIZE + 3, YangdiMgr.YD_SIZE - 2);
                    mapToScreen(ptLt, ox, oy, scale);
                    mapToScreen(ptRb, ox, oy, scale);
                    canvas.drawRect(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                }
                if (yf == 4) {
                    ptLt = new MyPoint(YangdiMgr.YD_SIZE - 2, -1);
                    ptRb = new MyPoint(YangdiMgr.YD_SIZE, -3);
                    mapToScreen(ptLt, ox, oy, scale);
                    mapToScreen(ptRb, ox, oy, scale);
                    canvas.drawRect(ptLt.x, ptLt.y, ptRb.x, ptRb.y, paint);
                }
            }

            int count = lstYangmus.size();

            lstLabels.clear();
            paint.setTextSize(textSize);
            paint.setColor(0xff000000);
            for (int i = 0; i < count; i++) {
                Yangmu ym = lstYangmus.get(i);
                if (ym.jclx == 13 || ym.jclx == 14 || ym.jclx == 15 || ym.jclx == 17 || ym.jclx == 24) {
                    continue;
                }
                if (Resmgr.IsZhuzi(ym.szdm)) {
                    continue;
                }
                float x = ym.GetX();
                float y = ym.GetY();
                MyPoint pt = new MyPoint(x, y);
                mapToScreen(pt, ox, oy, scale);
                x = pt.x;
                y = pt.y;

                LabelInfo label = new LabelInfo();
                label.label = String.valueOf(ym.ymh);
                float height = textSize;
                float width = paint.measureText(label.label);
                label.x = x;
                label.y = y;
                label.box = new MyRect();
                label.box.left = x + r1 + r2;
                label.box.top = y + r1 - r3;
                label.box.bottom = label.box.top - height;
                label.box.right = label.box.left + width;
                label.box.adjust();
                lstLabels.add(label);

                if (Resmgr.IsZhenye(ym.szdm)) {
                    paint.setStyle(Style.STROKE);
                    Path p = new Path();
                    p.moveTo(x, y - r1);
                    p.lineTo(x - r1, y + r1 - r3);
                    p.lineTo(x + r1, y + r1 - r3);
                    p.close();
                    canvas.drawPath(p, paint);
                    paint.setStyle(Style.FILL);
                    canvas.drawCircle(x, y, r3, paint);
                } else {
                    paint.setStyle(Style.STROKE);
                    canvas.drawCircle(x, y, r1, paint);
                    paint.setStyle(Style.FILL);
                    canvas.drawCircle(x, y, r2, paint);
                }
            }
            adjustLabel(textSize, r1);
            paint.setStyle(Style.FILL);
            paint.setTextSize(textSize);
            paint.setColor(0xff000000);
            for (int i = 0; i < lstLabels.size(); i++) {
                LabelInfo label = lstLabels.get(i);
                canvas.drawText(label.label, label.box.left, label.box.top, paint);
            }

            String pic = MyConfig.GetWorkdir() + "/temp/ymt.jpg";
            try {
                FileOutputStream outStream = new FileOutputStream(pic);
                bmp.compress(CompressFormat.PNG, 100, outStream);
                outStream.close();
                YangdiMgr.SetYangmutu(ydh, pic);
            } catch (IOException e) {
                e.printStackTrace();
            }

            postInvalidate();
        }

        public void ResetImage() {
            Canvas cv = new Canvas(bmpBackground);
            cv.drawColor(0xfff0f0ff);
            postInvalidate();
        }

        public void Release() {
            bmpBackground.recycle();
        }

        class LearnGestureListener extends GestureDetector.SimpleOnGestureListener {
            @Override
            public boolean onSingleTapUp(MotionEvent ev) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent ev) {
            }

            @Override
            public void onLongPress(MotionEvent ev) {
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return true;
            }

            @Override
            public boolean onDown(MotionEvent ev) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return true;
            }

            //轻击事件
            @Override
            public boolean onSingleTapConfirmed(MotionEvent ev) {
                float x0 = ev.getX();
                float y0 = ev.getY();
                if (iYmType == 0) {
                    iChecked = -1;
                    int count = lstYangmus.size();
                    for (int i = 0; i < count; i++) {
                        Yangmu ym = lstYangmus.get(i);
                        float x = ym.GetX();
                        float y = ym.GetY();
                        MyPoint pt = new MyPoint(x, y);
                        mapToScreen(pt);
                        x = pt.x;
                        y = pt.y;
                        float f1 = x - x0;
                        float f2 = y - y0;
                        f1 = f1 > 0 ? f1 : -f1;
                        f2 = f2 > 0 ? f2 : -f2;
                        int dx = 15;
                        if (dpiScale > 1.9 && dpiScale < 2.1) dx = 30;
                        if (f1 < dx && f2 < dx) {
                            iChecked = i;
                            initYangmu(ym);
                            break;
                        }
                    }
                    if (iChecked == -1 && Qianqimgr.IsYangfang(ydh)) {
                        int yf = YangdiMgr.GetYfwz(ydh);
                        MyPoint pt = new MyPoint(x0, y0);
                        screenToMap(pt);
                        MyRect rect = new MyRect(-3, 2, -1, 0);
                        if (rect.isPointInRect(pt)) {
                            yf = 1;
                        }
                        rect = new MyRect(0, YangdiMgr.YD_SIZE + 3, 2, YangdiMgr.YD_SIZE + 1);
                        if (rect.isPointInRect(pt)) {
                            yf = 2;
                        }
                        rect = new MyRect(YangdiMgr.YD_SIZE + 1, YangdiMgr.YD_SIZE, YangdiMgr.YD_SIZE + 3, YangdiMgr.YD_SIZE - 2);
                        if (rect.isPointInRect(pt)) {
                            yf = 3;
                        }
                        rect = new MyRect(YangdiMgr.YD_SIZE - 2, -1, YangdiMgr.YD_SIZE, -3);
                        if (rect.isPointInRect(pt)) {
                            yf = 4;
                        }
                        YangdiMgr.SetYfwz(ydh, yf);
                    }
                    if (iChecked == -1) {
                        initYangmu(null);
                    }
                } else if (iYmType == 1) {
                    MyPoint pt = new MyPoint(x0, y0);
                    screenToMap(pt);
                    x0 = pt.x;
                    y0 = pt.y;

                    tmpYangmu = new Yangmu(x0, y0);
                    tmpYangmu.ymh = getNextYmh();
                    tmpYangmu.jclx = 12;
                    tmpYangmu.szdm = MyConfig.GetLastSzdm();
                    tmpYangmu.szmc = Resmgr.GetSzName(tmpYangmu.szdm);
                    tmpYangmu.kjdlxh = MyConfig.GetLastKjxh();
                    initYangmu(tmpYangmu);
                }
                MyView.this.postInvalidate();

                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent ev) {
                float x0 = ev.getX();
                float y0 = ev.getY();
                if (Qianqimgr.IsYangfang(ydh)) {
                    int yf = YangdiMgr.GetYfwz(ydh);
                    MyPoint pt = new MyPoint(x0, y0);
                    screenToMap(pt);
                    MyRect rect = new MyRect(-3, 2, -1, 0);
                    if (rect.isPointInRect(pt) && yf == 1) {
                        yf = 0;
                    }
                    rect = new MyRect(0, YangdiMgr.YD_SIZE + 3, 2, YangdiMgr.YD_SIZE + 1);
                    if (rect.isPointInRect(pt) && yf == 2) {
                        yf = 0;
                    }
                    rect = new MyRect(YangdiMgr.YD_SIZE + 1, YangdiMgr.YD_SIZE, YangdiMgr.YD_SIZE + 3, YangdiMgr.YD_SIZE - 2);
                    if (rect.isPointInRect(pt) && yf == 3) {
                        yf = 0;
                    }
                    rect = new MyRect(YangdiMgr.YD_SIZE - 2, -1, YangdiMgr.YD_SIZE, -3);
                    if (rect.isPointInRect(pt) && yf == 4) {
                        yf = 0;
                    }
                    YangdiMgr.SetYfwz(ydh, yf);
                    MyView.this.postInvalidate();
                }
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent ev) {
                return true;
            }
        }
    }

    class LabelInfo {
        public String label;
        public float x;//屏幕坐标
        public float y;
        public MyRect box;
        public int color;
    }

    private void adjustLabel(int textSize, int r) {
        if (lstLabels.size() < 2) return;
        float left = 0;
        float right = 0;
        float top = 0;
        float bottom = 0;
        float width = 0;
        float height = 0;
        for (int i = 1; i < lstLabels.size(); i++) {
            LabelInfo label = lstLabels.get(i);
            left = label.box.left;
            right = label.box.right;
            top = label.box.top;
            bottom = label.box.bottom;
            width = right - left;
            height = top - bottom;
            if (isCover(label, i))//左
            {
                label.box.left = left;
                label.box.right = right;
                label.box.top = top;
                label.box.bottom = bottom;
                label.box.left -= (width + r + r + 12);
                label.box.right -= (width + r + r + 12);
            }
            if (isCover(label, i))//右下
            {
                label.box.left = left;
                label.box.right = right;
                label.box.top = top;
                label.box.bottom = bottom;
                label.box.bottom += height + 1;
                label.box.top += height + 1;
            }
            if (isCover(label, i))//右上
            {
                label.box.left = left;
                label.box.right = right;
                label.box.top = top;
                label.box.bottom = bottom;
                label.box.bottom -= height - 1;
                label.box.top -= height - 1;
            }

            if (isCover(label, i))//左下
            {
                label.box.left = left;
                label.box.right = right;
                label.box.top = top;
                label.box.bottom = bottom;
                label.box.left -= (width + r + r + 12);
                label.box.right -= (width + r + r + 12);
                label.box.bottom += height + 1;
                label.box.top += height + 1;
            }
            if (isCover(label, i))//左上
            {
                label.box.left = left;
                label.box.right = right;
                label.box.top = top;
                label.box.bottom = bottom;
                label.box.left -= (width + r + r + 12);
                label.box.right -= (width + r + r + 12);
                label.box.bottom -= height - 1;
                label.box.top -= height - 1;
            }
            if (isCover(label, i))//中下
            {
                label.box.left = left;
                label.box.right = right;
                label.box.top = top;
                label.box.bottom = bottom;
                label.box.left = label.x - width / 2;
                label.box.right = label.box.left + width;
                label.box.top = label.y + height + r + 12;
                label.box.bottom = label.box.top - height;
            }
            if (isCover(label, i))//中上
            {
                label.box.left = left;
                label.box.right = right;
                label.box.top = top;
                label.box.bottom = bottom;
                label.box.left = label.x - width / 2;
                label.box.right = label.box.left + width;
                label.box.top = label.y - r - 12;
                label.box.bottom = label.box.top - height;
            }
            /*
			if(isCover(label, i))
			{
				label.box.left = left;
				label.box.right = right;
				label.box.top = top;
				label.box.bottom = bottom;
				label.box.left = left + r + r;
				label.box.right = right + r + r;
				label.box.top = label.box.top - height - 1;
				label.box.bottom = label.box.top - height;
			}
			if(isCover(label, i))
			{
				label.box.left = left;
				label.box.right = right;
				label.box.top = top;
				label.box.bottom = bottom;
				label.box.left = left + r + r;
				label.box.right = right + r + r;
				label.box.top = label.box.top + height + 1;
				label.box.bottom = label.box.top + height;
			}
			if(isCover(label, i))
			{
				label.box.left = left;
				label.box.right = right;
				label.box.top = top;
				label.box.bottom = bottom;
				label.box.left -= (width + r + r + 20);
				label.box.right -= (width + r + r + 20);
				label.box.top = label.box.top - height - 1;
				label.box.bottom = label.box.top - height;
			}
			if(isCover(label, i))
			{
				label.box.left = left;
				label.box.right = right;
				label.box.top = top;
				label.box.bottom = bottom;
				label.box.left -= (width + r + r + 20);
				label.box.right -= (width + r + r + 20);
				label.box.top = label.box.top + height + 1;
				label.box.bottom = label.box.top + height;
			}
			*/
            if (isCover(label, i)) {
                label.box.left = left;
                label.box.right = right;
                label.box.top = top;
                label.box.bottom = bottom;
            }
        }
    }

    private boolean isCover(LabelInfo label, int pos) {
        //int r1 = 28;
        boolean b = false;
        for (int i = 0; i < pos; i++) {
            if (lstLabels.get(i).box.isIntersectToRect(label.box)) {
                b = true;
                break;
            }
			/*
			MyRect box = new MyRect(lstLabels.get(i).x-r1, lstLabels.get(i).y+r1, lstLabels.get(i).x+r1, lstLabels.get(i).y-r1);
			if(box.isIntersectToRect(label.box))
			{
				b = true;
				break;
			}
			*/
        }
        return b;
    }
}
