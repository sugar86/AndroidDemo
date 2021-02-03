package com.sugar.demo.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sugar.demo.utils.HttpDownloader;
import com.sugar.demo.R;


public class FileDownloadActivity extends Activity {
    private Context mContext;
    HttpDownloader downloader;

    private Button btn_downFile;
    private Button btn_downMP3;

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
                String urlStr = "http://172.17.54.91:8080/download/down.txt";
                downloader.download(urlStr);
            }
        });
        btn_downMP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlStr="http://192.168.43.231:8767/downloadMedia";
                String path="misc/audio";
                String fileName="my home looks neat.mp3";
                HttpDownloader httpDownloader = new HttpDownloader();
                int downloaded = httpDownloader.downloadFile(urlStr, path, fileName);
                if (downloaded != 1)
                    Toast.makeText(mContext, "下载文件失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
