package android.test.ui.epg;

import java.util.ArrayList;
import java.util.List;

import android.com.konka.dvb.prog.ProgManager.ProgShowInfo;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.test.ui.R;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProgListAdapter extends BaseAdapter {
	private String TAG = "ProgListAdapter";
	private Context mContext;
	private LayoutInflater mInflater;
	
	private List<ProgShowInfo> mProgList;
	private int curSelect = -1;
	
	public ProgListAdapter(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mProgList = new ArrayList<ProgShowInfo>();
	}

	public void updateProList(ArrayList<ProgShowInfo> progList){
		mProgList = progList;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mProgList != null){
			return mProgList.size();
		}
		return 0;
	}

	@Override
	public ProgShowInfo getItem(int position) {
		// TODO Auto-generated method stub
		if(mProgList != null){
			return mProgList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		if(mProgList != null && position < mProgList.size()){
			return position;
		}
		return -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = mInflater.inflate(R.layout.proglist_item, null);
		
		ProgListItemHolder holder = new ProgListItemHolder();
		holder.prog_select = (ImageView) convertView.findViewById(R.id.prog_select);
		holder.prog_seq = (TextView) convertView.findViewById(R.id.prog_seq);
		holder.prog_name = (TextView) convertView.findViewById(R.id.prog_name);
		
		ProgShowInfo progShowInfo = mProgList.get(position);
		holder.prog_seq.setText(progShowInfo.prog_num);
		holder.prog_name.setText(progShowInfo.prog_name);
		
		if(curSelect == position){
			holder.prog_select.setVisibility(View.VISIBLE);
		}
		
		convertView.setTag(holder);
		
		return convertView;
	}
	
	public void setSelect(int cur){
		curSelect = cur;
		notifyDataSetChanged();
	}
	
	private class ProgListItemHolder{
		private ImageView prog_select;
		private TextView prog_seq;
		private TextView prog_name;
	}
	
}