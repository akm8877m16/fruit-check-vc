package com.check.fruit.fruitcheck.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.check.fruit.fruitcheck.R;

import static java.lang.Math.round;

/**
 * TODO: document your custom view class.
 */
public class circleView extends View {
    public static final  float RADIUS = 25;
    private float currentX = 40;
    private float currentY = 50;
    private float xMax = 0;
    private float yMax = 0;
    private float xMin = 0;
    private float yMin = 0;
    private float xRange;
    private float yRange;
    private int tagName = 0; //0,1,2,3,4,5
    // 定义、创建画笔
    Paint p = new Paint();
    Paint p2 = new Paint();
    public circleView(Context context) {
        super(context);
        init();
    }

    public circleView(Context context,float centerX, float centerY,int tagName,float xRange,float yRange) {
        super(context);
        this.currentX = centerX;
        this.currentY = centerY;
        this.tagName = tagName;
        this.xRange = xRange;
        this.yRange = yRange;
        init();
    }

    public circleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public circleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        super.onDraw(canvas);
        // 绘制一个小圆（作为小球）
        canvas.drawCircle(currentX, currentY, RADIUS, p);
        if(currentX > RADIUS*3){
            canvas.drawText(String.valueOf(tagName),currentX-RADIUS-40,currentY,p2);
        }else{
            canvas.drawText(String.valueOf(tagName),currentX+RADIUS+20,currentY,p2);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // 当前组件的currentX、currentY两个属性
        float xTouch = event.getX();
        float yTouch = event.getY();
            if(isCircleTouched(xTouch,yTouch)){
                if(xTouch < xMin){
                    xTouch = xMin;
                }else if(xTouch > xMax){
                    xTouch = xMax;
                }
                if(yTouch < yMin){
                    yTouch = yMin;
                }else if(yTouch > yMax){
                    yTouch = yMax;
                }

                this.currentX = xTouch;
                this.currentY = yTouch;
                // 通知改组件重绘
                this.invalidate();
                // 返回true表明处理方法已经处理该事件
                return true;
            }


        return false;
    }

    private void init() {
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.GREEN);  //green
        p.setStrokeWidth(3);


        p2.setStyle(Paint.Style.FILL_AND_STROKE);
        p2.setColor(Color.GREEN);  //green
        p2.setStrokeWidth(2);
        p2.setTextSize(50);

        xMin = round(RADIUS/2+2);  // radius + stroke width
        xMax = xRange - round(RADIUS/2+2);
        yMin = round(RADIUS/2+2);
        yMax = yRange-round(RADIUS/2+2);
    }

    private boolean isCircleTouched(float xTouch,float yTouch) {
        return ((currentX - xTouch) * (currentX - xTouch) + (currentY - yTouch) * (currentY - yTouch) <= RADIUS*RADIUS);
    }



    public float getCurrentX() {
        return currentX;
    }

    public float getCurrentY() {
        return currentY;
    }

    public int getTagName() {
        return  tagName;
    }


    public void setxMax(float xMax) {
        this.xMax = xMax;
    }

    public void setyMax(float yMax) {
        this.yMax = yMax;
    }

    public void setxMin(float xMin) {
        this.xMin = xMin;
    }

    public void setyMin(float yMin) {
        this.yMin = yMin;
    }
}
