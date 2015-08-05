package com.konka.launcher.app;

import com.konka.launcher.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class KKAppButton extends RelativeLayout {
	private static final String TAG = "KKAppButton";

	private Context mContext;

	private RelativeLayout main;
	private ImageView icon;
	private TextView label;
	private String pkg;
	private String cls;
	private int rowIndex = -1;

	public KKAppButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		initView(context);
		initAttrs(context, attrs);
	}

	public KKAppButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		initView(context);
		initAttrs(context, attrs);
	}

	public KKAppButton(Context context) {
		super(context);

		initView(context);
	}

	public void setTag(String text){
		if(main != null){
			main.setTag(text);
		}
	}
	
	public String getTag(){
		if(main != null){
			return (String) main.getTag();
		}
		return null;
	}
	
	public void setText(String text){
		this.label.setText(text);
	}
	
	public String getText(){
		return label.getText().toString();
	}
	
	public void setIcon(Drawable drawable){
		this.icon.setBackground(drawable);
	}
	
	public Drawable getIcon(){
		return icon.getBackground();
	}
	
	public void setPkg(String pkg){
		this.pkg = pkg;
	}
	
	public String getPkg() {
		return pkg;
	}
	
	public void setCls(String cls){
		this.cls = cls;
	}

	public String getCls() {
		return cls;
	}
	
	public void setRowIndex(int row){
		this.rowIndex = row;
	}
	
	public int getRowIndex() {
		return rowIndex;
	}
	 
	public void initView(Context context) {
		mContext = context;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.kkapp_button, this);
		
		icon = (ImageView) findViewById(R.id.icon);
		label = (TextView) findViewById(R.id.label);
		
		main = (RelativeLayout) findViewById(R.id.main);
	}

	public void initAttrs(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.kkappbutton);

		// background
		Drawable background = a.getDrawable(R.styleable.kkappbutton_background);
		if(background != null){
			main.setBackground(background);
		}
		
		// tag
		String button_tag = a.getString(R.styleable.kkappbutton_tag);
		if (button_tag != null) {
//			imageView.setTag(button_tag);
			main.setTag(button_tag);
			Log.v(TAG, "======= main.getTag()="+main.getTag());
		}
		
		// contentDescription
		String button_contentDescription = a
				.getString(R.styleable.kkappbutton_contentDescription);
		if (button_contentDescription != null) {
			icon.setContentDescription(button_contentDescription);
		}
		
		// icon
		Drawable iconB = a.getDrawable(R.styleable.kkappbutton_icon);
		if(iconB != null){
			icon.setBackground(iconB);
		}
		
		// label
		String app_label = a.getString(R.styleable.kkappbutton_label);
		if(app_label != null){
			label.setText(app_label);
		}
		
		a.recycle();
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		// TODO Auto-generated method stub
		super.setOnClickListener(l);
		main.setOnClickListener(l);
	}

	
	@Override
	public void setOnFocusChangeListener(OnFocusChangeListener l) {
		// TODO Auto-generated method stub
//		super.setOnFocusChangeListener(l);
		main.setOnFocusChangeListener(l);
	}

	@Override
	public void setNextFocusUpId(int nextFocusUpId) {
		// TODO Auto-generated method stub
		super.setNextFocusUpId(nextFocusUpId);
		main.setNextFocusUpId(nextFocusUpId);
	}
	
	@Override
	public void setContentDescription(CharSequence contentDescription) {
		// TODO Auto-generated method stub
		super.setContentDescription(contentDescription);
		main.setContentDescription(contentDescription);
	}
	
}
