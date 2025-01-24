package com.emon.bookexchanger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.emon.bookexchanger.Fragment.LoginFragment;
import com.emon.bookexchanger.Fragment.RegisterFragment;
import com.google.android.material.tabs.TabLayout;

public class Login_Register extends AppCompatActivity {

    TabLayout tabLayout;
    RelativeLayout mainlay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabLayout = findViewById(R.id.tablayout);
        mainlay = findViewById(R.id.main);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentlaytab, new LoginFragment()).commit();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentlaytab, new LoginFragment()).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentlaytab, new RegisterFragment()).commit();

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setupClearFocusListeners();


    }


    @SuppressLint("ClickableViewAccessibility")
    private void setupClearFocusListeners() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        View.OnTouchListener clearFocusTouchListener = (v, event) -> {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentlaytab);

            if (fragment instanceof LoginFragment) {
                LoginFragment loginFragment = (LoginFragment) fragment;
                if (loginFragment.edemail != null) loginFragment.edemail.clearFocus();
                if (loginFragment.edpass != null) loginFragment.edpass.clearFocus();
            } else if (fragment instanceof RegisterFragment) {
                RegisterFragment registerFragment = (RegisterFragment) fragment;
                if (registerFragment.edname != null) registerFragment.edname.clearFocus();
                if (registerFragment.ed_address != null) registerFragment.ed_address.clearFocus();
                if (registerFragment.edemail != null) registerFragment.edemail.clearFocus();
                if (registerFragment.edpass != null) registerFragment.edpass.clearFocus();
            }

            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            return false;
        };

        mainlay.setOnTouchListener(clearFocusTouchListener);
    }

}