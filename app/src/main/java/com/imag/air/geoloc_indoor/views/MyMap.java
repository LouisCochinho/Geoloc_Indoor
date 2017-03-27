package com.imag.air.geoloc_indoor.views;

import android.content.Context;
import android.util.Log;

import com.imag.air.geoloc_indoor.viewmodels.BeaconViewModel;
import com.imag.air.geoloc_indoor.viewmodels.UserLocationViewModel;

import org.osmdroid.bonuspack.overlays.FolderOverlay;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 13/03/2017.
 */

public class MyMap {

    private List<Tile> tiles;
    private FolderOverlay mOverlay;
    private MapView mapView;
    private Context context;
    private GeoPoint center;
    private int zoomLevel;

    public MyMap(Context context,MapView mapView) {
        this.mapView = mapView;
        tiles = new ArrayList<>();
        this.context = context;
        this.zoomLevel = 14;
        center = new GeoPoint(45.1846431, 5.7526904);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(zoomLevel);
        mapView.getController().setCenter(center);
        mOverlay = new FolderOverlay(context);
        mapView.getOverlays().add(mOverlay);
        // Get tiles in res directory


    }

    private void placeMarker(Marker m){
        mOverlay.add(m);
        m.setEnabled(true);
    }

    private void removeMarker(Marker m){
        m.setEnabled(false);
    }

    public void placeNewBeaconMarker(BeaconViewModel beaconVM){
        placeMarker(beaconVM.getMarker());

    }

    public void placeNewBeaconMarkers(List<BeaconViewModel> beaconsVM){
        for(BeaconViewModel bvm : beaconsVM){
            placeNewBeaconMarker(bvm);
        }
    }

    public void removeBeaconMarker(BeaconViewModel bvm){
        removeMarker(bvm.getMarker());
    }

    public void removeAllMarkers(){
        List<Overlay> overlays = this.mOverlay.getItems();
        for(Overlay o : overlays){
            o.setEnabled(false);
        }

        this.mOverlay.closeAllInfoWindows();
    }

    public void placeUserMarker(UserLocationViewModel userLocationVM){
        placeMarker(userLocationVM.getMarker());
    }

    public void removeUserMarker(UserLocationViewModel userLocationVM){
        removeMarker(userLocationVM.getMarker());
    }

    public List<Tile> getTiles(){
        return tiles;
    }

    public void drawTiles(){

    }

    public MapView getMapView() {
        return mapView;
    }
}
