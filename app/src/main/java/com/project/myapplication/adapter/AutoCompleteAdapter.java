package com.project.myapplication.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;

import com.project.myapplication.dao.UndergroundHelper;

/**
 * Created by 최형식 on 2015-02-26.
 */
public class AutoCompleteAdapter extends SimpleCursorAdapter {

	public AutoCompleteAdapter(Context context, int layout, Cursor c,
	                                     String[] from, int[] to){
		super(context, layout, c, from, to);
	}
	public AutoCompleteAdapter(Context context, int layout, Cursor c,
	                                     String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}
	@Override
	public CharSequence convertToString(Cursor cursor){
		int indexColumnSuggestion = cursor.getColumnIndex(UndergroundHelper.FIELD_NAME);

		return cursor.getString(indexColumnSuggestion);
	}
}


