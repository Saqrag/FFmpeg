package com.zuga.ffmpeg.demo;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zuga.ffmpeg.FFmpegUtil;
import com.zuga.log.LogSa;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getThumb(View view) {
        MainActivityPermissionsDispatcher.thumbWithCheck(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void thumb() {
        final String videoPath = Environment.getExternalStoragePublicDirectory("zuga") + "/test.MP4";
        final String thumbDir = Environment.getExternalStoragePublicDirectory("zuga").getAbsolutePath();
        new Thread() {
            @Override
            public void run() {
                int h = 0;
                int m = 0;
                int s = 0;
                for (int i = 0; i < 100; i++) {
                    s++;
                    if (s >= 60) {
                        s = 0;
                        m++;
                    }
                    if (m >= 60) {
                        m = 0;
                        h++;
                    }
                    String outPath = thumbDir;
                    outPath += "/" + i + ".jpg";
                    LogSa.d("videoPath: %s outPath: %s time: %s", videoPath, outPath, String.format("%s:%s:%s", h, m, s));
                    int thumb = FFmpegUtil.getThumb(videoPath, outPath, String.format("%s:%s:%s", h, m, s));
                    if (thumb == 0) {
                        LogSa.d("success");
                    } else {
                        LogSa.e("error");
                    }
                }
            }
        }.start();
    }
}
