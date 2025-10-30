package edu.escuelaing.tdse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import edu.escuelaing.tdse.model.Inventory;

/**
 * Repository interface for managing {@link Inventory} entities.
 * <p>
 * Extends {@link JpaRepository} to provide standard CRUD operations and
 * query methods for the Inventory entity.
 * 
 * Extends {@link JpaSpecificationExecutor} to allow queries with parameters in
 * real time
 * </p>
 * <p>
 * This interface is a Spring Data JPA repository, and Spring will automatically
 * provide the implementation at runtime.
 * </p>
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>, JpaSpecificationExecutor<Inventory> {

}