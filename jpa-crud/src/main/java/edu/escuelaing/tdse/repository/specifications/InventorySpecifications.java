package edu.escuelaing.tdse.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import edu.escuelaing.tdse.model.Inventory;
import edu.escuelaing.tdse.model.dtos.InventorySearchParams;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for creating dynamic JPA {@link Specification} objects for the
 * {@link Inventory} entity.
 * This acts as a factory for building search criteria based on variable inputs.
 */
public class InventorySpecifications {

    /**
     * Creates a {@link Specification} for the {@link Inventory} entity based on the
     * filter criteria
     * provided in the {@link InventorySearchParams} DTO.
     * <p>
     * The criteria are combined using a logical AND. If a parameter in the DTO is
     * null or blank,
     * it's ignored and not added to the query's WHERE clause.
     *
     * @param params The DTO containing the optional filter values. Must not be
     *               null.
     * @return A {@link Specification<Inventory>} object that can be executed by a
     *         JpaSpecificationExecutor.
     */
    public static Specification<Inventory> withDynamicQuery(InventorySearchParams params) {
        return (root, query, criteriaBuilder) -> {

            // Listo to save inputs
            List<Predicate> predicates = new ArrayList<>();

            if (params.name() != null && !params.name().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                        "%" + params.name().toLowerCase() + "%"));
            }

            if (params.address() != null && !params.address().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address")),
                        "%" + params.address().toLowerCase() + "%"));
            }

            if (params.maxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), params.maxPrice()));
            }

            if (params.minSize() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("size"), params.minSize()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}