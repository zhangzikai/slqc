package com.tdgeos.dlg.yddc;

import java.util.List;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Sgcl;
import com.tdgeos.yangdi.YangdiMgr;
import com.tdgeos.yangdi.Yangmu;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class KjlSgclDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private Handler mHandler = null;
    private boolean result = false;
    private int ydh = 0;
    private int xh = 0;

    private EditText etYmh1 = null;
    private EditText etSg1 = null;
    private TextView tvSz1 = null;
    private TextView tvXj1 = null;

    private EditText etYmh2 = null;
    private EditText etSg2 = null;
    private TextView tvSz2 = null;
    private TextView tvXj2 = null;

    private EditText etYmh3 = null;
    private EditText etSg3 = null;
    private TextView tvSz3 = null;
    private TextView tvXj3 = null;

    private Button btnList = null;
    private Button btnOk = null;
    private Button btnCancel = null;

    public KjlSgclDialog(Context context, int ydh, int xh) {
        super(context);
        this.context = context;
        this.ydh = ydh;
        this.xh = xh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_kjlsgcl);
        setTitle("跨角林树高测量");
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        //this.getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        etYmh1 = (EditText) findViewById(R.id.et_ym1);
        etSg1 = (EditText) findViewById(R.id.et_sg1);
        tvSz1 = (TextView) findViewById(R.id.tv_sz1);
        tvXj1 = (TextView) findViewById(R.id.tv_xj1);

        etYmh2 = (EditText) findViewById(R.id.et_ym2);
        etSg2 = (EditText) findViewById(R.id.et_sg2);
        tvSz2 = (TextView) findViewById(R.id.tv_sz2);
        tvXj2 = (TextView) findViewById(R.id.tv_xj2);

        etYmh3 = (EditText) findViewById(R.id.et_ym3);
        etSg3 = (EditText) findViewById(R.id.et_sg3);
        tvSz3 = (TextView) findViewById(R.id.tv_sz3);
        tvXj3 = (TextView) findViewById(R.id.tv_xj3);

        etYmh1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                int i = -1;
                try {
                    i = Integer.parseInt(str);
                } catch (Exception e) {
                }
                Yangmu ym = YangdiMgr.GetYangmu(ydh, i);
                if (ym != null && ym.kjdlxh == xh) {
                    etYmh1.setTextColor(Color.BLACK);
                    tvSz1.setText(ym.szmc);
                    tvXj1.setText(String.valueOf(ym.bqxj));
                } else {
                    etYmh1.setTextColor(Color.RED);
                    tvSz1.setText("");
                    tvXj1.setText("");
                }
            }
        });

        etYmh2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                int i = -1;
                try {
                    i = Integer.parseInt(str);
                } catch (Exception e) {
                }
                Yangmu ym = YangdiMgr.GetYangmu(ydh, i);
                if (ym != null && ym.kjdlxh == xh) {
                    etYmh2.setTextColor(Color.BLACK);
                    tvSz2.setText(ym.szmc);
                    tvXj2.setText(String.valueOf(ym.bqxj));
                } else {
                    etYmh2.setTextColor(Color.RED);
                    tvSz2.setText("");
                    tvXj2.setText("");
                }
            }
        });

        etYmh3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                int i = -1;
                try {
                    i = Integer.parseInt(str);
                } catch (Exception e) {
                }
                Yangmu ym = YangdiMgr.GetYangmu(ydh, i);
                if (ym != null && ym.kjdlxh == xh) {
                    etYmh3.setTextColor(Color.BLACK);
                    tvSz3.setText(ym.szmc);
                    tvXj3.setText(String.valueOf(ym.bqxj));
                } else {
                    etYmh3.setTextColor(Color.RED);
                    tvSz3.setText("");
                    tvXj3.setText("");
                }
            }
        });

        etSg1.addTextChangedListener(new TextWatcher() {
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
                    etSg1.setTextColor(Color.BLACK);
                } else {
                    etSg1.setTextColor(Color.RED);
                }
            }
        });

        etSg2.addTextChangedListener(new TextWatcher() {
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
                    etSg2.setTextColor(Color.BLACK);
                } else {
                    etSg2.setTextColor(Color.RED);
                }
            }
        });

        etSg3.addTextChangedListener(new TextWatcher() {
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
                    etSg3.setTextColor(Color.BLACK);
                } else {
                    etSg3.setTextColor(Color.RED);
                }
            }
        });

        btnList = (Button) findViewById(R.id.btn_list);
        btnList.setOnClickListener(this);
        btnList.setVisibility(8);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        List<Sgcl> lst = YangdiMgr.GetKjlSgclList(ydh, xh);
        if (lst.size() >= 1) {
            etYmh1.setText(String.valueOf(lst.get(0).ymh));
            etSg1.setText(String.valueOf(lst.get(0).sg));
        }
        if (lst.size() >= 2) {
            etYmh2.setText(String.valueOf(lst.get(1).ymh));
            etSg2.setText(String.valueOf(lst.get(1).sg));
        }
        if (lst.size() >= 3) {
            etYmh3.setText(String.valueOf(lst.get(2).ymh));
            etSg3.setText(String.valueOf(lst.get(2).sg));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_list: {

                break;
            }
            case R.id.btn_ok: {
                String s_ymh1 = etYmh1.getText().toString();
                String s_sg1 = etSg1.getText().toString();
                String s_ymh2 = etYmh2.getText().toString();
                String s_sg2 = etSg2.getText().toString();
                String s_ymh3 = etYmh3.getText().toString();
                String s_sg3 = etSg3.getText().toString();
                int i_ymh1 = 0;
                float f_sg1 = 0;
                int i_ymh2 = 0;
                float f_sg2 = 0;
                int i_ymh3 = 0;
                float f_sg3 = 0;
                try {
                    i_ymh1 = Integer.parseInt(s_ymh1);
                } catch (Exception e) {
                }
                try {
                    f_sg1 = Float.parseFloat(s_sg1);
                } catch (Exception e) {
                }
                try {
                    i_ymh2 = Integer.parseInt(s_ymh2);
                } catch (Exception e) {
                }
                try {
                    f_sg2 = Float.parseFloat(s_sg2);
                } catch (Exception e) {
                }
                try {
                    i_ymh3 = Integer.parseInt(s_ymh3);
                } catch (Exception e) {
                }
                try {
                    f_sg3 = Float.parseFloat(s_sg3);
                } catch (Exception e) {
                }

                Sgcl sgcl1 = null;
                Sgcl sgcl2 = null;
                Sgcl sgcl3 = null;

                if (s_ymh1.equals("")) {
                    if (!s_sg1.equals("")) {
                        Toast.makeText(context, "第一株数平均木有树高但无样木号！", 1).show();
                        break;
                    }
                } else {
                    Yangmu ym1 = YangdiMgr.GetYangmu(ydh, i_ymh1);
                    if (ym1 == null) {
                        Toast.makeText(context, "第一株数平均木样木号无效！", 1).show();
                        break;
                    }
                    if (ym1.kjdlxh != xh) {
                        Toast.makeText(context, "第一株数平均木的跨角地类序号与当前跨角序号不一致！", 1).show();
                        break;
                    }
                    if (!YangdiMgr.IsKjlYssz(ydh, xh, i_ymh1)) {
                        Toast.makeText(context, "第一株数平均木不是优势树种！", 1).show();
                        break;
                    }
                    if (f_sg1 < YangdiMgr.MIN_SG || f_sg1 > YangdiMgr.MAX_SG) {
                        Toast.makeText(context, "第一株数平均木的树高超限！", 1).show();
                        break;
                    }
                    sgcl1 = new Sgcl();
                    sgcl1.ymh = i_ymh1;
                    sgcl1.sg = f_sg1;
                    sgcl1.sz = ym1.szdm;
                    sgcl1.xj = ym1.bqxj;
                }

                if (s_ymh2.equals("")) {
                    if (!s_sg2.equals("")) {
                        Toast.makeText(context, "第二株数平均木有树高但无样木号！", 1).show();
                        break;
                    }
                } else {
                    Yangmu ym2 = YangdiMgr.GetYangmu(ydh, i_ymh2);
                    if (ym2 == null) {
                        Toast.makeText(context, "第二株数平均木样木号无效！", 1).show();
                        break;
                    }
                    if (ym2.kjdlxh != xh) {
                        Toast.makeText(context, "第二株数平均木的跨角地类序号与当前跨角序号不一致！", 1).show();
                        break;
                    }
                    if (!YangdiMgr.IsKjlYssz(ydh, xh, i_ymh2)) {
                        Toast.makeText(context, "第二株数平均木不是优势树种！", 1).show();
                        break;
                    }
                    if (f_sg2 < YangdiMgr.MIN_SG || f_sg2 > YangdiMgr.MAX_SG) {
                        Toast.makeText(context, "第二株数平均木的树高超限！", 1).show();
                        break;
                    }
                    sgcl2 = new Sgcl();
                    sgcl2.ymh = i_ymh2;
                    sgcl2.sg = f_sg2;
                    sgcl2.sz = ym2.szdm;
                    sgcl2.xj = ym2.bqxj;
                }

                if (s_ymh3.equals("")) {
                    if (!s_sg3.equals("")) {
                        Toast.makeText(context, "第三株数平均木有树高但无样木号！", 1).show();
                        break;
                    }
                } else {
                    Yangmu ym3 = YangdiMgr.GetYangmu(ydh, i_ymh3);
                    if (ym3 == null) {
                        Toast.makeText(context, "第三株数平均木样木号无效！", 1).show();
                        break;
                    }
                    if (ym3.kjdlxh != xh) {
                        Toast.makeText(context, "第三株数平均木的跨角地类序号与当前跨角序号不一致！", 1).show();
                        break;
                    }
                    if (!YangdiMgr.IsKjlYssz(ydh, xh, i_ymh3)) {
                        Toast.makeText(context, "第三株数平均木不是优势树种！", 1).show();
                        break;
                    }
                    if (f_sg3 < YangdiMgr.MIN_SG || f_sg3 > YangdiMgr.MAX_SG) {
                        Toast.makeText(context, "第三株数平均木的树高超限！", 1).show();
                        break;
                    }
                    sgcl3 = new Sgcl();
                    sgcl3.ymh = i_ymh3;
                    sgcl3.sg = f_sg3;
                    sgcl3.sz = ym3.szdm;
                    sgcl3.xj = ym3.bqxj;
                }

                if (sgcl1 != null && sgcl2 != null) {
                    if (sgcl1.ymh == sgcl2.ymh) {
                        Toast.makeText(context, "第二株数平均木与第一株重复！", 1).show();
                        break;
                    }
                }
                if (sgcl1 != null && sgcl3 != null) {
                    if (sgcl1.ymh == sgcl3.ymh) {
                        Toast.makeText(context, "第三株数平均木与第一株重复！", 1).show();
                        break;
                    }
                }
                if (sgcl3 != null && sgcl2 != null) {
                    if (sgcl3.ymh == sgcl2.ymh) {
                        Toast.makeText(context, "第三株数平均木与第二株重复！", 1).show();
                        break;
                    }
                }

                YangdiMgr.ClearKjlSgcl(ydh, xh);
                if (sgcl1 != null) {
                    YangdiMgr.AddKjlSgcl(ydh, xh, sgcl1);
                }
                if (sgcl2 != null) {
                    YangdiMgr.AddKjlSgcl(ydh, xh, sgcl2);
                }
                if (sgcl3 != null) {
                    YangdiMgr.AddKjlSgcl(ydh, xh, sgcl3);
                }
                result = true;
                this.cancel();
                break;
            }
            case R.id.btn_cancel: {
                this.cancel();
                break;
            }
        }
    }

    public boolean showDialog() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };
        super.show();
        try {
            Looper.getMainLooper();
            Looper.loop();
        } catch (RuntimeException e2) {
        }
        return result;
    }
}