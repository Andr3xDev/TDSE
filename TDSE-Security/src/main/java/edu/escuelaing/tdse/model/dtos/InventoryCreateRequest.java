package edu.escuelaing.tdse.model.dtos;

/**
 * Data transfer object (DTO) for creating a new inventory item.
 * <p>
 * Encapsulates the necessary information required to create an inventory entry.
 *
 * @param name        the name of the inventory item
 * @param address     the address or location of the inventory item
 * @param price       the price of the inventory item
 * @param size        the size or quantity of the inventory item
 * @param description a brief description of the inventory item
 */
public record InventoryCreateRequest(
        String name,
        String address,
        Integer price,
        Integer size,
        String description) {
}