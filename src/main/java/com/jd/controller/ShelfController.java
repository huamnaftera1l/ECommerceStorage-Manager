package com.jd.controller;

import com.jd.dto.ShelfDTO;
import com.jd.model.Shelf;
import com.jd.service.ShelfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shelves")
public class ShelfController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private ShelfService shelfService;

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Shelf> addShelf(@RequestBody ShelfDTO shelfDto) {
        logger.info("添加了一个货架");
        Shelf newShelf = shelfService.addShelf(shelfDto);
        return ResponseEntity.ok(newShelf);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Shelf> updateShelf(@PathVariable int id, @RequestBody ShelfDTO shelfDto) {
        logger.info(id+"货架进行更新");
        Shelf updatedShelf = shelfService.updateShelf(id, shelfDto);
        return ResponseEntity.ok(updatedShelf);
    }

    //@PreAuthorize("hasAnyRole('ADMIN', 'STOCKER', 'UNSTOCKER')")
    @GetMapping
    public ResponseEntity<List<Shelf>> getAllShelves() {
        logger.info("查看所有货架");
        List<Shelf> shelves = shelfService.getAllShelves();
        return ResponseEntity.ok(shelves);
    }
}