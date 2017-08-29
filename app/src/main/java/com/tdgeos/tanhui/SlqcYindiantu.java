package com.tdgeos.tanhui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.write.Label;

import com.tdgeos.dlg.base.MyColorDialog;
import com.tdgeos.dlg.base.MyListDialog;
import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.dlg.yddc.YddwwDialog;
import com.tdgeos.dlg.yddc.YdzbDialog;
import com.tdgeos.lib.MyCoord;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.lib.MyPoint;
import com.tdgeos.yangdi.Cljl;
import com.tdgeos.yangdi.Qianqimgr;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.YangdiMgr;
import com.tdgeos.yangdi.Yddww;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Paint.Style;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SlqcYindiantu extends Activity implements View.OnClickListener {
    private int ydh = 0;
    private int iStatus = 0;

    private Button btnClose = null;
    private Button btnQianqi = null;
    private Button btnSave = null;
    private Button btnFinish = null;

    private LinearLayout lay = null;
    private MyView myView = null;
    private float dpiScale = 1;

    private EditText etZbfwj = null;
    private EditText etCfwj = null;
    private EditText etYxjl = null;
    private EditText etLc = null;

    private ImageButton btnColor = null;
    private SeekBar sbWidth = null;
    private ToggleButton tbPaint = null;
    private ToggleButton tbClean = null;
    private Button btnPrev = null;
    private Button btnNext = null;

    private Button btnTakephoto = null;
    private Button btnDelphoto = null;

    private Bitmap bmpColor = Bitmap.createBitmap(400, 100, Bitmap.Config.ARGB_4444);

    private Button btnAddDww = null;
    private Button btnDelDww = null;
    private LinearLayout layList = null;
    private LayoutInflater layInflater = null;
    private EditText etYdtz = null;
    private TextView tvYdzbgps = null;
    private TextView tvYdzbhzb = null;
    private Button btnInput = null;

    private LinearLayout layQq = null;
    private LinearLayout layQqList = null;
    private LinearLayout layQqWu = null;
    private CheckBox cbAll = null;
    private Button btnCopy = null;
    private boolean bVisible = false;
    private List<Yddww> lstQq = null;
    private EditText etQqYdtz = null;
    private EditText etQqZbfwj = null;
    private EditText etQqCfwj = null;
    private EditText etQqYxjl = null;
    private EditText etQqLc = null;
    private CheckBox cbQqYdtz = null;
    private CheckBox cbQqZbfwj = null;
    private CheckBox cbQqCfwj = null;
    private CheckBox cbQqYxjl = null;
    private CheckBox cbQqLc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.slqc_yindianwzt);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = this.getIntent();
        ydh = intent.getIntExtra("ydh", 0);
        int[] ii = YangdiMgr.GetDczt(ydh);
        iStatus = ii[1];

        dpiScale = getResources().getDisplayMetrics().densityDpi * 1.0f / 160;

        btnClose = (Button) findViewById(R.id.btn_close);
        btnQianqi = (Button) findViewById(R.id.btn_qq);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        btnClose.setOnClickListener(this);
        btnQianqi.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        myView = new MyView(this);
        lay = (LinearLayout) findViewById(R.id.lay_view);
        lay.addView(myView);

        etZbfwj = (EditText) findViewById(R.id.et_zbfwj);
        etCfwj = (EditText) findViewById(R.id.et_cfwj);
        etYxjl = (EditText) findViewById(R.id.et_yxjl);
        etLc = (EditText) findViewById(R.id.et_lc);
        etLc.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        btnTakephoto = (Button) findViewById(R.id.btn_take);
        btnDelphoto = (Button) findViewById(R.id.btn_delpic);
        btnColor = (ImageButton) findViewById(R.id.btn_color);
        tbPaint = (ToggleButton) findViewById(R.id.btn_painter);
        tbClean = (ToggleButton) findViewById(R.id.btn_eraser);
        btnPrev = (Button) findViewById(R.id.btn_pre);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnTakephoto.setOnClickListener(this);
        btnDelphoto.setOnClickListener(this);
        btnColor.setOnClickListener(this);
        tbPaint.setOnClickListener(this);
        tbClean.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        setColor(0xff000000);

        sbWidth = (SeekBar) findViewById(R.id.sb_width);
        sbWidth.setMax(20);
        sbWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                myView.SetPaintWidth(arg1);
            }
        });
        sbWidth.setProgress(3);

        btnAddDww = (Button) findViewById(R.id.btn_add);
        btnDelDww = (Button) findViewById(R.id.btn_del);
        btnAddDww.setOnClickListener(this);
        btnDelDww.setOnClickListener(this);
        btnInput = (Button) findViewById(R.id.btn_ydzb_input);
        btnInput.setOnClickListener(this);

        layList = (LinearLayout) findViewById(R.id.lay_list);
        layInflater = LayoutInflater.from(this);
        etYdtz = (EditText) findViewById(R.id.et_info);

        tvYdzbgps = (TextView) findViewById(R.id.tv_ydzb_gps);
        tvYdzbhzb = (TextView) findViewById(R.id.tv_ydzb_hzb);
        double[] dd = YangdiMgr.GetYdloc22(ydh);
        if (dd != null) {
            String slon = MyFuns.NumberToDegree(dd[0]);
            String slat = MyFuns.NumberToDegree(dd[1]);
            int hzb = (int) dd[2];
            int zzb = (int) dd[3];
            tvYdzbgps.setText("引点GPS坐标：" + slon + ",  " + slat);
            tvYdzbhzb.setText("引点横纵坐标：" + hzb + ",  " + zzb);
        }

        etZbfwj.addTextChangedListener(new TextWatcher() {
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
                    etZbfwj.setTextColor(Color.BLACK);
                } else {
                    etZbfwj.setTextColor(Color.RED);
                }
            }
        });

        etCfwj.addTextChangedListener(new TextWatcher() {
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
                    etCfwj.setTextColor(Color.BLACK);
                } else {
                    etCfwj.setTextColor(Color.RED);
                }
            }
        });
        /*
        etYxjl.addTextChangedListener(new TextWatcher() {
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
				try{
					f = Float.parseFloat(str);
				}catch(Exception e){}
				if(f >= YangdiMgr.MIN_YXCL_YXJL && f <= YangdiMgr.MAX_YXCL_YXJL)
				{
					etYxjl.setTextColor(Color.BLACK);
				}
				else
				{
					etYxjl.setTextColor(Color.RED);
				}
			}
		});
		*/
        etLc.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_LC && f <= YangdiMgr.MAX_LC) {
                    etLc.setTextColor(Color.BLACK);
                } else {
                    etLc.setTextColor(Color.RED);
                }
            }
        });

        String sql = "select zbfwj, cfwj, yxjl, lc, yindian from qt where ydh = '" + ydh + "'";
        String[][] sss = YangdiMgr.SelectData(ydh, sql);
        if (sss != null) {
            etZbfwj.setText(sss[0][0]);
            etCfwj.setText(sss[0][1]);
            etYxjl.setText(sss[0][2]);
            etLc.setText(sss[0][3]);
            etYdtz.setText(sss[0][4]);
        }
        //if(etYxjl.getText().toString().equals(""))
        //{
        //	float dis = getYxjl();
        //	if(dis > 0) etYxjl.setText(String.valueOf(dis));
        //}
        if (etZbfwj.getText().toString().equals("")) {
            if (dd != null) {
                int hzb = (int) dd[2];
                int zzb = (int) dd[3];
                MyCoord c = YangdiMgr.GetYdloc(ydh);
                if (c != null) {
                    YdzbDialog.GpsToBj54(c);
                } else {
                    c = Qianqimgr.GetYdloc(ydh);
                }
                if (c != null) {
                    float fwj = -1;
                    double x = c.x - hzb;
                    double y = c.y - zzb;
                    if (x > 0 && y > 0) {
                        fwj = (float) (Math.atan((x) / (y)) * 180 / Math.PI);
                    }
                    if (x > 0 && y < 0) {
                        fwj = 180 + (float) (Math.atan((x) / (y)) * 180 / Math.PI);
                    }
                    if (x < 0 && y < 0) {
                        fwj = 180 + (float) (Math.atan((x) / (y)) * 180 / Math.PI);
                    }
                    if (x < 0 && y > 0) {
                        fwj = 360 + (float) (Math.atan((x) / (y)) * 180 / Math.PI);
                    }
                    if (fwj >= 0) {
                        etZbfwj.setText(String.valueOf(fwj));
                        float cpj = YangdiMgr.GetYdcpj(ydh);
                        if (cpj >= 0) {
                            etCfwj.setText(String.valueOf(fwj + cpj));
                        }
                    }
                    double dis = Math.sqrt((c.x - hzb) * (c.x - hzb) + (c.y - zzb) * (c.y - zzb));
                    dis = MyFuns.MyDecimal(dis, 1);
                    etYxjl.setText(String.valueOf(dis));
                }
            }
        }

        layQq = (LinearLayout) findViewById(R.id.lay_qq);
        layQqList = (LinearLayout) findViewById(R.id.lay_qq_list);
        layQqWu = (LinearLayout) findViewById(R.id.lay_qq_wu);
        cbAll = (CheckBox) findViewById(R.id.cb_all);
        btnCopy = (Button) findViewById(R.id.btn_copy);
        btnCopy.setOnClickListener(this);

        etQqZbfwj = (EditText) findViewById(R.id.et_qq_zbfwj);
        etQqCfwj = (EditText) findViewById(R.id.et_qq_cfwj);
        etQqYxjl = (EditText) findViewById(R.id.et_qq_yxjl);
        etQqLc = (EditText) findViewById(R.id.et_qq_lc);
        etQqYdtz = (EditText) findViewById(R.id.et_qq_info);

        cbQqZbfwj = (CheckBox) findViewById(R.id.cb_zbfwj);
        cbQqCfwj = (CheckBox) findViewById(R.id.cb_cfwj);
        cbQqYxjl = (CheckBox) findViewById(R.id.cb_yxjl);
        cbQqLc = (CheckBox) findViewById(R.id.cb_lc);
        cbQqYdtz = (CheckBox) findViewById(R.id.cb_info);

        cbAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                int n = layQqList.getChildCount();
                for (int i = 0; i < n; i++) {
                    LinearLayout layRow = (LinearLayout) layQqList.getChildAt(i);
                    CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
                    cb.setChecked(arg1);
                }
                cbQqZbfwj.setChecked(arg1);
                cbQqCfwj.setChecked(arg1);
                cbQqYxjl.setChecked(arg1);
                cbQqLc.setChecked(arg1);
                cbQqYdtz.setChecked(arg1);
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
                    myView.Save();
                    boolean b = saveQt();
                    if (iStatus != 2) {
                        iStatus = 1;
                    } else if (!b) {
                        iStatus = 1;
                    }
                    String sql = "update dczt set yindiantu = '" + iStatus + "' where ydh = '" + ydh + "'";
                    YangdiMgr.ExecSQL(ydh, sql);
                    SlqcYindiantu.this.finish();
                }
            });
            builder.setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    SlqcYindiantu.this.finish();
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
                case 1001: {
                    String src = MyConfig.GetWorkdir() + "/temp/src.jpg";
                    String dst = MyConfig.GetWorkdir() + "/temp/dst.jpg";
                    Intent cj = new Intent("com.android.camera.action.CROP");
                    try {
                        cj.setData(Uri.parse(android.provider.MediaStore.Images.Media.insertImage(getContentResolver(), src, null, null)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    cj.putExtra("crop", "true");
                    cj.putExtra("outputX", 500);
                    cj.putExtra("outputY", 500);
                    cj.putExtra("return-data", false);
                    cj.putExtra("output", Uri.fromFile(new File(dst)));
                    cj.putExtra("outputFormat", "PNG");
                    this.startActivityForResult(cj, 1002);
                    break;
                }
                case 1002: {
                    String src = MyConfig.GetWorkdir() + "/temp/dst.jpg";
                    String dst = MyConfig.GetWorkdir() + "/temp/dst2.jpg";
                    Bitmap bmp = BitmapFactory.decodeFile(src);
                    int w = bmp.getWidth();
                    int h = bmp.getHeight();
                    float ws = myView.getWidth() * 1.0f / w;
                    float hs = myView.getHeight() * 1.0f / h;
                    float scale = ws < hs ? ws : hs;
                    float dx = (myView.getWidth() - (w * scale)) / 2;
                    float dy = (myView.getHeight() - (h * scale)) / 2;
                    Matrix matrix = new Matrix();
                    matrix.reset();
                    matrix.setScale(scale, scale);
                    matrix.postTranslate(dx, dy);
                    Bitmap bmpSave = Bitmap.createBitmap(myView.getWidth(), myView.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas cvSave = new Canvas(bmpSave);
                    cvSave.drawBitmap(bmp, matrix, new Paint());

                    try {
                        FileOutputStream outStream = new FileOutputStream(dst);
                        bmpSave.compress(CompressFormat.PNG, 100, outStream);
                        outStream.close();
                        bmpSave.recycle();
                        bmp.recycle();
                        YangdiMgr.SetYindiantu(ydh, dst);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

				/*
				int[] intArray = new int[bmpSave.getWidth()*bmpSave.getHeight()];  
				bmpSave.getPixels(intArray, 0, bmpSave.getWidth(), 0, 0, bmpSave.getWidth(), bmpSave.getHeight());
				bmpSave.recycle();
				bmp.recycle();
				YangdiMgr.SetYindiantu(ydh, intArray);
				*/
                    myView.ResetImage();
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
                        Yddww item = lstQq.get(i);
                        boolean b = YangdiMgr.IsHaveYindiandww(ydh, item);
                        if (b) continue;
                        YangdiMgr.AddYindianDww(ydh, item);
                        cb.setChecked(false);
                    }
                }
                initData();
                if (cbQqZbfwj.isChecked()) {
                    if (r == 0) {
                        etZbfwj.setText(etQqZbfwj.getText());
                    } else if (r == 1 && etZbfwj.getText().toString().equals("")) {
                        etZbfwj.setText(etQqZbfwj.getText());
                    }
                }
                if (cbQqCfwj.isChecked()) {
                    if (r == 0) {
                        etCfwj.setText(etQqCfwj.getText());
                    } else if (r == 1 && etCfwj.getText().toString().equals("")) {
                        etCfwj.setText(etQqCfwj.getText());
                    }
                }
                if (cbQqYxjl.isChecked()) {
                    if (r == 0) {
                        etYxjl.setText(etQqYxjl.getText());
                    } else if (r == 1 && etYxjl.getText().toString().equals("")) {
                        etYxjl.setText(etQqYxjl.getText());
                    }
                }
                if (cbQqLc.isChecked()) {
                    if (r == 0) {
                        etLc.setText(etQqLc.getText());
                    } else if (r == 1 && etLc.getText().toString().equals("")) {
                        etLc.setText(etQqLc.getText());
                    }
                }
                if (cbQqYdtz.isChecked()) {
                    if (r == 0) {
                        etYdtz.setText(etQqYdtz.getText());
                    } else if (r == 1 && etYdtz.getText().toString().equals("")) {
                        etYdtz.setText(etQqYdtz.getText());
                    }
                }
                cbAll.setChecked(false);
                cbQqZbfwj.setChecked(false);
                cbQqCfwj.setChecked(false);
                cbQqYxjl.setChecked(false);
                cbQqLc.setChecked(false);
                cbQqYdtz.setChecked(false);

                saveQt();
                break;
            }
            case R.id.btn_save: {
                myView.Save();
                boolean b = saveQt();
                if (iStatus != 2) {
                    iStatus = 1;
                } else if (!b) {
                    iStatus = 1;
                }
                String sql = "update dczt set yindiantu = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                break;
            }
            case R.id.btn_finish: {
                myView.Save();
                boolean b = saveQt();
                if (b) {
                    iStatus = 2;
                } else {
                    iStatus = 1;
                }
                String sql = "update dczt set yindiantu = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);

                if (b) finish();
                break;
            }
            case R.id.btn_take: {
                String pic = MyConfig.GetWorkdir() + "/temp/src.jpg";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(pic)));
                startActivityForResult(intent, 1001);
                break;
            }
            case R.id.btn_delpic: {
                YangdiMgr.DelYindiantu(ydh);
                myView.ResetImage();
                break;
            }
            case R.id.btn_color: {
                int c = new MyColorDialog(this, 0xff000000).showDialog();
                setColor(c);
                break;
            }
            case R.id.btn_painter: {
                boolean b = tbPaint.isChecked();
                myView.SetDraw(b);
                if (b) tbClean.setChecked(false);
                break;
            }
            case R.id.btn_eraser: {
                boolean b = tbClean.isChecked();
                myView.SetClean(b);
                if (b) tbPaint.setChecked(false);
                break;
            }
            case R.id.btn_pre: {
                myView.Revoke();
                break;
            }
            case R.id.btn_next: {
                myView.Restore();
                break;
            }
            case R.id.btn_add: {
                YddwwDialog dlg = new YddwwDialog(this, null);
                Yddww item = dlg.showDialog();
                if (item != null) {
                    YangdiMgr.AddYindianDww(ydh, item);
                    initData();
                }
                break;
            }
            case R.id.btn_del: {
                MyMakeSureDialog dlg = new MyMakeSureDialog(this, "删除", "删除后将无法恢复，是否继续删除？", "删除", "取消");
                if (dlg.showDialog()) {
                    delRow();
                }
                break;
            }
            case R.id.btn_ydzb_input: {
                YdzbDialog dlg = new YdzbDialog(this, ydh);
                dlg.showDialog();
                double[] dd = YangdiMgr.GetYdloc22(ydh);
                if (dd != null) {
                    String slon = MyFuns.NumberToDegree(dd[0]);
                    String slat = MyFuns.NumberToDegree(dd[1]);
                    int hzb = (int) dd[2];
                    int zzb = (int) dd[3];
                    tvYdzbgps.setText("引点GPS坐标：" + slon + ",  " + slat);
                    tvYdzbhzb.setText("引点横纵坐标：" + hzb + ",  " + zzb);
                    MyCoord c = YangdiMgr.GetYdloc(ydh);
                    if (c != null) {
                        YdzbDialog.GpsToBj54(c);
                    } else {
                        c = Qianqimgr.GetYdloc(ydh);
                    }
                    if (c != null) {
                        float fwj = -1;
                        double x = c.x - hzb;
                        double y = c.y - zzb;
                        if (x > 0 && y > 0) {
                            fwj = (float) (Math.atan((x) / (y)) * 180 / Math.PI);
                        }
                        if (x > 0 && y < 0) {
                            fwj = 180 + (float) (Math.atan((x) / (y)) * 180 / Math.PI);
                        }
                        if (x < 0 && y < 0) {
                            fwj = 180 + (float) (Math.atan((x) / (y)) * 180 / Math.PI);
                        }
                        if (x < 0 && y > 0) {
                            fwj = 360 + (float) (Math.atan((x) / (y)) * 180 / Math.PI);
                        }
                        if (fwj >= 0) {
                            etZbfwj.setText(String.valueOf(fwj));
                            float cpj = YangdiMgr.GetYdcpj(ydh);
                            if (cpj >= 0) {
                                etCfwj.setText(String.valueOf(fwj + cpj));
                            }
                        }
                        double dis = Math.sqrt((c.x - hzb) * (c.x - hzb) + (c.y - zzb) * (c.y - zzb));
                        dis = MyFuns.MyDecimal(dis, 1);
                        etYxjl.setText(String.valueOf(dis));
                    }
                }
                break;
            }
            default: {
                LinearLayout layRow = (LinearLayout) v;
                int xh = (Integer) layRow.getTag();
                Yddww item = YangdiMgr.GetYindianDww(ydh, xh);
                YddwwDialog dlg = new YddwwDialog(this, item);
                Yddww dww = dlg.showDialog();
                if (dww != null) {
                    dww.xh = item.xh;
                    YangdiMgr.UpdateYindianDww(ydh, dww);
                    TextView tvMc = (TextView) layRow.findViewById(R.id.tv_mc);
                    TextView tvBh = (TextView) layRow.findViewById(R.id.tv_bh);
                    TextView tvFwj = (TextView) layRow.findViewById(R.id.tv_fwj);
                    TextView tvSpj = (TextView) layRow.findViewById(R.id.tv_spj);
                    tvMc.setText(dww.mc);
                    tvBh.setText(String.valueOf(dww.bh));
                    tvFwj.setText(String.valueOf(dww.fwj));
                    tvSpj.setText(String.valueOf(dww.spj));
                }
            }
        }
    }

    private void initData() {
        layList.removeAllViews();
        List<Yddww> lst = YangdiMgr.GetYindianDww(ydh);
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                String sql = "update yindiandww set bh = '" + (i + 1) + "' where xh = '" + lst.get(i).xh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                lst.get(i).bh = i + 1;
                addRow(lst.get(i));
            }
        }
    }

    private float getYxjl() {
        float dis = -1;
        List<Cljl> lst = YangdiMgr.GetYxclList(ydh);
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                dis += lst.get(i).spj;
            }
        }
        return dis;
    }

    private void addRow(Yddww item) {
        if (item == null) return;
        LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.slqc_dww_item, null);
        layRow.setTag(item.xh);
        layRow.setOnClickListener(this);
        TextView tvMc = (TextView) layRow.findViewById(R.id.tv_mc);
        TextView tvBh = (TextView) layRow.findViewById(R.id.tv_bh);
        TextView tvFwj = (TextView) layRow.findViewById(R.id.tv_fwj);
        TextView tvSpj = (TextView) layRow.findViewById(R.id.tv_spj);
        tvMc.setText(item.mc);
        tvBh.setText(String.valueOf(item.bh));
        tvFwj.setText(String.valueOf(item.fwj));
        tvSpj.setText(String.valueOf(item.spj));
        layList.addView(layRow);
    }

    private void delRow() {
        int n = layList.getChildCount();
        for (int i = n - 1; i >= 0; i--) {
            LinearLayout layRow = (LinearLayout) layList.getChildAt(i);
            CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
            if (cb.isChecked()) {
                int xh = (Integer) layRow.getTag();
                YangdiMgr.DelYindianDww(ydh, xh);
            }
        }
        initData();
    }

    private void initQqData() {
        layQqList.removeAllViews();
        lstQq = Qianqimgr.GetQqYindiandwwList(ydh);
        if (lstQq != null && lstQq.size() > 0) {
            for (int i = 0; i < lstQq.size(); i++) {
                lstQq.get(i).bh = i + 1;
                addQqRow(lstQq.get(i));
            }
        }
        String[][] sss = Qianqimgr.GetQqQtinfo(ydh);
        if (sss != null) {
            etQqZbfwj.setText(sss[0][1]);
            etQqCfwj.setText(sss[0][2]);
            etQqYxjl.setText(sss[0][3]);
            etQqLc.setText(sss[0][4]);
            etQqYdtz.setText(sss[0][5]);
        }
        if (lstQq.size() == 0) {
            if (sss == null) {
                layQqWu.setVisibility(1);
            } else if (sss[0][1].equals("") && sss[0][2].equals("") && sss[0][3].equals("") && sss[0][4].equals("") && sss[0][5].equals("")) {
                layQqWu.setVisibility(1);
            }
        }
    }

    private void addQqRow(Yddww item) {
        if (item == null) return;
        LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.slqc_dww_item, null);
        TextView tvMc = (TextView) layRow.findViewById(R.id.tv_mc);
        TextView tvBh = (TextView) layRow.findViewById(R.id.tv_bh);
        TextView tvFwj = (TextView) layRow.findViewById(R.id.tv_fwj);
        TextView tvSpj = (TextView) layRow.findViewById(R.id.tv_spj);
        tvMc.setText(item.mc);
        tvBh.setText(String.valueOf(item.bh));
        tvFwj.setText(String.valueOf(item.fwj));
        tvSpj.setText(String.valueOf(item.spj));
        layQqList.addView(layRow);
    }

    private void setColor(int color) {
        myView.SetColor(color);
        Canvas canvas = new Canvas(bmpColor);
        canvas.drawColor(color);
        btnColor.setImageBitmap(bmpColor);
    }

    private boolean saveQt() {
        String zbfwj = etZbfwj.getText().toString();
        String cfwj = etCfwj.getText().toString();
        String yxjl = etYxjl.getText().toString();
        String lc = etLc.getText().toString();
        String ydtz = etYdtz.getText().toString();

        float f_zbfwj = -1;
        try {
            f_zbfwj = Float.parseFloat(zbfwj);
            f_zbfwj = MyFuns.MyDecimal(f_zbfwj, 1);
        } catch (NumberFormatException nfe) {
        }
        float f_cfwj = -1;
        try {
            f_cfwj = Float.parseFloat(cfwj);
            f_cfwj = MyFuns.MyDecimal(f_cfwj, 1);
        } catch (NumberFormatException nfe) {
        }
        float f_yxjl = -1;
        try {
            f_yxjl = Float.parseFloat(yxjl);
            f_yxjl = MyFuns.MyDecimal(f_yxjl, 2);
        } catch (NumberFormatException nfe) {
        }
        float f_lc = 0;
        try {
            f_lc = Float.parseFloat(lc);
            f_lc = MyFuns.MyDecimal(f_lc, 1);
        } catch (NumberFormatException nfe) {
        }

        if (f_zbfwj > YangdiMgr.MAX_FWJ) {
            Toast.makeText(this, "坐标方位角超限！", 1).show();
            return false;
        }
        if (f_cfwj > YangdiMgr.MAX_FWJ) {
            Toast.makeText(this, "磁方位角超限！", 1).show();
            return false;
        }
        if (f_lc < YangdiMgr.MIN_LC || f_lc > YangdiMgr.MAX_LC) {
            Toast.makeText(this, "罗差超限！", 1).show();
            return false;
        }

        String s_zbfwj = f_zbfwj < 0 ? "" : String.valueOf(f_zbfwj);
        String s_cfwj = f_cfwj < 0 ? "" : String.valueOf(f_cfwj);
        String s_yxjl = f_yxjl < 0 ? "" : String.valueOf(f_yxjl);
        String s_lc = String.valueOf(f_lc);

        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (YangdiMgr.QueryExists(ydh, sql)) {
            sql = "update qt set zbfwj = '" + s_zbfwj + "', cfwj = '" + s_cfwj + "', yxjl = '" + yxjl + "', lc = '" + s_lc + "', yindian = '" + ydtz + "' where ydh = '" + ydh + "'";
        } else {
            sql = "insert into qt(ydh, zbfwj, cfwj, yxjl, lc, yindian) values('" + ydh + "', '" + s_zbfwj + "', '" + s_cfwj + "', '" + s_yxjl + "', '" + s_lc + "', '" + ydtz + "')";
        }
        YangdiMgr.ExecSQL(ydh, sql);
        return true;
    }


    class MyCommand {
        public int color;
        public int width;
        public List<MyPoint> lstPts;
    }

    class MyView extends View implements View.OnTouchListener {
        int vw = 0;
        int vh = 0;
        private boolean isFirst = true;

        private Paint paint = new Paint();
        private Canvas cvSave = null;
        private Bitmap bmpSave = null;

        private boolean isDraw = false;
        private boolean isClean = false;

        private int width = 3;
        private int color = 0xff000000;
        private int earserWidth = width + 8;

        private List<MyCommand> lstCmds = new ArrayList<MyCommand>();
        private List<MyCommand> lstDels = new ArrayList<MyCommand>();
        private List<MyPoint> tmpLine = new ArrayList<MyPoint>();
        private List<MyPoint> cmdLine = null;

        public MyView(Context context) {
            super(context);
            this.setOnTouchListener(this);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (isFirst) {
                vw = this.getWidth();
                vh = this.getHeight();
                YangdiMgr.SetYindiantuWidth(vw, ydh);
                YangdiMgr.SetYindiantuHeight(vh, ydh);

                if (bmpSave != null) bmpSave.recycle();
                bmpSave = Bitmap.createBitmap(vw, vh, Bitmap.Config.ARGB_4444);
                cvSave = new Canvas(bmpSave);
                cvSave.drawColor(0xffffffff);
                Bitmap bmp = YangdiMgr.GetYindiantu(ydh);
                if (bmp != null) {
                    int w = bmp.getWidth();
                    int h = bmp.getHeight();
                    if (w > vw + 10 || h > vh + 10) {
                        float ws = vw * 1.0f / w;
                        float hs = vh * 1.0f / h;
                        float scale = ws < hs ? ws : hs;

                        float dx = (vw - (w * scale)) / 2;
                        float dy = (vh - (h * scale)) / 2;

                        Matrix matrix = new Matrix();
                        matrix.reset();
                        matrix.setScale(scale, scale);
                        matrix.postTranslate(dx, dy);
                        cvSave.drawBitmap(bmp, matrix, paint);
                        bmp.recycle();
                        bmp = null;
                    } else {
                        cvSave.drawBitmap(bmp, 0, 0, paint);
                        bmp.recycle();
                        bmp = null;
                    }
                }

                isFirst = false;
            }

            canvas.drawBitmap(bmpSave, 0, 0, paint);

            for (int i = 0; i < lstCmds.size(); i++) {
                List<MyPoint> pts = lstCmds.get(i).lstPts;
                if (pts.size() == 1) {
                    paint.setStyle(Style.FILL);
                    paint.setStrokeWidth(1);
                    paint.setColor(lstCmds.get(i).color);
                    canvas.drawCircle(pts.get(0).x, pts.get(0).y, lstCmds.get(i).width, paint);
                } else if (pts.size() > 1) {
                    paint.setStyle(Style.STROKE);
                    paint.setStrokeWidth(lstCmds.get(i).width);
                    paint.setColor(lstCmds.get(i).color);
                    Path path = new Path();
                    path.moveTo(pts.get(0).x, pts.get(0).y);
                    for (int j = 1; j < pts.size(); j++) {
                        path.lineTo(pts.get(j).x, pts.get(j).y);
                    }
                    canvas.drawPath(path, paint);
                }
            }

            paint.setStyle(Style.STROKE);
            if (isDraw) {
                paint.setStrokeWidth(width);
                paint.setColor(color);
            }
            if (isClean) {
                paint.setStrokeWidth(earserWidth);
                paint.setColor(0xffffffff);
            }
            if (tmpLine.size() > 0) {
                Path path = new Path();
                path.moveTo(tmpLine.get(0).x, tmpLine.get(0).y);
                for (int j = 1; j < tmpLine.size(); j++) {
                    path.lineTo(tmpLine.get(j).x, tmpLine.get(j).y);
                }
                canvas.drawPath(path, paint);
            }

            //paint.setStyle(Style.FILL);
            //paint.setColor(Color.BLACK);
            //paint.setStrokeWidth(1);
            //canvas.drawCircle(vw/2, vh/2, 2*dpiScale, paint);
            //paint.setStyle(Style.STROKE);
            //canvas.drawCircle(vw/2, vh/2, 5*dpiScale, paint);

            Paint p = new Paint();
            p.setColor(0xffc0c0ff);
            p.setStrokeWidth(5);
            p.setStyle(Style.STROKE);
            canvas.drawRect(0, 0, vw, vh, p);
        }

        //触摸事件处理
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int n = event.getPointerCount();
            if (n == 1) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        if (isDraw || isClean) {
                            cmdLine = new ArrayList<MyPoint>();
                        }
                    }
                    break;
                    case MotionEvent.ACTION_UP: {
                        if (isDraw || isClean) {
                            float x = event.getX();
                            float y = event.getY();
                            MyPoint pt = new MyPoint(x, y);
                            cmdLine.add(pt);
                            MyCommand myCommand = new MyCommand();
                            if (isDraw) {
                                myCommand.color = color;
                                myCommand.width = width;
                            }
                            if (isClean) {
                                myCommand.color = 0xffffffff;
                                myCommand.width = earserWidth;
                            }
                            myCommand.lstPts = cmdLine;
                            lstCmds.add(myCommand);
                            tmpLine.clear();
                            postInvalidate();
                        }
                    }
                    break;
                    case MotionEvent.ACTION_MOVE: {
                        if (isDraw || isClean) {
                            float x = event.getX();
                            float y = event.getY();
                            MyPoint pt = new MyPoint(x, y);
                            cmdLine.add(pt);
                            tmpLine.add(pt);
                            postInvalidate();
                        }
                    }
                    break;
                }
            }
            return true;
        }

        public void SetColor(int c) {
            color = c;
        }

        public void SetPaintWidth(int w) {
            width = w;
            earserWidth = width + 8;
        }

        public void SetDraw(boolean b) {
            isDraw = b;
            if (b) {
                isClean = false;
            }
        }

        public void SetClean(boolean b) {
            isClean = b;
            if (b) {
                isDraw = false;
            }
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

        public void ResetImage() {
            isFirst = true;
            lstCmds.clear();
            lstDels.clear();
            postInvalidate();
        }

        public void Save() {
            if (lstCmds.size() <= 0) {
                return;
            }

            for (int i = 0; i < lstCmds.size(); i++) {
                List<MyPoint> pts = lstCmds.get(i).lstPts;
                if (pts.size() == 1) {
                    paint.setStyle(Style.FILL);
                    paint.setStrokeWidth(1);
                    paint.setColor(lstCmds.get(i).color);
                    cvSave.drawCircle(pts.get(0).x, pts.get(0).y, lstCmds.get(i).width, paint);
                } else if (pts.size() > 1) {
                    paint.setStyle(Style.STROKE);
                    paint.setStrokeWidth(lstCmds.get(i).width);
                    paint.setColor(lstCmds.get(i).color);
                    Path path = new Path();
                    path.moveTo(pts.get(0).x, pts.get(0).y);
                    for (int j = 1; j < pts.size(); j++) {
                        path.lineTo(pts.get(j).x, pts.get(j).y);
                    }
                    cvSave.drawPath(path, paint);
                }
            }
			/*
			int[] intArray = new int[bmpSave.getWidth()*bmpSave.getHeight()];  
			bmpSave.getPixels(intArray, 0, bmpSave.getWidth(), 0, 0, bmpSave.getWidth(), bmpSave.getHeight());
			YangdiMgr.SetYindiantu(ydh, intArray);
			isFirst = true;
			lstCmds.clear();
			lstDels.clear();
			postInvalidate();
			*/

            String pic = MyConfig.GetWorkdir() + "/temp/pic.jpg";
            try {
                FileOutputStream outStream = new FileOutputStream(pic);
                bmpSave.compress(CompressFormat.PNG, 100, outStream);
                outStream.close();
                YangdiMgr.SetYindiantu(ydh, pic);
                isFirst = true;
                lstCmds.clear();
                lstDels.clear();
                postInvalidate();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
