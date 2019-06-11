package com.example.zw.photoalbum.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.icu.util.ValueIterator;
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
        ImageAsyncTask.setImageLoader(this.mImageLoader);
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
            //如果LruCache中没有该图片的缓存则显示默认默片
            img_item_content.setImageResource(R.drawable.ic_launcher_background);
      }
        return view;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //GridView静止时采取下载图片，滑动时则取消所有正在下载的任务
        if(scrollState==SCROLL_STATE_IDLE){
            loadBitmaps(mFirstVisibleItem,mVisibleItemCount);
        }else {
            cancelAllTasks();
        }
    }
    //firstVisibleItem是第一个可见的view下标，visibleItemCount是屏幕中总共可见的view数目
    private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
        for(int i=firstVisibleItem;i<firstVisibleItem+visibleItemCount;i++){
            String imgUrl=ImageList.ImageUrls[i];
            Bitmap bitmap=mImageLoader.getImgFromCache(imgUrl);
            //cache中没有，则从网络中下载
            if(bitmap==null){
                ImageAsyncTask task=new ImageAsyncTask();
                taskCollection.add(task);
                task.execute(imgUrl);
            }else {
                View view=(View)mGridView.findViewWithTag(imgUrl);
                TextView img_item_text=(TextView) view.findViewById(R.id.img_item_text);
                ImageView img_item_content=(ImageView)view.findViewById(R.id.img_item_content);
                img_item_text.setText(imgUrl);
                if(img_item_content!=null && bitmap!=null){
                    img_item_content.setImageBitmap(bitmap);
                }

            }
        }
    }
    public void cancelAllTasks(){
        if(taskCollection!=null){
            for (ImageAsyncTask task:taskCollection){
                task.cancel(false);
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
