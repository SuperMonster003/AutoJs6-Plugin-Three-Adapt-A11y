<!-- This file is generated. Edit .changelog/lang_*.json and rerun .python/generate_markdown.py. -->

# Accessibility Compat 릴리스 기록

> 이 페이지의 언어: 한국어

## v1.0.0 - 2026/09/02

### 힌트

- 이 호환 방식은 실험적입니다. 결과는 WeChat 버전, 페이지와 원격 설정에 따라 달라지며 모든 기기나 계정에서 성공한다고 보장할 수 없습니다
- WeChat Mini Program, XWeb 또는 Canvas가 원래 노출하지 않은 의미 노드를 만들 수 없으며 로그인, 위험 제어 또는 악용 방지 체계를 우회하지 않습니다

### 기능

- `com.tencent.mm`만 대상으로 하는 독립 no-op 접근성 동반 APK를 제공하고 실제 애플리케이션 ID, 아이콘, 라벨, 설명과 서명을 명확히 표시
- 공개 실험이 뒷받침하는 호환 서비스 구현 클래스 이름을 등록하면서 노드 읽기와 조작은 AutoJs6 자체 서비스가 계속 수행하고 동반 콜백은 사용자 내용을 읽지 않는 구조
- AutoJs6 플러그인 정보 인터페이스로 호환 모드, 대상 패키지, 서비스 구성 요소와 최소 호스트 빌드를 보고하고 사용자가 서비스를 켜고 끄는 화면 제공

### 개선

- [AutoJs6#289](https://github.com/SuperMonster003/AutoJs6/issues/289), [#382](https://github.com/SuperMonster003/AutoJs6/issues/382), [#432](https://github.com/SuperMonster003/AutoJs6/issues/432), [#463](https://github.com/SuperMonster003/AutoJs6/issues/463), [#520](https://github.com/SuperMonster003/AutoJs6/issues/520), [#521](https://github.com/SuperMonster003/AutoJs6/issues/521), GKD `47267c7`과 WeChat 8.0.72 비식별 정적 증거 문서화
- 개인 노드 텍스트나 스크린샷을 저장하지 않고 반복 통계와 가역성으로 호환 효과를 로딩 및 캐시 우연과 구분하는 A-B-A 기기 절차 제공
- 10개 언어 README와 changelog 문안, 재현 가능한 Markdown 생성기, 읽기 전용 일관성 검사와 GitHub Actions 게이트 추가
- QV710AF65F에서 AutoJs6 자체로 수행한 7회 표본 A-B-A 실측을 기록했습니다. 노드 수가 1에서 244로 안정적으로 증가하고 호환 서비스를 끈 뒤 1로 돌아왔으며 시스템 접근성 설정의 정확한 복원도 확인했습니다
