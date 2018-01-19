package com.pmberjaya.indotiki.models.bookingNew;

/**
 * Created by edwin on 22/11/2017.
 */

public class PriceEstimateData {

    private PriceCashData cash;
    private PriceDepositData deposit;

    public PriceCashData getCash() {
        return cash;
    }

    public void setCash(PriceCashData cash) {
        this.cash = cash;
    }

    public PriceDepositData getDeposit() {
        return deposit;
    }

    public void setDeposit(PriceDepositData deposit) {
        this.deposit= deposit;
    }
}
