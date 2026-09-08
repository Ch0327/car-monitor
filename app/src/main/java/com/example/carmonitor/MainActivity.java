package com.chuhe;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
    private TextureView preview1, preview2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 终极解法：纯代码直接创建全屏双画面界面，彻底杜绝 XML 文件丢失导致的报错
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setBackgroundColor(0xFF000000); // 黑色背景
        layout.setWeightSum(2f);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        params.setMargins(2, 2, 2, 2);
        
        preview1 = new TextureView(this);
        preview1.setLayoutParams(params);
        layout.addView(preview1);
        
        preview2 = new TextureView(this);
        preview2.setLayoutParams(params);
        layout.addView(preview2);
        
        // 直接显示纯代码创建的界面
        setContentView(layout);
        
        // 申请必备权限
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                Manifest.permission.CAMERA, 
                Manifest.permission.WRITE_EXTERNAL_STORAGE, 
                Manifest.permission.RECORD_AUDIO
            }, 100);
        }
    }
}
