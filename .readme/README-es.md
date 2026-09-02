<!-- This file is generated. Edit .readme/lang_*.json and rerun .python/generate_markdown.py. -->
<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-accessibility-compat-icon" border="0" width="128" />
  </p>
  <h1>Accessibility Compat</h1>
  <p>Un activador de compatibilidad de accesibilidad con privacidad mínima para versiones afectadas de WeChat</p>
  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/issues"><img alt="GitHub issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?color=534BAE&label=License"/></a>
  </p>
</div>

> Idioma de esta página: español

### Idiomas

[简体中文](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hans.md) | [香港繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hant-HK.md) | [台灣繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hant-TW.md) | [English](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-en.md) | [Français](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-fr.md) | [Español](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-es.md) | [日本語](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ja.md) | [한국어](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ko.md) | [Русский](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ru.md) | [العربية](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ar.md)

### Estado del proyecto

Accessibility Compat es un APK complementario, independiente y experimental. Intenta que las versiones afectadas de WeChat vuelvan a exponer su árbol de controles al servicio de accesibilidad de AutoJs6. No es un motor de automatización general y nunca sustituye a AutoJs6 como lector u operador de controles.

> La compatibilidad depende de la versión de WeChat y de su configuración remota. El proyecto no puede garantizar el resultado en cada versión, página, cuenta o dispositivo. Complete una validación A-B-A antes de usarlo en un script.

### Arquitectura complementaria no-op

El proyecto aísla el experimento de compatibilidad con WeChat en un APK separado y no cambia el nombre ni el comportamiento general del servicio principal de AutoJs6:

- El servicio de accesibilidad de AutoJs6 sigue siendo el único componente que lee, consulta y opera nodos.
- El complemento registra un nombre de clase de servicio respaldado por experimentos públicos, mientras que su ID, icono, etiquetas, descripción y firma identifican honestamente a Accessibility Compat.
- Las devoluciones del servicio de compatibilidad son no-op. Nunca leen `event.source`, `event.text`, `rootInActiveWindow`, capturas de pantalla ni contenido de la página.
- Esta versión no es un proxy de nodos ni un puente Binder. Solo intenta activar la exposición global de nodos de WeChat.

### Instalación y uso

1. Confirme que el dispositivo ejecuta Android 7.0 (API 24) o posterior y que el número de compilación interno de AutoJs6 es al menos 3923.
2. Instale el APK solo desde las [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/releases) de este proyecto o una entrada de complemento AutoJs6 de confianza.
3. Abra Accessibility Compat, compruebe su nombre y finalidad reales y siga la indicación hasta los ajustes de accesibilidad de Android.
4. Active manualmente el servicio de accesibilidad mostrado bajo Accessibility Compat. La advertencia de riesgo de Android es normal. No omita el consentimiento mediante ADB.
5. Mantenga también activo el servicio de accesibilidad de AutoJs6, vuelva a abrir la página de WeChat y lea los nodos desde el inspector de diseño de AutoJs6 o un script.

Instalar el APK por sí solo no produce ningún efecto. Desactive el servicio en los ajustes de Android cuando no lo necesite o desinstale la aplicación al terminar.

### Validación A-B-A en dispositivo

No considere una sola extracción correcta como prueba. Compare A-B-A en la misma página estática antes de atribuir un cambio al servicio:

- A: mantenga AutoJs6 activo y el servicio de compatibilidad inactivo y obtenga al menos 5 muestras desidentificadas.
- B: active solo el servicio de compatibilidad, vuelva a la misma página y obtenga al menos 5 muestras más.
- A2: vuelva a desactivar el servicio y repita la recopilación. El cambio debe revertirse para descartar carga y caché.
- Registre recuentos de nodos, campos text/desc/resource-id no vacíos, elementos clickable y un hash estructural. Nunca conserve chats, contactos ni capturas.

[Leer el protocolo completo de validación A-B-A](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/aba-device-validation.md)

### Resultado verificado en dispositivo

El 2026-09-02 se completó una prueba A-B-A reversible en el Sony XQ-AT72 autorizado con Android 12/API 31, WeChat 8.0.72 code 3085 y AutoJs6 6.8.0 code 5277. Los mismos 6 servicios de accesibilidad preexistentes se mantuvieron sin cambios. En cada fase se forzó la detención y se reinició WeChat LauncherUI, se esperaron más de 5 segundos y AutoJs6 tomó 7 muestras directamente:

