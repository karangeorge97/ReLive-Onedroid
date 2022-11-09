package com.onedroid.relive.design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onedroid.relive.R;
import com.onedroid.relive.model.Event;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private final Context context;
    private ArrayList<Event> eventList;

    public CustomAdapter(Context context, int i, ArrayList<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    public void setFilteredList(ArrayList<Event> filteredList){
        this.eventList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int i) {
        return eventList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HolderView holderView;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_event_list_card_view,
                    parent,false);
            holderView = new HolderView(convertView);
            convertView.setTag(holderView);
        }

        else {
            holderView = (HolderView) convertView.getTag();
        }

        Event list = eventList.get(position);
        holderView.eventName.setText(list.getName());
        holderView.fromDate.setText(list.getFromDate());
        holderView.toDate.setText(list.getToDate());

        return convertView;
    }

    private static  class HolderView{
        private final TextView eventName;
        private final TextView fromDate;
        private final TextView toDate;

        public HolderView(View view){
            eventName = view.findViewById(R.id.eventName);
            fromDate = view.findViewById(R.id.fromDate);
            toDate = view.findViewById(R.id.toDate);
        }
    }
}
