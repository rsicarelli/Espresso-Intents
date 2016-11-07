package br.com.rsicarelli.espressointents.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;

import java.util.ArrayList;

import br.com.rsicarelli.espressointents.presentation.GalleryActivity;
import br.com.rsicarelli.frescogallery.GalleryPhoto;

public class Navigator {
    private final Activity activity;

    public Navigator(Activity activity) {
        this.activity = activity;
    }

    public void navigateToAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(String.format("package:%s", activity.getPackageName())));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public void navigateToCamera(Uri photoLocalUri, int requestImageCapture) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoLocalUri);
        activity.startActivityForResult(intent, requestImageCapture);
    }

    public void navigateToGallery(int requestCode, ArrayList<GalleryPhoto> imagesOnDevice) {
        Intent callingIntent = GalleryActivity.getCallingIntent(activity, imagesOnDevice);
        activity.startActivityForResult(callingIntent, requestCode);
    }

    public void shareImage(String text, Uri imageUri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        activity.startActivity(Intent.createChooser(shareIntent, text));
    }
}
