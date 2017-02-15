package com.imag.air.geoloc_indoor;

import android.annotation.TargetApi;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.SparseBooleanArray;
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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.bonuspack.overlays.FolderOverlay;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imag.air.geoloc_indoor.domain.BeaconId;
import com.imag.air.geoloc_indoor.domain.LocationHistory;
import com.imag.air.geoloc_indoor.service.AmazonService;
import com.imag.air.geoloc_indoor.service.LocationService;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * The MapView of the activity
     */
    private MapView mMapView;

    /**
     * A boolean to hide/show all markers
     */
    private boolean beaconOverlayVisible;

    /**
     * URL of the Air Lab
     */
    public static final String AIR_WEBSITE = "http://air.imag.fr";

    /**
     * GeoPoint on which the map is centered
     */
    private GeoPoint hello = new GeoPoint(45.1846431, 5.7526904);

    /**
     * Overlay containing all the markers
     */
    private FolderOverlay mOverlay;

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
     * Map to store marker with associated id
     */
    private Map<Long, Marker> markerMap;

    private LocationService locationService;


    // If network is off, the application displays an AlertDialog and closes


    private boolean isGeolocated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Requesting beacon list
        new HttpRequestBeacons().execute();

        markerMap = new HashMap<>();

        lvBeacon = (ListView) findViewById(R.id.lv_beacons);

      /*
      bla = new BeaconListAdapter(getTestingBeaconIdList(15));
      lvBeacon.setAdapter(bla);
      */

        beaconOverlayVisible = false;

        // Check internet connection
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        TextView alertTV = new TextView(this);


        alertTV.setText(R.string.need_internet);
        alertTV.setPadding(16, 0, 16, 0);
        alert.setTitle(R.string.no_internet_connection)
                .setView(alertTV)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       //TODO
                    }
                });



        if (!networkChecking()) {
            AlertDialog dialog = alert.create();
            dialog.show();
        }

        // FloatingActionButton to show/hide all markers
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        // Géolocalisation

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

           /* if (!beaconOverlayVisible)
            {
               mOverlay.setEnabled(beaconOverlayVisible);
               mOverlay.closeAllInfoWindows();
               beaconOverlayVisible = true;
               mMapView.invalidate();
            } else
            {
               mOverlay.setEnabled(beaconOverlayVisible);
               beaconOverlayVisible = false;
               mMapView.invalidate();
            }*/

                // Boite de dialogue choix de géolocalisation
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.activate_geolocation)
                        .setMessage(R.string.choose_gps_or_bluetooth_geoloc)
                        .setPositiveButton(R.string.gps, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                locationService = new LocationService(MainActivity.this);
                                if (locationService.canGetLocation()) {
                                    isGeolocated = true;
                                    locationService.displayCoordinates();
                                    LocationHistory lh = new LocationHistory(8, "Me", locationService.getLatitude(), locationService.getLongitude());
                                    placeMe(lh);
                                } else {
                                    locationService.showSettingsAlert();
                                }
                            }
                        })
                        .setNegativeButton(R.string.bluetooth, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Bluetooth
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);

        // Map initialisation
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setMultiTouchControls(true);
        mMapView.getController().setZoom(14);
        mMapView.getController().setCenter(hello);

        mOverlay = new FolderOverlay(this);
        mMapView.getOverlays().add(mOverlay);

        LocationHistory l1, l2, l3, l4, l5, l6, l7;

        l1 = new LocationHistory(1, "Testing Beacon 1", 45.1846431, 5.7526904);
        l2 = new LocationHistory(2, "Testing Beacon 2", 45.1845412, 5.7543592);
        l3 = new LocationHistory(3, "Testing Beacon 3", 45.1935769, 5.7680371);
        l4 = new LocationHistory(4, "Testing Beacon 4", 45.1841286, 5.7554077);
        l5 = new LocationHistory(5, "Testing Beacon 5", 45.1833388, 5.7536574);
        l6 = new LocationHistory(6, "Testing Beacon 6", 45.1857809, 5.7514625);
        l7 = new LocationHistory(7, "Testing Beacon 7", 45.1853263, 5.758263);

        placeNewMarker(l1);
        placeNewMarker(l2);
        placeNewMarker(l3);
        placeNewMarker(l4);
        placeNewMarker(l5);
        placeNewMarker(l6);
        placeNewMarker(l7);

    }

    /**
     * Set an icon on a Marker (for API 19 and below)
     *
     * @param m
     */
    @TargetApi(19)
    public void setIcon19(Marker m) {
        m.setIcon(getResources().getDrawable(R.drawable.ic_place_red_600_48dp));
    }

    /**
     * Set an icon on a Marker (for API 21 and higher)
     *
     * @param m
     */
    @TargetApi(21)
    public void setIcon21(Marker m) {
        m.setIcon(getDrawable(R.drawable.ic_place_red_600_48dp));
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
                    bla.activeAll();
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * Check if the network is available on the device
     */
    private boolean networkChecking() {
        try {
            ConnectivityManager nInfo = (ConnectivityManager) getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            nInfo.getActiveNetworkInfo().isConnectedOrConnecting();
            ConnectivityManager cm = (ConnectivityManager) getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * AsyncTask to request the available beacon list
     */
    private class HttpRequestBeacons extends AsyncTask<String, Object, List<BeaconId>> {
        @Override
        protected List<BeaconId> doInBackground(String... params) {
            try {
                List<BeaconId> location = AmazonService.getDeviceList();
                return location;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<BeaconId> location) {
            if (location != null) {
                bla = new BeaconListAdapter(location);
                lvBeacon.setAdapter(bla);
            } else
                Toast.makeText(MainActivity.this, "Request to server failed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Place a Marker according to a LocationHistory
     *
     * @param lh
     */
    public void placeNewMarker(LocationHistory lh) {
        Marker mMarker = new Marker(mMapView);
        mMarker.setPosition(new GeoPoint(lh.getLatitude(), lh.getLongitude()));
        mMarker.setTitle(String.valueOf(lh.getLabel()));
        mMarker.setSnippet(getString(R.string.floor) + lh.getLevel() + "\n"
                + "ID : " + lh.getId());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            setIcon19(mMarker);
        else
            setIcon21(mMarker);

        mOverlay.add(mMarker);
        markerMap.put(lh.getId(), mMarker);

        mMapView.invalidate();
    }

    public void removeMarker(double id) {
        mOverlay.remove(markerMap.get(id));
        markerMap.remove(id);
    }

    /**
     * Place Markes according to a LocationHistory list
     *
     * @param locationHistory
     */
    public void placeNewMarker(List<LocationHistory> locationHistory) {
        for (LocationHistory l : locationHistory) {
            Marker mMarker = new Marker(mMapView);
            mMarker.setPosition(new GeoPoint(l.getLatitude(), l.getLongitude()));
            mMarker.setTitle(String.valueOf(l.getLabel()));
            mMarker.setSnippet(getString(R.string.floor) + l.getLevel() + "\n" + "ID : " + l.getId());

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
                setIcon19(mMarker);
            else
                setIcon21(mMarker);

            mOverlay.add(mMarker);
            markerMap.put(l.getId(), mMarker);
        }
        mMapView.invalidate();
    }

    public void placeMe(LocationHistory lh) {
        Marker mMarker = new Marker(mMapView);
        mMarker.setPosition(new GeoPoint(lh.getLatitude(), lh.getLongitude()));
        mMarker.setTitle(String.valueOf(lh.getLabel()));
        mMarker.setSnippet(getString(R.string.floor) + lh.getLevel() + "\n" + "ID : " + lh.getId());
        mMarker.setIcon(getResources().getDrawable(R.drawable.ic_place_white_48dp));
        mOverlay.add(mMarker);
        markerMap.put(lh.getId(), mMarker);

        mMapView.invalidate();
    }

    /**
     * Generate a BeaconId list to test the lvBeacon ListView
     *
     * @param n The number of beacons
     * @return A list with n BeaconId
     */
    private List<BeaconId> getTestingBeaconIdList(int n) {
        List<BeaconId> r = new ArrayList<>();
        for (int i = 0; i < n; i++)
            r.add(new BeaconId(i, "Testing Beacon" + i));

        return r;
    }

    /**
     * Adapter view for the beacon subscription list
     */
    public class BeaconListAdapter extends BaseAdapter {
        private List<BeaconId> list;
        private SparseBooleanArray mCheckStates;

        public BeaconListAdapter(List<BeaconId> list) {
            this.list = list;
            this.mCheckStates = new SparseBooleanArray();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.beacon_list_item, null);

                holder = new ViewHolder();

                holder.name = (TextView) view.findViewById(R.id.tv_name);
                holder.id = (TextView) view.findViewById(R.id.tv_id);
                holder.cb = (CheckBox) view.findViewById(R.id.cb_check);
                holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            // CheckBox have been checked, let's add the value in the table !
                            mCheckStates.put((Integer) buttonView.getTag(), isChecked);
                            // TODO Subscribe MQTT
                            // placeNewMarker
                        } else {
                            mCheckStates.delete((Integer) buttonView.getTag());
                            // TODO Unsubscribe MQTT
                            // removeMarker
                        }
                    }
                });

                view.setTag(holder);
                view.setTag(R.id.cb_check, holder.cb);
                view.setTag(R.id.tv_id, holder.id);
                view.setTag(R.id.tv_name, holder.name);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            BeaconId b = (BeaconId) getItem(i);

            if (b != null) {
                holder.name.setText(b.getNameOfDevice());
                holder.id.setText(String.valueOf(b.getDeviceId()));
            }

            holder.cb.setTag(i);
            holder.cb.setChecked(mCheckStates.get(i));

            return view;
        }

        public void activeAll() {
            if (list == null || list.size() == 0)
                return;

            if (mCheckStates.size() == list.size()) {
                mCheckStates.clear();
                notifyDataSetChanged();
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (!mCheckStates.get(i))
                        mCheckStates.put(i, true);
                }
                notifyDataSetChanged();
            }
        }
    }

    private static class ViewHolder {
        public TextView name;
        public TextView id;
        public CheckBox cb;
    }
}
