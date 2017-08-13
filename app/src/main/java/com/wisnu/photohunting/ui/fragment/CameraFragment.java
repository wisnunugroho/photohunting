package com.wisnu.photohunting.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.orhanobut.hawk.Hawk;
import com.wisnu.photohunting.PhotoHuntingApp;
import com.wisnu.photohunting.R;
import com.wisnu.photohunting.model.Category;
import com.wisnu.photohunting.network.Request;
import com.wisnu.photohunting.network.Response;
import com.wisnu.photohunting.system.ImageUtils;
import com.wisnu.photohunting.ui.activity.HomeActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.R.id.list;
import static android.app.Activity.RESULT_CANCELED;

public class CameraFragment extends Fragment implements
        com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = CameraFragment.class.getSimpleName();

    private final int REQUEST_TAKE_PHOTO = 100;
    private ImageView  imgPreviewPhoto;
    private ImageView  btnCamera;
    private ViewGroup  btnConfirmOk;
    private ViewGroup  btnConfirmRetake;
    private ScrollView additionalForm;
    private TextView   btnSubmit;
    private TextView   textLokasi;
    private Spinner    spnKategori;
    private EditText   edPhotoName;
    private EditText   edPhotoLocation;
    private EditText   edKeterangan;
    private Uri        photoURI;

    private GoogleApiClient          mGoogleClient;
    private LocationRequest          mLocationRequest;
    private FusedLocationProviderApi mFusedLocationClient;
    private Location                 mLocation;

    private List<Category> mCategoryList = new ArrayList<>();

    private ProgressDialog mLoading;

    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_camera_open, container, false);
        imgPreviewPhoto = (ImageView) view.findViewById(R.id.photo_image);
        btnCamera = (ImageView) view.findViewById(R.id.button_take_photo);
        btnConfirmOk = (ViewGroup) view.findViewById(R.id.button_confirm_ok);
        btnConfirmRetake = (ViewGroup) view.findViewById(R.id.button_confirm_retake);
        additionalForm = (ScrollView) view.findViewById(R.id.additional_form);
        spnKategori = (Spinner) view.findViewById(R.id.spn_kategori);
        textLokasi = (TextView) view.findViewById(R.id.text_lokasi);
        edPhotoName = (EditText) view.findViewById(R.id.text_photo_name);
        edPhotoLocation = (EditText) view.findViewById(R.id.text_photo_lokasi);
        edKeterangan = (EditText) view.findViewById(R.id.text_keterangan);
        btnSubmit = (TextView) view.findViewById(R.id.button_submit);

        mLoading = new ProgressDialog(getActivity());
        mLoading.setMessage("Mengunggah Foto");
        mLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mLoading.setCancelable(false);

        /**
         * Inisialisasi library lokasi
         */
        mFusedLocationClient = LocationServices.FusedLocationApi;
        mGoogleClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        btnConfirmOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnConfirmRetake.setVisibility(View.VISIBLE);
                imgPreviewPhoto.setVisibility(View.GONE);
                toggleAdditionalForm(View.VISIBLE);
            }
        });
        btnConfirmRetake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgPreviewPhoto.setVisibility(View.VISIBLE);
                toggleAdditionalForm(View.GONE);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String             uid       = Hawk.get(PhotoHuntingApp.USER_ID, "");
                String             cid       = mCategoryList.get(spnKategori.getSelectedItemPosition()).getCategoryID();
                String             photoName = edPhotoName.getText().toString();
                String             photoDesc = edKeterangan.getText().toString();
                String             location  = edPhotoLocation.getText().toString();
                String             photoUrl  = photoURI.getLastPathSegment();
                String             latitude  = java.lang.String.valueOf(mLocation.getLatitude());
                String             longitude = String.valueOf(mLocation.getLongitude());
                MultipartBody.Part photo     = ImageUtils.prepareImageForUpload(getActivity(), photoURI);

                Log.d(TAG, "UID\t: " + uid);
                Log.d(TAG, "CID\t: " + cid);
                Log.d(TAG, "Photo Name\t:" + photoName);
                Log.d(TAG, "PhotoDesc\t:" + photoDesc);
                Log.d(TAG, "Location\t:" + location);
                Log.d(TAG, "Photo URL\t:" + photoUrl);
                Log.d(TAG, "Latitude\t:" + latitude);
                Log.d(TAG, "Longitude\t:" + longitude);
                submitData(photoName, photoDesc, photoUrl, location, latitude, longitude, uid, cid, photo);
            }
        });
        return view;
    }


    private void initializeKategori() {
        final List<String> categoryArray = new ArrayList<>();
        Request.Categoy.get_all().enqueue(new Callback<Response.Category>() {
            @Override
            public void onResponse(Call<Response.Category> call, retrofit2.Response<Response.Category> response) {
                if (response.isSuccessful()) {
                    if (response.body().getListCategory() != null) {
                        mCategoryList.clear();
                        categoryArray.clear();
                        mCategoryList.addAll(response.body().getListCategory());
                        for (Category kategori : mCategoryList) {
                            categoryArray.add(kategori.getCategoryName());
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, categoryArray);
                        dataAdapter.setDropDownViewResource(R.layout.item_row_spinner_kategori);
                        spnKategori.setAdapter(dataAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<Response.Category> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG, "dispatchTakePictureIntent: ", ex);
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                System.out.println("|-->" + photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp     = new SimpleDateFormat("MMddHHmm").format(new Date());
        String imageFileName = "SPH_" + timeStamp + "_";
        //File   storageDir    = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    private void hapusFoto() {
        File file = new File(photoURI.getPath());
        if (file.exists()) {
            file.delete();
            photoURI = null;
        }
    }

    private void toggleAdditionalForm(int visibility) {
        getView().findViewById(R.id.additional_form).setVisibility(visibility);
        getView().findViewById(R.id.additional_photo_name).setVisibility(visibility);
        getView().findViewById(R.id.additional_photo_lokasi).setVisibility(visibility);
        getView().findViewById(R.id.additional_kategori).setVisibility(visibility);
        getView().findViewById(R.id.additional_komentar).setVisibility(visibility);
        getView().findViewById(R.id.additional_lokasi).setVisibility(visibility);
        getView().findViewById(R.id.additional_konfirm).setVisibility(visibility);
        getView().findViewById(R.id.button_submit).setVisibility(visibility);
    }

    protected void startLocationUpdates() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        mFusedLocationClient.requestLocationUpdates(mGoogleClient, mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mGoogleClient, this);
        System.out.println("Stop receiving location updates");
    }

    private void submitData(final String photoName, final String photoDescription, final String photoUrl,
                            final String location, final String latitude, final String longitude,
                            final String uid, final String cid, MultipartBody.Part photo) {

        mLoading.show();

        Request.Photo.insert_new_upload(photo).enqueue(new Callback<Response.Basic>() {
            @Override
            public void onResponse(Call<Response.Basic> call, retrofit2.Response<Response.Basic> response) {
                Request.Photo.insert_new(photoName, photoDescription, photoUrl,
                        location, latitude, longitude, uid, cid).enqueue(new Callback<Response.Basic>() {
                    @Override
                    public void onResponse(Call<Response.Basic> call, retrofit2.Response<Response.Basic> response) {
                        mLoading.dismiss();
                        Toast.makeText(getActivity(), "Foto berhasil diunggah", Toast.LENGTH_SHORT).show();
                        ((HomeActivity) getActivity()).viewPager.setCurrentItem(0);
                    }

                    @Override
                    public void onFailure(Call<Response.Basic> call, Throwable t) {
                        mLoading.dismiss();
                        Toast.makeText(getActivity(), "Gagal mengunggah foto", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<Response.Basic> call, Throwable t) {
                mLoading.dismiss();
                Toast.makeText(getActivity(), "Gagal mengunggah foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleClient.isConnected()) {
            stopLocationUpdates();
            mGoogleClient.disconnect();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCompatActivity.RESULT_OK) {
            initializeKategori();

            imgPreviewPhoto.setVisibility(View.VISIBLE);
            btnConfirmOk.setVisibility(View.VISIBLE);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap imageBitmap = BitmapFactory.decodeFile(photoURI.getPath(), options);
            imageBitmap = ThumbnailUtils.extractThumbnail(imageBitmap, 480, 480);

            if (imageBitmap == null)
                Toast.makeText(getActivity(), "Tidak dapat mendapatkan foto dari perangkat", Toast.LENGTH_SHORT).show();

            imgPreviewPhoto.setImageBitmap(imageBitmap);
            imgPreviewPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);

        } else if (resultCode == RESULT_CANCELED) {
            System.out.println("Result Cancel : " + RESULT_CANCELED);
            hapusFoto();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: Starting to find location");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), 9000);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.mLocation = location;
        float accuracy = location.getAccuracy();
        textLokasi.setText("X : " + location.getLatitude() + "\nY : " + location.getLongitude() + "\nAkurat sampai dengan " + accuracy + " meter");
        stopLocationUpdates();
    }
}
