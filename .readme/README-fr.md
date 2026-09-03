<!-- This file is generated. Edit .readme/lang_*.json and rerun .python/generate_markdown.py. -->
<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-accessibility-compat-icon" border="0" width="128" />
  </p>
  <h1>Accessibility Compat</h1>
  <p>Un déclencheur de compatibilité d'accessibilité minimal en matière de confidentialité pour les applications prises en charge</p>
  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/issues"><img alt="GitHub issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?color=534BAE&label=License"/></a>
  </p>
</div>

> Langue de cette page: français

### Langues

[简体中文](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hans.md) | [香港繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hant-HK.md) | [台灣繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hant-TW.md) | [English](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-en.md) | [Français](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-fr.md) | [Español](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-es.md) | [日本語](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ja.md) | [한국어](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ko.md) | [Русский](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ru.md) | [العربية](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ar.md)

### État du projet

Accessibility Compat est un APK compagnon expérimental et autonome. Il tente d'aider les applications couvertes par des profils de support publiés à exposer leurs arbres de contrôles au service d'accessibilité AutoJs6. Ce n'est pas un moteur d'automatisation général et il ne remplace jamais AutoJs6 pour lire ou actionner les contrôles.

> La compatibilité dépend de la version, de la page, de l'appareil et de la configuration distante de chaque application cible. Le projet ne peut garantir le résultat dans tous les environnements. Effectuez une validation A-B-A avant de l'utiliser dans un script.

### Architecture compagnon no-op

Le projet isole les profils de compatibilité des applications dans un APK séparé et ne modifie ni le nom ni le comportement général du service principal AutoJs6:

- Le service d'accessibilité AutoJs6 reste le seul composant qui lit, interroge et actionne les noeuds.
- Le compagnon enregistre un nom de classe de service observé dans des expériences publiques, tandis que son ID d'application, son icône, ses libellés, sa description et sa signature identifient honnêtement Accessibility Compat.
- Les rappels du service de compatibilité sont no-op. Ils ne lisent jamais `event.source`, `event.text`, `rootInActiveWindow`, les captures d'écran ou le contenu de la page.
- Cette version n'est ni un proxy de noeuds ni un pont Binder. Elle tente uniquement de déclencher l'exposition conditionnelle des noeuds documentée par ses profils de support.

### Installation et utilisation

1. Vérifiez que l'appareil utilise Android 7.0 (API 24) ou une version ultérieure et qu'AutoJs6 porte au moins le numéro de build interne 3923.
2. Installez l'APK uniquement depuis les [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/releases) du projet ou une entrée de plugin AutoJs6 de confiance.
3. Ouvrez Accessibility Compat, vérifiez son nom et son objectif réels, puis suivez l'invite vers les paramètres d'accessibilité Android.
4. Activez manuellement le service d'accessibilité affiché sous Accessibility Compat. L'avertissement de risque d'Android est normal. Ne contournez pas le consentement avec ADB.
5. Laissez aussi le service d'accessibilité AutoJs6 actif, rouvrez une page cible indiquée dans un profil de support, puis inspectez les noeuds avec l'analyseur de disposition AutoJs6 ou un script.

L'installation seule de l'APK n'a aucun effet. Désactivez le service dans les paramètres Android lorsqu'il est inutile, ou désinstallez l'application après usage.

### Validation A-B-A sur appareil

Ne prenez pas un seul dump réussi pour une preuve. Comparez A-B-A sur la même page statique avant d'attribuer un changement au service:

- A: gardez AutoJs6 actif et le service de compatibilité inactif, puis recueillez au moins 5 échantillons anonymisés.
- B: activez uniquement le service de compatibilité, revenez à la même page et recueillez au moins 5 autres échantillons.
- A2: désactivez de nouveau le service et répétez la collecte. Le changement doit s'inverser afin d'écarter le chargement et le cache.
- Notez le nombre de noeuds, les champs text/desc/resource-id non vides, les éléments clickable et un hash de structure. Ne conservez jamais de messages, contacts ou captures d'écran.

[Lire le protocole complet de validation A-B-A](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/aba-device-validation.md)

### Résultat vérifié sur appareil

