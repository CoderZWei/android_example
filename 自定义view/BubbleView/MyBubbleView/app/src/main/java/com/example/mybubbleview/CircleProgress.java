package com.example.mybubbleview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CircleProgress extends View {
    private float mCircleRadius;
    private float mStrokeWidth;
    private int mBackgroundColor;
    public int mForegroundColor;
    int mforceColor=R.color.colorPrimary;
    private int mTitleTextColor;
    int mCenterX,mCenterY;
    int mTempPercent=0;
    int mPercent=60;
    private boolean mIsFirstTime=true;
    private Paint mTextPaint=new Paint();
    private Paint mBackgroundPaint=new Paint();
    private Paint mForegroundPaint=new Paint();
    private Paint mImgPaint=new Paint();
    private Rect mTextRect=new Rect();//坐标是int型
    private RectF mFrameRectF=new RectF();//坐标是float型
    private TypedArray typedArray;
    Bitmap bitmap;
    public CircleProgress(Context context) {
        this(context,null);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypedArray(context,attrs);
        initPaint();
//        try {
//            File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"tu.jpg");
//            InputStream is=new FileInputStream(file);
//            bitmap= BitmapFactory.decodeStream(is);
//            is.close();
//            Log.d("zw_img:","success");
//        }catch (IOException e){
//            Log.d("zw_img:","error");
//        }
    }
    public void changemforceColor(){
        mforceColor=R.color.colorAccent;
        mForegroundColor=typedArray.getColor(R.styleable.CircleProgress_foreground_color,getResources().getColor(mforceColor,null));
        initPaint();

    }
    private void initPaint() {
        mBackgroundPaint.setColor(mBackgroundColor);
        //mBorderPaint.setColor(Color.YELLOW);

        //mBorderPaint.setStrokeWidth(mStrokeWidth);
        //mBorderPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setAntiAlias(true);

        mForegroundPaint.setColor(mForegroundColor);
        mForegroundPaint.setStrokeWidth(mStrokeWidth);
        mForegroundPaint.setStyle(Paint.Style.STROKE);
        mForegroundPaint.setStrokeCap(Paint.Cap.ROUND);
        mForegroundPaint.setAntiAlias(true);
        mImgPaint.setColor(Color.GRAY);
        mImgPaint.setStyle(Paint.Style.STROKE);
        mImgPaint.setStrokeWidth(5);

    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        typedArray=context.obtainStyledAttributes(attrs,R.styleable.CircleProgress);
        try {
            mStrokeWidth=typedArray.getDimension(R.styleable.CircleProgress_stroke_width1,getResources().getDimension(R.dimen.circle_progress_default_stroke_width));
            mBackgroundColor=typedArray.getColor(R.styleable.CircleProgress_background_color1,getResources().getColor(R.color.white));
            mForegroundColor=typedArray.getColor(R.styleable.CircleProgress_foreground_color,getResources().getColor(mforceColor));
            mTitleTextColor=typedArray.getColor(R.styleable.CircleProgress_text_color1,getResources().getColor(R.color.colorPrimary));
        }catch (Exception e){
            e.printStackTrace();
        }
        /*finally {
            if(typedArray!=null){
                typedArray.recycle();//销毁
            }
        }*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode=MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize=MeasureSpec.getSize(heightMeasureSpec);
        //AT_MOST：
        if(widthSpecMode==MeasureSpec.AT_MOST && heightSpecMode==MeasureSpec.AT_MOST){
            setMeasuredDimension((int)getResources().getDimension(R.dimen.circle_progress_default_width),
                    (int)getResources().getDimension(R.dimen.circle_progress_default_height));
        }else if(widthMeasureSpec==MeasureSpec.AT_MOST){
            setMeasuredDimension((int)getResources().getDimension(R.dimen.circle_progress_default_width),heightSpecSize);
        }else if(heightMeasureSpec==MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize,(int)getResources().getDimension(R.dimen.circle_progress_default_height));
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int mPaddingStart=getPaddingStart()+(int)(mStrokeWidth/4);
        int mPaddingEnd=getPaddingEnd()+(int)(mStrokeWidth/4);
        int mPaddingTop=getPaddingTop()+(int)(mStrokeWidth/4);
        int mPaddingBottom=getPaddingBottom()+(int)(mStrokeWidth/4);
        int mViewWidth=getWidth()-mPaddingStart-mPaddingEnd;//内部view的宽度
        int mViewHeight=getHeight()-mPaddingTop-mPaddingBottom;//内部view的高度
        mCenterX=mPaddingStart+mViewWidth/2;
        mCenterY=mPaddingTop+mViewHeight/2;
        mCircleRadius=Math.min(mViewHeight,mViewWidth)/2;
        mFrameRectF.set(mCenterX-mCircleRadius,mCenterY-mCircleRadius,
                mCenterX+mCircleRadius,mCenterY+mCircleRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("zw_debug",String.valueOf(getPaddingTop()));
        if(mIsFirstTime){
            startCircleProgressAnim();
            startValueAnim();
            mIsFirstTime=false;
        }
        //draw background
        //中心点X Y 圆的半径
        //canvas.drawRoundRect(mFrameRectF,(mFrameRectF.bottom-mFrameRectF.top)/2,(mFrameRectF.bottom-mFrameRectF.top)/2,mImgPaint);
        canvas.drawCircle(mCenterX,mCenterY,mCircleRadius-mStrokeWidth/2,mBackgroundPaint);

        //Log.d("zw_length:",String.valueOf((mFrameRectF.bottom-mFrameRectF.top)/2));

        //draw foreground,从三点的时针位置开始顺时针画圆弧
        canvas.drawArc(mFrameRectF.left+mStrokeWidth/2,mFrameRectF.top+mStrokeWidth/2,
                mFrameRectF.right-mStrokeWidth/2,mFrameRectF.bottom-mStrokeWidth/2,
                270,(float)(3.6* mTempPercent),false,mForegroundPaint);

        //Canvas imgCanvas=new Canvas(bitmap);

        //mImgPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //imgCanvas.drawBitmap(bitmap,0,0,mImgPaint);
    }
    public void startCircleProgressAnim() {
        ValueAnimator anim=ValueAnimator.ofInt(0,mPercent);
        anim.setDuration(500);
        anim.setInterpolator(new LinearInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTempPercent=(int)animation.getAnimatedValue();
                Log.i("zw:temppercent_",String.valueOf(mTempPercent));
                invalidate();//刷新UI，调用ondraw()
            }
        });
        anim.start();
    }
    private void startValueAnim() {
        //ValueAnimator anim=ValueAnimator.ofInt(0,mValue);
    }

}
