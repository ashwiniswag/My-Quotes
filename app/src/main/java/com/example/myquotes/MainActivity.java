package com.example.myquotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    Gridadapter gridadapter;

    List<String> name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent=new Intent(MainActivity.this,PhoneSIgnIn.class);
//        startActivity(intent);
        gridView=findViewById(R.id.grid);
        name = new ArrayList<>();
        name.add("Nature");
        name.add("Mother");
        name.add("Father");
        name.add("Sister");
        name.add("Brother");
        name.add("Country");
        name.add("Friends");
        name.add("Motivation");
        name.add("Love");
        name.add("Facts");
        name.add("Fiction");
        name.add("Memories");
        name.add("Broken Heart");
        name.add("News");
        gridadapter=new Gridadapter(this,name);
        gridView.setAdapter(gridadapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent=new Intent(MainActivity.this,Genre.class);
                intent.putExtra("Genre",name.get(i));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.options,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                SharedPreferences preferences =getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(MainActivity.this,PhoneSIgnIn.class);
                startActivity(intent);
                return true;
            case R.id.profile:
                Intent intent1=new Intent(MainActivity.this,ProfileDisplay.class);
                startActivity(intent1);
                return true;
            case  R.id.post:
                Intent intent2=new Intent(MainActivity.this,Post.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
