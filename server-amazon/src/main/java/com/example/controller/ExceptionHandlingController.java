package com.example.controller;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionHandlingController {

	// Specify the name of a specific view that will be used to display the
	// error:
	@ExceptionHandler({ Exception.class })
	public String databaseError(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exceptionAsString = sw.toString();
		return exceptionAsString;
	}

}