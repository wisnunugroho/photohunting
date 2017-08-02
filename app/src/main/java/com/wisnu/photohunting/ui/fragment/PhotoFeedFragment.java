package com.wisnu.photohunting.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wisnu.photohunting.R;
import com.wisnu.photohunting.adapter.PhotoFeedAdapter;
import com.wisnu.photohunting.model.Photo;
import com.wisnu.photohunting.model.User;
import com.wisnu.photohunting.network.Request;
import com.wisnu.photohunting.network.Response;
import com.wisnu.photohunting.savingstate.PhotoFeedList;
import com.wisnu.photohunting.savingstate.UserData;
import com.wisnu.photohunting.system.Utils;
import com.wisnu.photohunting.ui.activity.CommentListActivity;
import com.wisnu.photohunting.ui.activity.LikeListActivity;
import com.wisnu.photohunting.ui.activity.PhotoActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class PhotoFeedFragment extends Fragment {
    private final String TAG = "com.wisnu.photohunting";

    private List<Photo> mPhotoPostList = new ArrayList<>();

    private RecyclerView       mRecyclerView;
    private SwipeRefreshLayout mRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * Method refresh yang akan memperbarui data dari server dengan menjalankan method
         * fetchAllFeeds
         */
        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.dashboard_refresh);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchAllFeeds();
            }
        });

        /**
         * Check apakah data photo list sudah diambil dari server
         * Jika ya, maka set mPhotoPostList dengan data yang sudah ada
         * Jika tidak, maka ambil data dari server
         */
        mPhotoPostList = PhotoFeedList.getInstance().getPhotoList();
        if (null == mPhotoPostList || mPhotoPostList.size() == 0)
            fetchAllFeeds();

        /**
         * Membuat object baru dari kelas PhotoFeedAdapter yang akan digunakan untuk menghubungkan
         * data dengan view
         * Binding Adapter      : PhotoFeedAdapter.class
         * Data                 : List<Photo> mPhotoPostList
         */
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setAdapter(new PhotoFeedAdapter(mPhotoPostList));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ((PhotoFeedAdapter) mRecyclerView.getAdapter()).onItemClickedListener(new PhotoFeedAdapter.onClickListener() {
            @Override
            public void onActionClickView(int itemCode, String pid) {
                User   user   = UserData.getInstance().getUser();
                String userId = user.getUserId();
                switch (itemCode) {
                    case PhotoFeedAdapter.ITEM_COUNT_LIKE:
                        Log.d(TAG, "onActionClickView: photoId : " + pid);
                        PhotoActivity.photoId = pid;
                        LikeListActivity.photoId = pid;
                        startActivity(new Intent(getActivity(), LikeListActivity.class));
                        break;
                    case PhotoFeedAdapter.ITEM_COUNT_COMMENT:
                        Log.d(TAG, "onActionClickView: photoId : " + pid);
                        PhotoActivity.photoId = pid;
                        CommentListActivity.photoId = pid;
                        startActivity(new Intent(getActivity(), CommentListActivity.class));
                        break;
                    case PhotoFeedAdapter.ITEM_BUTTON_LIKE:
                        Log.d(TAG, "onActionClickView: userId : " + userId);

                        //TODO Delete this line if you've finished debugging method like.
                        userId = UserData.getInstance().getUser().getUserId();
                        PhotoActivity.userId = userId;
                        PhotoActivity.photoId = pid;
                        like(pid, userId);
                        break;
                    case PhotoFeedAdapter.ITEM_BUTTON_COMMENT:

                        //TODO Delete this line if you've finished debugging method comment.
                        userId = UserData.getInstance().getUser().getUserId();
                        PhotoActivity.userId = userId;
                        PhotoActivity.photoId = pid;
                        comment(pid, userId);
                        break;
                }
            }

            @Override
            public void onPhotoClickView(Photo photo) {
                onPhotoItemClicked(photo);
            }
        });
    }

    /**
     * Method yang bertugas untuk mengambil data dari server
     * Request      : Photo.get_all()
     */
    private void fetchAllFeeds() {
        mRefresh.setRefreshing(true);
        Request.Photo.get_all().enqueue(new Callback<Response.Photo>() {
            @Override
            public void onResponse(Call<Response.Photo> call, retrofit2.Response<Response.Photo> response) {
                mRefresh.setRefreshing(false);

                Response.Photo data = response.body();
                if (null != mPhotoPostList && null != data) {
                    mPhotoPostList.clear();
                    mPhotoPostList.addAll(response.body().getListPhotoFeeds());
                } else {
                    Log.d(TAG, "onResponse: listPhotoFeeds is null");
                }

                for (Photo photo : data.getListPhotoFeeds()) {
                    Log.d(TAG, "onResponse: " + photo.toString());
                }

                mRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Response.Photo> call, Throwable t) {
                mRefresh.setRefreshing(false);
                Toast.makeText(getActivity(), R.string.message_server_connect_is_failed,
                               Toast.LENGTH_SHORT).show();

                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    /**
     * Dijalankan jika user memberikan like foto
     *
     * @param photoId
     * @param userId
     */
    private void like(String photoId, String userId) {
        Log.d(TAG, "like: " + userId + " on photoId " + photoId);

        Request.Photo.add_like(photoId, userId).enqueue(new Callback<Response.Basic>() {
            @Override
            public void onResponse(Call<Response.Basic> call, retrofit2.Response<Response.Basic> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                    Utils.showToast(getActivity(), "Anda menyukai foto ini");
                } else {
                    Utils.showToast(getActivity(), "Anda sudah menyukai foto ini");
                    Log.d(TAG, "onResponse: Gagal menambahkan like pada foto");
                }
            }

            @Override
            public void onFailure(Call<Response.Basic> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(getActivity(), R.string.message_server_connect_is_failed,
                               Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Dijalankan jika user menambahkan komentar foto
     *
     * @param userId
     * @param photoId
     */
    private void comment(String photoId, String userId) {
        Log.d(TAG, "comment: " + userId + " on photoId " + photoId);
        InsertComment.newInstance(photoId, userId).show(getActivity().getFragmentManager(), "INSERT_COMMENT");
    }

    /**
     * Dijalankan jika user mengklik item foto yang ada di postfeed.
     * Method ini akan memindahkan user ke PhotoActivity.class
     *
     * @param photo
     */
    public void onPhotoItemClicked(Photo photo) {
        getActivity().startActivity(new Intent(getActivity(), PhotoActivity.class));

        Log.d(TAG, "onPhotoItemClicked: " + photo.getPhotoID() + " [ " + photo.getPhotoName() + " ]");
    }
}
