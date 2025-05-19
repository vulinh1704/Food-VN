package com.food_vn.model.customers;
import com.food_vn.lib.base_model.BaseModel;
import com.food_vn.model.users.User;
import jakarta.persistence.*;

@Entity
public class Customer extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private boolean isDefault;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public Customer(Long id, String name, String phone, String address, boolean isDefault, User user) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isDefault = isDefault;
        this.user = user;
    }

    public Customer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
