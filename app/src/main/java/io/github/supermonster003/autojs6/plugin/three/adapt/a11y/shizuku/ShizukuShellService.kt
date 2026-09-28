package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.shizuku

import android.content.Context
import android.os.Bundle
import android.os.Process
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control.ShellCommands
import kotlin.system.exitProcess

/**
 * Runs in the process spawned by the Shizuku server with the shell (or root) identity. It only
 * executes the `settings` commands the controller sends; it never touches accessibility content.
 * Both constructors are kept by proguard-rules.pro because the Shizuku server instantiates the
 * class reflectively.
 */
class ShizukuShellService : IShizukuShell.Stub {

    /** Required by the Shizuku server. */
    constructor()

    /** Available from Shizuku API v13; [context] is unused but the signature must exist. */
    @Suppress("unused")
    constructor(context: Context) : this()

    override fun destroy() {
        runCatching { Process.killProcess(Process.myPid()) }
        runCatching { exitProcess(0) }
    }

    override fun exit() = destroy()

    override fun execCommand(command: String): Bundle {
        val result = ShellCommands.run(arrayOf("sh", "-c", command))
        return Bundle().apply {
            putInt(KEY_CODE, result.code)
            putString(KEY_OUTPUT, result.output)
            putString(KEY_ERROR, result.error)
        }
    }

    companion object {
        const val KEY_CODE = "code"
        const val KEY_OUTPUT = "output"
        const val KEY_ERROR = "error"
    }
}
