package android.test.ui;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import android.app.Activity;
import android.app.AlertDialog;
import android.com.konka.dvb.DVB;
import android.com.konka.dvb.av.AVPlayerManager;
import android.com.konka.dvb.prog.ProgManager;
import android.com.konka.dvb.prog.ProgManager.ProgListShowInfo;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.test.ui.R;
import android.test.ui.epg.EpgWindow;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class Topmost extends Activity
{
	// 电视播放窗口
	private SurfaceView surfaceView;
	// 台号
	private TextView textview_prog_num;
	// 声道
	private TextView textview_track;

	public AVPlayerManager aVPlayerManager = null;
	public ProgManager pProgManager = null;
	public ProgListShowInfo proglist = null;
	// 控制台号显示时间的线程
	private ShowProgNumTask showProgNumTask;
	private Thread showProgNumThread;
	private static long prog_num_start_show_time = 0;// 台号刚开始显示的时间
	private static String curr_prog_num;// 当前台号

	// 控制声道显示时间的线程
	private ShowTrackTask showTrackTask;
	private Thread showTrackThread;
	private static long track_start_show_time = 0;// 声道信息刚开始显示的时间

	private static RecordNumKeyTask recordNumKeyTask;// 记录按键换台时的台号的处理时间线�?
	private static Thread recordNumKeyThread;

	private static Queue numKeyTimeRecordQueue;// 记录按键换台时的数字按键的触发时�?
	private static Queue progNumRecordQueue;// 记录按键换台时的数字
	private static StringBuffer temp_prog_num;// 记录数字键换台时的临时台�?

	// pf�?
	private PfInfo pfInfo;

	// 台号显示持续时间
	private static final int PROG_NUM_SHOW_DURATION = 10000;
	// 声道信息显示持续时间
	private static final int TRACK_SHOW_DURATION = 5000;

	// 数字键换台键号记录持续时�?
	private static final int NUM_KEY_RECORD_DURATION = 2500;
	private CaViewHolder caViewHolder;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topmost);

		init();
	}

	@Override
	protected void onResume()
	{
		/** add by liuhuasheng */
		DVB.GetInstance().SetDVBStatus(1);
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();

		// 隐藏PF�?
		if (pfInfo.isShowing())
		{
			pfInfo.dismiss();
		}
		/** add by liuhuasheng */
		DVB.GetInstance().ReleaseResource();
		aVPlayerManager.stopPlayer();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		// 回收线程资源
		if (showProgNumThread != null && showProgNumThread.isAlive())
		{
			showProgNumThread.interrupt();
		}
		if (recordNumKeyThread != null && recordNumKeyThread.isAlive())
		{
			recordNumKeyThread.interrupt();
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);

		if (hasFocus)
		{
			Intent intent = getIntent();

			int progNum = 1;
			try
			{
				progNum = Integer.parseInt(intent.getStringExtra("num"));
				Log.i("ID", "电视节目号：" + progNum);
			}
			catch (Exception e)
			{
				progNum = 1;
			}
			pProgManager = ProgManager.getInstance();
			proglist = pProgManager.getCurrverntProgListInfo();
			if (proglist != null)
			{
				playTv(progNum);
			}
			else
			{
				AlertDialog isExit = new AlertDialog.Builder(this).create();
				// 设置对话框标题
				isExit.setTitle("系统提示");
				// 设置对话框消息
				isExit.setMessage("未搜索到节目，重新全频搜索？");
				// 添加选择按钮并注册监听
				isExit.setButton("确定", listener);
				isExit.setButton2("取消", listener);
				// 显示对话框
				isExit.show();
			}

		}
	}

	/** 监听对话框里面的button点击事件 */
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{
			Intent intent = new Intent();
			switch (which)
			{
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				intent.setClass(Topmost.this,
						Setting_Search_Manual_Searching.class);
				intent.putExtra("way", 2);
				startActivity(intent);
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				intent.setClass(Topmost.this, SettingActivity.class);
				startActivity(intent);
				finish();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onNewIntent(Intent intent)
	{
		// TODO Auto-generated method stub
		super.onNewIntent(intent);

		setIntent(intent);
	}

	public void init()
	{
		surfaceView = (SurfaceView) findViewById(R.id.topmost_surface);
		textview_prog_num = (TextView) findViewById(R.id.topmost_prog_num);
		textview_track = (TextView) findViewById(R.id.topmost_track);

		aVPlayerManager = AVPlayerManager.getInstance();

		// 初始化PF对象
		pfInfo = PfInfo.getInstance(getApplicationContext());

		numKeyTimeRecordQueue = new ArrayBlockingQueue(3);
		progNumRecordQueue = new ArrayBlockingQueue(3);

		// 记录数字键换台时要用到的
		temp_prog_num = new StringBuffer();
		recordNumKeyTask = new RecordNumKeyTask();
		recordNumKeyThread = new Thread(recordNumKeyTask);
		recordNumKeyThread.start();

		// 控制台号显示时间时要用到
		showProgNumTask = new ShowProgNumTask();
		showProgNumThread = new Thread(showProgNumTask);
		showProgNumThread.start();
		
		// 控制声道信息显示时间要用到
		showTrackTask = new ShowTrackTask();
		showTrackThread = new Thread(showTrackTask);
		showTrackThread.start();

		/** register ca view hold */
		caViewHolder = new CaViewHolder(this);
	}

	/**
	 * @author wangyhang 播放电视
	 * */
	public void playTv(int progNum)
	{
		WindowManager wm = this.getWindowManager();

		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();

		aVPlayerManager.startPlayerByProgNum(progNum);
		aVPlayerManager.setPlayerWindow(0, 0, width, height);

		textview_prog_num.setText("" + progNum);
		textview_prog_num.setVisibility(View.VISIBLE);

		prog_num_start_show_time = System.currentTimeMillis();

		showPfInfo();
	}

	// 设置频道�?
	public void setNumKeyRecord(long time, String progNum)
	{
		if (numKeyTimeRecordQueue.size() == 3)
		{
			numKeyTimeRecordQueue.poll();
		}

		if (progNumRecordQueue.size() == 3)
		{
			progNumRecordQueue.poll();
		}

		numKeyTimeRecordQueue.add(time);
		progNumRecordQueue.add(progNum);

		Iterator it = progNumRecordQueue.iterator();
		temp_prog_num.setLength(0);// 先清�?
		while (it.hasNext())
		{
			temp_prog_num.append("" + it.next());
		}
		textview_prog_num.setText(temp_prog_num.toString());
		textview_prog_num.setVisibility(View.VISIBLE);
	}

	// 获取最终设置的频道�?
	public String getProgNumRecord()
	{
		if (progNumRecordQueue.size() == 0)
		{
			return null;
		}
		else
		{
			Iterator it = progNumRecordQueue.iterator();
			StringBuffer result = new StringBuffer();

			while (it.hasNext())
			{
				result.append("" + it.next());
			}

			return result.toString();
		}
	}

	// 播放具体电视节目之后控制显示台号时间的线�?
	class ShowProgNumTask implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				while (true)
				{
					Thread.sleep(1000);

					if (prog_num_start_show_time == 0)
					{
						continue;
					}

					if (System.currentTimeMillis() - prog_num_start_show_time >= PROG_NUM_SHOW_DURATION)
					{
						Message message = new Message();
						message.what = 0;
						progNumShowHandler.sendMessage(message);
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	};

	/**
	 * @author wangyuhang 播放电视台后控制台号
	 * */
	Handler progNumShowHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 0:// 隐藏台号
				textview_prog_num.setVisibility(View.INVISIBLE);

				prog_num_start_show_time = 0;
				break;
			case 1:// 显示台号
				curr_prog_num = aVPlayerManager.getCurrProgShowInfoByMap().get(
						"PROG_NUM");
				textview_prog_num.setText(curr_prog_num);
				textview_prog_num.setVisibility(View.VISIBLE);

				prog_num_start_show_time = System.currentTimeMillis();

				// 重新显示PF信息
				showPfInfo();
			case 2://隐藏声道信息
				textview_track.setVisibility(View.INVISIBLE);
				track_start_show_time = 0;
			default:
				break;
			}
		};
	};

	/**
	 * @author wangyuhang 记录数字按键的线�?
	 * */
	class RecordNumKeyTask implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				while (true)
				{
					Thread.sleep(1000);

					if (numKeyTimeRecordQueue.size() == 0
							|| progNumRecordQueue.size() == 0)
					{
						continue;
					}

					Iterator it = numKeyTimeRecordQueue.iterator();
					long lastKeyTime = 0;
					while (it.hasNext())
					{
						lastKeyTime = (Long) it.next();
					}

					if (System.currentTimeMillis() - lastKeyTime >= NUM_KEY_RECORD_DURATION)
					{
						// 换台
						aVPlayerManager
								.startPlayerByProgNum(getProgNumRecord());

						// 显示台号
						Message message = new Message();
						message.what = 1;
						progNumShowHandler.sendMessage(message);

						// 数字键换台后清除记录数据
						numKeyTimeRecordQueue.clear();
						progNumRecordQueue.clear();
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	class ShowTrackTask implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				while (true)
				{
					Thread.sleep(1000);

					if (track_start_show_time == 0)
					{
						continue;
					}

					if (System.currentTimeMillis() - track_start_show_time >= TRACK_SHOW_DURATION)
					{
						//隐藏声道信息
						Message message = new Message();
						message.what = 2;
						progNumShowHandler.sendMessage(message);
					}
				}
			}
			catch (Exception e)
			{
			}
		}
	}

	// 显示PF信息�?
	public void showPfInfo()
	{
		pfInfo.showAtLocation(findViewById(R.id.topmost_layout),
				Gravity.BOTTOM, 0, 70);
		pfInfo.update();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event)
	{
		Log.i("myLog", "keycode: " + event.getKeyCode());

		if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP
				&& event.getAction() == KeyEvent.ACTION_UP)
		{
			// 上键换台

			aVPlayerManager.startPlayerLastProg();

			curr_prog_num = aVPlayerManager.getCurrProgShowInfoByMap().get(
					"PROG_NUM");
			textview_prog_num.setText(curr_prog_num);
			textview_prog_num.setVisibility(View.VISIBLE);

			prog_num_start_show_time = System.currentTimeMillis();

			showPfInfo();

			return true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN
				&& event.getAction() == KeyEvent.ACTION_UP)
		{
			// 下键换台

			aVPlayerManager.startPlayerNextProg();

			curr_prog_num = aVPlayerManager.getCurrProgShowInfoByMap().get(
					"PROG_NUM");
			textview_prog_num.setText(curr_prog_num);
			textview_prog_num.setVisibility(View.VISIBLE);

			prog_num_start_show_time = System.currentTimeMillis();

			showPfInfo();

			return true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_0
				&& event.getAction() == KeyEvent.ACTION_UP)
		{
			setNumKeyRecord(System.currentTimeMillis(), "0");

			return true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_1
				&& event.getAction() == KeyEvent.ACTION_UP)
		{
			setNumKeyRecord(System.currentTimeMillis(), "1");

			return true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_2
				&& event.getAction() == KeyEvent.ACTION_UP)
		{
			setNumKeyRecord(System.currentTimeMillis(), "2");

			return true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_3
				&& event.getAction() == KeyEvent.ACTION_UP)
		{
			setNumKeyRecord(System.currentTimeMillis(), "3");

			return true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_4
				&& event.getAction() == KeyEvent.ACTION_UP)
		{
			setNumKeyRecord(System.currentTimeMillis(), "4");

			return true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_5
				&& event.getAction() == KeyEvent.ACTION_UP)
		{
			setNumKeyRecord(System.currentTimeMillis(), "5");

			return true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_6
				&& event.getAction() == KeyEvent.ACTION_UP)
		{
			setNumKeyRecord(System.currentTimeMillis(), "6");

			return true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_7
				&& event.getAction() == KeyEvent.ACTION_UP)
		{
			setNumKeyRecord(System.currentTimeMillis(), "7");

			return true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_8
				&& event.getAction() == KeyEvent.ACTION_UP)
		{
			setNumKeyRecord(System.currentTimeMillis(), "8");

			return true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_9
				&& event.getAction() == KeyEvent.ACTION_UP)
		{
			setNumKeyRecord(System.currentTimeMillis(), "9");

			return true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_INFO
				&& event.getAction() == KeyEvent.ACTION_UP)
		{
			if (pfInfo.isShowing())
			{
				pfInfo.dismiss();
			}
			else
			{
				showPfInfo();
			}

			return true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_INFO
				&& event.getAction() == KeyEvent.ACTION_DOWN)
		{
			return true;
		}
		else if (event.getKeyCode() == 256
				&& event.getAction() == KeyEvent.ACTION_UP)// 指南键
		{
			Intent i = new Intent(Topmost.this, EpgWindow.class);
			startActivity(i);

			return true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
				&& event.getAction() == KeyEvent.ACTION_UP)// 确认�?
		{
			Intent i = new Intent(Topmost.this, SettingActivity_List.class);
			startActivity(i);

			return true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_F5
				&& event.getAction() == KeyEvent.ACTION_UP)// 股票�?
		{
			ComponentName component = new ComponentName("com.HTRD.stock",
					"com.HTRD.stock.SplashActivity");
			Intent i = new Intent();
			i.setComponent(component);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);

			return true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_F2
				&& event.getAction() == KeyEvent.ACTION_UP)// 声道
		{
			int curr_track = aVPlayerManager.getPlayerTrack();

			curr_track++;

			if (curr_track == 3)
			{
				curr_track = 0;
			}

			aVPlayerManager.setPlayerTrack(curr_track);

			if (curr_track == 0)
			{
				textview_track.setText("左声道");
			}
			else if (curr_track == 1)
			{
				textview_track.setText("右声道");
			}
			else if (curr_track == 2)
			{
				textview_track.setText("立体声");
			}

			textview_track.setVisibility(View.VISIBLE);
			track_start_show_time = System.currentTimeMillis();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_F4
				&& event.getAction() == KeyEvent.ACTION_UP)// 邮件
		{
			Intent i = new Intent(Topmost.this, CaEmailListActivity.class);
			startActivity(i);
		}

		return super.dispatchKeyEvent(event);
	}
}
