package com.fmd.locker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Calendar;

public class LockMainActivity extends Activity {

    // Инициализация
    private Context c = null;
    private SharedPreferences pref = null;
    private HomeKeyLocker homeKeyLoader = null;
    private int del = 0;

    // Создаем Panel Locker
    private DisplayMetrics Display;
    private RelativeLayout Panel = null;
    private RelativeLayout P = null;

    // Фокусы спайпингов
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;

    // Разблоки
    private boolean zLock = false;
    private boolean zPhone = false;
    private boolean zCamera = false;

    // Фиксации пальца
    private int X2 = -1;
    private int YYY = -1;
    private int Y2 = -1;

    // Балансировка WidFMD
    private int hgd_y = -1;
    private int hgd_x = -1;
    private boolean hgd_b = false;
    private boolean hgd_b2 = false;
    private Handler Alphar = new Handler();

    // Настройка WidFMD
    private int month = -1;
    private String mm1,mm2,mm;
    private String hh1,hh2,hh;
    private TextView Dates = null;
    private TextView Times = null;
    private TextView Percent = null;
    private Time SysTime = null;
    private Handler Updater = new Handler();

    // Иконки
    private ImageView zLockImg = null;
    private ImageView zPhoneImg = null;
    private ImageView zCameraImg = null;


    private String getWeek() { // Дни недели
        Calendar cal = Calendar.getInstance();
        int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
        int dw = dayOfWeek - 1;
        String dws = ""+dw;
        if (dw == 1) { dws = c.getString(R.string.w_day_1); };
        if (dw == 2) { dws = c.getString(R.string.w_day_2); };
        if (dw == 3) { dws = c.getString(R.string.w_day_3); };
        if (dw == 4) { dws = c.getString(R.string.w_day_4); };
        if (dw == 5) { dws = c.getString(R.string.w_day_5); };
        if (dw == 6) { dws = c.getString(R.string.w_day_6); };
        if (dw == 0) { dws = c.getString(R.string.w_day_7); };
        return dws;
    }

    private String getMonth(int m) // Месяцы
    {
        String res="";
        if (m == 1){ res = getResources().getString(R.string.m_1);};
        if (m == 2){ res = getResources().getString(R.string.m_2);};
        if (m == 3){ res = getResources().getString(R.string.m_3);};
        if (m == 4){ res = getResources().getString(R.string.m_4);};
        if (m == 5){ res = getResources().getString(R.string.m_5);};
        if (m == 6){ res = getResources().getString(R.string.m_6);};
        if (m == 7){ res = getResources().getString(R.string.m_7);};
        if (m == 8){ res = getResources().getString(R.string.m_8);};
        if (m == 9){ res = getResources().getString(R.string.m_9);};
        if (m == 10){ res = getResources().getString(R.string.m_10);};
        if (m == 11){ res = getResources().getString(R.string.m_11);};
        if (m == 12){ res = getResources().getString(R.string.m_12);};
        return res;
    }

    private float GetLevelBattery() // Проценты зарядки
    {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if(level == -1 || scale == -1) {
            return Math.round(50.0f);
        }

        return Math.round(((float)level / (float)scale) * 100.0f);
    }

    private String GetPercentBattery(float s) // Парсинг процентов зарядки
    {
        String res = ""+s;
        StringBuffer sss = new StringBuffer(res);
        sss.delete(res.indexOf("."), res.length());
        return sss.toString()+"%";
    }

    private void T() // время и дата и проценты заряда
    {
        SysTime.setToNow();

        month = SysTime.month+1;

        mm1 = "0"+SysTime.minute;
        mm2 = ""+SysTime.minute;
        if (mm2.length() == 1) { mm = mm1; }else{ mm = mm2; };

        hh1 = "0"+SysTime.hour;
        hh2 = ""+SysTime.hour;
        if (hh2.length() == 1) { hh = hh1; }else{ hh = hh2; };

        Times.setText(hh+":"+mm);
        String now = getWeek()+", ";
        Dates.setText(now+SysTime.monthDay+" "+getMonth(month));
        Percent.setText(GetPercentBattery(GetLevelBattery()));
    }

    private Runnable UPDATE = new Runnable() {
        public void run() { // Обновление
            T();
            Updater.postDelayed(UPDATE, 1000);
        }
    };

