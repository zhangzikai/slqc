package com.tdgeos.yangdi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;

import com.tdgeos.lib.MyCoord;
import com.tdgeos.lib.MyFile;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.lib.MyPoint;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.MyMap;
import com.tdgeos.yangdi.Resmgr;

public class YangdiMgr {
    public static final String WORK_DIR_NAME = "slqc_tanhui";
    public static final String APP_DIR_NAME = ".com.tdgeos.tanhui";

    public static final String DATA_EXNAME = ".dat";
    public static final String EXPORT_EXNAME = ".slqc";

    public static final int SHENG_CODE = 310000;
    public static final float YD_SIZE = 25.82f;

    public static final int MZ_CODE = 6700;//杂竹

    public static final int MY_MAP_TYPE = 84;//84, 54, 80
    public static final int YMT_WG_TYPE = 1;//样木图网格类型，0表示圆网格, 1表示方网格
    public static final int YM_ZB_TYPE = 0;//样木坐标类型，0表示方位角坐标, 1表示直角坐标
    public static final int YM_CKD_DEFAULT = 1;//样木默认参考点，0表示中心点, 1表示西南角, 2表示西北角...
    public static final int YM_CKD_TYPE = 1;//样木参考点类型，0表示固定, 1表示可选, 2表示可加

    public static final int MIN_FWJ = 0;
    public static final int MAX_FWJ = 360;
    public static final int MIN_QXJ = -90;
    public static final int MAX_QXJ = 90;
    public static final int MIN_LC = -10;
    public static final int MAX_LC = 10;
    public static final int MIN_ZJCL_SPJ = 0;
    public static final int MAX_ZJCL_SPJ = 30;
    public static final int MIN_ZJCL_BHC = 0;
    public static final int MAX_ZJCL_BHC = 49;
    public static final int MIN_ZJCL_ZCWC = 0;
    public static final int MAX_ZJCL_ZCWC = 98;

    public static final int MIN_HB = 0;
    public static final int MAX_HB = 4000;
    public static final int MIN_SQGD = 0;
    public static final int MAX_SQGD = 30;
    public static final int MIN_FSHD = 0;
    public static final int MAX_FSHD = 50;
    public static final int MIN_BFB = 0;
    public static final int MAX_BFB = 100;
    public static final int MIN_TRHD = 0;
    public static final int MAX_TRHD = 200;
    public static final int MIN_FZCHD = 0;
    public static final int MAX_FZCHD = 50;
    public static final int MIN_KZLYHD = 0;
    public static final int MAX_KZLYHD = 50;
    public static final int MIN_GMGD = 0;
    public static final int MAX_GMGD = 10;
    public static final int MIN_CBGD = 0;
    public static final int MAX_CBGD = 2;
    public static final int MIN_PJNL = 0;
    public static final int MAX_PJNL = 200;
    public static final int MIN_PJXJ = 5;
    public static final int MAX_PJXJ = 50;
    public static final int MIN_SG = 2;
    public static final int MAX_SG = 30;
    public static final int MIN_YBD = 0;
    public static final int MAX_YBD = 1;

    public static final int MIN_KJL_MJBL = 0;
    public static final float MAX_KJL_MJBL = 0.5f;

    public static final int MIN_YM_XJ = 5;
    public static final int MAX_YM_XJ = 200;
    public static final int MIN_YM_ZB = -3;
    public static final int MAX_YM_ZB = 29;
    public static final int MIN_YM_SPJ = 0;
    public static final int MAX_YM_SPJ = 40;

    public static final int MIN_DBWGD = 0;
    public static final int MAX_DBWGD = 20;
    public static final int MIN_PJDJ = 0;
    public static final int MAX_PJDJ = 50;

    public static final int MIN_XM_SG = 2;
    public static final int MAX_XM_SG = 10;
    public static final int MIN_XM_XJ = 0;
    public static final int MAX_XM_XJ = 5;

    public static final int MIN_ZLND = 2000;
    public static final int MAX_ZLND = 2015;
    public static final int MIN_ML = 0;
    public static final int MAX_ML = 15;
    public static final int MIN_CZMD = 0;
    public static final int MAX_CZMD = 12000;

    public static String dataTemplate = MyConfig.GetAppdir() + "/yangdi" + DATA_EXNAME;
    public static String ydDir = MyConfig.GetWorkdir() + "/yangdi";

    public static void InitYangdi(int ydh) {
        try {
            String dst = getDbFile(ydh);
            if (!MyFile.Exists(dst)) {
                MyFile.CopyFile(dataTemplate, dst);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String sql = "select ydh from dczt where ydh = '" + ydh + "'";
        if (!QueryExists(ydh, sql)) {
            sql = "insert into dczt(ydh) values('" + ydh + "')";
            ExecSQL(ydh, sql);
        }
        sql = "select ydh from info where ydh = '" + ydh + "'";
        if (!QueryExists(ydh, sql)) {
            sql = "insert into info(ydh) values('" + ydh + "')";
            ExecSQL(ydh, sql);
        }
        sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (!QueryExists(ydh, sql)) {
            sql = "insert into qt(ydh) values('" + ydh + "')";
            ExecSQL(ydh, sql);
        }
        sql = "select ydh from tag where ydh = '" + ydh + "'";
        if (!QueryExists(ydh, sql)) {
            sql = "insert into tag(ydh) values('" + ydh + "')";
            ExecSQL(ydh, sql);
        }
    }

    public static void InitQqData(int ydh) {
        String sql = "select ydh from ydyz_old";
        String[][] s = SelectData(ydh, sql);
        if (s != null) return;

        //样地因子中灌木高度、草本高度、平均胸径、平均树高、郁闭度等在显示是转换成小数。
        String[] ssYdyz = Qianqimgr.GetQqYdyz(ydh);
        if (ssYdyz != null) {
            try {
                int dl = Integer.parseInt(ssYdyz[29]);
                dl = DlCodeOldToNew(dl);
                ssYdyz[29] = String.valueOf(dl);
            } catch (Exception e) {
            }
            try {
                int yssz = Integer.parseInt(ssYdyz[39]);
                yssz = Resmgr.GetSzNewCodeByOldCode(yssz);
                ssYdyz[39] = String.valueOf(yssz);
            } catch (Exception e) {
            }
            sql = "insert into ydyz_old values('" + ssYdyz[0] + "'";
            for (int i = 1; i < ssYdyz.length; i++) {
                sql += ", '" + ssYdyz[i] + "'";
            }
            sql += ")";
            ExecSQL(ydh, sql);
        }

        List<Kjl> lstKjls = Qianqimgr.GetQqKjlList(ydh);
        for (int i = 0; i < lstKjls.size(); i++) {
            sql = "insert into kjl_old(ydh, xh, mjbl, dl, tdqs, lmqs, linzh, qy, yssz, lingz, ybd, pjsg, slqljg, szjg, spljydj) values("
                    + "'" + ydh + "', "
                    + "'" + lstKjls.get(i).xh + "', "
                    + "'" + lstKjls.get(i).mjbl + "', "
                    + "'" + lstKjls.get(i).dl + "', "
                    + "'" + lstKjls.get(i).tdqs + "', "
                    + "'" + lstKjls.get(i).lmqs + "', "
                    + "'" + lstKjls.get(i).linzh + "', "
                    + "'" + lstKjls.get(i).qy + "', "
                    + "'" + lstKjls.get(i).yssz + "', "
                    + "'" + lstKjls.get(i).lingz + "', "
                    + "'" + lstKjls.get(i).ybd + "', "
                    + "'" + lstKjls.get(i).pjsg + "', "
                    + "'" + lstKjls.get(i).slqljg + "', "
                    + "'" + lstKjls.get(i).szjg + "', "
                    + "'" + lstKjls.get(i).spljydj + "')";
            ExecSQL(ydh, sql);
        }

        String[][] sss = Qianqimgr.GetQqMmjc(ydh);
        if (sss != null) {
            for (int j = 0; j < sss.length; j++) {
                Yangmu ym = new Yangmu();
                try {
                    ym.ymh = Integer.parseInt(sss[j][1]);
                } catch (Exception e) {
                }
                try {
                    ym.lmlx = Integer.parseInt(sss[j][2]);
                } catch (Exception e) {
                }
                try {
                    ym.jclx = Integer.parseInt(sss[j][3]);
                } catch (Exception e) {
                }
                try {
                    int code = Integer.parseInt(sss[j][4]);
                    code = Resmgr.GetSzNewCodeByOldCode(code);
                    String name = Resmgr.GetSzName(code);
                    if (code > 0 && !name.equals("")) {
                        ym.szdm = code;
                        ym.szmc = name;
                    } else {
                        ym.szdm = -1;
                        ym.szmc = "";
                    }
                } catch (Exception e) {
                }
                try {
                    ym.bqxj = Float.parseFloat(sss[j][5]);
                    ym.bqxj /= 10;
                } catch (Exception e) {
                }
                try {
                    ym.cfgllx = Integer.parseInt(sss[j][6]);
                    if (ym.cfgllx == 1) ym.cfgllx = 11;
                    if (ym.cfgllx == 2) ym.cfgllx = 12;
                    if (ym.cfgllx == 3) ym.cfgllx = 12;
                    if (ym.cfgllx == 4) ym.cfgllx = 20;
                } catch (Exception e) {
                }
                try {
                    ym.lc = Integer.parseInt(sss[j][7]);
                } catch (Exception e) {
                }
                try {
                    ym.kjdlxh = Integer.parseInt(sss[j][8]);
                } catch (Exception e) {
                }
                try {
                    ym.fwj = Float.parseFloat(sss[j][9]);
                } catch (Exception e) {
                }
                try {
                    ym.spj = Float.parseFloat(sss[j][10]);
                } catch (Exception e) {
                }

                sql = "insert into mmjc_old values("
                        + "'" + ydh + "', "
                        + "'" + ym.ymh + "', "
                        + "'" + ym.lmlx + "', "
                        + "'" + ym.jclx + "', "
                        + "'" + ym.szdm + "', "
                        + "'" + ym.bqxj + "', "
                        + "'" + ym.cfgllx + "', "
                        + "'" + ym.lc + "', "
                        + "'" + ym.kjdlxh + "', "
                        + "'" + ym.fwj + "', "
                        + "'" + ym.spj + "')";
                ExecSQL(ydh, sql);

                if (ym.jclx != 13 && ym.jclx != 14 && ym.jclx != 15 && ym.jclx != 17) {
                    ym.jclx = 11;
                    ym.qqxj = ym.bqxj;
                    ym.bqxj = -1;
                    if (ym.fwj <= 90) {
                        ym.ckd = 1;
                    } else if (ym.fwj <= 180) {
                        ym.ckd = 2;
                    } else if (ym.fwj <= 270) {
                        ym.ckd = 3;
                    } else if (ym.fwj <= 360) {
                        ym.ckd = 4;
                    } else {
                        ym.ckd = 1;
                    }
                    //ym.ckd = YM_CKD_DEFAULT;
                    sql = "insert into mmjc values("
                            + "'" + ydh + "', "
                            + "'" + ym.ymh + "', "
                            + "'" + ym.lmlx + "', "
                            + "'" + ym.jclx + "', "
                            + "'" + ym.szmc + "', "
                            + "'" + ym.szdm + "', "
                            + "'" + ym.qqxj + "', "
                            + "'" + ym.bqxj + "', "
                            + "'" + ym.cfgllx + "', "
                            + "'" + ym.lc + "', "
                            + "'" + ym.kjdlxh + "', "
                            + "'" + ym.fwj + "', "
                            + "'" + ym.spj + "', "
                            + "'" + ym.bz + "', "
                            + "'" + ym.ckd + "', "
                            + "'" + ym.szlx + "', "
                            + "'" + ym.jczt + "')";
                    ExecSQL(ydh, sql);
                }
            }
        }
    }

    public static void BackupData(Context context, int ydh) {
        String app = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            app = info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        String lib = Resmgr.GetLibversion();
        String id = MyConfig.GetDeviceId();
        String sql = "update info set app_version = '" + app + "', lib_version = '" + lib + "', device_id = '" + id + "' where ydh = '" + ydh + "'";
        ExecSQL(ydh, sql);

        String src = getDbFile(ydh);
        String dst = MyConfig.GetBackupdir() + "/" + ydh + ".slqc";
        try {
            MyFile.DeleteFile(dst);
            MyFile.CopyFile(src, dst);
            try {
                encryptFile(dst);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //检查指定表，type：0表示错误，1表示警告
    private static List<String> getCheckInfo(int ydh, String tablename, int type) {
        List<String> lst = new ArrayList<String>();

        String sqlCount = "select ydh from " + tablename;
        String[][] sssCount = SelectData(ydh, sqlCount);
        if (sssCount == null) {
            return null;//此处不能直接返回lst，返回null表示该项未填，返回元素个数为0的lst表示检查通过。
        } else {
            String sqlRes = "select checkfields, checktables, protasis, groupby, msg from datacheck where checkitem = '" + tablename + "' and msgtype = '" + type + "'";
            String[][] sss = Resmgr.SelectData(sqlRes);
            if (sss == null) {
                return lst;//没有对应的检查条件，直接通过。
            }

            String sql = null;
            String[][] sssCheck = null;
            for (int i = 0; i < sss.length; i++) {
                sql = "select " + sss[i][0] + " from " + sss[i][1];
                if (!sss[i][2].equals("")) sql += " where " + sss[i][2];
                if (!sss[i][3].equals("")) sql += " group by " + sss[i][3];
                sssCheck = SelectData(ydh, sql);
                if (sssCheck != null) {
                    lst.add(sss[i][4]);
                }
            }
        }

        return lst;
    }

    public static void CheckYdyz(int ydh, List<String> lstError, List<String> lstWarn) {
        List<String> tmp = getCheckInfo(ydh, "ydyz", 1);
        if (tmp == null) {
            lstError.add("样地因子调查记录表尚未填写！");
            return;
        }
        for (int i = 0; i < tmp.size(); i++) {
            lstWarn.add(tmp.get(i));
        }
        tmp.clear();
        tmp = getCheckInfo(ydh, "ydyz", 0);
        for (int i = 0; i < tmp.size(); i++) {
            lstError.add(tmp.get(i));
        }
    }

    public static void CheckKjl(int ydh, List<String> lstError, List<String> lstWarn) {
        if (YangdiMgr.IsHasKjlxh(ydh, 1) && !YangdiMgr.IsHasKjlym(ydh, 1)) {
            lstError.add("序号为1的跨角林没有对应的检尺样木！");
        }
        if (YangdiMgr.IsHasKjlxh(ydh, 2) && !YangdiMgr.IsHasKjlym(ydh, 2)) {
            lstError.add("序号为2的跨角林没有对应的检尺样木！");
        }
        if (YangdiMgr.IsHasKjlxh(ydh, 3) && !YangdiMgr.IsHasKjlym(ydh, 3)) {
            lstError.add("序号为3的跨角林没有对应的检尺样木！");
        }

        List<String> tmp = getCheckInfo(ydh, "kjl", 0);
        if (tmp != null) {
            for (int i = 0; i < tmp.size(); i++) {
                lstError.add(tmp.get(i));
            }
            tmp.clear();
        }
        tmp = getCheckInfo(ydh, "kjl", 1);
        if (tmp != null) {
            for (int i = 0; i < tmp.size(); i++) {
                lstWarn.add(tmp.get(i));
            }
        }
    }

    public static void CheckMmjc(int ydh, List<String> lstError, List<String> lstWarn) {
        String sql = "select count(ymh) from mmjc where ydh = '" + ydh + "' and (bqxj < '5' or jczt = '0')";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        int r = 0;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                r = cursor.getInt(0);
                if (r > 0) {
                    lstError.add("还有 " + r + " 颗样木未检尺！");
                }
            }
            cursor.close();
        }
        db.close();

        if (YangdiMgr.IsHasKjlym(ydh, 1) && !YangdiMgr.IsHasKjlxh(ydh, 1)) {
            lstError.add("跨角地类序号为1的样木没有对应的跨角林记录！");
        }
        if (YangdiMgr.IsHasKjlym(ydh, 2) && !YangdiMgr.IsHasKjlxh(ydh, 2)) {
            lstError.add("跨角地类序号为2的样木没有对应的跨角林记录！");
        }
        if (YangdiMgr.IsHasKjlym(ydh, 3) && !YangdiMgr.IsHasKjlxh(ydh, 3)) {
            lstError.add("跨角地类序号为3的样木没有对应的跨角林记录！");
        }

        List<String> tmp = getCheckInfo(ydh, "mmjc", 0);
        if (tmp != null) {
            for (int i = 0; i < tmp.size(); i++) {
                lstError.add(tmp.get(i));
            }
            tmp.clear();
        }
        tmp = getCheckInfo(ydh, "mmjc", 1);
        if (tmp != null) {
            for (int i = 0; i < tmp.size(); i++) {
                lstWarn.add(tmp.get(i));
            }
        }
    }

    public static void CheckSgcl(int ydh, List<String> lstError, List<String> lstWarn) {
        List<Integer> lst = new ArrayList<Integer>();
        String sql = "select szdm from mmjc where ydh = '" + ydh + "' "
                //+ "and kjdlxh not in(1,2,3) "
                + "and (jclx not in(13,14,15,17,24)) "
                + "and bqxj >= '5' "
                + "group by szdm";
        String dbFile = YangdiMgr.getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
            } else {
                cursor.moveToFirst();
                for (int i = 0; i < n; i++) {
                    int v = cursor.getInt(0);
                    lst.add(v);
                    cursor.moveToNext();
                }
                cursor.close();
                db.close();
            }
        } else {
            db.close();
        }

        List<Sgcl> sgcls = YangdiMgr.GetSgclList(ydh);
        int count = sgcls.size();

        for (int i = 0; i < lst.size(); i++) {
            int n = 0;
            int sz = lst.get(i);
            int m = YangdiMgr.GetZhushu2(ydh, sz);
            String mc = Resmgr.GetSzName(sz);
            for (int j = 0; j < count; j++) {
                if (sgcls.get(j).sz == sz) {
                    n++;
                }
            }
            if (n == 0) {
                lstError.add("树种‘" + mc + "’没有树高测量记录。");
            } else if (n < 3 && m >= 3) {
                lstWarn.add("树种‘" + mc + "’平均木过少。");
            }
        }

        for (int i = 0; i < count; i++) {
            if (sgcls.get(i).sg <= 0 && sgcls.get(i).zxg <= 0) {
                lstError.add("样木" + sgcls.get(i).ymh + "的树高（枝下高）尚未调查。");
            }
        }



		/*
        for(int i=0;i<count;i++)
		{
			if(!YangdiMgr.IsYssz(ydh, sgcls.get(i).ymh))
			{
				lstWarn.add("样木" + sgcls.get(i).ymh + "不是优势树种。");
			}
		}

		int zs = GetYsszYmCount(ydh);

		int dl = YangdiMgr.GetBqdl(ydh);
		if(IsNeedSgcl(dl) && zs > 3)
		{
			if(count == 0)
			{
				lstError.add("该项尚未录入！");
			}
			if(count > 3)
			{
				lstError.add("记录过多，应选3株平均木！");
			}
			if(count > 0 && count < 3)
			{
				lstError.add("记录过少，应选3株平均木！");
			}
		}
		*/
        sql = "select count(ymh) from sgcl where ydh = '" + ydh + "' group by ymh having count(ymh) > 1";
        String[][] sss = YangdiMgr.SelectData(ydh, sql);
        if (sss != null) {
            lstError.add("样木号重复！");
        }
    }

    public static void CheckSlzh(int ydh, List<String> lstError, List<String> lstWarn) {
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        String sql = "select slzhlx from ydyz where ydh = '" + ydh + "'";
        Cursor cursor = db.rawQuery(sql, null);
        int slzhlx = 0;
        if (cursor != null) {
            int n = cursor.getCount();
            if (n > 0) {
                cursor.moveToFirst();
                slzhlx = cursor.getInt(0);
            }
            cursor.close();
        }
        db.close();

        List<Slzh> slzhs = YangdiMgr.GetSlzhList(ydh);
        int count = slzhs.size();

        if (slzhlx > 0 && count == 0) {
            if (count == 0) {
                lstError.add("样地因子表中有森林灾害类型，但无森林灾害调查记录！");
            }
        }
        if (slzhlx <= 0) {
            if ((count == 1 && slzhs.get(0).zhlx != 0) || count > 1) {
                lstError.add("样地因子表中森林灾害类型与森林灾害调查记录表中灾害类型不一致！");
            }
        }
        if (slzhlx > 0 && count > 0) {
            boolean b = false;
            for (int i = 0; i < slzhs.size(); i++) {
                if (slzhlx == slzhs.get(i).zhlx) {
                    b = true;
                    break;
                }
            }
            if (!b) {
                lstError.add("样地因子表中森林灾害类型与森林灾害调查记录不一致！");
            }
        }

        boolean b = false;
        for (int i = 0; i < slzhs.size(); i++) {
            if (0 == slzhs.get(i).zhlx) {
                b = true;
                break;
            }
        }
        if (b && count > 1) {
            lstError.add("森林灾害调查记录表中灾害类型冲突！");
        }

        sql = "select zhlx from slzh where ydh = '" + ydh + "' group by zhlx";
        String[][] sss = YangdiMgr.SelectData(ydh, sql);
        if (sss != null && sss.length != count) {
            lstError.add("森林灾害调查记录表中灾害类型有重复！");
        }
    }

    public static void CheckTrgx(int ydh, List<String> lstError, List<String> lstWarn) {
        String dbFile = getDbFile(ydh);
        String sql = "select count(xh) from trgx where ydh = '" + ydh + "'";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
        if (cursor != null) {
            int n = cursor.getCount();
            if (n > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        sql = "select trgxdj from ydyz where ydh = '" + ydh + "'";
        cursor = db.rawQuery(sql, null);
        int trgxdj = 0;
        if (cursor != null) {
            int n = cursor.getCount();
            if (n > 0) {
                cursor.moveToFirst();
                trgxdj = cursor.getInt(0);
            }
            cursor.close();
        }
        db.close();

        int dl = YangdiMgr.GetBqdl(ydh);
        if (IsNeedTrgx(dl) && trgxdj > 0 && trgxdj < 3 && count == 0) {
            if (count == 0) {
                lstError.add("样地因子中天然更新等级为“良”或“中”，但无天然更新调查记录！");
            }
        }

        sql = "select count(sz) from trgx where ydh = '" + ydh + "' group by sz having count(sz) > 1";
        String[][] sss = YangdiMgr.SelectData(ydh, sql);
        if (sss != null) {
            lstError.add("树种重复！");
        }
    }

    public static void CheckYdbh(int ydh, List<String> lstError, List<String> lstWarn) {
        int qqdl = -1;
        int qqlinzh = -1;
        int qqqy = -1;
        int qqyssz = -1;
        int qqlingz = -1;
        int qqzblx = -1;
        int bqdl = -1;
        int bqlinzh = -1;
        int bqqy = -1;
        int bqyssz = -1;
        int bqlingz = -1;
        int bqzblx = -1;
        String[] qq = YangdiMgr.GetQqbhyz(ydh);
        String[] bq = YangdiMgr.GetBqbhyz(ydh);
        String[] bhyy = YangdiMgr.GetYdbhyy(ydh);
        if (qq == null) {
            lstError.add("无法获取前期因子！");
        }
        if (bq == null) {
            lstError.add("无法获取本期因子，请检查‘样地因子表’是否录入！");
        }
        if (bhyy == null) {
            lstError.add("无法获取因子变化原因，请检查是否录入！");
        }
        if (qq == null || bq == null || bhyy == null) {
            return;
        }
        try {
            qqdl = Integer.parseInt(qq[0]);
        } catch (Exception e) {
        }
        try {
            qqlinzh = Integer.parseInt(qq[1]);
        } catch (Exception e) {
        }
        try {
            qqqy = Integer.parseInt(qq[2]);
        } catch (Exception e) {
        }
        try {
            qqyssz = Integer.parseInt(qq[3]);
        } catch (Exception e) {
        }
        try {
            qqlingz = Integer.parseInt(qq[4]);
        } catch (Exception e) {
        }
        try {
            qqzblx = Integer.parseInt(qq[5]);
        } catch (Exception e) {
        }

        try {
            bqdl = Integer.parseInt(bq[0]);
        } catch (Exception e) {
        }
        try {
            bqlinzh = Integer.parseInt(bq[1]);
        } catch (Exception e) {
        }
        try {
            bqqy = Integer.parseInt(bq[2]);
        } catch (Exception e) {
        }
        try {
            bqyssz = Integer.parseInt(bq[3]);
        } catch (Exception e) {
        }
        try {
            bqlingz = Integer.parseInt(bq[4]);
        } catch (Exception e) {
        }
        try {
            bqzblx = Integer.parseInt(bq[5]);
        } catch (Exception e) {
        }

        if ((qqdl > 0 || bqdl > 0) && qqdl != bqdl && bhyy[0].equals("")) {
            lstError.add("前后期地类不一致，但无地类变化原因说明！");
        }
        if ((qqlinzh > 0 || bqlinzh > 0) && qqlinzh != bqlinzh && bhyy[1].equals("")) {
            lstError.add("前后期林种不一致，但无林种变化原因说明！");
        }
        if ((qqqy > 0 || bqqy > 0) && qqqy != bqqy && bhyy[2].equals("")) {
            lstError.add("前后期起源不一致，但无起源变化原因说明！");
        }
        if ((qqyssz > 0 || bqyssz > 0) && qqyssz != bqyssz && bhyy[3].equals("")) {
            lstError.add("前后期优势树种不一致，但无优势树种变化原因说明！");
        }
        if ((qqlingz > 0 || bqlingz > 0) && qqlingz != bqlingz && bhyy[4].equals("")) {
            lstError.add("前后期龄组不一致，但无地龄组化原因说明！");
        }
        if ((qqzblx > 0 || bqzblx > 0) && qqzblx != bqzblx && bhyy[5].equals("")) {
            lstError.add("前后期植被类型不一致，但无植被类型变化原因说明！");
        }
    }

    public static void CheckWclzld(int ydh, List<String> lstError, List<String> lstWarn) {
        int dl = YangdiMgr.GetBqdl(ydh);
        if (!YangdiMgr.IsNeedWclzld(dl)) {
            return;
        }

        String sql = "select ydh from wclzld where ydh = '" + ydh + "'";
        String[][] sss = SelectData(ydh, sql);
        if (sss == null) {
            lstError.add("地类为未成林造林地，但没有录入‘未成林造林地调查表’！");
        }
    }

    public static void CheckTrdc(int ydh, List<String> lstError, List<String> lstWarn) {
        int n = YangdiMgr.GetTrCount(ydh);
        if (n <= 0) lstError.add("尚未进行土壤调查！");
        else if (n < 3) lstError.add("土壤调查记录过少！");
        else if (n > 3) lstWarn.add("土壤调查记录过多！");
        int yf = YangdiMgr.GetYfwz(ydh);
        if (yf <= 0 || yf >= 5) {
            lstError.add("尚未指定土壤调查样方位置！");
        }
    }

    public static void CheckKlwdc(int ydh, List<String> lstError, List<String> lstWarn) {
        int n = YangdiMgr.GetKlwCount(ydh);
        if (n <= 0) lstError.add("尚未进行枯落物调查！");
        else if (n < 3) lstError.add("枯落物调查记录过少！");
        else if (n > 3) lstWarn.add("枯落物调查记录过多！");
    }

    public static void CheckOthers(int ydh, List<String> lstError, List<String> lstWarn) {
        if (YangdiMgr.GetYdloc(ydh) == null) {
            lstError.add("尚未测量样地坐标！");
        }
        String sql = "select yindian,yangdi,guding,tujing,cfsj,zdsj,jssj,fhsj,gps_dis,gps_begin,gps_end,ydphoto,ymphoto from qt where ydh = '" + ydh + "'";
        String[][] sss = SelectData(ydh, sql);
        if (sss != null) {
            if (sss[0][0] == null || sss[0][0].equals("")) {
                lstWarn.add("引点特征说明尚未录入！");
            }
            if (sss[0][1] == null || sss[0][1].equals("")) {
                lstWarn.add("样地特征说明尚未录入！");
            }
            if (sss[0][2] == null || sss[0][2].equals("")) {
                lstWarn.add("固定标志说明尚未录入！");
            }
            if (sss[0][3] == null || sss[0][3].equals("")) {
                lstWarn.add("寻找样地途径尚未录入！");
            }
            if (sss[0][4] == null || !YangdiMgr.IsValidTime(sss[0][4])) {
                lstWarn.add("驻地出发时间无效或尚未录入！");
            }
            if (sss[0][5] == null || !YangdiMgr.IsValidTime(sss[0][5])) {
                lstWarn.add("找到样点标桩时间无效或尚未录入！");
            }
            if (sss[0][6] == null || !YangdiMgr.IsValidTime(sss[0][6])) {
                lstWarn.add("调查结束时间无效或尚未录入！");
            }
            if (sss[0][7] == null || !YangdiMgr.IsValidTime(sss[0][7])) {
                lstWarn.add("返回驻地时间无效或尚未录入！");
            }
            if (sss[0][8] == null || sss[0][8].equals("")) {
                lstWarn.add("航迹采集距离尚未录入！");
            }
            if (sss[0][9] == null || !YangdiMgr.IsValidTime(sss[0][9])) {
                lstWarn.add("航迹采集开始时间无效或尚未录入！");
            }
            if (sss[0][10] == null || !YangdiMgr.IsValidTime(sss[0][10])) {
                lstWarn.add("航迹采集结束时间无效或尚未录入！");
            }
            if (sss[0][11] == null || sss[0][11].equals("")) {
                lstWarn.add("样地照片数量尚未录入！");
            }
            if (sss[0][12] == null || sss[0][12].equals("")) {
                lstWarn.add("样木照片数量尚未录入！");
            }
        }
        List<TrackInfo> lstTrkInfos = YangdiMgr.GetTrackInfo(ydh);
        if (lstTrkInfos.size() == 0) {
            lstError.add("尚未采集航迹数据！");
        } else {
            boolean b = false;
            boolean b2 = false;
            for (int i = 0; i < lstTrkInfos.size(); i++) {
                if (lstTrkInfos.get(i).count > 1 && lstTrkInfos.get(i).length > 1) {
                    b = true;
                }
                if (lstTrkInfos.get(i).length > 500) {
                    b2 = true;
                }
            }
            if (!b) {
                lstError.add("无有效的航迹数据！");
            } else if (!b2) {
                lstWarn.add("航迹采集距离偏短！");
            }
        }
        //照片数量至少3张
        if (GetZpCount(ydh) < 3) {
            lstError.add("照片数量过少，至少应有3张样地照片！");
        }
    }

    public static boolean Check(int ydh) {
        int[] ii = YangdiMgr.GetDczt(ydh);
        if (ii == null) return false;
        int dl = YangdiMgr.GetBqdl(ydh);
        boolean bYf = Qianqimgr.IsYangfang(ydh);

        MyCoord pt = GetYdloc(ydh);
        if (pt == null) return false;

        if (GetZpCount(ydh) < 3) {
            return false;
        }

        List<TrackInfo> lstTrkInfos = YangdiMgr.GetTrackInfo(ydh);
        if (lstTrkInfos.size() == 0) {
            return false;
        } else {
            boolean b = false;
            for (int i = 0; i < lstTrkInfos.size(); i++) {
                if (lstTrkInfos.get(i).count > 1 && lstTrkInfos.get(i).length > 1) {
                    b = true;
                }
            }
            if (!b) {
                return false;
            }
        }

        List<String> lstError = new ArrayList<String>();
        List<String> lstWarn = new ArrayList<String>();

        if (ii[0] != 2)//封面
        {
            return false;
        }
        if (ii[1] != 2)//引点特征
        {
            return false;
        }
        if (ii[2] != 2)//样地特征
        {
            return false;
        }
        if (ii[3] != 2)//引线测量
        {
            return false;
        }
        if (ii[4] != 2)//周界测量
        {
            return false;
        }
        if (ii[5] != 2)//样地因子
        {
            return false;
        }

        lstError.clear();
        lstWarn.clear();
        if (ii[6] != 2)//跨角林
        {
            return false;
        } else {
            YangdiMgr.CheckKjl(ydh, lstError, lstWarn);
            if (lstError.size() > 0) {
                return false;
            }
        }

        lstError.clear();
        lstWarn.clear();
        if (ii[7] != 2)//每木检尺
        {
            return false;
        } else {
            YangdiMgr.CheckMmjc(ydh, lstError, lstWarn);
            if (lstError.size() > 0) {
                return false;
            }
        }

        if (ii[8] != 2)//样木图
        {
            return false;
        }

        lstError.clear();
        lstWarn.clear();
        if (ii[9] != 2 && YangdiMgr.IsNeedSgcl(dl))//树高测量
        {
            return false;
        } else {
            YangdiMgr.CheckSgcl(ydh, lstError, lstWarn);
            if (lstError.size() > 0) {
                return false;
            }
        }

        lstError.clear();
        lstWarn.clear();
        if (ii[10] != 2 && YangdiMgr.IsNeedSlzh(dl))//森林灾害
        {
            return false;
        } else {
            YangdiMgr.CheckSlzh(ydh, lstError, lstWarn);
            if (lstError.size() > 0) {
                return false;
            }
        }

        if (ii[11] != 2 && bYf && YangdiMgr.IsNeedYangfang(dl))//植被调查
        {
            return false;
        }

        if (ii[12] != 2 && bYf && YangdiMgr.IsNeedYangfang(dl))//下木调查
        {
            return false;
        }

        lstError.clear();
        lstWarn.clear();
        if (ii[13] != 2 && YangdiMgr.IsNeedTrgx(dl))//天然更新
        {
            return false;
        } else {
            YangdiMgr.CheckTrgx(ydh, lstError, lstWarn);
            if (lstError.size() > 0) {
                return false;
            }
        }

        lstError.clear();
        lstWarn.clear();
        if (ii[14] != 2)//样地变化
        {
            return false;
        } else {
            YangdiMgr.CheckYdbh(ydh, lstError, lstWarn);
            if (lstError.size() > 0) {
                return false;
            }
        }

        lstError.clear();
        lstWarn.clear();
        if (ii[15] != 2 && YangdiMgr.IsNeedWclzld(dl))//未成林造林地
        {
            return false;
        } else {
            YangdiMgr.CheckWclzld(ydh, lstError, lstWarn);
            if (lstError.size() > 0) {
                return false;
            }
        }

        int tr = GetTrdcStatu(ydh);
        int klw = GetKlwdcStatu(ydh);
        if (tr != 2 || klw != 2) return false;

        return true;
    }

    public static boolean IsNeedSgcl(int dl) {
        if (dl == 111 || dl == 113 || dl == 120) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean IsNeedSlzh(int dl) {
        if (dl == 111 || dl == 113 || dl == 131 || dl == 1311 || dl == 1312 || dl == 1315) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean IsNeedTrgx(int dl) {
        if (dl == 120 || dl == 131 || dl == 1311 || dl == 1312 || dl == 1315 || dl == 132
                || dl == 161 || dl == 162 || dl == 163 || dl == 171
                || dl == 172 || dl == 1721 || dl == 1722 || dl == 173 || dl == 174) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean IsNeedWclzld(int dl) {
        if (dl == 141) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean IsNeedYangfang(int dl) {
        if (dl == 210 || dl == 220 || dl == 230 || dl == 240 || dl == 251 || dl == 252 || dl == 253 || dl == 254) {
            return true;
        } else {
            return false;
        }
    }

    //某地类是否需要填写优势树种
    public static boolean IsNeedYssz(int dl) {
        if ((dl > 0 && dl < 150) || (dl == 1311 || dl == 1312 || dl == 1315)) return true;
        return false;
    }

    public static boolean IsNeedSzjg(int dl) {
        if (dl == 111 || dl == 113) return true;
        return false;
    }


    public static void SetCfsj(int ydh) {
        String time = MyFuns.GetTimeByStringS();
        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (QueryExists(ydh, sql)) {
            sql = "update qt set cfsj = '" + time + "' where ydh = '" + ydh + "'";
        } else {
            sql = "insert into qt(ydh, cfsj) values('" + ydh + "', '" + time + "')";
        }
        ExecSQL(ydh, sql);
    }

    public static void SetZdsj(int ydh) {
        String time = MyFuns.GetTimeByStringS();
        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (QueryExists(ydh, sql)) {
            sql = "update qt set zdsj = '" + time + "' where ydh = '" + ydh + "'";
        } else {
            sql = "insert into qt(ydh, zdsj) values('" + ydh + "', '" + time + "')";
        }
        ExecSQL(ydh, sql);
    }

    public static void SetYdloc(int ydh, MyPoint pt) {
        /*
		String sql = "select ydh from info where ydh = '" + ydh + "'";
		if(QueryExists(ydh, sql))
		{
			sql = "update info set wgs84_x = '" + pt.x + "', wgs84_y = '" + pt.y + "', wgs84_z = '" + pt.z + "' where ydh = '" + ydh + "'";
		}
		else
		{
			sql = "insert into info(ydh, wgs84_x, wgs84_y, wgs84_z) values('" + ydh + "', '" + pt.x + "', '" + pt.y + "', '" + pt.z + "')";
		}
		*/
        String sql = "update info set wgs84_x = '" + pt.x + "', wgs84_y = '" + pt.y + "', wgs84_z = '" + pt.z + "' where ydh = '" + ydh + "'";
        ExecSQL(ydh, sql);
        SetZdsj(ydh);
    }

    public static MyCoord GetYdloc(int ydh) {
        MyCoord c = null;
        String dbFile = getDbFile(ydh);
        String sql = "select wgs84_x, wgs84_y, wgs84_z from info where ydh = '" + ydh + "'";
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                double lon = cursor.getDouble(0);
                double lat = cursor.getDouble(1);
                double alt = cursor.getDouble(2);
                if (lon > 0 && lat > 0) c = new MyCoord(lon, lat, alt);
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
        }
        return c;
    }

    public static void SetYdloc2(int ydh, MyPoint pt1, MyCoord pt2) {

        String sql = "update tag set f6 = '" + pt1.x + "', f7 = '" + pt1.y + "', f8 = '" + pt2.x + "', f9 = '" + pt2.y + "' where ydh = '" + ydh + "'";
        ExecSQL(ydh, sql);
        SetZdsj(ydh);
    }

    public static MyCoord GetYdloc2(int ydh) {
        MyCoord c = null;
        String dbFile = getDbFile(ydh);
        String sql = "select f6, f7 from tag where ydh = '" + ydh + "'";
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                double lon = cursor.getDouble(0);
                double lat = cursor.getDouble(1);
                if (lon > 0 && lat > 0) c = new MyCoord(lon, lat, 0);
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
        }
        return c;
    }

    public static double[] GetYdloc22(int ydh) {
        double[] dd = null;
        String dbFile = getDbFile(ydh);
        String sql = "select f6, f7, f8, f9 from tag where ydh = '" + ydh + "'";
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                double d1 = cursor.getDouble(0);
                double d2 = cursor.getDouble(1);
                double d3 = cursor.getDouble(2);
                double d4 = cursor.getDouble(3);
                if (d1 > 0 && d2 > 0) {
                    dd = new double[4];
                    dd[0] = d1;
                    dd[1] = d2;
                    dd[2] = d3;
                    dd[3] = d4;
                }
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
        }
        return dd;
    }

    public static float GetYdcpj(int ydh) {
        String sql = "select f10 from tag where ydh = '" + ydh + "'";
        return QueryFloat(ydh, sql);
    }

    public static void SetYdcpj(int ydh, float cpj) {
        String sql = "update tag set f10='" + cpj + "' where ydh = '" + ydh + "'";
        YangdiMgr.ExecSQL(ydh, sql);
    }


    public static boolean IsValidZpmc(int ydh, String name) {
        String sql = "select ydh from zp where ydh = '" + ydh + "' and name = '" + name + "'";
        return !QueryExists(ydh, sql);
    }

    public static void InsertZp(int ydh, int type, int ymh, String info, String pic) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_4444;
        opt.inJustDecodeBounds = false;
        opt.inSampleSize = 2;
        opt.inInputShareable = true;
        opt.inPurgeable = true;
        Bitmap bmp = BitmapFactory.decodeFile(pic, opt);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.JPEG, 40, baos);
        Object[] args = new Object[]{baos.toByteArray()};
        String name = MyFile.GetFileNameNoEx(pic);
        String sql = "insert into zp(ydh, type, ymh, name, notes, zp) values('" + ydh + "', '" + type + "', '" + ymh + "', '" + name + "', '" + info + "',  ?)";
        try {
            String dbFile = getDbFile(ydh);
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            db.execSQL(sql, args);
            db.close();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

    public static void ResetZpCount(int ydh) {
        int n1 = 0;
        int n2 = 0;
        String sql = "select count(ydh) from zp where ydh = '" + ydh + "' and type = '1'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        if (count == 0) {
            cursor.close();
        } else {
            cursor.moveToFirst();
            n1 = cursor.getInt(0);
            cursor.close();
        }
        sql = "select count(ydh) from zp where ydh = '" + ydh + "' and type = '2'";
        cursor = db.rawQuery(sql, null);
        count = cursor.getCount();
        if (count == 0) {
            cursor.close();
        } else {
            cursor.moveToFirst();
            n2 = cursor.getInt(0);
            cursor.close();
        }
        sql = "select ydh from qt where ydh = '" + ydh + "'";
        cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            count = cursor.getCount();
            if (count == 0) {
                cursor.close();
                sql = "insert into qt(ydh, ydphoto, ymphoto) values('" + ydh + "', '" + n1 + "', '" + n2 + "')";
                db.execSQL(sql);
            } else {
                cursor.close();
                sql = "update qt set ydphoto = '" + n1 + "', ymphoto = '" + n2 + "' where ydh = '" + ydh + "'";
                db.execSQL(sql);
            }
        } else {
            sql = "insert into qt(ydh, ydphoto, ymphoto) values('" + ydh + "', '" + n1 + "', '" + n2 + "')";
            db.execSQL(sql);
        }
        db.close();
    }

    public static int GetZpCount(int ydh) {
        String sql = "select count(ydh) from zp where ydh = '" + ydh + "'";
        int r = QueryInt(ydh, sql);
        if (r == -1) r = 0;
        return r;
    }

    public static Bitmap GetZp(int ydh, int zph) {
        String sql = "select zp from zp where ydh = '" + ydh + "' and zph = '" + zph + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        if (count == 0) {
            cursor.close();
            db.close();
            return null;
        }
        cursor.moveToFirst();
        byte[] bts = null;
        try {
            bts = cursor.getBlob(0);
        } catch (Exception e) {
        }
        if (bts == null) {
            cursor.close();
            db.close();
            return null;
        }
        Bitmap bmpImage = BitmapFactory.decodeByteArray(bts, 0, bts.length);
        cursor.close();
        db.close();
        return bmpImage;
    }

    public static void ClearZp(int ydh) {
        String sql = "delete from zp where ydh = '" + ydh + "'";
        ExecSQL(ydh, sql);
    }


    public static int[] GetDczt(int ydh) {
        int[] ii = new int[17];
        String sql = "select * from dczt where ydh = '" + ydh + "'";
        String[][] sss = SelectData(ydh, sql);
        if (sss == null) return ii;
        for (int i = 0; i < 17; i++) {
            try {
                ii[i] = Integer.parseInt(sss[0][i + 1]);
            } catch (Exception e) {
            }
        }
        return ii;
    }

    public static int GetTrdcStatu(int ydh) {
        String sql = "select f12 from tag where ydh = '" + ydh + "'";
        int r = QueryInt(ydh, sql);
        if (r == -1) r = 0;
        return r;
    }

    public static int GetKlwdcStatu(int ydh) {
        String sql = "select f11 from tag where ydh = '" + ydh + "'";
        int r = QueryInt(ydh, sql);
        if (r == -1) r = 0;
        return r;
    }


    //样木
    public static int GetMaxYmh(int ydh) {
        String sql = "select max(ymh) from mmjc where ydh = '" + ydh + "'";
        int r = QueryInt(ydh, sql);
        if (r == -1) r = 0;
        return r;
    }

    public static boolean IsHasYangmu(int ydh, int ymh) {
        String sql = "select * from mmjc where ydh = '" + ydh + "' and ymh = '" + ymh + "'";
        return QueryExists(ydh, sql);
    }

    public static boolean IsHasKjlym(int ydh, int xh) {
        String sql = "select * from mmjc where ydh = '" + ydh + "' and kjdlxh = '" + xh + "'";
        return QueryExists(ydh, sql);
    }

    //检尺样木数量
    public static int GetYangmuCount(int ydh) {
        String sql = "select count(ymh) from mmjc where ydh = '" + ydh + "'";
        int r = QueryInt(ydh, sql);
        if (r == -1) r = 0;
        return r;
    }

    public static int GetPassYangmuCount(int ydh) {
        String sql = "select count(ymh) from mmjc where ydh = '" + ydh + "' and jczt = '1'";
        int r = QueryInt(ydh, sql);
        if (r == -1) r = 0;
        return r;
    }

    public static String GetResidueYangmu(int ydh) {
        String sql = "select ymh from mmjc where ydh = '" + ydh + "' and jczt <> '1'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        String str = "";
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }

            cursor.moveToFirst();
            int a = n > 5 ? 5 : n;
            for (int i = 0; i < a; i++) {
                int ymh = cursor.getInt(0);
                if (i == 0) str = "尚有‘" + ymh + "’";
                else str += ", ‘" + ymh + "’";
                cursor.moveToNext();
            }
            str += "等" + n + "棵样木未检尺";
            cursor.close();
        }
        db.close();
        return str;
    }

