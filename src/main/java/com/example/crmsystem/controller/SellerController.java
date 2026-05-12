package com.example.crmsystem.controller;

import com.example.crmsystem.entity.Seller;
import com.example.crmsystem.service.SellerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {
    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @GetMapping
    public List<Seller> getAll() {
        return sellerService.getAllSellers();
    }

    @GetMapping("/{id}")
    public Seller getById(@PathVariable Long id) {
        return sellerService.getSellerById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Seller seller) {
        sellerService.createSeller(seller);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Seller seller) {
        sellerService.updateSeller(id, seller);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        sellerService.deleteSeller(id);
    }

    @GetMapping("/low-sales")
    public List<Seller> getLowSales(@RequestParam double threshold) {
        return sellerService.getSellersWithLowSales(threshold);
    }
}