package com.emon.bookexchanger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Forget_password extends AppCompatActivity {
    TextInputEditText edemail;
    Button forgetbt;
    TextView backtologinbt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        edemail = findViewById(R.id.edemail);
        forgetbt = findViewById(R.id.forgetbt);




        forgetbt.setOnClickListener(v -> {
            boolean isValid = true;

            if (TextUtils.isEmpty(Objects.requireNonNull(edemail.getText()).toString())) {
                edemail.setError("Input Your Email");
                isValid = false;
            } else {
                edemail.setError(null);
            }

            if (isValid) {
                sendPasswordResetRequest(edemail.getText().toString());

            }
        });






    }

    public void sendPasswordResetRequest(String email) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading... Please wait");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        JSONObject requestData = new JSONObject();
        try {
            requestData.put("email", email);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Api_Url.password_reset, requestData,
                response -> {
                    try {
                        progressDialog.dismiss();
                        Log.d("Volley Response", response.toString());

                        JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");


                        if (success) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        Log.d("Error", "Status Code: " + statusCode);

                        switch (statusCode) {
                            case 400:
                                Toast.makeText(this, "Bad Request - Invalid input", Toast.LENGTH_SHORT).show();
                                break;
                            case 401:
                                Toast.makeText(this, "Wrong User Email or Password", Toast.LENGTH_SHORT).show();
                                break;
                            case 500:
                                Toast.makeText(this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(this, "Unexpected Error", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } else {
                        Log.d("Error", "No network response");
                    }
                }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "test");
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);



    }
}