package com.jade.view.numrain;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.jade.view.R;

/**
 * Created by Jade on 2018/4/25.
 */
public class NumRain extends LinearLayout {

    private Context mContext;

    private  int normalColor = Color.GREEN;

    private  int lightColor = Color.WHITE;

    private float mTextSize = 15 * getResources().getDisplayMetrics().density;

    public NumRain(Context context) {
        this(context,null);
    }

    public NumRain(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        Log.w("jade","mTextSize--->" + mTextSize);
        init();
        if (null != attrs) {
            parseAttrs(attrs);
        }
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        setBackgroundColor(Color.BLACK);
    }

    /**
     * 自定义属性值
     */
    private void parseAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.NumRain);
        //a.attrs里面的自定义属性名,b.找不到的话默认值
        normalColor = typedArray.getColor(R.styleable.NumRain_normalColor, Color.GREEN);
        lightColor = typedArray.getColor(R.styleable.NumRain_lightColor, Color.RED);
        mTextSize = typedArray.getDimension(R.styleable.NumRain_textSize, mTextSize);

        Log.i("jade","normalColor--->" + normalColor);
        Log.i("jade","lightColor--->" + lightColor);
        Log.i("jade","mTextSize--->" + mTextSize);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
//        int measuredWidth = getMeasuredWidth();
//        int measuredHeight = getMeasuredHeight();
        Log.i("jade","measuredWidth--->" + measuredWidth);
        Log.i("jade","measuredHeight--->" + measuredHeight);
        if(measuredWidth != 0){
            int columnNum = measuredWidth / Math.round(mTextSize);

            for (int i = 0; i < columnNum; i++) {
                //添加item
                NumRainItem numRainItem = new NumRainItem(mContext);
                numRainItem.setNormalColor(normalColor);
                numRainItem.setLightColor(lightColor);
                numRainItem.setTextSize(mTextSize);

                LayoutParams layoutParams = new LayoutParams((int) mTextSize + 10, measuredHeight);
                numRainItem.setStartTime((long)(Math.random() * 2000));

                addView(numRainItem, layoutParams);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
