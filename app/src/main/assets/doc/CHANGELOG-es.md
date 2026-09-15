<!-- This file is generated. Edit .changelog/lang_*.json and rerun .python/generate_markdown.py. -->

# Historial de versiones de Accessibility Compat

> Idioma de esta página: español

## v1.3.0 - 2026/09/15

### Función

- Se añade una pantalla de ajustes con opciones de idioma, modo oscuro y color del tema que siguen a AutoJs6 de forma predeterminada, junto con las entradas de mecanismo, privacidad, limitaciones, historial de versiones y acerca de
- Se añade un gestor del servicio de compatibilidad con una política de tres opciones (seguir a AutoJs6, habilitado, deshabilitado) que puede cambiar el servicio mediante root, WRITE_SECURE_SETTINGS o Shizuku, con estado en vivo y un informe copiable
- El servicio sigue automáticamente al servicio de accesibilidad de AutoJs6 mediante una difusión de estado del anfitrión, una tarea activada por la lista de servicios habilitados y comprobaciones al conectar el servicio y al abrir la aplicación
- La política de servicio seleccionada se publica mediante las capacidades de plugin-info y el contrato compartido de complemento de accesibilidad (versión de contrato 3)

### Mejora

- Rediseño de la pantalla principal: la tarjeta de estado abre el gestor, las aplicaciones compatibles tienen botón de inicio y los antiguos botones de abrir aplicación, ajustes de accesibilidad y actualizar pasan a la pantalla de ajustes
- Compilación contra la API 37 de Android para coincidir con la API común de complementos actualizada

### Dependencia

- Se añaden Shizuku API 13.1.5 y AndroidX Annotation 1.10.0
- Se actualiza la API común de complementos incluida para incorporar el contrato de complemento de accesibilidad

## v1.2.0 - 2026/09/13

### Función

- Historial de versiones local desde la interfaz con traducciones y alternativa en inglés

### Mejora

- Comprobación de la firma completa, los APK esperados y la documentación reproducible de cada versión

## v1.1.0 - 2026/09/12

### Mejora

- Generalizar la aplicación, el servicio de accesibilidad, los metadatos del complemento, las instrucciones integradas y el README mediante perfiles de soporte independientes de la aplicación, manteniendo WeChat (`com.tencent.mm`) como único perfil verificado actualmente
- Sustituir la variante específica por `service-identity`, publicar los paquetes compatibles como una colección y listar o abrir aplicaciones compatibles instaladas mediante acciones genéricas de la interfaz
- Actualizar los iconos adaptativos del lanzador con variantes claras y oscuras y una capa monocroma para iconos temáticos
- Generalizar el protocolo A-B-A, la entrada de investigación y la herramienta de métricas respetuosa con la privacidad, incluida una entrada `targetPackage` explícita para futuros perfiles
- Eliminar las propiedades obsoletas de versión mínima de Android Studio e IntelliJ IDEA ahora que la compatibilidad del IDE y la cadena de herramientas se selecciona de forma centralizada
- La verificación de compilación rechaza dependencias nativas accidentales y genera un informe JSON

### Dependencia

- Actualizar `io.github.supermonster003.autojs6-platform-versions` de 1.7.0 a 1.7.3 para que las compilaciones con JDK 25 y 26 alineen automáticamente el KGP seleccionado en el classpath del buildscript raíz

## v1.0.0 - 2026/09/02

### Aviso

- Este enfoque de compatibilidad es experimental. El resultado depende de la versión, la página y la configuración remota de WeChat, y una versión no puede garantizar todos los dispositivos o cuentas
- El servicio no puede crear nodos semánticos que un Mini Program de WeChat, XWeb o Canvas nunca expuso, ni evita el inicio de sesión, los controles de riesgo o los sistemas contra abusos

### Función

- Proporcionar un APK complementario de accesibilidad no-op, independiente y limitado a `com.tencent.mm`, con ID, icono, etiquetas, descripción y firma veraces y distintos
- Registrar un nombre de clase de servicio de compatibilidad respaldado por experimentos públicos mientras AutoJs6 sigue leyendo y operando nodos con su propio servicio y las devoluciones del complemento no leen contenido del usuario
- Comunicar el modo de compatibilidad, paquete objetivo, componente de servicio y compilación mínima del host mediante la interfaz de información de AutoJs6, con una pantalla controlada por el usuario para activar y desactivar el servicio

### Mejora

- Documentar [AutoJs6#289](https://github.com/SuperMonster003/AutoJs6/issues/289), [#382](https://github.com/SuperMonster003/AutoJs6/issues/382), [#432](https://github.com/SuperMonster003/AutoJs6/issues/432), [#463](https://github.com/SuperMonster003/AutoJs6/issues/463), [#520](https://github.com/SuperMonster003/AutoJs6/issues/520), [#521](https://github.com/SuperMonster003/AutoJs6/issues/521), GKD `47267c7` y evidencia estática desidentificada de WeChat 8.0.72
- Proporcionar un protocolo A-B-A que no guarda texto privado de nodos ni capturas y usa estadísticas repetidas y reversibilidad para distinguir el efecto de compatibilidad de la carga y la caché
- Añadir fuentes de README y changelog en 10 idiomas, un generador Markdown reproducible, una comprobación de coherencia de solo lectura y controles de GitHub Actions
- Documentar una prueba A-B-A de 7 muestras realizada por AutoJs6 en QV710AF65F, donde el número de nodos subió de forma estable de 1 a 244 y volvió a 1 al desactivar la compatibilidad, verificando además la restauración exacta de la configuración de accesibilidad del sistema
