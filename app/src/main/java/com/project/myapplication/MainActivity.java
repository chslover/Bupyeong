package com.project.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.project.myapplication.adapter.AutoCompleteAdapter;
import com.project.myapplication.dao.UndergroundHelper;
import com.project.myapplication.dao.UserInputData;
import com.project.myapplication.fragment.MapFragment;
import com.project.myapplication.fragment.SearchingFragment;

public class MainActivity extends ActionBarActivity{
	public static final boolean ON_CLICK = true;
	public static final boolean ON_CLOSE = false;

	private DrawerLayout dlDrawer;
	private ActionBarDrawerToggle dtToggle;
	private Toolbar toolbar;
	private SearchView searchView;
	private SearchManager searchManager;
	private UndergroundHelper underDatabase;
	private UserInputData userDatabase;
	private ListView listNavi;
	private ArrayAdapter<String> adapter;
	private BackPressCloseHandler backPressCloseHandler;
	private Fragment frag;
	private FragmentManager fragManager;
	private FragmentTransaction fragTransaction;
	private boolean onCheck;
	private TimeReceiver tr;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		underDatabase = new UndergroundHelper(this,null,null);
		backPressCloseHandler = new BackPressCloseHandler(this);
		userDatabase = new UserInputData(this);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		listNavi = (ListView) findViewById(R.id.navi);
		tr = new TimeReceiver();
		adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_menu);

		listNavi.setAdapter(adapter);
		listNavi.setOnItemClickListener(onItemClickListener);
		setSupportActionBar(toolbar);

		onCheck = ON_CLOSE;

		adapter.add("홈");
		adapter.add("길 찾기");
		adapter.add("카테고리");
		adapter.add("출구정보");
		adapter.add("찜/방문기록");

		dtToggle = new ActionBarDrawerToggle(this, dlDrawer, toolbar, R.string.open, R.string.close) {
			@Override
			public void onDrawerClosed(View drawerView){
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView){
				super.onDrawerOpened(drawerView);
			}

		};
		dlDrawer.setDrawerListener(dtToggle);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		underDatabase.close();
		userDatabase.close();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_main, menu);

		searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView.setQueryHint("명칭, 키워드, 출구번호 검색");
		searchView.setOnSearchClickListener(onClickListener);
		searchView.setOnQueryTextListener(onQueryTextListener);
		searchView.setOnSuggestionListener(onSuggestionListener);
		searchView.setOnCloseListener(onCloseListener);
		if(null != searchManager){
			searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		}
		//searchView.setIconifiedByDefault(false); //검색창 닫아놓기
		return true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState){
		super.onPostCreate(savedInstanceState);
// Sync the toggle state after onRestoreInstanceState has occurred.
		dtToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		dtToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		return super.onOptionsItemSelected(item) || dtToggle.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed(){
		backPressCloseHandler.onBackPressed();
	}


	//네비게이션 메뉴 클릭 이벤트
	private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// 이벤트 발생 시 해당 아이템 위치의 텍스트를 출력
			Toast.makeText(getApplicationContext(), adapter.getItem(arg2), Toast.LENGTH_SHORT).show();
		}
	};
	private View.OnClickListener onClickListener = new View.OnClickListener(){
		@Override
		public void onClick(View v){
			if(v.getId()==R.id.search){
				Log.v("SearchTouch", "@Ok");
				onCheck = ON_CLICK;
				selectFragment();
			}
		}
	};
	private SearchView.OnCloseListener onCloseListener = new SearchView.OnCloseListener(){
		@Override
		public boolean onClose(){
			Log.v("SearchClose","@Ok");
			onCheck = ON_CLOSE;
			selectFragment();
			return false;
		}
	};

	private void selectFragment(){
		fragManager = getFragmentManager();
		fragTransaction = fragManager.beginTransaction();
		if(onCheck){
			frag = new SearchingFragment();
		} else {
			frag = new MapFragment();
		}
		fragTransaction.replace(R.id.container,frag);
		fragTransaction.commit();
	}

	private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
		@Override
		public boolean onQueryTextSubmit(String query){
			tr.setTime();
			long result = userDatabase.insertQuery(query, tr.getTime());
			Log.v("onQueryTextSubmit", query);

			Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
			intent.putExtra("Query", query);
			startActivity(intent);

			return result != -1;
		}@Override
		 public boolean onQueryTextChange(String newText){
			Cursor cursor;
			cursor = underDatabase.getAutoComplete(newText);
			if(cursor.getCount() != 0){
				String[] columns = new String[]{UndergroundHelper.FIELD_NAME};
				int[] columnTextId = new int[]{android.R.id.text1};
				AutoCompleteAdapter autoCompleteAdapter = new AutoCompleteAdapter(getBaseContext(), R.layout.dropdown_menu, cursor, columns, columnTextId, 0);
				searchView.setSuggestionsAdapter(autoCompleteAdapter);
				//cursor.close();
				return true;
			} else{
				return false;
			}
		}
	};
	private SearchView.OnSuggestionListener onSuggestionListener = new SearchView.OnSuggestionListener() {
		@Override
		public boolean onSuggestionSelect(int postion){

			return false;
		}
		//자동완성 메뉴에서 선택시 검색창에 적용
		@Override
		public boolean onSuggestionClick(int postion){
			SQLiteCursor cursor = (SQLiteCursor) searchView.getSuggestionsAdapter().getItem(postion);
			int indexColumnName = cursor.getColumnIndex(UndergroundHelper.FIELD_NAME);

			searchView.setQuery(cursor.getString(indexColumnName), false);
			return false;
		}
	};
}
