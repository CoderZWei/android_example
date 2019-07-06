package com.example.wallpaper;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionUtils.requestPermissionsIfNeed(this);
        mBtn=(Button)findViewById(R.id.Btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
//                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
//                        new ComponentName(MainActivity.this,ZwWallpaperService.class));
//                startActivity(intent);

                Intent chooseIntent=new Intent(Intent.ACTION_SET_WALLPAPER);
                Intent intent1=new Intent(Intent.ACTION_CHOOSER);
                intent1.putExtra(Intent.EXTRA_INTENT,chooseIntent);
                intent1.putExtra(Intent.EXTRA_TITLE,"choose");
                startActivity(intent1);

            }
        });

    }
}
