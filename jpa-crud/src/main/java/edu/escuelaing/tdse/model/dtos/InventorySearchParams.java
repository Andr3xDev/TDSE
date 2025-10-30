package edu.escuelaing.tdse.model.dtos;

/**
 * Data transfer object (DTO) for specifying search parameters when querying
 * inventory items.
 *
 * @param name     the name of the inventory item to search for (optional)
 * @param address  the address associated with the inventory item (optional)
 * @param maxPrice the maximum price filter for the inventory item (optional)
 * @param minSize  the minimum size filter for the inventory item (optional)
 */
public record InventorySearchParams(
        String name,
        String address,
        Integer maxPrice,
        Integer minSize) {
}