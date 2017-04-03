package com.example.service;

import java.util.Arrays;
import java.util.Scanner;

import jssc.SerialPortList;

public class SerialSelector {

	public static String selectPort() {

		Arrays.asList(SerialPortList.getPortNames()).forEach(
				System.out::println);
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please type the port to connect");

		String selectedPort = scanner.nextLine();

		scanner.close();

		return selectedPort;
	}

}
