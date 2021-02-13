package com.sugar.demo.activities.naozhong;

import java.lang.reflect.Field;
import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.sugar.demo.R;
import com.sugar.demo.utils.LogUtil;

public class NaoZhongMainActivity extends Activity {

    /* 声明对象变量 */
    TextView txtSettingOneTime;
    TextView setTime2;
    Button btnSettingOneTime;
    Button btnDeleteClock;
    Button mButton3;
    Button mButton4;
    Calendar calendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /* 载入main.xml Layout */
        setContentView(R.layout.naozhong_main);

        /* 以下为只响一次的闹钟的设置 */
        txtSettingOneTime =(TextView) findViewById(R.id.setTime1);
        /* 只响一次的闹钟的设置Button */
        btnSettingOneTime =(Button)findViewById(R.id.btnSettingOneTime);
        btnSettingOneTime.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                /* 取得点击按钮时的时间作为TimePickerDialog的默认值 */
                calendar.setTimeInMillis(System.currentTimeMillis() + 60000);
                //calendar.setTimeInMillis(System.currentTimeMillis() + 1000 * 60 * 10); // sumsung

                int mHour= calendar.get(Calendar.HOUR_OF_DAY);
                int mMinute= calendar.get(Calendar.MINUTE);

                /* 跳出TimePickerDialog来设置时间 */
                new TimePickerDialog(NaoZhongMainActivity.this,
                        new TimePickerDialog.OnTimeSetListener()
                        {
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                            {
                                /* 取得设置后的时间，秒跟毫秒设为0 */
                                calendar.setTimeInMillis(System.currentTimeMillis());
                                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar.set(Calendar.MINUTE,minute);
                                calendar.set(Calendar.SECOND,0);
                                calendar.set(Calendar.MILLISECOND,0);

                                setAlarm(hourOfDay, minute);
                            }
                        }, mHour, mMinute,true).show();
            }
        });

        calendar.setTimeInMillis(System.currentTimeMillis() + 1000 * 9);
        //calendar.setTimeInMillis(System.currentTimeMillis() + 1000 * 60 * 15);   // huawei was shishi 17:17 ok.
        //calendar.setTimeInMillis(System.currentTimeMillis() + 1000 * 60 * 30);   // huawei was shishi 17:53 ?
        //calendar.setTimeInMillis(System.currentTimeMillis() + 1000 * 60 * 5);    // sumsung => 16:45 ok.
        //calendar.setTimeInMillis(System.currentTimeMillis() + 1000 * 60 * 30); //   vivo => 17:17
