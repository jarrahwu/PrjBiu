package com.jaf.jcore;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class Application extends android.app.Application  {

	private static Application INSTANCE;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	@Override
	public void onCreate() {
		super.onCreate();
		synchronized (Application.class) {
			INSTANCE = this;
		}
		mRequestQueue = Volley.newRequestQueue(this);

	}

	public static final Application getInstance() {
		Application ret;
		synchronized (Application.class) {
			ret = INSTANCE;
		}
		return ret;
	}

	public RequestQueue getRequestQueue() {
		return mRequestQueue;
	}

	public void cancelRequest(String tag) {
		mRequestQueue.cancelAll(tag);
	}
	
	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					Cache.getInstance());
		}
		return this.mImageLoader;
	}

}
