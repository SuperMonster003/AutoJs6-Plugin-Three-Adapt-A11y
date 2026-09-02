<!-- This file is generated. Edit .readme/lang_*.json and rerun .python/generate_markdown.py. -->
<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-accessibility-compat-icon" border="0" width="128" />
  </p>
  <h1>Accessibility Compat</h1>
  <p>مشغل توافق لإمكانية الوصول بأقل قدر من التعامل مع الخصوصية لإصدارات WeChat المتأثرة</p>
  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/issues"><img alt="GitHub issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?color=534BAE&label=License"/></a>
  </p>
</div>

> لغة هذه الصفحة: العربية

### اللغات

[简体中文](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hans.md) | [香港繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hant-HK.md) | [台灣繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hant-TW.md) | [English](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-en.md) | [Français](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-fr.md) | [Español](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-es.md) | [日本語](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ja.md) | [한국어](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ko.md) | [Русский](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ru.md) | [العربية](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ar.md)

### حالة المشروع

Accessibility Compat هو APK مرافق مستقل وتجريبي. يحاول جعل إصدارات WeChat المتأثرة تعرض شجرة عناصر التحكم مجددا لخدمة إمكانية الوصول في AutoJs6. وهو ليس محرك أتمتة عاما ولا يحل محل AutoJs6 في قراءة العناصر أو تشغيلها.

> يعتمد التوافق على إصدار WeChat وإعداده البعيد. لا يضمن المشروع النجاح مع كل إصدار أو صفحة أو حساب أو جهاز. أكمل تحقق A-B-A قبل الاعتماد عليه في أي سكربت.

### بنية المرافق no-op

يعزل المشروع تجربة توافق WeChat في APK منفصل ولا يغير اسم خدمة AutoJs6 الأساسية أو سلوكها العام:

- تبقى خدمة إمكانية الوصول في AutoJs6 المكون الوحيد الذي يقرأ العقد ويبحث فيها وينفذ الإجراءات عليها.
- يسجل المرافق اسم فئة تنفيذ لخدمة تدعمه تجارب علنية, بينما تعرف هوية التطبيق والأيقونة والتسميات والوصف والتوقيع المشروع بصدق على أنه Accessibility Compat.
- استدعاءات خدمة التوافق هي no-op. ولا تقرأ `event.source` أو `event.text` أو `rootInActiveWindow` أو لقطات الشاشة أو محتوى الصفحة.
- هذا الإصدار ليس وسيط عقد ولا جسر Binder. إنه يحاول فقط تشغيل سلوك العرض العام للعقد في WeChat.

### التثبيت والاستخدام

1. تأكد من أن الجهاز يعمل بنظام Android 7.0 (API 24) أو أحدث وأن رقم بناء AutoJs6 الداخلي لا يقل عن 3923.
2. ثبت APK فقط من [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/releases) لهذا المشروع أو من مدخل موثوق لمكونات AutoJs6 الإضافية.
3. افتح Accessibility Compat وتحقق من اسمه الحقيقي وغرضه, ثم اتبع الإرشاد إلى إعدادات إمكانية الوصول في Android.
4. فعل يدويا خدمة إمكانية الوصول المعروضة تحت Accessibility Compat. تحذير المخاطر في Android متوقع. لا تتجاوز الموافقة باستخدام ADB.
5. أبق خدمة إمكانية الوصول في AutoJs6 مفعلة أيضا, وأعد فتح صفحة WeChat المستهدفة, ثم افحص العقد من محلل التخطيط في AutoJs6 أو من سكربت.

تثبيت APK وحده لا يحدث أثرا. عطل خدمة التوافق في إعدادات Android عند عدم الحاجة إليها, أو أزل التطبيق بعد الانتهاء.

### تحقق A-B-A على الجهاز

لا تعتبر نجاح dump واحد دليلا. نفذ مقارنة A-B-A على الصفحة الساكنة نفسها قبل نسبة التغيير إلى الخدمة:

