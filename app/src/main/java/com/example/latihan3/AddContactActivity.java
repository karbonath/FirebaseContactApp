package com.example.latihan3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddContactActivity extends AppCompatActivity {

    private EditText etName, etPhone, etUsername, etType;
    private Button btnSave, btnClear;

    private FirebaseDatabase database;
    private DatabaseReference contactsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout3);  // Pastikan layout ini sudah benar

        // Initialize views
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etUsername = findViewById(R.id.et_username);
        etType = findViewById(R.id.et_type);  // Inisialisasi etType
        btnSave = findViewById(R.id.btn_save);
        btnClear = findViewById(R.id.btn_clear);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        contactsRef = database.getReference("contacts");

        // Set up Save Button
        btnSave.setOnClickListener(v -> saveContact());

        // Set up Clear Button
        btnClear.setOnClickListener(v -> clearFields());
    }


    private void saveContact() {
        String name = etName.getText().toString();
        String phone = etPhone.getText().toString();
        String username = etUsername.getText().toString();
        String type = etType.getText().toString();

        // Check if any fields are empty
        if (!name.isEmpty() && !phone.isEmpty() && !username.isEmpty() && !type.isEmpty()) {
            String id = contactsRef.push().getKey();  // Generate a new unique ID for the contact

            // Buat objek Contact dan set propertinya satu per satu
            Contact contact = new Contact(name, phone, username);
            contact.setId(id);
            contact.setType(type); // Tambahkan type secara terpisah

            // atau jika menggunakan konstruktor 5 parameter yang sudah ditambahkan:
            // Contact contact = new Contact(id, name, phone, username, type);

            // Save the contact to Firebase
            contactsRef.child(id).setValue(contact)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // If successful, show a success message
                            Toast.makeText(AddContactActivity.this, "Contact saved", Toast.LENGTH_SHORT).show();

                            // Navigate to the ContactListActivity
                            Intent intent = new Intent(AddContactActivity.this, ContactListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the activity stack
                            startActivity(intent);
                            finish();  // Close AddContactActivity
                        } else {
                            // If failed, show an error message
                            Toast.makeText(AddContactActivity.this, "Failed to save contact", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // If any field is empty, show a message
            Toast.makeText(AddContactActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }


    private void clearFields() {
        // Clear all input fields
        etName.setText("");
        etPhone.setText("");
        etUsername.setText("");
        etType.setText("");
    }
}
