package com.stkj.dlm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

public class SwitchTab extends FrameLayout implements OnClickListener{

	private OnTabClickListener l;

	public SwitchTab(Context context) {
		super(context);
		loadContent();
	}
	
	public SwitchTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		loadContent();
	}

	private void loadContent() {
		LayoutInflater layoutInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.view_switch_tab, this, true);
		onViewDidLoad();
	}

	private void onViewDidLoad() {
		findViewById(R.id.btn0).setOnClickListener(this);
		findViewById(R.id.btn1).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (this.l != null) {
			if (v.getId() == R.id.btn0) {
				this.l.onLeftTabClick(v);
				findViewById(R.id.btn0).setBackgroundResource(R.drawable.bg_switch_selected);
				findViewById(R.id.btn1).setBackgroundResource(R.drawable.bg_switch_unselected);
			}
			
			if (v.getId() == R.id.btn1) {
				this.l.onRightTabClick(v);
				findViewById(R.id.btn0).setBackgroundResource(R.drawable.bg_switch_unselected);
				findViewById(R.id.btn1).setBackgroundResource(R.drawable.bg_switch_selected);
			}
		}
	}
	
	public interface OnTabClickListener {
		void onLeftTabClick(View v);
		void onRightTabClick(View v);
	}
	
	public void setOnTabClickListener(OnTabClickListener l) {
		this.l = l;
	}
}