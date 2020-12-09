package com.example.locationplanner;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_map;
    DBManager dbmanager;
    SQLiteDatabase sqlitedb;

    String searchmap;
    double lat;
    double lon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        tv_map = findViewById(R.id.tv_map);
        tv_map.setOnClickListener(this);

        Intent it = getIntent();
        searchmap = it.getStringExtra("it_map");
        lat = it.getDoubleExtra("it_lat", -5);
        lon = it.getDoubleExtra("it_lon", -5);

        EditText et_location = (EditText)findViewById(R.id.location);
        et_location.setText(searchmap);


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
            Intent it = new Intent(this, MapActivity.class);
            startActivity(it);
            finish();
        }
    }

    public void registration(View v){
        EditText et_memo = (EditText)findViewById(R.id.memo);
        String str_memo = et_memo.getText().toString();
        if(str_memo == ""){
            Toast.makeText(this, "메모를 입력하세요",Toast.LENGTH_SHORT).show();
            return;
        }

        EditText et_location = (EditText)findViewById(R.id.location);
        String str_location = et_location.getText().toString();
        double latitude = lat;
        double longitude = lon;

        try{
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("memo", str_memo);
            values.put("location", str_location);
            values.put("lat", latitude);
            values.put("lon", longitude);

            long newRowID = sqlitedb.insert("LPDB", null, values);

            sqlitedb.close();
            dbmanager.close();
        } catch(SQLiteException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        finish();
    }
}
