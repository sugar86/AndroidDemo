package com.sugar.demo.activities;

import android.app.Activity;
import android.content.Context;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sugar.demo.common.GlobalMethod;
import com.sugar.demo.utils.HttpDownloader;
import com.sugar.demo.R;
import com.sugar.demo.utils.LogUtil;

import java.io.File;
import java.net.URL;

import static android.net.Uri.fromFile;


public class FileDownloadActivity extends Activity {
    private Context mContext;
    HttpDownloader downloader;

    private Button btn_downFile;
    private Button btn_downMP3;
    private Button btn_Play;

    // private static final String server_url = "http://192.168.2.60";
    private static final String server_url = "http://172.16.3.4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_download);

        mContext = this;
        btn_downFile = (Button)this.findViewById(R.id.button);
        btn_downMP3 = (Button)this.findViewById(R.id.button2);
        btn_Play = (Button)this.findViewById(R.id.btnPlay);
        btn_downFile.setOnClickListener(view -> {
            downloader = new HttpDownloader();
            GlobalMethod.checkPermission(mContext);
            String urlStr = server_url+"/hi";
            /*String downloaded = downloader.download(mContext, urlStr);
            if (downloaded == null)
                Toast.makeText(mContext, "下载文件失败", Toast.LENGTH_SHORT).show();*/
            String folder="/remember-me/text/";
            String fileName="hi.txt";
            new HttpDownloader().downloadFile(mContext, urlStr, folder, fileName);
        });
        btn_downMP3.setOnClickListener(view -> {
            //String urlStr = server_url+"/test/myhome.mp3";
            //String urlStr = server_url + "/d";
            String urlStr = "https://www.zlsaas.com/guide/myhome.mp3";
            String folder = "/remember-me/audio/";
            String fileName="myhome.mp3";
            new HttpDownloader().downloadFile(mContext, urlStr, folder, fileName);
            //FileUtils.writeLog("log file: "+new Date().toString());
            LogUtil.i("downloadFile main thread exit.");
        });

        btn_Play.setOnClickListener(view -> {
//            String fileName = Environment.getExternalStorageDirectory()+"/remember-me/audio/myhome.mp3";
            String fileName = Environment.getExternalStorageDirectory()+"/remember-me/audio/Recording.m4a";
            LogUtil.i("start play mp3.");

            //MediaPlayer mMediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.fromFile(new File(fileName)));
            MediaPlayer mMediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse("https://www.zlsaas.com/guide/myhome.mp3"));
            mMediaPlayer.start();

            mMediaPlayer.setOnCompletionListener(onCompletion -> {
                Toast.makeText(mContext, "播放完成，可以调用下一首了！", Toast.LENGTH_LONG).show();
            });

            // AudioTrack audioTrack = new AudioTrack();

            //AudioTrack
            //mMediaPlayer.start();
        });
    }

}
