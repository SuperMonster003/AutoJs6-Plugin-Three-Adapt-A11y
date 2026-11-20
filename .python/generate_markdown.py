# -*- coding: utf-8 -*-
"""Generate and verify localized README, plugin instruction and changelog Markdown.

Sources of truth:
  .readme/common.json
  .readme/lang_<code>.json
  .readme/template_readme.md
  .readme/template_plugin_instruction.md
  .changelog/lang_<code>.json
  .changelog/template_changelog.md
  version.properties

Generated artifacts:
  README.md
  .readme/README-<code>.md
  app/src/main/res/raw*/plugin_instruction.md (raw is the English copy)
  docs/changelog/CHANGELOG.md
  docs/changelog/CHANGELOG-<code>.md
  app/src/main/assets/doc/CHANGELOG-<code>.md

The changelogs use the standard release history format (`## vX.Y.Z`, `###### yyyy/mm/dd`,
"* `label` text") that the release history page parses
(D:/idea-projects/AUTOJS6_PLUGIN_RELEASE_HISTORY_AGENTS.md).

Run without arguments to write artifacts. Run with --check for a read-only CI
comparison. Generated Markdown files must not be edited by hand.
"""

from __future__ import annotations

import argparse
import json
import re
import sys
from pathlib import Path
from typing import Any


LANGUAGE_CODES = [
    "zh-Hans",
    "zh-Hant-HK",
    "zh-Hant-TW",
    "en",
    "fr",
    "es",
    "ja",
    "ko",
    "ru",
    "ar",
]
DEFAULT_LANGUAGE = "zh-Hans"
CHANGELOG_CATEGORIES = ["hint", "feature", "fix", "improvement", "dependency"]
CHANGELOG_LABEL_KEYS = [f"changelog_label_{category}" for category in CHANGELOG_CATEGORIES]
README_LIST_KEYS = [
    "architecture_points",
    "install_steps",
    "settings_points",
    "validation_points",
    "verified_device_points",
    "limit_points",
    "privacy_points",
    "ethics_points",
]
README_FAQ_KEY = "faq"
# Plugin Center instruction copy lives in its own keys and never borrows README fields
# (D:/idea-projects/AUTOJS6_PLUGIN_INSTRUCTION_AGENTS.md).
INSTRUCTION_PREFIX = "instruction_"
INSTRUCTION_LIST_KEYS = ["instruction_start_steps", "instruction_features", "instruction_requirements"]
INSTRUCTION_FAQ_KEY = "instruction_faq"
INSTRUCTION_REQUIRED_KEYS = [
    "instruction_p_what",
    "instruction_h3_start",
    "instruction_start_steps",
    "instruction_h3_features",
    "instruction_features",
    "instruction_h3_requirements",
    "instruction_requirements",
    "instruction_p_more",
]
INSTRUCTION_DIRECTORIES = {
    "zh-Hans": "raw-zh",
    "zh-Hant-HK": "raw-zh-rHK",
    "zh-Hant-TW": "raw-zh-rTW",
    "en": "raw-en",
    "fr": "raw-fr",
    "es": "raw-es",
    "ja": "raw-ja",
    "ko": "raw-ko",
    "ru": "raw-ru",
    "ar": "raw-ar",
}
INSTRUCTION_DEFAULT_LANGUAGE = "en"
PARAGRAPH = chr(10) * 2
TEMPLATE_PATTERN = re.compile(r"\{\{\s*([A-Za-z0-9_$.-]+)\s*\}\}")
VERSION_PATTERN = re.compile(r"^(\d+\.\d+\.\d+)")
RELEASE_DATE_PATTERN = re.compile(r"^\d{4}/\d{2}/\d{2}$")
PLACEHOLDER_MARKERS = (
    "TODO_TRANSLATION",
    "TRANSLATION_PENDING",
    "MACHINE_TRANSLATION_PLACEHOLDER",
)
FORBIDDEN_PUNCTUATION = set("，。；：！？（）［］【】《》“”‘’、…　،؛؟")
EXPECTED_ARTIFACT_COUNT = 43


class MarkdownGenerationError(Exception):
    """Raised when documentation sources or generated files are inconsistent."""


def require(condition: bool, message: str) -> None:
    if not condition:
        raise MarkdownGenerationError(message)


