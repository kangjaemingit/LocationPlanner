package com.example.locationplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.map.e;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button register;
    DBManager dbmanager;
    SQLiteDatabase sqlitedb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 앱 실행시 Background Service 실행
        Intent serviceintent = new Intent(this, LocationService.class);
        startService(serviceintent);

        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        LinearLayout layout = (LinearLayout)findViewById(R.id.LocationPlanner);


        try{
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getReadableDatabase();
            Cursor cursor = sqlitedb.query("LPDB", null,null,null,null,null,null);

            int i = 0;
            while(cursor.moveToNext()){
                String str_memo = cursor.getString(cursor.getColumnIndex("memo"));
                String str_location = cursor.getString(cursor.getColumnIndex("location"));

                LinearLayout layout_item = new LinearLayout(this);
                layout_item.setOrientation(LinearLayout.VERTICAL);
                layout_item.setPadding(20,10,20,10);
                layout_item.setId(i);
                layout_item.setTag(str_memo);

                TextView tv_memo = new TextView(this);
                tv_memo.setText(str_memo);
                tv_memo.setTextSize(30);
                tv_memo.setBackgroundColor(Color.argb(50,0,255,0));
                layout_item.addView(tv_memo);

                TextView tv_location = new TextView(this);
                tv_location.setText("위치 : " + str_location);
                layout_item.addView(tv_location);

                layout_item.setOnClickListener(this);

                layout.addView(layout_item);

                i++;
            }

            cursor.close();
            sqlitedb.close();
            dbmanager.close();

        }  catch(SQLiteException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.register){
            Intent it = new Intent(this, RegistrationActivity.class);
            startActivity(it);
            finish();
        }
        else{
            int id = v.getId();

            LinearLayout layout_item = (LinearLayout)findViewById(id);
            String str_memo = (String)layout_item.getTag();

            Intent it = new Intent(this, DetailActivity.class);
            it.putExtra("it_memo", str_memo);
            startActivity(it);
            finish();
        }

    }
}