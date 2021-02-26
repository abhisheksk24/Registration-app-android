package com.devAB.register;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button goAccount,signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goAccount = findViewById(R.id.btn1);
        signout = findViewById(R.id.btn2);

        goAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, account.class);
                startActivity(intent);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                signout();
            }
        });


    }

    private void signout(){
        Intent startIntent = new Intent(MainActivity.this, login.class);
        startActivity(startIntent);
        finish();
    }
}