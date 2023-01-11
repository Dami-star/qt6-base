// Copyright (C) 2013 Klaralvdalens Datakonsult AB (KDAB)
// SPDX-License-Identifier: LicenseRef-Qt-Commercial OR LGPL-3.0-only OR GPL-2.0-only OR GPL-3.0-only
/***************************************************************************
** This file was generated by glgen version 0.1
** Command line was: glgen
**
** glgen is Copyright (C) 2013 Klaralvdalens Datakonsult AB (KDAB)
**
** This is an auto-generated file.
** Do not edit! All changes made to it will be lost.
**
****************************************************************************/

#include "qopenglfunctions_1_5.h"
#include "qopenglcontext.h"

QT_BEGIN_NAMESPACE

/*!
    \class QOpenGLFunctions_1_5
    \inmodule QtOpenGL
    \since 5.1
    \wrapper
    \brief The QOpenGLFunctions_1_5 class provides all functions for OpenGL 1.5 specification.

    This class is a wrapper for functions from OpenGL 1.5 specification.
    See reference pages on \l {http://www.opengl.org/sdk/docs/}{opengl.org}
    for function documentation.

    \sa QAbstractOpenGLFunctions
*/

QOpenGLFunctions_1_5::QOpenGLFunctions_1_5()
 : QAbstractOpenGLFunctions()
 , d_1_0_Core(nullptr)
 , d_1_1_Core(nullptr)
 , d_1_2_Core(nullptr)
 , d_1_3_Core(nullptr)
 , d_1_4_Core(nullptr)
 , d_1_5_Core(nullptr)
 , d_1_0_Deprecated(nullptr)
 , d_1_1_Deprecated(nullptr)
 , d_1_2_Deprecated(nullptr)
 , d_1_3_Deprecated(nullptr)
 , d_1_4_Deprecated(nullptr)
{
}

QOpenGLFunctions_1_5::~QOpenGLFunctions_1_5()
{
    if (d_1_0_Core) {
        d_1_0_Core->refs.deref();
        Q_ASSERT(d_1_0_Core->refs.loadRelaxed());
    }
    if (d_1_1_Core) {
        d_1_1_Core->refs.deref();
        Q_ASSERT(d_1_1_Core->refs.loadRelaxed());
    }
    if (d_1_2_Core) {
        d_1_2_Core->refs.deref();
        Q_ASSERT(d_1_2_Core->refs.loadRelaxed());
    }
    if (d_1_3_Core) {
        d_1_3_Core->refs.deref();
        Q_ASSERT(d_1_3_Core->refs.loadRelaxed());
    }
    if (d_1_4_Core) {
        d_1_4_Core->refs.deref();
        Q_ASSERT(d_1_4_Core->refs.loadRelaxed());
    }
    if (d_1_5_Core) {
        d_1_5_Core->refs.deref();
        Q_ASSERT(d_1_5_Core->refs.loadRelaxed());
    }
    if (d_1_0_Deprecated) {
        d_1_0_Deprecated->refs.deref();
        Q_ASSERT(d_1_0_Deprecated->refs.loadRelaxed());
    }
    if (d_1_1_Deprecated) {
        d_1_1_Deprecated->refs.deref();
        Q_ASSERT(d_1_1_Deprecated->refs.loadRelaxed());
    }
    if (d_1_2_Deprecated) {
        d_1_2_Deprecated->refs.deref();
        Q_ASSERT(d_1_2_Deprecated->refs.loadRelaxed());
    }
    if (d_1_3_Deprecated) {
        d_1_3_Deprecated->refs.deref();
        Q_ASSERT(d_1_3_Deprecated->refs.loadRelaxed());
    }
    if (d_1_4_Deprecated) {
        d_1_4_Deprecated->refs.deref();
        Q_ASSERT(d_1_4_Deprecated->refs.loadRelaxed());
    }
}

