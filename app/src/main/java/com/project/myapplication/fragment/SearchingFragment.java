package com.project.myapplication.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.myapplication.R;
import com.project.myapplication.adapter.SearchingAdapter;
import com.project.myapplication.dao.UserInputData;

/**
 * Created by 최형식 on 2015-02-26.
 */
public class SearchingFragment extends Fragment {
	private ListView listRecent;
	private SearchView searchView;
	private AlertDialog.Builder builder;
	private Button removeBtn;
	private UserInputData userDatabase;
	private Cursor cursor;
	private Cursor removeCur=null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		searchView = (SearchView) getActivity().findViewById(R.id.search);

		return inflater.inflate(R.layout.fragment_searching, container, false);
	}
	public void onStart(){
		super.onStart();
		userDatabase = new UserInputData(getActivity());
		builder = new AlertDialog.Builder(getActivity());
		cursor = userDatabase.getRecent();
		listRecent = (ListView) getActivity().findViewById(R.id.recent_list);
		removeBtn = (Button)getActivity().findViewById(R.id.all_remove_btn);

		listRecent.setOnItemClickListener(onItemClickListener);
		removeBtn.setOnClickListener(onClickListener);

		SearchingAdapter searchingAdapter = new SearchingAdapter(getActivity(), cursor);
		listRecent.setAdapter(searchingAdapter);
	}
	private View.OnClickListener onClickListener = new View.OnClickListener(){
		@Override
		public void onClick(View v){
			builder.setMessage("전체목록을 지우시겠습니까?");
			builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which){
					userDatabase.deleteAll();
				SearchingAdapter searchingAdapter = new SearchingAdapter(getActivity(), removeCur);
				listRecent.setAdapter(searchingAdapter);
				Toast.makeText(getActivity(),"전체목록이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
				}
			});
			builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which){
					dialog.cancel();
						}
			});

			AlertDialog dialog = builder.create();
			dialog.show();
		}
	};
	private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			//String str = (String)parent.getAdapter().getItem(position);
			TextView tv = (TextView) view.findViewById(R.id.recent_query);
			userDatabase.deleteBeforeRecentQuery(tv.getText().toString());
			searchView.setQuery(tv.getText(),true);
		}
	};
	public void onDestroy(){
		super.onDestroy();
		cursor.close();
	}
}
