package com.example.myquotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShowSuggestions extends AppCompatActivity {

    ListView listView;

//    ArrayAdapter<String> arrayAdapter;
    List<String> name,suggestion;

    Listadapter listadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_suggestions);
        listView=findViewById(R.id.show);
        name=new ArrayList<>();
        suggestion=new ArrayList<>();
        listadapter=new Listadapter(this,name,suggestion);
        listView.setAdapter(listadapter);
        disp();
//        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2,name);
    }

    protected void disp(){
        String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Suggestions");
        ref.orderByChild("timestamp");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> items=dataSnapshot.getChildren().iterator();
                while (items.hasNext()) {
                    DataSnapshot item = items.next();
                    if(item.child("Suggestions").getValue().toString()!=null){
                        suggestion.add(item.child("Suggestions").getValue().toString());
                        name.add(item.child("Name").getValue().toString());
                    }
                }
                listadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
