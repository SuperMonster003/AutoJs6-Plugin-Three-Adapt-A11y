package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.shizuku

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.DeadObjectException
import android.os.IBinder
import android.util.Log
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control.ShellResult
import rikka.shizuku.Shizuku
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

internal enum class ShizukuState {
    NOT_INSTALLED,
    NOT_RUNNING,
    PERMISSION_DENIED,
    AVAILABLE,
}

/**
 * Thin wrapper around the Shizuku client API: binder lifecycle, the permission request, and a
 * user service that runs `settings` commands with the shell identity.
 */
internal object ShizukuShell {
    private const val TAG = "ShizukuShell"
    private const val MANAGER_PACKAGE = "moe.shizuku.privileged.api"
    private const val PERMISSION_REQUEST_CODE = 0x5A11
    private const val BIND_TIMEOUT_MILLIS = 5_000L

    @Volatile
    private var listenersRegistered = false

    @Volatile
    private var binderReceived = false

    @Volatile
    private var service: IShizukuShell? = null

    private val serviceWaiters = CopyOnWriteArrayList<CountDownLatch>()
    private val permissionListeners = CopyOnWriteArrayList<(Boolean) -> Unit>()

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder?) {
            service = binder?.takeIf { it.pingBinder() }?.let(IShizukuShell.Stub::asInterface)
            serviceWaiters.forEach(CountDownLatch::countDown)
            serviceWaiters.clear()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            service = null
        }
    }

    private val binderReceivedListener = Shizuku.OnBinderReceivedListener { binderReceived = !Shizuku.isPreV11() }
    private val binderDeadListener = Shizuku.OnBinderDeadListener {
        binderReceived = false
        service = null
    }
    private val permissionResultListener = Shizuku.OnRequestPermissionResultListener { requestCode, grantResult ->
        if (requestCode != PERMISSION_REQUEST_CODE) return@OnRequestPermissionResultListener
        val granted = grantResult == PackageManager.PERMISSION_GRANTED
        permissionListeners.forEach { it(granted) }
    }

    @Synchronized
    fun initialize() {
        if (listenersRegistered) return
        listenersRegistered = true
        runCatching {
            Shizuku.addBinderReceivedListenerSticky(binderReceivedListener)
            Shizuku.addBinderDeadListener(binderDeadListener)
            Shizuku.addRequestPermissionResultListener(permissionResultListener)
        }.onFailure { Log.w(TAG, "Unable to register Shizuku listeners", it) }
    }

    fun isInstalled(context: Context): Boolean = runCatching {
        context.packageManager.getPackageInfo(MANAGER_PACKAGE, 0)
        true
    }.getOrDefault(false)

    fun isRunning(): Boolean {
        initialize()
        return binderReceived || runCatching { Shizuku.pingBinder() }.getOrDefault(false)
    }

    fun hasPermission(): Boolean = runCatching {
        isRunning() && Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
    }.getOrDefault(false)

    fun state(context: Context): ShizukuState = when {
        !isInstalled(context) -> ShizukuState.NOT_INSTALLED
        !isRunning() -> ShizukuState.NOT_RUNNING
        !hasPermission() -> ShizukuState.PERMISSION_DENIED
        else -> ShizukuState.AVAILABLE
    }

    /** Asks Shizuku for the permission; [onResult] runs on the Shizuku callback thread. */
    fun requestPermission(onResult: (Boolean) -> Unit): Boolean {
        initialize()
        if (!isRunning() || runCatching { Shizuku.isPreV11() }.getOrDefault(true)) return false
        permissionListeners.add(onResult)
        return runCatching { Shizuku.requestPermission(PERMISSION_REQUEST_CODE) }
            .onFailure { permissionListeners.remove(onResult) }
            .isSuccess
    }

    fun managerLaunchIntent(context: Context): Intent? =
        context.packageManager.getLaunchIntentForPackage(MANAGER_PACKAGE)?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    fun execCommand(context: Context, command: String): ShellResult = execCommand(context, command, allowRetry = true)

    private fun execCommand(context: Context, command: String, allowRetry: Boolean): ShellResult {
        val remote = obtainService(context)
            ?: return ShellResult(-1, "", "Shizuku user service is unavailable (state: ${state(context)})")
        return try {
            val bundle = remote.execCommand(command)
            ShellResult(
                code = bundle.getInt(ShizukuShellService.KEY_CODE, -1),
                output = bundle.getString(ShizukuShellService.KEY_OUTPUT).orEmpty(),
                error = bundle.getString(ShizukuShellService.KEY_ERROR).orEmpty(),
            )
        } catch (e: DeadObjectException) {
            service = null
            if (allowRetry) execCommand(context, command, allowRetry = false) else ShellResult(-1, "", e.toString())
        } catch (e: Exception) {
            ShellResult(-1, "", e.toString())
        }
    }

    private fun obtainService(context: Context): IShizukuShell? {
        service?.takeIf { it.asBinder().pingBinder() }?.let { return it }
        if (!hasPermission()) return null
        val latch = CountDownLatch(1)
        serviceWaiters.add(latch)
        try {
            Shizuku.bindUserService(userServiceArgs(context), connection)
            latch.await(BIND_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        } catch (e: Exception) {
            Log.w(TAG, "Unable to bind the Shizuku user service", e)
        } finally {
            serviceWaiters.remove(latch)
        }
        return service
    }

    private fun userServiceArgs(context: Context) =
        Shizuku.UserServiceArgs(ComponentName(context.packageName, ShizukuShellService::class.java.name))
            .processNameSuffix("shizuku-shell")
            .daemon(false)
            .version(USER_SERVICE_VERSION)

    private const val USER_SERVICE_VERSION = 1
}
