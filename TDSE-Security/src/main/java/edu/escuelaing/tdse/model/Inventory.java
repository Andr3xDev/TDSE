package edu.escuelaing.tdse.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an inventory entity with details about a property.
 * <p>
 * This class is mapped to the "inventory" table in the database.
 * </p>
 *
 * Fields:
 * <ul>
 * <li>id - Unique identifier for the inventory item (auto-generated).</li>
 * <li>address - Address of the property.</li>
 * <li>price - Price of the property.</li>
 * <li>size - Size of the property (e.g., in square meters).</li>
 * <li>description - Additional description of the property.</li>
 * </ul>
 */
@Entity
@Table(name = "inventory")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private Integer price;
    private Integer size;
    private String description;

}