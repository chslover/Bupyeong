package com.project.myapplication.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.project.myapplication.FileCopyAndSave;

import java.io.File;
import java.io.IOException;

/**
 * Created by 최형식 on 2015-03-06.
 */
public class UndergroundHelper extends SQLiteOpenHelper implements MyDatabase{
	private static String LOG_TAG = "Database Log";
	private static String PACKAGE = "com.project.myapplication";
	public final static String DB_UNDERGROUND = "under.db";
	public final static String TABLE_UNDERGROUND = "under";
	public final static String FIELD_ID = "_id";
	public final static String FIELD_NAME = "name";
	public final static int DB_VERSION = 18;
	private SQLiteDatabase db;
	private final Context context;

	public UndergroundHelper(Context context, String name, SQLiteDatabase.CursorFactory factory){
		super(context,name,factory,DB_VERSION);
		this.context = context;

		try{
			boolean checkDB = isCheckDB(this.context);
			Log.i(LOG_TAG,"DB Check : "+checkDB);
			if(!checkDB){
				copyDB(this.context);
			}else{

			}
		}catch(Exception e){
			e.printStackTrace();
		}
		db = SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(DB_UNDERGROUND),null);
	}
	private boolean isCheckDB(Context context){
		File file = context.getDatabasePath(DB_UNDERGROUND);
		if(file.exists()){
			return true;
		}else{
			return false;
		}
	}
	public void copyDB(Context context){
		Log.d(LOG_TAG,"copy db");
		String folderPath = "/data/data/" + PACKAGE + "/databases";
		try{
			FileCopyAndSave.fileCopy(context, folderPath);
		}catch(IOException e){
			e.printStackTrace();
		}

	}
	@Override
	public void onCreate(SQLiteDatabase db){

	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
	}
	@Override
	public void deleteAll(){
		db.execSQL("delete from " + TABLE_UNDERGROUND);
	}
	@Override
	public Cursor getFindAll(){
		return db.query(TABLE_UNDERGROUND, new String[]{FIELD_ID, FIELD_NAME}, null ,null, null, null, FIELD_NAME);
	}
	//검색결과 메소드 추후에 키워드 검색 방법을 생각해야함. 지하상가DB에서 검색한다.
	@Override
	public Cursor getFindToEqual(String query){
		//FIELD_NAME + " = '"+query+"'"
		return db.query(TABLE_UNDERGROUND, new String[]{FIELD_ID, FIELD_NAME}, FIELD_NAME + " = '"+query+"'" ,null, null, null, FIELD_NAME);
	}
	//자동완성 메소드, 지하상가DB에서 자동완성 시켜줘야함.
	public Cursor getAutoComplete(String text){
		Log.i(LOG_TAG, "db"+db.toString());
		return db.query(TABLE_UNDERGROUND, new String[]{FIELD_ID, FIELD_NAME}, FIELD_NAME + " LIKE '" + text + "%'", null, null, null, null);
	}
	public void close(){
		super.close();
		db.close();
	}
}