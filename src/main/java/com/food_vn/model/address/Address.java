package com.food_vn.model.address;
import com.food_vn.lib.base_model.BaseModel;
import com.food_vn.model.users.Role;
import com.food_vn.model.users.User;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Address extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String addressDetail;

    @Column(nullable = false)
    private boolean isDefault;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public Address(Long id, String fullName, String phone, String address, String addressDetail, boolean isDefault, User user) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.addressDetail = addressDetail;
        this.isDefault = isDefault;
        this.user = user;
    }

    public Address() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
