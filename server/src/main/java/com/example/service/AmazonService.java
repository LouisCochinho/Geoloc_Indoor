package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.domain.Beacon;

@Component
public class AmazonService {

	@Value("${amazon.server.url}")
	private String amazonRoot;

	private RestTemplate template = new RestTemplate();
	{
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		list.add(new MappingJackson2HttpMessageConverter());
		template.setMessageConverters(list);
	}

	public static final String ADD_LOCATION_URL = "/devices/%s/location";

	public Beacon sendBeacon(Beacon beacon) {

		String url = new String(amazonRoot
				+ String.format(ADD_LOCATION_URL, beacon.getDeviceId()));

		Beacon response =  template.postForObject(url, beacon, Beacon.class);
		
		System.out.println("BEACON SENT : " + response.getId());
		
		return response;

	}

}
