package com.datacomo.mc.spider.android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.Html.TagHandler;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.AdditiveFileListAdapter;
import com.datacomo.mc.spider.android.adapter.ImageChoosedGridViewAdapter;
import com.datacomo.mc.spider.android.adapter.ImageSingleGridViewAdapter;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.AdditiveFileBean;
import com.datacomo.mc.spider.android.bean.NoteDraftBean;
import com.datacomo.mc.spider.android.bean.TransitionBean;
import com.datacomo.mc.spider.android.db.NoteCreateService;
import com.datacomo.mc.spider.android.net.APINoteRequestServers;
import com.datacomo.mc.spider.android.net.HttpRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.note.AttachmentBean;
import com.datacomo.mc.spider.android.net.been.note.NoteInfoBean;
import com.datacomo.mc.spider.android.params.UploadFileParams;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.URLProperties;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.HTMLDecoder;
import com.datacomo.mc.spider.android.util.HandlerUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.StreamUtil;
import com.datacomo.mc.spider.android.util.StringUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.datacomo.mc.spider.android.util.ThumbnailImgUtil;
import com.datacomo.mc.spider.android.view.DialogView;
import com.datacomo.mc.spider.android.view.FileListView;
import com.datacomo.mc.spider.android.view.ImageGridView;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.XMLReader;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

public class NoteCreateActivity extends BasicActionBarActivity {
	private final String TAG = "NoteCreateActivity";

	private EditText title, content;
	private ImageView iv_xiangji, iv_wenb, iv_huat, iv_lux, iv_luyinj, iv_wanc;
	private LinearLayout ll_luyin, ll_add;
	private TextView tv_luyin_time;

	private boolean allowedPublish = true;
	private AlertDialog pDialog = null;
	private DialogView dialogContent = null;
	private ImageSingleGridViewAdapter mImgGvAdapter_Choosed;
	private AdditiveFileListAdapter mAdditiveFileLvAdapter_Choosed;
	private ImageGridView mImgGv_ChoosedImg;
	private FileListView mAdditiveFileLv_ChoosedImg;
	private ViewStub mVs_FileLv, mVs_ImgGv;
	private CheckBox checkBox;
	// 已上传成功的path与时间戳
	private LinkedHashMap<String, String> fileTempsList;
	private LinkedHashMap<String, String> photoTempsList;
	private int mItemWh;
	private int mSpace;

	private boolean isSaving;
	private NoteInfoBean nibean;
	private boolean isEditNote, isEditLac;
	private String nTitleStr, nContentStr, titleStr, contentStr;

