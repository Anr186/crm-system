package com.example.crmsystem.entity;

import java.time.LocalDateTime;

public class Transaction {
    private Long id;
    private Long sellerId;
    private Long amount;
    private String paymentType;
    private LocalDateTime transactionDate;

    public Transaction() {}

    public Transaction(Long id, Long sellerId, Long amount, String paymentType, LocalDateTime transactionDate) {
        this.id = id;
        this.sellerId = sellerId;
        this.amount = amount;
        this.paymentType = paymentType;
        this.transactionDate = transactionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", sellerId=" + sellerId +
                ", amount=" + amount +
                ", paymentType='" + paymentType + '\'' +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
