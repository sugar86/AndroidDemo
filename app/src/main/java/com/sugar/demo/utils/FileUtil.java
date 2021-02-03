package com.sugar.demo.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
    private String SD_PATH;

    public FileUtil() {}

    public String getSD_PATH() {
        return SD_PATH;
    }

    public FileUtil(String SDPATH){
        //得到外部存储设备的目录（/SDCARD）
        SDPATH = Environment.getExternalStorageDirectory() + "/" ;
    }

    /**
     * 在SD卡上创建文件
     */
    public File createSDFile(String fileName) throws IOException {
        File file = new File(SD_PATH + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     */
    public File createDir(String dirName){
        File dir = new File(SD_PATH + dirName);
        //dir.mkdir();
        dir.mkdirs();
        return dir;
    }

    /**
     * 判断文件是否存在
     */
    public boolean isFileExist(String fileName){
        File file = new File(SD_PATH + fileName);
        return file.exists();
    }

    /** 保存文件 */
    public File write2SDFromInput(String path, String fileName, InputStream input){
        File file = null;
        OutputStream output = null;

        try {
            createDir(path);
            file =createSDFile(path + fileName);
            output = new FileOutputStream(file);
            byte [] buffer = new byte[4 * 1024];
            int readCount;
            while((readCount = input.read(buffer)) != -1){
                output.write(buffer, 0, readCount);
                output.flush();
            }
        } catch (IOException e) {
            LogUtil.e("write2SDFromInput exception", e);
        }
        finally {
            if (output != null) try {output.close();} catch (IOException e) {}
        }
        return file;
    }
}
