from pathlib import Path
import importlib.util
import re
import tempfile
import unittest
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[2]
A = '{http://schemas.android.com/apk/res/android}'

class RepositoryContractTest(unittest.TestCase):
    def test_wake_manifest_contract(self):
        app = ET.parse(ROOT / 'app/src/main/AndroidManifest.xml').getroot().find('application')
        wake_name = next(item.get(A+'value') for item in app.findall('meta-data') if item.get(A+'name') == 'org.autojs.plugin.WAKE_ACTIVITY')
        wake = next(item for item in app.findall('activity') if item.get(A+'name') == wake_name)
        for key, value in {'exported':'true', 'permission':'org.autojs.permission.PLUGIN', 'theme':'@android:style/Theme.NoDisplay'}.items():
            self.assertEqual(value, wake.get(A+key), key)
        self.assertEqual(['org.autojs.plugin.action.WAKE'], [item.get(A+'name') for item in wake.findall('intent-filter/action')])
        self.assertIn('android.intent.category.DEFAULT', [item.get(A+'name') for item in wake.findall('intent-filter/category')])

    def test_localized_resources_and_placeholders(self):
        base = ROOT / 'app/src/main/res'
        qualifiers = ['', 'en', 'ar', 'es', 'fr', 'ja', 'ko', 'ru', 'zh', 'zh-rHK', 'zh-rTW']
        reference = None
        for qualifier in qualifiers:
            path = base / ('values' + ('-'+qualifier if qualifier else '')) / 'strings.xml'
            elements = list(ET.parse(path).getroot())
            values = {item.get('name'): ''.join(item.itertext()) for item in elements if item.tag == 'string'}
            self.assertEqual(len(values), len(elements), str(path))
            self.assertEqual(sorted(values), list(values), str(path))
            self.assertTrue(values.get('plugin_description'))
            self.assertNotIn(values['plugin_description'][-1], '.!?:;,')
            for text in values.values():
                self.assertIsNone(re.search(r'[\u2010-\u2027\u3000-\u303f\uff00-\uffef]', text), (path, text))
            if reference is None:
                reference = values
            else:
                self.assertEqual(set(reference), set(values), str(path))
                for key, text in values.items():
                    if qualifier == 'en': self.assertEqual(reference[key], text, key)
                    placeholder = r'%(?:\d+\$)?[sdf]'
                    self.assertEqual(sorted(re.findall(placeholder, reference[key])), sorted(re.findall(placeholder, text)), (qualifier, key))

    def test_plugin_instructions_share_one_shape(self):
        # Every locale keeps the same structure (AUTOJS6_PLUGIN_INSTRUCTION_AGENTS.md): 4 sections,
        # 4 numbered steps, 6 bold features + 4 requirements, 3 FAQ statements and the quoted UI names.
        shapes = {}
        for path in sorted((ROOT / 'app/src/main/res').glob('raw*/plugin_instruction.md')):
            text = path.read_text(encoding='utf-8')
            lines = text.splitlines()
            shapes[path.parent.name] = (
                sum(line.startswith('### ') for line in lines),
                sum(bool(re.match('[0-9]+[.] ', line)) for line in lines),
                sum(line.startswith('- **') for line in lines),
                sum(line.startswith('- ') for line in lines),
                sum(line.startswith('**') and line.endswith('**') for line in lines),
                text.count('"'),
            )
            self.assertFalse(text.startswith('# '), path)
            self.assertNotIn('{{', text, path)
            self.assertNotIn('?**', text, path)
            self.assertIn('https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y', text, path)
        self.assertEqual(11, len(shapes))
        self.assertEqual(1, len(set(shapes.values())), shapes)
        self.assertEqual((4, 4, 6, 10, 3), shapes['raw'][:5])
        self.assertEqual((ROOT / 'app/src/main/res/raw-en/plugin_instruction.md').read_bytes(),
                         (ROOT / 'app/src/main/res/raw/plugin_instruction.md').read_bytes())
        self.assertTrue(1024 <= len((ROOT / 'app/src/main/res/raw-zh/plugin_instruction.md').read_bytes()) <= 4096)

    def test_generator_check_never_writes_missing_or_drifted_output(self):
        spec = importlib.util.spec_from_file_location('repository_generator', ROOT / '.python/generate_markdown.py')
        module = importlib.util.module_from_spec(spec)
        spec.loader.exec_module(module)
        if not hasattr(module, 'CHECK_ERRORS'):
            self.skipTest('Native strict generator is covered by its own checks')
        with tempfile.TemporaryDirectory() as temporary:
            module.ROOT = Path(temporary)
            module.CHECK_MODE = True
            path = module.ROOT / 'README.md'
            module.write_text(path, 'expected\n')
            self.assertFalse(path.exists())
            path.write_bytes(b'drift\n')
            module.write_text(path, 'expected\n')
            self.assertEqual(b'drift\n', path.read_bytes())
            self.assertEqual(2, len(module.CHECK_ERRORS))

if __name__ == '__main__': unittest.main()
