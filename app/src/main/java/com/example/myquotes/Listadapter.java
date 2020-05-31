package com.example.myquotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.zip.Inflater;

public class Listadapter extends ArrayAdapter {

    List<String> name,suggestion;
    Activity activity;
    LayoutInflater layoutInflater;

//    public Listadapter() {
//        super(ac);
//    }

//    public void Listadapter(){};

    public Listadapter(Activity context, List<String> name,List<String> suggestion){
        super(context,R.layout.sugges,name);
        this.name=name;
        this.suggestion=suggestion;
        this.activity=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        layoutInflater=activity.getLayoutInflater();
        View v=layoutInflater.inflate(R.layout.sugges,null,true);
        TextView nam=v.findViewById(R.id.na);
        TextView sug=v.findViewById(R.id.sug);
        nam.setText(name.get(position));
        sug.setText(suggestion.get(position));
        return v;
    }
}
