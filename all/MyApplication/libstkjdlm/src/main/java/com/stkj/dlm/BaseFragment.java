package com.stkj.dlm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(onLoadView(), container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		onViewDidLoad(savedInstanceState);
	}

	public abstract void onViewDidLoad(Bundle savedInstanceState);

	public abstract int onLoadView();
	
	protected View findViewById(int id) {
		return getActivity().findViewById(id);
	}
}