- A: أبق AutoJs6 مفعلا وخدمة التوافق معطلة, ثم اجمع 5 عينات منزوعة الهوية على الأقل.
- B: فعل خدمة التوافق فقط بالإضافة إلى AutoJs6, وعد إلى الصفحة نفسها, واجمع 5 عينات أخرى على الأقل.
- A2: عطل خدمة التوافق مرة أخرى وكرر الجمع. يجب أن ينعكس التغيير لاستبعاد أثر التحميل أو الذاكرة المؤقتة.
- سجل عدد العقد وحقول text/desc/resource-id غير الفارغة وعدد clickable وhash للبنية. لا تحتفظ بالمحادثات أو أسماء جهات الاتصال أو لقطات الشاشة.

[قراءة بروتوكول تحقق A-B-A الكامل](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/aba-device-validation.md)

### نتيجة موثقة على جهاز فعلي

في 2026-09-02 اكتمل اختبار A-B-A قابل للعكس على جهاز Sony XQ-AT72 المصرح به مع Android 12/API 31 وWeChat 8.0.72 code 3085 وAutoJs6 6.8.0 code 5277. بقيت خدمات إمكانية الوصول الست الموجودة مسبقا بلا تغيير. في كل مرحلة تم إيقاف WeChat قسريا وإعادة تشغيل LauncherUI والانتظار أكثر من 5 ثوان ثم أخذ 7 عينات متتالية من AutoJs6 نفسه:

- A مع إيقاف التوافق: nodes 1, text 0, desc 0, id 0, clickable 0, مع hash بنية واحد في العينات السبع.
- B مع تشغيل التوافق وكون الخدمتين المستهدفتين bound: nodes 244, text 31, desc 13, id 157, clickable 38, مع hash بنية واحد في العينات السبع.
- A2 بعد إيقاف التوافق مجددا: عادت القيم بدقة إلى nodes 1 وإلى 0 لكل المقاييس الأخرى, مع hash بنية واحد في العينات السبع. تمت استعادة إعدادات إمكانية الوصول للنظام بدقة بعد الاختبار.

تثبت هذه النتيجة المشغل لهذه المجموعة فقط من الجهاز وإصدار WeChat وصفحة LauncherUI. لا يمكن تعميمها على إصدارات أو حسابات أو أجهزة أخرى أو Mini Programs أو XWeb أو صفحات Canvas.

[قراءة تقرير QV710AF65F الكامل والمنزوع الهوية](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/device-QV710AF65F.md)

### القيود المعروفة

- هدف التوافق هو `com.tencent.mm` فقط. هذا ليس تصحيحا عاما لإمكانية الوصول في التطبيقات الأخرى.
- قد لا تحتوي WeChat Mini Programs وXWeb وCanvas والعناصر المرسومة يدويا على عقد دلالية أصلية. لا يستطيع المكون اختراع `text` أو `content-desc` مفقود.
- حتى عند عودة الجذر قد تعرض بعض الصفحات خصائص فارغة أو عقدا قديمة أو أشجارا عشوائية أو حدودا هندسية فقط.
- قد يبطل تحديث WeChat أو تغير الإعداد البعيد هذه الطريقة في أي وقت. كما أن خفض الإصدار وOCR والإحداثيات لها تكاليف أمن وثبات خاصة بها.
- يعالج المكون قابلية الملاحظة فقط. ولا يتجاوز تسجيل الدخول أو ضوابط المخاطر أو captcha أو الأذونات أو قيود الحساب أو أنظمة مكافحة الإساءة.

### حدود الخصوصية

- لا تقرأ استدعاءات التوافق مصدر الحدث أو نصه أو جذر النافذة النشطة أو صورة الشاشة.
- لا يرفع التطبيق محتوى صفحات WeChat ولا يخزنه أو يسجله, ولا يتضمن وظيفة شبكة أو تحليلات.
- لا يطلب التطبيق أذونات التخزين أو الطبقات العائمة أو الكاميرا أو الميكروفون أو الوسائط.
- يبلغ Binder الخاص بمعلومات المكون عن بيانات الإصدار والهوية والقدرات فقط. ولا ينقل شجرة عقد.
- يفعل المستخدم الخدمة ويعطلها صراحة في إعدادات Android. ولا يغير المشروع هذا الإعداد خفية.

