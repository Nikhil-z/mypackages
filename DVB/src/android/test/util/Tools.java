package android.test.util;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
//import android.os.SystemProperties;
import android.widget.TextView;
import android.widget.Toast;

public class Tools {

	private static final String MSTAR_PRODUCT_CHARACTERISTICS = "mstar.product.characteristics";
	private static final String MSTAR_PRODUCT_STB = "stb";
	private static String mProduct = null;
	
	public static void toastShow(int resId, Context context) {
		TextView MsgShow = new TextView(context);
		MsgShow.setTextColor(Color.RED);
		MsgShow.setTextSize(25);
		MsgShow.setText(resId);
		
		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(MsgShow);
		toast.show();
	}

	/**
	 * 
	 * @param context
	 * @param text The text that toast will show.
	 * @param color The text color, use Color.xxx
	 */
	public static void toastShow(Context context, String text, int textColor, int bgColor) {
		TextView MsgShow = new TextView(context);
		MsgShow.setTextColor(textColor);
		MsgShow.setBackgroundColor(bgColor);
		MsgShow.setTextSize(20);
		MsgShow.setText(text);
		
		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(MsgShow);
		toast.show();
	}

    public static boolean isBox() {
        if (mProduct == null) {
//        	mProduct = System.getProperty(MSTAR_PRODUCT_CHARACTERISTICS, "");
        	//mProduct = SystemProperties.get(MSTAR_PRODUCT_CHARACTERISTICS, "");
        }
        if (MSTAR_PRODUCT_STB.equals(mProduct)) {
            return true;
        } else {
            return false;
        }
    }

	public static ArrayList<Integer> getPicFromNum(int number) {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		char pos;
		final String num = number + "";
	/*	
		for(int i = 0; i < num.length(); i++) {
			pos = num.charAt(i);
			switch (pos) {
			case '0':
				arr.add(R.drawable.popup_img_number_0);
				break;
			case '1':
				arr.add(R.drawable.popup_img_number_1);
				break;
			case '2':
				arr.add(R.drawable.popup_img_number_2);
				break;
			case '3':
				arr.add(R.drawable.popup_img_number_3);
				break;
			case '4':
				arr.add(R.drawable.popup_img_number_4);
				break;
			case '5':
				arr.add(R.drawable.popup_img_number_5);
				break;
			case '6':
				arr.add(R.drawable.popup_img_number_6);
				break;
			case '7':
				arr.add(R.drawable.popup_img_number_7);
				break;
			case '8':
				arr.add(R.drawable.popup_img_number_8);
				break;
			case '9':
				arr.add(R.drawable.popup_img_number_9);
				break;
			default:
			}
		}
		*/
		return arr;
	}

	/**
	 * Print Trace
	 * DEBUG ONLY
	 */
	public static void printTrace() {
	     try {
	           Throwable trace = new Throwable();
	           throw trace;
	     } catch (Throwable e) {
	          e.printStackTrace();
	     }
	}
}
