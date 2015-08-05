package android.test.ui;

import java.util.List;

import android.com.konka.dvb.prog.ProgManager.ProgShowInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProListAdapter extends BaseAdapter {

	private Context context;
	private List<ProgShowInfo>  list;
	private int curSelect = -1;

	public ProListAdapter(Context context, List<ProgShowInfo> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public ProgShowInfo getItem(int position) {
		if(list != null){
			return list.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		if(list != null && position < list.size()){
			return position;
		}
		return -1;
	}



	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		if(view ==null){
			view = LayoutInflater.from(context).inflate(R.layout.settinglist_prolist_item, null);
            /*得到各个控件的对象*/ 
		}
			ProgListItemHolder holder = new ProgListItemHolder();
		
		holder.prog_select = (ImageView) view.findViewById(R.id.prog_select);
		holder.prog_seq = (TextView) view.findViewById(R.id.prog_seq);
		holder.prog_name = (TextView) view.findViewById(R.id.prog_name);
		
		ProgShowInfo progShowInfo = list.get(position);
		holder.prog_seq.setText(progShowInfo.prog_num);
		holder.prog_name.setText(progShowInfo.prog_name);
		
		if(curSelect == position){
			holder.prog_select.setVisibility(View.VISIBLE);
		}
		
		view.setTag(holder);
		
		return view;
	}
	private class ProgListItemHolder{
		private ImageView prog_select;
		private TextView prog_seq;
		private TextView prog_name;
	}

}
