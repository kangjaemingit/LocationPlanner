package com.example.locationplanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_map;
    FragmentManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        tv_map = findViewById(R.id.tv_map);
        tv_map.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_map){
            manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_map, new MapActivity()).commit();
        }
    }
}
