<!-- This file is generated. Edit .readme/lang_*.json and rerun .python/generate_markdown.py. -->
<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-three-adapt-a11y-ic-launcher" border="0" width="128" />
    </picture>
  </p>
  <h1>3-Adapt A11y</h1>
  <p>지원 앱을 위한 개인정보 최소화 접근성 호환 트리거</p>
  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/issues"><img alt="GitHub issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y?color=534BAE&label=License"/></a>
  </p>
</div>

> 이 페이지의 언어: 한국어

### 언어 (Languages)

[简体中文](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-zh-Hans.md) | [香港繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-zh-Hant-HK.md) | [台灣繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-zh-Hant-TW.md) | [English](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-en.md) | [Français](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-fr.md) | [Español](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-es.md) | [日本語](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ja.md) | [한국어](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ko.md) | [Русский](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ru.md) | [العربية](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ar.md)

### 프로젝트 상태

3-Adapt A11y은 독립적인 실험용 동반 APK입니다. 게시된 지원 프로필의 대상 앱이 컨트롤 트리를 AutoJs6 접근성 서비스에 노출하도록 지원합니다. 범용 자동화 엔진이 아니며 컨트롤을 읽거나 조작하는 AutoJs6를 대체하지 않습니다.

> 호환성은 각 대상 앱의 버전, 페이지, 기기 및 원격 설정에 따라 달라집니다. 모든 환경에서 성공한다고 보장할 수 없습니다. 스크립트에 사용하기 전에 A-B-A 검증을 완료하십시오.

### No-op 동반 구조

앱 호환 프로필을 별도 APK에 격리하고 AutoJs6 핵심 서비스의 이름과 일반 동작을 변경하지 않습니다:

- 노드를 읽고, 검색하고, 조작하는 유일한 구성 요소는 계속 AutoJs6 접근성 서비스입니다.
- 동반 앱은 공개 실험에서 확인된 서비스 구현 클래스 이름을 등록하지만 애플리케이션 ID, 아이콘, 라벨, 설명과 서명은 3-Adapt A11y의 실제 신원을 표시합니다.
- 호환 서비스 콜백은 no-op입니다. `event.source`, `event.text`, `rootInActiveWindow`, 스크린샷 또는 페이지 내용을 읽지 않습니다.
- 이 릴리스는 노드 프록시나 Binder 브리지가 아닙니다. 지원 프로필에 기록된 조건부 노드 노출 동작을 트리거하는 것만 시도합니다.

### 설치와 사용

1. 기기가 Android 7.0 (API 24) 이상이고 AutoJs6 내부 빌드가 3923 이상인지 확인합니다.
2. APK는 이 프로젝트의 [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/releases) 또는 신뢰할 수 있는 AutoJs6 플러그인 경로에서만 설치합니다.
3. 3-Adapt A11y을 열고 실제 이름과 목적을 확인한 다음 상태 카드를 탭하여 호환 서비스 관리자를 엽니다. 기본 정책은 AutoJs6 따르기로, AutoJs6 접근성 서비스가 활성화된 동안에만 호환 서비스를 활성 상태로 유지합니다.
4. root, WRITE_SECURE_SETTINGS 또는 Shizuku가 없으면 3-Adapt A11y 아래 표시되는 접근성 서비스를 Android 설정에서 직접 활성화합니다. Android의 위험 경고는 정상입니다. 이 중 하나를 의도적으로 부여하면 앱이 선택한 정책을 스스로 적용하며 다른 설정은 건드리지 않습니다.
5. AutoJs6 접근성 서비스도 활성화한 상태로 지원 프로필에 기록된 대상 페이지를 다시 열고 AutoJs6 레이아웃 검사기 또는 스크립트에서 노드를 확인합니다.

APK 설치만으로는 효과가 없습니다. 필요하지 않을 때는 사용 안 함 정책을 선택하거나 Android 설정에서 호환 서비스를 끄고, 사용이 끝나면 앱을 제거할 수 있습니다.

### 설정과 서비스 제어

설정 화면은 AutoJs6를 따를 수 있는 일반 옵션을 모아 두고, 호환 서비스 관리자는 서비스를 언제 실행할지 결정합니다:

