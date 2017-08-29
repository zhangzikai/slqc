package com.tdgeos.yangdi;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.tanhui.MyConfig;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Resmgr {
    /****************************** list **********************************/
    public static List<String> GetValueList(String listid) {
        String sql = "select name from list where zu='" + listid + "'";
        String[][] sss = SelectData(sql);
        if (sss != null) {
            List<String> lst = new ArrayList<String>();
            for (int i = 0; i < sss.length; i++) {
                if (sss[i][0] != null) {
                    lst.add(sss[i][0].trim());
                }
            }
            return lst;
        }
        return new ArrayList<String>();
    }

    public static int GetPosByCode(String listid, int code) {
        String sql = "select code from list where zu='" + listid + "'";
        String[][] sss = SelectData(sql);
        if (sss != null) {
            int pos = 0;
            for (int i = 0; i < sss.length; i++) {
                if (sss[i][0] != null) {
                    int k = -1;
                    try {
                        k = Integer.parseInt(sss[i][0].trim());
                    } catch (Exception e) {
                    }
                    if (k == code) {
                        pos = i;
                        break;
                    }
                }
            }
            return pos;
        }
        return 0;
    }

    public static int GetPosByValue(String listid, String value) {
        String sql = "select name from list where zu='" + listid + "'";
        String[][] sss = SelectData(sql);
        if (sss != null) {
            int pos = 0;
            for (int i = 0; i < sss.length; i++) {
                if (sss[i][0] != null && sss[i][0].trim().equals(value)) {
                    pos = i;
                    break;
                }
            }
            return pos;
        }
        return 0;
    }

    public static int GetCodeByValue(String listid, String value) {
        String sql = "select code from list where zu='" + listid + "' and name='" + value + "'";
        String[][] sss = SelectData(sql);
        if (sss != null && sss.length > 0 && sss[0][0] != null) {
            int i = -1;
            try {
                i = Integer.parseInt(sss[0][0]);
            } catch (Exception e) {
            }
            return i;
        }
        return -1;
    }

    public static String GetValueByCode(String listid, int code) {
        String sql = "select name from list where zu='" + listid + "' and code='" + code + "'";
        String[][] sss = SelectData(sql);
        if (sss != null && sss.length > 0 && sss[0][0] != null) {
            return sss[0][0];
        }
        return "";
    }


    public static String GetSzName(int code) {
        String sql = "select name from sz where code = '" + code + "'";
        String dbFile = GetResFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        String str = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return "";
            }

            cursor.moveToFirst();
            str = cursor.getString(0);
            cursor.close();
        }
        db.close();
        if (str == null) str = "";
        return str;
    }

    public static int GetSzCode(String name) {
        String sql = "select code from sz where name = '" + name + "'";
        String dbFile = GetResFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        String str = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return -1;
            }

            cursor.moveToFirst();
            str = cursor.getString(0);
            cursor.close();
        }
        db.close();
        int code = -1;
        try {
            code = Integer.parseInt(str);
        } catch (Exception e) {
        }
        return code;
    }

    public static int GetSzNewCodeByOldCode(int code) {
        String sql = "select code_new from sz_old where code_old = '" + code + "'";
        String dbFile = GetResFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        String str = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return -1;
            }

            cursor.moveToFirst();
            str = cursor.getString(0);
            cursor.close();
        }
        db.close();
        int r = -1;
        try {
            r = Integer.parseInt(str);
        } catch (Exception e) {
        }
        return r;
    }

    public static List<String> GetSzList(String sql) {
        List<String> lst = new ArrayList<String>();
        String dbFile = GetResFile();
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

    public static String GetTukuEname(String mc) {
        String sql = "select mc_en from tuku where mc_cn = '" + mc + "'";
        String[][] sss = SelectData(sql);
        if (sss != null) {
            return sss[0][0];
        }
        return "";
    }

    public static boolean IsZhuzi(int dm) {
        return dm >= 660 && dm <= 690;
    }

    public static boolean IsHunjiaoSz(int dm) {
        return dm == 610 || dm == 620 || dm == 630;
    }

    public static boolean IsGuanmu(int dm) {
        return dm >= 900;
    }

    public static boolean IsZhenye(int dm) {
        return dm < 400;
    }

    public static boolean IsKuoye(int dm) {
        return dm >= 400;
    }

    public static boolean IsHasTuku(int szdm, String szmc) {
        String sql = "select dm from tuku where dm = '" + szdm + "' or mc_cn = '" + szmc + "'";
        String dbFile = GetResFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql, null);
        int n = 0;
        if (cursor != null) {
            n = cursor.getCount();
            cursor.close();
        }
        db.close();
        return n > 0;
    }

    public static String[][] SelectData(String sql) {
        String dbFile = GetResFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
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

    public static String GetResFile() {
        return MyConfig.GetAppdir() + "/res.dat";
    }

    public static String GetLibversion() {
        String dbFile = GetResFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        String sql = "select * from libversion";
        cursor = db.rawQuery(sql, null);
        String str = "";
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return str;
            }
            cursor.moveToFirst();
            str = cursor.getString(0);
            cursor.close();
        }
        db.close();
        return str;
    }

}
