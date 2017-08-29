package com.tdgeos.lib;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

public class RasterLayer {
    private String path;
    private boolean isVisible;
    private MyRect fBound;
    private int nPixHeight;                //栅格总行数
    private int nPixWidth;            //栅格总列数
    private int nBandCount;            //波段数量
    private int nLvs = 0;            //层数
    private int curLv = 0;
    private List<TLayer> lstTLayer;

    public static final int TILE_SIZE = 256;
    public static final int TILE_BUF_SIZE = TILE_SIZE * TILE_SIZE * 3;

    public RasterLayer() {
        path = null;
        isVisible = true;
        fBound = new MyRect();
        nPixHeight = 0;
        nPixWidth = 0;
        nBandCount = 0;
    }

    public boolean Open(String file) {
        if (file == null) return false;
        if (!new File(file).exists()) return false;
        String filename = MyFile.GetFileName(file);
        String fileex = MyFile.GetFileExtension(filename);

        if (fileex.equalsIgnoreCase("img")) {
            path = file;
            try {
                File fp = new File(file);
                if (!fp.exists()) return false;
                long filelen = fp.length();
                FileInputStream fis = new FileInputStream(fp);
                DataInputStream dis = new DataInputStream(fis);

                byte[] buf4 = new byte[4];
                byte[] buf8 = new byte[8];

                dis.read(buf4, 0, 4);
                nLvs = MyFuns.byteToLittleInt(buf4);

                dis.read(buf8, 0, 8);
                double td = MyFuns.byteToLittleDouble(buf8);
                fBound.left = (float) td;
                dis.read(buf8, 0, 8);
                td = MyFuns.byteToLittleDouble(buf8);
                fBound.bottom = (float) td;
                dis.read(buf8, 0, 8);
                td = MyFuns.byteToLittleDouble(buf8);
                fBound.right = (float) td;
                dis.read(buf8, 0, 8);
                td = MyFuns.byteToLittleDouble(buf8);
                fBound.top = (float) td;
                fBound.Print();

                dis.read(buf4, 0, 4);
                nPixWidth = MyFuns.byteToLittleInt(buf4);
                dis.read(buf4, 0, 4);
                nPixHeight = MyFuns.byteToLittleInt(buf4);

                dis.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            lstTLayer = new ArrayList<TLayer>();
            int cf = 0;
            int w1 = 0;
            int h1 = 0;
            int w2 = 0;
            int h2 = 0;
            for (int i = 0; i < nLvs; i++) {
                cf = cifang(i);
                TLayer tlayer = new TLayer();
                tlayer.nLvs = i;

                w1 = nPixWidth / cf;
                h1 = nPixHeight / cf;
                int w = w1 % TILE_SIZE;
                int h = h1 % TILE_SIZE;
                if (w == 0) w2 = w1;
                else w2 = w1 + TILE_SIZE - w;
                if (h == 0) h2 = h1;
                else h2 = h1 + TILE_SIZE - h;

                tlayer.scale = w1 / fBound.width();
                tlayer.nRows = h2 / TILE_SIZE;
                tlayer.nCols = w2 / TILE_SIZE;
                tlayer.lstTiles = new ArrayList<Tile>();


                float xScale = fBound.width() / w1;
                float yScale = fBound.height() / h1;
                int xBegin = 0;
                int yBegin = 0;
                for (int m = 0; m < tlayer.nRows; m++) {
                    for (int n = 0; n < tlayer.nCols; n++) {
                        Tile tile = new Tile();
                        tile.xIndex = n;
                        tile.yIndex = m;
                        xBegin = n * TILE_SIZE;
                        yBegin = m * TILE_SIZE;
                        float left = xBegin * xScale + fBound.left;
                        float right = (xBegin + TILE_SIZE) * xScale + fBound.left;
                        float top = fBound.top - yBegin * yScale;
                        float bottom = fBound.top - (yBegin + TILE_SIZE) * yScale;
                        tile.geoBox = new MyRect(left, top, right, bottom);
                        tlayer.lstTiles.add(tile);
                    }
                }
                lstTLayer.add(tlayer);
            }
            return true;
        }
        return false;
    }

    public void Draw(Canvas canvas, Paint paint, float mapOriginX, float mapOriginY, MyRect region, float scale) {
        if (!region.isIntersectToRect(fBound)) {
            return;
        }

        if (scale >= lstTLayer.get(0).scale) {
            curLv = 0;
        } else if (scale <= lstTLayer.get(nLvs - 1).scale) {
            curLv = nLvs - 1;
        } else {
            byte pos = 1;
            for (byte i = 1; i < nLvs; i++) {
                if (scale > lstTLayer.get(i).scale) {
                    pos = i;
                    break;
                }
            }
            float s1 = lstTLayer.get(pos - 1).scale - scale;
            float s2 = scale - lstTLayer.get(pos).scale;
            if (s1 > s2) curLv = pos;
            else curLv = pos - 1;
        }

        List<Tile> lstDraw = new ArrayList<Tile>();
        for (int i = 0; i < lstTLayer.get(curLv).lstTiles.size(); i++) {
            Tile tile = lstTLayer.get(curLv).lstTiles.get(i);
            if (region.isIntersectToRect(tile.geoBox)) {
                lstDraw.add(tile);
            }
        }

        int n = lstDraw.size();
        int i0 = lstDraw.get(0).xIndex;
        int j0 = lstDraw.get(0).yIndex;
        int in = lstDraw.get(n - 1).xIndex;
        int jn = lstDraw.get(n - 1).yIndex;
        int width = (in - i0 + 1) * TILE_SIZE;
        int height = (jn - j0 + 1) * TILE_SIZE;

        Bitmap myCanvas = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        try {
            Canvas cv = new Canvas(myCanvas);
            Bitmap bmpTile = null;
            byte[] buffer = new byte[TILE_BUF_SIZE];
            int size = TILE_BUF_SIZE / 3;
            int[] colors = new int[size];
            int r, g, b;
            int k = 0;
            int x = 0;
            int y = 0;
            String file = MyFile.GetFileNameNoEx(path) + "_" + curLv + ".raw";
            file = MyFile.GetParentPath(path) + "/" + file;
            FileInputStream fis = new FileInputStream(file);
            DataInputStream dis = new DataInputStream(fis);
            long offset = lstTLayer.get(curLv).nCols * 1L * j0 * TILE_BUF_SIZE + i0 * TILE_BUF_SIZE;
            for (int j = j0; j <= jn; j++) {
                dis.skip(offset);
                for (int i = i0; i <= in; i++) {
                    dis.read(buffer);
                    for (int a = 0; a < size; a++) {
                        r = buffer[a] & 0xff;
                        g = buffer[size + a] & 0xff;
                        b = buffer[size + size + a] & 0xff;
                        colors[a] = Color.rgb(r, g, b);
                    }
                    bmpTile = Bitmap.createBitmap(colors, TILE_SIZE, TILE_SIZE, Bitmap.Config.ARGB_4444);
                    x = (i - i0) * TILE_SIZE;
                    y = (j - j0) * TILE_SIZE;
                    cv.drawBitmap(bmpTile, x, y, paint);
                    bmpTile.recycle();
                    bmpTile = null;
                    k++;
                }
                offset = (lstTLayer.get(curLv).nCols - in + i0 - 1) * 1L * TILE_BUF_SIZE;
            }
            buffer = null;
            colors = null;
            dis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        float left = lstDraw.get(0).geoBox.left;
        float top = lstDraw.get(0).geoBox.top;
        left = left * scale + mapOriginX;
        top = mapOriginY - top * scale;
        Matrix m = new Matrix();
        float ss = scale / lstTLayer.get(curLv).scale;
        m.setScale(ss, ss);
        m.postTranslate(left, top);
        canvas.drawBitmap(myCanvas, m, paint);
        myCanvas.recycle();
        myCanvas = null;
    }

    private int cifang(int n) {
        int s = 1;
        for (int i = 0; i < n; i++) {
            s *= 2;
        }
        return s;
    }

    public String GetPath() {
        return path;
    }

    public MyRect GetBound() {
        return new MyRect(fBound);
    }

    public int GetPixHeight() {
        return nPixHeight;
    }

    public int GetPixWidth() {
        return nPixWidth;
    }

    public int GetBandCount() {
        return nBandCount;
    }

    public boolean IsVisible() {
        return isVisible;
    }

    public void SetVisible(boolean b) {
        isVisible = b;
    }

    class Tile {
        int xIndex;
        int yIndex;
        //int xBegin;
        //int yBegin;
        MyRect geoBox;
    }

    class TLayer {
        //int nPixWidth;
        //int nPixHeight;
        int nLvs;
        int nRows;
        int nCols;
        float scale;
        List<Tile> lstTiles;
    }
}
