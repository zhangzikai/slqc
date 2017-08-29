package com.tdgeos.tanhui;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.yangdi.YangdiMgr;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class SlqcPhoto extends Activity implements View.OnClickListener, View.OnTouchListener {
    private GestureDetector mGestureDetector = null;
    private EditText etName = null;
    private TextView tvYmh = null;
    private EditText etYmh = null;
    private EditText etInfo = null;
    private Button btnSave = null;
    private Button btnDelete = null;
    private RadioGroup rgType = null;
    private ImageView ivImage = null;
    private Bitmap bmpImage = null;
    private String dbPath = null;
    private int type = 0;
    private int ydh = 0;

    private int count = 0;
    private int curPos = 0;
    private List<PhotoInfo> lstZps = new ArrayList<PhotoInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.slqc_photo);

        Intent intent = this.getIntent();
        ydh = intent.getIntExtra("ydh", 0);
        dbPath = YangdiMgr.getDbFile(ydh);

        mGestureDetector = new GestureDetector(this, new LearnGestureListener());

        ivImage = (ImageView) findViewById(R.id.iv_image);
        ivImage.setScaleType(ScaleType.FIT_CENTER);
        ivImage.setOnTouchListener(this);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(this);
        rgType = (RadioGroup) findViewById(R.id.rg_type);
        rgType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                switch (arg1) {
                    case R.id.rb_all: {
                        type = 0;
                        initData();
                        break;
                    }
                    case R.id.rb_yd: {
                        type = 1;
                        initData();
                        break;
                    }
                    case R.id.rb_ym: {
                        type = 2;
                        initData();
                        break;
                    }
                    case R.id.rb_qt: {
                        type = 3;
                        initData();
                        break;
                    }
                }
            }
        });

        etName = (EditText) findViewById(R.id.et_name);
        tvYmh = (TextView) findViewById(R.id.tv_ymh);
        etYmh = (EditText) findViewById(R.id.et_ymh);
        etInfo = (EditText) findViewById(R.id.et_info);
        etName.setInputType(InputType.TYPE_NULL);
        etYmh.setInputType(InputType.TYPE_NULL);

        initData();
    }

    @Override
    public void onDestroy() {
        if (bmpImage != null) bmpImage.recycle();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initData() {
        curPos = -1;
        lstZps.clear();
        String sql = null;
        if (type == 0) sql = "select zph,type,ymh,name,notes from zp";
        if (type == 1) sql = "select zph,type,ymh,name,notes from zp where type = '1'";
        if (type == 2) sql = "select zph,type,ymh,name,notes from zp where type = '2'";
        if (type == 3) sql = "select zph,type,ymh,name,notes from zp where type = '3'";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
        Cursor cursor = db.rawQuery(sql, null);
        count = cursor.getCount();
        if (count == 0) {
            cursor.close();
            db.close();
            setNoData();
            return;
        }
        cursor.moveToFirst();
        for (int i = 0; i < count; i++) {
            PhotoInfo pi = new PhotoInfo();
            pi.zph = cursor.getInt(0);
            pi.type = cursor.getInt(1);
            pi.ymh = cursor.getInt(2);
            pi.name = cursor.getString(3);
            pi.info = cursor.getString(4);
            lstZps.add(pi);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        curPos = 0;
        setData(0);
    }

    private void setData(int pos) {
        if (pos < 0 || pos >= count) return;
        PhotoInfo pi = lstZps.get(pos);
        if (bmpImage != null) bmpImage.recycle();
        bmpImage = YangdiMgr.GetZp(ydh, pi.zph);
        if (bmpImage != null) ivImage.setImageBitmap(bmpImage);
        else ivImage.setImageResource(R.drawable.ic_default_image);
        etName.setText(pi.name);
        if (pi.type == 2) {
            etYmh.setText(String.valueOf(pi.ymh));
            tvYmh.setVisibility(1);
            etYmh.setVisibility(1);
        } else {
            etYmh.setText("");
            tvYmh.setVisibility(4);
            etYmh.setVisibility(4);
        }
        etInfo.setText(pi.info);
    }

    private void setNoData() {
        curPos = -1;
        btnDelete.setVisibility(4);
        ivImage.setImageResource(R.drawable.ic_default_image);
        etName.setText("");
        etYmh.setText("");
        tvYmh.setVisibility(4);
        etYmh.setVisibility(4);
        etInfo.setText("");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete: {
                if (count > 0 && curPos >= 0 && curPos < count) {
                    MyMakeSureDialog dlg = new MyMakeSureDialog(SlqcPhoto.this, "提示", "删除后将不可恢复，是否继续删除？", "删除", "取消");
                    if (dlg.showDialog()) {
                        String sql = "delete from zp where zph = '" + lstZps.get(curPos).zph + "'";
                        YangdiMgr.ExecSQL(ydh, sql);
                        YangdiMgr.ResetZpCount(ydh);
                        lstZps.remove(curPos);
                        count--;
                        if (curPos >= count) curPos = count - 1;
                        if (curPos < 0) curPos = 0;
                        if (count == 0) {
                            setNoData();
                        } else {
                            setData(curPos);
                        }
                    }
                }
            }
            break;
            case R.id.btn_save: {
                if (count > 0 && curPos >= 0 && curPos < count) {
                    String info = etInfo.getText().toString();
                    lstZps.get(curPos).info = info;
                    String sql = "update zp set notes = '" + info + "' where zph = '" + lstZps.get(curPos).zph + "'";
                    YangdiMgr.ExecSQL(ydh, sql);
                }
            }
            break;
        }
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
            if (e1.getX() - e2.getX() > 100 && Math.abs(velocityX) > 10)//右滑
            {
                if (curPos < count - 1) {
                    curPos++;
                    setData(curPos);
                }
            }
            if (e1.getX() - e2.getX() < 100 && Math.abs(velocityX) > 10)//左滑
            {
                if (curPos > 0) {
                    curPos--;
                    setData(curPos);
                }
            }
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent ev) {
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

    class PhotoInfo {
        int zph;
        int type;
        String name;
        String info;
        int ymh;
    }
}