//        calendar.setTimeInMillis(System.currentTimeMillis() + 1000 * 60 * 60);    // sumsung => 17:45 ?
//        calendar.setTimeInMillis(System.currentTimeMillis() + 1000 * 60 * 2);    // huawei jk

        // 早上8点
        /*calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);*/

        // 启动添加定时任务
        setAlarm(0,  15);

        /*// 定时开机*/
        //setPowerOnAlarm(this, calendar.getTimeInMillis());

        /* 只响一次的闹钟的删除Button */
        btnDeleteClock =(Button) findViewById(R.id.btnDeleteClock);
        btnDeleteClock.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                PendingIntent sender = PendingIntent.getBroadcast(
                        NaoZhongMainActivity.this,0,
                        new Intent(NaoZhongMainActivity.this, NaoZhongReceiver.class), 0);
                /* 由AlarmManager中删除 */
                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                alarmManager.cancel(sender);
                /* 以Toast提示已删除设置，并更新显示的闹钟时间 */
                Toast.makeText(NaoZhongMainActivity.this,"闹钟时间解除",
                        Toast.LENGTH_SHORT).show();
                txtSettingOneTime.setText("目前无设置");
            }
        });

        /* 以下为重复响起的闹钟的设置 */
        setTime2=(TextView) findViewById(R.id.setTime2);
        /* create重复响起的闹钟的设置画面 */
        /* 引用timeset.xml为Layout */
        LayoutInflater factory = LayoutInflater.from(this);
        final View setView = factory.inflate(R.layout.naozhong_timeset, null);
        final TimePicker tPicker = (TimePicker)setView
                .findViewById(R.id.tPicker);
        tPicker.setIs24HourView(true);

        /* create重复响起闹钟的设置Dialog */
        final AlertDialog alertDialog = new AlertDialog.Builder(NaoZhongMainActivity.this)
                .setIcon(R.drawable.clock)
                .setTitle("设置")
                .setView(setView)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                /* 取得设置的间隔秒数 */
                                EditText ed=(EditText)setView.findViewById(R.id.mEdit);
                                int times=Integer.parseInt(ed.getText().toString())
                                        *1000;
                                /* 取得设置的开始时间，秒及毫秒设为0 */
                                calendar.setTimeInMillis(System.currentTimeMillis());
                                calendar.set(Calendar.HOUR_OF_DAY,tPicker.getCurrentHour());
                                calendar.set(Calendar.MINUTE,tPicker.getCurrentMinute());
                                calendar.set(Calendar.SECOND,0);
                                calendar.set(Calendar.MILLISECOND,0);

                                /* 指定闹钟设置时间到时要运行CallAlarm.class */
                                Intent intent = new Intent(NaoZhongMainActivity.this,
                                        NaoZhongReceiver.class);
                                PendingIntent sender = PendingIntent.getBroadcast(
                                        NaoZhongMainActivity.this,1, intent, 0);
                                /* setRepeating()可让闹钟重复运行 */
                                AlarmManager alarmManager;
                                alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), times, sender);
                                /* 更新显示的设置闹钟时间 */
                                String tmpS=format(tPicker.getCurrentHour())+"："+
                                        format(tPicker.getCurrentMinute());
                                setTime2.setText("设置闹钟时间为"+tmpS+
                                        "开始，重复间隔为"+times/1000+"秒");
                                /* 以Toast提示设置已完成 */
                                Toast.makeText(NaoZhongMainActivity.this,"设置闹钟时间为"+tmpS+
                                                "开始，重复间隔为"+times/1000+"秒",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                            }
                        }).create();

        /* 重复响起的闹钟的设置Button */
        mButton3=(Button) findViewById(R.id.btnRepeatSetting);
        mButton3.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                /* 取得点击按钮时的时间作为tPicker的默认值 */
                calendar.setTimeInMillis(System.currentTimeMillis());
                tPicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                tPicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
                /* 跳出设置画面di */
                alertDialog.show();
            }
        });

        /* 重复响起的闹钟的删除Button */
        mButton4=(Button) findViewById(R.id.btnDeleteRepeat);
        mButton4.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(NaoZhongMainActivity.this, NaoZhongReceiver.class);
                PendingIntent sender = PendingIntent.getBroadcast(
                        NaoZhongMainActivity.this,1, intent, 0);
                /* 由AlarmManager中删除 */
                AlarmManager am;
                am = (AlarmManager)getSystemService(ALARM_SERVICE);
                am.cancel(sender);
                /* 以Toast提示已删除设置，并更新显示的闹钟时间 */
                Toast.makeText(NaoZhongMainActivity.this,"闹钟时间解除",
                        Toast.LENGTH_SHORT).show();
                setTime2.setText("目前无设置");
            }
        });
    }

    /**
     * 定时开机测试
     */
    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setPowerOnAlarm(Context mContext, long time) {
        //定时开机时间,时间设置最好不要小于一分钟，我此处设置为90 * 1000毫秒
        //long time = System.currentTimeMillis() + 90 * 1000;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            Intent intent = new Intent("org.codeaurora.poweroffalarm.action.SET_ALARM");
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            intent.setPackage("com.qualcomm.qti.poweroffalarm");
            intent.putExtra("time", time);
            sendBroadcast(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_BOOT_COMPLETED);
            intent.putExtra("seq", 1);
            PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(
                    Context.ALARM_SERVICE);
            int type = 8;
            try {
                Field field = AlarmManager.class.getField("RTC_POWEROFF_WAKEUP");
                type = field.getInt(null);
            } catch (Exception e) {
                //nothing to do
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                alarmManager.setExactAndAllowWhileIdle(type, time, pi);
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                alarmManager.setExact(type, time, pi);
            else
                alarmManager.set(type, time, pi);
        }
    }

    public void setAlarm(int hourOfDay, int minute) {
        /* 指定闹钟设置时间到时要运行CallAlarm.class */
        Intent intent = new Intent(NaoZhongMainActivity.this, NaoZhongReceiver.class);
        /* 创建PendingIntent */
        PendingIntent sender = PendingIntent.getBroadcast(NaoZhongMainActivity.this,0, intent, 0);
        AlarmManager alarmManager;

        /*
         * AlarmManager.RTC_WAKEUP设置服务在系统休眠时同样会运行
         */
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        // 以set()设置的PendingIntent只会运行一次
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        // 每 15 秒运行一次
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 15, sender);

        /* 更新显示的设置闹钟时间 */
        String tmpS = format(hourOfDay)+" : "+format(minute);
        txtSettingOneTime.setText(tmpS);
        /* 以Toast提示设置已完成 */
        Toast.makeText(NaoZhongMainActivity.this,"设置闹钟时间为: "+tmpS, Toast.LENGTH_SHORT).show();
    }

    /* 日期时间显示两位数的方法 */
    private String format(int x)
    {
        String s=""+x;
        if(s.length()==1) s="0"+s;
        return s;
    }
}
