package com.example.hongbaoapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class MyDialog extends DialogFragment implements View.OnTouchListener {
    private ImageView mImageView;
    private Button mBtn;
    private static float mScale;
    private int mImageViewHeight;
    private Context mContext;
    private String mDialogTheme="1";
    public MyDialog(Context context) {
        this.mContext=context;
    }

    public MyDialog() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.app.DialogFragment.STYLE_NO_FRAME, R.style.MyDialogStyle);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.dialog_layout,container);
        mImageView=(ImageView)view.findViewById(R.id.imgView);
        switch (mDialogTheme){
            case "1":
                mImageView.setBackground(mContext.getResources().getDrawable(R.drawable.img1));
                break;
            case "2":
                mImageView.setBackground(mContext.getResources().getDrawable(R.drawable.img2));
                break;
            case "3":
                mImageView.setBackground(mContext.getResources().getDrawable(R.drawable.img3));
                break;
        }
        mImageView.setOnTouchListener(this);
        ConstraintLayout.LayoutParams params=(ConstraintLayout.LayoutParams) mImageView.getLayoutParams();
        mImageViewHeight=pix2dip(params.height);
        mBtn=(Button)view.findViewById(R.id.close_btn);
        //mBtn.getLayoutParams().height=pix2dip(120);
        //mBtn.getLayoutParams().width=pix2dip(120);
        ConstraintLayout.LayoutParams layoutParams=(ConstraintLayout.LayoutParams) mBtn.getLayoutParams();
        layoutParams.height=pix2dip(120);
        layoutParams.width=pix2dip(120);
        layoutParams.rightMargin=pix2dip(96);
        layoutParams.topMargin=pix2dip(0);

        mBtn.setLayoutParams(layoutParams);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
    private int pix2dip(float pixValue){
        return (int)(pixValue/mScale+0.5f);
    }
    public void setScale(float scale){
        this.mScale=scale;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float y=event.getY();
                if(pix2dip(y)>mImageViewHeight/2){
                    //下半部分可点击
                    dismiss();
                    //HSCashCenterManager.getInstance().startLotteryWheel(this.mContext);
                }
                break;
        }
        return false;
    }

    public void setDialogTheme(String type){
        this.mDialogTheme=type;
    }

}
