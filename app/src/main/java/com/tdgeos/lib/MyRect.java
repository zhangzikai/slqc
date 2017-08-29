package com.tdgeos.lib;

//矩形类
public class MyRect {
    public float left;
    public float top;
    public float right;
    public float bottom;

    public MyRect() {
        left = 0.0f;
        top = 0.0f;
        right = 0.0f;
        bottom = 0.0f;
    }

    public MyRect(float l, float t, float r, float b) {
        if (l > r) {
            float tmp = l;
            l = r;
            r = tmp;
        }
        if (t < b) {
            float tmp = t;
            t = b;
            b = tmp;
        }
        left = l;
        top = t;
        right = r;
        bottom = b;
    }

    public MyRect(MyPoint pt1, MyPoint pt2) {
        float l = pt1.x;
        float t = pt1.y;
        float r = pt2.x;
        float b = pt2.y;
        if (l > r) {
            float tmp = l;
            l = r;
            r = tmp;
        }
        if (t < b) {
            float tmp = t;
            t = b;
            b = tmp;
        }
        left = l;
        top = t;
        right = r;
        bottom = b;
    }

    public MyRect(MyRect rect) {
        left = rect.left;
        top = rect.top;
        right = rect.right;
        bottom = rect.bottom;
    }

    public void adjust() {
        if (left > right) {
            float tmp = left;
            left = right;
            right = tmp;
        }
        if (top < bottom) {
            float tmp = top;
            top = bottom;
            bottom = tmp;
        }
    }

    public boolean isNull() {
        return (left == right && top == bottom);
    }

    public boolean isLine() {
        return (left == right && top != bottom || left != right && top == bottom);
    }

    public float width() {
        adjust();
        return right - left;
    }

    public float height() {
        adjust();
        return top - bottom;
    }

    public MyPoint center() {
        return new MyPoint((left + right) / 2, (top + bottom) / 2);
    }

    public boolean isPointInRect(float x, float y) {
        this.adjust();
        return (x >= left && y <= top && x <= right && y >= bottom);
    }

    public boolean isPointInRect(MyPoint pt) {
        this.adjust();
        return (pt.x >= left && pt.y <= top && pt.x <= right && pt.y >= bottom);
    }

    //是否与另一个矩形相交
    public boolean isIntersectToRect(MyRect rect) {
        if (this.isPointInRect(rect.left, rect.top)) return true;
        if (this.isPointInRect(rect.right, rect.top)) return true;
        if (this.isPointInRect(rect.left, rect.bottom)) return true;
        if (this.isPointInRect(rect.right, rect.bottom)) return true;

        if (rect.isPointInRect(this.left, this.top)) return true;
        if (rect.isPointInRect(this.right, this.top)) return true;
        if (rect.isPointInRect(this.left, this.bottom)) return true;
        if (rect.isPointInRect(this.right, this.bottom)) return true;
        if (rect.left < this.left && rect.right > this.right && rect.top < this.top && rect.bottom > this.bottom)
            return true;
        if (rect.left > this.left && rect.right < this.right && rect.top > this.top && rect.bottom < this.bottom)
            return true;
        return false;
    }

    //是否完全包含另一个矩形
    public boolean isContainRect(MyRect rect) {
        if (!isPointInRect(rect.left, rect.top)) return false;
        if (!isPointInRect(rect.right, rect.bottom)) return false;
        return true;
    }

    //线段是否过矩形
    public boolean isLinePassRect(MyPoint ptBegin, MyPoint ptEnd) {
        float x1 = ptBegin.x;
        float y1 = ptBegin.y;
        float x2 = ptEnd.x;
        float y2 = ptEnd.y;
        return isLinePassRect(x1, y1, x2, y2);
    }

    public boolean isLinePassRect(float x1, float y1, float x2, float y2) {
        this.adjust();
        if (this.isPointInRect(x1, y1)) return true;
        if (this.isPointInRect(x2, y2)) return true;
        if (y1 > y2) {
            if (y2 > top || y1 < bottom) return false;
        } else {
            if (y1 > top || y2 < bottom) return false;
        }
        if (x1 > x2) {
            if (x1 < left || x2 > right) return false;
        } else {
            if (x2 < left || x1 > right) return false;
        }
        boolean blt = isPointAtLineLeft(left, top, x1, y1, x2, y2);
        boolean blb = isPointAtLineLeft(left, bottom, x1, y1, x2, y2);
        boolean brt = isPointAtLineLeft(right, top, x1, y1, x2, y2);
        boolean brb = isPointAtLineLeft(right, bottom, x1, y1, x2, y2);
        if (blt && blb && brt && brb) return false;
        if (!blt && !blb && !brt && !brb) return false;

        return true;
    }

