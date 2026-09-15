package io.github.supermonster003.autojs6.plugin.accessibilitycompat.control

import android.content.ContentResolver
import android.content.Context
import android.provider.Settings.Secure
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.shizuku.ShizukuShell

/** Raw access to the `enabled_accessibility_services` setting; implementations may throw. */
internal interface EnabledServicesStore {
    fun read(): String?

    fun write(value: String)
}

/** Reads and writes the setting directly; writing requires `WRITE_SECURE_SETTINGS`. */
internal class SecureSettingsStore(private val resolver: ContentResolver) : EnabledServicesStore {
    override fun read(): String? = Secure.getString(resolver, Secure.ENABLED_ACCESSIBILITY_SERVICES)

    override fun write(value: String) {
        Secure.putString(resolver, Secure.ENABLED_ACCESSIBILITY_SERVICES, value)
    }
}

/** Reads and writes the setting through `settings get / put secure` in a root shell. */
internal class RootShellStore(private val log: (String) -> Unit = {}) : EnabledServicesStore {
    override fun read(): String? {
        val result = RootShell.exec("settings get secure ${Secure.ENABLED_ACCESSIBILITY_SERVICES}")
        check(result.succeeded) { "settings get secure failed (${result.code}): ${result.error.trim()}" }
        return result.output.trim()
    }

    override fun write(value: String) {
        val result = RootShell.exec(
            "settings put secure ${Secure.ENABLED_ACCESSIBILITY_SERVICES} ${ShellCommands.quote(value)}",
        )
        if (result.error.isNotBlank()) log("settings put secure (root): ${result.error.trim()}")
    }
}

/** Same commands executed by the Shizuku user service with the shell or root identity. */
internal class ShizukuShellStore(private val context: Context, private val log: (String) -> Unit = {}) : EnabledServicesStore {
    override fun read(): String? {
        val result = ShizukuShell.execCommand(context, "settings get secure ${Secure.ENABLED_ACCESSIBILITY_SERVICES}")
        check(result.succeeded) { "settings get secure failed (${result.code}): ${result.error.trim()}" }
        return result.output.trim()
    }

    override fun write(value: String) {
        val result = ShizukuShell.execCommand(
            context,
            "settings put secure ${Secure.ENABLED_ACCESSIBILITY_SERVICES} ${ShellCommands.quote(value)}",
        )
        if (result.error.isNotBlank()) log("settings put secure (shizuku): ${result.error.trim()}")
    }
}
