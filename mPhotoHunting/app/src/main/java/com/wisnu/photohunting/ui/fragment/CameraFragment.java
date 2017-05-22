package com.wisnu.photohunting.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wisnu.photohunting.R;

public class CameraFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_camera_open, container, false);
        return view;

        /*EasyCamera               camera  = DefaultEasyCamera.open();
        EasyCamera.CameraActions actions = camera.startPreview(surface);
        EasyCamera.PictureCallback callback = new EasyCamera.PictureCallback() {
            public void onPictureTaken(byte[] data, EasyCamera.CameraActions actions) {
                // store picture
                Toast.makeText(getActivity(), "Picture has taken", Toast.LENGTH_SHORT).show();
            }
        };
        actions.takePicture(EasyCamera.Callbacks.create().withJpegCallback(callback));*/

    }

}
