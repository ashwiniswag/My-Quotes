package com.example.myquotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    Gridadapter gridadapter;

    List<String> name;
//    List<Integer> images;
//    Integer[] images;

    Bundle a;
    DatabaseReference ref;
    FloatingActionButton add;
    List<Bitmap> bit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        a=new Bundle();
        a=getIntent().getExtras();
        gridView=findViewById(R.id.grid);
        add=findViewById(R.id.add);
        bit=new ArrayList<>();
//        images=new ArrayList<>();

//        images.add(R.drawable.nature);
//        images.add(R.drawable.mother);
//        images.add(R.drawable.father);
//        images.add(R.drawable.sister);
//        images.add(R.drawable.brother);
//        images.add(R.drawable.nation);
//        images.add(R.drawable.friends);
//        images.add(R.drawable.motivation);
//        images.add(R.drawable.heart);
//        images.add(R.drawable.facts);
//        images.add(R.drawable.fiction);
//        images.add(R.drawable.memories);
//        images.add(R.drawable.broken_heart);
//        images.add(R.drawable.news);
        name = new ArrayList<>();
//        name.add("Nature");
//        name.add("Mother");
//        name.add("Father");
//        name.add("Sister");
//        name.add("Brother");
//        name.add("Nation");
//        name.add("Friends");
//        name.add("Motivation");
//        name.add("Love");
//        name.add("Facts");
//        name.add("Fiction");
//        name.add("Memories");
//        name.add("Broken Heart");
//        name.add("News");
        gridadapter=new Gridadapter(this,name,bit);//,images);
        gridView.setAdapter(gridadapter);

        if(a.getString("admin").equals("1")){
            add.setVisibility(View.VISIBLE);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent=new Intent(MainActivity.this,Genre.class);
                intent.putExtra("Genre",name.get(i));
                String mcontri="0";
                mcontri = a.getString("mcontri");
                intent.putExtra("mcontri",mcontri);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,New_Category.class);
                startActivity(intent);
            }
        });
        disp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.options,menu);
        MenuItem menuItem=menu.findItem(R.id.signout);
//        menuItem.
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
            case R.id.contact:
                String userid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                ref=FirebaseDatabase.getInstance().getReference("Suggestions").child(userid).push();
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Enter Your Suggestion");
                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ref.child("Suggestions").setValue(input.getText().toString());
                    }
                });
                    alertDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void disp(){
        final StorageReference sref= FirebaseStorage.getInstance().getReference("Category");
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("All Category");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                while (items.hasNext()){
                    DataSnapshot item = items.next();
                    if(item.child("Name")!=null){

                        name.add(item.child("Name").getValue().toString());
                        final long ONE_MEGABYTE = 1024 * 1024;
                        sref.child(item.child("Name").getValue().toString()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                if(bytes!=null) {
                                    Bitmap bitmap;
                                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    bit.add(bitmap);
                                    System.out.println("Bitmsp is present :)    " + bitmap);
                                }
                                else{
                                    System.out.println("Bitmsp is null :(");
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Image Not Found",Toast.LENGTH_SHORT).show();
                            }
                        });


                        System.out.println(item.child("Name").getValue().toString());
                    }
                }
                gridadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
