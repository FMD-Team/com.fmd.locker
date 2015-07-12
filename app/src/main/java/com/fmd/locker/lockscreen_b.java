package com.fmd.locker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class lockscreen_b extends BroadcastReceiver {

	private SharedPreferences pref;

	@Override
	public void onReceive(Context context, Intent intent) {		
	    if ((intent.getAction().equals(Intent.ACTION_SCREEN_OFF))) 
	    {
			pref = context.getSharedPreferences("pref", 0);
			if (pref.getInt("",0) == 0)
			{
				Intent i = new Intent();
				i.setClassName("com.fmd.locker", "com.fmd.locker.LockMainActivity");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}
	    }
	      
	}
	
}