	private int position = -1;
	private NoteDraftBean nb;
	private int noteBookId;

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		sendHandler.removeCallbacksAndMessages(null);
		stopRecord();
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "22");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_note_create);

		fileTempsList = new LinkedHashMap<String, String>();
		photoTempsList = new LinkedHashMap<String, String>();

		findViews();
		checkInfo();
		title.requestFocus();
	}

	private void findViews() {
		title = (EditText) findViewById(R.id.t_title);
		content = (EditText) findViewById(R.id.t_content);
		TextWatcher watcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		};
		title.addTextChangedListener(watcher);
		content.addTextChangedListener(watcher);

		iv_xiangji = (ImageView) findViewById(R.id.note_create_xiangji);
		iv_wenb = (ImageView) findViewById(R.id.note_create_wenb);
		iv_huat = (ImageView) findViewById(R.id.note_create_huat);
		iv_lux = (ImageView) findViewById(R.id.note_create_lux);

		iv_luyinj = (ImageView) findViewById(R.id.note_create_luyinj);
		iv_wanc = (ImageView) findViewById(R.id.note_create_wanc);
		ll_add = (LinearLayout) findViewById(R.id.note_create_add);
		ll_luyin = (LinearLayout) findViewById(R.id.note_create_luyin);
		tv_luyin_time = (TextView) findViewById(R.id.note_create_time);

		iv_xiangji.setOnClickListener(this);
		iv_wenb.setOnClickListener(this);
		iv_huat.setOnClickListener(this);
		iv_lux.setOnClickListener(this);
		iv_luyinj.setOnClickListener(this);
		iv_wanc.setOnClickListener(this);

		int screenWidth = BaseData.getScreenWidth();
		mSpace = (int) (screenWidth * 0.02);
		mItemWh = (screenWidth - mSpace * 5) / 4;
		mImgGvAdapter_Choosed = new ImageSingleGridViewAdapter(
				new ArrayList<String>(), mItemWh);
		mAdditiveFileLvAdapter_Choosed = new AdditiveFileListAdapter(this,
				new ArrayList<Object>());
		mVs_ImgGv = (ViewStub) findViewById(R.id.note_create_imagegrid);
		mVs_ImgGv.setOnInflateListener(new OnInflateListener() {

			@Override
			public void onInflate(ViewStub stub, View inflated) {
				boolean upload_original_image = App.app.share
						.getBooleanMessage("program", "upload_original_image",
								false);
				checkBox = (CheckBox) inflated
						.findViewById(R.id.create_topic_checkBox);
				checkBox.setChecked(upload_original_image);
				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						showPhotoSize(mImgGvAdapter_Choosed.getUrls());
						App.app.share.saveBooleanMessage("program",
								"upload_original_image", isChecked);
					}
				});
				mImgGv_ChoosedImg = (ImageGridView) findViewById(R.id.layout_createtopic_choosedimg);
				mImgGv_ChoosedImg.setColumnWidth(mItemWh);
				mImgGv_ChoosedImg.setHorizontalSpacing(mSpace);
				mImgGv_ChoosedImg.setPadding(mSpace, mSpace, mSpace, mSpace);
				mImgGv_ChoosedImg.setVerticalSpacing(mSpace);
				mImgGv_ChoosedImg
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								if (position > mImgGvAdapter_Choosed.getUrls()
										.size() - 1) {
									choosePicture();
								} else {
									showPhoto(mImgGvAdapter_Choosed.getUrls()
											.get(position));
								}
							}
						});
				mImgGv_ChoosedImg
						.setOnItemLongClickListener(new OnItemLongClickListener() {

							@Override
							public boolean onItemLongClick(
									AdapterView<?> parent, View view,
									int position, long id) {
								if (position <= mImgGvAdapter_Choosed.getUrls()
										.size() - 1)
									deleteDialog(mImgGvAdapter_Choosed
											.getUrls().get(position), position);
								return true;
							}
						});
				mImgGvAdapter_Choosed.setUrls(new ArrayList<String>());
				mImgGv_ChoosedImg.setAdapter(mImgGvAdapter_Choosed);
			}
		});
		mVs_FileLv = (ViewStub) findViewById(R.id.note_create_filelist);
		mVs_FileLv.setOnInflateListener(new OnInflateListener() {

			@Override
			public void onInflate(ViewStub stub, View inflated) {
				mAdditiveFileLv_ChoosedImg = (FileListView) inflated
						.findViewById(R.id.layout_createtopic_filelist);
				mAdditiveFileLv_ChoosedImg
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								TransitionBean bean = null;
								try {
									bean = new TransitionBean(parent
											.getItemAtPosition(position));
								} catch (Exception e) {
									e.printStackTrace();
								}
								switch (bean.getType()) {
								case ADDITIVEFILE:
									new FileUtil().openFile(
											NoteCreateActivity.this, new File(
													bean.getPath()), bean
													.getMimeType());
									break;
								default:
									break;
								}
							}
						});
				mAdditiveFileLv_ChoosedImg
						.setAdapter(mAdditiveFileLvAdapter_Choosed);
			}
		});
	}

	private void changeImg(String path, boolean hasId) {
		L.i(TAG, "image=" + path);
		List<String> urls = new ArrayList<String>();
		if (hasId) {
			urls.add(path);
		} else {
			urls.add(path + ThumbnailImgUtil.SUFFIX);
		}
		spdDialog.showProgressDialog("正在获取图片...");
		new RequestTask(App.app, urls, 0).execute("");
	}

	class RequestTask extends AsyncTask<Object, Integer, List<String>> {
		private Context mContext;
		private List<String> mUrls;
		private int mType;

		/**
		 * 
		 * @param context
		 * @param urls
		 * @param type
		 *            0 is from camera 1 is from bucket
		 */
		public RequestTask(Context context, List<String> urls, int type) {
			mContext = context;
			mUrls = urls;
			mType = type;
		}

		@Override
		protected List<String> doInBackground(Object... params) {
			String[] urls = mUrls.toArray(new String[] {});
			mUrls.removeAll(mUrls);
			for (String url : urls) {
				if (ThumbnailImgUtil.isUrlEmpty(url)) {
					String[] bean = null;
					do {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						bean = ThumbnailImgUtil.split(mContext, url);
						L.d(TAG, "data:" + bean[0] + " id:" + bean[1]);
					} while (ThumbnailImgUtil.isIdEmpty(bean[1]));
					url = bean[0] + ThumbnailImgUtil.MARK + bean[1];
					mUrls.add(url);
				} else {
					mUrls.add(url);
				}
			}
			return mUrls;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			spdDialog.cancelProgressDialog(null);
			switch (mType) {
			case 0:
				addPhoto(result.get(0));
				break;
			case 1:
				addPhoto(result);
				break;
			}
		}

	}

	/**
	 * 添加图片
	 * 
	 * @param photoPath
	 */
	private void addPhoto(String photoPath) {
		// TODO
		showImageBox();
		mImgGvAdapter_Choosed.add(photoPath);
		showPhotoSize(mImgGvAdapter_Choosed.getUrls());

		// Editable eb = content.getEditableText();
		// // 获得光标所在位置
		// int startPosition = content.getSelectionStart();
		//
		// File file = FileUtil.ChangeImage(
		// new File(photoPath.substring(0, photoPath.lastIndexOf("_"))),
		// false);
		// /*
		// * <igame></igame>为自定义HTML标签 为Html.fromHtml(String source, ImageGetter
		// * imageGetter, TagHandler tagHandler)中TagHandler对 html自定义标签中的内容进行事件监听
		// */
		// eb.insert(
		// startPosition,
		// Html.fromHtml("<br/><noteimage><img src='" + file.getPath()
		// + "'/></noteimage><br/>", imageGetter, tHandler));
	}

	/**
	 * 添加图片
	 * 
	 * @param photoPath
	 */
	private void addPhoto(List<String> photoPath) {
		showImageBox();
		mImgGvAdapter_Choosed.flush(photoPath);
		showPhotoSize(mImgGvAdapter_Choosed.getUrls());
	}

	public void showImageBox() {
		if (mImgGvAdapter_Choosed.isInit())
			mVs_ImgGv.inflate();
	}

	public void showFileBox() {
		if (mAdditiveFileLvAdapter_Choosed.isInit())
			mVs_FileLv.inflate();
	}

	/**
	 * 删除对话框
	 * 
	 * @param filePath
	 * @param position
	 */
	private void deleteDialog(String filePath, final int position) {
		int index = filePath.lastIndexOf("/") + 1;
		String filename = filePath.substring(index);
		String msg = StringUtil.merge("是否删除照片", "“", filename, "”？");
		new AlertDialog.Builder(this).setTitle("提示").setMessage(msg)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mImgGvAdapter_Choosed.remove(position);
						showPhotoSize(mImgGvAdapter_Choosed.getUrls());
					}
				}).setNegativeButton("取消", null).show();
	}

	private void showPhoto(String url) {
		Bundle bundle = new Bundle();
		String[] bean = ThumbnailImgUtil.split(url);
		bundle.putString("url", bean[0]);
		bundle.putString("id", bean[1]);
		L.d(TAG, "url:" + ThumbnailImgUtil.getData(url));
		// bundle.putString("oUrl", "");
		bundle.putInt("index", 1);
		bundle.putString("type", "type_topic");
		LogicUtil.enter(this, PhotoGalleryActivity.class, bundle,
				PhotoGalleryActivity.IDDELETE);
	}

	/**
	 * 显示原图上传大小
	 */
	public void showPhotoSize(List<String> urls) {

		if (urls.size() > 0) {
			if (checkBox.getVisibility() != View.VISIBLE) {
				checkBox.setVisibility(View.VISIBLE);
			}
		} else {
			checkBox.setVisibility(View.GONE);
		}
		if (!checkBox.isChecked()) {
			checkBox.setText("原图上传");
			return;
		}
		long size = 0L;
		for (String filePath : urls) {
			L.d(TAG, "showPhotoSize filePath=" + filePath);
			if (filePath != null) {
				File file = new File(ThumbnailImgUtil.getData(filePath));
				size += file.length();
			}
		}
		if (size > 0)
			checkBox.setText("原图上传（" + FileUtil.computeFileSize(size) + "）");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.note_create_xiangji:
			choosePicture();
			break;
		case R.id.note_create_wenb:
			Intent intent = new Intent(this, FileBrowserActivity.class);
			intent.putExtra("isCreateTopic", true);
			startActivityForResult(intent, OTHAR);
			break;
		case R.id.note_create_huat:
			BaseData.hideKeyBoard(this);
			ll_add.setVisibility(View.GONE);
			ll_luyin.setVisibility(View.VISIBLE);
			iv_luyinj.setImageResource(R.drawable.note_luyinz);
			tv_luyin_time.setText("00:00");
			list = new ArrayList<String>();
			isPause = false;
			inThePause = false;
			isStart = false;
			break;
		case R.id.note_create_lux:
			createVideo();
			break;
		case R.id.note_create_luyinj:
			if (!isStart) {
				iv_luyinj.setImageResource(R.drawable.note_luyinj);
				second = 0;
				minute = 0;
				list.clear();
				isPause = false;
				isStart = true;
				start();
			} else {
				isPause = true;
				// 已经暂停过了，再次点击按钮 开始录音，录音状态在录音中
				if (inThePause) {
					iv_luyinj.setImageResource(R.drawable.note_luyinj);
					start();
					inThePause = false;
				} else {// 正在录音，点击暂停,现在录音状态为暂停
						// 当前正在录音的文件名，全程
					iv_luyinj.setImageResource(R.drawable.note_luyinz);
					list.add(myRecAudioFile.getPath());
					inThePause = true;
					recodeStop();
					// 计时停止
					timer.cancel();
				}
			}
			break;
		case R.id.note_create_wanc:
			// BaseData.showKeyBoard(this, content);
			stopRecord();
			break;
		default:
			break;
		}
	}

	private void stopRecord() {
		try {
			ll_add.setVisibility(View.VISIBLE);
			ll_luyin.setVisibility(View.GONE);
			if (timer != null)
				timer.cancel();
			// 这里写暂停处理的 文件！加上list里面 语音合成起来
			if (isPause) {
				// 在暂停状态按下结束键,处理list就可以了
				if (inThePause) {
					getInputCollection(list, false);
				} else {// 在正在录音时，处理list里面的和正在录音的语音
					list.add(myRecAudioFile.getPath());
					recodeStop();
					getInputCollection(list, true);
				}
				// 还原标志位
				isPause = false;
				inThePause = false;
			} else {// 若录音没有经过任何暂停
				if (myRecAudioFile != null && mMediaRecorder01 != null) {
					addFile(myRecAudioFile.getPath(), true);
					// 停止录音
					mMediaRecorder01.stop();
					mMediaRecorder01.release();
					mMediaRecorder01 = null;
				}
			}
			// 停止录音了
			isStopRecord = true;
			isStart = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 记录需要合成的几段amr语音文件 **/
	private ArrayList<String> list;
	private int second = 0;
	private int minute = 0;
	/** 计时器 **/
	Timer timer;
	/** 是否暂停标志位 **/
	private boolean isPause = true;
	private boolean isStart = false;
	/** 在暂停状态中 **/
	private boolean inThePause;
	/** 是否停止录音 **/
	private boolean isStopRecord;
	private final String SUFFIX = ".amr";
	private File myRecAudioFile;
	private MediaRecorder mMediaRecorder01;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String str = "";
			if (minute < 10)
				str += "0";
			str += minute + ":";
			if (second < 10)
				str += "0";
			str += second;
			tv_luyin_time.setText(str);
		}

	};

	@SuppressWarnings("deprecation")
	private void start() {

		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				second++;
				if (second >= 60) {
					second = 0;
					minute++;
				}
				handler.sendEmptyMessage(1);
			}
		};
		timer = new Timer();
		timer.schedule(timerTask, 1000, 1000);

		try {
			String mMinute1 = System.currentTimeMillis() + "";
			// 创建音频文件
			// myRecAudioFile = File.createTempFile(mMinute1, ".amr",
			// myRecAudioDir);

			myRecAudioFile = new File(ConstantUtil.TEMP_PATH, mMinute1 + SUFFIX);
			mMediaRecorder01 = new MediaRecorder();
			// 设置录音为麦克风
			mMediaRecorder01.setAudioSource(MediaRecorder.AudioSource.MIC);
			mMediaRecorder01
					.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			mMediaRecorder01.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

			// 录音文件保存这里
			mMediaRecorder01.setOutputFile(myRecAudioFile.getAbsolutePath());
			mMediaRecorder01.prepare();
			mMediaRecorder01.start();

			// mMediaRecorder01.setOnInfoListener(new OnInfoListener() {
			//
			// @Override
			// public void onInfo(MediaRecorder mr, int what, int extra) {
			// int a = mr.getMaxAmplitude();
			// Toast.makeText(NoteCreateActivity.this, a, 500).show();
			// }
			// });
			isStopRecord = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void recodeStop() {
		try {
			if (mMediaRecorder01 != null && !isStopRecord) {
				// 停止录音
				mMediaRecorder01.stop();
				mMediaRecorder01.release();
				mMediaRecorder01 = null;
			}
			timer.cancel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param isAddLastRecord
	 *            是否需要添加list之外的最新录音，一起合并
	 * @return 将合并的流用字符保存
	 */
	@SuppressWarnings("rawtypes")
	public void getInputCollection(List list, boolean isAddLastRecord) {
		String mMinute1 = System.currentTimeMillis() + "";
		// 创建音频文件,合并的文件放这里
		File aFile = new File(ConstantUtil.TEMP_PATH, mMinute1 + SUFFIX);
		FileOutputStream fileOutputStream = null;

		if (!aFile.exists()) {
			try {
				aFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			fileOutputStream = new FileOutputStream(aFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// list里面为暂停录音 所产生的 几段录音文件的名字，中间几段文件的减去前面的6个字节头文件

		for (int i = 0; i < list.size(); i++) {
			File file = new File((String) list.get(i));
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				byte[] myByte = new byte[fileInputStream.available()];
				// 文件长度
				int length = myByte.length;

				// 头文件
				if (i == 0) {
					while (fileInputStream.read(myByte) != -1) {
						fileOutputStream.write(myByte, 0, length);
					}
				} else {// 之后的文件，去掉头文件就可以了
					while (fileInputStream.read(myByte) != -1) {
						fileOutputStream.write(myByte, 6, length - 6);
					}
				}

				fileOutputStream.flush();
				fileInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		// 结束后关闭流
		try {
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 合成一个文件后，删除之前暂停录音所保存的零碎合成文件
		deleteListRecord(isAddLastRecord);
		addFile(aFile.getPath(), true);
	}

	private void deleteListRecord(boolean isAddLastRecord) {
		for (int i = 0; i < list.size(); i++) {
			File file = new File((String) list.get(i));
			if (file.exists()) {
				file.delete();
			}
		}
		// 正在暂停后，继续录音的这一段音频文件
		if (isAddLastRecord) {
			myRecAudioFile.delete();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.d(TAG, "onActivityResult requestCode=" + requestCode + ",resultCode="
				+ resultCode);
		if (resultCode == RESULT_OK) {
			Uri uri = null;
			String filePath = null;
			Bundle bundle = null;
			switch (requestCode) {
			case IMAGE:
				try {
					if (data != null) {
						uri = data.getData();
						L.i(TAG, "onActivityResult uri0=" + uri);
						if (uri != null) {
							filePath = queryPhoto(uri);
						} else {
							File saveFile = new File(ConstantUtil.CAMERA_PATH);
							File picture = new File(saveFile, pictureName);
							uri = Uri.fromFile(picture);
							L.i(TAG, "onActivityResult uri1=" + uri);
							filePath = uri.getPath();
							pictureName = null;
						}
					} else {
						File saveFile = new File(ConstantUtil.CAMERA_PATH);
						File picture = new File(saveFile, pictureName);
						uri = Uri.fromFile(picture);
						L.i(TAG, "onActivityResult uri2=" + uri);
						filePath = uri.getPath();
						pictureName = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
						uri));
				L.i(TAG, "onActivityResult IMAGE filepath=" + filePath);
				changeImg(filePath, false);
				return;
			case ImageBucketActivity.CHOOSEIMG:
				if (null != data)
					bundle = data.getExtras();
				if (null == bundle)
					break;
				addPhoto(bundle.getStringArrayList("chooseds"));
				return;
			case VIDEO:
			case AUDIO:
				if (data != null) {
					uri = data.getData();
					L.i(TAG, "onActivityResult uri=" + uri);
					if (uri != null) {
						Cursor cursor = getContentResolver().query(
								uri,
								new String[] { "_data", "_display_name",
										"_size", "mime_type" }, null, null,
								null);
						if (cursor != null) {
							cursor.moveToFirst();
							filePath = cursor.getString(0);
							cursor.close();
						} else {
							filePath = uri.getPath();
						}
					} else {
						File saveFile = new File(ConstantUtil.CAMERA_PATH);
						File picture = new File(saveFile, pictureName);
						uri = Uri.fromFile(picture);
						filePath = uri.getPath();
					}
				} else {
					L.d(TAG, "onActivityResult data=null");
					File saveFile = new File(ConstantUtil.CAMERA_PATH);
					File picture = new File(saveFile, pictureName);
					uri = Uri.fromFile(picture);
					filePath = uri.getPath();
				}
				break;
			case OTHAR:
				filePath = data.getStringExtra("filePath");
				break;
			}
			L.i(TAG, "onActivityResult filePath=" + filePath);
			if (filePath != null && !"".equals(filePath)) {
				addFile(filePath, false);
			}
		}
	}

	private void addFile(String filePath, boolean isMedia) {
		File file = new File(filePath);
		if (file.exists()) {
			// TODO
			// Editable eb = content.getEditableText();
			// // 获得光标所在位置
			// int startPosition = content.getSelectionStart();
			// /*
			// * <igame></igame>为自定义HTML标签 为Html.fromHtml(String source,
			// * ImageGetter imageGetter, TagHandler tagHandler)中TagHandler对
			// * html自定义标签中的内容进行事件监听
			// */
			// eb.insert(startPosition, Html.fromHtml(
			// FileUtil.getFileHtmlPhoto(file), imageGetter, tHandler));

			AdditiveFileBean afile = new AdditiveFileBean();
			afile.setUri(filePath);
			int length = filePath.lastIndexOf("/");
			afile.setName(filePath.substring(length + 1));
			File temp = new File(filePath);
			afile.setSize(temp.length());
			afile.setMime_type(FileUtil.getMIMEType(temp));
			if (isMedia && (second != 0 || minute != 0)) {
				afile.setTime((minute * 60 + second) * 1000);
			}
			filePath = null;
			temp = null;
			showFileBox();
			if (mAdditiveFileLvAdapter_Choosed.contains(afile.getUri()) != -1) {
				T.show(App.app, StringUtil.merge(afile.getName(), "已存在"));
				file = null;
			} else {
				mAdditiveFileLvAdapter_Choosed.addAtFirst(afile);
			}
		} else {
			showTip("找不到文件，请换其他方式");
		}
	}

	private final int IMAGE = 0, VIDEO = 1, AUDIO = 2, OTHAR = 3;
	private String pictureName;

	/**
	 * 查找本地图片
	 * 
	 * @param uri
	 * @return
	 */
	private String queryPhoto(Uri uri) {
		String filepath = "";
		Cursor cursor = getContentResolver()
				.query(uri,
						new String[] { "_data", "_display_name", "_size",
								"mime_type" }, null, null, null);
		// Thumbnails.EXTERNAL_CONTENT_URI
		if (cursor != null) {
			cursor.moveToFirst();
			filepath = cursor.getString(0); // 图片文件路径
			cursor.close();
		} else {
			filepath = uri.getPath();
		}
		return filepath;
	}

	/**
	 * 选择图片
	 */
	private void choosePicture() {
		if (!ConstantUtil.IsCanUseSdCard()) {
			T.show(getApplicationContext(), "请检查SD卡");
			return;
		}
		@SuppressWarnings({ "rawtypes", "unchecked" })
		AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(
				"上传图片").setAdapter(
				new ArrayAdapter(this, R.layout.choice_item, new String[] {
						"本地上传", "拍照上传" }),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Bundle bundle = new Bundle();
							if (null == mImgGvAdapter_Choosed)
								bundle.putStringArrayList("chooseds",
										new ArrayList<String>());
							else {
								bundle.putStringArrayList(
										"chooseds",
										(ArrayList<String>) mImgGvAdapter_Choosed
												.getUrls());
							}
							LogicUtil.enter(NoteCreateActivity.this,
									ImageBucketActivity.class, bundle,
									ImageBucketActivity.CHOOSEIMG);
							break;
						case 1:
							if (mImgGvAdapter_Choosed.getUrls().size() < ImageChoosedGridViewAdapter
									.getCanChoosedNum()) {
								Intent intent1 = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								pictureName = System.currentTimeMillis()
										+ ".jpg";
								File saveFile = new File(
										ConstantUtil.CAMERA_PATH);
								if (!saveFile.exists()) {
									saveFile.mkdirs();
								}

								intent1.putExtra(
										MediaStore.Images.Media.ORIENTATION, 0);
								intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri
										.fromFile(new File(saveFile,
												pictureName)));
								startActivityForResult(intent1, IMAGE);
							} else {
								T.show(getApplicationContext(), "最多添加9张图");
								return;
							}
							break;
						case 2:
							dialog.dismiss();
							break;
						}
					}
				});
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	/**
	 * 创建视频
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createVideo() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(
				"上传视频").setAdapter(
				new ArrayAdapter(this, R.layout.choice_item, new String[] {
						"本地视频", "新视频", }),// "取消"
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intent = new Intent(
									Intent.ACTION_GET_CONTENT);
							intent.setType("video/*");
							startActivityForResult(
									Intent.createChooser(intent, "选择视频"), VIDEO);
							break;
						case 1:
							Intent vIntent = new Intent(
									MediaStore.ACTION_VIDEO_CAPTURE);
							startActivityForResult(vIntent, VIDEO);
							break;
						}
					}
				});
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	private void checkInfo() {
		Bundle b = getIntent().getExtras();
		if (null == b) {
			ab.setTitle("写笔记");
		} else {
			noteBookId = b.getInt("id", 0);
			nb = (NoteDraftBean) b.getSerializable("NoteDraftBean");
			if (nb != null) {
				if (noteBookId == 0)
					noteBookId = nb.getNoteBookId();
				// NoteCreateService.getService(this).delete(nb.getTime(), 0);
				isEditLac = true;
				position = b.getInt("position", -1);
				ab.setTitle("写笔记");
				titleStr = nb.getTitle();
				if (titleStr != null && !"".equals(titleStr)) {
					title.setText(titleStr);
					title.setSelection(titleStr.length());
				}
				contentStr = nb.getContent();
				if (contentStr != null && !"".equals(contentStr))
					// content.setText(contentStr);
					// TODO
					content.append(Html.fromHtml(contentStr, imageGetter, null));
				try {
					String filePaths = nb.getFilePaths();
					JSONArray array = new JSONArray(filePaths);
					List<String> photos = new ArrayList<String>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject json = array.getJSONObject(i);
						if (json.getInt("type") == 1) {
							addFile(json.getString("path"), false);
						} else {
							// TODO
							// changeImg(json.getString("path"), true);
							photos.add(json.getString("path"));
						}
					}
					addPhoto(photos);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// ll_add.setVisibility(View.GONE);
				ab.setTitle("编辑笔记");
				nibean = (NoteInfoBean) b.getSerializable("diaryInfoBean");
				if (nibean == null)
					return;
				isEditNote = nibean.getNoteId() != 0;

				titleStr = nibean.getNoteTitle();
				if (titleStr != null && !"".equals(titleStr)) {
					title.setText(titleStr);
					title.setSelection(titleStr.length());
				}
				contentStr = nibean.getNoteContent();
				L.d(TAG, "checkInfo contentStr=" + contentStr);
				if (contentStr != null && !"".equals(contentStr))
					content.append(Html.fromHtml(contentStr, null, null));
				// TODO content.setText(Html.fromHtml(contentStr, imageGetter,
				// tHandler));
			}
		}
	}

	TagHandler tHandler = new TagHandler() {

		@Override
		public void handleTag(boolean opening, String tag, Editable output,
				XMLReader xmlReader) {
			L.d(TAG, "tHandler opening=" + opening + ",tag=" + tag);
			if (tag.toLowerCase().equals("noteimage")) {
				showTip("打开图片");
			} else if (tag.toLowerCase().equals("notefile")) {
				showTip("打开文件");
			}
		}
	};

	// 解析图片
	final Html.ImageGetter imageGetter = new Html.ImageGetter() {
		@SuppressWarnings("deprecation")
		public Drawable getDrawable(String source) {
			L.i(TAG, "imageGetter source=" + source);
			// 在此必须异步加载图片
			Drawable d = null;
			try {
				if (source.startsWith("http")) {
					InputStream is = new DefaultHttpClient()
							.execute(new HttpGet(source)).getEntity()
							.getContent();
					Bitmap bm = BitmapFactory.decodeStream(is);
					d = new BitmapDrawable(bm);
					d.setBounds(0, 0, bm.getWidth(), bm.getHeight());
				} else if (CharUtil.isNumber(source)) {
					d = getResources().getDrawable(Integer.valueOf(source));
					d.setBounds(0, 0, d.getIntrinsicWidth() / 2,
							d.getIntrinsicHeight() / 2);
				} else {
					d = Drawable.createFromPath(source);
					d.setBounds(0, 0, d.getIntrinsicWidth(),
							d.getIntrinsicHeight());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
			return d;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_send).setVisible(true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			BaseData.hideKeyBoard(this);
			if (isStart || inThePause) {
				showTip("请先完成录音功能");
				return false;
			} else {
				showSaveDialog();
			}
			return true;
		case R.id.action_send:
			BaseData.hideKeyBoard(this);
			getEdit();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void getEdit() {
		if (isSaving) {
			showTip("正在保存");
			return;
		}

		nTitleStr = title.getText().toString().trim();
		// nContentStr = content.getText().toString().trim();
		nContentStr = HTMLDecoder.decode(Html.toHtml(content.getText()));

		if ("".equals(nTitleStr) && "".equals(nContentStr)) {
			showTip("标题和内容不能全部为空");
			return;
		}

		if (!nTitleStr.equals(titleStr) || !nContentStr.equals(contentStr)
				|| !isEditNote) {
			nContentStr = StringUtil.trimInnerSpaceStr(nContentStr);
			save(++num);
		}
	}

	private int num = 0;

	private void save(final int cur) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		TextView dTitle = new TextView(this);
		dTitle.setGravity(Gravity.CENTER);
		dTitle.setPadding(0, 15, 0, 0);
		dTitle.setTextSize(22);
		dTitle.setText("发送中...");
		builder.setCustomTitle(dTitle);
		dialogContent = new DialogView(this);
		String allInfo = null;
		int numFile = mAdditiveFileLvAdapter_Choosed.getCount();
		int numPhoto = mImgGvAdapter_Choosed.getUrls().size();
		if (numFile > 0 && numPhoto > 0) {
			allInfo = "共 " + numPhoto + " 张图片，" + numFile + " 个文件";
		} else if (numFile > 0) {
			allInfo = "共 " + numFile + " 个文件";
		} else if (numPhoto > 0) {
			allInfo = "共 " + numPhoto + " 张图片";
		} else {
			allInfo = null;
		}
		dialogContent.setAllFileInfo(allInfo);
		builder.setView(dialogContent);
		builder.setCancelable(false);
		builder.setNegativeButton("取消上传",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						pDialog.cancel();
						allowedPublish = false;
					}
				});
		// builder.setOnCancelListener(new OnCancelListener() {
		//
		// @Override
		// public void onCancel(DialogInterface dialog) {
		// if (pDialog.getButton(Dialog.BUTTON_NEGATIVE).getTag() != null) {
		// finish();
		// }
		// }
		// });
		pDialog = builder.show();
		new Thread() {
			public void run() {
				// saveLocal(false);
				allowedPublish = true;
				try {
					if (nContentStr == null || "".equals(nContentStr)) {
						nContentStr = nTitleStr;
					}
					if (nTitleStr == null || "".equals(nTitleStr)) {
						nTitleStr = content.getText().toString().trim();
						while (nTitleStr.startsWith(" ")
								|| nTitleStr.startsWith("\n")) {
							nTitleStr = nTitleStr.substring(1,
									nTitleStr.length()).trim();
						}
						if (nTitleStr.contains("\n"))
							nTitleStr = nTitleStr.substring(0,
									nTitleStr.indexOf("\n"));
					}

					// 发送失败后，如果重选 去除已上传成功的
					if (!photoTempsList.isEmpty()) {
						for (Entry<String, String> entry : photoTempsList
								.entrySet()) {
							if (!mImgGvAdapter_Choosed.getUrls().contains(
									entry.getKey())) {
								photoTempsList.remove(entry.getKey());
							}
						}
					}

					// 发送失败后，如果重选 去除已上传成功的
					if (!fileTempsList.isEmpty()) {
						for (Entry<String, String> entry : fileTempsList
								.entrySet()) {
							if (mAdditiveFileLvAdapter_Choosed.contains(entry
									.getKey()) == -1) {
								fileTempsList.remove(entry.getKey());
							}
						}
					}

					int ffsize = fileTempsList.size();
					int ppsize = photoTempsList.size();
					String oriToast = null;
					if (ppsize > 0 && ffsize > 0) {
						oriToast = "已上传 " + ppsize + " 张图片，" + ffsize + " 个文件";
					} else if (ffsize > 0) {
						oriToast = "已上传 " + ffsize + " 个文件";
					} else if (ppsize > 0) {
						oriToast = "已上传 " + ppsize + " 张图片";
					}
					if (null != oriToast) {
						HandlerUtil.sendMsgToHandler(sendHandler, 4, oriToast,
								0);
					}

					mAdditiveFileLvAdapter_Choosed.classify();
					for (String path : mImgGvAdapter_Choosed.getUrls()) {
						if (!allowedPublish || num != cur) {
							break;
						}
						L.d(TAG, "imgPath" + path);
						if (null != path && !"".equals(path)
						// 发送失败后，如果重传 去除已上传成功的
								&& !photoTempsList.containsKey(path)) {
							String toast = null;
							if (null != pDialog && pDialog.isShowing()) {
								int fsize = fileTempsList.size();
								int psize = photoTempsList.size();
								if (fsize == 0) {
									toast = StringUtil.merge("已上传 ",
											(1 + psize), " 张图片");
								} else {
									toast = StringUtil.merge("已上传 ",
											(1 + psize), " 张图片，", (fsize),
											" 个文件");
								}

								HandlerUtil.sendMsgToHandler(sendHandler, 3,
										StringUtil.merge("正在上传第 ", (1 + psize),
												" 张图片..."), 0);
								HandlerUtil.sendMsgToHandler(sendHandler,
										HandlerUtil.getMessage(2, path, 0, -1));
							}
							startThread(path, true, toast, cur);
						}
					}
					for (Object file : mAdditiveFileLvAdapter_Choosed
							.getAdditiveFile(false)) {
						String path = new TransitionBean(file).getPath();
						if (!allowedPublish || num != cur) {
							break;
						}
						L.d(TAG, "imgPath" + path);
						if (null != path && !"".equals(path)
						// 发送失败后，如果重传 去除已上传成功的
								&& !fileTempsList.containsKey(path)) {
							String toast = null;
							if (null != pDialog && pDialog.isShowing()) {
								int fsize = fileTempsList.size();
								int psize = photoTempsList.size();
								if (psize == 0) {
									toast = StringUtil.merge("已上传 ",
											(1 + psize), " 个文件");
								} else {
									toast = StringUtil.merge("已上传 ",
											(1 + psize), " 张图片，", (fsize),
											" 个文件");
								}
								HandlerUtil.sendMsgToHandler(sendHandler, 3,
										StringUtil.merge("正在上传第 ", (1 + fsize),
												" 个文件..."), 0);
								HandlerUtil.sendMsgToHandler(sendHandler,
										HandlerUtil.getMessage(2,
												new File(path), 0, 1));
							}
							startThread(path, false, toast, cur);
						}
					}
					HandlerUtil.sendMsgToHandler(sendHandler, 5, "正在发布笔记...",
							View.GONE);

					int i = photoTempsList.size();
					L.d(TAG, "photoTempsList.size" + photoTempsList.size());

					String firstPhotoPath = null;
					for (Entry<String, String> entry : photoTempsList
							.entrySet()) {
						i--;
						try {
							JSONObject json = new JSONObject(entry.getValue());
							firstPhotoPath = json.getString("adjunctPath");
							nContentStr += String
									.format("<br /><img src=\"%s\" alt=\"\" /><br />",
											json.getString("adjunctUrl")
													+ ThumbnailImageUrl
															.getThumbnailNoteUrl(firstPhotoPath));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					i = fileTempsList.size();
					int fsize = i;
					String[] fileTemps = new String[i];
					for (Entry<String, String> entry : fileTempsList.entrySet()) {
						fileTemps[i - 1] = entry.getValue();
						i--;
						try {
							JSONObject json = new JSONObject(entry.getValue());
							String formatUrl = json.getString("formatUrl");
							String adjunctPath = json.getString("adjunctPath");
							String formatPath = json.getString("formatPath");
							String adjunctSize = json.getString("adjunctSize");
							String adjunctName = json.getString("adjunctName");
							String adjunctUrl = json.getString("adjunctUrl");
							nContentStr += "<br /><div class=\"mojiImgBks\"><a class=\"allShareYun\" href=\"javascript:;\" title=\"转存为云文件\" "
									+ "style=\"display:none;\"></a><a href=\"https://www.yuuquu.com/file/downloadDiaryAttachment.do?attachmentPath="
									+ adjunctPath
									+ "&attachmentName="
									+ adjunctName
									+ "\">"
									+ "<img class=\"note-accessory\" rel=\""
									+ adjunctName
									+ ","
									+ adjunctSize
									+ ","
									+ adjunctUrl
									+ ","
									+ adjunctPath
									+ ","
									+ adjunctName.substring(adjunctName
											.lastIndexOf(".") + 1)
									+ "\" style=\"padding-top:3px;border:1px solid #ccc;display:block;\" src=\""
									+ formatUrl
									+ formatPath
									+ "\" /></a></div><br />";
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					MCResult mc = null;
					try {
						// nContentStr = nContentStr.replaceAll("\n", "<br />");
						if (!isEditNote) {
							mc = APINoteRequestServers.createCloudNote(
									NoteCreateActivity.this, (noteBookId != 0)
											+ "", noteBookId + "", nTitleStr,
									nContentStr, fileTemps, "2", null,
									firstPhotoPath);
						} else {
							noteBookId = nibean.getNotebookId();
							if (firstPhotoPath == null)
								firstPhotoPath = nibean.getFirstPhotoPath();

							ArrayList<String> oldTemps = new ArrayList<String>();
							List<AttachmentBean> attachmentList = nibean
									.getAttachmentList();
							if (attachmentList != null)
								for (AttachmentBean ab : attachmentList) {
									if (nContentStr.contains(ab
											.getAdjunctPath())) {
										oldTemps.add(ab.toJsonString());
									}
								}
							int j = oldTemps.size();
							String[] oldTs = new String[j];
							for (String str : oldTemps) {
								oldTs[j - 1] = str;
								j--;
							}

							fsize = oldTs.length + fileTemps.length;
							String[] attachments = new String[fsize];
							System.arraycopy(oldTs, 0, attachments, 0,
									oldTs.length);
							System.arraycopy(fileTemps, 0, attachments,
									oldTs.length, fileTemps.length);

							mc = APINoteRequestServers.editCloudNote(
									NoteCreateActivity.this, nibean.getNoteId()
											+ "", (noteBookId != 0) + "",
									noteBookId + "", nTitleStr, nContentStr,
									attachments, "2", null, firstPhotoPath);
							// mc = APINoteRequestServers.editDiary(
							// NoteCreateActivity.this, dirayId,
							// nTitleStr, nContentStr);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (mc != null) {
						int resultCode = mc.getResultCode();
						L.i(TAG, "sendGroupTopic resultCode=" + resultCode
								+ ",firstPhotoPath=" + firstPhotoPath
								+ ",fsize=" + fsize);
						if (!allowedPublish || num != cur) {
							return;
						}
						if (resultCode == 1) {
							sendHandler.sendEmptyMessage(1);
							if (isEditLac)
								NoteCreateService.getService(
										NoteCreateActivity.this).delete(
										nb.getTime(), 0);

							// TODO
							if (nibean == null) {
								nibean = new NoteInfoBean();
								JSONObject json = new JSONObject(mc.getResult()
										.toString());
								nibean.setNoteId(json.getInt("CLOUDNOTEID"));
							}

							nibean.setNoteTitle(nTitleStr);
							nibean.setNoteContent(nContentStr);
							if (nibean.getFirstPhotoUrl() == null
									|| nibean.getFirstPhotoUrl().equals("")) {
								nibean.setFirstPhotoPath(firstPhotoPath);
							}
							if (fsize != 0 && nibean.getAdjunctNum() == 0)
								nibean.setAdjunctNum(fsize);
							// if (isEditNote) {
							// NoteCreateActivity.this.setResult(RESULT_OK,
							// new Intent());
							// } else {
							// NoteCreateActivity.this.setResult(RESULT_OK,
							// new Intent());
							// }
							Intent ii = new Intent();
							ii.putExtra("NoteInfoBean", nibean);
							ii.putExtra("position", position);
							NoteCreateActivity.this.setResult(RESULT_OK, ii);
							finish();
						} else {
							sendHandler.sendEmptyMessage(0);
							saveLocal(false);
						}
					} else {
						sendHandler.sendEmptyMessage(0);
						saveLocal(false);
					}
				} catch (SocketTimeoutException e) {
					sendHandler.sendEmptyMessage(10);
					saveLocal(false);
					e.printStackTrace();
				} catch (Exception e) {
					sendHandler.sendEmptyMessage(0);
					saveLocal(false);
					e.printStackTrace();
				}
				NoteCreateActivity.this.finish();
			}

		}.start();
	}

	private void saveLocal(boolean isBack) {
		if (!isEditNote) {
			NoteDraftBean b = new NoteDraftBean();
			b.setTime(System.currentTimeMillis());
			b.setTitle(nTitleStr);
			b.setContent(nContentStr);
			b.setNoteBookId(noteBookId);

			try {
				JSONArray array = new JSONArray();
				mAdditiveFileLvAdapter_Choosed.classify();
				for (String path : mImgGvAdapter_Choosed.getUrls()) {
					JSONObject json = new JSONObject();
					json.put("path", path);
					json.put("type", 0);// 0:图片，1:文件
					array.put(json);
				}

				for (Object file : mAdditiveFileLvAdapter_Choosed
						.getAdditiveFile(false)) {
					String path = new TransitionBean(file).getPath();
					JSONObject json = new JSONObject();
					json.put("path", path);
					json.put("type", 1);// 0:图片，1:文件
					array.put(json);
				}
				b.setFilePaths(array.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (isBack) {
				sendHandler.sendEmptyMessage(11);
			} else {
				sendHandler.sendEmptyMessage(12);
			}

			if (isEditLac) {
				NoteCreateService.getService(this).updateNote(nb.getTime(), b);
			} else {
				NoteCreateService.getService(this).save(b);
			}

			Intent i = new Intent();
			i.putExtra("NoteDraftBean", b);
			i.putExtra("position", position);
			NoteCreateActivity.this.setResult(RESULT_OK, i);
			finish();
		}
	}

	private void startThread(String filepath, boolean isPhoto,
			String finishToast, int cur) {
		String url = URLProperties.CLOUD_NOTE_JSON;
		String method = "";
		File file = null;
		if (isPhoto) {
			file = new File(ThumbnailImgUtil.getData(filepath));
			method = "uploadAttachmentImge";
			file = FileUtil.ChangeImage(file, checkBox.isChecked());
		} else {
			file = new File(filepath);
			method = "uploadAttachmentFile";
		}
		L.d(TAG, "startThread filepath=" + file.getAbsolutePath());
		String params = new UploadFileParams(App.app, method, file.getName(),
				null).getParams();
		L.d(TAG, "startThread url=" + url + "?" + params);
		try {
			String result = httpUpload(url + "?" + params, file, isPhoto,
					finishToast, cur);
			L.d(TAG, "startThread result=" + result);
			if (!allowedPublish || cur != num) {
				return;
			}
			if (result != null) {
				final JSONObject object = new JSONObject(result);
				if (object.getInt("resultCode") == 1) {
					String temps = object.getString("result");
					if (isPhoto) {
						photoTempsList.put(filepath, temps);
					} else {
						fileTempsList.put(file.getPath(), temps);
					}
				} else {
					onUploadFailed(file);
				}
			} else {
				onUploadFailed(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			onUploadFailed(file);
		}
	}

	private void onUploadFailed(File file) {
		allowedPublish = false;
		HandlerUtil.sendMsgToHandler(sendHandler, 9, file.getName()
				+ "上传失败，请重新上传！", 0);
	}

	/**
	 * HttpURLConnection POST上传文件
	 * 
	 * @param uploadUrl
	 * @param file
	 * @param isPhoto
	 * @param finishToast
	 * @param cur
	 * @throws Exception
	 */
	private String httpUpload(String uploadUrl, File file, boolean isPhoto,
			String finishToast, int cur) throws Exception {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		DataOutputStream dos = null;
		FileInputStream fis = null;
		String result = null;

		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = HttpRequestServers
					.getHttpURLConnection(url);

			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(true);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			String reqHeader = twoHyphens
					+ boundary
					+ end
					+ "Content-Disposition: form-data; name=\"upload\"; filename=\""
					+ file.getName() + "\"" + end
					+ "Content-Type: application/octet-stream" + end + end;
			String reqEnder = end + twoHyphens + boundary + twoHyphens + end;

			long totalLength = file.length();
			httpURLConnection.setFixedLengthStreamingMode(reqHeader.length()
					+ (int) (totalLength) + reqEnder.length());

			dos = new DataOutputStream(httpURLConnection.getOutputStream());
			dos.writeBytes(reqHeader);
			L.d(TAG, "path=" + file.getAbsolutePath());
			fis = new FileInputStream(file);
			L.d(TAG, "httpUpload totalLength=" + totalLength);
			long uploadSize = 0;
			int progress = 0;
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fis.read(buffer)) != -1) {
				if (!allowedPublish || num != cur) {
					return null;
				}
				dos.write(buffer, 0, count);
				uploadSize += count;
				progress = (int) (uploadSize * 100 / totalLength);
				if (progress > 0) {
					L.d(TAG, "httpUpload uploadSize=" + uploadSize
							+ ",progress=" + progress);
				}

				HandlerUtil.sendMsgToHandler(sendHandler, 2, null, progress);
				if (progress >= 100) {
					HandlerUtil.sendMsgToHandler(sendHandler,
							HandlerUtil.getMessage(4, finishToast, 0, 0));
				}
			}

			dos.writeBytes(reqEnder);
			dos.flush();

			L.d(TAG, "httpUpload dos.size=" + dos.size());
			result = StreamUtil.readData(httpURLConnection.getInputStream());
			L.d(TAG, "httpUpload result=" + result);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			if (dos != null)
				try {
					dos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return result;
	}

	/**
	 * 加载完成提醒
	 * 
	 * @param msg
	 */
	private void updateUI(String msg) {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.cancel();
		}
		T.show(this, msg);
	}

	@SuppressLint("HandlerLeak")
	private Handler sendHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				updateUI(T.ErrStr);
				break;
			case 1:
				updateUI("已发布！");
				break;
			case 2:
				if (null != dialogContent) {
					dialogContent.updatePBar(msg.arg1);
					if (null != msg.obj && 0 == msg.arg1) {
						if (1 == msg.arg2) { // 文件
							dialogContent.setFileIcon((File) msg.obj);
						} else if (-1 == msg.arg2) {
							dialogContent.setIcon((String) msg.obj);
						}
					}
				}
				break;

			case 3:
				if (null != dialogContent) {
					dialogContent.startNewFile((String) msg.obj);
				}
				break;
			case 4:
				if (null != dialogContent) {
					dialogContent.finishFile((String) msg.obj);
					dialogContent.setPublishProgress(msg.arg1);
				}
				break;
			case 5:
				if (null != dialogContent) {
					dialogContent.finishFile((String) msg.obj);
					dialogContent.setPublishProgress(msg.arg1);
					dialogContent.showDialogBar();
				}

				if (msg.arg1 == View.GONE) {
					Button b = pDialog.getButton(Dialog.BUTTON_NEGATIVE);
					b.setTag(new Object());
					b.setText("后台发送");
					b.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							NoteCreateActivity.this.finish();
						}
					});
				}
				break;
			case 9:
				showTip((String) msg.obj);
				if (pDialog != null && pDialog.isShowing()) {
					pDialog.cancel();
				}
				break;
			case 10:
				updateUI(T.TimeOutStr);
				break;
			case 11:
				showTip("本地已保存");
				break;
			case 12:
				showTip("发送失败，本地已保存");
				break;
			default:
				break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			spdDialog.cancelProgressDialog(null);
			if (isStart || inThePause) {
				showTip("请先完成录音功能");
				return false;
			} else {
				showSaveDialog();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showSaveDialog() {
		nTitleStr = title.getText().toString().trim();
		nContentStr = HTMLDecoder.decode(Html.toHtml(content.getText()));
		// nContentStr = content.getText().toString().trim();

		@SuppressWarnings("rawtypes")
		List ps = null, fs = null;
		try {
			mAdditiveFileLvAdapter_Choosed.classify();
			ps = mImgGvAdapter_Choosed.getUrls();
			fs = mAdditiveFileLvAdapter_Choosed.getAdditiveFile(false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ("".equals(nTitleStr) && "".equals(nContentStr)) {
			if ((ps != null && ps.size() > 0)) {
				nTitleStr = "图片笔记";
			} else if ((fs != null && fs.size() > 0)) {
				nTitleStr = "文件笔记";
			} else {
				finish();
				return;
			}
		}

		if (!isEditNote) {
			saveLocal(true);
			finish();
			return;
		}

		new AlertDialog.Builder(this)
				.setTitle("您的笔记有改动，需要重新保存吗？")
				.setPositiveButton("不需要",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						})
				.setNegativeButton("保存", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						getEdit();
					}
				}).setCancelable(false).show();
	}
}
