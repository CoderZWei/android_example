//package com.example.mybubbleview;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.LinearGradient;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.graphics.Rect;
//import android.graphics.RectF;
//import android.graphics.Shader;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.View;
//
//import androidx.annotation.Nullable;
//
//public class BubbleView extends View {
//    Paint mTextPaint;
//    Paint mBorderPaint;
//    Paint mContentPaint;
//
//
//    private int mOutPadding;//内容边距
//    //内部字体padding
//    private int mInPadding;
//
//    int mTextWidth;//字体总宽
//    int mTextHeight;//字体总高
//
//    int mRadius;
//
//    private Path mPath;
//
//
//    private int mBackgroundColor;
//    private int mTextColor;
//    private float mStrokeWidth;
//    private String mText;
//
//    private float mTextSize;
//    private float mTextLength;
//    private boolean mIsArrowUp;
//    private int mArrowWidth = 36;
//    private int mArrowHeight = 30;
//
//    private Rect textRect = new Rect();
//
//    public BubbleView(Context context) {
//        this(context, null);
//    }
//
//    public BubbleView(Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public BubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initTypedArray(context, attrs);
//        initPaint();
//    }
//
//    private void initTypedArray(Context context, AttributeSet attrs) {
//        if (attrs != null) {
//            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BubbleView);
//            mBackgroundColor = typedArray.getColor(R.styleable.BubbleView_background_color, getResources().getColor(R.color.colorAccent));
//            mTextColor = typedArray.getColor(R.styleable.BubbleView_text_color, getResources().getColor(R.color.black));
//            mStrokeWidth = typedArray.getDimension(R.styleable.BubbleView_stroke_width, getResources().getDimension(R.dimen.stroke_width));
//            Log.d("zw_debug",String.valueOf(mStrokeWidth));
//            mText = typedArray.getString(R.styleable.BubbleView_text_content);
//            mTextSize = typedArray.getDimension(R.styleable.BubbleView_text_size, getResources().getDimension(R.dimen.default_text_size));
//            mIsArrowUp = typedArray.getBoolean(R.styleable.BubbleView_is_arrow_up, true);
//            mInPadding = typedArray.getDimensionPixelOffset(R.styleable.BubbleView_padding_in, 0);
//        }
//    }
//
//    private void initPaint() {
//        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        //mTextPaint.setAntiAlias(true);
//        mTextPaint.setColor(mTextColor);
//        //mTextPaint.setStrokeWidth(mStrokeWidth);
//        mTextPaint.setStyle(Paint.Style.FILL);
//        mTextPaint.setTextSize(mTextSize);
//
//        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mBorderPaint.setColor(mBackgroundColor);
//        //mBorderPaint.setStyle(Paint.Style.FILL);
//        mBorderPaint.setStyle(Paint.Style.STROKE);
//        mBorderPaint.setShadowLayer(10F, 1F, 1F, Color.GRAY);
//        mBorderPaint.setStrokeWidth(mStrokeWidth);
//
//        mContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mContentPaint.setColor(getResources().getColor(R.color.white));
//        mContentPaint.setStyle(Paint.Style.FILL);
//
//
//        mTextLength = mTextPaint.measureText(mText); //mTextPaint.getTextBounds();
//        //mTextPaint.getTextSize()
//
//        mTextPaint.getTextBounds(mText, 0, mText.length(), textRect);
//
//        mOutPadding = getPaddingTop()+(int)(mStrokeWidth+1)*2;
//        mRadius = textRect.height() / 2 + mInPadding;
//        //mTextWidth=textRect.mTextWidth()+2*mOutPadding;
//        mTextWidth = textRect.width();
//        mTextHeight = textRect.height();
//
//        LinearGradient linearGradient = new LinearGradient(mOutPadding, mOutPadding + mInPadding + mArrowHeight + mRadius, mOutPadding + mRadius * 2 + mTextWidth, mOutPadding + mInPadding + mArrowHeight + mRadius, getResources().getColor(R.color.start_color), getResources().getColor(R.color.end_color), Shader.TileMode.CLAMP);
//        mBorderPaint.setShader(linearGradient);
//
//        //this.setLayerType(View.LAYER_TYPE_SOFTWARE,paint);
//
//        mPath = new Path();
//
//    }
//
//
//    public void setText(String text) {
//
//        this.mText = text;
//        invalidate();
//
//    }
//
//
//    @Override
//
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        drawRect(canvas);
//
//    }
//
//    //    private void drawRect(Canvas canvas) {
////        RectF rectLeftCircle = new RectF(mOutPadding, mOutPadding + mArrowHeight, mOutPadding + mRadius * 2, mOutPadding + mRadius * 2 + mArrowHeight);
////        mPath.arcTo(rectLeftCircle, 270, -180, false);//左边圆形
////        mPath.lineTo(mOutPadding + mRadius + mTextWidth, mOutPadding + mRadius * 2 + mArrowHeight);
////        RectF rectRightCircle = new RectF(mOutPadding + mTextWidth, mOutPadding + mArrowHeight, mOutPadding + mTextWidth + mRadius * 2, mOutPadding + mRadius * 2 + mArrowHeight);
////        mPath.arcTo(rectRightCircle, 90, -180, false);
////        mPath.close();
////
////        if (mIsArrowUp) {
////            mPath.moveTo(mOutPadding + mRadius + mTextWidth / 2 - mArrowWidth / 2, mOutPadding + mArrowHeight);
////            mPath.lineTo(mOutPadding + mRadius + mTextWidth / 2, mOutPadding);
////            mPath.lineTo(mOutPadding + mRadius + mTextWidth / 2 + mArrowWidth / 2, mOutPadding + mArrowHeight);
////            mPath.close();
////            //canvas.drawPath(mPath, mBorderPaint);
////        } else {
////            mPath.moveTo(mOutPadding + mRadius + mTextWidth / 2 - mArrowWidth / 2, mOutPadding + mArrowHeight + mInPadding * 2 + mRadius * 2);
////            mPath.lineTo(mOutPadding + mRadius + mTextWidth / 2, mOutPadding + mArrowHeight + mInPadding * 2 + mRadius * 2 + mArrowHeight);
////            mPath.lineTo(mOutPadding + mRadius + mTextWidth / 2 + mArrowWidth / 2, mOutPadding + mArrowHeight + mInPadding * 2 + mRadius * 2);
////            mPath.close();
////
////            //canvas.drawPath(mPath, mBorderPaint);
////        }
////        canvas.drawPath(mPath, mContentPaint);
////        canvas.drawPath(mPath, mBorderPaint);
////
////        canvas.drawText(mText, mOutPadding + mRadius, mOutPadding + mArrowHeight + mTextHeight + mInPadding, mTextPaint);
////        mIsArrowUp = false;
////
////    }
//    private void drawRect(Canvas canvas) {
//        mIsArrowUp=true;
//        if (mIsArrowUp) {
//            RectF rectLeftCircle = new RectF(mOutPadding, mOutPadding + mArrowHeight, mOutPadding + mRadius * 2, mOutPadding + mRadius * 2 + mArrowHeight);
//            mPath.arcTo(rectLeftCircle, 270, -180, false);//左边圆形
//            mPath.lineTo(mOutPadding + mRadius + mTextWidth, mOutPadding + mRadius * 2 + mArrowHeight);
//            RectF rectRightCircle = new RectF(mOutPadding + mTextWidth, mOutPadding + mArrowHeight, mOutPadding + mTextWidth + mRadius * 2, mOutPadding + mRadius * 2 + mArrowHeight);
//            mPath.arcTo(rectRightCircle, 90, -180, false);
//
//            mPath.lineTo(mOutPadding + mRadius + mTextWidth / 2 + mArrowWidth / 2, mOutPadding + mArrowHeight);
//            mPath.lineTo(mOutPadding + mRadius + mTextWidth / 2, mOutPadding);
//            mPath.lineTo(mOutPadding + mRadius + mTextWidth / 2 - mArrowWidth / 2, mOutPadding + mArrowHeight);
//            mPath.close();
//            canvas.drawPath(mPath, mContentPaint);
//            canvas.drawPath(mPath, mBorderPaint);
//            canvas.drawText(mText, mOutPadding + mRadius, mOutPadding + mArrowHeight + mTextHeight + mInPadding, mTextPaint);
//            //canvas.drawPath(mPath, mBorderPaint);
//        } else {
//            RectF rectLeftCircle = new RectF(mOutPadding, mOutPadding , mOutPadding + mRadius * 2, mOutPadding + mRadius * 2 );
//            mPath.arcTo(rectLeftCircle, 270, -180, false);//左边圆形
//            mPath.lineTo(mOutPadding + mRadius + mTextWidth / 2 - mArrowWidth / 2, mOutPadding  + mRadius * 2);
//            mPath.lineTo(mOutPadding + mRadius + mTextWidth / 2, mOutPadding + mArrowHeight  + mRadius * 2 );
//            mPath.lineTo(mOutPadding + mRadius + mTextWidth / 2 + mArrowWidth / 2, mOutPadding  + mRadius * 2);
//            RectF rectRightCircle = new RectF(mOutPadding + mTextWidth, mOutPadding , mOutPadding + mTextWidth + mRadius * 2, mOutPadding + mRadius * 2 );
//            mPath.arcTo(rectRightCircle, 90, -180, false);
//            mPath.close();
//            canvas.drawPath(mPath, mContentPaint);
//            canvas.drawPath(mPath, mBorderPaint);
//            canvas.drawText(mText, mOutPadding + mRadius, mOutPadding  + mTextHeight + mInPadding, mTextPaint);
//            //canvas.drawPath(mPath, mBorderPaint);
//        }
//
//
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int minimumWidth = getSuggestedMinimumWidth();
//        int minimumHeight = getSuggestedMinimumHeight();
//        int width = measureWidth(minimumWidth, widthMeasureSpec);
//        int height = measureHeight(minimumHeight, heightMeasureSpec);
//        setMeasuredDimension(width, height);
//    }
//
//    private int measureWidth(int defaultWidth, int measureSpec) {
//        int specMode = MeasureSpec.getMode(measureSpec);
//        int specSize = MeasureSpec.getSize(measureSpec);
//        switch (specMode) {
//            //wrap_content
//            case MeasureSpec.AT_MOST:
//                defaultWidth = mRadius * 2 + mTextWidth + mOutPadding *2 ;
//                break;
//            //match_parent and exactly_size
//            case MeasureSpec.EXACTLY:
//                defaultWidth = specSize;
//            case MeasureSpec.UNSPECIFIED:
//                defaultWidth = Math.max(defaultWidth, specSize);
//        }
//        return defaultWidth;
//    }
//
//    private int measureHeight(int defaultHeight, int measureSpec) {
//        int specMode = MeasureSpec.getMode(measureSpec);
//        int specSize = MeasureSpec.getSize(measureSpec);
//        switch (specMode) {
//            //wrap_content
//            case MeasureSpec.AT_MOST:
//                defaultHeight = mRadius * 2 + 2 * mInPadding +2* mOutPadding +mArrowHeight;
//                break;
//            //match_parent and exactly_size
//            case MeasureSpec.EXACTLY:
//                defaultHeight = specSize;
//            case MeasureSpec.UNSPECIFIED:
//                defaultHeight = Math.max(defaultHeight, specSize);
//        }
//        return defaultHeight;
//    }
//}
