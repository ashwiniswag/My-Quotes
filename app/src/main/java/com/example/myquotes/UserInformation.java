package com.example.myquotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserInformation extends AppCompatActivity {

    ListView listView;
    List<String> uname,pname,phone;
    Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        listView=findViewById(R.id.usetinfo);

        uname=new ArrayList<>();
        pname=new ArrayList<>();
        phone=new ArrayList<>();
        user=new Users(this,uname,pname,phone);
        listView.setAdapter(user);

        disp();

    }

    public void disp(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("User");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> items=dataSnapshot.getChildren().iterator();
                while (items.hasNext()) {
                    DataSnapshot item = items.next();
                    if(item.child("Information").child("Number").getValue().toString()!=null){
                        uname.add(item.child("Information").child("Username").getValue().toString());
                        pname.add(item.child("Information").child("Penname").getValue().toString());
                        phone.add(item.child("Information").child("Number").getValue().toString());
                    }
                }
                user.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
