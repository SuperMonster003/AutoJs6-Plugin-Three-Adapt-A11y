<!-- This file is generated. Edit .readme/lang_*.json and rerun .python/generate_markdown.py. -->
<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-accessibility-compat-icon" border="0" width="128" />
  </p>
  <h1>Accessibility Compat</h1>
  <p>Минимальный по отношению к конфиденциальности триггер совместимости специальных возможностей для затронутых версий WeChat</p>
  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/issues"><img alt="GitHub issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?color=534BAE&label=License"/></a>
  </p>
</div>

> Язык страницы: русский

### Языки

[简体中文](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hans.md) | [香港繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hant-HK.md) | [台灣繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hant-TW.md) | [English](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-en.md) | [Français](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-fr.md) | [Español](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-es.md) | [日本語](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ja.md) | [한국어](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ko.md) | [Русский](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ru.md) | [العربية](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ar.md)

### Состояние проекта

Accessibility Compat представляет собой отдельный экспериментальный APK-компаньон. Он пытается вернуть публикацию дерева элементов управления для службы специальных возможностей AutoJs6 в затронутых версиях WeChat. Это не универсальный механизм автоматизации и он не заменяет AutoJs6 при чтении или управлении элементами.

> Совместимость зависит от версии WeChat и удаленной конфигурации. Проект не гарантирует результат для каждой версии, страницы, учетной записи или устройства. Проведите проверку A-B-A до использования в сценарии.

### Архитектура no-op компаньона

Проект изолирует эксперимент совместимости WeChat в отдельном APK и не меняет имя или общее поведение основной службы AutoJs6:

- Служба специальных возможностей AutoJs6 остается единственным компонентом, который читает, запрашивает и активирует узлы.
- Компаньон регистрирует имя класса реализации службы, подтвержденное публичными экспериментами, но его ID приложения, значок, подписи, описание и цифровая подпись честно идентифицируют Accessibility Compat.
- Обратные вызовы службы совместимости выполняют no-op. Они не читают `event.source`, `event.text`, `rootInActiveWindow`, снимки экрана или содержимое страницы.
- Этот выпуск не является прокси узлов или мостом Binder. Он только пытается запустить глобальную публикацию узлов в WeChat.

### Установка и использование

1. Убедитесь, что устройство использует Android 7.0 (API 24) или новее, а внутренний номер сборки AutoJs6 не ниже 3923.
2. Устанавливайте APK только из [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/releases) проекта или через доверенную точку плагинов AutoJs6.
3. Откройте Accessibility Compat, проверьте настоящее имя и назначение, затем следуйте указанию к настройкам специальных возможностей Android.
4. Вручную включите службу специальных возможностей, указанную для Accessibility Compat. Предупреждение Android о рисках является ожидаемым. Не обходите согласие через ADB.
5. Оставьте службу AutoJs6 также включенной, снова откройте целевую страницу WeChat и проверьте узлы через анализатор разметки AutoJs6 или сценарий.

Одна установка APK ничего не меняет. Отключайте службу в настройках Android, когда она не нужна, или удалите приложение после завершения работы.

### Проверка A-B-A на устройстве

Не считайте один успешный dump доказательством. Сравните A-B-A на одной неподвижной странице до того, как связывать изменение со службой:

- A: оставьте AutoJs6 включенным, а службу совместимости выключенной, затем соберите не менее 5 обезличенных образцов.
- B: дополнительно включите только службу совместимости, вернитесь на ту же страницу и соберите еще не менее 5 образцов.
- A2: снова выключите службу и повторите сбор. Изменение должно обратиться, чтобы исключить случайную загрузку или кеш.
- Записывайте число узлов, непустых text/desc/resource-id, clickable и hash структуры. Не сохраняйте переписку, имена контактов или снимки экрана.

[Открыть полный протокол проверки A-B-A](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/aba-device-validation.md)

### Результат проверки на устройстве

2026-09-02 выполнена обратимая проверка A-B-A на разрешенном Sony XQ-AT72 с Android 12/API 31, WeChat 8.0.72 code 3085 и AutoJs6 6.8.0 code 5277. Те же 6 ранее включенных служб специальных возможностей оставались без изменений. На каждом этапе WeChat принудительно останавливался, LauncherUI запускался заново, выполнялось ожидание более 5 секунд, после чего сам AutoJs6 делал 7 последовательных выборок:

