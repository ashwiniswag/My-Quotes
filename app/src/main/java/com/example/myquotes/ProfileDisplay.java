package com.example.myquotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class ProfileDisplay extends AppCompatActivity {

    TextView pname,uname,contact,gender,setdp;
    Button mcontri;
    ImageView image;
    Uri imageuri;
    private static final int PICK_IMAGE=1;
    SharedPreferences preferences;

    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference ref,sref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);

        setdp=findViewById(R.id.dpset);
        pname=findViewById(R.id.pename);
        uname=findViewById(R.id.usname);
        contact=findViewById(R.id.contact);
        gender=findViewById(R.id.gen);
        mcontri=findViewById(R.id.mycontri);
        image=findViewById(R.id.dp);

        sref=storage.getReference("Images").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        final long ONE_MEGABYTE = 1024 * 1024;
        sref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                if(bytes!=null){
                    Bitmap bitmap;
                    bitmap=BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    image.setImageBitmap(bitmap);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        preferences =getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        pname.setText(preferences.getString("penname",""));
        uname.setText(preferences.getString("username",""));
        contact.setText(preferences.getString("number",""));
        gender.setText(preferences.getString("gender",""));

        mcontri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileDisplay.this,MainActivity.class);
                intent.putExtra("mcontri","1");
                startActivity(intent);
            }
        });

        setdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery=new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"Select Pictures"),PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            imageuri=data.getData();
            try{
                String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
                image.setImageBitmap(bitmap);
                Random rand=new Random();
                int n=rand.nextInt(1000000);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] dat = baos.toByteArray();
                ref=storage.getReference("AllImages").child(userid).child(String.valueOf(n));


                UploadTask uploadTask = sref.putBytes(dat);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
//                        Toast.makeText(ProfileDisplay.this,"Successfull",Toast.LENGTH_SHORT).show();
                    }
                });


                UploadTask uploadtask = ref.putBytes(dat);
                uploadtask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
//                        Toast.makeText(ProfileDisplay.this,"Successfull",Toast.LENGTH_SHORT).show();
                    }
                });
                System.out.println("Done");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
