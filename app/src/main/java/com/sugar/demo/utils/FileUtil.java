package com.sugar.demo.utils;

import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FileUtil {
    private String SD_PATH;

    public FileUtil() {
        SD_PATH = Environment.getExternalStorageDirectory() + "/" ;
    }

    public String getSD_PATH() {
        return SD_PATH;
    }

    /**
     * 在SD卡上创建文件
     */
    public File createSDFile(String fileName) throws IOException {
        File file = new File(getSD_PATH() + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     */
    public File createDir(String dirName){
        File dir = new File(SD_PATH + dirName);
        if (dir.exists())
            return dir;
        dir.mkdir();
        dir.mkdirs();
        return dir;
    }

    /**
     * 判断文件是否存在
     */
    public boolean isFileExist(String fileName){
        File file = new File(SD_PATH + fileName);
        if (file.exists())
            file.delete();
        //return file.exists();
        return false;
    }

    /** get InputStream */
    public InputStream getInputStearmFormUrl(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        InputStream input = urlConn.getInputStream();
        return input;
    }

    /** 保存文件 */
    public boolean downloadByMainThread(String folder, String fileName, String urlStr){
        File file = null;
        OutputStream output = null;
        InputStream input = null;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            input = urlConn.getInputStream();

            createDir(folder);
            file =createSDFile(folder + fileName);
            output = new FileOutputStream(file);
            byte [] buffer = new byte[4 * 1024];
            int readCount;
            while((readCount = input.read(buffer)) != -1){
                output.write(buffer, 0, readCount);
                output.flush();
            }
        } catch (Exception e) {
            LogUtil.e("write2SDFromInput exception", e);
            return false;
        }
        finally {
            if (output != null) try {output.close();} catch (IOException e) {}
            if (input != null) try {input.close();} catch (IOException e) {}
        }
        LogUtil.i("download file end.");
        return true;
    }

    public void downloadByOkhttp(String folder, String fileName, String url) {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            /** 下载失败 */
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("DOWNLOAD download failed", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    //File file = new File(folder, fileName);
                    createDir(folder);
                    //File file = createSDFile(folder + fileName);
                    File file = new File(savePath, folder + fileName);
                    fos = new FileOutputStream(file);
                    //long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        fos.flush();
                        //sum += len;
                        //int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
//                        listener.onDownloading(progress);
                    }
                    // 下载完成
//                    listener.onDownloadSuccess();
                    LogUtil.i("DOWNLOAD download success");
                } catch (Exception e) {
                    e.printStackTrace();
//                    listener.onDownloadFailed();
                    LogUtil.i("DOWNLOAD download failed");
                    throw e;
                } finally {
                    try { if (is != null) is.close(); } catch (IOException e) { throw e; }
                    try { if (fos != null) fos.close(); } catch (IOException e) { throw e; }
                }
            }
        });
    }
}
