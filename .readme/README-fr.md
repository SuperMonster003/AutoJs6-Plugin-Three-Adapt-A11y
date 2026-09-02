<!-- This file is generated. Edit .readme/lang_*.json and rerun .python/generate_markdown.py. -->
<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-accessibility-compat-icon" border="0" width="128" />
  </p>
  <h1>Accessibility Compat</h1>
  <p>Un déclencheur de compatibilité d'accessibilité minimal en matière de confidentialité pour les versions concernées de WeChat</p>
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

Accessibility Compat est un APK compagnon expérimental et autonome. Il tente de faire réexposer l'arbre des contrôles au service d'accessibilité AutoJs6 par les versions concernées de WeChat. Ce n'est pas un moteur d'automatisation général et il ne remplace jamais AutoJs6 pour lire ou actionner les contrôles.

> La compatibilité dépend de la version de WeChat et de sa configuration distante. Le projet ne peut garantir le résultat pour chaque version, page, compte ou appareil. Effectuez une validation A-B-A avant de l'utiliser dans un script.

### Architecture compagnon no-op

Le projet isole l'expérience de compatibilité WeChat dans un APK séparé et ne modifie ni le nom ni le comportement général du service principal AutoJs6:

- Le service d'accessibilité AutoJs6 reste le seul composant qui lit, interroge et actionne les noeuds.
- Le compagnon enregistre un nom de classe de service observé dans des expériences publiques, tandis que son ID d'application, son icône, ses libellés, sa description et sa signature identifient honnêtement Accessibility Compat.
- Les rappels du service de compatibilité sont no-op. Ils ne lisent jamais `event.source`, `event.text`, `rootInActiveWindow`, les captures d'écran ou le contenu de la page.
- Cette version n'est ni un proxy de noeuds ni un pont Binder. Elle tente uniquement de déclencher l'exposition globale des noeuds par WeChat.

### Installation et utilisation

1. Vérifiez que l'appareil utilise Android 7.0 (API 24) ou une version ultérieure et qu'AutoJs6 porte au moins le numéro de build interne 3923.
2. Installez l'APK uniquement depuis les [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/releases) du projet ou une entrée de plugin AutoJs6 de confiance.
3. Ouvrez Accessibility Compat, vérifiez son nom et son objectif réels, puis suivez l'invite vers les paramètres d'accessibilité Android.
4. Activez manuellement le service d'accessibilité affiché sous Accessibility Compat. L'avertissement de risque d'Android est normal. Ne contournez pas le consentement avec ADB.
5. Laissez aussi le service d'accessibilité AutoJs6 actif, rouvrez la page WeChat cible, puis inspectez les noeuds avec l'analyseur de disposition AutoJs6 ou un script.

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

- La cible de compatibilité est uniquement `com.tencent.mm`. Ce projet n'est pas un correctif d'accessibilité général pour d'autres applications.
- Les Mini Programs WeChat, XWeb, Canvas et contrôles dessinés sur mesure peuvent ne posséder aucun noeud sémantique natif. Le plugin ne peut inventer un `text` ou `content-desc` absent.
- Même lorsqu'une racine revient, certaines pages peuvent exposer des attributs vides, des noeuds obsolètes, des arbres aléatoires ou seulement des limites géométriques.
- Une mise à jour ou une configuration distante de WeChat peut invalider cette méthode à tout moment. Le retour de version, l'OCR et les coordonnées ont aussi des coûts de sécurité et de stabilité.
- Le plugin ne traite que l'observabilité. Il ne contourne pas la connexion, les contrôles de risque, les captchas, les permissions, les restrictions de compte ou les mécanismes anti-abus.

### Limite de confidentialité

- Les rappels de compatibilité ne lisent ni la source ni le texte des événements, ni la racine de la fenêtre active, ni l'image de l'écran.
- L'application ne téléverse, ne stocke et ne journalise pas le contenu des pages WeChat, et ne contient aucune fonction réseau ou analytique.
- L'application ne demande aucune permission de stockage, de superposition, de caméra, de microphone ou de média.
- Le Binder d'informations rapporte seulement les métadonnées de version, d'identité et de capacités. Il ne transfère jamais d'arbre de noeuds.
- L'utilisateur active et désactive explicitement le service dans Android. Le projet ne modifie jamais ce réglage en silence.

### Éthique et conformité

La compatibilité d'accessibilité doit uniquement aider un utilisateur à automatiser une interface qu'il est autorisé à utiliser. L'utilisateur reste responsable du script et des conséquences pour son compte.

- Utilisez le projet uniquement sur vos propres appareil et compte, dans des processus explicitement autorisés.
- Ne l'utilisez pas pour harceler, envoyer du spam, collecter des données sans consentement, surveiller autrui ou contourner un contrôle de sécurité.
- Respectez la loi, les règles de WeChat et les politiques de votre organisation, avec confirmation humaine et conditions d'arrêt en cas de changement d'interface ou d'erreur.
- Ne partagez que des statistiques agrégées et des structures anonymisées. Ne publiez jamais de conversations, contacts, tokens, APK ou fichiers dex bruts.

