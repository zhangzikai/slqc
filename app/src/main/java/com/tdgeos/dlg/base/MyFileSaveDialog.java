package com.tdgeos.dlg.base;

import java.io.File;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyFileSaveDialog extends Dialog implements OnClickListener {
    private EditText etName;
    private Button btnUp;
    private Button btnNewFolder;
    private Button btnSave;
    private Button btnCancel;
    private TextView tvPath;
    private GridView gvList;
    private String path;
    private String name;
    private List<String> lstFolders;
    private List<String> lstFiles;
    private String result;
    private Handler mHandler;
    private Context ctt;

    public MyFileSaveDialog(Context context, String dir, String oldname) {
        super(context);
        ctt = context;
        path = dir;
        name = oldname;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_base_filesave);
        setTitle("保存");
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

        etName = (EditText) findViewById(R.id.et_name);
        etName.setText(name);
        btnUp = (Button) findViewById(R.id.btn_up);
        btnUp.setOnClickListener(this);
        btnNewFolder = (Button) findViewById(R.id.btn_newfolder);
        btnNewFolder.setOnClickListener(this);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
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
                    MyFolder f = MyFile.getCurFile(tmp, null);
                    if (f == null) return;
                    path = tmp;
                    tvPath.setText("当前位置：" + path);
                    resetList(path);
                }
            }
        });

        resetList(path);
    }

    private void resetList(String dir) {
        MyFolder f = MyFile.getCurFile(dir, null);
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
                MyFolder f = MyFile.getCurFile(str, null);
                if (f == null) break;
                path = str;
                tvPath.setText("当前位置：" + path);
                resetList(path);
            }
            break;
            case R.id.btn_newfolder: {
                MyNewFolderDialog myNewFolderDialog = new MyNewFolderDialog(ctt);
                String newfoldername = myNewFolderDialog.showDialog();
                if (newfoldername != null) {
                    String newfolder = null;
                    if (path.equals("/")) newfolder = path + newfoldername;
                    else newfolder = path + "/" + newfoldername;
                    File file = new File(newfolder);
                    if (file.exists() && file.isDirectory()) {
                        Toast.makeText(ctt, "文件夹已存在.", 1).show();
                        break;
                    } else {
                        file.mkdir();
                        resetList(path);
                    }
                }
            }
            break;
            case R.id.btn_save: {
                String savename = etName.getText().toString();
                if (savename.equals("")) {
                    Toast.makeText(ctt, "请输入文件名.", 1).show();
                    etName.setFocusable(true);
                    break;
                }
                if (path.equals("/")) result = path + savename;
                else result = path + "/" + savename;
                File file = new File(result);
                if (file.exists() && file.isFile()) {
                    if (new MyMakeSureDialog(ctt, "提示", "文件已存在，是否覆盖原文件？", "覆盖", "取消").showDialog()) {
                        file.delete();
                    } else {
                        etName.setText("");
                        etName.setFocusable(true);
                        break;
                    }
                }
                this.cancel();
            }
            break;
            case R.id.btn_cancel: {
                this.cancel();
            }
            break;
        }
    }

}

