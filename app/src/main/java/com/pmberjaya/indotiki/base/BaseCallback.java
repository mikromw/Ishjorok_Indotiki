package com.pmberjaya.indotiki.base;

public class BaseCallback {
	int sukses;
	String pesan;
	String callback;
	public int getSukses(){
		return sukses;
	}
	public void setSukses(int sukses){
		this.sukses  = sukses;
	}
	public String getPesan(){
		return pesan;
	}
	public void setPesan(String pesan){
		this.pesan  = pesan;
	}
	public String getCallback(){
		return callback;
	}
	public void setCallback(String callback){
		this.callback  = callback;
	}
}
