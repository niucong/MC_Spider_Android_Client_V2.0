package com.datacomo.mc.spider.android;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.datacomo.mc.spider.android.adapter.ImageChooseGridViewAdapter;
import com.datacomo.mc.spider.android.adapter.ImageChoosedGridViewAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.ImageGridView;

public class ImageChooseActivity extends BasicActionBarActivity {

	// TAG
	private static final String TAG_LOG = "ImageChooseActivity";
	public static final int CHOOSEIMG = 102;
	// variable
	private int mItemWh;
	private int mSpace;
	// import class
	private ImageChooseGridViewAdapter mImgGvAdapter_Choose;
	private ImageChoosedGridViewAdapter mImgGvAdapter_Choosed;
	// view
	private GridView mImgGv_ChooseImg;
	private ImageGridView mImgGv_ChoosedImg;
	private Button btn_Submit;
	private HorizontalScrollView mHscroll_ChoosedBox;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_imgchoose);
		initView();
		initInfo();

	}

	private void initView() {
		// setTitle("", R.drawable.title_fanhui, null);
		ab.setTitle("");
		mImgGv_ChooseImg = (GridView) findViewById(R.id.layout_imgchoose_gv_choose);
		btn_Submit = (Button) findViewById(R.id.form_choosed_submit);
		mHscroll_ChoosedBox = (HorizontalScrollView) findViewById(R.id.form_choosed_box);
		int screenWidth = BaseData.getScreenWidth();
		int space = (int) (screenWidth * 0.02);
		mSpace = space;
		int itemWh = (screenWidth - space * 5) / 4;
		mImgGv_ChooseImg.setPadding(space, space, space, 0);
		mImgGv_ChooseImg.setColumnWidth(itemWh);
		mImgGv_ChooseImg.setHorizontalSpacing(space);
		mImgGv_ChooseImg.setVerticalSpacing(space);
		mImgGvAdapter_Choose = new ImageChooseGridViewAdapter(
				getApplicationContext(), itemWh);
		mItemWh = itemWh = (int) (screenWidth * 0.15);
		mImgGv_ChoosedImg = (ImageGridView) findViewById(R.id.form_choosed_choosed);
		mImgGv_ChoosedImg.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		mImgGv_ChoosedImg.setColumnWidth(itemWh);
		mImgGv_ChoosedImg.setHorizontalSpacing(space);
		mImgGv_ChoosedImg.setNumColumns(1);
		mImgGvAdapter_Choosed = new ImageChoosedGridViewAdapter(
				getApplicationContext(), itemWh);
	}

	private void initInfo() {
		computeWidth(ImageChoosedGridViewAdapter.getChoosed().size());
		btn_Submit.setText("完成 "
				+ ImageChoosedGridViewAdapter.getChoosed().size() + "/"
				+ ImageChoosedGridViewAdapter.getCanChoosedNum());
		Bundle bundle = getIntentMsg();
		String bucket = bundle.getString("bucket");
		bucket = bucket.substring(bucket.lastIndexOf("/") + 1);
		// setTitle(bucket);
		ab.setTitle(bucket);
		List<String> choose = bundle.getStringArrayList("data");
		mImgGvAdapter_Choose.setBeans(choose);
		mImgGv_ChooseImg.setAdapter(mImgGvAdapter_Choose);
		mImgGv_ChoosedImg.setAdapter(mImgGvAdapter_Choosed);
		if (choose.size() > 0) {
			bindListener();
		}
	}

	private Bundle getIntentMsg() {
		Intent intent = getIntent();
		Bundle bundle = null;
		if (null != intent)
			bundle = intent.getExtras();
		return bundle;
	}

	private void bindListener() {
		mImgGv_ChooseImg.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ImageView img = (ImageView) view
						.findViewById(R.id.item_grid_imgbox_img);
				ImageView brand = (ImageView) view
						.findViewById(R.id.item_grid_imgbox_brand);
				String key = img.getTag().toString();
				if (ImageChoosedGridViewAdapter.isChoosed(key)) {
					mHscroll_ChoosedBox.smoothScrollTo(0, 0);
					mImgGvAdapter_Choosed.unChoose(key);
					lessenWidth();
					brand.setImageResource(R.drawable.nothing);
				} else {
					if (!ImageChoosedGridViewAdapter.CanChoosed()) {
						T.show(getApplicationContext(), "最多添加9张图");
						return;
					}
					brand.setImageResource(R.drawable.icon_brand_setting);
					mHscroll_ChoosedBox.smoothScrollTo(0, 0);
					largenWidth();
					mImgGvAdapter_Choosed.choose(key);
				}
				btn_Submit.setText("完成 "
						+ ImageChoosedGridViewAdapter.getChoosed().size() + "/"
						+ ImageChoosedGridViewAdapter.getCanChoosedNum());
			}
		});
		mImgGv_ChoosedImg.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ImageView img = (ImageView) view
						.findViewById(R.id.item_grid_imgbox_img);
				String url = img.getTag().toString();
				mHscroll_ChoosedBox.smoothScrollTo(0, 0);
				mImgGvAdapter_Choosed.unChoose(url);
				lessenWidth();
				img = null;
				flush(url, false);
			}
		});
		btn_Submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.form_choosed_submit:
			setResult(RESULT_OK);
			finish();
			break;
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// setResult(RESULT_CANCELED);
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// // TODO Auto-generated method stub
	//
	// }

	public void flush(String url, boolean isFlushChoose) {
		mImgGvAdapter_Choose.unchoose(mImgGv_ChooseImg, url);
		if (isFlushChoose)
			mImgGvAdapter_Choosed.notifyDataSetChanged();
		btn_Submit.setText("完成 "
				+ ImageChoosedGridViewAdapter.getChoosed().size() + "/"
				+ ImageChoosedGridViewAdapter.getCanChoosedNum());
	}

	private void computeWidth(int size) {
		if (size == 0) {
			mImgGv_ChoosedImg.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			mImgGv_ChoosedImg.setNumColumns(1);
		} else {
			int w = mItemWh * size + mSpace * (size - 1);
			L.d(TAG_LOG, "choosing : w" + w);
			mImgGv_ChoosedImg.setLayoutParams(new LinearLayout.LayoutParams(w,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			mImgGv_ChoosedImg.setNumColumns(size);
		}
	}

	private void lessenWidth() {
		if (mImgGvAdapter_Choosed.getCount() == 0) {
			mImgGv_ChoosedImg.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			mImgGv_ChoosedImg.setNumColumns(1);
		} else {
			int w = mItemWh * mImgGvAdapter_Choosed.getCount() + mSpace
					* (mImgGvAdapter_Choosed.getCount() - 1);
			L.d(TAG_LOG, "choosing : w" + -w);
			mImgGv_ChoosedImg.setLayoutParams(new LinearLayout.LayoutParams(w,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			mImgGv_ChoosedImg.setNumColumns(mImgGvAdapter_Choosed.getCount());
		}
	}

	private void largenWidth() {
		int w = mItemWh
				* (mImgGvAdapter_Choosed.getCount() + 1)
				+ mSpace
				* (mImgGvAdapter_Choosed.getCount() == 0 ? 0
						: (mImgGvAdapter_Choosed.getCount()));
		L.d(TAG_LOG, "choosing : w" + w);
		mImgGv_ChoosedImg.setLayoutParams(new LinearLayout.LayoutParams(w,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		mImgGv_ChoosedImg.setNumColumns(mImgGvAdapter_Choosed.getCount() + 1);
	}

}
