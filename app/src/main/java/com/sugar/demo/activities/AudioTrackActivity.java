package com.sugar.demo.activities;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sugar.demo.R;

/**
 * 可以使用流来播放，但是只能播放 wav 等无损不需要解码的音乐
 */
public class AudioTrackActivity extends Activity {

    protected PCMAudioTrack m_player;

    private Context mContext;
    private Button playButton;
    private Button stopButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_track);
        mContext = this;
        playButton = (Button)findViewById(R.id.playButton);
        stopButton = (Button)findViewById(R.id.stopButton);

        // 播放MP3
        playButton.setOnClickListener((View v) -> {onBtnStartClick(); });

        // 停止
        stopButton.setOnClickListener(view->{onBtnStopClick(); });
    }

    /** 播放MP3 */
    private void onBtnStartClick() {
        m_player = new PCMAudioTrack();
        m_player.init();
        m_player.start();
    }

    /** 停止 */
    private void onBtnStopClick() {
        m_player.free();
        m_player = null;
    }
}

class PCMAudioTrack extends Thread {
    protected AudioTrack audioTrack;
    protected int m_out_buf_size;
    protected byte[] m_out_bytes;
    protected boolean m_keep_running;

    /*final String FILE_PATH = "/sdcard/";
    final String FILE_NAME = "test.wav";*/

    // Environment.getExternalStorageDirectory()+"/remember-me/audio/myhome.mp3" // 不能播放 mp3 (需要解码器)
    final String FILE_PATH = "/sdcard/remember-me/audio/";
    //final String FILE_NAME = "20201014_073423.m4a"; // 失败
    //final String FILE_NAME = "myhome.mp3"; // 失败
    final String FILE_NAME = "goodjob.wav"; // 成功

    File file;
    FileInputStream audioFileInputStream;

    public void init() {
        try {
            file = new File(FILE_PATH , FILE_NAME);
            file.createNewFile();
            audioFileInputStream = new FileInputStream(file);

            m_keep_running = true;

            m_out_buf_size = AudioTrack.getMinBufferSize(44100,
                    AudioFormat.CHANNEL_CONFIGURATION_STEREO, // CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                    AudioFormat.CHANNEL_CONFIGURATION_STEREO, // CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    m_out_buf_size,
                    AudioTrack.MODE_STREAM);

            m_out_bytes = new byte[m_out_buf_size];

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void free() {
        m_keep_running = false;
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            Log.d("sleep exceptions...\n", "");
        }
    }

    public void run() {
        byte[] bytes_pkg = null;
        audioTrack.play();
        while (m_keep_running) {
            try {
                int readCount = audioFileInputStream.read(m_out_bytes);
                if (readCount <= 0)
                    break;

                // 如果流需要解码，则解码后再输出到 audioTrack
                bytes_pkg = m_out_bytes.clone();
                audioTrack.write(bytes_pkg, 0, bytes_pkg.length);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (audioFileInputStream != null)
                    try {audioFileInputStream.close();} catch (Exception e){}
            }
        }

        audioTrack.stop();
        audioTrack = null;
    }
}
