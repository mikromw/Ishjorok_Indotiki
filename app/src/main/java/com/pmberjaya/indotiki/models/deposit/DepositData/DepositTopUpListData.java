package com.pmberjaya.indotiki.models.deposit.DepositData;

/**
 * Created by Gilbert on 13/07/2017.
 */

public class DepositTopUpListData {
    private String id;
    private String price;
    private String nominal;
    private String bonus;
    private String type_bonus;
    private String userlevel;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getType_bonus() {
        return type_bonus;
    }

    public void setType_bonus(String type_bonus) {
        this.type_bonus = type_bonus;
    }

    public String getUserlevel() {
        return userlevel;
    }

    public void setUserlevel(String userlevel) {
        this.userlevel = userlevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