- A con compatibilidad desactivada: nodes 1, text 0, desc 0, id 0, clickable 0, con el mismo hash estructural en las 7 muestras.
- B con compatibilidad activada y ambos servicios objetivo bound: nodes 244, text 31, desc 13, id 157, clickable 38, con el mismo hash estructural en las 7 muestras.
- A2 con compatibilidad desactivada otra vez: volvió exactamente a nodes 1 y 0 para todas las demás métricas, con el mismo hash estructural en las 7 muestras. La configuración de accesibilidad del sistema se restauró exactamente al finalizar.

Esto verifica el activador para esa combinación de dispositivo, versión de WeChat y página LauncherUI. No se puede generalizar a otras versiones, cuentas, dispositivos, Mini Programs, XWeb ni páginas Canvas.

[Leer el informe completo y anonimizado de QV710AF65F](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/device-QV710AF65F.md)

### Limitaciones conocidas

- El objetivo de compatibilidad es solo `com.tencent.mm`. No es un parche de accesibilidad general para otras aplicaciones.
- Los Mini Programs de WeChat, XWeb, Canvas y los controles dibujados a medida pueden carecer de nodos semánticos nativos. El complemento no puede inventar `text` ni `content-desc` ausentes.
- Aunque vuelva un nodo raíz, algunas páginas pueden exponer atributos vacíos, nodos obsoletos, árboles aleatorios o solo límites geométricos.
- Una actualización o configuración remota de WeChat puede invalidar el método en cualquier momento. Bajar de versión, OCR y coordenadas también tienen costes de seguridad y estabilidad.
- El complemento solo aborda la observabilidad. No evita inicios de sesión, controles de riesgo, captchas, permisos, restricciones de cuenta ni sistemas contra abusos.

### Límite de privacidad

- Las devoluciones de compatibilidad no leen fuentes o textos de eventos, la raíz de la ventana activa ni imágenes de pantalla.
- La aplicación no sube, almacena ni registra contenido de páginas de WeChat y no incluye funciones de red o análisis.
- La aplicación no solicita permisos de almacenamiento, superposición, cámara, micrófono ni multimedia.
- El Binder de información solo comunica metadatos de versión, identidad y capacidades. Nunca transfiere un árbol de nodos.
- El usuario activa y desactiva explícitamente el servicio en Android. El proyecto nunca cambia ese ajuste de forma silenciosa.

### Ética y cumplimiento

La compatibilidad de accesibilidad solo debe ayudar a automatizar interfaces que el usuario esté autorizado a operar. El usuario conserva la responsabilidad sobre el script y las consecuencias de la cuenta.

- Úselo solo en su propio dispositivo, cuenta y flujos con autorización explícita.
- No lo use para acoso, spam, recopilación sin consentimiento, vigilancia ni evasión de controles de seguridad.
- Cumpla la ley, las reglas de WeChat y las políticas de su organización, con confirmación humana y condiciones de parada ante cambios o errores de interfaz.
- Comparta solo estadísticas agregadas y estructuras desidentificadas. Nunca publique chats, contactos, tokens, APK ni archivos dex sin procesar.

### Información de compatibilidad

El componente de servicio contiene un nombre de clase usado en experimentos públicos. No es Google Select to Speak, no ofrece lectura de texto y no imita una aplicación ni una firma de Google. La identidad real siempre se muestra mediante el ID, las etiquetas, el icono, la página informativa y la firma del proyecto.

```text
application id: io.github.supermonster003.autojs6.plugin.accessibilitycompat
accessibility service: io.github.supermonster003.autojs6.plugin.accessibilitycompat/com.google.android.accessibility.selecttospeak.SelectToSpeakService
target package: com.tencent.mm
minimum Android: Android 7.0 (API 24)
minimum AutoJs6 build: 3923
```

### Preguntas frecuentes

#### Este proyecto suplanta una aplicación de Google?

No. Solo el nombre de clase de implementación del servicio se usa para el experimento. El paquete, las etiquetas, el icono, la documentación y la firma son veraces, y el proyecto declara que no es Google Select to Speak.

#### Por qué no cambia nada tras la instalación?

Android no permite que una aplicación active su propio servicio de accesibilidad. El usuario debe activar el servicio de compatibilidad y AutoJs6 en los ajustes. Si nada cambia, use el protocolo A-B-A para comprobar la versión y la página actuales de WeChat.

#### Por qué siguen faltando nodos de texto en un Mini Program?

