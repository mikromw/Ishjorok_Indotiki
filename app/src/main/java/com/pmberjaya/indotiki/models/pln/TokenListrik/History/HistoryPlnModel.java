package com.pmberjaya.indotiki.models.pln.TokenListrik.History;

import java.util.List;

/**
 * Created by Gilbert on 12/4/2017.
 */

public class HistoryPlnModel {

    private List<HistoryPlnData> result = null;
    private String total;

    public List<HistoryPlnData> getResult() {
        return result;
    }
    public void setResult(List<HistoryPlnData> result) {
        this.result = result;
    }
    public String getTotal() {
        return total;
    }
    public void setTotal(String total) {
        this.total = total;
    }
}