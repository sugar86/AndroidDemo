package com.sugar.demo.activities.naozhong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.sugar.demo.R;

/* 实际跳出闹铃Dialog的Activity(example_1) */
public class NaoZhongDialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /* 跳出的闹铃警示  */
        new AlertDialog.Builder(NaoZhongDialogActivity.this)
                .setIcon(R.drawable.clock)
                .setTitle("闹钟响了!!")
                .setMessage("赶快起床吧!!!")
                .setPositiveButton("关掉他",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                /* 关闭Activity */
                                NaoZhongDialogActivity.this.finish();
                            }
                        })
                .show();
    }
}
