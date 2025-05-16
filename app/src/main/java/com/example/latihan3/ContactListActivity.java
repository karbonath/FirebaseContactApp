package com.example.latihan3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference contactsRef;
    private List<Contact> contactList;
    private ContactAdapter contactAdapter;
    private LinearLayout emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout2);  // Ganti dengan layout yang benar

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        emptyView = findViewById(R.id.empty_view);
        contactList = new ArrayList<>();

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        contactsRef = database.getReference("contacts");

        // Setup adapter for RecyclerView
        contactAdapter = new ContactAdapter(contactList);
        recyclerView.setAdapter(contactAdapter);

        // Button to add a new contact
        Button btnAddContact = findViewById(R.id.btn_add_contact);  // Button to add contact
        btnAddContact.setOnClickListener(v -> {
            // Navigate to AddContactActivity
            startActivity(new Intent(ContactListActivity.this, AddContactActivity.class));
        });

        // Fetch contacts from Firebase
        fetchContacts();
    }

    private void fetchContacts() {
        contactsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contactList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Contact contact = snapshot.getValue(Contact.class);
                    if (contact != null) {
                        // Simpan ID kontak dengan jelas
                        String contactId = snapshot.getKey();
                        contact.setId(contactId);

                        // Tambahkan log untuk verifikasi ID
                        System.out.println("Contact loaded: " + contact.getName() + ", ID: " + contact.getId());

                        contactList.add(contact);
                    }
                }
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ContactListActivity.this, "Failed to load contacts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        if (contactList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        contactAdapter.notifyDataSetChanged();
    }

    // Contact Adapter for RecyclerView
    public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

        private final List<Contact> contactList;

        public ContactAdapter(List<Contact> contactList) {
            this.contactList = contactList;
        }

        @NonNull
        @Override
        public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
            return new ContactViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
            Contact contact = contactList.get(position);
            holder.tvName.setText(contact.getName());
            holder.tvPhone.setText(contact.getPhone());
            holder.tvUsername.setText(contact.getUsername());

            // Log untuk debugging
            final String contactId = contact.getId();
            System.out.println("Binding contact: " + contact.getName() + ", ID: " + contactId);

            holder.btnDelete.setOnClickListener(v -> {
                // Handle delete functionality
                if (contactId != null && !contactId.isEmpty()) {
                    // Simpan posisi untuk keamanan (menghindari perubahan posisi)
                    final int deletePosition = holder.getAdapterPosition();
                    if (deletePosition != RecyclerView.NO_POSITION) {
                        // Hapus di database
                        contactsRef.child(contactId).removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(ContactListActivity.this, "Contact deleted successfully", Toast.LENGTH_SHORT).show();
                                    // Pembaruan UI akan terjadi otomatis melalui ValueEventListener
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(ContactListActivity.this,
                                            "Failed to delete contact: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                } else {
                    Toast.makeText(ContactListActivity.this,
                            "Contact ID is invalid: " + (contactId == null ? "null" : "empty"), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return contactList.size();
        }

        public class ContactViewHolder extends RecyclerView.ViewHolder {

            TextView tvName, tvPhone, tvUsername;
            Button btnDelete;

            public ContactViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_name);
                tvPhone = itemView.findViewById(R.id.tv_phone);
                tvUsername = itemView.findViewById(R.id.tv_username);
                btnDelete = itemView.findViewById(R.id.btn_delete);
            }
        }
    }
}
