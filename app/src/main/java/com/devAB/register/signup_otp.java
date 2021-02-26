package com.devAB.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class signup_otp extends AppCompatActivity {

    String vercodebysys;
    Button verify_btn;
    PinView codebyuser;
    ProgressBar sgpprogress;
    String phoneno,name,username,email;
    TextView Signupshownumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_otp);
        Signupshownumber = findViewById(R.id.signupnumber);
        getSupportActionBar().hide();

        verify_btn = findViewById(R.id.sg_verifyb);
        codebyuser = findViewById(R.id.codeusersg);
        sgpprogress = findViewById(R.id.sg_pgbar);

        sgpprogress.setVisibility(View.GONE);

        phoneno = getIntent().getStringExtra("phoneno");
        name = getIntent().getStringExtra("name");
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");

        Signupshownumber.setText(phoneno);


        sendVcodetouser(phoneno);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codebyuser.getText().toString();


                if(code.isEmpty() || code.length()<6){
                    codebyuser.setError("Invalid OTP...!");
                    codebyuser.requestFocus();
                    return;
                }
                sgpprogress.setVisibility(View.VISIBLE);
                verfifyCode(code);

            }
        });
    }

    private void sendVcodetouser(String phoneno) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneno,        // Phone number to verify
                60,                    // Timeout duration
                TimeUnit.SECONDS,     // Unit of timeout
                TaskExecutors.MAIN_THREAD,                // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            vercodebysys = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                codebyuser.setText(code);
                sgpprogress.setVisibility(View.VISIBLE);
                verfifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(signup_otp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };

    private void verfifyCode(String verficationCode) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(vercodebysys, verficationCode);
        signUpUsersg(credential);

    }

    private void signUpUsersg(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(signup_otp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            storeNewUserData();

                            Toast.makeText(signup_otp.this, "Number Verified Successfully", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(signup_otp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void storeNewUserData() {

        FirebaseDatabase rootnode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootnode.getReference("userlist");

        UserHelperClass helperClass = new UserHelperClass(name, username, email, phoneno);
        reference.child(phoneno).setValue(helperClass);

        //Perform Your required action here to either let the user sign In or do something required

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

}