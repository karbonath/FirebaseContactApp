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
        setContentView(R.layout.layout3);
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etUsername = findViewById(R.id.et_username);
        etType = findViewById(R.id.et_type);
        btnSave = findViewById(R.id.btn_save);
        btnClear = findViewById(R.id.btn_clear);

        database = FirebaseDatabase.getInstance();
        contactsRef = database.getReference("contacts");

        btnSave.setOnClickListener(v -> saveContact());
        btnClear.setOnClickListener(v -> clearFields());
    }

    private void saveContact() {
        String name = etName.getText().toString();
        String phone = etPhone.getText().toString();
        String username = etUsername.getText().toString();
        String type = etType.getText().toString();

        if (!name.isEmpty() && !phone.isEmpty() && !username.isEmpty() && !type.isEmpty()) {
            String id = contactsRef.push().getKey();
            Contact contact = new Contact(name, phone, username);
            contact.setId(id);
            contact.setType(type);

            contactsRef.child(id).setValue(contact)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddContactActivity.this, "Contact saved", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddContactActivity.this, ContactListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(AddContactActivity.this, "Failed to save contact", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(AddContactActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        etName.setText("");
        etPhone.setText("");
        etUsername.setText("");
        etType.setText("");
    }
}