    public boolean isIntersectPolyline(float[] faX, float[] faY) {
        if (faX == null || faY == null) return false;
        if (faX.length != faY.length) return false;
        int len = faX.length;
        if (len < 2) return false;
        this.adjust();
        boolean b = false;
        for (int i = 1; i < len; i++) {
            if (isLinePassRect(faX[i], faY[i], faX[i - 1], faY[i - 1])) {
                b = true;
                break;
            }
        }
        return b;
    }

    public boolean isInRing(float[] faX, float[] faY) {
        if (faX == null || faY == null) return false;
        if (faX.length != faY.length) return false;
        int len = faX.length;
        if (len < 3) return false;
        this.adjust();

        float fMinX = MyFuns.dMin(faX);
        float fMaxX = MyFuns.dMax(faX);
        float fMinY = MyFuns.dMin(faY);
        float fMaxY = MyFuns.dMax(faY);
        if (!isIntersectToRect(new MyRect(fMinX, fMaxY, fMaxX, fMinY))) return false;
        if (!MyFuns.IsPointInRing(left, top, faX, faY)) return false;
        if (!MyFuns.IsPointInRing(left, bottom, faX, faY)) return false;
        if (!MyFuns.IsPointInRing(right, top, faX, faY)) return false;
        if (!MyFuns.IsPointInRing(right, bottom, faX, faY)) return false;
        for (int i = 0; i < len; i++) {
            if (isPointInRect(faX[i], faY[i])) return false;
        }
        for (int i = 0; i < len - 1; i++) {
            if (isLinePassRect(faX[i], faY[i], faX[i + 1], faY[i + 1])) return false;
        }
        if (isLinePassRect(faX[0], faY[0], faX[len - 1], faY[len - 1])) return false;
        return true;
    }

    public boolean isIntersectRing(float[] faX, float[] faY) {
        if (faX == null || faY == null) return false;
        if (faX.length != faY.length) return false;
        int len = faX.length;
        if (len < 3) return false;
        this.adjust();

        float fMinX = MyFuns.dMin(faX);
        float fMaxX = MyFuns.dMax(faX);
        float fMinY = MyFuns.dMin(faY);
        float fMaxY = MyFuns.dMax(faY);
        if (!isIntersectToRect(new MyRect(fMinX, fMaxY, fMaxX, fMinY))) return false;
        if (MyFuns.IsPointInRing(left, top, faX, faY)) return true;
        if (MyFuns.IsPointInRing(left, bottom, faX, faY)) return true;
        if (MyFuns.IsPointInRing(right, top, faX, faY)) return true;
        if (MyFuns.IsPointInRing(right, bottom, faX, faY)) return true;

        for (int i = 0; i < len - 1; i++) {
            if (isPointInRect(faX[i], faY[i])) return true;
            if (isLinePassRect(faX[i], faY[i], faX[i + 1], faY[i + 1])) return true;
        }
        if (isPointInRect(faX[len - 1], faY[len - 1])) return true;
        if (isLinePassRect(faX[0], faY[0], faX[len - 1], faY[len - 1])) return true;
        return false;
    }

    private boolean isPointAtLineLeft(float x0, float y0, float x1, float y1, float x2, float y2) {
        if (x1 == x2) {
            if (x0 < x1) return true;
            else return false;
        }
        if (y1 == y2) {
            if (y0 > y1) return true;
            else return false;
        }
        if (x1 > x2 && y1 > y2) {
            float y = y1 - (y1 - y2) * (x1 - x0) / (x1 - x2);
            if (y < y0) return true;
            else return false;
        }
        if (x1 < x2 && y1 < y2) {
            float y = y2 - (y2 - y1) * (x2 - x0) / (x2 - x1);
            if (y < y0) return true;
            else return false;
        }
        if (x1 < x2 && y1 > y2) {
            float y = (y1 - y2) * (x2 - x0) / (x2 - x1) + y2;
            if (y > y0) return true;
            else return false;
        }
        if (x1 > x2 && y1 < y2) {
            float y = (y2 - y1) * (x1 - x0) / (x1 - x2) + y1;
            if (y > y0) return true;
            else return false;
        }
        return false;
    }

    public void Print() {
        System.out.println("left = " + left + ", top = " + top + ", right = " + right + ", bottom = " + bottom);
    }
}