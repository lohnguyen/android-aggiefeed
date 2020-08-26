package com.lohnguyen.aggiefeed.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lohnguyen.aggiefeed.entities.AFActivity;
import com.lohnguyen.aggiefeed.R;

import java.util.List;

public class AFActivityAdapter extends ArrayAdapter<AFActivity> {
    public AFActivityAdapter(Context context, List<AFActivity> androidFlavors) {
        super(context, 0, androidFlavors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent,
                    false);

        AFActivity activity = getItem(position);
        assert activity != null;
        TextView titleView = listItemView.findViewById(R.id.title);
        TextView displayNameView = listItemView.findViewById(R.id.display_name);
        titleView.setText(activity.getTitle());
        displayNameView.setText(activity.getDisplayName());

        return listItemView;
    }
}
