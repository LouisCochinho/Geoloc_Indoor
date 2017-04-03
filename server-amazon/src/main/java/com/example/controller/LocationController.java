package com.example.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.validation.Valid;

import org.geojson.GeoJsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Beacon;
import com.example.domain.BeaconId;
import com.example.domain.LocationHistory;
import com.example.domain.LocationSTm;
import com.example.service.LocationService;

@RestController
@RequestMapping("devices")
public class LocationController {

	@Autowired
	private LocationService locationService;

	@RequestMapping(value = "/{deviceId}/locationHistory", method = RequestMethod.POST)
	public LocationHistory addDeviceLocation(@PathVariable double deviceId,
			@RequestBody @Valid LocationHistory location) {
		return locationService.addDeviceLocation(deviceId, location);
	}

	@RequestMapping(value = "/{deviceId}/location", method = RequestMethod.POST)
	public Beacon addBeaconInformation(@PathVariable String deviceId,
			@RequestBody @Valid Beacon beacon) {
		return locationService.addBeaconInformation(beacon);
	}

	@RequestMapping(value = "/locationSTm", method = RequestMethod.POST)
	public LocationSTm addSTmLocation(
			@RequestBody @Valid LocationSTm locationSTm) {
		return locationService.addLocationSTm(locationSTm);
	}

	@RequestMapping(value = "/{STmId}/deleteSTm", method = RequestMethod.DELETE)
	public void deleteSTmLocation(@PathVariable String STmId) {
		 locationService.deleteStm(STmId);
	}
	@RequestMapping(value = "/{deviceId}/history", method = RequestMethod.GET)
	public List<LocationHistory> getDevicePositionHistory(
			@PathVariable double deviceId,
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "20") int size) {
		return locationService.getDeviceHistory(deviceId, page, size);
	}

	@RequestMapping(value = "/{deviceId}/locationFin", method = RequestMethod.GET)
	public LocationHistory getSTmLocation(@PathVariable double deviceId) {

		LocationHistory beacon = locationService.getDevicePosition(deviceId);
		if (beacon != null) {
			return beacon;
		}
		return null;
	}

	@RequestMapping(value = "/files/{fileName}", method = RequestMethod.GET)
	public HttpEntity<byte[]> createPdf(
			@PathVariable("fileName") String fileName) throws IOException {
		Path path = Paths.get("sample.xml");
		byte[] documentBody = Files.readAllBytes(path);

		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "xml"));
		header.set("Content-Disposition",
				"attachment; filename=" + fileName.replace(" ", "_"));
		header.setContentLength(documentBody.length);

		return new HttpEntity<byte[]>(documentBody, header);
	}

	@RequestMapping(value = "/listBeacons", method = RequestMethod.GET)
	public List<BeaconId> getListOfBeacons() {
		return locationService.getListOfBeacons();
	}
	@RequestMapping(value = "/beacons", method = RequestMethod.GET)
	public List<Beacon> getBeacons() {
		return locationService.getBeacons();
	}
}
