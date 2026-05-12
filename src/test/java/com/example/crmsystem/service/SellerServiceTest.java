package com.example.crmsystem.service;

import com.example.crmsystem.entity.Seller;
import com.example.crmsystem.repository.SellerRepository;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SellerServiceTest {

    private SellerRepository sellerRepository;
    private SellerService sellerService;

    @BeforeEach
    void setUp() {
        Jdbi jdbi = mock(Jdbi.class);
        sellerRepository = mock(SellerRepository.class);
        when(jdbi.onDemand(SellerRepository.class)).thenReturn(sellerRepository);
        sellerService = new SellerService(jdbi);
    }

    @Test
    void createSeller_EXPECT_AutoSetRegistrationDate() {
        Seller seller = new Seller();
        seller.setName("Vladimir");
        sellerService.createSeller(seller);
        assertNotNull(seller.getRegistrationDate());
    }

    @Test
    void createSeller_EXPECT_InvokeRepositoryInsert() {
        Seller seller = new Seller();
        when(sellerRepository.insert(any(Seller.class))).thenReturn(1L);
        sellerService.createSeller(seller);
        verify(sellerRepository, times(1)).insert(seller);
    }

    @Test
    void deleteSeller_EXPECT_InvokeRepositoryDelete() {
        Long sellerId = 1L;
        sellerService.deleteSeller(sellerId);
        verify(sellerRepository, times(1)).delete(sellerId);
    }
}