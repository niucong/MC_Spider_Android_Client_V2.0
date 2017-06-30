package com.datacomo.mc.spider.android.xmpp;
///**
// * The contents of this file are subject to the terms
// * of the Common Development and Distribution License
// * (the License). You may not use this file except in
// * compliance with the License.
// *
// * Copyright 2010-2014 Sharesin Communications Technology INC.
// * 
// * This source file is a part of XMPPUtil project. 
// * date: 2014-8-4
// *
// */
//package com.datacomo.mc.spider.android.xmpp;
//
//import org.jivesoftware.smack.ConnectionConfiguration;
//import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
//import org.jivesoftware.smack.PacketListener;
//import org.jivesoftware.smack.SmackException.NotConnectedException;
//import org.jivesoftware.smack.XMPPConnection;
//import org.jivesoftware.smack.filter.PacketFilter;
//import org.jivesoftware.smack.packet.Message;
//import org.jivesoftware.smack.packet.Message.Type;
//import org.jivesoftware.smack.packet.Packet;
//import org.jivesoftware.smack.tcp.XMPPTCPConnection;
//import org.json.JSONObject;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.datacomo.mc.spider.android.QuuChatActivity;
//import com.datacomo.mc.spider.android.R;
//import com.datacomo.mc.spider.android.application.App;
//import com.datacomo.mc.spider.android.db.ChatGroupMessageBeanService;
//import com.datacomo.mc.spider.android.db.GroupListService;
//import com.datacomo.mc.spider.android.net.been.MessageNoticeBean;
//import com.datacomo.mc.spider.android.net.been.groupchat.GroupChatMessageBean;
//import com.datacomo.mc.spider.android.net.been.groupchat.ObjectInfoBean;
//import com.datacomo.mc.spider.android.receiver.BootBroadcastReceiver;
//import com.datacomo.mc.spider.android.url.L;
//import com.datacomo.mc.spider.android.url.URLProperties;
//import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
//import com.datacomo.mc.spider.android.util.JsonParseTool;
//
///**
// * 
// * @author zhujigao
// * @date 2014-8-4 上午9:49:05
// * @update developer zhujigao
// * @update date 2014-8-4 上午9:49:05
// * @version v1.0.0
// */
//public class XMPPManager {
//	private final static String TAG = "XMPPManager";
//
//	private static ConnectionConfiguration config = null;
//	private static XMPPConnection connection;
//
//	static {
//		try {
//			Class.forName("org.jivesoftware.smack.ReconnectionManager");
//		} catch (Exception e1) {
//		}
//		config = new ConnectionConfiguration(URLProperties.OPENFIRE_URL,
//				URLProperties.OPENFIRE_PORT);
//		config.setReconnectionAllowed(true);
//		config.setCompressionEnabled(true);
//		config.setSecurityMode(SecurityMode.disabled);
//		// 是否启用安全验证
//		// config.setSASLAuthenticationEnabled(false);
//		// 是否启用调试
//		config.setDebuggerEnabled(true);
//
//		config.setRosterLoadedAtLogin(true);
//		config.setSendPresence(true);
//	}
//
//	public static XMPPConnection getConnection() {
//		if (connection == null) {
//			connection = new XMPPTCPConnection(config);
//			try {
//				connection.connect();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		notificationManager = (NotificationManager) App.app
//				.getSystemService(Context.NOTIFICATION_SERVICE);// 初始化管理器
//		notification = new Notification();
//
//		return connection;
//	}
//
//	private static NotificationManager notificationManager = null;
//	private static Notification notification = null;
//
//	public static void addListener(final XMPPConnection conn, final Context c) {
//
//		final int mId = GetDbInfoUtil.getMemberId(c);
//		conn.addPacketListener(new PacketListener() {
//
//			@SuppressWarnings("deprecation")
//			@Override
//			public void processPacket(Packet p) throws NotConnectedException {
//				if (p instanceof Message) {
//					try {
//						Message m = (Message) p;
//						L.d(TAG,
//								"addListener From=" + m.getFrom() + ",To="
//										+ m.getTo() + ",Body=" + m.getBody()
//										+ ",Type=" + m.getType() + ",myId="
//										+ mId);
//
//						String from = m.getFrom();
//						// String to = m.getTo();
//						String msg = "";
//						Type type = m.getType();
//						String contentTitle = "优优工作圈";
//						String body = m.getBody();
//						JSONObject json = new JSONObject(body);
//						// TODO
//						try {
//							App.app.share.saveLongMessage("program",
//									"startTime", json.getLong("SERVER_TIME"));
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						if (Type.chat == type) {
//							MessageNoticeBean mnb = (MessageNoticeBean) JsonParseTool
//									.dealComplexResult(body,
//											MessageNoticeBean.class);
//
//							// contentTitle = from.substring(0,
//							// from.indexOf("@"));
//							// String MESSAGE_TYPE = json
//							// .getString("MESSAGE_TYPE");
//							// if ("1".equals(MESSAGE_TYPE)) {
//							// final int gid = json.getInt("GROUP_ID");
//							// L.i(TAG, "addListener gid=" + gid
//							// + ",MESSAGE_TYPE=" + MESSAGE_TYPE);
//							// new Thread() {
//							// public void run() {
//							// XMPPAPI.getXmppapi().enterGroupChat(
//							// mId + "",
//							// new String[] { gid + "" }, c);
//							// };
//							// }.start();
//							// }
//						} else if (Type.groupchat == type) {
//							GroupChatMessageBean bean = (GroupChatMessageBean) JsonParseTool
//									.dealComplexResult(
//											json.getString("MESSAGE"),
//											GroupChatMessageBean.class);
//							try {
//								JSONObject time = new JSONObject(bean
//										.getCreateTime());
//								App.app.share.saveLongMessage("program",
//										"startTime", time.getLong("time"));
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//							if (bean.getSendMemberId() == mId) {
//								return;
//							}
//
//							contentTitle = json.getString("GROUP_NAME");
//
//							int gid = json.getInt("GROUP_ID");
//							ObjectInfoBean ob = bean.getMessageList().get(0);
//							if (ob.getMessageType() == 1) {
//								msg += bean.getSendMemberName() + ":";
//								String ot = ob.getObjectType();
//								if ("OBJ_TEXT".equals(ot)) {
//									body = ob.getMessageContent();
//								} else if ("OBJ_PHOTO".equals(ot)) {
//									body = "分享照片";
//								} else if ("OBJ_VOICE".equals(ot)) {
//									body = "分享语音";
//								}
//							}
//							msg += body;
//
//							L.i(TAG, "addListener contentTitle=" + contentTitle
//									+ ",contentText=" + msg);
//							L.d(TAG,
//									"addListener group_chat_unread="
//											+ App.app.share.getIntMessage(
//													"group_chat_unread", gid
//															+ "", 0));
//							App.app.share.saveIntMessage(
//									"group_chat_unread",
//									gid + "",
//									App.app.share.getIntMessage(
//											"group_chat_unread", gid + "", 0) + 1);
//							L.i(TAG,
//									"addListener group_chat_unread="
//											+ App.app.share.getIntMessage(
//													"group_chat_unread", gid
//															+ "", 0));
//
//							Intent gIntent = new Intent(
//									BootBroadcastReceiver.QUUCHAT);
//							gIntent.putExtra("Chat", bean);
//							gIntent.putExtra("gId", gid);
//							gIntent.putExtra("gName", contentTitle);
//							c.sendBroadcast(gIntent);
//							ChatGroupMessageBeanService.getService(App.app)
//									.saveSend(bean, gid + "");
//
//							Intent messageIntent = new Intent(c,
//									QuuChatActivity.class);
//							Bundle b = new Bundle();
//							b.putString("name", contentTitle);
//							b.putString("memberId", gid + "");
//							b.putString("url", GroupListService.getService(c)
//									.queryGroupHead(gid + ""));
//							b.putBoolean("isManager", false);
//							b.putInt("num", 0);
//							messageIntent.putExtras(b);
//							PendingIntent messagePendingIntent = PendingIntent
//									.getActivity(c, 0, messageIntent,
//											PendingIntent.FLAG_UPDATE_CURRENT);
//							notification.setLatestEventInfo(App.app,
//									contentTitle, msg, messagePendingIntent);
//						} else if (Type.normal == type) {
//							// TODO
//							body = "系统消息";
//							msg += body;
//							notification.setLatestEventInfo(App.app,
//									contentTitle, msg, null);
//						}
//						notification.icon = R.drawable.icon;
//						// notificationManager.cancel(null, 0);
//						notificationManager.notify(0, notification);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}, new PacketFilter() {
//
//			@Override
//			public boolean accept(Packet arg0) {
//				return true;
//			}
//		});
//
//		// XMPPConnection
//		// .removeConnectionCreationListener(new ConnectionCreationListener() {
//		//
//		// @Override
//		// public void connectionCreated(XMPPConnection con) {
//		// int myId = GetDbInfoUtil.getMemberId(c);
//		// XMPPAPI.enterChat(myId + "", myId + "", c);
//		// }
//		// });
//		//
//		// MultiUserChat.addInvitationListener(conn, new InvitationListener() {
//		// @Override
//		// public void invitationReceived(XMPPConnection conn, String room,
//		// String inviter, String reason, String password,
//		// Message message) {
//		// L.i(TAG, "addInvitationListener room=" + room + ",reason="
//		// + reason);
//		// L.d(TAG, "addInvitationListener inviter=" + inviter
//		// + ",password=" + password);
//		// }
//		// });
//
//	}
//}
