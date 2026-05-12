package com.example.crmsystem.controller;

import com.example.crmsystem.entity.Seller;
import com.example.crmsystem.entity.Transaction;
import com.example.crmsystem.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void createTransaction_SHOULD_ReturnCreated() throws Exception {
        String json = "{\"sellerId\": 1, \"amount\": 500, \"paymentType\": \"CASH\"}";

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(transactionService, times(1)).createTransaction(any(Transaction.class));
    }

    @Test
    void getBestSeller_SHOULD_ReturnSellerInfo() throws Exception {
        Seller best = new Seller();
        best.setId(1L);
        best.setName("Champion");

        when(transactionService.getBestSeller(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(best);

        mockMvc.perform(get("/api/transactions/best-seller")
                        .param("start", "2026-01-01T00:00:00")
                        .param("end", "2026-12-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Champion"));
    }

    @Test
    void getProductiveHour_SHOULD_ReturnInteger() throws Exception {
        when(transactionService.getMostProductiveHour(1L)).thenReturn(14);

        mockMvc.perform(get("/api/transactions/seller/1/productive-hour"))
                .andExpect(status().isOk())
                .andExpect(content().string("14"));
    }
}