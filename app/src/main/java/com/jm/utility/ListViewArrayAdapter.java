package com.jm.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jm.JmPattern;

public class ListViewArrayAdapter extends ArrayAdapter<JmPattern> {
    private JmPattern[] items;
    private LayoutInflater inflater;

    public ListViewArrayAdapter(Context context, int resourceId, JmPattern[] items) {
        super(context, resourceId, items);
        this.items = items;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;  
        if(view == null){  
            //1行分layoutからViewの塊を生成
            view = inflater.inflate(android.R.layout.simple_list_item_1, null);  
        }      

        JmPattern item = items[position];  
        
        TextView idText01 = (TextView)view.findViewById(android.R.id.text1);  
        idText01.setText(item.getName());
        
        return view;
    }
    
}
