package com.devAB.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class login_otp extends AppCompatActivity {

    String vercodebysys;
    Button verify_btn;
    PinView codebyuser;
    ProgressBar lgprogress;
    String phoneno,name,username,email,password;
    TextView loginshownumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);
        loginshownumber = findViewById(R.id.loginnumber);
        getSupportActionBar().hide();

        verify_btn = findViewById(R.id.lg_verifyb);
        codebyuser = findViewById(R.id.codeuserlg);
        lgprogress = findViewById(R.id.lg_pgbar);

        lgprogress.setVisibility(View.GONE);

        phoneno = getIntent().getStringExtra("phoneno");
        loginshownumber.setText(phoneno);

        if (vercodebysys == null && savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }


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
                lgprogress.setVisibility(View.VISIBLE);
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
                lgprogress.setVisibility(View.VISIBLE);
                verfifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(login_otp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };

    private void verfifyCode(String verficationCode) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(vercodebysys, verficationCode);
        loginUserlg(credential);

    }

    private void loginUserlg(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(login_otp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            Toast.makeText(login_otp.this, "Number Verified Successfully", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(login_otp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}