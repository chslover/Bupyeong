package com.project.myapplication.models;



/**
 * Created by 최형식 on 2015-03-04.
 */
public class CategoryVO {
	private long cId;
	private String cName;

	public String getcName(){
		return cName;
	}

	public void setcName(String cName){
		this.cName = cName;
	}

	public long getcId(){
		return cId;
	}

	public void setcId(long cId){
		this.cId = cId;
	}
}
