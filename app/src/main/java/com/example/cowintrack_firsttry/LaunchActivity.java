package com.example.cowintrack_firsttry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LaunchActivity extends AppCompatActivity {

    public static final String PINCODE_KEY = "com.example.cowintrack_firsttry.LaunchActivity.PinCode";
    public static final String DATE_KEY = "com.example.cowintrack_firsttry.LaunchActivity.Date";

    private EditText editTextPinCode;
    private EditText editTextDate;
    private Button buttonSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        editTextPinCode = findViewById(R.id.editTextPinCode);
        editTextDate = findViewById(R.id.editTextDate);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PinCode = editTextPinCode.getText().toString();
                String DateInString = editTextDate.getText().toString();

                int len = PinCode.length();
                if(len != 6) {
                    Toast.makeText(LaunchActivity.this, "Please enter a valid pincode!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int PinCodeInt = Integer.parseInt(PinCode);
                    Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                    intent.putExtra(PINCODE_KEY, PinCode);
                    intent.putExtra(DATE_KEY, DateInString);
                    startActivity(intent);

                } catch (NumberFormatException e) {
                    Toast.makeText(LaunchActivity.this, "Please enter a valid pincode!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}