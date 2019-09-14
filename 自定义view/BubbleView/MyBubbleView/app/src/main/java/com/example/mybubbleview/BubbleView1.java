package com.example.mybubbleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class BubbleView1 extends View {
    Paint paint;

    int textWidth;

    int rectHeight =80; //矩形高

    int circleR = rectHeight/2;//圆弧半径

    String textPre ="距离你";

    int textPreWidth;

    String textEnd ="米";

    int textNumWidth;

    String textNum ="";

    String text;

    private int fontSize =30;//字体大小

    private int padding =20;//内容边距

    private int bottomLine =26;//等腰直角三角形底边长

    int width;//view总宽

    int height = padding + rectHeight + bottomLine/2 + padding; //view总高

    private Path path;

    private String distance;
    public BubbleView1(Context context) {
        this(context,null);
    }

    public BubbleView1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BubbleView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void init() {

        resetPaint();

        this.setLayerType(View.LAYER_TYPE_SOFTWARE,paint);

        paint.setShadowLayer(10F,1F,1F,Color.GRAY);

        text = textPre + textNum + textEnd;

        textWidth = (int) paint.measureText(text);

        textPreWidth = (int) paint.measureText(textPre);

        textNumWidth = (int) paint.measureText(textNum);

        path =new Path();

    }
    public String getDistance(){

        return distance;

    }

    public void setText(String textNum){

        this.textNum = textNum;

        init();

        invalidate();

    }

    public void resetPaint() {

        //初始化画笔

        paint =new Paint();

        //设置抗锯齿

        paint.setAntiAlias(true);

        //设置填充

        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        //设置防抖动

        paint.setDither(true);

        paint.setTextSize(fontSize);

    }

    @Override

    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        init();

        drawRect(canvas);

    }

    private void drawRect(Canvas canvas) {

        paint.setColor(Color.parseColor("#ffffff"));

        RectF rectF =new RectF(padding, padding, padding + circleR*2, padding + circleR*2);

        path.arcTo(rectF, 270, -180,false);

        path.lineTo(width/2 - bottomLine/2, padding + circleR*2);
//
        path.lineTo(width/2, padding + circleR*2 + bottomLine/2);
//
        path.lineTo(width/2 + bottomLine/2, padding + circleR*2);
//
        path.lineTo(padding + circleR + textWidth, padding + circleR *2);
//
        RectF rectF1 =new RectF(padding + circleR + textWidth - circleR, padding, padding + circleR + textWidth + circleR, padding + circleR*2);
//
        path.arcTo(rectF1, 90, -180,false);
//
        //path.lineTo(padding + circleR, padding);

        canvas.drawPath(path, paint);

        resetPaint();

        paint.setColor(Color.parseColor("#333333"));

        Rect bounds =new Rect();

        paint.getTextBounds(text, 0, text.length(), bounds);

        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();

        int baseline = (height - bottomLine/2 - fontMetrics.descent + fontMetrics.ascent) /2 - fontMetrics.ascent;

        canvas.drawText(textPre, width/2-textWidth/2, baseline, paint);

        canvas.drawText(textEnd, width/2-textWidth/2 + textPreWidth + textNumWidth, baseline, paint);

        resetPaint();

        paint.setColor(Color.parseColor("#ff9500"));

        canvas.drawText(textNum, width/2-textWidth/2 + textPreWidth, baseline, paint);

    }

    @Override

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = padding + circleR + textWidth + circleR + padding;

        setMeasuredDimension(width, height);
//        mTextWidth = measureSize(widthMeasureSpec);
//        height = measureSize(heightMeasureSpec);
//        setMeasuredDimension(mTextWidth,height);

    }
    private int measureSize(int measureSpec) {
        int result = 200;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(result, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

}
