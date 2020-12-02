package com.example.locationplanner;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;

public class MapActivity extends Fragment implements OnMapReadyCallback {

    //private MapView mapview;

    public View view;
    Context context;
    RegistrationActivity regisActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_map, container, false);

        context = container.getContext();
        regisActivity = (RegistrationActivity) context;

        MapFragment mapFragment = (MapFragment)regisActivity.getSupportFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            mapFragment = mapFragment.newInstance();
            regisActivity.getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        return view;
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        setContentView(R.layout.activity_map);
//
//        MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
//        if(mapFragment == null){
//            mapFragment = mapFragment.newInstance();
//            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
//        }
//        mapFragment.getMapAsync(this);
//
////        mapview = findViewById(R.id.map);
////        mapview.onCreate(savedInstanceState);
////
////        naverMapBasicSettings();
//
//    }
//
//    public void naverMapBasicSettings(){
//        mapview.getMapAsync(this);
//    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        UiSettings uisettings = naverMap.getUiSettings();
        uisettings.setLocationButtonEnabled(true);

        naverMap.setMapType(NaverMap.MapType.Basic);
    }
}
