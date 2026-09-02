<!-- This file is generated. Edit .changelog/lang_*.json and rerun .python/generate_markdown.py. -->

# Historial de versiones de Accessibility Compat

> Idioma de esta página: español

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