- A при выключенной совместимости: nodes 1, text 0, desc 0, id 0, clickable 0, один и тот же hash структуры во всех 7 выборках.
- B при включенной совместимости и состоянии bound обеих целевых служб: nodes 244, text 31, desc 13, id 157, clickable 38, один и тот же hash структуры во всех 7 выборках.
- A2 после повторного выключения: точный возврат к nodes 1 и 0 для всех остальных метрик, один и тот же hash структуры во всех 7 выборках. После теста системные настройки специальных возможностей были точно восстановлены.

Это подтверждает работу триггера только для данной комбинации устройства, сборки WeChat и страницы LauncherUI. Результат нельзя переносить на другие версии, аккаунты, устройства, Mini Programs, XWeb или страницы Canvas.

[Открыть полный обезличенный отчет QV710AF65F](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/device-QV710AF65F.md)

### Известные ограничения

- Цель совместимости только `com.tencent.mm`. Это не общий патч специальных возможностей для других приложений.
- В WeChat Mini Program, XWeb, Canvas и элементах с собственной отрисовкой могут отсутствовать нативные семантические узлы. Плагин не может создать отсутствующие `text` или `content-desc`.
- Даже при возврате корня некоторые страницы могут давать пустые атрибуты, устаревшие узлы, случайные деревья или только координаты.
- Обновление или удаленная конфигурация WeChat могут в любой момент сделать метод неработоспособным. Понижение версии, OCR и координаты также несут риски безопасности и стабильности.
- Плагин касается только наблюдаемости. Он не обходит вход, контроль рисков, captcha, разрешения, ограничения учетной записи или системы защиты от злоупотреблений.

### Граница конфиденциальности

- Обратные вызовы не читают источник или текст события, корень активного окна или изображение экрана.
- Приложение не загружает, не хранит и не журналирует содержимое страниц WeChat, а также не содержит сетевой аналитики.
- Приложение не запрашивает доступ к хранилищу, наложениям, камере, микрофону или мультимедиа.
- Информационный Binder сообщает только метаданные версии, идентичности и возможностей. Он не передает дерево узлов.
- Пользователь явно включает и выключает службу в Android. Проект никогда не меняет эту настройку скрытно.

### Этика и соответствие требованиям

Совместимость специальных возможностей должна помогать автоматизировать только интерфейс, с которым пользователь вправе работать. Пользователь отвечает за поведение сценария и последствия для учетной записи.

- Используйте проект только на своем устройстве и учетной записи либо в явно разрешенных процессах.
- Не используйте его для преследования, spam, сбора без согласия, слежки или обхода механизмов безопасности.
- Соблюдайте закон, правила WeChat и политику организации, применяя подтверждение человеком и условия остановки при изменениях интерфейса или ошибках.
- Публикуйте только агрегированную диагностику и обезличенные структуры. Не публикуйте переписку, контакты, token, APK или необработанные dex.

### Сведения о совместимости

Компонент службы содержит совместимое имя класса, использованное в публичных экспериментах. Это не Google Select to Speak, он не выполняет озвучивание и не имитирует приложение или подпись Google. Настоящая идентичность всегда видна в ID, подписях, значке, информационной странице и цифровой подписи проекта.

```text
application id: io.github.supermonster003.autojs6.plugin.accessibilitycompat
accessibility service: io.github.supermonster003.autojs6.plugin.accessibilitycompat/com.google.android.accessibility.selecttospeak.SelectToSpeakService
target package: com.tencent.mm
minimum Android: Android 7.0 (API 24)
minimum AutoJs6 build: 3923
```

### Частые вопросы

#### Проект выдает себя за приложение Google?

Нет. Для эксперимента используется только имя класса реализации службы специальных возможностей. Пакет, подписи приложения и службы, значок, документация и цифровая подпись остаются честными, а проект прямо указывает, что это не Google Select to Speak.

#### Почему после установки ничего не изменилось?

Android не позволяет приложению самостоятельно включить службу специальных возможностей. Пользователь должен включить службу совместимости и AutoJs6 в настройках. Если изменений нет, проверьте текущую версию и страницу WeChat по протоколу A-B-A.

#### Почему в Mini Program все еще нет текстовых узлов?

