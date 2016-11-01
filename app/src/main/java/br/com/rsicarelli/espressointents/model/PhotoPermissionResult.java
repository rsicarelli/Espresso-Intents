package br.com.rsicarelli.espressointents.model;

import android.content.Intent;
import android.net.Uri;

public class PhotoPermissionResult {
    private final int requestCode;
    private final Intent data;
    private final Uri photoUri;

    public PhotoPermissionResult(int requestCode, Intent data, Uri photoUri) {
        this.requestCode = requestCode;
        this.data = data;
        this.photoUri = photoUri;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public Intent getData() {
        return data;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public static class Builder {
        private int requestCode;
        private Intent data;
        private Uri photoUri;

        public Builder withRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public Builder withData(Intent data) {
            this.data = data;
            return this;
        }

        public Builder withPhotoUri(Uri photoUri) {
            this.photoUri = photoUri;
            return this;
        }

        public PhotoPermissionResult build() {
            return new PhotoPermissionResult(requestCode, data, photoUri);
        }
    }
}
