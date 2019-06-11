package com.example.zw.photoalbum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.zw.photoalbum.utils.GridViewAdapter;

public class MainActivity extends AppCompatActivity {
    private MyGridView mGridView;
    private GridViewAdapter mGridViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       mGridView=(MyGridView)findViewById(R.id.gridView_content);
       mGridViewAdapter=new GridViewAdapter(this,0,null,mGridView);
        mGridView.setAdapter(mGridViewAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGridViewAdapter.cancelAllTasks();
    }
}
