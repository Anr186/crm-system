package com.example.crmsystem.service;

import com.example.crmsystem.entity.Seller;
import com.example.crmsystem.entity.Transaction;
import com.example.crmsystem.repository.SellerRepository;
import com.example.crmsystem.repository.TransactionRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    private final Jdbi jdbi;
    private final TransactionRepository transactionRepository;

    public TransactionService(Jdbi jdbi) {
        this.jdbi = jdbi;
        this.transactionRepository = jdbi.onDemand(TransactionRepository.class);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public void createTransaction(Transaction transaction) {
        if (transaction.getTransactionDate() == null) {
            transaction.setTransactionDate(LocalDateTime.now());
        }
        long id = transactionRepository.insert(transaction);
        transaction.setId(id);
    }

    public List<Transaction> getTransactionsBySeller(Long sellerId) {
        return transactionRepository.findBySellerId(sellerId);
    }

    public Seller getBestSeller(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findBestSeller(start, end);
    }

    public Integer getMostProductiveHour(Long sellerId) {
        return transactionRepository.findMostProductiveHour(sellerId);
    }

    public List<Seller> getSellersWithLowSales(double threshold) {
        return jdbi.onDemand(SellerRepository.class).findSellersWithSalesLessThan(threshold);
    }
}