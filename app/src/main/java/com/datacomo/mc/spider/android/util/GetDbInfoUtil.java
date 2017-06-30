package com.datacomo.mc.spider.android.util;

import android.content.Context;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MemberBasicBean;
import com.datacomo.mc.spider.android.net.been.MemberBean;
import com.datacomo.mc.spider.android.net.been.MemberHeadBean;

public class GetDbInfoUtil {
	/**
	 * 获取用户Id
	 * 
	 * @param context
	 * @return
	 */
	public static int getMemberId(Context context) {
		try {
			String id_Member = null;
			UserBusinessDatabase db_UserBusiness = new UserBusinessDatabase(
					context);
			id_Member = db_UserBusiness.getMemberId(App.app.share
					.getSessionKey());
			return Integer.valueOf(id_Member);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 保存memberId、memberName、MemberHead
	 * 
	 * @param context
	 * @param session_key
	 * @param business
	 */
	public static void updateUserInfo(final Context context,
			final String session_key, final UserBusinessDatabase business) {
		new Thread() {
			public void run() {
				try {
					MCResult mcResult = APIRequestServers.getMemberBasicInfo(
							context, "0");
					if (mcResult.getResultCode() == 1 && session_key != null
							&& !"".equals(session_key)) {
						MemberBean memberBean = (MemberBean) mcResult
								.getResult();
						int memberId = memberBean.getMemberId();
						business.updateMemberId(session_key, memberId + "");
						MemberBasicBean basicInfo = memberBean.getBasicInfo();
						String name = basicInfo.getMemberName();
						business.updateName(session_key, name);
						MemberHeadBean mhb = basicInfo.getHeadImage();
						business.updateHeadUrlPath(session_key,
								mhb.getHeadUrl() + mhb.getHeadPath());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
}
