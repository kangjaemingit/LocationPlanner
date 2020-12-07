package com.example.locationplanner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PathOverlay;

import java.util.Arrays;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    //private MapView mapview;

    private NaverMap nmap;
    LatLng prev_Loc = null;
    LatLng curr_Loc;
    Marker mk = new Marker();

    LocationManager locationManager;
    LocationListener locationListener;

    @Nullable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = mapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        UiSettings uisettings = naverMap.getUiSettings();
        uisettings.setLocationButtonEnabled(true);

        naverMap.setMapType(NaverMap.MapType.Basic);

        nmap = naverMap;

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateMap(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                alertStatus(provider);
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                alertProvider(provider);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                checkProvider(provider);
            }
        };
        
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }

        String locationProvider;
        locationProvider = LocationManager.GPS_PROVIDER;
        locationManager.requestLocationUpdates(locationProvider, 1, 1, locationListener);

        locationProvider = LocationManager.NETWORK_PROVIDER;
        locationManager.requestLocationUpdates(locationProvider, 1, 1, locationListener);
    }

    public void checkProvider(String provider){
        Toast.makeText(this, provider + "에 의한 위치서비스가 꺼져있습니다. 위치서비스를 켜주세요", Toast.LENGTH_SHORT).show();

        //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        //startActivity(intent);
    }

    public void alertProvider(String provider){
        Toast.makeText(this, provider + "서비스가 켜져습니다!",Toast.LENGTH_SHORT).show();
    }

    public void alertStatus(String provider) {
        Toast.makeText(this, "위치서비스가" + provider + "로 변경되었습니다!", Toast.LENGTH_SHORT).show();
    }

    public void updateMap(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        curr_Loc = new LatLng(latitude, longitude);

        CameraUpdate cameraUpdate = CameraUpdate.zoomTo(15);
        cameraUpdate = cameraUpdate.scrollTo(curr_Loc);
        nmap.moveCamera(cameraUpdate);

        LocationOverlay locationOverlay = nmap.getLocationOverlay();
        locationOverlay.setVisible(true);
        locationOverlay.setPosition(curr_Loc);

        mk.setVisible(false);
        mk.setPosition(curr_Loc);
        mk.setMap(nmap);
        mk.setVisible(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationManager != null)
            locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(this, RegistrationActivity.class);
        startActivity(it);
        finish();
    }
}
