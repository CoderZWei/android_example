package com.example.zw.photoalbum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.TextView;

import com.example.zw.photoalbum.utils.GridViewAdapter;
import com.example.zw.photoalbum.utils.ImageList;
import com.example.zw.photoalbum.utils.PermissionUtils;

public class MainActivity extends AppCompatActivity {
    private GridView mGridView;
    private GridViewAdapter mGridViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionUtils.requestPermissionsIfNeed(this);
       mGridView=(GridView) findViewById(R.id.gridView_content);
       mGridViewAdapter=new GridViewAdapter(this,0,ImageList.ImageUrls,mGridView);
        mGridView.setAdapter(mGridViewAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGridViewAdapter.cancelAllTasks();
    }
}
