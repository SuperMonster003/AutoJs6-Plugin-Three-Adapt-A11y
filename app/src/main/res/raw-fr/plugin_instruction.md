# Compatibilité d'accessibilité AutoJs6

## Objectif

Ce compagnon expérimental et facultatif peut aider AutoJs6 à obtenir des noeuds d'accessibilité plus normaux depuis les applications couvertes par des profils de support publiés. Le service d'accessibilité de AutoJs6 reste responsable de l'automatisation.

## Activer le service

1. Installez AutoJs6 et ce compagnon.
2. Activez manuellement le service AutoJs6 et Compatibilité d'accessibilité AutoJs6 dans les paramètres d'accessibilité Android.
3. Fermez complètement puis rouvrez une application cible de la liste de support, et inspectez plusieurs fois la même page statique.
4. Désactivez ce service dans les paramètres système lorsque la compatibilité n'est pas nécessaire.

Android ne permet pas à une application ordinaire d'activer silencieusement un service d'accessibilité. Ce compagnon ne tente pas de contourner cette restriction.

## Mécanisme de compatibilité expérimental

Certaines applications affectées peuvent décider de publier les noeuds normaux selon le nom de classe d'implémentation d'un service activé. Ce compagnon contient le nom de classe de compatibilité suivant, vérifié expérimentalement et requis par ses profils actuels.

`com.google.android.accessibility.selecttospeak.SelectToSpeakService`

Ce compagnon ne correspond pas à Google Select to Speak, ne dépend pas de Google et ne copie aucun identifiant d'application, signature, nom ou icône de Google. Le nom de classe sert uniquement d'identifiant de compatibilité divulgué et peut cesser de fonctionner après une mise à jour de l'application cible.

## Profils actuellement pris en charge

- WeChat (`com.tencent.mm`) est le seul profil vérifié dans cette version.
- Un profil ne s'applique qu'aux versions et pages documentées. Son inclusion ne garantit pas tous les environnements.

## Confidentialité

- Le rappel du service est sans effet et ignore tous les événements d'accessibilité.
- Le compagnon ne lit pas les sources ou les textes des événements, les noeuds racines, les captures d'écran ou le contenu utilisateur.
- Le compagnon ne stocke, ne partage et ne transfère aucune donnée, et ne se connecte pas au réseau.
- Le service reçoit uniquement les événements des identifiants de paquet figurant dans les profils publiés.

AutoJs6 continue à lire et à manipuler les noeuds avec son propre service d'accessibilité. Ce compagnon ne relaie pas un arbre de noeuds et n'effectue aucune action d'automatisation.

## Limites et risques

La compatibilité dépend de la version et de la page de l'application cible, de l'appareil et de l'implémentation système. Le succès n'est pas garanti. Ce compagnon ne peut pas créer les noeuds sémantiques que la page ne publie pas. Les contrôles de WebView, de Canvas et des interfaces dessinées sur mesure peuvent rester indisponibles.

Une application cible peut modifier sa méthode de détection et rendre ce compagnon inefficace. L'automatisation peut aussi être soumise aux règles de la plateforme cible ou aux contrôles de risque du compte. Évaluez le risque sur votre appareil et votre compte.