- 언어, 다크 모드, 테마 색상은 기본적으로 AutoJs6를 따르며 AutoJs6 설정 계약에서 읽어 옵니다. AutoJs6가 없으면 시스템 또는 내장 값으로 대체됩니다.
- 제어 정책은 AutoJs6 따르기 (기본), 사용, 사용 안 함 중 하나입니다. AutoJs6 따르기는 호환 서비스를 AutoJs6 접근성 서비스와 같은 상태로 유지하며 AutoJs6가 변경을 알릴 때, 시스템의 활성 서비스 목록이 바뀔 때, 앱을 열 때 다시 확인합니다.
- 자동 전환은 root, WRITE_SECURE_SETTINGS (`adb shell pm grant`로 부여) 또는 Shizuku를 사용하며 각 방식은 개별적으로 끌 수 있습니다. 모두 사용할 수 없으면 앱은 시스템 접근성 설정만 엽니다.
- 관리자는 두 서비스와 각 방식의 실시간 상태를 표시하고, 진단 보고서를 복사하며, 이 서비스의 Android 설정 페이지를 엽니다.
- 독립 설정을 평면 그룹, 일관된 행과 중앙의 둥근 대화상자로 통일했습니다. 언어, 야간 모드, 테마 색과 런처 아이콘은 확인 후 적용되며 취소하면 저장 값이 유지됩니다. 색은 기본적으로 AutoJs6를 따르며 공통 팔레트, HEX/RGB 입력과 부분 미리 보기를 제공합니다. 중립 배경색은 고정하고 컨트롤은 선택한 테마를 따릅니다. 런처 기본값은 자동 적응형이며 업데이트 시 명시적 선택을 유지합니다.

### A-B-A 기기 검증

한 번 성공한 dump를 증거로 보지 마십시오. 변화를 서비스 때문이라고 판단하기 전에 동일한 정적 페이지에서 A-B-A 비교를 실행합니다:

- A: AutoJs6는 켜고 호환 서비스는 끈 상태에서 비식별 표본을 최소 5회 수집합니다.
- B: 호환 서비스만 추가로 켜고 같은 페이지로 돌아가 최소 5회 더 수집합니다.
- A2: 호환 서비스를 다시 끄고 수집을 반복합니다. 변화가 되돌아가야 로딩과 캐시 우연을 배제할 수 있습니다.
- 노드 수, 비어 있지 않은 text/desc/resource-id 수, clickable 수와 구조 hash만 기록합니다. 채팅, 연락처 이름 또는 스크린샷은 보관하지 않습니다.

[전체 A-B-A 검증 절차 보기](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/aba-device-validation.md)

### 실기기 검증 결과

2026-09-02에 승인된 Sony XQ-AT72, Android 12/API 31, WeChat 8.0.72 code 3085, AutoJs6 6.8.0 code 5277 조합에서 가역적인 A-B-A 검증을 완료했습니다. 기존 접근성 서비스 6개는 변경하지 않았습니다. 각 단계마다 WeChat을 강제 종료하고 LauncherUI를 다시 실행한 뒤 5초 넘게 기다리고, AutoJs6 자체에서 7회 연속 표본을 수집했습니다:

- A 호환 서비스 끔: nodes 1, text 0, desc 0, id 0, clickable 0, 7회 모두 동일한 구조 hash.
- B 호환 서비스 켬 및 대상 서비스 2개가 모두 bound: nodes 244, text 31, desc 13, id 157, clickable 38, 7회 모두 동일한 구조 hash.
- A2 다시 끔: nodes 1과 나머지 모든 지표 0으로 정확히 돌아왔고 7회 모두 동일한 구조 hash. 테스트 후 시스템 접근성 설정을 정확히 복원했습니다.

이 결과는 해당 기기, WeChat 빌드, LauncherUI 페이지 조합에서만 호환 트리거를 검증합니다. 다른 버전, 계정, 기기, Mini Program, XWeb 또는 Canvas 페이지로 일반화할 수 없습니다.

[QV710AF65F 전체 비식별 보고서 보기](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/device-QV710AF65F.md)

### 알려진 제한

