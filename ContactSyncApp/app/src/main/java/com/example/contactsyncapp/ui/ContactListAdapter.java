package com.example.contactsyncapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsyncapp.R;
import com.example.contactsyncapp.data.PhoneEntry;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.RowHolder> {
    private final List<PhoneEntry> rows = new ArrayList<>();

    public void replaceAll(List<PhoneEntry> freshRows) {
        rows.clear();
        rows.addAll(freshRows);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new RowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RowHolder holder, int position) {
        PhoneEntry entry = rows.get(position);
        holder.nameText.setText(entry.getDisplayName());
        holder.phoneText.setText(entry.getPhoneValue());
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    static class RowHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView phoneText;

        RowHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            phoneText = itemView.findViewById(R.id.phoneText);
        }
    }
}
