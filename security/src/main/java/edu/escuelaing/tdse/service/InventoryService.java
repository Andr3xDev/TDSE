package edu.escuelaing.tdse.service;

import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import edu.escuelaing.tdse.model.dtos.InventoryCreateRequest;
import edu.escuelaing.tdse.model.dtos.InventoryResponse;
import edu.escuelaing.tdse.model.dtos.InventorySearchParams;
import edu.escuelaing.tdse.model.dtos.InventoryUpdateRequest;

/**
 * Defines the contract for business logic operations related to inventory
 * management.
 * This interface abstracts CRUD and search operations, allowing for a decoupled
 * implementation.
 */
public interface InventoryService {

    /**
     * Creates a new item in the inventory from the provided data.
     *
     * @param request The DTO containing the information for the new item. Must not
     *                be null.
     * @return An {@link InventoryResponse} DTO representing the newly created item,
     *         including its generated ID.
     */
    InventoryResponse createItem(InventoryCreateRequest request);

    /**
     * Retrieves a paginated list of all inventory items.
     *
     * @param pageable An object containing pagination information (page number,
     *                 size, sort order).
     * @return A {@link Page} of {@link InventoryResponse} containing the items for
     *         the requested page and pagination metadata.
     */
    Page<InventoryResponse> getAllItems(Pageable pageable);

    /**
     * Searches and filters inventory items based on a set of optional criteria.
     *
     * @param params   The {@link InventorySearchParams} DTO containing the filter
     *                 parameters. Can be empty.
     * @param pageable An object containing pagination information.
     * @return A {@link Page} of {@link InventoryResponse} with the items that match
     *         the search criteria.
     */
    Page<InventoryResponse> getFilteredItems(InventorySearchParams params, Pageable pageable);

    /**
     * Updates an existing inventory item identified by its ID.
     *
     * @param id      The ID of the item to update.
     * @param request The DTO containing the new data for the item.
     * @return An {@link InventoryResponse} DTO representing the state of the item
     *         after the update.
     * @throws NoSuchElementException if no item is found with the provided ID.
     */
    InventoryResponse updateItem(Long id, InventoryUpdateRequest request);

    /**
     * Permanently deletes an inventory item identified by its ID.
     *
     * @param id The ID of the item to delete.
     * @throws NoSuchElementException if no item is found with the provided ID.
     */
    void deleteItem(Long id);

}