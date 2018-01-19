package com.pmberjaya.indotiki.models.promo;

import java.util.ArrayList;

/**
 * Created by edwin on 11/09/2017.
 */

public class PromoListModel {
    private ArrayList<PromoListData> general_promo = new ArrayList<>();
    private ArrayList<PromoListData> my_promo = new ArrayList<>();

    public ArrayList<PromoListData> getGeneral_promo() {
        return general_promo;
    }

    public void setGeneral_promo(ArrayList<PromoListData> general_promo) {
        this.general_promo = general_promo;
    }

    public ArrayList<PromoListData> getMy_promo() {
        return my_promo;
    }

    public void setMy_promo(ArrayList<PromoListData> my_promo) {
        this.my_promo = my_promo;
    }
}