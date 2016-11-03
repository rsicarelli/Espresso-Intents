package br.com.rsicarelli.espressointents.presentation;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import br.com.rsicarelli.frescogallery.GalleryPhoto;
import br.com.rsicarelli.frescogallery.GalleryUtils;

public class ClassTest {

    @NonNull
    public ArrayList<GalleryPhoto> getGalleryPhotos(Context context) {
        return GalleryUtils.getImagesOnDevice(context);
    }
}
