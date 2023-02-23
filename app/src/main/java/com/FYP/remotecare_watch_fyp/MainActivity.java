package com.FYP.remotecare_watch_fyp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    private Button submit;
    EditText email,pass;
    TextView changeip;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        submit=findViewById(R.id.submit);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        mAuth = FirebaseAuth.getInstance();
        changeip=findViewById(R.id.changeip);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("Ip", "");
        Log.d("s1",s1);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
//            mAuth.signOut();
//                finish();
            Intent intent=new Intent(MainActivity.this,MainScreen.class);
            startActivity(intent);
            Log.d("sucess","alreadylogged");
        }
        else{
            Log.d("sucess","not looged in");
        }

        changeip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Ip_change_screen.class);
                startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emaill=email.getText().toString();
                String password=pass.getText().toString();
                Log.d("user",emaill);
                Log.d("user",password);
                if(emaill.equals("")||password.equals("")){

                }else{
                    mAuth.signInWithEmailAndPassword(
                                    email.getText().toString(),
                                    pass.getText().toString()
                            )
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>()
                            {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    Log.d("user","sucess");
                                    Intent intent=new Intent(MainActivity.this,MainScreen.class);
                                    startActivity(intent);

                                }
                            })

                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(
                                            MainActivity.this,
                                            "Failed",
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                            });
                }

            }
        });


    }
}