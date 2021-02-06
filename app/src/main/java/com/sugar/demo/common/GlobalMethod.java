package com.sugar.demo.common;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;

import androidx.core.app.ActivityCompat;

public class GlobalMethod {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    static final int REQUEST_CODE_CONTACT = 101;
    private static String[] permissions = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    public static void confirm(Context context, String title, String message, DialogInterface.OnClickListener okListener) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        //normalDialog.setIcon(R.drawable.buttom_yello);
        normalDialog.setTitle(title);
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("确定", okListener);

        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(context,"点击了取消！",Toast.LENGTH_LONG).show();
                    }
                });

        // 显示出该对话框
        normalDialog.show();
    }

    /**
     * android 6.0之上的系统除了添加权限还要在你报错的代码前面添加请求权限的代码
     */
    public static void checkPermission(Context mContext) {
        // 如果是主线程下载，要添加下面两行代码
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());

        /*// Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions((Activity)mContext, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }*/

        if (Build.VERSION.SDK_INT >= 23) {
            //验证是否许可权限
            for (String str : permissions) {
                if (mContext.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    ActivityCompat.requestPermissions((Activity)mContext, permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
    }
}
