package edu.escuelaing.tdse.service;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.escuelaing.tdse.model.Inventory;
import edu.escuelaing.tdse.model.dtos.InventoryCreateRequest;
import edu.escuelaing.tdse.model.dtos.InventoryResponse;
import edu.escuelaing.tdse.model.dtos.InventorySearchParams;
import edu.escuelaing.tdse.model.dtos.InventoryUpdateRequest;
import edu.escuelaing.tdse.repository.InventoryRepository;
import edu.escuelaing.tdse.repository.specifications.InventorySpecifications;
import lombok.RequiredArgsConstructor;

/**
 * Implements the contract from the interface, define the bussiness logic y
 * validations to use the DB
 */
@Service
@Transactional
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryResponse createItem(InventoryCreateRequest request) {
        Inventory newInventory = new Inventory();
        newInventory.setName(request.name());
        newInventory.setAddress(request.address());
        newInventory.setPrice(request.price());
        newInventory.setSize(request.size());
        newInventory.setDescription(request.description());

        Inventory savedInventory = inventoryRepository.save(newInventory);

        return mapToResponse(savedInventory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InventoryResponse> getAllItems(Pageable pageable) {
        Page<Inventory> inventoryPage = inventoryRepository.findAll(pageable);

        return inventoryPage.map(this::mapToResponse);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryResponse updateItem(Long id, InventoryUpdateRequest request) {
        Inventory existingInventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Inventory item not found with id: " + id));

        if (request.name() != null) {
            existingInventory.setName(request.name());
        }
        if (request.address() != null) {
            existingInventory.setAddress(request.address());
        }
        if (request.price() != null) {
            existingInventory.setPrice(request.price());
        }
        if (request.size() != null) {
            existingInventory.setSize(request.size());
        }
        if (request.description() != null) {
            existingInventory.setDescription(request.description());
        }

        Inventory updatedInventory = inventoryRepository.save(existingInventory);

        return mapToResponse(updatedInventory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteItem(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new NoSuchElementException("Inventory item not found with id: " + id);
        }

        inventoryRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InventoryResponse> getFilteredItems(InventorySearchParams params, Pageable pageable) {
        Specification<Inventory> spec = InventorySpecifications.withDynamicQuery(params);

        Page<Inventory> results = inventoryRepository.findAll(spec, pageable);

        return results.map(this::mapToResponse);
    }

    private InventoryResponse mapToResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.getId(),
                inventory.getName(),
                inventory.getAddress(),
                inventory.getPrice(),
                inventory.getSize(),
                inventory.getDescription());
    }

}