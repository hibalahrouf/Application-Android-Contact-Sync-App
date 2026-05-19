# Application Android — Contact Sync App

## Présentation

Ce projet consiste à développer une application Android permettant :

- de lire les contacts enregistrés dans le téléphone ;
- d’afficher les contacts dans une interface moderne avec RecyclerView ;
- de synchroniser les contacts vers un serveur distant ;
- d’effectuer une recherche distante par nom ou numéro de téléphone.

Le projet combine Android, Retrofit, PHP et MySQL dans une architecture client/serveur complète.

---

# Fonctionnalités principales

- Lecture des contacts du téléphone
- Gestion des permissions Android
- Affichage des contacts dans RecyclerView
- Synchronisation vers un backend distant
- Recherche distante via API REST
- Communication HTTP avec Retrofit
- Réponses JSON automatiques
- Stockage des données dans MySQL

---

# Technologies utilisées

## Android

- Java
- Android Studio
- RecyclerView
- Retrofit
- Gson Converter
- ContentResolver

## Backend

- PHP
- MySQL
- PDO
- API REST JSON

---

# Structure du projet Android

```text
com.example.contactsyncapp
│
├── models
│   ├── Contact.java
│   └── ApiResponse.java
│
├── network
│   ├── ContactApi.java
│   └── ServiceFactory.java
│
├── ui
│   ├── MainActivity.java
│   └── ContactAdapter.java
│
└── res/layout
    └── activity_main.xml
```

---

# Structure du backend

```text
server-api
│
├── config
│   └── Database.php
│
├── services
│   └── ContactManager.php
│
├── endpoints
│   ├── add_entry.php
│   ├── list_entries.php
│   └── find_entries.php
│
└── database.sql
```

---

# Base de données

## Nom de la base

```sql
contactsync_lab
```

## Table utilisée

```sql
contacts
```

La table stocke :

- id
- name
- phone
- source
- created_at

---

# Permissions Android utilisées

Dans `AndroidManifest.xml` :

```xml
<uses-permission android:name="android.permission.READ_CONTACTS"/>
<uses-permission android:name="android.permission.INTERNET"/>
```

---

# Fonctionnement général

## 1. Chargement des contacts

L’application utilise :

```java
ContentResolver
```

pour accéder aux contacts du téléphone via :

```java
ContactsContract.CommonDataKinds.Phone
```

Les contacts récupérés sont ensuite affichés dans un RecyclerView.

---

## 2. Synchronisation distante

Lorsque l’utilisateur clique sur :

```text
Sync
```

les contacts sont envoyés vers le backend PHP via Retrofit.

Chaque contact est converti automatiquement en JSON.

---

## 3. Recherche distante

L’utilisateur peut rechercher :

- un nom ;
- ou un numéro.

Une requête HTTP est envoyée au backend, puis les résultats JSON sont affichés dans le RecyclerView.

---

# Communication réseau

## Retrofit

L’application utilise Retrofit pour :

- envoyer les requêtes HTTP ;
- convertir automatiquement JSON ↔ objets Java ;
- gérer les appels asynchrones.

---

# Tests réalisés

## Test 1 — Permission Android

- demande de permission READ_CONTACTS ;
- validation correcte ;
- chargement des contacts.

Résultat : les contacts apparaissent dans la liste.

---

## Test 2 — Affichage RecyclerView

Les contacts sont affichés avec :

- nom ;
- numéro de téléphone.

Résultat : affichage fluide et correct.

---

## Test 3 — Synchronisation serveur

Après clic sur :

```text
Sync
```

les contacts sont insérés dans MySQL.

Résultat : données présentes dans la table `contacts`.

---

## Test 4 — Recherche distante

Recherche par :

- nom ;
- numéro.

Résultat : seuls les contacts correspondants sont affichés.

---


# Démonstration vidéo

https://github.com/user-attachments/assets/727da2fb-9bd5-4d91-a173-6028bd350458

https://github.com/user-attachments/assets/a612cde4-87a9-4865-8b33-a5ff366dbeed

---

# Résultats obtenus

- Lecture correcte des contacts Android
- Gestion fonctionnelle des permissions
- Communication HTTP réussie
- Synchronisation vers MySQL opérationnelle
- Recherche distante fonctionnelle
- Conversion JSON automatique avec Retrofit

---

