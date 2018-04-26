package com.jade.view.loadview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.jade.view.R;

/**
 * Created by Jade on 2018/4/26.
 */
public class WavingView extends View {

    Paint paint;

    Path path1;
    Path path2;

    int bgw;
    int bgh;
    Bitmap bgBitmap;

    int startY =800;
    int view_width;

    int space;

    int x_distance = 0;
    int view_height =0;

    int wave_height = 300;

    OnLoadProcess listener;

    int lastPercent;
    private int mWidth;
    private int mHeight;

    public WavingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        // 屏幕宽度（像素）
        mWidth = dm.widthPixels;
        // 屏幕高度（像素）
        mHeight = dm.heightPixels;
        init();
    }


    public WavingView(Context context) {
        this(context,null);
    }


    public void setListener(OnLoadProcess listener) {
        this.listener = listener;
    }

    private void init() {
        //获取背景
        Drawable bgDrawable = getBackground();
        bgw = bgDrawable.getIntrinsicWidth();
        bgh = bgDrawable.getIntrinsicHeight();
        bgBitmap = Bitmap.createBitmap(bgw, bgh, Bitmap.Config.ARGB_4444);
        Log.i("jade","bgw-->" + bgw);
        Log.i("jade","bgh-->" + bgh);

        Log.i("jade","屏幕宽-->" + mWidth);
        Log.i("jade","屏幕高-->" + mHeight);
        Canvas canvas = new Canvas(bgBitmap);
        bgDrawable.setBounds(mWidth/2, mHeight/2, bgw/4, bgh/4);
        bgDrawable.draw(canvas);

        //获取画笔
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.paintColor));
        paint.setStyle(Paint.Style.FILL);

        path1 = new Path();
        path2 = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0, height = 0;

        int width_mode = MeasureSpec.getMode(widthMeasureSpec);
        int height_mode = MeasureSpec.getMode(heightMeasureSpec);

        int width_size = MeasureSpec.getSize(widthMeasureSpec);
        int height_size = MeasureSpec.getSize(heightMeasureSpec);

        switch (width_mode) {
            case MeasureSpec.EXACTLY:
                width = width_size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                width = Math.min(bgw, width_size);
                break;
        }

        switch (height_mode) {
            case MeasureSpec.EXACTLY:
                height = height_size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                height = Math.min(bgh, height_size);
                break;
        }

        setMeasuredDimension(width, height);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        startY = h;
        view_width = w;
        view_height = h;
        space = view_width/4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        path1.reset();
        path2.reset();

        path1.moveTo(x_distance,startY);
        path1.cubicTo(space+x_distance,startY+wave_height,3*space+x_distance,startY-wave_height,view_width+x_distance,startY);
        path1.lineTo(view_width+x_distance,view_height);
        path1.lineTo(x_distance,view_height);
        path1.close();

        path2.moveTo(x_distance,startY);
        path2.cubicTo(-space+x_distance,startY-wave_height,-3*space+x_distance,startY+wave_height,-view_width+x_distance,startY);
        path2.lineTo(-view_width+x_distance,view_height);
        path2.lineTo(x_distance,view_height);
        path2.close();

        canvas.drawPath(path1,paint);
        canvas.drawPath(path2,paint);


        x_distance = x_distance+5;

        if(x_distance>=view_width){
            x_distance = 0;
        }

        if(listener!=null){
            float p = (view_height-startY)*1.0f/view_height*1.0f;

            int percent =(int) (p*100) ;

            if(lastPercent!=percent){
                listener.onLoad(percent);
            }


            lastPercent = percent;
        }

        if(startY>=5){
            startY = startY-5;

        }else{
            canvas.drawColor(Color.RED);
            return;
        }

        invalidate();
    }
}
