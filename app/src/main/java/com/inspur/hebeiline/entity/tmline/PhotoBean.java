package com.inspur.hebeiline.entity.tmline;

import java.util.Date;

public class PhotoBean {
	
	private String photoString;
	private String gjId;
	private String routeID;
	private String userId;
	private String uid;
	private String relatedID;
	private String photoType;
	private String resourceType;//资源类型
	private Double latitude;// 照片形成时所处的纬度
	private Double longitude;// 照片形成时所处的经度
	private Date createTime;
	private String photoName;// 照片名
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getPhotoString() {
		return photoString;
	}
	public void setPhotoString(String photoString) {
		this.photoString = photoString;
	}
	public String getGjId() {
		return gjId;
	}
	public void setGjId(String gjId) {
		this.gjId = gjId;
	}
	public String getRouteId() {
		return routeID;
	}
	public void setRouteId(String routeId) {
		this.routeID = routeId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRelatedID() {
		return relatedID;
	}
	public void setRelatedID(String relatedID) {
		this.relatedID = relatedID;
	}
	public String getPhotoType() {
		return photoType;
	}
	public void setPhotoType(String photoType) {
		this.photoType = photoType;
	}
	

}
