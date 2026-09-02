from __future__ import annotations

import importlib.util
import unittest
from pathlib import Path


MODULE_PATH = Path(__file__).resolve().parents[1] / "generate_markdown.py"
SPEC = importlib.util.spec_from_file_location("generate_markdown", MODULE_PATH)
assert SPEC is not None and SPEC.loader is not None
MODULE = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(MODULE)


class GenerateMarkdownTest(unittest.TestCase):
    def test_render_template_replaces_known_values(self) -> None:
        self.assertEqual("before value after", MODULE.render_template("before {{ key }} after", {"key": "value"}))

    def test_render_template_rejects_missing_values(self) -> None:
        with self.assertRaises(MODULE.MarkdownGenerationError):
            MODULE.render_template("{{ missing }}", {})

    def test_duplicate_json_keys_are_rejected(self) -> None:
        with self.assertRaises(MODULE.MarkdownGenerationError):
            MODULE.reject_duplicate_pairs([("key", 1), ("key", 2)])

    def test_changelog_rendering_uses_fixed_order(self) -> None:
        changelog = {
            "changelog_label_hint": "Hint",
            "changelog_label_feature": "Feature",
            "changelog_label_fix": "Fix",
            "changelog_label_improvement": "Improvement",
            "changelog_label_dependency": "Dependency",
            "$data": {
                "v1.0.0": {
                    "released_date": "2026/09/02",
                    "hint": ["notice"],
                    "feature": ["behavior"],
                }
            },
        }
        rendered = MODULE.render_release_entries(changelog)
        self.assertLess(rendered.index("### Hint"), rendered.index("### Feature"))


if __name__ == "__main__":
    unittest.main()
