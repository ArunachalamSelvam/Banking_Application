package com.banking.service;

import java.sql.SQLException;
import java.util.List;

import com.banking.entities.Address;

public interface AddressService {

    Address saveAddress(Address address) throws SQLException;

    Address updateAddress(Integer addressId, Address address);

    List<Address> getAllAddresses();

    Address getAddressById(Integer addressId);

    int deleteAddress(Integer addressId);
}
