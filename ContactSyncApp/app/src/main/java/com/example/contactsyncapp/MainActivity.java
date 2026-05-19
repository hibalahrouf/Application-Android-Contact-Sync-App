package com.example.contactsyncapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsyncapp.data.ApiResult;
import com.example.contactsyncapp.data.PhoneEntry;
import com.example.contactsyncapp.network.ContactGateway;
import com.example.contactsyncapp.network.ServiceFactory;
import com.example.contactsyncapp.ui.ContactListAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private final List<PhoneEntry> deviceContacts = new ArrayList<>();
    private final ContactListAdapter adapter = new ContactListAdapter();
    private ContactGateway api;
    private TextView statusText;
    private EditText searchInput;
    private Button syncButton;

    private final ActivityResultLauncher<String> contactPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    readPhoneBook();
                } else {
                    setStatus("Contact permission is needed to read the phone list.");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = ServiceFactory.contacts();
        statusText = findViewById(R.id.statusText);
        searchInput = findViewById(R.id.searchInput);
        syncButton = findViewById(R.id.syncButton);
        Button searchButton = findViewById(R.id.searchButton);
        Button reloadButton = findViewById(R.id.reloadButton);

        RecyclerView contactList = findViewById(R.id.contactList);
        contactList.setLayoutManager(new LinearLayoutManager(this));
        contactList.setAdapter(adapter);

        syncButton.setOnClickListener(view -> pushContactsToServer());
        searchButton.setOnClickListener(view -> runRemoteSearch());
        reloadButton.setOnClickListener(view -> ensurePermissionThenLoad());
        searchInput.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                runRemoteSearch();
                return true;
            }
            return false;
        });

        ensurePermissionThenLoad();
    }

    private void ensurePermissionThenLoad() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            readPhoneBook();
        } else {
            contactPermission.launch(Manifest.permission.READ_CONTACTS);
        }
    }

    private void readPhoneBook() {
        Map<String, PhoneEntry> uniqueRows = new LinkedHashMap<>();
        String[] columns = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        try (Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                columns,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")) {

            if (cursor != null) {
                int nameColumn = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int phoneColumn = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);

                while (cursor.moveToNext()) {
                    String name = cursor.getString(nameColumn);
                    String phone = cursor.getString(phoneColumn);
                    if (isBlank(name) || isBlank(phone)) {
                        continue;
                    }
                    uniqueRows.put(name.trim() + "|" + phone.trim(), new PhoneEntry(name.trim(), phone.trim()));
                }
            }
        }

        deviceContacts.clear();
        deviceContacts.addAll(uniqueRows.values());
        adapter.replaceAll(deviceContacts);
        setStatus(deviceContacts.size() + " phone contacts loaded.");
    }

    private void pushContactsToServer() {
        if (deviceContacts.isEmpty()) {
            showToast("No phone contacts to sync.");
            return;
        }

        syncButton.setEnabled(false);
        syncNextContact(0, 0);
    }

    private void syncNextContact(int index, int sentCount) {
        if (index >= deviceContacts.size()) {
            syncButton.setEnabled(true);
            setStatus(sentCount + " of " + deviceContacts.size() + " contacts synced.");
            return;
        }

        api.sendContact(deviceContacts.get(index)).enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                int nextCount = sentCount;
                if (response.isSuccessful() && response.body() != null && response.body().isSuccessful()) {
                    nextCount++;
                }
                setStatus("Syncing " + (index + 1) + " / " + deviceContacts.size());
                syncNextContact(index + 1, nextCount);
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable throwable) {
                syncButton.setEnabled(true);
                setStatus("Sync stopped: " + throwable.getMessage());
            }
        });
    }

    private void runRemoteSearch() {
        String query = searchInput.getText().toString().trim();
        if (query.isEmpty()) {
            loadRemoteDirectory();
            return;
        }

        setStatus("Searching server...");
        api.searchStoredContacts(query).enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                handleRemoteList(response, "Search complete");
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable throwable) {
                setStatus("Search failed: " + throwable.getMessage());
            }
        });
    }

    private void loadRemoteDirectory() {
        setStatus("Loading server contacts...");
        api.loadStoredContacts().enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                handleRemoteList(response, "Server list loaded");
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable throwable) {
                setStatus("Server load failed: " + throwable.getMessage());
            }
        });
    }

    private void handleRemoteList(Response<ApiResult> response, String prefix) {
        if (!response.isSuccessful() || response.body() == null) {
            setStatus("Server returned HTTP " + response.code());
            return;
        }

        List<PhoneEntry> results = response.body().getContacts();
        adapter.replaceAll(results);
        setStatus(prefix + ": " + results.size() + " result(s).");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void setStatus(String message) {
        statusText.setText(message);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
