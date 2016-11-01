package br.com.rsicarelli.espressointents.app;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class EspressoIntentsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
