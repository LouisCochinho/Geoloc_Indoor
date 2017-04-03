package com.example.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import com.example.domain.Beacon;
import com.example.domain.Measurement;

public class STEventListener implements SerialPortEventListener {

	private SerialPort port;
	private AmazonService amazonService;
	
	
	private boolean receivedMessage = false;
	StringBuilder message = new StringBuilder();

	public STEventListener(SerialPort port, AmazonService amazonService) {
		this.port = port;
		this.amazonService = amazonService;
	}
	

	public Beacon convertStringToBeacon(String parts[]){
		
		if(parts.length != 4){
			throw new RuntimeException("Incorrect number of parts");
		}
		
		Beacon beacon = new Beacon();
		long deviceID = Long.decode("0x"+parts[1].replace(' ','0'));
		System.out.println("ID: " +deviceID);
		beacon.setDeviceId(deviceID);
//		System.out.println("pendant la trauction1");
		double signalStrength = Double.parseDouble(parts[2]);
//		System.out.println("pendant la trauction2");
		beacon.setIdPlaca(parts[0]);
		beacon.setMeasurement(signalStrength);
//		System.out.println("pendant la trauction3");
		beacon.setNameOfDevice(parts[3]);
//		System.out.println("pendant la trauction4");
		return beacon;
	}
	
	public void serialEvent(SerialPortEvent event) {

		if (event.isRXCHAR()) {

			try {
				//System.out.println("received nr bytes : " + event.getEventValue());
				
				byte[] receivedBytes = port.readBytes(event.getEventValue()); //citim exact cate caractere primim
				//System.out.println("Received bytes : " + new String(receivedBytes, "UTF-8")); //se goleste buffer-ul
				for (byte b : receivedBytes)
				{
					if(b == '>')
					{
						receivedMessage = true;
						message.setLength(0);
					}
					else if(receivedMessage == true){
						if(b == '\n')
						{
							receivedMessage = false;
							String messageReceived = message.toString();
							String[] parts = messageReceived.split("#");
							for(int i = 0; i<parts.length;i++){
								System.out.println(parts[i]);
							}
							
							try{
								Beacon beacon = convertStringToBeacon(parts);
								beacon.setReceivedDate(LocalDateTime.now());
								amazonService.sendBeacon(beacon);
								
								
							} catch(RuntimeException e){
								System.out.println(e);
							}
							
						}
						else{
							message.append((char)b);
						}
					}
				}
				//System.out.println(stringExtractor(receivedBytes));
			
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			//}
		}

		else if (event.isCTS()) {// If CTS line has changed state
			if (event.getEventValue() == 1) {// If line is ON
				System.out.println("CTS - ON");
			} else {
				System.out.println("CTS - OFF");
			}
		} else if (event.isDSR()) {// /If DSR line has changed state
			if (event.getEventValue() == 1) {// If line is ON
				System.out.println("DSR - ON");
			} else {
				System.out.println("DSR - OFF");
			}
		}
	}
}
