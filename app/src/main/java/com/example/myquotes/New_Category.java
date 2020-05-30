package com.example.myquotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class New_Category extends AppCompatActivity {

    EditText text;
    Button button,add;
    private static final int PICK_IMAGE=1;
    Uri imageuri;
    byte[] dat;

    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference ref;

    DatabaseReference dref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__category);

        text=findViewById(R.id.cname);
        button=findViewById(R.id.addpic);
        add=findViewById(R.id.addd);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery=new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"Select Pictures"),PICK_IMAGE);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ref=storage.getReference("Category").child(text.getText().toString());
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


                dref= FirebaseDatabase.getInstance().getReference("All Category").push().child("Name");
                dref.setValue(text.getText().toString());
                Intent intent=new Intent(New_Category.this,MainActivity.class);
                intent.putExtra("admin","1");
                intent.putExtra("mcontri","1");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            imageuri=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                dat = baos.toByteArray();
                    
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