bool QOpenGLFunctions_1_5::initializeOpenGLFunctions()
{
    if ( isInitialized() )
        return true;

    QOpenGLContext* context = QOpenGLContext::currentContext();

    // If owned by a context object make sure it is current.
    // Also check that current context is capable of resolving all needed functions
    if (((owningContext() && owningContext() == context) || !owningContext())
        && QOpenGLFunctions_1_5::isContextCompatible(context))
    {
        // Associate with private implementation, creating if necessary
        // Function pointers in the backends are resolved at creation time
        QOpenGLVersionFunctionsBackend* d = nullptr;
        d = QAbstractOpenGLFunctionsPrivate::functionsBackend(context, QOpenGLFunctions_1_0_CoreBackend::versionStatus());
        d_1_0_Core = static_cast<QOpenGLFunctions_1_0_CoreBackend*>(d);
        d->refs.ref();

        d = QAbstractOpenGLFunctionsPrivate::functionsBackend(context, QOpenGLFunctions_1_1_CoreBackend::versionStatus());
        d_1_1_Core = static_cast<QOpenGLFunctions_1_1_CoreBackend*>(d);
        d->refs.ref();

        d = QAbstractOpenGLFunctionsPrivate::functionsBackend(context, QOpenGLFunctions_1_2_CoreBackend::versionStatus());
        d_1_2_Core = static_cast<QOpenGLFunctions_1_2_CoreBackend*>(d);
        d->refs.ref();

        d = QAbstractOpenGLFunctionsPrivate::functionsBackend(context, QOpenGLFunctions_1_3_CoreBackend::versionStatus());
        d_1_3_Core = static_cast<QOpenGLFunctions_1_3_CoreBackend*>(d);
        d->refs.ref();

        d = QAbstractOpenGLFunctionsPrivate::functionsBackend(context, QOpenGLFunctions_1_4_CoreBackend::versionStatus());
        d_1_4_Core = static_cast<QOpenGLFunctions_1_4_CoreBackend*>(d);
        d->refs.ref();

        d = QAbstractOpenGLFunctionsPrivate::functionsBackend(context, QOpenGLFunctions_1_5_CoreBackend::versionStatus());
        d_1_5_Core = static_cast<QOpenGLFunctions_1_5_CoreBackend*>(d);
        d->refs.ref();

        d = QAbstractOpenGLFunctionsPrivate::functionsBackend(context, QOpenGLFunctions_1_0_DeprecatedBackend::versionStatus());
        d_1_0_Deprecated = static_cast<QOpenGLFunctions_1_0_DeprecatedBackend*>(d);
        d->refs.ref();

        d = QAbstractOpenGLFunctionsPrivate::functionsBackend(context, QOpenGLFunctions_1_1_DeprecatedBackend::versionStatus());
        d_1_1_Deprecated = static_cast<QOpenGLFunctions_1_1_DeprecatedBackend*>(d);
        d->refs.ref();

        d = QAbstractOpenGLFunctionsPrivate::functionsBackend(context, QOpenGLFunctions_1_2_DeprecatedBackend::versionStatus());
        d_1_2_Deprecated = static_cast<QOpenGLFunctions_1_2_DeprecatedBackend*>(d);
        d->refs.ref();

        d = QAbstractOpenGLFunctionsPrivate::functionsBackend(context, QOpenGLFunctions_1_3_DeprecatedBackend::versionStatus());
        d_1_3_Deprecated = static_cast<QOpenGLFunctions_1_3_DeprecatedBackend*>(d);
        d->refs.ref();

        d = QAbstractOpenGLFunctionsPrivate::functionsBackend(context, QOpenGLFunctions_1_4_DeprecatedBackend::versionStatus());
        d_1_4_Deprecated = static_cast<QOpenGLFunctions_1_4_DeprecatedBackend*>(d);
        d->refs.ref();

        QAbstractOpenGLFunctions::initializeOpenGLFunctions();
    }
    return isInitialized();
}

bool QOpenGLFunctions_1_5::isContextCompatible(QOpenGLContext *context)
{
    Q_ASSERT(context);
    QSurfaceFormat f = context->format();
    const QPair<int, int> v = qMakePair(f.majorVersion(), f.minorVersion());
    if (v < qMakePair(1, 5))
        return false;

    if (f.profile() == QSurfaceFormat::CoreProfile)
        return false;

    return true;
}

QOpenGLVersionProfile QOpenGLFunctions_1_5::versionProfile()
{
    QOpenGLVersionProfile v;
    v.setVersion(1, 5);
    return v;
}

QT_END_NAMESPACE
