package com.wisnu.photohunting.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ozcan on 06/08/2017.
 */

public class UserLike {
    @SerializedName("id_user")
    private String userId;
    @SerializedName("id_photo")
    private String photoId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    @Override
    public String toString() {
        return "UserLike{" +
                "userId='" + userId + '\'' +
                ", photoId='" + photoId + '\'' +
                '}';
    }
}
