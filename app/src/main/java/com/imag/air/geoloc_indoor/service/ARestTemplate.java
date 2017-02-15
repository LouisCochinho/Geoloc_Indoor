package com.imag.air.geoloc_indoor.service;

import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Diana Stoian on 02.04.2016.
 */
public class ARestTemplate {

    private static final ExecutorService executor = Executors.newFixedThreadPool(16);
    private static final RestTemplate restTemplate  = new RestTemplate();
    static {
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    public static <T> T getForObject(final String url, final Class<T> objectType){

        try{
            return restTemplate.getForObject(url, objectType) ;
        } catch (HttpClientErrorException ex) {
            Log.d("Rest Exception", ex.getResponseBodyAsString());
        }

        return null;

    }
}

