package com.project.myapplication.dao;

import android.database.Cursor;

/**
 * Created by 최형식 on 2015-03-06.
 */
public interface MyDatabase {
	public Cursor getFindAll();
	public Cursor getFindToEqual(String text);
	public void deleteAll();
	//public void deleteToEqual(String text);
	//public long insertQuery(String name, long time);
}
