package com.datacomo.mc.spider.android.thread;

import android.os.Handler;
import android.os.Message;

abstract class AsyncDownLoad {
	private static final String TAG = "AsynDownLoad";
	private Callback callback;
	private Object[] mTags;

	/**
	 * get result
	 * 
	 * @return Object
	 */
	public Object getResult() {
		Object object = get();
		return object;
	}

	/**
	 * 
	 * @param tags
	 * @param callback
	 *            Is called when download finish;
	 */
	protected AsyncDownLoad(Object[] tags, Callback callback) {
		this.mTags = tags;
		this.callback = callback;
	}

	private Handler mhandler = new Handler() {
		public void handleMessage(Message message) {
			callback.load(message.obj, mTags);
		}
	};

	/**
	 * get
	 * 
	 * @return Object
	 */
	abstract protected Object get();

	/**
	 * download
	 * 
	 * @param params
	 *            Object
	 */
	abstract protected void download(final Object params);

	/**
	 * get handler
	 * 
	 * @return Handler
	 */
	protected Handler getHandler() {
		return mhandler;
	}

	/**
	 * 
	 * @return String[]
	 */
	protected Object[] getAllTags() {
		return mTags;
	}

	/**
	 * 
	 * @param tags
	 *            String[]
	 */
	protected void setAllTags(Object[] tags) {
		this.mTags = tags;
	}

	/**
	 * 
	 * @param index
	 * @return String
	 */
	protected Object getTag(int index) {
		return mTags[index];
	}

	/**
	 * 
	 * @param tags
	 */
	protected void setTags(Object... tags) {
		this.mTags = tags;
	}

	/**
	 * callback
	 * 
	 * @author no 287
	 * 
	 */
	protected interface Callback {
		public void load(Object object, Object[] tags);
	}

}
