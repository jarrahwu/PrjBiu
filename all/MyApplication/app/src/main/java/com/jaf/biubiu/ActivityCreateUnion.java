package com.jaf.biubiu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.baidu.location.BDLocation;
import com.jaf.jcore.BaseActionBarActivity;
import com.jaf.jcore.BindView;
import com.jarrah.photo.FileUtil;
import com.jarrah.photo.ImageUtil;
import com.jarrah.photo.PhotoPicker;
import com.jarrah.photo.PopupUtil;
import com.jarrah.photo.ReqeustCode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ActivityCreateUnion extends BaseActionBarActivity
		implements
			ReqeustCode {

	@BindView(id = R.id.capture, onClick = "onCaptureClick")
	private ImageView mCapture;

	@BindView(id = R.id.unionEdit)
	private EditText mEditText;

	@BindView(id = R.id.locDesc)
	private TextView mLocDesc;

	@BindView(id = R.id.btnCreate, onClick = "onSubmitClick")
	private Button mSubmit;

	private File mImageFile;

	@Override
	protected int onLoadViewResource() {
		return R.layout.activity_create_union;
	}

	@Override
	protected void onViewDidLoad(Bundle savedInstanceState) {
		LocationManager.getInstance().requestLocation(
				new LocationManager.JLsn() {
					@Override
					public void onResult(double latitude, double longitude,
							BDLocation location) {
						super.onResult(latitude, longitude, location);
						mLocDesc.setText(location.getAddrStr());
					}
				});
	}

	public static void start(Activity activity) {
		activity.startActivity(new Intent(activity, ActivityCreateUnion.class));
	}

	public void onCaptureClick(View v) {
		popup(this);
	}

	private Dialog dialog;

	protected File captureFile;

	@SuppressLint("InflateParams")
	protected void popup(Context context) {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View holder = inflater.inflate(
				com.jarrah.photo.R.layout.view_popup_button, null, false);
		View gallery = holder.findViewById(com.jarrah.photo.R.id.btnPhoto);
		View capture = holder.findViewById(com.jarrah.photo.R.id.btnCapture);
		View cancel = holder.findViewById(com.jarrah.photo.R.id.btnCanel);

		ButtonClick click = new ButtonClick(this);
		gallery.setOnClickListener(click);
		capture.setOnClickListener(click);
		cancel.setOnClickListener(click);

		dialog = PopupUtil.makePopup(context, holder);
		dialog.show();
	}

	public class ButtonClick implements View.OnClickListener {

		private Activity activity;

		public ButtonClick(Activity activity) {
			this.activity = activity;
		}

		@Override
		public void onClick(View v) {

			if (dialog != null) {
				dialog.dismiss();
			}

			if (v.getId() == com.jarrah.photo.R.id.btnPhoto) {
				PhotoPicker.launchGallery(activity, FROM_GALLERY);
			}

			if (v.getId() == com.jarrah.photo.R.id.btnCapture) {
				captureFile = FileUtil.getCaptureFile(activity);
				PhotoPicker.launchCamera(activity, FROM_CAPTURE, captureFile);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {

			if (requestCode == FROM_GALLERY) {
				if (data != null) {
					String path = PhotoPicker
							.getPhotoPathByLocalUri(this, data);
					onGalleryComplete(path);
				}
			} else if (requestCode == FROM_CAPTURE) {

				onCaptureComplete(captureFile);

			} else if (requestCode == FROM_CROP) {
				if (data != null) {
					Bitmap bitmap = data.getParcelableExtra("data");
					onCropComplete(bitmap);
				}
			}

		}
	}

	protected void onGalleryComplete(String path) {

		PhotoPicker.startCrop(this, path, FROM_CROP, false);

		// if (path != null) {
		// Bitmap bmp = ImageUtil.getResizedImage(path, null, 500, true, 0);
		// mCapture.setImageBitmap(bmp);
		// }
		//
		// mImageFile = new File(path);
	}

	protected void onCropComplete(Bitmap bitmap) {
        bitmap = ImageUtil.circleImage(bitmap);
		mCapture.setImageBitmap(bitmap);
	}

	protected void onCaptureComplete(File captureFile) {
		// if (captureFile != null) {
		// Bitmap bmp = ImageUtil.getResizedImage(captureFile.getAbsolutePath(),
		// null, 500, true, 0);
		// mCapture.setImageBitmap(bmp);
		// mImageFile = captureFile;
		// }
		PhotoPicker.startCrop(this, captureFile.getAbsolutePath(), FROM_CROP,
				false);
	}

	public void onSubmitClick(View v) {
		L.dbg("onSubmit");
		asynMultipart();
	}

	private void asynMultipart() {
		if (mImageFile == null)
			return;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("source", mImageFile);

		AQuery aq = new AQuery(getApplicationContext());
		aq.ajax(Constant.MULTIPART, params, String.class,
				new AjaxCallback<String>() {
					@Override
					public void callback(String url, String object,
							AjaxStatus status) {
						super.callback(url, object, status);
						L.dbg("debug" + status.getCode() + " >>" + object
								+ mImageFile.getName());
						// L.dbg("upload " + object.toString());
					}
				});
	}

}
