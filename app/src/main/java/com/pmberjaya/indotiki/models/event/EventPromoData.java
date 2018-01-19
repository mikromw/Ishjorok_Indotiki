package com.pmberjaya.indotiki.models.event;

import android.os.Parcel;
import android.os.Parcelable;

public class EventPromoData implements Parcelable{

	private String id;
	private String avatar;
	private String title;
	private String description;
	private String start_time;
	private String end_time;
	private String contact_website;
	private String avatar_app;
	private String sort;
	private Object district;
	private String category;
	private String page;
	private String date_created;
	private String cover_member;
	private String cover_rider;
	private String code_promo;
	private String terms_and_cond;
	private String how_to_use;

	public EventPromoData(){

	}
	public EventPromoData(Parcel in) {
		id = in.readString();
		avatar = in.readString();
		title = in.readString();
		description = in.readString();
		start_time = in.readString();
		end_time = in.readString();
		contact_website = in.readString();
		avatar_app = in.readString();
		sort = in.readString();
		category = in.readString();
		page = in.readString();
		date_created = in.readString();
		cover_member = in.readString();
		cover_rider = in.readString();
		code_promo = in.readString();
		terms_and_cond = in.readString();
		how_to_use = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(avatar);
		dest.writeString(title);
		dest.writeString(description);
		dest.writeString(start_time);
		dest.writeString(end_time);
		dest.writeString(contact_website);
		dest.writeString(avatar_app);
		dest.writeString(sort);
		dest.writeString(category);
		dest.writeString(page);
		dest.writeString(date_created);
		dest.writeString(cover_member);
		dest.writeString(cover_rider);
		dest.writeString(code_promo);
		dest.writeString(terms_and_cond);
		dest.writeString(how_to_use);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<EventPromoData> CREATOR = new Parcelable.Creator<EventPromoData>() {
		@Override
		public EventPromoData createFromParcel(Parcel in) {
			return new EventPromoData(in);
		}

		@Override
		public EventPromoData[] newArray(int size) {
			return new EventPromoData[size];
		}
	};

	public String getCode_promo() {
		return code_promo;
	}

	public void setCode_promo(String code_promo) {
		this.code_promo = code_promo;
	}

	public String getTerms_and_cond() {
		return terms_and_cond;
	}

	public void setTerms_and_cond(String terms_and_cond) {
		this.terms_and_cond = terms_and_cond;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getContact_website() {
		return contact_website;
	}

	public void setContact_website(String contact_website) {
		this.contact_website = contact_website;
	}

	public String getAvatar_app() {
		return avatar_app;
	}

	public void setAvatar_app(String avatar_app) {
		this.avatar_app = avatar_app;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Object getDistrict() {
		return district;
	}

	public void setDistrict(Object district) {
		this.district = district;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getDate_created() {
		return date_created;
	}

	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}

	public String getCover_member() {
		return cover_member;
	}

	public void setCover_member(String cover_member) {
		this.cover_member = cover_member;
	}

	public String getCover_rider() {
		return cover_rider;
	}

	public void setCover_rider(String cover_rider) {
		this.cover_rider = cover_rider;
	}

	public String getHow_to_use() {
		return how_to_use;
	}

	public void setHow_to_use(String how_to_use) {
		this.how_to_use = how_to_use;
	}
}
