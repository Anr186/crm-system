package com.example.crmsystem.service;

import com.example.crmsystem.entity.Seller;
import com.example.crmsystem.repository.SellerRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SellerService {
    private final SellerRepository repository;

    public SellerService(Jdbi jdbi) {
        this.repository = jdbi.onDemand(SellerRepository.class);
    }

    public List<Seller> getAllSellers() {
        return repository.findAll();
    }

    public Seller getSellerById(Long id) {
        return repository.findById(id);
    }

    public void createSeller(Seller seller) {
        if (seller.getRegistrationDate() == null) seller.setRegistrationDate(LocalDateTime.now());
        long id = repository.insert(seller);
        seller.setId(id);
    }

    public void updateSeller(Long id, Seller seller) {
        repository.update(id, seller);
    }

    public void deleteSeller(Long id) {
        repository.delete(id);
    }

    public List<Seller> getSellersWithLowSales(double threshold) {
        return repository.findSellersWithSalesLessThan(threshold);
    }
}