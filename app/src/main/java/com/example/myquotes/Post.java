package com.example.myquotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Post extends AppCompatActivity {

    EditText title,content;
    Spinner category;

    List<String> c;

    FloatingActionButton save;

    BottomNavigationView style;

    ArrayList<String> cat;

    int b,i,u;
//     c={0};

    FirebaseDatabase database=FirebaseDatabase.getInstance();;
    DatabaseReference ref,mcat,cc;

    SharedPreferences preferences;// =getSharedPreferences("MyPref", Context.MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        c=new ArrayList<>();
        cat=new ArrayList<>();
        category=findViewById(R.id.category);
        cc=FirebaseDatabase.getInstance().getReference("All Category");
        cc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                while (items.hasNext()){
                    DataSnapshot item = items.next();
                    if(item.child("Name")!=null){
                        cat.add(item.child("Name").getValue().toString());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        Post.this, android.R.layout.simple_spinner_item, cat);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                category.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        style=findViewById(R.id.style);

        title=findViewById(R.id.title);
        content=findViewById(R.id.editor);


        save=findViewById(R.id.save);


        b=0;
        i=0;
        u=0;

        preferences =getSharedPreferences("MyPref", Context.MODE_PRIVATE);

//        check();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check();
                save.setEnabled(false);
//                if(check()){
//                if(!c.contains(title.getText().toString())){//&& (!c.get(0).equals("1"))) {

//                }
//                else{
//                    Toast.makeText(Post.this,"This title is already taken.",Toast.LENGTH_SHORT).show();
//                }
            }
        });

        style.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.bold:
                        b = b + 1;
                        b = b % 2;
                        sstyle(b, i, u);
                        return true;
                    case R.id.itallic:
                        i = i + 1;
                        i = i % 2;
                        sstyle(b, i, u);
                        return true;
                    case R.id.underline:
                        u = u + 1;
                        u = u % 2;
                        sstyle(b, i, u);
                        return true;
                }
                return true;
            }
        });
    }

    public void sstyle(int bo,int it,int ul){
        String text = content.getText().toString();

        String pre = "",post = "";
        if(bo==1){
            pre=pre + "<b>";
            post=post+ "</b>";
        }
        if(it==1){
            pre=pre + "<i>";
            post="</i>" + post;
        }
        if(ul==1){
            pre=pre +"<u>";
            post="</u>" + post;
        }
        String replace=pre + text + post;
        content.setText(Html.fromHtml(replace));
    }

    protected void check(){
//        int c = 0;
        c.add(0,"0");
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Category").child(category.getSelectedItem().toString());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            int d=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> items=dataSnapshot.getChildren().iterator();
                while (items.hasNext()) {
                    DataSnapshot item=items.next();
                    System.out.println("Abey men alag hu  " + item.child("Title").getValue().toString());
                    if(item.child("Title").getValue().toString().equals(title.getText().toString())){
                        c.add(0,"1");
                        Toast.makeText(Post.this,"I got it",Toast.LENGTH_SHORT).show();
                        System.out.println("Matched" + c.get(0));
                        break;
                    }
                }
                if(c.get(0).equals("0")){
                    upload();
                }
                else{
                    Toast.makeText(Post.this,"This title is already taken.",Toast.LENGTH_SHORT).show();
                    save.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        System.out.println("Mein Yaha tak pahuch gaya");

    }

    protected void upload(){
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mcat = database.getReference("Category").child(category.getSelectedItem().toString()).child(title.getText().toString());
        ref = database.getReference("User").child(userid).child("Category").child(category.getSelectedItem().toString()).child(title.getText().toString());

        String pname = preferences.getString("penname", "");

        ref.child("Title").setValue(title.getText().toString());
        ref.child("Content").setValue(content.getText().toString());
        ref.child("Bold").setValue(String.valueOf(b));
        ref.child("Itallic").setValue(String.valueOf(i));
        ref.child("Underline").setValue(String.valueOf(u));
        ref.child("timestamp").setValue(ServerValue.TIMESTAMP);
        ref.child("Pen Name").setValue(pname);

        mcat.child("Title").setValue(title.getText().toString());
        mcat.child("Content").setValue(content.getText().toString());
        mcat.child("Pen Name").setValue(pname);
        mcat.child("Bold").setValue(String.valueOf(b));
        mcat.child("Itallic").setValue(String.valueOf(i));
        mcat.child("Underline").setValue(String.valueOf(u));
        mcat.child("timestamp").setValue(ServerValue.TIMESTAMP);
        Intent intent = new Intent(Post.this, MainActivity.class);
        intent.putExtra("mcontri", "0");
        intent.putExtra("admin", "0");
        startActivity(intent);
    }
}
