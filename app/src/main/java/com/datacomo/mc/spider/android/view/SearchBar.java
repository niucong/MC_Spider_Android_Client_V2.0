package com.datacomo.mc.spider.android.view;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;

public class SearchBar extends LinearLayout implements OnClickListener {
	protected static final String TAG = "SearchBar";

	private Context mContext;
	private EditText edit;
	private ImageView delete;
	private Button img;
	private RelativeLayout input;

	private OnSearchListener searchListener;
	private OnClearListener clearListener;

	private boolean isOnKey;

	// 输入时自动匹配搜索
	private boolean autoSearch = false;

	public void setAutoSearch(boolean autoSearch) {
		this.autoSearch = autoSearch;
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (s.toString().trim().length() > 0) {
				delete.setVisibility(View.VISIBLE);
				if (autoSearch) {
					if (null != searchListener)
						searchListener.onSearch(s.toString());
				}
			} else {
				delete.setVisibility(View.GONE);
				if (null != clearListener) {
					clearListener.onClear(s.toString());
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	public SearchBar(Context context) {
		super(context);
		mContext = context;
		addViews();
	}

	public SearchBar(Context context, AttributeSet attr) {
		super(context, attr);
		mContext = context;
		addViews();
	}

	private void addViews() {
		LayoutParams ll = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LinearLayout content = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.layout_search_bar, null);
		addView(content, ll);

		input = (RelativeLayout) content.findViewById(R.id.search_input);
		edit = (EditText) content.findViewById(R.id.search_text);
		img = (Button) content.findViewById(R.id.search);
		img.setVisibility(View.GONE);
		img.setOnClickListener(this);
		delete = (ImageView) content.findViewById(R.id.delete);
		delete.setOnClickListener(this);

		edit.addTextChangedListener(textWatcher);
		edit.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				L.i(TAG, "keyCode=" + keyCode + ",isOnKey=" + isOnKey);
				if (keyCode == KeyEvent.KEYCODE_ENTER && isOnKey) {
					L.d(TAG, "KeyWords=" + getKeyWords());
					if (!"".equals(getKeyWords())) {
						onSearch();
					} else {
						BaseData.hideKeyBoard((Activity) mContext);
					}
					isOnKey = false;
				} else {
					isOnKey = true;
				}
				return false;
			}
		});
		// setEnableInput(false);
	}

	public void changeInputBg() {
		input.setBackgroundResource(R.drawable.menu_search_input);
		input.setPadding(10, 3, 10, 0);
	}

	public void setOnSearchListener(OnSearchListener onSearchListener) {
		searchListener = onSearchListener;
	}

	public void setOnClearListener(OnClearListener listener) {
		clearListener = listener;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search:
			onSearch();
			break;
		case R.id.delete:
			edit.setText("");
			BaseData.hideKeyBoard((Activity) mContext);
			break;
		}
	}

	private void onSearch() {
		if (null != searchListener) {
			searchListener.onSearch(getKeyWords());
			BaseData.hideKeyBoard((Activity) mContext);
		}
	}

	public interface OnSearchListener {
		public void onSearch(String keyWords);
	}

	public interface OnClearListener {
		public void onClear(String keyWords);
	}

	public String getKeyWords() {
		String ss = edit.getText().toString();
		if (null == ss) {
			ss = "";
		}
		return ss;
	}

	public void setKeySize(float size) {
		edit.setTextSize(size);
	}

	public void setKeyHint(String keyHint) {
		edit.setHint(keyHint);
	}

	// public void setSearchImg(Drawable drawable) {
	// img.setImageDrawable(drawable);
	// }

	// public void setEnableInput(boolean enable) {
	// edit.setEnabled(enable);
	// }
	public EditText getEditText() {
		return edit;
	}

	// public ImageView getSearchButton() {
	// return img;
	// }

	public TextWatcher getTextWatcher() {
		return textWatcher;
	}

	public void setKeyWords(String keyStr) {
		if (keyStr != null) {
			edit.setText(keyStr);
			edit.setSelection(keyStr.length());
		}
	}

	public void setHint(String hintStr) {
		edit.setHint(hintStr);
	}

	public void clearText() {
		edit.setText("");
	}
}