    public static int GetYsszYmCount(int ydh) {
        List<Yangmu> lst = GetYangmus(ydh);
        int count = 0;
        for (int i = 0; i < lst.size(); i++) {
            if (IsYssz(ydh, lst.get(i).ymh)) {
                count++;
            }
        }
        return count;
    }

    public static void AddYangmu(int ydh, Yangmu ym) {
        if (IsHasYangmu(ydh, ym.ymh)) return;

        String sql = "insert into mmjc values("
                + "'" + ydh + "', "
                + "'" + ym.ymh + "', "
                + "'" + ym.lmlx + "', "
                + "'" + ym.jclx + "', "
                + "'" + ym.szmc + "', "
                + "'" + ym.szdm + "', "
                + "'" + ym.qqxj + "', "
                + "'" + ym.bqxj + "', "
                + "'" + ym.cfgllx + "', "
                + "'" + ym.lc + "', "
                + "'" + ym.kjdlxh + "', "
                + "'" + ym.fwj + "', "
                + "'" + ym.spj + "', "
                + "'" + ym.bz + "', "
                + "'" + ym.ckd + "', "
                + "'" + ym.szlx + "', "
                + "'" + ym.jczt + "'"
                + ")";
        ExecSQL(ydh, sql);
        try {
            sql = "insert into qxjxj(ydh, type, id, qxj, xj) values("
                    + "'" + ydh + "', "
                    + "'3', "
                    + "'" + ym.ymh + "', "
                    + "'" + ym.qxj + "', "
                    + "'" + ym.xj + "')";
            ExecSQL(ydh, sql);
        } catch (Exception e) {
        }
    }

    public static void UpdateYangmu(int ydh, Yangmu ym) {
        String sql = "update mmjc set "
                + "ymh = '" + ym.ymh + "', "
                + "lmlx = '" + ym.lmlx + "', "
                + "jclx = '" + ym.jclx + "', "
                + "szmc = '" + ym.szmc + "', "
                + "szdm = '" + ym.szdm + "', "
                + "qqxj = '" + ym.qqxj + "', "
                + "bqxj = '" + ym.bqxj + "', "
                + "cfgllx = '" + ym.cfgllx + "', "
                + "lc = '" + ym.lc + "', "
                + "kjdlxh = '" + ym.kjdlxh + "', "
                + "fwj = '" + ym.fwj + "', "
                + "spj = '" + ym.spj + "', "
                + "bz = '" + ym.bz + "', "
                + "ckd = '" + ym.ckd + "', "
                + "szlx = '" + ym.szlx + "', "
                + "jczt = '" + ym.jczt + "' "
                + " where ydh = '" + ydh + "' and ymh = '" + ym.ymh + "'";
        ExecSQL(ydh, sql);
        try {
            sql = "select id from qxjxj where ydh = '" + ydh + "' and id = '" + ym.ymh + "' and type = '3'";
            int xh = YangdiMgr.QueryInt(ydh, sql);
            if (xh < 0) {
                sql = "insert into qxjxj(ydh, type, id, qxj, xj) values("
                        + "'" + ydh + "', "
                        + "'3', "
                        + "'" + ym.ymh + "', "
                        + "'" + ym.qxj + "', "
                        + "'" + ym.xj + "')";
                ExecSQL(ydh, sql);
            } else {
                sql = "update qxjxj set "
                        + "qxj = '" + ym.qxj + "', "
                        + "xj = '" + ym.xj + "' "
                        + "where ydh = '" + ydh + "' and id = '" + ym.ymh + "' and type = '3'";
                ExecSQL(ydh, sql);
            }
        } catch (Exception e) {
        }
    }

    public static void DelYangmu(int ydh, int ymh) {
        Yangmu ym = YangdiMgr.GetQqYangmu(ydh, ymh);
        if (ym == null) {
            String sql = "delete from  mmjc where ydh = '" + ydh + "' and ymh = '" + ymh + "'";
            ExecSQL(ydh, sql);
        } else if (ym.jclx == 13 || ym.jclx == 14 || ym.jclx == 15 || ym.jclx == 17) {
            String sql = "delete from  mmjc where ydh = '" + ydh + "' and ymh = '" + ymh + "'";
            ExecSQL(ydh, sql);
        } else {
            ym.jclx = 11;
            ym.qqxj = ym.bqxj;
            ym.bqxj = -1;
            String sql = "update mmjc set "
                    + "ymh = '" + ym.ymh + "', "
                    + "lmlx = '" + ym.lmlx + "', "
                    + "jclx = '" + ym.jclx + "', "
                    + "szmc = '" + ym.szmc + "', "
                    + "szdm = '" + ym.szdm + "', "
                    + "qqxj = '" + ym.qqxj + "', "
                    + "bqxj = '" + ym.bqxj + "', "
                    + "cfgllx = '" + ym.cfgllx + "', "
                    + "lc = '" + ym.lc + "', "
                    + "kjdlxh = '" + ym.kjdlxh + "', "
                    + "fwj = '" + ym.fwj + "', "
                    + "spj = '" + ym.spj + "', "
                    + "bz = '" + ym.bz + "', "
                    + "ckd = '" + ym.ckd + "', "
                    + "szlx = '" + ym.szlx + "', "
                    + "jczt = '" + ym.jczt + "' "
                    + " where ydh = '" + ydh + "' and ymh = '" + ym.ymh + "'";
            ExecSQL(ydh, sql);
        }
        try {
            String sql = "delete from qxjxj where ydh = '" + ydh + "' and id = '" + ym.ymh + "' and type = '3'";
            ExecSQL(ydh, sql);
        } catch (Exception e) {
        }
    }

    public static Yangmu GetYangmu(int ydh, int ymh) {
        String sql = "select * from mmjc where ydh = '" + ydh + "' and ymh = '" + ymh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        Yangmu ym = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }

