package com.example.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class LocationSTm {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@NotNull
	private String idSTm;
	@NotNull
	private Measurement location;
	@NotNull
	private int level;
	
	public String getIdSTm() {
		return idSTm;
	}
	public void setIdSTm(String idSTm) {
		this.idSTm = idSTm;
	}
	public Measurement getLocation() {
		return location;
	}
	public void setLocation(Measurement location) {
		this.location = location;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	
	

}
