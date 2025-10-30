package edu.escuelaing.tdse.model.dtos;

/**
 * DTO for updating an existing inventory item.
 * All fields are optional to allow for partial updates (PATCH).
 */
public record InventoryUpdateRequest(
        String name,
        String address,
        Integer price,
        Integer size,
        String description) {
}