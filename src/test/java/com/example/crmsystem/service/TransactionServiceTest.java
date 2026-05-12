package com.example.crmsystem.service;

import com.example.crmsystem.entity.Seller;
import com.example.crmsystem.entity.Transaction;
import com.example.crmsystem.repository.SellerRepository;
import com.example.crmsystem.repository.TransactionRepository;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private TransactionRepository transactionRepository;
    private SellerRepository sellerRepository;
    private TransactionService transactionService;
    private Jdbi jdbi;

    @BeforeEach
    void setUp() {
        jdbi = mock(Jdbi.class);
        transactionRepository = mock(TransactionRepository.class);
        sellerRepository = mock(SellerRepository.class);

        when(jdbi.onDemand(TransactionRepository.class)).thenReturn(transactionRepository);
        when(jdbi.onDemand(SellerRepository.class)).thenReturn(sellerRepository);

        transactionService = new TransactionService(jdbi);
    }

    @Test
    void createTransaction_EXPECT_AutoSetTransactionDate() {
        Transaction transaction = new Transaction();
        transaction.setSellerId(1L);
        transactionService.createTransaction(transaction);
        assertNotNull(transaction.getTransactionDate());
    }

    @Test
    void getBestSeller_EXPECT_ReturnSellerFromRepository() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        Seller expected = new Seller();
        expected.setName("Best");

        when(transactionRepository.findBestSeller(start, end)).thenReturn(expected);

        Seller actual = transactionService.getBestSeller(start, end);
        assertEquals("Best", actual.getName());
    }

    @Test
    void getMostProductiveHour_EXPECT_ReturnCorrectHourValue() {
        Long sellerId = 1L;
        when(transactionRepository.findMostProductiveHour(sellerId)).thenReturn(14);

        Integer hour = transactionService.getMostProductiveHour(sellerId);
        assertEquals(14, hour);
    }

    @Test
    void getSellersWithLowSales_EXPECT_ReturnFilteredList() {
        double threshold = 10000.0;
        Seller lowSalesSeller = new Seller();
        lowSalesSeller.setName("Low Sales Seller");
        List<Seller> expectedList = Collections.singletonList(lowSalesSeller);

        when(sellerRepository.findSellersWithSalesLessThan(threshold)).thenReturn(expectedList);

        List<Seller> result = transactionService.getSellersWithLowSales(threshold);

        assertEquals(1, result.size());
        assertEquals("Low Sales Seller", result.get(0).getName());
        verify(sellerRepository).findSellersWithSalesLessThan(threshold);
    }

    @Test
    void getBestSeller_EXPECT_PassCorrectDatesToRepository() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 12, 31, 23, 59);

        transactionService.getBestSeller(start, end);

        verify(transactionRepository).findBestSeller(start, end);
    }
}