package com.example.myquotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    FirebaseAuth auth;
    Handler handle;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar=findViewById(R.id.progress);
        progressBar.getProgress();
        handle=new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                check();
                finish();
            }
        },1500);
    }

    protected void check(){
//        auth=FirebaseAuth.getInstance().getCurrentUser();
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            Intent intent=new Intent(SplashScreen.this,PhoneSIgnIn.class);
            startActivity(intent);
        }
        else{
            Intent user=new Intent(SplashScreen.this,MainActivity.class);
            user.putExtra("mcontri","0");
            user.putExtra("admin","0");
            startActivity(user);
        }
    }

}
