package com.example.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geojson.GeoJsonObject;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.example.domain.Beacon;
import com.example.domain.BeaconId;
import com.example.domain.LocationHistory;
import com.example.domain.LocationSTm;
import com.example.repository.LocationHistoryRepository;
import com.example.repository.LocationRepository;
import com.example.repository.LocationSTmRepository;

@Component
public class LocationService {

	@Autowired
	private LocationHistoryRepository locationRepositoryHistory;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private LocationSTmRepository locationSTmRepository;

	public static final double TIME_LIMIT = 10;
	public LocationHistory addDeviceLocation(double deviceId, LocationHistory location) {
		return locationRepositoryHistory.save(location);
	}
	
	public Beacon addBeaconInformation(Beacon beacon){
		return locationRepository.save(beacon);
	}
	
	public LocationSTm addLocationSTm(LocationSTm locationSTm)
	{
		return locationSTmRepository.save(locationSTm);
	}
	
	
	public List<LocationHistory> getDeviceHistory(double deviceId,int page, int size){
		final PageRequest page1 = new PageRequest(
				page, size, Direction.DESC, "date");
		return locationRepositoryHistory.findByDeviceId(deviceId, page1);

	}
	public LocationHistory getDevicePosition(double deviceId){
		//prelevation des coordones des balises de la bases de donnees.
		HashMap<String, Integer> levels = new HashMap<>();
		Map<String, LocationSTm> stmLocations = new HashMap<>();
		List<Beacon> beaconList = locationRepository.findByDeviceId(deviceId); //
		List<Beacon> calculBeacons = new ArrayList<Beacon>(); //as
		LocalDateTime currentDate = LocalDateTime.now();
		if(beaconList.size()>0){
			for(Beacon enreg : beaconList){
				long seconds = ChronoUnit.SECONDS.between(enreg.getReceivedDate(),currentDate);
				if(seconds <=TIME_LIMIT)
				{
					calculBeacons.add(enreg);
					System.out.println("ADDED!");
					System.out.println("Strength of Signal :" +enreg.getMeasurement());
					
				}
				else
				{
					locationRepository.delete(enreg);
				}
			}
		}
		double longitude = 0;
		double latitude = 0;
		String nameOfDevice=null;
		if(calculBeacons.size()>=3)
		{
			double xStm=0;
			double yStm=0;
			int zStm = 0;
			double coordX=0;
			double coordY=0;
			double coordZ=0;
			double sumLatitude =0;
			double sumLongitude = 0;
			double sumLevel = 0;
			double strength;
			double SumStrength =0;
			double altitude = 0;
			for(Beacon beacon : calculBeacons)
			{
				String idSTM = beacon.getIdPlaca();
				LocationSTm locationSTm = locationSTmRepository.findByidSTm(idSTM);
				int level = locationSTm.getLevel();
				stmLocations.put(idSTM, locationSTm);
				levels.put(idSTM, level);
			}
			/*int noOfLevels = levels.size();
			if(noOfLevels == 1)
			{
			}
			*/
			for(Beacon beacon : calculBeacons)
			{
				String idSTM = beacon.getIdPlaca();
				LocationSTm locationSTm = stmLocations.get(idSTM);
				nameOfDevice =beacon.getNameOfDevice();
				xStm =locationSTm.getLocation().getLatitude();
				yStm = locationSTm.getLocation().getLongitude();
				zStm = locationSTm.getLevel();
				System.out.println("XStm : "+xStm);
				System.out.println("YStm : "+yStm);
				strength = beacon.getMeasurement();
				sumLongitude +=strength*xStm;
				sumLatitude+= strength*yStm;
				sumLevel += strength*zStm;
				SumStrength += strength;
			}
			coordX = sumLatitude/SumStrength;
			coordY = sumLongitude/SumStrength;
			coordZ = sumLevel/SumStrength;
			altitude = Math.floor(coordZ);
			System.out.println("COORDONATA X : " + coordX);
			System.out.println("COORDONATA y : " + coordY);
			System.out.println("Coordonnee z : " + coordZ);
				
			
			//LngLatAlt devicePosition = new LngLatAlt(coordX,coordY,altitude); //TODO addLeve
			//GeoJsonObject object = new Point(devicePosition);
			LocationHistory locationHistory = new LocationHistory();
			locationHistory.setDeviceId(deviceId);
			locationHistory.setDate(LocalDateTime.now());
			locationHistory.setLatitude(coordX);
			locationHistory.setLongitude(coordY);
			locationHistory.setLevel((int)altitude);
			locationHistory.setNameOfDevice(nameOfDevice);
			locationRepositoryHistory.save(locationHistory);
			System.out.println("SAVED !\n");
			for(Beacon b : calculBeacons){
				
				locationRepository.delete(b);
			}
			
			return locationHistory;

			
		}
		return null;
	}
	public void deleteStm(String idStm)
	{
		LocationSTm location = locationSTmRepository.findByidSTm(idStm);
		locationSTmRepository.delete(location);
	}
	public List<BeaconId> getListOfBeacons()
	 {
	  List<LocationHistory> listOfLocations = locationRepositoryHistory.findAll();
	  HashMap<Double, String> singleDevices = new HashMap<>();
	  for(LocationHistory locationDevice : listOfLocations)
	  {
	   singleDevices.put(locationDevice.getDeviceId(), locationDevice.getNameOfDevice());
	  }
	  List<BeaconId> listOfObjectsToSend = new ArrayList<>();
	  for(Entry<Double,String > entry : singleDevices.entrySet()) {
	      Double key = entry.getKey();
	      String value = entry.getValue();
	      listOfObjectsToSend.add(new BeaconId(key, value));
	  }
	  return listOfObjectsToSend;
	 }
	public List<Beacon> getBeacons()
	{
		return locationRepository.findAll();
	}
}
