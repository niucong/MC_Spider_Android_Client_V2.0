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

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.util.BaseData;

public class SearchBar2 extends LinearLayout implements OnClickListener {
	private Context mContext;
	private EditText edit;
	private ImageView delete;
	private Button img;
	private OnSearchListener searchListener;
	private OnClearListener clearListener;
	private TextWatcher textWatcher = new TextWatcher() {

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
			if (!"".equals(s.toString())) {
				delete.setVisibility(View.VISIBLE);
			} else {
				delete.setVisibility(View.GONE);
			}
			if (null != searchListener) {
				searchListener.onSearch(getKeyWords());
			}
		}
	};

	public SearchBar2(Context context) {
		super(context);
		mContext = context;
		addViews();
	}

	public SearchBar2(Context context, AttributeSet attr) {
		super(context, attr);
		mContext = context;
		addViews();
	}

	private boolean isOnKey;

	@SuppressWarnings("deprecation")
	private void addViews() {
		LayoutParams ll = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout content = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.layout_search_bar, null);
		addView(content, ll);
		edit = (EditText) content.findViewById(R.id.search_text);
		img = (Button) content.findViewById(R.id.search);
		img.setVisibility(View.GONE);
		delete = (ImageView) content.findViewById(R.id.delete);
		delete.setOnClickListener(this);

		edit.addTextChangedListener(textWatcher);
		edit.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER && isOnKey) {
					if (!"".equals(getKeyWords())) {
						if (null != searchListener) {
							searchListener.onSearch(getKeyWords());
						}
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

	public void setOnsearchListener(OnSearchListener onSearchListener) {
		searchListener = onSearchListener;
	}

	public void setOnClearListener(OnClearListener listener) {
		clearListener = listener;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.delete:
			edit.setText("");
			BaseData.hideKeyBoard((Activity) mContext);
			if (null != clearListener) {
				clearListener.onClear(getKeyWords());
			}
			break;
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

	// public void setEnableInput(boolean enable) {
	// edit.setEnabled(enable);
	// }
	public EditText getEditText() {
		return edit;
	}

	public TextWatcher getTextWatcher() {
		return textWatcher;
	}

	public void setKeyWords(String string) {
		edit.setText("");
	}
}