### الأخلاق والامتثال

يجب أن يساعد توافق إمكانية الوصول المستخدم فقط في أتمتة واجهات يملك صلاحية تشغيلها. ويبقى المستخدم مسؤولا عن سلوك السكربت وعواقب الحساب.

- استخدمه فقط على جهازك وحسابك وفي مسارات العمل المصرح بها صراحة.
- لا تستخدمه للمضايقة أو spam أو جمع البيانات من دون موافقة أو مراقبة الآخرين أو تجاوز ضوابط الأمان.
- التزم بالقانون وقواعد WeChat وسياسة المؤسسة, مع تأكيد بشري وشروط توقف عند تغير الواجهة أو وقوع خطأ.
- شارك إحصاءات مجمعة وبنى منزوعة الهوية فقط. لا تنشر المحادثات أو جهات الاتصال أو token أو APK أو ملفات dex الخام.

### معلومات التوافق

يتضمن مكون الخدمة اسم فئة توافق مستخدما في تجارب علنية. وهو ليس Google Select to Speak ولا يقدم قراءة نصية ولا ينتحل تطبيق Google أو توقيعه. تبقى الهوية الحقيقية ظاهرة في ID والتسميات والأيقونة وصفحة المعلومات وتوقيع المشروع.

```text
application id: io.github.supermonster003.autojs6.plugin.accessibilitycompat
accessibility service: io.github.supermonster003.autojs6.plugin.accessibilitycompat/com.google.android.accessibility.selecttospeak.SelectToSpeakService
target package: com.tencent.mm
minimum Android: Android 7.0 (API 24)
minimum AutoJs6 build: 3923
```

### الأسئلة المتكررة

#### هل ينتحل المشروع تطبيق Google?

لا. يستخدم اسم فئة تنفيذ خدمة إمكانية الوصول فقط في تجربة التوافق. تبقى الحزمة وتسميات التطبيق والخدمة والأيقونة والتوثيق والتوقيع صادقة, ويصرح المشروع بأنه ليس Google Select to Speak.

#### لماذا لم يتغير شيء بعد التثبيت?

لا يسمح Android للتطبيق بتفعيل خدمة إمكانية الوصول الخاصة به بنفسه. يجب أن يفعل المستخدم خدمة التوافق وAutoJs6 في الإعدادات. إذا لم يحدث تغيير فاستخدم بروتوكول A-B-A لفحص إصدار WeChat والصفحة الحاليين.

#### لماذا ما زالت عقد النص مفقودة في Mini Program?

قد تستخدم Mini Program أو صفحة XWeb الرسم عبر Canvas أو تصييرا خاصا من دون عقد دلالية في Android. تستطيع الخدمة فقط محاولة استعادة شجرة مخفية شرطيا, ولا يمكنها إنشاء معلومات لا تعرضها الصفحة أصلا.

#### هل يضمن المكون أمان الحساب?

لا. لا يتجاوز ضوابط مخاطر WeChat ولا يمكنه ضمان سلامة أي أتمتة. استخدم سكربتات منخفضة المخاطر وقابلة للتدقيق وبها تأكيد بشري, والتزم بقواعد المنصة.

### أساس البحث

تغطي مذكرة البحث AutoJs6 #289 و#382 و#432 و#463 و#520 و#521 وGKD `47267c7` وأدلة ثابتة منزوعة الهوية من WeChat 8.0.72 على جهاز الاختبار. وهي تفصل الحقائق العامة والتجارب القابلة للتكرار والاستنتاجات ولا تقدم آلية WeChat الداخلية كضمان عام.

[قراءة بحث توافق إمكانية الوصول في WeChat](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/research/wechat-accessibility-compat.md)

### سجل الإصدارات

