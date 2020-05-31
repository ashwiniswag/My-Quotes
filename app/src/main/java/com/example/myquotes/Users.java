package com.example.myquotes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class Users extends ArrayAdapter {

    Activity activity;
    LayoutInflater layoutInflater;
    List<String> uname,pname,phone;


    public Users(Activity context, List<String> uname, List<String> pname,List<String> phone){
        super(context,R.layout.userss,uname);
        this.activity=context;
        this.uname=uname;
        this.phone=phone;
        this.pname=pname;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        layoutInflater=activity.getLayoutInflater();
        View v=layoutInflater.inflate(R.layout.userss,null,true);
        TextView name=v.findViewById(R.id.un);
        TextView Pname=v.findViewById(R.id.pn);
        TextView Phone=v.findViewById(R.id.ph);
        name.setText(uname.get(position));
        Pname.setText(pname.get(position));
        Phone.setText(phone.get(position));
        return v;
    }

}
