package com.food_vn.service.address.impl;

import com.food_vn.lib.base_serivce.BaseService;
import com.food_vn.model.address.Address;
import com.food_vn.repository.address.AddressRepository;
import com.food_vn.service.address.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService extends BaseService implements IAddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address save(Address address) throws Exception {
        return addressRepository.save(address);
    }

    @Override
    public List<Address> findAll(Long userId) {
        return addressRepository.findAlByUserId(userId);
    }

    @Override
    public Optional<Address> findById(Long id) {
        return addressRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }

}
