package com.jade.gesture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jade on 2018/4/27.
 * 九宫格按钮的布局
 */
public class GuestureLayout extends ViewGroup {

    //行,列数---三行三列
    private int lineNumber = 3;

    //列宽
    private int column_space = 0;
    private int Linepading = 0;

    //用一个数组来记录所有按钮的位置
    List<CircleSpace> positions;

    //当前的x,y坐标
    float current_x;
    float current_y;

    //用来记录选中的圆区域
    LinkedHashMap<Integer, CircleSpace> selected;

    //画笔
    Paint paint;

    //连线
    Path path;

    //选中了哪些按钮
    onSelectDone selectListener;

    public GuestureLayout(Context context) {
        this(context, null);
    }

    public GuestureLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setSelectListener(onSelectDone selectListener) {
        this.selectListener = selectListener;
    }


    private void init(Context context) {

        positions = new ArrayList<>();

        int count = lineNumber * lineNumber;
        //一共9个按钮
        for (int i = 0; i < count; i++) {
            GuestureButton btn = new GuestureButton(context);
            addView(btn);

            CircleSpace tmp = new CircleSpace();
            tmp.setIndex(i);
            positions.add(tmp);
        }

        selected = new LinkedHashMap<>();

        //setWillNotDraw(false);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GREEN);
        //Paint.Style.STROKE 只绘制图形轮廓（描边）
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        //设置线冒样式
        paint.setStrokeCap(Paint.Cap.ROUND);
        //设置拐角样式
        paint.setStrokeJoin(Paint.Join.ROUND);

        path = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width_size = MeasureSpec.getSize(widthMeasureSpec);

        //一列的宽度
        column_space = width_size / 3;
        //间隔区域,三个按钮一共四个间隔区域
        Linepading = column_space / 4;

        //按钮的宽度
        int child_width = Linepading * 2;
        int childMeasure = MeasureSpec.makeMeasureSpec(child_width, MeasureSpec.EXACTLY);
        //确定子控件的宽高
        measureChildren(childMeasure, childMeasure);
        setMeasuredDimension(width_size, width_size);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            GuestureButton btn = (GuestureButton) getChildAt(i);
            //列的序号
            int column = i % lineNumber;
            //行的序号
            int line = i / lineNumber;


            int left = column * column_space + Linepading;
            int top = line * column_space + Linepading;
            int right = left + btn.getMeasuredWidth();
            int bottom = top + btn.getMeasuredHeight();

            //调用该方法需要传入放置View的矩形空间左上角left、top值和右下角right、bottom值。这四个值是相对于父控件而言的。
            btn.layout(left, top, right, bottom);

            CircleSpace space = positions.get(i);
            space.setSpace(left, top, right, bottom);
            space.setXAndY((left + right) / 2, (top + bottom) / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(0, 0, 500, 500, paint);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        //把所有的子控件画出来
        super.dispatchDraw(canvas);

        //路径的起点移动到,x,y
        //path.moveTo(x,y);
        //路径连接上x,y
        //path.lineTo(x,y)
        path.reset();

        if (selected.size() > 0) {
            //生成hashMap的遍历器
            Iterator<Map.Entry<Integer, CircleSpace>> iterator = selected.entrySet().iterator();

            //获取第一个点
            Map.Entry<Integer, CircleSpace> fisrt = iterator.next();
            CircleSpace value = fisrt.getValue();
            path.moveTo(value.x, value.y);

            //遍历剩下的所有点
            while (iterator.hasNext()) {
                Map.Entry<Integer, CircleSpace> tmp = iterator.next();

                CircleSpace tmp_value = tmp.getValue();
                path.lineTo(tmp_value.x, tmp_value.y);
            }

            //路径连接到当前的手指的坐标
            path.lineTo(current_x, current_y);

            canvas.drawPath(path, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                current_x = event.getX();
                current_y = event.getY();

                selectCircle(current_x, current_y);

                break;
            case MotionEvent.ACTION_MOVE:
                current_x = event.getX();
                current_y = event.getY();
                selectCircle(current_x, current_y);

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //手抬起来的时候所有状态恢复到最初状态
                //1.回滚按钮状态
                //2.回滚线的状态
                backToNormal();
                break;
        }

        //重绘,在主线程使用.....postInvalidate()在子线程使用
        invalidate();
        return true;
    }

    /**
     * 判断x,y是否在某一个圆的区域内
     */
    private void selectCircle(float x, float y) {
        CircleSpace space = getCircleByXAndY(x, y);
        if (space != null) {
            //点击中了一个按钮
            int index = space.getIndex();

            GuestureButton btn = (GuestureButton) getChildAt(index);
            btn.setSelect();

            //如果已经有选中过的按钮序号,排重,同一个按钮在一次连线中不能被连续选中两次或以上
            if (selected.containsKey(index)) {
                return;
            } else {
                selected.put(index, space);
            }
        }
    }

    private void backToNormal() {
        //遍历器
        Iterator<Map.Entry<Integer, CircleSpace>> iterator = selected.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, CircleSpace> next = iterator.next();
            int index = next.getKey();
            GuestureButton btn = (GuestureButton) getChildAt(index);
            //按钮取消选中状态
            btn.backToNormal();
        }

        if (null != selectListener) {
            selectListener.onSelectEd(getSequen());
        }
        selected.clear();
    }

    /**
     * 拼接被选中的按钮index
     * @return
     */
    private String getSequen() {

        StringBuilder builder = new StringBuilder();

        //遍历
        Iterator<Map.Entry<Integer, CircleSpace>> iterator = selected.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, CircleSpace> next = iterator.next();
            int index = next.getKey();

            builder.append(index);
        }

        return builder.toString();
    }


    private CircleSpace getCircleByXAndY(float x, float y) {
        for (int i = 0; i < positions.size(); i++) {

            CircleSpace space = positions.get(i);

            if (space.isContain(x, y)) {
                return space;
            }
        }
        return null;
    }
}

