package com.example.latihan3;

public class Contact {
    private String id;
    private String name;
    private String phone;
    private String username;
    private String type;

    // Konstruktor kosong diperlukan untuk Firebase
    public Contact() {
        // Diperlukan untuk deserialisasi Firebase
    }

    public Contact(String name, String phone, String username) {
        this.name = name;
        this.phone = phone;
        this.username = username;
    }

    // Konstruktor dengan 5 parameter termasuk id dan type
    public Contact(String id, String name, String phone, String username, String type) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.username = username;
        this.type = type;
    }

    // Getter dan Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}