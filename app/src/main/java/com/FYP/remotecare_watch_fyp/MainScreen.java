package com.FYP.remotecare_watch_fyp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;


import com.google.firebase.auth.FirebaseAuth;

public class MainScreen extends Activity {
    CardView heart,step,Logout;
    FirebaseAuth auth1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main_screen);

        auth1=FirebaseAuth.getInstance();
        heart=findViewById(R.id.Heartbeat);
        step=findViewById(R.id.steps);
        Logout=findViewById(R.id.Logout);
        step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainScreen.this,StepsCounter.class);
                startActivity(intent);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth1.signOut();
//                finish();
                startActivity(new Intent(MainScreen.this, MainActivity.class));
            }
        });
heart.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(MainScreen.this,Heart_beat_warch.class);
        startActivity(intent);
    }
});

    }
}