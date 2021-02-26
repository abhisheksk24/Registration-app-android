package com.devAB.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class login extends AppCompatActivity {

    public Button go;
    public Button returnsignup;
    EditText lgPhoneNo;
    FirebaseAuth auth;
    ProgressBar lgprogress2;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        countryCodePicker = findViewById(R.id.lg_countrycode);

        lgPhoneNo = findViewById(R.id.ver_phone);
        getSupportActionBar().hide();

        go = findViewById(R.id.buttongolg);
        returnsignup = findViewById(R.id.signupp);

        lgprogress2 = findViewById(R.id.lg_pgbar2);
        lgprogress2.setVisibility(View.INVISIBLE);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        returnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,signup.class);
                startActivity(intent);
            }
        });

    }

    private boolean validatePhoneNo() {
        String val = lgPhoneNo.getText().toString().trim();

        if (val.isEmpty()) {
            lgPhoneNo.setError("Field can not be empty");
            return false;

        } else {
            lgPhoneNo.setError(null);
            return true;
        }
    }


    public void loginUser (View view){

        if (!validatePhoneNo()) {
            return;
        }
        else{
            lgprogress2.setVisibility(View.VISIBLE);
            isUser();
        }

    }

    private void isUser() {

        final String val = lgPhoneNo.getText().toString().trim();
        final String userEnteredPhoneNo = "+"+countryCodePicker.getSelectedCountryCode()+val;


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("userlist");
        Query checkUser = reference.orderByChild("phoneno").equalTo(userEnteredPhoneNo);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    lgPhoneNo.setError(null);

                    String phonenoFromDB = dataSnapshot.child(userEnteredPhoneNo).child("phoneno").getValue(String.class);
                    Intent intent = new Intent (getApplicationContext(), login_otp.class);
                    intent.putExtra("phoneno",phonenoFromDB);

                    startActivity(intent);
                }

                else{
                    lgPhoneNo.setError("No such user Exist");
                    Intent intent = new Intent (getApplicationContext(), signup.class);
                    lgprogress2.setVisibility(View.GONE);
                    lgPhoneNo.requestFocus();
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}