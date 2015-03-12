package com.project.myapplication.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.project.myapplication.R;

/**
 * Created by 최형식 on 2015-02-27.
 */
public class SearchingAdapter extends CursorAdapter {
	public SearchingAdapter(Context context, Cursor c){
		super(context, c);
	}
	@Override
	public void bindView(View view, Context context, Cursor cursor){
		final TextView recent = (TextView)view.findViewById(R.id.recent_query);

		if(cursor!=null){
			recent.setText(cursor.getString(cursor.getColumnIndex("name")));
		} else{
			recent.setText("");
		}
	}
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent){
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.searching_listview, parent, false);
		return v;
	}

}

