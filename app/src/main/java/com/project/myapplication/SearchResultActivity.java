package com.project.myapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.project.myapplication.adapter.ResultAdapter;
import com.project.myapplication.dao.UndergroundHelper;


public class SearchResultActivity extends ActionBarActivity {
	private DrawerLayout dlDrawer;
	private ActionBarDrawerToggle dtToggle;
	private Toolbar toolbar;
	private Intent intent;
	private String resultQuery;
	private String title;
	private ArrayAdapter<String> adapter;
	private ListView listNavi;
	private ListView listResult;
	private UndergroundHelper underDatabase;
	private Cursor cursor;
	private ResultAdapter resultAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);

		adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_menu);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		dlDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
		listResult = (ListView) findViewById(R.id.result_list);
		underDatabase = new UndergroundHelper(this,null,null);


		listNavi = (ListView) findViewById(R.id.navi);
		listNavi.setOnItemClickListener(onItemClickListener);
		listResult.setOnItemClickListener(onItemClick);
		intent = getIntent();

		setSupportActionBar(toolbar);

		dtToggle = new ActionBarDrawerToggle(this, dlDrawer,
				toolbar, R.string.open, R.string.close){
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}

		};
		adapter.add("홈");
		adapter.add("길 찾기");
		adapter.add("카테고리");
		adapter.add("출구정보");
		adapter.add("찜/방문기록");
		listNavi.setAdapter(adapter);

		dlDrawer.setDrawerListener(dtToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
		//검색결과 제목 설정
		title = getSupportActionBar().getTitle().toString();
		resultQuery = intent.getExtras().getString("Query");
		getSupportActionBar().setTitle("\""+resultQuery+"\"   "+title);
		//일단 전체 검색.
		cursor = underDatabase.getFindToEqual(resultQuery);
		//검색결과 리스트 페이징 처리.
		resultAdapter = new ResultAdapter(this, cursor);
		listResult.setAdapter(resultAdapter);


	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		cursor.close();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_search_result, menu);
		return true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
// Sync the toggle state after onRestoreInstanceState has occurred.
		dtToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		dtToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
			case R.id.previous:
				Log.v("Previous","@Ok");
				finish();
				return true;
			//맵 버튼 누르면 지도결과액티비티 호출.
			case R.id.map:
				Log.v("Map","@Ok");
				/*Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
				intent.putExtra("Query", i);
				startActivity(intent);*/
				return true;

		}
		if (dtToggle.onOptionsItemSelected(item)) {
			//Log.v("네비게이션","터치");
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	//네비게이션 메뉴 클릭 이벤트
	private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> aView, View view, int position, long id){
			// 이벤트 발생 시 해당 아이템 위치의 텍스트를 출력
				Toast.makeText(getApplicationContext(), adapter.getItem(position), Toast.LENGTH_SHORT).show();
		}
	};
	private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> aView, View view, int position, long id){
			resultAdapter.notifyDataSetChanged();
			cursor.move(position);
			Toast.makeText(getApplicationContext(), cursor.getString(cursor.getColumnIndex("name")), Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(SearchResultActivity.this, SearchResultDetailActivity.class);
			intent.putExtra("ResultRaw", cursor.getString(cursor.getColumnIndex("name")));
			startActivity(intent);
		}
	};

}
