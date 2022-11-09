package com.onedroid.relive.design;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.onedroid.relive.R;
import com.onedroid.relive.model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CustomAdapter extends BaseAdapter {
    private final Context context;
    private Set<Event> eventList;

    public CustomAdapter(Context context, int i, Set<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }



    public void setFilteredList(Set<Event> filteredList){
        this.eventList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int i) {
        List<Event> arrList = new ArrayList<>(eventList);
        return arrList.get(i);
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
        List<Event> arrList = new ArrayList<>(eventList);
        Event list = arrList.get(position);
        holderView.eventName.setText(list.getName());
        holderView.fromDate.setText(list.getFromDate());
        holderView.toDate.setText(list.getToDate());
        holderView.image.setImageDrawable(getDrawable(list.getName()));

        return convertView;

    }


    private Drawable getDrawable(String eventName)
    {

        switch (eventName)
        {
            case "Graduation": return  context.getResources().getDrawable( R.drawable.ic_date_12_icon );
            case "Birthday": return  context.getResources().getDrawable( R.drawable.ic_date_14_icon );
            case "Halloween": return  context.getResources().getDrawable( R.drawable.ic_date_16_icon );
            case "Lakers Game": return  context.getResources().getDrawable( R.drawable.ic_date_23_icon );
            default: return context.getResources().getDrawable( R.drawable.ic_date_23_icon );

        }

    }



    private static  class HolderView{
        private final TextView eventName;
        private final TextView fromDate;
        private final TextView toDate;
        private final ImageView image;

        public HolderView(View view){
            eventName = view.findViewById(R.id.eventName);
            fromDate = view.findViewById(R.id.fromDate);
            toDate = view.findViewById(R.id.toDate);
            image = view.findViewById(R.id.eventDisplay);
        }
    }


}
