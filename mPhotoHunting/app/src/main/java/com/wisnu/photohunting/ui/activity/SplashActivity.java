package com.wisnu.photohunting.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wisnu.photohunting.ui.activity.HomeActivity;
import com.wisnu.photohunting.ui.activity.WelcomeActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }

    /*private void fetchAllPhoto() {
        Request.Photo.get_all().enqueue(new Callback<Response.Photo>() {
            @Override
            public void onResponse(Call<Response.Photo> call, retrofit2.Response<Response.Photo> response) {
                List<Photo> listPhotoFeeds = PhotoFeedList.getInstance().getPhotoList();
                if (listPhotoFeeds != null) {
                    listPhotoFeeds.clear();
                    listPhotoFeeds.addAll(response.body().getListPhotoFeeds());
                } else {
                    Utils.showOnConsole("SplashActivity", "onResponse : listPhotoFeeds is null");
                }

                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            }

            @Override
            public void onFailure(Call<Response.Photo> call, Throwable t) {
                Utils.showOnConsole("PhotoFeedFragment", "onFailure : " + t.getLocalizedMessage());
                Utils.showToast(SplashActivity.this, "Cant reach server at the moment");
            }
        });
    }*/
}
