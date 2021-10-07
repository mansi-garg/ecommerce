package com.example.ecommerce.controller;

import com.example.ecommerce.co.RequestCO;
import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    ProductService productService;

    @ModelAttribute
    public void initBinding(Model model) throws IOException {
        model.addAttribute("brandCount", productService.getCounts("brand"));
        model.addAttribute("colorCount", productService.getCounts("dominant_color"));
        model.addAttribute("priceRange", productService.getPriceRange());
    }

    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    @PostMapping("/products")
    @ResponseBody
    public List<ProductDTO> getProduct(@RequestBody RequestCO requestCO) throws IOException {
        return productService.getProducts(requestCO);
    }

}