def reject_duplicate_pairs(pairs: list[tuple[str, Any]]) -> dict[str, Any]:
    result: dict[str, Any] = {}
    for key, value in pairs:
        require(key not in result, f"Duplicate JSON key: {key!r}")
        result[key] = value
    return result


def validate_source_text(path: Path, text: str) -> None:
    for marker in PLACEHOLDER_MARKERS:
        require(marker not in text, f"Placeholder marker {marker!r} remains in {path}")
    for line_number, line in enumerate(text.splitlines(), start=1):
        for character in FORBIDDEN_PUNCTUATION:
            require(
                character not in line,
                f"Non-ASCII punctuation {character!r} in {path} at line {line_number}",
            )


def load_text(path: Path) -> str:
    require(path.is_file(), f"Missing source file: {path}")
    require(not path.is_symlink(), f"Refusing to read symlink: {path}")
    try:
        text = path.read_text(encoding="utf-8")
    except UnicodeDecodeError as error:
        raise MarkdownGenerationError(f"Invalid UTF-8 in {path}: {error}") from None
    validate_source_text(path, text)
    return text


def load_json(path: Path) -> dict[str, Any]:
    text = load_text(path)
    try:
        value = json.loads(text, object_pairs_hook=reject_duplicate_pairs)
    except MarkdownGenerationError as error:
        raise MarkdownGenerationError(f"{error} in {path}") from None
    except json.JSONDecodeError as error:
        raise MarkdownGenerationError(f"Invalid JSON in {path}: {error}") from None
    require(isinstance(value, dict), f"JSON root must be an object: {path}")
    return value


def source_files(directory: Path) -> set[str]:
    return {path.name for path in directory.glob("lang_*.json")}


def validate_language_file_sets(root: Path) -> None:
    expected = {f"lang_{code}.json" for code in LANGUAGE_CODES}
    for relative in (".readme", ".changelog"):
        actual = source_files(root / relative)
        require(
            actual == expected,
            f"Language source mismatch in {relative}: missing={sorted(expected - actual)} "
            f"extra={sorted(actual - expected)}",
        )


def shape_of(value: Any) -> Any:
    if isinstance(value, dict):
        return ("dict", tuple(sorted(value)))
    if isinstance(value, list):
        return ("list", len(value), tuple(shape_of(item) for item in value))
    return type(value).__name__


def validate_same_shape(items: dict[str, dict[str, Any]], kind: str) -> None:
    reference = items[DEFAULT_LANGUAGE]
    for code, content in items.items():
        require(
            set(content) == set(reference),
            f"{kind} keys for {code!r} differ from {DEFAULT_LANGUAGE!r}",
        )
        for key, value in content.items():
            require(
                shape_of(value) == shape_of(reference[key]),
                f"{kind} field {key!r} for {code!r} has a different shape",
            )


def validate_readme_sources(items: dict[str, dict[str, Any]]) -> None:
    validate_same_shape(items, "README")
    for code, content in items.items():
        for key in README_LIST_KEYS:
            value = content.get(key)
            require(isinstance(value, list) and value, f"README {code!r} {key!r} must be a non-empty list")
            require(all(isinstance(item, str) and item for item in value), f"README {code!r} {key!r} must contain strings")
        faq = content.get(README_FAQ_KEY)
        require(isinstance(faq, list) and faq, f"README {code!r} faq must be a non-empty list")
        for index, item in enumerate(faq):
            require(
                isinstance(item, dict) and set(item) == {"q", "a"},
                f"README {code!r} faq[{index}] must contain exactly q and a",
            )
        validate_instruction_language(code, content)
    require(
        "简体中文" in str(items[DEFAULT_LANGUAGE].get("language_notice", "")),
        "Default README must explicitly identify itself as Simplified Chinese",
    )


