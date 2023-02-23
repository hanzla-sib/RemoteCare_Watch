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


public class Ip_change_screen extends Activity {

    Button submit_ip;
    EditText ip_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ip_change_screen);
        submit_ip=findViewById(R.id.submit_ip);
        ip_address=findViewById(R.id.ipaddress);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        submit_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ip_address.getText().toString().equals(""))
                {
                }
                else
                {
                    String Ip = ip_address.getText().toString();
                    Log.d("Ip == " , Ip);
                    myEdit.putString("Ip", Ip);
                    myEdit.apply();
                    startActivity(new Intent(Ip_change_screen.this, MainActivity.class));
                }
            }
        });
    }
}