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
  <p>Un activador de compatibilidad de accesibilidad con privacidad mínima para aplicaciones compatibles</p>
  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/issues"><img alt="GitHub issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y?color=534BAE&label=License"/></a>
  </p>
</div>

> Idioma de esta página: español

### Idiomas

[简体中文](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-zh-Hans.md) | [香港繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-zh-Hant-HK.md) | [台灣繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-zh-Hant-TW.md) | [English](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-en.md) | [Français](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-fr.md) | [Español](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-es.md) | [日本語](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ja.md) | [한국어](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ko.md) | [Русский](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ru.md) | [العربية](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ar.md)

### Estado del proyecto

3-Adapt A11y es un APK complementario, independiente y experimental. Intenta ayudar a que las aplicaciones cubiertas por perfiles de soporte publicados expongan sus árboles de controles al servicio de accesibilidad de AutoJs6. No es un motor de automatización general y nunca sustituye a AutoJs6 como lector u operador de controles.

> La compatibilidad depende de la versión, la página, el dispositivo y la configuración remota de cada aplicación objetivo. El proyecto no puede garantizar el resultado en todos los entornos. Complete una validación A-B-A antes de usarlo en un script.

### Arquitectura complementaria no-op

El proyecto aísla los perfiles de compatibilidad de aplicaciones en un APK separado y no cambia el nombre ni el comportamiento general del servicio principal de AutoJs6:

- El servicio de accesibilidad de AutoJs6 sigue siendo el único componente que lee, consulta y opera nodos.
- El complemento registra un nombre de clase de servicio respaldado por experimentos públicos, mientras que su ID, icono, etiquetas, descripción y firma identifican honestamente a 3-Adapt A11y.
- Las devoluciones del servicio de compatibilidad son no-op. Nunca leen `event.source`, `event.text`, `rootInActiveWindow`, capturas de pantalla ni contenido de la página.
- Esta versión no es un proxy de nodos ni un puente Binder. Solo intenta activar la exposición condicional de nodos documentada por sus perfiles de soporte.

### Instalación y uso

1. Confirme que el dispositivo ejecuta Android 7.0 (API 24) o posterior y que el número de compilación interno de AutoJs6 es al menos 3923.
2. Instale el APK solo desde las [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/releases) de este proyecto o una entrada de complemento AutoJs6 de confianza.
3. Abra 3-Adapt A11y, compruebe su nombre y finalidad reales y toque la tarjeta de estado para abrir el gestor del servicio de compatibilidad. La política predeterminada, seguir a AutoJs6, mantiene el servicio habilitado solo mientras el servicio de accesibilidad de AutoJs6 esté habilitado.
4. Sin root, WRITE_SECURE_SETTINGS ni Shizuku, active manualmente el servicio de accesibilidad mostrado bajo 3-Adapt A11y en los ajustes de Android; la advertencia de riesgo de Android es normal. Si concede uno de ellos a propósito, la aplicación aplica por sí misma la política seleccionada y no toca ningún otro ajuste.
5. Mantenga también activo el servicio de accesibilidad de AutoJs6, vuelva a abrir una página objetivo indicada en un perfil de soporte y lea los nodos desde el inspector de diseño de AutoJs6 o un script.

Instalar el APK por sí solo no produce ningún efecto. Elija la política deshabilitado o desactive el servicio en los ajustes de Android cuando no lo necesite, o desinstale la aplicación al terminar.

### Ajustes y control del servicio

La pantalla de ajustes agrupa opciones generales que pueden seguir a AutoJs6, y el gestor del servicio de compatibilidad decide cuándo se ejecuta el servicio:

- El idioma, el modo oscuro y el color del tema siguen a AutoJs6 de forma predeterminada y se leen del contrato de ajustes de AutoJs6; sin AutoJs6 vuelven a los valores del sistema o integrados.
- La política de control tiene tres opciones: seguir a AutoJs6 (predeterminada), habilitado o deshabilitado. Seguir a AutoJs6 mantiene el servicio de compatibilidad en el mismo estado que el servicio de accesibilidad de AutoJs6 y lo vuelve a comprobar cuando AutoJs6 notifica un cambio, cuando cambia la lista de servicios habilitados del sistema y al abrir la aplicación.
- Los cambios automáticos usan root, WRITE_SECURE_SETTINGS (concedido con `adb shell pm grant`) o Shizuku, y cada método puede desactivarse. Sin ninguno de ellos, la aplicación solo abre los ajustes de accesibilidad del sistema.
- El gestor muestra el estado en vivo de ambos servicios y de cada método, copia un informe de diagnóstico y abre la página de ajustes de Android del servicio.

