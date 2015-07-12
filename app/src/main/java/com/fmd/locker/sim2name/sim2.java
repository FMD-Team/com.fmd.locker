package com.fmd.locker.sim2name;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fmd.locker.R;

public class sim2  extends TextView
{
    boolean b1_=false;
    String s1_="";
    boolean b2_=false;
    String s2_="";
    boolean b1=false;
    String s1="";
    boolean b2=false;
    String s2="";

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
        {
            if (("android.provider.Telephony.SPN_STRINGS_UPDATED".equals(paramAnonymousIntent.getAction())))
            {
                if ((paramAnonymousIntent.getIntExtra("simId", 0) == 0)) {
                    b1_=paramAnonymousIntent.getBooleanExtra("showSpn", false);
                    s1_=paramAnonymousIntent.getStringExtra("spn");
                    b2_=paramAnonymousIntent.getBooleanExtra("showPlmn", false);
                    s2_=paramAnonymousIntent.getStringExtra("plmn");
                }
                if ((paramAnonymousIntent.getIntExtra("simId", 1) == 1)) {
                    b1=paramAnonymousIntent.getBooleanExtra("showSpn", false);
                    s1=paramAnonymousIntent.getStringExtra("spn");
                    b2=paramAnonymousIntent.getBooleanExtra("showPlmn", false);
                    s2=paramAnonymousIntent.getStringExtra("plmn");
                }
                getSim(b1_,s1_,b2_,s2_,b1,s1,b2,s2);
            }
        }
    };
    private boolean sAttached;

    public sim2(Context paramContext)
    {
        this(paramContext, null);
    }

    public sim2(Context paramContext, AttributeSet paramAttributeSet)
    {
        this(paramContext, paramAttributeSet, 0);
    }

    public sim2(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        getSim(false, null, false, null, false, null, false, null);
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

    private void getSim(boolean paramBoolean1_, String paramString1_, boolean paramBoolean2_, String paramString2_,
                        boolean paramBoolean1, String paramString1, boolean paramBoolean2, String paramString2)
    {
        StringBuilder localStringBuilder2 = new StringBuilder();
        int j_ = 0;
        int i_ = j_;
        String res1 = "";
        if (paramBoolean2_)
        {
            i_ = j_;
            if (paramString2_ != null)
            {
                localStringBuilder2.append(paramString2_);
                i_ = 1;
            }
        }
        j_ = i_;
        if (paramBoolean1_)
        {
            j_ = i_;
            if (paramString1_ != null)
            {
                if (i_ != 0) {
                    localStringBuilder2.append('\n');
                }
                localStringBuilder2.append(paramString1_);
                j_ = 1;
            }
        }
        if (j_ != 0) {
            res1 = "- " + localStringBuilder2.toString();
            localStringBuilder2.toString();
            return;
        }

        StringBuilder localStringBuilder = new StringBuilder();
        int j = 0;
        int i = j;
        String res2 = "";
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
            res2 = localStringBuilder.toString();
            localStringBuilder.toString();
            return;
        }

        if (res1.equals(res2)) {
            setText("");
        } else {
            setText(getResources().getString(R.string.T_str)+" "+res2);
        }
    }
}