- 현재 지원 프로필에는 WeChat (`com.tencent.mm`)만 포함됩니다. 이는 명시적인 지원 집합이며 범용 호환성을 의미하지 않습니다.
- WebView, 미니 앱, Canvas와 사용자 정의 그리기 컨트롤에는 네이티브 의미 노드가 없을 수 있습니다. 누락된 `text` 또는 `content-desc`를 만들 수 없습니다.
- 루트가 돌아와도 일부 페이지는 빈 속성, 오래된 노드, 무작위 트리 또는 경계 정보만 노출할 수 있습니다.
- 대상 앱 업데이트나 원격 설정 변경으로 프로필이 언제든 무효가 될 수 있습니다. 다운그레이드, OCR과 좌표 대안도 보안 및 안정성 비용이 있습니다.
- 플러그인은 관찰 가능성만 다룹니다. 로그인, 위험 제어, captcha, 권한, 계정 제한 또는 악용 방지 체계를 우회하지 않습니다.
- Android 17 고급 보호는 이 호환 서비스를 포함해 접근성 도구가 아닌 접근성 서비스를 제한할 수 있습니다. 전역 모드만으로 이 서비스가 차단되었는지 판단할 수 없습니다. 실제 서비스 상태와 Android 접근성 설정을 확인하세요. 보안 설정 권한을 부여해도 시스템 제한을 우회하지 않습니다.

### 개인정보 경계

- 호환 콜백은 이벤트 소스, 이벤트 텍스트, 활성 창 루트 또는 화면 이미지를 읽지 않습니다.
- 앱은 대상 앱 페이지 내용을 업로드, 저장 또는 로그로 남기지 않으며 네트워크나 분석 기능도 포함하지 않습니다.
- 저장소, 오버레이, 카메라, 마이크 또는 미디어 권한을 요청하지 않습니다. WRITE_SECURE_SETTINGS와 Shizuku 권한은 선택적 서비스 제어를 위해서만 선언되며 사용자가 부여하기 전까지는 동작하지 않습니다.
- 플러그인 정보 Binder는 버전, 신원 및 기능 메타데이터만 보고하며 노드 트리를 전송하지 않습니다.
- 호환 서비스는 사용자가 선택한 제어 정책에 따라서만 바뀌며, 사용자가 허용한 경우에만 root, 보안 설정 또는 Shizuku를 통해 전환됩니다. 앱은 다른 시스템 설정을 수정하지 않으며 AutoJs6 서비스를 활성화하지도 않습니다.

### 윤리와 규정 준수

접근성 호환 기능은 사용자가 조작 권한을 가진 인터페이스의 자동화만 지원해야 합니다. 스크립트 동작과 계정 결과는 사용자 책임입니다.

- 자신의 기기와 계정 또는 명시적으로 허가된 흐름에서만 사용합니다.
- 괴롭힘, spam, 동의 없는 데이터 수집, 타인 감시 또는 보안 제어 우회에 사용하지 않습니다.
- 관련 법률, 대상 플랫폼 규칙과 조직 정책을 따르고 UI 변경이나 실수에 대해 사람의 확인과 중단 조건을 둡니다.
- 진단 공유는 집계 통계와 비식별 구조만 포함해야 합니다. 채팅, 연락처, token, APK 또는 원본 dex를 공개하지 않습니다.

### 호환 정보

서비스 구성 요소에는 공개 실험에서 사용한 호환 클래스 이름이 들어 있습니다. Google Select to Speak가 아니며 읽어주기 기능을 제공하지 않고 Google 앱이나 서명을 사칭하지 않습니다. 실제 신원은 프로젝트의 ID, 라벨, 아이콘, 정보 페이지와 서명에 항상 표시됩니다.

```text
application id: io.github.supermonster003.autojs6.plugin.three.adapt.a11y
accessibility service: io.github.supermonster003.autojs6.plugin.three.adapt.a11y/com.google.android.accessibility.selecttospeak.SelectToSpeakService
supported packages: com.tencent.mm
minimum Android: Android 7.0 (API 24)
minimum AutoJs6 build: 3923
```

### 자주 묻는 질문

#### 이 프로젝트가 Google 앱을 사칭합니까?

아닙니다. 호환 실험에는 접근성 서비스 구현 클래스 이름만 사용합니다. 패키지, 앱과 서비스 라벨, 아이콘, 문서 및 서명은 사실대로 유지하며 Google Select to Speak가 아님을 명시합니다.

#### 설치 후 아무 변화가 없는 이유는 무엇입니까?

root, WRITE_SECURE_SETTINGS 또는 Shizuku가 없으면 Android는 앱이 자체 접근성 서비스를 활성화하도록 허용하지 않으므로 설정에서 호환 서비스와 AutoJs6를 모두 켜야 합니다. 이 중 하나를 사용할 수 있으면 호환 서비스 관리자가 선택한 정책을 대신 적용합니다. 변화가 없으면 A-B-A 절차로 현재 대상 앱 버전과 페이지를 확인하십시오.

