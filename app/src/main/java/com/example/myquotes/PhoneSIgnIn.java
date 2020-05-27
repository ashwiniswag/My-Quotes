package com.example.myquotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PhoneSIgnIn extends AppCompatActivity {

    EditText number,otp,uname,pname;
    Button signup,otpmatch;

    Spinner gender;

    LinearLayout verify,match;

    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    FirebaseAuth mAuth;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private SharedPreferences mpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_s_ign_in);

        number=findViewById(R.id.phone_no);
        otp=findViewById(R.id.opt);

        uname=findViewById(R.id.Username);
        pname=findViewById(R.id.penname);

        gender=findViewById(R.id.gender);

        signup=findViewById(R.id.signup);
        otpmatch=findViewById(R.id.verify);

        verify=findViewById(R.id.vcode);
        match=findViewById(R.id.cverify);

        mAuth=FirebaseAuth.getInstance();

        mpref =getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);

        otpmatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                otpmatch.setEnabled(false);

                String votp=otp.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, votp);
                signInWithPhoneAuthCredential(credential);

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verify.setVisibility(View.INVISIBLE);
                match.setVisibility(View.VISIBLE);

                String phoneNumber="+91" + number.getText().toString();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        PhoneSIgnIn.this,               // Activity (for callback binding)
                        mCallbacks);        // OnVerificationStateChangedCallbacks
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                System.out.println("onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                System.out.println("onVerificationFailed" + e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
//                Log.d( "onCodeSent:" + verificationId);
                System.out.println("onCodeSent:"+ verificationId);
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };        

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser=mAuth.getCurrentUser();
        if(currentuser!=null){
            Intent user=new Intent(PhoneSIgnIn.this,MainActivity.class);
            startActivity(user);
        }
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            System.out.println( "signInWithCredential:success");

                            SharedPreferences.Editor editor=mpref.edit();
                            editor.putString("username",uname.getText().toString());
                            editor.putString("penname",pname.getText().toString());
                            editor.putString("number",number.getText().toString());
                            editor.putString("gender",gender.getSelectedItem().toString());
                            editor.commit();

                            String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();

                            FirebaseDatabase database=FirebaseDatabase.getInstance();;
                            DatabaseReference ref=database.getReference("User").child(userid).child("Information");

                            ref.child("Username").setValue(uname.getText().toString());
                            ref.child("Penname").setValue(pname.getText().toString());
                            ref.child("Number").setValue(number.getText().toString());
                            ref.child("Gender").setValue(gender.getSelectedItem().toString());

                            Intent intent = new Intent(PhoneSIgnIn.this,MainActivity.class);
                            startActivity(intent);
                        } else {
                            // Sign in failed, display a message and update the UI
                            System.out.println("signInWithCredential:failure"+ task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

}
