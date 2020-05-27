package com.example.myquotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Genre extends AppCompatActivity {

    GridView gridView;
    GenreAdapter genreAdapter;

    Bundle a;

    List<String> sher,pname,title,bold,itallic,underline;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);

        a=getIntent().getExtras();

        sher=new ArrayList<>();
        pname=new ArrayList<>();
        title=new ArrayList<>();
        bold=new ArrayList<>();
        itallic=new ArrayList<>();
        underline=new ArrayList<>();

        gridView=findViewById(R.id.showwork);
        genreAdapter=new GenreAdapter(this,sher,pname,title);
        gridView.setAdapter(genreAdapter);
        disp();

        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.ENGLISH);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
//                        Log.e("TTS", "The Language is not supported!");
                    } else {
//                        Log.i("TTS", "Language Supported.");
//                                Toast.makeText(Editor.this,"LAnguage Support",Toast.LENGTH_SHORT).show();
                    }
//                    Log.i("TTS", "Initialization success.");
//                            Toast.makeText(Editor.this,"Initialization success.",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int speechstatus=textToSpeech.speak(sher.get(position),TextToSpeech.QUEUE_FLUSH,null);
                if (speechstatus == TextToSpeech.ERROR) {
                    Toast.makeText(Genre.this,"Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void disp(){
        DatabaseReference ref;
//        String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref= FirebaseDatabase.getInstance().getReference("Category").child(a.getString("Genre"));
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("Ye kaam kar raha hai");
                Iterator<DataSnapshot> items=dataSnapshot.getChildren().iterator();
                while(items.hasNext()){
                    DataSnapshot item=items.next();
                    if(item.child("Title")!=null){
                        sher.add(item.child("Content").getValue().toString());
                        pname.add(item.child("Pen Name").getValue().toString());
                        title.add(item.child("Title").getValue().toString());
                        bold.add(item.child("Bold").getValue().toString());
                        itallic.add(item.child("Itallic").getValue().toString());
                        underline.add(item.child("Underline").getValue().toString());
                        System.out.println("Ye kaam kar raha hai");
                    }
                    else {
                        System.out.println("Ye kaam kar raha hai");
                    }
                    genreAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Genre.this,"Not Changed",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
