package com.jaf.jcore;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.stkj.dlm.DownloadManager;

import org.json.JSONObject;

public class UpdateManager {

	private static final String CHECK_VERSION = "check_version";
	private static final String URI_CHECK_UPDATE = "http://www.wagongsi.com/api/index.php/update/check";
	private static final long NO_LAST_CHECK = -2;
	
	public FragmentActivity activity;
	private Version mVersion;
	
	public UpdateManager(FragmentActivity activity) {
		this.activity = activity;
	}
	
	
	public void checkVersion(final boolean toast, final long delay) {
		//
		if (getLastCheckUpdateTime(activity) == NO_LAST_CHECK || isDelayTimeout(delay)) {
			checkVersion(toast);
		} 
	}
	
	private boolean isDelayTimeout(final long delay) {
		Util.e("check update @ isDelayTimeout");
		return (System.currentTimeMillis() - getLastCheckUpdateTime(activity)) >= delay ;
	}
	
	// --------------------------self version check
	// start-------------------------//
	public void checkVersion(final boolean toast) {

		Http http = new Http(CHECK_VERSION);
		http.url(URI_CHECK_UPDATE).get(new HttpCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				setLastCheckUpdateTime(activity,
						System.currentTimeMillis());
				mVersion = new Version();
				mVersion.parseJson(response);
				if (isVersionOutOfDate())
					update();
				else
					noUpdate(toast);
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				super.onErrorResponse(error);
				Toast.makeText(activity, "net work error",
						Toast.LENGTH_LONG).show();
			}
		});
	}

	private void noUpdate(boolean toast) {
		if (toast)
			Toast.makeText(activity, "no update",
					Toast.LENGTH_SHORT).show();
	}

	private void update() {
		if (null != mVersion) {
			showDialog();
		}
	}

	/**
	 * 打开更新的对话框
	 */
	private void showDialog() {
		if (activity.isFinishing())
			return;
		DialogFragment newFragment = MyAlertDialogFragment
				.newInstance(mVersion);
		activity.getSupportFragmentManager().beginTransaction()
				.add(newFragment, "dialog").commitAllowingStateLoss();
	}

	private static void doDownload(Context context, String uri) {
		String desc = context.getString(R.string.app_name);
		DownloadManager.getInstance().enqueue(context.getPackageName(),
				Uri.parse(uri), desc, null, "1.2M", true);
	}

	private boolean isVersionOutOfDate() {
		int curVersionCode = getCurVersion();
		int theVersionCode = mVersion.getVersionCode();
		return theVersionCode > curVersionCode;
	}

	public static class MyAlertDialogFragment extends DialogFragment {

		private static final int DIALOG_ICON = R.drawable.ic_launcher;

		public static MyAlertDialogFragment newInstance(Version version) {
			MyAlertDialogFragment frag = new MyAlertDialogFragment();
			Bundle args = new Bundle();
			args.putString("prompt", version.getPrompt());
			args.putString("uri", version.getUri());
			args.putBoolean("force", version.isForce());
			frag.setArguments(args);
			return frag;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			String prompt = getArguments().getString("prompt");
			final String uri = getArguments().getString("uri");
			final boolean isForce = getArguments().getBoolean("force");
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
					.setIcon(DIALOG_ICON)
					.setTitle(android.R.string.dialog_alert_title)
					.setMessage(prompt)
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (null != uri) {
										doDownload(getActivity()
												.getApplicationContext(), uri);
									}
								}
							})
					.setNegativeButton(android.R.string.no,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (isForce)// 强制更新
										MyAlertDialogFragment.this
												.getActivity().finish();
								}
							});
			return builder.create();
		}
	}

	/**
	 * 设置上次更新时间
	 * 
	 * @param context
	 * @param last
	 */
	public static void setLastCheckUpdateTime(Context context, long last) {
		SharedPreferences sp = context.getSharedPreferences(CHECK_VERSION,
				Context.MODE_PRIVATE);
		sp.edit().putLong("lastCheck", last).commit();
	}

	public static long getLastCheckUpdateTime(Context context) {
		SharedPreferences sp = context.getSharedPreferences(CHECK_VERSION,
				Context.MODE_PRIVATE);
		return sp.getLong("lastCheck", NO_LAST_CHECK);
	}
	
	
	/**
	 * 获取当前版本号
	 * 
	 * @return 版本号
	 */
	public final int getCurVersion() {
		int version = 0;
		try {
			PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(
					activity.getPackageName(), 0);
			version = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}
	
	
	public static class Version {
		
		private int versionCode;
		
		private String versionName;
		
		private String prompt;
		
		private boolean force;
		
		private String uri;

		public int getVersionCode() {
			return versionCode;
		}

		public void setVersionCode(int versionCode) {
			this.versionCode = versionCode;
		}

		public String getVersionName() {
			return versionName;
		}

		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}

		public String getPrompt() {
			return prompt;
		}

		public void setPrompt(String prompt) {
			this.prompt = prompt;
		}

		public boolean isForce() {
			return force;
		}

		public void setForce(boolean force) {
			this.force = force;
		}

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}

		public void parseJson(JSONObject response) {
			int versionCode = response.optInt("versionCode");
			String versionName = response.optString("versionName");
			String prompt = response.optString("prompt");
			boolean force = response.optBoolean("force");
			String uri = response.optString("uri");
			
			setVersionCode(versionCode);
			setVersionName(versionName);
			setPrompt(prompt);
			setForce(force);
			setUri(uri);
		}
	}
}