def validate_instruction_language(code: str, content: dict[str, Any]) -> None:
    for key in INSTRUCTION_REQUIRED_KEYS:
        require(key in content, f"Instruction language {code!r} is missing {key!r}")
    for key, value in content.items():
        if not key.startswith(INSTRUCTION_PREFIX):
            continue
        if key in INSTRUCTION_LIST_KEYS:
            require(
                isinstance(value, list) and value and all(isinstance(item, str) and item for item in value),
                f"Instruction {code!r} field {key!r} must be a non-empty list of non-empty strings",
            )
            texts = value
        elif key == INSTRUCTION_FAQ_KEY:
            require(isinstance(value, list) and value, f"Instruction {code!r} field {key!r} must be a non-empty list")
            for index, item in enumerate(value):
                require(
                    isinstance(item, dict) and set(item) == {"q", "a"} and all(isinstance(text, str) and text for text in item.values()),
                    f"Instruction {code!r} {key}[{index}] must be an object with non-empty 'q' and 'a'",
                )
            texts = [text for item in value for text in item.values()]
        else:
            require(isinstance(value, str) and value, f"Instruction {code!r} field {key!r} must be non-empty text")
            texts = [value]
        # UI names are quoted with ASCII double quotes; an odd count means a quote was swallowed on the way in.
        for text in texts:
            require(text.count('"') % 2 == 0, f"Instruction {code!r} field {key!r} has an unpaired double quote: {text[:60]!r}")


def validate_changelog_sources(items: dict[str, dict[str, Any]]) -> None:
    expected_top_level = set(CHANGELOG_LABEL_KEYS) | {"$data", "h1_release_history", "language_notice"}
    reference_versions: list[str] | None = None
    reference_shapes: dict[str, Any] | None = None
    for code, content in items.items():
        require(set(content) == expected_top_level, f"Unexpected changelog keys for {code!r}")
        data = content["$data"]
        require(isinstance(data, dict) and data, f"Changelog {code!r} must have release entries")
        versions = list(data)
        shapes: dict[str, Any] = {}
        for version, entry in data.items():
            require(isinstance(entry, dict), f"Changelog {code!r} {version!r} must be an object")
            require(RELEASE_DATE_PATTERN.match(str(entry.get("released_date", ""))) is not None, f"Invalid release date in {code!r} {version!r}")
            unknown = set(entry) - {"released_date", *CHANGELOG_CATEGORIES}
            require(not unknown, f"Unknown categories in {code!r} {version!r}: {sorted(unknown)}")
            for category in CHANGELOG_CATEGORIES:
                if category in entry:
                    require(
                        isinstance(entry[category], list) and entry[category] and all(isinstance(item, str) and item for item in entry[category]),
                        f"Changelog {code!r} {version!r} {category!r} must contain strings",
                    )
            shapes[version] = shape_of(entry)
        if reference_versions is None:
            reference_versions = versions
            reference_shapes = shapes
        else:
            require(versions == reference_versions, f"Changelog versions for {code!r} differ from the default")
            require(shapes == reference_shapes, f"Changelog entry shapes for {code!r} differ from the default")
            default_data = items[DEFAULT_LANGUAGE]["$data"]
            for version in versions:
                require(
                    data[version]["released_date"] == default_data[version]["released_date"],
                    f"Release date for {code!r} {version!r} differs from the default",
                )


def read_properties(path: Path) -> dict[str, str]:
    result: dict[str, str] = {}
    require(path.is_file(), f"Missing properties file: {path}")
    try:
        text = path.read_text(encoding="utf-8")
    except UnicodeDecodeError as error:
        raise MarkdownGenerationError(f"Invalid UTF-8 in {path}: {error}") from None
    for line in text.splitlines():
        stripped = line.strip()
        if not stripped or stripped.startswith("#") or "=" not in stripped:
            continue
        key, _, value = stripped.partition("=")
        result[key.strip()] = value.strip()
    return result


def current_version_label(root: Path) -> str:
    version_name = read_properties(root / "version.properties").get("VERSION_NAME", "")
    match = VERSION_PATTERN.match(version_name)
    require(match is not None, f"VERSION_NAME is not semantic: {version_name!r}")
    return f"v{match.group(1)}"


def render_template(template: str, values: dict[str, Any]) -> str:
    def replace(match: re.Match[str]) -> str:
        key = match.group(1)
        require(key in values, f"Missing template value: {key}")
        return str(values[key])

    rendered = TEMPLATE_PATTERN.sub(replace, template)
    unresolved = TEMPLATE_PATTERN.findall(rendered)
    require(not unresolved, f"Unresolved template values: {sorted(set(unresolved))}")
    return rendered


def render_dynamic(value: Any, values: dict[str, Any]) -> Any:
    if isinstance(value, dict):
        return {key: render_dynamic(item, values) for key, item in value.items()}
    if isinstance(value, list):
        return [render_dynamic(item, values) for item in value]
    if isinstance(value, str):
        return render_template(value, values)
    return value


def bullet_list(items: list[str]) -> str:
    return "\n".join(f"- {item}" for item in items)


