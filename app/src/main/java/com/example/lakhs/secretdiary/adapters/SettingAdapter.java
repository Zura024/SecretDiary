package com.example.lakhs.secretdiary.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lakhs.secretdiary.R;
import com.example.lakhs.secretdiary.model.Setting;

import java.util.List;

public class SettingAdapter extends BaseAdapter {

    private List<Setting> settings;
    private Context context;

    public SettingAdapter(Context context,List<Setting> settings){
        this.context = context;
        this.settings = settings;
    }

    @Override
    public int getCount() {
        return settings.size();
    }

    @Override
    public Object getItem(int position) {
        return settings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.setting_list_items,null);
        TextView title = (TextView) convertView.findViewById(R.id.setting_title);
        TextView desc = ( TextView) convertView.findViewById(R.id.setting_description);

        title.setText(settings.get(position).getTitle());
        desc.setText(settings.get(position).getDescription());
       /* switch (settings.get(position).getAction()){
            case App.get
        }*/
        return convertView;
    }
}
