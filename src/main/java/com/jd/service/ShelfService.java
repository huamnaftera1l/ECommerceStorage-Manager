package com.jd.service;

import com.jd.dto.ShelfDTO;
import com.jd.exception.InsufficientCapacityException;
import com.jd.exception.InsufficientStockException;
import com.jd.exception.ResourceNotFoundException;
import com.jd.exception.ShelfNotEmptyException;
import com.jd.model.Shelf;
import com.jd.repository.ShelfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShelfService {

    @Autowired
    private ShelfRepository shelfRepository;

    //货架添加
    @Transactional
    public Shelf addShelf(ShelfDTO shelfDto) {
        Shelf shelf = new Shelf();
        shelf.setName(shelfDto.getName());
        shelf.setCapacity(shelfDto.getCapacity());
        shelf.setAvailableCapacity(shelfDto.getCapacity()); // 初始可用容量等于总容量
        return shelfRepository.save(shelf);
    }

    //货架信息更新
    @Transactional
    public Shelf updateShelf(int id, ShelfDTO shelfDto) {
        Shelf shelf = shelfRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shelf not found with id: " + id));

        shelf.setName(shelfDto.getName());

        int usedCapacity = shelf.getCapacity() - shelf.getAvailableCapacity();
        if (shelfDto.getCapacity() < usedCapacity) {
            throw new InsufficientCapacityException("New capacity is too small for current stock");
        }

        shelf.setCapacity(shelfDto.getCapacity());
        shelf.setAvailableCapacity(shelfDto.getCapacity() - usedCapacity);

        return shelfRepository.save(shelf);
    }

   //所有的货架
    public List<Shelf> getAllShelves() {
        return shelfRepository.findAll();
    }

    //通过ID找货架
    public Shelf getShelfById(int id) {
        return shelfRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shelf not found with id: " + id));
    }

    //删除货架
    @Transactional
    public void deleteShelf(int id) {
        Shelf shelf = shelfRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shelf not found with id: " + id));

        if (shelf.getCapacity() != shelf.getAvailableCapacity()) {
            throw new ShelfNotEmptyException("Cannot delete shelf that still contains products");
        }

        shelfRepository.delete(shelf);
    }

    //添加货架上商品的数量
    @Transactional
    public void addProductToShelf(int shelfId, int quantity) {
        Shelf shelf = shelfRepository.findById(shelfId)
                .orElseThrow(() -> new ResourceNotFoundException("Shelf not found with id: " + shelfId));

        if (shelf.getAvailableCapacity() < quantity) {
            throw new InsufficientCapacityException("Not enough capacity on the shelf");
        }

        shelf.setAvailableCapacity(shelf.getAvailableCapacity() - quantity);
        shelfRepository.save(shelf);
    }

    //减少货架上的商品数量

    @Transactional
    public void removeProductFromShelf(int shelfId, int quantity) {
        Shelf shelf = shelfRepository.findById(shelfId)
                .orElseThrow(() -> new ResourceNotFoundException("Shelf not found with id: " + shelfId));

        int currentUsedCapacity = shelf.getCapacity() - shelf.getAvailableCapacity();
        if (currentUsedCapacity < quantity) {
            throw new InsufficientStockException("Not enough products on the shelf to remove");
        }

        shelf.setAvailableCapacity(shelf.getAvailableCapacity() + quantity);
        shelfRepository.save(shelf);
    }
}