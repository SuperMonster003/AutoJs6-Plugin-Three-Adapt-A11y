package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control

import java.io.File
import java.util.concurrent.TimeUnit

/** Outcome of one shell command; [code] is the process exit value or -1 when it could not run. */
internal data class ShellResult(val code: Int, val output: String, val error: String) {
    val succeeded: Boolean get() = code == 0
}

internal object ShellCommands {
    /** Wraps [value] in single quotes so that even an empty string reaches the command as an argument. */
    fun quote(value: String): String = "'${value.replace("'", "'\\''")}'"

    fun run(command: Array<String>, timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS): ShellResult {
        val process = try {
            ProcessBuilder(*command).start()
        } catch (e: Exception) {
            return ShellResult(-1, "", e.message ?: e.javaClass.simpleName)
        }
        val output = StringBuilder()
        val error = StringBuilder()
        val readers = listOf(
            Thread { process.inputStream.bufferedReader().use { output.append(it.readText()) } },
            Thread { process.errorStream.bufferedReader().use { error.append(it.readText()) } },
        )
        readers.forEach(Thread::start)
        process.outputStream.close()
        if (!awaitExit(process, timeoutMillis)) {
            process.destroy()
            readers.forEach { it.join(1_000L) }
            return ShellResult(-1, output.toString(), "Timed out after $timeoutMillis ms")
        }
        readers.forEach(Thread::join)
        return ShellResult(process.exitValue(), output.toString(), error.toString())
    }

    // Process.waitFor(timeout) only exists from API 26; polling keeps the behaviour identical on API 24 and 25.
    private fun awaitExit(process: Process, timeoutMillis: Long): Boolean {
        val deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMillis)
        while (true) {
            try {
                process.exitValue()
                return true
            } catch (_: IllegalThreadStateException) {
                if (System.nanoTime() >= deadline) return false
                Thread.sleep(POLL_INTERVAL_MILLIS)
            }
        }
    }

    private const val DEFAULT_TIMEOUT_MILLIS = 15_000L
    private const val POLL_INTERVAL_MILLIS = 40L
}

/** Root shell access through the `su` binary of the device's root solution. */
internal object RootShell {
    private val COMMON_SU_PATHS = listOf(
        "/system/xbin/su",
        "/system/bin/su",
        "/vendor/bin/su",
        "/system_ext/bin/su",
        "/odm/bin/su",
        "/sbin/su",
        "/su/bin/su",
        "/system/su",
    )

    /** Cheap probe that never prompts: whether an executable `su` binary can be found. */
    fun isSuPresent(): Boolean {
        val candidates = COMMON_SU_PATHS + System.getenv("PATH").orEmpty()
            .split(':')
            .filter { it.isNotEmpty() }
            .distinct()
            .map { directory -> directory.trimEnd('/') + "/su" }
        return candidates.any { path -> runCatching { File(path).let { it.isFile && it.canExecute() } }.getOrDefault(false) }
    }

    fun exec(command: String): ShellResult = ShellCommands.run(arrayOf("su", "-c", command))
}