### Validación A-B-A en dispositivo

No considere una sola extracción correcta como prueba. Compare A-B-A en la misma página estática antes de atribuir un cambio al servicio:

- A: mantenga AutoJs6 activo y el servicio de compatibilidad inactivo y obtenga al menos 5 muestras desidentificadas.
- B: active solo el servicio de compatibilidad, vuelva a la misma página y obtenga al menos 5 muestras más.
- A2: vuelva a desactivar el servicio y repita la recopilación. El cambio debe revertirse para descartar carga y caché.
- Registre recuentos de nodos, campos text/desc/resource-id no vacíos, elementos clickable y un hash estructural. Nunca conserve chats, contactos ni capturas.

[Leer el protocolo completo de validación A-B-A](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/aba-device-validation.md)

### Resultado verificado en dispositivo

El 2026-09-02 se completó una prueba A-B-A reversible en el Sony XQ-AT72 autorizado con Android 12/API 31, WeChat 8.0.72 code 3085 y AutoJs6 6.8.0 code 5277. Los mismos 6 servicios de accesibilidad preexistentes se mantuvieron sin cambios. En cada fase se forzó la detención y se reinició WeChat LauncherUI, se esperaron más de 5 segundos y AutoJs6 tomó 7 muestras directamente:

- A con compatibilidad desactivada: nodes 1, text 0, desc 0, id 0, clickable 0, con el mismo hash estructural en las 7 muestras.
- B con compatibilidad activada y ambos servicios objetivo bound: nodes 244, text 31, desc 13, id 157, clickable 38, con el mismo hash estructural en las 7 muestras.
- A2 con compatibilidad desactivada otra vez: volvió exactamente a nodes 1 y 0 para todas las demás métricas, con el mismo hash estructural en las 7 muestras. La configuración de accesibilidad del sistema se restauró exactamente al finalizar.

Esto verifica el activador para esa combinación de dispositivo, versión de WeChat y página LauncherUI. No se puede generalizar a otras versiones, cuentas, dispositivos, Mini Programs, XWeb ni páginas Canvas.

[Leer el informe completo y anonimizado de QV710AF65F](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/device-QV710AF65F.md)

### Limitaciones conocidas

- Los perfiles actuales solo contienen WeChat (`com.tencent.mm`). Es un conjunto de soporte explícito y no una afirmación de compatibilidad universal.
- WebView, miniaplicaciones, Canvas y los controles dibujados a medida pueden carecer de nodos semánticos nativos. El complemento no puede inventar `text` ni `content-desc` ausentes.
- Aunque vuelva un nodo raíz, algunas páginas pueden exponer atributos vacíos, nodos obsoletos, árboles aleatorios o solo límites geométricos.
- Una actualización o configuración remota de la aplicación objetivo puede invalidar un perfil en cualquier momento. Bajar de versión, OCR y coordenadas también tienen costes de seguridad y estabilidad.
- El complemento solo aborda la observabilidad. No evita inicios de sesión, controles de riesgo, captchas, permisos, restricciones de cuenta ni sistemas contra abusos.
- La protección avanzada de Android 17 puede restringir los servicios que no son herramientas de accesibilidad, incluido este servicio de compatibilidad. El modo global por sí solo no indica si el servicio está bloqueado. Comprueba su estado real y los ajustes de accesibilidad de Android. El permiso de ajustes seguros no elude las restricciones del sistema.

### Límite de privacidad

- Las devoluciones de compatibilidad no leen fuentes o textos de eventos, la raíz de la ventana activa ni imágenes de pantalla.
- La aplicación no sube, almacena ni registra contenido de páginas de aplicaciones objetivo y no incluye funciones de red o análisis.
- La aplicación no solicita permisos de almacenamiento, superposición, cámara, micrófono ni multimedia. Declara WRITE_SECURE_SETTINGS y el permiso de Shizuku solo para el control opcional del servicio, y ambos permanecen inactivos hasta que usted los conceda.
- El Binder de información solo comunica metadatos de versión, identidad y capacidades. Nunca transfiere un árbol de nodos.
- El servicio de compatibilidad solo cambia según la política de control que usted eligió, mediante root, ajustes seguros o Shizuku cuando los permitió. La aplicación nunca modifica otro ajuste del sistema ni habilita el servicio de AutoJs6.

### Ética y cumplimiento

La compatibilidad de accesibilidad solo debe ayudar a automatizar interfaces que el usuario esté autorizado a operar. El usuario conserva la responsabilidad sobre el script y las consecuencias de la cuenta.

