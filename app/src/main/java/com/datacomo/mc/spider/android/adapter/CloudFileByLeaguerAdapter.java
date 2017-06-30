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
import android.view.View.OnLongClickListener;
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
import com.datacomo.mc.spider.android.CloudFileWithActivity;
import com.datacomo.mc.spider.android.FileDetailActivity;
import com.datacomo.mc.spider.android.FriendsChooserActivity;
import com.datacomo.mc.spider.android.GroupsChooserActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.dialog.SpinnerProgressDialog;
import com.datacomo.mc.spider.android.net.APIFileRequestServers;
import com.datacomo.mc.spider.android.net.been.FileInfoBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ShareLeaguerRecord;
import com.datacomo.mc.spider.android.service.DownLoadFileThread;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.MenuItem;
import com.datacomo.mc.spider.android.view.PopupWindowUtil;

public class CloudFileByLeaguerAdapter extends BaseAdapter {
	Context mContext;
	protected ArrayList<ShareLeaguerRecord> beans;
	private int myId;

	boolean isShare;
	private FileInfoTask fileInfoTask;
	private DeleteFileStateTask deletefileStateTask;

	private SpinnerProgressDialog spdDialog;
	private PopupWindow ppw;

	public CloudFileByLeaguerAdapter(Context context,
			ArrayList<ShareLeaguerRecord> text) {
		mContext = context;
		this.beans = text;
		spdDialog = new SpinnerProgressDialog(context);
		UserBusinessDatabase business = new UserBusinessDatabase(App.app);
		String session_key = App.app.share.getSessionKey();
		try {
			myId = Integer.valueOf(business.getMemberId(session_key));
		} catch (Exception e) {
			e.printStackTrace();
		}
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

		final ShareLeaguerRecord bean = beans.get(position);
		String name = bean.getFileName() + "." + bean.getFormatName();
		String size = FileUtil.computeFileSize(bean.getFileSize());

		String time = "";

		time = DateTimeUtil.aTimeFormat(DateTimeUtil.getLongTime(bean
				.getShareTime()));

		holder.img.setImageResource(FileUtil.getFileIcon(name));
		holder.file.setText(name);
		holder.size.setText(size);
		holder.time.setText(time);

		if (bean.getShareMemberId() == 0 || bean.getShareMemberId() == myId) {
			holder.from.setVisibility(View.GONE);
		} else {
			holder.from.setVisibility(View.VISIBLE);
			String ownerName = bean.getShareMemberName();
			String groupName = bean.getSourceGroupName();
			if (null != groupName && !"".equals(groupName)
					&& !"null".equals(groupName)) {
				holder.from.setText("Fr：" + ownerName + "（圈子："
						+ bean.getSourceGroupName() + "）");
			} else {
				holder.from.setText("Fr：" + ownerName);
			}
		}

		OnClickListener detailsListener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null != fileInfoTask
						&& fileInfoTask.getStatus() == AsyncTask.Status.RUNNING) {
					fileInfoTask.cancel(true);
				}
				spdDialog.showProgressDialog("正在加载中...");
				fileInfoTask = new FileInfoTask("" + bean.getFileId());
				fileInfoTask.execute();
			}
		};
		convertView.setOnClickListener(detailsListener);

		convertView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				showPPW(position, v.findViewById(R.id.details), v);
				return false;
			}
		});
		holder.details.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				View parent = (View) v.getParent();
				showPPW(position, v, parent);
			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView img, details;
		TextView file, size, time, from;
	}

	private ViewGroup initItem(final int position, final FileInfoBean b) {
		final LinearLayout ll = new LinearLayout(mContext);
		ll.setGravity(Gravity.CENTER);
		MenuItem open = new MenuItem(mContext, R.drawable.file_open, "打开");
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		p.weight = 1;
		open.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openFile(b);
				ppw.dismiss();
			}
		});
		ll.addView(open, p);
		MenuItem share = new MenuItem(mContext, R.drawable.file_share, "分享");
		share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				shareFile(position);
				ppw.dismiss();
			}
		});
		ll.addView(share, p);
		MenuItem rename = new MenuItem(mContext, R.drawable.file_rename, "重命名");
		rename.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				editFile(position);
				ppw.dismiss();
			}
		});
		ll.addView(rename, p);
		MenuItem delete = new MenuItem(mContext, R.drawable.file_delete, "删除");
		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteFile(b);
				ppw.dismiss();
			}
		});
		ll.addView(delete, p);
		return ll;
	}

	@SuppressLint("HandlerLeak")
	private void showPPW(final int position, final View v, View anchor) {

		final Handler dHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					FileInfoBean b = (FileInfoBean) msg.obj;
					if (null == b) {
						T.show(mContext, "该文件已被删除！");
						return;
					}
					((ImageView) v).setImageResource(R.drawable.arrow_rights);
					ppw = PopupWindowUtil.showSimpleItemMenu(mContext, v,
							(View) v.getParent(), initItem(position, b));
					break;
				case 1:
					break;
				default:
					break;
				}
			};
		};
		new Thread() {
			public void run() {
				try {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = getFileInfoBean(beans.get(position).getFileId());
					dHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	// class MenuItem extends LinearLayout {
	// public MenuItem(Context context, int imgId, String text) {
	// super(context);
	// setOrientation(LinearLayout.VERTICAL);
	// setGravity(Gravity.CENTER);
	// ImageView img = new ImageView(context);
	// img.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
	// LayoutParams.WRAP_CONTENT));
	// img.setImageResource(imgId);
	// TextView textV = new TextView(context);
	// textV.setGravity(Gravity.CENTER);
	// textV.setTextColor(Color.WHITE);
	// textV.setTextSize(13);
	// textV.setText(text);
	// int px = DensityUtil.dip2px(mContext, 40);
	// addView(img, px, px);
	// addView(textV);
	// }
	//
	// public void setOnClickEvent(OnClickListener listener) {
	// if (null != listener) {
	// setOnClickListener(listener);
	// }
	// }
	// }

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
		spdDialog.cancelProgressDialog(null);
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
				T.show(mContext, T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					T.show(mContext, T.ErrStr);
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
							new DownLoadFileThread(mContext, fileUrl,
									fileInfoBean.getFileSize(),
									fileInfoBean.getFileName() + "."
											+ fileInfoBean.getFormatName(),
									true).start();
							openHandler.sendEmptyMessage(1);
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
		final ShareLeaguerRecord fileInfoBean = beans.get(position);
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
					T.show(mContext, "已修改！");
					int position = msg.arg1;
					beans.remove(position);
					beans.add(position, (ShareLeaguerRecord) msg.obj);
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
		CloudFileWithActivity.chosenBean = beans.get(position);
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

	/* 显示读取中对话框 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void showShareDia(Context context, String title,
			String text[]) {
		new AlertDialog.Builder(context)
				.setTitle(title)
				// .setItems(text, new DialogInterface.OnClickListener() {
				.setAdapter(
						new ArrayAdapter(context, R.layout.choice_item, text),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichcountry) {
								switch (whichcountry) {
								case 0:
									break;
								case 1:
									break;
								default:
									break;
								}
							}
						})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface d, int which) {
						d.dismiss();
					}
				}).show();
	}

	class FileInfoTask extends AsyncTask<String, Integer, MCResult> {
		private String fileId;

		public FileInfoTask(String fileId) {
			this.fileId = fileId;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIFileRequestServers.fileInfo(mContext,
						Integer.valueOf(fileId));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			spdDialog.cancelProgressDialog(null);
			if (null == mcResult) {
				T.show(mContext, T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					T.show(mContext, T.ErrStr);
				} else {
					FileInfoBean fileInfoBean = (FileInfoBean) mcResult
							.getResult();
					if (null == fileInfoBean) {
						T.show(mContext, "该文件已被删除！");
					} else {
						Bundle b = new Bundle();
						b.putSerializable("fileInfoBean", fileInfoBean);
						LogicUtil.enter(mContext, FileDetailActivity.class, b,
								false);
					}
				}
			}
		}
	}

	private FileInfoBean getFileInfoBean(int fileId) {
		MCResult result = null;
		try {
			result = APIFileRequestServers.fileInfo(mContext, fileId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		FileInfoBean fileInfoBean = null;
		if (null != result) {
			fileInfoBean = (FileInfoBean) result.getResult();
		}
		return fileInfoBean;
	}
}
