package com.wisnu.photohunting.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.hawk.Hawk;
import com.wisnu.photohunting.PhotoHuntingApp;
import com.wisnu.photohunting.R;
import com.wisnu.photohunting.model.Photo;
import com.wisnu.photohunting.savingstate.PhotoFeedList;
import com.wisnu.photohunting.ui.activity.WelcomeActivity;

import java.util.ArrayList;

public class UserFragment extends Fragment {
    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        view.findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hawk.remove(PhotoHuntingApp.USER_ID);
                Hawk.remove(PhotoHuntingApp.USER_NAME);
                Hawk.remove(PhotoHuntingApp.USER_MAIL);
                Hawk.remove(PhotoHuntingApp.USER_PHOTO);
                Hawk.remove(PhotoHuntingApp.USER_STATUS);
                PhotoFeedList.getInstance().setPhotoList(new ArrayList<Photo>());

                startActivity(new Intent(getActivity(), WelcomeActivity.class));
                getActivity().finish();
            }
        });
        return view;
    }
}
