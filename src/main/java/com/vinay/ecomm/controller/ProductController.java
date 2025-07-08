package com.vinay.ecomm.controller;

import com.vinay.ecomm.entity.Product;
import com.vinay.ecomm.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
//@CrossOrigin("*")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public List<Product> getAllProducts(HttpServletRequest request)
    {
        System.out.println("Authenticated user: " + request.getUserPrincipal());
        return productService.getAllProducts();
    }


    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id)
    {
        return productService.getProductById(id);
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product)
    {
        return productService.addProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id)
    {
        productService.deleteProduct(id);
    }
}
