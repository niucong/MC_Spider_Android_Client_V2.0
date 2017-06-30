package com.datacomo.mc.spider.android.view;
//package com.datacomo.mc.spider.android.view;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.Activity;
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.LinearLayout;
//
//import com.datacomo.mc.spider.android.bean.GroupEntity;
//import com.datacomo.mc.spider.android.view.HorScrollView.OnPageChangeListener;
//
//public class GroupsView extends LinearLayout implements OnPageChangeListener {
//	int rows = 4;
//	int liesInPage = 4;
//	List<GroupEntity> mGroups;
//	public final int DEFAULT_WIDTH = -1;
//	private int numPage = -1;
//
//	public List<GroupEntity> getRes() {
//		return mGroups;
//	}
//
//	public void setRes(List<GroupEntity> groups) {
//		mGroups = groups;
//	}
//
//	public GroupsView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		init();
//	}
//
//	public GroupsView(Context context) {
//		super(context);
//		init();
//	}
//
//	private void init() {
//		setOrientation(LinearLayout.VERTICAL);
//		setPadding(0, 0, 0, 10);
//	}
//
//	public int getRows() {
//		return rows;
//	}
//
//	public void setRows(int rows) {
//		this.rows = rows;
//	}
//
//	public int getLiesInPage() {
//		return liesInPage;
//	}
//
//	public void setLiesInPage(int liesInPage) {
//		this.liesInPage = liesInPage;
//	}
//
//	// @SuppressWarnings("deprecation")
//	// public void setFaces() {
//	// removeAllViews();
//	// HorScrollView hView = new HorScrollView(getContext());
//	// hView.setContents(getFacesGrid(res));
//	// hView.setOnPageChangeListener(this);
//	// addView(hView, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
//	// LayoutParams.WRAP_CONTENT));
//	//
//	// SignsView signs = new SignsView(getContext());
//	// signs.init(getPages(), 0);
//	// addView(signs, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
//	// LayoutParams.WRAP_CONTENT));
//	// }
//	//
//	 private View[] getGroupGrid(List<GroupEntity> chooseds) {
//	 View[] views = new View[getPages()];
//	 @SuppressWarnings("deprecation")
//	 int displayWidth = ((Activity) getContext()).getWindowManager()
//	 .getDefaultDisplay().getWidth();
//	 int start=0;
//	 int end=liesInPage * rows;
//	 int conpage=0;
//	 for (int i = 0; i < getPages(); i++) {
//		 List<GroupEntity> subRes = new ArrayList<GroupEntity>();
//		 if(chooseds.size()<(liesInPage * rows)){
//			 end=chooseds.size();
//		 }
//		 for(int j = start; i < getPages(); i++){
//			 
//		 }
//		 conpage++;
//		 }
//	 
//	 for (int j = 0; j < subRes.length; j++) {
//	 if (j == subRes.length - 1) {
//	 subRes[j] = FaceUtil.RES_BACK;
//	 } else if (i * liesInPage * rows + j + 1 > res.length) {
//	 subRes[j] = FaceUtil.RES_NULL;
//	 } else {
//	 subRes[j] = res[i * (liesInPage * rows - 1) + j];
//	 }
//	 }
//	 GridView grid = new GridView(getContext());
//	 grid.setSelector(getContext().getResources().getDrawable(
//	 R.drawable.nothing));
//	 grid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
//	 grid.setNumColumns(liesInPage);
//	 final int p = i;
//	 grid.setAdapter(new FaceAdapter(getContext(), subRes, displayWidth
//	 / liesInPage));
//	 grid.setOnItemClickListener(new OnItemClickListener() {
//	 @Override
//	 public void onItemClick(AdapterView<?> arg0, View arg1,
//	 int arg2, long arg3) {
//	 if (null != mListner) {
//	 int position = p * (liesInPage * rows - 1) + arg2;
//	 if (position < texts.length) {
//	 mListner.onChosen(texts[position], subRes[arg2]);
//	 } else {
//	 mListner.onChosen("", subRes[arg2]);
//	 }
//	 }
//	 }
//	 });
//	 views[i] = grid;
//	 }
//	 return views;
//	 }
//
//	private int getPages() {
//		if (-1 != numPage) {
//			return numPage;
//		}
//		numPage = (mGroups.size()+1) / (liesInPage * rows);
//		int extra = (mGroups.size()+) % (liesInPage * rows);
//		if (extra > 0) {
//			numPage += 1;
//		}
//		return numPage;
//	}
//
//	@Override
//	public void onPageFliping(int which) {
//		((SignsView) getChildAt(1)).changeIndex(which);
//	}
//
//}
