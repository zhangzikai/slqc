package com.tdgeos.dlg.yddc;

import java.util.List;

import com.tdgeos.lib.MyFuns;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Sgcl;
import com.tdgeos.yangdi.YangdiMgr;
import com.tdgeos.yangdi.Yangmu;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SgclDialog extends Dialog implements View.OnClickListener {
    private Handler mHandler = null;
    private Context context;

    private EditText etYmh = null;
    private EditText etSz = null;
    private EditText etXj = null;
    private EditText etSg = null;
    private EditText etZxg = null;

    private Button btnOk = null;
    private Button btnCancel = null;

    private int ydh = 0;
    private Sgcl sgcl = null;
    private Sgcl result = null;

    public SgclDialog(Context context, Sgcl sgcl, int ydh) {
        super(context);
        this.context = context;
        this.sgcl = sgcl;
        this.ydh = ydh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_sgcl);
        this.setTitle("树(毛竹)高测量记录");
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        this.setCanceledOnTouchOutside(false);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        etYmh = (EditText) findViewById(R.id.et_ymh);
        etSz = (EditText) findViewById(R.id.et_sz);
        etXj = (EditText) findViewById(R.id.et_xj);
        etSg = (EditText) findViewById(R.id.et_sg);
        etZxg = (EditText) findViewById(R.id.et_zxg);

        etSz.setEnabled(false);
        etXj.setEnabled(false);

        etYmh.setInputType(InputType.TYPE_CLASS_NUMBER);
        etSg.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etZxg.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        etYmh.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s_ymh = etYmh.getText().toString();
                if (!s_ymh.equals("")) {
                    int ymh = -1;
                    try {
                        ymh = Integer.parseInt(s_ymh);
                    } catch (Exception e) {
                    }
                    if (ymh != -1) {
                        if (sgcl == null) {
                            List<Sgcl> lst = YangdiMgr.GetSgclList(ydh);
                            boolean b = false;
                            for (int i = 0; i < lst.size(); i++) {
                                if (ymh == lst.get(i).ymh) {
                                    b = true;
                                    break;
                                }
                            }
                            if (b) {
                                etYmh.setTextColor(Color.RED);//重复
                            }
                        }

                        Yangmu ym = YangdiMgr.GetYangmu(ydh, ymh);
                        if (ym == null) {
                            etYmh.setTextColor(Color.RED);//无此样木
                        } else {
                            if (ym.jclx == 13 || ym.jclx == 14 || ym.jclx == 15) {
                                etYmh.setTextColor(Color.RED);//枯倒采
                            } else {
                                if (YangdiMgr.IsYssz(ydh, ym.ymh)) {
                                    etYmh.setTextColor(Color.BLACK);
                                    etXj.setText(String.valueOf(ym.bqxj));
                                    etSz.setText(ym.szmc);
                                } else {
                                    etYmh.setTextColor(Color.RED);//不是优势树种
                                }
                            }
                        }
                    } else {
                        etYmh.setTextColor(Color.RED);//无效样木号
                    }
                } else {
                    etYmh.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etYmh.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String s_ymh = etYmh.getText().toString();
                    if (!s_ymh.equals("")) {
                        int ymh = -1;
                        try {
                            ymh = Integer.parseInt(s_ymh);
                        } catch (Exception e) {
                        }
                        if (ymh != -1) {
                            if (sgcl == null) {
                                List<Sgcl> lst = YangdiMgr.GetSgclList(ydh);
                                boolean b = false;
                                for (int i = 0; i < lst.size(); i++) {
                                    if (ymh == lst.get(i).ymh) {
                                        b = true;
                                        break;
                                    }
                                }
                                if (b) {
                                    Toast.makeText(context, "该样木已经录入，请选择其他样木。", 1).show();
                                    return;
                                }
                            }

                            Yangmu ym = YangdiMgr.GetYangmu(ydh, ymh);
                            if (ym == null) {
                                Toast.makeText(context, "无效的样木号！请重新填写。", 1).show();
                                return;
                            } else {
                                if (ym.jclx == 13 || ym.jclx == 14 || ym.jclx == 15) {
                                    Toast.makeText(context, "\"枯倒采\"样木不能参与计算！请重新填写。", 1).show();
                                    return;
                                } else {
                                    //if(!YangdiMgr.IsYssz(ydh, ym.ymh))
                                    //{
                                    //	Toast.makeText(context, "该样木不属于优势树种！请重新填写。", 1).show();
                                    //	return;
                                    //}
                                }
                            }
                        } else {
                            Toast.makeText(context, "无效的样木号！请重新填写。", 1).show();
                            return;
                        }
                    }
                }
            }
        });

        etSg.addTextChangedListener(new TextWatcher() {
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
                    etSg.setTextColor(Color.BLACK);
                } else {
                    etSg.setTextColor(Color.RED);
                }
            }
        });

        etZxg.addTextChangedListener(new TextWatcher() {
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
                    etZxg.setTextColor(Color.BLACK);
                } else {
                    etZxg.setTextColor(Color.RED);
                }
            }
        });

        if (sgcl != null) {
            etYmh.setText(String.valueOf(sgcl.ymh));
            if (sgcl.sz > 0) etSz.setText(Resmgr.GetSzName(sgcl.sz));
            if (sgcl.xj > 0) etXj.setText(String.valueOf(sgcl.xj));
            if (sgcl.sg >= 0) etSg.setText(String.valueOf(sgcl.sg));
            if (sgcl.zxg >= 0) etZxg.setText(String.valueOf(sgcl.zxg));
        } else {
            etZxg.setText("0");
        }
    }

    public Sgcl showDialog() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok: {
                String s_ymh = etYmh.getText().toString();
                String s_sz = etSz.getText().toString();
                String s_xj = etXj.getText().toString();
                String s_sg = etSg.getText().toString();
                String s_zxg = etZxg.getText().toString();
                int i_ymh = -1;
                int i_sz = -1;
                float f_xj = 0;
                float f_sg = 0;
                float f_zxg = 0;

                if (s_ymh.equals("")) {
                    Toast.makeText(context, "样木号不能为空！", 1).show();
                    break;
                }

                try {
                    i_ymh = Integer.parseInt(s_ymh);
                } catch (java.lang.NumberFormatException nfe) {
                }

                if (sgcl == null) {
                    List<Sgcl> lst = YangdiMgr.GetSgclList(ydh);
                    boolean b = false;
                    for (int i = 0; i < lst.size(); i++) {
                        if (i_ymh == lst.get(i).ymh) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        etYmh.setTextColor(Color.RED);
                        Toast.makeText(context, "该样木已经录入，请选择其他样木。", 1).show();
                        break;
                    }
                }

                i_sz = Resmgr.GetSzCode(s_sz);

                try {
                    f_xj = Float.parseFloat(s_xj);
                    f_xj = MyFuns.MyDecimal(f_xj, 1);
                } catch (java.lang.NumberFormatException nfe) {
                }

                try {
                    f_sg = Float.parseFloat(s_sg);
                    f_sg = MyFuns.MyDecimal(f_sg, 1);
                } catch (java.lang.NumberFormatException nfe) {
                }

                try {
                    f_zxg = Float.parseFloat(s_zxg);
                    f_zxg = MyFuns.MyDecimal(f_zxg, 1);
                } catch (java.lang.NumberFormatException nfe) {
                }

                Yangmu ym = YangdiMgr.GetYangmu(ydh, i_ymh);
                if (ym == null) {
                    Toast.makeText(context, "无效的样木号！请重新填写。", 1).show();
                    break;
                } else {
                    //if(!YangdiMgr.IsYssz(ydh, ym.ymh))
                    //{
                    //	Toast.makeText(context, "该样木不属于优势树种！请重新填写。", 1).show();
                    //	break;
                    //}
                }

                if (Resmgr.IsZhuzi(i_sz)) {
                    if (f_zxg <= 0) {
                        Toast.makeText(context, "竹林树种的竹枝下高必须大于0。", 1).show();
                        break;
                    }
                    if (f_sg > 0) {
                        Toast.makeText(context, "竹林树种的树高应填0。", 1).show();
                        break;
                    }
                    if (f_zxg > YangdiMgr.MAX_SG) {
                        Toast.makeText(context, "竹枝下高超限！", 1).show();
                        break;
                    }
                } else {
                    if (f_sg <= 0) {
                        Toast.makeText(context, "乔木树种的树高必须大于0。", 1).show();
                        break;
                    }
                    if (f_zxg > 0) {
                        Toast.makeText(context, "乔木树种的竹枝下高高应填0。", 1).show();
                        break;
                    }
                    if (f_sg > YangdiMgr.MAX_SG) {
                        Toast.makeText(context, "树高超限！", 1).show();
                        break;
                    }
                }

                result = new Sgcl();
                result.ymh = i_ymh;
                result.sz = i_sz;
                result.xj = f_xj;
                result.sg = f_sg;
                result.zxg = f_zxg;

                if (sgcl != null) result.xh = sgcl.xh;

                SgclDialog.this.cancel();
                break;
            }
            case R.id.btn_cancel: {
                SgclDialog.this.cancel();
                break;
            }
        }
    }
}