def numbered_list(items: list[str]) -> str:
    return "\n".join(f"{index}. {item}" for index, item in enumerate(items, start=1))


def faq_list(items: list[dict[str, str]]) -> str:
    return "\n\n".join(f"#### {item['q']}\n\n{item['a']}" for item in items)


def render_release_entries(
    changelog: dict[str, Any],
    limit: int | None = None,
    version_heading_level: int = 2,
) -> str:
    releases: list[str] = []
    entries = list(changelog["$data"].items())
    if limit is not None:
        entries = entries[:limit]
    for version, entry in entries:
        version_prefix = "#" * version_heading_level
        category_prefix = "#" * (version_heading_level + 1)
        sections = [f"{version_prefix} {version} - {entry['released_date']}"]
        for category in CHANGELOG_CATEGORIES:
            values = entry.get(category)
            if not values:
                continue
            sections.extend((f"{category_prefix} {changelog[f'changelog_label_{category}']}", bullet_list(values)))
        releases.append("\n\n".join(sections))
    return "\n\n".join(releases)


def render_standard_release_history(changelog: dict[str, Any]) -> str:
    """Every version as `## vX.Y.Z`, `###### yyyy/mm/dd` and one `* `label` text` line per entry."""
    releases: list[str] = []
    for version, entry in changelog["$data"].items():
        lines: list[str] = []
        for category in CHANGELOG_CATEGORIES:
            label = changelog[f"changelog_label_{category}"]
            lines.extend(f"* `{label}` {item}" for item in entry.get(category, []))
        releases.append(PARAGRAPH.join((f"## {version}", f"###### {entry['released_date']}", chr(10).join(lines))))
    return PARAGRAPH.join(releases)


def build_instruction_values(localized: dict[str, Any]) -> dict[str, Any]:
    """Only instruction_* keys reach the instruction template; {{ repo_url }} and the like are already filled in."""
    values = {key: value for key, value in localized.items() if key.startswith(INSTRUCTION_PREFIX)}
    for key in INSTRUCTION_LIST_KEYS:
        if key in values:
            values[f"placeholder_{key}"] = (numbered_list if key == "instruction_start_steps" else bullet_list)(values[key])
    if INSTRUCTION_FAQ_KEY in values:
        faq = [f"**{item['q']}**{PARAGRAPH}{item['a']}" for item in values[INSTRUCTION_FAQ_KEY]]
        values[f"placeholder_{INSTRUCTION_FAQ_KEY}"] = PARAGRAPH.join(faq)
    return values


def language_navigation(common: dict[str, Any], readmes: dict[str, dict[str, Any]]) -> str:
    repo = common["repo_url"]
    values = []
    for code in LANGUAGE_CODES:
        name = readmes[code]["$name"]
        values.append(f"[{name}]({repo}/blob/master/.readme/README-{code}.md)")
    return " | ".join(values)


def read_sources(root: Path) -> tuple[dict[str, Any], dict[str, dict[str, Any]], dict[str, dict[str, Any]]]:
    validate_language_file_sets(root)
    common = load_json(root / ".readme" / "common.json")
    readmes = {code: load_json(root / ".readme" / f"lang_{code}.json") for code in LANGUAGE_CODES}
    changelogs = {code: load_json(root / ".changelog" / f"lang_{code}.json") for code in LANGUAGE_CODES}
    validate_readme_sources(readmes)
    validate_changelog_sources(changelogs)
    version = current_version_label(root)
    for code, changelog in changelogs.items():
        newest = next(iter(changelog["$data"]))
        require(newest == version, f"Newest changelog {newest!r} for {code!r} does not match {version!r}")
    return common, readmes, changelogs


