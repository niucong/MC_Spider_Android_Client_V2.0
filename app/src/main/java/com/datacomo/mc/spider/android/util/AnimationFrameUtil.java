package com.datacomo.mc.spider.android.util;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.view.GifDecode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * gif图片播放帮助类
 *
 * @author Administrator
 *
 */
public class AnimationFrameUtil {

    private static Bitmap[] mBitmap = null;
    public static boolean flag = false;

    public static AnimationDrawable getFrame(String filePath,
                                             ImageView mImageView, int position) {
        AnimationDrawable mAnimationDrawable = new AnimationDrawable();
        L.d("gifload", "start");
        try {
            GifDecode mGifDecode = new GifDecode();
            FileInputStream mFileInputStream = null;
            try {
                L.d("gifload", "loadfile " + filePath);
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
            mBitmap = new Bitmap[mFrameCount];
            int w = 0, h = 0;
            int[] mDelay = new int[mFrameCount];
            L.d("gifload", "loading+" + mFrameCount);
            if (mFrameCount <= 0)
                return null;
            for (int i = 0; i < mFrameCount; i++) {
                L.d("gifload", "loading");
                mBitmap[i] = mGifDecode.getFrame(i);
                mDelay[i] = mGifDecode.getDelay(i);
                w = Math.max(w, mBitmap[i].getWidth());
                h = Math.max(h, mBitmap[i].getHeight());
                mAnimationDrawable.addFrame(new BitmapDrawable(mBitmap[i]),
                        mDelay[i]);
            }
            L.d("gifload", "loadingend");
            mAnimationDrawable.setOneShot(false);// 设置播放模式是否循环，false循环，true不循环.
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
            L.d("gifload", "OutOfMemoryError");
            e.printStackTrace();
            mAnimationDrawable = null;
        }

        mImageView.setVisibility(View.VISIBLE);
        return mAnimationDrawable;
    }
}
