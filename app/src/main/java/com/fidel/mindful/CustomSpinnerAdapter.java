package com.fidel.mindful;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomSpinnerAdapter extends ArrayAdapter<CharSequence> {

    public CustomSpinnerAdapter(Context context, int resource, CharSequence[] objects) {
        super(context, resource, objects);
    }

    @Override
    public boolean isEnabled(int position) {
        // Disable the "Search Criteria" option
        return position != 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        // Disable the "Search Criteria" option if it's the first item
        if (position == 0) {
            ((TextView) view).setTextColor(getContext().getResources().getColor(R.color.disabled_color));
        } else {
            ((TextView) view).setTextColor(getContext().getResources().getColor(android.R.color.black));
        }

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);

        // Disable the "Search Criteria" option if it's the first item
        if (position == 0) {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.disabled_color));
        } else {
            view.setBackgroundColor(getContext().getResources().getColor(android.R.color.white));
        }

        return view;
    }
}

