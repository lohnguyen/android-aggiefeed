package com.example.aggiefeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AFActivityAdapter extends ArrayAdapter<AFActivity> {
    public AFActivityAdapter(Context context, ArrayList<AFActivity> androidFlavors) {
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
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        titleView.setText(activity.title);

        return listItemView;
    }
}
