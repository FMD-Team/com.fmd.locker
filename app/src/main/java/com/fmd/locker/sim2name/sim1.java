package com.fmd.locker.sim2name;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.widget.TextView;

public class sim1  extends TextView
{
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
        {
            if (("android.provider.Telephony.SPN_STRINGS_UPDATED".equals(paramAnonymousIntent.getAction())) && (paramAnonymousIntent.getIntExtra("simId", 0) == 0)) {
                sim1.this.updateSIM(paramAnonymousIntent.getBooleanExtra("showSpn", false), paramAnonymousIntent.getStringExtra("spn"), paramAnonymousIntent.getBooleanExtra("showPlmn", false), paramAnonymousIntent.getStringExtra("plmn"), ""+paramAnonymousIntent.getIntExtra("simId", 0));
            }
        }
    };
    private boolean sAttached;

    public sim1(Context paramContext)
    {
        this(paramContext, null);
    }

    public sim1(Context paramContext, AttributeSet paramAttributeSet)
    {
        this(paramContext, paramAttributeSet, 0);
    }

    public sim1(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
        super(paramContext, paramAttributeSet, paramInt);
        updateSIM(false, null, false, null, "-1");
    }

    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        if (!this.sAttached)
        {
            this.sAttached = true;
            IntentFilter localIntentFilter = new IntentFilter();
            localIntentFilter.addAction("android.provider.Telephony.SPN_STRINGS_UPDATED");
            getContext().registerReceiver(this.mIntentReceiver, localIntentFilter, null, getHandler());
        }
    }

    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        if (this.sAttached)
        {
            getContext().unregisterReceiver(this.mIntentReceiver);
            this.sAttached = false;
        }
    }

    void updateSIM(boolean paramBoolean1, String paramString1, boolean paramBoolean2, String paramString2, String SimNum)
    {
        StringBuilder localStringBuilder = new StringBuilder();
        int j = 0;
        int i = j;
        if (paramBoolean2)
        {
            i = j;
            if (paramString2 != null)
            {
                localStringBuilder.append(paramString2);
                i = 1;
            }
        }
        j = i;
        if (paramBoolean1)
        {
            j = i;
            if (paramString1 != null)
            {
                if (i != 0) {
                    localStringBuilder.append('\n');
                }
                localStringBuilder.append(paramString1);
                j = 1;
            }
        }
        if (j != 0) {
            setText(localStringBuilder.toString());
           // localStringBuilder.toString();
            return;
        }
        setText("");
    }
}
