# Compatibilidad de accesibilidad de AutoJs6

## Finalidad

Este es un complemento experimental y opcional. Puede ayudar a AutoJs6 a obtener nodos de accesibilidad más normales de las aplicaciones cubiertas por perfiles de soporte publicados. La automatización sigue a cargo del servicio de accesibilidad de AutoJs6.

## Habilitar el servicio

1. Instala AutoJs6 y este complemento.
2. Abre el complemento y toca la tarjeta de estado para abrir el gestor del servicio de compatibilidad. La política predeterminada, seguir a AutoJs6, mantiene el servicio de compatibilidad habilitado mientras el servicio de accesibilidad de AutoJs6 esté habilitado.
3. Sin root, WRITE_SECURE_SETTINGS ni Shizuku, habilita manualmente el servicio de AutoJs6 y Compatibilidad de accesibilidad de AutoJs6 en los ajustes de accesibilidad de Android. En cuanto concedas uno de ellos, el complemento aplica por sí mismo la política seleccionada.
4. Cierra por completo y vuelve a abrir una aplicación objetivo de la lista de soporte, y revisa varias veces la misma página estática.
5. Elige la política deshabilitado en el gestor, o deshabilita este servicio en los ajustes del sistema, cuando no necesites la compatibilidad.

Android no permite que una aplicación normal habilite silenciosamente un servicio de accesibilidad. Este complemento solo cambia el servicio automáticamente después de que concedas explícitamente root, WRITE_SECURE_SETTINGS o Shizuku, y solo modifica la lista de servicios de accesibilidad habilitados.

## Mecanismo experimental de compatibilidad

Algunas aplicaciones afectadas pueden decidir si exponen nodos normales según el nombre de la clase de implementación de un servicio habilitado. Este complemento contiene el siguiente nombre de clase de compatibilidad verificado de forma experimental y requerido por sus perfiles actuales.

`com.google.android.accessibility.selecttospeak.SelectToSpeakService`

Este complemento no es Google Select to Speak, no está afiliado con Google y no copia el identificador de aplicación, la firma, el nombre ni el icono de Google. El nombre de clase solo se usa como identificador de compatibilidad divulgado y puede dejar de funcionar tras una actualización de la aplicación objetivo.

## Perfiles de soporte actuales

- WeChat (`com.tencent.mm`) es el único perfil verificado en esta versión.
- Un perfil solo se aplica a las versiones y páginas documentadas. Estar en la lista no garantiza todos los entornos.

## Privacidad

- La llamada del servicio no realiza ninguna operación e ignora todos los eventos de accesibilidad.
- El complemento no lee fuentes o textos de eventos, nodos raíz, capturas de pantalla ni contenido del usuario.
- El complemento no guarda, comparte ni sube datos y no accede a la red.
- El servicio solo recibe eventos de los identificadores de paquete incluidos en los perfiles de soporte publicados.

AutoJs6 sigue leyendo y operando los nodos mediante su propio servicio de accesibilidad. Este complemento no retransmite un árbol de nodos ni realiza acciones de automatización.

## Limitaciones y riesgos

La compatibilidad depende de la versión y la página de la aplicación objetivo, el dispositivo y la implementación del sistema. No se garantiza el éxito. Este complemento no puede crear nodos semánticos que una página no exponga. Es posible que los controles de WebView, Canvas y las interfaces dibujadas de forma personalizada sigan sin estar disponibles.

Una aplicación objetivo puede cambiar su método de detección y hacer que el complemento deje de funcionar. La automatización también puede estar sujeta a las reglas de la plataforma objetivo o a controles de riesgo de la cuenta. Evalúa el riesgo en tu dispositivo y cuenta.
