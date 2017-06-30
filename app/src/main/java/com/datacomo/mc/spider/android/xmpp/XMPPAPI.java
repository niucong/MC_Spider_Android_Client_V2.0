package com.datacomo.mc.spider.android.xmpp;
//package com.datacomo.mc.spider.android.xmpp;
//
//import java.io.IOException;
//import java.util.List;
//
//import org.jivesoftware.smack.ConnectionListener;
//import org.jivesoftware.smack.SmackException;
//import org.jivesoftware.smack.XMPPConnection;
//import org.jivesoftware.smack.XMPPException;
//import org.jivesoftware.smackx.muc.DefaultUserStatusListener;
//import org.jivesoftware.smackx.muc.DiscussionHistory;
//import org.jivesoftware.smackx.muc.MultiUserChat;
//
//import android.content.Context;
//import android.content.Intent;
//
//import com.datacomo.mc.spider.android.application.App;
//import com.datacomo.mc.spider.android.db.MessageNoticeBeanService;
//import com.datacomo.mc.spider.android.net.APIRequestServers;
//import com.datacomo.mc.spider.android.net.been.MCResult;
//import com.datacomo.mc.spider.android.net.been.MessageContacterBean;
//import com.datacomo.mc.spider.android.net.been.MessageNoticeBean;
//import com.datacomo.mc.spider.android.net.been.map.MapMessageNoticeBean;
//import com.datacomo.mc.spider.android.receiver.BootBroadcastReceiver;
//import com.datacomo.mc.spider.android.url.L;
//import com.datacomo.mc.spider.android.url.URLProperties;
//import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
//
//public class XMPPAPI {
//
//	private final String TAG = "XMPPAPI";
//
//	private static XMPPAPI xmppapi;
//
//	private XMPPAPI() {
//
//	}
//
//	public static XMPPAPI getXmppapi() {
//		if (xmppapi == null)
//			xmppapi = new XMPPAPI();
//		return xmppapi;
//	}
//
//	// public static HashMap<String, MultiUserChat> mucs = new HashMap<String,
//	// MultiUserChat>();
//
//	public void enterGroupChat(final String mId, String[] ids, final Context c) {
//		if (ids == null) {
//			new Thread() {
//				public void run() {
//					try {
//						long startTime = App.app.share.getLongMessage(
//								"program", "startTime", 0);
//						MCResult mcResult = APIRequestServers.myGroupChatList(
//								App.app, "0", "20", "false", startTime);
//						@SuppressWarnings("unchecked")
//						List<Object> beans = (List<Object>) mcResult
//								.getResult();
//						String[] idss = new String[beans.size()];
//						int[] newNums = new int[beans.size()];
//						for (int i = 0; i < idss.length; i++) {
//							MessageContacterBean bean = (MessageContacterBean) beans
//									.get(i);
//							idss[i] = bean.getContacterId() + "";
//							newNums[i] = bean.getNewMessageNum();
//						}
//						joinGroup(mId, idss, newNums, c);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();
//		} else {
//			joinGroup(mId, ids, null, c);
//		}
//	}
//
//	/**
//	 * 加入聊天室
//	 * 
//	 * @param con
//	 * @param mId
//	 * @param ids
//	 */
//	private void joinGroup(String mId, String[] ids, int[] nums, final Context c) {
//		// String id = "9921";
//		for (int i = 0; i < ids.length; i++) {
//			L.d(TAG, "joinGroup id=" + ids[i] + ",mId=" + mId);
//
//			// MultiUserChat muc = mucs.get(id);
//			// if (muc == null) {
//			XMPPConnection con = XMPPManager.getConnection();
//			final MultiUserChat muc = new MultiUserChat(con, ids[i]
//					+ URLProperties.OPENFIRE_SERVER);
//			// muc.isJoined();
//			try {
//				DiscussionHistory history = new DiscussionHistory();
//				if (nums != null && nums.length > i) {
//					history.setMaxStanzas(nums[i]);
//				} else {
//					history.setMaxStanzas(0);
//				}
//				muc.join(mId, null, history, con.getPacketReplyTimeout());
//
//				// 监听自己的状态变更和事件
//				muc.addUserStatusListener(new DefaultUserStatusListener() {
//					@Override
//					public void voiceRevoked() {
//						super.voiceRevoked();
//						L.d(TAG, "voiceRevoked你被禁言了!");
//					}
//
//					@Override
//					public void voiceGranted() {
//						super.voiceGranted();
//						L.d(TAG, "voiceGranted你被批准发言了!");
//					}
//
//					@Override
//					public void membershipGranted() {
//						super.membershipGranted();
//						L.d(TAG, "membershipGranted...");
//					}
//
//					@Override
//					public void membershipRevoked() {
//						super.membershipRevoked();
//						String room = muc.getRoom();
//						L.d(TAG,
//								"membershipRevoked...Nickname="
//										+ muc.getNickname() + ",Room=" + room);
//						Intent gIntent = new Intent(
//								BootBroadcastReceiver.QUUCHAT_REMOVE);
//						int gid = Integer.valueOf(room.substring(0,
//								room.indexOf("@")));
//						gIntent.putExtra("gId", gid);
//						c.sendBroadcast(gIntent);
//						L.d(TAG, "membershipRevoked...gid=" + gid);
//					}
//
//					@Override
//					public void adminGranted() {
//						super.adminGranted();
//						L.d(TAG, "adminGranted你被赋予了管理员权限");
//					}
//
//					@Override
//					public void adminRevoked() {
//						super.adminRevoked();
//						L.d(TAG, "adminRevoked你被解除了管理员权限");
//					}
//				});
//
//				// mucs.put(id, muc);
//				L.d(TAG, "joinGroup isJoined=" + muc.isJoined());
//			} catch (Exception e) {
//				L.v(TAG, "joinGroup Exception...");
//				e.printStackTrace();
//			}
//			// } else {
//			// L.i(TAG, "joinGroup isJoined=" + muc.isJoined());
//			// }
//		}
//	}
//
//	/**
//	 * 离开聊天室
//	 * 
//	 * @param con
//	 * @param id
//	 */
//	public void leaveGroup(final String id, Context c) {
//		XMPPConnection con = XMPPManager.getConnection();
//		String user = con.getUser();
//		L.d(TAG, "leaveGroup id=" + id + ",user=" + user);
//		String jid;
//		if (user == null) {
//			jid = GetDbInfoUtil.getMemberId(c) + "";
//			enterChat(jid, jid, c);
//		} else {
//			jid = user.substring(0, user.indexOf("@"));
//			try {
//				// con.login("admin", "mc_spider_openfire", "android");
//				// MultiUserChat muc = new MultiUserChat(con, id
//				// + URLProperties.OPENFIRE_SERVER);
//				// muc.revokeAdmin(jid);
//				// muc.revokeMembership(con.getUser());
//				// muc.leave();
//				// con.login(user, user, "android");
//
//				con.disconnect();
//				enterChat(user, user, c);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		L.d(TAG, "leaveGroup jid=" + jid);
//
//		// L.d(TAG, "leaveGroup id=" + id + ",size=" + mucs.size());
//		// try {
//		// MultiUserChat muc = mucs.get(id);
//		// if (muc != null) {
//		// L.d(TAG, "leaveGroup isJoined=" + muc.isJoined());
//		// muc.leave();
//		// L.i(TAG, "leaveGroup isJoined=" + muc.isJoined());
//		// mucs.remove(id);
//		// } else {
//		// L.d(TAG, "leaveGroup muc=null");
//		// }
//		// } catch (Exception e) {
//		// L.v(TAG, "leaveGroup Exception...");
//		// e.printStackTrace();
//		// }
//	}
//
//	public void sendGroupMessage(String id, String msg) {
//		XMPPConnection con = XMPPManager.getConnection();
//
//		MultiUserChat muc = new MultiUserChat(con, id
//				+ URLProperties.OPENFIRE_SERVER);
//		String jid = GetDbInfoUtil.getMemberId(App.app) + "";
//		try {
//			DiscussionHistory history = new DiscussionHistory();
//			history.setMaxChars(0);
//			muc.join(jid, null, history, con.getPacketReplyTimeout());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void enterChat(final String mId, String psd, final Context c) {
//		L.d(TAG, "enterChat mId=" + mId + ",psd=" + psd);
//
//		String session_key = App.app.share.getSessionKey();
//		if (session_key == null || "".equals(session_key)) {
//			return;
//		}
//
//		new Thread() {
//			public void run() {
//				try {
//					long time = App.app.share.getLongMessage("program",
//							"noticeTime", 0);
//					MCResult mcResult = APIRequestServers.newNotices(c, "0",
//							"20", time + "");
//					MapMessageNoticeBean map = (MapMessageNoticeBean) mcResult
//							.getResult();
//					List<MessageNoticeBean> list = null;
//					if (map != null)
//						list = map.getLIST();
//					if (list != null && list.size() > 0) {
//						MessageNoticeBeanService.getService(c).save(list);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			};
//		}.start();
//
//		try {
//			final XMPPConnection connection = XMPPManager.getConnection();
//			// if (!connection.isConnected())
//			connection.login(mId, psd, "android");//
//			L.d(TAG, "enterChat user=" + connection.getUser());
//
//			enterGroupChat(mId, null, c);
//			XMPPManager.addListener(connection, c);
//
//			connection.addConnectionListener(new ConnectionListener() {
//
//				@Override
//				public void reconnectionSuccessful() {
//					L.d(TAG, "reconnectionSuccessful...");
//					// XMPPManager.addListener(connection);
//				}
//
//				@Override
//				public void reconnectionFailed(Exception arg0) {
//					L.d(TAG, "reconnectionFailed...");
//					// enterChat();
//				}
//
//				@Override
//				public void reconnectingIn(int arg0) {
//					L.d(TAG, "reconnectingIn..." + arg0);
//				}
//
//				@Override
//				public void connectionClosedOnError(Exception arg0) {
//					L.d(TAG, "connectionClosedOnError...");
//				}
//
//				@Override
//				public void connectionClosed() {
//					L.d(TAG, "connectionClosed...");
//				}
//
//				@Override
//				public void connected(XMPPConnection con) {
//					L.d(TAG, "connected...");
//				}
//
//				@Override
//				public void authenticated(XMPPConnection con) {
//					L.d(TAG, "authenticated...");
//					// mucs.clear();
//					enterGroupChat(mId, null, c);
//				}
//			});
//		} catch (SmackException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (XMPPException e) {
//			e.printStackTrace();
//		}
//	}
//
//}
