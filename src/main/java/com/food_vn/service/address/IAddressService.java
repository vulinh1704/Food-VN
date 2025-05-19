package com.food_vn.service.address;

import com.food_vn.model.address.Address;

import java.util.List;
import java.util.Optional;

public interface IAddressService {
    Address save(Address address) throws Exception;

    List<Address> findAll(Long userId);

    Optional<Address> findById(Long id);

    void deleteById(Long id);
}
