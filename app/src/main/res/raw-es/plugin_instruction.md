# Compatibilidad de accesibilidad de AutoJs6 para WeChat

## Finalidad

Este es un complemento experimental y opcional. Puede ayudar a AutoJs6 a obtener nodos de accesibilidad más normales de versiones afectadas de WeChat. La automatización sigue a cargo del servicio de accesibilidad de AutoJs6.

## Habilitar el servicio

1. Instala AutoJs6 y este complemento.
2. Habilita manualmente el servicio de AutoJs6 y Compatibilidad de accesibilidad de AutoJs6 para WeChat en los ajustes de accesibilidad de Android.
3. Cierra por completo y vuelve a abrir WeChat, y revisa varias veces la misma página estática.
4. Deshabilita este servicio en los ajustes del sistema cuando no necesites la compatibilidad.

Android no permite que una aplicación normal habilite silenciosamente un servicio de accesibilidad. Este complemento no intenta eludir esa restricción.

## Mecanismo experimental de compatibilidad

Algunas versiones de WeChat pueden decidir si exponen nodos normales según el nombre de la clase de implementación de un servicio habilitado. Este complemento contiene el siguiente nombre de clase de compatibilidad verificado de forma experimental.

`com.google.android.accessibility.selecttospeak.SelectToSpeakService`

Este complemento no es Google Select to Speak, no está afiliado con Google y no copia el identificador de aplicación, la firma, el nombre ni el icono de Google. El nombre de clase solo se usa como identificador de compatibilidad divulgado y puede dejar de funcionar tras una actualización de WeChat.

## Privacidad

- La llamada del servicio no realiza ninguna operación e ignora todos los eventos de accesibilidad.
- El complemento no lee fuentes o textos de eventos, nodos raíz, capturas de pantalla ni contenido del usuario.
- El complemento no guarda, comparte ni sube datos y no accede a la red.
- El servicio solo esta configurado para el paquete de WeChat `com.tencent.mm`.

AutoJs6 sigue leyendo y operando los nodos mediante su propio servicio de accesibilidad. Este complemento no retransmite un árbol de nodos ni realiza acciones de automatización.

## Limitaciones y riesgos

La compatibilidad depende de la versión de WeChat, el dispositivo y la implementación del sistema. No se garantiza el éxito. Este complemento no puede crear nodos semánticos que una página no exponga. Es posible que los controles de Mini Programs, XWeb, Canvas y las interfaces dibujadas de forma personalizada sigan sin estar disponibles.

WeChat puede cambiar su método de detección y hacer que el complemento deje de funcionar. La automatización también puede estar sujeta a las reglas de WeChat o a controles de riesgo de la cuenta. Evalúa el riesgo en tu dispositivo y cuenta.
