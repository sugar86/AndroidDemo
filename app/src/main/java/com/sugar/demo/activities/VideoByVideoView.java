package com.sugar.demo.activities;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.sugar.demo.R;
import com.sugar.demo.utils.LogUtil;

/** 使用 VideoView 来播放视频 */
public class VideoByVideoView extends AppCompatActivity {

    private Button go;
    private EditText url;
    private Button button;
    private VideoView videoview;
    private MediaController mMediaController;
    /*private String url1 = "http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4";
    private String url2 = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";
    private String url3 = "http://42.96.249.166/live/270.m3u8";
    private String url4 = "http://61.129.89.191/ThroughTrain/download.html?id=4035&flag=-org-";//音频url*/

    //public static String url3 = "http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4";
    public static String url3 = "/sdcard/remember-me/audio/caniusethe.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_by_video_view);
        go = (Button) findViewById(R.id.go);
        url = (EditText) findViewById(R.id.url);
        button = (Button) findViewById(R.id.play);
        videoview = (VideoView) findViewById(R.id.video);

        mMediaController = new MediaController(this, false);
        videoview.setMediaController(mMediaController);

        url.setText(url3);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loadView(url.getText().toString());
            }
        });

        go.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoByVideoView.this, VideoBySurfaceView.class);
                startActivity(intent);
            }
        });
    }


    public void loadView(String path) {
        Uri uri = Uri.parse(path);
        videoview.setVideoURI(uri);
        videoview.start();

        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //         mp.setLooping(true);
                mp.start();// 播放
                Toast.makeText(VideoByVideoView.this, "开始播放！", Toast.LENGTH_LONG).show();
            }
        });

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(VideoByVideoView.this, "播放完毕", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /** 进入后台 */
    @Override
    protected void onPause() {
        // 先判断是否正在播放
        if (videoview.isPlaying()) {
            // 如果正在播放我们就先保存这个播放位置
            // position = videoview.getCurrentPosition();
            // mediaPlayer.stop();
        }
        super.onPause();
    }

    /** 从后台到前台 */
    @Override
    protected void onResume() {
        LogUtil.i("onResume called ...");
        super.onResume();
    }
}
