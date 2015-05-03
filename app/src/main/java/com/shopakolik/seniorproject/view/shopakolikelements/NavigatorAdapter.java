package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopakolik.seniorproject.R;

import java.util.List;

/**
 * Created by IREM on 5/3/2015.
 */
public class NavigatorAdapter extends BaseAdapter {
    Context context;
    List<RowItem> rowItem;

    NavigatorAdapter(Context context, List<RowItem> rowItem) {
        this.context = context;
        this.rowItem = rowItem;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.drawer_list_item, null);
        }
        ImageView imgIcon = (ImageView) view.findViewById(R.id.icon);
        TextView txtTitle = (TextView) view.findViewById(R.id.title);
        RowItem row_pos = rowItem.get(position);
        // setting the image resource and title
        imgIcon.setImageResource(row_pos.getIcon());
        txtTitle.setText(row_pos.getTitle());
        return view;
    }

    @Override
    public int getCount() {
        return rowItem.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItem.indexOf(getItem(position));
    }
}
