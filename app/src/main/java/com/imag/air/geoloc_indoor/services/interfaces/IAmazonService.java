package com.imag.air.geoloc_indoor.services.interfaces;

import com.imag.air.geoloc_indoor.models.BeaconModel;
import com.imag.air.geoloc_indoor.viewmodels.BeaconViewModel;

import java.util.List;

/**
 * Created by louis on 13/03/2017.
 */

public interface IAmazonService {

    // CRUD
    List<BeaconModel> getAvailableBeacons();
   /* boolean addBeacon(BeaconModel beacon);
    boolean updateBeacon(BeaconModel beacon);
    boolean deleteBeacon(double beaconId);
    BeaconModel getBeaconById(double beaconId);
*/

}
