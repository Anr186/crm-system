package com.example.crmsystem.repository;

import com.example.crmsystem.entity.Seller;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.util.List;

@RegisterBeanMapper(Seller.class)
public interface SellerRepository {
    @SqlQuery("SELECT * FROM sellers WHERE is_deleted = false")
    List<Seller> findAll();

    @SqlQuery("SELECT * FROM sellers WHERE id = :id")
    Seller findById(long id);

    @SqlUpdate("INSERT INTO sellers (name, contact_info, registration_date) VALUES (:name, :contactInfo, :registrationDate)")
    @GetGeneratedKeys
    long insert(@BindBean Seller seller);

    @SqlUpdate("UPDATE sellers SET name = :name, contact_info = :contactInfo WHERE id = :id")
    void update(long id, @BindBean Seller seller);

    @SqlUpdate("UPDATE sellers SET is_deleted = true WHERE id = :id")
    void delete(long id);

    @SqlQuery("SELECT s.* FROM sellers s JOIN transactions t ON s.id = t.seller_id GROUP BY s.id HAVING SUM(t.amount) < :threshold")
    List<Seller> findSellersWithSalesLessThan(double threshold);
}