            cursor.moveToFirst();
            ym = new Yangmu();
            ym.ymh = ymh;
            ym.lmlx = cursor.getInt(2);
            ym.jclx = cursor.getInt(3);
            ym.szmc = cursor.getString(4);
            ym.szdm = cursor.getInt(5);
            ym.qqxj = cursor.getFloat(6);
            ym.bqxj = cursor.getFloat(7);
            ym.cfgllx = cursor.getInt(8);
            ym.lc = cursor.getInt(9);
            ym.kjdlxh = cursor.getInt(10);
            ym.fwj = cursor.getFloat(11);
            ym.spj = cursor.getFloat(12);
            ym.bz = cursor.getString(13);
            ym.ckd = cursor.getInt(14);
            ym.szlx = cursor.getInt(15);
            ym.jczt = cursor.getInt(16);
            try {
                sql = "select qxj,xj from qxjxj where ydh='" + ydh + "' and type='3' and id='" + ym.ymh + "'";
                String[][] sss = YangdiMgr.SelectData(ydh, sql);
                if (sss != null) {
                    ym.qxj = Float.parseFloat(sss[0][0]);
                    ym.xj = Float.parseFloat(sss[0][1]);
                }
            } catch (Exception e) {
            }
            cursor.close();
        }
        db.close();
        return ym;
    }

    public static Yangmu GetPrevYangmu(int ydh, int ymh) {
        String sql = "select * from mmjc where ydh = '" + ydh + "' and ymh < '" + ymh + "' order by ymh desc";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        Yangmu ym = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }

            cursor.moveToFirst();
            ym = new Yangmu();
            ym.ymh = cursor.getInt(1);
            ym.lmlx = cursor.getInt(2);
            ym.jclx = cursor.getInt(3);
            ym.szmc = cursor.getString(4);
            ym.szdm = cursor.getInt(5);
            ym.qqxj = cursor.getFloat(6);
            ym.bqxj = cursor.getFloat(7);
            ym.cfgllx = cursor.getInt(8);
            ym.lc = cursor.getInt(9);
            ym.kjdlxh = cursor.getInt(10);
            ym.fwj = cursor.getFloat(11);
            ym.spj = cursor.getFloat(12);
            ym.bz = cursor.getString(13);
            ym.ckd = cursor.getInt(14);
            ym.szlx = cursor.getInt(15);
            ym.jczt = cursor.getInt(16);
            try {
                sql = "select qxj,xj from qxjxj where ydh='" + ydh + "' and type='3' and id='" + ym.ymh + "'";
                String[][] sss = YangdiMgr.SelectData(ydh, sql);
                if (sss != null) {
                    ym.qxj = Float.parseFloat(sss[0][0]);
                    ym.xj = Float.parseFloat(sss[0][1]);
                }
            } catch (Exception e) {
            }
            cursor.close();
        }
        db.close();
        return ym;
    }

    public static Yangmu GetNextYangmu(int ydh, int ymh) {
        String sql = "select * from mmjc where ydh = '" + ydh + "' and ymh > '" + ymh + "' order by ymh";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        Yangmu ym = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }

            cursor.moveToFirst();
            ym = new Yangmu();
            ym.ymh = cursor.getInt(1);
            ym.lmlx = cursor.getInt(2);
            ym.jclx = cursor.getInt(3);
            ym.szmc = cursor.getString(4);
            ym.szdm = cursor.getInt(5);
            ym.qqxj = cursor.getFloat(6);
            ym.bqxj = cursor.getFloat(7);
            ym.cfgllx = cursor.getInt(8);
            ym.lc = cursor.getInt(9);
            ym.kjdlxh = cursor.getInt(10);
            ym.fwj = cursor.getFloat(11);
            ym.spj = cursor.getFloat(12);
            ym.bz = cursor.getString(13);
            ym.ckd = cursor.getInt(14);
            ym.szlx = cursor.getInt(15);
            ym.jczt = cursor.getInt(16);
            try {
                sql = "select qxj,xj from qxjxj where ydh='" + ydh + "' and type='3' and id='" + ym.ymh + "'";
                String[][] sss = YangdiMgr.SelectData(ydh, sql);
                if (sss != null) {
                    ym.qxj = Float.parseFloat(sss[0][0]);
                    ym.xj = Float.parseFloat(sss[0][1]);
                }
            } catch (Exception e) {
            }
            cursor.close();
        }
        db.close();
        return ym;
    }

    public static List<Yangmu> GetYangmus(int ydh) {
        List<Yangmu> lst = new ArrayList<Yangmu>();
        String sql = "select * from mmjc where ydh = '" + ydh + "' order by jczt,ymh";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }

            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                Yangmu ym = new Yangmu();
                ym.ymh = cursor.getInt(1);
                ym.lmlx = cursor.getInt(2);
                ym.jclx = cursor.getInt(3);
                ym.szmc = cursor.getString(4);
                ym.szdm = cursor.getInt(5);
                ym.qqxj = cursor.getFloat(6);
                ym.bqxj = cursor.getFloat(7);
                ym.cfgllx = cursor.getInt(8);
                ym.lc = cursor.getInt(9);
                ym.kjdlxh = cursor.getInt(10);
                ym.fwj = cursor.getFloat(11);
                ym.spj = cursor.getFloat(12);
                ym.bz = cursor.getString(13);
                ym.ckd = cursor.getInt(14);
                ym.szlx = cursor.getInt(15);
                ym.jczt = cursor.getInt(16);
                try {
                    sql = "select qxj,xj from qxjxj where ydh='" + ydh + "' and type='3' and id='" + ym.ymh + "'";
                    String[][] sss = YangdiMgr.SelectData(ydh, sql);
                    if (sss != null) {
                        ym.qxj = Float.parseFloat(sss[0][0]);
                        ym.xj = Float.parseFloat(sss[0][1]);
                    }
                } catch (Exception e) {
                }
                lst.add(ym);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static void SetYssz(int ydh, String str) {
        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (QueryExists(ydh, sql)) {
            sql = "update qt set yssz = '" + str + "' where ydh = '" + ydh + "'";
        } else {
            sql = "insert into qt(ydh, yssz) values('" + ydh + "', '" + str + "')";
        }
        ExecSQL(ydh, sql);
    }

    public static String GetYssz(int ydh) {
        String sql = "select yssz from qt where ydh = '" + ydh + "'";
        return QueryString(ydh, sql);
    }

    public static boolean IsYssz(int ydh, int ymh) {
        String sql = "select yssz from qt where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        String str = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return false;
            }

            cursor.moveToFirst();
            str = cursor.getString(0);
            cursor.close();
        }
        db.close();

        if (str == null || str.equals("")) return false;

        String[] ss = MyFuns.Split(str, ',');
        int[] szs = new int[ss.length];
        for (int i = 0; i < ss.length; i++) {
            try {
                szs[i] = Integer.parseInt(ss[i]);
            } catch (Exception e) {
            }
        }

        Yangmu ym = GetYangmu(ydh, ymh);
        if (ym == null) return false;
        if (ym.bqxj < 5) return false;
        if (ym.kjdlxh > 0) return false;
        if (ym.lmlx > 20 && ym.szdm != MZ_CODE) return false;
        if (ym.jclx == 13 || ym.jclx == 14 || ym.jclx == 15 || ym.jclx == 17 || ym.jclx == 24)
            return false;

        boolean b = false;
        for (int i = 0; i < szs.length; i++) {
            if (szs[i] == ym.szdm) {
                b = true;
                break;
            }
        }
        return b;
    }

    public static void SetKjlYssz(int ydh, int xh, int dm, String str) {
        if (xh == 1) {
            String sql = "update tag set f2 = '" + dm + "', f16 = '" + str + "' where ydh = '" + ydh + "'";
            ExecSQL(ydh, sql);
        }
        if (xh == 2) {
            String sql = "update tag set f3 = '" + dm + "', f17 = '" + str + "' where ydh = '" + ydh + "'";
            ExecSQL(ydh, sql);
        }
        if (xh == 3) {
            String sql = "update tag set f4 = '" + dm + "', f18 = '" + str + "' where ydh = '" + ydh + "'";
            ExecSQL(ydh, sql);
        }
    }

    public static int GetKjlYssz(int ydh, int xh) {
        if (xh == 1) {
            String sql = "select f2 from tag where ydh = '" + ydh + "'";
            return QueryInt(ydh, sql);
        }
        if (xh == 2) {
            String sql = "select f3 from tag where ydh = '" + ydh + "'";
            return QueryInt(ydh, sql);
        }
        if (xh == 3) {
            String sql = "select f4 from tag where ydh = '" + ydh + "'";
            return QueryInt(ydh, sql);
        }
        return 0;
    }

    public static boolean IsKjlYssz(int ydh, int xh, int ymh) {
        String sql = null;
        if (xh == 1) sql = "select f16 from tag where ydh = '" + ydh + "'";
        if (xh == 2) sql = "select f17 from tag where ydh = '" + ydh + "'";
        if (xh == 3) sql = "select f18 from tag where ydh = '" + ydh + "'";
        if (sql == null) return false;
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        String str = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return false;
            }

            cursor.moveToFirst();
            str = cursor.getString(0);
            cursor.close();
        }
        db.close();

        if (str == null || str.equals("")) return false;

        String[] ss = MyFuns.Split(str, ',');
        int[] szs = new int[ss.length];
        for (int i = 0; i < ss.length; i++) {
            try {
                szs[i] = Integer.parseInt(ss[i]);
            } catch (Exception e) {
            }
        }

        Yangmu ym = GetYangmu(ydh, ymh);
        if (ym == null) return false;
        if (ym.bqxj < 5) return false;
        if (ym.kjdlxh != xh) return false;
        if (ym.lmlx > 20 && ym.szdm != MZ_CODE) return false;
        if (ym.jclx == 13 || ym.jclx == 14 || ym.jclx == 15 || ym.jclx == 17 || ym.jclx == 24)
            return false;

        boolean b = false;
        for (int i = 0; i < szs.length; i++) {
            if (szs[i] == ym.szdm) {
                b = true;
                break;
            }
        }
        return b;
    }

    private static boolean isYouxiao(Yangmu ym) {
        if (ym == null) return false;
        if (ym.bqxj < 5) return false;
        if (ym.kjdlxh > 0) return false;
        if (ym.lmlx > 20 && ym.szdm != MZ_CODE) return false;
        if (ym.jclx == 13 || ym.jclx == 14 || ym.jclx == 15 || ym.jclx == 17 || ym.jclx == 24)
            return false;
        return true;
    }

    private static boolean isYouxiao2(Yangmu ym) {
        if (ym == null) return false;
        if (ym.bqxj < 5) return false;
        //if(ym.kjdlxh > 0) return false;
        //if(ym.lmlx > 20 && ym.szdm != MZ_CODE) return false;
        if (ym.jclx == 13 || ym.jclx == 14 || ym.jclx == 15 || ym.jclx == 17 || ym.jclx == 24)
            return false;
        return true;
    }

    //所有已经检尺的树种名称，包括杂竹，不包括跨角林样木、枯倒采、多测木、胸径小于5cm的样木
    public static List<String> GetShuzhong(int ydh) {
        List<String> lst = new ArrayList<String>();
        String sql = "select szmc from mmjc where ydh = '" + ydh + "' "
                + "and kjdlxh not in(1,2,3) "
                + "and (lmlx < '20' or szdm = '" + MZ_CODE + "') "
                + "and (jclx not in(13,14,15,17,24)) "
                + "and bqxj >= '5' "
                + "group by szmc";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }

            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                String str = cursor.getString(0);
                lst.add(str);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    //符合优势树种计算条件的，指定代码的树种数量
    public static int GetZhushu(int ydh, int code) {
        int n = 0;
        List<Yangmu> lst = GetYangmus(ydh);
        for (int i = 0; i < lst.size(); i++) {
            if (isYouxiao(lst.get(i)) && lst.get(i).szdm == code) {
                n++;
            }
        }
        return n;
    }

    public static int GetZhushu2(int ydh, int code) {
        int n = 0;
        List<Yangmu> lst = GetYangmus(ydh);
        for (int i = 0; i < lst.size(); i++) {
            if (isYouxiao2(lst.get(i)) && lst.get(i).szdm == code) {
                n++;
            }
        }
        return n;
    }

    //符合优势树种计算条件的，指定代码的树种平均胸径
    public static float GetPjxj(int ydh, int code) {
        float xj = 0;
        int n = 0;
        List<Yangmu> lst = GetYangmus(ydh);
        for (int i = 0; i < lst.size(); i++) {
            if (isYouxiao(lst.get(i)) && lst.get(i).szdm == code) {
                xj += lst.get(i).bqxj * lst.get(i).bqxj;
                n++;
            }
        }
        if (n == 0) {
            return 0;
        } else {
            xj /= n;
            xj = (float) Math.sqrt(xj);
            xj = MyFuns.MyDecimal(xj, 1);
            return xj;
        }
    }

    //符合优势树种计算条件的，指定代码的树种总断面积
    public static float GetDmj(int ydh, int code) {
        List<Yangmu> lst = GetYangmus(ydh);
        double dmj = 0;
        for (int i = 0; i < lst.size(); i++) {
            if (isYouxiao(lst.get(i)) && lst.get(i).szdm == code) {
                float r = (float) (lst.get(i).bqxj / 2);
                dmj += r * r * Math.PI;
            }
        }
        dmj = MyFuns.MyDecimal(dmj, 2);
        return (float) dmj;
    }

    //优势树种的平均胸径
    public static float GetPjxj(int ydh) {
        List<Yangmu> lst = GetYangmus(ydh);
        float xj = 0;
        int n = 0;
        for (int i = 0; i < lst.size(); i++) {
            if (IsYssz(ydh, lst.get(i).ymh)) {
                xj += lst.get(i).bqxj * lst.get(i).bqxj;
                n++;
            }
        }
        if (n == 0) return 0;
        xj /= n;
        xj = (float) Math.sqrt(xj);
        xj = MyFuns.MyDecimal(xj, 1);
        return xj;
    }

    public static int GetDefaultYssz(int ydh) {
        String sql = "select szdm from mmjc where ydh = '" + ydh + "' "
                + "and kjdlxh not in(1,2,3) "
                + "and (lmlx < '20' or szdm = '" + MZ_CODE + "') "
                + "and (jclx not in(13,14,15,17,24)) "
                + "and bqxj >= '5' "
                + "group by szdm";
        String[][] sss = SelectData(ydh, sql);
        if (sss == null) return -1;
        int sz = -1;
        float mj = 0;
        for (int i = 0; i < sss.length; i++) {
            int code = -1;
            try {
                code = Integer.parseInt(sss[i][0]);
            } catch (Exception e) {
            }
            float dmj = GetDmj(ydh, code);
            if (dmj > mj) {
                mj = dmj;
                sz = code;
            }
        }
        return sz;
    }

    public static int GetDefaultSzjg(int ydh) {
        String sql = "select szdm from mmjc where ydh = '" + ydh + "' "
                + "and kjdlxh not in(1,2,3) "
                + "and (lmlx < '20' or szdm = '" + MZ_CODE + "') "
                + "and (jclx not in(13,14,15,17,24)) "
                + "and bqxj >= '5' "
                + "group by szdm";
        String[][] sss = SelectData(ydh, sql);
        if (sss == null) return -1;
        float zmj = 0;
        int[] szs = new int[sss.length];
        float[] mjs = new float[sss.length];
        for (int i = 0; i < sss.length; i++) {
            int code = -1;
            try {
                code = Integer.parseInt(sss[i][0]);
            } catch (Exception e) {
            }
            float dmj = GetDmj(ydh, code);
            zmj += dmj;
            szs[i] = code;
            mjs[i] = dmj;
        }
        int pos = 0;
        float tmp = mjs[0];
        for (int i = 1; i < sss.length; i++) {
            if (mjs[i] > tmp) {
                tmp = mjs[i];
                pos = i;
            }
        }
        float bl = mjs[pos] / zmj * 100;

        if (bl >= 90) {
            int sz = szs[pos];
            if (Resmgr.IsZhenye(sz)) return 1;
            if (Resmgr.IsKuoye(sz)) return 2;
        } else if (bl >= 65) {
            int sz = szs[pos];
            if (Resmgr.IsZhenye(sz)) return 3;
            if (Resmgr.IsKuoye(sz)) return 4;
        } else {
            float zymj = 0;
            float kymj = 0;
            for (int i = 0; i < sss.length; i++) {
                if (Resmgr.IsZhenye(szs[i])) {
                    zymj += mjs[i];
                } else {
                    kymj += mjs[i];
                }
            }
            float zybl = zymj / zmj * 100;
            float kybl = kymj / zmj * 100;
            if (zybl >= 65) return 5;
            else if (kybl >= 65) return 7;
            else return 6;
        }
        return -1;
    }


    private static boolean isYouxiaoKjl(Yangmu ym, int xh) {
        if (ym == null) return false;
        if (ym.bqxj < 5) return false;
        if (ym.kjdlxh != xh) return false;
        if (ym.lmlx > 20 && ym.szdm != MZ_CODE) return false;
        if (ym.jclx == 13 || ym.jclx == 14 || ym.jclx == 15 || ym.jclx == 17 || ym.jclx == 24)
            return false;
        return true;
    }

    public static List<String> GetKjlShuzhong(int ydh, int xh) {
        List<String> lst = new ArrayList<String>();
        String sql = "select szmc from mmjc where ydh = '" + ydh + "' "
                + "and kjdlxh = '" + xh + "' "
                + "and (lmlx < '20' or szdm = '" + MZ_CODE + "') "
                + "and (jclx not in(13,14,15,17,24)) "
                + "and bqxj >= '5' "
                + "group by szmc";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }

            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                String str = cursor.getString(0);
                lst.add(str);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static int GetKjlZhushu(int ydh, int xh, int code) {
        int n = 0;
        List<Yangmu> lst = GetYangmus(ydh);
        for (int i = 0; i < lst.size(); i++) {
            if (isYouxiaoKjl(lst.get(i), xh) && lst.get(i).szdm == code) {
                n++;
            }
        }
        return n;
    }

    public static float GetKjlPjxj(int ydh, int xh, int code) {
        float xj = 0;
        int n = 0;
        List<Yangmu> lst = GetYangmus(ydh);
        for (int i = 0; i < lst.size(); i++) {
            if (isYouxiaoKjl(lst.get(i), xh) && lst.get(i).szdm == code) {
                xj += lst.get(i).bqxj * lst.get(i).bqxj;
                n++;
            }
        }
        if (n == 0) {
            return 0;
        } else {
            xj /= n;
            xj = (float) Math.sqrt(xj);
            xj = MyFuns.MyDecimal(xj, 1);
            return xj;
        }
    }

    public static float GetKjlDmj(int ydh, int xh, int code) {
        List<Yangmu> lst = GetYangmus(ydh);
        double dmj = 0;
        for (int i = 0; i < lst.size(); i++) {
            if (isYouxiaoKjl(lst.get(i), xh) && lst.get(i).szdm == code) {
                float r = (float) (lst.get(i).bqxj / 2);
                dmj += r * r * Math.PI;
            }
        }
        dmj = MyFuns.MyDecimal(dmj, 2);
        return (float) dmj;
    }

    public static int GetDefaultKjlYssz(int ydh, int xh) {
        if (xh < 1 || xh > 3) return -1;

        String sql = "select szdm from mmjc where ydh = '" + ydh + "' "
                + "and kjdlxh = '" + xh + "' "
                + "and (lmlx < '20' or szdm = '" + MZ_CODE + "') "
                + "and (jclx not in(13,14,15,17,24)) "
                + "and bqxj >= '5' "
                + "group by szdm";
        String[][] sss = SelectData(ydh, sql);
        if (sss == null) return -1;
        int sz = -1;
        float mj = 0;
        for (int i = 0; i < sss.length; i++) {
            int code = -1;
            try {
                code = Integer.parseInt(sss[i][0]);
            } catch (Exception e) {
            }
            float dmj = GetDmj(ydh, code);
            if (dmj > mj) {
                mj = dmj;
                sz = code;
            }
        }
        return sz;
    }

    public static float GetKjlPjsg(int ydh, int xh) {
        String sql = "select avg(sg) from sgcl_kjl where kjdlxh = '" + xh + "' and ydh = '" + ydh + "'";
        float sg = QueryFloat(ydh, sql);
        sg = MyFuns.MyDecimal(sg, 1);
        return sg;
    }


    //样木图
    public static void SetYangmutuDitu(int ydh, String pic) {
        Bitmap bmp = BitmapFactory.decodeFile(pic);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, baos);
        Object[] args = new Object[]{baos.toByteArray()};

        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (QueryExists(ydh, sql)) {
            sql = "update qt set ymtditu = ? where ydh = '" + ydh + "'";
        } else {
            sql = "insert into qt(ydh, ymtditu) values('" + ydh + "', ?)";
        }
        try {
            String dbFile = getDbFile(ydh);
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            db.execSQL(sql, args);
            db.close();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

    public static Bitmap GetYangmutuDitu(int ydh) {
        String sql = "select ymtditu from qt where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        if (count == 0) {
            cursor.close();
            db.close();
            return null;
        }
        cursor.moveToFirst();
        byte[] bts = cursor.getBlob(0);
        if (bts == null) {
            cursor.close();
            db.close();
            return null;
        }
        Bitmap bmpImage = BitmapFactory.decodeByteArray(bts, 0, bts.length);
        cursor.close();
        db.close();
        return bmpImage;
    }

    public static void DelYangmutuDitu(int ydh) {
        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (QueryExists(ydh, sql)) {
            sql = "update qt set ymtditu = '' where ydh = '" + ydh + "'";
            ExecSQL(ydh, sql);
        }
    }

    public static void SetYangmutu(int ydh, String pic) {
        Bitmap bmp = BitmapFactory.decodeFile(pic);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, baos);
        Object[] args = new Object[]{baos.toByteArray()};

        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (QueryExists(ydh, sql)) {
            sql = "update qt set yangmutu = ? where ydh = '" + ydh + "'";
        } else {
            sql = "insert into qt(ydh, yangmutu) values('" + ydh + "', ?)";
        }
        try {
            String dbFile = getDbFile(ydh);
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            db.execSQL(sql, args);
            db.close();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

    public static Bitmap GetYangmutu(int ydh) {
        String sql = "select yangmutu from qt where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        if (count == 0) {
            cursor.close();
            db.close();
            return null;
        }
        cursor.moveToFirst();
        byte[] bts = cursor.getBlob(0);
        if (bts == null) {
            cursor.close();
            db.close();
            return null;
        }
        Bitmap bmpImage = BitmapFactory.decodeByteArray(bts, 0, bts.length);
        cursor.close();
        db.close();
        return bmpImage;
    }

    public static void SetYfwz(int ydh, int yf) {
        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (QueryExists(ydh, sql)) {
            sql = "update qt set yf = '" + yf + "' where ydh = '" + ydh + "'";
        } else {
            sql = "insert into qt(ydh, yf) values('" + ydh + "', '" + yf + "')";
        }
        ExecSQL(ydh, sql);
    }

    public static int GetYfwz(int ydh) {
        String sql = "select yf from qt where ydh = '" + ydh + "'";
        int r = QueryInt(ydh, sql);
        return r;
    }


    public static int GetBqdl(int ydh) {
        String sql = "select dl from ydyz where ydh = '" + ydh + "'";
        return QueryInt(ydh, sql);
    }


    //人员
    public static List<WorkerInfo> GetWorkerList(int ydh) {
        List<WorkerInfo> lst = new ArrayList<WorkerInfo>();
        String sql = "select * from worker where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }

            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                WorkerInfo worker = new WorkerInfo();
                worker.id = cursor.getInt(1);
                worker.name = cursor.getString(2);
                worker.type = cursor.getInt(3);
                worker.phone = cursor.getString(4);
                worker.company = cursor.getString(5);
                worker.address = cursor.getString(6);
                worker.notes = cursor.getString(7);
                lst.add(worker);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static void AddWorker(int ydh, WorkerInfo worker) {
        if (worker == null) return;
        String sql = "insert into worker(ydh, name, type, phone, company, address, notes) values("
                + "'" + ydh + "', "
                + "'" + worker.name + "', "
                + "'" + worker.type + "', "
                + "'" + worker.phone + "', "
                + "'" + worker.company + "', "
                + "'" + worker.address + "', "
                + "'" + worker.notes + "'"
                + ")";
        ExecSQL(ydh, sql);
    }


    public static boolean IsHaveTrkName(int ydh, String name) {
        String sql = "select name from track where ydh = '" + ydh + "' and name = '" + name + "'";
        String[][] sss = SelectData(ydh, sql);
        return sss != null;
    }

    public static String GetValidTrkName(int ydh, String name) {
        String trk = new String(name);
        for (int i = 1; i < 1000; i++) {
            if (IsHaveTrkName(ydh, trk)) {
                trk = name + "-" + i;
            } else {
                break;
            }
        }
        return trk;
    }

    public static List<String> GetTrkNames(int ydh) {
        List<String> lst = new ArrayList<String>();

        String sql = "select name from track where ydh = '" + ydh + "' group by name";
        String[][] sss = SelectData(ydh, sql);
        if (sss != null) {
            for (int i = 0; i < sss.length; i++) {
                lst.add(sss[i][0]);
            }
        }

        return lst;
    }

    public static void AddTrkPoint(int ydh, String trkname, double lon, double lat) {
        String time = MyFuns.GetDateTimeByString();
        String sql = "insert into track values('" + ydh + "', '" + trkname + "', '" + lon + "', '" + lat + "', '" + time + "')";
        ExecSQL(ydh, sql);
    }

    public static List<MyPoint> GetTrkPoint(int ydh, String trkname) {
        List<MyPoint> lst = new ArrayList<MyPoint>();

        String dbFile = getDbFile(ydh);
        if (!MyFile.Exists(dbFile)) return lst;

        String sql = "select lon, lat from track where ydh = '" + ydh + "' and name = '" + trkname + "' and time <> '' order by time";
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                int n = cursor.getCount();
                cursor.moveToFirst();
                for (int i = 0; i < n; i++) {
                    double lon = cursor.getDouble(0);
                    double lat = cursor.getDouble(1);
                    lst.add(new MyPoint(lon, lat));
                    cursor.moveToNext();
                }
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
        }

        return lst;
    }

    public static List<TrackInfo> GetTrackInfo(int ydh) {
        List<TrackInfo> lst = new ArrayList<TrackInfo>();

        List<String> lstNames = GetTrkNames(ydh);
        if (lstNames.size() == 0) return lst;

        String dbFile = getDbFile(ydh);
        if (!MyFile.Exists(dbFile)) return lst;

        for (int i = 0; i < lstNames.size(); i++) {
            String begin = null;
            String end = null;
            int count = 0;
            double dis = 0;
            double x0 = 0;
            double y0 = 0;
            double x1 = 0;
            double y1 = 0;
            try {
                String sql = "select time, lon, lat from track where ydh = '" + ydh + "' and time <> '' and name = '" + lstNames.get(i) + "' order by time";
                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
                Cursor cursor = db.rawQuery(sql, null);
                if (cursor != null) {
                    int n = cursor.getCount();
                    count = n;
                    cursor.moveToFirst();
                    for (int j = 0; j < n; j++) {
                        if (j == 0) begin = cursor.getString(0);
                        if (j == n - 1) end = cursor.getString(0);
                        double lon = cursor.getDouble(1);
                        double lat = cursor.getDouble(2);
                        if (j == 0) {
                            x0 = lon;
                            y0 = lat;
                        } else {
                            x1 = lon;
                            y1 = lat;
                            dis += MyMap.GetDistance(y1, x1, y0, x0);
                            x0 = x1;
                            y0 = y1;
                            x1 = 0;
                            y1 = 0;
                        }
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
                db.close();
            } catch (Exception e) {
            }
            if (count > 0) {
                TrackInfo trk = new TrackInfo();
                trk.ydh = ydh;
                trk.name = lstNames.get(i);
                trk.count = count;
                trk.length = (int) dis;
                trk.begin = begin;
                trk.end = end;
                lst.add(trk);
            } else {
                TrackInfo trk = new TrackInfo();
                trk.ydh = ydh;
                trk.name = lstNames.get(i);
                trk.count = 0;
                trk.length = 0;
                trk.begin = "";
                trk.end = "";
                lst.add(trk);
            }
        }

        return lst;
    }

    public static void CreateTrack(int ydh, String name) {
        String sql = "insert into track values('" + ydh + "', '" + name + "', '', '', '')";
        ExecSQL(ydh, sql);
    }

    public static void DeleteTrack(int ydh, String name) {
        String sql = "delete from track where ydh = '" + ydh + "' and name = '" + name + "'";
        ExecSQL(ydh, sql);
    }

    public static void ExportTrack(int ydh, String name, String excel) {
        String sql = "select * from track where ydh = '" + ydh + "' and name = '" + name + "'";
        String[][] sss = SelectData(ydh, sql);
        if (sss == null) return;
        int count = sss.length;

        String[] fields = new String[4];
        fields[0] = "点号";
        fields[1] = "经度";
        fields[2] = "纬度";
        fields[3] = "时间";
        String[] types = new String[4];
        types[0] = "INTEGER";
        types[1] = "REAL";
        types[2] = "REAL";
        types[3] = "TEXT";
        String[][] values = new String[count][4];
        for (int i = 0; i < count; i++) {
            values[i][0] = String.valueOf(i + 1);
            values[i][1] = sss[i][2];
            values[i][2] = sss[i][3];
            values[i][3] = sss[i][4];
        }
        MyFile.ExportToExcel(values, types, fields, excel, true);
    }


    //样地定位与测设
    public static void SetYindiantu(int ydh, String pic) {
        Bitmap bmp = BitmapFactory.decodeFile(pic);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, baos);
        Object[] args = new Object[]{baos.toByteArray()};

        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (QueryExists(ydh, sql)) {
            sql = "update qt set yindiantu = ? where ydh = '" + ydh + "'";
        } else {
            sql = "insert into qt(ydh, yindiantu) values('" + ydh + "', ?)";
        }
        try {
            String dbFile = getDbFile(ydh);
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            db.execSQL(sql, args);
            db.close();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

    public static void SetYindiantu_(int ydh, int[] colors) {
        byte[] bts = new byte[colors.length * 3];
        int r, g, b;
        for (int i = 0; i < colors.length; i++) {
            r = Color.red(colors[i]);
            g = Color.green(colors[i]);
            b = Color.blue(colors[i]);
            bts[i] = (byte) r;
            bts[i + colors.length] = (byte) g;
            bts[i + colors.length + colors.length] = (byte) b;
        }

        Object[] args = new Object[]{bts};

        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (QueryExists(ydh, sql)) {
            sql = "update qt set yindiantu = ? where ydh = '" + ydh + "'";
        } else {
            sql = "insert into qt(ydh, yindiantu) values('" + ydh + "', ?)";
        }
        try {
            String dbFile = getDbFile(ydh);
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            db.execSQL(sql, args);
            db.close();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

    public static void SetYindiantu(int ydh, int[] colors) {
        byte[] btsR = new byte[colors.length];
        byte[] btsG = new byte[colors.length];
        byte[] btsB = new byte[colors.length];
        int r, g, b;
        for (int i = 0; i < colors.length; i++) {
            r = Color.red(colors[i]);
            g = Color.green(colors[i]);
            b = Color.blue(colors[i]);
            btsR[i] = (byte) r;
            btsG[i] = (byte) g;
            btsB[i] = (byte) b;
        }

        Object[] args = new Object[]{btsR, btsG, btsB};

        String sql = "update tag set f19 = ?, f20 = ?, f21 = ? where ydh = '" + ydh + "'";
        try {
            String dbFile = getDbFile(ydh);
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            db.execSQL(sql, args);
            db.close();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

    public static Bitmap GetYindiantu2(int ydh) {
        String sql = "select f19, f20, f21 from tag where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        if (count == 0) {
            cursor.close();
            db.close();
            return null;
        }
        cursor.moveToFirst();
        byte[] btsR = null;
        byte[] btsG = null;
        byte[] btsB = null;
        try {
            btsR = cursor.getBlob(0);
            btsG = cursor.getBlob(1);
            btsB = cursor.getBlob(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (btsR == null || btsG == null || btsB == null
                || btsG.length != btsR.length || btsB.length != btsR.length) {
            cursor.close();
            db.close();
            return null;
        }
        cursor.close();
        db.close();

        int w = GetYindiantuWidth(ydh);
        int h = GetYindiantuHeight(ydh);
        if (w <= 0 || h <= 0) return null;
        int size = btsR.length;
        int[] colors = new int[size];
        for (int i = 0; i < size; i++) {
            colors[i] = Color.rgb(btsR[i], btsG[i], btsB[i]);
        }
        Bitmap bmp = null;
        try {
            bmp = Bitmap.createBitmap(colors, w, h, Bitmap.Config.ARGB_8888);
        } catch (Exception e) {
        }
        return bmp;
    }

    public static Bitmap GetYindiantu1(int ydh) {
        String sql = "select yindiantu from qt where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        if (count == 0) {
            cursor.close();
            db.close();
            return null;
        }
        cursor.moveToFirst();
        byte[] bts = null;
        try {
            bts = cursor.getBlob(0);
        } catch (Exception e) {
        }
        if (bts == null) {
            cursor.close();
            db.close();
            return null;
        }
        cursor.close();
        db.close();

        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeByteArray(bts, 0, bts.length);
        } catch (Exception e) {
        }
        return bmp;
		/*
		if(bmp != null)
		{
			return bmp;
		}
		else
		{
			int w = GetYindiantuWidth(ydh);
			int h = GetYindiantuHeight(ydh);
			if(w <= 0 || h <= 0) return null;
			int size = bts.length / 3;
			int[] colors = new int[size];
			int r, g, b;
			for(int i=0;i<size;i++)
			{
				r = bts[i] & 0xff;
				g = bts[size+i] & 0xff;
				b = bts[size + size + i] & 0xff;
				colors[i] = Color.rgb(r, g, b);
			}

			try{
				bmp = Bitmap.createBitmap(colors, w, h, Bitmap.Config.ARGB_8888);
			}catch(Exception e){}
			return bmp;
		}
		*/
    }

    public static Bitmap GetYindiantu(int ydh) {
        Bitmap bmp = GetYindiantu2(ydh);
        if (bmp != null) {
            return bmp;
        } else {
            return GetYindiantu1(ydh);
        }
    }

    public static void SetYindiantuWidth(int w, int ydh) {
        String sql = "update tag set f11 = '" + w + "' where ydh = '" + ydh + "'";
        ExecSQL(ydh, sql);
    }

    public static void SetYindiantuHeight(int h, int ydh) {
        String sql = "update tag set f12 = '" + h + "' where ydh = '" + ydh + "'";
        ExecSQL(ydh, sql);
    }

    public static int GetYindiantuWidth(int ydh) {
        String sql = "select f11 from tag where ydh = '" + ydh + "'";
        return QueryInt(ydh, sql);
    }

    public static int GetYindiantuHeight(int ydh) {
        String sql = "select f12 from tag where ydh = '" + ydh + "'";
        return QueryInt(ydh, sql);
    }

    public static void DelYindiantu(int ydh) {
        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (QueryExists(ydh, sql)) {
            sql = "update qt set yindiantu = '' where ydh = '" + ydh + "'";
            ExecSQL(ydh, sql);
        }
    }

    public static void SetYangditu(int ydh, String pic) {
        Bitmap bmp = BitmapFactory.decodeFile(pic);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, baos);
        Object[] args = new Object[]{baos.toByteArray()};

        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (QueryExists(ydh, sql)) {
            sql = "update qt set yangditu = ? where ydh = '" + ydh + "'";
        } else {
            sql = "insert into qt(ydh, yangditu) values('" + ydh + "', ?)";
        }
        try {
            String dbFile = getDbFile(ydh);
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            db.execSQL(sql, args);
            db.close();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

    public static void SetYangditu_(int ydh, int[] colors) {
        byte[] bts = new byte[colors.length * 3];
        int r, g, b;
        for (int i = 0; i < colors.length; i++) {
            r = Color.red(colors[i]);
            g = Color.green(colors[i]);
            b = Color.blue(colors[i]);
            bts[i] = (byte) r;
            bts[i + colors.length] = (byte) g;
            bts[i + colors.length + colors.length] = (byte) b;
        }

        Object[] args = new Object[]{bts};

        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (QueryExists(ydh, sql)) {
            sql = "update qt set yangditu = ? where ydh = '" + ydh + "'";
        } else {
            sql = "insert into qt(ydh, yangditu) values('" + ydh + "', ?)";
        }
        try {
            String dbFile = getDbFile(ydh);
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            db.execSQL(sql, args);
            db.close();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

    public static void SetYangditu(int ydh, int[] colors) {
        byte[] btsR = new byte[colors.length];
        byte[] btsG = new byte[colors.length];
        byte[] btsB = new byte[colors.length];
        int r, g, b;
        for (int i = 0; i < colors.length; i++) {
            r = Color.red(colors[i]);
            g = Color.green(colors[i]);
            b = Color.blue(colors[i]);
            btsR[i] = (byte) r;
            btsG[i] = (byte) g;
            btsB[i] = (byte) b;
        }

        Object[] args = new Object[]{btsR, btsG, btsB};

        String sql = "update tag set f22 = ?, f23 = ?, f24 = ? where ydh = '" + ydh + "'";
        try {
            String dbFile = getDbFile(ydh);
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            db.execSQL(sql, args);
            db.close();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

    public static Bitmap GetYangditu2(int ydh) {
        String sql = "select f22, f23, f24 from tag where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        if (count == 0) {
            cursor.close();
            db.close();
            return null;
        }
        cursor.moveToFirst();
        byte[] btsR = null;
        byte[] btsG = null;
        byte[] btsB = null;
        try {
            btsR = cursor.getBlob(0);
            btsG = cursor.getBlob(1);
            btsB = cursor.getBlob(2);
        } catch (Exception e) {
        }
        if (btsR == null || btsG == null || btsB == null
                || btsG.length != btsR.length || btsB.length != btsR.length) {
            cursor.close();
            db.close();
            return null;
        }
        cursor.close();
        db.close();

        int w = GetYindiantuWidth(ydh);
        int h = GetYindiantuHeight(ydh);
        if (w <= 0 || h <= 0) return null;
        int size = btsR.length;
        int[] colors = new int[size];
        for (int i = 0; i < size; i++) {
            colors[i] = Color.rgb(btsR[i], btsG[i], btsB[i]);
        }
        Bitmap bmp = null;
        try {
            bmp = Bitmap.createBitmap(colors, w, h, Bitmap.Config.ARGB_8888);
        } catch (Exception e) {
        }
        return bmp;
    }

    public static Bitmap GetYangditu1(int ydh) {
        String sql = "select yangditu from qt where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        if (count == 0) {
            cursor.close();
            db.close();
            return null;
        }
        cursor.moveToFirst();
        byte[] bts = null;
        try {
            bts = cursor.getBlob(0);
        } catch (Exception e) {
        }
        if (bts == null) {
            cursor.close();
            db.close();
            return null;
        }
        cursor.close();
        db.close();
        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeByteArray(bts, 0, bts.length);
        } catch (Exception e) {
        }
        return bmp;
		/*
		if(bmp != null)
		{
			return bmp;
		}
		else
		{
			int w = GetYangdituWidth(ydh);
			int h = GetYangdituHeight(ydh);
			if(w <= 0 || h <= 0) return null;
			int size = bts.length / 3;
			int[] colors = new int[size];
			int r, g, b;
			for(int i=0;i<size;i++)
			{
				r = bts[i] & 0xff;
				g = bts[size+i] & 0xff;
				b = bts[size + size + i] & 0xff;
				colors[i] = Color.rgb(r, g, b);
			}

			try{
				bmp = Bitmap.createBitmap(colors, w, h, Bitmap.Config.ARGB_8888);
			}catch(Exception e){}
			return bmp;
		}
		*/
    }

    public static Bitmap GetYangditu(int ydh) {
        Bitmap bmp = GetYangditu2(ydh);
        if (bmp != null) {
            return bmp;
        } else {
            return GetYangditu1(ydh);
        }
    }

    public static void SetYangdituWidth(int w, int ydh) {
        String sql = "update tag set f13 = '" + w + "' where ydh = '" + ydh + "'";
        ExecSQL(ydh, sql);
    }

    public static void SetYangdituHeight(int h, int ydh) {
        String sql = "update tag set f14 = '" + h + "' where ydh = '" + ydh + "'";
        ExecSQL(ydh, sql);
    }

    public static int GetYangdituWidth(int ydh) {
        String sql = "select f13 from tag where ydh = '" + ydh + "'";
        return QueryInt(ydh, sql);
    }

    public static int GetYangdituHeight(int ydh) {
        String sql = "select f14 from tag where ydh = '" + ydh + "'";
        return QueryInt(ydh, sql);
    }

    public static void DelYangditu(int ydh) {
        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (QueryExists(ydh, sql)) {
            sql = "update qt set yangditu = '' where ydh = '" + ydh + "'";
            ExecSQL(ydh, sql);
        }
    }

    public static List<Yddww> GetYindianDww(int ydh) {
        List<Yddww> lst = new ArrayList<Yddww>();
        String sql = "select * from yindiandww where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }

            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                Yddww dww = new Yddww();
                dww.xh = cursor.getInt(1);
                dww.mc = cursor.getString(2);
                dww.bh = cursor.getInt(3);
                dww.fwj = cursor.getFloat(4);
                dww.spj = cursor.getFloat(5);

                try {
                    sql = "select qxj,xj from qxjxj where ydh='" + ydh + "' and type='2' and id='" + dww.xh + "'";
                    String[][] sss = YangdiMgr.SelectData(ydh, sql);
                    if (sss != null) {
                        dww.qxj = Float.parseFloat(sss[0][0]);
                        dww.xj = Float.parseFloat(sss[0][1]);
                    }
                } catch (Exception e) {
                }

                lst.add(dww);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static Yddww GetYindianDww(int ydh, int xh) {
        String sql = "select * from yindiandww where ydh = '" + ydh + "' and xh = '" + xh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        Yddww dww = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }

            cursor.moveToFirst();
            dww = new Yddww();
            dww.xh = cursor.getInt(1);
            dww.mc = cursor.getString(2);
            dww.bh = cursor.getInt(3);
            dww.fwj = cursor.getFloat(4);
            dww.spj = cursor.getFloat(5);
            try {
                sql = "select qxj,xj from qxjxj where ydh='" + ydh + "' and type='2' and id='" + dww.xh + "'";
                String[][] sss = YangdiMgr.SelectData(ydh, sql);
                if (sss != null) {
                    dww.qxj = Float.parseFloat(sss[0][0]);
                    dww.xj = Float.parseFloat(sss[0][1]);
                }
            } catch (Exception e) {
            }
            cursor.close();
        }
        db.close();
        return dww;
    }

    public static void AddYindianDww(int ydh, Yddww dww) {
        String sql = "insert into yindiandww(ydh, mc, bh, fwj, spj) values("
                + "'" + ydh + "', "
                + "'" + dww.mc + "', "
                + "'" + dww.bh + "', "
                + "'" + dww.fwj + "', "
                + "'" + dww.spj + "')";
        ExecSQL(ydh, sql);
        try {
            sql = "select xh from yindiandww where ydh = '" + ydh + "' and bh = '" + dww.bh + "'";
            int xh = YangdiMgr.QueryInt(ydh, sql);
            if (xh >= 0) {
                sql = "insert into qxjxj(ydh, type, id, qxj, xj) values("
                        + "'" + ydh + "', "
                        + "'2', "
                        + "'" + xh + "', "
                        + "'" + dww.qxj + "', "
                        + "'" + dww.xj + "')";
                ExecSQL(ydh, sql);
            }
        } catch (Exception e) {
        }
    }

    public static void UpdateYindianDww(int ydh, Yddww dww) {
        String sql = "update yindiandww set "
                + "mc = '" + dww.mc + "', "
                + "bh = '" + dww.bh + "', "
                + "fwj = '" + dww.fwj + "', "
                + "spj = '" + dww.spj + "' "
                + "where ydh = '" + ydh + "' and xh = '" + dww.xh + "'";
        ExecSQL(ydh, sql);
        try {
            sql = "select id from qxjxj where ydh = '" + ydh + "' and id = '" + dww.xh + "' and type = '2'";
            int xh = YangdiMgr.QueryInt(ydh, sql);
            if (xh < 0) {
                sql = "insert into qxjxj(ydh, type, id, qxj, xj) values("
                        + "'" + ydh + "', "
                        + "'2', "
                        + "'" + xh + "', "
                        + "'" + dww.qxj + "', "
                        + "'" + dww.xj + "')";
                ExecSQL(ydh, sql);
            } else {
                sql = "update qxjxj set "
                        + "qxj = '" + dww.qxj + "', "
                        + "xj = '" + dww.xj + "' "
                        + "where ydh = '" + ydh + "' and id = '" + dww.xh + "' and type = '2'";
                ExecSQL(ydh, sql);
            }
        } catch (Exception e) {
        }
    }

    public static void DelYindianDww(int ydh, int xh) {
        String sql = "select ydh from yindiandww where ydh = '" + ydh + "'";
        if (QueryExists(ydh, sql)) {
            sql = "delete from yindiandww where xh = '" + xh + "'";
            ExecSQL(ydh, sql);
        }
        try {
            sql = "delete from qxjxj where ydh = '" + ydh + "' and id = '" + xh + "' and type = '2'";
            ExecSQL(ydh, sql);
        } catch (Exception e) {
        }
    }

    public static List<Yddww> GetYangdiDww(int ydh) {
        List<Yddww> lst = new ArrayList<Yddww>();
        String sql = "select * from yangdidww where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }

            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                Yddww dww = new Yddww();
                dww.xh = cursor.getInt(1);
                dww.mc = cursor.getString(2);
                dww.bh = cursor.getInt(3);
                dww.fwj = cursor.getFloat(4);
                dww.spj = cursor.getFloat(5);
                try {
                    sql = "select qxj,xj from qxjxj where ydh='" + ydh + "' and type='1' and id='" + dww.xh + "'";
                    String[][] sss = YangdiMgr.SelectData(ydh, sql);
                    if (sss != null) {
                        dww.qxj = Float.parseFloat(sss[0][0]);
                        dww.xj = Float.parseFloat(sss[0][1]);
                    }
                } catch (Exception e) {
                }
                lst.add(dww);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static Yddww GetYangdiDww(int ydh, int xh) {
        String sql = "select * from yangdidww where ydh = '" + ydh + "' and xh = '" + xh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        Yddww dww = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }

            cursor.moveToFirst();
            dww = new Yddww();
            dww.xh = cursor.getInt(1);
            dww.mc = cursor.getString(2);
            dww.bh = cursor.getInt(3);
            dww.fwj = cursor.getFloat(4);
            dww.spj = cursor.getFloat(5);
            try {
                sql = "select qxj,xj from qxjxj where ydh='" + ydh + "' and type='1' and id='" + dww.xh + "'";
                String[][] sss = YangdiMgr.SelectData(ydh, sql);
                if (sss != null) {
                    dww.qxj = Float.parseFloat(sss[0][0]);
                    dww.xj = Float.parseFloat(sss[0][1]);
                }
            } catch (Exception e) {
            }
            cursor.close();
        }
        db.close();
        return dww;
    }

    public static void AddYangdiDww(int ydh, Yddww dww) {
        String sql = "insert into yangdidww(ydh, mc, bh, fwj, spj) values("
                + "'" + ydh + "', "
                + "'" + dww.mc + "', "
                + "'" + dww.bh + "', "
                + "'" + dww.fwj + "', "
                + "'" + dww.spj + "')";
        ExecSQL(ydh, sql);
        try {
            sql = "select xh from yindiandww where ydh = '" + ydh + "' and bh = '" + dww.bh + "'";
            int xh = YangdiMgr.QueryInt(ydh, sql);
            if (xh >= 0) {
                sql = "insert into qxjxj(ydh, type, id, qxj, xj) values("
                        + "'" + ydh + "', "
                        + "'1', "
                        + "'" + xh + "', "
                        + "'" + dww.qxj + "', "
                        + "'" + dww.xj + "')";
                ExecSQL(ydh, sql);
            }
        } catch (Exception e) {
        }
    }

    public static void UpdateYangdiDww(int ydh, Yddww dww) {
        String sql = "update yangdidww set "
                + "mc = '" + dww.mc + "', "
                + "bh = '" + dww.bh + "', "
                + "fwj = '" + dww.fwj + "', "
                + "spj = '" + dww.spj + "' "
                + "where ydh = '" + ydh + "' and xh = '" + dww.xh + "'";
        ExecSQL(ydh, sql);
        try {
            sql = "select id from qxjxj where ydh = '" + ydh + "' and id = '" + dww.xh + "' and type = '1'";
            int xh = YangdiMgr.QueryInt(ydh, sql);
            if (xh < 0) {
                sql = "insert into qxjxj(ydh, type, id, qxj, xj) values("
                        + "'" + ydh + "', "
                        + "'1', "
                        + "'" + xh + "', "
                        + "'" + dww.qxj + "', "
                        + "'" + dww.xj + "')";
                ExecSQL(ydh, sql);
            } else {
                sql = "update qxjxj set "
                        + "qxj = '" + dww.qxj + "', "
                        + "xj = '" + dww.xj + "' "
                        + "where ydh = '" + ydh + "' and id = '" + dww.xh + "' and type = '1'";
                ExecSQL(ydh, sql);
            }
        } catch (Exception e) {
        }
    }

    public static void DelYangdiDww(int ydh, int xh) {
        String sql = "select ydh from yangdidww where ydh = '" + ydh + "'";
        if (QueryExists(ydh, sql)) {
            sql = "delete from yangdidww where xh = '" + xh + "'";
            ExecSQL(ydh, sql);
        }
        try {
            sql = "delete from qxjxj where ydh = '" + ydh + "' and id = '" + xh + "' and type = '1'";
            ExecSQL(ydh, sql);
        } catch (Exception e) {
        }
    }


    //引线测量
    public static void AddYxcl(int ydh, Cljl cljl) {
        String sql = "insert into yxcl(ydh, cz, fwj, qxj, xj, spj, lj) values("
                + "'" + ydh + "', "
                + "'" + cljl.cz + "', "
                + "'" + cljl.fwj + "', "
                + "'" + cljl.qxj + "', "
                + "'" + cljl.xj + "', "
                + "'" + cljl.spj + "', "
                + "'" + cljl.lj + "')";
        ExecSQL(ydh, sql);
    }

    public static void UpdateYxcl(int ydh, Cljl cljl) {
        String sql = "update yxcl set "
                + "cz = '" + cljl.cz + "', "
                + "fwj = '" + cljl.fwj + "', "
                + "qxj = '" + cljl.qxj + "', "
                + "xj = '" + cljl.xj + "', "
                + "spj = '" + cljl.spj + "', "
                + "lj = '" + cljl.lj + "' "
                + " where ydh = '" + ydh + "' and xh = '" + cljl.xh + "'";
        ExecSQL(ydh, sql);
    }

    public static void DelYxcl(int ydh, int xh) {
        String sql = "delete from yxcl where ydh = '" + ydh + "' and xh = '" + xh + "'";
        ExecSQL(ydh, sql);
    }

    public static List<Cljl> GetYxclList(int ydh) {
        List<Cljl> lst = new ArrayList<Cljl>();
        String sql = "select * from yxcl where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }

            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                Cljl item = new Cljl();
                item.xh = cursor.getInt(1);
                item.cz = cursor.getString(2);
                item.fwj = cursor.getFloat(3);
                item.qxj = cursor.getFloat(4);
                item.xj = cursor.getFloat(5);
                item.spj = cursor.getFloat(6);
                item.lj = cursor.getFloat(7);
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static Cljl GetYxcl(int ydh, int xh) {
        Cljl item = null;
        String sql = "select * from yxcl where ydh = '" + ydh + "' and xh = '" + xh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }

            cursor.moveToFirst();
            item = new Cljl();
            item.xh = cursor.getInt(1);
            item.cz = cursor.getString(2);
            item.fwj = cursor.getFloat(3);
            item.qxj = cursor.getFloat(4);
            item.xj = cursor.getFloat(5);
            item.spj = cursor.getFloat(6);
            item.lj = cursor.getFloat(7);
            cursor.close();
        }
        db.close();
        return item;
    }

    public static float GetYxclFwj(int ydh) {
        String sql = "select fwj from yxcl where ydh = '" + ydh + "'";
        return QueryFloat(ydh, sql);
    }


    //周界测量
    public static void AddZjcl(int ydh, Cljl cljl) {
        String sql = "insert into zjcl(ydh, cz, fwj, qxj, xj, spj, lj) values("
                + "'" + ydh + "', "
                + "'" + cljl.cz + "', "
                + "'" + cljl.fwj + "', "
                + "'" + cljl.qxj + "', "
                + "'" + cljl.xj + "', "
                + "'" + cljl.spj + "', "
                + "'" + cljl.lj + "')";
        ExecSQL(ydh, sql);
    }

    public static void UpdateZjcl(int ydh, Cljl cljl) {
        String sql = "update zjcl set "
                + "cz = '" + cljl.cz + "', "
                + "fwj = '" + cljl.fwj + "', "
                + "qxj = '" + cljl.qxj + "', "
                + "xj = '" + cljl.xj + "', "
                + "spj = '" + cljl.spj + "', "
                + "lj = '" + cljl.lj + "' "
                + " where ydh = '" + ydh + "' and xh = '" + cljl.xh + "'";
        ExecSQL(ydh, sql);
    }

    public static void DelZjcl(int ydh, int xh) {
        String sql = "delete from zjcl where ydh = '" + ydh + "' and xh = '" + xh + "'";
        ExecSQL(ydh, sql);
    }

    public static List<Cljl> GetZjclList(int ydh) {
        List<Cljl> lst = new ArrayList<Cljl>();
        String sql = "select * from zjcl where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }

            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                Cljl item = new Cljl();
                item.xh = cursor.getInt(1);
                item.cz = cursor.getString(2);
                item.fwj = cursor.getFloat(3);
                item.qxj = cursor.getFloat(4);
                item.xj = cursor.getFloat(5);
                item.spj = cursor.getFloat(6);
                item.lj = cursor.getFloat(7);
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static Cljl GetZjcl(int ydh, int xh) {
        Cljl item = null;
        String sql = "select * from zjcl where ydh = '" + ydh + "' and xh = '" + xh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }

            cursor.moveToFirst();
            item = new Cljl();
            item.xh = cursor.getInt(1);
            item.cz = cursor.getString(2);
            item.fwj = cursor.getFloat(3);
            item.qxj = cursor.getFloat(4);
            item.xj = cursor.getFloat(5);
            item.spj = cursor.getFloat(6);
            item.lj = cursor.getFloat(7);
            cursor.close();
        }
        db.close();
        return item;
    }

    public static int GetZjclType(int ydh) {
        String sql = "select zjcllx from qt where ydh = '" + ydh + "'";
        String str = QueryString(ydh, sql);
        int r = -1;
        try {
            r = Integer.parseInt(str);
        } catch (Exception e) {
        }
        return r;
    }

    public static void SetZjclType(int ydh, int type) {
        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (QueryExists(ydh, sql)) {
            sql = "update qt set zjcllx = '" + type + "' where ydh = '" + ydh + "'";
        } else {
            sql = "insert into qt(ydh, zjcllx) values('" + ydh + "', '" + type + "')";
        }
        ExecSQL(ydh, sql);
    }


    //跨角林
    public static boolean IsHasKjlxh(int ydh, int xh) {
        String sql = "select * from kjl where ydh = '" + ydh + "' and xh = '" + xh + "'";
        return QueryExists(ydh, sql);
    }

    public static void AddKjl(int ydh, Kjl item) {
        String sql = "insert into kjl(ydh, xh, mjbl, dl, tdqs, lmqs, linzh, qy, yssz, lingz, ybd, pjsg, slqljg, szjg, spljydj) values("
                + "'" + ydh + "', "
                + "'" + item.xh + "', "
                + "'" + item.mjbl + "', "
                + "'" + item.dl + "', "
                + "'" + item.tdqs + "', "
                + "'" + item.lmqs + "', "
                + "'" + item.linzh + "', "
                + "'" + item.qy + "', "
                + "'" + item.yssz + "', "
                + "'" + item.lingz + "', "
                + "'" + item.ybd + "', "
                + "'" + item.pjsg + "', "
                + "'" + item.slqljg + "', "
                + "'" + item.szjg + "', "
                + "'" + item.spljydj + "')";
        ExecSQL(ydh, sql);
    }

    public static void UpdateKjl(int ydh, Kjl item) {
        String sql = "update kjl set "
                + "ydh = '" + ydh + "', "
                + "xh = '" + item.xh + "', "
                + "mjbl = '" + item.mjbl + "', "
                + "dl = '" + item.dl + "', "
                + "tdqs = '" + item.tdqs + "', "
                + "lmqs = '" + item.lmqs + "', "
                + "linzh = '" + item.linzh + "', "
                + "qy = '" + item.qy + "', "
                + "yssz = '" + item.yssz + "', "
                + "lingz = '" + item.lingz + "', "
                + "ybd = '" + item.ybd + "', "
                + "pjsg = '" + item.pjsg + "', "
                + "slqljg = '" + item.slqljg + "', "
                + "szjg = '" + item.szjg + "', "
                + "spljydj = '" + item.spljydj + "' "
                + " where ydh = '" + ydh + "' and xh = '" + item.xh + "'";
        ExecSQL(ydh, sql);
    }

    public static void DelKjl(int ydh, int xh) {
        String sql = "delete from kjl where ydh = '" + ydh + "' and xh = '" + xh + "'";
        ExecSQL(ydh, sql);
        SetKjlYssz(ydh, xh, 0, "");
    }

    public static List<Kjl> GetKjlList(int ydh) {
        List<Kjl> lst = new ArrayList<Kjl>();
        String sql = "select * from kjl where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }

            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                Kjl item = new Kjl();
                item.xh = cursor.getInt(1);
                item.mjbl = cursor.getFloat(2);
                item.dl = cursor.getInt(3);
                item.tdqs = cursor.getInt(4);
                item.lmqs = cursor.getInt(5);
                item.linzh = cursor.getInt(6);
                item.qy = cursor.getInt(7);
                item.yssz = cursor.getInt(8);
                item.lingz = cursor.getInt(9);
                item.ybd = cursor.getFloat(10);
                item.pjsg = cursor.getFloat(11);
                item.slqljg = cursor.getInt(12);
                item.szjg = cursor.getInt(13);
                item.spljydj = cursor.getInt(14);
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static Kjl GetKjl(int ydh, int xh) {
        Kjl item = null;
        String sql = "select * from kjl where ydh = '" + ydh + "' and xh = '" + xh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }

            cursor.moveToFirst();
            item = new Kjl();
            item.xh = cursor.getInt(1);
            item.mjbl = cursor.getFloat(2);
            item.dl = cursor.getInt(3);
            item.tdqs = cursor.getInt(4);
            item.lmqs = cursor.getInt(5);
            item.linzh = cursor.getInt(6);
            item.qy = cursor.getInt(7);
            item.yssz = cursor.getInt(8);
            item.lingz = cursor.getInt(9);
            item.ybd = cursor.getFloat(10);
            item.pjsg = cursor.getFloat(11);
            item.slqljg = cursor.getInt(12);
            item.szjg = cursor.getInt(13);
            item.spljydj = cursor.getInt(14);
            cursor.close();
        }
        db.close();
        return item;
    }


    //森林灾害
    public static void AddSlzh(int ydh, Slzh item) {
        String sql = "insert into slzh(ydh, zhlx, whbw, szymzs, szdj) values("
                + "'" + ydh + "', "
                + "'" + item.zhlx + "', "
                + "'" + item.whbw + "', "
                + "'" + item.szymzs + "', "
                + "'" + item.szdj + "')";
        ExecSQL(ydh, sql);
    }

    public static void UpdateSlzh(int ydh, Slzh item) {
        String sql = "update slzh set "
                + "zhlx = '" + item.zhlx + "', "
                + "whbw = '" + item.whbw + "', "
                + "szymzs = '" + item.szymzs + "', "
                + "szdj = '" + item.szdj + "' "
                + " where ydh = '" + ydh + "' and xh = '" + item.xh + "'";
        ExecSQL(ydh, sql);
    }

    public static void DelSlzh(int ydh, int xh) {
        String sql = "delete from slzh where ydh = '" + ydh + "' and xh = '" + xh + "'";
        ExecSQL(ydh, sql);
    }

    public static List<Slzh> GetSlzhList(int ydh) {
        List<Slzh> lst = new ArrayList<Slzh>();
        String sql = "select * from slzh where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }

            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                Slzh item = new Slzh();
                item.xh = cursor.getInt(1);
                item.zhlx = cursor.getInt(2);
                item.whbw = cursor.getString(3);
                item.szymzs = cursor.getInt(4);
                item.szdj = cursor.getInt(5);
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static Slzh GetSlzh(int ydh, int xh) {
        Slzh item = null;
        String sql = "select * from slzh where ydh = '" + ydh + "' and xh = '" + xh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }

            cursor.moveToFirst();
            item = new Slzh();
            item.xh = cursor.getInt(1);
            item.zhlx = cursor.getInt(2);
            item.whbw = cursor.getString(3);
            item.szymzs = cursor.getInt(4);
            item.szdj = cursor.getInt(5);
            cursor.close();
        }
        db.close();
        return item;
    }

    public static int GetMaxSlzhXh(int ydh) {
        String sql = "select max(xh) from slzh where ydh = '" + ydh + "'";
        return QueryInt(ydh, sql);
    }

    //树高测量
    public static void AddSgcl(int ydh, Sgcl item) {
        String sql = "insert into sgcl(ydh, ymh, sz, xj, sg, zxg) values("
                + "'" + ydh + "', "
                + "'" + item.ymh + "', "
                + "'" + item.sz + "', "
                + "'" + item.xj + "', "
                + "'" + item.sg + "', "
                + "'" + item.zxg + "')";
        ExecSQL(ydh, sql);
    }

    public static void UpdateSgcl(int ydh, Sgcl item) {
        String sql = "update sgcl set "
                + "ymh = '" + item.ymh + "', "
                + "sz = '" + item.sz + "', "
                + "xj = '" + item.xj + "', "
                + "sg = '" + item.sg + "', "
                + "zxg = '" + item.zxg + "' "
                + " where ydh = '" + ydh + "' and xh = '" + item.xh + "'";
        ExecSQL(ydh, sql);
    }

    public static void DelSgcl(int ydh, int xh) {
        String sql = "delete from sgcl where ydh = '" + ydh + "' and xh = '" + xh + "'";
        ExecSQL(ydh, sql);
    }

    public static List<Sgcl> GetSgclList(int ydh) {
        List<Sgcl> lst = new ArrayList<Sgcl>();
        String sql = "select * from sgcl where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }

            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                Sgcl item = new Sgcl();
                item.xh = cursor.getInt(1);
                item.ymh = cursor.getInt(2);
                item.sz = cursor.getInt(3);
                item.xj = cursor.getFloat(4);
                item.sg = cursor.getFloat(5);
                item.zxg = cursor.getFloat(6);
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static Sgcl GetSgcl(int ydh, int xh) {
        Sgcl item = null;
        String sql = "select * from sgcl where ydh = '" + ydh + "' and xh = '" + xh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }

            cursor.moveToFirst();
            item = new Sgcl();
            item.xh = cursor.getInt(1);
            item.ymh = cursor.getInt(2);
            item.sz = cursor.getInt(3);
            item.xj = cursor.getFloat(4);
            item.sg = cursor.getFloat(5);
            item.zxg = cursor.getFloat(6);
            cursor.close();
        }
        db.close();
        return item;
    }


    public static void AddKjlSgcl(int ydh, int xh, Sgcl item) {
        String sql = "insert into sgcl_kjl(ydh, kjdlxh, ymh, sz, xj, sg, zxg) values("
                + "'" + ydh + "', "
                + "'" + xh + "', "
                + "'" + item.ymh + "', "
                + "'" + item.sz + "', "
                + "'" + item.xj + "', "
                + "'" + item.sg + "', "
                + "'" + item.zxg + "')";
        ExecSQL(ydh, sql);
    }

    public static List<Sgcl> GetKjlSgclList(int ydh, int xh) {
        List<Sgcl> lst = new ArrayList<Sgcl>();
        String sql = "select * from sgcl_kjl where ydh = '" + ydh + "' and kjdlxh = '" + xh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }

            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                Sgcl item = new Sgcl();
                item.xh = cursor.getInt(1);
                item.ymh = cursor.getInt(3);
                item.sz = cursor.getInt(4);
                item.xj = cursor.getFloat(5);
                item.sg = cursor.getFloat(6);
                item.zxg = cursor.getFloat(7);
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static void ClearKjlSgcl(int ydh, int xh) {
        String sql = "delete from sgcl_kjl where ydh = '" + ydh + "' and kjdlxh = '" + xh + "'";
        ExecSQL(ydh, sql);
    }


    //下木调查
    public static void AddXm(int ydh, Xm item) {
        String sql = "insert into xmdc(ydh, mc, gd, xj) values("
                + "'" + ydh + "', "
                + "'" + item.mc + "', "
                + "'" + item.gd + "', "
                + "'" + item.xj + "')";
        ExecSQL(ydh, sql);
    }

    public static void UpdateXm(int ydh, Xm item) {
        String sql = "update xmdc set "
                + "mc = '" + item.mc + "', "
                + "gd = '" + item.gd + "', "
                + "xj = '" + item.xj + "' "
                + " where ydh = '" + ydh + "' and xh = '" + item.xh + "'";
        ExecSQL(ydh, sql);
    }

    public static void DelXm(int ydh, int xh) {
        String sql = "delete from xmdc where ydh = '" + ydh + "' and xh = '" + xh + "'";
        ExecSQL(ydh, sql);
    }

    public static List<Xm> GetXmList(int ydh) {
        List<Xm> lst = new ArrayList<Xm>();
        String sql = "select * from xmdc where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }

            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                Xm item = new Xm();
                item.xh = cursor.getInt(1);
                item.mc = cursor.getString(2);
                item.gd = cursor.getFloat(3);
                item.xj = cursor.getFloat(4);
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static Xm GetXm(int ydh, int xh) {
        Xm item = null;
        String sql = "select * from xmdc where ydh = '" + ydh + "' and xh = '" + xh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }

            cursor.moveToFirst();
            item = new Xm();
            item.xh = cursor.getInt(1);
            item.mc = cursor.getString(2);
            item.gd = cursor.getFloat(3);
            item.xj = cursor.getFloat(4);
            cursor.close();
        }
        db.close();
        return item;
    }

    public static int GetXmCount(int ydh) {
        String sql = "select count(*) from xmdc where ydh = '" + ydh + "'";
        int r = QueryInt(ydh, sql);
        if (r == -1) r = 0;
        return r;
    }

    //枯落物调查
    public static void AddKlw(int ydh, Klw item) {
        String sql = "insert into klwdc(ydh, yfh, hd, yfxz, yfgz, ypxz, ypgz) values("
                + "'" + ydh + "', "
                + "'" + item.yfh + "', "
                + "'" + item.hd + "', "
                + "'" + item.yfxz + "', "
                + "'" + item.yfgz + "', "
                + "'" + item.ypxz + "', "
                + "'" + item.ypgz + "')";
        ExecSQL(ydh, sql);
    }

    public static void UpdateKlw(int ydh, Klw item) {
        String sql = "update klwdc set "
                + "yfh = '" + item.yfh + "', "
                + "hd = '" + item.hd + "', "
                + "yfxz = '" + item.yfxz + "', "
                + "yfgz = '" + item.yfgz + "', "
                + "ypxz = '" + item.ypxz + "', "
                + "ypgz = '" + item.ypgz + "' "
                + " where ydh = '" + ydh + "' and xh = '" + item.xh + "'";
        ExecSQL(ydh, sql);
    }

    public static void DelKlw(int ydh, int xh) {
        String sql = "delete from klwdc where ydh = '" + ydh + "' and xh = '" + xh + "'";
        ExecSQL(ydh, sql);
    }

    public static List<Klw> GetKlwList(int ydh) {
        List<Klw> lst = new ArrayList<Klw>();
        String sql = "select * from klwdc where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }

            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                Klw item = new Klw();
                item.xh = cursor.getInt(1);
                item.yfh = cursor.getInt(2);
                item.hd = cursor.getFloat(3);
                item.yfxz = cursor.getFloat(4);
                item.yfgz = cursor.getFloat(5);
                item.ypxz = cursor.getFloat(6);
                item.ypgz = cursor.getFloat(7);
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static Klw GetKlw(int ydh, int xh) {
        Klw item = null;
        String sql = "select * from klwdc where ydh = '" + ydh + "' and xh = '" + xh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }

            cursor.moveToFirst();
            item = new Klw();
            item.xh = cursor.getInt(1);
            item.yfh = cursor.getInt(2);
            item.hd = cursor.getFloat(3);
            item.yfxz = cursor.getFloat(4);
            item.yfgz = cursor.getFloat(5);
            item.ypxz = cursor.getFloat(6);
            item.ypgz = cursor.getFloat(7);
            cursor.close();
        }
        db.close();
        return item;
    }

    public static int GetKlwCount(int ydh) {
        String sql = "select count(*) from klwdc where ydh = '" + ydh + "'";
        int r = QueryInt(ydh, sql);
        if (r == -1) r = 0;
        return r;
    }

    //土壤调查
    public static void AddTr(int ydh, Tr item) {
        String sql = "insert into trdc values("
                + "'" + ydh + "', "
                + "?, "
                + "'" + item.sd + "', "
                + "'" + item.rz + "', "
                + "'" + item.zd + "', "
                + "'" + item.yjt + "', "
                + "'" + item.yjtmd + "', "
                + "'" + item.tqkxd + "', "
                + "'" + item.fyzs + "', "
                + "'" + item.yjz + "', "
                + "'" + item.sf + "', "
                + "'" + item.ph + "', "
                + "'" + item.ec + "', "
                + "'" + item.TN + "', "
                + "'" + item.TP + "', "
                + "'" + item.TK + "', "
                + "'" + item.AN + "', "
                + "'" + item.AP + "', "
                + "'" + item.AK + "', "
                + "'" + item.Cl + "', "
                + "'" + item.B + "', "
                + "'" + item.Al + "', "
                + "'" + item.Ca + "', "
                + "'" + item.Mg + "', "
                + "'" + item.Na + "', "
                + "'" + item.Fe + "', "
                + "'" + item.Mn + "', "
                + "'" + item.Zn + "', "
                + "'" + item.Cu + "', "
                + "'" + item.S + "', "
                + "'" + item.Mo + "', "
                + "'" + item.As + "', "
                + "'" + item.Cd + "', "
                + "'" + item.Cr + "', "
                + "'" + item.Co + "', "
                + "'" + item.Pb + "', "
                + "'" + item.Hg + "', "
                + "'" + item.Ni + "', "
                + "'" + item.Se + "', "
                + "'" + item.Ag + "', "
                + "'" + item.V + "', "
                + "'" + item.C6_9 + "', "
                + "'" + item.C10_14 + "', "
                + "'" + item.C15_28 + "', "
                + "'" + item.C29_36 + "', "
                + "'" + item.Ben + "', "
                + "'" + item.JiaBen + "', "
                + "'" + item.YiBen + "', "
                + "'" + item.DJ_EJB + "', "
                + "'" + item.L_EJB + "')";
        ExecSQL(ydh, sql);
    }

    public static void UpdateTr(int ydh, Tr item) {
        String sql = "update trdc set "
                + "sd = '" + item.sd + "', "
                + "rz = '" + item.rz + "', "
                + "zd = '" + item.zd + "', "
                + "yjt = '" + item.yjt + "', "
                + "yjtmd = '" + item.yjtmd + "', "
                + "tqkxd = '" + item.tqkxd + "', "
                + "fyzs = '" + item.fyzs + "', "
                + "yjz = '" + item.yjz + "', "
                + "sf = '" + item.sf + "', "
                + "ph = '" + item.ph + "', "
                + "ec = '" + item.ec + "', "
                + "TN = '" + item.TN + "', "
                + "TP = '" + item.TP + "', "
                + "TK = '" + item.TK + "', "
                + "AN = '" + item.AN + "', "
                + "AP = '" + item.AP + "', "
                + "AK = '" + item.AK + "', "
                + "Cl = '" + item.Cl + "', "
                + "B = '" + item.B + "', "
                + "Al = '" + item.Al + "', "
                + "Ca = '" + item.Ca + "', "
                + "Mg = '" + item.Mg + "', "
                + "Na = '" + item.Na + "', "
                + "Fe = '" + item.Fe + "', "
                + "Mn = '" + item.Mn + "', "
                + "Zn = '" + item.Zn + "', "
                + "Cu = '" + item.Cu + "', "
                + "S = '" + item.S + "', "
                + "Mo = '" + item.Mo + "', "
                + "Shen = '" + item.As + "', "
                + "Cd = '" + item.Cd + "', "
                + "Cr = '" + item.Cr + "', "
                + "Co = '" + item.Co + "', "
                + "Pd = '" + item.Pb + "', "
                + "Hg = '" + item.Hg + "', "
                + "Ni = '" + item.Ni + "', "
                + "Se = '" + item.Se + "', "
                + "Ag = '" + item.Ag + "', "
                + "V = '" + item.V + "', "
                + "C6_9 = '" + item.C6_9 + "', "
                + "C10_14 = '" + item.C10_14 + "', "
                + "C15_28 = '" + item.C15_28 + "', "
                + "C29_36 = '" + item.C29_36 + "', "
                + "Ben = '" + item.Ben + "', "
                + "JiaBen = '" + item.JiaBen + "', "
                + "YiBen = '" + item.YiBen + "', "
                + "DJ_EJB = '" + item.DJ_EJB + "', "
                + "L_EJB = '" + item.L_EJB + "' "
                + " where ydh = '" + ydh + "' and xh = '" + item.xh + "'";
        ExecSQL(ydh, sql);
    }

    public static void DelTr(int ydh, int xh) {
        String sql = "delete from trdc where ydh = '" + ydh + "' and xh = '" + xh + "'";
        ExecSQL(ydh, sql);
    }

    public static List<Tr> GetTrList(int ydh) {
        List<Tr> lst = new ArrayList<Tr>();
        String sql = "select * from trdc where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }

            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                Tr item = new Tr();
                item.xh = cursor.getInt(1);
                item.sd = cursor.getInt(2);
                item.rz = cursor.getFloat(3);
                item.zd = cursor.getFloat(4);
                item.yjt = cursor.getFloat(5);
                item.yjtmd = cursor.getFloat(6);
                item.tqkxd = cursor.getFloat(7);
                item.fyzs = cursor.getFloat(8);
                item.yjz = cursor.getFloat(9);
                item.sf = cursor.getFloat(10);
                item.ph = cursor.getFloat(11);
                item.ec = cursor.getFloat(12);
                item.TN = cursor.getFloat(13);
                item.TP = cursor.getFloat(14);
                item.TK = cursor.getFloat(15);
                item.AN = cursor.getFloat(16);
                item.AP = cursor.getFloat(17);
                item.AK = cursor.getFloat(18);
                item.Cl = cursor.getFloat(19);
                item.B = cursor.getFloat(20);
                item.Al = cursor.getFloat(21);
                item.Ca = cursor.getFloat(22);
                item.Mg = cursor.getFloat(23);
                item.Na = cursor.getFloat(24);
                item.Fe = cursor.getFloat(25);
                item.Mn = cursor.getFloat(26);
                item.Zn = cursor.getFloat(27);
                item.Cu = cursor.getFloat(28);
                item.S = cursor.getFloat(29);
                item.Mo = cursor.getFloat(30);
                item.As = cursor.getFloat(31);
                item.Cd = cursor.getFloat(32);
                item.Cr = cursor.getFloat(33);
                item.Co = cursor.getFloat(34);
                item.Pb = cursor.getFloat(35);
                item.Hg = cursor.getFloat(36);
                item.Ni = cursor.getFloat(37);
                item.Se = cursor.getFloat(38);
                item.Ag = cursor.getFloat(39);
                item.V = cursor.getFloat(40);
                item.C6_9 = cursor.getFloat(41);
                item.C10_14 = cursor.getFloat(42);
                item.C15_28 = cursor.getFloat(43);
                item.C29_36 = cursor.getFloat(44);
                item.Ben = cursor.getFloat(45);
                item.JiaBen = cursor.getFloat(46);
                item.YiBen = cursor.getFloat(47);
                item.DJ_EJB = cursor.getFloat(48);
                item.L_EJB = cursor.getFloat(49);
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static Tr GetTr(int ydh, int xh) {
        Tr item = null;
        String sql = "select * from trdc where ydh = '" + ydh + "' and xh = '" + xh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }

            cursor.moveToFirst();
            item = new Tr();
            item.xh = cursor.getInt(1);
            item.sd = cursor.getInt(2);
            item.rz = cursor.getFloat(3);
            item.zd = cursor.getFloat(4);
            item.yjt = cursor.getFloat(5);
            item.yjtmd = cursor.getFloat(6);
            item.tqkxd = cursor.getFloat(7);
            item.fyzs = cursor.getFloat(8);
            item.yjz = cursor.getFloat(9);
            item.sf = cursor.getFloat(10);
            item.ph = cursor.getFloat(11);
            item.ec = cursor.getFloat(12);
            item.TN = cursor.getFloat(13);
            item.TP = cursor.getFloat(14);
            item.TK = cursor.getFloat(15);
            item.AN = cursor.getFloat(16);
            item.AP = cursor.getFloat(17);
            item.AK = cursor.getFloat(18);
            item.Cl = cursor.getFloat(19);
            item.B = cursor.getFloat(20);
            item.Al = cursor.getFloat(21);
            item.Ca = cursor.getFloat(22);
            item.Mg = cursor.getFloat(23);
            item.Na = cursor.getFloat(24);
            item.Fe = cursor.getFloat(25);
            item.Mn = cursor.getFloat(26);
            item.Zn = cursor.getFloat(27);
            item.Cu = cursor.getFloat(28);
            item.S = cursor.getFloat(29);
            item.Mo = cursor.getFloat(30);
            item.As = cursor.getFloat(31);
            item.Cd = cursor.getFloat(32);
            item.Cr = cursor.getFloat(33);
            item.Co = cursor.getFloat(34);
            item.Pb = cursor.getFloat(35);
            item.Hg = cursor.getFloat(36);
            item.Ni = cursor.getFloat(37);
            item.Se = cursor.getFloat(38);
            item.Ag = cursor.getFloat(39);
            item.V = cursor.getFloat(40);
            item.C6_9 = cursor.getFloat(41);
            item.C10_14 = cursor.getFloat(42);
            item.C15_28 = cursor.getFloat(43);
            item.C29_36 = cursor.getFloat(44);
            item.Ben = cursor.getFloat(45);
            item.JiaBen = cursor.getFloat(46);
            item.YiBen = cursor.getFloat(47);
            item.DJ_EJB = cursor.getFloat(48);
            item.L_EJB = cursor.getFloat(49);
            cursor.close();
        }
        db.close();
        return item;
    }

    public static int GetTrCount(int ydh) {
        String sql = "select count(*) from trdc where ydh = '" + ydh + "'";
        int r = QueryInt(ydh, sql);
        if (r == -1) r = 0;
        return r;
    }


    //天然更新
    public static void AddTrgx(int ydh, Trgx item) {
        String sql = "insert into trgx(ydh, sz, zs1, zs2, zs3, jkzk, phqk) values("
                + "'" + ydh + "', "
                + "'" + item.sz + "', "
                + "'" + item.zs1 + "', "
                + "'" + item.zs2 + "', "
                + "'" + item.zs3 + "', "
                + "'" + item.jkzk + "', "
                + "'" + item.phqk + "')";
        ExecSQL(ydh, sql);
    }

    public static void UpdateTrgx(int ydh, Trgx item) {
        String sql = "update trgx set "
                + "sz = '" + item.sz + "', "
                + "zs1 = '" + item.zs1 + "', "
                + "zs2 = '" + item.zs2 + "', "
                + "zs3 = '" + item.zs3 + "', "
                + "jkzk = '" + item.jkzk + "', "
                + "phqk = '" + item.phqk + "' "
                + " where ydh = '" + ydh + "' and xh = '" + item.xh + "'";
        ExecSQL(ydh, sql);
    }

    public static void DelTrgx(int ydh, int xh) {
        String sql = "delete from trgx where ydh = '" + ydh + "' and xh = '" + xh + "'";
        ExecSQL(ydh, sql);
    }

    public static List<Trgx> GetTrgxList(int ydh) {
        List<Trgx> lst = new ArrayList<Trgx>();
        String sql = "select * from trgx where ydh = '" + ydh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }

            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                Trgx item = new Trgx();
                item.xh = cursor.getInt(1);
                item.sz = cursor.getString(2);
                item.zs1 = cursor.getInt(3);
                item.zs2 = cursor.getInt(4);
                item.zs3 = cursor.getInt(5);
                item.jkzk = cursor.getString(6);
                item.phqk = cursor.getString(7);
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static Trgx GetTrgx(int ydh, int xh) {
        Trgx item = null;
        String sql = "select * from trgx where ydh = '" + ydh + "' and xh = '" + xh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }

            cursor.moveToFirst();
            item = new Trgx();
            item.xh = cursor.getInt(1);
            item.sz = cursor.getString(2);
            item.zs1 = cursor.getInt(3);
            item.zs2 = cursor.getInt(4);
            item.zs3 = cursor.getInt(5);
            item.jkzk = cursor.getString(6);
            item.phqk = cursor.getString(7);
            cursor.close();
        }
        db.close();
        return item;
    }


    //植被调查
    public static void AddZb(int ydh, Zb item) {
        String sql = "insert into zbdc(ydh, zblx, mc, pjgd, fgd, zs, pjdj) values("
                + "'" + ydh + "', "
                + "'" + item.zblx + "', "
                + "'" + item.mc + "', "
                + "'" + item.pjgd + "', "
                + "'" + item.fgd + "', "
                + "'" + item.zs + "', "
                + "'" + item.pjdj + "')";
        ExecSQL(ydh, sql);
    }

    public static void UpdateZb(int ydh, Zb item) {
        String sql = "update zbdc set "
                + "mc = '" + item.mc + "', "
                + "pjgd = '" + item.pjgd + "', "
                + "fgd = '" + item.fgd + "', "
                + "zs = '" + item.zs + "', "
                + "pjdj = '" + item.pjdj + "' "
                + " where ydh = '" + ydh + "' and xh = '" + item.xh + "'";
        ExecSQL(ydh, sql);
    }

    public static void DelZb(int ydh, int xh) {
        String sql = "delete from zbdc where ydh = '" + ydh + "' and xh = '" + xh + "'";
        ExecSQL(ydh, sql);
    }

    public static List<Zb> GetZbList(int ydh, int zblx) {
        List<Zb> lst = new ArrayList<Zb>();
        String sql = "select * from zbdc where ydh = '" + ydh + "' and zblx = '" + zblx + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }

            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                Zb item = new Zb();
                item.xh = cursor.getInt(1);
                item.zblx = cursor.getInt(2);
                item.mc = cursor.getString(3);
                item.pjgd = cursor.getFloat(4);
                item.fgd = cursor.getInt(5);
                item.zs = cursor.getInt(6);
                item.pjdj = cursor.getFloat(7);
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static Zb GetZb(int ydh, int xh) {
        Zb item = null;
        String sql = "select * from zbdc where ydh = '" + ydh + "' and xh = '" + xh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }

            cursor.moveToFirst();
            item = new Zb();
            item.xh = cursor.getInt(1);
            item.zblx = cursor.getInt(2);
            item.mc = cursor.getString(3);
            item.pjgd = cursor.getFloat(4);
            item.fgd = cursor.getInt(5);
            item.zs = cursor.getInt(6);
            item.pjdj = cursor.getFloat(7);
            cursor.close();
        }
        db.close();
        return item;
    }

    public static int GetZbCount(int ydh) {
        String sql = "select count(*) from zbdc where ydh = '" + ydh + "'";
        int r = QueryInt(ydh, sql);
        if (r == -1) r = 0;
        return r;
    }


    //样地变化
    public static String[] GetBqbhyz(int ydh) {
        String sql = "select dl, linzh, qy, yssz, lingz, zblx from ydyz where ydh = '" + ydh + "'";
        String[][] sss = SelectData(ydh, sql);
        if (sss != null) return sss[0];
        return null;
    }

    public static String[] GetQqbhyz(int ydh) {
        String sql = "select dl, linzh, qy, yssz, lingz, zblx from ydyz_old where ydh = '" + ydh + "'";
        String[][] sss = SelectData(ydh, sql);
        if (sss != null) return sss[0];
        return null;
    }

    public static String[] GetYdbhyy(int ydh) {
        String sql = "select dlbhyy, linzhbhyy, qybhyy, ysszbhyy, lingzbhyy, zblxbhyy, bz from ydbh where ydh = '" + ydh + "'";
        String[][] sss = SelectData(ydh, sql);
        if (sss != null) return sss[0];
        return null;
    }


    public static String GetJcrq(int ydh) {
        String sql = "select f1 from tag where ydh = '" + ydh + "'";
        String[][] sss = SelectData(ydh, sql);
        if (sss != null) return sss[0][0];
        return null;
    }

    public static void SetJcrq(int ydh, String jcrq) {
        String sql = "update tag set f1 = '" + jcrq + "' where ydh = '" + ydh + "'";
        ExecSQL(ydh, sql);
    }


    //log
    public static void AddLog(int ydh, String str) {
        String time = MyFuns.GetDateTimeByString();
        String sql = "insert into log_info(ydh, logtime, loginfo) values('" + ydh + "', '" + time + "', '" + str + "')";
        ExecSQL(ydh, sql);
    }


    public static void CombineKpfm(String inFile, boolean isCover, int ydh) {
        String sql = "select ydh from kpfm where ydh = '" + ydh + "'";
        String[][] sss0 = SelectData(ydh, sql);
        sql = "select * from kpfm where ydh = '" + ydh + "'";
        String[][] sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            if (sss0 == null) {
                sql = "insert into kpfm values("
                        + "'" + sss1[0][0] + "', "
                        + "'" + sss1[0][1] + "', "
                        + "'" + sss1[0][2] + "', "
                        + "'" + sss1[0][3] + "', "
                        + "'" + sss1[0][4] + "', "
                        + "'" + sss1[0][5] + "', "
                        + "'" + sss1[0][6] + "', "
                        + "'" + sss1[0][7] + "', "
                        + "'" + sss1[0][8] + "', "
                        + "'" + sss1[0][9] + "', "
                        + "'" + sss1[0][10] + "', "
                        + "'" + sss1[0][11] + "', "
                        + "'" + sss1[0][12] + "', "
                        + "'" + sss1[0][13] + "', "
                        + "'" + sss1[0][14] + "', "
                        + "'" + sss1[0][15] + "', "
                        + "'" + sss1[0][16] + "', "
                        + "'" + sss1[0][17] + "', "
                        + "'" + sss1[0][18] + "', "
                        + "'" + sss1[0][19] + "', "
                        + "'" + sss1[0][20] + "', "
                        + "'" + sss1[0][21] + "', "
                        + "'" + sss1[0][22] + "', "
                        + "'" + sss1[0][23] + "' "
                        + ")";
                ExecSQL(ydh, sql);
            } else if (isCover) {
                sql = "update kpfm set "
                        + "ztmc = '" + sss1[0][0] + "', "
                        + "ydh = '" + sss1[0][1] + "', "
                        + "ydxz = '" + sss1[0][2] + "', "
                        + "ydmj = '" + sss1[0][3] + "', "
                        + "zzb = '" + sss1[0][4] + "', "
                        + "hzb = '" + sss1[0][5] + "', "
                        + "ydjj = '" + sss1[0][6] + "', "
                        + "dxttfh = '" + sss1[0][7] + "', "
                        + "wph = '" + sss1[0][8] + "', "
                        + "dfxzbm = '" + sss1[0][9] + "', "
                        + "lyxzbm = '" + sss1[0][10] + "', "
                        + "shi = '" + sss1[0][11] + "', "
                        + "xian = '" + sss1[0][12] + "', "
                        + "xiang = '" + sss1[0][13] + "', "
                        + "cun = '" + sss1[0][14] + "', "
                        + "xdm = '" + sss1[0][15] + "', "
                        + "lyqyj = '" + sss1[0][16] + "', "
                        + "zrbhq = '" + sss1[0][17] + "', "
                        + "slgy = '" + sss1[0][18] + "', "
                        + "gylc = '" + sss1[0][19] + "', "
                        + "jtlc = '" + sss1[0][20] + "', "
                        + "xd = '" + sss1[0][21] + "', "
                        + "xddw = '" + sss1[0][22] + "', "
                        + "dcrq = '" + sss1[0][23] + "' "
                        + " where ydh = '" + ydh + "'";
                ExecSQL(ydh, sql);
            }
        }

        sql = "select ydh from qt where ydh = '" + ydh + "'";
        sss0 = SelectData(ydh, sql);
        sql = "select " +
                "ifnull(tujing, '') as tujing," +
                "ifnull(cfsj,'') as cfsj," +
                "ifnull(zdsj,'') as zdsj," +
                "ifnull(jssj,'') as jssj," +
                "ifnull(fhsj,'') as fhsj," +
                "ifnull(gps_type,'') as gps_type," +
                "ifnull(gps_dis,'') as gps_dis," +
                "ifnull(gps_begin,'') as gps_begin," +
                "ifnull(gps_end,'') as gps_end," +
                "ifnull(ydphoto,'') as ydphoto," +
                "ifnull(ymphoto,'') as ymphoto " +
                "from qt where ydh = '" + ydh + "'";
        sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            if (sss0 == null) {
                sql = "insert into qt(ydh,tujing,cfsj,zdsj,jssj,fhsj,gps_type,gps_dis,gps_begin,gps_end,ydphoto,ymphoto) values("
                        + "'" + ydh + "', "
                        + "'" + sss1[0][0] + "', "
                        + "'" + sss1[0][1] + "', "
                        + "'" + sss1[0][2] + "', "
                        + "'" + sss1[0][3] + "', "
                        + "'" + sss1[0][4] + "', "
                        + "'" + sss1[0][5] + "', "
                        + "'" + sss1[0][6] + "', "
                        + "'" + sss1[0][7] + "', "
                        + "'" + sss1[0][8] + "', "
                        + "'" + sss1[0][9] + "', "
                        + "'" + sss1[0][10] + "' "
                        + ")";
                ExecSQL(ydh, sql);
            } else if (isCover) {
                sql = "update qt set "
                        + "tujing = '" + sss1[0][0] + "', "
                        + "cfsj = '" + sss1[0][1] + "', "
                        + "zdsj = '" + sss1[0][2] + "', "
                        + "jssj = '" + sss1[0][3] + "', "
                        + "fhsj = '" + sss1[0][4] + "', "
                        + "gps_type = '" + sss1[0][5] + "', "
                        + "gps_dis = '" + sss1[0][6] + "', "
                        + "gps_begin = '" + sss1[0][7] + "', "
                        + "gps_end = '" + sss1[0][8] + "', "
                        + "ydphoto = '" + sss1[0][9] + "', "
                        + "ymphoto = '" + sss1[0][10] + "' "
                        + " where ydh = '" + ydh + "'";
                ExecSQL(ydh, sql);
            }
        }

        sql = "select * from worker where ydh = '" + ydh + "'";
        sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            for (int i = 0; i < sss1.length; i++) {
                if (isCover) {
                    sql = "delete from worker where ydh = '" + ydh + "' and name = '" + sss1[i][2] + "' and phone = '" + sss1[i][4] + "'";
                    ExecSQL(ydh, sql);
                    sql = "insert into worker(ydh, name, type, phone, company, address, notes) values("
                            + "'" + ydh + "', "
                            + "'" + sss1[i][2] + "', "
                            + "'" + sss1[i][3] + "', "
                            + "'" + sss1[i][4] + "', "
                            + "'" + sss1[i][5] + "', "
                            + "'" + sss1[i][6] + "', "
                            + "'" + sss1[i][7] + "'"
                            + ")";
                    ExecSQL(ydh, sql);
                } else {
                    sql = "select ydh from worker where ydh = '" + ydh + "' and name = '" + sss1[i][2] + "' and phone = '" + sss1[i][4] + "'";
                    sss0 = SelectData(ydh, sql);
                    if (sss0 == null) {
                        sql = "insert into worker(ydh, name, type, phone, company, address, notes) values("
                                + "'" + ydh + "', "
                                + "'" + sss1[i][2] + "', "
                                + "'" + sss1[i][3] + "', "
                                + "'" + sss1[i][4] + "', "
                                + "'" + sss1[i][5] + "', "
                                + "'" + sss1[i][6] + "', "
                                + "'" + sss1[i][7] + "'"
                                + ")";
                        ExecSQL(ydh, sql);
                    }
                }
            }
        }

        if (isCover) {
            sql = "select * from tag where ydh = '" + ydh + "'";
            sss1 = selectData(inFile, sql);
            sql = "update tag set "
                    + "f1 = '" + sss1[0][1] + "' "
                    + " where ydh = '" + ydh + "'";
            ExecSQL(ydh, sql);
        }
    }

    public static void CombineYindiantz(String inFile, boolean isCover, int ydh) {
        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        String[][] sss0 = SelectData(ydh, sql);
        sql = "select " +
                "ifnull(zbfwj,'') as zbfwj, " +
                "ifnull(cfwj,'') as cfwj, " +
                "ifnull(yxjl,'') as yxjl, " +
                "ifnull(lc,'') as lc, " +
                "ifnull(yindian,'') as yindian " +
                "from qt where ydh = '" + ydh + "'";
        String[][] sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            if (sss0 == null) {
                sql = "insert into qt(ydh, zbfwj, cfwj, yxjl, lc, yindian) values("
                        + "'" + ydh + "', "
                        + "'" + sss1[0][0] + "', "
                        + "'" + sss1[0][1] + "', "
                        + "'" + sss1[0][2] + "', "
                        + "'" + sss1[0][3] + "', "
                        + "'" + sss1[0][4] + "' "
                        + ")";
                ExecSQL(ydh, sql);
            } else if (isCover) {
                sql = "update qt set "
                        + "zbfwj = '" + sss1[0][0] + "', "
                        + "cfwj = '" + sss1[0][1] + "', "
                        + "yxjl = '" + sss1[0][2] + "', "
                        + "lc = '" + sss1[0][3] + "', "
                        + "yindian = '" + sss1[0][4] + "' "
                        + " where ydh = '" + ydh + "'";
                ExecSQL(ydh, sql);
            }
        }
        sql = "select ifnull(yindiantu,'') as yindiantu from qt where ydh = '" + ydh + "'";
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(inFile, null);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                byte[] blob = null;
                try {
                    blob = cursor.getBlob(0);
                } catch (Exception e) {
                }
                if (blob != null) {
                    sql = "update qt set yindiantu = ? where ydh = '" + ydh + "'";
                    String dbFile = getDbFile(ydh);
                    SQLiteDatabase db2 = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
                    db2.execSQL(sql, new Object[]{blob});
                    db2.close();
                }
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
        }


        sql = "select * from yindiandww where ydh = '" + ydh + "'";
        sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            for (int i = 0; i < sss1.length; i++) {
                sql = "insert into yindiandww(ydh, mc, bh, fwj, spj) values("
                        + "'" + ydh + "', "
                        + "'" + sss1[i][2] + "', "
                        + "'" + sss1[i][3] + "', "
                        + "'" + sss1[i][4] + "', "
                        + "'" + sss1[i][5] + "' "
                        + ")";
                ExecSQL(ydh, sql);
            }
        }
    }

    public static void CombineYangditz(String inFile, boolean isCover, int ydh) {
        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        String[][] sss0 = SelectData(ydh, sql);
        sql = "select ifnull(yangdi,'') as yangdi from qt where ydh = '" + ydh + "'";
        String[][] sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            if (sss0 == null) {
                sql = "insert into qt(ydh, yangdi) values("
                        + "'" + ydh + "', "
                        + "'" + sss1[0][0] + "' "
                        + ")";
                ExecSQL(ydh, sql);
            } else if (isCover) {
                sql = "update qt set "
                        + "yangdi = '" + sss1[0][0] + "'"
                        + " where ydh = '" + ydh + "'";
                ExecSQL(ydh, sql);
            }
        }
        sql = "select ifnull(yangditu,'') as yangditu from qt where ydh = '" + ydh + "'";
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(inFile, null);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                byte[] blob = null;
                try {
                    blob = cursor.getBlob(0);
                } catch (Exception e) {
                }
                if (blob != null) {
                    sql = "update qt set yangditu = ? where ydh = '" + ydh + "'";
                    String dbFile = getDbFile(ydh);
                    SQLiteDatabase db2 = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
                    db2.execSQL(sql, new Object[]{blob});
                    db2.close();
                }
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
        }

        sql = "select * from yangdidww where ydh = '" + ydh + "'";
        sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            for (int i = 0; i < sss1.length; i++) {
                sql = "insert into yangdidww(ydh, mc, bh, fwj, spj) values("
                        + "'" + ydh + "', "
                        + "'" + sss1[i][2] + "', "
                        + "'" + sss1[i][3] + "', "
                        + "'" + sss1[i][4] + "', "
                        + "'" + sss1[i][5] + "' "
                        + ")";
                ExecSQL(ydh, sql);
            }
        }
    }

    public static void CombineYxcl(String inFile, boolean isCover, int ydh) {
        String sql = "select * from yxcl where ydh = '" + ydh + "'";
        String[][] sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            for (int i = 0; i < sss1.length; i++) {
                sql = "insert into yxcl(ydh, cz, fwj, qxj, xj, spj, lj) values("
                        + "'" + ydh + "', "
                        + "'" + sss1[i][2] + "', "
                        + "'" + sss1[i][3] + "', "
                        + "'" + sss1[i][4] + "', "
                        + "'" + sss1[i][5] + "', "
                        + "'" + sss1[i][6] + "', "
                        + "'" + sss1[i][7] + "' "
                        + ")";
                ExecSQL(ydh, sql);
            }
        }
    }

    public static void CombineZjcl(String inFile, boolean isCover, int ydh) {
        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        String[][] sss0 = SelectData(ydh, sql);
        sql = "select " +
                "ifnull(zjcllx,'') as zjcllx, " +
                "ifnull(jdbhc,'') as jdbhc, " +
                "ifnull(xdbhc,'') as xdbhc, " +
                "ifnull(zcwc,'') as zcwc " +
                "from qt where ydh = '" + ydh + "'";
        String[][] sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            if (sss0 == null) {
                sql = "insert into qt(ydh, zjcllx, jdbhc, xdbhc, zcwc) values("
                        + "'" + ydh + "', "
                        + "'" + sss1[0][0] + "', "
                        + "'" + sss1[0][1] + "', "
                        + "'" + sss1[0][2] + "', "
                        + "'" + sss1[0][3] + "' "
                        + ")";
                ExecSQL(ydh, sql);
            } else if (isCover) {
                sql = "update qt set "
                        + "zjcllx = '" + sss1[0][0] + "', "
                        + "jdbhc = '" + sss1[0][1] + "', "
                        + "xdbhc = '" + sss1[0][2] + "', "
                        + "zcwc = '" + sss1[0][3] + "' "
                        + " where ydh = '" + ydh + "'";
                ExecSQL(ydh, sql);
            }
        }

        sql = "select * from zjcl where ydh = '" + ydh + "'";
        sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            for (int i = 0; i < sss1.length; i++) {
                sql = "insert into zjcl(ydh, cz, fwj, qxj, xj, spj, lj) values("
                        + "'" + ydh + "', "
                        + "'" + sss1[i][2] + "', "
                        + "'" + sss1[i][3] + "', "
                        + "'" + sss1[i][4] + "', "
                        + "'" + sss1[i][5] + "', "
                        + "'" + sss1[i][6] + "', "
                        + "'" + sss1[i][7] + "' "
                        + ")";
                ExecSQL(ydh, sql);
            }
        }
    }

    public static void CombineYdyz(String inFile, boolean isCover, int ydh) {
        String sql = "select ydh from ydyz where ydh = '" + ydh + "'";
        String[][] sss0 = SelectData(ydh, sql);
        sql = "select * from ydyz where ydh = '" + ydh + "'";
        String[][] sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            if (sss0 == null) {
                sql = "insert into ydyz values("
                        + "'" + ydh + "', "
                        + "'" + sss1[0][1] + "', "
                        + "'" + sss1[0][2] + "', "
                        + "'" + sss1[0][3] + "', "
                        + "'" + sss1[0][4] + "', "
                        + "'" + sss1[0][5] + "', "
                        + "'" + sss1[0][6] + "', "
                        + "'" + sss1[0][7] + "', "
                        + "'" + sss1[0][8] + "', "
                        + "'" + sss1[0][9] + "', "
                        + "'" + sss1[0][10] + "', "
                        + "'" + sss1[0][11] + "', "
                        + "'" + sss1[0][12] + "', "
                        + "'" + sss1[0][13] + "', "
                        + "'" + sss1[0][14] + "', "
                        + "'" + sss1[0][15] + "', "
                        + "'" + sss1[0][16] + "', "
                        + "'" + sss1[0][17] + "', "
                        + "'" + sss1[0][18] + "', "
                        + "'" + sss1[0][19] + "', "
                        + "'" + sss1[0][20] + "', "
                        + "'" + sss1[0][21] + "', "
                        + "'" + sss1[0][22] + "', "
                        + "'" + sss1[0][23] + "', "
                        + "'" + sss1[0][24] + "', "
                        + "'" + sss1[0][25] + "', "
                        + "'" + sss1[0][26] + "', "
                        + "'" + sss1[0][27] + "', "
                        + "'" + sss1[0][28] + "', "
                        + "'" + sss1[0][29] + "', "
                        + "'" + sss1[0][30] + "', "
                        + "'" + sss1[0][31] + "', "
                        + "'" + sss1[0][32] + "', "
                        + "'" + sss1[0][33] + "', "
                        + "'" + sss1[0][34] + "', "
                        + "'" + sss1[0][35] + "', "
                        + "'" + sss1[0][36] + "', "
                        + "'" + sss1[0][37] + "', "
                        + "'" + sss1[0][38] + "', "
                        + "'" + sss1[0][39] + "', "
                        + "'" + sss1[0][40] + "', "
                        + "'" + sss1[0][41] + "', "
                        + "'" + sss1[0][42] + "', "
                        + "'" + sss1[0][43] + "', "
                        + "'" + sss1[0][44] + "', "
                        + "'" + sss1[0][45] + "', "
                        + "'" + sss1[0][46] + "', "
                        + "'" + sss1[0][47] + "', "
                        + "'" + sss1[0][48] + "', "
                        + "'" + sss1[0][49] + "', "
                        + "'" + sss1[0][50] + "', "
                        + "'" + sss1[0][51] + "', "
                        + "'" + sss1[0][52] + "', "
                        + "'" + sss1[0][53] + "', "
                        + "'" + sss1[0][54] + "', "
                        + "'" + sss1[0][55] + "', "
                        + "'" + sss1[0][56] + "', "
                        + "'" + sss1[0][57] + "', "
                        + "'" + sss1[0][58] + "', "
                        + "'" + sss1[0][59] + "', "
                        + "'" + sss1[0][60] + "', "
                        + "'" + sss1[0][61] + "', "
                        + "'" + sss1[0][62] + "', "
                        + "'" + sss1[0][63] + "', "
                        + "'" + sss1[0][64] + "', "
                        + "'" + sss1[0][65] + "' "
                        + ")";
                ExecSQL(ydh, sql);
            } else if (isCover) {
                sql = "update ydyz set "
                        + "ydh = '" + ydh + "', "
                        + "ydlb = '" + sss1[0][1] + "', "
                        + "y = '" + sss1[0][2] + "', "
                        + "x = '" + sss1[0][3] + "', "
                        + "gps_y = '" + sss1[0][4] + "', "
                        + "gps_x = '" + sss1[0][5] + "', "
                        + "xian = '" + sss1[0][6] + "', "
                        + "dm = '" + sss1[0][7] + "', "
                        + "hb = '" + sss1[0][8] + "', "
                        + "px = '" + sss1[0][9] + "', "
                        + "pw = '" + sss1[0][10] + "', "
                        + "pd = '" + sss1[0][11] + "', "
                        + "dbxt = '" + sss1[0][12] + "', "
                        + "sqgd = '" + sss1[0][13] + "', "
                        + "fshd = '" + sss1[0][14] + "', "
                        + "qsgmjbl = '" + sss1[0][15] + "', "
                        + "jyll = '" + sss1[0][16] + "', "
                        + "trmc = '" + sss1[0][17] + "', "
                        + "trzd = '" + sss1[0][18] + "', "
                        + "trlshl = '" + sss1[0][19] + "', "
                        + "tchd = '" + sss1[0][20] + "', "
                        + "fzchd = '" + sss1[0][21] + "', "
                        + "kzlyhd = '" + sss1[0][22] + "', "
                        + "zblx = '" + sss1[0][23] + "', "
                        + "gmfgd = '" + sss1[0][24] + "', "
                        + "gmgd = '" + sss1[0][25] + "', "
                        + "cbfgd = '" + sss1[0][26] + "', "
                        + "cbgd = '" + sss1[0][27] + "', "
                        + "zbzfgd = '" + sss1[0][28] + "', "
                        + "dl = '" + sss1[0][29] + "', "
                        + "tdqs = '" + sss1[0][30] + "', "
                        + "lmqs = '" + sss1[0][31] + "', "
                        + "sllb = '" + sss1[0][32] + "', "
                        + "gylsqdj = '" + sss1[0][33] + "', "
                        + "gylbhdj = '" + sss1[0][34] + "', "
                        + "spljydj = '" + sss1[0][35] + "', "
                        + "fycs = '" + sss1[0][36] + "', "
                        + "linzh = '" + sss1[0][37] + "', "
                        + "qy = '" + sss1[0][38] + "', "
                        + "yssz = '" + sss1[0][39] + "', "
                        + "pjnl = '" + sss1[0][40] + "', "
                        + "lingz = '" + sss1[0][41] + "', "
                        + "cq = '" + sss1[0][42] + "', "
                        + "pjxj = '" + sss1[0][43] + "', "
                        + "pjsg = '" + sss1[0][44] + "', "
                        + "ybd = '" + sss1[0][45] + "', "
                        + "slqljg = '" + sss1[0][46] + "', "
                        + "lcjg = '" + sss1[0][47] + "', "
                        + "szjg = '" + sss1[0][48] + "', "
                        + "zrd = '" + sss1[0][49] + "', "
                        + "kjd = '" + sss1[0][50] + "', "
                        + "slzhlx = '" + sss1[0][51] + "', "
                        + "slzhdj = '" + sss1[0][52] + "', "
                        + "sljkdj = '" + sss1[0][53] + "', "
                        + "spszs = '" + sss1[0][54] + "', "
                        + "zzzs = '" + sss1[0][55] + "', "
                        + "trgxdj = '" + sss1[0][56] + "', "
                        + "dlmjdj = '" + sss1[0][57] + "', "
                        + "dlbhyy = '" + sss1[0][58] + "', "
                        + "ywtsdd = '" + sss1[0][59] + "', "
                        + "dcrq = '" + sss1[0][60] + "', "
                        + "f1 = '" + sss1[0][61] + "', "
                        + "f2 = '" + sss1[0][62] + "', "
                        + "f3 = '" + sss1[0][63] + "', "
                        + "f4 = '" + sss1[0][64] + "', "
                        + "f5 = '" + sss1[0][65] + "' "
                        + " where ydh = '" + ydh + "'";
                ExecSQL(ydh, sql);
            }
        }
    }

    public static void CombineKjl(String inFile, boolean isCover, int ydh) {
        String sql = "select * from kjl where ydh = '" + ydh + "'";
        String[][] sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            for (int i = 0; i < sss1.length; i++) {
                if (isCover) {
                    sql = "delete from kjl where ydh = '" + ydh + "' and xh = '" + sss1[i][1] + "'";
                    ExecSQL(ydh, sql);
                    sql = "insert into kjl values("
                            + "'" + sss1[i][0] + "', "
                            + "'" + sss1[i][1] + "', "
                            + "'" + sss1[i][2] + "', "
                            + "'" + sss1[i][3] + "', "
                            + "'" + sss1[i][4] + "', "
                            + "'" + sss1[i][5] + "', "
                            + "'" + sss1[i][6] + "', "
                            + "'" + sss1[i][7] + "', "
                            + "'" + sss1[i][8] + "', "
                            + "'" + sss1[i][9] + "', "
                            + "'" + sss1[i][10] + "', "
                            + "'" + sss1[i][11] + "', "
                            + "'" + sss1[i][12] + "', "
                            + "'" + sss1[i][13] + "', "
                            + "'" + sss1[i][14] + "'"
                            + ")";
                    ExecSQL(ydh, sql);
                } else {
                    sql = "select ydh from kjl where ydh = '" + ydh + "' and xh = '" + sss1[i][1] + "'";
                    String[][] sss0 = SelectData(ydh, sql);
                    if (sss0 == null) {
                        sql = "insert into kjl values("
                                + "'" + sss1[i][0] + "', "
                                + "'" + sss1[i][1] + "', "
                                + "'" + sss1[i][2] + "', "
                                + "'" + sss1[i][3] + "', "
                                + "'" + sss1[i][4] + "', "
                                + "'" + sss1[i][5] + "', "
                                + "'" + sss1[i][6] + "', "
                                + "'" + sss1[i][7] + "', "
                                + "'" + sss1[i][8] + "', "
                                + "'" + sss1[i][9] + "', "
                                + "'" + sss1[i][10] + "', "
                                + "'" + sss1[i][11] + "', "
                                + "'" + sss1[i][12] + "', "
                                + "'" + sss1[i][13] + "', "
                                + "'" + sss1[i][14] + "'"
                                + ")";
                        ExecSQL(ydh, sql);
                    }
                }
            }
        }

        if (isCover) {
            sql = "delete from sgcl_kjl where ydh = '" + ydh + "'";
            ExecSQL(ydh, sql);

            sql = "select * from sgcl_kjl where ydh = '" + ydh + "'";
            sss1 = selectData(inFile, sql);
            if (sss1 != null) {
                for (int i = 0; i < sss1.length; i++) {
                    sql = "insert into sgcl_kjl values("
                            + "'" + sss1[i][0] + "', "
                            + "'" + sss1[i][1] + "', "
                            + "'" + sss1[i][2] + "', "
                            + "'" + sss1[i][3] + "', "
                            + "'" + sss1[i][4] + "', "
                            + "'" + sss1[i][5] + "', "
                            + "'" + sss1[i][6] + "', "
                            + "'" + sss1[i][7] + "' "
                            + ")";
                    ExecSQL(ydh, sql);
                }
            }

            sql = "select * from tag where ydh = '" + ydh + "'";
            sss1 = selectData(inFile, sql);
            sql = "update tag set "
                    + "f2 = '" + sss1[0][2] + "', "
                    + "f3 = '" + sss1[0][3] + "', "
                    + "f4 = '" + sss1[0][4] + "', "
                    + "f16 = '" + sss1[0][16] + "', "
                    + "f17 = '" + sss1[0][17] + "', "
                    + "f18 = '" + sss1[0][18] + "' "
                    + " where ydh = '" + ydh + "'";
            ExecSQL(ydh, sql);
        }
    }

    public static void CombineMmjc(String inFile, boolean isCover, int ydh) {
        String sql = "select * from mmjc where ydh = '" + ydh + "' and jczt = '1'";
        String[][] sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            for (int i = 0; i < sss1.length; i++) {
                if (isCover) {
                    sql = "delete from mmjc where ydh = '" + ydh + "' and ymh = '" + sss1[i][1] + "'";
                    ExecSQL(ydh, sql);
                    sql = "insert into mmjc values("
                            + "'" + sss1[i][0] + "', "
                            + "'" + sss1[i][1] + "', "
                            + "'" + sss1[i][2] + "', "
                            + "'" + sss1[i][3] + "', "
                            + "'" + sss1[i][4] + "', "
                            + "'" + sss1[i][5] + "', "
                            + "'" + sss1[i][6] + "', "
                            + "'" + sss1[i][7] + "', "
                            + "'" + sss1[i][8] + "', "
                            + "'" + sss1[i][9] + "', "
                            + "'" + sss1[i][10] + "', "
                            + "'" + sss1[i][11] + "', "
                            + "'" + sss1[i][12] + "', "
                            + "'" + sss1[i][13] + "', "
                            + "'" + sss1[i][14] + "', "
                            + "'" + sss1[i][15] + "', "
                            + "'" + sss1[i][16] + "'"
                            + ")";
                    ExecSQL(ydh, sql);
                } else {
                    sql = "select ydh from mmjc where ydh = '" + ydh + "' and ymh = '" + sss1[i][1] + "' and jczt = '0'";
                    String[][] sss0 = SelectData(ydh, sql);
                    if (sss0 != null) {
                        sql = "delete from mmjc where ydh = '" + ydh + "' and ymh = '" + sss1[i][1] + "'";
                        ExecSQL(ydh, sql);
                    }
                    sql = "select ydh from mmjc where ydh = '" + ydh + "' and ymh = '" + sss1[i][1] + "' and jczt = '1'";
                    sss0 = SelectData(ydh, sql);
                    if (sss0 == null) {
                        sql = "insert into mmjc values("
                                + "'" + sss1[i][0] + "', "
                                + "'" + sss1[i][1] + "', "
                                + "'" + sss1[i][2] + "', "
                                + "'" + sss1[i][3] + "', "
                                + "'" + sss1[i][4] + "', "
                                + "'" + sss1[i][5] + "', "
                                + "'" + sss1[i][6] + "', "
                                + "'" + sss1[i][7] + "', "
                                + "'" + sss1[i][8] + "', "
                                + "'" + sss1[i][9] + "', "
                                + "'" + sss1[i][10] + "', "
                                + "'" + sss1[i][11] + "', "
                                + "'" + sss1[i][12] + "', "
                                + "'" + sss1[i][13] + "', "
                                + "'" + sss1[i][14] + "', "
                                + "'" + sss1[i][15] + "', "
                                + "'" + sss1[i][16] + "'"
                                + ")";
                        ExecSQL(ydh, sql);
                    }
                }
            }
        }
    }

    public static void CombineYmwzt(String inFile, boolean isCover, int ydh) {
        String sql = "select ydh from qt where ydh = '" + ydh + "'";
        String[][] sss0 = SelectData(ydh, sql);
        sql = "select ifnull(guding,'') as guding, ifnull(yf,'') as yf from qt where ydh = '" + ydh + "'";
        String[][] sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            if (sss0 == null) {
                sql = "insert into qt(ydh, guding, yf) values("
                        + "'" + ydh + "', "
                        + "'" + sss1[0][0] + "', "
                        + "'" + sss1[0][1] + "' "
                        + ")";
                ExecSQL(ydh, sql);
            } else if (isCover) {
                sql = "update qt set "
                        + "guding = '" + sss1[0][0] + "', "
                        + "yf = '" + sss1[0][1] + "' "
                        + " where ydh = '" + ydh + "'";
                ExecSQL(ydh, sql);
            }
        }

        sql = "select ifnull(yangmutu,'') as yangmutu, ifnull(ymtditu,'') as ymtditu from qt where ydh = '" + ydh + "'";
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(inFile, null);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                byte[] blob1 = cursor.getBlob(0);
                byte[] blob2 = cursor.getBlob(1);
                cursor.close();
                sql = "update qt set yangmutu = ?, ymtditu = ? where ydh = '" + ydh + "'";
                String dbFile = getDbFile(ydh);
                SQLiteDatabase db2 = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
                db2.execSQL(sql, new Object[]{blob1, blob2});
                db2.close();
            }
            db.close();
        } catch (Exception e) {
        }
    }

    public static void CombineSgcl(String inFile, boolean isCover, int ydh) {
        String sql = "select * from sgcl where ydh = '" + ydh + "'";
        String[][] sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            for (int i = 0; i < sss1.length; i++) {
                if (isCover) {
                    sql = "delete from sgcl where ydh = '" + ydh + "' and ymh = '" + sss1[i][2] + "'";
                    ExecSQL(ydh, sql);
                    sql = "insert into sgcl values("
                            + "'" + sss1[i][0] + "', "
                            + "'" + sss1[i][1] + "', "
                            + "'" + sss1[i][2] + "', "
                            + "'" + sss1[i][3] + "', "
                            + "'" + sss1[i][4] + "', "
                            + "'" + sss1[i][5] + "', "
                            + "'" + sss1[i][6] + "' "
                            + ")";
                    ExecSQL(ydh, sql);
                } else {
                    sql = "select ydh from sgcl where ydh = '" + ydh + "' and ymh = '" + sss1[i][2] + "'";
                    String[][] sss0 = SelectData(ydh, sql);
                    if (sss0 == null) {
                        sql = "insert into sgcl values("
                                + "'" + sss1[i][0] + "', "
                                + "'" + sss1[i][1] + "', "
                                + "'" + sss1[i][2] + "', "
                                + "'" + sss1[i][3] + "', "
                                + "'" + sss1[i][4] + "', "
                                + "'" + sss1[i][5] + "', "
                                + "'" + sss1[i][6] + "' "
                                + ")";
                        ExecSQL(ydh, sql);
                    }
                }
            }
        }
    }

    public static void CombineSlzh(String inFile, boolean isCover, int ydh) {
        String sql = "select * from slzh where ydh = '" + ydh + "'";
        String[][] sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            for (int i = 0; i < sss1.length; i++) {
                if (isCover) {
                    sql = "delete from slzh where ydh = '" + ydh + "' and zhlx = '" + sss1[i][2] + "'";
                    ExecSQL(ydh, sql);
                    sql = "insert into slzh values("
                            + "'" + sss1[i][0] + "', "
                            + "'" + sss1[i][1] + "', "
                            + "'" + sss1[i][2] + "', "
                            + "'" + sss1[i][3] + "', "
                            + "'" + sss1[i][4] + "', "
                            + "'" + sss1[i][5] + "' "
                            + ")";
                    ExecSQL(ydh, sql);
                } else {
                    sql = "select ydh from slzh where ydh = '" + ydh + "' and zhlx = '" + sss1[i][2] + "'";
                    String[][] sss0 = SelectData(ydh, sql);
                    if (sss0 == null) {
                        sql = "insert into slzh values("
                                + "'" + sss1[i][0] + "', "
                                + "'" + sss1[i][1] + "', "
                                + "'" + sss1[i][2] + "', "
                                + "'" + sss1[i][3] + "', "
                                + "'" + sss1[i][4] + "', "
                                + "'" + sss1[i][5] + "' "
                                + ")";
                        ExecSQL(ydh, sql);
                    }
                }
            }
        }
    }

    public static void CombineZbdc(String inFile, boolean isCover, int ydh) {
        String sql = "select * from zbdc where ydh = '" + ydh + "'";
        String[][] sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            for (int i = 0; i < sss1.length; i++) {
                if (isCover) {
                    sql = "delete from zbdc where ydh = '" + ydh + "' and zblx = '" + sss1[i][2] + "' and mc = '" + sss1[i][3] + "'";
                    ExecSQL(ydh, sql);
                    sql = "insert into zbdc values("
                            + "'" + sss1[i][0] + "', "
                            + "'" + sss1[i][1] + "', "
                            + "'" + sss1[i][2] + "', "
                            + "'" + sss1[i][3] + "', "
                            + "'" + sss1[i][4] + "', "
                            + "'" + sss1[i][5] + "', "
                            + "'" + sss1[i][6] + "', "
                            + "'" + sss1[i][7] + "' "
                            + ")";
                    ExecSQL(ydh, sql);
                } else {
                    sql = "select ydh from zbdc where ydh = '" + ydh + "' and zblx = '" + sss1[i][2] + "' and mc = '" + sss1[i][3] + "'";
                    String[][] sss0 = SelectData(ydh, sql);
                    if (sss0 == null) {
                        sql = "insert into zbdc values("
                                + "'" + sss1[i][0] + "', "
                                + "'" + sss1[i][1] + "', "
                                + "'" + sss1[i][2] + "', "
                                + "'" + sss1[i][3] + "', "
                                + "'" + sss1[i][4] + "', "
                                + "'" + sss1[i][5] + "', "
                                + "'" + sss1[i][6] + "', "
                                + "'" + sss1[i][7] + "' "
                                + ")";
                        ExecSQL(ydh, sql);
                    }
                }
            }
        }
    }

    public static void CombineXmdc(String inFile, boolean isCover, int ydh) {
        String sql = "select * from xmdc where ydh = '" + ydh + "'";
        String[][] sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            for (int i = 0; i < sss1.length; i++) {
                if (isCover) {
                    sql = "delete from xmdc where ydh = '" + ydh + "' and mc = '" + sss1[i][2] + "'";
                    ExecSQL(ydh, sql);
                    sql = "insert into xmdc values("
                            + "'" + sss1[i][0] + "', "
                            + "'" + sss1[i][1] + "', "
                            + "'" + sss1[i][2] + "', "
                            + "'" + sss1[i][3] + "', "
                            + "'" + sss1[i][4] + "' "
                            + ")";
                    ExecSQL(ydh, sql);
                } else {
                    sql = "select ydh from xmdc where ydh = '" + ydh + "' and mc = '" + sss1[i][2] + "'";
                    String[][] sss0 = SelectData(ydh, sql);
                    if (sss0 == null) {
                        sql = "insert into xmdc values("
                                + "'" + sss1[i][0] + "', "
                                + "'" + sss1[i][1] + "', "
                                + "'" + sss1[i][2] + "', "
                                + "'" + sss1[i][3] + "', "
                                + "'" + sss1[i][4] + "' "
                                + ")";
                        ExecSQL(ydh, sql);
                    }
                }
            }
        }
    }

    public static void CombineTrgx(String inFile, boolean isCover, int ydh) {
        String sql = "select * from trgx where ydh = '" + ydh + "'";
        String[][] sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            for (int i = 0; i < sss1.length; i++) {
                if (isCover) {
                    sql = "delete from trgx where ydh = '" + ydh + "' and sz = '" + sss1[i][2] + "'";
                    ExecSQL(ydh, sql);
                    sql = "insert into trgx values("
                            + "'" + sss1[i][0] + "', "
                            + "'" + sss1[i][1] + "', "
                            + "'" + sss1[i][2] + "', "
                            + "'" + sss1[i][3] + "', "
                            + "'" + sss1[i][4] + "', "
                            + "'" + sss1[i][5] + "', "
                            + "'" + sss1[i][6] + "', "
                            + "'" + sss1[i][7] + "' "
                            + ")";
                    ExecSQL(ydh, sql);
                } else {
                    sql = "select ydh from trgx where ydh = '" + ydh + "' and sz = '" + sss1[i][2] + "'";
                    String[][] sss0 = SelectData(ydh, sql);
                    if (sss0 == null) {
                        sql = "insert into trgx values("
                                + "'" + sss1[i][0] + "', "
                                + "'" + sss1[i][1] + "', "
                                + "'" + sss1[i][2] + "', "
                                + "'" + sss1[i][3] + "', "
                                + "'" + sss1[i][4] + "', "
                                + "'" + sss1[i][5] + "', "
                                + "'" + sss1[i][6] + "', "
                                + "'" + sss1[i][7] + "' "
                                + ")";
                        ExecSQL(ydh, sql);
                    }
                }
            }
        }
    }

    public static void CombineYdbh(String inFile, boolean isCover, int ydh) {
        String sql = "select ydh from ydbh where ydh = '" + ydh + "'";
        String[][] sss0 = SelectData(ydh, sql);
        sql = "select dlbhyy, linzhbhyy, qybhyy, ysszbhyy, lingzbhyy, zblxbhyy, bz from ydbh where ydh = '" + ydh + "'";
        String[][] sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            if (sss0 == null) {
                sql = "insert into ydbh(ydh, dlbhyy, linzhbhyy, qybhyy, ysszbhyy, lingzbhyy, zblxbhyy, bz) values("
                        + "'" + ydh + "', "
                        + "'" + sss1[0][0] + "', "
                        + "'" + sss1[0][1] + "', "
                        + "'" + sss1[0][2] + "', "
                        + "'" + sss1[0][3] + "', "
                        + "'" + sss1[0][4] + "', "
                        + "'" + sss1[0][5] + "', "
                        + "'" + sss1[0][6] + "' "
                        + ")";
                ExecSQL(ydh, sql);
            } else if (isCover) {
                sql = "update ydbh set "
                        + "dlbhyy = '" + sss1[0][0] + "', "
                        + "linzhbhyy = '" + sss1[0][1] + "', "
                        + "qybhyy = '" + sss1[0][2] + "', "
                        + "ysszbhyy = '" + sss1[0][3] + "', "
                        + "lingzbhyy = '" + sss1[0][4] + "', "
                        + "zblxbhyy = '" + sss1[0][5] + "', "
                        + "bz = '" + sss1[0][6] + "' "
                        + " where ydh = '" + ydh + "'";
                ExecSQL(ydh, sql);
            }
        }
    }

    public static void CombineWclzld(String inFile, boolean isCover, int ydh) {
        String sql = "select ydh from wclzld where ydh = '" + ydh + "'";
        String[][] sss0 = SelectData(ydh, sql);
        sql = "select * from wclzld where ydh = '" + ydh + "'";
        String[][] sss1 = selectData(inFile, sql);
        if (sss1 != null) {
            if (sss0 == null) {
                sql = "insert into wclzld values("
                        + "'" + sss1[0][0] + "', "
                        + "'" + sss1[0][1] + "', "
                        + "'" + sss1[0][2] + "', "
                        + "'" + sss1[0][3] + "', "
                        + "'" + sss1[0][4] + "', "
                        + "'" + sss1[0][5] + "', "
                        + "'" + sss1[0][6] + "', "
                        + "'" + sss1[0][7] + "', "
                        + "'" + sss1[0][8] + "', "
                        + "'" + sss1[0][9] + "', "
                        + "'" + sss1[0][10] + "', "
                        + "'" + sss1[0][11] + "', "
                        + "'" + sss1[0][12] + "' "
                        + ")";
                ExecSQL(ydh, sql);
            } else if (isCover) {
                sql = "update wclzld set "
                        + "ydh = '" + sss1[0][0] + "', "
                        + "wclzldqk = '" + sss1[0][1] + "', "
                        + "zlnd = '" + sss1[0][2] + "', "
                        + "ml = '" + sss1[0][3] + "', "
                        + "czmd = '" + sss1[0][4] + "', "
                        + "mmchl = '" + sss1[0][5] + "', "
                        + "gg = '" + sss1[0][6] + "', "
                        + "bz = '" + sss1[0][7] + "', "
                        + "sf = '" + sss1[0][8] + "', "
                        + "fy = '" + sss1[0][9] + "', "
                        + "gh = '" + sss1[0][10] + "', "
                        + "sz = '" + sss1[0][11] + "', "
                        + "szbl = '" + sss1[0][12] + "' "
                        + " where ydh = '" + ydh + "'";
                ExecSQL(ydh, sql);
            }
        }
    }

    public static void CombineYdzp(String inFile, boolean isCover, int ydh) {
        try {
            String sql = "select * from zp where ydh = '" + ydh + "'";
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(inFile, null);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    int v1 = cursor.getInt(0);
                    int v2 = cursor.getInt(2);
                    int v3 = cursor.getInt(3);
                    String v4 = cursor.getString(4);
                    String v5 = cursor.getString(5);
                    byte[] v6 = cursor.getBlob(6);
                    cursor.moveToNext();
                    sql = "insert into zp(ydh, type, ymh, name, notes, zp) values('" + v1 + "', '" + v2 + "', '" + v3 + "', '" + v4 + "', '" + v5 + "', ?)";
                    String dbFile = getDbFile(ydh);
                    SQLiteDatabase db2 = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
                    db2.execSQL(sql, new Object[]{v6});
                    db2.close();
                }
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void CombineTrack(String inFile, boolean isCover, int ydh) {
        String sql = "select name from track where ydh = '" + ydh + "' group by name";
        String[][] sss0 = SelectData(ydh, sql);
        if (sss0 == null) {
            sql = "select * from track where ydh = '" + ydh + "'";
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(inFile, null);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    int v1 = cursor.getInt(0);
                    String v2 = cursor.getString(1);
                    double v3 = cursor.getDouble(2);
                    double v4 = cursor.getDouble(3);
                    String v5 = cursor.getString(4);
                    cursor.moveToNext();
                    sql = "insert into track values("
                            + "'" + v1 + "', "
                            + "'" + v2 + "', "
                            + "'" + v3 + "', "
                            + "'" + v4 + "', "
                            + "'" + v5 + "' "
                            + ")";
                    ExecSQL(ydh, sql);
                }
                cursor.close();
            }
            db.close();
        } else {
            for (int i = 0; i < sss0.length; i++) {
                sql = "update track set name = '" + sss0[0][i] + "_hb" + "' where name = '" + sss0[0][i] + "'";
                try {
                    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(inFile, null);
                    db.execSQL(sql);
                    db.close();
                } catch (Exception e) {
                }
            }

            sql = "select * from track where ydh = '" + ydh + "'";
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(inFile, null);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    int v1 = cursor.getInt(0);
                    String v2 = cursor.getString(1);
                    double v3 = cursor.getDouble(2);
                    double v4 = cursor.getDouble(3);
                    String v5 = cursor.getString(4);
                    cursor.moveToNext();
                    sql = "insert into track values("
                            + "'" + v1 + "', "
                            + "'" + v2 + "', "
                            + "'" + v3 + "', "
                            + "'" + v4 + "', "
                            + "'" + v5 + "' "
                            + ")";
                    ExecSQL(ydh, sql);
                }
                cursor.close();
            }
            db.close();
        }

        sql = "select wgs84_x, wgs84_y from info where ydh = '" + ydh + "'";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(inFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            double v1 = cursor.getDouble(0);
            double v2 = cursor.getDouble(1);
            sql = "update info set wgs84_x = '" + v1 + "', wgs84_y = '" + v2 + "' where ydh = '" + ydh + "'";
            ExecSQL(ydh, sql);
            cursor.close();
        }
        db.close();
    }


    public static int GetQqdl(int ydh) {
        String sql = "select dl from ydyz_old where ydh = '" + ydh + "'";
        return QueryInt(ydh, sql);
    }

    public static int DlCodeOldToNew(int dl) {
        //if(dl == 112) dl = 111;
        //if(dl == 118) dl = 111;
        //if(dl == 1131) dl = 113;
        //if(dl == 142) dl = 141;
        //if(dl == 180) dl = 173;
        //if(dl == 171) dl = 173;
        return dl;
    }


    public static float GetJinjiemuSize(int ydh) {
        String sql = "select max(xj) from mmjc_old where ydh = '" + ydh + "' and jclx = '12'";
        return QueryFloat(ydh, sql);
    }

    public static int GetMaxYmhQq(int ydh) {
        String sql = "select max(ymh) from mmjc_old where ydh = '" + ydh + "'";
        int r = QueryInt(ydh, sql);
        if (r == -1) r = 0;
        return r;
    }

    public static Yangmu GetQqYangmu(int ydh, int ymh) {
        String sql = "select * from mmjc_old where ydh = '" + ydh + "' and ymh = '" + ymh + "'";
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        String[] ss = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }
            ss = new String[m];
            cursor.moveToFirst();
            for (int j = 0; j < m; j++) {
                ss[j] = cursor.getString(j);
            }
            cursor.close();
        }
        db.close();

        if (ss != null) {
            Yangmu ym = new Yangmu();
            try {
                ym.ymh = Integer.parseInt(ss[1]);
            } catch (Exception e) {
            }
            try {
                ym.lmlx = Integer.parseInt(ss[2]);
            } catch (Exception e) {
            }
            try {
                ym.jclx = Integer.parseInt(ss[3]);
            } catch (Exception e) {
            }
            try {
                ym.szdm = Integer.parseInt(ss[4]);
                ym.szmc = Resmgr.GetSzName(ym.szdm);
            } catch (Exception e) {
            }
            try {
                ym.bqxj = Float.parseFloat(ss[5]);
            } catch (Exception e) {
            }
            try {
                ym.cfgllx = Integer.parseInt(ss[6]);
            } catch (Exception e) {
            }
            try {
                ym.lc = Integer.parseInt(ss[7]);
            } catch (Exception e) {
            }
            try {
                ym.kjdlxh = Integer.parseInt(ss[8]);
            } catch (Exception e) {
            }
            try {
                ym.fwj = Float.parseFloat(ss[9]);
            } catch (Exception e) {
            }
            try {
                ym.spj = Float.parseFloat(ss[10]);
            } catch (Exception e) {
            }
            return ym;
        }

        return null;
    }

    public static String[] GetQqYdyz(int ydh) {
        String sql = "select * from ydyz_old where ydh = '" + ydh + "'";
        String[][] sss = SelectData(ydh, sql);
        if (sss != null) {
            return sss[0];
        }
        return null;
    }

    public static boolean IsHaveYindiandww(int ydh, Yddww item) {
        String sql = "select * from yindiandww where ydh = '" + ydh + "' "
                + "and mc = '" + item.mc + "' "
                + "and fwj = '" + item.fwj + "' "
                + "and spj = '" + item.spj + "' ";
        String[][] sss = SelectData(ydh, sql);
        return sss != null;
    }

    public static boolean IsHaveYangdidww(int ydh, Yddww item) {
        String sql = "select * from yangdidww where ydh = '" + ydh + "' "
                + "and mc = '" + item.mc + "' "
                + "and fwj = '" + item.fwj + "' "
                + "and spj = '" + item.spj + "' ";
        String[][] sss = SelectData(ydh, sql);
        return sss != null;
    }

    public static boolean IsHaveYxcl(int ydh, Cljl item) {
        String sql = "select * from yxcl where ydh = '" + ydh + "' "
                + "and cz = '" + item.cz + "' "
                + "and fwj = '" + item.fwj + "' "
                + "and qxj = '" + item.qxj + "' "
                + "and xj = '" + item.xj + "' "
                + "and spj = '" + item.spj + "' "
                + "and lj = '" + item.lj + "' ";
        String[][] sss = SelectData(ydh, sql);
        return sss != null;
    }

    public static boolean IsHaveZjcl(int ydh, Cljl item) {
        String sql = "select * from Zjcl where ydh = '" + ydh + "' "
                + "and cz = '" + item.cz + "' "
                + "and fwj = '" + item.fwj + "' "
                + "and qxj = '" + item.qxj + "' "
                + "and xj = '" + item.xj + "' "
                + "and spj = '" + item.spj + "' "
                + "and lj = '" + item.lj + "' ";
        String[][] sss = SelectData(ydh, sql);
        return sss != null;
    }

    public static boolean IsHaveSgcl(int ydh, Sgcl item) {
        String sql = "select * from sgcl where ydh = '" + ydh + "' "
                + "and ymh = '" + item.ymh + "' ";
        String[][] sss = SelectData(ydh, sql);
        return sss != null;
    }

    public static boolean IsHaveSlzh(int ydh, Slzh item) {
        String sql = "select * from slzh where ydh = '" + ydh + "' "
                + "and zhlx = '" + item.zhlx + "' ";
        String[][] sss = SelectData(ydh, sql);
        return sss != null;
    }

    public static boolean IsHaveSlzh(int ydh, int zhlx) {
        String sql = "select * from slzh where ydh = '" + ydh + "' "
                + "and zhlx = '" + zhlx + "' ";
        String[][] sss = SelectData(ydh, sql);
        return sss != null;
    }

    public static boolean IsHaveZbdc(int ydh, Zb item) {
        String sql = "select * from zbdc where ydh = '" + ydh + "' "
                + "and zblx = '" + item.zblx + "' "
                + "and mc = '" + item.mc + "' ";
        String[][] sss = SelectData(ydh, sql);
        return sss != null;
    }

    public static boolean IsHaveXmdc(int ydh, Xm item) {
        String sql = "select * from xmdc where ydh = '" + ydh + "' "
                + "and mc = '" + item.mc + "' ";
        String[][] sss = SelectData(ydh, sql);
        return sss != null;
    }

    public static boolean IsHaveTrgx(int ydh, Trgx item) {
        String sql = "select * from yxcl where ydh = '" + ydh + "' "
                + "and sz = '" + item.sz + "' ";
        String[][] sss = SelectData(ydh, sql);
        return sss != null;
    }


    //
    public static void ExportToExcel(String db, String xls, int ydh) {
        MyFile.DeleteFile(xls);
        String srcFile = MyConfig.GetAppdir() + "/yangdi.xls";
        try {
            MyFile.CopyFile(srcFile, xls);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File xlsFile = new File(xls);
        try {
            Workbook wb = Workbook.getWorkbook(xlsFile);
            WritableWorkbook book = Workbook.createWorkbook(xlsFile, wb);

            WritableFont wf11 = new WritableFont(WritableFont.createFont("宋体"), 11);
            WritableCellFormat wcf11 = new WritableCellFormat(wf11);
            wcf11.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            wcf11.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableFont wf8 = new WritableFont(WritableFont.createFont("宋体"), 8);
            WritableCellFormat wcf8 = new WritableCellFormat(wf8);
            wcf8.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            wcf8.setBorder(Border.ALL, BorderLineStyle.THIN);

            //卡片封面
            WritableSheet sheet = book.getSheet(0);
            String sql = "select * from kpfm where ydh = '" + ydh + "'";
            String[][] sss = YangdiMgr.SelectData(ydh, sql);
            if (sss != null) {
                String[] ss = sss[0];
                Label c0 = new Label(1, 1, ss[0], wcf11);//总体名称
                sheet.addCell(c0);
                Label c1 = new Label(3, 1, ss[1], wcf11);//样地号
                sheet.addCell(c1);
                Label c2 = new Label(1, 2, ss[2], wcf11);//样地形状
                sheet.addCell(c2);
                Label c3 = new Label(3, 2, ss[3], wcf11);//样地面积
                sheet.addCell(c3);
                Label c4 = new Label(1, 3, ss[4], wcf11);//纵坐标
                sheet.addCell(c4);
                Label c5 = new Label(1, 4, ss[5], wcf11);//横坐标
                sheet.addCell(c5);
                Label c8 = new Label(3, 3, ss[6], wcf11);//样地间距
                sheet.addCell(c8);
                Label c9 = new Label(3, 4, ss[10], wcf11);//林业行政编码
                sheet.addCell(c9);
                Label c6 = new Label(1, 5, ss[7], wcf11);//地形图图幅号
                sheet.addCell(c6);
                Label c7 = new Label(3, 5, ss[9], wcf11);//地方行政编码
                sheet.addCell(c7);

                Label c11 = new Label(1, 7, ss[11], wcf11);
                sheet.addCell(c11);
                Label c12 = new Label(1, 8, ss[12], wcf11);
                sheet.addCell(c12);
                Label c13 = new Label(1, 9, ss[13], wcf11);
                sheet.addCell(c13);
                Label c14 = new Label(1, 10, ss[14], wcf11);
                sheet.addCell(c14);
                Label c15 = new Label(1, 11, ss[15], wcf11);
                sheet.addCell(c15);
                Label c16 = new Label(3, 7, ss[16], wcf11);
                sheet.addCell(c16);
                Label c17 = new Label(3, 8, ss[17], wcf11);
                sheet.addCell(c17);
                Label c18 = new Label(3, 9, ss[18], wcf11);
                sheet.addCell(c18);
                Label c19 = new Label(3, 10, ss[19], wcf11);
                sheet.addCell(c19);
                Label c20 = new Label(3, 11, ss[20], wcf11);
                sheet.addCell(c20);
                Label c21 = new Label(1, 18, ss[21], wcf11);
                sheet.addCell(c21);
                Label c22 = new Label(3, 18, ss[22], wcf11);
                sheet.addCell(c22);
                Label c23 = new Label(1, 21, ss[23], wcf11);
                sheet.addCell(c23);
            }
            sql = "select name, company from worker where ydh = '" + ydh + "' and type = 0";
            sss = YangdiMgr.SelectData(ydh, sql);
            if (sss != null) {
                if (sss.length >= 1) {
                    Label c0 = new Label(1, 13, sss[0][0], wcf11);
                    sheet.addCell(c0);
                    Label c1 = new Label(3, 13, sss[0][1], wcf11);
                    sheet.addCell(c1);
                }
                if (sss.length >= 2) {
                    Label c0 = new Label(1, 14, sss[1][0], wcf11);
                    sheet.addCell(c0);
                    Label c1 = new Label(3, 14, sss[1][1], wcf11);
                    sheet.addCell(c1);
                }
                if (sss.length >= 3) {
                    Label c0 = new Label(1, 15, sss[2][0], wcf11);
                    sheet.addCell(c0);
                    Label c1 = new Label(3, 15, sss[2][1], wcf11);
                    sheet.addCell(c1);
                }
                if (sss.length >= 4) {
                    Label c0 = new Label(1, 16, sss[3][0], wcf11);
                    sheet.addCell(c0);
                    Label c1 = new Label(3, 16, sss[3][1], wcf11);
                    sheet.addCell(c1);
                }
                if (sss.length >= 5) {
                    Label c0 = new Label(1, 17, sss[4][0], wcf11);
                    sheet.addCell(c0);
                    Label c1 = new Label(3, 17, sss[4][1], wcf11);
                    sheet.addCell(c1);
                }
            }

            sql = "select name, company from worker where ydh = '" + ydh + "' and type = 1";
            sss = YangdiMgr.SelectData(ydh, sql);
            if (sss != null) {
                if (sss.length >= 1) {
                    Label c0 = new Label(1, 19, sss[0][0], wcf11);
                    sheet.addCell(c0);
                    Label c1 = new Label(3, 19, sss[0][1], wcf11);
                    sheet.addCell(c1);
                }
                if (sss.length >= 2) {
                    Label c0 = new Label(1, 20, sss[1][0], wcf11);
                    sheet.addCell(c0);
                    Label c1 = new Label(3, 20, sss[1][1], wcf11);
                    sheet.addCell(c1);
                }
            }

            sql = "select name, company from worker where ydh = '" + ydh + "' and type = 2";
            sss = YangdiMgr.SelectData(ydh, sql);
            if (sss != null) {
                if (sss.length >= 1) {
                    Label c0 = new Label(1, 18, sss[0][0], wcf11);
                    sheet.addCell(c0);
                    Label c1 = new Label(3, 18, sss[0][1], wcf11);
                    sheet.addCell(c1);
                }
            }

            String jcrq = YangdiMgr.GetJcrq(ydh);
            if (jcrq != null) {
                Label c0 = new Label(3, 21, jcrq, wcf11);
                sheet.addCell(c0);
            }

            //样地定位与测设
            WritableSheet sheet1 = book.getSheet(1);
            sql = "select cfsj, zdsj from qt where ydh = '" + ydh + "'";
            sss = YangdiMgr.SelectData(ydh, sql);
            if (sss != null) {
                Label c0 = new Label(3, 1, sss[0][0], wcf8);
                sheet1.addCell(c0);
                Label c1 = new Label(10, 1, sss[0][1], wcf8);
                sheet1.addCell(c1);
            }
            sql = "select zbfwj, cfwj, yxjl, lc, yindian, yangdi from qt where ydh = '" + ydh + "'";
            sss = YangdiMgr.SelectData(ydh, sql);
            if (sss != null) {
                Label c0 = new Label(2, 3, sss[0][0], wcf8);
                sheet1.addCell(c0);
                Label c1 = new Label(5, 3, sss[0][1], wcf8);
                sheet1.addCell(c1);
                Label c2 = new Label(2, 4, sss[0][2], wcf8);
                sheet1.addCell(c2);
                Label c3 = new Label(5, 4, sss[0][3], wcf8);
                sheet1.addCell(c3);
                Label c4 = new Label(0, 14, sss[0][4], wcf8);
                sheet1.addCell(c4);
                Label c5 = new Label(7, 14, sss[0][5], wcf8);
                sheet1.addCell(c5);
            }
            sql = "select * from yindiandww where ydh = '" + ydh + "'";
            sss = YangdiMgr.SelectData(ydh, sql);
            if (sss != null) {
                if (sss.length >= 1) {
                    Label c0 = new Label(2, 7, sss[0][2], wcf8);
                    sheet1.addCell(c0);
                    Label c1 = new Label(3, 7, sss[0][3], wcf8);
                    sheet1.addCell(c1);
                    Label c2 = new Label(4, 7, sss[0][4], wcf8);
                    sheet1.addCell(c2);
                    Label c3 = new Label(5, 7, sss[0][5], wcf8);
                    sheet1.addCell(c3);
                }
                if (sss.length >= 2) {
                    Label c0 = new Label(2, 8, sss[1][2], wcf8);
                    sheet1.addCell(c0);
                    Label c1 = new Label(3, 8, sss[1][3], wcf8);
                    sheet1.addCell(c1);
                    Label c2 = new Label(4, 8, sss[1][4], wcf8);
                    sheet1.addCell(c2);
                    Label c3 = new Label(5, 8, sss[1][5], wcf8);
                    sheet1.addCell(c3);
                }
                if (sss.length >= 3) {
                    Label c0 = new Label(2, 9, sss[2][2], wcf8);
                    sheet1.addCell(c0);
                    Label c1 = new Label(3, 9, sss[2][3], wcf8);
                    sheet1.addCell(c1);
                    Label c2 = new Label(4, 9, sss[2][4], wcf8);
                    sheet1.addCell(c2);
                    Label c3 = new Label(5, 9, sss[2][5], wcf8);
                    sheet1.addCell(c3);
                }
                if (sss.length >= 4) {
                    Label c0 = new Label(2, 10, sss[3][2], wcf8);
                    sheet1.addCell(c0);
                    Label c1 = new Label(3, 10, sss[3][3], wcf8);
                    sheet1.addCell(c1);
                    Label c2 = new Label(4, 10, sss[3][4], wcf8);
                    sheet1.addCell(c2);
                    Label c3 = new Label(5, 10, sss[3][5], wcf8);
                    sheet1.addCell(c3);
                }
                if (sss.length >= 5) {
                    Label c0 = new Label(2, 11, sss[4][2], wcf8);
                    sheet1.addCell(c0);
                    Label c1 = new Label(3, 11, sss[4][3], wcf8);
                    sheet1.addCell(c1);
                    Label c2 = new Label(4, 11, sss[4][4], wcf8);
                    sheet1.addCell(c2);
                    Label c3 = new Label(5, 11, sss[4][5], wcf8);
                    sheet1.addCell(c3);
                }
            }
            sql = "select * from yangdidww where ydh = '" + ydh + "'";
            sss = YangdiMgr.SelectData(ydh, sql);
            if (sss != null) {
                if (sss.length >= 1) {
                    Label c0 = new Label(9, 7, sss[0][2], wcf8);
                    sheet1.addCell(c0);
                    Label c1 = new Label(10, 7, sss[0][3], wcf8);
                    sheet1.addCell(c1);
                    Label c2 = new Label(11, 7, sss[0][4], wcf8);
                    sheet1.addCell(c2);
                    Label c3 = new Label(12, 7, sss[0][5], wcf8);
                    sheet1.addCell(c3);
                }
                if (sss.length >= 2) {
                    Label c0 = new Label(9, 8, sss[1][2], wcf8);
                    sheet1.addCell(c0);
                    Label c1 = new Label(10, 8, sss[1][3], wcf8);
                    sheet1.addCell(c1);
                    Label c2 = new Label(11, 8, sss[1][4], wcf8);
                    sheet1.addCell(c2);
                    Label c3 = new Label(12, 8, sss[1][5], wcf8);
                    sheet1.addCell(c3);
                }
                if (sss.length >= 3) {
                    Label c0 = new Label(9, 9, sss[2][2], wcf8);
                    sheet1.addCell(c0);
                    Label c1 = new Label(10, 9, sss[2][3], wcf8);
                    sheet1.addCell(c1);
                    Label c2 = new Label(11, 9, sss[2][4], wcf8);
                    sheet1.addCell(c2);
                    Label c3 = new Label(12, 9, sss[2][5], wcf8);
                    sheet1.addCell(c3);
                }
                if (sss.length >= 4) {
                    Label c0 = new Label(9, 10, sss[3][2], wcf8);
                    sheet1.addCell(c0);
                    Label c1 = new Label(10, 10, sss[3][3], wcf8);
                    sheet1.addCell(c1);
                    Label c2 = new Label(11, 10, sss[3][4], wcf8);
                    sheet1.addCell(c2);
                    Label c3 = new Label(12, 10, sss[3][5], wcf8);
                    sheet1.addCell(c3);
                }
                if (sss.length >= 5) {
                    Label c0 = new Label(9, 11, sss[4][2], wcf8);
                    sheet1.addCell(c0);
                    Label c1 = new Label(10, 11, sss[4][3], wcf8);
                    sheet1.addCell(c1);
                    Label c2 = new Label(11, 11, sss[4][4], wcf8);
                    sheet1.addCell(c2);
                    Label c3 = new Label(12, 11, sss[4][5], wcf8);
                    sheet1.addCell(c3);
                }
            }
            String pic1 = xls + "_1.png";
            Bitmap bmp1 = YangdiMgr.GetYindiantu(ydh);
            if (bmp1 != null) {
                try {
                    FileOutputStream outStream = new FileOutputStream(pic1);
                    bmp1.compress(CompressFormat.PNG, 100, outStream);
                    outStream.close();
                    bmp1.recycle();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File f1 = new File(pic1);
                WritableImage image = new WritableImage(0, 5, 6, 1, f1);
                sheet1.addImage(image);
            }
            String pic2 = xls + "_2.png";
            Bitmap bmp2 = YangdiMgr.GetYangditu(ydh);
            if (bmp2 != null) {
                try {
                    FileOutputStream outStream = new FileOutputStream(pic2);
                    bmp2.compress(CompressFormat.PNG, 100, outStream);
                    outStream.close();
                    bmp2.recycle();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File f2 = new File(pic2);
                WritableImage image = new WritableImage(7, 5, 6, 1, f2);
                sheet1.addImage(image);
            }

            double[] dd = GetYdloc22(ydh);
            if (dd != null) {
                String slon = MyFuns.NumberToDegree(dd[0]);
                String slat = MyFuns.NumberToDegree(dd[1]);
                int hzb = (int) dd[2];
                int zzb = (int) dd[3];
                Label c0 = new Label(3, 15, slon, wcf8);
                sheet1.addCell(c0);
                Label c1 = new Label(10, 15, slat, wcf8);
                sheet1.addCell(c1);
                Label c2 = new Label(3, 16, String.valueOf(hzb), wcf8);
                sheet1.addCell(c2);
                Label c3 = new Label(10, 16, String.valueOf(zzb), wcf8);
                sheet1.addCell(c3);
            }

            List<Cljl> lst = YangdiMgr.GetYxclList(ydh);
            int a = 21;
            for (int i = 0; i < lst.size(); i++) {
                if (i < 4) {
                    Label c0 = new Label(0, a + i, lst.get(i).cz, wcf8);
                    sheet1.addCell(c0);
                    Label c1 = new Label(1, a + i, String.valueOf(lst.get(i).fwj), wcf8);
                    sheet1.addCell(c1);
                    Label c2 = new Label(2, a + i, String.valueOf(lst.get(i).qxj), wcf8);
                    sheet1.addCell(c2);
                    Label c3 = new Label(3, a + i, String.valueOf(lst.get(i).xj), wcf8);
                    sheet1.addCell(c3);
                    Label c4 = new Label(4, a + i, String.valueOf(lst.get(i).spj), wcf8);
                    sheet1.addCell(c4);
                    Label c5 = new Label(5, a + i, String.valueOf(lst.get(i).lj), wcf8);
                    sheet1.addCell(c5);
                } else if (i < 8) {
                    Label c0 = new Label(7, a + i - 10, lst.get(i).cz, wcf8);
                    sheet1.addCell(c0);
                    Label c1 = new Label(8, a + i - 10, String.valueOf(lst.get(i).fwj), wcf8);
                    sheet1.addCell(c1);
                    Label c2 = new Label(9, a + i - 10, String.valueOf(lst.get(i).qxj), wcf8);
                    sheet1.addCell(c2);
                    Label c3 = new Label(10, a + i - 10, String.valueOf(lst.get(i).xj), wcf8);
                    sheet1.addCell(c3);
                    Label c4 = new Label(11, a + i - 10, String.valueOf(lst.get(i).spj), wcf8);
                    sheet1.addCell(c4);
                    Label c5 = new Label(12, a + i - 10, String.valueOf(lst.get(i).lj), wcf8);
                    sheet1.addCell(c5);
                }
            }

            lst.clear();
            lst = YangdiMgr.GetZjclList(ydh);
            a = 27;
            for (int i = 0; i < lst.size(); i++) {
                if (i < 6) {
                    Label c0 = new Label(0, a + i, lst.get(i).cz, wcf8);
                    sheet1.addCell(c0);
                    Label c1 = new Label(1, a + i, String.valueOf(lst.get(i).fwj), wcf8);
                    sheet1.addCell(c1);
                    Label c2 = new Label(2, a + i, String.valueOf(lst.get(i).qxj), wcf8);
                    sheet1.addCell(c2);
                    Label c3 = new Label(3, a + i, String.valueOf(lst.get(i).xj), wcf8);
                    sheet1.addCell(c3);
                    Label c4 = new Label(4, a + i, String.valueOf(lst.get(i).spj), wcf8);
                    sheet1.addCell(c4);
                    Label c5 = new Label(5, a + i, String.valueOf(lst.get(i).lj), wcf8);
                    sheet1.addCell(c5);
                } else if (i < 11) {
                    Label c0 = new Label(7, a + i - 10, lst.get(i).cz, wcf8);
                    sheet1.addCell(c0);
                    Label c1 = new Label(8, a + i - 10, String.valueOf(lst.get(i).fwj), wcf8);
                    sheet1.addCell(c1);
                    Label c2 = new Label(9, a + i - 10, String.valueOf(lst.get(i).qxj), wcf8);
                    sheet1.addCell(c2);
                    Label c3 = new Label(10, a + i - 10, String.valueOf(lst.get(i).xj), wcf8);
                    sheet1.addCell(c3);
                    Label c4 = new Label(11, a + i - 10, String.valueOf(lst.get(i).spj), wcf8);
                    sheet1.addCell(c4);
                    Label c5 = new Label(12, a + i - 10, String.valueOf(lst.get(i).lj), wcf8);
                    sheet1.addCell(c5);
                }
            }

            sql = "select jdbhc, xdbhc, zcwc from qt where ydh = '" + ydh + "'";
            sss = YangdiMgr.SelectData(ydh, sql);
            if (sss != null) {
                int type = YangdiMgr.GetZjclType(ydh);
                if (type == 0) {
                    Label c1 = new Label(9, 32, sss[0][1], wcf8);
                    sheet1.addCell(c1);
                } else {
                    Label c2 = new Label(12, 32, sss[0][2], wcf8);
                    sheet1.addCell(c2);
                }
            }

            //样地因子 + 跨角林
            WritableSheet sheet2 = book.getSheet(2);
            sql = "select * from ydyz where ydh = '" + ydh + "'";
            sss = YangdiMgr.SelectData(ydh, sql);
            if (sss != null) {
                String[] ss = sss[0];
                int code = -1;
                float f = -1;
                String value = "";
                String[] aa = null;
                String s1 = "";
                String s2 = "";
                //样地号
                code = -1;
                try {
                    code = Integer.parseInt(ss[0]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(4, 1, ss[0], wcf8);
                    sheet2.addCell(c0);
                }
                //样地类别
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[1]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("ydlb", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(4, 2, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(8, 2, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //纵坐标
                code = -1;
                try {
                    code = Integer.parseInt(ss[2]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(4, 3, ss[2], wcf8);
                    sheet2.addCell(c0);
                }
                //横坐标
                code = -1;
                try {
                    code = Integer.parseInt(ss[3]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(4, 4, ss[3], wcf8);
                    sheet2.addCell(c0);
                }
                //GPS纵坐标
                code = -1;
                try {
                    code = Integer.parseInt(ss[4]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(4, 5, ss[4], wcf8);
                    sheet2.addCell(c0);
                }
                //GPS横坐标
                code = -1;
                try {
                    code = Integer.parseInt(ss[5]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(4, 6, ss[5], wcf8);
                    sheet2.addCell(c0);
                }
                //县代码
                code = -1;
                try {
                    code = Integer.parseInt(ss[6]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(4, 7, ss[6], wcf8);
                    sheet2.addCell(c0);
                }
                //地貌
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[7]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("dm", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(4, 8, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(8, 8, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //海拔
                code = -1;
                try {
                    code = Integer.parseInt(ss[8]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(4, 9, ss[8], wcf8);
                    sheet2.addCell(c0);
                }
                //坡向
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[9]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("px", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(4, 10, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(8, 10, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //坡位
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[10]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("pw", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(4, 11, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(8, 11, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //坡度
                code = -1;
                try {
                    code = Integer.parseInt(ss[11]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(4, 12, ss[11], wcf8);
                    sheet2.addCell(c0);
                }
                //地表形态
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[12]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("dbxt", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(4, 13, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(8, 13, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //沙丘高度
                f = -1;
                try {
                    f = Float.parseFloat(ss[13]);
                } catch (Exception e) {
                }
                if (f >= 0) {
                    Label c0 = new Label(4, 14, ss[13], wcf8);
                    sheet2.addCell(c0);
                }
                //覆沙厚度
                code = -1;
                try {
                    code = Integer.parseInt(ss[14]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(4, 15, ss[14], wcf8);
                    sheet2.addCell(c0);
                }
                //侵蚀沟面积比例
                code = -1;
                try {
                    code = Integer.parseInt(ss[15]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(4, 16, ss[15], wcf8);
                    sheet2.addCell(c0);
                }
                //基岩裸露
                code = -1;
                try {
                    code = Integer.parseInt(ss[16]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(4, 17, ss[16], wcf8);
                    sheet2.addCell(c0);
                }
                //土壤名称
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[17]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("trmc", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(4, 18, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(8, 18, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //土壤质地
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[18]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("trzd", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(4, 19, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(8, 19, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //土壤砾石含量
                code = -1;
                try {
                    code = Integer.parseInt(ss[19]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(4, 20, ss[19], wcf8);
                    sheet2.addCell(c0);
                }
                //土壤厚度
                code = -1;
                try {
                    code = Integer.parseInt(ss[20]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(4, 21, ss[20], wcf8);
                    sheet2.addCell(c0);
                }
                //腐殖质厚度
                code = -1;
                try {
                    code = Integer.parseInt(ss[21]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(16, 1, ss[21], wcf8);
                    sheet2.addCell(c0);
                }
                //枯枝落叶厚度
                code = -1;
                try {
                    code = Integer.parseInt(ss[22]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(16, 2, ss[22], wcf8);
                    sheet2.addCell(c0);
                }
                //植被类型
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[23]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("zblx", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(16, 3, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(20, 3, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //灌木覆盖度
                code = -1;
                try {
                    code = Integer.parseInt(ss[24]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(16, 4, ss[24], wcf8);
                    sheet2.addCell(c0);
                }
                //灌木高度
                f = -1;
                try {
                    f = Float.parseFloat(ss[25]);
                } catch (Exception e) {
                }
                if (f >= 0) {
                    Label c0 = new Label(16, 5, ss[25], wcf8);
                    sheet2.addCell(c0);
                }
                //草本覆盖度
                code = -1;
                try {
                    code = Integer.parseInt(ss[26]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(16, 6, ss[26], wcf8);
                    sheet2.addCell(c0);
                }
                //草本高度
                f = -1;
                try {
                    f = Float.parseFloat(ss[27]);
                } catch (Exception e) {
                }
                if (f >= 0) {
                    Label c0 = new Label(16, 7, ss[27], wcf8);
                    sheet2.addCell(c0);
                }
                //植被总覆盖度
                code = -1;
                try {
                    code = Integer.parseInt(ss[28]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(16, 8, ss[28], wcf8);
                    sheet2.addCell(c0);
                }
                //地类
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[29]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("dl", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(16, 9, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(20, 9, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //土地权属
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[30]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("tdqs", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(16, 10, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(20, 10, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //林木权属
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[31]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("lmqs", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(16, 11, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(20, 11, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //森林类别
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[32]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("sllb", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(16, 12, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(20, 12, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //公益林事权等级
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[33]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("gylsqdj", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(16, 13, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(20, 13, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //公益林保护等级
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[34]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("gylbhdj", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(16, 14, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(20, 14, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //商品林经营等级
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[35]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("spljydj", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(16, 15, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(20, 15, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //抚育措施
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[36]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("fycs", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(16, 16, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(20, 16, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //林种
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[37]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("linzh", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(16, 17, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(20, 17, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //起源
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[38]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("qy", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(16, 18, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(20, 18, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //优势树种
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[39]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetSzName(code);
                    Label c1 = new Label(16, 19, ss[39], wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(20, 19, value, wcf8);
                    sheet2.addCell(c2);
                }
                //植被总覆盖度
                code = -1;
                try {
                    code = Integer.parseInt(ss[40]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(16, 20, ss[40], wcf8);
                    sheet2.addCell(c0);
                }
                //龄组
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[41]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("lingz", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(16, 21, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(20, 21, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //产期
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[42]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("cq", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(28, 1, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(32, 1, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //平均胸径
                f = -1;
                try {
                    f = Float.parseFloat(ss[43]);
                } catch (Exception e) {
                }
                if (f >= 0) {
                    Label c0 = new Label(28, 2, ss[43], wcf8);
                    sheet2.addCell(c0);
                }
                //平均树高
                f = -1;
                try {
                    f = Float.parseFloat(ss[44]);
                } catch (Exception e) {
                }
                if (f >= 0) {
                    Label c0 = new Label(28, 3, ss[44], wcf8);
                    sheet2.addCell(c0);
                }
                //郁闭度
                f = -1;
                try {
                    f = Float.parseFloat(ss[45]);
                } catch (Exception e) {
                }
                if (f >= 0) {
                    Label c0 = new Label(28, 4, ss[45], wcf8);
                    sheet2.addCell(c0);
                }

                //森林群落结构
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[46]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("slqljg", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(28, 5, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(32, 5, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //林层结构
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[47]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("ydlcjg", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(28, 6, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(32, 6, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //树种结构
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[48]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("szjg", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(28, 7, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(32, 7, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //自然度
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[49]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("zrd", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(28, 8, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(32, 8, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //可及度
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[50]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("kjd", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(28, 9, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(32, 9, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //森林灾害类型
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[51]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("slzhlx", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(28, 10, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(32, 10, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //森林灾害等级
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[52]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("slzhdj", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(28, 11, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(32, 11, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //森林健康等级
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[53]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("sljkdj", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(28, 12, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(32, 12, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //四旁树株数
                code = -1;
                try {
                    code = Integer.parseInt(ss[54]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(28, 13, ss[54], wcf8);
                    sheet2.addCell(c0);
                }
                //杂竹株数
                code = -1;
                try {
                    code = Integer.parseInt(ss[55]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    Label c0 = new Label(28, 14, ss[55], wcf8);
                    sheet2.addCell(c0);
                }
                //天然更新等级
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[56]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("trgxdj", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(28, 15, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(32, 15, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //地类面积等级
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[57]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("dlmjdj", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(28, 16, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(32, 16, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //地类变化原因
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[58]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("dlbhyy", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(28, 17, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(32, 17, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //有无特殊对待
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[59]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("ywtsdd", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(28, 18, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(32, 18, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //调查日期
                Label c0 = new Label(28, 19, ss[60], wcf8);
                sheet2.addCell(c0);
                //是否农田经济林
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[61]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("sfntjjl", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(28, 20, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(32, 20, s2, wcf8);
                    sheet2.addCell(c2);
                }
                //是否可变林地
                code = -1;
                s1 = "";
                s2 = "";
                try {
                    code = Integer.parseInt(ss[62]);
                } catch (Exception e) {
                }
                if (code >= 0) {
                    value = Resmgr.GetValueByCode("sffldsl", code);
                    aa = MyFuns.Split(value, ' ');
                    if (aa.length > 1) {
                        s1 = aa[0];
                        s2 = aa[1];
                    }
                    Label c1 = new Label(28, 21, s1, wcf8);
                    sheet2.addCell(c1);
                    Label c2 = new Label(32, 21, s2, wcf8);
                    sheet2.addCell(c2);
                }
            }

            Kjl kjl = YangdiMgr.GetKjl(ydh, 1);
            if (kjl != null) {
                Label c0 = new Label(3, 24, String.valueOf(ydh), wcf8);
                sheet2.addCell(c0);
                Label c1 = new Label(3, 25, String.valueOf(kjl.xh), wcf8);
                sheet2.addCell(c1);
                Label c2 = new Label(3, 26, String.valueOf(kjl.mjbl), wcf8);
                if (kjl.mjbl > 0) sheet2.addCell(c2);
                Label c3 = new Label(3, 27, Resmgr.GetValueByCode("dl", kjl.dl), wcf8);
                sheet2.addCell(c3);
                Label c4 = new Label(3, 28, Resmgr.GetValueByCode("tdqs", kjl.tdqs), wcf8);
                sheet2.addCell(c4);
                Label c5 = new Label(15, 24, Resmgr.GetValueByCode("lmqs", kjl.lmqs), wcf8);
                sheet2.addCell(c5);
                Label c6 = new Label(15, 25, Resmgr.GetValueByCode("linzh", kjl.linzh), wcf8);
                sheet2.addCell(c6);
                Label c7 = new Label(15, 26, Resmgr.GetValueByCode("qy", kjl.qy), wcf8);
                sheet2.addCell(c7);
                Label c8 = new Label(15, 27, kjl.yssz + " " + Resmgr.GetSzName(kjl.yssz), wcf8);
                if (kjl.yssz > 0) sheet2.addCell(c8);
                Label c9 = new Label(15, 28, Resmgr.GetValueByCode("lingz", kjl.lingz), wcf8);
                sheet2.addCell(c9);
                Label c10 = new Label(27, 24, String.valueOf(kjl.ybd), wcf8);
                if (kjl.ybd > 0) sheet2.addCell(c10);
                Label c11 = new Label(27, 25, String.valueOf(kjl.pjsg), wcf8);
                if (kjl.pjsg > 0) sheet2.addCell(c11);
                Label c12 = new Label(27, 26, Resmgr.GetValueByCode("slqljg", kjl.slqljg), wcf8);
                sheet2.addCell(c12);
                Label c13 = new Label(27, 27, Resmgr.GetValueByCode("szjg", kjl.szjg), wcf8);
                sheet2.addCell(c13);
                Label c14 = new Label(27, 28, Resmgr.GetValueByCode("spljydj", kjl.spljydj), wcf8);
                sheet2.addCell(c14);
            }
            kjl = YangdiMgr.GetKjl(ydh, 2);
            if (kjl != null) {
                Label c0 = new Label(6, 24, String.valueOf(ydh), wcf8);
                sheet2.addCell(c0);
                Label c1 = new Label(6, 25, String.valueOf(kjl.xh), wcf8);
                sheet2.addCell(c1);
                Label c2 = new Label(6, 26, String.valueOf(kjl.mjbl), wcf8);
                if (kjl.mjbl > 0) sheet2.addCell(c2);
                Label c3 = new Label(6, 27, Resmgr.GetValueByCode("dl", kjl.dl), wcf8);
                sheet2.addCell(c3);
                Label c4 = new Label(6, 28, Resmgr.GetValueByCode("tdqs", kjl.tdqs), wcf8);
                sheet2.addCell(c4);
                Label c5 = new Label(18, 24, Resmgr.GetValueByCode("lmqs", kjl.lmqs), wcf8);
                sheet2.addCell(c5);
                Label c6 = new Label(18, 25, Resmgr.GetValueByCode("linzh", kjl.linzh), wcf8);
                sheet2.addCell(c6);
                Label c7 = new Label(18, 26, Resmgr.GetValueByCode("qy", kjl.qy), wcf8);
                sheet2.addCell(c7);
                Label c8 = new Label(18, 27, kjl.yssz + " " + Resmgr.GetSzName(kjl.yssz), wcf8);
                if (kjl.yssz > 0) sheet2.addCell(c8);
                Label c9 = new Label(18, 28, Resmgr.GetValueByCode("lingz", kjl.lingz), wcf8);
                sheet2.addCell(c9);
                Label c10 = new Label(30, 24, String.valueOf(kjl.ybd), wcf8);
                if (kjl.ybd > 0) sheet2.addCell(c10);
                Label c11 = new Label(30, 25, String.valueOf(kjl.pjsg), wcf8);
                if (kjl.pjsg > 0) sheet2.addCell(c11);
                Label c12 = new Label(30, 26, Resmgr.GetValueByCode("slqljg", kjl.slqljg), wcf8);
                sheet2.addCell(c12);
                Label c13 = new Label(30, 27, Resmgr.GetValueByCode("szjg", kjl.szjg), wcf8);
                sheet2.addCell(c13);
                Label c14 = new Label(30, 28, Resmgr.GetValueByCode("spljydj", kjl.spljydj), wcf8);
                sheet2.addCell(c14);
            }
            kjl = YangdiMgr.GetKjl(ydh, 3);
            if (kjl != null) {
                Label c0 = new Label(9, 24, String.valueOf(ydh), wcf8);
                sheet2.addCell(c0);
                Label c1 = new Label(9, 25, String.valueOf(kjl.xh), wcf8);
                sheet2.addCell(c1);
                Label c2 = new Label(9, 26, String.valueOf(kjl.mjbl), wcf8);
                if (kjl.mjbl > 0) sheet2.addCell(c2);
                Label c3 = new Label(9, 27, Resmgr.GetValueByCode("dl", kjl.dl), wcf8);
                sheet2.addCell(c3);
                Label c4 = new Label(9, 28, Resmgr.GetValueByCode("tdqs", kjl.tdqs), wcf8);
                sheet2.addCell(c4);
                Label c5 = new Label(21, 24, Resmgr.GetValueByCode("lmqs", kjl.lmqs), wcf8);
                sheet2.addCell(c5);
                Label c6 = new Label(21, 25, Resmgr.GetValueByCode("linzh", kjl.linzh), wcf8);
                sheet2.addCell(c6);
                Label c7 = new Label(21, 26, Resmgr.GetValueByCode("qy", kjl.qy), wcf8);
                sheet2.addCell(c7);
                Label c8 = new Label(21, 27, kjl.yssz + " " + Resmgr.GetSzName(kjl.yssz), wcf8);
                if (kjl.yssz > 0) sheet2.addCell(c8);
                Label c9 = new Label(21, 28, Resmgr.GetValueByCode("lingz", kjl.lingz), wcf8);
                sheet2.addCell(c9);
                Label c10 = new Label(33, 24, String.valueOf(kjl.ybd), wcf8);
                if (kjl.ybd > 0) sheet2.addCell(c10);
                Label c11 = new Label(33, 25, String.valueOf(kjl.pjsg), wcf8);
                if (kjl.pjsg > 0) sheet2.addCell(c11);
                Label c12 = new Label(33, 26, Resmgr.GetValueByCode("slqljg", kjl.slqljg), wcf8);
                sheet2.addCell(c12);
                Label c13 = new Label(33, 27, Resmgr.GetValueByCode("szjg", kjl.szjg), wcf8);
                sheet2.addCell(c13);
                Label c14 = new Label(33, 28, Resmgr.GetValueByCode("spljydj", kjl.spljydj), wcf8);
                sheet2.addCell(c14);
            }

            //每木检尺
            WritableSheet sheet3 = book.getSheet(3);
            List<Yangmu> lstYms = YangdiMgr.GetYangmus(ydh);
            for (int i = 0; i < lstYms.size(); i++) {
                Yangmu ym = lstYms.get(i);
                Label c0 = new Label(0, 3 + i, String.valueOf(ym.ymh), wcf8);
                sheet3.addCell(c0);
                Label c1 = new Label(1, 3 + i, String.valueOf(ym.lmlx), wcf8);//Resmgr.GetValueByCode("lmlx", ym.lmlx)
                sheet3.addCell(c1);
                Label c2 = new Label(2, 3 + i, String.valueOf(ym.jclx), wcf8);//Resmgr.GetValueByCode("jclx", ym.jclx)
                sheet3.addCell(c2);
                Label c3 = new Label(3, 3 + i, ym.szmc, wcf8);
                sheet3.addCell(c3);
                Label c4 = new Label(4, 3 + i, String.valueOf(ym.szdm), wcf8);
                if (ym.szdm > 0) sheet3.addCell(c4);
                Label c5 = new Label(5, 3 + i, String.valueOf(ym.qqxj), wcf8);
                if (ym.qqxj > 0) sheet3.addCell(c5);
                Label c6 = new Label(6, 3 + i, String.valueOf(ym.bqxj), wcf8);
                if (ym.bqxj > 0) sheet3.addCell(c6);
                Label c7 = new Label(7, 3 + i, String.valueOf(ym.cfgllx), wcf8);//Resmgr.GetValueByCode("cfgllx", ym.cfgllx)
                sheet3.addCell(c7);
                Label c8 = new Label(8, 3 + i, String.valueOf(ym.lc), wcf8);//Resmgr.GetValueByCode("ymlcjg", ym.lc)
                sheet3.addCell(c8);
                Label c9 = new Label(9, 3 + i, String.valueOf(ym.kjdlxh), wcf8);
                if (ym.kjdlxh > 0) sheet3.addCell(c9);
                Label c10 = new Label(10, 3 + i, String.valueOf(ym.fwj), wcf8);
                sheet3.addCell(c10);
                Label c11 = new Label(11, 3 + i, String.valueOf(ym.spj), wcf8);
                sheet3.addCell(c11);
                Label c12 = new Label(12, 3 + i, ym.bz, wcf8);
                sheet3.addCell(c12);
            }

            //样木位置图、树高测量
            WritableSheet sheet4 = book.getSheet(4);
            String pic3 = xls + "_3.png";
            Bitmap bmp3 = YangdiMgr.GetYangmutu(ydh);
            if (bmp3 != null) {
                try {
                    FileOutputStream outStream = new FileOutputStream(pic3);
                    bmp3.compress(CompressFormat.PNG, 100, outStream);
                    outStream.close();
                    bmp3.recycle();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File file = new File(pic3);
                WritableImage image = new WritableImage(0, 2, 11, 2, file);
                sheet4.addImage(image);
            }
            sql = "select guding from qt where ydh = '" + ydh + "'";
            sss = YangdiMgr.SelectData(ydh, sql);
            if (sss != null) {
                Label c0 = new Label(0, 5, sss[0][0], wcf11);
                sheet4.addCell(c0);
            }

            List<Sgcl> lstSgcl = YangdiMgr.GetSgclList(ydh);
            float pjsg = 0;
            float pjzxg = 0;
            for (int i = 0; i < lstSgcl.size(); i++) {
                Sgcl sgcl = lstSgcl.get(i);
                pjsg += sgcl.sg;
                pjzxg += sgcl.zxg;
                if (i < 3) {
                    Label c0 = new Label(0, 9 + i, String.valueOf(sgcl.ymh), wcf11);
                    sheet4.addCell(c0);
                    Label c1 = new Label(1, 9 + i, Resmgr.GetSzName(sgcl.sz), wcf11);
                    sheet4.addCell(c1);
                    Label c2 = new Label(2, 9 + i, String.valueOf(sgcl.xj), wcf11);
                    sheet4.addCell(c2);
                    Label c3 = new Label(3, 9 + i, String.valueOf(sgcl.sg), wcf11);
                    sheet4.addCell(c3);
                    //Label c4 = new Label(4, 9+i, String.valueOf(sgcl.bz), wcf11);
                    //sheet4.addCell(c4);
                }
            }
            if (lstSgcl.size() > 0) {
                pjsg /= lstSgcl.size();
                pjzxg /= lstSgcl.size();
                pjsg = MyFuns.MyDecimal(pjsg, 1);
                pjzxg = MyFuns.MyDecimal(pjzxg, 1);
                Label c1 = new Label(3, 12, String.valueOf(pjsg), wcf11);
                sheet4.addCell(c1);
            }


            //其他
            WritableSheet sheet5 = book.getSheet(5);
            //森林灾害
            List<Slzh> lstSlzh = YangdiMgr.GetSlzhList(ydh);
            for (int i = 0; i < lstSlzh.size(); i++) {
                if (i >= 5) break;
                Slzh slzh = lstSlzh.get(i);
                Label c0 = new Label(0, 2 + i, String.valueOf(slzh.xh), wcf11);
                sheet5.addCell(c0);
                Label c1 = new Label(2, 2 + i, Resmgr.GetValueByCode("slzhlx", slzh.zhlx), wcf11);
                sheet5.addCell(c1);
                Label c2 = new Label(4, 2 + i, slzh.whbw, wcf11);
                sheet5.addCell(c2);
                Label c3 = new Label(6, 2 + i, slzh.szymzs > 0 ? String.valueOf(slzh.szymzs) : "0", wcf11);
                sheet5.addCell(c3);
                Label c4 = new Label(9, 2 + i, Resmgr.GetValueByCode("slzhdj", slzh.szdj), wcf11);
                sheet5.addCell(c4);
            }

            List<Zb> lstZb = YangdiMgr.GetZbList(ydh, 1);
            for (int i = 0; i < lstZb.size(); i++) {
                if (i >= 5) break;
                Zb zb = lstZb.get(i);
                Label c0 = new Label(1 + i, 10, zb.mc, wcf11);
                sheet5.addCell(c0);
                Label c1 = new Label(1 + i, 11, String.valueOf(zb.pjgd), wcf11);
                sheet5.addCell(c1);
                Label c2 = new Label(1 + i, 12, String.valueOf(zb.fgd), wcf11);
                sheet5.addCell(c2);
                Label c3 = new Label(1 + i, 13, String.valueOf(zb.zs), wcf11);
                sheet5.addCell(c3);
                Label c4 = new Label(1 + i, 14, String.valueOf(zb.pjdj), wcf11);
                sheet5.addCell(c4);
            }
            lstZb = YangdiMgr.GetZbList(ydh, 2);
            for (int i = 0; i < lstZb.size(); i++) {
                if (i >= 5) break;
                Zb zb = lstZb.get(i);
                Label c0 = new Label(6 + i, 10, zb.mc, wcf11);
                sheet5.addCell(c0);
                Label c1 = new Label(6 + i, 11, String.valueOf(zb.pjgd), wcf11);
                sheet5.addCell(c1);
                Label c2 = new Label(6 + i, 12, String.valueOf(zb.fgd), wcf11);
                sheet5.addCell(c2);
            }
            lstZb = YangdiMgr.GetZbList(ydh, 3);
            for (int i = 0; i < lstZb.size(); i++) {
                if (i >= 5) break;
                Zb zb = lstZb.get(i);
                Label c0 = new Label(9 + i, 10, zb.mc, wcf11);
                sheet5.addCell(c0);
                Label c1 = new Label(9 + i, 11, String.valueOf(zb.pjgd), wcf11);
                sheet5.addCell(c1);
                Label c2 = new Label(9 + i, 12, String.valueOf(zb.fgd), wcf11);
                sheet5.addCell(c2);
            }

            List<Xm> lstXm = YangdiMgr.GetXmList(ydh);
            for (int i = 0; i < lstXm.size(); i++) {
                Xm xm = lstXm.get(i);
                if (i < 5) {
                    Label c0 = new Label(0, 18 + i, xm.mc, wcf11);
                    sheet5.addCell(c0);
                    Label c1 = new Label(2, 18 + i, String.valueOf(xm.gd), wcf11);
                    sheet5.addCell(c1);
                    Label c2 = new Label(3, 18 + i, String.valueOf(xm.xj), wcf11);
                    sheet5.addCell(c2);
                } else if (i < 10) {
                    Label c0 = new Label(4, 18 + i - 5, xm.mc, wcf11);
                    sheet5.addCell(c0);
                    Label c1 = new Label(6, 18 + i - 5, String.valueOf(xm.gd), wcf11);
                    sheet5.addCell(c1);
                    Label c2 = new Label(7, 18 + i - 5, String.valueOf(xm.xj), wcf11);
                    sheet5.addCell(c2);
                } else if (i < 15) {
                    Label c0 = new Label(8, 18 + i - 10, xm.mc, wcf11);
                    sheet5.addCell(c0);
                    Label c1 = new Label(10, 18 + i - 10, String.valueOf(xm.gd), wcf11);
                    sheet5.addCell(c1);
                    Label c2 = new Label(11, 18 + i - 10, String.valueOf(xm.xj), wcf11);
                    sheet5.addCell(c2);
                }
            }

            List<Trgx> lstTrgx = YangdiMgr.GetTrgxList(ydh);
            for (int i = 0; i < lstTrgx.size(); i++) {
                if (i >= 8) break;
                Trgx trgx = lstTrgx.get(i);
                Label c0 = new Label(0, 27 + i, trgx.sz, wcf11);
                sheet5.addCell(c0);
                Label c1 = new Label(2, 27 + i, String.valueOf(trgx.zs1), wcf11);
                sheet5.addCell(c1);
                Label c2 = new Label(4, 27 + i, String.valueOf(trgx.zs2), wcf11);
                sheet5.addCell(c2);
                Label c3 = new Label(6, 27 + i, String.valueOf(trgx.zs3), wcf11);
                sheet5.addCell(c3);
                Label c4 = new Label(8, 27 + i, trgx.jkzk, wcf11);
                sheet5.addCell(c4);
                Label c5 = new Label(10, 27 + i, trgx.phqk, wcf11);
                sheet5.addCell(c5);
            }


            WritableSheet sheet6 = book.getSheet(6);

            String[] qq = YangdiMgr.GetQqbhyz(ydh);
            String[] bq = YangdiMgr.GetBqbhyz(ydh);
            String[] ydbh = YangdiMgr.GetYdbhyy(ydh);
            if (qq != null) {
                try {
                    int code = -1;
                    code = Integer.parseInt(qq[0]);
                    if (code >= 0) {
                        Label c0 = new Label(3, 2, Resmgr.GetValueByCode("dl", code), wcf11);
                        sheet6.addCell(c0);
                    }
                } catch (Exception e) {
                }
                try {
                    int code = -1;
                    code = Integer.parseInt(qq[1]);
                    if (code >= 0) {
                        Label c0 = new Label(3, 3, Resmgr.GetValueByCode("linzh", code), wcf11);
                        sheet6.addCell(c0);
                    }
                } catch (Exception e) {
                }
                try {
                    int code = -1;
                    code = Integer.parseInt(qq[2]);
                    if (code >= 0) {
                        Label c0 = new Label(3, 4, Resmgr.GetValueByCode("qy", code), wcf11);
                        sheet6.addCell(c0);
                    }
                } catch (Exception e) {
                }
                try {
                    int code = -1;
                    code = Integer.parseInt(qq[3]);
                    if (code >= 0) {
                        Label c0 = new Label(3, 5, code + " " + Resmgr.GetSzName(code), wcf11);
                        sheet6.addCell(c0);
                    }
                } catch (Exception e) {
                }
                try {
                    int code = -1;
                    code = Integer.parseInt(qq[4]);
                    if (code >= 0) {
                        Label c0 = new Label(3, 6, Resmgr.GetValueByCode("lingz", code), wcf11);
                        sheet6.addCell(c0);
                    }
                } catch (Exception e) {
                }
                try {
                    int code = -1;
                    code = Integer.parseInt(qq[5]);
                    if (code >= 0) {
                        Label c0 = new Label(3, 7, Resmgr.GetValueByCode("zblx", code), wcf11);
                        sheet6.addCell(c0);
                    }
                } catch (Exception e) {
                }
            }
            if (bq != null) {
                try {
                    int code = -1;
                    code = Integer.parseInt(bq[0]);
                    if (code >= 0) {
                        Label c0 = new Label(6, 2, Resmgr.GetValueByCode("dl", code), wcf11);
                        sheet6.addCell(c0);
                    }
                } catch (Exception e) {
                }
                try {
                    int code = -1;
                    code = Integer.parseInt(bq[1]);
                    if (code >= 0) {
                        Label c0 = new Label(6, 3, Resmgr.GetValueByCode("linzh", code), wcf11);
                        sheet6.addCell(c0);
                    }
                } catch (Exception e) {
                }
                try {
                    int code = -1;
                    code = Integer.parseInt(bq[2]);
                    if (code >= 0) {
                        Label c0 = new Label(6, 4, Resmgr.GetValueByCode("qy", code), wcf11);
                        sheet6.addCell(c0);
                    }
                } catch (Exception e) {
                }
                try {
                    int code = -1;
                    code = Integer.parseInt(bq[3]);
                    if (code >= 0) {
                        Label c0 = new Label(6, 5, code + " " + Resmgr.GetSzName(code), wcf11);
                        sheet6.addCell(c0);
                    }
                } catch (Exception e) {
                }
                try {
                    int code = -1;
                    code = Integer.parseInt(bq[4]);
                    if (code >= 0) {
                        Label c0 = new Label(6, 6, Resmgr.GetValueByCode("lingz", code), wcf11);
                        sheet6.addCell(c0);
                    }
                } catch (Exception e) {
                }
                try {
                    int code = -1;
                    code = Integer.parseInt(bq[5]);
                    if (code >= 0) {
                        Label c0 = new Label(6, 7, Resmgr.GetValueByCode("zblx", code), wcf11);
                        sheet6.addCell(c0);
                    }
                } catch (Exception e) {
                }
            }
            if (ydbh != null) {
                Label c0 = new Label(9, 2, ydbh[0], wcf11);
                sheet6.addCell(c0);
                Label c1 = new Label(9, 3, ydbh[1], wcf11);
                sheet6.addCell(c1);
                Label c2 = new Label(9, 4, ydbh[2], wcf11);
                sheet6.addCell(c2);
                Label c3 = new Label(9, 5, ydbh[3], wcf11);
                sheet6.addCell(c3);
                Label c4 = new Label(9, 6, ydbh[4], wcf11);
                sheet6.addCell(c4);
                Label c5 = new Label(9, 7, ydbh[5], wcf11);
                sheet6.addCell(c5);
            }

            sql = "select bz from ydbh where ydh = '" + ydh + "'";
            sss = YangdiMgr.SelectData(ydh, sql);
            if (sss != null) {
                Label c0 = new Label(3, 8, sss[0][0], wcf11);
                sheet6.addCell(c0);
            }

            sql = "select * from wclzld where ydh = '" + ydh + "'";
            sss = YangdiMgr.SelectData(ydh, sql);
            if (sss != null) {
                try {
                    int zldqk = -1;
                    zldqk = Integer.parseInt(sss[0][1]);
                    if (zldqk >= 0) {
                        Label c0 = new Label(0, 13, Resmgr.GetValueByCode("wclzldqk", zldqk), wcf11);
                        sheet6.addCell(c0);
                    }
                } catch (Exception e) {
                }
                try {
                    int zlnd = -1;
                    zlnd = Integer.parseInt(sss[0][2]);
                    if (zlnd > 0) {
                        Label c0 = new Label(1, 13, String.valueOf(zlnd), wcf11);
                        sheet6.addCell(c0);
                    }
                } catch (Exception e) {
                }
                try {
                    int ml = -1;
                    ml = Integer.parseInt(sss[0][3]);
                    if (ml > 0) {
                        Label c0 = new Label(2, 13, String.valueOf(ml), wcf11);
                        sheet6.addCell(c0);
                    }
                } catch (Exception e) {
                }
                try {
                    int czmd = -1;
                    czmd = Integer.parseInt(sss[0][4]);
                    if (czmd > 0) {
                        Label c0 = new Label(3, 13, String.valueOf(czmd), wcf11);
                        sheet6.addCell(c0);
                    }
                } catch (Exception e) {
                }
                try {
                    int chl = -1;
                    chl = Integer.parseInt(sss[0][5]);
                    if (chl > 0) {
                        Label c0 = new Label(4, 13, String.valueOf(chl), wcf11);
                        sheet6.addCell(c0);
                    }
                } catch (Exception e) {
                }

                try {
                    int gg = -1;
                    gg = Integer.parseInt(sss[0][6]);
                    Label c0 = new Label(5, 13, gg == 1 ? "有" : "无", wcf11);
                    sheet6.addCell(c0);
                } catch (Exception e) {
                }
                try {
                    int bz = -1;
                    bz = Integer.parseInt(sss[0][7]);
                    Label c0 = new Label(6, 13, bz == 1 ? "有" : "无", wcf11);
                    sheet6.addCell(c0);
                } catch (Exception e) {
                }
                try {
                    int sf = -1;
                    sf = Integer.parseInt(sss[0][8]);
                    Label c0 = new Label(7, 13, sf == 1 ? "有" : "无", wcf11);
                    sheet6.addCell(c0);
                } catch (Exception e) {
                }
                try {
                    int fy = -1;
                    fy = Integer.parseInt(sss[0][9]);
                    Label c0 = new Label(8, 13, fy == 1 ? "有" : "无", wcf11);
                    sheet6.addCell(c0);
                } catch (Exception e) {
                }
                try {
                    int gh = -1;
                    gh = Integer.parseInt(sss[0][10]);
                    Label c0 = new Label(9, 13, gh == 1 ? "有" : "无", wcf11);
                    sheet6.addCell(c0);
                } catch (Exception e) {
                }

                String[] sz = MyFuns.Split(sss[0][11], ',');
                String[] bl = MyFuns.Split(sss[0][12], ',');
                int n = sz.length < bl.length ? sz.length : bl.length;
                for (int i = 0; i < n; i++) {
                    if (i >= 5) break;
                    Label c0 = new Label(10, 13 + i, sz[i], wcf11);
                    sheet6.addCell(c0);
                    Label c1 = new Label(11, 13 + i, bl[i], wcf11);
                    sheet6.addCell(c1);
                }
            }

            sql = "select gps_type,gps_begin,gps_end,gps_dis,tujing,ydphoto,ymphoto,jssj,fhsj from qt where ydh = '" + ydh + "'";
            sss = YangdiMgr.SelectData(ydh, sql);
            if (sss != null) {
                Label c0 = new Label(3, 20, sss[0][0], wcf11);
                sheet6.addCell(c0);
                Label c1 = new Label(3, 21, sss[0][1], wcf11);
                sheet6.addCell(c1);
                Label c2 = new Label(3, 22, sss[0][2], wcf11);
                sheet6.addCell(c2);
                Label c3 = new Label(3, 23, sss[0][3], wcf11);
                sheet6.addCell(c3);
                Label c4 = new Label(3, 24, sss[0][4], wcf11);
                sheet6.addCell(c4);
                Label c5 = new Label(6, 25, sss[0][5], wcf11);
                sheet6.addCell(c5);
                Label c6 = new Label(9, 25, sss[0][6], wcf11);
                sheet6.addCell(c6);
                Label c7 = new Label(3, 26, sss[0][7], wcf11);
                sheet6.addCell(c7);
                Label c8 = new Label(7, 26, sss[0][8], wcf11);
                sheet6.addCell(c8);
                int n = YangdiMgr.GetZpCount(ydh);
                Label c9 = new Label(2, 25, String.valueOf(n), wcf11);
                sheet6.addCell(c9);
            }

            book.write();
            book.close();
            wb.close();
            MyFile.DeleteFile(pic1);
            MyFile.DeleteFile(pic2);
            MyFile.DeleteFile(pic3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int QueryInt(int ydh, String sql) {
        int r = -1;
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            r = cursor.getInt(0);
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }
        return r;
    }

    public static float QueryFloat(int ydh, String sql) {
        float r = -1;
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            r = cursor.getFloat(0);
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }
        return r;
    }

    public static String QueryString(int ydh, String sql) {
        String r = null;
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            r = cursor.getString(0);
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }
        return r;
    }

    public static boolean QueryExists(int ydh, String sql) {
        boolean r = false;
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            cursor = db.rawQuery(sql, null);
            r = cursor.getCount() > 0;
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }
        return r;
    }

    public static String[][] SelectData(int ydh, String sql) {
        String dbFile = getDbFile(ydh);
        SQLiteDatabase db = null;
        try {
            db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        } catch (Exception e) {
        }
        if (db == null) return null;

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor == null) {
            db.close();
            return null;
        }

        String[][] sss = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }
            sss = new String[n][m];
            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                sss[i] = new String[m];
                for (int j = 0; j < m; j++) {
                    sss[i][j] = cursor.getString(j);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return sss;
    }

    public static void ExecSQL(int ydh, String sql) {
        String dbFile = getDbFile(ydh);
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            db.execSQL(sql);
            db.close();
        } catch (Exception e) {
        }
    }


    private static String[][] selectData(String dbFile, String sql) {
        SQLiteDatabase db = null;
        try {
            db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        } catch (Exception e) {
        }
        if (db == null) return null;

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor == null) {
            db.close();
            return null;
        }

        String[][] sss = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }
            sss = new String[n][m];
            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                sss[i] = new String[m];
                for (int j = 0; j < m; j++) {
                    sss[i][j] = cursor.getString(j);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return sss;
    }

    public static String getDbFile(int ydh) {
        return ydDir + "/" + ydh + DATA_EXNAME;
    }


    private static String getStringMd5(String s) {
        try {
            char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte b[] = digest.digest();
            StringBuilder sb = new StringBuilder(b.length * 2);
            for (int i = 0; i < b.length; i++) {
                sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
                sb.append(HEX_DIGITS[b[i] & 0x0f]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getFileMd5_(String file) {
        String value = null;
        try {
            FileInputStream in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    private static String getFileMd5(String file) {
        try {
            char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6',
                    '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            File fp = new File(file);
            FileInputStream in = new FileInputStream(fp);
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, fp.length());
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.update(byteBuffer);
            byte[] bytes = messagedigest.digest();
            StringBuffer stringbuffer = new StringBuffer(2 * bytes.length);
            for (int i = 0; i < bytes.length; i++) {
                char c0 = hexDigits[(bytes[i] & 0xf0) >> 4];
                char c1 = hexDigits[bytes[i] & 0xf];
                stringbuffer.append(c0);
                stringbuffer.append(c1);
            }
            in.close();
            return stringbuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //加密
    public static void encryptFile(String file) throws Exception {
        String dst = file + ".tmp";
        String md5 = getFileMd5(file);
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(dst);
        fos.write(md5.getBytes());
        byte[] bts = new byte[1024];
        int r = fis.read(bts);
        while (r > 0) {
            fos.write(bts, 0, r);
            r = fis.read(bts);
        }

        fis.close();
        fos.close();
        MyFile.DeleteFile(file);
        MyFile.Rename(dst, file);
    }

    //解密
    public static boolean decodeFile(String file) throws Exception {
        String dst = file + ".tmp";
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(dst);
        byte[] bmd5 = new byte[32];
        fis.read(bmd5);
        String md5 = new String(bmd5);
        byte[] bts = new byte[1024];
        int r = fis.read(bts);
        while (r > 0) {
            fos.write(bts, 0, r);
            r = fis.read(bts);
        }

        fis.close();
        fos.close();
        String dstMd5 = getFileMd5(dst);
        if (md5.equals(dstMd5)) {
            MyFile.DeleteFile(file);
            MyFile.Rename(dst, file);
            return true;
        } else {
            MyFile.DeleteFile(dst);
            return false;
        }
    }

    public static String NumberDateToString(int date) {
        int a = date;
        int y = a / 10000;
        y += 2000;
        int m = a % 10000 / 100;
        int d = a % 100;
        return y + "-" + m + "-" + d;
    }

    public static boolean IsValidDate(String str) {
        int a = 0;
        try {
            a = Integer.parseInt(str);
        } catch (Exception e) {
        }

        if (a < 150101) return false;
        if (a > 151230) return false;
        int y = a / 10000;
        int m = a % 10000 / 100;
        int d = a % 100;
        if (y != 15) return false;
        if (m <= 0 || m > 12) return false;
        if (d <= 0 || d > 31) return false;
        if (m == 2 && d > 28) return false;
        if ((m == 4 || m == 6 || m == 9 || m == 11) && d == 31) return false;
        return true;
    }

    public static boolean IsValidTime(String str) {
        String[] ss = MyFuns.Split(str, ':');
        if (ss.length != 2) return false;
        String sh = ss[0];
        String sm = ss[1];
        int h = -1;
        int m = -1;
        try {
            h = Integer.parseInt(sh);
        } catch (Exception e) {
        }
        try {
            m = Integer.parseInt(sm);
        } catch (Exception e) {
        }
        if (h < 0 || h >= 24) return false;
        if (m < 0 || m >= 60) return false;
        return true;
    }

}