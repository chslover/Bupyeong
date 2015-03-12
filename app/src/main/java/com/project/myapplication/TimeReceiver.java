package com.project.myapplication;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 최형식 on 2015-03-02.
 */
public class TimeReceiver{
	private Calendar cal;
	private long time;

	public TimeReceiver(){

	}

	public void setTime(){
		cal = Calendar.getInstance();
		Date date = cal.getTime();
		String tempTime = (new SimpleDateFormat("yyyyMMddHHmmss").format(date));
		time = Long.parseLong(tempTime);
		Log.i("time", tempTime);
	}
	public long getTime(){
		return time;
	}
}