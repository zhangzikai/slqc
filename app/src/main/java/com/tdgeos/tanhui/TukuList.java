package com.tdgeos.tanhui;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.yangdi.Resmgr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class TukuList extends Activity implements View.OnClickListener {
    private Spinner spField = null;
    private EditText etInfo = null;
    private Button btnSearch = null;
    private Button btnClose = null;

    private ListView lvList = null;
    private List<String> lstMcs = new ArrayList<String>();

    private String strSql = "select * from tuku";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tuku_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        spField = (Spinner) findViewById(R.id.sp_field);
        List<String> lstField = new ArrayList<String>();
        lstField.add("名称");
        lstField.add("科");
        lstField.add("属");
        lstField.add("识别要点");
        lstField.add("分布");
        ArrayAdapter<String> apField = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lstField);
        apField.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spField.setAdapter(apField);
        spField.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        etInfo = (EditText) findViewById(R.id.et_info);

        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);
        btnClose = (Button) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        lvList = (ListView) findViewById(R.id.lv_list);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String mc = lstMcs.get(arg2);
                int dm = Resmgr.GetSzCode(mc);
                String en = Resmgr.GetTukuEname(mc);
                Intent intent = new Intent();
                intent.putExtra("szmc", mc);
                intent.putExtra("szdm", dm);
                intent.putExtra("enmc", en);
                intent.setClass(TukuList.this, TukuPic.class);
                startActivity(intent);
            }
        });

        initData();

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
            return super.onKeyDown(keyCode, event);
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
            case R.id.btn_search: {
                int pos = spField.getSelectedItemPosition();
                String str = etInfo.getText().toString().trim();
                if (str.equals("")) {
                    strSql = "select * from tuku";
                } else {
                    if (pos == 0) {
                        strSql = "select * from tuku where mc_cn like '%" + str + "%'";
                    }
                    if (pos == 1) {
                        strSql = "select * from tuku where ke like '%" + str + "%'";
                    }
                    if (pos == 2) {
                        strSql = "select * from tuku where shu like '%" + str + "%'";
                    }
                    if (pos == 3) {
                        strSql = "select * from tuku where shibie like '%" + str + "%'";
                    }
                    if (pos == 4) {
                        strSql = "select * from tuku where fenbu like '%" + str + "%'";
                    }
                }
                initData();
                break;
            }
            case R.id.btn_close: {
                this.finish();
                break;
            }
        }
    }

    private void initData() {
        lstMcs.clear();
        List<java.util.Map<String, String>> list = new ArrayList<java.util.Map<String, String>>();
        String[][] sss = Resmgr.SelectData(strSql);
        if (sss != null && sss.length > 0) {
            for (int i = 0; i < sss.length; i++) {
                java.util.Map<String, String> item = new java.util.HashMap<String, String>();
                item.put("mc", sss[i][0]);
                item.put("ke", sss[i][3]);
                item.put("shu", sss[i][4]);
                item.put("sbyd", sss[i][5]);
                item.put("fb", sss[i][6]);
                list.add(item);
                lstMcs.add(sss[i][0]);
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.tuku_list_item,
                new String[]{"mc", "ke", "shu", "sbyd", "fb"},
                new int[]{R.id.tv_mc, R.id.tv_ke, R.id.tv_shu, R.id.tv_sbyd, R.id.tv_fb});
        lvList.setAdapter(adapter);
    }

}
