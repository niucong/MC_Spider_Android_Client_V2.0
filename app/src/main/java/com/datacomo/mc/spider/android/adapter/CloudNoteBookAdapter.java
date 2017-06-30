package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.CloudNoteWithActivity;
import com.datacomo.mc.spider.android.NoteActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.bean.NoteDraftBean;
import com.datacomo.mc.spider.android.db.NoteCreateService;
import com.datacomo.mc.spider.android.net.APINoteRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.note.NoteBookBean;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;

public class CloudNoteBookAdapter extends BaseAdapter {
	// private final String TAG = "CloudNoteBookAdapter";
	private Context mContext;
	private ArrayList<NoteBookBean> notes;

	// private boolean isLocal;

	public CloudNoteBookAdapter(Context c, ArrayList<NoteBookBean> notes) {
		mContext = c;
		this.notes = notes;
	}

	// public void setLocal(boolean isLocal) {
	// this.isLocal = isLocal;
	// }

	@Override
	public int getCount() {
		return notes.size();
	}

	@Override
	public Object getItem(int position) {
		return notes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_note_book, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.fileNum = (TextView) convertView.findViewById(R.id.file_num);
			holder.head = (ImageView) convertView.findViewById(R.id.head_img);
			convertView.findViewById(R.id.head_img_bg).setVisibility(View.GONE);
			holder.num = (TextView) convertView.findViewById(R.id.num);
			holder.details = (LinearLayout) convertView
					.findViewById(R.id.ll_details);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final NoteBookBean note = notes.get(position);
		final String title = note.getTitle();
		if ("默认笔记本".equals(title)) {
			holder.head.setImageResource(R.drawable.note_def);
		} else if ("我的日记".equals(title)) {
			holder.head.setImageResource(R.drawable.note_diary);
		} else if ("我的备忘".equals(title)) {
			holder.head.setImageResource(R.drawable.note_backup);
		} else {
			holder.head.setImageResource(R.drawable.note_other);
		}
		holder.name.setText(title);

		int num = note.getNotesNum();
		ArrayList<NoteDraftBean> beans = NoteCreateService.getService(mContext)
				.queryNotesByBook(Integer.valueOf(note.getNoteBookId()));
		if (beans != null)
			num += beans.size();
		holder.fileNum.setText(num + "");
		if ("1".equals(note.getIsRead())) {
			holder.num.setVisibility(View.VISIBLE);
		} else {
			holder.num.setVisibility(View.GONE);
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if (note.getNotesNum() > 0) {
				Bundle b = new Bundle();
				b.putString("id", note.getNoteBookId() + "");
				b.putString("name", title);
				b.putInt("num", note.getNotesNum());
				b.putBoolean("IsBook", true);
				LogicUtil.enter(mContext, CloudNoteWithActivity.class, b, 10);
				// }
			}
		});

		convertView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				operBook(note, v);
				return false;
			}
		});

		return convertView;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void operBook(final NoteBookBean note, final View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
				.setAdapter(new ArrayAdapter(mContext, R.layout.choice_item,
						new String[] { "重命名", "删除" }),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									editBook(note, v);
									break;
								case 1:
									deleteFile(note);
									break;
								}
							}
						});
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	/**
	 * 删除
	 */
	@SuppressLint("HandlerLeak")
	private void deleteFile(final NoteBookBean note) {
		final Handler handler = new Handler() {
			@SuppressWarnings("static-access")
			public void handleMessage(Message msg) {
				((NoteActivity) mContext).spdDialog.cancelProgressDialog(null);
				switch (msg.what) {
				case 0:
					((NoteActivity) mContext).showTip(T.ErrStr);
					break;
				case 1:
					((NoteActivity) mContext).deleteNoteBook(note);
					break;
				default:
					break;
				}
			};
		};

		String tip = "";
		if (note.getNotesNum() > 0) {
			tip = "笔记本下的所有笔记将同时被彻底删除,";
		}
		new AlertDialog.Builder(mContext).setTitle("删除笔记本")
				.setMessage(tip + "确定要删除 " + note.getTitle() + " 笔记本吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@SuppressWarnings("static-access")
					@Override
					public void onClick(DialogInterface dialog, int which) {
						((NoteActivity) mContext).spdDialog
								.showProgressDialog("正在处理中...");
						new Thread() {
							public void run() {
								Message msg = new Message();
								try {
									MCResult mcResult = APINoteRequestServers
											.deleteNoteBook(mContext,
													note.getNoteBookId() + "");
									if (mcResult != null
											&& mcResult.getResultCode() == 1) {
										if ("1".equals(mcResult.getResult()
												.toString())) {
											msg.what = 1;
										} else if ("4".equals(mcResult
												.getResult().toString())) {
											msg.what = 1;
										} else {
											msg.what = 0;
										}
									} else {
										msg.what = 0;
									}
								} catch (Exception e) {
									msg.what = 0;
									e.printStackTrace();
								}
								handler.sendMessage(msg);
							};
						}.start();
					}
				}).setNegativeButton("取消", null).show();
	}

	@SuppressLint("HandlerLeak")
	private void editBook(final NoteBookBean note, final View v) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.edit_file,
				null);
		final EditText editText = (EditText) view
				.findViewById(R.id.editfile_name);
		TextView textView = (TextView) view.findViewById(R.id.edit_fromat);
		textView.setVisibility(View.GONE);
		final String title = note.getTitle();
		editText.setText(title);
		try {
			if (title != null && !"".equals(title))
				editText.setSelection(title.length());
		} catch (Exception e) {
			e.printStackTrace();
		}

		final Handler handler = new Handler() {
			@SuppressWarnings("static-access")
			public void handleMessage(Message msg) {
				((NoteActivity) mContext).spdDialog.cancelProgressDialog(null);
				switch (msg.what) {
				case 0:
					((NoteActivity) mContext).showTip(T.ErrStr);
					break;
				case 1:
					((TextView) v.findViewById(R.id.name)).setText(msg.obj
							.toString());
					break;
				case 2:
					((NoteActivity) mContext).showTip("笔记本已删除");
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

					@SuppressWarnings("static-access")
					@Override
					public void onClick(DialogInterface dialog, int which) {
						BaseData.hideKeyViewBoard((NoteActivity) mContext,
								editText);

						final String noteBookName = editText.getEditableText()
								.toString();
						if (noteBookName.equals(title)) {
							return;
						}
						if (noteBookName != null && !noteBookName.equals("")) {
							((NoteActivity) mContext).spdDialog
									.showProgressDialog("正在处理中...");
							new Thread() {
								public void run() {
									Message msg = new Message();
									try {
										MCResult mcResult = APINoteRequestServers
												.editNoteBook(mContext,
														note.getNoteBookId()
																+ "",
														noteBookName);
										if (mcResult != null
												&& mcResult.getResultCode() == 1) {
											if ("1".equals(mcResult.getResult()
													.toString())) {
												msg.what = 1;
												msg.obj = noteBookName;
											} else if ("3".equals(mcResult
													.getResult().toString())) {
												msg.what = 2;
											} else {
												msg.what = 0;
											}
										} else {
											msg.what = 0;
										}
									} catch (Exception e) {
										msg.what = 0;
										e.printStackTrace();
									}
									handler.sendMessage(msg);
								};
							}.start();
						} else {
							((NoteActivity) mContext).showTip("名字不能为空");
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						BaseData.hideKeyViewBoard((NoteActivity) mContext,
								editText);
					}
				}).show();
	}

	class ViewHolder {
		TextView name, fileNum, num;
		ImageView head;
		LinearLayout details;
	}

}
