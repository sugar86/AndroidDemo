package com.sugar.demo.activities.naozhong;


import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.os.Bundle;

import com.sugar.demo.activities.AudioMp3Activity;

/* 调用闹钟 Alert 的Receiver (example_2) */
public class NaoZhongReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        /* create Intent，调用AlarmAlert.class */
        //Intent i = new Intent(context, NaoZhongDialogActivity.class);
        Intent i = new Intent(context, AudioMp3Activity.class);

        Bundle bundleRet = new Bundle();
        bundleRet.putString("STR_CALLER", "");
        i.putExtras(bundleRet);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
