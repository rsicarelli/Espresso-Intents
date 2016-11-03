package br.com.rsicarelli.espressointents.presentation;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;

import br.com.rsicarelli.espressointents.R;
import br.com.rsicarelli.espressointents.util.FileUtil;
import br.com.rsicarelli.frescogallery.GalleryPhoto;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    @Rule
    public IntentsTestRule<HomeActivity> activityRule = new IntentsTestRule<>(HomeActivity.class);

    @Test
    public void shouldSelectImageOnCamera() {
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, getFakeIntentForCamera());

        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);

        onView(withId(R.id.camera)).perform(click());

        assertNotNull(activityRule.getActivity().currentImageUri);
    }

    @Test
    public void shouldSelectImageFromGallery() {
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, getFakeIntentForCamera());

        ArrayList<GalleryPhoto> galleryPhotos = new ArrayList<>();
        galleryPhotos.add(getFakeGalleryPhoto());

        ClassTest galleryActivity = spy(activityRule.getActivity().test);
        doReturn(galleryPhotos).when(galleryActivity).getGalleryPhotos(activityRule.getActivity());

        onView(withId(R.id.gallery)).perform(click());

        onView(withId(R.id.photos)).perform(actionOnItemAtPosition(0, click()));

        waitLittle();
    }

    public static void waitLittle() {
        try {
            Thread.sleep(1500L);
        } catch (InterruptedException var1) {
            var1.printStackTrace();
        }

    }

    private Intent getFakeIntentForCamera() {
        GalleryPhoto galleryPhoto = getFakeGalleryPhoto();
        Intent intent = new Intent();
        intent.putExtra(GalleryActivity.EXTRA_GALLERY_PHOTO, galleryPhoto);
        return intent;
    }
//
//    private Intent getFakeIntentForGallery() {
//
//        return GalleryActivity.getCallingIntent(InstrumentationRegistry.getTargetContext(), galleryPhotos);
//    }

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

}