Le 2026-09-02, un essai A-B-A réversible a été effectué sur le Sony XQ-AT72 autorisé avec Android 12/API 31, WeChat 8.0.72 code 3085 et AutoJs6 6.8.0 code 5277. Les 6 services d'accessibilité préexistants sont restés inchangés. Chaque phase a forcé l'arrêt puis relancé WeChat LauncherUI, attendu plus de 5 secondes, puis pris 7 échantillons via AutoJs6 lui-même:

- A avec la compatibilité désactivée: nodes 1, text 0, desc 0, id 0, clickable 0, avec le même hash de structure dans les 7 échantillons.
- B avec la compatibilité activée et les deux services cibles bound: nodes 244, text 31, desc 13, id 157, clickable 38, avec le même hash de structure dans les 7 échantillons.
- A2 avec la compatibilité de nouveau désactivée: retour exact à nodes 1 et 0 pour chaque autre mesure, avec le même hash de structure dans les 7 échantillons. Les paramètres système d'accessibilité ont ensuite été restaurés exactement.

Ce résultat vérifie le déclencheur pour cette combinaison d'appareil, de version WeChat et de page LauncherUI. Il ne peut pas être généralisé à d'autres versions, comptes, appareils, Mini Programs, XWeb ou pages Canvas.

[Lire le rapport QV710AF65F complet et expurgé](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/device-QV710AF65F.md)

### Limites connues

- Les profils actuels ne contiennent que WeChat (`com.tencent.mm`). Il s'agit d'un ensemble de support explicite et non d'une affirmation de compatibilité universelle.
- WebView, les mini-applications, Canvas et les contrôles dessinés sur mesure peuvent ne posséder aucun noeud sémantique natif. Le plugin ne peut inventer un `text` ou `content-desc` absent.
- Même lorsqu'une racine revient, certaines pages peuvent exposer des attributs vides, des noeuds obsolètes, des arbres aléatoires ou seulement des limites géométriques.
- Une mise à jour ou une configuration distante de l'application cible peut invalider un profil à tout moment. Le retour de version, l'OCR et les coordonnées ont aussi des coûts de sécurité et de stabilité.
- Le plugin ne traite que l'observabilité. Il ne contourne pas la connexion, les contrôles de risque, les captchas, les permissions, les restrictions de compte ou les mécanismes anti-abus.

### Limite de confidentialité

- Les rappels de compatibilité ne lisent ni la source ni le texte des événements, ni la racine de la fenêtre active, ni l'image de l'écran.
- L'application ne téléverse, ne stocke et ne journalise pas le contenu des pages des applications cibles, et ne contient aucune fonction réseau ou analytique.
- L'application ne demande aucune permission de stockage, de superposition, de caméra, de microphone ou de média.
- Le Binder d'informations rapporte seulement les métadonnées de version, d'identité et de capacités. Il ne transfère jamais d'arbre de noeuds.
- L'utilisateur active et désactive explicitement le service dans Android. Le projet ne modifie jamais ce réglage en silence.

### Éthique et conformité

La compatibilité d'accessibilité doit uniquement aider un utilisateur à automatiser une interface qu'il est autorisé à utiliser. L'utilisateur reste responsable du script et des conséquences pour son compte.

- Utilisez le projet uniquement sur vos propres appareil et compte, dans des processus explicitement autorisés.
- Ne l'utilisez pas pour harceler, envoyer du spam, collecter des données sans consentement, surveiller autrui ou contourner un contrôle de sécurité.
- Respectez la loi, les règles de la plateforme cible et les politiques de votre organisation, avec confirmation humaine et conditions d'arrêt en cas de changement d'interface ou d'erreur.
- Ne partagez que des statistiques agrégées et des structures anonymisées. Ne publiez jamais de conversations, contacts, tokens, APK ou fichiers dex bruts.

### Informations de compatibilité

Le composant de service contient un nom de classe utilisé par des expériences publiques. Il ne s'agit pas de Google Select to Speak, il ne fournit aucune synthèse vocale et n'imite ni une application ni une signature Google. L'identité réelle reste visible dans l'ID, les libellés, l'icône, la page d'information et la signature du projet.

```text
application id: io.github.supermonster003.autojs6.plugin.accessibilitycompat
accessibility service: io.github.supermonster003.autojs6.plugin.accessibilitycompat/com.google.android.accessibility.selecttospeak.SelectToSpeakService
supported packages: com.tencent.mm
minimum Android: Android 7.0 (API 24)
minimum AutoJs6 build: 3923
```

