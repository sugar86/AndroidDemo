package com.sugar.demo.activities;


import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sugar.demo.R;
import com.sugar.demo.utils.LogUtil;

/** 后台播放视频 */
public class VideoBySurfaceView extends Activity implements View.OnClickListener {
    private ImageButton btnplay, btnstop, btnpause;
    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private boolean isSurfaceCreated;
    private int position;/*
    private String url1 = "http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4";
    private String url2 = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";
    private String url3 = "http://42.96.249.166/live/388.m3u8";
    private String url4 = "http://61.129.89.191/ThroughTrain/download.html?id=4035&flag=-org-"; //音频url*/

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_by_surface_view);
        btnplay = (ImageButton) this.findViewById(R.id.btnplay);
        btnstop = (ImageButton) this.findViewById(R.id.btnstop);
        btnpause = (ImageButton) this.findViewById(R.id.btnpause);

        btnstop.setOnClickListener(this);
        btnplay.setOnClickListener(this);
        btnpause.setOnClickListener(this);

        mediaPlayer = new MediaPlayer();
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
        // 设置SurfaceView自己不管理的缓冲区(兼容4.0以下的版本)
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (position > 0) {
                    try {
                        // 开始播放
                        //play();
                        // 并直接从指定位置开始播放
                        //mediaPlayer.seekTo(position);
                        mediaPlayer.setDisplay(surfaceView.getHolder());
                        position = 0;
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });

        // 解决视频拉伸变形
        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener(){
            @Override
            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
                if (width == 0 || height == 0) {
                    LogUtil.e("invalid video width(" + width + ") or height(" + height + ")");
                    return;
                }
                LogUtil.e("video width(" + width + ") or height(" + height + ")");

                //surfaceView.getHolder().setFixedSize(width, height);
                //surfaceView.getHolder().setFixedSize(height, width);
                WindowManager windowManager = (WindowManager) VideoBySurfaceView.this.getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics dm = new DisplayMetrics();
                windowManager.getDefaultDisplay().getMetrics(dm);

                int width_ = dm.widthPixels;// 屏幕宽度（像素）
                int height_= dm.heightPixels; // 屏幕高度（像素）
                float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
                //int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
                // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
                int screenWidth = (int) (width_/density);//屏幕宽度(dp)
                int screenHeight = (int)(height_/density);//屏幕高度(dp)
                LogUtil.e("screenWidth(" + screenWidth + ") or screenHeight(" + screenHeight + ")");
                // surfaceView.getHolder().setFixedSize(0, 720 * 640 / 1280);

                int width__ = Math.max(width, height);
                int height__ = Math.min(width, height);
                // 竖屏
                surfaceView.getHolder().setFixedSize(0, height__ * screenHeight / width__);
                // 横屏
                //surfaceView.getHolder().setFixedSize(0, width__ * screenHeight / height__);
            }
        });/*
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer mp) {
                LogUtil.i("onPrepared ...");
            }
        });*/

        //play();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnplay:
                play();
                break;

            case R.id.btnpause:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
                break;

            case R.id.btnstop:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                break;
            default:
                break;
        }

    }

    /** 进入后台 */
    @Override
    protected void onPause() {
        // 先判断是否正在播放
        if (mediaPlayer.isPlaying()) {
            // 如果正在播放我们就先保存这个播放位置
            position = mediaPlayer.getCurrentPosition();
            // mediaPlayer.stop();
        }
        super.onPause();
    }

    /** 从后台到前台 */
    @Override
    protected void onResume() {
        /*LogUtil.i("onResume called ...");
        new Handler().postDelayed(new Runnable()
        {
            public void run()
            {
                if(isSurfaceCreated)
                {
                    play();
                }
            }
        }, 10);*/
        super.onResume();
    }

    private void play() {
        try {
            LogUtil.i("call play() ...");
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置需要播放的视频
            Uri uri = Uri.parse(VideoByVideoView.url3);
            mediaPlayer.setDataSource(getApplicationContext(), uri);
            // 把视频画面输出到 SurfaceView
            mediaPlayer.setDisplay(surfaceView.getHolder());
            mediaPlayer.prepare();
            // 播放
            mediaPlayer.start();
            Toast.makeText(this, "开始播放！", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
        }
    }

    //Service被关闭之前回调
    @Override
    public void onDestroy() {
        if (mediaPlayer != null)
            mediaPlayer.release();

        LogUtil.e("onDestory方法被调用!");
        super.onDestroy();
    }

}
