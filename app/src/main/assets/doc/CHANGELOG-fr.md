<!-- This file is generated. Edit .changelog/lang_*.json and rerun .python/generate_markdown.py. -->

# Historique des versions Accessibility Compat

> Langue de cette page: français

## v1.3.0 - 2026/09/15

### Fonctionnalité

- Ajout d'un écran de paramètres avec des options de langue, de mode sombre et de couleur de thème qui suivent AutoJs6 par défaut, ainsi que les entrées mécanisme, confidentialité, limites, historique des versions et à propos
- Ajout d'un gestionnaire du service de compatibilité avec une politique à trois choix (suivre AutoJs6, activé, désactivé) capable de changer le service via root, WRITE_SECURE_SETTINGS ou Shizuku, avec l'état en direct et un rapport copiable
- Suivi automatique du service d'accessibilité AutoJs6 grâce à une diffusion d'état de l'hôte, une tâche déclenchée par la liste des services activés et des vérifications à la connexion du service et à l'ouverture de l'application
- Publication de la politique de service choisie via les capacités plugin-info et le contrat partagé de compagnon d'accessibilité (version de contrat 3)
- Ajouter un écran à propos de l'application et du développeur avec la version, le nom du paquet, la licence open source, la page du projet et les liens de commentaires

### Correctif

- Les titres et résumés des lignes s'alignent sur le sens de la mise en page et non sur celui du texte, si bien que l'option arabe de la boîte de dialogue de langue reste à côté de son bouton radio

### Amélioration

- Refonte de l'écran principal: la carte d'état ouvre le gestionnaire, les applications compatibles ont un bouton de lancement et les anciens boutons d'ouverture, de paramètres d'accessibilité et d'actualisation passent dans l'écran des paramètres
- Compilation avec l'API Android 37 pour correspondre à l'API commune des plugins mise à jour
- Aligner les options de politique du gestionnaire de service sur ses autres lignes, supprimer la ligne redondante d'application immédiate puisqu'une politique s'applique dès son choix, et reconstruire les boîtes de dialogue de choix d'apparence avec des tailles de texte, marges et espacements cohérents

### Dépendance

- Ajout de Shizuku API 13.1.5 et AndroidX Annotation 1.10.0
- Mise à jour de l'API commune des plugins intégrée pour inclure le contrat de compagnon d'accessibilité

## v1.2.0 - 2026/09/13

### Fonctionnalité

- Historique local accessible depuis l'interface, avec traductions et repli en anglais

### Amélioration

- Vérification de la signature complète, des APK attendus et de la reproductibilité de la documentation

## v1.1.0 - 2026/09/12

### Amélioration

- Généraliser les termes de l'application, du service d'accessibilité, des métadonnées du plugin, des instructions intégrées et du README autour de profils indépendants de l'application, tout en conservant WeChat (`com.tencent.mm`) comme seul profil actuellement vérifié
- Remplacer la variante propre à la cible par `service-identity`, publier les paquets pris en charge sous forme de collection et répertorier ou ouvrir les applications compatibles installées avec des actions génériques
- Actualiser les icônes adaptatives du lanceur avec des variantes claires et sombres et une couche monochrome pour les icônes à thème
- Généraliser le protocole A-B-A, le point d'entrée de recherche et l'outil de mesure respectueux de la confidentialité, avec une entrée `targetPackage` explicite pour les futurs profils
- Supprimer les propriétés obsolètes de version minimale d'Android Studio et d'IntelliJ IDEA puisque la compatibilité de l'IDE et de la chaîne d'outils est désormais sélectionnée de manière centralisée
- La vérification de compilation rejette les dépendances natives involontaires et produit un rapport JSON

### Dépendance

- Mettre à niveau `io.github.supermonster003.autojs6-platform-versions` de 1.7.0 vers 1.7.3 afin que les builds JDK 25 et 26 alignent automatiquement le KGP sélectionné sur le classpath du buildscript racine

## v1.0.0 - 2026/09/02

### Note

- Cette méthode de compatibilité est expérimentale. Le résultat dépend de la version, de la page et de la configuration distante de WeChat, et aucune version ne peut garantir chaque appareil ou compte
- Le service ne peut créer les noeuds sémantiques qu'un Mini Program WeChat, XWeb ou Canvas n'a jamais exposés, et il ne contourne pas la connexion, les contrôles de risque ou les systèmes anti-abus

### Fonctionnalité

- Fournir un APK compagnon d'accessibilité no-op autonome limité à `com.tencent.mm`, avec un ID, une icône, des libellés, une description et une signature véridiques et distincts
- Enregistrer un nom de classe de service de compatibilité soutenu par des expériences publiques, tandis qu'AutoJs6 continue à lire et actionner les noeuds par son propre service et que les rappels du compagnon ne lisent aucun contenu utilisateur
- Publier le mode de compatibilité, le paquet cible, le composant de service et le build hôte minimal via l'interface d'information AutoJs6, avec un écran contrôlé par l'utilisateur pour activer ou désactiver le service

### Amélioration

- Documenter [AutoJs6#289](https://github.com/SuperMonster003/AutoJs6/issues/289), [#382](https://github.com/SuperMonster003/AutoJs6/issues/382), [#432](https://github.com/SuperMonster003/AutoJs6/issues/432), [#463](https://github.com/SuperMonster003/AutoJs6/issues/463), [#520](https://github.com/SuperMonster003/AutoJs6/issues/520), [#521](https://github.com/SuperMonster003/AutoJs6/issues/521), GKD `47267c7` et les preuves statiques anonymisées de WeChat 8.0.72
- Fournir un protocole A-B-A qui ne conserve ni texte privé des noeuds ni capture et utilise des statistiques répétées et la réversibilité pour distinguer l'effet de compatibilité du chargement et du cache
- Ajouter les sources de README et changelog en 10 langues, un générateur Markdown reproductible, un contrôle de cohérence en lecture seule et des portes GitHub Actions
- Documenter un essai A-B-A de 7 échantillons effectué par AutoJs6 sur QV710AF65F, où le nombre de noeuds est passé de façon stable de 1 à 244 puis revenu à 1 après la désactivation de la compatibilité, avec vérification de la restauration exacte des paramètres système d'accessibilité
