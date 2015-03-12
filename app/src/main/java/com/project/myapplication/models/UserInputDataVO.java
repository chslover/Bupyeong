package com.project.myapplication.models;

/**
 * Created by 최형식 on 2015-03-04.
 */
public class UserInputDataVO{
	private long _id;
	private String name;
	private long time;

	public long getuId(){
		return _id;
	}

	public void setuId(long _id){
		this._id = _id;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public long getTime(){
		return time;
	}

	public void setTime(long time){
		this.time = time;
	}
}
