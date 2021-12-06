package com.example.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.instagramclone.fragment.AddPostFragment;
import com.example.instagramclone.fragment.HomeFragment;
import com.example.instagramclone.fragment.ProfileFragment;
import com.example.instagramclone.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView navBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportActionBar().setTitle("Instagram");

        navBtn = findViewById(R.id.bottomNavBar);

        navBtn.setOnNavigationItemSelectedListener(this);
        navBtn.setSelectedItemId(R.id.home);
    }

    HomeFragment homeFragment = new HomeFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    AddPostFragment addPostFragment = new AddPostFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home){
            //home fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.dashboardFrame,
                    homeFragment).commit();
            return true;
        }else if(item.getItemId() == R.id.addPost){
            //add post fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.dashboardFrame,
                    addPostFragment).commit();
            return true;
        }else if(item.getItemId() == R.id.profile){
            //profile framgemt
            getSupportFragmentManager().beginTransaction().replace(R.id.dashboardFrame,
                    profileFragment).commit();
            return  true;
        }
        return false;
    }
}