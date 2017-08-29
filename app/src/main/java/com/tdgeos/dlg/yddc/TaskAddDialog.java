package com.tdgeos.dlg.yddc;

import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Qianqimgr;
import com.tdgeos.yangdi.YangdiMgr;

public class TaskAddDialog extends Dialog implements OnClickListener {
    private LinearLayout layXian;
    private LinearLayout layInput;

    private Spinner spShi;
    private Spinner spXian;
    private EditText etInputYdh;
    private CheckBox cbClear;
    private Button btnOk;
    private Button btnCancel;
    private Button btnAll;

    private int curType = 1;

    private Context ctt;
    private Handler mHandler;
    private int[] result = null;

    public TaskAddDialog(Context context) {
        super(context);
        ctt = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_yddc_taskadd);
        setTitle("添加样地");
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        //this.getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        btnAll = (Button) findViewById(R.id.btn_all);
        btnAll.setOnClickListener(this);

        cbClear = (CheckBox) findViewById(R.id.cb_clear);

        layXian = (LinearLayout) findViewById(R.id.lay_xian);
        layInput = (LinearLayout) findViewById(R.id.lay_ydh);

        spShi = (Spinner) findViewById(R.id.sp_shi);
        spXian = (Spinner) findViewById(R.id.sp_xian);

        etInputYdh = (EditText) findViewById(R.id.et_ydh);

