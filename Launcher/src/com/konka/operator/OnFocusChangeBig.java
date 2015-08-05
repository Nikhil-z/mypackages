package com.konka.operator;

import com.konka.launcher.MainTabLive;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class OnFocusChangeBig implements OnFocusChangeListener {

	//public Context mContext;
	public Animation focusAnimation;
	public Animation defocusAnimation;
	public ImageView focus_img;
	private int normal_focus_width_diff;
	private int normal_focus_height_diff;
	
	public OnFocusChangeBig(){						
		// 焦点框宽高差异值
		normal_focus_width_diff = 50;
		normal_focus_height_diff =50;
		
		focusAnimation = new ScaleAnimation(1.0f,1.0f,1.0f,1.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		focusAnimation.setDuration(100);
		focusAnimation.setFillAfter(true);
		
		defocusAnimation = new ScaleAnimation(1.0f,1.0f,1.0f,1.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		defocusAnimation.setDuration(50);
		defocusAnimation.setFillAfter(false);
		
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {

			focus_img = MainTabLive.focus_img;

		if(hasFocus){
			showFocusImg(v);
		}
		else{
			dismissFocusImg();
		}
	}

	public void dismissFocusImg() {
		focus_img.startAnimation(defocusAnimation);
		focus_img.setVisibility(View.INVISIBLE);
	}

	public void showFocusImg(View v) {
		if (v == null)
		{
			return;
		}		
		FrameLayout.LayoutParams focusImgParams = (FrameLayout.LayoutParams) focus_img.getLayoutParams();
		focusImgParams.width = v.getWidth()
				+ normal_focus_width_diff;
		focusImgParams.height = v.getHeight()
				+ normal_focus_height_diff;
		
		focusImgParams.leftMargin = v.getLeft()- normal_focus_width_diff / 2+55;
		focusImgParams.topMargin = v.getTop()- normal_focus_height_diff / 2+28;
		
		focus_img.setLayoutParams(focusImgParams);
		
		focus_img.setVisibility(View.VISIBLE);
		focus_img.startAnimation(focusAnimation);
		focus_img.bringToFront();
		
	}

}
