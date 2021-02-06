package com.sugar.demo.activities;

import com.sugar.demo.R;
import com.sugar.demo.common.GlobalMethod;
import com.sugar.demo.utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/** 录音 */
public class AudioRecordActivity extends Activity {

    private Context mContext;
    private View btnRecord;
    private View btnStop;
    private View btnPlay;
    private View btnDelete;
    private ListView recFileNameListView;
    private String strTempFile = "ex07_11_";
    private File myRecAudioFile;
    private File myRecAudioDir;
    private File myPlayFile;
    private MediaRecorder mMediaRecorder01;

    private ArrayList<String> recordFiles;
    private ArrayAdapter<String> adapter;
    private TextView myOperationInfoView;
    private boolean sdCardExit;
    private boolean isStopRecord;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_record);
        mContext = this;
        GlobalMethod.checkPermission(this); // 提前检查权限

        btnRecord = findViewById(R.id.btnRecord);
        btnStop = findViewById(R.id.btnStop);
        btnPlay = findViewById(R.id.btnPlay);
        btnDelete = findViewById(R.id.btnDelete);
        recFileNameListView = (ListView) findViewById(R.id.recFileNameList);
        myOperationInfoView = (TextView) findViewById(R.id.myOperationInfo);
        btnStop.setEnabled(false);
        btnPlay.setEnabled(false);
        btnDelete.setEnabled(false);

        /* 判断SD Card是否插入 */
        sdCardExit = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        /* 取得SD Card路径作为录音的文件位置 */
        if (sdCardExit)
            myRecAudioDir = Environment.getExternalStorageDirectory();

        /* 取得SD Card目录里的所有.amr文件 */
        getRecordFiles();

        adapter = new ArrayAdapter<String>(this, R.layout.audio_record_item, recordFiles);
        /* 将 ArrayAdapter添加ListView对象中 */
        recFileNameListView.setAdapter(adapter);

        /* 录音 */
        btnRecord.setOnClickListener((View arg0) -> {
            onBtnRecordClick();
        });

        /* 停止录音 */
        btnStop.setOnClickListener((View arg0) -> {
            onBtnStopClick();
        });

        /* 播放 */
        btnPlay.setOnClickListener((View arg0) -> {
            if (myPlayFile != null && myPlayFile.exists()) {
                /* 打开播放的程序 */
                openFile(myPlayFile);
            }
        });

        /* 删除 */
        btnDelete.setOnClickListener((View arg0)-> {
            if (myPlayFile != null)
            {
                GlobalMethod.confirm(mContext,
                "删除", "确定要删除录音文件："+myPlayFile.getName(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doDeleteFile();
                    }
                });
            }
        });

        /** 录音文件被选择中 */
        recFileNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View checkedView,
                                    int arg2, long arg3)
            {
                onFileItemClick((CheckedTextView) checkedView);
            }
        });

    }

    /** 删除录音文件 */
    private void doDeleteFile() {
        /* 先将Adapter删除文件名 */
        adapter.remove(myPlayFile.getName());
        /* 删除文件 */
        if (myPlayFile.exists())
            myPlayFile.delete();
        myOperationInfoView.setText("完成删除");
    }


    /** 开始录音 */
    private void onBtnRecordClick() {
        try
        {
            if (!sdCardExit) {
                Toast.makeText(AudioRecordActivity.this, "请插入SD Card",
                        Toast.LENGTH_LONG).show();
                return;
            }

            /* 创建录音频文件 */
            myRecAudioFile = File.createTempFile(strTempFile, ".amr", myRecAudioDir);
            mMediaRecorder01 = new MediaRecorder();
            /* 设置录音来源为麦克风 */
            mMediaRecorder01.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder01.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mMediaRecorder01.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mMediaRecorder01.setOutputFile(myRecAudioFile.getAbsolutePath());
            mMediaRecorder01.prepare();
            mMediaRecorder01.start();
            myOperationInfoView.setText("录音中");
            btnStop.setEnabled(true);
            btnPlay.setEnabled(false);
            btnDelete.setEnabled(false);
            isStopRecord = false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /** 停止录音 */
    private void onBtnStopClick() {
        if (myRecAudioFile != null) {
            /* 停止录音 */
            mMediaRecorder01.stop();
            mMediaRecorder01.release();
            mMediaRecorder01 = null;
            /* 将录音频文件名给 Adapter */
            adapter.add(myRecAudioFile.getName());
            myOperationInfoView.setText("停止录音：" + myRecAudioFile.getName());
            btnStop.setEnabled(false);
            isStopRecord = true;
        }
    }

    private static long pre_click_ms = 0;
    /** 录音文件被选择中 */
    private void onFileItemClick(CheckedTextView checkedFile) {
        btnPlay.setEnabled(true);
        btnDelete.setEnabled(true);

        // 双击则直接打开文件
        if (System.currentTimeMillis() - pre_click_ms < 500 &&
                myPlayFile != null && myPlayFile.getName().equals(checkedFile.getText())
        ) {
            openFile(myPlayFile);
            LogUtil.i("open file:"+myPlayFile);
        }

        // 保存当前选择的文件名称
        myPlayFile = new File(myRecAudioDir.getAbsolutePath()
                + File.separator
                + checkedFile.getText());
        myOperationInfoView.setText("你选的是：" + checkedFile.getText());

        pre_click_ms = System.currentTimeMillis();
    }

    @Override
    protected void onStop()
    {
        if (mMediaRecorder01 != null && !isStopRecord)
        {
            /* 停止录音 *//*
            mMediaRecorder01.stop();
            mMediaRecorder01.release();
            mMediaRecorder01 = null;*/

            onBtnStopClick();
        }
        super.onStop();
    }

    /** 取得SD Card目录里的所有.amr文件 */
    private void getRecordFiles()
    {
        recordFiles = new ArrayList<String>();
        if (sdCardExit)
        {
            File files[] = myRecAudioDir.listFiles();
            if (files != null)
            {
                for (int i = 0; i < files.length; i++)
                {
                    if (files[i].getName().indexOf(".") >= 0)
                    {
                        /* 只取.amr文件 */
                        String fileS = files[i].getName().substring(
                                files[i].getName().indexOf("."));
                        if (fileS.toLowerCase().equals(".amr"))
                            recordFiles.add(files[i].getName());
                    }
                }
            }
        }
    }

    /* 打开播放录音文件的程序 */
    private void openFile(File f)
    {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        String type = getMIMEType(f);
        intent.setDataAndType(Uri.fromFile(f), type);
        startActivity(intent);
    }

    private String getMIMEType(File f)
    {
        String end = f.getName().substring(
                f.getName().lastIndexOf(".") + 1, f.getName().length())
                .toLowerCase();
        String type = "";
        if (end.equals("mp3") || end.equals("aac") || end.equals("aac")
                || end.equals("amr") || end.equals("mpeg")
                || end.equals("mp4"))
        {
            type = "audio";
        }
        else if (end.equals("jpg") || end.equals("gif")
                || end.equals("png") || end.equals("jpeg"))
        {
            type = "image";
        }
        else
        {
            type = "*";
        }
        type += "/*";
        return type;
    }
}
