package com.datacomo.mc.spider.android;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.ImageBucketAdapter;
import com.datacomo.mc.spider.android.adapter.ImageChooseGridViewAdapter;
import com.datacomo.mc.spider.android.adapter.ImageChoosedGridViewAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImgUtil;
import com.datacomo.mc.spider.android.view.ImageGridView;

public class ImageBucketActivity extends BasicActionBarActivity implements
		OnClickListener {
	// TAG
	private static final String TAG_LOG = "ImageBucketActivity";
	public static final int CHOOSEIMG = 101;
	// variable
	private final int mNum_Recent = 8;
	private int mItemWh;
	private int mSpace;
	// import class
	private ImageChooseGridViewAdapter mImgGvAdapter_Recent;
	private ImageChoosedGridViewAdapter mImgGvAdapter_Choosed;
	private ImageBucketAdapter mLvAdapter_Bucket;
	// view
	private ImageGridView mImgGv_RecentImg;
	private ImageGridView mImgGv_ChoosedImg;
	private ListView mLv_Buchket;
	private Button btn_Submit;
	private LinearLayout mLlayout_RecentBox;
	private HorizontalScrollView mHscroll_ChoosedBox;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_imgbucket);
		initView();
		initInfo();

	}

	private void initView() {
		// setTitle("相册", R.drawable.title_fanhui, null);
		ab.setTitle("相册");
		mLv_Buchket = (ListView) findViewById(R.id.layout_imagechoose_lv);
		btn_Submit = (Button) findViewById(R.id.form_choosed_submit);
		mHscroll_ChoosedBox = (HorizontalScrollView) findViewById(R.id.form_choosed_box);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLlayout_RecentBox = (LinearLayout) inflater.inflate(
				R.layout.head_bucket_recent, null);
		mImgGv_RecentImg = (ImageGridView) mLlayout_RecentBox
				.findViewById(R.id.head_bucket_recent_gv);
		int screenWidth = BaseData.getScreenWidth();
		int space = (int) (screenWidth * 0.02);
		mSpace = space;
		mImgGv_RecentImg.setPadding(space, space, space, space);
		mImgGv_RecentImg.setHorizontalSpacing(space);
		mImgGv_RecentImg.setVerticalSpacing(space);
		int itemWh = (screenWidth - space * 5) / 4;
		mImgGv_RecentImg.setColumnWidth(itemWh);
		mImgGvAdapter_Recent = new ImageChooseGridViewAdapter(
				getApplicationContext(), itemWh);
		mItemWh = itemWh = (int) (screenWidth * 0.15);
		mImgGv_ChoosedImg = (ImageGridView) findViewById(R.id.form_choosed_choosed);
		// mImgGv_ChoosedImg.setLayoutParams(new
		// LinearLayout.LayoutParams(itemWh
		// * 9 + space * 8, LinearLayout.LayoutParams.WRAP_CONTENT));
		mImgGv_ChoosedImg.setColumnWidth(itemWh);
		mImgGv_ChoosedImg.setHorizontalSpacing(space);
		mImgGvAdapter_Choosed = new ImageChoosedGridViewAdapter(
				getApplicationContext(), itemWh);
	}

	private void initInfo() {
		Bundle bundle = getIntentMsg();
		ImageChoosedGridViewAdapter.initChooseds(bundle
				.getStringArrayList("chooseds"));
		computeWidth(ImageChoosedGridViewAdapter.getChoosed().size());
		btn_Submit.setText("完成 "
				+ ImageChoosedGridViewAdapter.getChoosed().size() + "/"
				+ ImageChoosedGridViewAdapter.getCanChoosedNum());
		mImgGvAdapter_Recent.isInit();
		mImgGvAdapter_Recent.setBeans(new ArrayList<String>());
		mImgGv_RecentImg.setAdapter(mImgGvAdapter_Recent);
		mLvAdapter_Bucket = new ImageBucketAdapter(this,
				new LinkedHashMap<String, List<String>>());
		mLv_Buchket.addHeaderView(mLlayout_RecentBox);
		mLv_Buchket.setAdapter(mLvAdapter_Bucket);
		mImgGv_ChoosedImg.setAdapter(mImgGvAdapter_Choosed);
		spdDialog.showProgressDialog("图片加载中...");
		new RequestTask(this).execute(mNum_Recent);

	}

	private Bundle getIntentMsg() {
		Intent intent = getIntent();
		Bundle bundle = null;
		if (null != intent)
			bundle = intent.getExtras();
		return bundle;
	}

	private void bindListener() {
		mImgGv_RecentImg.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ImageView img = (ImageView) view
						.findViewById(R.id.item_grid_imgbox_img);
				ImageView brand = (ImageView) view
						.findViewById(R.id.item_grid_imgbox_brand);
				String key = img.getTag().toString();
				L.d(TAG_LOG, "key:" + key);
				if (ImageChoosedGridViewAdapter.isChoosed(key)) {
					brand.setImageResource(R.drawable.nothing);
					mHscroll_ChoosedBox.smoothScrollTo(0, 0);
					mImgGvAdapter_Choosed.unChoose(key);
					lessenWidth();
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
				img = null;
				brand = null;
				btn_Submit.setText("完成 "
						+ ImageChoosedGridViewAdapter.getChoosed().size() + "/"
						+ ImageChoosedGridViewAdapter.getCanChoosedNum());
			}
		});
		mLv_Buchket.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView bucket = (TextView) view
						.findViewById(R.id.item_imgbucket_txt_buchket);
				String key = bucket.getTag().toString();
				List<String> data = mLvAdapter_Bucket.getData(key);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("data", (ArrayList<String>) data);
				bundle.putString("bucket", key);
				LogicUtil.enter(ImageBucketActivity.this,
						ImageChooseActivity.class, bundle,
						ImageChooseActivity.CHOOSEIMG);
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
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		mLvAdapter_Bucket.clear();
		ImageChoosedGridViewAdapter.cleanChooseds();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.form_choosed_submit:
			Bundle bundle = new Bundle();
			bundle.putStringArrayList("chooseds",
					(ArrayList<String>) ImageChoosedGridViewAdapter
							.getChoosed());
			Intent data = new Intent();
			data.putExtras(bundle);
			setResult(RESULT_OK, data);
			finish();
			break;
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			Bundle bundle = new Bundle();
			bundle.putStringArrayList("chooseds",
					(ArrayList<String>) ImageChoosedGridViewAdapter
							.getChoosed());
			data = new Intent();
			data.putExtras(bundle);
			setResult(RESULT_OK, data);
			finish();
			break;
		case RESULT_CANCELED:
			computeWidth(ImageChoosedGridViewAdapter.getChoosed().size());
			flush(ImageChoosedGridViewAdapter.getChoosed(), true);
			break;
		}
	}

	private void flush(String url, boolean isFlushChoose) {
		mImgGvAdapter_Recent.unchoose(mImgGv_RecentImg, url);
		if (isFlushChoose)
			mImgGvAdapter_Choosed.notifyDataSetChanged();
		btn_Submit.setText("完成 "
				+ ImageChoosedGridViewAdapter.getChoosed().size() + "/"
				+ ImageChoosedGridViewAdapter.getCanChoosedNum());
	}

	private void flush(List<String> urls, boolean isFlushChoose) {
		mImgGvAdapter_Recent.unchoose(mImgGv_RecentImg, urls);
		if (isFlushChoose)
			mImgGvAdapter_Choosed.notifyDataSetChanged();
		btn_Submit.setText("完成 "
				+ ImageChoosedGridViewAdapter.getChoosed().size() + "/"
				+ ImageChoosedGridViewAdapter.getCanChoosedNum());
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

	class RequestTask extends AsyncTask<Object, Integer, Object[]> {
		private Object[] mParams;
		private Context mContext;

		public RequestTask(Context context) {
			mContext = context;
		}

		@Override
		protected Object[] doInBackground(Object... params) {
			mParams = params;
			Cursor cursor = null;
			LinkedHashMap<String, List<String>> dataMap = new LinkedHashMap<String, List<String>>();
			List<String> recent = new ArrayList<String>();
			try {
				cursor = mContext.getContentResolver().query(
						Media.EXTERNAL_CONTENT_URI,
						new String[] { Media._ID, Media.DATA }, null, null,
						Media._ID + " desc");
				if (null != cursor && cursor.getCount() > 0) {
					List<String> list;
					int index = 0;
					String id = null;
					String data = null;
					cursor.moveToFirst();
					File file = null;
					do {
						try {
							data = cursor.getString(cursor
									.getColumnIndex(Media.DATA));
							id = cursor.getString(cursor
									.getColumnIndex(Media._ID));
							L.d("weeee", "we:" + data + ThumbnailImgUtil.MARK
									+ id);
							L.d("keeee", "index:" + index + " num:"
									+ (Integer) mParams[0]);
							file = new File(data);
							if (!file.exists())
								continue;
							if (index < (Integer) mParams[0]) {
								L.d("reeee", "re:" + data
										+ ThumbnailImgUtil.MARK + id);
								recent.add(data + ThumbnailImgUtil.MARK + id);
								index++;
							}
							String path = data.substring(0,
									data.lastIndexOf("/"));
							if (dataMap.containsKey(path)) {
								list = dataMap.get(path);
							} else {
								list = new ArrayList<String>();
							}
							list.add(data + ThumbnailImgUtil.MARK + id);
							dataMap.put(path, list);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} while (cursor.moveToNext());
					return new Object[] { recent, dataMap };
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != cursor) {
					cursor.close();
					cursor = null;
				}
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Object[] result) {
			spdDialog.cancelProgressDialog(null);
			if (null == result) {
				T.show(mContext, "暂无照片，请选择拍照上传");
			} else {
				mImgGvAdapter_Recent.flush((List<String>) result[0]);
				mImgGvAdapter_Recent.UnInit();
				mImgGv_RecentImg.setAdapter(mImgGvAdapter_Recent);
				mLvAdapter_Bucket
						.flush((LinkedHashMap<String, List<String>>) result[1]);
				bindListener();
			}
		}

	}

}
