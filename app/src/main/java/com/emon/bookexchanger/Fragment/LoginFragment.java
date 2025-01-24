package com.emon.bookexchanger.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.emon.bookexchanger.Api_Url;
import com.emon.bookexchanger.MainActivity;
import com.emon.bookexchanger.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginFragment extends Fragment {

    public TextInputEditText edemail, edpass;
    TextInputLayout edpasslay;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CheckBox checkboxRememberMe; // Add Remember Me checkbox

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button loginbt = view.findViewById(R.id.loginbt);
        edemail = view.findViewById(R.id.edemail);
        edpass = view.findViewById(R.id.edpass);
        edpasslay = view.findViewById(R.id.edpasslay);
        checkboxRememberMe = view.findViewById(R.id.checkbox_rememberme); // Initialize Remember Me checkbox

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Restore saved credentials
        String savedEmail = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("remember_email", "");
        String savedPassword = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("remember_password", "");
        boolean isRemembered = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("is_remembered", false);

        if (isRemembered) {
            edemail.setText(savedEmail);
            edpass.setText(savedPassword);
            checkboxRememberMe.setChecked(true);
        }

        loginbt.setOnClickListener(v -> {
            boolean isValid = true;

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

            if (isValid) {
                // Save credentials if Remember Me is checked
                if (checkboxRememberMe.isChecked()) {
                    editor.putString("remember_email", edemail.getText().toString());
                    editor.putString("remember_password", edpass.getText().toString());
                    editor.putBoolean("is_remembered", true);

                } else {
                    editor.putString("remember_email", "");
                    editor.putString("remember_password", "");
                    editor.putBoolean("is_remembered", false);
                }
                editor.apply();

                // Call login request
                LogigRequest(edemail.getText().toString(), edpass.getText().toString());
            }
        });

        edpass.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                if (!TextUtils.isEmpty(charSequence)) {
                    edpass.setError(null);
                    edpasslay.setEndIconVisible(true);
                } else {
                    edpasslay.setEndIconVisible(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (!TextUtils.isEmpty(charSequence)) {
                    edpass.setError(null);
                    edpasslay.setEndIconVisible(true);
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {
            }
        });

        return view;
    }

    public void LogigRequest(String email, String password) {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading... Please wait");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        JSONObject requestData = new JSONObject();
        try {
            requestData.put("email", email);
            requestData.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Api_Url.login_url, requestData,
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


    private void setupClearFocusListeners(View view) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        @SuppressLint("ClickableViewAccessibility") View.OnTouchListener clearFocusTouchListener = (v, event) -> {

            edemail.clearFocus();
            edpass.clearFocus();


            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            return false;
        };

        view.findViewById(R.id.main).setOnTouchListener(clearFocusTouchListener);
        /*view.findViewById(R.id.linerlay_set).setOnTouchListener(clearFocusTouchListener);
        view.findViewById(R.id.scroll_set).setOnTouchListener(clearFocusTouchListener);*/
    }
}