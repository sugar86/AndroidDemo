package com.sugar.demo.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.sugar.demo.common.GlobalMethod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpDownloader {

    /**
     * 读取文本文件
     * @param urlStr url路径
     * @return 文本信息
     * 根据url下载文件，前提是这个文件中的内容是文本，
     * 1.创建一个URL对象
     * 2.通过URL对象，创建一个Http连接
     * 3.得到InputStream
     * 4.从InputStream中得到数据
     */
    public String download(Context mContext, String urlStr) {
        GlobalMethod.checkPermission(mContext);
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader bufferedReader = null;

        try {
            URL url = new URL(urlStr);
            //创建http连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            //使用IO流读取数据
            bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            LogUtil.e("download error. url:" + urlStr, e);
        }
        finally {
            if (bufferedReader != null) try {bufferedReader.close();} catch (IOException e) {e.printStackTrace();}
        }
        LogUtil.e("下载txt文件");
        LogUtil.e(sb.toString());
        return sb.toString();
    }

    /**
     * 读取任何文件
     * 返回-1 ，代表下载失败。返回0，代表成功。返回1代表文件已经存在
     */
    public boolean downloadFile(Context mContext, String url, String folder, String fileName) {
        GlobalMethod.checkPermission(mContext);
        //InputStream input = null;
        try {
            FileUtil fileUtil = new FileUtil();
            if (fileUtil.isFileExist(folder + fileName))
                return false;

            //return fileUtil.downloadByMainThread(folder, fileName, url);

            fileUtil.downloadByOkhttp(folder, fileName, url);
            return true;
        } catch (Throwable e) {
            LogUtil.e("downlaodFile error.", e);
            return false;
        }
    }
}
