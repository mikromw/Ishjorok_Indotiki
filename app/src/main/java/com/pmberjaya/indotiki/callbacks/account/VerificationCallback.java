package com.pmberjaya.indotiki.callbacks.account;

import com.pmberjaya.indotiki.base.BaseCallback;
import com.pmberjaya.indotiki.models.account.VerificationData;

import java.util.ArrayList;


public class VerificationCallback extends BaseCallback {
	VerificationData data;
	ArrayList<String> error;
	public VerificationData getUserDataVerification(){
		return data;
	}
	public void setUserDataVerification(VerificationData data){
		this.data  = data;
	}

	public VerificationData getData() {
		return data;
	}

	public void setData(VerificationData data) {
		this.data = data;
	}

	public ArrayList<String> getError() {
		return error;
	}

	public void setError(ArrayList<String> error) {
		this.error = error;
	}
}
