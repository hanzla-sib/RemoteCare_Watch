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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.androidgamesdk.gametextinput.Listener;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class StepsCounter extends Activity implements SensorEventListener {
    private PowerManager.WakeLock wakeLock;
    private TextView mTextView;
    String url1="",url2="";
    private int stepCount = 0;
    private SensorManager sensorManager;
    private Sensor stepDetectorSensor;
    private static final int REQUEST_ACTIVITY_RECOGNITION_PERMISSION = 1;
    double caloriesburnt=0.0;
    String useremail="";
    FirebaseAuth mAuth;
    JSONArray obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_steps_counter);
        mAuth= FirebaseAuth.getInstance();
        useremail = mAuth.getCurrentUser().getEmail();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("Ip", "");
        Log.d("url","=-----");
        Log.d("url",s1);
        url1 ="http://"+s1+"/smd_project_watch/update_daily_steps.php";
        url2 ="http://"+s1+"/smd_project_watch/initial_steps_from_DB.php";
        Log.d("url",url1);


        RequestQueue queue = Volley.newRequestQueue(StepsCounter.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url2, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                // on below line we are displaying a success toast message.
                Toast.makeText(StepsCounter.this, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    obj = new JSONArray(response);
                    for(int i=0;i<obj.length()-1;i++){
                        JSONObject jsonObject = obj.getJSONObject(i);
                        String stepsss = jsonObject.getString("steps");
                        stepCount=Integer.parseInt(stepsss);
                        mTextView.setText(String.valueOf(stepCount));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(StepsCounter.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("email",useremail);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);


        mTextView = findViewById(R.id.stepcounter);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyApp::MyWakelockTag");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
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
        if (stepDetectorSensor != null) {
            sensorManager.registerListener((SensorEventListener) this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
        Log.d("hello","hellll");
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            // Increment step count
            stepCount++;
            caloriesburnt = 0.04*stepCount;
            mTextView.setText(String.valueOf(stepCount));

            StringRequest request=new StringRequest(Request.Method.POST, url1, new com.android.volley.Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    Log.d("checking",response.toString());
//                                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
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
                    Log.d("------","dasd");
                    param.put("email",useremail);
                    param.put("steps",Integer.toString(stepCount));
                    param.put("calories_burn",Double.toString(caloriesburnt));
                    param.put("Motion","Resting");
                    return param;
                }
            };
            RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
            queue.add(request);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ACTIVITY_RECOGNITION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Log.d("pppp","permison granted");
            } else {
                Log.d("pppp","permison denied");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

}