    private Runnable ALPHA = new Runnable() { // Балансировка положения WidFMD
        public void run() {
            if (hgd_b) {

                if (hgd_y < 0) {
                    hgd_y = hgd_y + 20;
                    P.setTranslationY(hgd_y);
                }
                if (hgd_y > 0) {
                    hgd_y = hgd_y - 20;
                    P.setTranslationY(hgd_y);
                }

                if (P.getTranslationY() < 0) {
                    hgd_y = hgd_y + 1;
                    P.setTranslationY(hgd_y);
                }
                if (P.getTranslationY() > 0) {
                    hgd_y = hgd_y - 1;
                    P.setTranslationY(hgd_y);
                }
            }

            if (hgd_b2) {
                if (hgd_x < 0) {
                    hgd_x = hgd_x + 20;
                    P.setTranslationX(hgd_x);
                }
                if (hgd_x > 0) {
                    hgd_x = hgd_x - 20;
                    P.setTranslationX(hgd_x);
                }

                if (P.getTranslationX() < 0) {
                    hgd_x = hgd_x + 1;
                    P.setTranslationX(hgd_x);
                }
                if (P.getTranslationX() > 0) {
                    hgd_x = hgd_x - 1;
                    P.setTranslationX(hgd_x);
                }
            }

            if ((hgd_y == 0) && (hgd_x == 0)) {
                Alphar.removeCallbacks(ALPHA);
                hgd_b = false;
                hgd_b2 = false;
            } else {
                Alphar.postDelayed(ALPHA, 1);
            }
        }
    };

