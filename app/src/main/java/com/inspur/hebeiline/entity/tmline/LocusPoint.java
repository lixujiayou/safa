package com.inspur.hebeiline.entity.tmline;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.amap.api.maps.model.LatLng;

import java.io.Serializable;

/**
 */
@Entity(tableName = "locus_point")
public class LocusPoint implements Serializable
{
	/**
	 * 
	 */
	@Ignore
	private static final long serialVersionUID = 2535060223773355585L;


	@PrimaryKey(autoGenerate = true)
	private int idAuto;

	private String taskId;

	@ColumnInfo(name = "ids")
	private Integer ID;
	private double latitude;
	private double longitude;

	@Ignore
	private LatLng latLng;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getIdAuto() {
		return idAuto;
	}

	public void setIdAuto(int idAuto) {
		this.idAuto = idAuto;
	}

	public Integer getID() {
		return ID;
	}

	public Double getLatitude() {
		return latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setID(Integer iD) {
		ID = iD;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
		this.latitude = latLng.latitude;
		this.longitude = latLng.longitude;
	}
}
