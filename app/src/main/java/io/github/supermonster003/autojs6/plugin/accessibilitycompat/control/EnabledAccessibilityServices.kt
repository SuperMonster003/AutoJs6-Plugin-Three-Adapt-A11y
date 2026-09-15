package io.github.supermonster003.autojs6.plugin.accessibilitycompat.control

/**
 * Operations on the value of the `enabled_accessibility_services` secure setting: a colon-separated
 * list of `package/class` component names. Blank entries and the literal "null" (what `settings get`
 * prints for an unset value) are ignored so that the helpers accept raw settings and shell output alike.
 */
internal object EnabledAccessibilityServices {
    const val DELIMITER = ":"

    fun parse(raw: String?): List<String> = raw.orEmpty()
        .replace("\n", "")
        .split(DELIMITER)
        .map { it.trim() }
        .filter { it.isNotEmpty() && it != "null" }

    fun contains(raw: String?, serviceId: String): Boolean = parse(raw).any { it == serviceId }

    fun containsPackage(raw: String?, packageName: String): Boolean =
        parse(raw).any { packageOf(it) == packageName }

    /** [raw] without every service of [serviceId]'s package. */
    fun detach(raw: String?, serviceId: String): String {
        val packageName = packageOf(serviceId)
        return parse(raw).filterNot { packageOf(it) == packageName }.joinToString(DELIMITER)
    }

    /** [detach] followed by [serviceId] appended at the end. */
    fun attach(raw: String?, serviceId: String): String =
        (parse(detach(raw, serviceId)) + serviceId).joinToString(DELIMITER)

    fun packageOf(componentName: String): String = componentName.substringBefore('/')
}
