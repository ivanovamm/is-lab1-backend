package com.example.islab1new.services;

import com.example.islab1new.models.history.AddressHistory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import com.example.islab1new.models.Address;
import com.example.islab1new.dao.AddressDAO;
import jakarta.servlet.http.Part;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.InputStream;
import java.util.List;

@ApplicationScoped
public class AddressService {

    @Inject
    private AddressDAO AddressDAO;

    public void addAddress(Address address, Integer userId) {
        AddressDAO.save(address, userId);
    }

    public List<AddressHistory> allHistory(){
        return AddressDAO.findAllHistory();
    }

    public void updateAddress(Address address, Integer userId){
        AddressDAO.update(address, userId);
    }

    public Address getAddressById(Integer id) {
        return AddressDAO.findById(id);
    }

    public List<Address> getAllAddresses() {
        return AddressDAO.findAll();
    }

    public void removeAddress(Integer id, Integer userId) {
        AddressDAO.delete(id, userId);
    }

    public int importAddresses(InputStream file, Integer userId) throws Exception {
        return AddressDAO.importAddresses(file, userId);
    }
}