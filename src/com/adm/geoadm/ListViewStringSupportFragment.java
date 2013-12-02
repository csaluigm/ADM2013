package com.adm.geoadm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListViewStringSupportFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_list_string, null);
		ListView list = (ListView) view.findViewById(R.id.lvFragment);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), R.layout.fragment_list_item, getResources().getStringArray(R.array.items_list));
		list.setAdapter(adapter);
		return view;
	}

}
