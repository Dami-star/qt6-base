// Copyright (C) 2021 The Qt Company Ltd.
// SPDX-License-Identifier: LicenseRef-Qt-Commercial OR LGPL-3.0-only OR GPL-2.0-only OR GPL-3.0-only

#include "module_api.h"

int main(int, char*[])
{
    TestApi api;
    api.dummy();
    return 0;
}
