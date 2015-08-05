package com.konka.launcher;

import java.util.ArrayList;
import java.util.List;
import com.konka.launcher.app.ApplicationOperation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends FragmentActivity
{
	private static final String TAG = "MainActivity";
	
	public static final String LAUNCHER_CODE = "KK11";

	// Ali提供的service
//	public static IDesktopConfigService configManagerService = null;

	// 有更新的坑位列表
	public static ArrayList<String> updateList = new ArrayList<String>();
	// 有删除的坑位列表
	public static ArrayList<String> deleteList = new ArrayList<String>();

	// 坑位信息变动类型标识
	public static int changeFlag = 0;

	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragments = new ArrayList<Fragment>();

	private Button mTabBtnLive;
	private Button mTabBtnHD;
	private Button mTabBtnLookback;
	private Button mTabBtnApplication;
	
	//整体布局
	private LinearLayout mainLayout;

	// headbar上的下划线
	private ImageView headbar_underline;
	// headbar下划线上一次位置所在
	private static int headbar_underline_lastPos = 0;

	// 视频坑位尺寸
	public static int extra_button_width;
	public static int extra_button_height;

	// 当tab的index
	public static int currentIndex = 0;

	// 总页数
	public static final int PAGE_NUM = 4;

	// 应用操作
	private ApplicationOperation applicationOperation;
	public static RelativeLayout main_bottom;
	private LinearLayout tips;
	private String preContentDes;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();

		mAdapter = new TabAdapter(getSupportFragmentManager());
		mViewPager.setOffscreenPageLimit(PAGE_NUM);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new TabChangeLis());
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	/**
	 * @author wangyuhang head bar上的tab按钮焦点效果清除
	 * */
	protected void defocusTabBtn()
	{
		mTabBtnLive.setTextColor(getResources().getColor(R.color.white_50));
		mTabBtnHD.setTextColor(getResources().getColor(R.color.white_50));
		mTabBtnLookback.setTextColor(getResources().getColor(R.color.white_50));
		mTabBtnApplication.setTextColor(getResources().getColor(R.color.white_50));
	}

	/**
	 * @author wangyuhang 初始化工作
	 * */
	private void initView()
	{
		mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
		
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);


		// tab按钮初始化
		mTabBtnLive = (Button) findViewById(R.id.tab_btn_recommend);
		mTabBtnHD = (Button) findViewById(R.id.tab_btn_movie);
		mTabBtnLookback = (Button) findViewById(R.id.tab_btn_game);
		mTabBtnApplication = (Button) findViewById(R.id.tab_btn_child);

		// 各个分界面, 按顺序添加
		mFragments.add(new MainTabLive(getApplicationContext()));
		mFragments.add(new MainTabLookback(getApplicationContext()));
		mFragments.add(new MainTabHD(getApplicationContext()));
		mFragments.add(new MainTabApplication(getApplicationContext()));

		// tab按钮添加监听
		TabBtnFocLis tabBtnFocLis = new TabBtnFocLis();
		mTabBtnLive.setOnFocusChangeListener(tabBtnFocLis);
		mTabBtnHD.setOnFocusChangeListener(tabBtnFocLis);
		mTabBtnLookback.setOnFocusChangeListener(tabBtnFocLis);
		mTabBtnApplication.setOnFocusChangeListener(tabBtnFocLis);

		// headbar下划线
		headbar_underline = (ImageView) findViewById(R.id.headbar_underline);

		extra_button_width = (int) getResources().getDimension(
				R.dimen.extra_button_width);
		extra_button_height = (int) getResources().getDimension(
				R.dimen.extra_button_height);

		// 底部视图
		main_bottom = (RelativeLayout) findViewById(R.id.main_bottom);
		tips = (LinearLayout) findViewById(R.id.tips);
		mViewPager.setCurrentItem(0);
	}

	/**
	 * @author wangyuhang 获取tab内容页的顺序
	 * */
	public int getTabOrder(View v)
	{
		switch (v.getId())
		{
		case R.id.tab_btn_recommend:
			return 0;
		case R.id.tab_btn_movie:
			return 1;
		case R.id.tab_btn_game:
			return 2;
		case R.id.tab_btn_child:
			return 3;
		default:
			return 0;
		}
	}



	class TabChangeLis implements OnPageChangeListener
	{
		@Override
		public void onPageSelected(int position)
		{
			defocusTabBtn();

			// 显示推荐页下面的下拉提示箭头按钮
			if (position == 0)
			{
				main_bottom.setVisibility(View.VISIBLE);
			}
			else
			{
				main_bottom.setVisibility(View.INVISIBLE);
			}

			switch (position)
			{
			case 0:
				mTabBtnLive.setTextColor(getResources().getColor(
						R.color.white));
				break;
			case 1:
				mTabBtnHD.setTextColor(getResources()
						.getColor(R.color.white));
				break;
			case 2:
				mTabBtnLookback
						.setTextColor(getResources().getColor(R.color.white));
				break;
			case 3:
				mTabBtnApplication.setTextColor(getResources()
						.getColor(R.color.white));
				break;
			}

			currentIndex = position;

			startLineAnimation(position);// 显示headbar的下划线效果
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
		}

		@Override
		public void onPageScrollStateChanged(int arg0)
		{
		}
	}

	class TabAdapter extends FragmentPagerAdapter
	{
		public TabAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public int getCount()
		{
			return mFragments.size();
		}

		@Override
		public Fragment getItem(int arg0)
		{
			return mFragments.get(arg0);
		}
	}

	/**
	 * @author wangyuhang head bar上的tab按钮焦点状态变化监听
	 * */
	class TabBtnFocLis implements OnFocusChangeListener
	{
		@Override
		public void onFocusChange(View v, boolean hasFocus)
		{
			if (hasFocus)
			{
				headbar_underline
						.setBackgroundResource(R.drawable.headbar_focus_underline);

				mViewPager.setCurrentItem(getTabOrder(v));

				if (!v.hasFocus())
				{
					v.requestFocus();
				}
			}
			else
			{
				headbar_underline
						.setBackgroundResource(R.color.transparent_100);
			}
		}
	}
	
	/**
	 * @author wangyuhang
	 * 调整HorizontalScrollView的位置
	 * */
	public void adjustScrollViewPos(HorizontalScrollView hScrollView, View currentFocus)
	{
		// 当焦点在tab标题切换时，tab下方的内容横向坐标置为0
		if (hScrollView != null 
				&& currentFocus != null
				&& currentFocus.getContentDescription()!=null 
				&& currentFocus.getContentDescription().equals("headbar"))
		{
			hScrollView.scrollTo(0, 0);
		}
	}

	/**
	 * @author wangyuhang
	 * headbar下划线移动
	 * */
	private void startLineAnimation(int index)
	{
		if (index < 0 || index >= mFragments.size())
		{
			return;
		}

		int stepLength = ((int) getResources().getDimension(
				R.dimen.headbar_title_width))
				+ ((int) getResources().getDimension(
						R.dimen.headbar_divide_width));// 移动步长

		int underline_to_left = (int) getResources().getDimension(
				R.dimen.headbar_underline_to_left);

		Animation animation = null;
		animation = new TranslateAnimation(headbar_underline_lastPos,
				stepLength * index, 0, 0);// 下滑线动画
		headbar_underline_lastPos = stepLength * index;

		animation.setFillAfter(true);
		animation.setDuration(300);
		headbar_underline.startAnimation(animation);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK )  
        { 
			return false;
        }
		return super.onKeyDown(keyCode, event);
	}
	
}
