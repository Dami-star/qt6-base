// Copyright (C) 2024 The Qt Company Ltd.
// SPDX-License-Identifier: LicenseRef-Qt-Commercial OR LGPL-3.0-only OR GPL-2.0-only OR GPL-3.0-only

package org.qtproject.qt.android;

import static org.qtproject.qt.android.QtNative.ApplicationState.*;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.HashMap;

class QtEmbeddedDelegate extends QtActivityDelegateBase
        implements QtNative.AppStateDetailsListener, QtEmbeddedViewInterface, QtWindowInterface,
                   QtMenuInterface
{
    private static final String QtTAG = "QtEmbeddedDelegate";
    // TODO simplistic implementation with one QtView, expand to support multiple views QTBUG-117649
    private QtView m_view;
    private QtNative.ApplicationStateDetails m_stateDetails;
    private boolean m_windowLoaded = false;
    private boolean m_backendsRegistered = false;

    QtEmbeddedDelegate(Activity context) {
        super(context);

        m_stateDetails = QtNative.getStateDetails();
        QtNative.registerAppStateListener(this);

        m_activity.getApplication().registerActivityLifecycleCallbacks(
        new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

            @Override
            public void onActivityStarted(Activity activity) {}

            @Override
            public void onActivityResumed(Activity activity) {
                if (m_activity == activity && m_stateDetails.isStarted) {
                    QtNative.setApplicationState(ApplicationActive);
                    QtNative.updateWindow();
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if (m_activity == activity && m_stateDetails.isStarted) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N ||
                        !activity.isInMultiWindowMode()) {
                        QtNative.setApplicationState(ApplicationInactive);
                    }
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (m_activity == activity && m_stateDetails.isStarted) {
                    QtNative.setApplicationState(ApplicationSuspended);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (m_activity == activity && m_stateDetails.isStarted) {
                    m_activity.getApplication().unregisterActivityLifecycleCallbacks(this);
                    QtNative.unregisterAppStateListener(QtEmbeddedDelegate.this);
                    QtEmbeddedViewInterfaceFactory.remove(m_activity);
                    QtNative.terminateQt();
                    QtNative.setActivity(null);
                    QtNative.getQtThread().exit();
                }
            }
        });
    }

    @Override
    public void onAppStateDetailsChanged(QtNative.ApplicationStateDetails details) {
        synchronized (this) {
            m_stateDetails = details;
            if (details.isStarted && !m_backendsRegistered) {
                m_backendsRegistered = true;
                BackendRegister.registerBackend(QtWindowInterface.class, (QtWindowInterface)this);
                BackendRegister.registerBackend(QtMenuInterface.class, (QtMenuInterface)this);
                BackendRegister.registerBackend(QtInputInterface.class, m_inputDelegate);
            } else if (!details.isStarted && m_backendsRegistered) {
                m_backendsRegistered = false;
                BackendRegister.unregisterBackend(QtWindowInterface.class);
                BackendRegister.unregisterBackend(QtMenuInterface.class);
                BackendRegister.unregisterBackend(QtInputInterface.class);
            }
        }
    }

    @Override
    public void onNativePluginIntegrationReadyChanged(boolean ready)
    {
        synchronized (this) {
            if (ready) {
                QtNative.runAction(() -> {
                    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
                    QtDisplayManager.setApplicationDisplayMetrics(m_activity, metrics.widthPixels,
                                                                  metrics.heightPixels);

                });
                createRootWindow();
            }
        }
    }

    @Override
    void startNativeApplicationImpl(String appParams, String mainLib)
    {
        QtNative.startApplication(appParams, mainLib);
    }

    // QtEmbeddedViewInterface implementation begin
    @Override
    public void startQtApplication(String appParams, String mainLib)
    {
        super.startNativeApplication(appParams, mainLib);
    }

    @Override
    public void queueLoadWindow()
    {
        synchronized (this) {
            if (m_stateDetails.nativePluginIntegrationReady)
                createRootWindow();
        }
    }

    @Override
    public void setView(QtView view)
    {
        m_view = view;
    }
    // QtEmbeddedViewInterface implementation end

    private void createRootWindow() {
        if (m_view != null && !m_windowLoaded) {
            QtView.createRootWindow(m_view, m_view.getLeft(), m_view.getTop(), m_view.getWidth(),
                                    m_view.getHeight());
            m_windowLoaded = true;
        }
    }

    // QtMenuInterface implementation begin
    @Override
    public void resetOptionsMenu() { QtNative.runAction(() -> m_activity.invalidateOptionsMenu()); }

    @Override
    public void openOptionsMenu() { QtNative.runAction(() -> m_activity.openOptionsMenu()); }

    @Override
    public void closeContextMenu() { QtNative.runAction(() -> m_activity.closeContextMenu()); }

    @Override
    public void openContextMenu(final int x, final int y, final int w, final int h)
    {
        m_view.postDelayed(() -> {
            final QtEditText focusedEditText = m_inputDelegate.getCurrentQtEditText();
            if (focusedEditText == null) {
                Log.w(QtTAG, "No focused view when trying to open context menu");
                return;
            }
            PopupMenu popup = new PopupMenu(m_activity, focusedEditText);
            QtNative.fillContextMenu(popup.getMenu());
            popup.setOnMenuItemClickListener(menuItem ->
                    m_activity.onContextItemSelected(menuItem));
            popup.setOnDismissListener(popupMenu ->
                    m_activity.onContextMenuClosed(popupMenu.getMenu()));
            popup.show();
        }, 100);
    }
    // QtMenuInterface implementation end
}