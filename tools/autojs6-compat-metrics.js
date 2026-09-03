"auto";

/**
 * Privacy-safe AutoJs6 verifier for the Accessibility Compat companion.
 *
 * Run while a supported target page is visible, or pass a targetPackage launch extra.
 * The script reads nodes through AutoJs6's own
 * accessibility service, but persists only counts and a text-free structural hash.
 * It never writes node text, descriptions, IDs, bounds, or screenshots.
 */

var SAMPLE_COUNT = 7;
var SAMPLE_DELAY_MILLIS = 450;
var DEFAULT_OUTPUT = files.join(
    context.getFilesDir().getAbsolutePath(),
    "autojs6-accessibility-compat-metrics.json"
);

function intentStringExtra(name, fallback) {
    try {
        var launchArguments = engines.myEngine().execArgv;
        var launchIntent = launchArguments && launchArguments.intent;
        if (launchIntent) {
            return launchIntent.getStringExtra(name) || fallback;
        }
    } catch (ignored) {
        // Interactive runs do not expose an Android launch intent.
    }
    return fallback;
}

var TARGET_PACKAGE = intentStringExtra("targetPackage", currentPackage());
if (!TARGET_PACKAGE) {
    throw new Error("A foreground or explicit target package is required");
}

function sha256(value) {
    var digest = java.security.MessageDigest.getInstance("SHA-256");
    var bytes = new java.lang.String(value)
        .getBytes(java.nio.charset.StandardCharsets.UTF_8);
    var result = digest.digest(bytes);
    var hex = "";
    for (var i = 0; i < result.length; i += 1) {
        var item = (result[i] & 0xff).toString(16);
        hex += item.length === 1 ? "0" + item : item;
    }
    return hex.toUpperCase();
}

function valueOrEmpty(value) {
    return value === null || typeof value === "undefined" ? "" : String(value);
}

function sampleTree() {
    auto.clearCache();
    var nodes = packageName(TARGET_PACKAGE).find();
    var metrics = {
        nodeCount: nodes.size(),
        nonEmptyText: 0,
        nonEmptyDescription: 0,
        nonEmptyViewId: 0,
        clickable: 0,
        enabled: 0,
        visibleToUser: 0,
        structuralSha256: ""
    };
    var structure = [];

    for (var i = 0; i < nodes.size(); i += 1) {
        var node = nodes.get(i);
        if (node === null) continue;

        var hasText = valueOrEmpty(node.text()).length > 0;
        var hasDescription = valueOrEmpty(node.desc()).length > 0;
        var hasViewId = valueOrEmpty(node.id()).length > 0;
        var bounds = node.bounds();

        if (hasText) metrics.nonEmptyText += 1;
        if (hasDescription) metrics.nonEmptyDescription += 1;
        if (hasViewId) metrics.nonEmptyViewId += 1;
        if (node.clickable()) metrics.clickable += 1;
        if (node.enabled()) metrics.enabled += 1;
        if (node.visibleToUser()) metrics.visibleToUser += 1;

        structure.push([
            i,
            valueOrEmpty(node.className()),
            node.childCount(),
            bounds.left,
            bounds.top,
            bounds.right,
            bounds.bottom,
            hasText ? 1 : 0,
            hasDescription ? 1 : 0,
            hasViewId ? 1 : 0,
            node.clickable() ? 1 : 0,
            node.enabled() ? 1 : 0,
            node.visibleToUser() ? 1 : 0
        ].join("|"));
    }

    metrics.structuralSha256 = sha256(structure.join("\n"));
    return metrics;
}

var phase = intentStringExtra("phase", "manual")
    .replace(/[^A-Za-z0-9_-]/g, "_");
var output = intentStringExtra("output", DEFAULT_OUTPUT);
var foregroundDeadline = Date.now() + 5000;
while (currentPackage() !== TARGET_PACKAGE && Date.now() < foregroundDeadline) {
    sleep(100);
}
var report = {
    schema: 1,
    phase: phase,
    targetPackage: TARGET_PACKAGE,
    foregroundPackageMatches: currentPackage() === TARGET_PACKAGE,
    samples: []
};

for (var sampleIndex = 0; sampleIndex < SAMPLE_COUNT; sampleIndex += 1) {
    report.samples.push(sampleTree());
    if (sampleIndex + 1 < SAMPLE_COUNT) sleep(SAMPLE_DELAY_MILLIS);
}

files.write(output, JSON.stringify(report, null, 2));
toast("Accessibility Compat metrics saved: " + phase);
