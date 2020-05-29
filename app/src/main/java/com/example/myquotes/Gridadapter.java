package com.example.myquotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Gridadapter extends BaseAdapter {

    Context applicationcontext;
    List<String> names;
    List<Integer> images;
    LayoutInflater inflater;

    public  Gridadapter(Context applicationcontext, List<String> names,List<Integer> images){
        this.names=names;
        this.images=images;
        this.applicationcontext=applicationcontext;
        inflater=LayoutInflater.from(applicationcontext);
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v=inflater.inflate(R.layout.options,null);
        TextView oname=v.findViewById(R.id.tname);
        ImageView imageView=v.findViewById(R.id.img);
        imageView.setImageResource(images.get(i));
        oname.setText(names.get(i));
        return v;
    }
}
