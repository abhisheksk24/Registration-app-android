package com.devAB.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class signup extends AppCompatActivity {

    //Variables
    EditText regName, regUsername, regEmail, regPhoneno;
    Button returnlogin, signupbtn;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();


        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("");


        regName = findViewById(R.id.sg_name);
        regUsername = findViewById(R.id.sg_uname);
        regEmail = findViewById(R.id.sg_email);
        regPhoneno = findViewById(R.id.sg_phone);

        countryCodePicker = findViewById(R.id.sg_ccp);

        signupbtn = findViewById(R.id.buttongo2);
        returnlogin = findViewById(R.id.returnlogin);
        returnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, login.class);
                startActivity(intent);
            }
        });
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registeruser();
            }
        });
    }

    private boolean validateFullName() {
        String val = regName.getText().toString().trim();

        if (val.isEmpty()) {
            regName.setError("Field can not be empty");
            return false;

        } else {
            regName.setError(null);
            return true;
        }
    }

    private boolean validateemail() {
        String val = regEmail.getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            regEmail.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            regEmail.setError("Invalid Email ID");
            return false;
        } else {
            regEmail.setError(null);
            return true;
        }
    }

    private boolean validateUsername() {
        String val = regUsername.getText().toString().trim();
        String checkspace = "\\A\\w{1,20}\\z";

        if (val.isEmpty()) {
            regUsername.setError("Field can not be empty");
            return false;

        } else if (val.length() > 20) {
            regUsername.setError("UserName can not exceed 20 Characters");
            return false;
        } else if (!val.matches(checkspace)) {
            regUsername.setError("No White Spaces are Allowed");
            return false;
        } else {
            regUsername.setError(null);
            return true;
        }
    }

    private boolean validatePhonenumber() {
        String val = regPhoneno.getText().toString().trim();
        //String finalno = "+"+countryCodePicker.getFullNumber()+val;


        if (val.isEmpty()) {
            regPhoneno.setError("Field can not be empty");
            return false;

        } else {
            regPhoneno.setError(null);
            return true;
        }
    }

    public void registeruser() {

        if (!validateFullName() | !validateemail() | !validatePhonenumber() | !validateUsername()) {
            return;
        } else {
            isUser();
        }


    }

    private void isUser() {

        String finalno = regPhoneno.getText().toString().trim();
        String userEnteredPhoneNo = "+"+countryCodePicker.getSelectedCountryCode()+finalno;
        //final String userEnteredPhoneNo = "+"+countryCodePicker.getFullNumber()+val;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("userlist");
        Query checkUser = reference.orderByChild("phoneno").equalTo(userEnteredPhoneNo);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    regPhoneno.setError("Number Already in Use");
                    regPhoneno.requestFocus();


                } else {
                    isemail();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void isemail() {

        String verifyemail = regEmail.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("userlist");
        Query checkUser2 = reference.orderByChild("email").equalTo(verifyemail);

        checkUser2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    regEmail.setError("Email ID Already Taken");
                    regEmail.requestFocus();

                } else {

                    regEmail.setError(null);
                    isusername();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void isusername() {

        String uname = regUsername.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("userlist");
        Query checkUser1 = reference.orderByChild("username").equalTo(uname);

        checkUser1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    regUsername.setError("Username Already Taken");
                    regUsername.requestFocus();
                } else {
                    regUsername.setError(null);
                    //Get values in string
                    String name = regName.getText().toString();
                    String username = regUsername.getText().toString();
                    String email = regEmail.getText().toString();
                    String number = regPhoneno.getText().toString().trim();
                    String phoneno = "+"+countryCodePicker.getSelectedCountryCode()+number;;

                    Intent intent = new Intent(getApplicationContext(), signup_otp.class);
                    intent.putExtra("phoneno", phoneno);
                    intent.putExtra("name", name);
                    intent.putExtra("username", username);
                    intent.putExtra("email", email);

                    startActivity(intent);
                    //Toast.makeText(this,"Enter OTP sent on your phone number for verification",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this,"Enter OTP sent on your phone number for verification",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}