### Informations de compatibilité

Le composant de service contient un nom de classe utilisé par des expériences publiques. Il ne s'agit pas de Google Select to Speak, il ne fournit aucune synthèse vocale et n'imite ni une application ni une signature Google. L'identité réelle reste visible dans l'ID, les libellés, l'icône, la page d'information et la signature du projet.

```text
application id: io.github.supermonster003.autojs6.plugin.accessibilitycompat
accessibility service: io.github.supermonster003.autojs6.plugin.accessibilitycompat/com.google.android.accessibility.selecttospeak.SelectToSpeakService
target package: com.tencent.mm
minimum Android: Android 7.0 (API 24)
minimum AutoJs6 build: 3923
```

### Questions fréquentes

#### Le projet imite-t-il une application Google?

Non. Seul le nom de la classe d'implémentation du service sert à l'expérience de compatibilité. Le paquet, les libellés, l'icône, la documentation et la signature restent honnêtes, et le projet précise qu'il ne s'agit pas de Google Select to Speak.

#### Pourquoi rien ne change après l'installation?

Android interdit à une application d'activer elle-même son service d'accessibilité. L'utilisateur doit activer le service de compatibilité et AutoJs6 dans les paramètres. Si rien ne change, utilisez le protocole A-B-A pour tester la version et la page actuelles de WeChat.

#### Pourquoi les noeuds texte manquent-ils encore dans un Mini Program?

Un Mini Program ou une page XWeb peut utiliser Canvas ou un rendu personnalisé sans noeuds sémantiques Android. Le service peut seulement tenter de restaurer un arbre masqué conditionnellement. Il ne peut pas créer une information que la page n'expose pas.

#### Le plugin garantit-il la sécurité du compte?

Non. Il ne contourne pas les contrôles de risque WeChat et ne peut promettre qu'une automatisation est sûre. Utilisez des scripts à faible risque, auditables et confirmés par une personne, puis respectez les règles de la plateforme.

### Base de recherche

La note de recherche couvre AutoJs6 #289, #382, #432, #463, #520 et #521, GKD `47267c7` et des preuves statiques anonymisées de WeChat 8.0.72 sur l'appareil de test. Elle sépare les faits publics, les expériences reproductibles et les déductions sans présenter le fonctionnement interne de WeChat comme une garantie publique.

[Lire la note de recherche sur la compatibilité d'accessibilité WeChat](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/research/wechat-accessibility-compat.md)

### Historique des versions

#### v1.0.0 - 2026/09/02

##### Note

- Cette méthode de compatibilité est expérimentale. Le résultat dépend de la version, de la page et de la configuration distante de WeChat, et aucune version ne peut garantir chaque appareil ou compte
- Le service ne peut créer les noeuds sémantiques qu'un Mini Program WeChat, XWeb ou Canvas n'a jamais exposés, et il ne contourne pas la connexion, les contrôles de risque ou les systèmes anti-abus

##### Fonctionnalité

- Fournir un APK compagnon d'accessibilité no-op autonome limité à `com.tencent.mm`, avec un ID, une icône, des libellés, une description et une signature véridiques et distincts
- Enregistrer un nom de classe de service de compatibilité soutenu par des expériences publiques, tandis qu'AutoJs6 continue à lire et actionner les noeuds par son propre service et que les rappels du compagnon ne lisent aucun contenu utilisateur
- Publier le mode de compatibilité, le paquet cible, le composant de service et le build hôte minimal via l'interface d'information AutoJs6, avec un écran contrôlé par l'utilisateur pour activer ou désactiver le service

##### Amélioration

- Documenter [AutoJs6#289](https://github.com/SuperMonster003/AutoJs6/issues/289), [#382](https://github.com/SuperMonster003/AutoJs6/issues/382), [#432](https://github.com/SuperMonster003/AutoJs6/issues/432), [#463](https://github.com/SuperMonster003/AutoJs6/issues/463), [#520](https://github.com/SuperMonster003/AutoJs6/issues/520), [#521](https://github.com/SuperMonster003/AutoJs6/issues/521), GKD `47267c7` et les preuves statiques anonymisées de WeChat 8.0.72
- Fournir un protocole A-B-A qui ne conserve ni texte privé des noeuds ni capture et utilise des statistiques répétées et la réversibilité pour distinguer l'effet de compatibilité du chargement et du cache
- Ajouter les sources de README et changelog en 10 langues, un générateur Markdown reproductible, un contrôle de cohérence en lecture seule et des portes GitHub Actions
- Documenter un essai A-B-A de 7 échantillons effectué par AutoJs6 sur QV710AF65F, où le nombre de noeuds est passé de façon stable de 1 à 244 puis revenu à 1 après la désactivation de la compatibilité, avec vérification de la restauration exacte des paramètres système d'accessibilité

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
- [Lire la note de recherche sur la compatibilité d'accessibilité WeChat](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/research/wechat-accessibility-compat.md)
- [Lire le protocole complet de validation A-B-A](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/aba-device-validation.md)
- [Lire le rapport QV710AF65F complet et expurgé](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/device-QV710AF65F.md)
