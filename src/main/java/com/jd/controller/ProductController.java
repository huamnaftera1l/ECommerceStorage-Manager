package com.jd.controller;

import com.jd.dto.ProductDTO;
import com.jd.model.Product;
import com.jd.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private ProductService productService;

    //@PreAuthorize("hasRole('ADMIN') or hasRole('STOCKER')")
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductDTO productDto) {
        Product newProduct = productService.addProduct(productDto);
        logger.info("有商品添加");
        return ResponseEntity.ok(newProduct);
    }

    //@PreAuthorize("hasAnyRole('ADMIN', 'STOCKER', 'UNSTOCKER')")
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        logger.info("用户申请了一次查看所有商品");
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    //@PreAuthorize("hasAnyRole('ADMIN', 'STOCKER', 'UNSTOCKER')")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        logger.info("用户查找了商品信息："+id);
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('STOCKER')")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody ProductDTO productDto) {
        logger.info("进行了一次商品信息更新");
        Product updatedProduct = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        logger.info("删除了商品："+id);
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('STOCKER')")
    @PostMapping("/{id}/stock")
    public ResponseEntity<Product> stockProduct(@PathVariable int id, @RequestParam int quantity) {
        logger.info("进行了一次上架（商品，数量）:"+id+", "+quantity);
        Product product = productService.stockProduct(id, quantity);
        return ResponseEntity.ok(product);
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('UNSTOCKER')")
    @PostMapping("/{id}/unstock")
    public ResponseEntity<Product> unstockProduct(@PathVariable int id, @RequestParam int quantity) {
        logger.info("进行了一次下架商品（商品，数量）:"+id+", "+quantity);
        Product product = productService.unstockProduct(id, quantity);
        return ResponseEntity.ok(product);
    }
}
