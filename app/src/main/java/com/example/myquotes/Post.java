package com.example.myquotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class Post extends AppCompatActivity {

    EditText title,content;
    Spinner category;

    FloatingActionButton save;

    BottomNavigationView style;

    int b,i,u;

    FirebaseDatabase database=FirebaseDatabase.getInstance();;
    DatabaseReference ref,mcat;

    SharedPreferences preferences;// =getSharedPreferences("MyPref", Context.MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        style=findViewById(R.id.style);

        title=findViewById(R.id.title);
        content=findViewById(R.id.editor);

        category=findViewById(R.id.category);
        save=findViewById(R.id.save);

        b=0;
        i=0;
        u=0;
//        database=FirebaseDatabase.getInstance();

        preferences =getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                mcat=database.getReference("Category").child(category.getSelectedItem().toString()).push();
                ref=database.getReference("User").child(userid).child("Category").child(category.getSelectedItem().toString()).push();
                Firebasetree firebasetree = new Firebasetree(title.getText().toString(),content.getText().toString());
//                Firebasetree firebasetrees = new Firebasetree(title.getText().toString(),content.getText().toString(),preferences.getString("penname",""));
//                ref.setValue(firebasetree);
//                mcat.setValue(firebasetrees);
//                System.out.println(title.getText().toString());

                String pname=preferences.getString("penname","");

                ref.child("Title").setValue(title.getText().toString());
                ref.child("Content").setValue(content.getText().toString());
                ref.child("Bold").setValue(String.valueOf(b));
                ref.child("Itallic").setValue(String.valueOf(i));
                ref.child("Underline").setValue(String.valueOf(u));
                ref.child("timestamp").setValue(ServerValue.TIMESTAMP);

                mcat.child("Title").setValue(title.getText().toString());
                mcat.child("Content").setValue(content.getText().toString());
                mcat.child("Pen Name").setValue(pname);
                mcat.child("Bold").setValue(String.valueOf(b));
                mcat.child("Itallic").setValue(String.valueOf(i));
                mcat.child("Underline").setValue(String.valueOf(u));
                mcat.child("timestamp").setValue(ServerValue.TIMESTAMP);
                Intent intent=new Intent(Post.this,MainActivity.class);
                intent.putExtra("mcontri","0");
                startActivity(intent);
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
}
