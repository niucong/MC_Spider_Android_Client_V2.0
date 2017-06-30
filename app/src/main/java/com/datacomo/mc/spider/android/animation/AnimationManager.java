package com.datacomo.mc.spider.android.animation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.datacomo.mc.spider.android.view.GifDecode;

public class AnimationManager {

	public static Animation[] getArrowRotateUp() {
		Animation[] anims = new Animation[2];
		anims[0] = new RotateAnimation(0.0f, -90.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		anims[0].setInterpolator(new LinearInterpolator());
		anims[0].setFillAfter(true);
		anims[0].setDuration(250);
		anims[1] = new RotateAnimation(-90.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		anims[1].setInterpolator(new LinearInterpolator());
		anims[1].setFillAfter(true);
		anims[1].setDuration(250);
		return anims;
	}

	public static Animation[] getArrowRotateDown() {
		Animation[] anims = new Animation[2];
		anims[0] = new RotateAnimation(0.0f, 90.0f, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		anims[0].setInterpolator(new LinearInterpolator());
		anims[0].setFillAfter(true);
		anims[0].setDuration(250);
		anims[1] = new RotateAnimation(90.0f, 0.0f, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		anims[1].setInterpolator(new LinearInterpolator());
		anims[1].setFillAfter(true);
		anims[1].setDuration(250);
		return anims;
	}

	/**
	 * 
	 * @return 0 is hide 1 is show
	 */
	public Animation[] getGroupBoxAnim() {
		Animation[] anims = new Animation[2];
		anims[0] = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anims[0].setInterpolator(new LinearInterpolator());
		anims[0].setDuration(200);
		anims[0].setFillAfter(true);
		anims[1] = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anims[1].setInterpolator(new LinearInterpolator());
		anims[1].setDuration(250);
		anims[1].setFillAfter(true);
		return anims;
	}

	@SuppressLint("NewApi")
	/**
	 * 0 is up 1 is down
	 * @param view
	 * @param time
	 * @return
	 */
	public static ObjectAnimator[] getArrowRotateAnim_180(View view,Long time) {
		ObjectAnimator[] objAnim =new 	ObjectAnimator[2];
		objAnim[0] = ObjectAnimator.ofFloat(view, "rotation",
				180f, 0f);
		objAnim[0].setDuration(time);
		objAnim[1] = ObjectAnimator.ofFloat(view, "rotation", 0f,
				180f);
		objAnim[1].setDuration(time);
		return objAnim;
	}

	@SuppressLint("NewApi")
	public static LayoutTransition getLayoutTransition() {
		LayoutTransition lTransticion = new LayoutTransition();
		Animator defaultAppearingAnim = lTransticion
				.getAnimator(LayoutTransition.APPEARING);
		Animator defaultDisappearingAnim = lTransticion
				.getAnimator(LayoutTransition.DISAPPEARING);
		Animator defaultChangingAppearingAnim = lTransticion
				.getAnimator(LayoutTransition.CHANGE_APPEARING);
		Animator defaultChangingDisappearingAnim = lTransticion
				.getAnimator(LayoutTransition.CHANGE_DISAPPEARING);
		lTransticion.setAnimator(LayoutTransition.APPEARING,
				defaultAppearingAnim);
		lTransticion.setAnimator(LayoutTransition.DISAPPEARING,
				defaultDisappearingAnim);
		lTransticion.setAnimator(LayoutTransition.CHANGE_APPEARING,
				defaultChangingAppearingAnim);
		lTransticion.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,
				defaultChangingDisappearingAnim);
		return lTransticion;
	}

	/**
	 * 
	 * @param filePath
	 * @param mImageView
	 * @param position
	 * @param isOneShot
	 * @return
	 */
	public static AnimationDrawable getGifFrame(String filePath,
			ImageView mImageView, int position, boolean isOneShot) {
		AnimationDrawable mAnimationDrawable = new AnimationDrawable();

		try {
			GifDecode mGifDecode = new GifDecode();
			FileInputStream mFileInputStream = null;
			try {
				mFileInputStream = new FileInputStream(filePath);
				mGifDecode.read(mFileInputStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (null != mFileInputStream) {
					try {
						mFileInputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			int mFrameCount = mGifDecode.getFrameCount();
			Bitmap[] bitmap = new Bitmap[mFrameCount];
			int w = 0, h = 0;
			int[] mDelay = new int[mFrameCount];
			for (int i = 0; i < mFrameCount; i++) {
				bitmap[i] = mGifDecode.getFrame(i);
				mDelay[i] = mGifDecode.getDelay(i);
				w = Math.max(w, bitmap[i].getWidth());
				h = Math.max(h, bitmap[i].getHeight());
				mAnimationDrawable.addFrame(new BitmapDrawable(bitmap[i]),
						mDelay[i]);
			}
			mAnimationDrawable.setOneShot(isOneShot);// 设置播放模式是否循环，false循环，true不循环.
			if (position == 1) {
				RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
						w, h);
				rlp.addRule(RelativeLayout.CENTER_IN_PARENT,
						RelativeLayout.TRUE);
				mImageView.setLayoutParams(rlp);
			} else if (position == 2) {
				LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
						w, h);
				rlp.gravity = Gravity.CENTER_HORIZONTAL;
				mImageView.setLayoutParams(rlp);
			}
			mImageView.setImageDrawable(mAnimationDrawable);// 加载显示动画.
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}

		mImageView.setVisibility(View.VISIBLE);
		return mAnimationDrawable;
	}

}
