package com.datacomo.mc.spider.android.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.adapter.GroupSingleGridViewAdapter;
import com.datacomo.mc.spider.android.bean.GroupEntity;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.view.HorScrollView.OnPageChangeListener;

public class GroupChoosedsHorScrollView extends LinearLayout implements
		OnPageChangeListener {
	private static final String LOG_TAG = "GroupChoosedsHorScrollView";
	private static boolean mIsShow;
	private final int PAGESIZE = 6;
	private final int DEFAULT_WIDTH = BaseData.getScreenWidth();
	private int mGroupBoxHeight;
	private List<List<GroupEntity>> mGroupsBox;
	private Handler mHandler;
	private boolean mIsAddEnable;

	public GroupChoosedsHorScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public GroupChoosedsHorScrollView(Context context) {
		super(context);
		init();
	}

	public static boolean isShow() {
		return mIsShow;
	}

	public static void setIsShow(Boolean isShow) {
		mIsShow = isShow;
	}

	public List<GroupEntity> getGroups() {
		return merge(mGroupsBox);
	}

	public List<String> getGroupIds() {
		List<GroupEntity> temp = merge(mGroupsBox);
		List<String> ids = new ArrayList<String>();
		for (GroupEntity entity : temp) {
			ids.add(entity.getId());
		}
		temp.clear();
		temp = null;
		return ids;
	}

	public void setGroups(List<GroupEntity> groups) {
		mGroupsBox = paging(groups);
	}

	public void setHandler(Handler handler) {
		mHandler = handler;
	}

	
	public int getGroupBoxHeight() {
		return mGroupBoxHeight;
	}

	private void init() {
		setOrientation(LinearLayout.VERTICAL);
		mIsAddEnable = true;
	}

	public void initGroupBox() {
		removeAllViews();
		SignsView signs = new SignsView(getContext());
		int page=getPages();
		if(page>1){
			signs.init(getPages(), 0);
		}
		addView(signs, new LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (DEFAULT_WIDTH * 0.05)));
		int spacing = (int) (DEFAULT_WIDTH * 0.01);
		int itemWidth = (DEFAULT_WIDTH - (spacing * 10 + spacing * 10)) / 3;
		mGroupBoxHeight = spacing * 3 + itemWidth * 2;
		L.d(LOG_TAG, "height:" + mGroupBoxHeight);
		HorScrollView hView = new HorScrollView(getContext());
		hView.setContents(getGroupsGird());
		hView.setOnPageChangeListener(this);
		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, mGroupBoxHeight);
		lp.setMargins(0, 5, 0, 0);
		addView(hView, lp);
	}

	public void flush(List<GroupEntity> groups) {
		mGroupsBox.clear();
		mGroupsBox.addAll(paging(groups));
		initGroupBox();
	}
	
	
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}

	public void isAddEnabled(Boolean isEnabled) {
		mIsAddEnable = isEnabled;
	}

	public boolean isEmpty() {
		if (null != mGroupsBox && mGroupsBox.size() > 1)
			return true;
		return false;
	}

	private View[] getGroupsGird() {
		List<View> views = new ArrayList<View>();
		int spacing = (int) (DEFAULT_WIDTH * 0.01);
		int itemWidth = (DEFAULT_WIDTH - (spacing * 10 + spacing * 10)) / 3;
		for (List<GroupEntity> groups : mGroupsBox) {
			GridView grid = new GridView(getContext());
			grid.setSelector(getContext().getResources().getDrawable(
					R.drawable.nothing));
			grid.setCacheColorHint(getContext().getResources().getColor(
					R.color.transparent));
			grid.setStretchMode(GridView.NO_STRETCH);
			grid.setNumColumns(3);
			grid.setColumnWidth(itemWidth);
			grid.setHorizontalSpacing(spacing * 5);
			grid.setVerticalSpacing(spacing * 3);
			grid.setPadding(spacing * 5, 0, spacing * 5, 0);
			grid.setAdapter(new GroupSingleGridViewAdapter(groups, itemWidth));
			grid.setOnItemClickListener(onItemClickListener);
			views.add(grid);
		}
		return views.toArray(new View[0]);
	}

	private List<GroupEntity> merge(List<List<GroupEntity>> groupsbox) {
		List<GroupEntity> list = new ArrayList<GroupEntity>();
		for (List<GroupEntity> groups : groupsbox) {
			list.addAll(groups);
		}
		if (mIsAddEnable)
			list.remove(0);
		return list;
	}

	private List<List<GroupEntity>> paging(List<GroupEntity> groups) {
		List<List<GroupEntity>> groupsbox = new ArrayList<List<GroupEntity>>();
		int page = 1;
		int size = 0;
		List<GroupEntity> temp = new ArrayList<GroupEntity>();
		if (mIsAddEnable) {
			temp.add(new GroupEntity("", GroupSingleGridViewAdapter.STR_Add, "", "", "", ""));
			size = 1;
		}
		for (GroupEntity bean : groups) {
			temp.add(bean);
			size++;
			if (size == (PAGESIZE * page)) {
				groupsbox.add(temp);
				temp = new ArrayList<GroupEntity>();
				page++;
			} else if ((mIsAddEnable&&size == groups.size() + 1)||(!mIsAddEnable&&size == groups.size())) {
				groupsbox.add(temp);
			}
		}
		return groupsbox;
	}

	private int getPages() {
		if (null == mGroupsBox)
			return 0;
		return mGroupsBox.size();
	}

	@Override
	public void onPageFliping(int which) {
		((SignsView) getChildAt(0)).changeIndex(which);
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			GroupEntity bean = (GroupEntity) parent.getItemAtPosition(position);
			if (GroupSingleGridViewAdapter.STR_Add.equals(bean.getName()))
				mHandler.sendEmptyMessage(0);
		}
	};

}
