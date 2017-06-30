package com.datacomo.mc.spider.android.thread;

import android.content.Context;
import android.os.Message;

import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.GroupLeaguerPermissionBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceBean;

public class AsyncBeanDownLoad extends AsyncDownLoad {
	private static final String TAG = "AsynBeanDownLoad";
	protected String[] mParams;
	protected Context mContext;
	protected String mObjectType;
	private final String TYPE_MORE = "type_more";

	/**
	 * 
	 * @param ids
	 * @param tags
	 * @param context
	 * @param beanCallback
	 */
	public AsyncBeanDownLoad(String[] params, String[] tags, String objectType,
			Context context, BeanCallback beanCallback) {
		super(tags, beanCallback);
		this.mParams = params;
		this.mContext = context;
		this.mObjectType = objectType;
	}

	public void downLoadBean() {
		download(mParams);
	}

	@Override
	protected void download(final Object params) {
		new Thread() {
			@Override
			public void run() {
				try {
					MCResult results[] = loadResultFromId(mParams);
					if (results[0].getResultCode() == 1) {
						Object bean = results[0].getResult();
						if (null != results[1]
								&& results[1].getResultCode() == 1) {
							String leaguerStatus = ((GroupLeaguerPermissionBean) results[1]
									.getResult()).getLeaguerStatus();
							if ("6".equals(leaguerStatus)
									|| "NO_RELATION".equals(leaguerStatus)
									|| "COOPERATION_LEAGUER"
											.equals(leaguerStatus))
								((ResourceBean) bean).setHasAuthority(false);
							else
								((ResourceBean) bean).setHasAuthority(true);

						}
						Message message = getHandler().obtainMessage(0, bean);
						getHandler().sendMessage(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	/**
	 * 联网下载信息
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public MCResult[] loadResultFromId(String[] params) throws Exception {
		MCResult[] results = new MCResult[2];
		results[0] = APIRequestServers.resourceInfo(mContext, mParams[1],
				mParams[0], mObjectType);
		if (mParams[2].equals(TYPE_MORE))
			results[1] = APIRequestServers.leaguerPermissionInfo(mContext,
					mParams[1]);
		return results;
	}

	/**
	 * 回调借口
	 * 
	 * @author no 287
	 * 
	 */
	public interface BeanCallback extends Callback {
	}

	@Override
	protected Object get() {
		return null;
	};

}
