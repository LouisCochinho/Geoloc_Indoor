package com.imag.air.geoloc_indoor.views;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.imag.air.geoloc_indoor.R;
import com.imag.air.geoloc_indoor.viewmodels.BeaconViewModel;
import com.imag.air.geoloc_indoor.viewmodels.UserLocationViewModel;

import org.osmdroid.bonuspack.overlays.FolderOverlay;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.TilesOverlay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by louis on 13/03/2017.
 */

public class MyMap {

    // map min zoom level
    public static final int MAP_ZOOM_ZOOM_MIN = 17;
    // map max zoom level
    public static final int MAP_ZOOM_ZOOM_MAX = 20;
    // maps folder name in assets
    public static final String MAP_ASSETS_FOLDER_NAME = "/assets/Maps/Maps/Polytech Indoor Plan 0/";
    private FolderOverlay mOverlay;
    private MapView mapView;
    private Context context;
    private GeoPoint center;
    private int zoomLevel;
    private Map<String, Marker> markers;

    public MyMap(final Context context, MapView mapView,boolean indoorMode) {

        this.mapView = mapView;
        this.context = context;
        this.markers = new HashMap<>();
        this.mOverlay = new FolderOverlay(context);
        mapView.setMultiTouchControls(true);
        mapView.setMaxZoomLevel(20);

        if(indoorMode){
            // Doesn't work
            final MapTileProviderBasic tileProvider =
                    new MapTileProviderBasic(this.context);

            // use path where there are tiles
            final ITileSource tileSource = new XYTileSource("tileSource",17,20,256,".png",new String[]{
                    "http://192.168.56.1:80/Maps/Polytech Indoor Plan 0/",
                    "http://192.168.56.1:80/Maps/Polytech Indoor Plan 1/",
                    "http://192.168.56.1:80/Maps/Polytech Indoor Plan 2/"});

            tileProvider.setTileSource(tileSource);
            final TilesOverlay tilesOverlay = new TilesOverlay(tileProvider,
                    this.context);

            tilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
            mapView.getOverlays().add(tilesOverlay);

        }else{
            mapView.setTileSource(TileSourceFactory.MAPNIK);
        }

        mapView.getOverlays().add(mOverlay);
        this.mapView.setTilesScaledToDpi(true);
        this.zoomLevel = indoorMode ? 18 : 14;
        center = indoorMode ? new GeoPoint(45.184564,5.7529978) : new GeoPoint(45.1846431, 5.7526904);

        mapView.getController().setZoom(zoomLevel);
        mapView.getController().setCenter(center);
    }


    public void setPolytechIcon(){
        //Create polytech icon
        Marker m = new Marker(mapView);
        m.setPosition(new GeoPoint(45.184564,5.7529978));
        m.setTitle("Polytech Grenoble");
        m.setIcon(this.context.getResources().getDrawable(R.drawable.ic_school));
        m.setSnippet("Indoor mode");
        m.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {

                mapView.getController().setZoom(18);
                mapView.getController().setCenter(new GeoPoint(45.184564,5.7529978));
                mapView.invalidate();
                // TODO : Load tiles
                return true;
            }
        });
        this.mOverlay.add(m);
    }

    private void placeMarker(Marker m){
        mOverlay.add(m);
        m.setEnabled(true);
    }

    public Marker getMarkerByTitle(String title){
        List<Overlay> overlays = this.mOverlay.getItems();
        for(Overlay o : overlays){
            Marker m = (Marker)o;
            if(m.getTitle().equals(title)){
                return m;
            }
        }
        return null;
    }

    private void removeMarker(Marker m){
        m.setEnabled(false);
    }

    public void placeNewBeaconMarker(BeaconViewModel beaconVM){
        this.markers.put(beaconVM.getLabel(),beaconVM.getMarker());
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
        Log.i("USER_LOCATION_LABEL","label : "+userLocationVM.getLabel());
        // Remove l'ancien userMarker si existe
        Marker m = markers.get(userLocationVM.getLabel());
        if(m != null){
            Log.i("USER_LOCATION_REMOVE","remove");
            removeMarker(m);
        }
        // put/update marker in hashmap
        markers.put(userLocationVM.getLabel(),userLocationVM.getMarker());

        // ajouter le nouveau marker sur la map
        placeMarker(userLocationVM.getMarker());
    }

    public void removeUserMarker(UserLocationViewModel userLocationVM){
        removeMarker(userLocationVM.getMarker());
    }

    public void drawTiles(){

    }

    public MapView getMapView() {
        return mapView;
    }
}
