package com.example.crmsystem.controller;

import com.example.crmsystem.entity.Seller;
import com.example.crmsystem.entity.Transaction;
import com.example.crmsystem.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<Transaction> getAll() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public Transaction getById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Transaction transaction) {
        transactionService.createTransaction(transaction);
    }

    @GetMapping("/seller/{sellerId}")
    public List<Transaction> getBySeller(@PathVariable Long sellerId) {
        return transactionService.getTransactionsBySeller(sellerId);
    }

    @GetMapping("/best-seller")
    public Seller getBestSeller(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return transactionService.getBestSeller(start, end);
    }

    @GetMapping("/seller/{sellerId}/productive-hour")
    public Integer getProductiveHour(@PathVariable Long sellerId) {
        return transactionService.getMostProductiveHour(sellerId);
    }
}