package com.zheng.mobilesafe.fragment;

import com.zheng.mobilesafe.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CleanCacheFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
	
		return View.inflate(getActivity(), R.layout.fragment_cache_clean, null);
	}

}
