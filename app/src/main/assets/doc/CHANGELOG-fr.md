<!-- This file is generated. Edit .changelog/lang_*.json and rerun .python/generate_markdown.py. -->

# Historique des versions 3-Adapt A11y

> Langue de cette page: français

## v1.4.3

###### 2026/11/20

* `Amélioration` L'historique des versions passe d'une page de texte brut à une nouvelle page qui se développe et se réduit par version: une version réduite affiche sa date et le nombre d'entrées par catégorie, une version développée ne nomme chaque catégorie qu'une fois; toutes les versions peuvent être développées ou réduites d'un coup, la plus récente s'ouvre développée et le nom du plugin sert de sous-titre
* `Amélioration` Réécriture des instructions affichées dans le Centre des plugins en quatre parties: prise en main, fonctionnalités principales, prérequis et questions fréquentes
* `Dépendance` Ajout de Jetpack Compose BOM 2026.09.00 (page Historique des versions)
* `Dépendance` Mise à niveau des plugins de compilation partagés version 1.9.0 -> 1.9.7

## v1.4.2

###### 2026/11/13

* `Correction` Lorsque le processus du service de compatibilité est arrêté, les paramètres système l'affichent toujours comme activé mais le système ne l'exécute plus; la synchronisation le désactive puis le réactive désormais via root, WRITE_SECURE_SETTINGS ou Shizuku, et sans aucun d'eux la page d'accueil explique comment le désactiver puis le réactiver dans les paramètres d'accessibilité du système et propose un bouton pour les ouvrir
* `Amélioration` Lorsque Shizuku est le seul moyen d'activer le service de compatibilité, une synchronisation lancée avant que Shizuku soit prêt au démarrage du plugin est refaite une fois Shizuku prêt

## v1.4.1

###### 2026/10/17

* `Correction` Dans l'interface en arabe, 3-Adapt A11y ne s'affiche plus à l'envers sous la forme Adapt A11y-3
* `Amélioration` Les icônes du centre de plugins utilisent les tailles, positions, images claires et sombres et fonds circulaires réglés dans Icon Studio, avec les sources et paramètres permettant de les reproduire
* `Amélioration` Les icônes des informations d'application Android utilisent les illustrations et les fonds clairs et sombres d'Icon Studio, en conservant les images transparentes du centre de plugins et les choix du lanceur

## v1.4.0

###### 2026/10/03

* `Astuce` 3-Adapt A11y utilise le nouveau paquet io.github.supermonster003.autojs6.plugin.three.adapt.a11y et reste une application distincte. Accessibility Compat et ses données peuvent être conservés, sans migration automatique des réglages. Activez le nouveau service si nécessaire
* `Fonctionnalité` Unifier les réglages avec des groupes plats, des lignes cohérentes et des dialogues arrondis centrés. La langue, le mode nuit, la couleur et l'icône ne changent qu'après validation; Annuler conserve les valeurs enregistrées. La couleur suit AutoJs6 par défaut et propose une palette commune, une saisie HEX/RGB et un aperçu local. Les fonds neutres restent stables et les contrôles suivent le thème. L'icône utilise le mode adaptatif automatique par défaut, tout en préservant les choix explicites lors des mises à jour.
* `Correction` Conserver la ressource automatique dynamique après installation afin que le lanceur puisse charger la variante claire ou sombre correspondante.
* `Amélioration` Le plugin Accessibility Compat est renommé 3-Adapt A11y dans le titre de l'application, le libellé du service d'accessibilité, l'ID de plugin `three-adapt-a11y`, les noms de package et de composants, les artefacts de publication, la documentation et le dépôt GitHub
* `Amélioration` Taille visuelle harmonisée des icônes du lanceur et du Centre de plugins, avec des fonds transparents et des motifs noirs, blancs ou gris neutres
* `Dépendance` Material Components 1.13.0 / AppCompat 1.7.1 (Material 3).

## v1.3.1

###### 2026/09/19

* `Correction` Avertissements de lecture SDK XML v4 avec AGP 9.1 et contrôles d'alignement natif des APK déclenchés par erreur lors de l'assemblage des tests unitaires JVM, avec les plugins de compilation partagés 1.8.3
* `Amélioration` Explications de la protection avancée dans l'hôte et le compagnon, distinguant le mode global de la disponibilité réelle du service et du contrôle des paramètres sécurisés
* `Amélioration` Après compileSdk, targetSdk passe à 37 (Android 17) ; le comportement du plugin ne dépend pas de la nouvelle cible

## v1.3.0

###### 2026/09/15

