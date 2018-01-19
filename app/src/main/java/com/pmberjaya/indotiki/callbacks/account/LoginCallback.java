package com.pmberjaya.indotiki.callbacks.account;

import java.util.ArrayList;

import com.pmberjaya.indotiki.base.BaseCallback;
import com.pmberjaya.indotiki.models.account.LoginData;
import com.pmberjaya.indotiki.models.account.VerificationData;


public class LoginCallback extends BaseCallback {
	boolean status;
	ArrayList<String> error;
	String token;
	LoginData data;
	public LoginData getLoginData(){
		return data;
	}
	public void setLoginData(LoginData data){
		this.data  = data;
	}
	public ArrayList<String> getError_form() {
		return error;
	}

	public void setError_form(ArrayList<String> error_form) {
		this.error = error;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
