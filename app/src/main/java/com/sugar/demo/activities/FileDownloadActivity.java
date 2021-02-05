package com.sugar.demo.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sugar.demo.utils.HttpDownloader;
import com.sugar.demo.R;
import com.sugar.demo.utils.LogUtil;


public class FileDownloadActivity extends Activity {
    private Context mContext;
    HttpDownloader downloader;

    private Button btn_downFile;
    private Button btn_downMP3;

    // private static final String server_url = "http://192.168.2.60";
    private static final String server_url = "http://172.16.3.4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_download);

        mContext = this;
        btn_downFile = (Button)this.findViewById(R.id.button);
        btn_downMP3 = (Button)this.findViewById(R.id.button2);
        btn_downFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloader = new HttpDownloader();
                downloader.checkPermission(mContext);
                String urlStr = server_url+"/hi";
                /*String downloaded = downloader.download(mContext, urlStr);
                if (downloaded == null)
                    Toast.makeText(mContext, "下载文件失败", Toast.LENGTH_SHORT).show();*/
                String folder="/remember-me/text/";
                String fileName="hi.txt";
                new HttpDownloader().downloadFile(mContext, urlStr, folder, fileName);
            }
        });
        btn_downMP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String urlStr = server_url+"/test/myhome.mp3";
                //String urlStr = server_url + "/d";
                String urlStr = "https://www.zlsaas.com/guide/myhome.mp3";
                String folder="/remember-me/audio/";
                String fileName="myhome.mp3";
                new HttpDownloader().downloadFile(mContext, urlStr, folder, fileName);
                //FileUtils.writeLog("log file: "+new Date().toString());
                LogUtil.i("downloadFile main thread exit.");
            }
        });
    }

}
