package com.example.locationplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;

public class MapFragmentActivity extends Fragment implements OnMapReadyCallback {

    public View view;
    Context context;
    DetailActivity detailactivity;

    private NaverMap nmap;
    LatLng Location;
    Marker mk = new Marker();

    double latitude;
    double longitude;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);

        Bundle bundle = getArguments();
        latitude = bundle.getDouble("latitude", -1);
        longitude = bundle.getDouble("longitude", -1);

        context = container.getContext();
        detailactivity = (DetailActivity) context;

        MapFragment mapFragment = (MapFragment) detailactivity.getSupportFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            mapFragment = mapFragment.newInstance();
            detailactivity.getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        nmap = naverMap;
        UiSettings uisettings = naverMap.getUiSettings();
        uisettings.setLocationButtonEnabled(true);

        naverMap.setMapType(NaverMap.MapType.Basic);

        Location = new LatLng(latitude, longitude);

        CameraUpdate cameraUpdate = CameraUpdate.zoomTo(15);
        cameraUpdate = cameraUpdate.scrollTo(Location);
        nmap.moveCamera(cameraUpdate);

        LocationOverlay locationOverlay = nmap.getLocationOverlay();
        locationOverlay.setVisible(true);
        locationOverlay.setPosition(Location);

        mk.setVisible(false);
        mk.setPosition(Location);
        mk.setMap(nmap);
        mk.setVisible(true);


    }
}