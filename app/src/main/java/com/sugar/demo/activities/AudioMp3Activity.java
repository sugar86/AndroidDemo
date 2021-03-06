package com.sugar.demo.activities;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.sugar.demo.R;
import com.sugar.demo.common.GlobalMethod;
import com.sugar.demo.utils.LogUtil;

/**
 * 播放网络或本地 mp3
 */
public class AudioMp3Activity extends Activity {

    private Context mContext;
    private MediaPlayer mediaPlayer;
    private Button playButton;
    private Button stopButton;
    private int index = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    // @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_mp3);
        mContext = this;
        GlobalMethod.checkPermission(this); // 提前申请权限

        // 申请加入白名单
        try {
            if (!isIgnoringBatteryOptimizations()) {
                requestIgnoreBatteryOptimizations();
            }
        } catch (Throwable e){
            // e.printStackTrace();
        }

        playButton = (Button)findViewById(R.id.playButton);
        stopButton = (Button)findViewById(R.id.stopButton);

        // 播放MP3
        playButton.setOnClickListener((View v) -> {playOrPause(); });

        // 停止
        stopButton.setOnClickListener(view->{
            if(mediaPlayer != null){
                mediaPlayer.stop(); //停止播放
                mediaPlayer.release(); //释放资源
                mediaPlayer = null;
                playButton.setText("播放");
                stopButton.setEnabled(false);
            }
        });

        // 启动就播放音乐
        playOrPause();
    }

    /** 播放MP3 */
    private void playOrPause() {
        if(playButton.getText().toString().equals("暂停")) {
            if(mediaPlayer != null){
                int duration = mediaPlayer.getDuration();
                int pos = mediaPlayer.getCurrentPosition();
                LogUtil.e("duration:"+duration+" pos:"+pos);

                mediaPlayer.pause();
            }
            playButton.setText("播放");
            return;
        }

        if(playButton.getText().toString().equals("播放")) {
            boolean createState = false;
            if(mediaPlayer == null) {
                if (index++ % 2 == 1)
                    //mediaPlayer = createNetMp3();
                    //mediaPlayer = createLocalMp3("/sdcard/remember-me/audio/myhome.mp3");
                    mediaPlayer = createLocalMp3("/sdcard/remember-me/audio/caniusethe.mp4");
                if (mediaPlayer == null)
                    mediaPlayer = createLocalMp3(null);

                if (mediaPlayer == null) {
                    LogUtil.e("not source to play, mediaPlayer is null.");
                    return;
                }

                mediaPlayer.setOnCompletionListener(getOnCompletionListener());
                mediaPlayer.setOnErrorListener(getOnErrorListener());
                createState = true;
            }

            try {
                // 开始播放音频
                mediaPlayer.start();
                playButton.setText("暂停");
            } catch (Exception e) {
                LogUtil.e("Exception.", e);
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
        stopButton.setEnabled(true);
    }

    /** 播放失败 */
    private MediaPlayer.OnErrorListener getOnErrorListener() {
        return new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mediaPlayer.release();
                mediaPlayer = null;
                playButton.setText("播放");
                Toast.makeText(mContext, "出错了，请重试！", Toast.LENGTH_LONG).show();
                return false;
            }
        };
    }

    /**
     * 播放结束
     *
     * 当播放完音频资源时，会触发onCompletion事件，可以在该事件中释放音频资源，
     * 以便其他应用程序可以使用该资源:
     */
    private OnCompletionListener getOnCompletionListener() {
        return mPlayer -> {
            mediaPlayer.release(); //释放音频资源
            mediaPlayer = null;

            stopButton.setEnabled(false);
            playButton.setText("播放");
            setTitle("资源已经被释放了");

            LogUtil.i("自动播放下一首");
            playOrPause();
        };
    }

    /**
     * 创建网络mp3
     */
    public MediaPlayer createNetMp3(){
        //String url = "https://www.zlsaas.com/guide/myhome.mp3";
        String fileName = "/sdcard/remember-me/audio/best.3gpp";
        MediaPlayer mediaPlayer = null;
        try {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(fileName));
            LogUtil.i("准备播放网络 mp3");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return mediaPlayer;
    }

    /**
     * 创建本地MP3
     */
    public MediaPlayer createLocalMp3(String fileName) {
        //String fileName = Environment.getExternalStorageDirectory()+"/remember-me/audio/Recording.m4a";
        if (fileName == null)
            fileName = "/sdcard/remember-me/audio/best.3gpp";

        MediaPlayer mMediaPlayer;
        try {
            // MediaPlayer 可以加载音频，也可以加载视频
            mMediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.fromFile(new File(fileName)));
            if (mMediaPlayer == null) // if the file can not be read.
                return null;
        } catch (Exception e) {
            LogUtil.e("can not load file, fileName:" + fileName, e);
            return null;
        }
        LogUtil.i("准备播放本地资源 mp3");

        mMediaPlayer.setOnCompletionListener(onCompletion -> {
            Toast.makeText(mContext, "播放完成，可以调用下一首了！", Toast.LENGTH_LONG).show();
            // mMediaPlayer.stop();
        });
        return mMediaPlayer;
    }

    /**
     * 判断应用是否在白名单中
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isIgnoringBatteryOptimizations() {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(getPackageName());
        }
        return isIgnoring;
    }

    /**
     * 申请加入白名单
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestIgnoreBatteryOptimizations() {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
