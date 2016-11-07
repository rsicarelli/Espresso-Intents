package br.com.rsicarelli.espressointents.presentation;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;

import br.com.rsicarelli.espressointents.R;
import br.com.rsicarelli.espressointents.helper.PhotoManager;
import br.com.rsicarelli.espressointents.util.FileUtil;
import br.com.rsicarelli.frescogallery.GalleryPhoto;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.BundleMatchers.hasEntry;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasType;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    @Rule
    public IntentsTestRule<HomeActivity> activityRule = new IntentsTestRule<>(HomeActivity.class);

    @Before
    public void setup() {
        ArrayList<GalleryPhoto> fakePhotos = new ArrayList<>();
        fakePhotos.add(getFakeGalleryPhoto());

        PhotoManager mockManager = Mockito.mock(PhotoManager.class);
        when(mockManager.getGalleryPhotos(activityRule.getActivity())).thenReturn(fakePhotos);
        activityRule.getActivity().setPhotoManager(mockManager);
    }

    @Test
    public void shouldSelectImageOnCamera() {
        Instrumentation.ActivityResult result = getActivityResult(getFakeIntentForCamera());

        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);

        onView(withId(R.id.camera)).perform(click());

        allowPermissionsIfNeeded();

        assertNotNull(activityRule.getActivity().currentImageUri);
    }

    @Test
    public void shouldSelectImageFromGallery() {
        onView(withId(R.id.gallery)).perform(click());

        allowPermissionsIfNeeded();

        onView(withId(R.id.photos)).perform(actionOnItemAtPosition(0, click()));

        assertNotNull(activityRule.getActivity().currentImageUri);
    }

    @Test
    public void shouldShareImageFromGallery() {
        onView(withId(R.id.gallery)).perform(click());

        allowPermissionsIfNeeded();

        onView(withId(R.id.photos)).perform(actionOnItemAtPosition(0, click()));

        Instrumentation.ActivityResult result = getActivityResult(getFakeIntentForCamera());

        intending(hasAction(Intent.ACTION_CHOOSER)).respondWith(result);

        onView(withId(R.id.share)).perform(click());

        intended(allOf(
                hasExtras(allOf(
                        hasEntry(equalTo(Intent.EXTRA_INTENT), hasAction(Intent.ACTION_SEND)),
                        hasEntry(equalTo(Intent.EXTRA_INTENT), hasType("image/*")),
                        hasEntry(equalTo(Intent.EXTRA_TITLE), containsString(activityRule.getActivity().getString(R.string.image_share))))
                ),
                hasAction(equalTo(Intent.ACTION_CHOOSER))));
    }

    private Intent getFakeIntentForCamera() {
        GalleryPhoto galleryPhoto = getFakeGalleryPhoto();
        Intent intent = new Intent();
        intent.putExtra(GalleryActivity.EXTRA_GALLERY_PHOTO, galleryPhoto);
        return intent;
    }

    @NonNull
    private Instrumentation.ActivityResult getActivityResult(Intent data) {
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, data);
    }

    @NonNull
    private GalleryPhoto getFakeGalleryPhoto() {
        String path = null;
        try {
            path = FileUtil.getStubFile(activityRule.getActivity()).getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new GalleryPhoto(path, "0");
    }

    private void allowPermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT >= 23) {
            UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            UiObject allowPermissions = device.findObject(new UiSelector().text("ALLOW"));
            if (allowPermissions.exists()) {
                try {
                    allowPermissions.click();
                } catch (UiObjectNotFoundException e) {
                    Log.e("test", "There is no permissions dialog to interact with " + e);
                }
            }
        }
    }

}