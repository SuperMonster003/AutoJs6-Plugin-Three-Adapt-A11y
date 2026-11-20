3-Adapt A11y est un compagnon de compatibilité facultatif pour AutoJs6: il active un service d'accessibilité qui ne traite aucun événement, ce qui aide AutoJs6 à obtenir davantage de nœuds d'accessibilité normaux dans les applications couvertes par les profils de prise en charge publiés. L'automatisation reste assurée par le service d'accessibilité d'AutoJs6, et le plugin ne lit pas le contenu de l'écran ni n'utilise le réseau.

### Prise en main

1. Installez AutoJs6 et 3-Adapt A11y, ouvrez le plugin et touchez la carte d'état "Service de compatibilité" pour ouvrir "Gestionnaire du service de compatibilité".
2. La "Politique de contrôle" par défaut, "Suivre AutoJs6", active le service de compatibilité dès que le service d'accessibilité d'AutoJs6 est activé; choisissez "Désactivé" lorsqu'il n'est pas utile.
3. Sans accès root, WRITE_SECURE_SETTINGS ni Shizuku, touchez "Paramètres d'accessibilité du système" dans "Paramètres" et activez à la main le service d'AutoJs6 et 3-Adapt A11y.
4. Quittez complètement puis rouvrez une application cible répertoriée dans "Applications actuellement compatibles", puis lisez les nœuds de la page avec AutoJs6.

### Fonctionnalités principales

- **Gestionnaire du service de compatibilité**: consultez l'état actuel du service de compatibilité et du service d'AutoJs6 dans "État", et choisissez "Suivre AutoJs6", "Activé" ou "Désactivé" dans "Politique de contrôle".
- **Contrôle automatique**: après avoir accordé l'accès root, WRITE_SECURE_SETTINGS ou Shizuku, activez l'option correspondante dans "Contrôle automatique" et le plugin active ou désactive le service selon la politique choisie, en ne modifiant que la liste des services d'accessibilité activés.
- **Réparation du service arrêté**: lorsque les paramètres système affichent le service comme activé alors qu'il ne s'exécute plus, le plugin le désactive puis le réactive de la même façon; s'il ne le peut pas, la page d'accueil affiche "Le service de compatibilité ne s'exécute pas" avec un bouton "Paramètres système".
- **Profils de prise en charge**: "Applications actuellement compatibles" sur la page d'accueil liste les applications vérifiées, uniquement WeChat dans cette version; un profil ne s'applique qu'aux versions et pages documentées et ne couvre pas tous les environnements.
- **Confidentialité**: le rappel du service ne fait rien et ignore tous les événements d'accessibilité; le plugin ne lit ni le contenu des événements, ni les nœuds racines, ni les captures d'écran, ni le contenu de l'utilisateur, ne stocke et n'envoie aucune donnée et n'a pas accès au réseau.
- **Apparence et informations**: dans "Paramètres", réglez "Langue", "Mode sombre", "Couleur du thème" et "Icône du lanceur", et consultez "Mécanisme de compatibilité", "Confidentialité", "Limites" et "Historique des versions".

### Prérequis

- Android 7.0 (API 24) ou version ultérieure, et AutoJs6 6.8.0 ou version ultérieure; le service d'accessibilité d'AutoJs6 lit les nœuds et effectue les actions.
- Le service d'accessibilité de ce plugin n'est activé que comme identifiant de compatibilité; il ne reçoit que les événements des applications des profils de prise en charge et ne les traite pas.
- L'accès root, WRITE_SECURE_SETTINGS et Shizuku sont facultatifs et ne servent qu'à activer ou désactiver le service automatiquement; WRITE_SECURE_SETTINGS s'accorde par ADB ou un shell root.
- L'autorisation d'interroger le mode Protection avancée vérifie si la "Protection avancée" d'Android 17 restreint ce service; le résultat s'affiche dans "Paramètres".

### Questions fréquentes

**Les nœuds ne changent pas une fois le service activé**

Vérifiez d'abord que le service d'AutoJs6 et 3-Adapt A11y sont activés, puis quittez complètement et rouvrez l'application cible. Le résultat dépend de la version de l'application, de la page, de l'appareil et du système et n'est pas garanti; les commandes dans WebView, Canvas et les interfaces dessinées sur mesure peuvent rester inaccessibles.

**Le service est activé dans les paramètres système, mais la page d'accueil affiche "Le service de compatibilité ne s'exécute pas"**

Android ne relance pas le service après l'arrêt de son processus. Avec l'accès root, WRITE_SECURE_SETTINGS ou Shizuku, le plugin le répare automatiquement; sinon, touchez "Paramètres système" et désactivez puis réactivez 3-Adapt A11y.

**Le nom de classe du service est celui de Google Sélectionner pour prononcer**

Certaines applications décident d'exposer ou non des nœuds normaux d'après le nom de classe d'un service activé, et le plugin n'utilise ce nom de classe que comme identifiant de compatibilité déclaré. Ce plugin n'est pas Google Sélectionner pour prononcer, n'est pas affilié à Google et n'utilise aucun identifiant d'application, signature, nom ni icône de Google.

Les profils de prise en charge, le mécanisme de compatibilité et les rapports de validation se trouvent sur la [page du projet](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y).
