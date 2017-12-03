package com.check.fruit.fruitcheck.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.check.fruit.fruitcheck.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TODO: document your custom view class.
 */
public class CameraFrameView extends View {

    private static final int DEFAULT_SIZE = 300;
    private static final int OFFSET = 100;
    private int viewWidth;
    private int viewHeight;
    private int framePositionX;
    private int framePositionY;
    private int frameWidth;
    private Bitmap frameBackground = null;
    private Bitmap scanLine = null;
    private int scanStartPosition = 0;
    Paint paint = new Paint();
    Paint paint2 = new Paint();
    public CameraFrameView(Context context) {
        super(context);
        init();
    }

    public CameraFrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraFrameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);//设置线宽
        paint.setAlpha(100);

        paint2.setAntiAlias(true);
        paint2.setColor(Color.RED);
        paint2.setStyle(Paint.Style.FILL_AND_STROKE);
        paint2.setStrokeWidth(2);//设置线宽
        paint2.setTextSize(40); //以px为单位
        paint2.setColor(Color.WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.viewWidth > 0) {
            long rectWith = Math.round(Math.min(this.viewWidth, this.viewHeight) * 0.6);
            int range = Math.round(rectWith);
            int left = Math.round(this.viewWidth / 2 - rectWith / 2);
            int top = Math.round(this.viewHeight / 2 - rectWith / 2) - OFFSET;
            if (scanStartPosition == 0) {
                scanStartPosition = top;
            }
            this.framePositionX = left;
            this.framePositionY = top;
            this.frameWidth = range;
            int circleOffset = 20 + 5 + 30;//do not forget circle stroke widh
            int radius = Math.round(range / 2 - circleOffset);
            int startPointX = Math.round(left + range / 2);
            int startPointY = Math.round(top + circleOffset);
            int xOffset = (int) Math.round(radius * Math.sin(Math.PI / 3));
            int yOffset = (int) Math.round(radius * Math.cos(Math.PI / 3));
            //Rect rectTEST =  new Rect(left, top, left+range, top+range);
            //canvas.drawRect(rectTEST, paint);//绘制矩形
            //draw bitmap as crop frame background
            if (frameBackground == null) {
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.qr_code_bg2);
                frameBackground = Bitmap.createScaledBitmap(b, (int) rectWith, (int) rectWith, false);
                if (!b.isRecycled()) {
                    b.recycle();
                }
            }
            if (scanLine == null) {
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.scan_line2);
                scanLine = Bitmap.createScaledBitmap(b, (int) rectWith, 30, false);
                if (!b.isRecycled()) {
                    b.recycle();
                }
            }
            Rect targetRect = new Rect(0, top - 50, this.viewWidth, top);
            String info = "将取景框对准试纸，即可拍照";
            Paint.FontMetricsInt fontMetrics = paint2.getFontMetricsInt();
            // 转载请注明出处：http://blog.csdn.net/hursing
            int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
            // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
            paint2.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(info, targetRect.centerX(), baseline, paint2);

            canvas.drawText("基准", startPointX + 20 + 80, startPointY + 20, paint2);

            canvas.drawBitmap(frameBackground, left, top, paint);

            canvas.drawCircle(startPointX, startPointY, 20, paint);
            canvas.drawCircle(startPointX + xOffset, startPointY + Math.round(radius - yOffset), 20, paint);
            canvas.drawCircle(startPointX + xOffset, startPointY + radius + yOffset, 20, paint);
            canvas.drawCircle(startPointX, startPointY + radius + radius, 20, paint);
            canvas.drawCircle(startPointX - xOffset, startPointY + radius + yOffset, 20, paint);
            canvas.drawCircle(startPointX - xOffset, startPointY + Math.round(radius - yOffset), 20, paint);


            canvas.drawBitmap(scanLine, left, scanStartPosition, paint);
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int width = getMySize(widthMeasureSpec);
        final int height = getMySize(heightMeasureSpec);
        this.viewWidth = width;
        this.viewHeight = height;
        setMeasuredDimension(width, height);
    }



    /**
     * 获取测量大小
     */
    private int getMySize(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;//确切大小,所以将得到的尺寸给view
        } else if (specMode == MeasureSpec.AT_MOST) {
            //默认值为450px,此处要结合父控件给子控件的最多大小(要不然会填充父控件),所以采用最小值
            result = Math.min(DEFAULT_SIZE, specSize);
        } else {
            result = DEFAULT_SIZE;
        }
        return result;
    }



    public int getFramePositionX() {
        return framePositionX;
    }

    public int getFramePositionY() {
        return framePositionY;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getScanStartPosition(){
        return scanStartPosition;
    }

    public void setScanStartPosition(int scanStartPosition){
        this.scanStartPosition = scanStartPosition;
    }

}
