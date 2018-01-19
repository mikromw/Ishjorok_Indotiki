package com.pmberjaya.indotiki.callbacks.bookingNew;

import com.pmberjaya.indotiki.base.BaseCallback;
import com.pmberjaya.indotiki.models.bookingNew.DistanceData;
import com.pmberjaya.indotiki.models.bookingNew.PriceEstimateData;

public class TimeFeeCallback extends BaseCallback {
	private String price;
	private String total_item_price;
	private String deposit_paid;
	private String cash_paid;
	private Boolean default_promo;
	private String balance;
	private String original_price;
	private String total_price;
	private String promo_price;
	private String overflow_km_fee;
	private String promo_category_voucher;
	private String kode_promo;
	private Boolean code_promo_valid;
	private String term_km;
	private PriceEstimateData price_est;
	private DistanceData distance;
	private String polyline;

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTotal_item_price() {
		return total_item_price;
	}

	public void setTotal_item_price(String total_item_price) {
		this.total_item_price = total_item_price;
	}

	public String getDeposit_paid() {
		return deposit_paid;
	}

	public void setDeposit_paid(String deposit_paid) {
		this.deposit_paid = deposit_paid;
	}

	public String getCash_paid() {
		return cash_paid;
	}

	public void setCash_paid(String cash_paid) {
		this.cash_paid = cash_paid;
	}

	public Boolean isDefault_promo() {
		return default_promo;
	}

	public void setDefault_promo(Boolean default_promo) {
		this.default_promo = default_promo;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(String original_price) {
		this.original_price = original_price;
	}

	public String getTotal_price() {
		return total_price;
	}

	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}

	public String getPromo_price() {
		return promo_price;
	}

	public void setPromo_price(String promo_price) {
		this.promo_price = promo_price;
	}

	public String getOverflow_km_fee() {
		return overflow_km_fee;
	}

	public void setOverflow_km_fee(String overflow_km_fee) {
		this.overflow_km_fee = overflow_km_fee;
	}

	public String getPromo_category_voucher() {
		return promo_category_voucher;
	}

	public void setPromo_category_voucher(String promo_category_voucher) {
		this.promo_category_voucher = promo_category_voucher;
	}

	public String getKode_promo() {
		return kode_promo;
	}

	public void setKode_promo(String kode_promo) {
		this.kode_promo = kode_promo;
	}

	public Boolean isCode_promo_valid() {
		return code_promo_valid;
	}

	public void setCode_promo_valid(Boolean code_promo_valid) {
		this.code_promo_valid = code_promo_valid;
	}

	public String getTerm_km() {
		return term_km;
	}

	public void setTerm_km(String term_km) {
		this.term_km = term_km;
	}

	public PriceEstimateData getPrice_est() {
		return price_est;
	}

	public void setPrice_est(PriceEstimateData price_est) {
		this.price_est = price_est;
	}

	public DistanceData getDistance() {
		return distance;
	}

	public void setDistance(DistanceData distance) {
		this.distance = distance;
	}

	public String getPolyline() {
		return polyline;
	}

	public void setPolyline(String polyline) {
		this.polyline = polyline;
	}
}