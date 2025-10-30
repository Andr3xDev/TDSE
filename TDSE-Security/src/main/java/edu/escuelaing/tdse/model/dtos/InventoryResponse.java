package edu.escuelaing.tdse.model.dtos;

/**
 * Data Transfer Object (DTO) representing the response for inventory
 * information.
 *
 * @param id          the unique identifier of the inventory item
 * @param name        the name of the inventory item
 * @param address     the address or location associated with the inventory item
 * @param price       the price of the inventory item
 * @param size        the size or quantity of the inventory item
 * @param description a brief description of the inventory item
 */
public record InventoryResponse(
        Long id,
        String name,
        String address,
        Integer price,
        Integer size,
        String description) {
}