package com.devAB.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class account extends AppCompatActivity {

    TextInputEditText acc_name, acc_email, acc_phonenumber, acc_username;
    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentuser;
    //private StorageReference imagestorage;
    private  static final int GALLERY_PICK = 1;
    private ProgressDialog dpuploadprogress;
    Button updateDP,backbtn;
    //CircleImageView acc_dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getSupportActionBar().hide();

        //Data retriving from firebase
        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        final String user_phoneno = mCurrentuser.getPhoneNumber();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("userlist").child(user_phoneno);
        mDatabase.keepSynced(true);

        //Input Fields
        acc_name = (TextInputEditText)findViewById(R.id.fullname);
        acc_email = (TextInputEditText)findViewById(R.id.fullemail);
        acc_phonenumber = (TextInputEditText)findViewById(R.id.fullphone);
        acc_username = (TextInputEditText)findViewById(R.id.fullusername);

        //Button
        backbtn = findViewById(R.id.acc_bacbtn);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String setname = snapshot.child("name").getValue().toString();
                String setemail = snapshot.child("email").getValue().toString();
                String setphonenumber = snapshot.child("phoneno").getValue().toString();
                String setusername = snapshot.child("username").getValue().toString();

                acc_name.setText(setname);
                acc_email.setText(setemail);
                acc_phonenumber.setText(setphonenumber);
                acc_username.setText(setusername);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}