package com.example.myquotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileDisplay extends AppCompatActivity {

    TextView pname,uname,contact,gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);

        pname=findViewById(R.id.pename);
        uname=findViewById(R.id.usname);
        contact=findViewById(R.id.contact);
        gender=findViewById(R.id.gen);

        SharedPreferences preferences =getSharedPreferences("MyPref", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();

//        String penname=preferences.
        pname.setText(preferences.getString("penname",""));
        uname.setText(preferences.getString("username",""));
        contact.setText(preferences.getString("number",""));
        gender.setText(preferences.getString("gender",""));
    }
}
