package com.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class BeaconId {
	private double deviceId;
	private String nameOfDevice;
	public BeaconId(double deviceId,String nameOfDevice){
		this.deviceId = deviceId;
		this.nameOfDevice = nameOfDevice;
	}
	public BeaconId(){
		
	}
	public double getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(double deviceId) {
		this.deviceId = deviceId;
	}
	public String getNameOfDevice() {
		return nameOfDevice;
	}
	public void setNameOfDevice(String nameOfDevice) {
		this.nameOfDevice = nameOfDevice;
	}
}
