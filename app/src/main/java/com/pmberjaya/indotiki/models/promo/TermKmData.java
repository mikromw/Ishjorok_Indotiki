package com.pmberjaya.indotiki.models.promo;


/**
 * Created by Gilbert on 18/07/2017.
 */

public class TermKmData {

    private String id;
    private String id_code_promo;
    private String start_km;
    private String end_km;
    private String amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_code_promo() {
        return id_code_promo;
    }

    public void setId_code_promo(String id_code_promo) {
        this.id_code_promo = id_code_promo;
    }

    public String getStart_km() {
        return start_km;
    }

    public void setStart_km(String start_km) {
        this.start_km = start_km;
    }

    public String getEnd_km() {
        return end_km;
    }

    public void setEnd_km(String end_km) {
        this.end_km = end_km;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
