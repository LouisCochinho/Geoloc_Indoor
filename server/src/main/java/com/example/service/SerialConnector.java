package com.example.service;

import jssc.SerialPort;
import jssc.SerialPortException;

public class SerialConnector {

	public static SerialPort connect(String port){
		SerialPort serialPort = new SerialPort(port);
	    try {
	        serialPort.openPort();//Open serial port
	        serialPort.setParams(SerialPort.BAUDRATE_9600, 
	                             SerialPort.DATABITS_8,
	                             SerialPort.STOPBITS_1,
	                             SerialPort.PARITY_NONE);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
	       
	    }
	    catch (SerialPortException ex) {
	        System.out.println(ex);
	    }
	    
	    return serialPort;
	}
	
}
