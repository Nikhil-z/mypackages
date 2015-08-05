package com.konka.custom.view;

import com.konka.launcher.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class KKVideoView extends VideoView
{
	public KKVideoView(Context context)
	{
		super(context);
	}

	public KKVideoView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public KKVideoView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = (int) getResources().getDimension(R.dimen.extra_button_width);
		int height = (int) getResources().getDimension(
				R.dimen.extra_button_height);

		setMeasuredDimension(width, height);
	}
}
