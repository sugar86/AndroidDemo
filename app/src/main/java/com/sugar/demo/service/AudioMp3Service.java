package com.sugar.demo.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import com.sugar.demo.activities.AudioMp3ByServiceActivity;
import com.sugar.demo.utils.LogUtil;

import java.io.File;
import java.util.Calendar;

public class AudioMp3Service extends Service {

    private int count;
    private boolean quit;

    private MediaPlayer mediaPlayer;

    //必须要实现的方法
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.e("onBind方法被调用!");
        return null;
    }

    //Service被创建时调用
    @Override
    public void onCreate() {
        LogUtil.e("onCreate方法被调用!");
        super.onCreate();

        mediaPlayer = createLocalMp3();
        //创建一个线程动态地修改count的值
        new Thread() {
            public void run() {
                while (!quit) {
                    try {
                        Thread.sleep(1000);
                        if (!mediaPlayer.isPlaying()) {
                            // load next mp3 ...
                            /*Thread.sleep(5000);
                            mediaPlayer.start();*/

                            checkPlayNext();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                    LogUtil.i("count = " + count);
                }
            }
        }.start();
    }

    /** 检查并播放下一首 */
    private static Calendar now = Calendar.getInstance();
    private void checkPlayNext() {
        now.setTimeInMillis(System.currentTimeMillis());
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE);
        if (hour == 7 && min > 20 && min < 50 ||  // 7:50
                hour == 22 // sleeping now
        ) {
            // load next ...
            try {Thread.sleep(6000); } catch (Exception e){}
            mediaPlayer.start();
        }
    }

    //Service被启动时调用
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e("onStartCommand方法被调用!");
        return super.onStartCommand(intent, flags, startId);
    }

    //Service被关闭之前回调
    @Override
    public void onDestroy() {
        if (mediaPlayer != null)
            mediaPlayer.release();

        LogUtil.e("onDestory方法被调用!");
        super.onDestroy();
    }


    /**
     * 创建本地MP3
     */
    public MediaPlayer createLocalMp3() {
        //String fileName = Environment.getExternalStorageDirectory()+"/remember-me/audio/Recording.m4a";
        // String fileName = "/sdcard/remember-me/audio/best.3gpp";
        String fileName = "/sdcard/remember-me/audio/myhome.mp3";

        MediaPlayer mMediaPlayer;
        try {
            mMediaPlayer = MediaPlayer.create(AudioMp3ByServiceActivity.mContext, Uri.fromFile(new File(fileName)));
            if (mMediaPlayer == null) // if the file can not be read.
                return null;
        } catch (Exception e) {
            LogUtil.e("can not load file, fileName:" + fileName, e);
            return null;
        }
        LogUtil.i("准备播放本地资源 mp3");

        mMediaPlayer.setOnCompletionListener(onCompletion -> {
            // Toast.makeText(mContext, "播放完成，可以调用下一首了！", Toast.LENGTH_LONG).show();
            // mMediaPlayer.stop();
        });
        return mMediaPlayer;
    }
}
