// Copyright (C) 2016 The Qt Company Ltd.
// SPDX-License-Identifier: LicenseRef-Qt-Commercial OR GPL-3.0-only WITH Qt-GPL-exception-1.0

#include <QtCore/qglobal.h>

// SCENARIO 3
// this is the "no harm done" version. Only operator% is active,
// with NO_CAST * _not_ defined
#undef QT_USE_QSTRINGBUILDER
#undef QT_NO_CAST_FROM_ASCII
#undef QT_NO_CAST_TO_ASCII

#include <QtCore/QObject>
#include <QtCore/QString>
#include <QtCore/QStringBuilder>
#include <QtTest/QTest>

#define LITERAL "some literal"

void runScenario(); // Defined in stringbuilder.cpp #included below.

class tst_QStringBuilder3 : public QObject
{
    Q_OBJECT

private slots:
    void scenario() { runScenario(); }
};

#define P %
#include "../qstringbuilder1/stringbuilder.cpp"
#undef P

#include "tst_qstringbuilder3.moc"

QTEST_APPLESS_MAIN(tst_QStringBuilder3)
