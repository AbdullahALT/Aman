package com.amanapp.application.core.logics;

import android.app.Application;
import android.content.Context;

/**
 * Created by Abdullah ALT on 8/26/2016.
 */
public class AmanApplication extends Application {
    private static AmanApplication instance;

    public AmanApplication() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }
}
