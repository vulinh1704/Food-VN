package com.food_vn.service.users;


import com.food_vn.model.users.Role;


public interface IRoleService {
    Iterable<Role> findAll();

    void save(Role role);

    Role findByName(String name);
}
