package com.example.zw.photoalbum.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zw.photoalbum.MyGridView;
import com.example.zw.photoalbum.R;

import java.util.Set;

public class GridViewAdapter extends ArrayAdapter<String> implements AbsListView.OnScrollListener {
    private Set<ImageAsyncTask>taskCollection;
    private MyGridView mGridView;
    private ImageLoader mImageLoader;
    //第一张可见图片的下标
    private int mFirstVisibleItem;
    //一屏有多少张图片可见
    private int mVisibleItemCount;
    private boolean isFirstEnter=true;
    public GridViewAdapter(Context context, int resource, String[] objects,MyGridView gridView) {
        super(context, resource, objects);
        this.mGridView=gridView;
        this.mImageLoader=new ImageLoader();
        mGridView.setOnScrollListener(this);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String imageUrl=getItem(position);
        View view;
        if(convertView==null){
            view=LayoutInflater.from(getContext()).inflate(R.layout.image_item_layout,null);
        }else {
            view=convertView;
        }
        TextView img_item_text=(TextView) view.findViewById(R.id.img_item_text);
        ImageView img_item_content=(ImageView)view.findViewById(R.id.img_item_content);
        view.setTag(imageUrl);
        img_item_text.setText(imageUrl);
        Bitmap bitmap=mImageLoader.getImgFromCache(imageUrl);
        if(bitmap!=null){
            img_item_content.setImageBitmap(bitmap);
        }else {

        }
        return view;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==SCROLL_STATE_IDLE){
            loadBitmaps(mFirstVisibleItem,mVisibleItemCount);
        }else {

        }
    }

    private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
        for(int i=firstVisibleItem;i<firstVisibleItem+visibleItemCount;i++){
            String imgUrl=ImageList.ImageUrls[i];
            Bitmap bitmap=mImageLoader.getImgFromCache(imgUrl);
            //cache中没有，则从网络中下载
            if(bitmap==null){

            }else {

            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem=firstVisibleItem;
        mVisibleItemCount=visibleItemCount;
        if(isFirstEnter &&visibleItemCount>0){
            loadBitmaps(mFirstVisibleItem,mVisibleItemCount);
            isFirstEnter=false;
        }
    }
}
