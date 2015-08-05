package com.konka.operator;

import com.konka.launcher.MainTabApplication;
import com.konka.launcher.MainTabHD;
import com.konka.launcher.MainTabLive;
import com.konka.launcher.MainTabLookback;

import android.view.View;
import android.widget.ImageView;

public class Refrences {
	
	public static ImageView getFocusImg(View view)
	{
		int contentDescription = Integer.parseInt((String) view
				.getContentDescription());

		switch (contentDescription)
		{
		case 1:
			return MainTabLive.focus_img;
		case 2:
			return MainTabHD.focus_img;
		case 3:
			return MainTabLookback.focus_img;
		case 4:
			return MainTabApplication.focus_img;
		default:
			return null;
		}
	}

}
