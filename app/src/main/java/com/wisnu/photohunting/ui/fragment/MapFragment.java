package com.wisnu.photohunting.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orhanobut.hawk.Hawk;
import com.wisnu.photohunting.PhotoHuntingApp;
import com.wisnu.photohunting.R;
import com.wisnu.photohunting.model.Photo;
import com.wisnu.photohunting.network.Request;
import com.wisnu.photohunting.network.Response;
import com.wisnu.photohunting.savingstate.PhotoFeedList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private final String TAG = "com.wisnu.photohunting";
    private GoogleMap mMap;

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View               view        = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_location);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng initialLoc = new LatLng(-8.5906454, 116.4567144);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(initialLoc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(3.48f));
        mMap.addMarker(new MarkerOptions().position(initialLoc).title("Inisial Lokasi"));

        addMarkerOnMap();
    }

    private void addMarkerOnMap() {
        Log.d(TAG, "addMarkerOnMap: =============================================================");
        for (Photo photo : PhotoFeedList.getInstance().getPhotoList()) {

            Log.d(TAG, "addMarkerOnMap: " + photo.toString());

            double latitude  = Double.parseDouble(photo.getPhotoLatitude());
            double longitude = Double.parseDouble(photo.getPhotoLongitude());

            MarkerOptions marker = new MarkerOptions();
            marker.position(new LatLng(latitude, longitude))
                    .title(photo.getPhotoName());

            mMap.addMarker(marker);
        }
    }
}
