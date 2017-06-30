package com.datacomo.mc.spider.android.adapter;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.datacomo.mc.spider.android.CloudFileActivity;
import com.datacomo.mc.spider.android.FileDetailActivity;
import com.datacomo.mc.spider.android.FriendsChooserActivity;
import com.datacomo.mc.spider.android.GroupsChooserActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.dialog.SpinnerProgressDialog;
import com.datacomo.mc.spider.android.net.APIFileRequestServers;
import com.datacomo.mc.spider.android.net.been.FileInfoBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.service.DownLoadFileThread;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.MenuItem;
import com.datacomo.mc.spider.android.view.PopupWindowUtil;

public class CloudFileAdapter extends BaseAdapter {
	Context mContext;
	protected ArrayList<FileInfoBean> beans;
	boolean isShare;
	private DeleteFileStateTask deletefileStateTask;

	private SpinnerProgressDialog spdDialog;
	private PopupWindow ppw;

	public CloudFileAdapter(Context context, ArrayList<FileInfoBean> text) {
		mContext = context;
		this.beans = text;
		spdDialog = new SpinnerProgressDialog(context);
	}

	@Override
	public int getCount() {
		return beans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return beans.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_cloudfile, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.file = (TextView) convertView.findViewById(R.id.file);
			holder.size = (TextView) convertView.findViewById(R.id.size);
			holder.from = (TextView) convertView.findViewById(R.id.from);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.details = (ImageView) convertView.findViewById(R.id.details);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final FileInfoBean bean = beans.get(position);
		String name = bean.getFileName() + "." + bean.getFormatName();
		String size = FileUtil.computeFileSize(bean.getFileSize());

		String time = "";

		time = DateTimeUtil.aTimeFormat(DateTimeUtil.getLongTime(bean
				.getUploadTime()));

		try {
			holder.img.setImageResource(FileUtil.getFileIcon(name));
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		holder.file.setText(name);
		holder.size.setText(size);
		holder.time.setText(time);

		if (bean.getShareMemberId() == 0) {
			holder.from.setVisibility(View.GONE);
		} else {
			holder.from.setVisibility(View.VISIBLE);
			String ownerName = bean.getShareMemberName();
			if (bean.getFileOwnerId() != bean.getMemberId()) {
				holder.from.setText("Fr：" + ownerName + "（圈子："
						+ bean.getFileOwnerName() + "）");
			} else {
				holder.from.setText("Fr：" + ownerName);
			}
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putInt("position", position);
				b.putSerializable("fileInfoBean", bean);
				LogicUtil.enter(mContext, FileDetailActivity.class, b, 10);
			}
		});

