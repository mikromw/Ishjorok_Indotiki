package com.pmberjaya.indotiki.models.deposit.DepositTransactionData;

/**
 * Created by Gilbert on 4/15/2017.
 */

public class DepositTransactionData {

    private String ID_Deposit;
    private String jumlah;
    private String tanggal;
    private String status;

    public String getID_Deposit() {
        return ID_Deposit;
    }

    public void setID_Deposit(String ID_Deposit) {
        this.ID_Deposit = ID_Deposit;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
