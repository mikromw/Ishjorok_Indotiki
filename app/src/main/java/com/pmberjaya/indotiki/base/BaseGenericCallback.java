package com.pmberjaya.indotiki.base;

/**
 * Created by Gilbert on 06/07/2017.
 */

public class BaseGenericCallback<T> {
    int sukses;
    String pesan;
    String user_id;
    String request_type;
    String token;
    T data;

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
    public String getRequest_type() {
        return request_type;
    }
    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public T getData () {
        return data;
    }
    public void setData (T data) {
        this.data = data;
    }
}
