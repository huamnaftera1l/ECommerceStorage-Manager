package com.jd.controller;

import com.jd.dto.StockRequest;
import com.jd.dto.UnstockRequest;
import com.jd.model.Product;
import com.jd.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private InventoryService inventoryService;

    //@PreAuthorize("hasRole('STOCKER')")
    @PostMapping("/stock")
    public ResponseEntity<?> stockProduct(@RequestBody StockRequest request) {
        //System.out.println("123\n");
        Product updatedProduct = inventoryService.stockProduct(
                request.getProductId(),
                request.getShelfId(),
                request.getQuantity(),
                request.getCreatedBy(),
                request.getAssignedTo()
        );
        logger.info("有货物上架");
        return ResponseEntity.ok(updatedProduct);
    }

    @PostMapping("/unstock")
    public ResponseEntity<?> unstockProduct(@RequestBody UnstockRequest request, HttpServletRequest httpRequest) {
        // 从请求属性中获取角色信息
        List<String> roles = (List<String>) httpRequest.getAttribute("roles");

        // 检查用户是否有 UNSTOCKER 或 ADMIN 角色
        if (roles != null && (roles.contains("UNSTOCKER") || roles.contains("ADMIN"))) {
            try {
                Product updatedProduct = inventoryService.unstockProduct(
                        request.getProductId(),
                        request.getShelfId(),
                        request.getContainerId(),
                        request.getQuantity(),
                        request.getCreatedBy(),
                        request.getAssignedTo()
                );
                logger.info("Product unstocked successfully");
                return ResponseEntity.ok(updatedProduct);
            } catch (Exception e) {
                logger.error("Error unstocking product: ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error unstocking product: " + e.getMessage());
            }
        } else {
            logger.warn("Unauthorized access attempt to unstock product");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to perform this action");
        }
    }
}
