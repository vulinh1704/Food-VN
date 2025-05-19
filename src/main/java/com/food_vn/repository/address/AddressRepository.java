package com.food_vn.repository.address;
import com.food_vn.model.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAlByUserId(Long id);
}