    private int px = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_main);
        homeKeyLoader = new HomeKeyLocker();
        homeKeyLoader.lock(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        c = getApplicationContext();
        pref = getSharedPreferences("pref", 0);

        if (Build.VERSION.SDK_INT >= 19) { // Прозрачный СБ
            Window w = getWindow();
            w.setFlags(0x08000000, 0x08000000);
            w.setFlags(0x04000000, 0x04000000);
        }

        TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        Display = c.getResources().getDisplayMetrics();
        pref.edit().putInt("run", 1).commit(); del = 0;
        Panel = (RelativeLayout) findViewById(R.id.Panel);
        P = (RelativeLayout) findViewById(R.id.P);
        YYY = (int) (getResources().getDimension(R.dimen.top_clock_and_date) +
                getResources().getDimension(R.dimen.txt_size_clock) +
                getResources().getDimension(R.dimen.txt_size_date));

        // Виджет времени и даты
        Typeface ttf_clock = Typeface.createFromAsset(c.getAssets(), "fonts/Clock.ttf");
        Typeface ttf_date = Typeface.createFromAsset(c.getAssets(), "fonts/Date.ttf");
        SysTime = new Time();
        Times = (TextView)findViewById(R.id.Time);
        Dates = (TextView)findViewById(R.id.Date);
        Percent = (TextView)findViewById(R.id.Percent);
        Times.setTypeface(ttf_clock);
        Dates.setTypeface(ttf_date);
        Percent.setTypeface(ttf_date);
        T();
        Updater.removeCallbacks(UPDATE);
        Updater.post(UPDATE);
        ///////////////////////////

        // Настройка иконок
        zLockImg = (ImageView)findViewById(R.id.ic_lock);
        zPhoneImg = (ImageView)findViewById(R.id.ic_phone);
        zCameraImg = (ImageView)findViewById(R.id.ic_camera);
        zLockImg.setAlpha(185);
        zPhoneImg.setAlpha(185);
        zCameraImg.setAlpha(185);
        ///////////////////////////

        // Свайпинги
        Panel.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, final MotionEvent event) {
                int eid = event.getAction();
                switch (eid) {
                    case MotionEvent.ACTION_DOWN:
                        Y2 = (int) event.getY();
                        X2 = (int) event.getX();
                        Updater.removeCallbacks(UPDATE);
                        hgd_b = false;
                        hgd_b2 = false;
                        Alphar.removeCallbacks(ALPHA);
                        b1 = false;
                        b2 = false;
                        b3 = false;
                        b4 = true;
                        zLock = false;
                        zPhone = false;
                        zCamera = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (zLock)
                        {
                            hgd_b = false;
                            hgd_b2 = false;
                            Alphar.removeCallbacks(ALPHA);
                            if (del != 1) {
                                del();
                            }
                        }
                        if (zPhone)
                        {
                            hgd_b = false;
                            hgd_b2 = false;
                            Alphar.removeCallbacks(ALPHA);
                            if (del != 1) {
                                del();
                            }
                        }
                        if (zCamera)
                        {
                            hgd_b = false;
                            hgd_b2 = false;
                            Alphar.removeCallbacks(ALPHA);
                            if (del != 1)
                            {
                                del();
                                Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                                intent.setFlags(
                                        Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                                                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                c.startActivity(intent);
                            }
                        }
                        if ((zLock == false) && (zPhone == false) && (zCamera == false))
                        {
                            b1 = false;
                            b2 = false;
                            b3 = false;
                            b4 = true;
                            zLock = false;
                            zPhone = false;
                            zCamera = false;
                            Alphar.removeCallbacks(ALPHA);
                            Alphar.post(ALPHA);
                            Updater.post(UPDATE);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int Y = (int) event.getY();
                        int X = (int) event.getX();
                        if ((Y > (Y2+px)))
                        {
                            if ((b1 == false) & (b2 == false) & (b3 == false))
                            {
                                b4 = false;
                                b2 = false;
                                b3 = false;
                                b1 = false;
                            }
                        }
                        if (b4 == false)
                        {
                            if (!(Y > ((Y2+px)+100)))
                            {
                                hgd_b = true;
                                hgd_b2 = false;
                                P.setTranslationY(Math.round((Y - (Y2+px))));
                                hgd_y = (int)P.getTranslationY();
                            }
                        }
                        if ((Y < (Y2-px)))
                    {
                        if ((b2 == false) & (b3 == false))
                        {
                            b1 = true;
                            b2 = false;
                            b3 = false;
                            b4 = true;
                        }
                    }
                        if (b1)
                        {
                            if ((Y < ((Y2+px)+100))) {
                                hgd_b = true;
                                hgd_b2 = false;
                                P.setTranslationY(Math.round((Y - (Y2-px))));
                                hgd_y = (int)P.getTranslationY();
                            }
                        }

                        if ((X < (X2-px)))
                        {
                            if ((b1 == false) & (b3 == false) & b4)
                            {
                                b1 = false;
                                b2 = true;
                                b3 = false;
                            }
                        }
                        if (b2)
                        {
                            hgd_b2 = true;
                            hgd_b = false;
                            P.setTranslationX(Math.round((X - (X2 - px))));
                            hgd_x = (int)P.getTranslationX();
                        }

                        if ((X > (X2+px)))
                        {
                            if ((b1 == false) & (b2 == false) & b4)
                            {
                                b1 = false;
                                b2 = false;
                                b3 = true;
                            }
                        }
                        if (b3)
                        {
                            hgd_b2 = true;
                            hgd_b = false;
                            P.setTranslationX(Math.round((X - (X2+px))));
                            hgd_x = (int)P.getTranslationX();
                        }

                        // Свайп вверх
                        if (YYY > Display.heightPixels)
                        {
                            if (P.getTranslationY() < -Math.round(YYY / 2)) {
                                zLock = true;
                            } else {
                                zLock = false;
                            }
                        } else {
                            if (P.getTranslationY() < -YYY) {
                                zLock = true;
                            } else {
                                zLock = false;
                            }
                        }

                        // Свайп вправо
                        if (P.getTranslationX()+Display.widthPixels > Display.widthPixels+Math.round(Display.widthPixels / 2)) {
                            zPhone = true;
                        } else {
                            zPhone = false;
                        }

                        // Свайп влево
                        if (P.getTranslationX() < -Math.round(Display.widthPixels / 2)) {
                            zCamera = true;
                        } else {
                            zCamera = false;
                        }
                        break;
                }
                return true;
            }

        });
    }

    void del()
    {
        homeKeyLoader.unlock();
        del = 1;
        Alphar.removeCallbacks(ALPHA);
        Updater.removeCallbacks(UPDATE);
        pref.edit().putInt("run", 0).commit();
        finish();
    }

    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                case TelephonyManager.CALL_STATE_RINGING:
                    if (del != 1)
                    {
                        del();
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (del != 1)
                    {
                        del();
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        FullScreencall();
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public void FullScreencall() { // Спасибо Evish Verma
        if(Build.VERSION.SDK_INT < 19)
        {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
    } else {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
}
