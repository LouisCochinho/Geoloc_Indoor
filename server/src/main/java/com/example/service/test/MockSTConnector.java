package com.example.service.test;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.domain.Beacon;
import com.example.domain.Measurement;
import com.example.service.AmazonService;

// TODO Remove @Component when using real devices
// @Component
public class MockSTConnector {

	@Autowired
	private AmazonService amazonService;
	
	@PostConstruct
	public void init(){
		System.out.println("Simulating connection to 3 devices ..");
		connectToThreeDevices();
		
	}
	
	public Beacon convertStringToBeacon(String parts[], String id){
		
		if(parts.length !=4){
			throw new RuntimeException("Incorrect number of parts");
		}
		
		Beacon beacon = new Beacon();
		double value = Double.parseDouble(parts[1]);
		beacon.setDeviceId(value);
		beacon.setNameOfDevice(parts[3]);
		
		double signalStrength = Double.parseDouble(parts[2]);
		
		beacon.setIdPlaca(id);
		beacon.setMeasurement(signalStrength);
		return beacon;
	}
	
	public  Runnable simulatePlaca(String idPlaca, String deviceId){
		return () -> {
			
			while(true){
				try {
					Thread.sleep(5000);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String value1 = String.valueOf(new Random().nextDouble());	
				String value2 = UUID.randomUUID().toString();
				String[] parts = new String[]{"", deviceId , value1,value2};
				
		
				Beacon beacon = convertStringToBeacon(parts, idPlaca);
				beacon.setReceivedDate(LocalDateTime.now());
				amazonService.sendBeacon(beacon);
				
			}
			
			
		};
	}
	
	public boolean connectToThreeDevices(){
		Thread placa11 = new Thread(simulatePlaca("placa4", "123"));
		Thread placa21 = new Thread(simulatePlaca("placa5", "123"));
		Thread placa31 = new Thread(simulatePlaca("placa6", "123"));
		
		Thread placa12 = new Thread(simulatePlaca("placa4", "163"));
		Thread placa22 = new Thread(simulatePlaca("placa5", "163"));
		Thread placa32 = new Thread(simulatePlaca("placa6", "163"));
		
		placa11.start();
		placa21.start();
		placa31.start();
		placa12.start();
		placa22.start();
		placa32.start();
		
		return true;
	}
	

}
