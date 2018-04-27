package com.jade.gesture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jade on 2018/4/27.
 * 每一个按钮的布局
 */
public class GuestureButton extends View {

    //内圆画笔
    Paint inner_paint;
    //外圆画笔
    Paint out_paint;
    //圆环宽度
    static final int STOKE_WIDTH = 4;

    boolean isSelect = false;

    public GuestureButton(Context context) {
        this(context, null);
    }

    public GuestureButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inner_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        inner_paint.setColor(Color.BLUE);

        out_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        out_paint.setColor(Color.BLUE);
        out_paint.setStyle(Paint.Style.STROKE);
        out_paint.setStrokeWidth(STOKE_WIDTH);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (isSelect) {
            inner_paint.setColor(Color.GREEN);
            out_paint.setColor(Color.GREEN);
        } else {
            inner_paint.setColor(Color.BLUE);
            out_paint.setColor(Color.BLUE);
        }

        //获取到已经测量好的控件的宽度和高度
        int view_width = getMeasuredWidth();
        int view_height = getMeasuredHeight();

        canvas.drawCircle(view_width / 2, view_height / 2, 15, inner_paint);
        canvas.drawCircle(view_width / 2, view_height / 2, view_height / 2 - STOKE_WIDTH / 2, out_paint);
    }


    public void setSelect() {
        isSelect = true;
        invalidate();
    }

    public void backToNormal() {
        isSelect = false;
        invalidate();
    }

}