- Úselo solo en su propio dispositivo, cuenta y flujos con autorización explícita.
- No lo use para acoso, spam, recopilación sin consentimiento, vigilancia ni evasión de controles de seguridad.
- Cumpla la ley, las reglas de la plataforma objetivo y las políticas de su organización, con confirmación humana y condiciones de parada ante cambios o errores de interfaz.
- Comparta solo estadísticas agregadas y estructuras desidentificadas. Nunca publique chats, contactos, tokens, APK ni archivos dex sin procesar.

### Información de compatibilidad

El componente de servicio contiene un nombre de clase usado en experimentos públicos. No es Google Select to Speak, no ofrece lectura de texto y no imita una aplicación ni una firma de Google. La identidad real siempre se muestra mediante el ID, las etiquetas, el icono, la página informativa y la firma del proyecto.

```text
application id: io.github.supermonster003.autojs6.plugin.three.adapt.a11y
accessibility service: io.github.supermonster003.autojs6.plugin.three.adapt.a11y/com.google.android.accessibility.selecttospeak.SelectToSpeakService
supported packages: com.tencent.mm
minimum Android: Android 7.0 (API 24)
minimum AutoJs6 build: 3923
```

### Preguntas frecuentes

#### Este proyecto suplanta una aplicación de Google?

No. Solo el nombre de clase de implementación del servicio se usa para el experimento. El paquete, las etiquetas, el icono, la documentación y la firma son veraces, y el proyecto declara que no es Google Select to Speak.

#### Por qué no cambia nada tras la instalación?

Sin root, WRITE_SECURE_SETTINGS ni Shizuku, Android no permite que una aplicación active su propio servicio de accesibilidad, así que active el servicio de compatibilidad y AutoJs6 en los ajustes. Si uno de ellos está disponible, el gestor del servicio de compatibilidad aplica la política seleccionada por usted. Si nada cambia, use el protocolo A-B-A para comprobar la versión y la página actuales de la aplicación objetivo.

#### Por qué siguen faltando nodos de texto en una página compatible?

Una página WebView, una miniaplicación, Canvas o un renderizado personalizado puede carecer de nodos semánticos Android. El servicio solo intenta restaurar un árbol oculto condicionalmente. No puede crear datos que la página nunca expone.

#### El complemento garantiza la seguridad de la cuenta?

No. No evita los controles de riesgo de la plataforma objetivo ni puede prometer que una automatización sea segura. Use scripts de bajo riesgo, auditables y con confirmación humana y respete las reglas de la plataforma.

### Base de investigación

La nota de investigación cubre AutoJs6 #289, #382, #432, #463, #520 y #521, GKD `47267c7` y evidencia estática desidentificada de WeChat 8.0.72 en el dispositivo de prueba. Separa hechos públicos, experimentos reproducibles e inferencias sin presentar el funcionamiento interno de WeChat como garantía pública.

[Leer la nota sobre compatibilidad de identidad de servicios de accesibilidad](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/research/accessibility-service-identity-compat.md)

### Historial de versiones

#### v1.4.0 - 2026/09/29

##### Aviso

- El ID de aplicación cambia de `io.github.supermonster003.autojs6.plugin.accessibilitycompat` a `io.github.supermonster003.autojs6.plugin.three.adapt.a11y`, por lo que Android trata esta versión como una aplicación nueva: desinstala primero Accessibility Compat 1.3.1 o anterior y vuelve a habilitar el servicio de accesibilidad 3-Adapt A11y

##### Mejora

- El complemento Accessibility Compat pasa a llamarse 3-Adapt A11y en el título de la aplicación, la etiqueta del servicio de accesibilidad, el ID de complemento `three-adapt-a11y`, los nombres de paquete y componentes, los artefactos de publicación, la documentación y el repositorio de GitHub
- Unificar los iconos del lanzador de la serie Three con dibujos claros sobre un fondo oscuro fijo, mantener transparentes los del centro de complementos y de la aplicación según su tema y evitar fondos superpuestos en algunos dispositivos

[Leer el historial completo](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/changelog/CHANGELOG-es.md)

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

El código del proyecto usa la [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/LICENSE). Los nombres WeChat, Google y Select to Speak pertenecen a sus propietarios. El proyecto no está afiliado ni respaldado por esas empresas.

### Enlaces

- [AutoJs6](https://github.com/SuperMonster003/AutoJs6)
- [Leer la nota sobre compatibilidad de identidad de servicios de accesibilidad](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/research/accessibility-service-identity-compat.md)
- [Leer el protocolo completo de validación A-B-A](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/aba-device-validation.md)
- [Leer el informe completo y anonimizado de QV710AF65F](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/device-QV710AF65F.md)


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/16kb.md)
