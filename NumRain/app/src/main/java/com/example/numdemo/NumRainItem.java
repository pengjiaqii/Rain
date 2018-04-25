package com.example.numdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Jade on 2018/4/25.
 */

public class NumRainItem extends View {

    private Paint mPaint;

    private int normalColor = Color.RED;

    private int lightColor = Color.YELLOW;

    private float mTextSize = 15 * getResources().getDisplayMetrics().density;

    private Long startTime = 0L;

    private float nowHeight = 0f;

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        if(isAttachedToWindow()){
            postInvalidate();
        }
    }

    public void setLightColor(int lightColor) {
        this.lightColor = lightColor;
        if(isAttachedToWindow()){
            postInvalidate();
        }
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
        if(isAttachedToWindow()){
            postInvalidate();
        }
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
        if(isAttachedToWindow()){
            postInvalidate();
        }
    }

    private int hightLightNumIndex = 0;

    public NumRainItem(Context context) {
        this(context, null);
    }

    public NumRainItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (attrs != null) {
            parseAttrs(context, attrs);
        }
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumRainItem);
        normalColor = typedArray.getColor(R.styleable.NumRainItem_normalColor, Color.GREEN);
        lightColor = typedArray.getColor(R.styleable.NumRainItem_lightColor, Color.RED);
        startTime = Long.valueOf(typedArray.getInt(R.styleable.NumRainItem_startOffset, 0));
        mTextSize = typedArray.getDimension(R.styleable.NumRainItem_textSize, mTextSize);

        Log.e("jade", "normalColor--->" + normalColor);
        Log.e("jade", "lightColor--->" + lightColor);
        Log.e("jade", "startTime--->" + startTime);
        Log.e("jade", "mTextSize--->" + mTextSize);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(normalColor);

        if (isShowAllNumber()) {
            drawTotalNums(canvas);
        } else {
            drawPartNums(canvas);
        }

    }

    private boolean isShowAllNumber() {
        return nowHeight >= getHeight();
    }

    private void drawPartNums(Canvas canvas) {
        int count = (int) (nowHeight / mTextSize);

        nowHeight += mTextSize;

        drawNumbers(canvas, count);

    }

    private void drawTotalNums(Canvas canvas) {
        int count = (int) (getHeight() / mTextSize);

        drawNumbers(canvas, count);
    }

    private void drawNumbers(Canvas canvas, int count) {
        if (count == 0) {
            postInvalidateDelayed(startTime);
        } else {
            float offset = 0f;
            for (int i = 0; i < count; i++) {

                String value = Integer.toString((int) (Math.random() * 9));

                if (hightLightNumIndex == i) {
                    mPaint.setColor(lightColor);
                    mPaint.setShadowLayer(10f, 0f, 0f, lightColor);

                } else {
                    mPaint.setColor(normalColor);
                    mPaint.setShadowLayer(10f, 0f, 0f, normalColor);
                }

                canvas.drawText(value, 0f, mTextSize + offset, mPaint);
                offset += mTextSize;
            }
        }

        if (!isShowAllNumber()) {
            hightLightNumIndex++;
        } else {
            hightLightNumIndex = (++hightLightNumIndex) % count;
        }
        postInvalidateDelayed(100L);
    }
}

