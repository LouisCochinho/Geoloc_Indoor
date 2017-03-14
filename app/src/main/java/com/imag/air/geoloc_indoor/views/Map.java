package com.imag.air.geoloc_indoor.views;

import android.content.Context;

import com.imag.air.geoloc_indoor.viewmodels.BeaconViewModel;
import com.imag.air.geoloc_indoor.viewmodels.UserLocationViewModel;

import org.osmdroid.bonuspack.overlays.FolderOverlay;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 13/03/2017.
 */

public class Map extends MapView{

    private List<Tile> tiles;
    private FolderOverlay mOverlay;

    public Map(Context context) {
        super(context);
        tiles = new ArrayList<>();
        // Get tiles in res directory
    }

    public void placeNewBeaconMarker(BeaconViewModel beaconVM){

    }

    public void placeNewBeaconMarkers(List<BeaconViewModel> beaconsVM){

    }

    public void removeMarker(int index){

    }

    public void removeAllMarkers(){

    }

    public void placeUserMarker(UserLocationViewModel userLocationVM){

    }

    public void removeUserMarker(){

    }

    public List<Tile> getTiles(){
        return tiles;
    }

    public void drawTiles(){

    }
}