### Questions fréquentes

#### Le projet imite-t-il une application Google?

Non. Seul le nom de la classe d'implémentation du service sert à l'expérience de compatibilité. Le paquet, les libellés, l'icône, la documentation et la signature restent honnêtes, et le projet précise qu'il ne s'agit pas de Google Select to Speak.

#### Pourquoi rien ne change après l'installation?

Android interdit à une application d'activer elle-même son service d'accessibilité. L'utilisateur doit activer le service de compatibilité et AutoJs6 dans les paramètres. Si rien ne change, utilisez le protocole A-B-A pour tester la version et la page actuelles de l'application cible.

#### Pourquoi les noeuds texte manquent-ils encore sur une page prise en charge?

Une page WebView, une mini-application, Canvas ou un rendu personnalisé peut ne posséder aucun noeud sémantique Android. Le service peut seulement tenter de restaurer un arbre masqué conditionnellement. Il ne peut pas créer une information que la page n'expose pas.

#### Le plugin garantit-il la sécurité du compte?

Non. Il ne contourne pas les contrôles de risque de la plateforme cible et ne peut promettre qu'une automatisation est sûre. Utilisez des scripts à faible risque, auditables et confirmés par une personne, puis respectez les règles de la plateforme.

### Base de recherche

La note de recherche couvre AutoJs6 #289, #382, #432, #463, #520 et #521, GKD `47267c7` et des preuves statiques anonymisées de WeChat 8.0.72 sur l'appareil de test. Elle sépare les faits publics, les expériences reproductibles et les déductions sans présenter le fonctionnement interne de WeChat comme une garantie publique.

[Lire la note sur la compatibilité d'identité des services d'accessibilité](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/research/accessibility-service-identity-compat.md)

### Historique des versions

#### v1.1.0 - 2026/09/03

##### Amélioration

- Généraliser les termes de l'application, du service d'accessibilité, des métadonnées du plugin, des instructions intégrées et du README autour de profils indépendants de l'application, tout en conservant WeChat (`com.tencent.mm`) comme seul profil actuellement vérifié
- Remplacer la variante propre à la cible par `service-identity`, publier les paquets pris en charge sous forme de collection et répertorier ou ouvrir les applications compatibles installées avec des actions génériques
- Généraliser le protocole A-B-A, le point d'entrée de recherche et l'outil de mesure respectueux de la confidentialité, avec une entrée `targetPackage` explicite pour les futurs profils
- Supprimer les propriétés obsolètes de version minimale d'Android Studio et d'IntelliJ IDEA puisque la compatibilité de l'IDE et de la chaîne d'outils est désormais sélectionnée de manière centralisée

##### Dépendance

- Mettre à niveau `io.github.supermonster003.autojs6-platform-versions` de 1.7.0 vers 1.7.3 afin que les builds JDK 25 et 26 alignent automatiquement le KGP sélectionné sur le classpath du buildscript racine

[Lire l'historique complet](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/changelog/CHANGELOG-fr.md)

### Compilation et contrôle de la documentation

Les utilisateurs ordinaires doivent installer l'APK précompilé des Releases. Les développeurs peuvent compiler et vérifier le projet avec le Gradle Wrapper du dépôt:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug
py .python\generate_markdown.py --check
```

Les README et changelog sont générés depuis des sources JSON. Après une modification de `.readme/lang_*.json`, `.changelog/lang_*.json` ou d'un modèle, exécutez:

```powershell
py .python\generate_markdown.py
py .python\generate_markdown.py --check
```

### Licence

Le code du projet est sous [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/LICENSE). Les noms WeChat, Google et Select to Speak appartiennent à leurs propriétaires. Le projet n'est ni affilié à ces sociétés ni approuvé par elles.

### Liens

- [AutoJs6](https://github.com/SuperMonster003/AutoJs6)
- [Lire la note sur la compatibilité d'identité des services d'accessibilité](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/research/accessibility-service-identity-compat.md)
- [Lire le protocole complet de validation A-B-A](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/aba-device-validation.md)
- [Lire le rapport QV710AF65F complet et expurgé](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/device-QV710AF65F.md)
