# ContactSyncApp Backend

This folder contains a small PHP/MySQL API for the Android app.

## Setup

1. Import `database.sql` in MySQL.
2. Copy `server-api` into your local PHP server folder, for example:
   `C:\xampp\htdocs\contactsync\server-api`
3. If your MySQL credentials are not `root` with an empty password, edit:
   `server-api/db/ConnectionProvider.php`
4. Confirm these URLs work in a browser:
   `http://localhost/contactsync/server-api/endpoints/list_entries.php`
   `http://localhost/contactsync/server-api/endpoints/find_entries.php?q=test`

The Android emulator reaches your computer as `10.0.2.2`, so the app currently uses:
`http://10.0.2.2/contactsync/server-api/endpoints/`

If you test on a real phone, replace `BASE_URL` in `ServiceFactory.java` with your computer LAN IP address, for example:
`http://192.168.1.20/contactsync/server-api/endpoints/`
