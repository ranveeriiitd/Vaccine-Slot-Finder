package com.example.cowintrack_firsttry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    TextView textViewData;
    LinearLayout LinearLayout_llMain;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //editTextPinCode = findViewById(R.id.editTextPinCode);
        //editTextDate = findViewById(R.id.editTextDate);
        //buttonSubmit = findViewById(R.id.buttonSubmit);

        //textViewData = findViewById(R.id.textViewData);
        LinearLayout_llMain = findViewById(R.id.LinearLayout_llMain);
        requestQueue = Volley.newRequestQueue(this);



        String PinCode = getIntent().getStringExtra(LaunchActivity.PINCODE_KEY);
        String Date = getIntent().getStringExtra(LaunchActivity.DATE_KEY);
        Log.d("Gotcha", "PINCODE: " + PinCode + ", DATE: " + Date);

        parseJSON(Date, PinCode);


    }
    private String getNextDate(String curDate) {
        String nextDate = "";
        try {
            Calendar today = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date date = format.parse(curDate);
            today.setTime(date);
            today.add(Calendar.DAY_OF_YEAR, 1);
            nextDate = format.format(today.getTime());
        } catch (Exception e) {
            return nextDate;
        }
        return nextDate;
    }

    private void parseJSON(String date, String pincode) {
        int how_Many = 30;

        while(how_Many-- > 0) {
            String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode=" + pincode + "&date=" + date;
            String Date = date;
            //Log.d("Gotcha", "The next date is: " + Date.toString());
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray output = (JSONArray) response.get("sessions");
                                int no_of_center = output.length();
                                //Log.d("Gotcha", "The Request is: " + output + " :::: And Length is: " + no_of_center);

                                String to_return = "";
                                to_return += ("Date: " + Date + "\n");
                                int i = 0;
                                while (i < no_of_center) {
                                    //Log.d("Centers", "The Request is: " + (output.get(i).toString()));
                                    JSONObject obj = ((JSONObject) output.get(i));
                                    to_return += (((i + 1) + ".\n Center ID: " + obj.getString("center_id") + "\n Name: " + obj.getString("name") +
                                            "\n Address: " + obj.getString("address") + "\n Fee: " + obj.getString("fee_type") + "\n Vaccine: " + obj.getString("vaccine") +
                                            "\n Slots: " + obj.getString("slots").toString() + "\n"));
                                    i++;
                                }

                                Log.d("Gotcha", to_return);

                                if(no_of_center != 0) {
                                    TextView textView = new TextView(MainActivity.this);
                                    textView.setText(to_return);
                                    textView.setTextSize(16);
                                    LinearLayout_llMain.addView(textView);
                                    //ToReturn += (to_return);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error", "Error 403, Something went wrong with CO-WIN API!");
                        }
                    }
            );
            requestQueue.add(jsonObjectRequest);
            date = getNextDate(Date);
        }
    }
}