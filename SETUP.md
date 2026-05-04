# MPE Admin — Application Android

Application Android pour gérer le site Marrakech Premium Experience.

## Prérequis
- Android Studio Hedgehog (2023.1) ou plus récent
- JDK 17+
- Android SDK 35
- Un compte Firebase (déjà configuré pour ce projet)

---

## ÉTAPE 1 — Configurer Firebase pour Android

### 1.1 Accéder à la console Firebase
1. Aller sur https://console.firebase.google.com
2. Sélectionner le projet **marrakech-premium-experi-b3042**

### 1.2 Ajouter une application Android
1. Cliquer sur l'icône **Android** (Ajouter une application)
2. Remplir :
   - **Package Android** : `com.mpe.admin`
   - **Surnom** : MPE Admin (optionnel)
   - **SHA-1** : (optionnel pour commencer)
3. Cliquer **Enregistrer l'application**

### 1.3 Télécharger google-services.json
1. Télécharger le fichier `google-services.json`
2. **Remplacer** le fichier `app/google-services.json` par celui téléchargé

---

## ÉTAPE 2 — Ouvrir dans Android Studio

```
File → Open → Sélectionner le dossier mpe-android
```

Android Studio va synchroniser Gradle automatiquement.

---

## ÉTAPE 3 — Lancer l'application

### Sur émulateur
- `Run → Run 'app'` ou Shift+F10

### Sur appareil physique
1. Activer **Options développeur** (7 pressions sur "Numéro de build")
2. Activer **Débogage USB**
3. Brancher le téléphone via USB
4. `Run → Run 'app'`

---

## Identifiants de connexion

```
Mot de passe : MPEADMIN@MOUIZ
```

---

## Fonctionnalités

| Écran       | Fonctionnalité                                      |
|-------------|-----------------------------------------------------|
| Connexion   | Login sécurisé avec mot de passe                    |
| Accueil     | Statistiques (villas, apparts, véhicules) + récents |
| Villas      | Ajouter / Modifier / Supprimer des villas           |
| Appartements| Ajouter / Modifier / Supprimer des appartements     |
| Véhicules   | Ajouter / Modifier / Supprimer + filtre Luxe/Écon.  |
| Paramètres  | WhatsApp, Instagram, Snapchat, TikTok, Clé ImgBB   |

---

## Synchronisation Firebase

- Les données se synchronisent en temps réel avec Firebase Firestore
- Toute modification sur l'app Android est visible immédiatement sur le site web
- Toute modification sur le site web est visible immédiatement sur l'app

---

## Structure du projet

```
mpe-android/
├── app/
│   ├── google-services.json          ← Remplacer par le vrai fichier
│   └── src/main/java/com/mpe/admin/
│       ├── MainActivity.kt           ← Activité principale
│       ├── data/
│       │   ├── model/                ← Property, Car, AppSettings
│       │   └── repository/           ← Firebase operations
│       ├── viewmodel/                ← AdminViewModel (état global)
│       └── ui/
│           ├── theme/                ← Thème or/obsidian
│           ├── navigation/           ← Navigation + bottom bar
│           ├── screens/              ← Login, Dashboard, Villas...
│           └── components/           ← Cards, Dialogs
```
