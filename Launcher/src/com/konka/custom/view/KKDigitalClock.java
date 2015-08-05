package com.konka.custom.view;


import android.com.konka.dvb.SWTimer.SysTime;
import android.com.konka.dvb.timer.TimerManager;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.DigitalClock;

public class KKDigitalClock extends DigitalClock
{
//	private final static String m12 = "h:mm aa";// h:mm:ss aa
	//private final static String m24 = "k:mm";// k:mm:ss
	//private final static String dateTime = "yyyy-MM-dd EEEE k:mm";

	private Runnable mTicker;
	private Handler mHandler;

	private boolean mTickerStopped = false;

	public KKDigitalClock(Context context)
	{
		super(context);
	}

	public KKDigitalClock(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}


	@Override
	protected void onAttachedToWindow()
	{
		mTickerStopped = false;
		super.onAttachedToWindow();
		mHandler = new Handler();

		/**
		 * requests a tick on the next hard-second boundary
		 */
		mTicker = new Runnable()
		{
			public void run()
			{
				if (mTickerStopped){
					return;
				}
				SysTime sysTime = TimerManager.getInstance().GetSysTime();
				if (sysTime != null)
				{
					String hour = "" + sysTime.Hour;
					String minute = "" + sysTime.Minute;
					
					if(hour.length() == 1)
					{
						hour = "0" + hour;
					}
					
					if(minute.length() == 1)
					{
						minute = "0" + minute;
					}
					
					StringBuffer sb = new StringBuffer();
					sb.append(sysTime.Year).append("-").append(sysTime.Month)
							.append("-").append(sysTime.Day).append("  ")
							.append(hour).append(":").append(minute);

					setText(sb.toString());
					
				}
				invalidate();
				long now = SystemClock.uptimeMillis();
				long next = now + (1000 - now % 1000);
				mHandler.postAtTime(mTicker, next);
			}
		};
		mTicker.run();
	}

	@Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
		mTickerStopped = true;
	}

}
