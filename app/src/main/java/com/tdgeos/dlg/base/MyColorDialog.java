package com.tdgeos.dlg.base;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.tanhui.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MyColorDialog extends Dialog implements View.OnClickListener, View.OnFocusChangeListener {
    private Handler mHandler = null;
    private Context context;
    private Button btnOk;
    private Button btnCancel;

    private LinearLayout layView;
    private MyView myview;

    private EditText etRed = null;
    private EditText etGreen = null;
    private EditText etBlue = null;
    private EditText etAlpha = null;
    private SeekBar sbColor = null;
    private TextView tvColor = null;
    private int iSBType = 1;

    private int iRow = 8;
    private int iCol = 8;
    private int iChecked = -1;
    private List<Integer> lstColors = new ArrayList<Integer>();

    private int inColor = 0;
    private int outColor = 0;

    public MyColorDialog(Context context, int color) {
        super(context);
        this.context = context;
        inColor = color;
        outColor = color;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_base_color);
        setTitle("ÑÕÉ«");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        layView = (LinearLayout) findViewById(R.id.lay_view);
        myview = new MyView(context);
        layView.addView(myview);

        tvColor = (TextView) findViewById(R.id.tv_color);
        etRed = (EditText) findViewById(R.id.et_red);
        etGreen = (EditText) findViewById(R.id.et_green);
        etBlue = (EditText) findViewById(R.id.et_blue);
        etAlpha = (EditText) findViewById(R.id.et_alpha);
        sbColor = (SeekBar) findViewById(R.id.sb_color);
        etRed.setInputType(InputType.TYPE_NULL);
        etGreen.setInputType(InputType.TYPE_NULL);
        etBlue.setInputType(InputType.TYPE_NULL);
        etAlpha.setInputType(InputType.TYPE_NULL);

        etRed.setOnClickListener(this);
        etRed.setOnFocusChangeListener(this);
        etGreen.setOnClickListener(this);
        etGreen.setOnFocusChangeListener(this);
        etBlue.setOnClickListener(this);
        etBlue.setOnFocusChangeListener(this);
        etAlpha.setOnClickListener(this);
        etAlpha.setOnFocusChangeListener(this);

        sbColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (iSBType == 0) etAlpha.setText(String.valueOf(progress));
                if (iSBType == 1) etRed.setText(String.valueOf(progress));
                if (iSBType == 2) etGreen.setText(String.valueOf(progress));
                if (iSBType == 3) etBlue.setText(String.valueOf(progress));
                String sr = etRed.getText().toString();
                String sg = etGreen.getText().toString();
                String sb = etBlue.getText().toString();
                String sa = etAlpha.getText().toString();
                int r = 0;
                int g = 0;
                int b = 0;
                int a = 0;
                try {
                    r = Integer.parseInt(sr);
                } catch (Exception e) {
                }
                try {
                    g = Integer.parseInt(sg);
                } catch (Exception e) {
                }
                try {
                    b = Integer.parseInt(sb);
                } catch (Exception e) {
                }
                try {
                    a = Integer.parseInt(sa);
                } catch (Exception e) {
                }
                int color = Color.argb(255 - a, r, g, b);
                tvColor.setBackgroundColor(color);
                outColor = color;
            }
        });

        //0  5  a  f
        lstColors.add(0xff000000);
        lstColors.add(0xff005500);
        lstColors.add(0xff00aa00);
        lstColors.add(0xff00ff00);

        lstColors.add(0xff550000);
        lstColors.add(0xff555500);
        lstColors.add(0xff55aa00);
        lstColors.add(0xff55ff00);

        lstColors.add(0xffaa0000);
        lstColors.add(0xffaa5500);
        lstColors.add(0xffaaaa00);
        lstColors.add(0xffaaff00);

        lstColors.add(0xffff0000);
        lstColors.add(0xffff5500);
        lstColors.add(0xffffaa00);
        lstColors.add(0xffffff00);


        lstColors.add(0xff000055);
        lstColors.add(0xff005555);
        lstColors.add(0xff00aa55);
        lstColors.add(0xff00ff55);

        lstColors.add(0xff550055);
        lstColors.add(0xff555555);
        lstColors.add(0xff55aa55);
        lstColors.add(0xff55ff55);

        lstColors.add(0xffaa0055);
        lstColors.add(0xffaa5555);
        lstColors.add(0xffaaaa55);
        lstColors.add(0xffaaff55);

        lstColors.add(0xffff0055);
        lstColors.add(0xffff5555);
        lstColors.add(0xffffaa55);
        lstColors.add(0xffffff55);


        lstColors.add(0xff0000aa);
        lstColors.add(0xff0055aa);
        lstColors.add(0xff00aaaa);
        lstColors.add(0xff00ffaa);

        lstColors.add(0xff5500aa);
        lstColors.add(0xff5555aa);
        lstColors.add(0xff55aaaa);
        lstColors.add(0xff55ffaa);

        lstColors.add(0xffaa00aa);
        lstColors.add(0xffaa55aa);
        lstColors.add(0xffaaaaaa);
        lstColors.add(0xffaaffaa);

        lstColors.add(0xffff00aa);
        lstColors.add(0xffff55aa);
        lstColors.add(0xffffaaaa);
        lstColors.add(0xffffffaa);


        lstColors.add(0xff0000ff);
        lstColors.add(0xff0055ff);
        lstColors.add(0xff00aaff);
        lstColors.add(0xff00ffff);

        lstColors.add(0xff5500ff);
        lstColors.add(0xff5555ff);
        lstColors.add(0xff55aaff);
        lstColors.add(0xff55ffff);

        lstColors.add(0xffaa00ff);
        lstColors.add(0xffaa55ff);
        lstColors.add(0xffaaaaff);
        lstColors.add(0xffaaffff);

        lstColors.add(0xffff00ff);
        lstColors.add(0xffff55ff);
        lstColors.add(0xffffaaff);
        lstColors.add(0xffffffff);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok: {
                this.cancel();
                break;
            }
            case R.id.btn_cancel: {
                outColor = inColor;
                this.cancel();
                break;
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_red: {
                if (hasFocus) {
                    iSBType = 1;
                    String s = etRed.getText().toString();
                    int red = 0;
                    try {
                        red = Integer.parseInt(s);
                    } catch (Exception e) {
                    }
                    sbColor.setProgress(red);
                }
                break;
            }
            case R.id.et_green: {
                if (hasFocus) {
                    iSBType = 2;
                    String s = etGreen.getText().toString();
                    int green = 0;
                    try {
                        green = Integer.parseInt(s);
                    } catch (Exception e) {
                    }
                    sbColor.setProgress(green);
                }
                break;
            }
            case R.id.et_blue: {
                if (hasFocus) {
                    iSBType = 3;
                    String s = etBlue.getText().toString();
                    int blue = 0;
                    try {
                        blue = Integer.parseInt(s);
                    } catch (Exception e) {
                    }
                    sbColor.setProgress(blue);
                }
                break;
            }
            case R.id.et_alpha: {
                if (hasFocus) {
                    iSBType = 0;
                    String s = etAlpha.getText().toString();
                    int alpha = 0;
                    try {
                        alpha = Integer.parseInt(s);
                    } catch (Exception e) {
                    }
                    sbColor.setProgress(alpha);
                }
                break;
            }
        }
    }

    public int showDialog() {
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
        return outColor;
    }

    class MyView extends View implements View.OnTouchListener {
        private GestureDetector mGestureDetector = null;
        private Paint paint = new Paint();
        private int width = 0;
        private int height = 0;
        private int bdWidth = 3;
        private int iSpac = 16;
        int iBlockWidth = 0;
        int iBlockHeight = 0;
        int iColorWidth = 0;
        int iColorHeight = 0;
        boolean isFirst = true;

        public MyView(Context context) {
            super(context);
            mGestureDetector = new GestureDetector(context, new LearnGestureListener());
            this.setOnTouchListener(this);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (isFirst) {
                width = this.getWidth();
                height = this.getHeight();
                iBlockWidth = (int) ((width - bdWidth * 2 - iSpac * iRow - bdWidth * 2 * iRow) * 1.0f / iRow);
                iBlockHeight = (int) ((height - bdWidth * 2 - iSpac * iCol - bdWidth * 2 * iCol) * 1.0f / iCol);
                iColorWidth = iBlockWidth + bdWidth * 2 + iSpac;
                iColorHeight = iBlockHeight + bdWidth * 2 + iSpac;
                isFirst = false;
            }

            paint.setColor(0xff80c0ff);
            paint.setStyle(Style.STROKE);
            paint.setStrokeWidth(bdWidth * 2);
            canvas.drawRect(0, 0, width, height, paint);

            int xBegin = 0;
            int yBegin = 0;
            float left = 0;
            float top = 0;
            float right = 0;
            float bottom = 0;
            int k = 0;
            for (int i = 0; i < iRow; i++) {
                for (int j = 0; j < iCol; j++) {
                    xBegin = iColorWidth * j;
                    yBegin = iColorHeight * i;
                    left = xBegin + iSpac / 2.0f + bdWidth / 2.0f + bdWidth;
                    top = yBegin + iSpac / 2.0f + bdWidth / 2.0f + bdWidth;
                    right = xBegin + iSpac / 2.0f + iBlockWidth + bdWidth / 2.0f + bdWidth;
                    bottom = yBegin + iSpac / 2.0f + iBlockHeight + bdWidth / 2.0f + bdWidth;
                    if (iChecked == k) {
                        paint.setColor(0xff0000ff);
                        paint.setStyle(Style.STROKE);
                        paint.setStrokeWidth(bdWidth * 2);
                        canvas.drawRect(left, top, right, bottom, paint);
                        paint.setStyle(Style.FILL);
                        paint.setStrokeWidth(0);
                        int r = 5;
                        canvas.drawRect(left - bdWidth - r, top - bdWidth - r, left + bdWidth + r, top + bdWidth + r, paint);
                        canvas.drawRect(left - bdWidth - r, bottom - bdWidth - r, left + bdWidth + r, bottom + bdWidth + r, paint);
                        canvas.drawRect(right - bdWidth - r, top - bdWidth - r, right + bdWidth + r, top + bdWidth + r, paint);
                        canvas.drawRect(right - bdWidth - r, bottom - bdWidth - r, right + bdWidth + r, bottom + bdWidth + r, paint);
                    } else {
                        paint.setColor(0xff000000);
                        paint.setStyle(Style.STROKE);
                        paint.setStrokeWidth(bdWidth);
                        canvas.drawRect(left, top, right, bottom, paint);
                    }

                    left = xBegin + iSpac / 2.0f + bdWidth + bdWidth;
                    top = yBegin + iSpac / 2.0f + bdWidth + bdWidth;
                    right = xBegin + iSpac / 2.0f + iBlockWidth + bdWidth;
                    bottom = yBegin + iSpac / 2.0f + iBlockHeight + bdWidth;
                    int color = lstColors.get(k++);
                    paint.setColor(color);
                    paint.setStyle(Style.FILL);
                    paint.setStrokeWidth(0);
                    canvas.drawRect(left, top, right, bottom, paint);
                }
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return mGestureDetector.onTouchEvent(event);
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

            @Override
            public boolean onSingleTapConfirmed(MotionEvent ev) {
                int k = 0;
                int xBegin = 0;
                int yBegin = 0;
                float left = 0;
                float right = 0;
                float bottom = 0;
                float top = 0;
                float x = ev.getX();
                float y = ev.getY();
                iChecked = -1;
                for (int i = 0; i < iRow; i++) {
                    for (int j = 0; j < iCol; j++) {
                        xBegin = iColorWidth * j;
                        yBegin = iColorHeight * i;
                        left = xBegin + iSpac / 2.0f + bdWidth / 2.0f + bdWidth;
                        top = yBegin + iSpac / 2.0f + bdWidth / 2.0f + bdWidth;
                        right = xBegin + iSpac / 2.0f + iBlockWidth + bdWidth / 2.0f + bdWidth;
                        bottom = yBegin + iSpac / 2.0f + iBlockHeight + bdWidth / 2.0f + bdWidth;
                        if (x > left && x < right && y > top && y < bottom) {
                            iChecked = k;
                            break;
                        }
                        k++;
                    }
                    if (iChecked != -1) break;
                }
                if (iChecked >= 0) {
                    int color = lstColors.get(iChecked);
                    int red = Color.red(color);
                    int green = Color.green(color);
                    int blue = Color.blue(color);
                    etRed.setText(String.valueOf(red));
                    etGreen.setText(String.valueOf(green));
                    etBlue.setText(String.valueOf(blue));
                    tvColor.setBackgroundColor(color);
                    outColor = color;
                    if (iSBType == 1) sbColor.setProgress(red);
                    if (iSBType == 2) sbColor.setProgress(green);
                    if (iSBType == 3) sbColor.setProgress(blue);
                }
                MyView.this.postInvalidate();
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent ev) {
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent ev) {
                return true;
            }
        }
    }


}