        spShi.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                setXian();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        RadioGroup rg = (RadioGroup) findViewById(R.id.rg_type);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_xzq) {
                    layXian.setVisibility(1);
                    layInput.setVisibility(8);
                    curType = 1;
                }
                if (checkedId == R.id.rb_ydh) {
                    layXian.setVisibility(8);
                    layInput.setVisibility(1);
                    curType = 2;
                }
            }
        });

        setShi();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok: {
                if (cbClear.isChecked()) {
                    MyMakeSureDialog dlg = new MyMakeSureDialog(ctt, "提示", "清空之前是否需要备份原有列表中的数据？\n清空操作并不会删除任何已经录入的数据！", "继续清空", "返回备份");
                    if (!dlg.showDialog()) {
                        break;
                    }
                }
                if (curType == 1) {
                    int pos = spXian.getSelectedItemPosition();
                    if (pos < 0) {
                        Toast.makeText(ctt, "输入的样地号无效！请重新输入", 1).show();
                        return;
                    }
                    String xian = spXian.getSelectedItem().toString();
                    int code = Qianqimgr.GetXianCode(xian);
                    result = new int[3];
                    result[0] = 1;
                    result[1] = code;
                    result[2] = cbClear.isChecked() ? 1 : 0;
                } else if (curType == 2) {
                    String str = etInputYdh.getText().toString();
                    int ydh = -1;
                    try {
                        ydh = Integer.parseInt(str);
                    } catch (NumberFormatException e) {
                    }
                    int xianCode = Qianqimgr.GetXianCodeByYdh(ydh);
                    String xianName = Qianqimgr.GetXianName(xianCode);
                    if (xianName == null || xianCode <= 0) {
                        Toast.makeText(ctt, "输入的样地号无效！请重新输入", 1).show();
                        return;
                    }
                    result = new int[3];
                    result[0] = 2;
                    result[1] = ydh;
                    result[2] = cbClear.isChecked() ? 1 : 0;
                }
                this.cancel();
            }
            break;
            case R.id.btn_all: {
                result = new int[3];
                result[0] = 3;
                result[1] = 0;
                result[2] = cbClear.isChecked() ? 1 : 0;
                this.cancel();
                break;
            }
            case R.id.btn_cancel:
                this.cancel();
                break;
        }
    }

    /*
    @Override
    public boolean onLongClick(View arg0) {
        List<String> lst = new ArrayList<String>();
        lst.add("删除");
        MyListDialog dlg = new MyListDialog(ctt, "选项", lst);
        int r = dlg.showDialog();
        if(r == 0)
        {
            MyMakeSureDialog msdlg = new MyMakeSureDialog(ctt, "删除", "删除后将无法恢复，是否继续删除？\n该操作不会删除任何已调查数据.", "删除", "取消");
            if(msdlg.showDialog())
            {
                int code = (Integer)arg0.getTag();
                delData(code);
                initData();
            }
        }
        return false;
    }
    */
    public int[] showDialog() {
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

    private void setShi() {
        List<String> lstShi = Qianqimgr.GetShiList(YangdiMgr.SHENG_CODE);
        ArrayAdapter<String> apShi = new ArrayAdapter<String>(ctt, android.R.layout.simple_spinner_item, lstShi);
        apShi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spShi.setAdapter(apShi);
    }

    private void setXian() {
        String shi = spShi.getSelectedItem().toString().trim();
        List<String> lstXian = Qianqimgr.GetXianList(Qianqimgr.GetShiCode(shi));
        ArrayAdapter<String> apXian = new ArrayAdapter<String>(ctt, android.R.layout.simple_spinner_item, lstXian);
        apXian.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spXian.setAdapter(apXian);
    }
    /*
    private void initData()
	{
		if(!MyFile.Exists(MyConfig.GetAppdir() + "/qianqi.dat"))
		{
			return;
		}
		layList.removeAllViews();
		List<String> lstShi = Qianqimgr.GetShiList(YangdiMgr.SHENG_CODE);
		List<String> lstXian = Qianqimgr.GetXianList(149000);

		for(int i=0;i<lstShi.size();i++)
		{
			String name = lstShi.get(i);
			int code = Qianqimgr.GetShiCode(name);
			if(code != 149000) addRow(name, code);
		}
		
		for(int i=0;i<lstXian.size();i++)
		{
			String name = lstXian.get(i);
			int code = Qianqimgr.GetXianCode(name);
			addRow(name, code);
		}
	}
	
	private void addRow(String name, int code)
	{
		if(name == null || code <= 0) return;
		LinearLayout layRow = (LinearLayout)layInflater.inflate(R.layout.dlg_yddc_taskadd_dataitem, null);
		TextView tvName = (TextView)layRow.findViewById(R.id.tv_name);
		tvName.setText(name);
		layRow.setTag(code);
		//layRow.setOnLongClickListener(this);
		layList.addView(layRow);
	}

	private boolean addData(String dat)
	{
		SQLiteDatabase dbSrc = null;
		try{
			dbSrc = SQLiteDatabase.openOrCreateDatabase(dat, null);
		}catch(Exception e){}
		if(dbSrc == null) return false;
		
		SQLiteDatabase dbDst = null;
		try{
			dbDst = SQLiteDatabase.openOrCreateDatabase(MyConfig.GetAppdir() + "/qianqi.dat", null);
		}catch(Exception e){}
		if(dbDst == null) return false;
		
		List<Integer> lstShi = new ArrayList<Integer>();
		List<Integer> lstXian = new ArrayList<Integer>();
		
		String sql = "select * from shi";
		Cursor sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				int code = sor.getInt(0);
				String name = sor.getString(1);
				int parent = sor.getInt(2);
				sql = "delete from shi where code = '" + code + "'";
				dbDst.execSQL(sql);
				sql = "insert into shi values('" + code + "', '" + name + "', '" + parent + "')";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		sql = "select * from xian";
		sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				int code = sor.getInt(0);
				sql = "delete from xian where code = '" + code + "'";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		String sql_1 = "delete from slqc_ydyz where xian not in(select code from xian) and (f2 is null or f2 not in('1','2','3','4','5','6','7','8','9'))";
		String sql_2 = "delete from slqc_ydyz where f2 is not null and f2 in('1','2','3','4','5','6','7','8','9') and f2 not in(select code from xian where code < 10)";
		String sql_3 = "delete from slqc_kjl where ydh not in(select ydh from slqc_ydyz)";
		String sql_4 = "delete from slqc_mmjc where ydh not in(select ydh from slqc_ydyz)";
		String sql_5 = "delete from slqc_kpfm where ydh not in(select ydh from slqc_ydyz)";
		String sql_6 = "delete from slqc_qt where ydh not in(select ydh from slqc_ydyz)";
		String sql_7 = "delete from slqc_sgcl where ydh not in(select ydh from slqc_ydyz)";
		String sql_8 = "delete from slqc_slzh where ydh not in(select ydh from slqc_ydyz)";
		String sql_9 = "delete from slqc_trgx where ydh not in(select ydh from slqc_ydyz)";
		String sql_10 = "delete from slqc_wclzld where ydh not in(select ydh from slqc_ydyz)";
		String sql_11 = "delete from slqc_xmdc where ydh not in(select ydh from slqc_ydyz)";
		String sql_12 = "delete from slqc_yangdidww where ydh not in(select ydh from slqc_ydyz)";
		String sql_13 = "delete from slqc_ydbh where ydh not in(select ydh from slqc_ydyz)";
		String sql_14 = "delete from slqc_yindiandww where ydh not in(select ydh from slqc_ydyz)";
		String sql_15 = "delete from slqc_yxcl where ydh not in(select ydh from slqc_ydyz)";
		String sql_16 = "delete from slqc_zbdc where ydh not in(select ydh from slqc_ydyz)";
		String sql_17 = "delete from slqc_zjcl where ydh not in(select ydh from slqc_ydyz)";
		dbDst.execSQL(sql_1);
		dbDst.execSQL(sql_2);
		dbDst.execSQL(sql_3);
		dbDst.execSQL(sql_4);
		dbDst.execSQL(sql_5);
		dbDst.execSQL(sql_6);
		dbDst.execSQL(sql_7);
		dbDst.execSQL(sql_8);
		dbDst.execSQL(sql_9);
		dbDst.execSQL(sql_10);
		dbDst.execSQL(sql_11);
		dbDst.execSQL(sql_12);
		dbDst.execSQL(sql_13);
		dbDst.execSQL(sql_14);
		dbDst.execSQL(sql_15);
		dbDst.execSQL(sql_16);
		dbDst.execSQL(sql_17);
		
		sql = "select * from xian";
		sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				int code = sor.getInt(0);
				String name = sor.getString(1);
				int parent = sor.getInt(2);
				sql = "insert into xian values('" + code + "', '" + name + "', '" + parent + "')";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		sql = "select * from slqc_ydyz";
		sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				sql = "insert into slqc_ydyz values(";
				for(int j=0;j<sor.getColumnCount();j++)
				{
					if(j == 0) sql += "'" + sor.getString(j) + "'";
					else sql += "', " + sor.getString(j) + "'";
				}
				sql += ")";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		sql = "select * from slqc_kjl";
		sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				sql = "insert into slqc_kjl values(";
				for(int j=0;j<sor.getColumnCount();j++)
				{
					if(j == 0) sql += "'" + sor.getString(j) + "'";
					else sql += "', " + sor.getString(j) + "'";
				}
				sql += ")";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		sql = "select * from slqc_mmjc";
		sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				sql = "insert into slqc_mmjc values(";
				for(int j=0;j<sor.getColumnCount();j++)
				{
					if(j == 0) sql += "'" + sor.getString(j) + "'";
					else sql += "', " + sor.getString(j) + "'";
				}
				sql += ")";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		sql = "select * from slqc_kpfm";
		sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				sql = "insert into slqc_kpfm values(";
				for(int j=0;j<sor.getColumnCount();j++)
				{
					if(j == 0) sql += "'" + sor.getString(j) + "'";
					else sql += "', " + sor.getString(j) + "'";
				}
				sql += ")";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		sql = "select * from slqc_qt";
		sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				sql = "insert into slqc_qt values(";
				for(int j=0;j<sor.getColumnCount();j++)
				{
					if(j == 0) sql += "'" + sor.getString(j) + "'";
					else sql += "', " + sor.getString(j) + "'";
				}
				sql += ")";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		sql = "select * from slqc_sgcl";
		sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				sql = "insert into slqc_sgcl values(";
				for(int j=0;j<sor.getColumnCount();j++)
				{
					if(j == 0) sql += "'" + sor.getString(j) + "'";
					else sql += "', " + sor.getString(j) + "'";
				}
				sql += ")";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		sql = "select * from slqc_slzh";
		sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				sql = "insert into slqc_slzh values(";
				for(int j=0;j<sor.getColumnCount();j++)
				{
					if(j == 0) sql += "'" + sor.getString(j) + "'";
					else sql += "', " + sor.getString(j) + "'";
				}
				sql += ")";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		sql = "select * from slqc_trgx";
		sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				sql = "insert into slqc_trgx values(";
				for(int j=0;j<sor.getColumnCount();j++)
				{
					if(j == 0) sql += "'" + sor.getString(j) + "'";
					else sql += "', " + sor.getString(j) + "'";
				}
				sql += ")";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		sql = "select * from slqc_wclzld";
		sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				sql = "insert into slqc_wclzld values(";
				for(int j=0;j<sor.getColumnCount();j++)
				{
					if(j == 0) sql += "'" + sor.getString(j) + "'";
					else sql += "', " + sor.getString(j) + "'";
				}
				sql += ")";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		sql = "select * from slqc_xmdc";
		sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				sql = "insert into slqc_xmdc values(";
				for(int j=0;j<sor.getColumnCount();j++)
				{
					if(j == 0) sql += "'" + sor.getString(j) + "'";
					else sql += "', " + sor.getString(j) + "'";
				}
				sql += ")";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		sql = "select * from slqc_yangdidww";
		sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				sql = "insert into slqc_yangdidww values(";
				for(int j=0;j<sor.getColumnCount();j++)
				{
					if(j == 0) sql += "'" + sor.getString(j) + "'";
					else sql += "', " + sor.getString(j) + "'";
				}
				sql += ")";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		sql = "select * from slqc_ydbh";
		sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				sql = "insert into slqc_ydbh values(";
				for(int j=0;j<sor.getColumnCount();j++)
				{
					if(j == 0) sql += "'" + sor.getString(j) + "'";
					else sql += "', " + sor.getString(j) + "'";
				}
				sql += ")";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		sql = "select * from slqc_yindiandww";
		sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				sql = "insert into slqc_yindiandww values(";
				for(int j=0;j<sor.getColumnCount();j++)
				{
					if(j == 0) sql += "'" + sor.getString(j) + "'";
					else sql += "', " + sor.getString(j) + "'";
				}
				sql += ")";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		sql = "select * from slqc_yxcl";
		sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				sql = "insert into slqc_yxcl values(";
				for(int j=0;j<sor.getColumnCount();j++)
				{
					if(j == 0) sql += "'" + sor.getString(j) + "'";
					else sql += "', " + sor.getString(j) + "'";
				}
				sql += ")";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		sql = "select * from slqc_zbdc";
		sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				sql = "insert into slqc_zbdc values(";
				for(int j=0;j<sor.getColumnCount();j++)
				{
					if(j == 0) sql += "'" + sor.getString(j) + "'";
					else sql += "', " + sor.getString(j) + "'";
				}
				sql += ")";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		sql = "select * from slqc_zjcl";
		sor = dbSrc.rawQuery(sql, null);
		if(sor != null)
		{
			sor.moveToFirst();
			for(int i=0;i<sor.getCount();i++)
			{
				sql = "insert into slqc_zjcl values(";
				for(int j=0;j<sor.getColumnCount();j++)
				{
					if(j == 0) sql += "'" + sor.getString(j) + "'";
					else sql += "', " + sor.getString(j) + "'";
				}
				sql += ")";
				dbDst.execSQL(sql);
				sor.moveToNext();
			}
			sor.close();
		}
		
		dbSrc.close();
		dbDst.close();
		
		return true;
	}
	
	private void delData(int code)
	{
		SQLiteDatabase db = null;
		try{
			db = SQLiteDatabase.openOrCreateDatabase(MyConfig.GetAppdir() + "/qianqi.dat", null);
		}catch(Exception e){}
		if(db == null) return;
		
		if(code > 10)
		{
			String sql = "delete from shi where code = '" + code + "'";
			db.execSQL(sql);
			sql = "delete from xian where parent = '" + code + "'";
			db.execSQL(sql);
		}
		else
		{
			String sql = "delete from xian where code = '" + code + "'";
			db.execSQL(sql);
			sql = "select * from xian where parent = '149000'";
			Cursor sor = db.rawQuery(sql, null);
			if(sor == null || sor.getCount() == 0)
			{
				sql = "delete from shi where code = '149000'";
				db.execSQL(sql);
			}
		}
		
		String sql_1 = "delete from slqc_ydyz where xian not in(select code from xian) and (f2 is null or f2 not in('1','2','3','4','5','6','7','8','9'))";
		String sql_2 = "delete from slqc_ydyz where f2 is not null and f2 in('1','2','3','4','5','6','7','8','9') and f2 not in(select code from xian where code < 10)";
		String sql_3 = "delete from slqc_kjl where ydh not in(select ydh from slqc_ydyz)";
		String sql_4 = "delete from slqc_mmjc where ydh not in(select ydh from slqc_ydyz)";
		String sql_5 = "delete from slqc_kpfm where ydh not in(select ydh from slqc_ydyz)";
		String sql_6 = "delete from slqc_qt where ydh not in(select ydh from slqc_ydyz)";
		String sql_7 = "delete from slqc_sgcl where ydh not in(select ydh from slqc_ydyz)";
		String sql_8 = "delete from slqc_slzh where ydh not in(select ydh from slqc_ydyz)";
		String sql_9 = "delete from slqc_trgx where ydh not in(select ydh from slqc_ydyz)";
		String sql_10 = "delete from slqc_wclzld where ydh not in(select ydh from slqc_ydyz)";
		String sql_11 = "delete from slqc_xmdc where ydh not in(select ydh from slqc_ydyz)";
		String sql_12 = "delete from slqc_yangdidww where ydh not in(select ydh from slqc_ydyz)";
		String sql_13 = "delete from slqc_ydbh where ydh not in(select ydh from slqc_ydyz)";
		String sql_14 = "delete from slqc_yindiandww where ydh not in(select ydh from slqc_ydyz)";
		String sql_15 = "delete from slqc_yxcl where ydh not in(select ydh from slqc_ydyz)";
		String sql_16 = "delete from slqc_zbdc where ydh not in(select ydh from slqc_ydyz)";
		String sql_17 = "delete from slqc_zjcl where ydh not in(select ydh from slqc_ydyz)";
		db.execSQL(sql_1);
		db.execSQL(sql_2);
		db.execSQL(sql_3);
		db.execSQL(sql_4);
		db.execSQL(sql_5);
		db.execSQL(sql_6);
		db.execSQL(sql_7);
		db.execSQL(sql_8);
		db.execSQL(sql_9);
		db.execSQL(sql_10);
		db.execSQL(sql_11);
		db.execSQL(sql_12);
		db.execSQL(sql_13);
		db.execSQL(sql_14);
		db.execSQL(sql_15);
		db.execSQL(sql_16);
		db.execSQL(sql_17);
		
		db.close();
	}
	
	private void resetTask()
	{
		SQLiteDatabase db = null;
		try{
			db = SQLiteDatabase.openOrCreateDatabase(MyConfig.GetAppdir() + "/qianqi.dat", null);
		}catch(Exception e){}
		if(db == null) return;
		
		
		
		db.close();
	}
	*/
}