* `Fonctionnalité` Ajout d'un écran de paramètres avec des options de langue, de mode sombre et de couleur de thème qui suivent AutoJs6 par défaut, ainsi que les entrées mécanisme, confidentialité, limites, historique des versions et à propos
* `Fonctionnalité` Ajout d'un gestionnaire du service de compatibilité avec une politique à trois choix (suivre AutoJs6, activé, désactivé) capable de changer le service via root, WRITE_SECURE_SETTINGS ou Shizuku, avec l'état en direct et un rapport copiable
* `Fonctionnalité` Suivi automatique du service d'accessibilité AutoJs6 grâce à une diffusion d'état de l'hôte, une tâche déclenchée par la liste des services activés et des vérifications à la connexion du service et à l'ouverture de l'application
* `Fonctionnalité` Publication de la politique de service choisie via les capacités plugin-info et le contrat partagé de compagnon d'accessibilité (version de contrat 3)
* `Fonctionnalité` Ajouter un écran à propos de l'application et du développeur avec la version, le nom du paquet, la licence open source, la page du projet et les liens de commentaires
* `Correction` Les titres et résumés des lignes s'alignent sur le sens de la mise en page et non sur celui du texte, si bien que l'option arabe de la boîte de dialogue de langue reste à côté de son bouton radio
* `Amélioration` Refonte de l'écran principal: la carte d'état ouvre le gestionnaire, les applications compatibles ont un bouton de lancement et les anciens boutons d'ouverture, de paramètres d'accessibilité et d'actualisation passent dans l'écran des paramètres
* `Amélioration` Compilation avec l'API Android 37 pour correspondre à l'API commune des plugins mise à jour
* `Amélioration` Aligner les options de politique du gestionnaire de service sur ses autres lignes, supprimer la ligne redondante d'application immédiate puisqu'une politique s'applique dès son choix, et reconstruire les boîtes de dialogue de choix d'apparence avec des tailles de texte, marges et espacements cohérents
* `Dépendance` Ajout de Shizuku API 13.1.5 et AndroidX Annotation 1.10.0
* `Dépendance` Mise à jour de l'API commune des plugins intégrée pour inclure le contrat de compagnon d'accessibilité

## v1.2.0

###### 2026/09/13

* `Fonctionnalité` Historique local accessible depuis l'interface, avec traductions et repli en anglais
* `Amélioration` Vérification de la signature complète, des APK attendus et de la reproductibilité de la documentation

## v1.1.0

###### 2026/09/12

* `Amélioration` Généraliser les termes de l'application, du service d'accessibilité, des métadonnées du plugin, des instructions intégrées et du README autour de profils indépendants de l'application, tout en conservant WeChat (`com.tencent.mm`) comme seul profil actuellement vérifié
* `Amélioration` Remplacer la variante propre à la cible par `service-identity`, publier les paquets pris en charge sous forme de collection et répertorier ou ouvrir les applications compatibles installées avec des actions génériques
* `Amélioration` Actualiser les icônes adaptatives du lanceur avec des variantes claires et sombres et une couche monochrome pour les icônes à thème
* `Amélioration` Généraliser le protocole A-B-A, le point d'entrée de recherche et l'outil de mesure respectueux de la confidentialité, avec une entrée `targetPackage` explicite pour les futurs profils
* `Amélioration` Supprimer les propriétés obsolètes de version minimale d'Android Studio et d'IntelliJ IDEA puisque la compatibilité de l'IDE et de la chaîne d'outils est désormais sélectionnée de manière centralisée
* `Amélioration` La vérification de compilation rejette les dépendances natives involontaires et produit un rapport JSON
* `Dépendance` Mettre à niveau `io.github.supermonster003.autojs6-platform-versions` de 1.7.0 vers 1.7.3 afin que les builds JDK 25 et 26 alignent automatiquement le KGP sélectionné sur le classpath du buildscript racine

## v1.0.0

###### 2026/09/02

* `Astuce` Cette méthode de compatibilité est expérimentale. Le résultat dépend de la version, de la page et de la configuration distante de WeChat, et aucune version ne peut garantir chaque appareil ou compte
* `Astuce` Le service ne peut créer les noeuds sémantiques qu'un Mini Program WeChat, XWeb ou Canvas n'a jamais exposés, et il ne contourne pas la connexion, les contrôles de risque ou les systèmes anti-abus
* `Fonctionnalité` Fournir un APK compagnon d'accessibilité no-op autonome limité à `com.tencent.mm`, avec un ID, une icône, des libellés, une description et une signature véridiques et distincts
* `Fonctionnalité` Enregistrer un nom de classe de service de compatibilité soutenu par des expériences publiques, tandis qu'AutoJs6 continue à lire et actionner les noeuds par son propre service et que les rappels du compagnon ne lisent aucun contenu utilisateur
* `Fonctionnalité` Publier le mode de compatibilité, le paquet cible, le composant de service et le build hôte minimal via l'interface d'information AutoJs6, avec un écran contrôlé par l'utilisateur pour activer ou désactiver le service
* `Amélioration` Documenter [AutoJs6#289](https://github.com/SuperMonster003/AutoJs6/issues/289), [#382](https://github.com/SuperMonster003/AutoJs6/issues/382), [#432](https://github.com/SuperMonster003/AutoJs6/issues/432), [#463](https://github.com/SuperMonster003/AutoJs6/issues/463), [#520](https://github.com/SuperMonster003/AutoJs6/issues/520), [#521](https://github.com/SuperMonster003/AutoJs6/issues/521), GKD `47267c7` et les preuves statiques anonymisées de WeChat 8.0.72
* `Amélioration` Fournir un protocole A-B-A qui ne conserve ni texte privé des noeuds ni capture et utilise des statistiques répétées et la réversibilité pour distinguer l'effet de compatibilité du chargement et du cache
* `Amélioration` Ajouter les sources de README et changelog en 10 langues, un générateur Markdown reproductible, un contrôle de cohérence en lecture seule et des portes GitHub Actions
* `Amélioration` Documenter un essai A-B-A de 7 échantillons effectué par AutoJs6 sur QV710AF65F, où le nombre de noeuds est passé de façon stable de 1 à 244 puis revenu à 1 après la désactivation de la compatibilité, avec vérification de la restauration exacte des paramètres système d'accessibilité
