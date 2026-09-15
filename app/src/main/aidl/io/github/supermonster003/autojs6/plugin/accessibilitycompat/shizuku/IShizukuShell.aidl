package io.github.supermonster003.autojs6.plugin.accessibilitycompat.shizuku;

import android.os.Bundle;

interface IShizukuShell {

    void destroy() = 16777114; // Destroy method defined by the Shizuku server

    void exit() = 1;

    Bundle execCommand(String command) = 2;

}
