package com.datacomo.mc.spider.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;

public abstract class BaseMidMenuActivity extends BaseActWithDropTitle {
	LinearLayout midll;
	public int sType;
	public final static int TYPE_MBER = 0;
	public final static int TYPE_GROUP = 1;
	public final static int TYPE_OPAGE = 2;

	public final static String MM_NEWS = "动态";
	public final static String MM_FILE = "文件";
	public final static String MM_IMG = "图片";
	public final static String MM_PERINFO = "个人信息";
	public final static String MM_VISITOR = "访客";
	public final static String MM_MOOD = "心情";
	public final static String MM_LVMSG = "留言";

	// public final static String MMG_PERINFO = "圈子信息";
	// public final static String MM_FAMILY = "圈子图谱";
	// public final static String MM_LEAGURE = "圈子成员";

	public String id, name, number;
	public static int visitMemberNum, memberNum;
	int sH, sW;
	PopupWindow ppw = null;
	boolean userSelf;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sH = getWindowManager().getDefaultDisplay().getHeight();
		sW = getWindowManager().getDefaultDisplay().getWidth();
		getIntentMsg();
		midll = new LinearLayout(this);
		midll.setOrientation(LinearLayout.VERTICAL);
		midll.setPadding(4, 0, 4, 4);
		midll.setBackgroundResource(R.drawable.bg_midmenu);
		if (userSelf) {
			setTitle("", R.drawable.title_menu, null);
		} else {
			setTitle("", R.drawable.title_fanhui, null);
		}
		initMidMenu(this);
	}

	abstract void initData();

	@Override
	protected void onLeftClick(View v) {
		// if (userSelf) {
		// slide.sliding(menus);
		// } else {
		finish();
		// }
	}

	@Override
	protected void onTitleClick() {
		super.onTitleClick();
		MenuShow(midll);
	}

	@Override
	protected void onRightClick(View v) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(BundleKey.TYPE_REQUEST,
				Type.STARTCREATGROUPTOPIC);
		LogicUtil.enter(this, GroupsChooserActivity.class, bundle, false);
	}

	// @Override
	// protected void onResume() {
	// super.onResume();
	// slide.close();
	// }

	void initMidMenu(final Context c) {
		View.OnClickListener midMenu = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((ViewGroup) v).getChildAt(0).performClick();
				ppw.dismiss();
				String curActivityName = ((Activity) c).getClass().getName();
				try {
					switch ((Integer) (v.getTag())) {
					case 0:
						if (!curActivityName.endsWith("HomePgActivity")) {
							Bundle b0 = new Bundle();
							b0.putString("id", id);
							b0.putString("name", name);
							b0.putInt("type", TYPE_MBER);
							LogicUtil.enter(c, HomePgActivity.class, b0, false);
						}
						break;
					case 1:
						if (!curActivityName.endsWith("FileListActivity")) {
							LogicUtil.enter(c, FileListActivity.class,
									getBasicBoundle(sType), false);
						}
						break;
					case 2:
						if (!curActivityName.endsWith("ImgGridActivity")) {
							LogicUtil.enter(c, ImgGridActivity.class,
									getBasicBoundle(sType), false);
						}
						break;
					case 3:
						if (!curActivityName.endsWith("PerInformationActivity")) {
							LogicUtil.enter(c, PerInformationActivity.class,
									getBasicBoundle(sType), false);
						}
						break;
					case 4:
						// if (!s[1].equals("MemberId") &&
						// !s[1].equals("OpenPageId")) {
						// if ("NO_RELATION".equals(joinGroupStatus)
						// || "COOPERATION_LEAGUER".equals(joinGroupStatus)) {
						// enterHandler.sendEmptyMessage(2);
						// return;
						// }
						// }
						if (sType == TYPE_MBER) {
							LogicUtil.enter(c, VisitorListActivity.class,
									getBasicBoundle(TYPE_MBER), false);
						}
						break;
					case 5:
						LogicUtil.enter(c, MoodListActivity.class,
								getBasicBoundle(sType), false);
						break;
					case 6:
						LogicUtil.enter(c, LeaveMsgActivity.class,
								getBasicBoundle(sType), false);
						break;
					default:
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		midll.removeAllViews();
		String visiNum = "";
		if (visitMemberNum > 99) {
			visiNum = "(99+)";
		} else if (visitMemberNum > 0) {
			visiNum = "(" + visitMemberNum + ")";
		}
		int i = 0;
		addChild(midll,
				getMidMenuItem(MM_NEWS, R.drawable.midmenu_news, i++, midMenu));
		addChild(midll,
				getMidMenuItem(MM_FILE, R.drawable.midmenu_file, i++, midMenu));
		addChild(midll,
				getMidMenuItem(MM_IMG, R.drawable.midmenu_img, i++, midMenu));
		addChild(
				midll,
				getMidMenuItem(MM_PERINFO, R.drawable.midmenu_perinfo, i++,
						midMenu));
		addChild(
				midll,
				getMidMenuItem(MM_VISITOR + visiNum,
						R.drawable.midmenu_visitor, i++, midMenu));
		addChild(midll,
				getMidMenuItem(MM_MOOD, R.drawable.midmenu_faver, i++, midMenu));
		addChild(
				midll,
				getMidMenuItem(MM_LVMSG, R.drawable.midmenu_leavemsg, i++,
						midMenu));
		midll.removeViewAt(midll.getChildCount() - 1);
		midll.getChildAt(midll.getChildCount() - 1).setBackgroundResource(
				R.drawable.btn_menu_white_bottom_round);
	}

	void addChild(LinearLayout parent, View child) {
		parent.addView(child);
		View driver = new View(this);
		driver.setBackgroundColor(getResources()
				.getColor(R.color.driver_c9caca));
		parent.addView(driver, new LayoutParams(
				LayoutParams.WRAP_CONTENT, 1));
	}

	public void setMidChosen(int which) {
		((ViewGroup) (midll.findViewWithTag(which))).setEnabled(false);
		((ViewGroup) (midll.findViewWithTag(which))).getChildAt(2)
				.setVisibility(View.VISIBLE);
	}

	View getMidMenuItem(Context c, String string, final int i, int tag,
			View.OnClickListener listener, int selected) {
		LinearLayout ll = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.item_mid, null);
		ll.setTag(tag);
		ll.setOnClickListener(listener);
		ImageView icon = (ImageView) ll.findViewById(R.id.mid_icon);
		icon.setImageResource(i);
		ImageView check = (ImageView) ll.findViewById(R.id.mid_check);
		check.setVisibility(selected);
		TextView text = (TextView) ll.findViewById(R.id.mid_name);
		text.setText(string);
		return ll;
	}

	View getMidMenuItem(String string, int i, int tag,
			View.OnClickListener listener) {
		return getMidMenuItem(this, string, i, tag, listener, View.GONE);
	}

	void MenuShow(View v) {
		if (null != v) {
			boolean mid = (v == midll);
			if (mid) {
				ppw = new PopupWindow(v, sW - 140, LayoutParams.WRAP_CONTENT);
			} else {
				ppw = new PopupWindow(v, sW / 3 + 100,
						LayoutParams.WRAP_CONTENT);
			}
			ppw.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.nothing));
			ppw.setFocusable(true);
			ppw.setOutsideTouchable(true);
			if (mid) {
				ppw.showAsDropDown(findViewById(R.id.header), 70, 0);
			} else {
				ppw.showAsDropDown(right, 0, 10);
			}
			Animation show = AnimationUtils.loadAnimation(this,
					R.anim.p_enter_up);
			v.setVisibility(View.VISIBLE);
			v.startAnimation(show);
		}
	}

	private void getIntentMsg() {
		Bundle b = getIntent().getExtras();
		id = b.getString("id");
		name = b.getString("name");
		sType = b.getInt("type");
		if ((GetDbInfoUtil.getMemberId(this) + "").equals(id)) {
			userSelf = true;
		} else {
			userSelf = false;
		}
	}

	protected Bundle getBasicBoundle(int type) {
		Bundle b = new Bundle();
		b.putString("id", id);
		b.putString("name", name);
		b.putInt("type", type);
		return b;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		getIntentMsg();
		initData();
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// if (userSelf)
	// slide.sliding(menus);
	// return false;
	// }

	// @Override
	// public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu)
	// {
	// if (userSelf)
	// slide.sliding(menus);
	// return super.onCreateOptionsMenu(menu);
	// }
}
