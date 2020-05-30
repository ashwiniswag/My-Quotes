package com.example.myquotes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class Gridadapter extends BaseAdapter {

    Context applicationcontext;
    List<String> names;
//    List<Integer> images;
    List<Bitmap> bit;
    LayoutInflater inflater;
    StorageReference ref;//= FirebaseStorage.getInstance().getReference("Category");

    public  Gridadapter(Context applicationcontext, List<String> names,List<Bitmap> bit){//,List<Integer> images){
        this.names=names;
//        this.images=images;
        this.bit=bit;
        this.applicationcontext=applicationcontext;
        inflater=LayoutInflater.from(applicationcontext);
    }

    @Override
    public int getCount() {
            return names.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        ref=FirebaseStorage.getInstance().getReference("Category").child(names.get(i));

        View v=inflater.inflate(R.layout.options,null);
        TextView oname=v.findViewById(R.id.tname);
        oname.setText(names.get(i));
        final ImageView imageView=v.findViewById(R.id.img);
//        imageView.setImageResource(images.get(i));
//        imageView.setImageBitmap(imgset(names.get(i)));

//        ref=FirebaseStorage.getInstance().getReference();

        final long ONE_MEGABYTE = 1024 * 1024;
        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                if(bytes!=null){
                    Bitmap bitmap;
                    bitmap=BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(bitmap);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Gridadapter.this,"");
                System.out.println("Unable to download");
            }
        });



//        if(i<bit.size()){
//        imageView.setImageBitmap(bit.get(i));
//            System.out.println("Bitmap is present "+ bit.size());
//        }
//        else{
//            System.out.println("Bitmap is not present "+ bit.size());
//        }
        return v;
    }

//    public Bitmap imgset(String a){
//        Bitmap bitmap;
//        ref=FirebaseStorage.getInstance().getReference("Category").child(a);
//        final long ONE_MEGABYTE = 1024 * 1024;
//        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            Bitmap bit;
//            @Override
//            public void onSuccess(byte[] bytes) {
//                // Data for "images/island.jpg" is returns, use this as needed
//                if(bytes!=null){
//
//                    bit = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
////                    imageView.setImageBitmap(bitmap);
////                    return bitmap;
//                }
//            }
//            bitmap=bit;
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });
//        return bitmap;
//    }
}
