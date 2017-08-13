package com.wisnu.photohunting.savingstate;

import com.wisnu.photohunting.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoFeedList {
    // singleton object
    private static PhotoFeedList instance;

    private List<Photo> mPhotoList = new ArrayList<>();

    private PhotoFeedList() {
    }

    public static PhotoFeedList getInstance() {
        if (instance == null) instance = new PhotoFeedList();
        return instance;
    }

    public List<Photo> getPhotoList() {
        return mPhotoList;
    }

    public void setPhotoList(List<Photo> photoListParam) {
        mPhotoList = photoListParam;
    }
}
