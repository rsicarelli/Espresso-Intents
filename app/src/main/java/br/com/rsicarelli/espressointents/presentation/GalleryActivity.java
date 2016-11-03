package br.com.rsicarelli.espressointents.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.List;

import br.com.rsicarelli.espressointents.R;
import br.com.rsicarelli.frescogallery.GalleryAdapter;
import br.com.rsicarelli.frescogallery.GalleryPhoto;
import br.com.rsicarelli.frescogallery.MarginDecoration;
import br.com.rsicarelli.frescogallery.widget.GalleryRecyclerView;

public class GalleryActivity extends AppCompatActivity implements
        GalleryAdapter.OnGalleryClickListener {

    public static final String EXTRA_GALLERY_PHOTO = "extraGalleryPhoto";
    public static final String EXTRA_IMAGES_ON_DEVICE = "extraImagesOnDevice";

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        setUpPhotos();
    }

    public void setUpPhotos() {
        GalleryRecyclerView galleryRecyclerView = (GalleryRecyclerView) findViewById(R.id.photos);
        galleryRecyclerView.addItemDecoration(new MarginDecoration(this));
        galleryRecyclerView.setHasFixedSize(true);

        List<GalleryPhoto> imagesOnDevice = new ArrayList<>();

        if (getIntent() != null && getIntent().hasExtra(EXTRA_IMAGES_ON_DEVICE)) {
            imagesOnDevice = getIntent().getParcelableArrayListExtra(EXTRA_IMAGES_ON_DEVICE);
        }

        GalleryAdapter galleryAdapter = new GalleryAdapter(imagesOnDevice, this, this);
        galleryAdapter.setColumnSize(galleryRecyclerView.getColumnWidth());
        galleryRecyclerView.setAdapter(galleryAdapter);
    }

    @Override
    public void onItemSelected(GalleryPhoto galleryPhoto) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_GALLERY_PHOTO, galleryPhoto);
        setResult(RESULT_OK, intent);
        finish();
    }

    public static Intent getCallingIntent(Context context, ArrayList<GalleryPhoto> imagesOnDevice) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_IMAGES_ON_DEVICE, imagesOnDevice);
        return intent;
    }
}
