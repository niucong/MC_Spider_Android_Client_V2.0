package com.datacomo.mc.spider.android.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.adapter.FaceAdapter;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.FaceUtil;

public class FacesView extends RelativeLayout {
	// implements OnPageChangeListener {
	protected final String TAG = "FacesView";

	int rows = 4;
	int liesInPage = 8;
	int[] res = FaceUtil.FACE_RES_IDS;
	String[] texts = FaceUtil.FACE_TEXTS;
	// public final int DEFAULT_WIDTH = -1;
	private OnFaceChosenListner mListner;
	private int numPage = -1;

	public FacesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FacesView(Context context) {
		super(context);
		init();
	}

	private void init() {
		// setOrientation(LinearLayout.VERTICAL);
		setPadding(0, 0, 0, 10);
	}

	@SuppressWarnings("deprecation")
	public void setFaces() {
		removeAllViews();
		// HorScrollView hView = new HorScrollView(getContext());
		// hView.setContents(getFacesGrid(res));
		// hView.setOnPageChangeListener(this);
		// addView(hView, new
		// LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT));

		Init_viewPager();
		addView(Init_Data(), new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

		SignsView signs = new SignsView(getContext());
		signs.init(getPages(), 0);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		addView(signs, lp);
	}

	/** 表情页界面集合 */
	private ArrayList<View> pageViews;

	/**
	 * 初始化显示表情的viewpager
	 */
	@SuppressWarnings("deprecation")
	private void Init_viewPager() {
		pageViews = new ArrayList<View>();

		int displayWidth = ((Activity) getContext()).getWindowManager()
				.getDefaultDisplay().getWidth();

		List<FaceAdapter> faceAdapters = new ArrayList<FaceAdapter>();
		for (int i = 0; i < getPages(); i++) {

			final int[] subRes = new int[liesInPage * rows];
			for (int j = 0; j < subRes.length; j++) {
				if (j == subRes.length - 1) {
					subRes[j] = FaceUtil.RES_BACK;
				} else if (i * liesInPage * rows + j + 1 > res.length) {
					subRes[j] = FaceUtil.RES_NULL;
				} else {
					subRes[j] = res[i * (liesInPage * rows - 1) + j];
				}
			}

			FaceAdapter adapter = new FaceAdapter(getContext(), subRes,
					displayWidth / liesInPage);

			GridView view = new GridView(getContext());
			view.setAdapter(adapter);
			faceAdapters.add(adapter);
			final int p = i;
			view.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (null != mListner) {
						int pos = p * (liesInPage * rows - 1) + position;
						if (pos < texts.length) {
							mListner.onChosen(texts[pos], subRes[position]);
						} else {
							mListner.onChosen("", subRes[position]);
						}
					} else {
						L.d(TAG, "getFacesGrid mListner=null");
					}
				}
			});
			view.setNumColumns(8);
			view.setBackgroundColor(Color.TRANSPARENT);
			view.setHorizontalSpacing(1);
			view.setVerticalSpacing(1);
			view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			view.setCacheColorHint(0);
			view.setPadding(5, 0, 5, 0);
			view.setSelector(new ColorDrawable(Color.TRANSPARENT));
			view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			view.setGravity(Gravity.CENTER);
			pageViews.add(view);
		}
	}

	/**
	 * 填充数据
	 */
	@SuppressWarnings("deprecation")
	private ViewPager Init_Data() {
		ViewPager vp_face = new ViewPager(getContext());
		vp_face.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		vp_face.setAdapter(new ViewPagerAdapter(pageViews));

		vp_face.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				((SignsView) getChildAt(1)).changeIndex(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

		});
		return vp_face;
	}

	class ViewPagerAdapter extends PagerAdapter {

		private List<View> pageViews;

		public ViewPagerAdapter(List<View> pageViews) {
			super();
			this.pageViews = pageViews;
		}

		// 显示数目
		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		/***
		 * 获取每一个item�?类于listview中的getview
		 */
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}
	}

	private View[] getFacesGrid(int[] icons) {
		View[] views = new View[getPages()];
		@SuppressWarnings("deprecation")
		int displayWidth = ((Activity) getContext()).getWindowManager()
				.getDefaultDisplay().getWidth();
		for (int i = 0; i < getPages(); i++) {
			final int[] subRes = new int[liesInPage * rows];
			for (int j = 0; j < subRes.length; j++) {
				if (j == subRes.length - 1) {
					subRes[j] = FaceUtil.RES_BACK;
				} else if (i * liesInPage * rows + j + 1 > res.length) {
					subRes[j] = FaceUtil.RES_NULL;
				} else {
					subRes[j] = res[i * (liesInPage * rows - 1) + j];
				}
			}
			GridView grid = new GridView(getContext());
			grid.setSelector(getContext().getResources().getDrawable(
					R.drawable.nothing));
			grid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			grid.setNumColumns(liesInPage);
			grid.setClickable(true);
			final int p = i;
			grid.setAdapter(new FaceAdapter(getContext(), subRes, displayWidth
					/ liesInPage));
			grid.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					L.i(TAG, "getFacesGrid...");
					if (null != mListner) {
						int position = p * (liesInPage * rows - 1) + arg2;
						if (position < texts.length) {
							mListner.onChosen(texts[position], subRes[arg2]);
						} else {
							mListner.onChosen("", subRes[arg2]);
						}
					} else {
						L.d(TAG, "getFacesGrid mListner=null");
					}
				}
			});
			views[i] = grid;
		}
		return views;
	}

	private int getPages() {
		if (-1 != numPage) {
			return numPage;
		}
		numPage = res.length / (liesInPage * rows - 1);
		int extra = res.length % (liesInPage * rows - 1);
		if (extra > 0) {
			numPage += 1;
		}
		return numPage;
	}

	// @Override
	// public void onPageFliping(int which) {
	// ((SignsView) getChildAt(1)).changeIndex(which);
	// }

	public interface OnFaceChosenListner {
		void onChosen(String text, int resId);
	}

	public void setOnFaceChosenListner(OnFaceChosenListner listner) {
		mListner = listner;
	}

	/**
	 * 
	 * @param context
	 * @param edit
	 * @param text
	 * @param resId
	 */
	public static void doEditChange(Context context, EditText edit,
			String text, int resId) {
		// 获取光标位置
		int curPosition = edit.getSelectionStart();
		if (FaceUtil.RES_NULL == resId) {
			return;
		} else if (FaceUtil.RES_BACK == resId) {
			edit.onKeyDown(KeyEvent.KEYCODE_DEL, new KeyEvent(
					KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
			return;
		}
		Editable eb = edit.getEditableText();
		Drawable drawable = context.getResources().getDrawable(resId);
		int dw = drawable.getIntrinsicWidth();
		int dh = drawable.getIntrinsicHeight();
		drawable.setBounds(0, 0, dw / 2, dh / 2);
		// 需要处理的文本，[smile]是需要被替代的文本
		SpannableString spannable = new SpannableString(text);
		// 要让图片替代指定的文字就要用ImageSpan
		ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
		// 开始替换，注意第2和第3个参数表示从哪里开始替换到哪里替换结束（start和end）
		// 最后一个参数类似数学中的集合,[5,12)表示从5到12，包括5但不包括12
		spannable.setSpan(span, 0, text.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		eb.insert(curPosition, spannable);
		edit.setText(eb);
		// 设置光标位置
		edit.setSelection(curPosition + spannable.length());
	}
}
