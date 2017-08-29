package com.tdgeos.dlg.yddc;

import com.tdgeos.lib.CoordTransform;
import com.tdgeos.lib.MyCoord;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.lib.MyPoint;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.YangdiMgr;
import com.tdgeos.yangdi.Yddww;

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
import android.view.WindowManager;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class YdzbDialog extends Dialog implements View.OnClickListener {
    private Handler mHandler = null;
    private Context context;

    private EditText etLon = null;
    private EditText etLat = null;
    private EditText etHzb = null;
    private EditText etZzb = null;
    private EditText etCpj = null;

    private Button btnOk = null;
    private Button btnCancel = null;

    private int ydh = 0;

    private boolean lock = true;

    public YdzbDialog(Context context, int ydh) {
        super(context);
        this.context = context;
        this.ydh = ydh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_yindianwzt_input);
        this.setTitle("引点坐标及磁偏角");
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

        etLon = (EditText) findViewById(R.id.et_lon);
        etLat = (EditText) findViewById(R.id.et_lat);
        etHzb = (EditText) findViewById(R.id.et_hzb);
        etZzb = (EditText) findViewById(R.id.et_zzb);
        etCpj = (EditText) findViewById(R.id.et_cpj);

        etLon.setEnabled(false);
        etLat.setEnabled(false);
        etHzb.setEnabled(true);
        etZzb.setEnabled(true);

        etCpj.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        RadioGroup rg = (RadioGroup) findViewById(R.id.rg_type);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_hzb) {
                    etLon.setEnabled(false);
                    etLat.setEnabled(false);
                    etHzb.setEnabled(true);
                    etZzb.setEnabled(true);
                }
                if (checkedId == R.id.rb_lon) {
                    etHzb.setEnabled(false);
                    etZzb.setEnabled(false);
                    etLon.setEnabled(true);
                    etLat.setEnabled(true);
                }
            }
        });

        etLon.addTextChangedListener(new TextWatcher() {
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
                String s2 = etLat.getText().toString();
                float f2 = -1;
                try {
                    f2 = Float.parseFloat(s2);
                } catch (Exception e) {
                }
                if (f >= 73 && f <= 135) {
                    etLon.setTextColor(Color.BLACK);
                    if (f2 >= 4 && f2 <= 53 && lock) {
                        MyCoord c = new MyCoord(f, f2, 0);
                        GpsToBj54(c);
                        etHzb.setText(String.valueOf((int) c.x));
                        etZzb.setText(String.valueOf((int) c.y));
                    }
                } else {
                    etLon.setTextColor(Color.RED);
                    if (lock) {
                        etHzb.setText("");
                        etZzb.setText("");
                    }
                }
            }
        });
        etLon.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) lock = arg1;
            }
        });

        etLat.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().trim();
                if (!text.equals("")) {
                    float f = -1;
                    try {
                        f = Float.parseFloat(text);
                    } catch (Exception e) {
                    }
                    String s2 = etLon.getText().toString();
                    float f2 = -1;
                    try {
                        f2 = Float.parseFloat(s2);
                    } catch (Exception e) {
                    }
                    if (f < 4 || f > 53) {
                        etLat.setTextColor(Color.RED);
                        if (lock) {
                            etHzb.setText("");
                            etZzb.setText("");
                        }
                    } else {
                        etLat.setTextColor(Color.BLACK);
                        if (f2 >= 73 && f2 <= 135 && lock) {
                            MyCoord c = new MyCoord(f2, f, 0);
                            GpsToBj54(c);
                            etHzb.setText(String.valueOf((int) c.x));
                            etZzb.setText(String.valueOf((int) c.y));
                        }
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etLat.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) lock = arg1;
            }
        });

        etHzb.addTextChangedListener(new TextWatcher() {
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
                String s2 = etZzb.getText().toString();
                float f2 = -1;
                try {
                    f2 = Float.parseFloat(s2);
                } catch (Exception e) {
                }
                if (!lock) {
                    MyCoord c = new MyCoord(f, f2, 0);
                    Bj54ToGps(c);
                    c.x = MyFuns.MyDecimal(c.x, 7);
                    c.y = MyFuns.MyDecimal(c.y, 7);
                    etLon.setText(String.valueOf(c.x));
                    etLat.setText(String.valueOf(c.y));
                }
            }
        });
        etHzb.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) lock = !arg1;
            }
        });

        etZzb.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().trim();
                if (!text.equals("")) {
                    float f = -1;
                    try {
                        f = Float.parseFloat(text);
                    } catch (Exception e) {
                    }
                    String s2 = etHzb.getText().toString();
                    float f2 = -1;
                    try {
                        f2 = Float.parseFloat(s2);
                    } catch (Exception e) {
                    }
                    if (!lock) {
                        MyCoord c = new MyCoord(f2, f, 0);
                        Bj54ToGps(c);
                        c.x = MyFuns.MyDecimal(c.x, 7);
                        c.y = MyFuns.MyDecimal(c.y, 7);
                        etLon.setText(String.valueOf(c.x));
                        etLat.setText(String.valueOf(c.y));
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etZzb.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) lock = !arg1;
            }
        });

        double[] dd = YangdiMgr.GetYdloc22(ydh);
        if (dd != null) {
            int hzb = (int) dd[2];
            int zzb = (int) dd[3];
            etLon.setText(String.valueOf(dd[0]));
            etLat.setText(String.valueOf(dd[1]));
            etHzb.setText(String.valueOf(hzb));
            etZzb.setText(String.valueOf(zzb));
        }
        float cpj = YangdiMgr.GetYdcpj(ydh);
        if (cpj >= 0) {
            etCpj.setText(String.valueOf(cpj));
        }

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    public void showDialog() {
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
        return;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok: {
                String s_lon = etLon.getText().toString();
                String s_lat = etLat.getText().toString();
                String s_hzb = etHzb.getText().toString();
                String s_zzb = etZzb.getText().toString();

                String s_cpj = etCpj.getText().toString();

                double f_lon = -1;
                double f_lat = -1;

                int i_hzb = -1;
                int i_zzb = -1;

                float f_cpj = -1;

                try {
                    f_lon = Double.parseDouble(s_lon);
                } catch (java.lang.NumberFormatException nfe) {
                }
                try {
                    f_lat = Double.parseDouble(s_lat);
                } catch (java.lang.NumberFormatException nfe) {
                }

                try {
                    i_hzb = Integer.parseInt(s_hzb);
                } catch (java.lang.NumberFormatException nfe) {
                }
                try {
                    i_zzb = Integer.parseInt(s_zzb);
                } catch (java.lang.NumberFormatException nfe) {
                }

                try {
                    f_cpj = Float.parseFloat(s_cpj);
                } catch (java.lang.NumberFormatException nfe) {
                }

                if (!s_lon.equals("") && f_lon < 73 || f_lon > 135) {
                    Toast.makeText(context, "经度超限！", 1).show();
                    break;
                }
                if (!s_lat.equals("") && f_lat < 4 || f_lat > 53) {
                    Toast.makeText(context, "纬度超限！", 1).show();
                    break;
                }
                if (!s_cpj.equals("") && f_cpj < YangdiMgr.MIN_FWJ || f_cpj > YangdiMgr.MAX_FWJ) {
                    Toast.makeText(context, "磁偏角超限！", 1).show();
                    break;
                }

                YangdiMgr.SetYdloc2(ydh, new MyPoint(f_lon, f_lat), new MyCoord(i_hzb, i_zzb, 0));
                YangdiMgr.SetYdcpj(ydh, f_cpj);

                this.cancel();
                break;
            }
            case R.id.btn_cancel: {
                this.cancel();
                break;
            }
        }
    }

    public static int Get6Daihao(int lon) {
        if (lon == 75) return 13;
        if (lon == 81) return 14;
        if (lon == 87) return 15;
        if (lon == 93) return 16;
        if (lon == 99) return 17;
        if (lon == 105) return 18;
        if (lon == 111) return 19;
        if (lon == 117) return 20;
        if (lon == 123) return 21;
        if (lon == 129) return 22;
        if (lon == 135) return 23;
        return 0;
    }

    public static int Get3Daihao(int lon) {
        return lon / 3;
    }

    public static void GpsToBj54(MyCoord pt) {
        int zone = MyConfig.GetParamsZone();
        int lon0 = MyConfig.GetParamsLon0();
        double dx = MyConfig.GetParamsDx();
        double dy = MyConfig.GetParamsDy();
        double dz = MyConfig.GetParamsDz();
        double rx = MyConfig.GetParamsRx();
        double ry = MyConfig.GetParamsRy();
        double rz = MyConfig.GetParamsRz();
        double k = MyConfig.GetParamsK();

        MyCoord c1 = CoordTransform.GeoToKjzj_wgs84(pt);
        MyCoord c2 = CoordTransform.Transform(c1, -dx, -dy, -dz, rx, ry, rz, k);
        MyCoord c3 = CoordTransform.KjzjToGeo_bj54(c2);
        MyCoord c4 = CoordTransform.GeoToGauss_bj54(c3, lon0);
        if (zone == 6) {
            pt.x = c4.x + Get6Daihao(lon0) * 1000000;
        } else if (zone == 3) {
            pt.x = c4.x + Get3Daihao(lon0) * 1000000;
        }
        pt.y = c4.y;
    }

    public static void Bj54ToGps(MyCoord pt) {
        int zone = MyConfig.GetParamsZone();
        int lon0 = MyConfig.GetParamsLon0();
        double dx = MyConfig.GetParamsDx();
        double dy = MyConfig.GetParamsDy();
        double dz = MyConfig.GetParamsDz();
        double rx = MyConfig.GetParamsRx();
        double ry = MyConfig.GetParamsRy();
        double rz = MyConfig.GetParamsRz();
        double k = MyConfig.GetParamsK();

        pt.x = pt.x % 1000000;
        MyCoord c1 = CoordTransform.GaussToGeo_bj54(pt, lon0);
        MyCoord c2 = CoordTransform.GeoToKjzj_bj54(c1);
        MyCoord c3 = CoordTransform.Transform(c2, dx, dy, dz, -rx, -ry, -rz, k);
        MyCoord c4 = CoordTransform.KjzjToGeo_wgs84(c3);
        pt.x = c4.x;
        pt.y = c4.y;
    }
}
