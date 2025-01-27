package com.emon.bookexchanger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.emon.bookexchanger.Fragment.Dashboard;
import com.emon.bookexchanger.Fragment.LoginFragment;
import com.emon.bookexchanger.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = prefs.edit();


        String email = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("email",  "");
        boolean OnBoard = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean("OnBoard",  false);



        //------------------------------------------------------------------------
        if (OnBoard == false) {
            startActivity(new Intent(this, OnBoard.class));
            finish();
        } else {
            if (email.length() <= 0) {
                startActivity(new Intent(this, Login_Register.class));
                finish();
            }
        }
        //------------------------------------------------------------------------



        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Dashboard()).commit();





    }
}