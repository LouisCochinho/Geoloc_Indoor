package com.imag.air.geoloc_indoor.views.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.imag.air.geoloc_indoor.R;
import com.imag.air.geoloc_indoor.controllers.BeaconLocationController;
import com.imag.air.geoloc_indoor.controllers.UserLocationController;
import com.imag.air.geoloc_indoor.services.NetworkService;
import com.imag.air.geoloc_indoor.viewmodels.BeaconViewModel;
import com.imag.air.geoloc_indoor.viewmodels.UserLocationViewModel;
import com.imag.air.geoloc_indoor.views.MyMap;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;


import java.util.ArrayList;
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
    private boolean beaconOverlayVisible = false;

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

    /**
     * FloatingActionButton
     */
    private FloatingActionButton fab_floor;

    /**
     * context
     */
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set context
        this.context = MainActivity.this;

        // initializing BeaconListAdapter
        bla = new BeaconListAdapter();

        // initializing controllers
        userLocationController = new UserLocationController(context);
        beaconLocationController = new BeaconLocationController();

        // get xml file
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // If internet is disabled => Enable it (WIFI only)
        boolean networkEnabled = NetworkService.networkChecking(context);
        Log.i("CHECK_NETWORK","networkEnabled : "+networkEnabled);

        if(!NetworkService.networkChecking(context)){
            Log.i("ENABLE_NETWORK","enable network process...");
            beaconLocationController.enableInternetConnection(context);
            networkEnabled = NetworkService.networkChecking(context);
            Log.i("CHECK_NETWORK_2","networkEnabled : "+networkEnabled);
        }

        // ListView of beacons
        lvBeacon = (ListView) findViewById(R.id.lv_beacons);

        if(lvBeacon != null){
            Log.i("LVBEACON_INITIALIZED","lvbeacon initialized.");
        }
        else{
            Log.e("LVBEACON_INITIALIZED","lvbeacon null");
        }

        // Create Floating action button + Listener(geolocation) + fab Floor + Change Floor
        initFloatingActionButton();
       // initFloatingActionButtonFloor();
        if(fab!= null){
            Log.i("FAB_INITIALIZED","fab initialized.");
        }
        else{
            Log.e("FAB_INITIALIZED","fab null");
        }

        // MyMap initialisation
        initMap();
        if(fab!= null){
            Log.i("MAP_INITIALIZED","map initialized.");
        }
        else{
            Log.e("MAP_INITIALIZED","map null");
        }
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

        // If internet is enabled => Try to get beacons from cloud server
        if(NetworkService.networkChecking(context)){
            // Requesting beacon list
            Log.i("BEACONS_REQUEST","requesting beacon list");
            new HttpRequestBeacons().execute();
        }
    }

    // If no internet or request to server failed
    private void addFakeBeacons(){
        if(bla.getBeaconItems().isEmpty()){
            bla.addBeacon(new BeaconViewModel(0,"Beacon 1",new GeoPoint(45.1846431, 5.7526904),this.map.getMapView(),context));
            bla.addBeacon(new BeaconViewModel(1,"Beacon 2",new GeoPoint(45.1845412, 5.7543592),this.map.getMapView(),context));
            bla.addBeacon(new BeaconViewModel(2,"Beacon 3",new GeoPoint(45.1935769, 5.7680371),this.map.getMapView(),context));
            bla.addBeacon(new BeaconViewModel(3,"Beacon 4",new GeoPoint(45.1841286, 5.7554077),this.map.getMapView(),context));
            bla.addBeacon(new BeaconViewModel(4,"Beacon 5",new GeoPoint(45.1833388, 5.7536574),this.map.getMapView(),context));
            bla.addBeacon(new BeaconViewModel(5,"Beacon 6",new GeoPoint(45.1857809, 5.7514625),this.map.getMapView(),context));
            bla.addBeacon(new BeaconViewModel(6,"Beacon 7",new GeoPoint(45.1853263, 5.758263),this.map.getMapView(),context));

            lvBeacon.setAdapter(bla);
        }
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
            case R.id.nav_refresh:
                new HttpRequestBeacons().execute();
                break;
            case R.id.nav_des_select_all:
                if (bla != null)
                    if(!beaconOverlayVisible)
                        bla.enableAll();
                    else
                        bla.disableAll();
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * AsyncTask to request the available beacon list
     */
    public class HttpRequestBeacons extends AsyncTask<String, Object, List<BeaconViewModel>> {
        @Override
        protected List<BeaconViewModel> doInBackground(String... params) {
            try {
                Log.i("GET_BEACONS","getting available beacons...");
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
                Log.i("GET_BEACONS_SUCCESS","got beacons successfully.");
                bla.setBeaconItems(beaconList);
                lvBeacon.setAdapter(bla);
            } else {
                Log.i("GET_BEACONS_FAILED","got beacons failed, add fake beacons instead.");
                Toast.makeText(context, "Request to server failed, fake beacons added", Toast.LENGTH_LONG).show();
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


    /*private void initFloatingActionButtonFloor(){
        this.fab_floor = (FloatingActionButton)findViewById(R.id.fab_floor);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : Change floor view
                Snackbar.make(drawer, "Change floor view : Feature in development", Snackbar.LENGTH_LONG).show();
            }
        });
    }*/

    // Create FloatingActionButton + Listener
    private void initFloatingActionButton(){
        Log.i("INIT_FAB","initializing Fab...");
        // FloatingActionButton(in the bottom right) to geolocate user
        this.fab = (FloatingActionButton) findViewById(R.id.fab);

        // Géolocalisation
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("GET_USER_LOCATION","getting user location...");
                UserLocationViewModel userLocationVM = null;
                try {
                    userLocationVM = userLocationController.getLocation(context,map.getMapView());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context,"Failed to get your location",Toast.LENGTH_SHORT).show();
                }
                if(userLocationVM != null){
                    map.placeUserMarker(userLocationVM);
                    map.getMapView().invalidate();
                    //fab.setEnabled(false);
                }
            }
        });
    }


    // Create map Object
    private void initMap(){
        Log.i("INIT_MAP","initializing map...");
        this.map = new MyMap(context, (MapView) findViewById(R.id.map),false);
        this.map.setPolytechIcon();
    }

    /**
     * Created by louis on 13/03/2017.
     */
    /**
     * Adapter view for the beacon subscription list
     */
    // TODO : ADD COMs
    private class BeaconListAdapter extends BaseAdapter {

        private List<BeaconViewModel> beaconItems;
        private SparseBooleanArray mCheckStates;

        public BeaconListAdapter() {
            this.beaconItems = new ArrayList<>();
            this.mCheckStates = new SparseBooleanArray();
        }

        @Override
        public int getCount() {
            return beaconItems.size();
        }

        @Override
        public Object getItem(int i) {
            return beaconItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public void addBeacon(BeaconViewModel bvm){
            this.beaconItems.add(bvm);
        }

        public List<BeaconViewModel> getBeaconItems(){
            return this.beaconItems;
        }

        public void setBeaconItems(List<BeaconViewModel> beaconItems) {
            this.beaconItems = beaconItems;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            Log.i("GET_VIEW_LIST_ADAPTER","entering getView method...");
            if (view == null) {
                Log.i("GET_VIEW_LIST_ADAPTER","view == null");
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.beacon_list_item, null);


                holder = new ViewHolder();

                holder.name = (TextView) view.findViewById(R.id.tv_name);
                holder.id = (TextView) view.findViewById(R.id.tv_id);
                holder.cb = (CheckBox) view.findViewById(R.id.cb_check);


                holder.cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox checkBox = (CheckBox)v;
                        if ((checkBox.isChecked())) {
                            // CheckBox have been checked, let's add the value in the table !
                            Log.i("GET_VIEW_LIST_ADAPTER","checkbox is checked, placing marker...");
                            mCheckStates.put((Integer) checkBox.getTag(), checkBox.isChecked());
                            // TODO Subscribe MQTT
                            // placeNewMarker
                            map.placeNewBeaconMarker((BeaconViewModel) getItem((Integer)checkBox.getTag()));
                        } else {
                            Log.i("GET_VIEW_LIST_ADAPTER","checkbox is unchecked remove marker...");
                            mCheckStates.delete((Integer) checkBox.getTag());
                            // TODO Unsubscribe MQTT
                            // removeMarker
                            map.removeBeaconMarker((BeaconViewModel) getItem((Integer)checkBox.getTag()));
                        }
                        map.getMapView().invalidate();
                    }
                });


                view.setTag(holder);
                view.setTag(R.id.cb_check, holder.cb);
                view.setTag(R.id.tv_id, holder.id);
                view.setTag(R.id.tv_name, holder.name);
            } else {
                Log.i("GET_VIEW_LIST_ADAPTER","view != null...");
                holder = (ViewHolder) view.getTag();
            }

            BeaconViewModel b = (BeaconViewModel) getItem(i);

            if (b != null) {
                holder.name.setText(b.getLabel());
                holder.id.setText(String.valueOf(b.getBeaconId()));
            }

            holder.cb.setTag(i);
            holder.cb.setChecked(mCheckStates.get(i));

            return view;
        }

        // Passer la map en paramètre
        public void enableAll() {
            Log.i("ENABLE_ALL", "enableAllBeacon");
            if (beaconItems == null || beaconItems.size() == 0)
                return;

            for (int i = 0; i < beaconItems.size(); i++) {
                if (!mCheckStates.get(i))
                    mCheckStates.put(i, true);
            }
            map.placeNewBeaconMarkers(beaconItems);
            beaconOverlayVisible = true;
            notifyDataSetChanged();
            map.getMapView().invalidate();
        }

        public void disableAll() {

            mCheckStates.clear();
            map.removeAllMarkers();
            beaconOverlayVisible = false;

            notifyDataSetChanged();
            map.getMapView().invalidate();
        }

        private class ViewHolder {
            public TextView name;
            public TextView id;
            public CheckBox cb;
        }
    }
}
