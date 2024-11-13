package com.jd.controller;

import com.jd.dto.ContainerDTO;
import com.jd.exception.ResourceNotFoundException;
import com.jd.model.Container;
import com.jd.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/containers")
public class ContainerController {

    @Autowired
    private ContainerService containerService;

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Container> addContainer(@RequestBody ContainerDTO containerDto) {
        try {
            Container newContainer = containerService.addContainer(containerDto);
            return ResponseEntity.ok(newContainer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ContainerDTO> updateContainer(@PathVariable Integer id, @RequestBody ContainerDTO containerDto) {
        try {
            ContainerDTO updatedContainer = containerService.updateContainer(id, containerDto);
            return ResponseEntity.ok(updatedContainer);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //@PreAuthorize("hasAnyRole('ADMIN', 'STOCKER', 'UNSTOCKER')")
    @GetMapping("/{id}")
    public ResponseEntity<Container> getContainerById(@PathVariable Integer id) {
        Container container = containerService.getContainerById(id);
        return ResponseEntity.ok(container);
    }

    //@PreAuthorize("hasAnyRole('ADMIN', 'STOCKER', 'UNSTOCKER')")
    @GetMapping
    public ResponseEntity<List<Container>> getAllContainers() {
        List<Container> containers = containerService.getAllContainers();
        return ResponseEntity.ok(containers);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContainer(@PathVariable Integer id) {
        containerService.deleteContainer(id);
        return ResponseEntity.ok().build();
    }
}
