package br.com.rsicarelli.espressointents.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

import br.com.rsicarelli.espressointents.model.Permission;
import br.com.rsicarelli.espressointents.model.PhotoPermissionResult;
import br.com.rsicarelli.espressointents.presentation.component.NeverAskPermissionDialogFragment;
import br.com.rsicarelli.espressointents.presentation.component.RationaleDialogFragment;
import br.com.rsicarelli.espressointents.util.ChoosePhotoUtils;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_OK;

@RuntimePermissions
public class ChoosePhotoHelper extends Fragment implements
        NeverAskPermissionDialogFragment.OnNeverAskPermissionDialogListener {
    public static final int REQUEST_IMAGE_CAPTURE = 1984;
    public static final int REQUEST_IMAGE_GALLERY = 1948;
    private static final String FRAG_TAG = ChoosePhotoHelper.class.getCanonicalName();
    private static final String SAVED_INSTANCE_PHOTO_LOCAL_URI = "state_local_uri";

    private Permission permission;
    private Uri photoLocalUri;

    public static <ParentActivity extends AppCompatActivity & OnPermissionHelperListener>
    ChoosePhotoHelper attach(ParentActivity parent) {
        return attach(parent.getSupportFragmentManager());
    }

    public static <ParentActivity extends Fragment & OnPermissionHelperListener>
    ChoosePhotoHelper attach(ParentActivity parent) {
        return attach(parent.getChildFragmentManager());
    }

    private static ChoosePhotoHelper attach(FragmentManager fragmentManager) {
        ChoosePhotoHelper frag = (ChoosePhotoHelper) fragmentManager.findFragmentByTag(FRAG_TAG);
        if (frag == null) {
            frag = new ChoosePhotoHelper();
            fragmentManager.beginTransaction().add(frag, FRAG_TAG).commit();
        }
        return frag;
    }

    private OnPermissionHelperListener getParent() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof OnPermissionHelperListener) {
            return (OnPermissionHelperListener) parentFragment;
        } else {
            Activity activity = getActivity();
            if (activity instanceof OnPermissionHelperListener) {
                return (OnPermissionHelperListener) activity;
            }
        }
        return null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getParent() != null) {
            permission = getParent().getPermission();
            restoreInstances(savedInstanceState);
        }
    }

    private void restoreInstances(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_PHOTO_LOCAL_URI)) {
            photoLocalUri = savedInstanceState.getParcelable(SAVED_INSTANCE_PHOTO_LOCAL_URI);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_INSTANCE_PHOTO_LOCAL_URI, photoLocalUri);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ChoosePhotoHelperPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void chooseImageFromGallery() {
        if (getParent() != null) {
            PhotoPermissionResult photoPermissionResult = new PhotoPermissionResult.Builder()
                    .withRequestCode(REQUEST_IMAGE_GALLERY)
                    .build();
            getParent().navigateToGallery(photoPermissionResult);
        }
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void requestCamera() {
        File photoFile = ChoosePhotoUtils.createImageFile();
        if (photoFile != null && getParent() != null) {
            photoLocalUri = Uri.fromFile(photoFile);
            PhotoPermissionResult photoPermissionResult = new PhotoPermissionResult.Builder()
                    .withRequestCode(REQUEST_IMAGE_CAPTURE)
                    .withPhotoUri(photoLocalUri)
                    .build();
            getParent().navigateToCamera(photoPermissionResult);
        }
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleDialog(final PermissionRequest request) {
        RationaleDialogFragment dialogFragment = new RationaleDialogFragment.Builder()
                .setTitle(permission.getTitle())
                .setMessage(permission.getRationaleMessage())
                .setDialogListener(RationaleDialogListener.getOnRationaleDialogListener(request))
                .build();
        dialogFragment.show(
                getChildFragmentManager(),
                RationaleDialogFragment.class.getCanonicalName()
        );
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showNeverAskAgainDialog() {
        NeverAskPermissionDialogFragment dialog =
                new NeverAskPermissionDialogFragment.Builder()
                        .setTitle(permission.getTitle())
                        .setMessage(permission.getNeverAskAgainMessage())
                        .setDialogListener(this)
                        .build();
        dialog.show(
                getChildFragmentManager(),
                NeverAskPermissionDialogFragment.class.getCanonicalName()
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && getParent() != null) {
            PhotoPermissionResult photoPermissionResult = new PhotoPermissionResult.Builder()
                    .withData(data)
                    .withRequestCode(requestCode)
                    .withPhotoUri(photoLocalUri)
                    .build();
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    getParent().onImageCaptureResultReceived(photoPermissionResult);
                    break;
                case REQUEST_IMAGE_GALLERY:
                    getParent().onImageGalleryResultReceived(photoPermissionResult);
                    break;
            }
        }
    }

    //region NeverAskPermissionDialogFragment.OnNeverAskPermissionDialogListener
    @Override
    public void onGoToSettingsClick() {
        ChoosePhotoUtils.navigateToAppSettings(getContext());
    }
    //endregion

    public void requestCameraWithCheck() {
        ChoosePhotoHelperPermissionsDispatcher.requestCameraWithCheck(this);
    }

    public void chooseImageFromGalleryWithCheck() {
        ChoosePhotoHelperPermissionsDispatcher.chooseImageFromGalleryWithCheck(this);
    }

    public interface OnPermissionHelperListener {
        Permission getPermission();

        void navigateToGallery(PhotoPermissionResult photoPermissionResult);

        void navigateToCamera(PhotoPermissionResult photoPermissionResult);

        void onImageCaptureResultReceived(PhotoPermissionResult photoPermissionResult);

        void onImageGalleryResultReceived(PhotoPermissionResult photoPermissionResult);
    }
}
