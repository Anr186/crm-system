package com.example.crmsystem.controller;

import com.example.crmsystem.entity.Seller;
import com.example.crmsystem.service.SellerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SellerController.class)
class SellerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SellerService sellerService;

    @Test
    void getAllSellers_SHOULD_ReturnListAndOkStatus() throws Exception {
        Seller s = new Seller();
        s.setId(1L);
        s.setName("Test Seller");

        when(sellerService.getAllSellers()).thenReturn(List.of(s));

        mockMvc.perform(get("/api/sellers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Test Seller"))
                .andExpect(jsonPath("$.length()").value(1));

        verify(sellerService, times(1)).getAllSellers();
    }

    @Test
    void createSeller_SHOULD_ReturnCreatedStatus() throws Exception {
        String sellerJson = "{\"name\": \"New Seller\", \"contactInfo\": \"test@mail.com\"}";
        mockMvc.perform(post("/api/sellers").contentType(MediaType.APPLICATION_JSON).content(sellerJson)).andExpect(status().isCreated());
        verify(sellerService, times(1)).createSeller(any(Seller.class));
    }

    @Test
    void deleteSeller_SHOULD_ReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/sellers/1")).andExpect(status().isNoContent());
        verify(sellerService, times(1)).deleteSeller(1L);
    }
}