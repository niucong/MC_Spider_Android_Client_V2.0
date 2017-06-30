package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;

public class AutoWordAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<String> texts;
	private OnTextChosenListener listener;
	public AutoWordAdapter(Context context, ArrayList<String> res, OnTextChosenListener onTextChosenListener) {
		mContext = context;
		texts = res;
		listener = onTextChosenListener;
	}
	@Override
	public int getCount() {
		return texts.size();
	}

	@Override
	public Object getItem(int arg0) {
		return texts.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if(null == arg1){
			arg1 = View.inflate(mContext, R.layout.item_simple_text, null);
		}
		TextView tv = (TextView) arg1.findViewById(R.id.text);
		if(0 == arg0){
			tv.setTextColor(mContext.getResources().getColor(R.color.blue));
		}
		final int index = arg0;
		final String text = texts.get(index);
		tv.setText(text);
		arg1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(null != listener){
					if(texts.size() >= 2){
						listener.onChosen(text, (0 == index) && (!text.equals(texts.get(1))));//第一条为用户输入的key， 同时前两条不能相同	
					}else{
						listener.onChosen(text, 0 == index); //只有一条数据的时候，增加搜索记录
					}
				}
			}
		});
		return arg1;
	}
	
	public interface OnTextChosenListener{
		void onChosen(String chosenString, boolean isFreshKeyWords);
	}

}