Mini Program или XWeb могут использовать Canvas или собственную отрисовку без семантических узлов Android. Служба может только попытаться восстановить условно скрытое дерево. Она не создает данные, которые страница не публикует.

#### Плагин гарантирует безопасность учетной записи?

Нет. Он не обходит контроль рисков WeChat и не может гарантировать безопасность любой автоматизации. Используйте низкорисковые, проверяемые сценарии с подтверждением человеком и соблюдайте правила платформы.

### Исследовательская основа

Исследовательская заметка охватывает AutoJs6 #289, #382, #432, #463, #520, #521, GKD `47267c7` и обезличенные статические данные WeChat 8.0.72 на тестовом устройстве. Она разделяет публичные факты, воспроизводимые эксперименты и выводы, не представляя внутреннюю работу WeChat как публичную гарантию.

[Открыть исследование совместимости специальных возможностей WeChat](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/research/wechat-accessibility-compat.md)

### История выпусков

#### v1.0.0 - 2026/09/02

##### Подсказка

- Это экспериментальный метод совместимости. Результат зависит от версии, страницы и удаленной конфигурации WeChat, и выпуск не гарантирует успех для каждого устройства или аккаунта
- Служба не может создать семантические узлы, которые WeChat Mini Program, XWeb или Canvas изначально не публикуют, и не обходит вход, контроль рисков или защиту от злоупотреблений

##### Функция

- Предоставить отдельный no-op APK-компаньон специальных возможностей только для `com.tencent.mm`, с честными и отличимыми ID приложения, значком, подписями, описанием и цифровой подписью
- Зарегистрировать имя класса службы совместимости, поддержанное публичными экспериментами, при этом AutoJs6 продолжает читать и активировать узлы через собственную службу, а обратные вызовы компаньона не читают данные пользователя
- Сообщать режим совместимости, целевой пакет, компонент службы и минимальную сборку хоста через интерфейс информации плагина AutoJs6, с управляемым пользователем экраном включения и выключения

##### Улучшение

- Документировать [AutoJs6#289](https://github.com/SuperMonster003/AutoJs6/issues/289), [#382](https://github.com/SuperMonster003/AutoJs6/issues/382), [#432](https://github.com/SuperMonster003/AutoJs6/issues/432), [#463](https://github.com/SuperMonster003/AutoJs6/issues/463), [#520](https://github.com/SuperMonster003/AutoJs6/issues/520), [#521](https://github.com/SuperMonster003/AutoJs6/issues/521), GKD `47267c7` и обезличенные статические данные WeChat 8.0.72
- Предоставить протокол A-B-A без сохранения приватного текста узлов или снимков экрана, использующий повторные статистики и обратимость для отделения эффекта совместимости от загрузки и кеша
- Добавить источники README и changelog на 10 языках, воспроизводимый генератор Markdown, проверку согласованности только для чтения и барьеры GitHub Actions
- Зафиксировать проверку A-B-A из 7 выборок, выполненную самим AutoJs6 на QV710AF65F, где число узлов стабильно выросло с 1 до 244 и вернулось к 1 после отключения совместимости, а точное восстановление системных настроек специальных возможностей было подтверждено

[Открыть полную историю выпусков](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/changelog/CHANGELOG-ru.md)

### Сборка и проверка документации

Обычным пользователям следует установить готовый APK из Releases. Разработчики могут собрать и проверить проект через Gradle Wrapper репозитория:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug
py .python\generate_markdown.py --check
```

README и changelog создаются из текстов JSON. После изменения `.readme/lang_*.json`, `.changelog/lang_*.json` или шаблона выполните:

```powershell
py .python\generate_markdown.py
py .python\generate_markdown.py --check
```

### Лицензия

Код проекта распространяется по [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/LICENSE). Названия WeChat, Google и Select to Speak принадлежат их владельцам. Проект не связан с этими компаниями и не одобрен ими.

### Ссылки

- [AutoJs6](https://github.com/SuperMonster003/AutoJs6)
- [Открыть исследование совместимости специальных возможностей WeChat](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/research/wechat-accessibility-compat.md)
- [Открыть полный протокол проверки A-B-A](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/aba-device-validation.md)
- [Открыть полный обезличенный отчет QV710AF65F](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/device-QV710AF65F.md)
