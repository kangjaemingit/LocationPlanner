package com.example.locationplanner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    DBManager dbmanager;
    SQLiteDatabase sqlitedb;

    String str_memo;
    String str_location;
    double latitude;
    double longitude;

    private Button delete;

    MapFragmentActivity mapfragment;
    FragmentManager manager;
    LatLng Location;
    Marker mk = new Marker();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        delete = findViewById(R.id.delete);
        delete.setOnClickListener(this);

        TextView tv_memo = (TextView)findViewById(R.id.memo);
        TextView tv_location = (TextView)findViewById(R.id.location);
        TextView tv_latitude = (TextView)findViewById(R.id.latitude);
        TextView tv_longitude = (TextView)findViewById(R.id.longitude);

        Intent it = getIntent();
        str_memo = it.getStringExtra("it_memo");

        try{
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getReadableDatabase();

            Cursor cursor = sqlitedb.query("LPDB", null, "memo = ?", new String[]{str_memo}, null, null, null, null);

            if(cursor.moveToNext()){
                str_location = cursor.getString(cursor.getColumnIndex("location"));
                latitude = cursor.getDouble(cursor.getColumnIndex("lat"));
                longitude = cursor.getDouble(cursor.getColumnIndex("lon"));
            }

            sqlitedb.close();
            dbmanager.close();
        } catch(SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        tv_memo.setText(str_memo);
        tv_location.setText(str_location);
        tv_latitude.setText("" + latitude);
        tv_longitude.setText("" + longitude);

        mapfragment = new MapFragmentActivity();

        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);

        mapfragment.setArguments(bundle);

        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_map, mapfragment).commit();



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
        if(v.getId() == R.id.delete){
            try{
                dbmanager = new DBManager(this);
                sqlitedb = dbmanager.getReadableDatabase();
                sqlitedb.delete("LPDB", "memo = ?", new String[]{str_memo});

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
}
