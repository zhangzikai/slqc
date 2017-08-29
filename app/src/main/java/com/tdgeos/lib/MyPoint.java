package com.tdgeos.lib;

public class MyPoint {
    public float x;
    public float y;
    public float z;

    public MyPoint() {
        x = 0.0f;
        y = 0.0f;
    }

    public MyPoint(float tx, float ty) {
        x = tx;
        y = ty;
    }

    public MyPoint(double tx, double ty) {
        x = (float) tx;
        y = (float) ty;
    }

    public MyPoint(MyPoint pt) {
        if (pt != null) {
            x = pt.x;
            y = pt.y;
        }
    }

    public boolean IsEqual(MyPoint pt) {
        if (pt == null) return false;
        if ((x - pt.x) < 0.0000001 && (x - pt.x) > -0.0000001 && (y - pt.y) < 0.0000001 && (y - pt.y) > -0.0000001)
            return true;
        return false;
    }
}
