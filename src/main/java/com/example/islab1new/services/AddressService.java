package com.example.islab1new.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import com.example.islab1new.models.Address;
import com.example.islab1new.dao.AddressDAO;
import java.util.List;

@ApplicationScoped
public class AddressService {

    @Inject
    private AddressDAO AddressDAO;

    public void addAddress(Address address) {
        AddressDAO.save(address);
    }

    public void updateAddress(Address address){
        AddressDAO.update(address);
    }

    public Address getAddressById(Integer id) {
        return AddressDAO.findById(id);
    }

    public List<Address> getAllAddresses() {
        return AddressDAO.findAll();
    }

    public void removeAddress(Integer id) {
        AddressDAO.delete(id);
    }
}