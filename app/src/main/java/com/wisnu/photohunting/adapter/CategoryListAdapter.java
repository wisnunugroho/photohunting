package com.wisnu.photohunting.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wisnu.photohunting.R;
import com.wisnu.photohunting.model.Photo;
import com.wisnu.photohunting.network.BaseURL;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.Holder> {
    AppCompatActivity activity;
    List<Photo> listPhotoCategory = new ArrayList<>();

    public CategoryListAdapter(AppCompatActivity activity, List<Photo> listPhotoCategory) {
        this.activity = activity;
        this.listPhotoCategory = listPhotoCategory;
    }

    @Override
    public CategoryListAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_category_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final CategoryListAdapter.Holder holder, int position) {
        Photo  photoFeeds = listPhotoCategory.get(position);
        String postedBy   = "dipost oleh " + photoFeeds.getPhotoBy();

        holder.postName.setText(photoFeeds.getPhotoName());
        holder.postedBy.setText(postedBy);
        holder.photoDescription.setText(photoFeeds.getPhotoDescription());

        String photoUrl = BaseURL.MAIN_POINT + BaseURL.END_POINT_IMAGE + photoFeeds.getPhotoUrl();
        Picasso.with(holder.photoImage.getContext()).load(photoUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.photoNoImage.setVisibility(View.GONE);
                holder.photoImage.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                holder.photoNoImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPhotoCategory.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView  postName;
        TextView  postedBy;
        ImageView photoNoImage;
        ImageView photoImage;
        TextView  photoDate;
        TextView  photoDescription;
        ViewGroup photoLokasi;

        public Holder(View itemView) {
            super(itemView);
            postName = (TextView) itemView.findViewById(R.id.postfeed_tv_name);
            postedBy = (TextView) itemView.findViewById(R.id.postfeed_tv_postedby);
            photoNoImage = (ImageView) itemView.findViewById(R.id.no_photo);
            photoImage = (ImageView) itemView.findViewById(R.id.postfeed_img_photoImage);
            photoDate = (TextView) itemView.findViewById(R.id.postfeed_tv_date);
            photoDescription = (TextView) itemView.findViewById(R.id.postfeed_tv_description);
            photoLokasi = (ViewGroup) itemView.findViewById(R.id.pergi_ke_lokasi);

            photoLokasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String lat = listPhotoCategory.get(getAdapterPosition()).getPhotoLatitude();
                    String lng = listPhotoCategory.get(getAdapterPosition()).getPhotoLongitude();
                    Intent i   = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + lat + "," + lng));
                    i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    activity.startActivity(i);
                }
            });
        }
    }
}