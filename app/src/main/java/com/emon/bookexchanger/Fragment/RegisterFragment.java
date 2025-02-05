package com.emon.bookexchanger.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.emon.bookexchanger.Api_Url;
import com.emon.bookexchanger.MainActivity;
import com.emon.bookexchanger.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    public TextInputEditText edemail, edpass, edname, ed_address;
    private TextInputLayout edpasslay;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CheckBox checkbox_term;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View registerView = inflater.inflate(R.layout.fragment_register, container, false);

        Button registerbt = registerView.findViewById(R.id.registerbt);
        edname = registerView.findViewById(R.id.edname);
        ed_address = registerView.findViewById(R.id.ed_address);
        edemail = registerView.findViewById(R.id.edemail_reg);
        edpass = registerView.findViewById(R.id.edpass_reg);
        edpasslay = registerView.findViewById(R.id.edpasslay);
        checkbox_term = registerView.findViewById(R.id.checkbox_term);

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Initially disable the register button
        registerbt.setEnabled(false);

        // Enable button only when the checkbox is checked
        checkbox_term.setOnCheckedChangeListener((buttonView, isChecked) -> registerbt.setEnabled(isChecked));

        registerbt.setOnClickListener(v -> {
            boolean isValid = true;

            // Validate inputs
            if (TextUtils.isEmpty(edname.getText().toString())) {
                edname.setError("Input Your Name");
                isValid = false;
            } else {
                edname.setError(null);
            }

            if (TextUtils.isEmpty(ed_address.getText().toString())) {
                ed_address.setError("Input Your Address");
                isValid = false;
            } else {
                ed_address.setError(null);
            }

            if (TextUtils.isEmpty(edemail.getText().toString())) {
                edemail.setError("Input Your Email");
                isValid = false;
            } else {
                edemail.setError(null);
            }

            if (TextUtils.isEmpty(edpass.getText().toString().trim())) {
                edpasslay.setEndIconVisible(false);
                edpass.setError("Input Your Password");
                isValid = false;
            } else {
                edpass.setError(null);
                edpasslay.setEndIconVisible(true);
            }

            // If all inputs are valid, proceed with login request
            if (isValid) {
                editor.putString("pass", null);
                RegisterRequest(edname.getText().toString(), edemail.getText().toString(),edpass.getText().toString(), ed_address.getText().toString());
            }
        });

        // Password input behavior
        edpass.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (!TextUtils.isEmpty(charSequence)) {
                    edpass.setError(null);
                    edpasslay.setEndIconVisible(true);
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        return registerView;
    }

    public void RegisterRequest(String name , String email, String password,String address) {

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading... Please wait");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        JSONObject requestData = new JSONObject();
        try {
            requestData.put("name", name);
            requestData.put("address", address);
            requestData.put("email", email);
            requestData.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Api_Url.register_url, requestData,
                response -> {
                    try {
                        progressDialog.dismiss();
                        Log.d("Volley Response", response.toString());

                        JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");

                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        if (success) {

                            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", edemail.getText().toString());
                            editor.apply();

                            startActivity(new Intent(getContext(), MainActivity.class));
                            requireActivity().finish();

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
                                Toast.makeText(getContext(), "Bad Request - Invalid input", Toast.LENGTH_SHORT).show();
                                break;
                            case 401:
                                Toast.makeText(getContext(), "Wrong User Email or Password", Toast.LENGTH_SHORT).show();
                                break;
                            case 500:
                                Toast.makeText(getContext(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getContext(), "Unexpected Error", Toast.LENGTH_SHORT).show();
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
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }
}
