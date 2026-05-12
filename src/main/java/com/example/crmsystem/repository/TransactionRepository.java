package com.example.crmsystem.repository;

import com.example.crmsystem.entity.Seller;
import com.example.crmsystem.entity.Transaction;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.time.LocalDateTime;
import java.util.List;

@RegisterBeanMapper(Transaction.class)
public interface TransactionRepository {
    @SqlQuery("SELECT * FROM transactions")
    List<Transaction> findAll();

    @SqlQuery("SELECT * FROM transactions WHERE id = :id")
    Transaction findById(long id);

    @SqlUpdate("INSERT INTO transactions (seller_id, amount, payment_type, transaction_date) VALUES (:sellerId, :amount, :paymentType, :transactionDate)")
    @GetGeneratedKeys
    long insert(@BindBean Transaction transaction);

    @SqlQuery("SELECT * FROM transactions WHERE seller_id = :sellerId")
    List<Transaction> findBySellerId(long sellerId);

    @SqlQuery("SELECT s.* FROM sellers s JOIN transactions t ON s.id = t.seller_id WHERE t.transaction_date BETWEEN :start AND :end GROUP BY s.id ORDER BY SUM(t.amount) DESC LIMIT 1")
    @RegisterBeanMapper(Seller.class)
    Seller findBestSeller(LocalDateTime start, LocalDateTime end);

    @SqlQuery("SELECT EXTRACT(HOUR FROM transaction_date) FROM transactions WHERE seller_id = :sellerId GROUP BY 1 ORDER BY SUM(amount) DESC LIMIT 1")
    Integer findMostProductiveHour(long sellerId);
}