		holder.details.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((ImageView) v).setImageResource(R.drawable.arrow_down_0);
				View parent = (View) v.getParent();
				View anchor = parent.findViewById(R.id.img);
				showPPW(position, parent, anchor, v);
			}
		});
		return convertView;
	}

	private ViewGroup initItem(final int position, View parent) {
		final LinearLayout ll = new LinearLayout(mContext);
		ll.setGravity(Gravity.CENTER);
		MenuItem open = new MenuItem(mContext, R.drawable.file_open, "打开");
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		p.weight = 1;
		open.setOnClickEvent(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openFile(beans.get(position));
				ppw.dismiss();
			}
		});
		ll.addView(open, p);
		MenuItem share = new MenuItem(mContext, R.drawable.file_share, "分享");
		share.setOnClickEvent(new OnClickListener() {
			@Override
			public void onClick(View v) {
				shareFile(position);
				ppw.dismiss();
			}
		});
		ll.addView(share, p);
		MenuItem rename = new MenuItem(mContext, R.drawable.file_rename, "重命名");
		rename.setOnClickEvent(new OnClickListener() {
			@Override
			public void onClick(View v) {
				editFile(position);
				ppw.dismiss();
			}
		});
		ll.addView(rename, p);
		MenuItem delete = new MenuItem(mContext, R.drawable.file_delete, "删除");
		delete.setOnClickEvent(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteFile(beans.get(position));
				ppw.dismiss();
			}
		});
		ll.addView(delete, p);
		return ll;
	}

	private void showPPW(final int position, View pt, View anchor,
			final View arrow) {
		ppw = PopupWindowUtil.showSimpleItemMenu(mContext, arrow, pt,
				initItem(position, pt));
	}

	class ViewHolder {
		ImageView img, details;
		TextView file, size, time, from;
	}

	/**
	 * 删除文件
	 */
	private void deleteFile(final FileInfoBean file) {
		new AlertDialog.Builder(mContext)
				.setTitle("删除文件")
				.setMessage(
						"确定要删除 " + file.getFileName() + "."
								+ file.getFormatName() + " 吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						stopTask();
						spdDialog.showProgressDialog("正在删除...");
						deletefileStateTask = new DeleteFileStateTask(file);
						deletefileStateTask.execute();
					}
				}).setNegativeButton("取消", null).show();
	}

	private void stopTask() {
		if (null != deletefileStateTask
				&& deletefileStateTask.getStatus() == AsyncTask.Status.RUNNING) {
			deletefileStateTask.cancel(true);
		}
	}

	public class DeleteFileStateTask extends
			AsyncTask<String, Integer, MCResult> {

		private FileInfoBean bean;

		public DeleteFileStateTask(FileInfoBean fileBean) {
			this.bean = fileBean;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			if (null != bean) {
				try {
					int fileId = bean.getFileId();
					result = APIFileRequestServers.deleteFile(mContext, fileId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			if (null == mcResult) {
				T.show(mContext, "网络不稳定，请稍后再试！");
			} else {
				if (1 != mcResult.getResultCode()) {
					T.show(mContext, "网络不稳定，请稍后再试！");
				} else {
					T.show(mContext, "已删除！");
					beans.remove(bean);
					notifyDataSetChanged();
				}
			}
			spdDialog.cancelProgressDialog(null);
		}
	}

	@SuppressLint("HandlerLeak")
	private void openFile(final FileInfoBean fileInfoBean) {
		String filePath = fileInfoBean.getFilePath();
		final String tempName = filePath
				.substring(filePath.lastIndexOf("/") + 1);
		File myFile = new File(ConstantUtil.CLOUD_PATH + tempName);
		if (myFile != null && myFile.exists()) {
			new FileUtil().openFile(mContext, myFile);
			return;
		} else if (ConstantUtil.downloadingList.contains(tempName)) {
			// TODO 待定
			T.show(mContext, R.string.downloading);
			return;
		} else {
			spdDialog.showProgressDialog("正在处理中...");
			final Handler openHandler = new Handler() {
				public void handleMessage(Message msg) {
					spdDialog.cancelProgressDialog(null);
					switch (msg.what) {
					case 0:
						T.show(mContext, T.ErrStr);
						break;
					case 1:
						break;
					default:
						break;
					}
				};
			};
			ConstantUtil.downloadingList.add(tempName);
			new Thread() {
				public void run() {
					try {
						MCResult mcResult = APIFileRequestServers.getFilePath(
								mContext, fileInfoBean.getFileId());
						if (mcResult != null && mcResult.getResultCode() == 1) {
							String fileUrl = mcResult.getResult().toString();
							// String tn = fileUrl.substring(fileUrl
							// .lastIndexOf("/") + 1);
							// File myFile = new File(ConstantUtil.CLOUD_PATH +
							// tn);
							// if (myFile != null && myFile.exists()) {
							// new FileUtil().openFile(mContext, myFile);
							// } else {
							new DownLoadFileThread(mContext, fileUrl,
									fileInfoBean.getFileSize(),
									fileInfoBean.getFileName() + "."
											+ fileInfoBean.getFormatName(),
									true).start();
							openHandler.sendEmptyMessage(1);
							// }
						} else {
							ConstantUtil.downloadingList.remove(tempName);
							openHandler.sendEmptyMessage(0);
						}
					} catch (Exception e) {
						e.printStackTrace();
						ConstantUtil.downloadingList.remove(tempName);
						openHandler.sendEmptyMessage(0);
					}
				};
			}.start();
		}
	}

	/**
	 * 编辑文件
	 */
	@SuppressLint("HandlerLeak")
	private void editFile(final int position) {
		final FileInfoBean fileInfoBean = beans.get(position);
		View view = LayoutInflater.from(mContext).inflate(R.layout.edit_file,
				null);
		final EditText editText = (EditText) view
				.findViewById(R.id.editfile_name);
		TextView textView = (TextView) view.findViewById(R.id.edit_fromat);

		String mName = fileInfoBean.getFileName();
		int id = fileInfoBean.getFileId();

		editText.setText(mName);
		if (mName != null && !"".equals(mName))
			editText.setSelection(mName.length());
		editText.setHint("请输入文件名");
		textView.setVisibility(View.VISIBLE);
		textView.setText("." + fileInfoBean.getFormatName());

		final String name = mName;
		final int objectId = id;

		final Handler renameHandler = new Handler() {
			public void handleMessage(Message msg) {
				spdDialog.cancelProgressDialog(null);
				switch (msg.what) {
				case 0:
					T.show(mContext, T.ErrStr);
					break;
				case 1:
					int position = msg.arg1;
					beans.remove(position);
					beans.add(position, (FileInfoBean) msg.obj);
					notifyDataSetChanged();
					CloudFileActivity.isNeedRefresh = true;
					break;
				default:
					break;
				}
			};
		};

		new AlertDialog.Builder(mContext)
				.setTitle("重命名")
				.setView(view)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						BaseData.hideKeyViewBoard((Activity) mContext, editText);
						final String reName = editText.getEditableText()
								.toString();
						if (reName != null && !reName.equals("")) {
							if (reName.equals(name)) {
								return;
							}
							spdDialog.showProgressDialog("正在处理中...");
							new Thread() {
								public void run() {
									Message msg = new Message();
									try {
										MCResult mcResult = APIFileRequestServers
												.editFile(mContext, objectId,
														reName, null, null);
										if (mcResult != null
												&& mcResult.getResultCode() == 1) {
											msg.what = 1;
											fileInfoBean.setFileName(reName);
											msg.obj = fileInfoBean;
											msg.arg1 = position;
										} else {
											msg.what = 0;
										}
									} catch (Exception e) {
										msg.what = 0;
										e.printStackTrace();
									}
									renameHandler.sendMessage(msg);
								};
							}.start();
						} else {
							T.show(mContext, "名字不能为空");
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						BaseData.hideKeyViewBoard((Activity) mContext, editText);
					}
				}).show();
	}

	/**
	 * 分享文件
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void shareFile(int position) {
		CloudFileActivity.chosenBean = beans.get(position);
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
				.setTitle("分享")
				// .setItems(new String[] { "分享到朋友", "分享到交流圈" },
				// new DialogInterface.OnClickListener() {
				.setAdapter(
						new ArrayAdapter(mContext, R.layout.choice_item,
								new String[] { "分享到朋友", "分享到交流圈" }),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:// 分享到朋友
									Intent intent = new Intent(mContext,
											FriendsChooserActivity.class);
									intent.putExtra("type", 3);
									((Activity) mContext)
											.startActivityForResult(
													intent,
													FriendsChooserActivity.RESCODE);
									break;
								case 1:// 分享到交流圈
									Bundle b = new Bundle();
									b.putString("btnString", "分享");
									LogicUtil.enter(mContext,
											GroupsChooserActivity.class, b,
											GroupsChooserActivity.RESCODE);
									break;
								}
							}
						});// .setNegativeButton("取消", null).show();
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();

	}
}
