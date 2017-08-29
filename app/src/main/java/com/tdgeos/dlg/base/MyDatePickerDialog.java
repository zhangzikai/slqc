package com.tdgeos.dlg.base;

import java.util.Calendar;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class MyDatePickerDialog extends Dialog implements OnClickListener {
    private Handler mHandler;
    private String result = null;
    private String date = null;
    private String time = null;

    private LinearLayout layDp = null;
    private LinearLayout layTp = null;
    private DatePicker datePicker = null;
    private TimePicker timePicker = null;

    private int iType = 0; //0表示日期+时间，1表示仅日期，2表示仅时间

    private Button btnOk;
    private Button btnCancel;

    public MyDatePickerDialog(Context context, int type) {
        super(context);
        iType = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_base_datepicker);
        setTitle("日期和时间");
        getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setCanceledOnTouchOutside(false);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        layDp = (LinearLayout) findViewById(R.id.lay_dp);
        layTp = (LinearLayout) findViewById(R.id.lay_tp);
        datePicker = (DatePicker) findViewById(R.id.dp);
        timePicker = (TimePicker) findViewById(R.id.tp);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        //date = year + "-" + (month+1) + "-" + day;
        int y = year % 100;
        int m = month + 1;
        String sy = y >= 10 ? String.valueOf(y) : "0" + y;
        String sm = m >= 10 ? String.valueOf(m) : "0" + m;
        String sd = day >= 10 ? String.valueOf(day) : "0" + day;
        date = sy + sm + sd;
        String sh = hour >= 10 ? String.valueOf(hour) : "0" + hour;
        String smi = minute >= 10 ? String.valueOf(minute) : "0" + minute;
        time = sh + ":" + smi;

        datePicker.init(year, month, day, new OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //date = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                year = year % 100;
                monthOfYear += 1;
                String sy = year >= 10 ? String.valueOf(year) : "0" + year;
                String sm = monthOfYear >= 10 ? String.valueOf(monthOfYear) : "0" + monthOfYear;
                String sd = dayOfMonth >= 10 ? String.valueOf(dayOfMonth) : "0" + dayOfMonth;
                date = sy + sm + sd;
            }
        });
        datePicker.setCalendarViewShown(false);

        timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                //time = hourOfDay + ":" + minute;
                String sh = hourOfDay >= 10 ? String.valueOf(hourOfDay) : "0" + hourOfDay;
                String smi = minute >= 10 ? String.valueOf(minute) : "0" + minute;
                time = sh + ":" + smi;
            }
        });

        if (iType == 1) {
            layTp.setVisibility(8);
        }
        if (iType == 2) {
            layDp.setVisibility(8);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok: {
                if (iType == 0) {
                    result = date + " " + time;
                }
                if (iType == 1) {
                    result = date;
                }
                if (iType == 2) {
                    result = time;
                }
                this.cancel();
                break;
            }
            case R.id.btn_cancel: {
                this.cancel();
                break;
            }
        }
    }

    public String showDialog() {
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
