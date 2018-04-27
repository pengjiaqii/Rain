package com.jade.gesture;

import android.graphics.RectF;

/**
 * Created by Jade on 2018/4/27.
 * 按钮所占的区域
 */
public class CircleSpace {
    //按钮的圆心
    int x, y;
    //按钮的序号
    int index;
    //按钮的区域
    RectF space;

    public CircleSpace() {
        space = new RectF();
    }

    public void setSpace(int left, int top, int right, int bottom) {
        space.set(left, top, right, bottom);
    }

    //这个坐标是否在当前圆的区域范围内
    public boolean isContain(float x, float y) {
        return space.contains(x, y);
    }

    public void setXAndY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public RectF getSpace() {
        return space;
    }

    public void setSpace(RectF space) {
        this.space = space;
    }
}
