package com.imag.air.geoloc_indoor.views;

import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.imag.air.geoloc_indoor.BeaconId;
import com.imag.air.geoloc_indoor.R;
import com.imag.air.geoloc_indoor.viewmodels.BeaconViewModel;
import com.imag.air.geoloc_indoor.views.activities.MainActivity;

import java.util.List;

/**
 * Created by louis on 13/03/2017.
 */

public class BeaconListAdapter extends BaseAdapter {

    private List<BeaconViewModel> list;
    private SparseBooleanArray mCheckStates;

    public BeaconListAdapter(List<BeaconViewModel> list) {
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


    // passer la view et la map en paramètre
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
      /*  ViewHolder holder;

        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.beacon_list_item, null);

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

                        placeNewMarker((Integer)checkBox.getTag());
                    } else {
                        mCheckStates.delete((Integer) checkBox.getTag());
                        // TODO Unsubscribe MQTT
                        // removeMarker
                        removeMarker(((Integer) checkBox.getTag()));
                    }
                    mMapView.invalidate();
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

        */

        return view;
    }

    // Passer la map en paramètre
    public void activeAll() {
        /*
        if (list == null || list.size() == 0)
            return;

        if (mCheckStates.size() == list.size()) {
            mCheckStates.clear();
            notifyDataSetChanged();
            removeAllMarkers();
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (!mCheckStates.get(i))
                    mCheckStates.put(i, true);
            }
            notifyDataSetChanged();
            placeAllMarkers();
        }
        mMapView.invalidate();

        */
    }

    private static class ViewHolder {
        public TextView name;
        public TextView id;
        public CheckBox cb;
    }
}
