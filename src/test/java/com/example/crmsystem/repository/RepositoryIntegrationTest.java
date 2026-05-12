package com.example.crmsystem.repository;

import com.example.crmsystem.entity.Seller;
import com.example.crmsystem.entity.Transaction;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RepositoryIntegrationTest {

    @Autowired
    private Jdbi jdbi;

    @Test
    void insertSeller_EXPECT_GeneratedIdInH2() {
        SellerRepository repo = jdbi.onDemand(SellerRepository.class);
        Seller seller = new Seller();
        seller.setName("H2 Seller");
        seller.setRegistrationDate(LocalDateTime.now());

        long id = repo.insert(seller);

        assertTrue(id > 0);
    }

    @Test
    void findSellersWithSalesLessThan_EXPECT_CorrectFilteringInH2() {
        SellerRepository sellerRepo = jdbi.onDemand(SellerRepository.class);
        TransactionRepository transRepo = jdbi.onDemand(TransactionRepository.class);

        Seller s = new Seller();
        s.setName("Weak Seller");
        long sId = sellerRepo.insert(s);

        Transaction t = new Transaction();
        t.setSellerId(sId);
        t.setAmount((long) 100.0);
        t.setPaymentType("CASH");
        t.setTransactionDate(LocalDateTime.now());
        transRepo.insert(t);

        List<Seller> result = sellerRepo.findSellersWithSalesLessThan(500.0);

        assertFalse(result.isEmpty());
    }

    @Test
    void findBestSeller_EXPECT_CorrectChampionInH2() {
        SellerRepository sellerRepo = jdbi.onDemand(SellerRepository.class);
        TransactionRepository transRepo = jdbi.onDemand(TransactionRepository.class);

        Seller s1 = new Seller(); s1.setName("Leader"); s1.setRegistrationDate(LocalDateTime.now());
        Seller s2 = new Seller(); s2.setName("Laggard"); s2.setRegistrationDate(LocalDateTime.now());
        long id1 = sellerRepo.insert(s1);
        long id2 = sellerRepo.insert(s2);

        Transaction t1 = new Transaction();
        t1.setSellerId(id1);
        t1.setAmount((long) 1000.0);
        t1.setPaymentType("CASH");
        t1.setTransactionDate(LocalDateTime.now());

        Transaction t2 = new Transaction();
        t2.setSellerId(id2);
        t2.setAmount((long) 10.0);
        t2.setPaymentType("CASH");
        t2.setTransactionDate(LocalDateTime.now());

        transRepo.insert(t1);
        transRepo.insert(t2);

        Seller best = transRepo.findBestSeller(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));

        assertNotNull(best);
        assertEquals("Leader", best.getName());
    }

    @Test
    void findMostProductiveHour_EXPECT_CorrectHourValueInH2() {
        SellerRepository sellerRepo = jdbi.onDemand(SellerRepository.class);
        TransactionRepository transRepo = jdbi.onDemand(TransactionRepository.class);

        Seller s = new Seller(); s.setName("Timer"); s.setRegistrationDate(LocalDateTime.now());
        long sId = sellerRepo.insert(s);

        Transaction t = new Transaction();
        t.setSellerId(sId);
        t.setAmount((long) 500.0);
        t.setPaymentType("CASH");
        t.setTransactionDate(LocalDateTime.now().withHour(14));
        transRepo.insert(t);

        Integer hour = transRepo.findMostProductiveHour(sId);

        assertEquals(14, hour);
    }

    @Test
    void findAll_EXPECT_ReturnAllNonDeletedSellers() {
        SellerRepository repo = jdbi.onDemand(SellerRepository.class);

        Seller s1 = new Seller();
        s1.setName("Seller A");
        s1.setRegistrationDate(LocalDateTime.now());

        Seller s2 = new Seller();
        s2.setName("Seller B");
        s2.setRegistrationDate(LocalDateTime.now());

        repo.insert(s1);
        repo.insert(s2);

        List<Seller> allSellers = repo.findAll();

        assertTrue(allSellers.size() >= 2);
        assertTrue(allSellers.stream().anyMatch(s -> s.getName().equals("Seller A")));
        assertTrue(allSellers.stream().anyMatch(s -> s.getName().equals("Seller B")));
    }

    @Test
    void deleteById_EXPECT_SellerIsMarkedAsDeleted() {
        SellerRepository repo = jdbi.onDemand(SellerRepository.class);

        Seller seller = new Seller();
        seller.setName("To Be Deleted");
        seller.setRegistrationDate(LocalDateTime.now());
        long id = repo.insert(seller);

        repo.delete(id);

        List<Seller> activeSellers = repo.findAll();
        boolean existsInActive = activeSellers.stream().anyMatch(s -> s.getId() == id);
        assertFalse(existsInActive, "Удаленный продавец не должен быть в списке активных");

        Boolean isDeleted = jdbi.withHandle(handle ->
                handle.createQuery("SELECT is_deleted FROM sellers WHERE id = :id")
                        .bind("id", id)
                        .mapTo(Boolean.class)
                        .one()
        );
        assertTrue(isDeleted, "Флаг is_deleted должен быть true");
    }
}