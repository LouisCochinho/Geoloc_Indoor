package com.imag.air.geoloc_indoor.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.imag.air.geoloc_indoor.R;
import com.imag.air.geoloc_indoor.controllers.BeaconLocationController;
import com.imag.air.geoloc_indoor.controllers.UserLocationController;
import com.imag.air.geoloc_indoor.services.NetworkService;
import com.imag.air.geoloc_indoor.viewmodels.BeaconViewModel;
import com.imag.air.geoloc_indoor.viewmodels.UserLocationViewModel;
import com.imag.air.geoloc_indoor.views.BeaconListAdapter;
import com.imag.air.geoloc_indoor.views.MyMap;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.List;


//TODO : change methods visibility to private
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    /**
     * The MapView of the activity
     */
    private MyMap map;

    /**
     * A boolean to hide/show all markers
     */
    private boolean beaconOverlayVisible;

    /**
     * URL of the Air Lab
     */
    public static final String AIR_WEBSITE = "http://air.imag.fr";

    /**
     * ListView of available Beacons
     */
    private ListView lvBeacon;

    /**
     * Adapter for the lvBeacon ListView
     */
    private BeaconListAdapter bla;

    /**
     * The DrawerLayout (the menu which opens from the left)
     */
    private DrawerLayout drawer;

    /**
     * viewModel for user location
     */
    private UserLocationViewModel userLocation;

    /**
     *  controller for user location
     */
    private UserLocationController userLocationController;

    /**
     *  controller for beacon location
     */
    private BeaconLocationController beaconLocationController;

    /**
     * FloatingActionButton
     */
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get xml file
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // If internet is disabled => Enable it (WIFI only)
        if(!NetworkService.networkChecking(MainActivity.this)){
            beaconLocationController.enableInternetConnection(MainActivity.this);
        }

        // If internet is enabled => Try to get beacons from cloud server
        if(NetworkService.networkChecking(MainActivity.this)){
            // Requesting beacon list
            new HttpRequestBeacons().execute();
        }

        // ListView of beacons
        lvBeacon = (ListView) findViewById(R.id.lv_beacons);

        // Create Floating action button + Listener(geolocation)
        initFloatingActionButton();

        // MyMap initialisation
        initMap();

        // BeaconListAdapter creation
        bla = new BeaconListAdapter();

        // drawer and action bar initialisation
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Navigation view initialisation
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // If no internet or request to server failed
    private void addFakeBeacons(){

        bla.addBeacon(new BeaconViewModel(0,"Beacon 1",new GeoPoint(45.1846431, 5.7526904),this.map.getMapView(),MainActivity.this));
        bla.addBeacon(new BeaconViewModel(1,"Beacon 2",new GeoPoint(45.1845412, 5.7543592),this.map.getMapView(),MainActivity.this));
        bla.addBeacon(new BeaconViewModel(2,"Beacon 3",new GeoPoint(45.1935769, 5.7680371),this.map.getMapView(),MainActivity.this));
        bla.addBeacon(new BeaconViewModel(3,"Beacon 4",new GeoPoint(45.1841286, 5.7554077),this.map.getMapView(),MainActivity.this));
        bla.addBeacon(new BeaconViewModel(4,"Beacon 5",new GeoPoint(45.1833388, 5.7536574),this.map.getMapView(),MainActivity.this));
        bla.addBeacon(new BeaconViewModel(5,"Beacon 6",new GeoPoint(45.1857809, 5.7514625),this.map.getMapView(),MainActivity.this));
        bla.addBeacon(new BeaconViewModel(6,"Beacon 7",new GeoPoint(45.1853263, 5.758263),this.map.getMapView(),MainActivity.this));

        lvBeacon.setAdapter(bla);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_website:
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AIR_WEBSITE));
                startActivity(myIntent);
                break;
            case R.id.nav_manage:
                Snackbar.make(drawer, "Feature in development", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.nav_des_select_all:
                if (bla != null)
                    bla.activeAll(map);
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * AsyncTask to request the available beacon list
     */
    private class HttpRequestBeacons extends AsyncTask<String, Object, List<BeaconViewModel>> {
        @Override
        protected List<BeaconViewModel> doInBackground(String... params) {
            try {
                List<BeaconViewModel> beaconList = beaconLocationController.getAvailableBeacons();
                return beaconList;
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * Post execution of the HttpRequestBeacons above
         * @param beaconList
         */
        @Override
        protected void onPostExecute(List<BeaconViewModel> beaconList) {
            if (beaconList != null) {
                bla.setBeaconItems(beaconList);
                lvBeacon.setAdapter(bla);
            } else {
                Toast.makeText(MainActivity.this, "Request to server failed, fake beacons added", Toast.LENGTH_LONG).show();
                addFakeBeacons();
                lvBeacon.setAdapter(bla);

            }
        }
    }


    private void subscribeAll(){
        //TODO
    }

    private void unsubscribeAll(){
        // TODO
    }


    // Create FloatingActionButton + Listener
    private void initFloatingActionButton(){
        // FloatingActionButton(in the bottom right) to geolocate user
        this.fab = (FloatingActionButton) findViewById(R.id.fab);

        // Géolocalisation
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLocationController.getLocation();
            }
        });
    }


    // Create map Object
    private void initMap(){
        this.map = new MyMap(MainActivity.this, (MapView) findViewById(R.id.map));
    }
}
