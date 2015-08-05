package android.test.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ExceptionInfo extends PopupWindow
{
	private static ExceptionInfo instance;
	
	private Context mContext;
	
	private LinearLayout mLinearLayout;
	private LayoutInflater inflater;
	
	private TextView textview_exception_content;
	
	private ExceptionInfo(){}
	
	private ExceptionInfo(Context context)
	{
		mContext = context;
		
		init();
		
		this.setFocusable(false);
		this.setOutsideTouchable(false);
		this.setContentView(mLinearLayout);
		this.setWidth(770);
		this.setHeight(120);
	}
	
	public synchronized static ExceptionInfo getInstance(Context context)
	{
		if(instance == null)
		{
			instance = new ExceptionInfo(context);
		}
		
		return instance;
	}
	
	public void init()
	{
		inflater = (LayoutInflater) mContext
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		mLinearLayout = (LinearLayout) inflater.inflate(R.layout.exception_info, null);
		
		textview_exception_content = (TextView) mLinearLayout
				.findViewById(R.id.exception_content);
	}
}
