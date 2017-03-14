package com.imag.air.geoloc_indoor.views;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.imag.air.geoloc_indoor.R;
import com.imag.air.geoloc_indoor.viewmodels.BeaconViewModel;

import java.util.List;

/**
 * Created by louis on 13/03/2017.
 */
/**
 * Adapter view for the beacon subscription list
 */
public class BeaconListAdapter extends BaseAdapter {

    private List<BeaconViewModel> beaconItems;
    private SparseBooleanArray mCheckStates;

    public BeaconListAdapter(List<BeaconViewModel> beaconItems) {
        this.beaconItems = beaconItems;
        this.mCheckStates = new SparseBooleanArray();
    }

    public BeaconListAdapter(){
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public void addBeacon(BeaconViewModel bvm){
        this.beaconItems.add(bvm);
    }

    public void setBeaconItems(List<BeaconViewModel> beaconItems) {
        this.beaconItems = beaconItems;
    }

    public View getView(int i, View view, ViewGroup viewGroup, Context context, final MyMap map) {
        ViewHolder holder;

        if (view == null) {

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

                        mCheckStates.put((Integer) checkBox.getTag(), checkBox.isChecked());
                        // TODO Subscribe MQTT
                        // placeNewMarker
                        map.placeNewBeaconMarker((BeaconViewModel) getItem((Integer)checkBox.getTag()));
                    } else {
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
    public void activeAll(MyMap map) {

        if (beaconItems == null || beaconItems.size() == 0)
            return;

        if (mCheckStates.size() == beaconItems.size()) {
            mCheckStates.clear();
            notifyDataSetChanged();
            map.removeAllMarkers();
        } else {
            for (int i = 0; i < beaconItems.size(); i++) {
                if (!mCheckStates.get(i))
                    mCheckStates.put(i, true);
            }
            notifyDataSetChanged();
            map.placeNewBeaconMarkers(beaconItems);
        }
        map.getMapView().invalidate();
    }

    private static class ViewHolder {
        public TextView name;
        public TextView id;
        public CheckBox cb;
    }
}
