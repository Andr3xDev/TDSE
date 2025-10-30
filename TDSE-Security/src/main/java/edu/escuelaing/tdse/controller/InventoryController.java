package edu.escuelaing.tdse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import edu.escuelaing.tdse.model.dtos.InventoryCreateRequest;
import edu.escuelaing.tdse.model.dtos.InventoryResponse;
import edu.escuelaing.tdse.model.dtos.InventorySearchParams;
import edu.escuelaing.tdse.model.dtos.InventoryUpdateRequest;
import edu.escuelaing.tdse.service.InventoryServiceImpl;

/**
 * REST Controller for handling all HTTP requests related to the Inventory
 * resource.
 * All endpoints are mapped under the base path "/api/inventory".
 */
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryServiceImpl inventoryService;

    /**
     * Handles POST requests to create a new inventory item.
     *
     * @param request The request body containing the data for the new item.
     * @return A {@link ResponseEntity} with the created {@link InventoryResponse}
     *         and a 201 Created status.
     */
    @PostMapping
    public ResponseEntity<InventoryResponse> createItem(@RequestBody InventoryCreateRequest request) {
        InventoryResponse createdItem = inventoryService.createItem(request);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    /**
     * Handles GET requests to retrieve a paginated list of all inventory items.
     * Pagination parameters can be provided via query params
     *
     * @param pageable An object containing pagination information.
     * @return A {@link ResponseEntity} with a {@link Page} of
     *         {@link InventoryResponse} and a 200 OK status.
     */
    @GetMapping
    public ResponseEntity<Page<InventoryResponse>> getAllItems(Pageable pageable) {
        Page<InventoryResponse> items = inventoryService.getAllItems(pageable);
        return ResponseEntity.ok(items);
    }

    /**
     * Handles GET requests to search for inventory items based on dynamic filter
     * criteria.
     * Filter and pagination params are provided via query params
     *
     * @param params   An object containing the optional search filters.
     * @param pageable An object containing pagination information.
     * @return A {@link ResponseEntity} with a {@link Page} of matching
     *         {@link InventoryResponse} and a 200 OK status.
     */
    @GetMapping("/search")
    public ResponseEntity<Page<InventoryResponse>> searchItems(InventorySearchParams params, Pageable pageable) {
        Page<InventoryResponse> results = inventoryService.getFilteredItems(params,
                pageable);
        return ResponseEntity.ok(results);
    }

    /**
     * Handles PUT requests to update an existing inventory item by its ID.
     *
     * @param id      The ID of the item to update, extracted from the path.
     * @param request The request body containing the updated data for the item.
     * @return A {@link ResponseEntity} with the updated {@link InventoryResponse}
     *         and a 200 OK status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventoryResponse> updateItem(
            @PathVariable Long id,
            @RequestBody InventoryUpdateRequest request) {
        InventoryResponse updatedItem = inventoryService.updateItem(id, request);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * Handles DELETE requests to remove an inventory item by its ID.
     *
     * @param id The ID of the item to delete, extracted from the path.
     * @return An empty {@link ResponseEntity} with a 204 No Content status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        inventoryService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

}