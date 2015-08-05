package android.test.ui;

import java.util.List;

import android.com.konka.dvb.prog.ProgManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	private Context context;
	private List<String>  list;
	

	public MyAdapter(Context context, List<String> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	



	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		if(view ==null){
			view = LayoutInflater.from(context).inflate(R.layout.listview_item, null);
            /*得到各个控件的对象*/  
			
		}
		TextView textview1 = (TextView)view.findViewById(R.id.listview_item_textview);
		textview1.setText(list.get(position));
		
		return view;
	}

}
