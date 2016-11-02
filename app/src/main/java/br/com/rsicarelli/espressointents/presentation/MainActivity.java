package br.com.rsicarelli.espressointents.presentation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.view.SimpleDraweeView;

import br.com.rsicarelli.espressointents.R;
import br.com.rsicarelli.espressointents.app.Navigator;
import br.com.rsicarelli.espressointents.helper.ChoosePhotoHelper;
import br.com.rsicarelli.espressointents.model.Permission;
import br.com.rsicarelli.espressointents.model.PhotoPermissionResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements
        ChoosePhotoHelper.OnPermissionHelperListener {

    @BindView(R.id.image_result)
    SimpleDraweeView imageResult;

    private ChoosePhotoHelper choosePhotoHelper;
    private Navigator navigator;
    private Uri currentImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navigator = new Navigator(this);
        choosePhotoHelper = ChoosePhotoHelper.attach(this);
    }

    @OnClick(R.id.camera)
    public void onCameraClick() {
        choosePhotoHelper.requestCameraWithCheck();
    }

    @OnClick(R.id.gallery)
    public void onGalleryClick() {
        choosePhotoHelper.chooseImageFromGalleryWithCheck();
    }

    @Override
    public Permission getPermission() {
        return new Permission.Builder()
                .withTitle(getText(R.string.title_rationale))
                .withRationaleMessage(getText(R.string.message_rationale))
                .withNeverAskAgainMessage(getText(R.string.message_never_ask_again))
                .build();
    }

    @Override
    public void navigateToGallery(PhotoPermissionResult photoPermissionResult) {
        navigator.navigateToGallery(photoPermissionResult.getRequestCode());
    }

    @Override
    public void navigateToCamera(PhotoPermissionResult photoPermissionResult) {
        navigator.navigateToCamera(photoPermissionResult.getPhotoUri(),
                photoPermissionResult.getRequestCode());
    }

    @Override
    public void onImageCaptureResultReceived(PhotoPermissionResult photoPermissionResult) {
        setImageResult(photoPermissionResult.getPhotoUri());
    }

    @Override
    public void onImageGalleryResultReceived(PhotoPermissionResult photoPermissionResult) {
        setImageResult(photoPermissionResult.getPhotoUri());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @OnClick(R.id.share)
    public void onShare() {
        if (currentImageUri != null) {
            navigator.shareImage(getString(R.string.image_share), currentImageUri);
        }
    }

    private void setImageResult(Uri imageUri) {
        currentImageUri = imageUri;
        imageResult.setImageURI(imageUri);
    }
}
