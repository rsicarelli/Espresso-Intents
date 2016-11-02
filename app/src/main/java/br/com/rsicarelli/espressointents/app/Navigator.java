package br.com.rsicarelli.espressointents.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;

import br.com.rsicarelli.espressointents.presentation.GalleryActivity;

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

    public void navigateToGallery(int requestCode) {
        Intent intent = new Intent(activity, GalleryActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }
}
