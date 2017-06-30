package com.datacomo.mc.spider.android;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.receiver.SimpleReceiver;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.StringUtil;
import com.datacomo.mc.spider.android.util.T;

public class EditActivity extends BasicActionBarActivity {
	public final static int TYPE_MOOD = 0;
	public final static int TYPE_MSG = TYPE_MOOD + 1;
	public final static int TYPE_DESCRIPTION = TYPE_MOOD + 2;

	private EditText edit;
	private ImageView delete;
	private TextView length;
	private Class<?> clazz;
	private String id, ori;
	private int type;

	private boolean hasSaved;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_edit);
		findViews();
		setView();
	}

	private void findViews() {
		edit = (EditText) findViewById(R.id.edit);
		edit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				setLength();
				hasSaved = false;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		length = (TextView) findViewById(R.id.lenght);
		setLength();
		delete = (ImageView) findViewById(R.id.delete);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edit.setText("");
			}
		});
		@SuppressWarnings("deprecation")
		int sH = getWindowManager().getDefaultDisplay().getHeight();
		LayoutParams llp = new LayoutParams(
				LayoutParams.MATCH_PARENT, (sH - 80) / 2);
		findViewById(R.id.relativelayout).setLayoutParams(llp);
	}

	private void setLength() {
		length.setText(edit.getText().toString().length() + "/140");
	}

	private void setView() {
		String title = "";
		Bundle b = getIntent().getExtras();
		clazz = getIntent().getClass();
		id = b.getString("id");
		ori = b.getString("ori");
		type = b.getInt("typedata");
		switch (type) {
		case TYPE_MOOD:
			title = "写心情";
			edit.setHint("亲，闲来写几句想必也是极好的吧……");
			break;
		case TYPE_MSG:
			title = "写留言";
			edit.setHint("给Ta留个言吧...");
			break;
		case TYPE_DESCRIPTION:
			title = "圈子简介";
			// edit.setHint("");
			break;
		default:
			break;
		}
		ab.setTitle(title);
		if (ori != null) {
			if (ori.length() > 140) {
				ori = ori.substring(0, 139);
			}
			edit.setText(ori);
			edit.setSelection(ori.length());
			edit.selectAll();
			hasSaved = true;
		}
	}

	private MCResult sendMessage() throws Exception {
		MCResult mcResult = null;
		if (type == TYPE_MSG) {
			mcResult = APIRequestServers.leaveGuestBookWord(EditActivity.this,
					id, getInput());
		} else if (type == TYPE_MOOD) {
			mcResult = APIRequestServers.createMoodContent(EditActivity.this,
					getInput());
		} else if (type == TYPE_DESCRIPTION) {
			mcResult = APIRequestServers
					.editGroupBasicInfo(EditActivity.this, id, null, false,
							getInput(), true, null, false, null, false);
		}
		return mcResult;

	}

	class loadInfoTask extends AsyncTask<String, Integer, MCResult> {

		Context context;

		public loadInfoTask(Context context) {
			spdDialog.showProgressDialog("正在提交...");
			this.context = context;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult mcResult = null;
			try {
				mcResult = sendMessage();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			spdDialog.cancelProgressDialog(null);
			if (result == null) {
				showTip(T.ErrStr);
			} else {
				if (result.getResultCode() != 1) {
					showTip(T.ErrStr);
				} else {
					switch (type) {
					case TYPE_MOOD:
						Intent intent = new Intent(EditActivity.this, clazz);
						intent.putExtra("feelword", getInput());
						setResult(RESULT_OK, intent);
						showTip("修改成功！");
						break;

					case TYPE_MSG:
						showTip("已发送！");
						break;
					case TYPE_DESCRIPTION:
						Bundle b = new Bundle();
						b.putString(BundleKey.DESCRIPTION, getInput());
						SimpleReceiver.sendBoardcast(EditActivity.this,
								SimpleReceiver.RECEIVER_EDIT_DESCRIPTION, b);
						showTip("修改成功！");
						break;
					default:
						break;
					}
					LogicUtil.finish(EditActivity.this);
				}
			}
		}

	}

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
			if (!hasSaved) {
				switch (type) {
				case TYPE_MOOD:
					isExit("是否放弃发布？");
					break;
				case TYPE_MSG:
					isExit("是否放弃留言？");
					break;
				case TYPE_DESCRIPTION:
					isExit("是否放弃编辑？");
					break;
				default:
					break;
				}
			} else {
				finish();
			}
			return true;
		case R.id.action_send:
			String info = getInput();
			if (info != null && !info.equals("")) {
				info = StringUtil.trimInnerSpaceStr(info);
				if (type == TYPE_MOOD) {
					if (!info.equals(ori)) {
						new loadInfoTask(EditActivity.this).execute();
					} else {
						LogicUtil.finish(EditActivity.this);
					}
				} else {
					new loadInfoTask(EditActivity.this).execute();
				}
			} else {
				showTip("亲，您还没有输入内容哦~~~");
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			spdDialog.cancelProgressDialog(null);
			if (!hasSaved) {
				switch (type) {
				case TYPE_MOOD:
					isExit("是否放弃发布？");
					break;
				case TYPE_MSG:
					isExit("是否放弃留言？");
					break;
				case TYPE_DESCRIPTION:
					isExit("是否放弃编辑？");
					break;
				default:
					break;
				}
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private String getInput() {
		return edit.getText().toString();
	}

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

}
