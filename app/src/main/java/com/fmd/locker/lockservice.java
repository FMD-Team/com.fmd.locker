package com.fmd.locker;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.List;

@SuppressWarnings("deprecation")
public class lockservice extends Service {

	@Override
    public void onCreate()
    {       
		KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
		KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
	    lock.disableKeyguard();
    	super.onCreate();   
    }
    
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
    public void onStart(Intent paramIntent, int paramInt)
    {
		disable_deflock();
		super.onStart(paramIntent, paramInt);
    }
    
	private void disable_deflock()
	{	 
	    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);	   
	    lockscreen_b SR = new lockscreen_b();
	    registerReceiver(SR, filter);	
	}
	
	 
}