Un Mini Program o una página XWeb puede dibujarse con Canvas o renderizado personalizado sin nodos semánticos Android. El servicio solo intenta restaurar un árbol oculto condicionalmente. No puede crear datos que la página nunca expone.

#### El complemento garantiza la seguridad de la cuenta?

No. No evita los controles de riesgo de WeChat ni puede prometer que una automatización sea segura. Use scripts de bajo riesgo, auditables y con confirmación humana y respete las reglas de la plataforma.

### Base de investigación

La nota de investigación cubre AutoJs6 #289, #382, #432, #463, #520 y #521, GKD `47267c7` y evidencia estática desidentificada de WeChat 8.0.72 en el dispositivo de prueba. Separa hechos públicos, experimentos reproducibles e inferencias sin presentar el funcionamiento interno de WeChat como garantía pública.

[Leer la nota de compatibilidad de accesibilidad de WeChat](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/research/wechat-accessibility-compat.md)

### Historial de versiones

#### v1.0.0 - 2026/09/02

##### Aviso

- Este enfoque de compatibilidad es experimental. El resultado depende de la versión, la página y la configuración remota de WeChat, y una versión no puede garantizar todos los dispositivos o cuentas
- El servicio no puede crear nodos semánticos que un Mini Program de WeChat, XWeb o Canvas nunca expuso, ni evita el inicio de sesión, los controles de riesgo o los sistemas contra abusos

##### Función

- Proporcionar un APK complementario de accesibilidad no-op, independiente y limitado a `com.tencent.mm`, con ID, icono, etiquetas, descripción y firma veraces y distintos
- Registrar un nombre de clase de servicio de compatibilidad respaldado por experimentos públicos mientras AutoJs6 sigue leyendo y operando nodos con su propio servicio y las devoluciones del complemento no leen contenido del usuario
- Comunicar el modo de compatibilidad, paquete objetivo, componente de servicio y compilación mínima del host mediante la interfaz de información de AutoJs6, con una pantalla controlada por el usuario para activar y desactivar el servicio

##### Mejora

- Documentar [AutoJs6#289](https://github.com/SuperMonster003/AutoJs6/issues/289), [#382](https://github.com/SuperMonster003/AutoJs6/issues/382), [#432](https://github.com/SuperMonster003/AutoJs6/issues/432), [#463](https://github.com/SuperMonster003/AutoJs6/issues/463), [#520](https://github.com/SuperMonster003/AutoJs6/issues/520), [#521](https://github.com/SuperMonster003/AutoJs6/issues/521), GKD `47267c7` y evidencia estática desidentificada de WeChat 8.0.72
- Proporcionar un protocolo A-B-A que no guarda texto privado de nodos ni capturas y usa estadísticas repetidas y reversibilidad para distinguir el efecto de compatibilidad de la carga y la caché
- Añadir fuentes de README y changelog en 10 idiomas, un generador Markdown reproducible, una comprobación de coherencia de solo lectura y controles de GitHub Actions
- Documentar una prueba A-B-A de 7 muestras realizada por AutoJs6 en QV710AF65F, donde el número de nodos subió de forma estable de 1 a 244 y volvió a 1 al desactivar la compatibilidad, verificando además la restauración exacta de la configuración de accesibilidad del sistema

[Leer el historial completo](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/changelog/CHANGELOG-es.md)

### Compilación y comprobación documental

Los usuarios normales deben instalar el APK precompilado de Releases. Los desarrolladores pueden compilar y verificar el proyecto con el Gradle Wrapper del repositorio:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug
py .python\generate_markdown.py --check
```

Los README y changelog se generan desde textos JSON. Tras cambiar `.readme/lang_*.json`, `.changelog/lang_*.json` o una plantilla, ejecute:

```powershell
py .python\generate_markdown.py
py .python\generate_markdown.py --check
```

### Licencia

El código del proyecto usa la [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/LICENSE). Los nombres WeChat, Google y Select to Speak pertenecen a sus propietarios. El proyecto no está afiliado ni respaldado por esas empresas.

### Enlaces

- [AutoJs6](https://github.com/SuperMonster003/AutoJs6)
- [Leer la nota de compatibilidad de accesibilidad de WeChat](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/research/wechat-accessibility-compat.md)
- [Leer el protocolo completo de validación A-B-A](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/aba-device-validation.md)
- [Leer el informe completo y anonimizado de QV710AF65F](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/device-QV710AF65F.md)
