/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.nostra13.universalimageloader.core;

import java.io.File;
import java.io.IOException;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.assist.FailReason.FailType;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

public abstract class BaseLoadAndDisplayImageTask implements Runnable {


	public BaseLoadAndDisplayImageTask() {
	}

	/**
	 * @return true - if task should be interrupted; false - otherwise
	 */
	abstract protected boolean waitIfPaused();

	/**
	 * @return true - if task should be interrupted; false - otherwise
	 */
	abstract protected boolean delayIfNeed();

	/**
	 * Check whether the image URI of this task matches to image URI which is actual for current ImageView at this
	 * moment and fire {@link ImageLoadingListener#onLoadingCancelled()} event if it doesn't.
	 */
	abstract protected boolean checkTaskIsNotActual();

	/** Check whether the current task was interrupted */
	abstract protected boolean checkTaskIsInterrupted();

	abstract protected Bitmap tryLoadBitmap();

	abstract protected File getImageFileInDiscCache();

	abstract protected Bitmap decodeImage(String imageUri) throws IOException ;

	/**
	 * @return Cached image URI; or original image URI if caching failed
	 */
	abstract protected String tryCacheImageOnDisc(File targetFile);

	abstract protected boolean downloadSizedImage(File targetFile, int maxWidth, int maxHeight) throws IOException;

	abstract protected void downloadImage(File targetFile) throws IOException ;

	abstract protected void fireImageLoadingFailedEvent(final FailType failType, final Throwable failCause) ;

	abstract protected ImageDownloader getDownloader() ;

	abstract String getLoadingUri();

	abstract protected void log(String message);

	abstract protected void log(String message, Object... args);
}
