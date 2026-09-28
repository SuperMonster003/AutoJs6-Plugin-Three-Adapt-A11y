<!-- This file is generated. Edit .readme/lang_*.json and rerun .python/generate_markdown.py. -->
<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="{{ repo_url }}/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="{{ icon_alt }}" border="0" width="128" />
  </p>
  <h1>3-Adapt A11y</h1>
  <p>{{ synopsis }}</p>
  <p>
    <a href="{{ repo_url }}/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/{{ repo_slug }}?label=Release"/></a>
    <a href="{{ repo_url }}/issues"><img alt="GitHub issues" src="https://img.shields.io/github/issues/{{ repo_slug }}?color=A24232&label=Issues"/></a>
    <a href="{{ license_url }}"><img alt="GitHub License" src="https://img.shields.io/github/license/{{ repo_slug }}?color=534BAE&label=License"/></a>
  </p>
</div>

> {{ language_notice }}

### {{ h3_languages }}

{{ placeholder_language_navigation }}

### {{ h3_status }}

{{ p_status }}

> {{ p_experimental }}

### {{ h3_architecture }}

{{ p_architecture }}

{{ placeholder_architecture_points }}

### {{ h3_install }}

{{ placeholder_install_steps }}

{{ p_disable }}

### {{ h3_settings }}

{{ p_settings }}

{{ placeholder_settings_points }}

### {{ h3_validation }}

{{ p_validation }}

{{ placeholder_validation_points }}

[{{ text_validation_link }}]({{ validation_url }})

### {{ h3_verified_device_result }}

{{ p_verified_device_result }}

{{ placeholder_verified_device_points }}

{{ p_verified_device_scope }}

[{{ text_verified_device_report }}]({{ device_report_url }})

### {{ h3_limits }}

{{ placeholder_limit_points }}

### {{ h3_privacy }}

{{ placeholder_privacy_points }}

### {{ h3_ethics }}

{{ p_ethics }}

{{ placeholder_ethics_points }}

### {{ h3_compatibility }}

{{ p_compatibility }}

```text
application id: {{ application_id }}
accessibility service: {{ service_component }}
supported packages: {{ supported_packages }}
minimum Android: {{ minimum_android }}
minimum AutoJs6 build: {{ required_host_build }}
```

### {{ h3_faq }}

{{ placeholder_faq }}

### {{ h3_research }}

{{ p_research }}

[{{ text_research_link }}]({{ research_url }})

### {{ h3_release_history }}

{{ placeholder_latest_release }}

[{{ text_full_changelog }}]({{ changelog_url }})

### {{ h3_build }}

{{ p_build }}

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug
py .python\generate_markdown.py --check
```

{{ p_generate }}

```powershell
py .python\generate_markdown.py
py .python\generate_markdown.py --check
```

### {{ h3_license }}

{{ p_license }}

### {{ h3_links }}

- [AutoJs6]({{ autojs6_url }})
- [{{ text_research_link }}]({{ research_url }})
- [{{ text_validation_link }}]({{ validation_url }})
- [{{ text_verified_device_report }}]({{ device_report_url }})


[16 KB page alignment and build verification]({{ repo_url }}/blob/master/docs/16kb.md)
