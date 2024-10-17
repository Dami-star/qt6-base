// Copyright (C) 2023 The Qt Company Ltd.
// Copyright (c) 2016, BogDan Vatra <bogdan@kde.org>
// SPDX-License-Identifier: LicenseRef-Qt-Commercial OR LGPL-3.0-only OR GPL-2.0-only OR GPL-3.0-only

package org.qtproject.qt.android;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import java.lang.IllegalArgumentException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

class QtActivityLoader extends QtLoader {
    private final Activity m_activity;

    private QtActivityLoader(Activity activity) throws IllegalArgumentException
    {
        super(new ContextWrapper(activity));
        m_activity = activity;

        extractContextMetaData(m_activity);
    }

    static QtActivityLoader getActivityLoader(Activity activity) throws IllegalArgumentException {
        if (m_instance == null)
            m_instance = new QtActivityLoader(activity);
        return (QtActivityLoader) m_instance;
    }

    private String getDecodedUtfString(String str)
    {
        byte[] decodedExtraEnvVars = Base64.decode(str, Base64.DEFAULT);
        return new String(decodedExtraEnvVars, StandardCharsets.UTF_8);
    }

    int getAppIconSize()
    {
        int size = m_activity.getResources().getDimensionPixelSize(android.R.dimen.app_icon_size);
        if (size < 36 || size > 512) { // check size sanity
            DisplayMetrics metrics = new DisplayMetrics();
            m_activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            size = metrics.densityDpi / 10 * 3;
            if (size < 36)
                size = 36;

            if (size > 512)
                size = 512;
        }

        return size;
    }

    private void setupStyleExtraction()
    {
        int displayDensity = m_activity.getResources().getDisplayMetrics().densityDpi;
        setEnvironmentVariable("QT_ANDROID_THEME_DISPLAY_DPI", String.valueOf(displayDensity));

        String extractOption = getMetaData("android.app.extract_android_style");
        if (extractOption.equals("full"))
            setEnvironmentVariable("QT_USE_ANDROID_NATIVE_STYLE", String.valueOf(1));

        String stylePath = ExtractStyle.setup(m_activity, extractOption, displayDensity);
        setEnvironmentVariable("ANDROID_STYLE_PATH", stylePath);
    }

    @Override
    protected void extractContextMetaData(Context context)
    {
        super.extractContextMetaData(context);

        setEnvironmentVariable("QT_USE_ANDROID_NATIVE_DIALOGS", String.valueOf(1));
        setEnvironmentVariable("QT_ANDROID_APP_ICON_SIZE", String.valueOf(getAppIconSize()));

        setupStyleExtraction();

        Intent intent = m_activity.getIntent();
        if (intent == null) {
            Log.w(QtTAG, "Null Intent from the current Activity.");
            return;
        }

        String intentArgs = intent.getStringExtra("applicationArguments");
        if (intentArgs != null)
            appendApplicationParameters(intentArgs);

        Bundle extras = intent.getExtras();
        if (extras == null) {
            Log.w(QtTAG, "Null extras from the Activity's intent.");
            return;
        }

        int flags = m_activity.getApplicationInfo().flags;
        boolean isDebuggable = (flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;

        if (isDebuggable) {
            if (extras.containsKey("extraenvvars")) {
                String extraEnvVars = extras.getString("extraenvvars");
                setEnvironmentVariables(getDecodedUtfString(extraEnvVars));
            }

            if (extras.containsKey("extraappparams")) {
                String extraAppParams = extras.getString("extraappparams");
                appendApplicationParameters(getDecodedUtfString(extraAppParams));
            }
        } else {
            Log.d(QtNative.QtTAG, "Not in debug mode! It is not allowed to use extra arguments " +
                    "in non-debug mode.");
        }
    }
}
