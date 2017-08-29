package com.tdgeos.dlg.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tdgeos.lib.MyFile;
import com.tdgeos.lib.MyFolder;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyFileOpenDialog extends Dialog implements OnClickListener {
    private Button btnUp;
    private Button btnCancel;
    private TextView tvPath;
    private GridView gvList;
    private String path;
    private List<String> lstFolders;
    private List<String> lstFiles;
    private String result;
    private Handler mHandler;
    private Context ctt;
    private String[] saShowTypes;

    public MyFileOpenDialog(Context context, String dir, String[] types) {
        super(context);
        ctt = context;
        path = dir;
        saShowTypes = types;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_base_fileopen);
        setTitle("打开");
        this.getWindow().getAttributes().width = MyConfig.GetBigWidth();
        this.getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        btnUp = (Button) findViewById(R.id.btn_up);
        btnUp.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        tvPath = (TextView) findViewById(R.id.tv_path);
        tvPath.setText("当前位置:" + path);
        gvList = (GridView) findViewById(R.id.gv_list);
        gvList.setBackgroundColor(Color.LTGRAY);
        gvList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 < lstFolders.size()) {
                    String tmp = path;
                    if (tmp.equals("/")) tmp = tmp + lstFolders.get(arg2);
                    else tmp = tmp + "/" + lstFolders.get(arg2);
                    MyFolder f = MyFile.getCurFile(tmp, saShowTypes);
                    if (f == null) return;
                    path = tmp;
                    tvPath.setText("当前位置：" + path);
                    resetList(path);
                } else {
                    String file = path + "/" + lstFiles.get(arg2 - lstFolders.size());
                    result = file;
                    MyFileOpenDialog.this.cancel();
                }
            }
        });

        resetList(path);
    }

    private void resetList(String dir) {
        MyFolder f = MyFile.getCurFile(dir, saShowTypes);
        if (f == null) return;
        lstFolders = f.folders;
        lstFiles = f.files;
        List<HashMap<String, Object>> lstList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < lstFolders.size(); i++) {
            HashMap<String, Object> hmap = new HashMap<String, Object>();
            hmap.put("image", R.drawable.folder);
            hmap.put("name", lstFolders.get(i));
            lstList.add(hmap);
        }
        for (int i = 0; i < lstFiles.size(); i++) {
            HashMap<String, Object> hmap = new HashMap<String, Object>();
            hmap.put("image", R.drawable.file);
            hmap.put("name", lstFiles.get(i));
            lstList.add(hmap);
        }
        SimpleAdapter adapter = new SimpleAdapter(ctt, lstList, R.layout.dlg_base_file_item, new String[]{"image", "name"},
                new int[]{R.id.iv_icon, R.id.tv_name});
        gvList.setAdapter(adapter);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_up: {
                String str = MyFile.GetParentPath(path);
                if (str == null) {
                    Toast.makeText(ctt, "已经到根目录.", 1).show();
                    break;
                }
                MyFolder f = MyFile.getCurFile(str, saShowTypes);
                if (f == null) break;
                path = str;
                tvPath.setText("当前位置：" + path);
                resetList(path);
            }
            break;
            case R.id.btn_cancel: {
                this.cancel();
            }
            break;
        }
    }
}
