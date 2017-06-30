package com.datacomo.mc.spider.android.manager;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.CustomPlayer;
import com.datacomo.mc.spider.android.util.DBUtil;
import com.datacomo.mc.spider.android.util.DateUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.CustomPlayer.OnCustomCompletionListener;

public class MediaManager {
	private static final String LOG_TAG = "MediaManager";
	private static final String SUFFIX = ".amr";
	private static final int MAXTIME = 60;
	private static final int PLAY = 1;
	private static final int STOP = 0;
	private static int state;
	private static final int MINTIME = 1;
	private static final int LASTTIME = 10;
	private static final int BASE = 600;
	private static final int SPACE = 70;// 間隔取样時間
	private static String voicePath;
	private static int second;
	// 引用类
	private static MediaRecorder mMediaRecorder;
	private static Runnable mUpdateMicStatusTimer;
	private static CustomPlayer player;
	private static Timer timer;
	private static MicStatus mMicStatus;
	private final static Handler mHandler = new Handler();

	/**
	 * start record audio
	 * 
	 * @param context
	 * @param handler
	 */
	public static void startRecord(final Context context, final Handler handler) {
		Log.d(LOG_TAG, "start");
		// voiceNum++;
		state = PLAY;
		boolean sdcardExit = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		try {
			if (!sdcardExit) {
				T.show(context, "请插入SD card");
				return;
			}
			File saveFile = new File(ConstantUtil.TEMP_PATH);
			if (!saveFile.exists()) {
				Log.d(LOG_TAG, "create file" + ConstantUtil.TEMP_PATH);
				saveFile.mkdirs();
			}
			String mMinute1 = getTime();
			String name = null;
			voicePath = name = mMinute1 + SUFFIX;
			L.d(LOG_TAG, "fathername" + voicePath);
			// 创建音频文件
			File myRecAudioFile = new File(ConstantUtil.TEMP_PATH, name);
			mMediaRecorder = new MediaRecorder();
			// 设置录音为麦克风
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

			// 录音文件保存这里
			mMediaRecorder.setOutputFile(myRecAudioFile.getAbsolutePath());
			// mMediaRecorder.setMaxDuration(MAXTIME*1000);
			mMediaRecorder.prepare();
			mMediaRecorder.start();
			mMicStatus = new MicStatus();
			mUpdateMicStatusTimer = new UpdateMicStatusTimer(handler,
					mMicStatus);
			second = 0;
			mHandler.postDelayed(mUpdateMicStatusTimer, 0);
			TimerTask timerTask = new TimerTask() {

				@Override
				public void run() {
					second++;
					int diffValue = MAXTIME - second;
					if (diffValue < LASTTIME) {
						Message message = Message.obtain();
						message.what = 4;
						message.arg1 = diffValue;
						handler.sendMessage(message);
					}
					if (second >= MAXTIME) {
						L.d(LOG_TAG, "timeout" + second);
						handler.sendEmptyMessage(2);
						return;
					}
				}
			};
			timer = new Timer();
			timer.schedule(timerTask, 1000, 1000);
			L.d(LOG_TAG, "stateout" + state);
			if (state == PLAY) {
				L.d(LOG_TAG, "state" + state);
				handler.sendEmptyMessage(0);
			} else {
				timer.cancel();
				timer = null;
			}

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	private static String getTime() {
		return DateUtil.getDateTime("yyyyMMddHHmmss");
	}

	/**
	 * stop current record audio
	 * 
	 * @param context
	 * @return object[0] is path object[1] is time object[1] is true the has
	 *         stop else other
	 */
	public static Object[] stopRecord() {
		L.d(LOG_TAG, "second" + second);
		L.d(LOG_TAG, "stop");
		String path = null;
		boolean hasStop = true;
		path = ConstantUtil.TEMP_PATH + voicePath;
		if (second < MINTIME) {
			Log.d(LOG_TAG, "a lot time");
			if (second != -1) {
				// 停止录音
				if (null != mMediaRecorder) {
					mMediaRecorder.stop();
					mMediaRecorder.release();
					mMediaRecorder = null;
				}
			}
			File myRecAudioFile = new File(path);
			if (myRecAudioFile.exists()) {
				myRecAudioFile.delete();
			}
			if (null != timer) {
				timer.cancel();
				timer = null;
			}
			mMicStatus.isStop = true;
			state = STOP;
			return null;
		}
		if (mMediaRecorder != null) {
			Log.d(LOG_TAG, "stop");
			// 停止录音
			mMediaRecorder.stop();
			mMediaRecorder.release();
			mMediaRecorder = null;
			hasStop = false;
		}
		if (null != timer) {
			timer.cancel();
			timer = null;
		}
		mMicStatus.isStop = true;
		state = STOP;
		return new Object[] { path, second, hasStop };
	}

	/**
	 * stop current record audio
	 * 
	 * @param context
	 * @return object[0] is path object[1] is time object[1] is true the has
	 *         stop else other
	 */
	public static void Release() {
		// 停止录音
		if (state!=STOP) {
			L.d(LOG_TAG, "Release");
			String path = ConstantUtil.TEMP_PATH + voicePath;
			if (null != mMediaRecorder) {
				mMediaRecorder.stop();
				mMediaRecorder.release();
				mMediaRecorder = null;
			}
			File myRecAudioFile = new File(path);
			if (myRecAudioFile.exists()) {
				myRecAudioFile.delete();
			}
			if (null != timer) {
				timer.cancel();
				timer = null;
			}
			mMicStatus.isStop = true;
			state = STOP;
		}
	}

	/**
	 * init
	 */
	public static void init() {
		second = -1;
	}

	private static class MicStatus {
		private int currentDB = 0;;
		private int previousDB = 0;
		public boolean isStop = false;

		private void updateMicStatus(Handler handler) {
			if (mMediaRecorder != null) {
				// int vuSize = 10 * mMediaRecorder.getMaxAmplitude() / 32768;
				int ratio = mMediaRecorder.getMaxAmplitude() / BASE;
				int db = 0;// 分貝
				if (ratio > 1)
					db = (int) (20 * Math.log10(ratio));
				L.d(LOG_TAG, "db" + " " + db);
				L.d(LOG_TAG, "db" + " " + db + " " + "db/4" + " " + db / 4);
				currentDB = db / 6;
				L.d(LOG_TAG, "previousDB" + " " + previousDB + " "
						+ "currentDB" + " " + currentDB);
				// int[] checkedDBs = checkDB(currentDB, previousDB);
				// List<Integer> dbs = getDBs(checkedDBs[0], checkedDBs[1]);
				if (currentDB > 5)
					currentDB = 5;
				else if (currentDB < 0)
					currentDB = 0;
				// previousDB = currentDB;
				Message message = Message.obtain();
				message.what = 3;
				// message.obj = dbs;
				message.arg1 = DBUtil.DB_RES_IDS[currentDB];
				handler.sendMessage(message);
				if (!isStop)
					mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
				else
					mMicStatus = null;

			}
		}
	}

	private static class UpdateMicStatusTimer implements Runnable {
		private Handler mHandler;
		private MicStatus mMicStatus;

		public UpdateMicStatusTimer(Handler handler, MicStatus micStatus) {
			mHandler = handler;
			mMicStatus = micStatus;
		}

		public void run() {
			mMicStatus.updateMicStatus(mHandler);
		}
	}

	/**
	 * play audio by specified path
	 * 
	 * @param path
	 * @param context
	 */
	public static void playAudio(String path, Context context) {
		if (null == path || "".equals(path)) {
			return;
		}
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		Uri uri = Uri.fromFile(file);
		L.d(LOG_TAG, uri.toString());
		player.play(context, uri, false, AudioManager.STREAM_MUSIC);
	}

	public static void setOnCustomCompletionListener(
			OnCustomCompletionListener onCustomCompletionListener) {
		CustomPlayer.setOnCustomCompletionListener(onCustomCompletionListener);
	}

	/**
	 * stop current audio
	 */
	public static void stopAudio() {
		player.stop();
	}

	public static void initPlayer() {
		player = new CustomPlayer(LOG_TAG);
	}

}
