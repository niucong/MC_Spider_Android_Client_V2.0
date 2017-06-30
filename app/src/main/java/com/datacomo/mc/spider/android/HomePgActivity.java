package com.datacomo.mc.spider.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.SearchAutoComplete;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.fragmt.BaseGroupFragment;
import com.datacomo.mc.spider.android.fragmt.HomeFileFragment;
import com.datacomo.mc.spider.android.fragmt.HomeInfoFragment;
import com.datacomo.mc.spider.android.fragmt.HomeLeaveFragment;
import com.datacomo.mc.spider.android.fragmt.HomeMoodFragment;
import com.datacomo.mc.spider.android.fragmt.HomePhotoFragment;
import com.datacomo.mc.spider.android.fragmt.HomeQboFragment;
import com.datacomo.mc.spider.android.fragmt.HomeVisitorFragment;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.DensityUtil;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomePgActivity extends BasicActionBarActivity {
    private final String TAG = "HomePgActivity";

    LinearLayout midll;
    public int sType;
    public final static int TYPE_MBER = 0;
    public final static int TYPE_GROUP = 1;
    public final static int TYPE_OPAGE = 2;

    public final static int FROM_GALLERY = 0;
    public final static int FROM_CAMERA = FROM_GALLERY + 1;
    public final static int FROM_CROP = FROM_GALLERY + 2;
    public final static int SEND_MOOD = FROM_GALLERY + 3;
    public final static int ADD_GROUP = FROM_GALLERY + 4;

    public final static String MM_NEWS = "动态";
    public final static String MM_FILE = "文件";
    public final static String MM_IMG = "图片";
    public final static String MM_PERINFO = "个人信息";
    public final static String MM_VISITOR = "访客";
    public final static String MM_MOOD = "心情";
    public final static String MM_LVMSG = "留言";
    int[] ds = { R.drawable.midmenu_news, R.drawable.midmenu_file,
            R.drawable.midmenu_img, R.drawable.midmenu_perinfo,
            R.drawable.midmenu_visitor, R.drawable.midmenu_faver,
            R.drawable.midmenu_leavemsg };

    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, Fragment> hm = new HashMap<Integer, Fragment>();

    public String id, name, number, title;
    public static int visitMemberNum, memberNum;

    public int sW, sH;
    public PopupWindow ppw = null;
    public boolean userSelf;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.layout_home);
        sH = getWindowManager().getDefaultDisplay().getHeight();
        sW = getWindowManager().getDefaultDisplay().getWidth();

        getIntentMsg();
        midll = new LinearLayout(this);
        midll.setOrientation(LinearLayout.VERTICAL);
        midll.setPadding(4, 0, 4, 4);
        midll.setBackgroundResource(R.drawable.bg_midmenu);
        ab.setIcon(R.drawable.action_logo);
        ab.setTitle("");

        // 生成一个SpinnerAdapter
        // SpinnerAdapter adapter = ArrayAdapter.createFromResource(this,
        // R.array.member_titles, R.layout.actionbar_spinner_item);

        List<String> sList = new ArrayList<String>();
        sList.add(MM_NEWS);
        sList.add(MM_FILE);
        sList.add(MM_IMG);
        sList.add(MM_PERINFO);
        sList.add(MM_VISITOR);
        sList.add(MM_MOOD);
        sList.add(MM_LVMSG);
        final SpinnerAdapter adapter = new CustomAdapter(this,
                R.layout.actionbar_spinner_item, android.R.id.text1, sList);

        // 将ActionBar的操作模型设置为NAVIGATION_MODE_LIST
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        // 为ActionBar设置下拉菜单和监听器
        ab.setListNavigationCallbacks(adapter, new DropDownListenser());
        // initMidMenu(this);
    }

    class CustomAdapter extends ArrayAdapter<String> {

        public CustomAdapter(Context context, int resource,
                             int textViewResourceId, List<String> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(final int position, final View convertView,
                            final ViewGroup parent) {
            View view = convertView;
            if (null == view) {
                view = LayoutInflater.from(this.getContext()).inflate(
                        R.layout.actionbar_spinner_item, null);
                ((TextView) view).setText(getItem(position));
            }
            final LayoutParams params = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            view.setPadding(DensityUtil.dip2px(HomePgActivity.this, -1), 0, 0,
                    0);
            view.setLayoutParams(params);
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView view = (TextView) LayoutInflater.from(this.getContext())
                    .inflate(R.layout.actionbar_spinner_item, null);
            view.setTextColor(Color.BLACK);
            view.setText(" " + getItem(position));
            Drawable drawable = getResources().getDrawable(ds[position]);
            int dw = drawable.getIntrinsicWidth();
            int dh = drawable.getIntrinsicHeight();
            drawable.setBounds(0, 0, 2 * dw / 3, 2 * dh / 3);
            view.setCompoundDrawables(drawable, null, null, null);
            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_refresh).setVisible(true);

        this.menu = menu;
        addSearch();
        L.d(TAG, "onCreateOptionsMenu...");
        return super.onCreateOptionsMenu(menu);
    }

    private void addSearch() {
        MenuItem smi = menu.findItem(R.id.action_search);
        // smi.setVisible(true);
        searchView = (SearchView) smi.getActionView();
        ImageView v = (ImageView) searchView
                .findViewById(com.actionbarsherlock.R.id.abs__search_button);
        v.setImageResource(R.drawable.action_search);
        View vp = searchView
                .findViewById(com.actionbarsherlock.R.id.abs__search_plate);
        vp.setBackgroundResource(R.drawable.edit_bg);

        final ImageView cv = (ImageView) searchView
                .findViewById(com.actionbarsherlock.R.id.abs__search_close_btn);
        cv.setImageResource(R.drawable.search_close);
        searchView.setOnSearchClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SearchAutoComplete mQueryTextView = (SearchAutoComplete) searchView
                        .findViewById(com.actionbarsherlock.R.id.abs__search_src_text);
                if (mQueryTextView.isShown()
                        && "".equals(mQueryTextView.getText().toString())) {
                    cv.setVisibility(View.GONE);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                L.d(TAG, "addSearch s=" + s);
                ((BaseGroupFragment) hm.get(itemP)).search(s);
                BaseData.hideKeyBoard(HomePgActivity.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if ("".equals(s)) {
                    cv.setVisibility(View.GONE);
                } else {
                    cv.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                ((BaseGroupFragment) hm.get(itemP)).search("");
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(
            MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (searchView.findViewById(
                        com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
                    searchView.onActionViewCollapsed();
                    ((BaseGroupFragment) hm.get(itemP)).search("");
                    return false;
                }
                return super.onOptionsItemSelected(item);
            case R.id.action_refresh:
                try {
                    ((BaseGroupFragment) hm.get(itemP)).onFragmentRefresh(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.action_more:
                MenuShow(((HomeQboFragment) hm.get(itemP)).rightll);
                return true;
            case R.id.action_write:
                if (itemP == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BundleKey.TYPE_REQUEST,
                            Type.STARTCREATGROUPTOPIC);
                    LogicUtil.enter(this, GroupsChooserActivity.class, bundle,
                            false);
                } else if (itemP == 3) {
                    try {
                        Intent intent = new Intent();
                        intent.setClass(this, PiEditorActivity.class);
                        Bundle b = new Bundle();
                        b.putString("id", id);
                        b.putSerializable("memberBean",
                                ((HomeInfoFragment) hm.get(itemP)).memberBean);
                        intent.putExtras(b);
                        startActivityForResult(intent, 11);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
                return true;
            case R.id.action_write_mood:
                if (itemP == 5) {
                    Intent mIntent = new Intent(this, EditActivity.class);
                    Bundle b = new Bundle();
                    b.putString("id", id);
                    b.putInt("type", TYPE_MBER);
                    b.putInt("typedata", EditActivity.TYPE_MOOD);
                    mIntent.putExtras(b);
                    startActivityForResult(mIntent, EditActivity.TYPE_MOOD);
                } else {
                    Bundle b = getBasicBoundle(TYPE_MBER);
                    b.putInt("typedata", EditActivity.TYPE_MSG);
                    LogicUtil.enter(this, EditActivity.class, b, true);
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
                if (searchView.findViewById(
                        com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
                    searchView.onActionViewCollapsed();
                    ((BaseGroupFragment) hm.get(itemP)).search("");
                    return true;
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    int itemP;

    /**
     * 实现 ActionBar.OnNavigationListener接口
     */
    class DropDownListenser implements OnNavigationListener {

        /* 当选择下拉菜单项的时候，将Activity中的内容置换为对应的Fragment */
        public boolean onNavigationItemSelected(int itemPosition, long itemId) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment fragment = hm.get(itemPosition);
            itemP = itemPosition;
            if (null == fragment) {
                switch (itemPosition) {
                    case 0:
                        fragment = new HomeQboFragment();
                        break;
                    case 1:
                        fragment = new HomeFileFragment();
                        break;
                    case 2:
                        fragment = new HomePhotoFragment();
                        break;
                    case 3:
                        fragment = new HomeInfoFragment();
                        break;
                    case 4:
                        fragment = new HomeVisitorFragment();
                        break;
                    case 5:
                        fragment = new HomeMoodFragment();
                        break;
                    case 6:
                        fragment = new HomeLeaveFragment();
                        break;
                    default:
                        break;
                }
                hm.put(itemPosition, fragment);
            }
            ft.setCustomAnimations(R.anim.scale_in_mid, R.anim.scale_out_mid);
            ft.replace(R.id.container, fragment);
            ft.commitAllowingStateLoss();
            return true;
        }
    }

    public void addChild(LinearLayout parent, View child) {
        parent.addView(child);
        View driver = new View(this);
        driver.setBackgroundColor(getResources()
                .getColor(R.color.driver_c9caca));
        parent.addView(driver, new LayoutParams(
                LayoutParams.WRAP_CONTENT, 1));
    }

    void MenuShow(View v) {
        if (null != v) {
            // boolean mid = (v == midll);
            // if (mid) {
            // ppw = new PopupWindow(v, sW - 140, LayoutParams.WRAP_CONTENT);
            // } else {
            ppw = new PopupWindow(v, sW / 3 + 100, LayoutParams.WRAP_CONTENT);
            // }
            ppw.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.nothing));
            ppw.setFocusable(true);
            ppw.setOutsideTouchable(true);
            // if (mid) {
            // ppw.showAsDropDown(findViewById(R.id.header), 70, 0);
            // } else {
            // ppw.showAsDropDown(v, 0, 10);
            ppw.showAtLocation(v, Gravity.RIGHT | Gravity.TOP, 0, getWindow()
                    .findViewById(Window.ID_ANDROID_CONTENT).getTop());
            // }
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
    }

    public void showTip(String text) {
        T.show(getApplicationContext(), text);
    }
}
