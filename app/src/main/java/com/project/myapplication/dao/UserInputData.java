package com.project.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 최형식 on 2015-03-06.
 */
public class UserInputData implements MyDatabase{
	public final static String DB_USERINPUT = "USERINPUT_DB.db";
	public final static String TABLE_USERINPUT = "USERINPUT_TB";
	public final static String FIELD_UID = "_id";
	public final static String FIELD_NAME = "name";
	public final static String FIELD_TIME = "currenttime";
	public final static int DB_VERSION = 3;

	private SQLiteDatabase db;
	private Helper helper;

	public UserInputData(Context context){
		helper = new Helper(context, DB_USERINPUT, null, DB_VERSION);
		db = helper.getWritableDatabase();
	}
	@Override
	public Cursor getFindAll(){
		return db.query(TABLE_USERINPUT, new String[]{FIELD_UID, FIELD_NAME, FIELD_TIME}, null ,null, null, null, FIELD_NAME);
	}
	@Override
	public Cursor getFindToEqual(String text){
		return db.query(TABLE_USERINPUT, new String[]{FIELD_UID, FIELD_NAME, FIELD_TIME}, null ,null, null, null, FIELD_NAME);
	}
	@Override
	public void deleteAll(){
		db.execSQL("delete from "+TABLE_USERINPUT);
	}
	public long insertQuery(String text, long time){
		ContentValues values = new ContentValues();
		values.put(FIELD_NAME, text);
		values.put(FIELD_TIME, time);
		return db.insert(TABLE_USERINPUT, null, values);
	}
	//최근검색어 메소드, 유저 DB로 저장된 최근검색어 리턴
	public Cursor getRecent(){
		return db.query(TABLE_USERINPUT, new String[]{FIELD_UID, FIELD_NAME, FIELD_TIME}, null, null, null, null, FIELD_TIME+" desc");
	}
	public void deleteBeforeRecentQuery(String query){
		db.execSQL("delete from "+TABLE_USERINPUT+" where "+FIELD_NAME+" = '"+query+"'");
	}
	private class Helper extends SQLiteOpenHelper {
		public Helper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
			super(context,name,factory,version);
		}
		@Override
		public void onCreate(SQLiteDatabase db){
			String sql = "create table "+TABLE_USERINPUT+" ("+FIELD_UID+" integer primary key autoincrement, "+
					FIELD_NAME+" text, "+FIELD_TIME+" long);";
			db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
			String sql = "drop table "+TABLE_USERINPUT;
			db.execSQL(sql);
			onCreate(db);
		}
	}
	public void close(){
		db.close();
	}
}
