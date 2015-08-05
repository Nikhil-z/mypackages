package android.test.ui;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.com.konka.dvb.SWTimer.SysTime;
import android.com.konka.dvb.av.AVPlayerManager;
import android.com.konka.dvb.epg.EpgManager;
import android.com.konka.dvb.epg.EpgManager.CurrPfInfo;
import android.com.konka.dvb.prog.ProgManager;
import android.com.konka.dvb.timer.TimerManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.WindowManager.LayoutParams;

public class PfInfo extends PopupWindow
{
	private static PfInfo instance;

	private Context mContext;

	private LinearLayout mLinearLayout;
	private LayoutInflater inflater;

	private TextView textview_prog_num;
	private TextView textview_prog_name;
	private TextView textview_curr_prog_info;
	private TextView textview_next_prog_info;

	private TextView textview_curr_time;

	private Button pf_ad;

	// 控制PF条显示的线程
	private Thread showTimeThread;
	private ShowTimeTask showTimeTask;
	private static long begin_show_time = 0;

	private PfInfo()
	{
	}

	private PfInfo(Context context)
	{
		mContext = context;

		init();

		this.setFocusable(false);
		this.setOutsideTouchable(false);
		this.setContentView(mLinearLayout);
		this.setWidth(950);
		this.setHeight(150);
	}

	public synchronized static PfInfo getInstance(Context context)
	{
		if (instance == null)
		{
			instance = new PfInfo(context);
		}

		return instance;
	}

	public void init()
	{
		inflater = (LayoutInflater) mContext
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		mLinearLayout = (LinearLayout) inflater.inflate(R.layout.pf_info, null);

		textview_prog_num = (TextView) mLinearLayout
				.findViewById(R.id.pf_prog_num);
		textview_prog_name = (TextView) mLinearLayout
				.findViewById(R.id.pf_prog_name);
		textview_curr_prog_info = (TextView) mLinearLayout
				.findViewById(R.id.pf_curr_prog_info);
		textview_next_prog_info = (TextView) mLinearLayout
				.findViewById(R.id.pf_next_prog_info);
		textview_curr_time = (TextView) mLinearLayout
				.findViewById(R.id.pf_curr_time);
		pf_ad = (Button) mLinearLayout.findViewById(R.id.pf_ad);

		showTimeTask = new ShowTimeTask();
		showTimeThread = new Thread(showTimeTask);
		showTimeThread.start();
	}

	@Override
	public void update()
	{
		super.update();

		// 开始显示的时间
		begin_show_time = System.currentTimeMillis();

		AVPlayerManager avPlayerManager = AVPlayerManager.getInstance();
		String progNum = avPlayerManager.getCurrProgShowInfoByMap().get(
				"PROG_NUM");
		String progName = avPlayerManager.getCurrProgShowInfoByMap().get(
				"PROG_NAME");

		EpgManager epgManager = EpgManager.getInstance();
		CurrPfInfo currPfInfo = epgManager.getCurrverntProgPfInfo(0);
		CurrPfInfo nextPfInfo = epgManager.getCurrverntProgPfInfo(1);

		textview_prog_num.setText(progNum);
		textview_prog_name.setText(progName);
		if (currPfInfo.memEventName == null)
		{
			textview_curr_prog_info.setText("");
		}
		else
		{
			textview_curr_prog_info
					.setText(getProgEventStartTime(currPfInfo.utcStartTime)
							+ "-"
							+ getProgEventFinishTime(currPfInfo.utcStartTime,
									currPfInfo.DurSeconds) + "   "
							+ currPfInfo.memEventName);
		}
		if (nextPfInfo.memEventName == null)
		{
			textview_next_prog_info.setText("");
		}
		else
		{
			textview_next_prog_info
					.setText(getProgEventStartTime(nextPfInfo.utcStartTime)
							+ "-"
							+ getProgEventFinishTime(nextPfInfo.utcStartTime,
									nextPfInfo.DurSeconds) + "   "
							+ nextPfInfo.memEventName);
		}

		// 广告
		try
		{
			String imageUrl = ProgManager.getInstance().GetAdFileNameByProNo(
					Integer.parseInt(progNum));

			if (new File(imageUrl).exists())
			{
				pf_ad.setBackground(new BitmapDrawable(imageUrl));
			}
		}
		catch (Exception e)
		{
			pf_ad.setBackgroundResource(R.drawable.default_ad);
		}

		// 当前时间
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

			textview_curr_time.setText(sb.toString());
		}
	}

	@Override
	public void dismiss()
	{
		super.dismiss();

		begin_show_time = 0;
	}

	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();

		if (showTimeThread != null && showTimeThread.isAlive())
		{
			showTimeThread.interrupt();
		}
	}

	public String getProgEventStartTime(int utcTime)
	{
		StringBuffer result = new StringBuffer();

		TimerManager timerManager = TimerManager.getInstance();
		SysTime sysTime = timerManager.TransUtctimeToSystime(0, utcTime);

		String hour = "" + sysTime.Hour;
		String minute = "" + sysTime.Minute;

		if (hour.length() == 1)
		{
			hour = "0" + hour;
		}

		if (minute.length() == 1)
		{
			minute = "0" + minute;
		}

		result.append(hour).append(":").append(minute);

		return result.toString();
	}

	public String getProgEventFinishTime(int statrUtcTime, int duration)
	{
		StringBuffer result = new StringBuffer();

		TimerManager timerManager = TimerManager.getInstance();
		SysTime sysTime = timerManager.TransUtctimeToSystime(0, statrUtcTime
				+ duration);

		String hour = "" + sysTime.Hour;
		String minute = "" + sysTime.Minute;

		if (hour.length() == 1)
		{
			hour = "0" + hour;
		}

		if (minute.length() == 1)
		{
			minute = "0" + minute;
		}

		result.append(hour).append(":").append(minute);

		return result.toString();
	}

	class ShowTimeTask implements Runnable
	{
		@Override
		public void run()
		{
			while (true)
			{
				try
				{
					Thread.sleep(1000);

					if (begin_show_time == 0)
					{
						continue;
					}

					if (System.currentTimeMillis() - begin_show_time >= 10000)
					{
						Message message = new Message();
						message.what = 0;

						handler.sendMessage(message);
					}
				}
				catch (Exception e)
				{
				}
			}
		}
	}

	Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch (msg.what)
			{
			case 0:
				instance.dismiss();
				break;
			default:
				break;
			}
		};
	};
}
