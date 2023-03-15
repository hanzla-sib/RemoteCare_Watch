package com.FYP.remotecare_watch_fyp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class Heart_beat_warch extends Activity implements SensorEventListener {
    private PowerManager.WakeLock wakeLock;
    private TextView mTextView;
    String url1="",url2="";
    private int stepCount = 0;
    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private static final int REQUEST_ACTIVITY_RECOGNITION_PERMISSION = 1;
    private int heartRateReadings;
    private final int HEART_RATE_READINGS_THRESHOLD = 10;
    private int stop=0;
    private final int stoping_thres = 7;
    String useremail="";
    FirebaseAuth mAuth;
    JSONArray obj;
    ProgressBar simpleProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_beat_warch);
        mAuth= FirebaseAuth.getInstance();
        useremail = mAuth.getCurrentUser().getEmail();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("Ip", "");
        Log.d("url","=-----");
        Log.d("url",s1);

        url1 ="http://"+s1+"/smd_project_watch/insert_heartbeat.php";
         simpleProgressBar=(ProgressBar) findViewById(R.id.progress_bar); // initiate the progress bar
        simpleProgressBar.setMax(100); // 100 maximum value for the progress bar

        mTextView = findViewById(R.id.heartcounter);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyApp::MyWakelockTag");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                    REQUEST_ACTIVITY_RECOGNITION_PERMISSION);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
        if (heartRateSensor != null) {
            sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            // Increment step count
            int heartRate = (int) event.values[0];
            heartRateReadings++;

            // Check if we have reached the threshold for a complete detection
            if (heartRateReadings >= HEART_RATE_READINGS_THRESHOLD) {
                mTextView.setText(String.valueOf(heartRate));
                stop++;
                if(stop==stoping_thres){
                     simpleProgressBar.setVisibility(View.GONE);
                    mTextView.setText(String.valueOf(heartRate));
                    if(heartRate!=0){
                        StringRequest request=new StringRequest(Request.Method.POST, url1, new com.android.volley.Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response)
                            {
                                Log.d("checking",response.toString());
//                               Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                            }
                        })
                        {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> param=new HashMap<String,String>();
                                param.put("email",useremail);
                                param.put("h_rate",String.valueOf(heartRate));
                                return param;
                            }
                        };
                        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
                        queue.add(request);
                    }
                    else{

                        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);

                        mTextView.setText("Place The watch on wrist");
                    }

                    sensorManager.unregisterListener(this);
                }
            }



        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACTIVITY_RECOGNITION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Log.d("pppp", "permison granted");
            } else {
                Log.d("pppp", "permison denied");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

}