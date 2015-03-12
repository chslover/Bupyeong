package com.project.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.TextView;


public class SearchResultDetailActivity extends ActionBarActivity {
	private TextView tv;
	private Intent intent;
	private String str;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result_detail);
		tv = (TextView) findViewById(R.id.text11);
		intent = getIntent();
		str = intent.getExtras().getString("ResultRaw");
		tv.setText(str);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_search_result_detail, menu);
		return true;
	}

}
