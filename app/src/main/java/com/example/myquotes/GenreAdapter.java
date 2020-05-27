package com.example.myquotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class GenreAdapter extends BaseAdapter {
    List<String> sher,pname,titl;
    Context context;
    LayoutInflater inflater;

    public GenreAdapter(Context context,List<String> sher,List<String> pname,List<String> title){
        this.context=context;
        this.sher=sher;
        this.pname=pname;
        this.titl=title;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return sher.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=inflater.inflate(R.layout.content,null);
        TextView content=v.findViewById(R.id.sher);
        TextView penname=v.findViewById(R.id.wname);
        TextView title=v.findViewById(R.id.tit);
        content.setText(sher.get(position));
        penname.setText("-"+pname.get(position));
        title.setText(titl.get(position));
        return v;
    }
}