#### v1.0.0 - 2026/09/02

##### تلميح

- هذه طريقة توافق تجريبية. تعتمد النتيجة على إصدار WeChat والصفحة والإعداد البعيد, ولا يضمن أي إصدار النجاح مع كل جهاز أو حساب
- لا تستطيع الخدمة إنشاء عقد دلالية لم تعرضها WeChat Mini Program أو XWeb أو Canvas أصلا, ولا تتجاوز تسجيل الدخول أو ضوابط المخاطر أو أنظمة مكافحة الإساءة

##### ميزة

- توفير APK مرافق مستقل لإمكانية الوصول من نوع no-op ومقيد بـ `com.tencent.mm`, مع ID وأيقونة وتسميات ووصف وتوقيع حقيقية ومميزة
- تسجيل اسم فئة تنفيذ لخدمة توافق تدعمه تجارب علنية, مع استمرار AutoJs6 في قراءة العقد وتشغيلها عبر خدمته وعدم قراءة استدعاءات المرافق لأي محتوى للمستخدم
- الإبلاغ عن نمط التوافق والحزمة المستهدفة ومكون الخدمة والحد الأدنى لبناء المضيف عبر واجهة معلومات مكون AutoJs6, مع شاشة يتحكم بها المستخدم لتفعيل الخدمة وتعطيلها

##### تحسين

- توثيق [AutoJs6#289](https://github.com/SuperMonster003/AutoJs6/issues/289) و[#382](https://github.com/SuperMonster003/AutoJs6/issues/382) و[#432](https://github.com/SuperMonster003/AutoJs6/issues/432) و[#463](https://github.com/SuperMonster003/AutoJs6/issues/463) و[#520](https://github.com/SuperMonster003/AutoJs6/issues/520) و[#521](https://github.com/SuperMonster003/AutoJs6/issues/521) وGKD `47267c7` والأدلة الثابتة منزوعة الهوية من WeChat 8.0.72
- توفير بروتوكول A-B-A لا يحفظ نصوص العقد الخاصة أو لقطات الشاشة ويستخدم إحصاءات متكررة وقابلية العكس لتمييز أثر التوافق من مصادفات التحميل والذاكرة المؤقتة
- إضافة مصادر README وchangelog في 10 لغات ومولد Markdown قابل للتكرار وفحص اتساق للقراءة فقط وبوابات GitHub Actions
- توثيق اختبار A-B-A من 7 عينات أجراه AutoJs6 نفسه على QV710AF65F, حيث ارتفع عدد العقد بثبات من 1 إلى 244 ثم عاد إلى 1 بعد إيقاف التوافق, مع التحقق من الاستعادة الدقيقة لإعدادات إمكانية الوصول للنظام

[قراءة سجل الإصدارات الكامل](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/changelog/CHANGELOG-ar.md)

### البناء وفحص التوثيق

ينبغي للمستخدم العادي تثبيت APK الجاهز من Releases. ويمكن للمطور بناء المشروع والتحقق منه باستخدام Gradle Wrapper في المستودع:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug
py .python\generate_markdown.py --check
```

يتم توليد README وchangelog من نصوص JSON. بعد تغيير `.readme/lang_*.json` أو `.changelog/lang_*.json` أو قالب, شغل:

```powershell
py .python\generate_markdown.py
py .python\generate_markdown.py --check
```

### الترخيص

كود المشروع مرخص وفق [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/LICENSE). تعود أسماء WeChat وGoogle وSelect to Speak إلى مالكيها. هذا المشروع غير تابع لتلك الشركات ولا معتمد منها.

### الروابط

- [AutoJs6](https://github.com/SuperMonster003/AutoJs6)
- [قراءة بحث توافق إمكانية الوصول في WeChat](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/research/wechat-accessibility-compat.md)
- [قراءة بروتوكول تحقق A-B-A الكامل](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/aba-device-validation.md)
- [قراءة تقرير QV710AF65F الكامل والمنزوع الهوية](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/device-QV710AF65F.md)
