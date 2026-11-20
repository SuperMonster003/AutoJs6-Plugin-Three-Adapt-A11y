3-Adapt A11y es un complemento de compatibilidad opcional para AutoJs6: habilita un servicio de accesibilidad que no procesa ningún evento, lo que ayuda a AutoJs6 a obtener más nodos de accesibilidad normales de las aplicaciones cubiertas por los perfiles de compatibilidad publicados. La automatización la sigue realizando el servicio de accesibilidad de AutoJs6, y el plugin no lee el contenido de la pantalla ni usa la red.

### Primeros pasos

1. Instala AutoJs6 y 3-Adapt A11y, abre el plugin y toca la tarjeta de estado "Servicio de compatibilidad" para abrir "Gestor del servicio de compatibilidad".
2. La "Política de control" predeterminada, "Seguir a AutoJs6", habilita el servicio de compatibilidad siempre que el servicio de accesibilidad de AutoJs6 esté habilitado; elige "Deshabilitado" cuando no lo necesites.
3. Sin acceso root, WRITE_SECURE_SETTINGS ni Shizuku, toca "Ajustes de accesibilidad del sistema" en "Ajustes" y habilita a mano el servicio de AutoJs6 y 3-Adapt A11y.
4. Cierra por completo y vuelve a abrir una aplicación de destino de "Aplicaciones compatibles actualmente", y después lee los nodos de la página con AutoJs6.

### Funciones principales

- **Gestor del servicio de compatibilidad**: consulta el estado actual del servicio de compatibilidad y del servicio de AutoJs6 en "Estado", y elige "Seguir a AutoJs6", "Habilitado" o "Deshabilitado" en "Política de control".
- **Control automático**: tras conceder acceso root, WRITE_SECURE_SETTINGS o Shizuku, activa la opción correspondiente en "Control automático" y el plugin habilita o deshabilita el servicio según la política elegida, modificando solo la lista de servicios de accesibilidad habilitados.
- **Reparación del servicio detenido**: cuando los ajustes del sistema muestran el servicio como activado pero ya no se ejecuta, el plugin lo desactiva y lo vuelve a activar del mismo modo; si no puede, la página de inicio muestra "El servicio de compatibilidad no está en ejecución" con un botón "Ajustes del sistema".
- **Perfiles de compatibilidad**: "Aplicaciones compatibles actualmente" en la página de inicio enumera las aplicaciones verificadas, que en esta versión es solo WeChat; un perfil solo se aplica a las versiones y páginas documentadas y no cubre todos los entornos.
- **Privacidad**: la devolución de llamada del servicio no hace nada e ignora todos los eventos de accesibilidad; el plugin no lee el contenido de los eventos, los nodos raíz, las capturas de pantalla ni el contenido del usuario, no guarda ni sube datos y no tiene acceso a la red.
- **Apariencia e información**: en "Ajustes" puedes ajustar "Idioma", "Modo oscuro", "Color del tema" e "Icono del lanzador", y consultar "Mecanismo de compatibilidad", "Privacidad", "Limitaciones" e "Historial de versiones".

### Requisitos

- Android 7.0 (API 24) o posterior, y AutoJs6 6.8.0 o posterior; el servicio de accesibilidad de AutoJs6 lee los nodos y realiza las acciones.
- El servicio de accesibilidad de este plugin solo se habilita como identificador de compatibilidad; solo recibe eventos de las aplicaciones de los perfiles de compatibilidad y no los procesa.
- El acceso root, WRITE_SECURE_SETTINGS y Shizuku son opcionales y solo sirven para habilitar o deshabilitar el servicio automáticamente; WRITE_SECURE_SETTINGS se concede mediante ADB o una shell root.
- El permiso para consultar el modo Protección avanzada comprueba si la "Protección avanzada" de Android 17 restringe este servicio; el resultado se muestra en "Ajustes".

### Preguntas frecuentes

**Los nodos no cambian después de habilitar el servicio**

Comprueba primero que el servicio de AutoJs6 y 3-Adapt A11y estén activados, y después cierra por completo y vuelve a abrir la aplicación de destino. El resultado depende de la versión de la aplicación, la página, el dispositivo y el sistema, y no está garantizado; los controles en WebView, Canvas e interfaces dibujadas a medida pueden seguir sin estar disponibles.

**El servicio está activado en los ajustes del sistema, pero la página de inicio muestra "El servicio de compatibilidad no está en ejecución"**

Android no restablece el servicio después de que se cierre su proceso. Con acceso root, WRITE_SECURE_SETTINGS o Shizuku, el plugin lo repara automáticamente; si no, toca "Ajustes del sistema" y desactiva y vuelve a activar 3-Adapt A11y.

**El nombre de clase del servicio coincide con el de Seleccionar para pronunciar de Google**

Algunas aplicaciones deciden si exponen nodos normales según el nombre de clase de un servicio habilitado, y el plugin usa ese nombre de clase solo como identificador de compatibilidad declarado. Este plugin no es Seleccionar para pronunciar de Google, no está afiliado a Google y no usa ningún ID de aplicación, firma, nombre ni icono de Google.

Los perfiles de compatibilidad, el mecanismo de compatibilidad y los registros de validación están en la [página del proyecto](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y).
