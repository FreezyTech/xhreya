package com.example.xhreya;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EmergencyContactsActivity extends AppCompatActivity {

    DBHelper dbHelper;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);

        dbHelper = new DBHelper(this);
        listView = findViewById(R.id.listView);

        loadContacts();

        findViewById(R.id.btnAdd).setOnClickListener(v -> showAddDialog());
    }

    // Load contacts from SQLite
    private void loadContacts() {
        ArrayList<String> contactList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllContacts();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No contacts added", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String contact =
                        cursor.getString(1) + " (" +
                                cursor.getString(2) + ")\n" +
                                cursor.getString(3);
                contactList.add(contact);
            }
        }

        cursor.close();

        listView.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                contactList
        ));
    }

    // Show add contact dialog
    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Emergency Contact");

        View view = getLayoutInflater()
                .inflate(R.layout.dialog_add_contact, null);
        builder.setView(view);

        EditText etName = view.findViewById(R.id.etName);
        EditText etRelation = view.findViewById(R.id.etRelation);
        EditText etPhone = view.findViewById(R.id.etPhone);

        builder.setPositiveButton("Save", (dialog, which) -> {

            String name = etName.getText().toString().trim();
            String relation = etRelation.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this,
                        "Name and phone are required",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = dbHelper.insertContact(name, relation, phone);

            if (inserted) {
                Toast.makeText(this,
                        "Contact added successfully",
                        Toast.LENGTH_SHORT).show();
                loadContacts();
            } else {
                Toast.makeText(this,
                        "Failed to add contact",
                        Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
