package com.datacomo.mc.spider.android.task;
//package com.datacomo.mc.spider.android.task;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.widget.ImageView;
//
//import com.datacomo.mc.spider.android.thread.AsyncImageDownLoad;
//import com.datacomo.mc.spider.android.thread.BasicAsyncImageDownLoad.ImageCallback;
//import com.datacomo.mc.spider.android.util.ConstantUtil;
//import com.datacomo.mc.spider.android.util.ImageDealUtil;
//import com.datacomo.mc.spider.android.util.TaskUtil;
//
//public class PublicLoadPicture {
//
//	/**
//	 * 异步加载头像的方法。
//	 * 
//	 * @param head_Url
//	 *            头像的Url地址
//	 * @param imageView
//	 *            头像显示的组件
//	 * @return
//	 */
//
////	public static Drawable getDrawable(Context context, String head_Url,
////			final ImageView imageView, int r) {
////		return getDrawable(context, head_Url, imageView, r, false);
////	}
//
////	public static Drawable getDrawable(Context context, String head_Url,
////			final ImageView imageView, int r, final boolean roundCorner) {
////		// Drawable drawable = new AsyncImageLoader(head_Url, context,
////		// r,ConstantUtil.HEAD_PATH,
////		// new ImageCallback() {
////		// @Override
////		// public void imageLoaded(Drawable imageDrawable,
////		// String imageUrl) {
////		// if (roundCorner) {
////		// loadImg(ImageDealUtil.getPosterCorner(
////		// imageDrawable, 6), imageView);
////		// } else {
////		// loadImg(imageDrawable, imageView);
////		// }
////		// }
////		// }).loadDrawable();
////		Drawable drawable = new AsyncImageDownLoad(head_Url,
////				new String[] { head_Url }, TaskUtil.HEADDEFAULTLOADSTATEIMG,
////				ConstantUtil.HEAD_PATH, context, new ImageCallback() {
////
////					@Override
////					public void load(Object object, Object[] tags) {
////						Drawable imageDrawable = (Drawable) object;
////						if (roundCorner) {
////							loadImg(ImageDealUtil.getPosterCorner(
////									imageDrawable, 6), imageView);
////						} else {
////							loadImg(imageDrawable, imageView);
////						}
////					}
////				}).getDrawable();
////		return drawable;
////	}
//	
//	/**
//	 * 
//	 * @param context
//	 * @param head_Url
//	 * @param imageView
//	 * @param from which call the asyncdownload
//	 * @return
//	 */
//	public static Drawable loadHead(Context context, String head_Url,
//			final ImageView imageView,String from) {
//		return loadRoundHead(context, head_Url, imageView, 0, false,from);
//	}
//
//	/**
//	 * 
//	 * @param context
//	 * @param head_Url
//	 * @param imageView
//	 * @param round
//	 * @param roundCorner
//	 * @param from which call the asyncdownload
//	 * @return
//	 */
//	public static Drawable loadRoundHead(Context context, String head_Url,
//			final ImageView imageView, final int round,
//			final boolean roundCorner,String from) {
//		Drawable drawable = new AsyncImageDownLoad(head_Url,
//				new String[] { head_Url }, TaskUtil.HEADDEFAULTLOADSTATEIMG,
//				ConstantUtil.HEAD_PATH, context, from,new ImageCallback() {
//
//					@Override
//					public void load(Object object, Object[] tags) {
//						Drawable imageDrawable = (Drawable) object;
//						if (roundCorner) {
//							loadImg(ImageDealUtil.getPosterCorner(
//									imageDrawable, round), imageView);
//						} else {
//							loadImg(imageDrawable, imageView);
//						}
//					}
//				}).getDrawable();
//		return drawable;
//	}
//	/**
//	 * 
//	 * @param context
//	 * @param poster_Url
//	 * @param imageView
//	 * @param from which call the asyncdownload
//	 * @return
//	 */
//	public static Drawable loadPoster(Context context, String poster_Url,
//			final ImageView imageView,String from) {
//		return loadRoundPoster(context, poster_Url, imageView, 0, false,from);
//	}
//
//	/**
//	 * 
//	 * @param context
//	 * @param poster_Url
//	 * @param imageView
//	 * @param round
//	 * @param roundCorner
//	 * @param from which call the asyncdownload
//	 * @return
//	 */
//	public static Drawable loadRoundPoster(Context context, String poster_Url,
//			final ImageView imageView, final int round,
//			final boolean roundCorner,String from) {
//		Drawable drawable = new AsyncImageDownLoad(poster_Url,
//				new String[] { poster_Url }, TaskUtil.POSTERDEFAULTLOADSTATEIMG,
//				ConstantUtil.POSTER_PATH, context,from, new ImageCallback() {
//
//					@Override
//					public void load(Object object, Object[] tags) {
//						Drawable imageDrawable = (Drawable) object;
//						if (roundCorner) {
//							loadImg(ImageDealUtil.getPosterCorner(
//									imageDrawable, round), imageView);
//						} else {
//							loadImg(imageDrawable, imageView);
//						}
//					}
//				}).getDrawable();
//		return drawable;
//	}
//
//	/**
//	 * 
//	 * @param context
//	 * @param img_Url
//	 * @param imageView
//	 * @param from which call the asyncdownload
//	 * @return
//	 */
//	public static Drawable loadImage(Context context, String img_Url,
//			final ImageView imageView,String from) {
//		Drawable drawable = new AsyncImageDownLoad(img_Url,
//				new String[] { img_Url }, TaskUtil.IMGDEFAULTLOADSTATEIMG,
//				ConstantUtil.IMAGE_PATH, context,from,new ImageCallback() {
//
//					@Override
//					public void load(Object object, Object[] tags) {
//						loadImg((Drawable) object, imageView);
//					}
//				}).getDrawable();
//		return drawable;
//	}
//
//	/**
//	 * 
//	 * @param url
//	 * @param tags
//	 * @param loadStateImageId
//	 * @param savePath
//	 * @param context
//	 * @param from which call the asyncdownload
//	 * @param imageCallback
//	 * @return
//	 */
//	public static Drawable loadOther(String url, String[] tags,
//			int[] loadStateImageId, String savePath, Context context,String from,
//			ImageCallback imageCallback) {
//		Drawable drawable = new AsyncImageDownLoad(url, new String[] { url },
//				loadStateImageId, savePath, context,from,
//				imageCallback).getDrawable();
//		return drawable;
//	}
//
//	private static void loadImg(Drawable imageDrawable, ImageView imgView) {
//		if (null != imgView) {
//			if (null != imageDrawable) {
//				imgView.setImageDrawable(imageDrawable);
//			}
//		}
//	}
//
//}
