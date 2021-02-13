package com.sugar.demo.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import com.sugar.demo.R;
import com.sugar.demo.common.GlobalMethod;
import com.sugar.demo.service.AudioMp3Service;
import com.sugar.demo.utils.LogUtil;


/**
 * 播放网络或本地 mp3 (台后进程)
 */
public class AudioMp3ByServiceActivity extends Activity {

    public static Context mContext;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_mp3);
        mContext = this;
        GlobalMethod.checkPermission(this); // 提前申请权限

        // 申请加入白名单
        try {
            if (!isIgnoringBatteryOptimizations()) {
                requestIgnoreBatteryOptimizations();
            }
        } catch (Throwable e){
            // e.printStackTrace();
        }

        // 启动后台进程
        final Intent intent = new Intent(AudioMp3ByServiceActivity.this, AudioMp3Service.class);
        startService(intent);
    }

    /**
     * 判断应用是否在白名单中
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isIgnoringBatteryOptimizations() {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(getPackageName());
        }
        return isIgnoring;
    }

    /**
     * 申请加入白名单
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestIgnoreBatteryOptimizations() {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("onDestory 即将销毁时调用!");
    }
    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("onResume ---> 可见可交互时调用");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e("onPause ---> 即将暂停时调用");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.e("onStop ---> 即将停止不可见时调用");
    }
}