def expected_artifacts(root: Path) -> dict[Path, str]:
    common, readmes, changelogs = read_sources(root)
    readme_template = load_text(root / ".readme" / "template_readme.md")
    instruction_template = load_text(root / ".readme" / "template_plugin_instruction.md")
    changelog_template = load_text(root / ".changelog" / "template_changelog.md")
    navigation = language_navigation(common, readmes)
    outputs: dict[Path, str] = {}

    for code in LANGUAGE_CODES:
        localized = render_dynamic(readmes[code], common)
        changelog = render_dynamic(changelogs[code], common)
        values = {
            **common,
            **localized,
            "placeholder_language_navigation": navigation,
            "placeholder_architecture_points": bullet_list(localized["architecture_points"]),
            "placeholder_install_steps": numbered_list(localized["install_steps"]),
            "placeholder_settings_points": bullet_list(localized["settings_points"]),
            "placeholder_validation_points": bullet_list(localized["validation_points"]),
            "placeholder_verified_device_points": bullet_list(localized["verified_device_points"]),
            "placeholder_limit_points": bullet_list(localized["limit_points"]),
            "placeholder_privacy_points": bullet_list(localized["privacy_points"]),
            "placeholder_ethics_points": bullet_list(localized["ethics_points"]),
            "placeholder_faq": faq_list(localized["faq"]),
            "placeholder_latest_release": render_release_entries(
                changelog,
                limit=1,
                version_heading_level=4,
            ),
            "changelog_url": f"{common['repo_url']}/blob/master/docs/changelog/CHANGELOG-{code}.md",
        }
        rendered_readme = render_template(readme_template, values).rstrip() + "\n"
        outputs[root / ".readme" / f"README-{code}.md"] = rendered_readme

        instruction = render_template(instruction_template, build_instruction_values(localized)).rstrip() + "\n"
        resource_dir = root / "app" / "src" / "main" / "res"
        outputs[resource_dir / INSTRUCTION_DIRECTORIES[code] / "plugin_instruction.md"] = instruction
        if code == INSTRUCTION_DEFAULT_LANGUAGE:
            outputs[resource_dir / "raw" / "plugin_instruction.md"] = instruction

        changelog_values = {
            **common,
            **changelog,
            "placeholder_release_history": render_standard_release_history(changelog),
        }
        rendered_changelog = render_template(changelog_template, changelog_values).rstrip() + "\n"
        outputs[root / "docs" / "changelog" / f"CHANGELOG-{code}.md"] = rendered_changelog
        outputs[root / "app" / "src" / "main" / "assets" / "doc" / f"CHANGELOG-{code}.md"] = rendered_changelog

    outputs[root / "README.md"] = outputs[root / ".readme" / f"README-{DEFAULT_LANGUAGE}.md"]
    outputs[root / "docs" / "changelog" / "CHANGELOG.md"] = outputs[
        root / "docs" / "changelog" / f"CHANGELOG-{DEFAULT_LANGUAGE}.md"
    ]
    require(len(outputs) == EXPECTED_ARTIFACT_COUNT, f"Expected {EXPECTED_ARTIFACT_COUNT} artifacts, got {len(outputs)}")
    return outputs


def validate_no_orphans(root: Path, expected: dict[Path, str]) -> None:
    actual = set((root / ".readme").glob("README-*.md"))
    changelog_dir = root / "docs" / "changelog"
    if changelog_dir.is_dir():
        actual.update(changelog_dir.glob("CHANGELOG*.md"))
    actual.update((root / "app/src/main/assets/doc").glob("CHANGELOG*.md"))
    actual.update((root / "app/src/main/res").glob("raw*/plugin_instruction.md"))
    expected_generated = {path for path in expected if path != root / "README.md"}
    orphans = sorted(path.relative_to(root).as_posix() for path in actual - expected_generated)
    require(not orphans, f"Orphan generated Markdown files: {orphans}")


def write_or_check(root: Path, check: bool) -> None:
    outputs = expected_artifacts(root)
    validate_no_orphans(root, outputs)
    drift: list[str] = []
    for path, expected in outputs.items():
        if check:
            if not path.is_file() or path.read_text(encoding="utf-8") != expected:
                drift.append(path.relative_to(root).as_posix())
            continue
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(expected, encoding="utf-8", newline="\n")
    require(not drift, f"Generated Markdown is missing or stale: {drift}")


def parse_args(argv: list[str]) -> argparse.Namespace:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--check", action="store_true", help="compare generated files without writing")
    return parser.parse_args(argv)


def main(argv: list[str] | None = None) -> int:
    args = parse_args(sys.argv[1:] if argv is None else argv)
    root = Path(__file__).resolve().parent.parent
    try:
        write_or_check(root, args.check)
    except (MarkdownGenerationError, OSError) as error:
        print(f"MARKDOWN_ERROR {error}", file=sys.stderr)
        return 1
    mode = "check" if args.check else "write"
    print(f"MARKDOWN_OK mode={mode} artifacts={EXPECTED_ARTIFACT_COUNT}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
