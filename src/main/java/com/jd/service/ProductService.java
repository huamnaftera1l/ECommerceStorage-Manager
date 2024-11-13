package com.jd.service;

import com.jd.dto.ProductDTO;
import com.jd.exception.InsufficientCapacityException;
import com.jd.exception.InsufficientStockException;
import com.jd.exception.InventoryException;
import com.jd.exception.ResourceNotFoundException;
import com.jd.model.Product;
import com.jd.model.Shelf;
import com.jd.repository.ProductRepository;
import com.jd.repository.ShelfRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Product getProductWithShelf(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Hibernate.initialize(product.getShelf());
        return product;
    }

    @Transactional
    public Product updateProduct(int id, ProductDTO productDto) {
        Product product = getProductById(id);
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(int id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    @Transactional
    public Product stockProduct(int id, int quantity) {
        Product product = getProductById(id);
        product.setQuantity(product.getQuantity() + quantity);
        return productRepository.save(product);
    }

    @Transactional
    public Product unstockProduct(int id, int quantity) {
        Product product = getProductById(id);
        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock for product with id: " + id);
        }
        product.setQuantity(product.getQuantity() - quantity);
        return productRepository.save(product);
    }

    @Autowired
    private ShelfRepository shelfRepository;

    @Transactional
    public Product addProduct(ProductDTO productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());

        if (productDto.getShelfId() != null) {
            Shelf shelf = shelfRepository.findById(productDto.getShelfId())
                    .orElseThrow(() -> new InventoryException("Shelf not found"));
            product.setShelf(shelf);
        }

        return productRepository.save(product);
    }

    @Transactional
    public Product stockProductToShelf(int productId, int shelfId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Shelf shelf = shelfRepository.findById(shelfId)
                .orElseThrow(() -> new ResourceNotFoundException("Shelf not found"));

        if (shelf.getAvailableCapacity() < quantity) {
            throw new InsufficientCapacityException("Not enough capacity on the shelf");
        }

        product.setQuantity(product.getQuantity() + quantity);
        shelf.setAvailableCapacity(shelf.getAvailableCapacity() - quantity);

        productRepository.save(product);
        shelfRepository.save(shelf);

        return product;
    }

    @Transactional
    public Product unstockProductToContainer(int productId, int containerId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException("Not enough stock to unstock");
        }

        product.setQuantity(product.getQuantity() - quantity);


        return productRepository.save(product);
    }
}