#### 지원 페이지에서 텍스트 노드가 여전히 없는 이유는 무엇입니까?

WebView, 미니 앱, Canvas 또는 사용자 정의 렌더링 페이지에는 Android 의미 노드가 없을 수 있습니다. 서비스는 조건부로 숨겨진 트리 복원만 시도할 수 있으며 원래 노출되지 않은 정보를 만들 수 없습니다.

#### 플러그인이 계정 안전을 보장합니까?

아닙니다. 대상 플랫폼 위험 제어를 우회하지 않으며 어떤 자동화도 안전하다고 약속할 수 없습니다. 위험이 낮고 감사 가능하며 사람이 확인하는 스크립트를 사용하고 플랫폼 규칙을 따르십시오.

### 연구 근거

연구 문서는 AutoJs6 #289, #382, #432, #463, #520, #521, GKD `47267c7`과 테스트 기기의 WeChat 8.0.72에서 얻은 비식별 정적 증거를 다룹니다. 공개 사실, 재현 가능한 실험과 추론을 구분하며 WeChat 내부 동작을 공식 보장으로 표현하지 않습니다.

[접근성 서비스 신원 호환 연구 문서 보기](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/research/accessibility-service-identity-compat.md)

### 릴리스 기록

#### v1.4.0 - 2026/10/03

##### 힌트

- 3-Adapt A11y는 새 패키지 io.github.supermonster003.autojs6.plugin.three.adapt.a11y로 별도 설치됩니다. 기존 Accessibility Compat와 데이터는 유지할 수 있지만 설정은 자동 이전되지 않습니다. 필요할 때 새 접근성 서비스를 활성화하세요

##### 기능

- 독립 설정을 평면 그룹, 일관된 행과 중앙의 둥근 대화상자로 통일했습니다. 언어, 야간 모드, 테마 색과 런처 아이콘은 확인 후 적용되며 취소하면 저장 값이 유지됩니다. 색은 기본적으로 AutoJs6를 따르며 공통 팔레트, HEX/RGB 입력과 부분 미리 보기를 제공합니다. 중립 배경색은 고정하고 컨트롤은 선택한 테마를 따릅니다. 런처 기본값은 자동 적응형이며 업데이트 시 명시적 선택을 유지합니다.

##### 수정

- 설치 시 자동 아이콘의 색상이 고정되는 문제를 수정하여 런처가 설정에 맞는 밝거나 어두운 리소스를 불러올 수 있습니다.

##### 개선

- 플러그인 이름을 Accessibility Compat에서 3-Adapt A11y로 변경하고 앱 제목, 접근성 서비스 라벨, 플러그인 ID `three-adapt-a11y`, 패키지 및 구성 요소 이름, 릴리스 산출물, 문서, GitHub 저장소를 함께 갱신
- 런처와 플러그인 센터 아이콘의 시각적 크기를 통일하고 투명 배경과 흑백 또는 중성 회색조 적용

##### 의존성

- Material Components 1.13.0 / AppCompat 1.7.1 (Material 3).

[전체 릴리스 기록 보기](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/changelog/CHANGELOG-ko.md)

### 빌드와 문서 검사

일반 사용자는 Releases의 미리 빌드된 APK를 설치해야 합니다. 개발자는 저장소 Gradle Wrapper로 프로젝트를 빌드하고 검증할 수 있습니다:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug
py .python\generate_markdown.py --check
```

README와 changelog는 JSON 문안에서 생성됩니다. `.readme/lang_*.json`, `.changelog/lang_*.json` 또는 템플릿을 변경한 후 실행합니다:

```powershell
py .python\generate_markdown.py
py .python\generate_markdown.py --check
```

### 라이선스

프로젝트 코드는 [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/LICENSE)으로 제공됩니다. WeChat, Google 및 Select to Speak 이름은 각 소유자에게 속하며 이 프로젝트는 해당 회사와 제휴하거나 승인을 받지 않았습니다.

### 링크

- [AutoJs6](https://github.com/SuperMonster003/AutoJs6)
- [접근성 서비스 신원 호환 연구 문서 보기](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/research/accessibility-service-identity-compat.md)
- [전체 A-B-A 검증 절차 보기](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/aba-device-validation.md)
- [QV710AF65F 전체 비식별 보고서 보기](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/device-QV710AF65F.md)


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/16kb.md)
