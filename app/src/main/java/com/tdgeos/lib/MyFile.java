package com.tdgeos.lib;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class MyFile {
    /**
     * ����ָ���ļ����ڵ������ļ����������ļ����ڵġ�
     *
     * @param folder   ָ���ļ���
     * @param fileList �ļ��б�����
     */
    public static void getAllFile(String folder, List<String> fileList) {
        File path = new File(folder);
        if (!path.exists()) return;
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (null == files)
                return;
            for (int i = 0; i < files.length; i++) {
                getAllFile(files[i].getPath(), fileList);
            }
        } else {
            String filePath = path.getAbsolutePath();
            fileList.add(filePath);
        }
    }

    /**
     * ����ָ���ļ����ڵ�ָ�����͵��ļ������ļ��У���������Ŀ¼
     *
     * @param path  ָ���ļ���
     * @param types ָ�����ļ����ͣ�EX��jpg,bmp,txt��null��ʾ��������
     * @return ·����Ч��û��Ȩ�޷���null
     */
    public static MyFolder getCurFile(String path, String[] types) {
        if (path == null) return null;

        List<String> fileList = new ArrayList<String>();
        List<String> folderList = new ArrayList<String>();
        MyFolder folder = new MyFolder();
        File file = new File(path);
        if (!file.exists()) return null;//��Ч·��
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) return null;//û��Ȩ��
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    folderList.add(files[i].getName());
                } else {
                    String name = files[i].getName();
                    if (types == null) {
                        fileList.add(name);
                    } else {
                        String ex = MyFile.GetFileExtension(name);
                        if (ex == null) continue;
                        boolean b = false;
                        for (int j = 0; j < types.length; j++) {
                            if (ex.equalsIgnoreCase(types[j])) {
                                b = true;
                                break;
                            }
                        }
                        if (b) fileList.add(name);
                    }
                }
            }
        } else {
            String name = file.getName();
            if (types == null) {
                fileList.add(name);
            } else {
                String ex = MyFile.GetFileExtension(name);
                boolean b = false;
                for (int j = 0; j < types.length; j++) {
                    if (ex.equalsIgnoreCase(types[j])) {
                        b = true;
                        break;
                    }
                }
                if (b) fileList.add(name);
            }
        }
        folder.files = fileList;
        folder.folders = folderList;
        return folder;
    }

    /**
     * �ļ���������Դ�ļ������ݿ�����Ŀ���ļ����ļ������Բ�ͬ�����Ŀ���ļ��Ѵ��ڣ������κδ���ֱ�ӷ���
     *
     * @param src Դ�ļ�
     * @param dst Ŀ���ļ�
     * @throws IOException �׳�IO�쳣
     */
    public static void CopyFile(String src, String dst) throws IOException {
        if (src == null || dst == null) return;
        File srcFile = new File(src);
        if (!srcFile.exists()) return;
        File dstFile = new File(dst);
        if (dstFile.exists()) return;
        else {
            File dstDir = dstFile.getParentFile();
            if (dstDir == null) return;
            if (!dstDir.exists()) return;
            dstFile.createNewFile();

            FileInputStream from = new FileInputStream(srcFile);
            FileOutputStream to = new FileOutputStream(dstFile);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = from.read(buffer)) != -1)
                to.write(buffer, 0, r);
            from.close();
            to.close();
        }
    }

    /**
     * �ļ���������Դ�ļ������ݿ�����Ŀ���ļ����ļ������Բ�ͬ�����Ŀ���ļ��Ѵ��ڣ������κδ���ֱ�ӷ���
     *
     * @param fis Դ�ļ���
     * @param dst Ŀ���ļ�
     * @throws IOException �׳�IO�쳣
     */
    public static void CopyFile(InputStream fis, String dst) throws IOException {
        if (fis == null || dst == null) return;
        File dstFile = new File(dst);
        if (dstFile.exists()) return;
        else {
            File dstDir = dstFile.getParentFile();
            if (dstDir == null) return;
            if (!dstDir.exists()) return;
            dstFile.createNewFile();

            FileOutputStream to = new FileOutputStream(dstFile);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = fis.read(buffer)) != -1)
                to.write(buffer, 0, r);
            to.close();
        }
    }


    /**
     * ��ȡ�ļ����ڵ��ļ���·��
     *
     * @param path �ļ�����·��
     * @return ��Ч·������null
     */
    public static String GetParentPath(String path) {
        if (path == null || path.equals("")) return null;
        if (path.length() == 1) return path;

        String parent = null;
        char[] arTmp = path.toCharArray();
        int pos = arTmp.length;
        if (arTmp[0] != '/' && arTmp[0] != '\\') return null;
        if (arTmp[pos - 1] == '/' && arTmp[pos - 1] == '\\') {
            char[] tmp = new char[pos - 1];
            for (int i = 0; i < pos - 1; i++) {
                tmp[i] = arTmp[i];
            }
            return new String(tmp);
        }
        while (arTmp[pos - 1] != '/' && arTmp[pos - 1] != '\\') {
            pos--;
        }
        if (pos == 1) {
            parent = "/";
        } else {
            char[] tmp = new char[pos - 1];
            for (int i = 0; i < pos - 1; i++) {
                tmp[i] = arTmp[i];
            }
            parent = new String(tmp);
        }
        return parent;
    }

    /**
     * ���ļ������ļ�·���л�ȡ�ļ���չ��������"."
     *
     * @param file �ļ������ļ�·��
     * @return �ļ���Ч��û����չ������null
     */
    public static String GetFileExtension(String file) {
        String name = MyFile.GetFileName(file);
        if (name == null) return null;
        char[] tmp = name.toCharArray();
        int len = tmp.length;
        int pos = len - 1;
        for (int i = pos; i >= 0; i--) {
            if (tmp[i] == '.') {
                pos = i + 1;
                break;
            } else {
                pos--;
            }
        }
        if (pos == -1) return null;
        char[] ac = new char[len - pos];
        for (int i = 0; i < len - pos; i++) {
            ac[i] = tmp[i + pos];
        }
        String str = String.valueOf(ac);
        if (str.equals("")) return null;
        return str;
    }

    /**
     * ���ļ�·���л�ȡ�ļ���������չ��
     *
     * @param path �ļ�·��
     * @return
     */
    public static String GetFileName(String path) {
        if (path == null || path.equals("")) return null;
        char[] tmp = path.toCharArray();
        int len = tmp.length;
        int pos = len - 1;
        for (int i = pos; i >= 0; i--) {
            if (tmp[i] == '/' || tmp[i] == '\\') {
                pos = i + 1;
                break;
            } else {
                pos--;
            }
        }
        if (pos == -1) return path;
        char[] ac = new char[len - pos];
        for (int i = 0; i < len - pos; i++) {
            ac[i] = tmp[i + pos];
        }
        String str = String.valueOf(ac);
        if (str.equals("")) return null;
        return str;
    }

    /**
     * ���ļ�·���л�ȡ�ļ�����������չ��
     *
     * @param path �ļ�·��
     * @return
     */
    public static String GetFileNameNoEx(String path) {
        if (path == null) return null;
        String str = GetFileName(path);
        if (str == null) return null;
        String ex = GetFileExtension(str);
        if (ex == null) {
            char[] tmp1 = str.toCharArray();
            if (tmp1[tmp1.length - 1] != '.') return str;
            else {
                char[] tmp2 = new char[tmp1.length - 1];
                for (int i = 0; i < tmp2.length; i++) {
                    tmp2[i] = tmp1[i];
                }
                String tmp = new String(tmp2);
                if (tmp.equals("")) return null;
                return tmp;
            }
        }
        char[] ac1 = str.toCharArray();
        char[] ac2 = ex.toCharArray();
        int n1 = ac1.length;
        int n2 = ac2.length + 1;
        char[] ac3 = new char[n1 - n2];
        for (int i = 0; i < n1 - n2; i++) {
            ac3[i] = ac1[i];
        }
        String name = String.valueOf(ac3);
        if (name.equals("")) return null;
        return name;
    }

    /**
     * ���ĺ�׺���������ظ��ĺ������
     *
     * @param path �ļ�·��
     * @param ex   �޸ĺ����չ��
     * @return ���ظ��ĺ���ļ�·��
     */
    public static String SetFileExtension(String path, String ex) {
        if (path == null || path.equals("")) return null;
        if (ex == null || ex.equals("")) return path;

        String oldex = MyFile.GetFileExtension(path);
        if (oldex == null) {
            char[] tmp = path.toCharArray();
            int len = tmp.length;
            if (tmp[len - 1] == '.') return path + ex;
            return path + "." + ex;
        }

        char[] tmp = path.toCharArray();
        int len = tmp.length - oldex.length() - 1;
        if (len <= 0) return null;
        char[] tmp2 = new char[len];
        for (int i = 0; i < len; i++) {
            tmp2[i] = tmp[i];
        }
        String str = new String(tmp2);
        str += "." + ex;
        return str;
    }

    /**
     * �����ļ��У�����ļ����Ѵ��ڣ�ֱ�ӷ��ء�����ϼ��ļ��в����ڣ����ȴ����ϼ��ļ��У��ڴ���Ŀ���ļ��С��ݹ顣
     *
     * @param dir �ļ���·��
     */
    public static void MakeDir(String dir) {
        if (dir == null) return;
        File file = new File(dir);
        if (file.exists() && file.isDirectory()) return;
        else {
            String parent = GetParentPath(dir);
            MakeDir(parent);
            file.mkdir();
        }
    }

    /**
     * �ж��ļ����ļ����Ƿ���Ч
     *
     * @param path
     * @return
     */
    public static boolean Exists(String path) {
        if (path == null) return false;
        File file = new File(path);
        if (file.exists()) return true;
        else return false;
    }

    public static String GetValidFileName(String path, char c, int i) {
        if (!Exists(path)) return path;

        String dir = MyFile.GetParentPath(path);
        String name = MyFile.GetFileNameNoEx(path);
        String ex = MyFile.GetFileExtension(path);

        String s1 = MyFuns.Split(name, c)[0];

        String str = dir + "/" + s1 + c + (i + 1) + "." + ex;
        if (!Exists(str)) return str;
        else return GetValidFileName(path, c, i + 1);
    }

    /**
     * �ж�·���Ƿ�Ϊ�ļ���
     *
     * @param path
     * @return
     */
    public static boolean IsDirectory(String path) {
        if (path == null) return false;
        File file = new File(path);
        if (file.exists() && file.isDirectory()) return true;
        else return false;
    }

    /**
     * �������ļ����ļ��У����Ŀ���ļ����Ѵ��ڣ�ֱ�ӷ���
     *
     * @param srcName
     * @param dstName
     */
    public static void Rename(String srcName, String dstName) {
        if (srcName == null || dstName == null) return;
        File dst = new File(dstName);
        if (dst.exists()) return;
        File file = new File(srcName);
        if (file.exists()) {
            file.renameTo(dst);
        }
    }

    /**
     * �����ļ��У����Ŀ���ļ����Ѵ��ڣ�ֱ�ӷ���
     *
     * @param srcDir
     * @param dstDir
     */
    public static void CopyDir(String srcDir, String dstDir) {
        if (srcDir == null || dstDir == null) return;

        if (!IsDirectory(srcDir)) return;
        if (IsDirectory(dstDir)) return;

        MakeDir(dstDir);

        MyFolder myFolder = getCurFile(srcDir, null);
        if (myFolder != null) {
            for (int i = 0; i < myFolder.files.size(); i++) {
                String srcPath = srcDir + "/" + myFolder.files.get(i);
                String dstPath = dstDir + "/" + myFolder.files.get(i);
                try {
                    CopyFile(srcPath, dstPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < myFolder.folders.size(); i++) {
                String srcPath = srcDir + "/" + myFolder.folders.get(i);
                String dstPath = dstDir + "/" + myFolder.folders.get(i);
                CopyDir(srcPath, dstPath);
            }
        }
    }

    /**
     * ɾ���ļ��У��������е��ļ������ļ��У��ݹ顣
     *
     * @param root ��ɾ�����ļ���·��
     */
    public static void DeleteDir(File root) {
        if (!root.exists()) return;
        if (root.isDirectory()) {
            File[] files = root.listFiles();
            if (files == null) {
                root.delete();
                return;
            } else {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        DeleteDir(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
                root.delete();
            }
        } else {
            root.delete();
        }
    }

    /**
     * ɾ���ļ�
     *
     * @param path ��ɾ�����ļ�·����ȫ·����ʽ
     */
    public static void DeleteFile(String path) {
        if (path == null) return;
        File file = new File(path);
        if (file.exists()) file.delete();
    }

    /**
     * �����ݵ�����excel
     *
     * @param values  ����
     * @param types   ����
     * @param fields  �ֶ���
     * @param path    excel�ļ�·��
     * @param iscover true��ʾ���ǣ�false��ʾ׷��
     */
    public static void ExportToExcel(String[][] values, String[] types, String[] fields, String path, boolean iscover) {
        if (values == null || path == null) return;
        File file = new File(path);
        if (file.exists()) {
            if (iscover) file.delete();
            else return;
        }
        try {
            WritableWorkbook book = Workbook.createWorkbook(file);
            WritableSheet sheet = book.createSheet("book1", 0);
            if (fields != null) {
                for (int j = 0; j < fields.length; j++) {
                    Label label = new Label(j, 0, fields[j]);
                    sheet.addCell(label);
                }
                for (int i = 0; i < values.length; i++) {
                    for (int j = 0; j < values[0].length; j++) {
                        if (types == null) {
                            Label label = new Label(j, i + 1, values[i][j]);
                            sheet.addCell(label);
                        } else {
                            if (types[j].equalsIgnoreCase("TEXT")) {
                                Label label = new Label(j, i + 1, values[i][j]);
                                sheet.addCell(label);
                            } else if (types[j].equalsIgnoreCase("REAL")) {
                                double value = 0.0;
                                if (values[i][j] != null && !values[i][j].equals(""))
                                    value = Double.valueOf(values[i][j]);
                                jxl.write.Number n = new jxl.write.Number(j, i + 1, value);
                                sheet.addCell(n);
                            } else if (types[j].equalsIgnoreCase("INTEGER")) {
                                int value = 0;
                                if (values[i][j] != null && !values[i][j].equals(""))
                                    value = Integer.valueOf(values[i][j]);
                                jxl.write.Number n = new jxl.write.Number(j, i + 1, value);
                                sheet.addCell(n);
                            } else {
                                Label label = new Label(j, i + 1, "null");
                                sheet.addCell(label);
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < values.length; i++) {
                    for (int j = 0; j < values[0].length; j++) {
                        if (types == null) {
                            Label label = new Label(j, i + 1, values[i][j]);
                            sheet.addCell(label);
                        } else {
                            if (types[j].equalsIgnoreCase("TEXT")) {
                                Label label = new Label(j, i, values[i][j]);
                                sheet.addCell(label);
                            } else if (types[j].equalsIgnoreCase("REAL")) {
                                double value = Double.valueOf(values[i][j]);
                                jxl.write.Number n = new jxl.write.Number(j, i, value);
                                sheet.addCell(n);
                            } else if (types[j].equalsIgnoreCase("INTEGER")) {
                                int value = Integer.valueOf(values[i][j]);
                                jxl.write.Number n = new jxl.write.Number(j, i, value);
                                sheet.addCell(n);
                            } else {
                                Label label = new Label(j, i, "null");
                                sheet.addCell(label);
                            }
                        }
                    }
                }
            }

            book.write();
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ��ȡexcel�ļ�
     *
     * @param path excel�ļ�·��
     * @return
     */
    public static String[][] ReadExcel(String path, int iSheet) {
        if (path == null) return null;
        File file = new File(path);
        if (!file.exists()) return null;
        String[][] values = null;
        try {
            Workbook book = Workbook.getWorkbook(file);
            Sheet sheet = book.getSheet(iSheet);
            int w = sheet.getColumns();
            int h = sheet.getRows();
            values = new String[h][w];
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    Cell cell = sheet.getCell(j, i);
                    values[i][j] = cell.getContents();
                }
            }
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    public static String[][] ReadExcel(InputStream fis, int iSheet) {
        String[][] values = null;
        try {
            Workbook book = Workbook.getWorkbook(fis);
            Sheet sheet = book.getSheet(iSheet);
            int w = sheet.getColumns();
            int h = sheet.getRows();
            values = new String[h][w];
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    Cell cell = sheet.getCell(j, i);
                    values[i][j] = cell.getContents();
                }
            }
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    /**
     * ��excel��ָ��һ������
     *
     * @param path excel�ļ�
     * @param pos  ָ���У���0��ʼ
     * @return
     */
    public static String[] ReadExcel(String path, int iSheet, int iRow) {
        if (path == null) return null;
        File file = new File(path);
        if (!file.exists()) return null;
        String[] values = null;
        try {
            Workbook book = Workbook.getWorkbook(file);
            Sheet sheet = book.getSheet(iSheet);
            int w = sheet.getColumns();
            int h = sheet.getRows();
            if (iRow < 0 || iRow >= h) {
                book.close();
                return null;
            }
            values = new String[w];
            for (int i = 0; i < w; i++) {
                Cell cell = sheet.getCell(i, iRow);
                values[i] = cell.getContents();
            }
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    /**
     * ��excel�ļ������¼����ʱ������
     *
     * @param values ��¼����
     * @param types  ��������
     * @param pos    ����λ��
     * @param path   excel�ļ�·��
     */
    public static void InsertToExcel(String[] values, String[] types, int pos, String path) {
        /*
        if(values == null || types == null || path == null) return;
    	File file = new File(path);
    	if(!file.exists()) return;
    	try{
    		Workbook book0 = Workbook.getWorkbook(file);
    		WritableWorkbook book = Workbook.createWorkbook(file, book0);
			WritableSheet sheet = book.getSheet(0);
			sheet.insertRow(pos);
			for(int j=0;j<values.length;j++)
			{
				if(types[j].equalsIgnoreCase("TEXT"))
				{
					Label label = new Label(j, pos, values[j]);   
					sheet.addCell(label);  
				}
				else if(types[j].equalsIgnoreCase("REAL"))
				{
					double value = Double.valueOf(values[j]);
					jxl.write.Number n = new jxl.write.Number(j, pos, value);    
					sheet.addCell(n); 
				}
				else if(types[j].equalsIgnoreCase("INTEGER"))
				{
					int value = Integer.valueOf(values[j]);
					jxl.write.Number n = new jxl.write.Number(j, pos, value);    
					sheet.addCell(n); 
				}
				else
				{
					Label label = new Label(j, pos, "null");   
					sheet.addCell(label);  
				}
			}
			book.write();
			book.close();
		}catch(Exception e){
		   e.printStackTrace();
		}
		*/
    }


    private static final int BUFFER = 8192;

    public static void Zip(String srcPathName, String zipPathName) {
        File zipFile = new File(zipPathName);
        File file = new File(srcPathName);
        if (!file.exists()) return;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);
            String basedir = "";
            compress(file, out, basedir);
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void compress(File file, ZipOutputStream out, String basedir) {
        if (file.isDirectory()) {
            compressDirectory(file, out, basedir);
        } else {
            compressFile(file, out, basedir);
        }
    }

    /**
     * ѹ��һ��Ŀ¼
     */
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists())
            return;

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            compress(files[i], out, basedir + dir.getName() + "/");
        }
    }

    /**
     * ѹ��һ���ļ�
     */
    private static void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {
            return;
        } else if (file.getName().equals("upload.log")) {
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void Unzip(String unZipFileName, String unZipPath) throws Exception {
        ZipFile zipFile = new ZipFile(unZipFileName);
        unZipFileByOpache(zipFile, unZipPath);
    }

    private static void unZipFileByOpache(ZipFile zipFile, String unZipRoot) throws Exception, IOException {
        java.util.Enumeration e = zipFile.entries();
        ZipEntry zipEntry;
        while (e.hasMoreElements()) {
            zipEntry = (ZipEntry) e.nextElement();
            InputStream fis = zipFile.getInputStream(zipEntry);
            if (zipEntry.isDirectory()) {
                File file = new File(unZipRoot + File.separator + zipEntry.getName());
                file.mkdirs();
            } else {
                File file = new File(unZipRoot + File.separator + zipEntry.getName());
                File parentFile = file.getParentFile();
                parentFile.mkdirs();
                FileOutputStream fos = new FileOutputStream(file);

                byte[] b = new byte[BUFFER];
                int len;
                while ((len = fis.read(b, 0, b.length)) != -1) {
                    fos.write(b, 0, len);
                }
                fos.close();
                fis.close();
            }
        }
    }

	/*
	public static void writeGpsToExif(String img, double lon, double lat, double h) throws IOException
	{
		String tmp = img + ".bak";
		File fTmp = new File(tmp);
		if(fTmp.exists()) fTmp.delete();
		
		File fImg = new File(img);
		if(!fImg.exists()) return;
		fImg.renameTo(fTmp);

		InputStream fip = new BufferedInputStream(new FileInputStream(fTmp));
        LLJTran llj = new LLJTran(fip);
        try {
            llj.read(LLJTran.READ_INFO, true);
        } catch (LLJTranException e) {
            e.printStackTrace();
        }

        AbstractImageInfo imageInfo = llj.getImageInfo();

        if(!(imageInfo instanceof Exif))
        {
            return;
        }
        
        char lonFag = 'E';
        if(lon < 0) lonFag = 'W';
        char latFag = 'N';
        if(lat < 0) latFag = 'S';
        
        if(lon < 0) lon = -lon;
        if(lat < 0) lat = -lat;
        
		int lonDu = (int)lon;
		int latDu = (int)lat;
		double flon = (lon - lonDu) * 60;
		double flat = (lat - latDu) * 60;
		int lonFen = (int)flon;
		int latFen = (int)flat;
		double mlon = (flon - lonFen) * 60;
		double mlat = (flat - latFen) * 60;
        int lonMiao = (int)mlon;
        int latMiao = (int)mlat;
        int heigh = (int)h;

        Exif exif = (Exif) imageInfo;
        IFD mainIfd = exif.getIFDs()[0];
        IFD gpsIfd = mainIfd.getIFD(Exif.GPSINFO);

        if(gpsIfd == null)
        {
            gpsIfd = new IFD(Exif.GPSINFO, Exif.LONG);
            mainIfd.addIFD(gpsIfd);
        }

        Entry e;

        e = new Entry(Exif.ASCII);
        e.setValue(0, latFag);
        gpsIfd.setEntry(new Integer(Exif.GPSLatitudeRef), 0, e);
        e = new Entry(Exif.RATIONAL);
        e.setValue(0, new Rational(latDu, 1));
        e.setValue(1, new Rational(latFen, 1));
        e.setValue(2, new Rational(latMiao, 1));
        gpsIfd.setEntry(new Integer(Exif.GPSLatitude), 0, e);
        //e = new Entry(Exif.BYTE);

        e = new Entry(Exif.ASCII);
        e.setValue(0, lonFag);
        gpsIfd.setEntry(new Integer(Exif.GPSLongitudeRef), 0, e);
        e = new Entry(Exif.RATIONAL);
        e.setValue(0, new Rational(lonDu, 1));
        e.setValue(1, new Rational(lonFen, 1));
        e.setValue(2, new Rational(lonMiao, 1));
        gpsIfd.setEntry(new Integer(Exif.GPSLongitude), 0, e);
        
        e = new Entry(Exif.BYTE);
        int i = heigh >= 0 ? 0 : 1;// 1��ʾ��ƽ�����£�0��ʾ��ƽ������
        e.setValue(0, new Integer(i)); 
        gpsIfd.setEntry(new Integer(Exif.GPSAltitudeRef), 0, e);
        e = new Entry(Exif.RATIONAL);
        if(heigh < 0) heigh = -heigh;
        e.setValue(0, new Rational(heigh, 1));
        gpsIfd.setEntry(new Integer(Exif.GPSAltitude), 0, e);

        llj.refreshAppx();

        OutputStream out = new BufferedOutputStream(new FileOutputStream(fImg));

        try {
			llj.xferInfo(null, out, LLJTran.REPLACE, LLJTran.REPLACE);
		} catch (LLJTranException e1) {
			e1.printStackTrace();
		}

        fip.close();
        out.close();

        llj.freeMemory();
	}

	public static String readGpsFromExif(String img) throws IOException
	{
		File file = new File(img);
		if(!file.exists()) return null;
		
		InputStream fip = new BufferedInputStream(new FileInputStream(file));
        LLJTran llj = new LLJTran(fip);
        try {
            llj.read(LLJTran.READ_INFO, true);
        } catch (LLJTranException e) {
            e.printStackTrace();
        }

        AbstractImageInfo imageInfo = llj.getImageInfo();

        if(! (imageInfo instanceof Exif))
        {
            return null;
        }
        
        String str = null;

        Exif exif = (Exif) imageInfo;
        IFD mainIfd = exif.getIFDs()[0];
        IFD gpsIfd = mainIfd.getIFD(Exif.GPSINFO);

        if(gpsIfd != null)
        {
        	Entry e = null;
        	Rational r = null;
        	String s = null;
        	int i = 0;
        	str = "";
        	
        	e = (Entry)gpsIfd.getEntry(Exif.GPSLatitudeRef, 0);
        	s = (String)e.getValue(0);
        	str += s.trim() + ":";
        	
        	e = (Entry)gpsIfd.getEntry(Exif.GPSLatitude, 0);
        	r = (Rational)e.getValue(0);
        	i = (int)r.floatValue();
        	str += i + ",";
        	r = (Rational)e.getValue(1);
        	i = (int)r.floatValue();
        	str += i + ",";
        	r = (Rational)e.getValue(2);
        	i = (int)r.floatValue();
        	str += i + ";";
        	
        	e = (Entry)gpsIfd.getEntry(Exif.GPSLongitudeRef, 0);
        	s = (String)e.getValue(0);
        	str += s.trim() + ":";
        	
        	e = (Entry)gpsIfd.getEntry(Exif.GPSLongitude, 0);
        	r = (Rational)e.getValue(0);
        	i = (int)r.floatValue();
        	str += i + ",";
        	r = (Rational)e.getValue(1);
        	i = (int)r.floatValue();
        	str += i + ",";
        	r = (Rational)e.getValue(2);
        	i = (int)r.floatValue();
        	str += i + ";";
        	
        	e = (Entry)gpsIfd.getEntry(Exif.GPSAltitudeRef, 0);
        	i = (Integer)e.getValue(0);
        	str += i + ":";
        	
        	e = (Entry)gpsIfd.getEntry(Exif.GPSAltitude, 0);
        	r = (Rational)e.getValue(0);
        	i = (int)r.floatValue();
        	str += i;
        }

		fip.close(); 
		llj.freeMemory(); 
		
		return str;
	}
	*/
}




































