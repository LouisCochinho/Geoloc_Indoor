package com.example.domain;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.example.serializer.LocalDateTimeDeserializer;
import com.example.serializer.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Class which represents a Beacon, which is inplemented by a STM32 or an
 * Arduino
 */

public class Beacon {
	/**
	 * Identification of the beacon
	 */

	private Long id;

	@NotNull
	private double deviceId;

	@NotNull
	private String idPlaca;
	
	@NotNull
	private String nameOfDevice;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@NotNull
	private LocalDateTime receivedDate;
	
	public LocalDateTime getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(LocalDateTime receivedDate) {
		this.receivedDate = receivedDate;
	}

	/**
	 * force du signal recu of the beacon TODO : Which kind of object ?
	 */
	@NotNull
	private double measurement;

	/*
	 * Connection with the beacon
	 */
	private boolean connection;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(double deviceId) {
		this.deviceId = deviceId;
	}

	public boolean isConnection() {
		return connection;
	}

	public void setConnection(boolean connection) {
		this.connection = connection;
	}

	public String getIdPlaca() {
		return idPlaca;
	}

	public void setIdPlaca(String idPlaca) {
		this.idPlaca = idPlaca;
	}

	public double getMeasurement() {
		return measurement;
	}

	public void setMeasurement(double measurement) {
		this.measurement = measurement;
	}

	public String getNameOfDevice() {
		return nameOfDevice;
	}

	public void setNameOfDevice(String nameOfDevice) {
		this.nameOfDevice = nameOfDevice;
	}
	
}