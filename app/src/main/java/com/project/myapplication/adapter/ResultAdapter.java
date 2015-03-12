package com.project.myapplication.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.myapplication.R;

/**
 * Created by 최형식 on 2015-03-03.
 */
public class ResultAdapter extends CursorAdapter {
	public ResultAdapter(Context context, Cursor c){
		super(context, c);

	}
	@Override
	public void bindView(View view, Context context, Cursor cursor){
		final ImageView image = (ImageView)view.findViewById(R.id.image);
		final TextView resultName = (TextView)view.findViewById(R.id.result_name);
		final TextView resultCategory = (TextView)view.findViewById(R.id.result_category);
		final TextView resultYet = (TextView)view.findViewById(R.id.result_notyet);
		final Button visitBtn = (Button)view.findViewById(R.id.visitBtn);
		final Button hotlistBtn = (Button)view.findViewById(R.id.hotlistBtn);
		visitBtn.setFocusable(false);
		hotlistBtn.setFocusable(false);

		visitBtn.setOnClickListener(onClickListener);
		hotlistBtn.setOnClickListener(onClickListener);

		if(cursor!=null){
			visitBtn.setTag(cursor.getPosition());
			hotlistBtn.setTag(cursor.getPosition());

			image.setBackgroundResource(R.drawable.ic_launcher);
			resultName.setText(cursor.getString(cursor.getColumnIndex("name")));
			resultCategory.setText("남자@테스트");
			resultYet.setText("등등@테스트");
		} else{
			resultName.setText("");
		}
		Log.i("Test","Test");
	}
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent){
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.result_listview, parent, false);
		return v;
	}

	private Button.OnClickListener onClickListener = new Button.OnClickListener(){
		@Override
		public void onClick(View v){
			//Button visitBtn = (Button) v.findViewById(R.id.result_name);
			switch(v.getId()){
				case R.id.visitBtn:
					Log.i("방문기록", "@Ok =" + v.getTag());
					break;
				case R.id.hotlistBtn:
					Log.i("찜","@Ok ="+ v.getTag());
					break;
			}
		}
	};
}

