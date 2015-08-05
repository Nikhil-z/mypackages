package com.konka.operator;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

public class OnFocusChangeLis implements OnFocusChangeListener {

	//public Context mContext;
	public Animation focusAnimation;
	public Animation defocusAnimation;
	public Animation focusPicAnimation;
	public Animation defocusPicAnimation;
	public ImageView focus_img;
	public  ImageButton imageButton;
	private int normal_focus_width_diff;
	private int normal_focus_height_diff;
	
	public OnFocusChangeLis(){						
		// 焦点框宽高差异值
		normal_focus_width_diff = 48;
		normal_focus_height_diff =50;
		
		focusAnimation = new ScaleAnimation(1.0f,1.1f,1.0f,1.1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		focusAnimation.setDuration(100);
		focusAnimation.setFillAfter(true);
		
		defocusAnimation = new ScaleAnimation(1.0f,1.0f,1.0f,1.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		defocusAnimation.setDuration(50);
		defocusAnimation.setFillAfter(false);
		
		focusPicAnimation = new ScaleAnimation(1.0f,1.1f,1.0f,1.1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		focusPicAnimation.setDuration(100);
		focusPicAnimation.setFillAfter(true);
		
		defocusPicAnimation = new ScaleAnimation(1.0f,1.0f,1.0f,1.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		defocusPicAnimation.setDuration(50);
		defocusPicAnimation.setFillAfter(false);
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		
			focus_img = Refrences.getFocusImg(v);
			//focus_img = MainTabLive.focus_img;	

		if(hasFocus){
			showFocusImg(v);
			
		}
		else{
			dismissFocusImg();
			v.startAnimation(defocusPicAnimation);
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
		
		focusImgParams.leftMargin = v.getLeft()- normal_focus_width_diff / 2-2;
		focusImgParams.topMargin = v.getTop()- normal_focus_height_diff / 2;
		
		focus_img.setLayoutParams(focusImgParams);
		
		focus_img.setVisibility(View.VISIBLE);
		focus_img.startAnimation(focusAnimation);
		v.startAnimation(focusPicAnimation);
		focus_img.bringToFront();
		
	}

}
