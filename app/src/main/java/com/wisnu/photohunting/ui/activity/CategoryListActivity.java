package com.wisnu.photohunting.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.wisnu.photohunting.R;
import com.wisnu.photohunting.adapter.CategoryListAdapter;
import com.wisnu.photohunting.adapter.PhotoFeedAdapter;
import com.wisnu.photohunting.model.Photo;
import com.wisnu.photohunting.network.Request;
import com.wisnu.photohunting.network.Response;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CategoryListActivity extends AppCompatActivity {
    private static final String TAG = CategoryListActivity.class.getSimpleName();

    public static String mCategorySelected = null;
    private List<Photo>  mPhotoCatList;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylist);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.photo_support_toolbar);
        mToolbar.setTitle("Daftar Lokasi Menarik");
        setSupportActionBar(mToolbar);

        mPhotoCatList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_referensi);
        mRecyclerView.setAdapter(new CategoryListAdapter(this, mPhotoCatList));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (getIntent() != null) {
            mCategorySelected = getIntent().getStringExtra("catId");
            fetchAllFeeds(mCategorySelected);
        }
    }

    /**
     * Method yang bertugas untuk mengambil data dari server
     * Request      : Photo.get_with_cid()
     */
    private void fetchAllFeeds(String cid) {
        Request.Photo.get_photo_with_cid(cid).enqueue(new Callback<Response.Photo>() {
            @Override
            public void onResponse(Call<Response.Photo> call, retrofit2.Response<Response.Photo> response) {
                Response.Photo data = response.body();
                if (null != mPhotoCatList && null != data) {
                    mPhotoCatList.clear();
                    mPhotoCatList.addAll(response.body().getListPhotoFeeds());

                    for (Photo photo : data.getListPhotoFeeds()) {
                        Log.d(TAG, "onResponse: " + photo.toString());
                    }
                } else {
                    Log.d(TAG, "onResponse: listPhotoFeeds is null");
                }

                mRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Response.Photo> call, Throwable t) {
                Toast.makeText(CategoryListActivity.this, R.string.message_server_connect_is_failed,
                        Toast.LENGTH_SHORT).show();

                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}
