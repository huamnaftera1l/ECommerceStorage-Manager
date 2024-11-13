package com.jd.service;

import com.jd.exception.InsufficientCapacityException;
import com.jd.exception.InsufficientStockException;
import com.jd.exception.InventoryException;
import com.jd.exception.ResourceNotFoundException;
import com.jd.model.*;
import com.jd.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class InventoryService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShelfRepository shelfRepository;

    @Autowired
    private StockTaskRepository stockTaskRepository;

    @Autowired
    private UnstockTaskRepository unstockTaskRepository;
    @Autowired
    private ContainerRepository containerRepository;

    @Transactional
    public Product stockProduct(Integer productId, Integer shelfId, Integer quantity, Integer createdBy, Integer assignedTo) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Shelf shelf = shelfRepository.findById(shelfId)
                .orElseThrow(() -> new ResourceNotFoundException("Shelf not found"));

        if (shelf.getAvailableCapacity() < quantity) {
            throw new InsufficientCapacityException("Not enough capacity on the shelf");
        }

        // 更新产品库存
        product.setQuantity(product.getQuantity() + quantity);
        product.setShelf(shelf);
        Product updatedProduct = productRepository.save(product);

        // 更新货架可用容量
        shelf.setAvailableCapacity(shelf.getAvailableCapacity() - quantity);
        shelfRepository.save(shelf);

        // 创建 StockTask
        StockTask stockTask = new StockTask();
        stockTask.setProductId(productId);
        stockTask.setShelfId(shelfId);
        stockTask.setQuantity(quantity);
        stockTask.setStatus(StockTask.TaskStatus.COMPLETED);
        stockTask.setCreatedBy(createdBy);
        stockTask.setAssignedTo(assignedTo);
        stockTask.setCreatedAt(LocalDateTime.now());
        stockTask.setUpdatedAt(LocalDateTime.now());

        stockTaskRepository.save(stockTask);

        return updatedProduct;
    }

    @Transactional(rollbackFor = Exception.class)
    public Product unstockProduct(Integer productId, Integer shelfId, Integer containerId, Integer quantity, Integer createdBy, Integer assignedTo) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Shelf shelf = shelfRepository.findById(shelfId)
                .orElseThrow(() -> new ResourceNotFoundException("Shelf not found"));

        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException("Not enough quantity to unstock");
        }

        // 更新产品库存
        product.setQuantity(product.getQuantity() - quantity);
        Product updatedProduct = productRepository.save(product);

        // 更新货架可用容量
        shelf.setAvailableCapacity(shelf.getAvailableCapacity() + quantity);
        shelfRepository.save(shelf);

        // 创建 UnstockTask
        UnstockTask unstockTask = new UnstockTask();
        unstockTask.setProductId(productId);
        unstockTask.setShelfId(shelfId);
        unstockTask.setQuantity(quantity);
        unstockTask.setStatus(UnstockTask.TaskStatus.COMPLETED);
        unstockTask.setCreatedBy(createdBy);
        unstockTask.setAssignedTo(assignedTo);
        unstockTaskRepository.save(unstockTask);

        // Container Logic
        Container container = containerRepository.findById(containerId)
                .orElseThrow(() -> new ResourceNotFoundException("Container not found"));
        if (container.getAvailableCapacity() < quantity) {
            throw new InsufficientCapacityException("Not enough capacity in the container");
        }
        container.setAvailableCapacity(container.getAvailableCapacity() - quantity);
        containerRepository.save(container);

        return updatedProduct;
    }



}